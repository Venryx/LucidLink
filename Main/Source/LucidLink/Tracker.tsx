import {Log, ToJSON} from "../Frame/Globals";
import {Script} from "./Scripts/Script";
import {Session} from "./Tracker/Session";
import ScriptContext from "./Scripts/ScriptContext";
import {BaseComponent} from "../Frame/ReactGlobals";
import Moment from "moment";

import GraphUI from "./Tracker/Graph/GraphUI";
import ListUI from "./Tracker/ListUI";
import DisplayersUI from "./Tracker/DisplayersUI";
import ScrollableTabView from "react-native-scrollable-tab-view";

import scriptDefaultText_BuiltInDisplayers from "./Tracker/UserScriptDefaults/BuiltInDisplayers";
import scriptDefaultText_CustomDisplayers from "./Tracker/UserScriptDefaults/CustomDisplayers";
import {transaction, computed} from "mobx";
import Node from "../Packages/VTree/Node";
import {LL} from "../LucidLink";
import DisplayerScriptRunner from "./Tracker/DisplayerScriptRunner";
import {_VDFPreSerialize, P} from "../Packages/VDF/VDFTypeInfo";
import {Assert, AssertWarn} from "../Frame/General/Assert";
import {SleepStage} from "../Frame/SPBridge";
import SPBridge from "../Frame/SPBridge";
import {SleepSegment} from "./Tracker/Session/SleepSession";
import SleepSession from "./Tracker/Session/SleepSession";

SPBridge.listeners_onReceiveSleepStage.push((rawStage: SleepStage)=> {
	if (LL.tracker.currentSession.CurrentSleepSession == null) return;

	var currentSegment = LL.tracker.currentSession.CurrentSleepSegment;
	if (currentSegment == null || rawStage != currentSegment.stage) {
		var session = LL.tracker.currentSession.CurrentSleepSession;
		var sleepSegment = new SleepSegment(session, rawStage);
		session.segments.push(sleepSegment);
	}
});

export class Tracker extends Node {
	@_VDFPreSerialize() PreSerialize() {
	    if (this.selectedDisplayerScript)
	        this.selectedDisplayerScriptName = this.selectedDisplayerScript.Name;
	}
	/*@_VDFSerializeProp() SerializeProp(path, options) {
	    if (path.currentNode.prop.name == "selectedDisplayerScript" && this.selectedDisplayerScript)
	        return new VDFNode(this.selectedDisplayerScript.Name);
	}*/

	@O displayerScripts = [] as Script[];
	@computed get DisplayerScriptFilesOutdated() {
		return this.displayerScripts.Any(a=>a.fileOutdated);
	}
	@O selectedDisplayerScript: Script;
	@P() selectedDisplayerScriptName: string; // used only during save-to/load-from disk 
	scriptRunner = new DisplayerScriptRunner();

	LoadFileSystemData(onDone = null) {
		this.LoadDisplayerScripts(onDone);
	}
	async LoadDisplayerScripts(onDone = null) {
		var scriptsFolder = LL.RootFolder.GetFolder("Displayer scripts");
		var scriptsFolderExists = await scriptsFolder.Exists();
		if (!scriptsFolderExists)
			scriptsFolder.Create();
		// ensure these scripts always exist
		if (!await scriptsFolder.GetFile("Built-in displayers.js").Exists()) {
			await scriptsFolder.GetFile("Built-in displayers.js").WriteAllText(scriptDefaultText_BuiltInDisplayers);
			await scriptsFolder.GetFile("Built-in displayers.meta").WriteAllText(ToJSON({index: 1, editable: false, enabled: true}));
		}
		// only create these scripts once; if user deletes them, that's fine
		if (!scriptsFolderExists) {
			await scriptsFolder.GetFile("Custom displayers.js").WriteAllText(scriptDefaultText_CustomDisplayers);
			await scriptsFolder.GetFile("Custom displayers.meta").WriteAllText(ToJSON({index: 2, editable: true, enabled: true}));
		}
		
		var scriptFiles = (await scriptsFolder.GetFiles()).Where(a=>a.Extension == "js");
		this.displayerScripts = [];
		for (let scriptFile of scriptFiles) {
			let script = await Script.Load(scriptFile);
			this.displayerScripts.push(script);
		}

		this.selectedDisplayerScript = this.displayerScripts.First(a=>a.Name == this.selectedDisplayerScriptName);

		//if (LL.settings.applyScriptsOnLaunch)
		this.ApplyDisplayerScripts();
		
		Log("Finished loading displayer scripts.");

		if (onDone) onDone();
	}

	SaveFileSystemData() {
		//this.SaveScripts();
		this.SaveScriptMetas();
		this.SaveCurrentSession();
	}
	async SaveScriptMetas() {
		var {displayerScripts} = this;
		for (let script of displayerScripts)
			await script.SaveMeta();
		Log("Finished saving displayer script metas.");
	}
	async SaveCurrentSession() {
		await this.currentSession.Save();
		Log("Finished saving current session.");
	}

	ApplyDisplayerScripts() {
		this.scriptRunner.Reset();
		var scripts_ordered = this.displayerScripts.Where(a=>a.enabled).OrderBy(a=> {
			AssertWarn(a.index != -1000, `Script-order not found in meta file for: ${a.file.Name}`);
			return a.index;
		});
		this.scriptRunner.Apply(scripts_ordered);
	}

	@O loadedSessions: Session[] = [];
	async LoadSessionsForRange(start, endOut) {
		var sessionsFolder = LL.RootFolder.GetFolder("Sessions");
		//await sessionsFolder.Create();
		
		var sessionFolders = await sessionsFolder.GetFolders();
		sessionFolders = sessionFolders.Where(a=> {
			var startTime = Moment(a.Name);
			var isInMonth = startTime >= start && startTime < endOut;
			return isInMonth;
		});

		var justLoadedSessions = [];
		for (let folder of sessionFolders) {
			let alreadyLoaded = this.loadedSessions.Any(a=>a.folder.Path == folder.Path);
			if (alreadyLoaded) continue;
			let session = await Session.Load(folder);
			justLoadedSessions.push(session);
		}
		if (!justLoadedSessions.length) return;
		
		transaction(()=> {
			for (let session of justLoadedSessions) {
				// check again for duplicate, cause one might have been added during one of the awaits in the loop above
				let alreadyLoaded = this.loadedSessions.Any(a=>a.folder.Path == session.folder.Path);
				if (alreadyLoaded) continue;
				this.loadedSessions.push(session);
			}
			// make sure ordered by date (otherwise current-session is ordered wrong)
			this.loadedSessions = this.loadedSessions.OrderBy(a=>a.date);
		});
	}
	GetLoadedSessionsForRange(start, endOut) {
		return this.loadedSessions.Where(a=> {
			return a.date >= start && a.date < endOut;
		});
	}

	GetSleepSessionsForRange(start, endOut) {
		return this.loadedSessions.SelectMany(session=> {
			return session.sleepSessions.Where(a=> {
				//return a.endTime >= start && a.startTime < endOut;
				return a.endTime > start && a.startTime < endOut;
			});
		});
	}
	GetEventsForRange(start, endOut) {
		return this.loadedSessions.SelectMany(session=> {
			return session.events.Where(a=> {
				return a.date >= start && a.date < endOut;
			})
		});
	}
	
	currentSession: Session = null;
	async SetUpCurrentSession() {
		var session = new Session(Moment());
		await session.Save();
		this.loadedSessions.push(session);
		this.currentSession = session;
	}

	@O @P() rowCount = 3;

	@O openSleepSession: SleepSession;
}
g.Extend({Tracker});

export class TrackerUI extends BaseComponent<any, any> {
	render() {
		var node = LL.tracker;
		return (
			<ScrollableTabView style={{flex: 1}}>
				<GraphUI tabLabel="Graph"/>
				<ListUI tabLabel="List"/>
				<DisplayersUI tabLabel="Displayers"/>
			</ScrollableTabView>
		);
	}
}
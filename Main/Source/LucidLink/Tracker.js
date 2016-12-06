import Moment from "moment";

import GraphUI from "./Tracker/GraphUI";
import ListUI from "./Tracker/ListUI";
import DisplayersUI from "./Tracker/DisplayersUI";
import ScrollableTabView from "react-native-scrollable-tab-view";

import scriptDefaultText_BuiltInDisplayers from "./Tracker/UserScriptDefaults/BuiltInDisplayers";
import scriptDefaultText_CustomDisplayers from "./Tracker/UserScriptDefaults/CustomDisplayers";

g.Tracker = class Tracker extends Node {
	@_VDFSerializeProp() SerializeProp(path, options) {
	    if (path.currentNode.prop.name == "selectedDisplayerScript" && this.selectedDisplayerScript)
	        return new VDFNode(this.selectedDisplayerScript.name);
	}

	@O displayerScripts = [];
	@O displayerScriptFilesOutdated = false;
	@O @P() selectedDisplayerScript = null; // holds the actual script, but only the name is serialized
	scriptRunner = new ScriptRunner();

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

		var newSelectedScriptName = this.selectedScript; // was serialized as a name (if at all)
		var newSelectedScript = this.displayerScripts.First(a=>a.file.NameWithoutExtension == newSelectedScriptName);
		if (newSelectedScript)
			this.selectedDisplayerScript = newSelectedScript;

		//if (LL.settings.applyScriptsOnLaunch)
		this.ApplyDisplayerScripts();
		
		Log("Finished loading displayer scripts.");

		onDone && onDone();
	}

	SaveFileSystemData() {
		//this.SaveScripts();
		this.SaveScriptMetas();
	}
	async SaveScriptMetas() {
		var {displayerScripts} = this;
		for (let script of displayerScripts)
			script.SaveMeta();
		Log("Finished saving displayer script metas.");
	}

	ApplyDisplayerScripts() {
		this.scriptRunner.Reset();
		var scripts_ordered = this.displayerScripts.Where(a=>a.enabled).OrderBy(a=> {
			AssertWarn(a.index != -1000, `Script-order not found in meta file for: ${a.file.Name}`);
			return a.index;
		});
		this.scriptRunner.Init(scripts_ordered);
		if (this.ui)
			this.ui.setState({scriptLastRunsOutdated: false});
	}

	@O loadedSessions = [];
	async LoadSessionsForMonth(month) {
		var sessionsFolder = LL.RootFolder.GetFolder("Sessions");
		//await sessionsFolder.Create();
		
		var sessionFolders = await sessionsFolder.GetFolders();
		sessionFolders = sessionFolders.Where(a=> {
			var startTime = Moment(a.Name);
			var isInMonth = startTime >= month && startTime < month.AddingMonths(1);
			return isInMonth;
		});

		var justLoadedSessions = [];
		for (let folder of sessionFolders) {
			let alreadyLoaded = this.loadedSessions.Any(a=>a.folder.Path == folder.Path);
			if (!alreadyLoaded) {
				let session = await Session.Load(folder);
				justLoadedSessions.push(session);
			}
		}
		Transaction(()=> {
			this.loadedSessions.AddRange(justLoadedSessions);
			// make sure ordered by date (otherwise current-session is ordered wrong)
			this.loadedSessions = this.loadedSessions.OrderBy(a=>a.date);
		});
	}
	GetLoadedSessionsForMonth(month) {
		return this.loadedSessions.Where(a=> {
			return a.date >= month && a.date < month.AddingMonths(1);
		});
	}
	
	currentSession = null;
	async SetUpCurrentSession() {
		var session = new Session(Moment());
		await session.Save();
		this.loadedSessions.push(session);
		this.currentSession = session;
	}
}

export class TrackerUI extends BaseComponent {
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
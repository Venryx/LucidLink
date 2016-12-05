import GraphUI from "./Tracker/GraphUI";
import ListUI from "./Tracker/ListUI";
import DisplayersUI from "./Tracker/DisplayersUI";

import scriptDefaultText_BuiltInDisplayers from "./Scripts/UserScriptDefaults/BuiltInDisplayers";
import scriptDefaultText_CustomDisplayers from "./Scripts/UserScriptDefaults/CustomDisplayers";

g.Tracker = class Tracker extends Node {
	@_VDFSerializeProp() SerializeProp(path, options) {
	    if (path.currentNode.prop.name == "selectedDisplayerScript" && this.selectedDisplayerScript)
	        return new VDFNode(this.selectedDisplayerScript.name);
	}

	displayerScripts = [];
	@P() selectedDisplayerScript = null; // holds the actual script, but only the name is serialized
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
		
		Log("Finished loading scripts.");

		onDone && onDone();
	}

	SaveFileSystemData() {
		//this.SaveScripts();
		this.SaveScriptMetas();
	}

	ApplyDisplayerScripts() {
		this.scriptRunner.Reset();
		var scripts_ordered = this.scripts.Where(a=>a.enabled).OrderBy(a=> {
			AssertWarn(a.index != -1000, `Script-order not found in meta file for: ${a.file.Name}`);
			return a.index;
		});
		this.scriptRunner.Init(scripts_ordered);
		if (this.ui)
			this.ui.setState({scriptLastRunsOutdated: false});
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
import RNFS from "react-native-fs";
var ScrollableTabView = require("react-native-scrollable-tab-view");
var DialogAndroid = require("react-native-dialogs");
import Drawer from "react-native-drawer";

import ScriptRunner from "./Scripts/ScriptRunner";

import scriptDefaultText_CoreFunctions from "./Scripts/UserScriptDefaults/CoreFunctions";
import scriptDefaultText_BuiltInHelpers from "./Scripts/UserScriptDefaults/BuiltInHelpers";
import scriptDefaultText_BuiltInScript from "./Scripts/UserScriptDefaults/BuiltInScript";
import scriptDefaultText_FakeDataProvider from "./Scripts/UserScriptDefaults/FakeDataProvider";
import scriptDefaultText_CustomHelpers from "./Scripts/UserScriptDefaults/CustomHelpers";
import scriptDefaultText_CustomScript from "./Scripts/UserScriptDefaults/CustomScript";

g.Script = class Script {
	static async Load(file) {
		let scriptText = await file.ReadAllText();
		var result = new Script(file, scriptText);
		await result.LoadMeta();
		return result;
	}
	async LoadMeta() {
		if (!await this.MetaFile.Exists()) return;
		let scriptMetaJSON = await this.MetaFile.ReadAllText();
		try {
			var scriptMeta = FromJSON(scriptMetaJSON);
			this.index = scriptMeta.index;
			this.editable = scriptMeta.editable;
			this.enabled = scriptMeta.enabled;
		} catch (ex) {
			Log(`Meta file for script "${this.Name}" invalid.
Meta file JSON: ${scriptMetaJSON}
Error: ${ex.stack}`);
		}
	}

	async Save() {
		await this.file.WriteAllText(this.text);
		await this.SaveMeta();
		this.fileOutdated = false;
	}
	async SaveMeta() {
		var scriptMeta = {index: this.index, editable: this.editable, enabled: this.enabled};
		var scriptMetaJSON = ToJSON(scriptMeta);
		await this.MetaFile.WriteAllText(scriptMetaJSON);
	}

	Delete() {
		var dialog = new DialogAndroid();
		dialog.set({
			"title": `Delete script "${this.file.NameWithoutExtension}"`,
			"content": `Permanently delete script?`,
			"positiveText": "OK",
			"negativeText": "Cancel",
			"onPositive": ()=> {
				LL.scripts.scripts.Remove(this);
				if (LL.scripts.selectedScript == this)
					LL.scripts.selectedScript = null;
				this.file.Delete();
				this.MetaFile.Delete();

				if (LL.scripts.ui)
					LL.scripts.ui.forceUpdate();
			},
		});
		dialog.show();
	}
	Rename() {
		var dialog = new DialogAndroid();
		dialog.set({
			"title": `Rename script "${this.file.NameWithoutExtension}"`,
			"input": {
				prefill: this.file.NameWithoutExtension,
				callback: newName=> {
					this.file.Delete();
					this.file = this.file.Folder.GetFile(newName + ".js");
					this.Save();
					if (LL.scripts.ui)
						LL.scripts.ui.forceUpdate();
				}
			},
			"positiveText": "OK",
			"negativeText": "Cancel"
		});
		dialog.show();
	}

	constructor(file, text) {
		this.file = file;
		this.text = text;
	}

	file = null;
	fileOutdated = false;
	get Name() { return this.file.NameWithoutExtension; }
	get MetaFile() { return this.file.Folder.GetFile(this.file.NameWithoutExtension + ".meta"); }

	text = null;

	// stored in meta file
	index = -1;
	editable = true;
	enabled = true;
}

g.Scripts = class Scripts extends Node {
	@_VDFSerializeProp() SerializeProp(path, options) {
	    if (path.currentNode.prop.name == "selectedScript" && this.selectedScript)
	        return new VDFNode(this.selectedScript.name);
	}

	ui = null;

	scripts = [];
	@P() selectedScript = null; // holds the actual script, but only the name is serialized
	scriptRunner = new ScriptRunner();

	LoadFileSystemData() {
		this.LoadScripts();
	}
	async LoadScripts() {
		var scripts = [];
		var scriptsFolder = LL.RootFolder.GetFolder("Scripts");
		var scriptsFolderExists = await scriptsFolder.Exists();
		if (!scriptsFolderExists)
			scriptsFolder.Create();
		// ensure these scripts always exist
		if (!await scriptsFolder.GetFile("Core functions.js").Exists()) {
			await scriptsFolder.GetFile("Core functions.js").WriteAllText(scriptDefaultText_CoreFunctions);
			await scriptsFolder.GetFile("Core functions.meta").WriteAllText(ToJSON({index: 0, editable: false, enabled: true}));
		}
		if (!await scriptsFolder.GetFile("Built-in helpers.js").Exists()) {
			await scriptsFolder.GetFile("Built-in helpers.js").WriteAllText(scriptDefaultText_BuiltInHelpers);
			await scriptsFolder.GetFile("Built-in helpers.meta").WriteAllText(ToJSON({index: 1, editable: false, enabled: true}));
		}
		// only create these scripts once; if user deletes them, that's fine
		if (!scriptsFolderExists) {
			await scriptsFolder.GetFile("Built-in script.js").WriteAllText(scriptDefaultText_BuiltInScript);
			await scriptsFolder.GetFile("Built-in script.meta").WriteAllText(ToJSON({index: 2, editable: true, enabled: true}));
			await scriptsFolder.GetFile("Fake-data provider.js").WriteAllText(scriptDefaultText_FakeDataProvider);
			await scriptsFolder.GetFile("Fake-data provider.meta").WriteAllText(ToJSON({index: 3, editable: true, enabled: false}));
			await scriptsFolder.GetFile("Custom helpers.js").WriteAllText(scriptDefaultText_CustomHelpers);
			await scriptsFolder.GetFile("Custom helpers.meta").WriteAllText(ToJSON({index: 4, editable: true, enabled: true}));
			await scriptsFolder.GetFile("Custom script.js").WriteAllText(scriptDefaultText_CustomScript);
			await scriptsFolder.GetFile("Custom script.meta").WriteAllText(ToJSON({index: 5, editable: true, enabled: true}));
		}
		
		var scriptFiles = (await scriptsFolder.GetFiles()).Where(a=>a.Extension == "js");
		this.scripts = [];
		for (let scriptFile of scriptFiles) {
			let script = await Script.Load(scriptFile);
			this.scripts.push(script);
		}

		var newSelectedScriptName = this.selectedScript; // was serialized as a name (if at all)
		var newSelectedScript = this.scripts.First(a=>a.file.NameWithoutExtension == newSelectedScriptName);
		if (newSelectedScript)
			this.selectedScript = newSelectedScript;

		if (LL.settings.applyScriptsOnLaunch)
			this.ApplyScripts();
		
		if (this.ui)
			this.ui.forceUpdate();
		Log("Finished loading scripts.");
	}

	SaveFileSystemData() {
		//this.SaveScripts();
		this.SaveScriptMetas();
	}
	/*async SaveScripts() {
		var {scripts} = this;
		for (let script of scripts) {
			script.Save();
		}
		
		if (this.ui)
			this.ui.forceUpdate();
		Log("Finished saving scripts.");
	}*/
	async SaveScriptMetas() {
		var {scripts} = this;
		for (let script of scripts)
			script.SaveMeta();
		Log("Finished saving script metas.");
	}

	ResetScript(scriptName) {
		var dialog = new DialogAndroid();
		dialog.set({
			"title": `Reset script "${scriptName}"`,
			"content": `Reset script to its factory state?

This will permanently remove all custom code from the script.`,
			"positiveText": "OK",
			"negativeText": "Cancel",
			"onPositive": ()=> {
				var scriptFileName = scriptName + ".js";
				var script = this.scripts.First(a=>a.file.Name == scriptFileName);
				if (script == null) {
					var file = LL.RootFolder.GetFolder("Scripts").GetFile(scriptFileName);
					var script = new Script(file, `Log("Hello world!");`);
					script.index = LL.scripts.scripts.length;
					LL.scripts.scripts.push(script);
				}

				var nameToTextMap = {
					"Built-in script": scriptDefaultText_BuiltInScript,
					"Fake-data provider": scriptDefaultText_FakeDataProvider,
					"Custom script": scriptDefaultText_CustomScript,
				};
				Assert(nameToTextMap[scriptName]);
				script.text = nameToTextMap[scriptName];

				script.Save();
				if (this.ui)
					this.ui.forceUpdate();
			},
		});
		dialog.show();
	}
	
	ApplyScripts() {
		this.scriptRunner.Reset();
		var scripts_ordered = this.scripts.Where(a=>a.enabled).OrderBy(a=> {
			AssertWarn(a.index != -1, `Script not found in script-order list: ${a.file.Name}`);
			return a.index;
		});
		this.scriptRunner.Init(scripts_ordered);
		if (this.ui)
			this.ui.setState({scriptLastRunsOutdated: false});
	}
}

import ScriptsPanel from "./Scripts/ScriptsPanel";

export class ScriptsUI extends BaseComponent {
	constructor(props) {
		super(props);
		this.state = {scriptLastRunsOutdated: false};
		LL.scripts.ui = this;
	}

	scriptsPanel = null;
	@Bind ToggleScriptsPanelOpen() {
		if (this._drawer._open)
			this._drawer.close();
		else
			this._drawer.open();
	}

	SelectScript(script) {
		LL.scripts.selectedScript = script;
		this.forceUpdate();
		this._drawer.close();
	}

	render() {
		var node = LL.scripts;
		var {selectedScript} = node;
		var {scriptLastRunsOutdated} = this.state;
		
		const drawerStyles = {
			drawer: {shadowColor: "#000000", shadowOpacity: .8, shadowRadius: 3},
			main: {paddingLeft: 3},
		};

		return (
			<Drawer ref={comp=>this._drawer = comp}
					content={<ScriptsPanel parent={this} scripts={node.scripts}/>}
					type="overlay" openDrawerOffset={0.5} panCloseMask={0.5} tapToClose={true}
					closedDrawerOffset={-3} styles={drawerStyles}>
				<Panel style={{flex: 1, flexDirection: "column", backgroundColor: colors.background}}>
					<Panel style={[styles.header, {flexDirection: "row", flexWrap: "wrap", padding: 3, paddingBottom: -5}]}>
						<VButton text="Scripts" style={{width: 100}} onPress={this.ToggleScriptsPanelOpen}/>
						<Text style={[styles.text, {marginLeft: 10, marginTop: 8, fontSize: 18}]}>
						Script: {selectedScript ? selectedScript.file.NameWithoutExtension : "n/a"}
						{selectedScript && !selectedScript.editable ? " (read only)" : ""}
						</Text>
						{selectedScript && selectedScript.editable &&
							<VButton text="Rename" style={{marginLeft: 10, width: 100}} onPress={()=>selectedScript.Rename()}/>}
						<Panel style={{flex: 1}}/>
						<Panel style={{flexDirection: "row", alignItems: "flex-end"}}>
							<VButton color="#777" text="Save" enabled={selectedScript != null && selectedScript.fileOutdated}
								style={{width: 100, marginLeft: 5}}
								onPress={()=>selectedScript.Save().then(()=>this.forceUpdate())}/>
							<VButton color="#777" text="Apply all"
								//enabled={scriptLastRunsOutdated}
								enabled={true}
								style={{width: 100, marginLeft: 5}}
								onPress={()=>node.ApplyScripts()}/>
						</Panel>
					</Panel>
					<Panel style={{marginTop: -7, flex: 1}}>
						<ScriptTextUI parent={this} text={selectedScript ? selectedScript.text : ""}
							//editable={selectedScript ? selectedScript.editable : false}
							editable={selectedScript != null}
							onChangeText={text=> {
								if (!selectedScript.editable) return;
								selectedScript.text = text;
								selectedScript.fileOutdated = true;
								this.forceUpdate();
							}}/>
					</Panel>
				</Panel>
			</Drawer>
		);
	}

	/*componentWillUnmount() {
		this.SaveScripts();
	}*/
}

class ScriptTextUI extends BaseComponent {
	static defaultProps = {editable: true};
	render() {
		var {parent, editable, onChangeText, text} = this.props;
		return <TextInput {...{editable}}
			style={{flex: 1, textAlignVertical: "top", color: colors.text}}
			multiline={true} editable={editable} value={text} autoCapitalize="none" autoCorrect={false}
			onChangeText={text=> {
				LL.scripts.selectedScript.fileOutdated = true;
				parent.setState({scriptLastRunsOutdated: true});
				onChangeText(text);
			}}/>;
	}
}
import {Script} from "./Scripts/Script";
import {BaseComponent, Panel, VButton, VTextInput} from "../Frame/ReactGlobals";
import {colors, styles} from "../Frame/Styles";
import RNFS from "react-native-fs";
import ScrollableTabView from "react-native-scrollable-tab-view";
import DialogAndroid from "react-native-dialogs";
import Drawer from "react-native-drawer";
import {Text} from "react-native";

import ScriptRunner from "./Scripts/ScriptRunner";

import scriptDefaultText_BuiltInHelpers from "./Scripts/UserScriptDefaults/BuiltInHelpers";
import scriptDefaultText_BuiltInScript from "./Scripts/UserScriptDefaults/BuiltInScript";
import scriptDefaultText_FakeDataProvider from "./Scripts/UserScriptDefaults/FakeDataProvider";
import scriptDefaultText_CustomHelpers from "./Scripts/UserScriptDefaults/CustomHelpers";
import scriptDefaultText_CustomScript from "./Scripts/UserScriptDefaults/CustomScript";

import ScriptsPanel from "./Scripts/ScriptsPanel";
import {_VDFPreSerialize, Assert, AssertWarn, E, Log, P, ToJSON} from "../Frame/Globals";
import Bind from "autobind-decorator";
import Node from "../Packages/VTree/Node";
import {LL} from "../LucidLink";

export class Scripts extends Node {
	@_VDFPreSerialize() PreSerialize() {
	    if (this.selectedScript)
	        this.selectedScriptName = this.selectedScript.Name;
	}
	/*@_VDFSerializeProp() SerializeProp(path, options) {
	    if (path.currentNode.prop.name == "selectedScript" && this.selectedScript)
	        return new VDFNode(this.selectedScript.Name);
	}*/

	ui = null;

	scripts = [];
	selectedScript = null;
	@P() selectedScriptName = null; // used only during save-to/load-from disk 
	scriptRunner = new ScriptRunner();

	LoadFileSystemData() {
		this.LoadScripts();
	}
	async LoadScripts() {
		var scriptsFolder = LL.RootFolder.GetFolder("Scripts");
		var scriptsFolderExists = await scriptsFolder.Exists();
		if (!scriptsFolderExists)
			scriptsFolder.Create();
		// ensure these scripts always exist
		if (!await scriptsFolder.GetFile("Built-in helpers.js").Exists()) {
			await scriptsFolder.GetFile("Built-in helpers.js").WriteAllText(scriptDefaultText_BuiltInHelpers);
			await scriptsFolder.GetFile("Built-in helpers.meta").WriteAllText(ToJSON({index: 1, editable: false, enabled: false}));
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

		this.selectedScript = this.scripts.First(a=>a.Name == this.selectedScriptName);

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
				let scriptFileName = scriptName + ".js";
				let script = this.scripts.First(a=>a.file.Name == scriptFileName);
				if (script == null) {
					let file = LL.RootFolder.GetFolder("Scripts").GetFile(scriptFileName);
					let script = new Script(file, `Log("Hello world!");`);
					script.index = LL.scripts.scripts.length;
					LL.scripts.scripts.push(script);
				}

				let nameToTextMap = {
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
		let scripts_ordered = this.scripts.Where(a=>a.enabled).OrderBy(a=> {
			AssertWarn(a.index != -1000, `Script-order not found in meta file for: ${a.file.Name}`);
			return a.index;
		});
		this.scriptRunner.Apply(scripts_ordered);
		if (this.ui)
			this.ui.setState({scriptLastRunsOutdated: false});
	}
}
g.Extend({Scripts});

export class ScriptsUI extends BaseComponent<any, any> {
	constructor(props) {
		super(props);
		this.state = {scriptLastRunsOutdated: false};
		LL.scripts.ui = this;
	}

	_drawer;
	@Bind ToggleSidePanelOpen() {
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
					<Panel style={E(styles.header, {flexDirection: "row", flexWrap: "wrap", padding: 3, paddingBottom: -5})}>
						<VButton text="Scripts" style={{width: 100}} onPress={this.ToggleSidePanelOpen}/>
						<Text style={{marginLeft: 10, marginTop: 8, fontSize: 18}}>
						Script: {selectedScript ? selectedScript.file.NameWithoutExtension : "n/a"}
						{selectedScript && !selectedScript.editable ? " (read only)" : ""}
						</Text>
						{selectedScript && selectedScript.editable &&
							<VButton text="Rename" style={{marginLeft: 10, width: 100}}
								onPress={()=>selectedScript.Rename(()=>this.forceUpdate())}/>}
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
						<VTextInput text={selectedScript ? selectedScript.text : ""}
							accessible={true} accessibilityLabel="script text input"
							//editable={selectedScript ? selectedScript.editable : false}
							editable={selectedScript != null}
							onChangeText={text=> {
								LL.scripts.selectedScript.fileOutdated = true;
								this.setState({scriptLastRunsOutdated: true});
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
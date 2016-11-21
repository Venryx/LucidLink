import React, {Component} from "react";
import {Dimensions, StyleSheet,
	View, Button, Text, TextInput} from "react-native";
import RNFS from "react-native-fs";
var ScrollableTabView = require("react-native-scrollable-tab-view");
var DialogAndroid = require("react-native-dialogs");
import Drawer from "react-native-drawer";

import ScriptRunner from "./Scripts/ScriptRunner";

import scriptDefaultText_CoreFunctions from "./Scripts/UserScriptDefaults/CoreFunctions";
import scriptDefaultText_BuiltInHelpers from "./Scripts/UserScriptDefaults/BuiltInHelpers";
import scriptDefaultText_BuiltInScript from "./Scripts/UserScriptDefaults/BuiltInScript";
import scriptDefaultText_CustomHelpers from "./Scripts/UserScriptDefaults/CustomHelpers";
import scriptDefaultText_CustomScript from "./Scripts/UserScriptDefaults/CustomScript";

class Script {
	constructor(file, text) {
		this.file = file;
		this.text = text;
	}
}

g.Scripts = class Scripts extends Node {
	ui = null;

	scripts = [];
	scriptRunner = new ScriptRunner();

	LoadFileSystemData() {
		this.LoadScripts();
	}
	async LoadScripts() {
		var scripts = [];
		var scriptsFolder = new Folder(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/");
		var scriptsFolderExists = await scriptsFolder.Exists();
		if (!scriptsFolderExists) {
			scriptsFolder.Create();
			await scriptsFolder.GetFile("1) Core functions.js").WriteAllText(scriptDefaultText_CoreFunctions);
			await scriptsFolder.GetFile("2) Built-in helpers.js").WriteAllText(scriptDefaultText_BuiltInHelpers);
			await scriptsFolder.GetFile("3) Built-in script.js").WriteAllText(scriptDefaultText_BuiltInScript);
			await scriptsFolder.GetFile("4) Custom helpers.js").WriteAllText(scriptDefaultText_CustomHelpers);
			await scriptsFolder.GetFile("5) Custom script.js").WriteAllText(scriptDefaultText_CustomScript);
		}

		var scriptFiles = await scriptsFolder.GetFiles();
		var scripts = [];
		for (let scriptFile of scriptFiles) {
			let scriptText = await scriptFile.ReadAllText();
			let script = new Script(scriptFile, scriptText);
			scripts.push(script);
		}
		this.scripts = scripts;

		if (LL.settings.applyScriptsOnLaunch)
			this.ApplyScripts();
		
		if (this.ui)
			this.ui.forceUpdate();
		Log("Finished loading scripts.");
	}

	SaveFileSystemData() {
		this.SaveScripts();
	}
	async SaveScripts() {
		var {scripts} = this;
		for (let script of scripts) {
			script.file.WriteAllText(script.text);
		}

		if (this.ui)
			this.ui.setState({scriptFilesOutdated: false});
		Log("Finished saving scripts.");
	}

	ResetScript(script) {
		var dialog = new DialogAndroid();
		dialog.set({
			"title": `Reset script "${script.file.NameWithoutExtension}"`,
			"content": `Reset script to its factory state?

This will permanently remove all custom code from the script.`,
			"positiveText": "OK",
			"negativeText": "Cancel",
			"onPositive": ()=> {
				if (script.file.Name == "3) Built-in script.js")
					script.text = scriptDefaultText_BuiltInScript;
				else {
					Assert(script.file.Name == "5) Custom script.js");
					script.text = scriptDefaultText_CustomScript;
				}
				if (this.ui)
					this.ui.setState({scriptFilesOutdated: true});
			},
		});
		dialog.show();
	}
	
	ApplyScripts() {
		this.scriptRunner.Reset();
		this.scriptRunner.Init(this.scripts);
		if (this.ui)
			this.ui.setState({scriptLastRunsOutdated: false});
	}
}

import ScriptsPanel from "./Scripts/ScriptsPanel";

export class ScriptsUI extends BaseComponent {
	constructor(props) {
		super(props);
		this.state = {scriptFilesOutdated: false, scriptLastRunsOutdated: false};
		LL.scripts.ui = this;
	}

	scriptsPanel = null;
	@Bind ToggleScriptsPanelOpen() {
		if (this._drawer._open)
			this._drawer.close();
		else
			this._drawer.open();
	}

	render() {
		var node = LL.scripts;
		var {activeScript, scriptFilesOutdated, scriptLastRunsOutdated} = this.state;
		
		var barHeight = isLandscape ? 35 : 50;

		const drawerStyles = {
			drawer: {shadowColor: "#000000", shadowOpacity: .8, shadowRadius: 3},
			main: {paddingLeft: 3},
		};

		return (
			<Drawer ref={comp=>this._drawer = comp} content={<ScriptsPanel parent={this}/>}
					type="overlay" openDrawerOffset={0.5} panCloseMask={0.5} tapToClose={true}
					closedDrawerOffset={-3} styles={drawerStyles}>
				<View style={{flex: 1, flexDirection: "column", height: barHeight + 3}}>
					<View style={{flexDirection: "row", flexWrap: "wrap", padding: 3, paddingBottom: 0, height: barHeight}}>
						<View style={{flex: .8, flexDirection: "row"}}>
							<VButton text="Scripts" style={{width: 100}} onPress={this.ToggleScriptsPanelOpen}/>
							<View style={{flex: 1}}/>
							<View style={{flexDirection: "row", alignItems: "flex-end"}}>
								<VButton color="#777" text="Save" enabled={scriptFilesOutdated}
									style={{width: 100, marginLeft: 5, height: barHeight + 3}}
									onPress={()=>node.SaveScripts()}/>
								<VButton color="#777" text="Apply" enabled={scriptLastRunsOutdated}
									style={{width: 100, marginLeft: 5, height: barHeight + 3}}
									onPress={()=>node.ApplyScripts()}/>
							</View>
						</View>
					</View>
					<View style={{flex: 1}}>
						<ScriptTextUI parent={this} text={activeScript ? activeScript.text : ""} editable={true}
							onChangeText={text=> {
								if (!activeScript.editable) return;
								activeScript.text = text;
								this.forceUpdate();
							}}/>
					</View>
				</View>
			</Drawer>
		);
	}

	PostScriptChange() {
		var node = LL.scripts;
		BufferFuncToBeRun("PostScriptChange_1", 1000, ()=>node.SaveScripts());
		this.forceUpdate();
	}

	/*componentWillUnmount() {
		this.SaveScripts();
	}*/
}

class ScriptTextUI extends BaseComponent {
	static defaultProps = {editable: true};
	render() {
		var {parent, editable, onChangeText, text} = this.props;
		var textStyle = {
			//height: screenHeight,
			flex: 1, textAlignVertical: "top",
		};
		return <TextInput {...{editable}} style={textStyle} multiline={true} editable={editable} value={text}
			autoCapitalize="none" autoCorrect={false}
			onChangeText={text=> {
				parent.setState({scriptFilesOutdated: true, scriptLastRunsOutdated: true});
				onChangeText(text);
			}}/>;
	}
}
import React, {Component} from "react";
import {Dimensions, StyleSheet,
	View, Button, Text, TextInput} from "react-native";
import RNFS from "react-native-fs";
var ScrollableTabView = require("react-native-scrollable-tab-view");
var DialogAndroid = require("react-native-dialogs");

import ScriptRunner from "./Scripts/ScriptRunner";

import scriptDefaultText_CoreFunctions from "./Scripts/UserScriptDefaults/CoreFunctions";
import scriptDefaultText_BuiltInHelpers from "./Scripts/UserScriptDefaults/BuiltInHelpers";
import scriptDefaultText_BuiltInScript from "./Scripts/UserScriptDefaults/BuiltInScript";
import scriptDefaultText_CustomHelpers from "./Scripts/UserScriptDefaults/CustomHelpers";
import scriptDefaultText_CustomScript from "./Scripts/UserScriptDefaults/CustomScript";
var scriptDefaultTexts = [scriptDefaultText_CoreFunctions, scriptDefaultText_BuiltInHelpers,
	scriptDefaultText_BuiltInScript, scriptDefaultText_CustomHelpers, scriptDefaultText_CustomScript];
var scriptNames = ["Core functions", "Built-in helpers", "Built-in script", "Custom helpers", "Custom script"];

g.Scripts = class Scripts extends Node {
	ui = null;

	scriptTexts = [];
	scriptRunner = new ScriptRunner();

	LoadFileSystemData() {
		this.LoadScripts();
	}
	async LoadScripts() {
		var scriptTexts = [];
		var scriptsFolder = VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/";
		//scriptTexts[0] = await VFile.ReadAllTextAsync(scriptsFolder + "Script1.js", scriptDefaultText_CoreFunctions);
		//scriptTexts[1] = await VFile.ReadAllTextAsync(scriptsFolder + "Script2.js", scriptDefaultText_BuiltInHelpers);
		scriptTexts[0] = scriptDefaultTexts[0];
		scriptTexts[1] = scriptDefaultTexts[1];
		scriptTexts[2] = await VFile.ReadAllTextAsync(scriptsFolder + "Script3.js", scriptDefaultTexts[2]);
		scriptTexts[3] = await VFile.ReadAllTextAsync(scriptsFolder + "Script4.js", scriptDefaultTexts[3]);
		scriptTexts[4] = await VFile.ReadAllTextAsync(scriptsFolder + "Script5.js", scriptDefaultTexts[4]);

		this.scriptTexts = scriptTexts;
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
		var {scriptTexts} = this;
		Assert(scriptTexts.length == 5, `Script-text count should be 5, not ${scriptTexts.length}.`);
		for (let text of scriptTexts)
			Assert(text != null);
		await VFile.CreateFolderAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/");
		//await VFile.WriteAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/Script1.js", scriptTexts[0]);
		//await VFile.WriteAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/Script2.js", scriptTexts[1]);
		await VFile.WriteAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/Script3.js", scriptTexts[2]);
		await VFile.WriteAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/Script4.js", scriptTexts[3]);
		await VFile.WriteAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/Script5.js", scriptTexts[4]);

		if (this.ui)
			this.ui.setState({scriptFilesOutdated: false});
		Log("Finished saving scripts.");
	}

	ResetScript(index) {
		var dialog = new DialogAndroid();
		dialog.set({
			"title": `Reset script "${scriptNames[index]}"`,
			"content": `Reset script to its factory state?

This will permanently remove all custom code from the script.`,
			"positiveText": "OK",
			"negativeText": "Cancel",
			"onPositive": ()=> {
				this.scriptTexts[index] = scriptDefaultTexts[index];
				if (this.ui)
					this.ui.setState({scriptFilesOutdated: true});
			},
		});
		dialog.show();
	}
	
	ApplyScripts() {
		this.scriptRunner.Reset();
		this.scriptRunner.Init(this.scriptTexts);
		if (this.ui)
			this.ui.setState({scriptLastRunsOutdated: false});
	}
}

export class ScriptsUI extends BaseComponent {
	constructor(props) {
		super(props);
		this.state = {activeTab: 0, scriptFilesOutdated: false, scriptLastRunsOutdated: false};
		LL.scripts.ui = this;
	}

	render() {
		var node = LL.scripts;
		var {scriptTexts} = node;
		var {scriptFilesOutdated, scriptLastRunsOutdated} = this.state;
		
		var barHeight = isLandscape ? 35 : 50;
		var tabStyle = {flex: .2, marginLeft: 5, height: barHeight + 3};

		function GetTabLabel(index) { return `${index + 1}: ${scriptNames[index]}`; }
		
		return (
			<View style={{flex: 1, flexDirection: "column", height: barHeight + 3}}>
				<View style={{flexDirection: "row", flexWrap: "wrap", padding: 3, paddingBottom: 0, height: barHeight}}>
					<View style={{flex: .8, flexDirection: "row"}}>
						<VButton style={E(tabStyle, {marginLeft: 0})} text={GetTabLabel(0)}
							onPress={()=>this.setState({activeTab: 0})}/>
						<VButton style={tabStyle} color="#777" textStyle={{margin: 0}}
							onPress={()=>this.setState({activeTab: 1})} text={GetTabLabel(1)}/>
						<VButton style={tabStyle} color="#777" textStyle={{margin: 5}}
							onPress={()=>this.setState({activeTab: 2})} text={GetTabLabel(2)}/>
						<VButton style={tabStyle} color="#777" textStyle={{margin: 5}}
							onPress={()=>this.setState({activeTab: 3})} text={GetTabLabel(3)}/>
						<VButton style={tabStyle} color="#777" textStyle={{margin: 5}}
							onPress={()=>this.setState({activeTab: 4})} text={GetTabLabel(4)}/>
						<View style={{flex: .1}}/>
						<View style={{flex: .3, flexDirection: "row", alignItems: "flex-end"}}>
							<VButton style={tabStyle} color="#777" text="Save" enabled={scriptFilesOutdated}
								onPress={()=>node.SaveScripts()}/>
							<VButton style={tabStyle} color="#777" text="Apply" enabled={scriptLastRunsOutdated}
								onPress={()=>node.ApplyScripts()}/>
						</View>
					</View>
				</View>
				<View style={{flex: 1}}>
                	{this.GetActiveTabUI()}
				</View>
            </View>
		);
	}
	GetActiveTabUI() {
		var tabEditabilities = [false, false, true, true, true];

		var node = LL.scripts;
		var {scriptTexts} = node;
		var {activeTab} = this.state;
		return (
			<View style={{flex: 1}}>
				<ScriptTextUI parent={this} text={scriptTexts[activeTab]} editable={true}
					onChangeText={text=> {
						if (!tabEditabilities[activeTab]) return;
						scriptTexts[activeTab] = text;
						this.forceUpdate();
					}}/>
			</View>
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
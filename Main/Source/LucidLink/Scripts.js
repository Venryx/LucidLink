import React, {Component} from "react";
import {Dimensions, StyleSheet,
	View, Button, Text, TextInput} from "react-native";
import RNFS from "react-native-fs";
var ScrollableTabView = require("react-native-scrollable-tab-view");
//var {JavaBridge, BaseComponent, VFile} = require("./Globals");

g.Scripts = class Scripts {
}

export class ScriptsUI extends BaseComponent {
	constructor(props) {
		super(props);
		this.state = {
			scriptTexts: [],
			activeTab: 0,
		};
	}

	componentWillMount() {
		this.LoadScriptTexts();
	}

	async LoadScriptTexts() {
		var scriptTexts = [];
		scriptTexts[0] = await VFile.ReadAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/Script1.js", "test1");
		scriptTexts[1] = await VFile.ReadAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/Script2.js", "test2");
		scriptTexts[2] = await VFile.ReadAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/Script3.js", "test3");
		scriptTexts[3] = await VFile.ReadAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/Script4.js", "test4");
		scriptTexts[4] = await VFile.ReadAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/Script5.js", "test5");
		this.setState({scriptTexts});
		Log("Finished loading.");
	}
	async SaveScriptTexts() {
		var {scriptTexts} = this.state;
		Assert(scriptTexts.length == 5, `Script-text count should be 5, not ${scriptTexts.length}.`);
		for (let text of scriptTexts)
			Assert(text != null);
		await VFile.CreateFolderAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/");
		await VFile.WriteAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/Script1.js", scriptTexts[0]);
		await VFile.WriteAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/Script2.js", scriptTexts[1]);
		await VFile.WriteAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/Script3.js", scriptTexts[2]);
		await VFile.WriteAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/Script4.js", scriptTexts[3]);
		await VFile.WriteAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/Scripts/Script5.js", scriptTexts[4]);
		Log("Finished saving.");
	}

	ApplyScripts() {

	}

	render() {
		var {scriptTexts} = this.state;
		
		var tabStyle = {flex: .2, marginLeft: 5};
		return (
			<View style={{flex: 1, flexDirection: "column"}}>
				<View style={{flexDirection: "row", flexWrap: "wrap", padding: 3}}>
					<View style={{flex: .8, flexDirection: "row"}}>
						<VButton style={E(tabStyle, {marginLeft: 0})} color="#777"
							onPress={()=>this.setState({activeTab: 0})} title="1: Built-in functions"/>
						<VButton style={tabStyle} color="#777" onPress={()=>this.setState({activeTab: 1})} title="2: Built-in helpers"/>
						<VButton style={tabStyle} color="#777" onPress={()=>this.setState({activeTab: 2})} title="3: Built-in script"/>
						<VButton style={tabStyle} color="#777" onPress={()=>this.setState({activeTab: 3})} title="4: Custom helpers"/>
						<VButton style={tabStyle} color="#777" onPress={()=>this.setState({activeTab: 4})} title="5: Custom script"/>
						<View style={{flex: .2}}/>
						<View style={{flex: .2, flexDirection: "row", alignItems: "flex-end"}}>
							<VButton style={tabStyle} color="#777" onPress={()=>this.ApplyScripts()} title="Apply scripts"/>
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
		var tabLabels = ["1: Core functions", "2: Built-in helpers", "3: Built-in script", "4: Custom helpers", "5: Custom script"];
		var tabEditabilities = [false, false, true, true, true];
		
		var {scriptTexts, activeTab} = this.state;
		return (
			<View tabLabel={tabLabels[activeTab]} style={{flex: 1}}>
				<ScriptTextUI text={scriptTexts[activeTab]} editable={tabEditabilities[activeTab]}
					onChangeText={text=>(scriptTexts[activeTab] = text) | this.forceUpdate()}/>
			</View>
		);
	}

	PostScriptChange() {
		BufferFuncToBeRun("PostScriptChange_1", 1000, ()=>this.SaveScriptTexts());
		this.forceUpdate();
	}

	/*componentWillUnmount() {
		this.SaveScriptTexts();
	}*/
}

class ScriptTextUI extends BaseComponent {
	static defaultProps = {editable: true};
	render() {
		var {editable, onChangeText, text} = this.props;
		var textStyle = {
			//height: screenHeight,
			flex: 1, textAlignVertical: "top",
		};
		return <TextInput {...{editable, onChangeText}} style={textStyle} multiline={true} editable={editable} value={text}/>;
	}
}
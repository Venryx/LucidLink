import React, {Component} from "react";
import {Dimensions, StyleSheet,
	Text, TextInput, View} from "react-native";
var ScrollableTabView = require("react-native-scrollable-tab-view");

//var {JavaBridge, BaseComponent, VFile} = require("./Globals");

//let screenHeight = Dimensions.get("window").height;
var styles = StyleSheet.create({
	root: {},
	tab: {flex: 1},
	text: {
		//height: screenHeight,
		flex: 1, textAlignVertical: "top",
	},
});

class ScriptTextUI extends BaseComponent {
	static defaultProps = {editable: true};
	render() {
		var {editable, onChangeText, text} = this.props;
		return <TextInput {...{editable, onChangeText}} style={styles.text} multiline={true} editable={editable} value={text}/>;
	}
}
import RNFS from "react-native-fs";

export default class ScriptsUI extends BaseComponent {
	constructor(props) {
		super(props);
		this.state = {scriptTexts: []};
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

	render() {
		var {scriptTexts} = this.state;
		return (
			<ScrollableTabView>
                <View style={styles.tab} tabLabel="1: Built-in functions">
					<ScriptTextUI text={scriptTexts[0]} editable={false}/>
                </View>
                <View style={styles.tab} tabLabel="2: Built-in helpers">
					<ScriptTextUI text={scriptTexts[1]} editable={false}/>
                </View>
                <View style={styles.tab} tabLabel="3: Built-in script">
					<ScriptTextUI text={scriptTexts[2]} onChangeText={text=>(scriptTexts[2] = text) | this.PostScriptChange()}/>
                </View>
                <View style={styles.tab} tabLabel="4: Custom helpers">
					<ScriptTextUI text={scriptTexts[3]} onChangeText={text=>(scriptTexts[3] = text) | this.PostScriptChange()}/>
                </View>
                <View style={styles.tab} tabLabel="5: Custom script">
					<ScriptTextUI text={scriptTexts[4]} onChangeText={text=>(scriptTexts[4] = text) | this.PostScriptChange()}/>
                </View>
            </ScrollableTabView>
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
import React, {Component} from "react";
import {AppRegistry, StyleSheet, AppState, DeviceEventEmitter} from "react-native";
import {Text, View, ViewPagerAndroid} from "react-native";
import Orientation from "react-native-orientation";
var ScrollableTabView = require("react-native-scrollable-tab-view");

// simple imports
var g = global;
g.g = g;
require("./CE");
import * as UM1 from "./Packages/VDF/VDF";
import * as UM2 from "./Packages/VDF/VDFLoader";
import * as UM3 from "./Packages/VDF/VDFNode";
import * as UM4 from "./Packages/VDF/VDFSaver";
import * as UM5 from "./Packages/VDF/VDFTokenParser";
import * as UM6 from "./Packages/VDF/VDFTypeInfo";
import * as Globals from "./Globals";
import * as ReactGlobals from "./ReactGlobals";
import * as UM7 from "./Packages/VTree/Node";
import * as UM8 from "./Packages/V/V";

import TestData from "./TestData";
//import {JavaBridge} from "./Globals";

import {ScriptsUI} from "./LucidLink/Scripts";
import {SettingsUI} from "./LucidLink/Settings";
import {AboutUI} from "./LucidLink/About";

// key-codes can be found here: https://developer.android.com/ndk/reference/keycodes_8h.html
DeviceEventEmitter.addListener("OnKeyDown", args=> {
	var [keyCode] = args;
	//Log("KeyDown:" + keyCode);
	LL.scripts.scriptRunner.TriggerKeyDown(keyCode);
});
DeviceEventEmitter.addListener("OnKeyUp", args=> {
	var [keyCode] = args;
	//Log("KeyUp:" + keyCode);
	LL.scripts.scriptRunner.TriggerKeyUp(keyCode);
});


g.isLandscape = Orientation.getInitialOrientation() == "LANDSCAPE";
Orientation.addOrientationListener(orientation=> {
	g.isLandscape = orientation == "LANDSCAPE";
	LL.ui.forceUpdate();
});

g.appState = AppState.currentState;
AppState.addEventListener("change", appState=> {
	g.appState = appState;
	if (appState == "background")
		LL.SaveFileSystemData();
});

const styles = StyleSheet.create({
	container: {
		flex: 1,
		justifyContent: 'center',
		alignItems: 'center',
		backgroundColor: '#F5FCFF',
	},
	welcome: {
		fontSize: 20,
		textAlign: 'center',
		margin: 10,
	},
	instructions: {
		textAlign: 'center',
		color: '#333333',
		marginBottom: 5,
	},
});

g.LucidLink = class LucidLink extends Node {
	@T("Scripts") @P(true, true) scripts = null;
	@T("Settings") @P(true, true) settings = null;
	@T("About") @P(true, true) about = null;

	PushBasicDataToJava() {
		JavaBridge.Main.SetBlockUnusedKeys(LL.settings.blockUnusedKeys);
	}

	SaveFileSystemData() {
		this.SaveMainData();
		
		this.scripts.SaveFileSystemData();
	}
	async SaveMainData() {
		var mainDataVDF = ToVDF(g.LL, false);
		await VFile.CreateFolderAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/");
		await VFile.WriteAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/MainData.vdf", mainDataVDF);
		Log("Finished saving main-data.");
	}
}
//LucidLink.typeInfo = new VDFTypeInfo(new VDFType("^(?!_)(?!s$)(?!root$)", true));
//LucidLink.typeInfo.typeTag = new VDFType("^(?!_)(?!s$)(?!root$)", true);
LucidLink.typeInfo.typeTag = new VDFType(null, true);

async function Init(ui) {
	g.LL = new LucidLink();
	g.LL.ui = ui;
	var mainDataVDF = await VFile.ReadAllTextAsync(VFile.ExternalStorageDirectoryPath + "/Lucid Link/MainData.vdf", null);
	if (mainDataVDF) {
		var data = FromVDF(mainDataVDF, "LucidLink");
		for (var propName in data) {
			g.LL[propName] = data[propName];
		}
	}
	else {
		TestData.LoadInto(g.LL);
	}

	g.LL.scripts.LoadFileSystemData();

	g.LL.PushBasicDataToJava();
}

export default class LucidLinkUI extends Component {
    constructor(props) {
        super(props);
        this.state = {};
		Init(this);
    }

    componentWillMount() {
        //JavaBridge.Main.GetTestName(name=>this.setState({testName: name}));
        this.GetTestName();
    }
    async GetTestName() {
        var result = await JavaBridge.Main.GetTestName();
        console.log(`Got test name: ${result}`);
        this.setState({testName: result});
    }

    render() {
      var {testName} = this.state;

        return (
            <ScrollableTabView>
                <View style={styles.container} tabLabel="Monitor">
                    <Text style={styles.welcome}>
                    Welcome to React Native!
                    TestName: {testName}
                    </Text>
                    <Text style={styles.instructions}>
                    To get started, edit index.android.js
                    </Text>
                    <Text style={styles.instructions}>
                    Double tap R on your keyboard to reload,{'\n'}
                    Shake or press menu button for dev menu
                    </Text>
                </View>
                <View style={styles.container} tabLabel="Tracker">
                </View>
                <View style={styles.container} tabLabel="Journal">
                </View>
                <ScriptsUI tabLabel="Scripts"/>
                <SettingsUI tabLabel="Settings"/>
                <AboutUI tabLabel="About"/>
            </ScrollableTabView>
        );
    }

	/*componentWillUnmount() {
		SaveMainData();
	}*/
}
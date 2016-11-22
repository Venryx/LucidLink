import React, {Component} from "react";
import {Dimensions, AppRegistry, StyleSheet, AppState, DeviceEventEmitter, Keyboard} from "react-native";
import {Text, View, KeyboardAvoidingView, ViewPagerAndroid} from "react-native";
import Orientation from "react-native-orientation";
var ScrollableTabView = require("react-native-scrollable-tab-view");
import Moment from "moment";

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
import UM9 from "./Packages/V/VFile";

import TestData from "./TestData";
//import {JavaBridge} from "./Globals";

import {MonitorUI} from "./LucidLink/Monitor";
import {ScriptsUI} from "./LucidLink/Scripts";
import {SettingsUI} from "./LucidLink/Settings";
import {MoreUI} from "./LucidLink/More";

// key-codes can be found here: https://developer.android.com/ndk/reference/keycodes_8h.html
DeviceEventEmitter.addListener("OnKeyDown", args=> {
	var [keyCode] = args;
	Log("keyboard", "KeyDown: " + keyCode);
	LL.scripts.scriptRunner.TriggerKeyDown(keyCode);
});
DeviceEventEmitter.addListener("OnKeyUp", args=> {
	var [keyCode] = args;
	Log("keyboard", "KeyUp: " + keyCode);
	LL.scripts.scriptRunner.TriggerKeyUp(keyCode);
});

g.isLandscape = Orientation.getInitialOrientation() == "LANDSCAPE";
Orientation.addOrientationListener(orientation=> {
	g.isLandscape = orientation == "LANDSCAPE";
	if (LL && LL.ui)
		LL.ui.forceUpdate();
});

g.appState = AppState.currentState;
AppState.addEventListener("change", appState=> {
	g.appState = appState;
	if (appState == "background" && LL)
		LL.SaveFileSystemData();
});

g.keyboardVisible = false;
g.keyboardHeight = 0;
Keyboard.addListener("keyboardDidShow", e=> {
	g.keyboardVisible = true;
	g.keyboardHeight = e.endCoordinates.height;
	LL.ui.forceUpdate();
});
Keyboard.addListener("keyboardDidHide", ()=> {
	g.keyboardVisible = false;
	g.keyboardHeight = 0;
	LL.ui.forceUpdate();
});

g.LucidLink = class LucidLink extends Node {
	@T("Monitor") @P(true, true) monitor = new Monitor();
	@T("Scripts") @P(true, true) scripts = new Scripts();
	@T("Settings") @P(true, true) settings = new Settings();
	@T("More") @P(true, true) more = new More();

	PushBasicDataToJava() {
		JavaBridge.Main.SetBlockUnusedKeys(LL.settings.blockUnusedKeys);
	}

	sessionKey = null;
	get RootFolder() { return new Folder(VFile.ExternalStorageDirectoryPath + "/Lucid Link/"); }
	get SessionFolder() { return this.RootFolder.GetFolder(`Sessions/${this.sessionKey}`); }
	sessionLogFile = null;
	async SetUpSession() {
		this.sessionKey = Moment().format("YYYY-M-D HH:mm:ss");
		await this.SessionFolder.Create();
		this.sessionLogFile = this.SessionFolder.GetFile("Log.txt");
	}

	SaveFileSystemData() {
		this.SaveMainData();
		
		this.scripts.SaveFileSystemData();
	}
	async SaveMainData() {
		var mainDataVDF = ToVDF(g.LL, false);
		await this.RootFolder.Create();
		await this.RootFolder.GetFile("MainData.vdf").WriteAllText(mainDataVDF);
		Log("Finished saving main-data.");
	}

	ui = null;
}
//LucidLink.typeInfo = new VDFTypeInfo(new VDFType("^(?!_)(?!s$)(?!root$)", true));
//LucidLink.typeInfo.typeTag = new VDFType("^(?!_)(?!s$)(?!root$)", true);
LucidLink.typeInfo.typeTag = new VDFType(null, true);

async function Init(ui) {
	g.LL = new LucidLink();
	LL.ui = ui;
	var mainDataVDF = await LL.RootFolder.GetFile("MainData.vdf").ReadAllText();
	if (mainDataVDF) {
		var data = FromVDF(mainDataVDF, "LucidLink");
		for (var propName in data) {
			LL[propName] = data[propName];
		}
	}
	else {
		TestData.LoadInto(LL);
	}

	await LL.SetUpSession();
	Log("Finished loading main-data.");
	Log("Logging to: " + LL.sessionLogFile.path);

	LL.scripts.LoadFileSystemData();

	LL.PushBasicDataToJava();
}

const styles = StyleSheet.create({
	container: {
		flex: 1,
		justifyContent: "center",
		alignItems: "center",
		backgroundColor: "#F5FCFF",
	},
	welcome: {
		fontSize: 20,
		textAlign: "center",
		margin: 10,
	},
	instructions: {
		textAlign: "center",
		color: "#333333",
		marginBottom: 5,
	},
});

export default class LucidLinkUI extends Component {
    constructor(props) {
        super(props);
        this.state = {};
		Init(this);
    }

    render() {
     	var {activeTab} = this.state;
        return (
			//<View style={{flex: 1}}>
			<ScrollableTabView style_disabled={{flex: 1}} onChangeTab={data=>this.setState({activeTab: data.i})}>
				<MonitorUI tabLabel="Monitor"/>
				<View style={styles.container} tabLabel="Tracker">
				</View>
				<View style={styles.container} tabLabel="Journal">
				</View>
				<ScriptsUI tabLabel="Scripts"/>
				<SettingsUI tabLabel="Settings"/>
				<MoreUI tabLabel="More" active={activeTab == 5}/>
			</ScrollableTabView>
				/*{keyboardVisible &&
					<View style={{position: "absolute", bottom: 0, height: 30}}>
						<VButton style={{alignItems: "flex-end", width: 100, height: 30}}
							textStyle={{margin: 3}} color="#777" text="Copy" onPress={()=>alert("Test1")}/>
					</View>}
			</View>*/
        );
    }

	/*componentWillUnmount() {
		SaveMainData();
	}*/
}
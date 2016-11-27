// simple imports
var g = global;
g.g = g;

Object.freeze = obj=>obj; // mwahahaha!! React can no longer freeze it's objects, so we can do as we please
Object.isFrozen = obj=>true;

import React, {Component} from "react";
import {Dimensions, AppRegistry, StyleSheet, AppState, DeviceEventEmitter, Keyboard} from "react-native";
import {Text, View, KeyboardAvoidingView, ViewPagerAndroid} from "react-native";
import Orientation from "react-native-orientation";
import Moment from "moment";
var ScrollableTabView = require("react-native-scrollable-tab-view");

require("./CE");
import * as UM1 from "./Packages/VDF/VDF";
import * as UM2 from "./Packages/VDF/VDFLoader";
import * as UM3 from "./Packages/VDF/VDFNode";
import * as UM4 from "./Packages/VDF/VDFSaver";
import * as UM5 from "./Packages/VDF/VDFTokenParser";
import * as UM6 from "./Packages/VDF/VDFTypeInfo";
import * as Globals from "./Globals";
import * as UM0 from "./Frame/Styles";
import * as ReactGlobals from "./ReactGlobals";
import * as UM7 from "./Packages/VTree/Node";
import * as UM8 from "./Packages/V/V";
import UM9 from "./Packages/V/VFile";
require("./Packages/Sketchy/Sketchy");

import TestData from "./TestData";
//import {JavaBridge} from "./Globals";

import {MonitorUI} from "./LucidLink/Monitor";
import {ScriptsUI} from "./LucidLink/Scripts";
import {SettingsUI} from "./LucidLink/Settings";
import {MoreUI} from "./LucidLink/More";

//var ScrollableTabView = require("react-native-scrollable-tab-view");
ScrollableTabView.defaultProps = E(ScrollableTabView.defaultProps,
	{
		tabBarBackgroundColor: colors.background_dark,
		tabBarActiveTextColor: colors.text,
		tabBarInactiveTextColor: colors.text_inactive,
		tabBarStyle: {borderColor: "#555"},
		tabBarUnderlineStyle: {backgroundColor: "#777"},
		contentProps: E(ScrollableTabView.defaultProps.contentProps, {
			contentContainerStyle: {backgroundColor: colors.background},
		})
	}
);

DeviceEventEmitter.addListener("PostJavaLog", args=> {
	var [tag, message] = args;
	Log(tag + " [java]", message);
});

// key-codes can be found here: https://developer.android.com/ndk/reference/keycodes_8h.html
DeviceEventEmitter.addListener("OnKeyDown", args=> {
	try {
		var [keyCode] = args;
		Log("keyboard", "KeyDown: " + keyCode);
		LL.scripts.scriptRunner.TriggerKeyDown(keyCode);
	} catch (ex) {}
});
DeviceEventEmitter.addListener("OnKeyUp", args=> {
	try {
		var [keyCode] = args;
		Log("keyboard", "KeyUp: " + keyCode);
		LL.scripts.scriptRunner.TriggerKeyUp(keyCode);
	} catch (ex) {}
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
	if (LL && LL.ui)
		LL.ui.forceUpdate();
});
Keyboard.addListener("keyboardDidHide", ()=> {
	g.keyboardVisible = false;
	g.keyboardHeight = 0;
	if (LL && LL.ui)
		LL.ui.forceUpdate();
});

g.LucidLink = class LucidLink extends Node {
	@T("Monitor") @P(true, true) monitor = new Monitor();
	@T("Scripts") @P(true, true) scripts = new Scripts();
	@T("Settings") @P(true, true) settings = new Settings();
	@T("More") @P(true, true) more = new More();

	PushBasicDataToJava() {
		JavaBridge.Main.SetBasicData({
			blockUnusedKeys: LL.settings.blockUnusedKeys,
			updateInterval: LL.monitor.updateInterval,
			monitor: LL.monitor.monitor,
		});
	}

	sessionKey = null;
	get RootFolder() { return new Folder(VFile.ExternalStorageDirectoryPath + "/Lucid Link/"); }
	get SessionFolder() { return this.RootFolder.GetFolder(`Sessions/${this.sessionKey}`); }
	sessionLogFile = null;
	/*async WaitTillLogFileReady() { 
		if (this.sessionLogFile) return;
		var result = new Promise();
		sessionLogFile_waitingPromises.push(result);
		return result;
	}*/
	async SetUpSession() {
		this.sessionKey = Moment().format("YYYY-M-D HH:mm:ss");
		await this.SessionFolder.Create();
		this.sessionLogFile = this.SessionFolder.GetFile("Log.txt");

		/*for (let promise of sessionLogFile_waitingPromises)
			promise.resolve();
		this.sessionLogFile_waitingPromises = [];*/
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

	mainDataLoaded = false;
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
		var node = FromVDFToNode(mainDataVDF, "LucidLink");
		var data = FromVDFNode(node, "LucidLink");
		for (var propName of node.mapChildren.keys) {
			LL[propName] = data[propName];
		}
	}
	else {
		TestData.LoadInto(LL);
	}

	await LL.SetUpSession();

	LL.mainDataLoaded = true;
	LL.ui.forceUpdate();
	Log("Finished loading main-data.");
	Log("Logging to: " + LL.sessionLogFile.path);

	LL.scripts.LoadFileSystemData();

	LL.PushBasicDataToJava();

	CheckIfInEmulator_ThenMaybeInitAndStartSearching();
}
import MuseBridge from "./Frame/MuseBridge";
async function CheckIfInEmulator_ThenMaybeInitAndStartSearching() {
	var inEmulator = await JavaBridge.Main.IsInEmulator();
	if (inEmulator)
		Log("general", `In emulator: ${inEmulator}`);
	if (!inEmulator && !MuseBridge.initialized) {
		MuseBridge.Init();
		if (LL.monitor.connect)
			MuseBridge.StartSearch(); // start listening for a muse headband
	}
}

const styles = StyleSheet.create({
	container: {
		flex: 1,
		justifyContent: "center",
		alignItems: "center",
		backgroundColor: colors.backgroundColor,
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

// note: DO NOT have this inherit from BaseComponent; it breaks react-native's hot-reloading
export default class LucidLinkUI extends Component {
    constructor(props) {
        super(props);
        this.state = {};
		Init(this);
    }

    render() {
		// if main-data not yet loaded, render blank ui
		if (LL.mainDataLoaded == false) {
			var marker = null;
			return (
				<ScrollableTabView style_disabled={{flex: 1}}
						onChangeTab={data=>this.setState({activeTab: data.i})}>
					<Panel tabLabel="Monitor">{marker}</Panel>
					<Panel tabLabel="Tracker">{marker}</Panel>
					<Panel tabLabel="Journal">{marker}</Panel>
					<Panel tabLabel="Scripts">{marker}</Panel>
					<Panel tabLabel="Settings">{marker}</Panel>
					<Panel tabLabel="More">{marker}</Panel>
				</ScrollableTabView>
			)
		}

     	var {activeTab} = this.state;
        return (
			//<Panel style={{flex: 1}}>
			<ScrollableTabView style_disabled={{flex: 1}}
					onChangeTab={data=> {
						this.setState({activeTab: data.i})
						JavaBridge.Main.OnTabSelected(data.i);
					}}>
				<MonitorUI tabLabel="Monitor" active={activeTab == 0}/>
				<Panel style={styles.container} tabLabel="Tracker" active={activeTab == 1}>
				</Panel>
				<Panel style={styles.container} tabLabel="Journal" active={activeTab == 2}>
				</Panel>
				<ScriptsUI tabLabel="Scripts" active={activeTab == 3}/>
				<SettingsUI tabLabel="Settings" active={activeTab == 4}/>
				<MoreUI tabLabel="More" active={activeTab == 5}/>
			</ScrollableTabView>
				/*{keyboardVisible &&
					<Panel style={{position: "absolute", bottom: 0, height: 30}}>
						<VButton style={{alignItems: "flex-end", width: 100, height: 30}}
							textStyle={{margin: 3}} color="#777" text="Copy" onPress={()=>alert("Test1")}/>
					</Panel>}
			</Panel>*/
        );
    }

	componentDidMount() {
		JavaBridge.Main.OnTabSelected(0);
	}

	/*componentWillUnmount() {
		SaveMainData();
	}*/
}
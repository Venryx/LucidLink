// special, early codes
var g: any = global;
g.g = g;
Object.freeze = obj=>obj; // mwahahaha!! React can no longer freeze it's objects, so we can do as we please
Object.isFrozen = obj=>true;

// imports for external (basic js) modules
import "./Packages/VDF/VDF";
import "./Packages/VDF/VDFLoader";
import "./Packages/VDF/VDFNode";
import "./Packages/VDF/VDFSaver";
import "./Packages/VDF/VDFTokenParser";
import "./Packages/VDF/VDFTypeInfo";

// special, early imports (eg type extensions)
import "./Frame/CE";

import {Assert, E, FromVDFNode, FromVDFToNode, JavaBridge, Log, P, T, ToVDF} from './Frame/Globals';
import {Panel} from "./Frame/ReactGlobals";
import Node from "./Packages/VTree/Node";
import {colors} from './Frame/Styles';

import {Component} from "react";
import {Dimensions, AppRegistry, StyleSheet, AppState, DeviceEventEmitter} from "react-native";
var {Keyboard} = require("react-native");
import {Text, View, KeyboardAvoidingView, ViewPagerAndroid} from "react-native";
import Orientation from "react-native-orientation";
//import Moment from "moment";
var Moment = require("moment");
import ScrollableTabView from "react-native-scrollable-tab-view";

import {MonitorUI, Monitor} from "./LucidLink/Monitor";
import {TrackerUI, Tracker} from "./LucidLink/Tracker";
import {JournalUI, Journal} from "./LucidLink/Journal";
import {ScriptsUI, Scripts} from "./LucidLink/Scripts";
import {SettingsUI, Settings} from "./LucidLink/Settings";
import {MoreUI, More} from "./LucidLink/More";

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

DeviceEventEmitter.addListener("PostJavaLog", (args: any)=> {
	var [tag, message] = args;
	Log(tag + " [java]", message, false);
});

// key-codes can be found here: https://developer.android.com/ndk/reference/keycodes_8h.html
DeviceEventEmitter.addListener("OnKeyDown", (args: any)=> {
	try {
		var [keyCode] = args;
		Log("keyboard", "KeyDown: " + keyCode);
		LL.scripts.scriptRunner.TriggerKeyDown(keyCode);
	} catch (ex) {}
});
DeviceEventEmitter.addListener("OnKeyUp", (args: any)=> {
	try {
		var [keyCode] = args;
		Log("keyboard", "KeyUp: " + keyCode);
		LL.scripts.scriptRunner.TriggerKeyUp(keyCode);
	} catch (ex) {}
});

var g: any = global;
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

export class LucidLink extends Node {
	/*constructor() {
		super();
		g.LL = this; // set early, so LL can be used during initial construction
	}*/

	@T("Monitor") @P(true, true) monitor = new Monitor();
	@T("Tracker") @P(true, true) tracker = new Tracker();
	@T("Journal") @P(true, true) journal = new Journal();
	@T("Scripts") @P(true, true) scripts = new Scripts();
	@T("Settings") @P(true, true) settings = new Settings();
	@T("More") @P(true, true) more = new More();

	PushBasicDataToJava() {
		var basicData = {};
		// monitor
		for (let prop of ["updateInterval", "channel1", "channel2", "channel3", "channel4", "monitor", "patternMatch"])
			basicData[prop] = LL.monitor[prop];
		// settings
		for (let prop of ["blockUnusedKeys", "patternMatchInterval", "patternMatchOffset", "museEEGPacketBufferSize",
				"eyeTracker_horizontalSensitivity", "eyeTracker_verticalSensitivity", "eyeTracker_offScreenGravity",
				"eyeTracker_relaxVSTenseIntensity", "eyeTraceSegmentSize", "eyeTraceSegmentCount"])
			basicData[prop] = LL.settings[prop];
		JavaBridge.Main.SetBasicData(basicData);
		LibMuse.reconnectAttemptInterval = LL.settings.reconnectAttemptInterval * 1000;
	}

	get RootFolder() { return new Folder(VFile.ExternalStorageDirectoryPath + "/Lucid Link/"); }

	SaveFileSystemData() {
		this.SaveMainData();
		
		this.tracker.SaveFileSystemData();
		this.scripts.SaveFileSystemData();
	}
	async SaveMainData() {
		var mainDataVDF = ToVDF(LL, false);
		await this.RootFolder.Create();
		await this.RootFolder.GetFile("MainData.vdf").WriteAllText(mainDataVDF);
		Log("Finished saving main-data.");
	}

	mainDataLoaded = false;
	ui = null;
}
//LucidLink.typeInfo = new VDFTypeInfo(new VDFType("^(?!_)(?!s$)(?!root$)", true));
//LucidLink.typeInfo.typeTag = new VDFType("^(?!_)(?!s$)(?!root$)", true);
(LucidLink as any).typeInfo.typeTag = new VDFType(null, true);
g.Extend({LucidLink});

export var LL: LucidLink;

import KeepAwake from "react-native-keep-awake";

export async function Init(ui) {
	try {

	LL = new LucidLink();
	g.LL = LL; // also make global, for debugging
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

	await LL.tracker.SetUpCurrentSession();

	LL.mainDataLoaded = true;
	LL.ui.forceUpdate();
	//ui.forceUpdate();
	Log("Finished loading main-data.");
	Log("Logging to: " + LL.tracker.currentSession.logFile.path);

	LL.tracker.LoadFileSystemData();
	LL.scripts.LoadFileSystemData();

	// whenever the basic-data changes, push it to Java
	autorun(()=> {
		LL.PushBasicDataToJava();
	});

	// whenever keep-device-awake setting changes, update the keep-awake library's state
	autorun(()=> {
		if (LL.settings.keepDeviceAwake)
			KeepAwake.activate();
		else
			KeepAwake.deactivate();
	});

	CheckIfInEmulator_ThenMaybeInitAndStartSearching();

	} catch (ex) { alert("Startup error) " + ex + "\n" + ex.stack); }
}

import MuseBridge from "./Frame/MuseBridge";
import {Folder, VFile} from "./Packages/V/VFile";
import {autorun} from "mobx";
import TestData from "./Frame/TestData";
import LibMuse from "react-native-libmuse";
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
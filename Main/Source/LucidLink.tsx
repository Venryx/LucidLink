// imports for external (basic js) modules
import "./Packages/VDF/VDF";
import "./Packages/VDF/VDFLoader";
import "./Packages/VDF/VDFNode";
import "./Packages/VDF/VDFSaver";
import "./Packages/VDF/VDFTokenParser";
import "./Packages/VDF/VDFTypeInfo";

// special, early imports (eg type extensions)
import "./Frame/CE";

// imports for otherwise-not-imported files
import "moment-round";
import "./Frame/General/Errors";

import {E, FromVDFNode, FromVDFToNode, JavaBridge, Log, ToVDF, Global} from "./Frame/Globals";
import {Panel} from "./Frame/ReactGlobals";
import Node from "./Packages/VTree/Node";
import {colors} from "./Frame/Styles";

import {Component} from "react";
import {Dimensions, AppRegistry, StyleSheet, AppState, DeviceEventEmitter} from "react-native";
import {Keyboard} from "react-native";
import {Text, View, KeyboardAvoidingView, ViewPagerAndroid} from "react-native";
//import Moment from "moment";
import ScrollableTabView from "react-native-scrollable-tab-view";

import {MonitorUI, Monitor} from "./LucidLink/Tools/Monitor";
import {TrackerUI, Tracker} from "./LucidLink/Tracker";
import {JournalUI, Journal} from "./LucidLink/Journal";
import {ScriptsUI, Scripts} from "./LucidLink/Scripts";
import {SettingsUI, Settings} from "./LucidLink/Settings";
import {MoreUI, More} from "./LucidLink/More";

import KeepAwake from "react-native-keep-awake";
import MuseBridge from "./Frame/MuseBridge";
import {Folder, VFile} from "./Packages/V/VFile";
import {autorun} from "mobx";
import TestData from "./Frame/TestData";
import LibMuse from "react-native-libmuse";
import {P, T, VDFType} from "./Packages/VDF/VDFTypeInfo";
import {Tools} from "./LucidLink/Tools";
import SPBridge from "./Frame/SPBridge";

//Assert([].Except instanceof Function, "Could not add extension-methods. Make sure function names are preserved by js-minifier.");
if (!([].Except instanceof Function))
	throw new Error("Could not add extension-methods. Make sure function names are preserved by js-minifier.");

//var ScrollableTabView = require("react-native-scrollable-tab-view");
ScrollableTabView.defaultProps = E(ScrollableTabView.defaultProps,
	{
		tabBarBackgroundColor: colors.background_dark,
		tabBarActiveTextColor: colors.text,
		tabBarInactiveTextColor: colors.text_inactive,
		tabBarStyle: {borderColor: "#555"},
		tabBarUnderlineStyle: {backgroundColor: "#777"},
		contentProps: E(ScrollableTabView.defaultProps.contentProps, {
			//style: E(ScrollableTabView.defaultProps.contentProps.style, {backgroundColor: colors.background}),
			contentContainerStyle: {backgroundColor: colors.background},
		})
	}
);

DeviceEventEmitter.addListener("PostJavaLog", (args: any)=> {
	var [tag, message] = args;
	Log(tag + " [java]", message, false);
});
DeviceEventEmitter.addListener("Alert", (args: any)=> {
	var [message] = args;
	alert(message);
});

// key-codes can be found here: https://developer.android.com/ndk/reference/keycodes_8h.html
DeviceEventEmitter.addListener("OnKeyDown", (args: any)=> {
	try {
		var [keyCode, keyChar] = args;
		Log("keyboard", "KeyDown: " + keyCode);
		LL.scripts.scriptRunner.TriggerKeyDown(keyCode);
		LL.journal.OnKeyDown(keyCode, keyChar);
	} catch (ex) {}
});
DeviceEventEmitter.addListener("OnKeyUp", (args: any)=> {
	try {
		var [keyCode, keyChar] = args;
		Log("keyboard", "KeyUp: " + keyCode);
		LL.scripts.scriptRunner.TriggerKeyUp(keyCode);
	} catch (ex) {}
});

DeviceEventEmitter.addListener("PreAppClose", (args: any)=> {
	//throw new Error("Test100");
	if (this.tracker.currentSession.CurrentSleepSession)
		this.tracker.currentSession.CurrentSleepSession.End();
	Log("PreAppClose done!");
});

/*var receivedNextHeartBeat = false;
DeviceEventEmitter.addListener("OnPause_HeartBeat", (args: any)=> {
	receivedNextHeartBeat = true;
});
setInterval(()=> {
	if (g.appState == "background" && !receivedNextHeartBeat) {
		alert("Not receiving heartbeat! (ie closing)");
	}
}, 1000);*/

AppRegistry.registerHeadlessTask('PreAppClose2', ()=> {
	Log("PreAppClose2 done!");
});

var g: any = global;

/*import Orientation from "react-native-orientation";
import {EveryXSecondsDo} from "./LucidLink/Scripts/ScriptGlobals";
g.isLandscape = Orientation.getInitialOrientation() == "LANDSCAPE";
Orientation.addOrientationListener(orientation=> {
	g.isLandscape = orientation == "LANDSCAPE";
	if (LL && LL.ui)
		LL.ui.forceUpdate();
});*/

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
}, null);
Keyboard.addListener("keyboardDidHide", ()=> {
	g.keyboardVisible = false;
	g.keyboardHeight = 0;
	if (LL && LL.ui)
		LL.ui.forceUpdate();
}, null);

@Global
export class LucidLink extends Node {
	/*constructor() {
		super();
		g.LL = this; // set early, so LL can be used during initial construction
	}*/

	@T("Tools") @P(true, true) tools = new Tools();
	@T("Tracker") @P(true, true) tracker = new Tracker();
	@T("Journal") @P(true, true) journal = new Journal();
	@T("Scripts") @P(true, true) scripts = new Scripts();
	@T("Settings") @P(true, true) settings = new Settings();
	@T("More") @P(true, true) more = new More();

	PushBasicDataToJava() {
		var basicData = {};
		// monitor
		for (let prop of ["updateInterval", "channel1", "channel2", "channel3", "channel4", "monitor", "patternMatch"])
			basicData[prop] = LL.tools.monitor[prop];
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

export var LL: LucidLink;

export async function Init(ui) {
	try {

	var permissionsGranted = await JavaBridge.Main.ArePermissionsGranted();
	if (!permissionsGranted) return;

	LL = new LucidLink();
	g.LL = LL; // also make global, for debugging
	LL.ui = ui;

	var mainDataFile = LL.RootFolder.GetFile("MainData.vdf");
	var mainDataVDF = await mainDataFile.Exists() && await mainDataFile.ReadAllText();
	if (mainDataVDF) {
		var node = FromVDFToNode(mainDataVDF, "LucidLink");
		var data = FromVDFNode(node, "LucidLink");
		for (var propName of node.mapChildren.keys) {
			LL[propName] = data[propName];
		}
	} else {
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

	for (let listener of postInitActions)
		listener();
	postInitActions = "done" as any;	

	} catch (ex) { alert("Startup error) " + ex + "\n" + ex.stack); }
}
var postInitActions = [] as (()=>void)[];
export function RunPostInit(action: ()=>void) {
	if (postInitActions == "done" as any) // if init already done, just run directly
		action();
	else {
		postInitActions = postInitActions || [];
		postInitActions.push(action);
	}
}

async function CheckIfInEmulator_ThenMaybeInitAndStartSearching() {
	var inEmulator = await JavaBridge.Main.IsInEmulator();
	if (inEmulator) {
		Log("general", `In emulator: ${inEmulator}`);
		return;
	}
	
	if (!MuseBridge.initialized)
		MuseBridge.Init();
	autorun(()=> {
		if (LL.tools.monitor.connect)
			MuseBridge.StartSearch(); // start listening for a muse headband
		else
			MuseBridge.Disconnect();
	});

	// also for sp-monitor
	if (!SPBridge.initialized)
		SPBridge.Init();
	autorun(()=> {
		if (LL.tools.spMonitor.connect) {
			SPBridge.Connect();
		} else {
			//SPBridge.StopSession();
			SPBridge.Disconnect();
		}
	});
	//autorun(()=>SPBridge.SetUserInfo(LL.settings.age, LL.settings.gender.name.toLowerCase() as any));
	autorun(()=> {
		SPBridge.SetUserInfo(LL.settings.age, LL.settings.gender.name.toLowerCase() as any);
	});
}
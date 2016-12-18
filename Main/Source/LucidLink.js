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
import ScrollableTabView from "react-native-scrollable-tab-view";

require("./Frame/CE");
import "./Packages/VDF/VDF";
import "./Packages/VDF/VDFLoader";
import "./Packages/VDF/VDFNode";
import "./Packages/VDF/VDFSaver";
import "./Packages/VDF/VDFTokenParser";
import "./Packages/VDF/VDFTypeInfo";
import "./Frame/Globals";
import "./Frame/Styles";
import "./Frame/ReactGlobals";
require("./Frame/Graphics/VectorStructs");
import "./Packages/VTree/Node";
import "./Packages/V/V";
import "./Packages/V/VFile";
require("./LucidLink/Tracker/Session");
require("./LucidLink/Scripts/Script");
require("./Packages/Sketchy/Sketchy");
require("./Frame/LCE");

import TestData from "./Frame/TestData";
//import {JavaBridge} from "./Globals";

import {MonitorUI} from "./LucidLink/Monitor";
import {TrackerUI} from "./LucidLink/Tracker";
import {JournalUI} from "./LucidLink/Journal";
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
	Log(tag + " [java]", message, false);
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

class LucidLink extends Node {
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
	}
	PushPatternsToJava() {
		Assert(this.settings.patterns.All(a=>a instanceof Pattern),
			`Not all entries are Patterns! Types: ${this.settings.patterns.Select(a=>a.GetTypeName()).join(",")}`);
		JavaBridge.Main.SetPatterns(this.settings.patterns);
	}

	get RootFolder() { return new Folder(VFile.ExternalStorageDirectoryPath + "/Lucid Link/"); }

	SaveFileSystemData() {
		this.SaveMainData();
		
		this.tracker.SaveFileSystemData();
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
g.LucidLink = LucidLink;

async function Init(ui) {
	try {

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

	await LL.tracker.SetUpCurrentSession();

	LL.mainDataLoaded = true;
	LL.ui.forceUpdate();
	Log("Finished loading main-data.");
	Log("Logging to: " + LL.tracker.currentSession.logFile.path);

	LL.tracker.LoadFileSystemData();
	LL.scripts.LoadFileSystemData();

	// whenever the basic-data changes, push it to Java
	AutoRun(()=> {
		LL.PushBasicDataToJava();
	});
	// whenever a pattern changes, push the patterns to Java
	AutoRun(()=> {
		LL.PushPatternsToJava();
	});

	CheckIfInEmulator_ThenMaybeInitAndStartSearching();

	} catch (ex) { alert(ex); }
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
				<TrackerUI tabLabel="Tracker" active={activeTab == 1}/>
				<JournalUI tabLabel="Journal" active={activeTab == 2}/>
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
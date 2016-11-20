import React, {Component} from "react";
import {AppRegistry, NativeModules, StyleSheet} from "react-native";
import {Text, View, ViewPagerAndroid} from "react-native";
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

import TestData from "./TestData";
//import {JavaBridge} from "./Globals";

import {ScriptsUI} from "./LucidLink/Scripts";
import {SettingsUI} from "./LucidLink/Settings";
import {AboutUI} from "./LucidLink/About";

import Orientation from "react-native-orientation";
//var Orientation = NativeModules.Orientation;

g.isLandscape = Orientation.getInitialOrientation() == "LANDSCAPE";
Orientation.addOrientationListener(orientation=> {
	g.isLandscape = orientation == "LANDSCAPE";
	LL.forceUpdate();
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

g.LucidLink = class LucidLink {
	scripts = null;
	settings = null;
	about = null;
}

function Init() {
	g.LL = new LucidLink();
	TestData.LoadInto(g.LL);
}

export default class LucidLinkUI extends Component {
    constructor(props) {
        super(props);
        this.state = {};
		Init();
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
}
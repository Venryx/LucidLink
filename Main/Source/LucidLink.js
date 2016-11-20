import React, {Component} from "react";
import {AppRegistry, NativeModules, StyleSheet} from "react-native";
import {Text, View, ViewPagerAndroid} from "react-native";
var ScrollableTabView = require("react-native-scrollable-tab-view");

require("./CE");
import * as Test1 from "./Globals";
import * as Test2 from "./ReactGlobals";
import TestData from "./TestData";
//import {JavaBridge} from "./Globals";
import {ScriptsUI} from "./LucidLink/Scripts";
import {SettingsUI} from "./LucidLink/Settings";

export default class LucidLink extends Component {
    constructor(props) {
        super(props);
        this.state = {};
		g.LL = this;
		TestData.LoadInto(this);
    }

    componentWillMount() {
        //JavaBridge.Main.GetTestName(name=>this.setState({testName: name}));
        this.GetTestName();
    }
    async GetTestName() {
        var result = await JavaBridge.Main.GetTestName();
        console.log(`Got test name:${result}`);
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
                <View style={styles.container} tabLabel="About">
                </View>
            </ScrollableTabView>
        );
    }
}

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
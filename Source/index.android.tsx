import {AppRegistry, NativeModules} from "react-native";

// for some reason, a native (C++) error occurs for certain NativeModules, unless we import them early
import "react-native-libmuse";
import "./EarlyRun";

//Object.getOwnPropertyDescriptor(NativeModules, "SPlus");

import "./LucidLink"; // make sure this is imported first
import LucidLinkUI from "./LucidLinkUI";

AppRegistry.registerComponent("LucidLink", ()=>LucidLinkUI);

/*import * as React from "react";
import {Component} from "react";
import {Text} from "react-native";
class Test1 extends Component<{}, {}> {
	render() {
		return <Text>Hi</Text>;
	}
}
AppRegistry.registerComponent("LucidLink", ()=>Test1);*/
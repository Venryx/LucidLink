import React, {Component} from "react";
import {NativeModules} from "react-native";
//import {View, Button} from "react-native";
import {Dimensions, StyleSheet,
	View, Text, Switch, TextInput, ScrollView, TouchableHighlight} from "react-native";
import RNFS from "react-native-fs";
//import autobind from "react-autobind"; // caused error in Babel transpiler
import Bind from "autobind-decorator";

import Button from 'apsl-react-native-button'

var g = global;
g.g = g;

var globalComps = {React, View, Text, TextInput, Switch, ScrollView, TouchableHighlight};
//g.Extend(globalComps);
for (let key in globalComps)
	g[key] = globalComps[key];

g.Bind = Bind;

// component.js
import EStyleSheet from 'react-native-extended-stylesheet';
// calculate styles
EStyleSheet.build();

g.BaseComponent = class BaseComponent extends Component {
	constructor(props) {
		super(props);
		this.state = {};
	}
}

g.Row = class Row extends BaseComponent {
	render() {
		var {style, height, children} = this.props;
		return (
			<View style={E({flexDirection: "row", padding: 3}, style,
					height != null ? {height} : {flex: 1})}>
				{children}
			</View>
		);
	}
}
g.RowLR = class RowLR extends BaseComponent {
    render() {
		var {height, leftStyle, rightStyle, children} = this.props;
        Assert(children.length == 2, "Row child-count must be 2. (one for left-side, one for right-side)");
        return (
			<View style={E({flexDirection: "row", padding: 3}, height != null ? {height} : {flex: 1})}>
				<View style={E({alignItems: "flex-start", flex: 1}, leftStyle)}>
					{children[0]}
				</View>
				<View style={E({alignItems: "flex-end", flex: 1}, rightStyle)}>
					{children[1]}
				</View>
			</View>
        );
    }
}

g.VButton = class VButton extends BaseComponent {
	static defaultProps = {caps: true, enabled: true};
	render() {
		var {text, caps, style, textStyle, enabled} = this.props;
		var restProps = this.props.RemovingProps("text", "style");

		if (caps)
			text = text.toUpperCase();

		var baseStyle = {borderColor: "#777", backgroundColor: "#777", borderRadius: 3};
		return (
			<Button {...restProps} isDisabled={!enabled}
					style={E(baseStyle, style)}
					textStyle={E({color: "#FFF", fontWeight: "bold", fontSize: 15}, textStyle)}>
				{text}
			</Button>
		);
	}
}
import React, {Component} from "react";
import {NativeModules} from "react-native";
//import {View, Button} from "react-native";
import {Dimensions, StyleSheet,
	View, Text, Switch, TextInput, ScrollView} from "react-native";
import RNFS from "react-native-fs";

import Button from 'apsl-react-native-button'

var g = global;
g.g = g;

g.React = React;
g.View = View;
g.Text = Text;
g.TextInput = TextInput;
g.Switch = Switch;
g.ScrollView = ScrollView;

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
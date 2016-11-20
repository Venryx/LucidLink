import React, {Component} from "react";
import {NativeModules} from "react-native";
//import {View, Button} from "react-native";
import {View} from "react-native";
import RNFS from "react-native-fs";

import Button from 'apsl-react-native-button'

var g = global;
g.g = g;

//g.View = View;

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
	static defaultProps = {caps: true};
	render() {
		var {text, caps, style, textStyle} = this.props;
		var restProps = this.props.RemovingProps("text", "style");

		if (caps)
			text = text.toUpperCase();

		var baseStyle = {borderColor: "#777", backgroundColor: "#777", borderRadius: 3};
		return (
			<Button {...restProps}
					style={E(baseStyle, style)}
					textStyle={E({color: "#FFF", fontWeight: "bold", fontSize: 15}, textStyle)}>
				{text}
			</Button>
		);
	}
}
import React, {Component} from "react";
import {NativeModules} from "react-native";
//import {View, Button} from "react-native";
import {View} from "react-native";
import RNFS from "react-native-fs";

import Button from 'apsl-react-native-button'

var g = global;
g.g = g;

//g.View = View;

g.BaseComponent = class BaseComponent extends Component {
	constructor(props) {
		super(props);
		this.state = {};
	}
}

g.VButton = class VButton extends BaseComponent {
	render() {
		var {style, innerStyle, height} = this.props;
		var restProps = this.props.RemovingProps("style", "height");
		return (
			<View style={E(style, height != null && {height})}>
				<Button {...restProps} style={innerStyle}/>
			</View>
		);
	}
}
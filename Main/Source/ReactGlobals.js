import React, {Component} from "react";
import {NativeModules} from "react-native";
import {View, Button} from "react-native";
import RNFS from "react-native-fs";

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
		var {style} = this.props;
		var restProps = this.props.RemovingProps("style");
		return (
			<View style={{marginLeft: style.marginLeft}}>
				<Button {...restProps}/>
			</View>
		);
	}
}
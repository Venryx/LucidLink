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

var globalComps = {React, View, Text, TextInput, Switch, ScrollView, TouchableHighlight, Dimensions, StyleSheet};
for (let key in globalComps)
	g[key] = globalComps[key];

g.Bind = Bind;

function AddPropModifierFunc(type, modifierFunc) {
	var proto = type.prototype;
	if (proto.componentWillMount_orig) return; // was already wrapped

	proto.componentWillMount_orig = proto.componentWillMount_orig || proto.componentWillMount;
	proto.componentWillMount = function(...args) {
		proto.componentWillMount_orig.apply(this, args);
		modifierFunc.call(this, this.props);
	}

	proto.componentWillReceiveProps_orig = proto.componentWillReceiveProps_orig || proto.componentWillReceiveProps;
	proto.componentWillReceiveProps = function(...args) {
		proto.componentWillReceiveProps_orig.apply(this, args);
		modifierFunc.call(this, args[0]);
	}
}
function AddEarlyStyle(type, style) {
	AddPropModifierFunc(type, function(props) {
		props.style = E(style, props.style);
	});
}

AddEarlyStyle(Text, {color: colors.text});
AddEarlyStyle(TextInput, {color: colors.text});

// component.js
import EStyleSheet from "react-native-extended-stylesheet";
// calculate styles
EStyleSheet.build();

g.BaseComponent = class BaseComponent extends Component {
	constructor(props) {
		super(props);
		this.state = {};
	}

	get FlattenedChildren() {
	    var children = this.props.children;
	    if (!(children instanceof Array))
	        children = [children];
	    return React.Children.map(children.Where(a=>a), a=>a);
	}

	changeListeners = [];
	AddChangeListeners(host, ...funcs) {
	    /*host.extraMethods = funcs;
	    for (let func of funcs)
			this.changeListeners.push({host: host, func: func});*/
	    for (let func of funcs) {
			//if (!host.HasExtraMethod(func)) {
			host.extraMethod = func;
			this.changeListeners.push({host: host, func: func});
		}
	}
	RemoveChangeListeners() {
	    for (let changeListener of this.changeListeners)
	        changeListener.host.removeExtraMethod = changeListener.func;
	    this.changeListeners = [];
	}
	RemoveChangeListenersFor(host) {
	    var changeListenersToRemove = this.changeListeners.Where(a=>a.host == host);
	    for (let changeListener of changeListenersToRemove)
			changeListener.host.removeExtraMethod = changeListener.func;
	    this.changeListeners.RemoveAll(changeListenersToRemove);
	}

	autoRemoveChangeListeners = true;
	componentWillMount() {
		if (this.autoRemoveChangeListeners)
			this.RemoveChangeListeners();
		this.ComponentWillMount && this.ComponentWillMount(); 
	    this.ComponentWillMountOrReceiveProps && this.ComponentWillMountOrReceiveProps(this.props, true); 
	}
	componentDidMount(...args) {
	    this.ComponentDidMount && this.ComponentDidMount(...args);
		this.ComponentDidMountOrUpdate && this.ComponentDidMountOrUpdate(true);
		if (this.PostRender) {
			WaitXThenRun(0, ()=>window.requestAnimationFrame(()=> {
				//if (!this.IsMounted()) return;
			    this.PostRender(true);
			}));
			/*WaitXThenRun(0, ()=> {
				this.PostRender(true);
			});*/
		}
	}
	componentWillReceiveProps(newProps) {
		if (this.autoRemoveChangeListeners)
			this.RemoveChangeListeners();
		this.ComponentWillReceiveProps && this.ComponentWillReceiveProps(newProps);
	    this.ComponentWillMountOrReceiveProps && this.ComponentWillMountOrReceiveProps(newProps, false);
	}
	componentDidUpdate(...args) {
	    this.ComponentDidUpdate && this.ComponentDidUpdate(...args);
		this.ComponentDidMountOrUpdate && this.ComponentDidMountOrUpdate(false);
		if (this.PostRender) {
			WaitXThenRun(0, ()=>window.requestAnimationFrame(()=> {
			    //if (!this.IsMounted()) return;
			    this.PostRender(false);
			}));
			/*WaitXThenRun(0, ()=> {
				this.PostRender(false);
			});*/
		}
	}

	// maybe temp
	/*IsMounted() {
	    return ReactInstanceMap.get(this) != null;
	}*/
}

g.Row = class Row extends BaseComponent {
	render() {
		var {style, height, children} = this.props;
		height = height != null ? height : (style||{}).height;
		return (
			<Panel style={E({flexDirection: "row", padding: 3}, style,
					height != null ? {height} : {flex: 1})}>
				{children}
			</Panel>
		);
	}
}
g.RowLR = class RowLR extends BaseComponent {
    render() {
		var {height, leftStyle, rightStyle, children} = this.props;
        Assert(children.length == 2, "Row child-count must be 2. (one for left-side, one for right-side)");
        return (
			<Panel style={E({flexDirection: "row", padding: 3}, height != null ? {height} : {flex: 1})}>
				<Panel style={E({alignItems: "flex-start", flex: 1}, leftStyle)}>
					{children[0]}
				</Panel>
				<Panel style={E({alignItems: "flex-end", flex: 1}, rightStyle)}>
					{children[1]}
				</Panel>
			</Panel>
        );
    }
}

g.Column = class Column extends BaseComponent {
	render() {
		var {style, width, children} = this.props;
		return (
			<Panel style={E({flexDirection: "column"}, style, width != null ? {width} : {flex: 1})}>
				{children}
			</Panel>
		);
	}
}

g.Panel = class Panel extends View {
	render() {
		var {children, style} = this.props;
		var restProps = this.props.Excluding("style", "children");
		return (
			<View {...{restProps}} style={E({backgroundColor: "transparent"}, style)}>
				{children}
			</View>
		);
	}
}

function BasicStyles(props) {
	var result = {};
	for (let key in props) {
		if (key.startsWith("ml"))
			result.marginLeft = parseInt(key.substr(2));
		else if (key.startsWith("mt"))
			result.marginTop = parseInt(key.substr(2)); 
	}
	return result;
}

g.VButton = class VButton extends BaseComponent {
	static defaultProps = {caps: true, enabled: true};
	render() {
		var {text, caps, style, textStyle, enabled} = this.props;
		var restProps = this.props.Excluding("text", "style");

		if (caps)
			text = text.toUpperCase();
		
		return (
			<Button {...restProps} isDisabled={!enabled}
					style={E(
						{borderColor: "#777", backgroundColor: "#777", borderRadius: 3},
						BasicStyles(this.props),
						style
					)}
					textStyle={E({color: "#FFF", fontWeight: "bold", fontSize: 15}, textStyle)}>
				{text}
			</Button>
		);
	}
}

g.VSwitch = class VSwitch extends BaseComponent {
	render() {
		var {text, style} = this.props;
		var restProps = this.props.Excluding("text", "style");
		return (
			<View style={E({flexDirection: "row"}, BasicStyles(this.props))}>
				<Text style={{marginLeft: 5, height: 50, top: 12, textAlignVertical: "top"}}>{text}</Text>
				<Switch {...restProps}
					style={E(
						{height: 50, top: 0, transform: [{translateY: -3}]},
						style
					)}/>
			</View>
		);
	}
}
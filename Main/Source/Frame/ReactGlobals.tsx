import * as React from "react";
import {Component} from "react";
import {Assert, E, WaitXThenRun} from "./Globals";
import {colors, styles} from "./Styles";
import {Observer, observer} from "mobx-react/native";
//import {View, Button} from "react-native";
import {Dimensions, StyleSheet,
	View, Text, Switch, TextInput, ScrollView, TouchableOpacity, TouchableHighlight,
	DatePickerAndroid, TimePickerAndroid} from "react-native";
import RNFS from "react-native-fs";
//import autobind from "react-autobind"; // caused error in Babel transpiler

import Button from "apsl-react-native-button"

var globalComps = {React, View, Text, TextInput, Switch, ScrollView, TouchableOpacity, TouchableHighlight, Dimensions, StyleSheet,
	DatePickerAndroid, TimePickerAndroid};
var g: any = global;
for (let key in globalComps)
	g[key] = globalComps[key];

// maybe temp: re-export some external-library stuff, since auto-importer fails for some
//export {TextInput} from "react";

function AddPropModifierFunc(type, modifierFunc) {
	var proto = type.prototype;
	if (proto.componentWillMount_orig) return; // was already wrapped

	proto.componentWillMount_orig = proto.componentWillMount_orig || proto.componentWillMount;
	proto.componentWillMount = function(...args) {
		if (proto.componentWillMount_orig) // needed in non-debug mode, fsr
			proto.componentWillMount_orig.apply(this, args);
		modifierFunc.call(this, this.props);
	}

	proto.componentWillReceiveProps_orig = proto.componentWillReceiveProps_orig || proto.componentWillReceiveProps;
	proto.componentWillReceiveProps = function(...args) {
		if (proto.componentWillReceiveProps_orig) // needed in non-debug mode, fsr
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
import Bind from "autobind-decorator";
// calculate styles
EStyleSheet.build();

import autoBind from "react-autobind";

/*export class BaseComponent<P, S> extends Component<P, S> {
}*/

export class BaseComponent<P, S> extends Component<P, S> {
	constructor(props) {
		super(props);
		autoBind(this);
		this.state = this.state || {} as any;
	}

	get FlattenedChildren() {
	    var children = this.props.children;
	    if (!(children instanceof Array))
	        children = [children];
	    return React.Children.map((children as any).Where(a=>a), a=>a);
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
	ComponentWillMount(): void {};
	ComponentWillMountOrReceiveProps(props: any, forMount: boolean): void {};
	componentWillMount() {
		if (this.autoRemoveChangeListeners)
			this.RemoveChangeListeners();
		this.ComponentWillMount && this.ComponentWillMount(); 
	    this.ComponentWillMountOrReceiveProps && this.ComponentWillMountOrReceiveProps(this.props, true); 
	}
	ComponentDidMount(...args: any[]): void {};
	ComponentDidMountOrUpdate(forMount: boolean): void {};
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
	ComponentWillReceiveProps(newProps: any[]): void {};
	componentWillReceiveProps(newProps) {
		if (this.autoRemoveChangeListeners)
			this.RemoveChangeListeners();
		this.ComponentWillReceiveProps && this.ComponentWillReceiveProps(newProps);
	    this.ComponentWillMountOrReceiveProps && this.ComponentWillMountOrReceiveProps(newProps, false);
	}
	ComponentDidUpdate(...args: any[]): void {};
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

	PostRender(initialMount: boolean): void {};

	// maybe temp
	/*IsMounted() {
	    return ReactInstanceMap.get(this) != null;
	}*/
}
//global.Extend({Component2: Component, BaseComponent: Component});

function BasicStyles(props) {
	var result: any = {};
	for (let key in props) {
		if (key.startsWith("ml"))
			result.marginLeft = (key.startsWith("mlN") ? -1 : 1) * parseInt(key.substr(2));
		else if (key.startsWith("mr"))
			result.marginRight = (key.startsWith("mrN") ? -1 : 1) * parseInt(key.substr(2));
		else if (key.startsWith("mt"))
			result.marginTop = (key.startsWith("mtN") ? -1 : 1) * parseInt(key.substr(2));
		else if (key.startsWith("mb"))
			result.marginBottom = (key.startsWith("mbN") ? -1 : 1) * parseInt(key.substr(2));
	}
	return result;
}

export class Row extends BaseComponent<any, any> {
	render() {
		var {style, height, children} = this.props;
		height = height != null ? height : (style||{}).height;
		var otherProps = this.props.Excluding(style, height, children);
		return (
			<Panel {...otherProps} style={E({flexDirection: "row", padding: 3}, BasicStyles(this.props), style,
					//height != null ? {height} : {flex: 1})}>
					height != null && {height})}>
				{children}
			</Panel>
		);
	}
}
export class RowLR extends BaseComponent<any, any> {
    render() {
		var {height, leftStyle, rightStyle, children} = this.props;
        Assert(children.length == 2, "Row child-count must be 2. (one for left-side, one for right-side)");
        return (
			<Panel style={E({flexDirection: "row", padding: 3}, BasicStyles(this.props), height != null && {height})}>
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

export class Column extends BaseComponent<any, any> {
	render() {
		var {style, width, children} = this.props;
		var otherProps = this.props.Excluding(style, width, children);
		return (
			<Panel {...otherProps} style={E({flexDirection: "column"}, BasicStyles(this.props), style, width != null ? {width} : {flex: 1})}>
				{children}
			</Panel>
		);
	}
}

var View2: React.ComponentClass<any> = View;
export class Panel extends View2 {
	render() {
		var {children, style} = this.props;
		var otherProps = this.props.Excluding("style", "children");
		return (
			<View {...otherProps} style={E({backgroundColor: "transparent"}, BasicStyles(this.props), style)}>
				{children}
			</View>
		);
	}
}

export class VText extends BaseComponent<any, any> {
	render() {
		var {style, children} = this.props;
		var otherProps = this.props;
		return (
			<Text {...otherProps}
					style={E(
						{},
						BasicStyles(this.props),
						style
					)}>
				{children}
			</Text>
		);
	}
}

export class VButton extends BaseComponent<any, any> {
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

@observer
export class VSwitch extends BaseComponent<any, any> {
	render() {
		var {text, value, onValueChange, valuePath, style} = this.props;
		var restProps = this.props.Excluding("text", "style");
		if (valuePath) {
			let obj = valuePath[0];
			let prop = valuePath[1];
			value = obj[prop];
			onValueChange = newVal=>obj[prop] = newVal;
		}
		var View2: any = View; // fix for issue in VSwitch
		return (
			<View2 style={E({flexDirection: "row"}, BasicStyles(this.props))}>
				<Text style={{marginLeft: 5, height: 50, top: 12, textAlignVertical: "top"}}>{text}</Text>
				<Switch {...restProps} {...{value, onValueChange}}
					style={E(
						{height: 50, top: 0, transform: [{translateY: -3}]},
						style
					)}/>
			</View2>
		);
	}
}

export class AutoExpandingTextInput extends BaseComponent<any, any> {
	constructor(props) {
  		super(props);
		var {defaultValue, height} = props;
		this.state = {text: defaultValue, height};
	}
	render() {
		var {style} = this.props;
		return (
			<TextInput {...this.props} multiline={true}
				style={E(styles.default, {height: Math.max(35, this.state.height)}, style)}
				value={this.state.text}
				onChange={event=> {
					this.setState({
						text: event.nativeEvent.text,
						height: (event.nativeEvent as any).contentSize.height,
					});
				}}
			/>
		);
	}
}

export class VTextInput extends BaseComponent<any, any> {
	static defaultProps = {editable: true};
	render() {
		var {text} = this.props;
		return <TextInput {...this.props}
			style={{flex: 1, textAlignVertical: "top", color: colors.text}}
			multiline={true} value={text} autoCapitalize="none" autoCorrect={false}/>;
	}
}
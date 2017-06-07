import * as React from "react";
import {Component} from "react";
import {E, Toast} from "./Globals";
import {colors, styles} from "./Styles";
import {Observer, observer} from "mobx-react/native";
//import {View, Button} from "react-native";
import {Dimensions, StyleSheet,
	View, Text, Switch, TextInput, ScrollView, TouchableOpacity, TouchableHighlight,
	DatePickerAndroid, TimePickerAndroid} from "react-native";
import RNFS from "react-native-fs";
//import autobind from "react-autobind"; // caused error in Babel transpiler
import ShallowCompare from "react-addons-shallow-compare";
import Button from "apsl-react-native-button"
import EStyleSheet from "react-native-extended-stylesheet";
import Bind from "autobind-decorator";
import autoBind from "react-autobind";
import {Assert} from "./General/Assert";
import {WaitXThenRun, WaitXThenRun_BuiltIn, Timer} from "./General/Timers";
import {ViewProperties, TouchableOpacityProperties, ButtonProperties, TextProperties, TextInputProperties} from "react-native";
import {IsString} from "./Types";

var g: any = global;
g.Extend({React, View, Text, TextInput, Switch, ScrollView, TouchableOpacity, TouchableHighlight, Dimensions, StyleSheet,
	DatePickerAndroid, TimePickerAndroid});

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
// calculate styles
EStyleSheet.build();

/*export class BaseComponent<P, S> extends Component<P, S> {
}*/

export class BaseComponent<P, S> extends Component<P & BaseProps, S> {
	constructor(props) {
		super(props);
		autoBind(this);
		this.state = this.state || {} as any;
	}

	timers = [] as Timer[];

	get FlattenedChildren() {
	    var children = this.props.children;
	    if (!(children instanceof Array))
	        children = [children];
	    return React.Children.map((children as any).Where(a=>a), a=>a);
	}

	/** safe force-update;*/
	Update(postUpdate?) {
		//if (!this.Mounted) return;
		this.forceUpdate(postUpdate);
	}
	Clear(postClear?) {
		var oldRender = this.render;
		this.render = function() {
			this.render = oldRender;
			//WaitXThenRun(0, this.Update);
			WaitXThenRun(0, ()=>this.Update());
			return <div/>;
		};
		postClear();
	}
	ClearThenUpdate() {
		//this.Clear(this.Update);
		this.Clear(()=>this.Update());
	}
	/** Shortcut for "()=>(this.forceUpdate(), this.ComponentWillMountOrReceiveProps(props))". */
	UpdateAndReceive(props) {
		return ()=> {
			//if (!this.Mounted) return;
			this.forceUpdate();
			if (this.autoRemoveChangeListeners)
				this.RemoveChangeListeners();
			this.ComponentWillMountOrReceiveProps(props)
		};
	}

	changeListeners = [];
	AddChangeListeners(host, ...funcs) {
		if (host == null) return; // maybe temp

	    /*host.extraMethods = funcs;
	    for (let func of funcs)
			this.changeListeners.push({host: host, func: func});*/
	    for (let func of funcs) {
			if (IsString(func))
				func = (func as any).Func(this.Update);
			// if actual function, add it (else, ignore entry--it must have been a failed conditional)
			if (func instanceof Function) {
				//if (!host.HasExtraMethod(func)) {
				host.extraMethod = func;
				this.changeListeners.push({host: host, func: func});
			}
		}
	}
	RemoveChangeListeners() {
		//this.changeListeners = this.changeListeners || []; // temp fix for odd "is null" issue
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
	ComponentWillMountOrReceiveProps(props: any, forMount?: boolean): void {};
	private componentWillMount() {
		if (this.autoRemoveChangeListeners)
			this.RemoveChangeListeners();
		this.ComponentWillMount(); 
	    this.ComponentWillMountOrReceiveProps(this.props, true); 
	}
	ComponentDidMount(...args: any[]): void {};
	ComponentDidMountOrUpdate(forMount: boolean): void {};
	mounted = false;
	private componentDidMount(...args) {
		this.ComponentDidMount(...args);
		this.ComponentDidMountOrUpdate(true);
		this.mounted = true;
		if (this.PostRender) {
			WaitXThenRun_BuiltIn(0, ()=>window.requestAnimationFrame(()=> {
				if (!this.mounted) return;
			    this.PostRender(true);
			}));
			/*WaitXThenRun(0, ()=> {
				this.PostRender(true);
			});*/
		}
	}
	private componentWillUnmount() {
		for (let timer of this.timers)
			timer.Stop();
		this.timers = [];
		this.mounted = false;
	}
	
	ComponentWillReceiveProps(newProps: any[]): void {};
	private componentWillReceiveProps(newProps) {
		if (this.autoRemoveChangeListeners)
			this.RemoveChangeListeners();
		this.ComponentWillReceiveProps(newProps);
	    this.ComponentWillMountOrReceiveProps(newProps, false);
	}
	ComponentDidUpdate(...args: any[]): void {};
	private componentDidUpdate(...args) {
	    this.ComponentDidUpdate(...args);
		this.ComponentDidMountOrUpdate(false);
		if (this.PostRender) {
			WaitXThenRun_BuiltIn(0, ()=>window.requestAnimationFrame(()=> {
			    if (!this.mounted) return;
			    this.PostRender(false);
			}));
			/*WaitXThenRun(0, ()=> {
				this.PostRender(false);
			});*/
		}
	}

	PostRender(initialMount: boolean): void {};

	// maybe temp
	/*get Mounted() {
	    return ReactInstanceMap.get(this) != null;
	}*/
}
//global.Extend({Component2: Component, BaseComponent: Component});

export function SimpleShouldUpdate(target) {
	target.prototype.shouldComponentUpdate = function(newProps, newState) {
	    return ShallowCompare(this, newProps, newState);
		/*var result = ShallowCompare(this, newProps, newState);
		g.Log(result + ";" + g.ToJSON(this.props) + ";" + g.ToJSON(newProps));
		return result;*/
	}
}

export interface BaseProps {
	ml?; mr?; mt?; mb?;
	pl?; pr?; pt?; pb?;
	plr?; ptb?;

	tabLabel?: string; active?: boolean;
}
export function BasicStyles(props) {
	var result: any = {};

	// old way
	/*for (let key in props) {
		if (key.startsWith("ml"))
			result.marginLeft = (key.startsWith("mlN") ? -1 : 1) * parseInt(key.substr(2));
		else if (key.startsWith("mr"))
			result.marginRight = (key.startsWith("mrN") ? -1 : 1) * parseInt(key.substr(2));
		else if (key.startsWith("mt"))
			result.marginTop = (key.startsWith("mtN") ? -1 : 1) * parseInt(key.substr(2));
		else if (key.startsWith("mb"))
			result.marginBottom = (key.startsWith("mbN") ? -1 : 1) * parseInt(key.substr(2));
		else if (key.startsWith("pl"))
			result.paddingLeft = (key.startsWith("plN") ? -1 : 1) * parseInt(key.substr(2));
		else if (key.startsWith("pr"))
			result.paddingRight = (key.startsWith("prN") ? -1 : 1) * parseInt(key.substr(2));
		else if (key.startsWith("pt"))
			result.paddingTop = (key.startsWith("ptN") ? -1 : 1) * parseInt(key.substr(2));
		else if (key.startsWith("pb"))
			result.paddingBottom = (key.startsWith("pbN") ? -1 : 1) * parseInt(key.substr(2));
	}*/

	var fullKeys = {
		ml: "marginLeft", mr: "marginRight", mt: "marginTop", mb: "marginBottom",
		pl: "paddingLeft", pr: "paddingRight", pt: "paddingTop", pb: "paddingBottom",
	};
	for (let key in props) {
		if (key in fullKeys) {
			let fullKey = fullKeys[key];
			result[fullKey] = props[key];
		} else if (key == "plr") {
			result.paddingLeft = props[key];
			result.paddingRight = props[key];
		} else if (key == "ptb") {
			result.paddingTop = props[key];
			result.paddingBottom = props[key];
		}
	}

	return result;
}

// for some reason, errors here if we extend BaseComponent (see: http://stackoverflow.com/questions/31741705)
// (I guess cause it renders just a View)
/*var View2: React.ComponentClass<any> = View;
export class Panel extends View {*/

//type PanelProps = {touchable?, style?} & ViewProperties;
type PanelProps = {touchable?, style?} & Partial<ViewProperties> & Partial<TouchableOpacityProperties> & {fixer?};
export class Panel extends BaseComponent<PanelProps, {}> {
	setNativeProps(nativeProps) {
		this.root.setNativeProps(nativeProps);
	}
	root;
	render() {
		var {touchable, style, children, ...rest} = this.props;
		if (touchable) {
			return (
				<TouchableOpacity {...rest as any} ref={c=>this.root = c}
						style={E({backgroundColor: "transparent"}, BasicStyles(this.props), style)}>
					{children}
				</TouchableOpacity>
			);
		}
		return (
			<View {...rest as any} ref={c=>this.root = c}
					style={E({backgroundColor: "transparent"}, BasicStyles(this.props), style)}>
				{children}
			</View>
		);
	}
}

export class Row extends BaseComponent<{height?, style?} & PanelProps, {}> {
	render() {
		var {height, style, children, ...rest} = this.props as any;
		height = height != null ? height : (style||{}).height;
		return (
			<Panel {...rest} style={E({flexDirection: "row"}, BasicStyles(this.props), style,
					//height != null ? {height} : {flex: 1})}>
					height != null && {height})}>
				{children}
			</Panel>
		);
	}
}
export class RowLR extends BaseComponent<{height?, leftStyle?, rightStyle?} & PanelProps, {}> {
    render() {
		var {height, leftStyle, rightStyle, children, ...rest} = this.props as any;
        Assert(children.length == 2, "Row child-count must be 2. (one for left-side, one for right-side)");
        return (
			<Panel {...rest} style={E({flexDirection: "row"}, BasicStyles(this.props), height != null && {height})}>
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

export class Column extends BaseComponent<{width?, style?} & PanelProps, {}> {
	render() {
		var {width, style, children, ...rest} = this.props as any;
		return (
			<Panel {...rest} style={E({flexDirection: "column"}, BasicStyles(this.props), style, width != null ? {width} : {flex: 1})}>
				{children}
			</Panel>
		);
	}
}

export class VButton extends BaseComponent
		<{text, caps?, style?, textStyle?, enabled?, title?} & Partial<ButtonProperties>, {}> {
	static defaultProps = {caps: true, enabled: true};
	render() {
		var {text, caps, style, textStyle, enabled, ...rest} = this.props;
		if (caps)
			text = text.toUpperCase();
		
		return (
			<Button {...rest} isDisabled={!enabled}
					style={E(
						{borderColor: "#777", backgroundColor: "#777", borderRadius: 3},
						(style || {}).width == null && {paddingLeft: 10, paddingRight: 10},
						BasicStyles(this.props),
						style
					)}
					textStyle={E({color: "#FFF", fontWeight: "bold", fontSize: 15}, textStyle)}>
				{text}
			</Button>
		);
	}
}

/*export class AutoExpandingTextInput extends BaseComponent<{style?} & TextInputProperties, {height, text}> {
	constructor(props) {
  		super(props);
		var {defaultValue, height} = props;
		this.state = {text: defaultValue, height};
	}
	render() {
		var {style, ...rest} = this.props;
		return (
			<TextInput {...rest as any} multiline={true}
				style={E({height: Math.max(35, this.state.height)}, style)}
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
}*/

//export type AutoProps = {path: ()=>any};
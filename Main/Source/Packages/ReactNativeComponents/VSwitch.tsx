import {BaseComponent as Component, BasicStyles} from "../../Frame/ReactGlobals";
import {observer} from "mobx-react/native";
import {View, Text, Switch} from "react-native";
import {E} from "../../Frame/Globals";
import NumberPicker from "../ReactNativeComponents/NumberPicker";

@observer
export class VSwitch extends Component<any, {}> {
	render() {
		var {text, value, onValueChange, valuePath, style, ...rest} = this.props;
		if (valuePath) {
			let obj = valuePath[0];
			let prop = valuePath[1];
			value = obj[prop];
			onValueChange = newVal=>obj[prop] = newVal;
		}
		return (
			<View {...{} as any} style={E({flexDirection: "row"}, BasicStyles(this.props))}>
				<Text style={{marginLeft: 5, height: 50, top: 12, textAlignVertical: "top"}}>{text}</Text>
				<Switch {...rest} {...{value, onValueChange}}
					style={E(
						{height: 50, top: 0, transform: [{translateY: -3}]},
						style
					)}/>
			</View>
		);
	}
}

@observer
export class VSwitch_Auto extends Component<any, {}> {
	render() {
		var {onChange, path, ...rest} = this.props;
		let {node, key: propName} = path();
		return (
			<NumberPicker {...rest} value={node[propName]} onChange={val=> {
				node[propName] = val;
				if (onChange) onChange(val);
			}}/>
		);
	}
}
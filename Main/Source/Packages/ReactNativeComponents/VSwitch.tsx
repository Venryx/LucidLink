import {BaseComponent as Component, BasicStyles, BaseProps, Row} from "../../Frame/ReactGlobals";
import {observer} from "mobx-react/native";
import {View, Text, Switch} from "react-native";
import {E} from "../../Frame/Globals";
import NumberPicker from "../ReactNativeComponents/NumberPicker";
import Node from "../VTree/Node";
import {Assert} from "../../Frame/General/Assert";

@observer
export class VSwitch extends Component<{text?, value?, onChange?, style?, containerStyle?} & BaseProps, {}> {
	render() {
		var {text, value, onChange, style, containerStyle, ...rest} = this.props;

		// temp fix
		containerStyle = containerStyle || {};
		if (this.props.mt)
			containerStyle.transform = [{translateY: this.props.mt}];

		return (
			<Row style={E(BasicStyles(this.props), containerStyle)}>
				{text && <Text style={{marginLeft: 5, height: 20}}>{text}</Text>}
				<Switch {...rest} {...{value, onValueChange: onChange}}
					style={E(
						{height: 20, marginBottom: 3},
						style
					)}/>
			</Row>
		);
	}
}

@observer
export class VSwitch_Auto extends Component<
		{text?, value?, onChange?, style?, containerStyle?,
			path: ()=>any} & BaseProps, {}> {
	render() {
		var {onChange, path, ...rest} = this.props;
		let {node, key: propName} = path();
		Assert(node instanceof Node, `Prop "node" must be of type Node.`);
		return (
			<VSwitch {...rest} value={node[propName]} onChange={val=> {
				node[propName] = val;
				if (onChange) onChange(val);
			}}/>
		);
	}
}
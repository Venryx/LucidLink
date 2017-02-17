import {BaseComponent} from "../../Frame/ReactGlobals";
import {Component} from "react";
import {observer} from "mobx-react/native";
import {TextInput} from "react-native";
import {E} from "../../Frame/Globals";
import {colors} from "../../Frame/Styles";

export class VTextInput extends BaseComponent<
		{value?: string, onChange?: (val: string)=>void,
			accessible?, accessibilityLabel?, // for "@ConvertStartSpacesToTabs" feature
			editable?, style?}, {}> {
	static defaultProps = {editable: true};
	render() {
		var {value, onChange, style, ...rest} = this.props;
		return <TextInput {...rest}
			style={E({flex: 1, textAlignVertical: "top", color: colors.text}, style)}
			multiline={true} value={value} onChangeText={onChange}
			autoCapitalize="none" autoCorrect={false}/>;
	}
}
@observer
export class VTextInput_Auto extends Component<
	{value?: string, onChange?: (text: string)=>void,
		editable?, style?,
		path: ()=>any}, {}> {
	render() {
		var {value, onChange, path, ...rest} = this.props;
		let {node, key: propName} = path();
		return (
			<VTextInput {...rest} value={node[propName]} onChange={val=> {
				node[propName] = val;
				if (onChange) onChange(val);
			}}/>
		);
	}
}
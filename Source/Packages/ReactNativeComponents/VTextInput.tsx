import {BaseComponent} from "../../Frame/ReactGlobals";
import {Component} from "react";
import {observer} from "mobx-react/native";
import {TextInput, TextInputProperties} from "react-native";
import {E} from "../../Frame/Globals";
import {colors} from "../../Frame/Styles";

type VTextInputProps = TextInputProperties;
export class VTextInput extends BaseComponent<VTextInputProps, {}> {
	static defaultProps = {
		editable: true, multiline: true, autoCapitalize: "none", autoCorrect: false,
		blurOnSubmit: false, // temp fix for bug
	};
	selection = {start: 0, end: 0};
	ignoreNext = false;
	render() {
		var {style, ...rest} = this.props;
		return <TextInput {...rest as any} style={E({flex: 1, textAlignVertical: "top", color: colors.text}, style)}
			// temp fix for bug
			onSelectionChange={event=>this.selection = event.nativeEvent.selection}
			/*onKeyPress={key=> {
				if (this.props.value == null) return; // only process value-based ones
				g.Toast("Key:" + key);
				if (key == "Enter") {
					let newText = this.props.value;
					newText = newText.substr(0, this.selection.start) + "\n" + newText.substr(this.selection.end);
					this.props.onChangeText(newText);
					//this.forceUpdate();
				}
			}}*/
			onSubmitEditing={()=> {
				if (this.ignoreNext) {
					this.ignoreNext = false;
					return;
				}
				
				let newText = this.props.value;
				newText = newText.substr(0, this.selection.start) + "\n" + newText.substr(this.selection.end);
				this.ignoreNext = true;
				this.props.onChangeText(newText);
				//this.forceUpdate();
			}}
			/>;
	}
}
@observer
export class VTextInput_Auto extends Component<{path: ()=>any} & VTextInputProps, {}> {
	render() {
		var {value, onChangeText, path, ...rest} = this.props;
		let {node, key: propName} = path();
		return (
			<VTextInput {...rest as any} value={node[propName]} onChangeText={val=> {
				node[propName] = val;
				if (onChangeText) onChangeText(val);
			}}/>
		);
	}
}
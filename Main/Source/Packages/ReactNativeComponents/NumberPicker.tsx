import {BaseComponent as Component, VButton} from "../../Frame/ReactGlobals";
import NumberPickerDialog from "react-native-numberpicker-dialog";
import {E, JavaLog} from "../../Frame/Globals";
import Node from "../VTree/Node";
import {observer} from "mobx-react/native";
import V from "../V/V";

export default class NumberPicker extends Component<
		{min?, max?, step?,
			values?, // alternative to min-max-step
			precision?, format?,
			value?, enabled?, style?,
			dialogTitle?, dialogMessage?,
			onChange: (val: number)=>void, onFocus?}, {}> {
	static defaultProps = {min: 0, max: 1000, step: 1, precision: 0, value: 0, enabled: true};
	render() {
	    var {min, max, step, values, precision, format,
			value, enabled, style, dialogTitle, dialogMessage, onChange, onFocus} = this.props;

		format = format || (val=>val.toFixed(precision));
		if (values == null) {
			values = [];
			//for (let val = min; val <= max; val += step)
			for (let val = min; val.RoundToMultipleOf(step) <= max; val += step)
				values.push(val);
		}
		var names = values.Select(format);
		value = value || values[0];

	    return (
			<VButton text={format(value)}
				style={E({width: 100, height: 32}, style)}
				onPress={async ()=> {
					var id = await NumberPickerDialog.show({
						title: dialogTitle || "",
						message: dialogMessage || "",
						values: names,
						//selectedValueIndex: values.indexOf(value),
						// pre-select current value by text, rather than raw actual-number
						selectedValueIndex: names.indexOf(format(value)),
						positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
					});

					if (id == -1) return;
					let newValue = values[id];
					onChange(newValue);
				}}/>
		);
	}
}

@observer
export class NumberPicker_Auto extends Component<
		{min?, max?, step?,
			values?, // alternative to step-min-max
			precision?, format?,
			value?, enabled?, style?,
			dialogTitle?, dialogMessage?,
			onChange?: (val: number)=>void, onFocus?,
			path: ()=>any}, {}> {
	static defaultProps = NumberPicker.defaultProps;

	/*ComponentWillMountOrReceiveProps(props) {
		var {path} = props;
		let {node, key: propName} = path();
		this.AddChangeListeners(node,
			(propName + "_PostSet" as any).Func(this.Update),
		);
	}*/

	render() {
		var {onChange, path, dialogTitle, ...rest} = this.props;
		let {node, key: propName} = path();

		if (dialogTitle === null || dialogTitle === undefined)
			dialogTitle = V.PropNameToTitle(propName);
		
		return (
			<NumberPicker {...rest} dialogTitle={dialogTitle} value={node[propName]} onChange={val=> {
				//node.a(propName).set = val;
				node[propName] = val;
				if (onChange) onChange(val);
			}}/>
		);
	}
}
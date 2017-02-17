import {Assert} from "../../Frame/General/Assert";
import {BaseComponent as Component, VButton} from "../../Frame/ReactGlobals";
import {Enum} from "../../Frame/General/Enums";
import {IsConstructor} from "../../Frame/Types";
import {observer} from "mobx-react/native";
import NumberPickerDialog from "react-native-numberpicker-dialog";
import {E, ToJSON} from "../../Frame/Globals";

export default class Select extends Component<
		{options: {name: string, value, style?}[] | string[] | (new()=>Enum) | {[s: string]: any},
			compareBy?: "name" | "value" | "value toString",
			value, verifyValue?: boolean,
			className?, title?, style?, childStyle?,
			dialogTitle?, dialogMessage?,			
			onChange}, {}> {
	static defaultProps = {
		compareBy: "value",
		verifyValue: true,
	};

	get OptionsList() {
		let {options: options_raw} = this.props;

		let result = [] as {name: string, value, style?}[];
		if (options_raw instanceof Array) {
			if (options_raw[0] && (options_raw[0] as any).name)
				result = options_raw as any;
			else
				result = (options_raw as string[]).Select(a=>({name: a, value: a}));
		} else if (IsConstructor(options_raw)) {
			Assert(options_raw.prototype instanceof Enum, "Class provided must derive from class 'Enum'.");
			result = (options_raw as any).options;
		} else {
			let optionsMap = options_raw as any;
			for (let {name, value} of optionsMap.Props)
				//result.push(new Option(name, value));
				result.push({name, value});
		}
		return result;
	}
	
	GetIndexOfOption(option) {
		return this.OptionsList.indexOf(option);
	}
	GetIndexOfValue(value) {
		var {compareBy} = this.props;
	    var options = this.OptionsList;
		return options.FindIndex((option: any)=> {
	        if (compareBy == "name")
				return option.name === value;
			if (compareBy == "value")
		    	return option.value === value;
		    return option.value == null ? value == null : option.value.toString() === value.toString();
		});
	}
	
	render() {
	    var {value, verifyValue, className, title, style, childStyle, dialogTitle, dialogMessage, onChange} = this.props;
		var options = this.OptionsList;
		Assert(options.Select(a=>a.name).Distinct().length == options.length, ()=> {
			var optionsWithNonUniqueNames = options.Where(a=>options.VCount(b=>b.name == a.name) > 1);
			return `Select options must have unique names. (shared: ${optionsWithNonUniqueNames.Select(a=>a.name).join(", ")})`;
		});
		let valueValid = this.GetIndexOfValue(value) != -1;
		// if there are no options yet, or value provided is null, don't require match, since this may be a pre-data render
		if (options.length && value != null && verifyValue) {
			Assert((valueValid), `Select's value must match one of the options. @options(${
				options.Select(a=>a.name).join(", ")}) @value(${value})`);
		}

		var names = options.Select(a=>a.name);
		var values = options.Select(a=>a.value);
		var optionIndex = this.GetIndexOfValue(value);
		var option = options[optionIndex];

	    return (
			<VButton text={option.name}
				style={E({width: 100, height: 32}, style)}
				onPress={async ()=> {
					var id = await NumberPickerDialog.show({
						title: dialogTitle,
						message: dialogMessage || "",
						values: names,
						selectedValueIndex: optionIndex,
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
export class Select_Auto extends Component<
		{options: {name: string, value, style?}[] | string[] | (new()=>Enum) | {[s: string]: any},
			compareBy?: "name" | "value" | "value toString",
			className?, title?, style?, childStyle?,
			dialogTitle?, dialogMessage?,
			path: ()=>any, onChange?: (val)=>void}, {}> {
	static defaultProps = Select.defaultProps;

	render() {
		var {path, onChange, ...rest} = this.props;
		let {node, key: propName} = path();
		return (
			<Select {...rest} value={node[propName]} onChange={val=> {
				node[propName] = val;
				if (onChange) onChange(val);
			}}/>
		);
	}
}
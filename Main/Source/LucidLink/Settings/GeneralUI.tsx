import {Range} from "../../Frame/Globals";
import {BaseComponent as Component, Panel, Row, RowLR, VButton, VText, BaseProps} from "../../Frame/ReactGlobals";
import {colors} from "../../Frame/Styles";
import {Observer, observer} from "mobx-react/native";
import {Text, Switch} from "react-native";
import NumberPickerDialog from "react-native-numberpicker-dialog";
import {LL} from "../../LucidLink";
import {NumberPicker_Auto} from "../../Packages/ReactNativeComponents/NumberPicker";
import {VSwitch, VSwitch_Auto} from "../../Packages/ReactNativeComponents/VSwitch";
import Select from "../../Packages/ReactNativeComponents/Select";
import {Gender} from "../Settings";
import {Select_Auto} from "../../Packages/ReactNativeComponents/Select";

@observer
export default class GeneralUI extends Component<{} & BaseProps, {}> { 
	render() {
		var node = LL.settings;
		return (
			<Panel style={{flex: 1, backgroundColor: colors.background}}>
				<Row style={{flex: 1, flexDirection: "column", padding: 10}}>
					<Row height={30}>
						<VText mt={2} mr={10}>Apply scripts on launch:</VText>
						<VSwitch_Auto path={()=>node.p.applyScriptsOnLaunch}/>
					</Row>
					<Row height={30}>
						<VText mt={2} mr={10}>Block unused keys:</VText>
						<VSwitch_Auto path={()=>node.p.blockUnusedKeys}/>
					</Row>
					<Row height={30}>
						<VText mt={2} mr={10}>Keep device awake:</VText>
						<VSwitch_Auto path={()=>node.p.keepDeviceAwake}/>
					</Row>
					<Row height={35}>
						<VText mt={5} mr={10}>Age:</VText>
						<NumberPicker_Auto path={()=>node.p.age} min={1} max={100} format={val=>val + " years"} dialogTitle="Age"/>
					</Row>
					<Row height={35}>
						<VText mt={5} mr={10}>Gender:</VText>
						<Select_Auto path={()=>node.p.gender} options={Gender} dialogTitle="Gender"/>
					</Row>
				</Row>
            </Panel>
		);
	}
}
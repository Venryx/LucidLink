import {Range} from "../../Frame/Globals";
import {BaseComponent as Component, Panel, Row, RowLR, VButton, VText} from "../../Frame/ReactGlobals";
import {colors} from "../../Frame/Styles";
import {Observer, observer} from "mobx-react/native";
import {Text, Switch} from "react-native";
import NumberPickerDialog from "react-native-numberpicker-dialog";
import {LL} from "../../LucidLink";
import {NumberPicker_Auto} from "../../Packages/ReactNativeComponents/NumberPicker";
import {VSwitch, VSwitch_Auto} from "../../Packages/ReactNativeComponents/VSwitch";

/*@observer
class NumberSettingUI extends Component<{propName: string, text: string, description?: string, values: number[]}, any> {
	static defaultProps = {description: ""};
	render() {
		var {propName, text, description, values} = this.props;
		var node = LL.settings;
		return (
			<Row>
				<VText mt5 mr10>{text}</VText>
				<VButton text={node[propName].toString()} style={{width: 100, height: 32}}
					onPress={async ()=> {
						var id = await NumberPickerDialog.show({
							title: text,
							message: description,
							values: values.Select(a=>a.toString()),
							selectedValueIndex: values.indexOf(node[propName]),
							positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
						});

						if (id == -1) return;
						let val = values[id];
						node[propName] = val;
					}}/>
			</Row>
		)
	}
}*/

@observer
export default class GeneralUI extends Component<any, {}> { 
	render() {
		var node = LL.settings;
		return (
			<Panel style={{flex: 1, backgroundColor: colors.background}}>
				<Row style={{flex: 1, flexDirection: "column", padding: 10}}>
					<Row height={30}>
						<VText mt2 mr10>Apply scripts on launch</VText>
						<VSwitch_Auto path={()=>node.p.applyScriptsOnLaunch}/>
					</Row>
					<Row height={30}>
						<VText mt2 mr10>Block unused keys</VText>
						<VSwitch_Auto path={()=>node.p.blockUnusedKeys}/>
					</Row>
					<Row height={30}>
						<VText mt2 mr10>Keep device awake</VText>
						<VSwitch_Auto path={()=>node.p.keepDeviceAwake}/>
					</Row>
					<Row>
						<VText mt5 mr10>Muse EEG-packet buffer size</VText>
						<NumberPicker_Auto path={()=>node.p.museEEGPacketBufferSize} min={1} max={500}
							dialogTitle="Muse EEG-packet buffer size"
							dialogMessage="Select the number of eeg-packets to buffer before they're sent to the JS."/>
					</Row>
					<Row>
						<VText mt5 mr10>Eye-tracker horizontal-sensitivity</VText>
						<NumberPicker_Auto path={()=>node.p.eyeTracker_horizontalSensitivity} max={1} step={.01}
							format={val=>(val * 100).toFixed(0) + "%"}
							dialogTitle="Eye-tracker horizontal-sensitivity"/>
					</Row>
					<Row>
						<VText mt5 mr10>Eye-tracker vertical-sensitivity</VText>
						<NumberPicker_Auto path={()=>node.p.eyeTracker_verticalSensitivity} max={1} step={.01}
							format={val=>(val * 100).toFixed(0) + "%"}
							dialogTitle="Eye-tracker vertical-sensitivity"/>
					</Row>
					<Row>
						<VText mt5 mr10>Eye-tracker off-screen gravity</VText>
						<NumberPicker_Auto path={()=>node.p.eyeTracker_offScreenGravity} max={1} step={.01} precision={2}
							dialogTitle="Eye-tracker off-screen gravity"/>
					</Row>
					<Row>
						<VText mt5 mr10>Relax vs tense intensity</VText>
						<NumberPicker_Auto path={()=>node.p.eyeTracker_relaxVSTenseIntensity} min={.1} max={1} step={.01} precision={2}
							dialogTitle="Relax vs tense intensity"/>
					</Row>

					<Row>
						<VText mt5 mr10>Eye-trace segment size</VText>
						<NumberPicker_Auto path={()=>node.p.eyeTraceSegmentSize} min={.01} max={.5} step={.01} precision={2}
							dialogTitle="Eye-trace segment count"/>
					</Row>
					<Row>
						<VText mt5 mr10>Eye-trace segment count</VText>
						<NumberPicker_Auto path={()=>node.p.eyeTraceSegmentCount} min={0} max={100} step={10}
							dialogTitle="Eye-trace segment count"/>
					</Row>
					<Row>
						<VText mt5 mr10>Log stats every X minutes</VText>
						<NumberPicker_Auto path={()=>node.p.logStatsEveryXMinutes} min={1} max={100}
							dialogTitle="Log stats every X minutes"/>
					</Row>
					<Row>
						<VText mt5 mr10>Reconnect attempt interval</VText>
						<NumberPicker_Auto path={()=>node.p.reconnectAttemptInterval} values={[-1].concat(Range(1, 100))}
							dialogTitle="Log stats every X minutes"/>
					</Row>
					<Row>
						<VText mt5 mr10>Log stats every X minutes</VText>
						<NumberPicker_Auto path={()=>node.p.sessionSaveInterval} min={1} max={100}
							dialogTitle="Session save interval"/>
					</Row>
				</Row>
            </Panel>
		);
	}
}
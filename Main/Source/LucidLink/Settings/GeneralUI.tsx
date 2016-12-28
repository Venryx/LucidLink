import {Range} from "../../Frame/Globals";
import {BaseComponent as Component, Panel, Row, RowLR, VButton, VText} from "../../Frame/ReactGlobals";
import {colors} from "../../Frame/Styles";
import {Observer, observer} from "mobx-react/native";
import {Text, Switch} from "react-native";
import NumberPickerDialog from "react-native-numberpicker-dialog";
import {LL} from "../../LucidLink";

@observer
class NumberSettingUI extends Component<{propName: string, text: string, description?: string, values: number[]}, any> {
	static defaultProps = {description: ""};
	render() {
		var {propName, text, description, values} = this.props;
		var node = LL.settings;
		return (
			<Row>
				<VText ml10 mt5 mr10>{text}</VText>
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
}

@observer
export default class GeneralUI extends Component<any, any> { 
	render() {
		var node = LL.settings;
		return (
			<Panel style={{flex: 1, backgroundColor: colors.background}}>
				<Row style={{flex: 1, flexDirection: "column"}}>
					<RowLR height={25}>
						<Text>Apply scripts on launch</Text>
						<Switch value={node.applyScriptsOnLaunch}
							onValueChange={value=>{
								node.applyScriptsOnLaunch = value;
							}}/>
					</RowLR>
					<RowLR height={25}>
						<Text>Block unused keys</Text>
						<Switch value={node.blockUnusedKeys}
							onValueChange={value=>{
								node.blockUnusedKeys = value;
							}}/>
					</RowLR>
					<Row>
						<VText ml10 mt5 mr10>Muse EEG-packet buffer size</VText>
						<VButton text={node.museEEGPacketBufferSize.toString()} style={{width: 100, height: 32}}
							onPress={async ()=> {
								var values = [];
								for (let val = 1; val < 500; val++)
									values.push(val);
								var id = await NumberPickerDialog.show({
									title: "Muse eeg-packet buffer size",
									message: "Select the number of eeg-packets to buffer before they're sent to the JS.",
									values: values.Select(a=>a.toString()),
									selectedValueIndex: values.indexOf(node.museEEGPacketBufferSize),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								});

								if (id == -1) return;
								let val = values[id];
								node.museEEGPacketBufferSize = val;
							}}/>
					</Row>
					<Row>
						<VText ml10 mt5 mr10>Eye-tracker horizontal-sensitivity</VText>
						<VButton text={(node.eyeTracker_horizontalSensitivity * 100).toFixed(0)} style={{width: 100, height: 32}}
							onPress={async ()=> {
								var values = [];
								for (let val = 0; val < 1; val += .01)
									values.push(val);
								var id = await NumberPickerDialog.show({
									title: "Eye-tracker horizontal-sensitivity",
									message: "",
									values: values.Select(a=>(a * 100).toFixed(0)),
									selectedValueIndex: values.indexOf(node.eyeTracker_horizontalSensitivity),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								});

								if (id == -1) return;
								let val = values[id];
								node.eyeTracker_horizontalSensitivity = val;
							}}/>
					</Row>
					<Row>
						<VText ml10 mt5 mr10>Eye-tracker vertical-sensitivity</VText>
						<VButton text={(node.eyeTracker_verticalSensitivity * 100).toFixed(0)} style={{width: 100, height: 32}}
							onPress={async ()=> {
								var values = [];
								for (let val = 0; val < 1; val += .01)
									values.push(val);
								var id = await NumberPickerDialog.show({
									title: "Eye-tracker vertical-sensitivity",
									message: "",
									values: values.Select(a=>(a * 100).toFixed(0)),
									selectedValueIndex: values.indexOf(node.eyeTracker_verticalSensitivity),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								});

								if (id == -1) return;
								let val = values[id];
								node.eyeTracker_verticalSensitivity = val;
							}}/>
					</Row>
					<Row>
						<VText ml10 mt5 mr10>Eye-tracker off-screen gravity</VText>
						<VButton text={node.eyeTracker_offScreenGravity.toFixed(2)} style={{width: 100, height: 32}}
							onPress={async ()=> {
								var values = [];
								for (let val = 0; val <= 1; val += .01)
									values.push(val);
								var id = await NumberPickerDialog.show({
									title: "Eye-tracker off-screen gravity",
									message: "",
									values: values.Select(a=>a.toFixed(2)),
									selectedValueIndex: values.indexOf(node.eyeTracker_offScreenGravity),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								});

								if (id == -1) return;
								let val = values[id];
								node.eyeTracker_offScreenGravity = val;
							}}/>
					</Row>
					<Row>
						<VText ml10 mt5 mr10>Relax vs tense intensity</VText>
						<VButton text={node.eyeTracker_relaxVSTenseIntensity.toFixed(2)} style={{width: 100, height: 32}}
							onPress={async ()=> {
								var values = [];
								for (let val = .1; val <= 1; val += .01)
									values.push(val);
								var id = await NumberPickerDialog.show({
									title: "Relax vs tense intensity",
									message: "",
									values: values.Select(a=>a.toFixed(2)),
									selectedValueIndex: values.indexOf(node.eyeTracker_relaxVSTenseIntensity),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								});

								if (id == -1) return;
								let val = values[id];
								node.eyeTracker_relaxVSTenseIntensity = val;
							}}/>
					</Row>

					<Row>
						<VText ml10 mt5 mr10>Eye-trace segment size</VText>
						<VButton text={node.eyeTraceSegmentSize.toFixed(2)} style={{width: 100, height: 32}}
							onPress={async ()=> {
								var values = [];
								for (let val = .01; val <= .5; val += .01)
									values.push(val);
								var id = await NumberPickerDialog.show({
									title: "Eye-trace segment size",
									message: "",
									values: values.Select(a=>a.toFixed(2)),
									selectedValueIndex: values.indexOf(node.eyeTraceSegmentSize),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								});

								if (id == -1) return;
								let val = values[id];
								node.eyeTraceSegmentSize = val;
							}}/>
					</Row>
					<Row>
						<VText ml10 mt5 mr10>Eye-trace segment count</VText>
						<VButton text={node.eyeTraceSegmentCount.toString()} style={{width: 100, height: 32}}
							onPress={async ()=> {
								var values = [];
								for (let val = 0; val <= 1000; val += 10)
									values.push(val);
								var id = await NumberPickerDialog.show({
									title: "Eye-trace segment count",
									message: "",
									values: values.Select(a=>a.toString()),
									selectedValueIndex: values.indexOf(node.eyeTraceSegmentCount),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								});

								if (id == -1) return;
								let val = values[id];
								node.eyeTraceSegmentCount = val;
							}}/>
					</Row>
					<Row>
						<VText ml10 mt5 mr10>Log stats every X minutes</VText>
						<VButton text={node.logStatsEveryXMinutes.toString()} style={{width: 100, height: 32}}
							onPress={async ()=> {
								var values = [];
								for (let val = 1; val <= 100; val++)
									values.push(val);
								var id = await NumberPickerDialog.show({
									title: "Log stats every X minutes",
									message: "",
									values: values.Select(a=>a.toString()),
									selectedValueIndex: values.indexOf(node.logStatsEveryXMinutes),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								});

								if (id == -1) return;
								let val = values[id];
								node.logStatsEveryXMinutes = val;
							}}/>
					</Row>
					<NumberSettingUI propName="reconnectAttemptInterval" text="Reconnect attempt interval" values={[-1].concat(Range(1, 100))}/>
					<NumberSettingUI propName="sessionSaveInterval" text="Session save interval" values={Range(1, 100)}/>
				</Row>
            </Panel>
		);
	}
}
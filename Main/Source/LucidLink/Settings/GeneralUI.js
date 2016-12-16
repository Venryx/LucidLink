import NumberPickerDialog from "react-native-numberpicker-dialog";

@Observer
export default class GeneralUI extends BaseComponent { 
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
						<VText ml10 mt5 mr10>Pattern match interval</VText>
						<VButton text={node.patternMatchInterval.toFixed(1)} style={{width: 100, height: 32}}
							onPress={async ()=> {
								var values = [];
								for (let val = .1; val < 1; val += .1)
									values.push(val);
								for (let val = 1; val <= 10; val += .5)
									values.push(val);
								var id = await NumberPickerDialog.show({
									title: "Pattern match interval",
									message: "Select interval/repeat-time (in seconds) at which to check for eeg-pattern matches.",
									values: values.Select(a=>a.toFixed(1)),
									selectedValueIndex: values.indexOf(node.patternMatchInterval),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								});
								if (id == -1) return;
								let val = values[id];
								node.patternMatchInterval = val;
							}}/>
					</Row>
					<Row>
						<VText ml10 mt5 mr10>Pattern match offset</VText>
						<VButton text={node.patternMatchOffset.toFixed(2)} style={{width: 100, height: 32}}
							onPress={async ()=> {
								var values = [];
								for (let val = .01; val < .1; val += .01)
									values.push(val);
								for (let val = .1; val < 1; val += .1)
									values.push(val);
								for (let val = 1; val <= 10; val += .5)
									values.push(val);
								var id = await NumberPickerDialog.show({
									title: "Pattern match offset",
									message: "Select the offset/range-interval to use, on the x-axis, at which to test for eeg-pattern matches.",
									values: values.Select(a=>a.toFixed(2)),
									selectedValueIndex: values.indexOf(node.patternMatchOffset),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								});

								if (id == -1) return;
								let val = values[id];
								node.patternMatchOffset = val;
							}}/>
					</Row>
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
					{/*<Row>
						<VText ml10 mt5 mr10>Eye-tracker ignore-x-movement-under</VText>
						<VButton text={node.eyeTracker_ignoreXMovementUnder.toFixed(3)} style={{width: 100, height: 32}}
							onPress={async ()=> {
								var values = [];
								for (let val = 0; val < 1; val += .001)
									values.push(val);
								var id = await NumberPickerDialog.show({
									title: "Eye-tracker ignore-x-movement-under",
									message: "",
									values: values.Select(a=>a.toFixed(3)),
									selectedValueIndex: values.indexOf(node.eyeTracker_ignoreXMovementUnder),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								});

								if (id == -1) return;
								let val = values[id];
								node.eyeTracker_ignoreXMovementUnder = val;
							}}/>
					</Row>
					<Row>
						<VText ml10 mt5 mr10>Eye-tracker ignore-y-movement-under</VText>
						<VButton text={node.eyeTracker_ignoreYMovementUnder.toFixed(3)} style={{width: 100, height: 32}}
							onPress={async ()=> {
								var values = [];
								for (let val = 0; val < 1; val += .001)
									values.push(val);
								var id = await NumberPickerDialog.show({
									title: "Eye-tracker ignore-y-movement-under",
									message: "",
									values: values.Select(a=>a.toFixed(3)),
									selectedValueIndex: values.indexOf(node.eyeTracker_ignoreYMovementUnder),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								});

								if (id == -1) return;
								let val = values[id];
								node.eyeTracker_ignoreYMovementUnder = val;
							}}/>
					</Row>*/}

					{/* probably temp */}
					<Row>
						<VText ml10 mt5 mr10>Eye-tracker ignore values within X of baseline</VText>
						<VButton text={(node.eyeTracker_ignoreXMovementUnder * 1000).toFixed(3)} style={{width: 100, height: 32}}
							onPress={async ()=> {
								var values = [];
								for (let val = 0; val < 1; val += .001)
									values.push(val);
								var id = await NumberPickerDialog.show({
									title: "Eye-tracker ignore values within X of baseline",
									message: "",
									values: values.Select(a=>(a * 1000).toFixed(3)),
									selectedValueIndex: values.indexOf(node.eyeTracker_ignoreXMovementUnder),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								});

								if (id == -1) return;
								let val = values[id];
								node.eyeTracker_ignoreXMovementUnder = val;
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


					{/*<Row>
						<Text ml10 mt5 mr10>Eye-tracker vertical-sensitivity</Text>
						<VButton text={node.eyeTracker_verticalSensitivity.toFixed(2)} style={{width: 100, height: 32}}
							onPress={async ()=> {
								var values = [];
								for (let val = 0; val < 100; val++)
									values.push(val);
								var id = await NumberPickerDialog.show({
									title: "Eye-tracker vertical-sensitivity",
									//message: "Select the offset/range-interval to use, on the x-axis, at which to test for eeg-pattern matches.",
									values: values.Select(a=>a.toFixed(2)),
									selectedValueIndex: values.indexOf(node.eyeTracker_verticalSensitivity),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								});

								if (id == -1) return;
								let val = values[id];
								node.eyeTracker_verticalSensitivity = val;
							}}/>
					</Row>*/}
				</Row>
            </Panel>
		);
	}
}
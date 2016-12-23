import {BaseComponent, Column, Panel, Row, VButton, VSwitch, VText} from "../../Frame/ReactGlobals";
import {colors, styles} from "../../Frame/Styles";
import {E, FromVDF, Toast, ToVDF} from "../../Frame/Globals";
import {Actions} from "react-native-router-flux";
import {Observer, observer} from "mobx-react/native";
import {ScrollView, Text, TextInput, Dimensions} from "react-native";
import NumberPickerDialog from "react-native-numberpicker-dialog";
import Chart from "react-native-chart";
var DialogAndroid = require("react-native-dialogs");
import Drawer from "react-native-drawer";

import PatternsPanel from "./PatternsPanel";
import Bind from "autobind-decorator";
import {LL} from "../../LucidLink";

@observer
export default class PatternsUI extends BaseComponent<any, any> {
	_drawer;
	@Bind ToggleSidePanelOpen() {
		if (this._drawer._open)
			this._drawer.close();
		else
			this._drawer.open();
	}

	SelectPattern(pattern) {
		LL.settings.selectedPattern = pattern;
		this._drawer.close();
	}
	
	render() {
		var node = LL.settings;
		var {selectedPattern: pattern} = node;

		return (
			<Drawer ref={comp=>this._drawer = comp}
					content={<PatternsPanel parent={this} patterns={node.patterns}/>}
					type="overlay" openDrawerOffset={0.5} panCloseMask={0.5} tapToClose={true}
					closedDrawerOffset={-3} styles={{
						drawer: {shadowColor: "#000000", shadowOpacity: .8, shadowRadius: 3},
						main: {paddingLeft: 3},
					}}>
				<Column style={{backgroundColor: colors.background}}>
					<Row style={E(styles.header, {flexWrap: "wrap", padding: 3, paddingBottom: -5})}>
						<VButton text="Patterns" style={{width: 100}} onPress={this.ToggleSidePanelOpen}/>
						<Text style={{marginLeft: 10, marginTop: 8, fontSize: 18}}>
						Pattern: {pattern ? pattern.name : "n/a"}
						</Text>
						{pattern &&
							<VButton text="Rename" style={{marginLeft: 10, width: 100}} onPress={()=>pattern.Rename()}/>}
						{pattern &&
							<VSwitch text="Enabled" value={pattern.enabled} onValueChange={value=>pattern.enabled = value}/>}
						<Panel style={{flex: 1}}/>
						{/*<Panel style={{flexDirection: "row", alignItems: "flex-end"}}>
							<VButton color="#777" text="Save" enabled={selectedScript != null && selectedScript.fileOutdated}
								style={{width: 100, marginLeft: 5}}
								onPress={()=>selectedScript.Save().then(()=>this.forceUpdate())}/>
							<VButton color="#777" text="Apply all"
								//enabled={scriptLastRunsOutdated}
								enabled={true}
								style={{width: 100, marginLeft: 5}}
								onPress={()=>node.ApplyScripts()}/>
						</Panel>*/}

						<Text style={{marginLeft: 10, marginTop: 10, marginRight: 10}}>Preview chart value range: </Text>
						<VButton text={node.previewChartRangeX.toString()} style={{marginTop: 5, width: 100, height: 32}}
							onPress={()=> {
								var values = [-1];
								for (let val = 0; val < 100; val += 10)
									values.push(val);
								for (let val = 100; val < 1000; val += 100)
									values.push(val);
								for (let val = 1000; val <= 10000; val += 1000)
									values.push(val);
								NumberPickerDialog.show({
									title: "Preview chart value range - x (horizontal)",
									message: "Select value-range (horizontal) to display in the pattern-previews below.",
									values: values.Select(a=>a.toString()),
									selectedValueIndex: values.indexOf(node.previewChartRangeX),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								}).then(id=> {
									if (id == -1) return;
									let val = values[id];
									node.previewChartRangeX = val;
								});
							}}/>

						<Text style={{marginLeft: 10, marginTop: 10, marginRight: 10}}> by </Text>
						<VButton text={node.previewChartRangeY.toString()} style={{marginTop: 5, width: 100, height: 32}}
							onPress={()=> {
								var values = [-1];
								for (let val = 0; val < 100; val += 10)
									values.push(val);
								for (let val = 100; val < 1000; val += 100)
									values.push(val);
								for (let val = 1000; val <= 10000; val += 1000)
									values.push(val);
								NumberPickerDialog.show({
									title: "Preview chart value range - y (vertical)",
									message: "Select value-range (vertical) to display in the pattern-previews below.",
									values: values.Select(a=>a.toString()),
									selectedValueIndex: values.indexOf(node.previewChartRangeY),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
								}).then(id=> {
									if (id == -1) return;
									let val = values[id];
									node.previewChartRangeY = val;
								});
							}}/>
					</Row>
					{pattern && <PatternUI parent={this} pattern={pattern}/>}
				</Column>
			</Drawer>
		);
	}
}

@observer
@Bind
class PatternUI extends BaseComponent<any, any> {
	render() {
		var {parent, pattern} = this.props;
		var node = LL.settings;

		var pointsForChart = pattern.points.Select(a=>[a.x, a.y]);
		if (pointsForChart.length == 0)
			pointsForChart = [[0, 0]];

		return (
			<ScrollView style={{flex: 1, flexDirection: "column", borderTopWidth: 1, marginTop: -7}}
					automaticallyAdjustContentInsets={false}>
				<Row height={35}>
					<Text style={{marginLeft: 10, marginTop: 5, marginRight: 10}}>Sensitivity</Text>
					<VButton text={pattern.sensitivity.toString()} style={{width: 100, height: 32}}
						onPress={async ()=> {
							var values = [];
							for (let val = 0; val <= 200; val++)
								values.push(val);
							var id = await NumberPickerDialog.show({
								title: "Pattern match sensitivity",
								message: "The 'sensitivity' is the average distance of channel-data points to\
pattern-points that yields a pattern-match certainty of 0.",
								values: values.Select(a=>a.toString()),
								selectedValueIndex: values.indexOf(pattern.sensitivity),
								positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
							});

							if (id == -1) return;
							let val = values[id];
							pattern.sensitivity = val;
						}}/>

					<VText ml10 style={{marginTop: 5, marginRight: 10}}>Channels: </VText>
					<VSwitch text="1" value={pattern.channel1}
						onValueChange={value=>pattern.channel1 = value}/>
					<VSwitch text="2" value={pattern.channel2}
						onValueChange={value=>pattern.channel2 = value}/>
					<VSwitch text="3" value={pattern.channel3}
						onValueChange={value=>pattern.channel3 = value}/>
					<VSwitch text="4" value={pattern.channel4}
						onValueChange={value=>pattern.channel4 = value}/>
				</Row>
				<Row height={35}>
					<VText ml10 style={{marginTop: 5, marginRight: 10}}>Actions: </VText>
					<VButton text="Offset X" ml5 style={{height: 30}} onPress={()=>this.OffsetPoints("x")}/>
					<VButton text="Offset Y" ml5 style={{height: 30}} onPress={()=>this.OffsetPoints("y")}/>
					<VButton text="Clone" ml5 style={{height: 30}} onPress={()=>this.Clone()}/>
					<VButton text="Transform" ml5 style={{height: 30}} onPress={()=>this.Transform()}/>
				</Row>
				<Row height={320}>
					<Chart style={{width: Dimensions.get("window").width - 30, height: 300}}
						minX={-node.previewChartRangeX / 2} maxX={node.previewChartRangeX / 2} legendStepsX={11}
						minY={-node.previewChartRangeY / 2} maxY={node.previewChartRangeY / 2} legendStepsY={5}
						axisColor="#AAA" axisLabelColor="#AAA" gridColor="#777"
						type="line" color={["#e1cd00"]} data={[pointsForChart]}/>
				</Row>
				<Row>
					<VSwitch text="Text editor" value={pattern.textEditor}
						onValueChange={value=>pattern.textEditor = value}/>
				</Row>
				{pattern.textEditor && 
					<TextInput style={{height: 100, paddingTop: 0, paddingBottom: 0}} multiline={true}
						editable={true} defaultValue={ToVDF(pattern.points, false)}
						onChangeText={text=> {
							try {
								pattern.points = FromVDF(text, "List(Vector2i)");
							} catch (ex) {
								Toast("Invalid points JSON");
							}
						}}/>}
			</ScrollView>
		);
	}

	async OffsetPoints(axis) {
		var {pattern} = this.props;

		var values = [];
		for (let val = -1000; val < -100; val += 100) values.push(val);
		for (let val = -100; val < -10; val += 10) values.push(val);
		for (let val = -10; val < 10; val++) values.push(val);
		for (let val = 10; val < 100; val += 10) values.push(val);
		for (let val = 100; val <= 1000; val += 100) values.push(val);
		var id = await NumberPickerDialog.show({
			title: `Offset pattern ${axis}-values`,
			message: `Select amount to offset the ${axis}-values of the pattern's points.`,
			values: values.Select(a=>a.toString()),
			selectedValueIndex: values.indexOf(0),
			positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
		});
		if (id == -1) return;

		let offsetAmount = values[id];
		for (let point of pattern.points)
			point[axis] = point[axis] + offsetAmount;

		pattern.points = pattern.points; // trigger update
	}

	Clone() {
		var {pattern} = this.props;

		var newPattern = pattern.clone();
		newPattern.name = pattern.name + " - clone";
		LL.settings.patterns.push(newPattern);

		var dialog = new DialogAndroid();
		dialog.set({
			"title": `Choose name for pattern clone`,
			"input": {
				prefill: newPattern.name,
				callback: newName=> {
					newPattern.name = newName;
				}
			},
			"positiveText": "OK", //"negativeText": "Cancel"
		});
		dialog.show();		
	}

	Transform() {
		var {pattern} = this.props;
		
		var dialog = new DialogAndroid();
		dialog.set({
			"title": `Enter transformation code`,
			"input": {
				prefill: "new Vector2i(point.x, point.y)",
				callback: transformCode=> {
					for (let [index, point] of pattern.points.entries())
						pattern.points[index] = eval(transformCode);
					pattern.points = pattern.points; // trigger update
				}
			},
			"positiveText": "OK", //"negativeText": "Cancel"
		});
		dialog.show();		
	}
}
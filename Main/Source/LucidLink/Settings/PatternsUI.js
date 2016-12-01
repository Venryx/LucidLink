import NumberPickerDialog from "react-native-numberpicker-dialog";
import Chart from "react-native-chart";
var DialogAndroid = require("react-native-dialogs");

export default class PatternsUI extends BaseComponent { 
	render() {
		var node = LL.settings;
		return (
			<Panel style={{flex: 1, backgroundColor: colors.background}}>
				<Column style={{marginTop: 10, flex: 1}}>
					<Row style={{height: 35}}>
						<Text style={{marginLeft: 10, marginTop: 5, marginRight: 10}}>Preview chart value range: </Text>
						<VButton text={node.previewChartRangeX.toString()} style={{width: 100, height: 32}}
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
									this.forceUpdate();
								});
							}}/>

						<Text style={{marginLeft: 10, marginTop: 5, marginRight: 10}}> by </Text>
						<VButton text={node.previewChartRangeY.toString()} style={{width: 100, height: 32}}
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
									this.forceUpdate();
								});
							}}/>
					</Row>
					
					<ScrollView ref="scrollView" style={{flex: 100, flexDirection: "column", borderTopWidth: 1}}
							automaticallyAdjustContentInsets={false}>
						{node.patterns.map((pattern, index)=> {
							return <PatternUI key={index} parent={this} pattern={pattern}/>; 
						})}
						<Row height={45}>
							<VButton onPress={()=>this.CreatePattern()} text="Create" style={{width: 100, height: 40}}/>
						</Row>
					</ScrollView>
				</Column>
            </Panel>
		);
	}

	/*ToVectors(points) {
		return points.Select(a=>new Vector2i(a[0], a[1]));
	}
	ToArrays(points) {
		return points.Select(a=>[a.x, a.y]);
	}*/

	CreatePattern() {
		LL.settings.patterns.push(new Pattern("none"));

		LL.PushPatternsToJava();
		this.forceUpdate();
	}
}

@Bind
class PatternUI extends BaseComponent {
	render() {
		var {parent, pattern} = this.props;
		var node = LL.settings;

		// call this after you make any changes to a Pattern object (so Java knows of changes)
		var Change = ()=> {
			LL.PushPatternsToJava();
			parent.forceUpdate();
		};

		var pointsForChart = pattern.points.Select(a=>[a.x, a.y]);
		if (pointsForChart.length == 0)
			pointsForChart = [[0, 0]];

		return (
			<Row height={35 + (pattern.textEditor ? 35 : 0) + (pattern.actions ? 35 : 0) + 100}>
				<Column>
					<Row height={35}>
						<TextInput style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
							editable={true} value={pattern.name}
							onChangeText={text=>Change(pattern.name = text)}/>

						<VSwitch text="Enabled" value={pattern.enabled} onValueChange={value=>Change(pattern.enabled = value)}/>

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
								Change();
							}}/>

						<Text style={{marginLeft: 10, marginTop: 5, marginRight: 10}}>Channels: </Text>
						<VSwitch text="1" value={pattern.channel1}
							onValueChange={value=>Change(pattern.channel1 = value)}/>
						<VSwitch text="2" value={pattern.channel2}
							onValueChange={value=>Change(pattern.channel2 = value)}/>
						<VSwitch text="3" value={pattern.channel3}
							onValueChange={value=>Change(pattern.channel3 = value)}/>
						<VSwitch text="4" value={pattern.channel4}
							onValueChange={value=>Change(pattern.channel4 = value)}/>
						
						<VSwitch text="Text editor" value={pattern.textEditor}
							onValueChange={value=>Change(pattern.textEditor = value)}/>

						<VSwitch text="Actions" value={pattern.actions}
							onValueChange={value=>Change(pattern.actions = value)}/>

						<VButton text="X" style={{marginLeft: 5, width: 28, height: 28}} textStyle={{marginBottom: 3}}
							onPress={()=>pattern.Delete()}/>
					</Row>
					{pattern.textEditor && 
						<Row height={35}>
							<TextInput style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
								editable={true} defaultValue={ToVDF(pattern.points, false)}
								onChangeText={text=> {
									try {
										pattern.points = FromVDF(text, "List(Vector2i)");
									} catch (ex) {
										Toast("Invalid points JSON");
									}
									Change();
								}}/>
						</Row>}
					{pattern.actions && 
						<Row height={35}>
							<VButton text="Offset X" ml5 style={{height: 30}} onPress={()=>this.OffsetPoints("x")}/>
							<VButton text="Offset Y" ml5 style={{height: 30}} onPress={()=>this.OffsetPoints("y")}/>
							<VButton text="Clone" ml5 style={{height: 30}} onPress={()=>this.Clone()}/>
							<VButton text="Transform" ml5 style={{height: 30}} onPress={()=>this.Transform()}/>
						</Row>}
					<Row height={100} style={{backgroundColor: "#FFFFFF55"}}>
						<Chart style={{width: Dimensions.get("window").width - 30, height: 80}}
							minX={-node.previewChartRangeX / 2} maxX={node.previewChartRangeX / 2} legendStepsX={11}
							minY={-node.previewChartRangeY / 2} maxY={node.previewChartRangeY / 2} legendStepsY={5}
							type="line" color={["#e1cd00"]} data={[pointsForChart]}/>
					</Row>
				</Column>
			</Row>
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
		
		LL.PushPatternsToJava();
		this.forceUpdate();
	}

	Clone() {
		var {pattern} = this.props;

		var newPattern = pattern.Clone();
		newPattern.name = pattern.name + " - clone";
		LL.settings.patterns.push(newPattern);

		LL.PushPatternsToJava();
		this.forceUpdate();

		var dialog = new DialogAndroid();
		dialog.set({
			"title": `Choose name for pattern clone`,
			"input": {
				prefill: newPattern.name,
				callback: newName=> {
					newPattern.name = newName;

					LL.PushPatternsToJava();
					if (LL.settings.ui)
						LL.settings.ui.forceUpdate();
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
					
					LL.PushPatternsToJava();
					this.forceUpdate();
				}
			},
			"positiveText": "OK", //"negativeText": "Cancel"
		});
		dialog.show();		
	}
}
import NumberPickerDialog from "react-native-numberpicker-dialog";
import Chart from "react-native-chart";

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
									selectedValueIndex: values.indexOf(node.previewChartRangeX),
									values: values.Select(a=>a.toString()),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
									message: "Select value-range (horizontal) to display in the pattern-previews below.",
									title: "Preview chart value range - x (horizontal)",
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
									selectedValueIndex: values.indexOf(node.previewChartRangeY),
									values: values.Select(a=>a.toString()),
									positiveButtonLabel: "Ok", negativeButtonLabel: "Cancel",
									message: "Select value-range (vertical) to display in the pattern-previews below.",
									title: "Preview chart value range - y (vertical)",
								}).then(id=> {
									if (id == -1) return;
									let val = values[id];
									node.previewChartRangeY = val;
									this.forceUpdate();
								});
							}}/>
					</Row>
					
					{node.patterns.map((pattern, index)=> {
						return <PatternUI key={index} pattern={pattern}/>; 
					})}
					<Row height={45}>
						<VButton onPress={()=>this.CreatePattern()} text="Create" style={{width: 100, height: 40}}/>
					</Row>
					<View style={{flex: 111222}}/>
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
		LL.settings.patterns.push({name: "none", points: []});

		LL.PushPatternsToJava();
		this.forceUpdate();
	}
}

class PatternUI extends BaseComponent {
	render() {
		var {pattern} = this.props;
		var node = LL.settings;

		// call this after you make any changes to a Pattern object (so Java knows of changes)
		function Change() {
			LL.PushPatternsToJava();
			this.forceUpdate();
		}

		var pointsForChart = pattern.points.Select(a=>[a.x, a.y]);
		if (pointsForChart.length == 0)
			pointsForChart = [[0, 0]];

		return (
			<Row height={35 + (pattern.textEditor ? 35 : 0) + 100}>
				<Column>
					<Row height={35}>
						<TextInput style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
							editable={true} value={pattern.name}
							onChangeText={text=>Change(pattern.name = text)}/>

						<Text style={{marginLeft: 10, marginTop: 5, marginRight: 10}}>Channels: </Text>
						<Text style={{marginLeft: 10, marginTop: 5, marginRight: 10}}>1</Text>
						<Switch value={pattern.channel1}
							onValueChange={value=>Change(pattern.channel1 = value)}/>
						<Text style={{marginLeft: 10, marginTop: 5, marginRight: 10}}>2</Text>
						<Switch value={pattern.channel2}
							onValueChange={value=>Change(pattern.channel2 = value)}/>
						<Text style={{marginLeft: 10, marginTop: 5, marginRight: 10}}>3</Text>
						<Switch value={pattern.channel3}
							onValueChange={value=>Change(pattern.channel3 = value)}/>
						<Text style={{marginLeft: 10, marginTop: 5, marginRight: 10}}>4</Text>
						<Switch value={pattern.channel4}
							onValueChange={value=>Change(pattern.channel4 = value)}/>
							
						<Text style={{marginTop: 5}}>Text editor</Text>
						<Switch value={pattern.textEditor}
							onValueChange={value=>Change(pattern.textEditor = value)}/>
						<VButton text="X" style={{alignItems: "flex-end", marginLeft: 5, width: 28, height: 28}} textStyle={{marginBottom: 3}}
							onPress={()=>Change(node.patterns.Remove(pattern))}/>
					</Row>
					{pattern.textEditor && 
						<Row height={35}>
							<TextInput style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
								editable={true} defaultValue={ToVDF(pattern.points, false)}
								onChangeText={text=> {
									try {
										pattern.points = FromVDF(text, "List(Vector2i)");
									} catch (ex) {
										V.Toast("Invalid points JSON");
									}
									Change();
								}}/>
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
}
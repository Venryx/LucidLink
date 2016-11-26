import Chart from "react-native-chart";

const data = [
    [0, 1],
    [1, 3],
    [3, 7],
    [4, 9],
];

export default class PatternsUI extends BaseComponent { 
	render() {
		var node = LL.settings;
		return (
			<View style={{flex: 1}}>
				<Row style={{flex: 1, flexDirection: "column"}}>
					<Row style={{marginTop: 10, flex: 1, flexDirection: "column"}}>
						{node.patterns.map((pattern, index)=> {
							return (
								<Row key={index} height={35 + 100 + (pattern.textEditorEnabled ? 35 : 0)}>
									<Column>
										<Row height={35}>
											<TextInput style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
												editable={true} value={pattern.name}
												onChangeText={text=>(pattern.name = text) | this.forceUpdate()}/>
											<Text style={{marginTop: 5}}>Text editor</Text>
											<Switch value={pattern.textEditorEnabled}
												onValueChange={value=>(pattern.textEditorEnabled = value) | this.forceUpdate()}/>
											<VButton text="X" style={{alignItems: "flex-end", marginLeft: 5, width: 28, height: 28}} textStyle={{marginBottom: 3}}
												onPress={()=>node.patterns.Remove(pattern) | this.forceUpdate()}/>
										</Row>
										<Row height={100}>
											<Chart style={{width: Dimensions.get("window").width - 30, height: 100}}
												verticalGridStep={5} type="line" showDataPoint={true} color="#e1cd00" data={data}/>
										</Row>
										{pattern.textEditorEnabled && 
											<Row height={35}>
												<TextInput style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
													editable={true} defaultValue={ToJSON(pattern.points)}
													onChangeText={text=> {
														try {
															pattern.points = FromJSON(text);
														} catch (ex) {
															V.Toast("Invalid points JSON");
														}
														this.forceUpdate()
													}}/>
											</Row>}
									</Column>
								</Row>
							);
						})}
						<Row height={40}>
							<VButton onPress={()=>this.CreatePattern()} text="Create" style={{width: 100, height: 40}}/>
						</Row>
					</Row>
					<View style={{flex: 111222}}>
					</View>
				</Row>
            </View>
		);
	}

	CreatePattern() {
		LL.settings.patterns.push({name: "none", points: []});
		this.forceUpdate();
	}
}
export default class PatternsUI extends BaseComponent { 
	render() {
		var node = LL.settings;
		return (
			<View style={{flex: 1}}>
				<Row style={{flex: 1, flexDirection: "column"}}>
					<Row style={{marginTop: 10, flex: 1, flexDirection: "column"}}>
						{node.patterns.map((pattern, index)=> {
							return (
								<Row key={index} height={35 + (pattern.textEditorEnabled && 35)}>
									<TextInput style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
										editable={true} value={pattern.name}
										onChangeText={text=>(pattern.name = text) | this.forceUpdate()}/>
									<Switch value={pattern.textEditorEnabled}
										onValueChange={value=>(pattern.textEditorEnabled = value) | this.forceUpdate()}/>
									<VButton text="X" style={{alignItems: "flex-end", marginLeft: 5, width: 28, height: 28}} textStyle={{marginBottom: 3}}
										onPress={()=>node.patterns.Remove(pattern) | this.forceUpdate()}/>
									{pattern.textEditorEnabled && 
										<TextInput style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
											editable={true} defaultValue={ToJSON(pattern.points)}
											onChangeText={text=> {
												try {
													pattern.points = FromJSON(text);
												} catch (ex) {
													V.Toast("Invalid points JSON");
												}
												this.forceUpdate()
											}}/>}
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
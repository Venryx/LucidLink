export default class ConsoleUI extends BaseComponent {
	render() {
		var {onChangeText, text} = this.props;
		var buttonStyle = {flex: .2, marginLeft: 5, height: isLandscape ? 35 : 50};
		var textStyle = {
			//height: screenHeight,
			flex: 1, textAlignVertical: "top",
		};
		
		return (
			<View style={{flex: 1, flexDirection: "column"}}>
				<View style={{flexDirection: "row", flexWrap: "wrap", padding: 3}}>
					<View style={{flex: .8, flexDirection: "row"}}>
						<VButton style={E(buttonStyle, {marginLeft: 0})} text="Pause/Debugger" onPress={Debugger}/>
						<View style={{flex: .6}}/>
						<View style={{flex: .2, flexDirection: "row", alignItems: "flex-end"}}>
							<VButton style={buttonStyle} color="#777" onPress={()=>this.RunScript()} text="Run script"/>
						</View>
					</View>
				</View>
				<TextInput {...{onChangeText}} style={textStyle} multiline={true} editable={true} value={text}/>
			</View>
		);
	}
	RunScript() {
		try {
			eval(this.props.text);
		} catch(error) {
			g.HandleError(error);
		}
	}
}
var g = global;

export default class ConsoleUI extends BaseComponent {
	render() {
		var {onChangeText, text} = this.props;
		var buttonStyle = {flex: .2, marginLeft: 5};
		var textStyle = {flex: 1, textAlignVertical: "top"};
		
		return (
			<Column style={{flex: 1, backgroundColor: colors.background}}>
				<Panel style={{flexDirection: "row", flexWrap: "wrap", padding: 3}}>
					<Panel style={{flex: .8, flexDirection: "row"}}>
						<VButton style={E(buttonStyle, {marginLeft: 0})} text="Pause/Debugger" onPress={Debugger}/>
						<Panel style={{flex: .6}}/>
						<Panel style={{flex: .2, flexDirection: "row", alignItems: "flex-end"}}>
							<VButton style={buttonStyle} color="#777" onPress={()=>this.RunScript()} text="Run script"/>
						</Panel>
					</Panel>
				</Panel>
				<TextInput {...{onChangeText}} style={textStyle} multiline={true} editable={true} value={text}/>
			</Column>
		);
	}
	RunScript() {
		try {
			eval(this.props.text);
		} catch(error) {
			g.HandleError(error);
			alert(error + "\n" + error.stack);
		}
	}
}
export default class OthersUI extends BaseComponent {
	render() {
		return (
			<View style={{flex: 1, flexDirection: "column"}}>
				<Row height={40} style={{marginTop: 5, marginBottom: 10}}>
					<VButton text='Reset "built-in script"' style={{width: 300, height: 40}}
						onPress={()=>LL.scripts.ResetScript(2)}/>
					<VButton text='Reset "custom script"' style={{marginLeft: 5, width: 300, height: 40}}
						onPress={()=>LL.scripts.ResetScript(4)}/>
				</Row>
			</View>
		);
	}
}
export default class OthersUI extends BaseComponent {
	render() {
		return (
			<View style={{flex: 1, flexDirection: "column"}}>
				<Row height={40}>
					<VButton text='Reset "built-in script"' style={{width: 300}}
						onPress={()=>LL.scripts.ResetScript("Built-in script")}/>
					<VButton text='Reset "custom script"' style={{marginLeft: 5, width: 300}}
						onPress={()=>LL.scripts.ResetScript("Custom script")}/>
				</Row>
			</View>
		);
	}
}
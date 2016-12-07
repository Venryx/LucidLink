import Spinner from "rn-spinner";

export default class OptionsPanel extends BaseComponent {
	render() {
		var {parent} = this.props;
		var node = LL.monitor;
		
		return (
			<Column style={{flex: 1, backgroundColor: colors.background_light}}>
				<Row>
					<Text style={{marginLeft: 5, marginTop: 5}}>UI update interval: </Text>
					<Spinner min={1} max={1000} default={node.updateInterval} color={colors.background_dark} numColor={colors.text_dark}
						onNumChange={value=> {
							node.updateInterval = value;
							LL.PushBasicDataToJava();
							//this.forceUpdate();
						}}/>
				</Row>
			</Column>
		)
	}
}
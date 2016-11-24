import Spinner from "rn-spinner";

export default class OptionsPanel extends BaseComponent {
	render() {
		var {parent} = this.props;
		var node = LL.monitor;
		
		return (
			<View style={{flex: 1, flexDirection: "column", backgroundColor: "#CCC"}}>
				<View style={{flexDirection: "row"}}>
					<Text style={{marginLeft: 5, marginTop: 5}}>UI update interval: </Text>
					<Spinner min={1} max={1000} default={node.updateInterval} color="#f60" numColor="#f60"
						onNumChange={value=> {
							node.updateInterval = value;
							LL.PushBasicDataToJava();
							//this.forceUpdate();
						}}/>
				</View>
			</View>
		)
	}
}
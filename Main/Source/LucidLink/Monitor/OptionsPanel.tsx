import {observer} from "mobx-react/native";
import {colors} from "../../Frame/Styles";
import {Text} from "react-native";
import Spinner from "rn-spinner";
import {BaseComponent, Column, Row, VSwitch, VText} from "../../Frame/ReactGlobals";
import {LL} from "../../LucidLink";

@observer
export default class OptionsPanel extends BaseComponent<any, any> {
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
						}}/>
				</Row>
				<Row>
					<VText mt12>Channels: </VText>
					<VSwitch text="1" valuePath={[node, "channel1"]}/>
					<VSwitch text="2" valuePath={[node, "channel2"]}/>
					<VSwitch text="3" valuePath={[node, "channel3"]}/>
					<VSwitch text="4" valuePath={[node, "channel4"]}/>
				</Row>
			</Column>
		)
	}
}
import {observer} from "mobx-react/native";
import {colors} from "../../../Frame/Styles";
import {Text} from "react-native";
import Spinner from "rn-spinner";
import {BaseComponent, Column, Row, VText} from "../../../Frame/ReactGlobals";
import {LL} from "../../../LucidLink";
import {VSwitch, VSwitch_Auto} from "../../../Packages/ReactNativeComponents/VSwitch";
import {SPMonitorUI} from "../SPMonitor";

@observer
export default class OptionsPanel extends BaseComponent<{parent: SPMonitorUI}, {}> {
	render() {
		var {parent} = this.props;
		var node = LL.tools.spMonitor;
		
		return (
			<Column style={{flex: 1, backgroundColor: colors.background_light}}>
				<Row>
					{/*<VSwitch_Auto text="Calculate breathing rate" path={()=>node.p.calculateBreathingRate} mt={3}/>*/}
				</Row>
			</Column>
		)
	}
}
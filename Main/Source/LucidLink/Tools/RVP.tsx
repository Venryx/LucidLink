import {JavaBridge,} from "../../Frame/Globals";
import {EEGProcessor} from "../../Frame/Patterns/EEGProcessor";
import {BaseComponent as Component, Column, Panel, Row, VButton} from "../../Frame/ReactGlobals";
import {colors, styles} from "../../Frame/Styles";
import {Vector2i} from "../../Frame/Graphics/VectorStructs";
import {Observer, observer} from "mobx-react/native";
import Drawer from "react-native-drawer";
import {MKRangeSlider} from "react-native-material-kit";
import DialogAndroid from "react-native-dialogs";
import {Text, Switch, View} from "react-native";
import {LL} from "../../LucidLink";
import Node from "../../Packages/VTree/Node";

export class RVP extends Node {
}
g.Extend({RVP});

@observer
export class RVPUI extends Component<any, any> {
	render() {
		var node = LL.tools.rvp;
		return (
			<Column style={{flex: 1, backgroundColor: colors.background}}>
				<Panel style={{flex: 1}}>
				</Panel>
			</Column>
		);
	}
}
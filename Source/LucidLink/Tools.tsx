import {FromVDF, ToVDF, Global} from "../Frame/Globals";
import {BaseComponent, BaseProps} from "../Frame/ReactGlobals";
import ScrollableTabView from "react-native-scrollable-tab-view";
import DialogAndroid from "react-native-dialogs";
import Node from "../Packages/VTree/Node";

import GeneralUI from "./Settings/GeneralUI";
import AudiosUI from "./Settings/AudiosUI";
import {LL} from "../LucidLink";
import {P, T} from "../Packages/VDF/VDFTypeInfo";
import {RVPUI, RVP} from "./Tools/RVP";
import {SPMonitor, SPMonitorUI} from "./Tools/SPMonitor";
import {FBA} from "./Tools/FBA";
import FBAUI from "./Tools/FBAUI";
import {View} from "react-native";

@Global
export class Tools extends Node {
	@T("FBA") @P(true, true) fba = new FBA();
	@T("RVP") @P(true, true) rvp = new RVP();
	@T("SPMonitor") @P(true, true) spMonitor = new SPMonitor();
}

export class ToolsUI extends BaseComponent<BaseProps & {}, {show: boolean}> {
	ComponentDidMount() {
		// fixes that first-tab's tab-view would not display
		setTimeout(()=>this.setState({show: true}), 0);
	}
	render() {
		if (!this.state.show) return <View/>;
		return (
			<ScrollableTabView style={{flex: 1}}>
				<FBAUI tabLabel="FBA Technique"/>
				<RVPUI tabLabel="RVP Technique"/>
				<SPMonitorUI tabLabel="S+ Monitor"/>
			</ScrollableTabView>
		);
	}
}
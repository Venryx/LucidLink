import {FromVDF, IsString, ToVDF} from "../Frame/Globals";
import {BaseComponent} from "../Frame/ReactGlobals";
var ScrollableTabView = require("react-native-scrollable-tab-view");
var DialogAndroid = require("react-native-dialogs");
import Node from "../Packages/VTree/Node";

import GeneralUI from "./Settings/GeneralUI";
import AudiosUI from "./Settings/AudiosUI";
import {LL} from "../LucidLink";
import {P, T} from "../Packages/VDF/VDFTypeInfo";
import {MonitorUI, Monitor} from "./Tools/Monitor";
import {RVPUI, RVP} from "./Tools/RVP";

export class Tools extends Node {
	@T("RVP") @P(true, true) rvp = new RVP();
	@T("Monitor") @P(true, true) monitor = new Monitor();
}
g.Extend({Tools});

export class ToolsUI extends BaseComponent<any, any> {
	render() {
		return (
			<ScrollableTabView style={{flex: 1}}>
				<RVPUI tabLabel="RVP Technique"/>
				<MonitorUI tabLabel="Monitor"/>
			</ScrollableTabView>
		);
	}
}
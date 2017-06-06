import {Global, JavaBridge, Log, Toast} from "../../../Frame/Globals";
import {EEGProcessor} from "../../../Frame/Patterns/EEGProcessor";
import {BaseComponent as Component, Column, Panel, Row, VButton, BaseProps} from "../../../Frame/ReactGlobals";
import {colors, styles} from "../../../Frame/Styles";
import {Vector2i, VVector2} from "../../../Frame/Graphics/VectorStructs";
import {Observer, observer} from "mobx-react/native";
import Drawer from "react-native-drawer";
import {MKRangeSlider} from "react-native-material-kit";
import DialogAndroid from "react-native-dialogs";
import {Text, Switch, View, NativeModules} from "react-native";
import {LL} from "../../../LucidLink";
import Node from "../../../Packages/VTree/Node";
import {VSwitch, VSwitch_Auto} from "../../../Packages/ReactNativeComponents/VSwitch";
import OptionsPanel from ".././SPMonitor/OptionsPanel";
import {P} from "../../../Packages/VDF/VDFTypeInfo";
import SPBridge, {SleepStage} from "../../../Frame/SPBridge";
import {Timer, TimerContext} from "../../../Frame/General/Timers";
import {autorun} from "mobx";
import {ListenersContext} from "../FBA/FBARun";
import { SPMonitorClass } from "../../Tools/SPMonitor";

export default class GraphUI extends Component<{}, {}> {
	render() {
		return (
			/*<View style={{flex: 1, backgroundColor: colors.background}}>
			</View>*/
			<GraphOverlayUI monitor={LL.tools.spMonitor.monitor}/>
		);
	}
}

export class GraphOverlayUI extends Component<{monitor: SPMonitorClass}, {}> {
	timerContext = new TimerContext();
	ComponentDidMount() {
		let {monitor} = this.props;
		//monitor.Start();
		new Timer(1, ()=> {
			if (!this.mounted) return;
			this.forceUpdate();
		}).Start().SetContext(this.timerContext);
	}
	ComponentWillUnmount() {
		let {monitor} = this.props;
		//monitor.Reset();
		this.timerContext.Reset();
	}

	render() {
		let {monitor: mon} = this.props;
		return (
			<Column style={{/*position: "absolute", left: 0, right: 0, top: 0, bottom: 0,*/ backgroundColor: colors.background}}>
				<Text>Temp: {mon.temp}f</Text>
				<Text>Light: {mon.light}</Text>
				<Text>Breath #1: {mon.breathVal.x} (min/max/avg of last 15s: {mon.breathVal_min.x}/{mon.breathVal_max.x}/{mon.breathVal_avg.x.toFixed(1)})</Text>
				<Text>Breath #2: {mon.breathVal.y} (min/max/avg of last 15s: {mon.breathVal_min.y}/{mon.breathVal_max.y}/{mon.breathVal_avg.y.toFixed(1)})</Text>
				<Text>Breathing depth (prev 15s): {mon.breathingDepth_prev.toFixed(1)}</Text>
				<Text>Breathing depth (last 15s): {mon.breathingDepth_last.toFixed(1)
					} (change: {((mon.breathingDepth_last / mon.breathingDepth_prev).Distance(1) * 100).toFixed()}%)</Text>
				<Text>Breathing rate: {mon.breathingRate}</Text>
				<Text>Sleep stage: {mon.sleepStage ? mon.sleepStage.name : "Unknown"}</Text>
			</Column>
		);
	}
}
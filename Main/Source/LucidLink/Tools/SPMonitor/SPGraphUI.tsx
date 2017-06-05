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

export default class GraphUI extends Component<{}, {}> {
	render() {
		return (
			/*<View style={{flex: 1, backgroundColor: colors.background}}>
			</View>*/
			<GraphOverlayUI/>
		);
	}
}

export class GraphOverlayUI extends Component<{}, {}> {
	timerContext = new TimerContext();
	listenersContext = new ListenersContext();
	
	ComponentDidMount() {
		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveTemp, this.OnReceiveTemp);
		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveLightValue, this.OnReceiveLightValue);
		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveBreathValues, this.OnReceiveBreathValues);
		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveBreathValueMinMaxAndAverages, this.OnReceiveBreathValueMinMaxAndAverage);
		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveBreathingDepth, this.OnReceiveBreathingDepth);
		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveBreathingRate, this.OnReceiveBreathingRate);
		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveSleepStage, this.OnReceiveSleepStage);
		new Timer(1, ()=> {
			if (!this.mounted) return;
			this.forceUpdate();
		}).Start().SetContext(this.timerContext);
	}
	ComponentWillUnmount() {
		this.timerContext.Reset();
		this.listenersContext.Reset();
	}

	// raw data
	// ==========

	temp = -1;
	OnReceiveTemp(tempInC: number, tempInF: number) {
		this.temp = tempInF;
	}
	light = -1;
	OnReceiveLightValue(lightVal: number) {
		this.light = lightVal;
	}
	breathVal = new VVector2(0, 0);
	OnReceiveBreathValues(breathVal1: number, breathVal2: number) {
		this.breathVal = new VVector2(breathVal1, breathVal2);
	}

	// calculated data
	// ==========

	breathVal_min = new VVector2(0, 0);
	breathVal_max = new VVector2(0, 0);
	breathVal_avg = new VVector2(0, 0);
	OnReceiveBreathValueMinMaxAndAverage(min_1: number, min_2: number, max_1: number, max_2: number, avg_1: number, avg_2: number) {
		this.breathVal_min = new VVector2(min_1, min_2);
		this.breathVal_max = new VVector2(max_1, max_2);
		this.breathVal_avg = new VVector2(avg_1, avg_2);
	}
	breathingDepth_prev = -1;
	breathingDepth_last = -1;
	OnReceiveBreathingDepth(breathingDepth_prev: number, breathingDepth_last: number) {
		this.breathingDepth_prev = breathingDepth_prev;
		this.breathingDepth_last = breathingDepth_last;
	}
	breathingRate = -1;
	OnReceiveBreathingRate(breathingRate: number) {
		this.breathingRate = breathingRate;
	}
	sleepStage = null as SleepStage;
	OnReceiveSleepStage(sleepStage: SleepStage) {
		this.sleepStage = sleepStage;
	}

	render() {
		return (
			<Column style={{/*position: "absolute", left: 0, right: 0, top: 0, bottom: 0,*/ backgroundColor: colors.background}}>
				<Text>Temp: {this.temp}f</Text>
				<Text>Light: {this.light}</Text>
				<Text>Breath #1: {this.breathVal.x} (min/max/avg of last 15s: {this.breathVal_min.x}/{this.breathVal_max.x}/{this.breathVal_avg.x.toFixed(1)})</Text>
				<Text>Breath #2: {this.breathVal.y} (min/max/avg of last 15s: {this.breathVal_min.y}/{this.breathVal_max.y}/{this.breathVal_avg.y.toFixed(1)})</Text>
				<Text>Breathing depth (prev 15s): {this.breathingDepth_prev.toFixed(1)}</Text>
				<Text>Breathing depth (last 15s): {this.breathingDepth_last.toFixed(1)}</Text>
				<Text>Breathing rate: {this.breathingRate}</Text>
				<Text>Sleep stage: {this.sleepStage ? this.sleepStage.name : "Unknown"}</Text>
			</Column>
		);
	}
}
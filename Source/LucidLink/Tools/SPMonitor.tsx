import {Global, JavaBridge, Log, Toast} from "../../Frame/Globals";
import {BaseComponent as Component, Column, Panel, Row, VButton, BaseProps} from "../../Frame/ReactGlobals";
import {colors, styles} from "../../Frame/Styles";
import {Vector2i, VVector2} from "../../Frame/Graphics/VectorStructs";
import {Observer, observer} from "mobx-react/native";
import Drawer from "react-native-drawer";
import {MKRangeSlider} from "react-native-material-kit";
import DialogAndroid from "react-native-dialogs";
import {Text, Switch, View, NativeModules} from "react-native";
import {LL} from "../../LucidLink";
import Node from "../../Packages/VTree/Node";
import {VSwitch, VSwitch_Auto} from "../../Packages/ReactNativeComponents/VSwitch";
import OptionsPanel from "./SPMonitor/OptionsPanel";
import {P} from "../../Packages/VDF/VDFTypeInfo";
import SPBridge, {SleepStage} from "../../Frame/SPBridge";
import {Timer} from "../../Frame/General/Timers";
import {autorun} from "mobx";
import {GraphOverlayUI} from "./SPMonitor/SPGraphUI";
import GraphUI from "./SPMonitor/SPGraphUI";
import {ListenersContext} from "./FBA/FBARun";
import { Assert } from "../../Frame/General/Assert";

@Global
export class SPMonitor extends Node {
	/*constructor() {
		super();
		this.monitor.Start(); // just have it always running in the background
	}*/

	@O @P() connect = true;
	/*@O @P() monitor = true;
	@O @P() process = true;*/

	//@O @P() calculateBreathingRate = true;

	monitor = new SPMonitorClass();
}

export class SPMonitorClass {
	constructor() {
		// for now, just auto-start (and have it running in the background, as singleton basically)
		this.Start();
	}

	listenersContext = new ListenersContext();
	Start() {
		Assert(this.listenersContext.listEntries.length == 0, "Cannot start SPMonitorClass twice.");
		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveTemp, this.OnReceiveTemp.bind(this));
		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveLightValue, this.OnReceiveLightValue.bind(this));
		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveBreathValues, this.OnReceiveBreathValues.bind(this));
		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveBreathValueMinMaxAndAverages, this.OnReceiveBreathValueMinMaxAndAverage.bind(this));
		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveBreathingDepth, this.OnReceiveBreathingDepth.bind(this));
		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveBreathingRate, this.OnReceiveBreathingRate.bind(this));
		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveSleepStage, this.OnReceiveSleepStage.bind(this));
	}
	/*Reset() {
		this.listenersContext.Reset();
	}*/

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
	sleepStage: SleepStage;
	OnReceiveSleepStage(sleepStage: SleepStage) {
		this.sleepStage = sleepStage;
	}
}

@observer
export class SPMonitorUI extends Component<BaseProps, {}> {
	sidePanel = null;
	ToggleSidePanelOpen() {
		if (this.sidePanel._open) {
			this.sidePanel.close();
		} else {
			this.sidePanel.open();
		}
	}

	render() {
		var node = LL.tools.spMonitor;
		var bridge = SPBridge;
		
		const drawerStyles = {
			drawer: {shadowColor: "#000000", shadowOpacity: .8, shadowRadius: 3},
			main: {paddingLeft: 3},
		};
		
		return (
			<Drawer ref={comp=>this.sidePanel = comp}
					content={<OptionsPanel parent={this}/>}
					type="overlay" openDrawerOffset={0.7} panCloseMask={0.7} tapToClose={true}
					closedDrawerOffset={-3} styles={drawerStyles}>
				<Column style={{flex: 1, backgroundColor: colors.background}}>
					<Row style={{padding: 3, height: 56, backgroundColor: "#303030"}}>
						<VButton text="Options" plr={10} onPress={this.ToggleSidePanelOpen}/>
						<Panel style={{flex: 1}}/>

						{/*{["unknown", "disconnected", "needs_update"].Contains(bridge.status) && node.connect &&
							<Text style={{height: 50, top: 10, textAlignVertical: "top"}}>Searching for S+ device...</Text>}
						{bridge.status == "connecting" &&
							<Text style={{height: 50, top: 10, textAlignVertical: "top"}}>Connecting...</Text>}
						{bridge.status == "connected" &&
							<Text style={{height: 50, top: 10, textAlignVertical: "top"}}>Connected</Text>}
						<VSwitch_Auto mt={6} path={()=>node.p.connect}/>*/}
						<VSwitch_Auto text="Connect" mt={6} path={()=>node.p.connect}/>
						{/*<VSwitch_Auto text="Monitor" ml={5} mt={6} path={()=>node.p.monitor}/>
						<VSwitch_Auto text="Process" ml={5} mt={6} path={()=>node.p.process}/>*/}
						
						<VButton text="RealTime" ml={10} plr={10} onPress={()=> {
							//LL.tracker.currentSession.StartSleepSession_RealTime();
							if (LL.tracker.currentSession.CurrentSleepSession) {
								LL.tracker.currentSession.CurrentSleepSession.End();
							}
							SPBridge.StartRealTimeSession();
						}}/>
						<VButton text="Sleep" ml={10} plr={10} onPress={()=> {
							LL.tracker.currentSession.StartSleepSession();
						}}/>
						<VButton text="Stop" ml={10} plr={10} onPress={()=> {
							if (LL.tracker.currentSession.CurrentSleepSession) {
								LL.tracker.currentSession.CurrentSleepSession.End();
							} else {
								SPBridge.StopSession();
							}
						}}/>
					</Row>
					{/*<Panel style={{marginTop: -7, flex: 1}}>*/}
					<Column>
						<GraphUI/>
					</Column>
				</Column>
			</Drawer>
		);
	}
}
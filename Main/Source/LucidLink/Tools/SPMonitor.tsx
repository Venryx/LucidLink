import {Global, JavaBridge, Log, Toast} from "../../Frame/Globals";
import {EEGProcessor} from "../../Frame/Patterns/EEGProcessor";
import {BaseComponent as Component, Column, Panel, Row, VButton, BaseProps} from "../../Frame/ReactGlobals";
import {colors, styles} from "../../Frame/Styles";
import {Vector2i} from "../../Frame/Graphics/VectorStructs";
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
import SPBridge from "../../Frame/SPBridge";
import {Timer} from "../../Frame/General/Timers";
import {autorun} from "mobx";

// keep order, since must match with Java
enum SleepState_Detailed {
    Wake = 1, // -> Simple.Wake
    Absent = 2, Unknown = 3, Break = 4, // -> Simple.Other
    LightSleep = 5, // -> Simple.Light
    DeepSleep = 6, // -> Simple.Deep
    RemSleep = 7, // -> Simple.REM
}
// keep order, since must match with Java
enum SleepState_Simple {
    Other = 0,
    Wake = -1,
    REM = 1,
    Light = 2,
    Deep = 3,
}
function ConvertDetailedSleepStateToSimpleSleepState(detailedState: SleepState_Detailed) {
	if ([SleepState_Detailed.Absent, SleepState_Detailed.Unknown, SleepState_Detailed.Break].Contains(detailedState))
		return SleepState_Simple.Other;
	if (detailedState == SleepState_Detailed.Wake)
		return SleepState_Simple.Wake;
	if (detailedState == SleepState_Detailed.RemSleep)
		return SleepState_Simple.REM;
	if (detailedState == SleepState_Detailed.LightSleep)
		return SleepState_Simple.Light;
	//if (detailedState == SleepState_Detailed.DeepSleep)
	return SleepState_Simple.Deep;
}

// for now, check for sleep-stage manually every 10 seconds (when "connect" and "monitor" are enabled)
new Timer(10, async ()=> {
	if (LL.tools.spMonitor.connect && LL.tools.spMonitor.monitor) {
		var stageValue = await LL.spBridge.GetSleepStage();
		var stageEnum_detailed_name =  SleepState_Detailed[stageValue];
		var stageEnum_detailed: SleepState_Detailed = SleepState_Detailed[stageEnum_detailed_name];
		var stageEnum_simple = ConvertDetailedSleepStateToSimpleSleepState(stageEnum_detailed);
		Toast("SleepStage:" + stageValue + ";" + stageEnum_detailed_name);
		/*if (stage != 3)
			alert("Got other result!" + stage);*/
	}
}).Start();

@Global
export class SPMonitor extends Node {
	@O @P() connect = true;
	@O @P() monitor = true;
	@O @P() process = true;
}

@observer
export class SPMonitorUI extends Component<BaseProps, {}> {
	sidePanel = null;
	ToggleSidePanelOpen() {
		if (this.sidePanel._open)
			this.sidePanel.close();
		else
			this.sidePanel.open();
	}

	render() {
		var node = LL.tools.spMonitor;
		var bridge = LL.spBridge;
		
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
						<VButton text="Options" style={{width: 100}} onPress={this.ToggleSidePanelOpen}/>
						<Panel style={{flex: 1}}/>

						{["unknown", "disconnected", "needs_update"].Contains(bridge.status) && node.connect &&
							<Text style={{height: 50, top: 10, textAlignVertical: "top"}}>Searching for S+ device...</Text>}
						{bridge.status == "connecting" &&
							<Text style={{height: 50, top: 10, textAlignVertical: "top"}}>Connecting...</Text>}
						{bridge.status == "connected" &&
							<Text style={{height: 50, top: 10, textAlignVertical: "top"}}>Connected</Text>}
						<VSwitch_Auto mt={8} path={()=>node.p.connect}/>
						<VSwitch_Auto text="Monitor" ml={5} mt={8} path={()=>node.p.monitor}/>
						<VSwitch_Auto text="Process" ml={5} mt={8} path={()=>node.p.process}/>

						<VButton text="Get stage" onPress={async ()=> {
							alert(await LL.spBridge.GetSleepStage());
						}}/>
						<VButton text="RealTime" onPress={()=> {
							LL.spBridge.StartRealTimeStream();
						}}/>
						<VButton text="Sleep" onPress={()=> {
							LL.spBridge.StartSleep();
						}}/>
					</Row>
					<Panel style={{marginTop: -7, flex: 1}}>
						<GraphUI/>
						<GraphOverlayUI/>
					</Panel>
				</Column>
			</Drawer>
		);
	}
}

class GraphUI extends Component<{}, {}> {
    render() {
        return (
			<View style={{flex: 1, backgroundColor: colors.background}}/>
        );
    }
}

class GraphOverlayUI extends Component<{}, {}> {
	timer: Timer;
	componentDidMount() {
		LL.spBridge.listeners_onReceiveTempValue.push(this.OnReceiveTempValue);
		LL.spBridge.listeners_onReceiveLightValue.push(this.OnReceiveLightValue);
		LL.spBridge.listeners_onReceiveBreathValue.push(this.OnReceiveBreathValue);
		this.timer = new Timer(1, ()=> {
			this.forceUpdate();
		}).Start();
	}
	componentWillUnmount() {
		LL.spBridge.listeners_onReceiveTempValue.Remove(this.OnReceiveTempValue);
		LL.spBridge.listeners_onReceiveLightValue.Remove(this.OnReceiveLightValue);
		LL.spBridge.listeners_onReceiveBreathValue.Remove(this.OnReceiveBreathValue);
		this.timer.Stop();
	}

	temp = -1;
	OnReceiveTempValue(tempInC, tempInF) {
		this.temp = tempInF;
	}
	light = -1;
	OnReceiveLightValue(lightVal) {
		this.light = lightVal;
	}
	breath = -1;
	OnReceiveBreathValue(breathVal) {
		this.breath = breathVal;
	}

    render() {
        return (
			<Column style={{position: "absolute", left: 0, right: 0, top: 0, bottom: 0, backgroundColor: colors.background}}>
				<Text>Temp: {this.temp}f</Text>
				<Text>Light: {this.light}</Text>
				<Text>Breath: {this.breath}</Text>
			</Column>
        );
    }
}
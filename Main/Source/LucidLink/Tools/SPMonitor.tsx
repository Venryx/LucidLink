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

// for now, check for sleep-stage manually every 10 seconds (when "connect" and "monitor" are enabled)
new Timer(10, async ()=> {
	if (LL.tools.spMonitor.connect && LL.tools.spMonitor.monitor) {
		var stage = await LL.spBridge.GetSleepStage();
		Toast("SleepStage:" + stage);
		if (stage != 3)
			alert("Got other result!" + stage);
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
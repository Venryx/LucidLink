import {JavaBridge, P} from "../Frame/Globals";
import {EEGProcessor} from "../Frame/Patterns/EEGProcessor";
import {BaseComponent as Component, Column, Panel, Row, VButton, VSwitch} from "../Frame/ReactGlobals";
import {colors, styles} from "../Frame/Styles";
import {Vector2i} from "../Frame/Graphics/VectorStructs";
import {Observer, observer} from "mobx-react/native";
import Drawer from "react-native-drawer";
import {MKRangeSlider} from "react-native-material-kit";
var DialogAndroid = require("react-native-dialogs");
import {Text, Switch, View} from "react-native";
import Node from "../Packages/VTree/Node";

export class Monitor extends Node {
	@O @P() updateInterval = 3;
	@O @P() channel1 = true;
	@O @P() channel2 = true;
	@O @P() channel3 = true;
	@O @P() channel4 = true;

	@O @P() connect = true;
	@O @P() monitor = true;
	@O @P() patternMatch = true;

	eegProcessor = new EEGProcessor();

	ui = null;
}
g.Extend({Monitor});

import OptionsPanel from "./Monitor/OptionsPanel";
import MuseBridge from "../Frame/MuseBridge";
import {LL} from "../LucidLink";

@observer
export class MonitorUI extends Component<any, any> {
	constructor(props) {
		super(props);
		LL.monitor.ui = this;
		this.patternGrab_minX = 0;
		this.patternGrab_maxX = 1000;
	}
	patternGrab_minX;
	patternGrab_maxX;

	/*ComponentWillMountOrReceiveProps(props) {
		var {visible} = props;
		JavaBridge.Main.OnMonitorChangeVisible(visible);
	}*/

	sidePanel = null;
	ToggleSidePanelOpen() {
		if (this.sidePanel._open)
			this.sidePanel.close();
		else
			this.sidePanel.open();
	}

	render() {
		var {visible} = this.props;
		var node = LL.monitor;
		
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
						{/*temp*/}
						<VButton text="Center" ml10 mt3 style={{width: 100, height: 35}}
							enabled={MuseBridge.status == "connected"} onPress={()=>JavaBridge.Main.CenterEyeTracker()}/>
						<Panel style={{flex: 1}}/>

						{["unknown", "disconnected", "needs_update"].Contains(MuseBridge.status) && node.connect &&
							<Text style={{height: 50, top: 12, textAlignVertical: "top"}}>Searching for muse headband...</Text>}
						{MuseBridge.status == "connecting" &&
							<Text style={{height: 50, top: 12, textAlignVertical: "top"}}>Connecting...</Text>}
						{MuseBridge.status == "connected" &&
							<Text style={{height: 50, top: 12, textAlignVertical: "top"}}>Connected</Text>}
						<Switch style={{height: 50, top: 0, transform: [{translateY: -3}]} as any} value={node.connect}
							onValueChange={value=> {
								node.connect = value;
								if (node.connect)
									MuseBridge.StartSearch(); // start listening for a muse headband
								else
									MuseBridge.Disconnect();
							}}/>
						<VSwitch text="Monitor" ml5 value={node.monitor}
							onValueChange={value=> {
								node.monitor = value;
							}}/>
						<VSwitch text="Pattern match" ml5 value={node.patternMatch}
							onValueChange={value=> {
								node.patternMatch = value;
							}}/>
					</Row>
					<Panel style={{marginTop: -7, flex: 1}}>
						<ChannelsUI {...{visible}} parent={this}/>
					</Panel>
				</Column>
			</Drawer>
		);
	}
}

var didFirstRender = false;
class ChannelsUI extends Component<any, any> {
    render() {
        return (
            <View style={{flex: 1, backgroundColor: colors.background}} accessible={true} accessibilityLabel="chart holder">
            </View>
        );
    }

	PostRender() {
		if (!didFirstRender) {
			didFirstRender = true;
			JavaBridge.Main.AddChart();
			/*DeviceEventEmitter.addListener("PostAddChart", args=> {
				// do one more render, to fix positioning
				this.forceUpdate();
			});*/
		} else {
			JavaBridge.Main.UpdateChartBounds();
		}
	}
}
import Drawer from "react-native-drawer";
import {MKRangeSlider} from 'react-native-material-kit';
var DialogAndroid = require("react-native-dialogs");

import MuseBridge from "../Frame/MuseBridge";

g.Monitor = class Monitor extends Node {
	@O @P() updateInterval = 3;
	@O @P() channel1 = true;
	@O @P() channel2 = true;
	@O @P() channel3 = true;
	@O @P() channel4 = true;

	@O @P() patternGrabber = false;

	@O @P() connect = true;
	@O @P() monitor = true;
	@O @P() patternMatch = true;

	ui = null;
}

import OptionsPanel from "./Monitor/OptionsPanel";

@Observer
@Bind
export class MonitorUI extends BaseComponent {
	constructor(props) {
		super(props);
		LL.monitor.ui = this;
		this.patternGrab_minX = 0;
		this.patternGrab_maxX = 1000;
	}

	/*ComponentWillMountOrReceiveProps(props) {
		var {visible} = props;
		JavaBridge.Main.OnMonitorChangeVisible(visible);
	}*/

	sidePanel = null;
	@Bind ToggleSidePanelOpen() {
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
						<VSwitch text="Pattern grabber" ml5 value={node.patternGrabber}
							onValueChange={value=> {
								node.patternGrabber = value;
								this.forceUpdate();
							}}/>
						{node.patternGrabber &&
							<VButton text="Grab" ml10 mt3 style={{width: 100, height: 35}}
								enabled={MuseBridge.status == "connected"} onPress={this.GrabPattern}/>}
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
						<Switch style={{height: 50, top: 0, transform: [{translateY: -3}]}} value={node.connect}
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
					{node.patternGrabber && 
						<Row style={{height: 50}}>
							<MKRangeSlider min={0} max={1000} style={{flex: 1}}
								minValue={this.patternGrab_minX} maxValue={this.patternGrab_maxX}
								onChange={val=> {
									this.patternGrab_minX = parseInt(val.min);
									this.patternGrab_maxX = parseInt(val.max);
									//this.forceUpdate();
								}}/>
						</Row>}
					<Panel style={{marginTop: -7, flex: 1}}>
						<ChannelsUI {...{visible}} parent={this}/>
					</Panel>
				</Column>
			</Drawer>
		);
	}

	async GrabPattern() {
		var {channelPointsGrabbed: channelPointsGrabbed_rawData, channelBaselines} =
			await JavaBridge.Main.StartPatternGrab(this.patternGrab_minX, this.patternGrab_maxX);
		var channelPointsGrabbed = channelPointsGrabbed_rawData.Select(rawPoints=> {
			return rawPoints.Select(rawPoint=>new Vector2i(rawPoint.x, rawPoint.y));
		})

		var dialog = new DialogAndroid();
		dialog.set({
			title: `Grab pattern`,
			content: `Range is ${this.patternGrab_minX}-${this.patternGrab_maxX}.`,
			items: ["Channel 1", "Channel 2", "Channel 3", "Channel 4"],
			itemsCallbackMultiChoice: (ids, texts)=> {
				if (ids.length == 0) return;

				//Toast(ToJSON(ids) + ";" + ToJSON(texts));
				for (let channelIndex of ids) {
					var pattern = new Pattern("new pattern - channel " + (channelIndex + 1));
					pattern["channel" + (channelIndex + 1)] = true;

					let points = channelPointsGrabbed[channelIndex];
					let minX = points.Select(a=>a.x).Min();
					//let medianY = points.Select(a=>a.y).Median();
					points = points.Select(a=>a.NewX(x=>x - minX).NewY(y=>y - channelBaselines[channelIndex]));
					pattern.points = points;

					LL.settings.patterns.push(pattern);
				}
				
				this.forceUpdate();

				var dialog = new DialogAndroid();
				dialog.set({
					"title": `Choose pattern name`,
					"input": {
						prefill: pattern.name,
						callback: newName=> {
							pattern.name = newName;
							
							if (LL.scripts.ui)
								LL.scripts.ui.forceUpdate();
						}
					},
					"positiveText": "OK",
					//"negativeText": "Cancel"
				});
				dialog.show();
			},
			"positiveText": "OK",
			"negativeText": "Cancel"
		});
		dialog.show();
	}
}

var didFirstRender = false;
class ChannelsUI extends BaseComponent {
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
import Drawer from "react-native-drawer";
import {MKRangeSlider} from 'react-native-material-kit';
var DialogAndroid = require("react-native-dialogs");

import MuseBridge from "../Frame/MuseBridge";

g.Monitor = class Monitor extends Node {
	@P() updateInterval = 3;
	@P() patternGrabber = false;

	@P() connect = true;
	@P() monitor = true;
	@P() patternMatch = true;

	ui = null;
}

import OptionsPanel from "./Monitor/OptionsPanel";

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
				<Panel style={{flex: 1, flexDirection: "column", backgroundColor: colors.background}}>
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
								LL.PushBasicDataToJava();
								if (node.connect)
									MuseBridge.StartSearch(); // start listening for a muse headband
								else
									MuseBridge.Disconnect();
								this.forceUpdate();
							}}/>
						<VSwitch text="Monitor" ml5 value={node.monitor}
							onValueChange={value=> {
								node.monitor = value;
								LL.PushBasicDataToJava();
								this.forceUpdate();
							}}/>
						<VSwitch text="Pattern match" ml5 value={node.patternMatch}
							onValueChange={value=> {
								node.patternMatch = value;
								LL.PushBasicDataToJava();
								this.forceUpdate();
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
					<Panel style={{marginTop: -7, flex: 111222333}}>
						<ChannelsUI {...{visible}} parent={this}/>
					</Panel>
				</Panel>
			</Drawer>
		);
	}

	componentWillUnmount() { 
		Toast("Unmounting...");
	}

	async GrabPattern() {
		var channelPointsGrabbed_rawData = await JavaBridge.Main.StartPatternGrab(this.patternGrab_minX, this.patternGrab_maxX);
		var channelPointsGrabbed = channelPointsGrabbed_rawData.Select(rawPoints=> {
			return rawPoints.Select(rawPoint=>new Vector2i(rawPoint.x, rawPoint.y));
		})

		var dialog = new DialogAndroid();
		dialog.set({
			title: `Grab pattern`,
			content: `Range is ${this.patternGrab_minX}-${this.patternGrab_maxX}.`,
			items: ["Channel 1", "Channel 2", "Channel 3", "Channel 4"],
			itemsCallbackMultiChoice: (ids, texts)=> {
				//Toast(ToJSON(ids) + ";" + ToJSON(texts));
				for (let channelIndex of ids) {
					let pattern = new Pattern("new pattern - channel " + (channelIndex + 1));
					pattern["channel" + (channelIndex + 1)] = true;
					pattern.points = channelPointsGrabbed[channelIndex];
					LL.settings.patterns.push(pattern);
				}

				LL.PushPatternsToJava();
				this.forceUpdate();
			},
			"positiveText": "OK",
			"negativeText": "Cancel"
		});
		dialog.show();
	}
}

//import Chart from "react-native-chart";

const styles = {
    container: {
        flex: 1,
    	backgroundColor: colors.background,
	    //backgroundColor: "red",
	},
    chart: {
        width: 1000,
        height: 500,
    },
};

class ChannelsUI extends BaseComponent {
	data = [
		[0, 1],
		[1, 3],
		[3, 7],
		[4, 9]
	];

	/*shouldComponentUpdate() {
		return false;
	}*/

	/*timer = null;
	componentWillReceiveProps(nextProps, nextState) {
		var visibleChanging = nextProps.visible != this.props.visible;
		if (visibleChanging) {
			if (nextProps.visible) {
				this.timer = new TimerMS(300, ()=> {
					this.forceUpdate();
				});
				this.timer.Start();
			}
			else {
				if (this.timer)
					this.timer.Stop();
			}
		}
	}*/
    render() {
		/*var channelPoints = MuseBridge.channelPoints;
		var channel0Points = channelPoints[0];
		/*if (channel0Points.length == 0)
			return <Panel/>;*#/

		<Chart style={styles.chart} type="line" verticalGridStep={5}
			showDataPoint={false} color="black" data={channel0Points}/>*/

        return (
            <View style={styles.container} accessible={true} accessibilityLabel="chart holder">
            </View>
        );
    }

	PostRender() {
		JavaBridge.Main.UpdateChartBounds();
	}
}
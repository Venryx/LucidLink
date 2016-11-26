import Drawer from "react-native-drawer";

import MuseBridge from "../Frame/MuseBridge";

g.Monitor = class Monitor extends Node {
	@P() updateInterval = 3;

	@P() connect = true;
	@P() monitor = true;

	ui = null;
}

import OptionsPanel from "./Monitor/OptionsPanel";

@Bind
export class MonitorUI extends BaseComponent {
	constructor(props) {
		super(props);
		LL.monitor.ui = this;
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
				<View style={{flex: 1, flexDirection: "column"}}>
					<View style={{flexDirection: "row", flexWrap: "wrap", padding: 3, paddingBottom: 0}}>
						<View style={{flex: .8, flexDirection: "row"}}>
							<VButton text="Options" style={{width: 100}} onPress={this.ToggleSidePanelOpen}/>
							<View style={{flex: 1}}/>

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
							
							<Text style={{marginLeft: 5, height: 50, top: 12, textAlignVertical: "top"}}>Monitor</Text>
							<Switch style={{height: 50, top: 0, transform: [{translateY: -3}]}} value={node.monitor}
								onValueChange={value=> {
									node.monitor = value;
									LL.PushBasicDataToJava();
									this.forceUpdate();
								}}/>
						</View>
					</View>
					<View style={{marginTop: -7, flex: 1}}>
						<ChannelsUI {...{visible}} parent={this}/>
					</View>
				</View>
			</Drawer>
		);
	}

	componentWillUnmount() { 
		V.Toast("Unmounting...");
	}
}

//import Chart from "react-native-chart";

const styles = {
    container: {
        flex: 1,
        backgroundColor: "white",
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
		var channelPoints = MuseBridge.channelPoints;
		var channel0Points = channelPoints[0];
		/*if (channel0Points.length == 0)
			return <View/>;*/

		/*<Chart style={styles.chart} type="line" verticalGridStep={5}
			showDataPoint={false} color="black" data={channel0Points}/>*/

        return (
            <View style={styles.container} accessible={true} accessibilityLabel="chart holder">
            </View>
        );
    }
}
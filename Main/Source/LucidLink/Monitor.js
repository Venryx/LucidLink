import Drawer from "react-native-drawer";

import MuseBridge from "../Frame/MuseBridge";

g.Monitor = class Monitor extends Node {
	@P() connect = true;

	ui = null;
}

import OptionsPanel from "./Monitor/OptionsPanel";

@Bind
export class MonitorUI extends BaseComponent {
	constructor(props) {
		super(props);
		LL.monitor.ui = this;
	}

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
							<View style={{flexDirection: "row", alignItems: "flex-end"}}>
								{["unknown", "disconnected", "needs_update"].Contains(MuseBridge.status) && node.connect &&
									<Text style={{transform: [{translateY: -23}]}}>Searching for muse headband...</Text>}
								{MuseBridge.status == "connecting" &&
									<Text style={{height: 50, top: 8, textAlignVertical: "top"}}>Connecting...</Text>}
								{MuseBridge.status == "connected" &&
									<Text style={{height: 50, top: 8, textAlignVertical: "top"}}>Connected</Text>}
								<Switch style={{transform: [{translateY: -18}]}} value={node.connect}
									onValueChange={value=> {
										node.connect = value;
										if (value)
											MuseBridge.StartSearch(); // start listening for a muse headband
										else {
											MuseBridge.StopSearch();
											if (MuseBridge.status == "connected")
												MuseBridge.Disconnect();
										}
										this.forceUpdate();
									}}/>
							</View>
						</View>
					</View>
					<View style={{marginTop: -7, flex: 1}}>
						<ChannelsUI {...{visible}} parent={this}/>
					</View>
				</View>
			</Drawer>
		);
	}
}

import Chart from "react-native-chart";

const styles = {
    container: {
        flex: 1,
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "white",
    },
    chart: {
        width: 200,
        height: 200,
    },
};

class ChannelsUI extends BaseComponent {
	data = [
		[0, 1],
		[1, 3],
		[3, 7],
		[4, 9]
	];

	timer = null;
	componentDidMount() {
		/*this.data = [];
		for (let )*/
		// todo
	}

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
	}
    render() {
		var channelPoints = MuseBridge.channelPoints;
		var channel0Points = channelPoints[0];
		if (channel0Points.length == 0)
			return <View/>;
        return (
            <View style={styles.container}>
                <Chart style={styles.chart} type="line" verticalGridStep={5}
					showDataPoint={true} color="black" data={channel0Points}/>
            </View>
        );
    }
}
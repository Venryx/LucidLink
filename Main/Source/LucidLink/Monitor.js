import Drawer from "react-native-drawer";

import MuseBridge from "../Frame/MuseBridge";

g.Monitor = class Monitor extends Node {
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
								<VButton text="Listen" style={{width: 100}} enabled={!MuseBridge.started} onPress={this.Listen}/>
								<VButton text="Refresh" style={{marginLeft: 10, width: 100}} enabled={MuseBridge.started} onPress={this.Refresh}/>
								<Text style={{top: 0, textAlignVertical: "top"}}>Found: {MuseBridge.museList.length}</Text>
								<VButton text="Connect" style={{marginLeft: 10, width: 100}} enabled={MuseBridge.museList.length > 0} onPress={this.Connect}/>
							</View>
						</View>
					</View>
					<View style={{marginTop: -7, flex: 1}}>
						<ChannelsUI parent={this}/>
					</View>
				</View>
			</Drawer>
		);
	}

	Listen() {
		MuseBridge.Start();
		this.forceUpdate();
	}
	Refresh() {
		MuseBridge.Refresh();
	}
	Connect() {
		MuseBridge.Connect();
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

	componentDidMount() {
		/*this.data = [];
		for (let )*/
		// todo
	}

    render() {
        return (
            <View style={styles.container}>
                <Chart style={styles.chart} type="line" verticalGridStep={5}
					showDataPoint={true} color="black" data={this.data}/>
            </View>
        );
    }
}
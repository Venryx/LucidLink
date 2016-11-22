import React, {Component} from "react";
import {Dimensions, StyleSheet,
	View, Button, Text, TextInput} from "react-native";
import RNFS from "react-native-fs";
var ScrollableTabView = require("react-native-scrollable-tab-view");
var DialogAndroid = require("react-native-dialogs");
import Drawer from "react-native-drawer";

import EEGBridge from "../Frame/EEGBridge";

g.Monitor = class Monitor extends Node {
}

import OptionsPanel from "./Monitor/OptionsPanel";

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
						</View>
					</View>
					<View style={{marginTop: -7, flex: 1}}>
						<ChannelsUI parent={this}/>
					</View>
				</View>
			</Drawer>
		);
	}
}

import Chart from "react-native-chart";

const styles = StyleSheet.create({
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
});

class ChannelsUI extends BaseComponent {
	data = [];

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
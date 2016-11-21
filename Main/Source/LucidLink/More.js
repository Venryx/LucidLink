import React, {Component} from "react";
import {Dimensions, StyleSheet,
	View, Button, Text, TextInput} from "react-native";
import RNFS from "react-native-fs";
var ScrollableTabView = require("react-native-scrollable-tab-view");
//var {JavaBridge, BaseComponent, VFile} = require("./Globals");

import LogsUI from "./More/LogsUI";
import ConsoleUI from "./More/ConsoleUI";
import AboutUI from "./More/AboutUI";

g.More = class More extends Node {
	// logs
	@P() showLogs_general = true;
	@P() showLogs_keyboard = true;
	@P() showLogs_custom1 = true;
	@P() showLogs_custom2 = true;
	@P() showLogs_custom3 = true;

	@P() maxLogCount = 100;
	@P() autoScroll = true;

	logEntries = [];
	AddLogEntry(entry) {
		entry.type = entry.type.toLowerCase();
		this.logEntries.push(entry);
		if (this.maxLogCount != -1 && this.logEntries.length > this.maxLogCount)
			this.logEntries.splice(0, this.logEntries.length - this.maxLogCount);
		if (this.logsUI && this.logsUI.props.active)
			this.logsUI.forceUpdate();
	}

	
	logsUI = null;

	// console
	@P() jsCode = "";
}

export class MoreUI extends BaseComponent {
	constructor(props) {
		super(props);
		this.state = {activeTab: 0};
	}

	render() {
		var {active} = this.props;
		var {activeTab, scriptTexts} = this.state;
		var node = LL.more;
		return (
			<ScrollableTabView style={{flex: 1}} onChangeTab={data=>this.setState({activeTab: data.i})}>
				<LogsUI tabLabel="Logs" active={active && activeTab == 0}/>
				<ConsoleUI tabLabel="Console" text={node.jsCode} onChangeText={text=>(node.jsCode = text) | this.forceUpdate()}/>
				<AboutUI tabLabel="About"/>
			</ScrollableTabView>
		);
	}

	SelectAudioFileForEntry(entry) {
		// todo
	}

	AddAudioFile() {
		// todo
	}
}
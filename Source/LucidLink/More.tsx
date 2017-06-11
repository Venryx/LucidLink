import {BaseComponent} from '../Frame/ReactGlobals';
import {DN} from "../Frame/Globals";
import RNFS from "react-native-fs";
var Moment = require("moment");
var ScrollableTabView = require("react-native-scrollable-tab-view");
//var {JavaBridge, BaseComponent, VFile} = require("./Globals");
import Node from "../Packages/VTree/Node";

import LogsUI from "./More/LogsUI";
import ConsoleUI from "./More/ConsoleUI";
import OthersUI from "./More/OthersUI";
import AboutUI from "./More/AboutUI";
import {LL} from "../LucidLink";
import {P} from "../Packages/VDF/VDFTypeInfo";

export class LogEntry {
	constructor(type, message, time) {
		this.type = type.toLowerCase();
		this.message = message;
		this.time = time;
	}
	type: string;
	message: string;
	time;
	toString(showMoreInfo = true) {
		/*var dateStr = this.time.toString();
		var dateStartPos = dateStr.IndexOf_X(3, " ") + 1;
		dateStr = dateStr.substring(dateStartPos, dateStr.indexOf(" ", dateStartPos));*/
		if (showMoreInfo) {
			//var dateStr = Moment(this.time).format("YYYY-M-D HH:mm:ss.SSS");
			var dateStr = Moment(this.time).format("D HH:mm:ss.SSS");
			return `[D${dateStr}, ${this.type}] ${this.message}`;
		}
		var dateStr = Moment(this.time).format("HH:mm:ss")
		return `[${dateStr}] ${this.message}`;
	}
}
//alert(window + ";" + global + ";" + eval("g") + ";" + (window === global) + ";" + (global === eval("g"))); 
//alert(window + ";" + global + ";" + (window === global) + ";" + global.g); 
global.Extend({LogEntry});

export class More extends Node {
	// logs
	@P() showLogs_general = true;
	@P() showLogs_keyboard = true;
	@P() showLogs_others = true;

	@P() showMoreInfo = false;
	@P() autoScroll = true;
	@P() maxLogCount = 100;

	static logEntries = [];
	static logEntries_lastLoggedToFile = -1;
	static AddLogEntry(entry) {
		entry.origIndex = More.logEntries.length;
		More.logEntries.push(entry);
		if (LL && LL.tracker.currentSession) {
			for (let i = More.logEntries_lastLoggedToFile + 1; i < More.logEntries.length; i++)
				LL.tracker.currentSession.logFile.AppendText("\n" + More.logEntries[i].toString());
			More.logEntries_lastLoggedToFile = More.logEntries.length - 1;
		}
	}

	// console
	@P() jsCode = "";
}
global.Extend({More});

export class MoreUI extends BaseComponent<any, any> {
	constructor(props) {
		super(props);
		this.state = {activeTab: 0};
	}

	render() {
		var {active} = this.props;
		var {activeTab} = this.state;
		var node = LL.more;
		
		return (
			<ScrollableTabView style={{flex: 1}} onChangeTab={data=>this.setState({activeTab: data.i})}>
				<LogsUI tabLabel="Logs" active={active && activeTab == 0}/>
				<ConsoleUI tabLabel="Console" text={node.jsCode} onChangeText={text=>DN(node.jsCode = text, this.forceUpdate())}/>
				<OthersUI tabLabel="Others"/>
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
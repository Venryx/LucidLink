import {BaseComponent, Panel, VButton} from "../../Frame/ReactGlobals";
import {colors, styles} from "../../Frame/Styles";
import {Log} from "../../Frame/Globals";
import NumberPickerDialog from "react-native-numberpicker-dialog";
import {More} from "../More";
import {Switch, Text, ScrollView as ScrollView0} from "react-native";
var ScrollView: any = ScrollView0;
import {LL} from "../../LucidLink";

export default class LogsUI extends BaseComponent<any, any> {
	constructor(props) {
		super(props);
		LL.more.logsUI = this;
	}

	render() {
		var node = LL.more;

		var logEntriesToShow = [];
		for (var i = More.logEntries.length - 1; i >= 0; i--) {
			var entry = More.logEntries[i];
			var showEntry =
				entry.type == "general" ? node.showLogs_general :
				entry.type == "keyboard" ? node.showLogs_keyboard :
				node.showLogs_others;
			if (showEntry) {
				logEntriesToShow.push(entry);
				if (LL.more.maxLogCount != -1 && logEntriesToShow.length >= LL.more.maxLogCount)
					break;
			}
		}
		logEntriesToShow.reverse();

		var Change = (_: any)=> { this.forceUpdate(); }

		return (
			<Panel style={{flex: 1, flexDirection: "column", backgroundColor: colors.background}}>
				<Panel style={{height: 32, padding: 5, flexDirection: "row"}}>
					<Text style={styles.text}>Log types: </Text>
					<Text style={styles.text}>    General: </Text>
					<Switch value={node.showLogs_general} onValueChange={value=>Change(node.showLogs_general = value)}/>
					<Text style={styles.text}>    Keyboard: </Text>
					<Switch value={node.showLogs_keyboard} onValueChange={value=>Change(node.showLogs_keyboard = value)}/>
					<Text style={styles.text}>    Others: </Text>
					<Switch value={node.showLogs_others} onValueChange={value=>Change(node.showLogs_others = value)}/>
					<Panel style={{flex: 1}}/>
					<Panel style={{flexDirection: "row", alignItems: "flex-end"}}>
						<VButton text="Clear" style={{marginTop: -5, width: 100, height: 32}}
							onPress={()=>Change(More.logEntries.length = 0)}/>
						{/*<VButton text="Refresh" style={{marginLeft: 5, marginTop: -5, width: 100, height: 32}}
							onPress={()=>this.forceUpdate()}/>*/}
					</Panel>
				</Panel>
				<Panel style={{height: 32, padding: 5, flexDirection: "row"}}>
					<Text style={styles.text}>More info: </Text>
					<Switch value={node.showMoreInfo} onValueChange={value=>Change(node.showMoreInfo = value)}/>
					<Text style={{marginLeft: 10}}>Auto-scroll: </Text>
					<Switch value={node.autoScroll} onValueChange={value=>Change(node.autoScroll = value)}/>
					<Text style={{marginLeft: 10}}>Display: </Text>
					<VButton text={node.maxLogCount.toString()} style={{marginLeft: 3, marginTop: -5, width: 100, height: 32}}
						onPress={()=> {
							var values = [-1];
							for (let val = 0; val < 100; val += 10)
								values.push(val);
							for (let val = 100; val < 1000; val += 100)
								values.push(val);
							for (let val = 1000; val <= 10000; val += 1000)
								values.push(val);
							NumberPickerDialog.show({
								selectedValueIndex: values.indexOf(node.maxLogCount),
								values: values.Select(a=>a.toString()),
								positiveButtonLabel: "Ok",
								negativeButtonLabel: "Cancel",
								message: "Select max number of log-entries to show in the logs panel.\n\n(-1 for unlimited)",
								title: "Max log-entry count",
							}).then(id=> {
								if (id == -1) return;
								let val = values[id];
								node.maxLogCount = val;
								this.forceUpdate();
							});
						}}/>
				</Panel>
				<ScrollView ref="scrollView" style={{flex: 1, flexDirection: "column", borderTopWidth: 1}}
						automaticallyAdjustContentInsets={false}
						onContentSizeChange={(contentWidth, contentHeight)=> {
							if (node.autoScroll)
								(this.refs as any).scrollView.scrollTo({x: 0, y: contentHeight, animated: false});
						}}>
					{logEntriesToShow.map(entry=> {
						return <Text key={entry.origIndex} style={styles.text}>{entry.toString(node.showMoreInfo)}</Text>;
					})}
				</ScrollView>
			</Panel>
		);
	}
}
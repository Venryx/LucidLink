import NumberPickerDialog from "react-native-numberpicker-dialog";

export default class LogsUI extends BaseComponent {
	constructor(props) {
		super(props);
		LL.more.logsUI = this;
	}

	render() {
		var node = LL.more;

		var logEntriesToShow = [];
		for (var i = node.logEntries.length - 1; i >= 0; i--) {
			var entry = node.logEntries[i];
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

		return (
			<View style={{flex: 1, flexDirection: "column"}}>
				<View style={{height: 32, padding: 5, flexDirection: "row"}}>
					<Text>Log types: </Text>
					<Text>    General: </Text>
					<Switch value={node.showLogs_general} onValueChange={value=>(node.showLogs_general = value) | this.forceUpdate()}/>
					<Text>    Keyboard: </Text>
					<Switch value={node.showLogs_keyboard} onValueChange={value=>(node.showLogs_keyboard = value) | this.forceUpdate()}/>
					<Text>    Others: </Text>
					<Switch value={node.showLogs_others} onValueChange={value=>(node.showLogs_others = value) | this.forceUpdate()}/>
					<View style={{flex: 1}}/>
					<View style={{flexDirection: "row", alignItems: "flex-end"}}>
						<VButton text="Clear" style={{marginTop: -5, width: 100, height: 32}}
							onPress={()=>(LL.more.logEntries.length = 0) | this.forceUpdate()}/>
						{/*<VButton text="Refresh" style={{marginLeft: 5, marginTop: -5, width: 100, height: 32}}
							onPress={()=>this.forceUpdate()}/>*/}
					</View>
				</View>
				<View style={{height: 32, padding: 5, flexDirection: "row"}}>
					<Text>More info: </Text>
					<Switch value={node.showMoreInfo} onValueChange={value=>(node.showMoreInfo = value) | this.forceUpdate()}/>
					<Text style={{marginLeft: 10}}>Auto-scroll: </Text>
					<Switch value={node.autoScroll} onValueChange={value=>(node.autoScroll = value) | this.forceUpdate()}/>
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
				</View>
				<ScrollView ref="scrollView" style={{flex: 1, flexDirection: "column", borderTopWidth: 1}}
						automaticallyAdjustContentInsets={false}
						onContentSizeChange={(contentWidth, contentHeight)=> {
							if (node.autoScroll)
								this.refs.scrollView.scrollTo({x: 0, y: contentHeight, animated: false});
						}}>
					{logEntriesToShow.map(entry=> {
						return <Text key={entry.origIndex}>{entry.toString(node.showMoreInfo)}</Text>;
					})}
				</ScrollView>
			</View>
		);
	}
}
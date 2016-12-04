import RNFS from "react-native-fs";
var ScrollableTabView = require("react-native-scrollable-tab-view");
var DialogAndroid = require("react-native-dialogs");
import Drawer from "react-native-drawer";
import Moment from "moment";

import DreamUI from "./Journal/DreamUI";

@Bind
class Dream {
	static async Load(file) {
		var vdf = await file.ReadAllText();

		var result = FromVDF(vdf, "Dream");
		result.file = file;
		result.date = Moment(file.NameWithoutExtension);

		return result;
	}

	Save() {
		var vdf = ToVDF(this, false);
		this.file.WriteAllText(vdf);
	}
	Delete(onDone = null) {
		var dialog = new DialogAndroid();
		dialog.set({
			"title": `Delete dream "${this.name}"`,
			"content": `Permanently delete dream?`,
			"positiveText": "OK", "negativeText": "Cancel",
			"onPositive": ()=> {
				LL.journal.loadedDreams.Remove(this);
				this.file.Delete();
				onDone && onDone();
			},
		});
		dialog.show();
	}

	constructor(date) {
		// if called by VDF, do nothing
		if (date == null) return;

		Assert(date instanceof Moment, `Date must be an instance of the Moment class, not ${GetTypeName(date)}.`);
		this.date = date;
		var journalFolder = LL.RootFolder.GetFolder("Journal");
		this.file = journalFolder.GetFile(Moment().format("YYYY-MM-DD HH:mm:ss") + ".vdf");
	}

	file = null;
	date = null;

	@P() name = null;
	@P() text = null;
	@P() lucid = false;
}
g.Dream = Dream;

g.Journal = class Journal extends Node {
	loadedDreams = [];

	async LoadDreamsForMonth(month, onFinish) {
		var journalFolder = LL.RootFolder.GetFolder("Journal");
		var journalFolderExists = await journalFolder.Exists();
		if (!journalFolderExists)
			journalFolder.Create();
		
		var dreamFiles = (await journalFolder.GetFiles()).Where(a=>a.Extension == "vdf");
		dreamFiles = dreamFiles.Where(a=> {
			var dreamTime = Moment(a.NameWithoutExtension);
			var isInMonth = dreamTime >= month && dreamTime < month.AddingMonths(1);
			return isInMonth;
		});
		for (let file of dreamFiles) {
			let alreadyLoaded = this.loadedDreams.Any(a=>a.file.path == file.path);
			if (!alreadyLoaded) {
				let dream = await Dream.Load(file);
				this.loadedDreams.push(dream);
			}
		}
		onFinish && onFinish();
	}
	GetLoadedDreamsForMonth(month) {
		return this.loadedDreams.Where(a=> {
			return a.date >= month && a.date < month.AddingMonths(1);
		});
	}
}

@Bind
export class JournalUI extends BaseComponent {
	state = {month: Moment(new Date().MonthDate)};

	componentDidMount() {
		var {month} = this.state;
		LL.journal.LoadDreamsForMonth(month, ()=>this.forceUpdate());
	}

	ShiftMonth(amount) {
		var {month} = this.state;
		this.setState({month: month.AddingMonths(amount)});
	}

	render() {
		var {month, dreams, openDream} = this.state;
		var node = LL.journal;

		if (openDream) {
			return <DreamUI dream={openDream} onBack={(save = true)=> {
				this.setState({openDream: null});
				if (save)
					openDream.Save();
			}}/>
		}

		var entriesForMonth = LL.journal.GetLoadedDreamsForMonth(month);

		return (
			<Column>
				<Row>
					<VButton text="<" style={{width: 100}} onPress={()=>this.ShiftMonth(-1)}/>
					<VButton text={Moment(month).format("MMMM, YYYY")} style={{flex: 1, marginLeft: 5, marginRight: 5}}
						onPress={async ()=> {
							const {action, year, month: month2, day} = await DatePickerAndroid.open({
								date: month.toDate()
							});
							if (action == DatePickerAndroid.dismissedAction) return;
							this.setState({month: month.Clone().set({year, month: month2})});
						}}/>
					<VButton text=">" style={{width: 100}} onPress={()=>this.ShiftMonth(1)}/>
				</Row>
				<ScrollView ref="scrollView" style={{flex: 1, flexDirection: "column", borderTopWidth: 1}}
						automaticallyAdjustContentInsets={false}>
					{entriesForMonth.Reversed().map((dream, index)=> {
						return <DreamHeaderUI key={index} parent={this} dream={dream} index={index}/>;
					})}
				</ScrollView>
				<VButton text="+"
					style={{position: "absolute", right: 30, bottom: 30, width: 50, height: 50, borderRadius: 50}}
					textStyle={{fontSize: 23}}
					onPress={this.CreateDream}/>
			</Column>
		);
	}

	CreateDream() {
		var dream = new Dream(Moment());
		LL.journal.loadedDreams.push(dream);
		this.setState({openDream: dream});
	}
}

class DreamHeaderUI extends BaseComponent {
	render() {
		var {parent, dream, index, style} = this.props;
		return (
			<TouchableOpacity
					style={E({backgroundColor: "#555", height: 100, borderRadius: 10, padding: 7}, index != 0 && {marginTop: 5})}
					onPress={()=>parent.setState({openDream: dream})}>
				<Text style={{fontSize: 18}}>{dream.name || "[no title]"}</Text>
				<Text style={{marginTop: -25, fontSize: 18, textAlign: "right"}}>{dream.date.format("YYYY-MM-DD, HH:mm")}</Text>
				{dream.lucid &&
					<Text style={{position: "absolute", right: 7, fontSize: 18, textAlign: "right", color: "#0F0"}}>Lucid</Text>}
				<Text>{dream.text}</Text>
			</TouchableOpacity>
		);
	}
}
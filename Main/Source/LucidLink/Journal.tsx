import {BaseComponent as Component, Column, Row, VButton, BaseProps} from "../Frame/ReactGlobals";
import DialogAndroid from "react-native-dialogs";
import Moment from "moment";

import DreamUI from "./Journal/DreamUI";
import {FromVDF, GetTypeNameOf, ToVDF, E, Toast} from "../Frame/Globals";
import {DatePickerAndroid, ScrollView, TouchableOpacity, Text} from "react-native";
import Node from "../Packages/VTree/Node";
import {LL} from "../LucidLink";
import {P} from "../Packages/VDF/VDFTypeInfo";
import {File} from "../Packages/V/VFile";
import {autorun} from "mobx";
import {observer} from "mobx-react/native";
import {Assert} from "../Frame/General/Assert";

export class Dream extends Node {
	static async Load(file) {
		var vdf = await file.ReadAllText();

		var result = FromVDF(vdf, "Dream");
		result.file = file;
		result.date = Moment(file.NameWithoutExtension);

		return result;
	}

	async Save() {
		var vdf = ToVDF(this, false);
		var oldFile = this.file;
		this.file = this.NewFile;
		await this.file.WriteAllText(vdf);

		if (oldFile && oldFile.Path != this.file.Path)
			oldFile.Delete();
		this.fileOutdated = false;

		if (this.Draft) {
			LL.journal.draftDream = null;
			LL.journal.loadedDreams.push(this);
		}
	}
	Delete(onDone = null) {
		var dialog = new DialogAndroid();
		dialog.set({
			"title": `Delete dream "${this.name}"`,
			"content": `Permanently delete dream?`,
			"positiveText": "OK", "negativeText": "Cancel",
			"onPositive": ()=> {
				if (this.Draft) {
					LL.journal.draftDream = null;
				} else {
					LL.journal.loadedDreams.Remove(this);
					this.file.Delete();
				}
				onDone && onDone();
			},
		});
		dialog.show();
	}

	constructor(date) {
		super();
		// if called by VDF, do nothing
		if (date == null) return;

		Assert(date instanceof Moment, `Date must be an instance of the Moment class, not ${GetTypeNameOf(date)}.`);
		this.date = date;
		this.file = this.NewFile;
	}

	file: File = null; // can become outdated (until next save) if dream date is changed (or is draft dream)
	get NewFile() {
		var journalFolder = LL.RootFolder.GetFolder("Journal");
		return journalFolder.GetFile(this.date.format("YYYY-MM-DD HH:mm:ss") + ".vdf");
	}
	@O fileOutdated = false;
	date = null;

	@P() @O name = null;
	@P() @O text = null;
	@P() @O lucid = false;

	//@P() @O draft = false;
	get Draft() {
		return LL.journal.draftDream == this;
	}
}
g.Extend({Dream});

export class Journal extends Node {
	constructor() {
		super();
		autorun(()=> {
			// if we're loading draft-dream from last session, restore data to correct state (since info lost)
			if (this.draftDream && this.draftDream.date == null) {
				this.draftDream.date = Moment();
				this.draftDream.fileOutdated = true;
			}
		});
	}

	@P() @O draftDream: Dream = null;
	OnKeyDown(androidKeyCode: number, char: string) {
		/*var isNumber = keyCode >= 7 && keyCode <= 16; // 0-9: code 7-16
		var isLetter = keyCode >= 29 && keyCode <= 54; // a-z: code 29-54
		//var isOtherTypingChar = [55, 56].Contains(keyCode);
		var isOtherTypingChar = [",", ".", "'", "\"", "[", "]", "{", "}", "(", ")", "-", "_"].Contains(keyCode);
		if (isNumber || isLetter || isOtherTypingChar) {*/

		var specialChars = [24, 25, 4, 82]; // volume up, volume down, back, options
		if (specialChars.Contains(androidKeyCode)) return;

		var isBackspace = androidKeyCode == 67;
		if (isBackspace && this.draftDream.text.length > 0) {
			this.draftDream.text = this.draftDream.text.substr(0, this.draftDream.text.length - 1);
			this.draftDream.fileOutdated = true;
			return;
		}

		// if normal char (see: www.asciitable.com)
		var charCode = char.charCodeAt(0);
		var basicChar = charCode >= 32 && charCode <= 126;
		var otherNormalChar = ["\n"].Contains(char);
		if (basicChar || otherNormalChar) {
			if (this.draftDream == null)
				this.draftDream = new Dream(Moment()).Init({draft: true, text: ""});
			this.draftDream.text += char;
			this.draftDream.fileOutdated = true;
			//Toast(`Text (${this.draftDream.text.length}, ${charCode}):` + this.draftDream.text);
		}
	}

	loadedDreams: Dream[] = [];

	async LoadDreamsForMonth(month, onFinish) {
		var journalFolder = LL.RootFolder.GetFolder("Journal");
		var journalFolderExists = await journalFolder.Exists();
		if (!journalFolderExists)
			journalFolder.Create();
		
		var dreamFiles = (await journalFolder.GetFiles()).Where(a=>a.Extension == "vdf");
		dreamFiles = dreamFiles.Where(a=> {
			var dreamTime = Moment(a.NameWithoutExtension);
			var isInMonth = dreamTime >= month && dreamTime < month.clone().add(1, "month");
			return isInMonth;
		});
		for (let file of dreamFiles) {
			let alreadyLoaded = this.loadedDreams.Any(a=>a.file.Path == file.Path);
			if (!alreadyLoaded) {
				let dream = await Dream.Load(file);
				this.loadedDreams.push(dream);
			}
		}
		this.loadedDreams = this.loadedDreams.OrderBy(a=>a.date); // make sure ordered by date
		onFinish && onFinish();
	}
	GetLoadedDreamsForMonth(month) {
		return this.loadedDreams.Where(a=> {
			return a.date >= month && a.date < month.clone().add(1, "month");
		});
	}
}
g.Extend({Journal});

@observer
export class JournalUI extends Component<{} & BaseProps, {month?, openDream?}> {
	state = {month: Moment(new Date().MonthDate), openDream: null};

	loadedMonths = [];
	ComponentDidMountOrUpdate() {
		var {month} = this.state;
		if (this.loadedMonths.Contains(month)) return;
		LL.journal.LoadDreamsForMonth(month, ()=>this.forceUpdate());
		this.loadedMonths.push(month);
	}

	ShiftMonth(amount) {
		var {month} = this.state;
		this.setState({month: month.clone().add(amount, "months")});
	}

	render() {
		var {month, openDream} = this.state;
		var node = LL.journal;

		if (openDream) {
			return <DreamUI dream={openDream} onBack={()=> {
				this.setState({openDream: null});
			}}/>
		}

		var entriesForMonth = LL.journal.GetLoadedDreamsForMonth(month);
		if (node.draftDream)
			entriesForMonth.push(node.draftDream);

		return (
			<Column>
				<Row style={{padding: 3}}>
					<VButton text="<" style={{width: 100}} onPress={()=>this.ShiftMonth(-1)}/>
					<VButton text={Moment(month).format("MMMM, YYYY")} style={{flex: 1, marginLeft: 5, marginRight: 5}}
						onPress={async ()=> {
							const {action, year, month: month2, day} = await DatePickerAndroid.open({
								date: month.toDate()
							});
							if (action == DatePickerAndroid.dismissedAction) return;
							this.setState({month: month.clone().set({year, month: month2})});
						}}/>
					<VButton text=">" style={{width: 100}} onPress={()=>this.ShiftMonth(1)}/>
				</Row>
				<ScrollView style={{flex: 1, flexDirection: "column", borderTopWidth: 1}}
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

@observer
class DreamHeaderUI extends Component<{parent, dream: Dream, index, style?}, {}> {
	render() {
		var {parent, dream, index, style} = this.props;
		return (
			<TouchableOpacity
					style={E({backgroundColor: "#555", height: 100, borderRadius: 10, padding: 7}, index != 0 && {marginTop: 5})}
					onPress={()=>parent.setState({openDream: dream})}>
				<Row style={{marginTop: -3}}>
					{dream.name && <Text style={{marginRight: 5, fontSize: 18}}>{dream.name}</Text>}
					{dream.Draft && <Text style={{marginRight: 5, fontSize: 18, color: "rgb(255,255,100)"}}>(draft)</Text>}
					{!dream.Draft && dream.fileOutdated &&
						<Text style={{marginRight: 5, fontSize: 18, color: "rgb(255,100,100)"}}>(unsaved changes)</Text>}
				</Row>
				<Text style={{marginTop: dream.name || dream.Draft || dream.fileOutdated ? -25 : 0, fontSize: 18, textAlign: "right"}}>
					{dream.date.format("YYYY-MM-DD, HH:mm")}
				</Text>
				{dream.lucid &&
					<Text style={{position: "absolute", right: 7, top: 25, fontSize: 18, textAlign: "right", color: "#0F0"}}>Lucid</Text>}
				<Text>{dream.text}</Text>
			</TouchableOpacity>
		);
	}
}
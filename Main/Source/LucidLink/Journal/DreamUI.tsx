import {BaseComponent as Component, Column, Row, VButton, Panel} from "../../Frame/ReactGlobals";
import ActionBar from "react-native-action-bar";
import {TextInput, DatePickerAndroid, TimePickerAndroid} from "react-native";
import {DN} from "../../Frame/Globals";
import {VSwitch, VSwitch_Auto} from "../../Packages/ReactNativeComponents/VSwitch";
import Moment from "moment";
import {Dream} from "../Journal";
import {LL} from "../../LucidLink";
import {observer} from "mobx-react/native";

@observer
export default class DreamUI extends Component<{onBack: Function, dream: Dream}, {}> {
	render() {
		var {onBack, dream} = this.props;
		return (
			<Column>
				{/*<ActionBar backgroundColor="#3B373C" leftText="Back" onLeftPress={onBack} //title={dream.name}
					rightText="X" onRightPress={()=> {
						if (dream.draft) {
							LL.journal.draftDream = null;
							onBack(false);
						} else {
							dream.Delete(()=>onBack(false));
						}
					}}/>*/}
				<Row style={{padding: 3, height: 56, backgroundColor: "#303030"}}>
					<VButton text="Back" style={{width: 100}} onPress={()=> {
						onBack();
					}}/>
					<Panel style={{flex: 1}}/>
					<VButton text="Save" mr10 style={{width: 100}} enabled={dream.fileOutdated} onPress={()=> {
						dream.Save();
					}}/>
					<VButton text="Delete" style={{width: 100}} onPress={()=> {
						dream.Delete(onBack);
					}}/>
				</Row>
				<Row>
					<TextInput ref="dateInput" style={{flex: .8}} value={dream.date.format("YYYY-MM-DD (MMMM Do)")}
						onFocus={async ()=> {
							(this.refs as any).dateInput.blur();
							const {action, year, month, day} = await DatePickerAndroid.open({
								date: dream.date.toDate()
							});
							if (action == DatePickerAndroid.dismissedAction) return;
							/*dream.date = dream.date.clone();
							dream.date.set({year, month: month, day});*/
							var date = new Date(year, month, day);
							dream.date = Moment(date);
							dream.fileOutdated = true;
						}}/>
					<TextInput ref="timeInput" style={{flex: .2}} value={dream.date.format("HH:mm (h:mma)")}
						onFocus={async ()=> {
							(this.refs as any).timeInput.blur();
							const {action, hour, minute} = await TimePickerAndroid.open({
								hour: dream.date.get("hour"), minute: dream.date.get("minute"), is24Hour: true
							});
							if (action == DatePickerAndroid.dismissedAction) return;
							dream.date = dream.date.clone().set({hour, minute});
							dream.fileOutdated = true;
						}}/>
					<VSwitch_Auto text="Lucid" path={()=>dream.p.lucid} containerStyle={{marginTop: 15}}/>
				</Row>
				<Row>
					<TextInput style={{flex: 1}} editable={true} value={dream.name}
						onChangeText={text=> {
							dream.name = text;
							dream.fileOutdated = true;
						}}/>
				</Row>
				<Row style={{flex: 1}}>
					<TextInput style={{flex: 1, textAlignVertical: "top"}} editable={true} multiline={true} value={dream.text}
						onChangeText={text=> {
							dream.text = text;
							dream.fileOutdated = true;
						}}/>
				</Row>
			</Column>
		);
	}
}
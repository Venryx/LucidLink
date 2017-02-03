import {BaseComponent, Column, Row} from "../../Frame/ReactGlobals";
import ActionBar from "react-native-action-bar";
import {TextInput, DatePickerAndroid, TimePickerAndroid} from "react-native";
import {DN} from "../../Frame/Globals";
import {VSwitch, VSwitch_Auto} from "../../Packages/ReactNativeComponents/VSwitch";
var Moment = require("moment");

export default class DreamUI extends BaseComponent<any, any> {
	render() {
		var {onBack, dream} = this.props;
		return (
			<Column>
				<ActionBar backgroundColor="#3B373C" leftText="Back" onLeftPress={onBack} //title={dream.name}
					rightText="X" onRightPress={()=>dream.Delete(()=>onBack(false))}/>
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
							this.forceUpdate();
						}}/>
					<TextInput ref="timeInput" style={{flex: .2}} value={dream.date.format("HH:mm (h:mma)")}
						onFocus={async ()=> {
							(this.refs as any).timeInput.blur();
							const {action, hour, minute} = await TimePickerAndroid.open({
								hour: dream.date.get("hour"), minute: dream.date.get("minute"), is24Hour: true
							});
							if (action == DatePickerAndroid.dismissedAction) return;
							dream.date = dream.date.clone().set({hour, minute});
							this.forceUpdate();
						}}/>
					<VSwitch_Auto text="Lucid" path={()=>dream.lucid} onValueChange={this.Update}/>
				</Row>
				<Row>
					<TextInput style={{flex: 1}} editable={true} value={dream.name}
						onChangeText={text=> {
							dream.name = text;
							this.forceUpdate();
						}}/>
				</Row>
				<Row style={{flex: 1}}>
					<TextInput style={{flex: 1, textAlignVertical: "top"}} editable={true} multiline={true} value={dream.text}
						onChangeText={text=> {
							dream.text = text;
							this.forceUpdate();
						}}/>
				</Row>
			</Column>
		);
	}
}
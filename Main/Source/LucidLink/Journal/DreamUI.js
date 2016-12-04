import ActionBar from "react-native-action-bar";
import Moment from "moment";

export default class DreamUI extends BaseComponent {
	render() {
		var {onBack, dream} = this.props;
		var Change = ()=>this.forceUpdate();
		return (
			<Column>
				<ActionBar backgroundColor="#3B373C" leftText="Back" onLeftPress={onBack} //title={dream.name}
					rightText="X" onRightPress={()=>dream.Delete(()=>onBack(false))}/>
				<Row>
					<TextInput ref="dateInput" style={{flex: .8}} value={dream.date.format("YYYY-MM-DD (MMMM Do)")}
						onFocus={async ()=> {
							this.refs.dateInput.blur();
							const {action, year, month, day} = await DatePickerAndroid.open({
								date: dream.date.toDate()
							});
							if (action == DatePickerAndroid.dismissedAction) return;
							/*dream.date = dream.date.Clone();
							dream.date.set({year, month: month, day});*/
							var date = new Date(year, month, day);
							dream.date = Moment(date);
							this.forceUpdate();
						}}/>
					<TextInput ref="timeInput" style={{flex: .2}} value={dream.date.format("HH:mm (h:mma)")}
						onFocus={async ()=> {
							this.refs.timeInput.blur();
							const {action, hour, minute} = await TimePickerAndroid.open({
								hour: dream.date.get("hour"), minute: dream.date.get("minute"), is24Hour: true
							});
							if (action == DatePickerAndroid.dismissedAction) return;
							dream.date = dream.date.Clone().set({hour, minute});
							this.forceUpdate();
						}}/>
					<VSwitch text="Lucid" value={dream.lucid} onValueChange={value=>Change(dream.lucid = value)}/>
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
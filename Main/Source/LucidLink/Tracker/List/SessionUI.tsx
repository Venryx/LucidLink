import {ToJSON} from '../../../Frame/Globals';
import {Text, TextInput, ScrollView} from "react-native"
import {BaseComponent, Column, Row} from '../../../Frame/ReactGlobals';
import ActionBar from "react-native-action-bar";
var Moment = require("moment");

export default class SessionUI extends BaseComponent {
	render() {
		var {onBack, session} = this.props;

		var isCurrentSession = session == LL.tracker.currentSession;
		return (
			<Column>
				<ActionBar backgroundColor="#3B373C" leftText="Back" onLeftPress={onBack} //title={session.name}
					rightText={isCurrentSession ? null : "X"} onRightPress={()=> {
						if (isCurrentSession) {
							//alert("Cannot delete the current session. (restart app first)");
							return;
						}
						session.Delete(()=>onBack(false));
					}}/>
				<Row>
					<TextInput ref="dateInput" editable={false} style={{flex: .8}} value={session.date.format("YYYY-MM-DD (MMMM Do)")}/>
					<TextInput ref="timeInput" editable={false} style={{flex: .2}} value={session.date.format("HH:mm (h:mma)")}/>
				</Row>
				<Text>Events:</Text>
				<ScrollView style={{flex: 1, flexDirection: "column", borderTopWidth: 1}}
						automaticallyAdjustContentInsets={false}>
					{session.events.map((event, index)=> {
						return <EventUI key={index} parent={this} event={event} index={index}/>;
					})}
				</ScrollView>
			</Column>
		);
	}
}

class EventUI extends BaseComponent {
	render() {
		var {event} = this.props;
		return (
			<Row>
				<Text style={{flex: .15}}>{event.date.format("HH:mm:ss.SSS")}</Text>
				<Text style={{flex: .15}}>{event.type}</Text>
				<Text style={{flex: .7}}>{ToJSON(event.args)}</Text>
			</Row>
		);
	}
}
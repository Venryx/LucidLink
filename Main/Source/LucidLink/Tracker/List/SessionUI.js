import ActionBar from "react-native-action-bar";
import Moment from "moment";

export default class SessionUI extends BaseComponent {
	render() {
		var {onBack, session} = this.props;
		return (
			<Column>
				<ActionBar backgroundColor="#3B373C" leftText="Back" onLeftPress={onBack} //title={session.name}
					rightText="X" onRightPress={()=> {
						if (session == LL.tracker.currentSession) {
							alert("Cannot delete the current session. (restart app first)");
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
			<Text>{event.type} | {ToJSON(event.args)}</Text>
		);
	}
}
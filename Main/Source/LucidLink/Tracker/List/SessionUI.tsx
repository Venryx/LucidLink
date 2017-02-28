import {ToJSON} from "../../../Frame/Globals";
import {Text, TextInput, ScrollView} from "react-native"
import {BaseComponent as Component, Column, Row} from "../../../Frame/ReactGlobals";
import ActionBar from "react-native-action-bar";
import {LL} from "../../../LucidLink";
import {Session, Event} from "../Session";
import SleepSession from "../Session/SleepSession";
import {SleepSegment} from "../Session/SleepSession";
import {colors} from "../../../Frame/Styles";
import {VTextInput} from "../../../Packages/ReactNativeComponents/VTextInput";
var Moment = require("moment");

export default class SessionUI extends Component<{onBack: Function, session: Session}, any> {
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
						session.Delete(true, ()=>onBack(false));
					}}/>
				<Row>
					<VTextInput ref="dateInput" editable={false} style={{flex: .8}} value={session.date.format("YYYY-MM-DD (MMMM Do)")}/>
					<VTextInput ref="timeInput" editable={false} style={{flex: .2}} value={session.date.format("HH:mm (h:mma)")}/>
				</Row>
				<Text>Events:</Text>
				<ScrollView style={{flex: 1, flexDirection: "column", borderTopWidth: 1}}
						automaticallyAdjustContentInsets={false}>
					{session.events.map((event, index)=> {
						return <EventUI key={index} event={event}/>;
					})}
				</ScrollView>
				<Text>Sleep sessions:</Text>
				<ScrollView style={{flex: 1, flexDirection: "column", borderTopWidth: 1}}
						automaticallyAdjustContentInsets={false}>
					{session.sleepSessions.map((session, index)=> {
						return <SleepSessionUI key={index} session={session}/>;
					})}
				</ScrollView>
			</Column>
		);
	}
}

class EventUI extends Component<{event: Event}, {}> {
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

class SleepSessionUI extends Component<{session: SleepSession}, {}> {
	render() {
		var {session} = this.props;
		return (
			<Column ml={10} style={{backgroundColor: colors.background_dark}}>
				<Text style={{flex: .15}}>Time-span: {session.startTime.format("HH:mm:ss")} to {
					session.endTime ? session.endTime.format("HH:mm:ss") : "now (still active)"}</Text>
				<Text style={{flex: .7}}>Segments:</Text>
				<Column>
					{session.segments.map((segment, index)=>{
						return <SegmentUI key={index} session={session} segment={segment}/>
					})}
				</Column>
			</Column>
		);
	}
}
class SegmentUI extends Component<{session: SleepSession, segment: SleepSegment}, {}> {
	render() {
		var {session, segment} = this.props;
		/*var endTime;
		if (segment == session.segments.Last())
			endTime = session.Active ? Moment() : session.endTime;
		else
			endTime = session.segments[session.segments.indexOf(segment) + 1];*/		
		return (
			<Row ml={10}>
				<Text style={{flex: .5}}>Stage: {segment.stage.name}</Text>
				{/*<Text style={{flex: .5}}>Time-span: {segment.startTime.format("HH:mm:ss")} to {endTime.format("HH:mm:ss")}</Text>*/}
				<Text style={{flex: .5}}>Start time: {segment.startTime.format("HH:mm:ss")}</Text>
			</Row>
		);
	}
}
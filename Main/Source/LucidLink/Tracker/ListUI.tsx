import {BaseComponent, Column, Row, VButton} from '../../Frame/ReactGlobals';
import {Observer, observer} from "mobx-react/native";
var Moment = require("moment");

import SessionUI from "./List/SessionUI";
import { DatePickerAndroid, ScrollView, TouchableOpacity, Text } from 'react-native';
import { E } from '../../Frame/Globals';
import Bind from "autobind-decorator";
import {LL} from "../../LucidLink";

@observer
@Bind
export default class ListUI extends BaseComponent {
	state = {month: Moment(new Date().MonthDate), openSession: null};

	ComponentDidMountOrUpdate() {
		var {month} = this.state;
		LL.tracker.LoadSessionsForRange(month, month.clone().add(1, "month"));
	}

	ShiftMonth(amount) {
		var {month} = this.state;
		this.setState({month: month.clone().add(amount, "months")});
	}

	render() {
		var {month, openSession} = this.state;
		var node = LL.tracker;

		if (openSession) {
			return <SessionUI session={openSession} onBack={(save = true)=> {
				this.setState({openSession: null});
				// for now, we can't change anything in the UI, so don't save
				/*if (save)
					openSession.Save();*/
			}}/>
		}

		var sessions = LL.tracker.GetLoadedSessionsForRange(month, month.clone().add(1, "month"));

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
							this.setState({month: month.clone().set({year, month: month2})});
						}}/>
					<VButton text=">" style={{width: 100}} onPress={()=>this.ShiftMonth(1)}/>
				</Row>
				<ScrollView style={{flex: 1, flexDirection: "column", borderTopWidth: 1}}
						automaticallyAdjustContentInsets={false}>
					{sessions.Reversed().map((session, index)=> {
						return <SessionHeaderUI key={index} parent={this} session={session} index={index}/>;
					})}
				</ScrollView>
			</Column>
		);
	}
}

@observer
class SessionHeaderUI extends BaseComponent {
	render() {
		var {parent, session, index, style} = this.props;
		return (
			<TouchableOpacity
					style={E({backgroundColor: "#555", height: 100, borderRadius: 10, padding: 7}, index != 0 && {marginTop: 5})}
					onPress={()=>parent.setState({openSession: session})}>
				<Text style={{fontSize: 18}}>Session</Text>
				<Text style={{marginTop: -25, fontSize: 18, textAlign: "right"}}>{session.date.format("YYYY-MM-DD, HH:mm")}</Text>
				<Text>Event count: {session.events.length}</Text>
			</TouchableOpacity>
		);
	}
}
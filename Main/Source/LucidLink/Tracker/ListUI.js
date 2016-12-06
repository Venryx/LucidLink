import Moment from "moment";

import SessionUI from "./List/SessionUI";

@Observer
@Bind
export default class ListUI extends BaseComponent {
	state = {month: Moment(new Date().MonthDate)};

	loadedMonths = [];
	ComponentDidMountOrUpdate() {
		var {month} = this.state;
		if (this.loadedMonths.Contains(month)) return;
		LL.tracker.LoadSessionsForMonth(month);
		this.loadedMonths.push(month);
	}

	ShiftMonth(amount) {
		var {month} = this.state;
		this.setState({month: month.AddingMonths(amount)});
	}

	render() {
		var {month, openSession} = this.state;
		var node = LL.tracker;

		if (openSession) {
			return <SessionUI session={openSession} onBack={(save = true)=> {
				this.setState({openSession: null});
				if (save)
					openSession.Save();
			}}/>
		}

		var entriesForMonth = LL.tracker.GetLoadedSessionsForMonth(month);

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
				<ScrollView style={{flex: 1, flexDirection: "column", borderTopWidth: 1}}
						automaticallyAdjustContentInsets={false}>
					{entriesForMonth.Reversed().map((session, index)=> {
						return <SessionHeaderUI key={index} parent={this} session={session} index={index}/>;
					})}
				</ScrollView>
			</Column>
		);
	}
}

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
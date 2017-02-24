import {Assert} from "../../../Frame/General/Assert";
import {GraphRow, Segment} from "./GraphRow";
import {BaseComponent as Component, Row, Panel} from "../../../Frame/ReactGlobals";
import {E, Range, Notify} from "../../../Frame/Globals";
import {Event} from "../Session";
import Moment from "moment";
import {SleepStage} from "../../../Frame/SPBridge";
import {SleepSegment} from "../Session/SleepSession";
import SleepSession from "../Session/SleepSession";
import {LL} from "../../../LucidLink";
import {View} from "react-native";

export default class SleepSegmentsUI extends Component<
		{startTime: Moment.Moment, endTime: Moment.Moment,
			sleepSessions: SleepSession[], style?}, {}> {
	render() {
		let {startTime, endTime, sleepSessions, style} = this.props;

		// for testing
		/*let session = new SleepSession();
		session.segments.push(new SleepSegment(session, SleepStage.V.Absent).VSet({startTime: Moment().add(-5, "hours")}));
		session.segments.push(new SleepSegment(session, SleepStage.V.Wake).VSet({startTime: Moment().add(-4, "hours")}));
		session.segments.push(new SleepSegment(session, SleepStage.V.Light).VSet({startTime: Moment().add(-3, "hours")}));
		session.segments.push(new SleepSegment(session, SleepStage.V.Deep).VSet({startTime: Moment().add(-2, "hours")}));
		session.segments.push(new SleepSegment(session, SleepStage.V.Rem).VSet({startTime: Moment().add(-1, "hours")}));
		sleepSegments = session.segments;*/

		if (1)
			return <View style={{backgroundColor: 'blue', height: '50%' as any, width: '50%' as any}}/>;

		return (
			<Row style={E({position: "absolute", width: "100%", height: "100%"}, style)}>
				{sleepSessions.map((session, index)=> {
					let sessionStart_posPercent = session.startTime.diff(startTime) / endTime.diff(startTime);
					let sessionWidth_sizePercent = session.endTime.diff(session.startTime) / endTime.diff(startTime);

					let sleepSegments = session.segments.Where(a=> {
						return a.startTime >= startTime && a.EndTime < endTime;
					});
					return (
						<Row key={index}
								style={E({
									position: "absolute", height: "100%",
									left: sessionStart_posPercent + "%",
									width: sessionWidth_sizePercent + "%",
									backgroundColor: "rgba(255,255,255,.1)",
								}, style)}
								touchable={true}
								onPress={()=> {
									LL.tracker.openSleepSession = session;
								}}>
							{sleepSegments.map((segment, index)=> {
								let segmentStart_posPercent = segment.startTime.diff(startTime) / endTime.diff(startTime);
								let segmentWidth_sizePercent = segment.EndTime.diff(segment.startTime) / endTime.diff(startTime);
								return (
									<Panel key={index} style={E({position: "absolute", bottom: 0,
										height: segment.Height + "%",
										left: (segmentStart_posPercent - sessionStart_posPercent) + "%",
										width: segmentWidth_sizePercent + "%",
										backgroundColor: segment.Color})}/>
								);
							})}
						</Row>
					);
				})}
			</Row>
		);
	}
}
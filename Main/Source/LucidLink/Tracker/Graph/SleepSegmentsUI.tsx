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
			sleepSessions: SleepSession[], height: number | string, clickable?: boolean, style?}, {}> {
	static defaultProps = {clickable: true};
	render() {
		let {startTime, endTime, sleepSessions, height, clickable, style} = this.props;

		// for testing
		/*let session = new SleepSession();
		session.segments.push(new SleepSegment(session, SleepStage.V.Absent).VSet({startTime: Moment().add(-5, "hours")}));
		session.segments.push(new SleepSegment(session, SleepStage.V.Wake).VSet({startTime: Moment().add(-4, "hours")}));
		session.segments.push(new SleepSegment(session, SleepStage.V.Light).VSet({startTime: Moment().add(-3, "hours")}));
		session.segments.push(new SleepSegment(session, SleepStage.V.Deep).VSet({startTime: Moment().add(-2, "hours")}));
		session.segments.push(new SleepSegment(session, SleepStage.V.Rem).VSet({startTime: Moment().add(-1, "hours")}));
		sleepSegments = session.segments;*/

		return (
			<Row style={E({position: "absolute", width: "100%", height}, style)}>
				{sleepSessions.map((session, index)=> {
					let rowLength = endTime.diff(startTime);
					let sessionStart_posPercent = session.startTime.diff(startTime) / rowLength;
					let sessionWidth_sizePercent = session.endTime.diff(session.startTime) / rowLength;

					let sleepSegments = session.segments.Where(a=> {
						//return a.endTime >= startTime && a.startTime < endTime;
						return a.EndTime > startTime && a.startTime < endTime;
					});
					return (
						<Row key={index}
								style={E({
									position: "absolute", height: "100%",
									left: sessionStart_posPercent.ToPercentStr(),
									width: sessionWidth_sizePercent.ToPercentStr(),
									backgroundColor: "rgba(255,255,255,.1)",
								}, style)}
								touchable={clickable}
								onPress={()=> {
									LL.tracker.openSleepSession = session;
								}}>
							{sleepSegments.map((segment, index)=> {
								let sessionLength = session.endTime.diff(session.startTime);
								let segmentStart_posPercent = segment.startTime.diff(session.startTime) / sessionLength;
								if (segmentStart_posPercent >= 1) return null;
								let segmentWidth_sizePercent = segment.EndTime.diff(segment.startTime) / sessionLength;
								if (segmentStart_posPercent + segmentWidth_sizePercent > 1)
									segmentWidth_sizePercent = 1 - segmentStart_posPercent;
								return (
									<Panel key={index} style={E({position: "absolute", bottom: 0,
										height: segment.Height.ToPercentStr(),
										left: segmentStart_posPercent.ToPercentStr(),
										width: segmentWidth_sizePercent.ToPercentStr(),
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
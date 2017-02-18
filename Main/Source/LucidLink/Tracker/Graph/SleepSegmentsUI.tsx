import {Assert} from "../../../Frame/General/Assert";
import {GraphRow, Segment} from "./GraphRow";
import {BaseComponent as Component, Row, Panel} from "../../../Frame/ReactGlobals";
import {E, Range, Notify} from "../../../Frame/Globals";
import {Event} from "../Session";
import Moment from "moment";
import {SleepStage} from "../../../Frame/SPBridge";
import {SleepSegment} from "../Session/SleepSession";
import SleepSession from "../Session/SleepSession";

export default class SleepSegmentsUI extends Component<
		{startTime: Moment.Moment, endTime: Moment.Moment, width: number, height: number,
			sleepSegments: SleepSegment[], style?}, {}> {
	render() {
		let {startTime, endTime, width, height, sleepSegments, style} = this.props;

		// for testing
		/*let session = new SleepSession();
		session.segments.push(new SleepSegment(session, SleepStage.V.Absent).VSet({startTime: Moment().add(-5, "hours")}));
		session.segments.push(new SleepSegment(session, SleepStage.V.Wake).VSet({startTime: Moment().add(-4, "hours")}));
		session.segments.push(new SleepSegment(session, SleepStage.V.Light).VSet({startTime: Moment().add(-3, "hours")}));
		session.segments.push(new SleepSegment(session, SleepStage.V.Deep).VSet({startTime: Moment().add(-2, "hours")}));
		session.segments.push(new SleepSegment(session, SleepStage.V.Rem).VSet({startTime: Moment().add(-1, "hours")}));
		sleepSegments = session.segments;*/

		return (
			<Row style={E({position: "absolute", width, height}, style)}>
				{sleepSegments.map((segment, index)=> {
					let segmentStart_percentFromLeftToRight = segment.startTime.diff(startTime) / endTime.diff(startTime);
					let segmentWidth_percentOfChartWidth = segment.EndTime.diff(segment.startTime) / endTime.diff(startTime);
					return (
						<Panel key={index} style={E({position: "absolute", bottom: 0,
							height: height * segment.Height,
							left: segmentStart_percentFromLeftToRight * width, width: segmentWidth_percentOfChartWidth * width,
							backgroundColor: segment.Color})}/>
					);
				})}
			</Row>
		);
	}
}
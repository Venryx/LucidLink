import {Assert} from "../../../Frame/General/Assert";
import {GraphRow, Segment} from "./GraphRow";
import {BaseComponent as Component, Row, Panel} from "../../../Frame/ReactGlobals";
import {E, Range, Notify} from "../../../Frame/Globals";
import {Event} from "../Session";
import Moment from "moment";
import {SleepSegment} from "../Session/SleepSession";

export default class GraphRowUI extends Component<
		{startTime: Moment.Moment, endTime: Moment.Moment, width: number, height: number,
			sleepSegments: SleepSegment[], events: Event[], row: GraphRow, style}, {}> {
	render() {
		let {startTime, endTime, width, height, sleepSegments, events, row, style} = this.props;

		let segmentUIs = [];

		// sleep-session segments
		// ==========

		for (let [index, segment] of sleepSegments.entries()) {
			let segmentStart_percentFromLeftToRight = segment.startTime.diff(startTime) / endTime.diff(startTime);
			let segmentWidth_percentOfChartWidth = segment.EndTime.diff(segment.startTime) / endTime.diff(startTime);
			segmentUIs.push(
				<Panel key={index} style={E({position: "absolute", height: row.height * height,
					left: segmentStart_percentFromLeftToRight * width, width: segmentWidth_percentOfChartWidth * width,
					backgroundColor: segment.Color})}/>
			);
		}

		// user-configured segments
		// ==========

		let matchedEvents = events.Where(a=>row.events.Contains(a.type));		
		for (let segmentStartTime = startTime.clone(); segmentStartTime < endTime; segmentStartTime.add(row.segmentLength, "minutes")) {
			let segmentEndTime = segmentStartTime.clone().add(row.segmentLength, "minutes");
			let matchedEventsInSegment = matchedEvents.Where(a=>a.date >= segmentStartTime && a.date < segmentEndTime);
			
			let segment = new Segment(matchedEventsInSegment);
			let renderInfo = row.renderSegment(segment);
			if (renderInfo == null) continue;
			
			let segmentStart_percentFromLeftToRight = segmentStartTime.diff(startTime) / endTime.diff(startTime);
			let segmentWidth_percentOfChartWidth = segmentEndTime.diff(segmentStartTime) / endTime.diff(startTime);
			segmentUIs.push(
				<Panel key={segmentStartTime.valueOf()} style={E({position: "absolute", height: row.height * height,
					left: segmentStart_percentFromLeftToRight * width, width: segmentWidth_percentOfChartWidth * width,
					backgroundColor: renderInfo.color})}/>
			);
		}

		return (
			<Row style={E({position: "absolute", width, height}, style)}>
				{segmentUIs}
			</Row>
		);
	}
}
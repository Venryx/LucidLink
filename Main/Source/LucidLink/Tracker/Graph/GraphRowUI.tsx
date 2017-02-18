import {GraphRow, Segment} from "./GraphRow";
import {BaseComponent as Component, Row, Panel} from "../../../Frame/ReactGlobals";
import {E, Range, Notify} from "../../../Frame/Globals";
import {Event} from "../Session";
import Moment from "moment";

export default class GraphRowUI extends Component<
		{startTime: Moment.Moment, endTime: Moment.Moment, width: number, height: number, events: Event[], row: GraphRow, style}, {}> {
	render() {
		var {startTime, endTime, width, height, events, row, style} = this.props;

		var matchedEvents = events.Where(a=>row.events.Contains(a.type));

		var segmentUIs = [];
		for (let segmentStartTime = startTime.clone(); segmentStartTime < endTime; segmentStartTime.add(row.segmentLength, "minutes")) {
			let segmentEndTime = segmentStartTime.clone().add(row.segmentLength, "minutes");
			var matchedEventsInSegment = matchedEvents.Where(a=>a.date >= segmentStartTime && a.date < segmentEndTime);
			
			let segment = new Segment(matchedEventsInSegment);
			var renderInfo = row.renderSegment(segment);
			if (renderInfo == null) continue;
			
			var segmentStart_percentFromLeftToRight = segmentStartTime.diff(startTime) / endTime.diff(startTime);
			var segmentWidth_percentOfChartWidth = segmentEndTime.diff(segmentStartTime) / endTime.diff(startTime);
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
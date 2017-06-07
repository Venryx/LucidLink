import {Event} from "../Session";
import {A} from "../../../Frame/General/Assert";

export class Segment {
	constructor(events: Event[]) {
		this.events = events;
	}
	events: Event[];
}
export class SegmentRenderInfo {
	color: string;
}

export class GraphRow {
	constructor(info) {
		this.events = A.NonNull = info.events as string[];
		if (info.height) this.height = info.height;
		if (info.segmentLength) this.segmentLength = info.segmentLength;
		this.renderSegment = A.NonNull = info.renderSegment;
	}

	events: string[];
	height = .1;
	/** in minutes */
	segmentLength = 10;
	renderSegment: (segment: Segment)=>SegmentRenderInfo;
}
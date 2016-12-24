import {Packet} from "./EEGProcessor";
//import * as g from "../Globals";
import {D} from "../Globals";
import PatternMatchAttempt from "./PatternMatchAttempt";
export class FuncPattern {
	constructor(info) {
		if (info.minStartInterval) this.minStartInterval = info.minStartInterval;
		if (info.maxOverlappingAttempts) this.maxOverlappingAttempts = info.maxOverlappingAttempts;
		if (info.gapTraverse) this.gapTraverse = info.gapTraverse;
		if (info.segments) this.segments = info.segments;
		this.onPartialMatch = info.onPartialMatch;
		this.onMatch = info.onMatch;
	}

	minStartInterval = 10;
	maxOverlappingAttempts = 3;
	gapTraverse = {system: "binary search", minInterval: 5}; // todo: make this work
	segments: Segment[] = [];
	onPartialMatch: (matchAttempt: PatternMatchAttempt)=>void;
	onMatch: (matchAttempt: PatternMatchAttempt)=>void;
	onEnd: (succeeded: boolean)=>void;
}

export class Segment {
}

export class Gap extends Segment {
	constructor(info) {
		super();
		this.min = info.min;
		this.max = info.max;
	}
	min = 0;
	max = 0;
}
export class Matcher extends Segment {
	constructor(matchFunc) {
		super();
		this.matchFunc = matchFunc;
	}
	matchFunc: (packet: Packet)=>boolean;
}
import {Toast, Trace} from '../Globals';
import {Gap, Matcher} from "./FuncPattern";
import {LL} from "../../LucidLink";

export default class PatternMatchAttempt {
	constructor(key, pattern) {
		this.key = key;
		this.pattern = pattern;
	}
	key = null;
	pattern = null;

	segmentsMatched = 0;
	segmentMatchXs = [];
	ProcessPacket(x, packet) {
		var lastSegment = this.pattern.segments[this.segmentsMatched - 1];
		var lastSegment_matchX = this.segmentMatchXs[this.segmentsMatched - 1];
		var nextSegmentIndex = this.segmentsMatched;
		var nextSegment = this.pattern.segments[nextSegmentIndex];
		//if (nextSegment == null) return;

		let distFromLastMatch = x - lastSegment_matchX;
		if (nextSegment instanceof Gap) {
			let minDist = nextSegment.min + 1;
			let matched = distFromLastMatch >= minDist;
			Toast("Test1" + minDist + ";" + matched + ";" + distFromLastMatch + ";" + nextSegmentIndex);
			if (matched)
				this.MatchSegment(nextSegmentIndex, x);
		} else if (nextSegment instanceof Matcher) {
			let maxDist = lastSegment instanceof Gap ? lastSegment.max - lastSegment.min : 0;

			let matched = nextSegment.matchFunc(packet);
			if (matched)
				this.MatchSegment(nextSegmentIndex, x);
			else if (distFromLastMatch > maxDist)
				this.Cancel();
		}
	}
	MatchSegment(segmentIndex, x) {
		this.segmentsMatched++;
		this.segmentMatchXs[segmentIndex] = x;

		if (this.pattern.onPartialMatch) this.pattern.onPartialMatch(this);
		if (this.segmentsMatched >= this.pattern.segments.length)
			this.Complete();
	}

	Complete() {
		LL.monitor.eegProcessor.EndPatternMatchAttempt(this);
		if (this.pattern.onMatch) this.pattern.onMatch(this);
		if (this.pattern.onEnd) this.pattern.onEnd(true);
	}
	Cancel() {
		LL.monitor.eegProcessor.EndPatternMatchAttempt(this);
		if (this.pattern.onEnd) this.pattern.onEnd(false);
	}
}
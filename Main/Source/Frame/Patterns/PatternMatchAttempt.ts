import {Toast, Trace} from '../Globals';
import {Gap, Matcher, FuncPattern} from "./FuncPattern";
import {LL} from "../../LucidLink";
import {ProfileMethod} from "../VProfiler";
import {Pattern} from "../../LucidLink/Settings";

export default class PatternMatchAttempt {
	constructor(key, pattern) {
		this.key = key;
		this.pattern = pattern;
	}
	key: string;
	pattern: FuncPattern;

	//onEnd: Function;

	segmentsMatched = 0;
	segmentMatchIndexes = [];
	ProcessPacket(index, packet) {
		let p = ProfileMethod("ProcessPacket");

		p.Section("part 1");
		var lastSegment = this.pattern.segments[this.segmentsMatched - 1];
		var lastSegment_matchIndex = this.segmentMatchIndexes[this.segmentsMatched - 1];
		var nextSegmentIndex = this.segmentsMatched;
		var nextSegment = this.pattern.segments[nextSegmentIndex];
		//if (nextSegment == null) return;

		let distFromLastMatch = index - lastSegment_matchIndex;
		if (nextSegment instanceof Gap) {
			p.Section("part 2");
			let minDist = nextSegment.min + 1;
			let matched = distFromLastMatch >= minDist;
			//Toast("Test1" + minDist + ";" + matched + ";" + distFromLastMatch + ";" + nextSegmentIndex);
			if (matched)
				this.MatchSegment(nextSegmentIndex, index);
		} else if (nextSegment instanceof Matcher) {
			p.Section("part 3");
			let maxDist = lastSegment instanceof Gap ? lastSegment.max - lastSegment.min : 0;

			let matched = nextSegment.matchFunc(packet);
			if (matched)
				this.MatchSegment(nextSegmentIndex, index);
			else if (distFromLastMatch > maxDist)
				this.Cancel();
		}

		p.End();
	}
	MatchSegment(segmentIndex, index) {
		this.segmentsMatched++;
		this.segmentMatchIndexes[segmentIndex] = index;

		if (this.pattern.onPartialMatch) this.pattern.onPartialMatch(this);
		if (this.segmentsMatched >= this.pattern.segments.length)
			this.Complete();
	}

	Complete() {
		Toast("Completed");
		LL.monitor.eegProcessor.EndPatternMatchAttempt(this);
		if (this.pattern.onMatch) this.pattern.onMatch(this);
		if (this.pattern.onEnd) this.pattern.onEnd(true);
		//if (this.onEnd) this.onEnd(true);
	}
	Cancel() {
		Toast("Canceled");
		LL.monitor.eegProcessor.EndPatternMatchAttempt(this);
		if (this.pattern.onEnd) this.pattern.onEnd(false);
		//if (this.onEnd) this.onEnd(false);
	}
}
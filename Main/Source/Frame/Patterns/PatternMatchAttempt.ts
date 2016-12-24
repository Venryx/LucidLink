import {Toast, Trace} from '../Globals';
import {Gap, Matcher, Pattern} from "./Pattern";
import {LL} from "../../LucidLink";
import {ProfileMethod} from "../VProfiler";
import {Packet} from "./Packet";

export default class PatternMatchAttempt {
	constructor(key, pattern) {
		this.key = key;
		this.pattern = pattern;
	}
	key: string;
	pattern: Pattern;
	active = true;

	// for scripts
	get MatcherMatchFrames() {
		var result: number[] = [];
		for (let [index, segment] of this.pattern.segments.entries()) {
			if (segment instanceof Matcher && index < this.SegmentsMatched)
				result.push(this.segmentMatchPacketFrames[index]);
		}
		return result;
	}
	get MatcherMatchIntervals() {
		var matchIntervals = [];
		var matchFrames = this.MatcherMatchFrames;
		for (var i = 1; i < matchFrames.length; i++)
			matchIntervals.push(matchFrames[i] - matchFrames[i - 1]);
		return matchIntervals;
	}

	get SegmentsMatched() { return this.segmentMatchPackets.length; }

	segmentMatchPackets: Packet[] = [];
	segmentMatchPacketFrames: number[] = [];
	ProcessPacket(frame, packet) {
		let p = ProfileMethod("ProcessPacket");

		p.Section("part 1");
		var lastSegment = this.pattern.segments[this.SegmentsMatched - 1];
		var lastSegment_matchPacketFrame = this.segmentMatchPacketFrames[this.SegmentsMatched - 1];
		var nextSegmentIndex = this.SegmentsMatched;
		var nextSegment = this.pattern.segments[nextSegmentIndex];
		//if (nextSegment == null) return;

		let distFromLastMatch = frame - lastSegment_matchPacketFrame;
		if (nextSegment instanceof Gap) {
			p.Section("part 2");
			let minDist = nextSegment.min + 1;
			let matched = distFromLastMatch >= minDist;
			//Toast("Test1" + minDist + ";" + matched + ";" + distFromLastMatch + ";" + nextSegmentIndex);
			if (matched)
				this.MatchSegment(nextSegmentIndex, frame, packet);
		} else if (nextSegment instanceof Matcher) {
			p.Section("part 3");
			let maxDist = lastSegment instanceof Gap ? lastSegment.max - lastSegment.min : 0;

			let matched = nextSegment.matchFunc(packet);
			if (matched)
				this.MatchSegment(nextSegmentIndex, frame, packet);
			else if (distFromLastMatch > maxDist)
				this.Cancel();
		}

		p.End();
	}
	MatchSegment(segmentIndex, frame, packet) {
		this.segmentMatchPackets.push(packet);
		this.segmentMatchPacketFrames[segmentIndex] = frame;

		if (this.pattern.onPartialMatch) this.pattern.onPartialMatch(this);
		if (this.SegmentsMatched >= this.pattern.segments.length)
			this.Complete();
	}

	Complete() {
		if (!this.active) return;
		this.active = false;
		//Toast("Completed");

		LL.monitor.eegProcessor.EndPatternMatchAttempt(this);
		if (this.pattern.onMatch) this.pattern.onMatch(this);
		if (this.pattern.onEnd) this.pattern.onEnd(true);
		//if (this.onEnd) this.onEnd(true);
	}
	Cancel() {
		if (!this.active) return;
		this.active = false;
		//Toast("Canceled");

		LL.monitor.eegProcessor.EndPatternMatchAttempt(this);
		if (this.pattern.onEnd) this.pattern.onEnd(false);
		//if (this.onEnd) this.onEnd(false);
	}
}
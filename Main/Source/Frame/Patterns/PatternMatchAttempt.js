g.PatternMatchAttempt = class PatternMatchAttempt {
	constructor(key, pattern) {
		this.key = key;
		this.pattern = pattern;
	}
	key = null;
	pattern = null;

	segmentsMatched = 0;
	segmentMatchXs = [];
	ProcessPacket(x, packet) {
		var lastSegment = this.pattern.segments[segmentsMatched - 1];
		var lastSegment_matchX = this.segmentMatchXs[segmentsMatched - 1];
		var nextSegment = this.pattern.segments[segmentsMatched];
		//if (nextSegment == null) return;

		let distFromLastMatch = x - lastSegment_matchX;
		if (nextSegment instanceof Gap) {
			let minDist = nextSegment.min + 1;
			let matched = distFromLastMatch >= minDist;
			if (matched)
				this.MatchSegment(nextSegment);
		} else if (nextSegment instanceof Matcher) {
			let maxDist = lastSegment instanceof Gap ? lastSegment.max - lastSegment.min : 0;

			let matched = nextSegment.matchFunc(packet);
			if (matched)
				this.MatchSegment(nextSegment);
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
		LL.scripts.scriptRunner.CancelPatternMatchAttempt(this);
		if (this.pattern.onMatch) this.pattern.onMatch(this);
	}
	Cancel() {
		LL.scripts.scriptRunner.CancelPatternMatchAttempt(this);
	}
}
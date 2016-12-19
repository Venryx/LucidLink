g.FuncPattern = class FuncPattern {
	constructor(info) {
		if (info.minStartInterval) this.minStartInterval = info.minStartInterval;
		if (info.gapTraverse) this.gapTraverse = info.gapTraverse;
		if (info.segments) this.segments = info.segments;
		this.onPartialMatch = info.onPartialMatch;
		this.onMatch = info.onMatch;
	}

	minStartInterval = 10;
	gapTraverse = {system: "binary search", minInterval: 5}; // todo: make this work
	segments = [];
	onPartialMatch = null; // matchAttempt=>{}
	onMatch = null; // matchAttempt=>{}
}

g.Segment = class Segment {
}

g.Gap = class Gap extends Segment {
	constructor(info) {
		super();
		this.min = info.min;
		this.max = info.max;
	}
	min = 0;
	max = 0;
}
g.Matcher = class Matcher extends Segment {
	constructor(matchFunc) {
		super();
		this.matchFunc = matchFunc;
	}
	matchFunc = null;
}
import {BaseComponent} from "../ReactGlobals";
import V from "../../Packages/V/V";
import BackgroundTimer from "react-native-background-timer";
import {Global, Log} from "../Globals";
import {LL} from "../../LucidLink";

export class TimerContext {
	timers = [] as Timer[];
	CloseAndReset() {
		for (let timer of this.timers)
			timer.Stop();
		this.timers = [];
	}
}

// methods
// ==========

export function TryCall(func, /*optional:*/ args_) { if (func instanceof Function) func.apply(this, V.CloneArray(arguments).splice(0, 1)); }
export function TryCall_OnX(obj, func, /*optional:*/ args_) { if (func instanceof Function) func.apply(obj, V.CloneArray(arguments).splice(0, 1)); }
g.Extend({TryCall, TryCall_OnX});

//window.requestAnimationFrame = function() {};

//export function WaitXThenRun(waitTime, func): number { return setTimeout(func, waitTime); }
export function WaitXThenRun(waitTime, func): number { return BackgroundTimer.setTimeout(func, waitTime); }
export function WaitXThenRun_BuiltIn(waitTime, func): number { return setTimeout(func, waitTime); }
export function Sleep(ms) {
	var startTime = new Date().getTime();
	while (new Date().getTime() - startTime < ms)
	{}
}

var DoNothingXTimesThenDoY_counters = {};
export function DoNothingXTimesThenDoY(doNothingCount: number, func: Function, key = "default") {
	if (DoNothingXTimesThenDoY_counters[key] == null)
		DoNothingXTimesThenDoY_counters[key] = 0;
		
	if (DoNothingXTimesThenDoY_counters[key] >= doNothingCount)
		func();
	DoNothingXTimesThenDoY_counters[key]++;
}

// interval is in seconds (can be decimal)
export class Timer {
	constructor(interval, func, maxCallCount = -1, asBackground = false) {
	    this.interval = interval;
	    this.func = func;
	    this.maxCallCount = maxCallCount;
		this.asBackground = asBackground;
	}

	interval: number;
	func: Function;
	maxCallCount: number;
	asBackground: boolean;

	SetContext(contextOrComp: TimerContext | BaseComponent<any, any>) {
		if (contextOrComp)
			contextOrComp.timers.push(this);
		return this;
	}

	timerID = -1;
	get IsRunning() { return this.timerID != -1; }

	callCount = 0;
	Start() {
		let wrapperFunc = ()=> {
			this.func();
			this.callCount++;
			if (this.maxCallCount != -1 && this.callCount >= this.maxCallCount)
				this.Stop();
		};
		this.timerID = this.asBackground
			? BackgroundTimer.setInterval(wrapperFunc, this.interval * 1000)
			: setInterval(wrapperFunc, this.interval * 1000);
		return this;
	}
	Stop() {
		if (this.asBackground)
			BackgroundTimer.clearInterval(this.timerID);
		else
			clearInterval(this.timerID);
		this.timerID = -1;
	}
}
export class TimerMS extends Timer {
    constructor(interval_decimal, func, maxCallCount = -1) {
        super(interval_decimal / 1000, func, maxCallCount);
    }
}

export class Sequence {
	constructor(asBackground = true) {
		this.asBackground = asBackground;
	}
	asBackground: boolean;

	context: TimerContext;
	SetContext(context: TimerContext) {
		this.context = context;
		return this;
	}
	
	segments = [] as SequenceSegment[];
	AddSegment(delayInS: number, func: Function) {
		this.segments.push(new SequenceSegment(delayInS, func));
	}

	enabled = true;
	currentSegmentTimeout = null as Timer;
	get Active() { return this.currentSegmentTimeout != null; }

	Start() {
		this.currentSegmentTimeout = this.segments[0].StartDelay(this, this.asBackground).SetContext(this.context);
	}
	OnCompleteSegment(segment: SequenceSegment) {
		this.currentSegmentTimeout = null;
		if (this.enabled) {
			let nextSegment = this.segments[this.segments.indexOf(segment) + 1];
			if (nextSegment) {
				this.currentSegmentTimeout = nextSegment.StartDelay(this, this.asBackground).SetContext(this.context);
			}
		}
	}
	Stop() {
		this.currentSegmentTimeout.Stop();
		this.currentSegmentTimeout = null;
		this.enabled = false;
	}
}
class SequenceSegment {
	constructor(delayInS: number, func: Function) {
		this.delayInS = delayInS;
		this.func = func;
	}
	delayInS = 0;
	func = null as Function;

	StartDelay(sequence: Sequence, asBackground: boolean): Timer {
		return new Timer(this.delayInS, ()=> {
			this.func();
			sequence.OnCompleteSegment(this);
		}, 1, asBackground).Start();
	}
}

var funcLastScheduledRunTimes = {};
/** If time-since-last-run is above minInterval, run func right away.
 * Else, schedule next-run to occur as soon as the minInterval is passed. */
export function BufferAction(minInterval: number, func: Function);
/** If time-since-last-run is above minInterval, run func right away.
 * Else, schedule next-run to occur as soon as the minInterval is passed. */
export function BufferAction(key: string, minInterval: number, func: Function);
export function BufferAction(...args) {
	if (args.length == 2) var [minInterval, func] = args, key = null;
	else if (args.length == 3) var [key, minInterval, func] = args;

    var lastScheduledRunTime = funcLastScheduledRunTimes[key] || 0;
    var now = new Date().getTime();
    var timeSinceLast = now - lastScheduledRunTime;
    if (timeSinceLast >= minInterval) { // if we've waited enough since last run, run right now
        func();
        funcLastScheduledRunTimes[key] = now;
    } else {
		let waitingForNextRunAlready = lastScheduledRunTime > now;
		if (!waitingForNextRunAlready) { // else, if we're not already waiting for next-run, schedule next-run
			var nextRunTime = lastScheduledRunTime + minInterval;
			var timeTillNextRun = nextRunTime - now;
			WaitXThenRun(timeTillNextRun, func);
			funcLastScheduledRunTimes[key] = nextRunTime;
		}
	}
}
import {BaseComponent} from "../ReactGlobals";
import V from "../../Packages/V/V";
import BackgroundTimer from "react-native-background-timer";
import {Global, IDProvider, Log} from "../Globals";
import {LL} from "../../LucidLink";
import {Assert} from "../../Packages/VDF/VDF";

export class TimerContext {
	timers = [] as Timer[];
	Reset() {
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
	constructor(intervalInS: number, func: ()=>void, maxCallCount = -1, asBackground = false) {
	    this.intervalInS = intervalInS;
	    this.func = func;
	    this.maxCallCount = maxCallCount;
		this.asBackground = asBackground;
	}

	intervalInS: number;
	func: ()=>void;
	maxCallCount: number;
	asBackground: boolean;

	SetContext(timerContext: TimerContext | BaseComponent<any, any>) {
		Assert(timerContext, "TimerContext cannot be null.");
		timerContext.timers.push(this);
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
			? BackgroundTimer.setInterval(wrapperFunc, this.intervalInS * 1000)
			: setInterval(wrapperFunc, this.intervalInS * 1000);
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
	static idProvider = new IDProvider();
	constructor(asBackground = true) {
		this.id = Sequence.idProvider.GetID();
		this.asBackground = asBackground;
	}
	id: number;
	asBackground: boolean;

	timerContext: TimerContext;
	SetContext(timerContext: TimerContext) {
		Assert(timerContext, "Timer-context cannot be null.");
		Assert(this.Active, "Can't set timer-context after starting.");
		this.timerContext = timerContext;
		return this;
	}
	
	segments = [] as SequenceSegment[];
	AddSegment(delayInS: number, func: Function) {
		this.segments.push(new SequenceSegment(delayInS, func));
	}

	nextSegment = null as SequenceSegment;
	get Active() { return this.nextSegment != null; }

	Start() {
		Log(`Starting sequence ${this.id}`);
		this.nextSegment = this.segments[0];
		this.nextSegment.StartDelay(this, this.asBackground);
	}
	OnCompleteSegment(segment: SequenceSegment) {
		if (this.Active) {
			this.nextSegment = this.segments[this.segments.indexOf(segment) + 1];
			this.nextSegment.StartDelay(this, this.asBackground);
		} else {
			this.nextSegment = null;
		}
		Log(`Completed segment ${segment.id} of sequence ${this.id}. Next segment: ${this.nextSegment ? this.nextSegment.id : -1}`);
	}
	Stop() {
		Log(`Stopping sequence ${this.id}`);
		if (this.nextSegment)
			this.nextSegment.Stop();
		this.nextSegment = null;
	}
}
class SequenceSegment {
	static idProvider = new IDProvider();
	constructor(delayInS: number, func: Function) {
		this.id = SequenceSegment.idProvider.GetID();
		this.delayInS = delayInS;
		this.func = func;
	}
	id: number;
	delayInS = 0;
	func = null as Function;

	timer: Timer;
	StartDelay(sequence: Sequence, asBackground: boolean) {
		Log(`Starting segment ${this.id}`);
		this.timer = new Timer(this.delayInS, ()=> {
			Log(`Running-and-ending segment ${this.id}`);
			this.func();
			sequence.OnCompleteSegment(this);
		}, 1, asBackground).Start();
		if (sequence.timerContext)
			this.timer.SetContext(sequence.timerContext);
	}
	Stop() {
		Log(`Stopping segment ${this.id} (timer call-count: ${this.timer ? this.timer.callCount : -1})`);
		if (this.timer)
			this.timer.Stop();
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
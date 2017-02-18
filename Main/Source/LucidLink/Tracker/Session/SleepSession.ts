import {Assert} from "../../../Frame/General/Assert";
import Moment from "moment";
import {P} from "../../../Packages/VDF/VDFTypeInfo";
import {SleepStage} from "../../../Frame/SPBridge";
import SPBridge from "../../../Frame/SPBridge";
import {LL} from "../../../LucidLink";

export default class SleepSession {
	constructor() {
		if (this.startTime) return; // if called by VDF, don't do anything
		this.startTime = Moment();
	}

	@P() startTime = null as Moment.Moment;
	@P() endTime = null as Moment.Moment;
	get Active() { return this.endTime == null; }

	@P() segments = [] as SleepSegment[];

	public End() {
		Assert(LL.tracker.currentSession.CurrentSleepSession == this && this.Active, "Can only call End() on the active sleep-session.");
		this.endTime = Moment();
		SPBridge.StopSession();
	}
}
global.Extend({SleepSession});

export class SleepSegment {
	constructor(stage) {
		if (stage == null) return; // if called by VDF, don't do anything
		this.startTime = Moment();
		this.stage = stage;
	}
	@P() startTime = null as Moment.Moment;
	@P() stage = null as SleepStage;
}
global.Extend({SleepSegment});
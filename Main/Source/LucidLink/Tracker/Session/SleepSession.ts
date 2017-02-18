import {Assert} from "../../../Frame/General/Assert";
import Moment from "moment";
import {P, _VDFPostDeserialize} from "../../../Packages/VDF/VDFTypeInfo";
import {SleepStage} from "../../../Frame/SPBridge";
import SPBridge from "../../../Frame/SPBridge";
import {LL} from "../../../LucidLink";

export default class SleepSession {
	@_VDFPostDeserialize() PostDeserialize() {
		for (let segment of this.segments)
			segment.parent = this;
	}

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
	constructor(parent: SleepSession, stage: SleepStage) {
		if (parent == null) return; // if called by VDF, don't do anything
		this.parent = parent;
		this.startTime = Moment();
		this.stage = stage;
	}

	parent = null as SleepSession;
	@P() startTime = null as Moment.Moment;
	get EndTime() {
		var endTime: Moment.Moment;
		if (this == this.parent.segments.Last())
			endTime = this.parent.Active ? Moment() : this.parent.endTime;
		else
			endTime = this.parent.segments[this.parent.segments.indexOf(this) + 1].startTime;
		return endTime;
	}

	@P() stage = null as SleepStage;
	get Color() {
		return SleepStage.GetColorForStage(this.stage);
	}
}
global.Extend({SleepSegment});
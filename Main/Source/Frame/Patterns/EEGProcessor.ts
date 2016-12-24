import {ProfileMethod} from '../VProfiler';
import {IsNumber, Toast} from '../Globals';
import PatternMatchAttempt from './PatternMatchAttempt';
import Bind from "autobind-decorator";
import {LL} from "../../LucidLink";
import {Packet} from "./Packet";

export class EEGProcessor {
	static maxX = 1000;
	constructor() {
		/*this.channelPoints = [];
		for (let i = 0; i < 4; i++)
			this.channelPoints[i] = Array(1 + EEGProcessor.maxX).fill().map((_, i)=>new Vector2i(i, 0));*/
		this.packets = Array(1 + EEGProcessor.maxX);
	}

	channelBaselines = [];
	//channelPoints = [];
	packets = [];

	currentFrame = -1;
	currentX = -1;
	@Bind OnReceiveMusePacket(packet: Packet) {
		(packet as any).__proto__ = Packet.prototype; // make-so packet is actually of type Packet

		this.currentFrame++;
		this.currentX = this.currentX < EEGProcessor.maxX ? this.currentX + 1 : 0;

		if (packet.channelBaselines)
			this.channelBaselines = packet.channelBaselines;
		packet.x = this.currentX;
		// maybe temp; extract the only 2 channels actually sent
		packet.eegValues = [0, packet.eegValues[0], packet.eegValues[1], 0];

		/*for (let ch = 0; ch < 4; ch++)
			this.channelPoints[ch][this.currentX] = packet.eegValues[ch];*/
		this.packets[this.currentX] = packet;

		if (LL.monitor.patternMatch)
			this.DoPatternMatching(packet);
	}

	ResetScriptsRelatedStuff() {
		this.patternMatchAttempts = {};
		this.patternIndex_lastMatchStart = {};
		this.patternIndex_liveMatchAttemptCount = {};
	}

	patternMatchAttempts = {};
	patternIndex_lastMatchStart = {};
	patternIndex_liveMatchAttemptCount = {};

	EndPatternMatchAttempt(matchAttempt: PatternMatchAttempt) {
		var patternIndex = LL.scripts.scriptRunner.patterns.indexOf(matchAttempt.pattern);
		delete this.patternMatchAttempts[matchAttempt.key];
		this.patternIndex_liveMatchAttemptCount[patternIndex]--;
	}
	
	DoPatternMatching(packet) {
		let p = ProfileMethod("DoPatternMatching");

		p.Section("checking for pattern continuations");
		//BufferAction(1000, ()=>Toast("Count: " + this.patternMatchAttempts.Props.length));
		for (let {value: matchAttempt} of this.patternMatchAttempts.Props as {value: PatternMatchAttempt}[]) {
			matchAttempt.ProcessPacket(this.currentFrame, packet);
		}

		var p1 = p.Section("checking for pattern starts");
		for (let [index, pattern] of LL.scripts.scriptRunner.patterns.entries()) {
			// if already at max overlapping attempts, don't try to start another
			if (this.patternIndex_liveMatchAttemptCount[index] >= pattern.maxOverlappingAttempts) continue;
			// if we're too close to last pattern-match 
			let packetsSinceStartOfLastPatternMatchAttempt = this.currentFrame - this.patternIndex_lastMatchStart[index];
			if (packetsSinceStartOfLastPatternMatchAttempt < pattern.minStartInterval) continue;

			let key = `pattern${index}_x${this.currentX}`;
			let matchAttempt = new PatternMatchAttempt(key, pattern);

			// try to match first segment
			matchAttempt.ProcessPacket(this.currentFrame, packet);
			// if first segment matched, this is valid start of pattern, so add it to collection
			if (matchAttempt.SegmentsMatched > 0) {
				this.patternMatchAttempts[key] = matchAttempt;
				this.patternIndex_lastMatchStart[index] = this.currentFrame;

				if (this.patternIndex_liveMatchAttemptCount[index] == null)
					this.patternIndex_liveMatchAttemptCount[index] = 0;
				this.patternIndex_liveMatchAttemptCount[index]++;
				//matchAttempt.onEnd = ()=>this.patternIndex_liveMatchAttemptCount[index]--;
			}
		}

		p.End();
	}
}
import {ProfileMethod} from '../VProfiler';
import {IsNumber, Toast} from '../Globals';
import PatternMatchAttempt from './PatternMatchAttempt';
import Bind from "autobind-decorator";
class Packet {
	x;
	maxX;
	eegValues;

	GetPeer(offset) {
		LL.monitor.eegProcessor.packets[(this.x + offset).WrapToRange(0, this.maxX)];
	}
	GetChannelVal(channel) {
		var channelIndex = IsNumber(channel) ? channel : ["bl", "fl", "fr", "br"].indexOf(channel);
		return this.eegValues[channelIndex];
	}
	GetChannelValDif(channel) {
		var channelIndex = IsNumber(channel) ? channel : ["bl", "fl", "fr", "br"].indexOf(channel);
		return this.eegValues[channelIndex] - LL.monitor.eegProcessor.channelBaselines[channelIndex];
	}
}

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

	currentIndex = -1;
	currentX = -1;
	@Bind OnReceiveMusePacket(packet) {
		this.currentIndex++;
		this.currentX = this.currentX < EEGProcessor.maxX ? this.currentX + 1 : 0;

		if (packet.channelBaselines)
			this.channelBaselines = packet.channelBaselines;

		packet.x = this.currentX;
		packet.__proto__ = Packet.prototype; // make-so packet is of type Packet

		/*for (let ch = 0; ch < 4; ch++)
			this.channelPoints[ch][this.currentX] = packet.eegValues[ch];*/
		this.packets[this.currentX] = packet;

		if (LL.monitor.patternMatch)
			this.DoPatternMatching(packet);
	}

	patternMatchAttempts = {};
	EndPatternMatchAttempt(matchAttempt) {
		Toast("Ending");
		delete this.patternMatchAttempts[matchAttempt.key];
	}
	
	DoPatternMatching(packet) {
		let p = ProfileMethod("DoPatternMatching");

		p.Section("part 1");
		for (let [index, pattern] of LL.scripts.scriptRunner.patterns.entries()) {
			let tooCloseToOtherMatchAttempt = false;
			for (let x = this.currentX - 1; x > this.currentX - pattern.minStartInterval; x--) {
				let realX = x.WrapToRange(0, EEGProcessor.maxX);
				let key = `pattern${index}_x${realX}`;
				if (this.patternMatchAttempts[realX]) {
					tooCloseToOtherMatchAttempt = true;
					break;
				}
			}
			if (tooCloseToOtherMatchAttempt) continue;

			let key = `pattern${index}_x${this.currentX}`;
			let matchAttempt = new PatternMatchAttempt(key, pattern);
			this.patternMatchAttempts[key] = matchAttempt;
		}

		p.Section("part 2");
		//BufferAction(1000, ()=>Toast("Count: " + this.patternMatchAttempts.Props.length));
		for (let {name: key, value: matchAttempt} of this.patternMatchAttempts.Props) {
			matchAttempt.ProcessPacket(this.currentX, packet);
		}

		p.End();
	}
}
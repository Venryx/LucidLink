import {ProfileMethod} from '../VProfiler';
import {IsNumber, Toast} from '../Globals';
import PatternMatchAttempt from './PatternMatchAttempt';
import Bind from "autobind-decorator";
import {LL} from "../../LucidLink";

export class Packet {
	x;
	maxX;
	eegValues;

	viewDirection: number;
	viewDistance: number;
	channelBaselines: any[];

	GetPeer(offset) {
		LL.monitor.eegProcessor.packets[(this.x + offset).WrapToRange(0, this.maxX)];
	}
	GetChannelValue(channel) {
		var channelIndex = IsNumber(channel) ? channel : ["bl", "fl", "fr", "br"].indexOf(channel);
		return this.eegValues[channelIndex];
	}
	GetChannelOffset(channel) {
		var channelIndex = IsNumber(channel) ? channel : ["bl", "fl", "fr", "br"].indexOf(channel);
		return this.eegValues[channelIndex] - LL.monitor.eegProcessor.channelBaselines[channelIndex];
	}
	GetChannelDeviation(channel) {
		return Math.abs(this.GetChannelOffset(channel));
	}
	GetMaxDeviationForChannels(...channels) {
		return channels.Select(ch=>this.GetChannelDeviation(ch)).Max();
	}
}
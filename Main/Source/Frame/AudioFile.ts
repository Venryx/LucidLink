import {Assert} from "../Packages/VDF/VDF";
import {Timer, WaitXThenRun} from "./General/Timers";

export class AudioFile {
	constructor(baseFile) {
		this.baseFile = baseFile;
		//this.PlayCount = -1;
	}
	baseFile;

	Play() {
		this.playStartTime = new Date().getTime();
		this.baseFile.play();
		return this;
	}
	Pause() {
		this.baseFile.pause();
		this.wasPaused = true;
		return this;
	}
	Stop() {
		this.SetCurrentTime(0);
		this.baseFile.stop();
		WaitXThenRun(1000, ()=> {
			this.SetCurrentTime(0);
			this.baseFile.stop();
		});

		if (this.fadeVolumeTimer)
			this.fadeVolumeTimer.Stop();

		this.playStartTime = null;
		return this;
	}
	Release() { this.baseFile.release(); }

	playStartTime = null;
	wasPaused = false;
	IsPlaying() {
		if (this.playStartTime == null)
			return false;
		Assert(!this.wasPaused, "Cannot get IsPlaying state if audio was paused.");
		var playCount = this.PlayCount;
		var playDurationMS = this.Duration * (playCount != -1 ? playCount : 111222333) * 1000;
		//Log(this.Duration + ";" + playCount + ";" + this.playStartTime + ";" + playDurationMS);
		return this.playStartTime + playDurationMS > new Date().getTime();
	}

	get Duration() { return this.baseFile.getDuration(); } // in seconds
	//get ChannelCount() { return this.baseFile.getNumberOfChannels(); } // ios only

	// volume range is 0-1
	GetVolume() { return this.baseFile.getVolume(); }
	SetVolume(volume) { return this.baseFile.setVolume(volume); }
	AddVolume(amount) { return this.SetVolume(this.GetVolume() + amount); }
	fadeVolumeTimer: Timer;
	FadeVolume(options) {
		// if earlier fade in progress, stop it
		if (this.fadeVolumeTimer)
			this.fadeVolumeTimer.Stop();

		var {from, to, over} = options;
		var startTime = new Date().getTime();
		var endTime = startTime + (over * 1000);

		var startVolume = from || this.GetVolume();
		if (from)
			this.SetVolume(startVolume);

		this.fadeVolumeTimer = new Timer(.1, ()=> {
			var time = new Date().getTime();
			var percentThroughFade = V.GetPercentFromXToY(startTime, endTime, time);
			this.SetVolume(V.Lerp(startVolume, to, percentThroughFade));
			if (percentThroughFade >= 1)
				this.fadeVolumeTimer.Stop();
		}).Start();
	}

	GetCurrentTime(callback) { this.baseFile.getCurrentTime(callback); }
	SetCurrentTime(newTime) { this.baseFile.setCurrentTime(newTime); }
	get PlayCount() { 
		var result = this.baseFile.getNumberOfLoops();
		// library "loop count" is apparently *extra* times to play audio
		//		since base-file's "loop count" didn't include 1 for the first-play, we add it ourselves
		if (result != -1) result--;
		return result;
	}
	// set to -1 for endless loop
	set PlayCount(count) {
		// library "loop count" is apparently *extra* times to play audio
		//		since first-play is outside that, remove it's 1 from the base-file's "loop count" we're sending
		if (count != -1) count--;
		this.baseFile.setNumberOfLoops(count);
	}

	//GetPan() { return this.baseFile.getPan(pan); } // ios only
	//SetPan(pan) { this.baseFile.setPan(pan); } // ios only
}
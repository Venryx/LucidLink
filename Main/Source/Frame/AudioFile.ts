import {Assert, Log} from "../Packages/VDF/VDF";
import {Timer, WaitXThenRun} from "./General/Timers";
import Sound from "react-native-sound";
import {LL} from "../LucidLink";
import V from "../Packages/V/V";

export class AudioFileManager {
	audioFiles = {};
	GetAudioFile(name: string, onLoaded?: Function): AudioFile {
		if (this.audioFiles[name] == null) {
			var audioFileEntry = LL.settings.audioFiles.First(a=>a.name == name);
			if (audioFileEntry == null)
				alert(`Cannot find audio-file entry with name "${name}".`);
			this.audioFiles[name] = new AudioFile(audioFileEntry.path, onLoaded);
		}
		return this.audioFiles[name];
	}
	Reset() {
		for (let audioFile of this.audioFiles.Props.Select(a=>a.value))
			audioFile.Stop();
		this.audioFiles = [];
	}
}

export class AudioFile {
	constructor(filePath: string, onLoaded?: Function) {
		let [basePath, fileName] = filePath.SplitAt(filePath.lastIndexOf("/"), false);
		this.baseFile = new Sound(fileName, basePath, error=> {
			if (error)
				Log(`Failed to load audio-file at "${filePath}":`, error);
			else {
				// apply delayed commands, since base-file's loaded now
				for (let func of this.postLoadFuncs)
					func();
			}
			if (onLoaded) onLoaded(error);
		});
		//this.PlayCount = -1;
	}
	baseFile;
	postLoadFuncs = [] as (()=>void)[];

	Play() {
		if (!this.baseFile._loaded) { this.postLoadFuncs.push(()=>this.Play()); return this; }
		this.playStartTime = new Date().getTime();
		this.baseFile.play();
		return this;
	}
	Pause() {
		if (!this.baseFile._loaded) { this.postLoadFuncs.push(()=>this.Pause()); return this; }
		this.baseFile.pause();
		this.wasPaused = true;
		return this;
	}
	Stop() {
		if (!this.baseFile._loaded) { this.postLoadFuncs.push(()=>this.Stop()); return this; }
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
	Release() {
		if (!this.baseFile._loaded) { this.postLoadFuncs.push(()=>this.Release()); return; }
		this.baseFile.release();
	}

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
	SetVolume(volume) {
		if (!this.baseFile._loaded) { this.postLoadFuncs.push(()=>this.SetVolume(volume)); return this; }
		this.baseFile.setVolume(volume);
		return this;
	}

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
	SetCurrentTime(newTime) {
		if (!this.baseFile._loaded) { this.postLoadFuncs.push(()=>this.SetCurrentTime(newTime)); return; }
		this.baseFile.setCurrentTime(newTime);
	}

	get PlayCount() { 
		var loopCount = this.baseFile.getNumberOfLoops();
		// library "loop count" is apparently *extra* times to play audio
		//		since base-file's "loop count" didn't include 1 for the first-play, we add it ourselves
		let playCount = loopCount == -1 ? -1 : loopCount + 1;
		return playCount;
	}
	// set to -1 for endless loop
	set PlayCount(playCount) {
		if (!this.baseFile._loaded) { this.postLoadFuncs.push(()=>this.PlayCount = playCount); return; }
		// library "loop count" is apparently *extra* times to play audio
		//		since first-play is outside that, remove it's 1 from the base-file's "loop count" we're sending
		let loopCount = playCount == -1 ? -1 : playCount - 1;
		this.baseFile.setNumberOfLoops(loopCount);
	}

	//GetPan() { return this.baseFile.getPan(pan); } // ios only
	//SetPan(pan) { this.baseFile.setPan(pan); } // ios only
}
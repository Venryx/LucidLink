import {Assert, E, Timer, WaitXThenRun, IsString} from "../../Frame/Globals";
import {max, min} from "moment";
import {Event} from "../Tracker/Session";
import {Pattern, Matcher, Gap} from "../../Frame/Patterns/Pattern";
import V from "../../Packages/V/V";
//import Sound from "react-native-sound";
import {DeviceEventEmitter} from "react-native";
import {LL} from "../../LucidLink";
var Sound = require("react-native-sound");

// re-exports
// ==========

export {
	// values (ie normal variables)
	LL,
	// classes
	V, Pattern, Matcher, Gap
};

// listeners
// ==========

export function EveryXSecondsDo(seconds, func, maxCallCount = -1) {
	var timer = new Timer(seconds, func, maxCallCount);
	timer.Start();
	LL.scripts.scriptRunner.timers.push(timer);
	return timer;
};

export function WhenChangeMuseConnectStatus(func) {
	LL.scripts.scriptRunner.listeners_whenChangeMuseConnectStatus.push(func);
}
export function WhenMusePacketReceived(func) {
	LL.scripts.scriptRunner.listeners_whenMusePacketReceived.push(func);
}
/*export function WhenViewDirectionUpdated(func) {
	LL.scripts.scriptRunner.listeners_whenViewDirectionUpdated.push(func);
}
export function WhenViewDistanceUpdated(func) {
	LL.scripts.scriptRunner.listeners_whenViewDistanceUpdated.push(func);
}*/

// general
// ==========

export function GetRandomNumber(options) {
	var {min, max, mustBeInteger} = options;
	var range = max - min;
	if (options.mustBeInteger)
		return min + Math.floor(Math.random() * (range + 1));
	return min + (Math.random() * range);
}

export function AddEvent(type, ...args) {
	var event = new Event(type, args);
	LL.tracker.currentSession.events.push(event);
}

export function AddPattern(info) {
	var pattern = new Pattern(info);
	LL.scripts.scriptRunner.patterns.push(pattern);
}

export function CreateTimer(intervalInSec: number, func: Function, maxCallCount = -1) {
	var timer = new Timer(intervalInSec, func, maxCallCount);
	LL.scripts.scriptRunner.timers.push(timer);
	return timer;
}

// input
// ==========

// key-codes can be found here: https://developer.android.com/ndk/reference/keycodes_8h.html
// (or by checking the logs for the key-codes of those pressed)
export function AddKeyDownListener(keyCode, func) {
	LL.scripts.scriptRunner.keyDownListeners.push({keyCode: keyCode, func: func});
}
export function AddKeyUpListener(keyCode, func) {
	LL.scripts.scriptRunner.keyDownListeners.push({keyCode: keyCode, func: func});
}

// pattern matching
// ==========

DeviceEventEmitter.addListener("OnSetPatternMatchProbabilities", (args: any)=> {
	var [x, probabilities] = args;
	//Log(`X: ${x}; Probabilities: ${ToJSON(probabilities)}`);
	for (let listener of LL.scripts.scriptRunner.listeners_onUpdatePatternMatchProbabilities)
		listener(probabilities, x);
});
export function AddListener_OnUpdatePatternMatchProbabilities(func) {
	LL.scripts.scriptRunner.listeners_onUpdatePatternMatchProbabilities.push(func);
};

// audio playback
// ==========

export var audioFiles = audioFiles || {};
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
	FadeVolume(options) {
		var {from, to, over} = options;
		var startTime = new Date().getTime();
		var endTime = startTime + (over * 1000);

		var startVolume = from || this.GetVolume();
		if (from)
			this.SetVolume(startVolume);

		var timer = new Timer(.1, ()=> {
			var time = new Date().getTime();
			var percentThroughFade = V.GetPercentFromXToY(startTime, endTime, time);
			this.SetVolume(V.Lerp(startVolume, to, percentThroughFade));
			if (percentThroughFade >= 1)
				timer.Stop();
		});
		LL.scripts.scriptRunner.timers.push(timer);
		timer.Start();
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
export function GetAudioFile(name) {
	if (audioFiles[name] == null) {
		var audioFileEntry = LL.settings.audioFiles.First(a=>a.name == name);
		if (audioFileEntry == null)
			alert("Cannot find audio-file entry with name '" + name + "'.");
		var baseFile = new Sound(audioFileEntry.path, "", function(error) {
			if (error)
				console.log("Failed to load the sound '" + name + "':", error);
		});
		var audioFile = new AudioFile(baseFile);
		audioFiles[name] = audioFile;
	}
	return audioFiles[name];
}

// text-to-speech
// ==========

var Speech = require("react-native-android-speech");

export function Speak(options) {
	options = E({forceStop: true}, options);
	options.text = options.text.toString();
	return new Promise((resolve, reject)=> {
		Speech.speak(options).then(resolve).catch(ex=> {
			if (ex.toString().contains("TTS is already speaking something")) return;
			throw ex;
		});
	});
};
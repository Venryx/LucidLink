//import Sound from "react-native-sound";
var Sound = require("react-native-sound");

// general
// ==========

g.GetRandomNumber = function(options) {
	var {min, max, mustBeInteger} = options;
	var range = max - min;
	if (options.mustBeInteger)
		return min + Math.floor(Math.random() * (range + 1));
	return min + (Math.random() * range);
}

// input
// ==========

// key-codes can be found here: https://developer.android.com/ndk/reference/keycodes_8h.html
// (or by checking the logs for the key-codes of those pressed)
g.AddKeyDownListener = function(keyCode, func) {
	LL.scripts.scriptRunner.keyDownListeners.push({keyCode: keyCode, func: func});
}
g.AddKeyUpListener = function(keyCode, func) {
	LL.scripts.scriptRunner.keyDownListeners.push({keyCode: keyCode, func: func});
}

// pattern matching
// ==========

DeviceEventEmitter.addListener("OnSetPatternMatchProbabilities", args=> {
	var [x, probabilities] = args;
	//Log(`X: ${x}; Probabilities: ${ToJSON(probabilities)}`);
	for (let listener of LL.scripts.scriptRunner.listeners_onUpdatePatternMatchProbabilities)
		listener(probabilities, x);
});
g.AddListener_OnUpdatePatternMatchProbabilities = function(func) {
	LL.scripts.scriptRunner.listeners_onUpdatePatternMatchProbabilities.push(func);
};

// audio playback
// ==========

var audioFiles = audioFiles || {};
class AudioFile {
	constructor(baseFile) {
		this.baseFile = baseFile;
		//this.SetLoopCount(-1);
	}

	Play() {
		this.SetVolume(1);
		return this.baseFile.play();
	}
	Pause() {
		this.baseFile.pause();
		this.wasPaused = true;
	}
	Stop() {
		this.SetCurrentTime(0);
		this.SetVolume(0);
		WaitXThenRun(1000, ()=> {
			this.baseFile.stop();
		});
	}
	Release() { this.baseFile.release(); }

	playStartTime = null;
	wasPaused = false;
	IsPlaying() {
		if (playStartTime == null)
			return false;
		Assert(!this.wasPaused, "Cannot get IsPlaying state if audio was paused.");
		var loopCount = this.LoopCount;
		var playDurationMS = this.Duration * (loopCount != -1 ? loopCount : 111222333) * 1000;
		return playStartTime + playDurationMS > new Date().getTime();
	}

	get Duration() { return this.baseFile.getDuration(); } // in seconds
	//get ChannelCount() { return this.baseFile.getNumberOfChannels(); } // ios only

	// volume range is 0-1
	GetVolume() { return this.baseFile.getVolume(); }
	SetVolume(volume) { return this.baseFile.setVolume(volume); }
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
		timer.Start();
	}

	GetCurrentTime(callback) { this.baseFile.getCurrentTime(callback); }
	SetCurrentTime(newTime) { this.baseFile.setCurrentTime(newTime); }
	get LoopCount() { return this.baseFile.getNumberOfLoops(); }
	// set to -1 for endless loop
	set LoopCount(count) { this.baseFile.setNumberOfLoops(count); }

	//GetPan() { return this.baseFile.getPan(pan); } // ios only
	//SetPan(pan) { this.baseFile.setPan(pan); } // ios only
}
g.GetAudioFile = function(name) {
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

g.Speak = function(options) {
	options = E({forceStop: true}, options);
	return new Promise((resolve, reject)=> {
		Speech.speak(options).then(resolve).catch(ex=> {
			if (ex.toString().contains("TTS is already speaking something")) return;
			throw ex;
		});
	});
};
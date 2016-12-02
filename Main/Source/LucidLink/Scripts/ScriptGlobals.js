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
	Pause() { this.baseFile.pause(); }
	Stop() {
		this.SetCurrentTime(0);
		this.SetVolume(0);
		WaitXThenRun(1000, ()=> {
			this.baseFile.stop();
		});
	}
	Release() { this.baseFile.release(); }

	GetVolume() { return this.baseFile.getVolume(); }
	SetVolume(volume) { return this.baseFile.setVolume(volume); }
	GetCurrentTime(callback) { this.baseFile.getCurrentTime(callback); }
	SetCurrentTime(newTime) { this.baseFile.setCurrentTime(newTime); }
	//SetPan(pan) { this.baseFile.setPan(pan); }
	// set to -1 for endless loop
	SetLoopCount(count) { this.baseFile.setNumberOfLoops(count); }
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
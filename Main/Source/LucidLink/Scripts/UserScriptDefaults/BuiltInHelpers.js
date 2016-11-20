export default `
// key-codes can be found here: https://developer.android.com/ndk/reference/keycodes_8h.html
function AddKeyDownListener(keyCode, func) {
	LL.scripts.scriptRunner.keyDownListeners.push({keyCode: keyCode, func: func});
}
function AddKeyUpListener(keyCode, func) {
	LL.scripts.scriptRunner.keyDownListeners.push({keyCode: keyCode, func: func});
}



g.audioFiles = g.audioFiles || {};
function AudioFile(baseFile) {
	this.baseFile = baseFile;
	/*this.SetPan(1);
	this.SetLoopCount(-1);*/
}
AudioFile.prototype.Extend({
	Play: function() {
		this.SetVolume(1);
		return this.baseFile.play();
	},
	Pause: function() { this.baseFile.pause(); },
	Stop: function() {
		this.SetCurrentTime(0);
		this.SetVolume(0);
		var s = this;
		g.WaitXThenRun(1000, function() {
			s.baseFile.stop();
		});
	},
	Release: function() { this.baseFile.release(); },

	GetVolume: function() { return this.baseFile.getVolume(); },
	SetVolume: function(volume) { return this.baseFile.setVolume(volume); },
	GetCurrentTime: function(callback) { this.baseFile.getCurrentTime(callback); },
	SetCurrentTime: function(newTime) { this.baseFile.setCurrentTime(newTime); },
	// set to -1 for endless loop
	SetLoopCount: function(count) { this.baseFile.setNumberOfLoops(count); },
})
function GetAudioFile(name) {
	if (audioFiles[name] == null) {
		var audioFileEntry = LL.settings.audioFiles.FirstWith("name", name);
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
`.trim();
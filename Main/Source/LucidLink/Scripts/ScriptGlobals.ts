import {Assert} from "../../Frame/General/Assert";
import {E} from "../../Frame/Globals";
import {Event} from "../Tracker/Session";
import {Pattern, Matcher, Gap} from "../../Frame/Patterns/Pattern";
import V from "../../Packages/V/V";
import Sound from "react-native-sound";
import {DeviceEventEmitter} from "react-native";
import {LL} from "../../LucidLink";
import {AudioFile} from "../../Frame/AudioFile";
import {Timer, Sleep} from "../../Frame/General/Timers";
import Speech from "react-native-android-speech";
import SPBridge from "../../Frame/SPBridge";
import {SleepStage} from "../../Frame/SPBridge";
import Moment from "moment";

// re-exports
// ==========

export {
	// values (ie normal variables)
	LL,
	// classes
	V, Pattern, Matcher, Gap,
	// enums
	SleepStage,
};

// listeners
// ==========

export function EveryXSecondsDo(seconds, func, maxCallCount = -1) {
	var timer = new Timer(seconds, func, maxCallCount);
	timer.Start();
	LL.scripts.scriptRunner.timers.push(timer);
	return timer;
}

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

export function WhenChangeSleepStageDo(func) {
	LL.scripts.scriptRunner.listeners_whenChangeSleepStage.push(func);
}

var currentSegment_stage = null as SleepStage;
var currentSegment_startTime: Moment.Moment = null;
SPBridge.listeners_onReceiveSleepStage.push((stage: SleepStage)=> {
	if (stage != currentSegment_stage) {
		currentSegment_stage = stage;
		currentSegment_startTime = Moment();
		for (let entry of WhenXMinutesIntoSleepStageDo_entries)
			entry.triggeredForCurrentSleepSegment = false;
		for (let listener of LL.scripts.scriptRunner.listeners_whenChangeSleepStage)
			listener(stage);
	}

	var timeInSegment = Moment().diff(currentSegment_startTime, "minutes", true);
	for (let entry of WhenXMinutesIntoSleepStageDo_entries) {
		if (entry.sleepStage == stage && timeInSegment >= entry.minutes && !entry.triggeredForCurrentSleepSegment) {
			entry.func();
			entry.triggeredForCurrentSleepSegment = true;
		}
	}
});

class WhenXMinutesIntoSleepStageDo_Entry {
	constructor(minutes: number, sleepStage: SleepStage, func: ()=>void) {
		this.minutes = minutes;
		this.sleepStage = sleepStage;
		this.func = func;
	}
	minutes: number;
	sleepStage: SleepStage;
	func: ()=>void;
	triggeredForCurrentSleepSegment = false;
}

var WhenXMinutesIntoSleepStageDo_entries = [] as WhenXMinutesIntoSleepStageDo_Entry[];
export function WhenXMinutesIntoSleepStageYDo(minutesX: number, sleepStageY: string, func: ()=>void) {
	//let sleepStage = SleepStage.entries.FirstOrX(a=>a.name.toLowerCase() == sleepStage.toLowerCase());
	let sleepStage = SleepStage.entries.FirstOrX(a=>a.name == sleepStage);
	Assert(sleepStage, `Sleep-stage must exactly match one of the following: "Absent", "Awake", "Light", "Deep", "Rem"`)
	WhenXMinutesIntoSleepStageDo_entries.push(new WhenXMinutesIntoSleepStageDo_Entry(minutesX, sleepStage, func));
}

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
	LL.tracker.currentSession.AddEvent(event);
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
}

// audio playback
// ==========

export var audioFiles = audioFiles || {};
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
import ScriptContext from "./ScriptContext";
import {Assert} from "../../Frame/General/Assert";
import {E} from "../../Frame/Globals";
import {Event} from "../Tracker/Session";
import {Pattern, Matcher, Gap} from "../../Frame/Patterns/Pattern";
import V from "../../Packages/V/V";
import Sound from "react-native-sound";
import {DeviceEventEmitter} from "react-native";
import {LL, LucidLink} from "../../LucidLink";
import {AudioFile} from "../../Frame/AudioFile";
import {Sleep, Timer, WaitXThenRun, Sequence} from "../../Frame/General/Timers";
import Speech from "react-native-android-speech";
import SPBridge from "../../Frame/SPBridge";
import {SleepStage} from "../../Frame/SPBridge";
import Moment from "moment";

export var scriptContext: ScriptContext = null; // set to LL.scripts.scriptContext;
export function SetScriptContext(context) { scriptContext = context; }

// re-exports
// ==========

export {
	// values (ie normal variables)
	LL,
	// classes
	V, Pattern, Matcher, Gap,
	// enums
	SleepStage,
	// functions,
	//WaitXThenRun,
};

// listeners
// ==========

export function WhenChangeMuseConnectStatus(func) {
	scriptContext.listeners_whenChangeMuseConnectStatus.push(func);
}
export function WhenMusePacketReceived(func) {
	scriptContext.listeners_whenMusePacketReceived.push(func);
}
/*export function WhenViewDirectionUpdated(func) {
	currentScriptContext.listeners_whenViewDirectionUpdated.push(func);
}
export function WhenViewDistanceUpdated(func) {
	currentScriptContext.listeners_whenViewDistanceUpdated.push(func);
}*/

export function WhenChangeSleepStageDo(func) {
	scriptContext.listeners_whenChangeSleepStage.push(func);
}

SPBridge.listeners_onReceiveSleepStage.push((stage: SleepStage)=> {
	if (stage != scriptContext.currentSegment_stage) {
		scriptContext.currentSegment_stage = stage;
		scriptContext.currentSegment_startTime = Moment();
		for (let entry of scriptContext.listeners_whenXMinutesIntoSleepStageY)
			entry.triggeredForCurrentSleepSegment = false;
		for (let listener of scriptContext.listeners_whenChangeSleepStage)
			listener(stage);
	}

	var timeInSegment = Moment().diff(scriptContext.currentSegment_startTime, "minutes", true);
	for (let entry of scriptContext.listeners_whenXMinutesIntoSleepStageY) {
		if (entry.sleepStage == stage && timeInSegment >= entry.minutes && !entry.triggeredForCurrentSleepSegment) {
			entry.func();
			entry.triggeredForCurrentSleepSegment = true;
		}
	}
});

export class WhenXMinutesIntoSleepStageDo_Entry {
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

export function WhenXMinutesIntoSleepStageYDo(minutes: number, sleepStageName: string, func: ()=>void) {
	//let sleepStage = SleepStage.entries.FirstOrX(a=>a.name.toLowerCase() == sleepStageName.toLowerCase());
	let sleepStage = SleepStage.entries.FirstOrX(a=>a.name == sleepStageName) as SleepStage;
	Assert(sleepStage, `Sleep-stage must exactly match one of the following: "Absent", "Awake", "Light", "Deep", "Rem"`);
	let entry = new WhenXMinutesIntoSleepStageDo_Entry(minutes, sleepStage, func);
	scriptContext.listeners_whenXMinutesIntoSleepStageY.push(entry);
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
	scriptContext.patterns.push(pattern);
}

export function CreateTimer(intervalInSec: number, func: Function, maxCallCount = -1, asBackground = true) {
	return new Timer(intervalInSec, func, maxCallCount, asBackground).SetContext(scriptContext.timerContext);
}
export function WaitXThenDo(seconds, func, asBackground = true) {
	return CreateTimer(seconds, func, 1, asBackground).Start();
}
export function EveryXSecondsDo(seconds, func, maxCallCount = -1, asBackground = true) {
	return CreateTimer(seconds, func, maxCallCount, asBackground).Start();
}

export function CreateSequence(asBackground = true) {
	return new Sequence(asBackground).SetContext(scriptContext.timerContext);
}

// input
// ==========

// key-codes can be found here: https://developer.android.com/ndk/reference/keycodes_8h.html
// (or by checking the logs for the key-codes of those pressed)
export function AddKeyDownListener(keyCode, func) {
	scriptContext.keyDownListeners.push({keyCode: keyCode, func: func});
}
export function AddKeyUpListener(keyCode, func) {
	scriptContext.keyDownListeners.push({keyCode: keyCode, func: func});
}

// pattern matching
// ==========

DeviceEventEmitter.addListener("OnSetPatternMatchProbabilities", (args: any)=> {
	var [x, probabilities] = args;
	//Log(`X: ${x}; Probabilities: ${ToJSON(probabilities)}`);
	for (let listener of scriptContext.listeners_onUpdatePatternMatchProbabilities)
		listener(probabilities, x);
});
export function AddListener_OnUpdatePatternMatchProbabilities(func) {
	scriptContext.listeners_onUpdatePatternMatchProbabilities.push(func);
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
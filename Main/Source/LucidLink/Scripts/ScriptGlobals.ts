import ScriptContext from "./ScriptContext";
import {Assert} from "../../Frame/General/Assert";
import {E} from "../../Frame/Globals";
import {Event} from "../Tracker/Session";
import {Pattern, Matcher, Gap} from "../../Frame/Patterns/Pattern";
import Sound from "react-native-sound";
import {DeviceEventEmitter} from "react-native";
import {LL, LucidLink, RunPostInit} from "../../LucidLink";
import {AudioFile, AudioFileManager} from "../../Frame/AudioFile";
import {Sleep, Timer, WaitXThenRun, Sequence} from "../../Frame/General/Timers";
import Speech from "react-native-android-speech";
import SPBridge from "../../Frame/SPBridge";
import {SleepStage} from "../../Frame/SPBridge";
import Moment from "moment";
import V from "../../Packages/V/V";

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

export function CreateTimer(intervalInSec: number, func: ()=>void, maxCallCount = -1, asBackground = true) {
	return new Timer(intervalInSec, func, maxCallCount, asBackground).SetContext(scriptContext.timerContext);
}
export function WaitXThenDo(seconds: number, func: ()=>void, asBackground = true) {
	return CreateTimer(seconds, func, 1, asBackground).Start();
}
export function EveryXSecondsDo(seconds: number, func: ()=>void, maxCallCount = -1, asBackground = true) {
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

export var audioFileManager = new AudioFileManager();
export var GetAudioFile = V.Bind(audioFileManager.GetAudioFile, audioFileManager);

// internal version
var audioFileManager_internal = new AudioFileManager();
var GetAudioFile_Internal = V.Bind(audioFileManager_internal.GetAudioFile, audioFileManager_internal);
RunPostInit(()=> {
	GetAudioFile_Internal("waterfall"); // pre-load waterfall audio
});

// text-to-speech
// ==========

class SpeakOptions {
	text: string;
	forceStop = true;
	pitch = 1;
	delayLength = 1;
	whiteNoiseVolume = 0;
}
export function Speak(optionsDelta: Partial<SpeakOptions>) {
	let options = new SpeakOptions().Extended(optionsDelta);
	options.text = options.text.toString();
	return new Promise((resolve, reject)=> {
		function Speak() {
			Speech.speak(options).then(resolve).catch(ex=> {
				if (ex.toString().contains("TTS is already speaking something")) return;
				throw ex;
			});
		}
		
		if (options.delayLength) {
			// start silent waterfall audio, then delay speaking by 1 second, so bluetooth speaker can activate in time
			GetAudioFile_Internal("waterfall").SetVolume(options.whiteNoiseVolume).Play();
			WaitXThenDo(options.delayLength, ()=> {
				Speak();
				GetAudioFile_Internal("waterfall").Stop();
			});
		} else {
			Speak();
		}
	});
};
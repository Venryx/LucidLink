import {scriptContext, SetScriptContext, WhenXMinutesIntoSleepStageDo_Entry} from "./ScriptGlobals";
import {EEGProcessor} from "../../Frame/Patterns/EEGProcessor";
import {Log} from "../../Frame/Globals";
import {LL} from "../../LucidLink";

import * as Globals from "../../Frame/Globals";
import * as ScriptGlobals from "./ScriptGlobals";
import {Pattern} from "../../Frame/Patterns/Pattern";
import {WaitXThenRun, Timer, TimerContext} from "../../Frame/General/Timers";
import Moment from "moment";
import {SleepStage} from "../../Frame/SPBridge";

export default class ScriptContext {
	constructor() {
		SetScriptContext(this);
		//this.Reset();
		WaitXThenRun(0, ()=>this.Reset()); // call in a bit, so LL is initialized
	}

	// general
	// ==========

	patterns = [] as Pattern[]; // func-based patterns

	timerContext = new TimerContext();
	keyDownListeners = [];
	TriggerKeyDown(keyCode) {
		for (let listener of this.keyDownListeners) {
			if (listener.keyCode == keyCode)
				listener.func(keyCode);
		}
	}
	keyUpListeners = [];
	TriggerKeyUp(keyCode) {
		for (let listener of this.keyUpListeners) {
			if (listener.keyCode == keyCode)
				listener.func(keyCode);
		}
	}
	
	listeners_whenChangeMuseConnectStatus = [];
	listeners_whenMusePacketReceived = [];
	/*listeners_whenViewDirectionUpdated = [];
	listeners_whenViewDistanceUpdated = [];*/
	listeners_onUpdatePatternMatchProbabilities = [];
	listeners_whenChangeSleepStage = [];
	
	currentSegment_stage = null as SleepStage;
	currentSegment_startTime: Moment.Moment = null;
	listeners_whenXMinutesIntoSleepStageY = [] as WhenXMinutesIntoSleepStageDo_Entry[];

	Reset() {
		this.patterns = [];
		//this.patternMatchAttempts = {};
		//LL.monitor.eegProcessor.patternMatchAttempts = {};
		//LL.monitor.eegProcessor = new EEGProcessor();
		LL.tools.monitor.eegProcessor.ResetScriptsRelatedStuff();
		
		this.timerContext.CloseAndReset();
		this.keyDownListeners = [];
		this.keyUpListeners = [];

		this.listeners_whenChangeMuseConnectStatus = [];
		this.listeners_whenMusePacketReceived = [LL.tools.monitor.eegProcessor.OnReceiveMusePacket];
		this.listeners_onUpdatePatternMatchProbabilities = [];
		this.listeners_whenChangeSleepStage = [];
		this.listeners_whenXMinutesIntoSleepStageY = [];
	}
	//currentExecutionScope = {};
	Apply(scripts) {
		/*var scriptExecutionScope = {};
		this.currentExecutionScope = scriptExecutionScope;
		scriptExecutionScope.Extend(Globals);
		scriptExecutionScope.Extend(ScriptGlobals);*/

		// make exports from Globals and ScriptGlobals directly accessible [eg: Log("something")]
		for (var key in Globals)
			eval(`var ${key} = Globals.${key}`);
		for (var key in ScriptGlobals)
			eval(`var ${key} = ScriptGlobals.${key}`);

		for (let script of scripts) {
			try {
				/*eval(`with (scriptExecutionScope) { ${script.text}
}`);*/
				eval(script.text);
			} catch (error) {
				var stack = error.stack.replace(/\r/g, "")
				stack = stack.substr(0, stack.indexOf("  at ScriptRunner.Apply (")) // remove out-of-user-script stack-entries
				// simplify the "eval ([...])" line
				stack = stack.replace(/eval \([^)]+?\), (<anonymous>:)/g, (match, sub1)=>sub1);
				var errorStr = `${error}
Stack) ${stack}`;
				Log(errorStr)
				alert(errorStr);
			}
		}
	}
}
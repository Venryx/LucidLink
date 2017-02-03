import {EEGProcessor} from "../../Frame/Patterns/EEGProcessor";
import {Log, WaitXThenRun} from "../../Frame/Globals";
import {LL} from "../../LucidLink";

import * as Globals from "../../Frame/Globals";
import * as ScriptGlobals from "./ScriptGlobals";
import {Pattern} from "../../Frame/Patterns/Pattern";

export default class ScriptRunner {
	constructor() {
		//this.Reset();
		WaitXThenRun(0, ()=>this.Reset()); // call in a bit, so LL is initialized
	}

	// general
	// ==========

	patterns: Pattern[] = []; // func-based patterns

	timers = [];
	listeners_whenChangeMuseConnectStatus = [];
	listeners_whenMusePacketReceived = [];
	/*listeners_whenViewDirectionUpdated = [];
	listeners_whenViewDistanceUpdated = [];*/
	listeners_onUpdatePatternMatchProbabilities = [];

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

	Reset() {
		this.patterns = [];
		//this.patternMatchAttempts = {};
		//LL.monitor.eegProcessor.patternMatchAttempts = {};
		//LL.monitor.eegProcessor = new EEGProcessor();
		LL.tools.monitor.eegProcessor.ResetScriptsRelatedStuff();
		
		for (let timer of this.timers)
			timer.Stop();
		this.timers = [];
		this.listeners_whenChangeMuseConnectStatus = [];
		this.listeners_whenMusePacketReceived = [LL.tools.monitor.eegProcessor.OnReceiveMusePacket];
		this.listeners_onUpdatePatternMatchProbabilities = [];
		this.keyDownListeners = [];
		this.keyUpListeners = [];
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
import {Log, WaitXThenRun} from "../../Frame/Globals";
import {LL} from "../../LucidLink";
require("./ScriptGlobals");

export default class ScriptRunner {
	//get Main() { return LL.scripts.scriptRunner; }

	constructor() {
		//this.Reset();
		WaitXThenRun(0, ()=>this.Reset()); // call in a bit, so LL is initialized
	}

	// general
	// ==========

	patterns = []; // func-based patterns

	timers = [];
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
		LL.monitor.eegProcessor.patternMatchAttempts = {};
		
		for (let timer of this.timers)
			timer.Stop();
		this.timers = [];
		this.listeners_whenMusePacketReceived = [LL.monitor.eegProcessor.OnReceiveMusePacket];
		this.listeners_onUpdatePatternMatchProbabilities = [];
		this.keyDownListeners = [];
		this.keyUpListeners = [];
	}
	Init(scripts) {
		var finalScriptsText = "";
		for (let script of scripts) {
			finalScriptsText += (finalScriptsText.length ? "\n\n//==========\n\n" : "") + script.text;
		}
		try {
			eval(finalScriptsText);
		} catch (error) {
			var stack = error.stack.replace(/\r/g, "")
			stack = stack.substr(0, stack.indexOf("  at ScriptRunner.Init (")) // remove out-of-user-script stack-entries
			stack = stack.replace(/eval \([^)]+?\), (<anonymous>:)/g, (match, sub1)=>sub1);
			var errorStr = `${error}
Stack) ${stack}`;
			Log(errorStr)
			alert(errorStr);
		}
	}
}
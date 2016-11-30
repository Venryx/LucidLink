// for scripts
//import Sound from "react-native-sound";
var Sound = require("react-native-sound");

DeviceEventEmitter.addListener("OnSetPatternMatchProbabilities", args=> {
	var [x, probabilities] = args;
	//Log(`X: ${x}; Probabilities: ${ToJSON(probabilities)}`);
	for (let listener of LL.scripts.scriptRunner.listeners_onUpdatePatternMatchProbabilities)
		listener(probabilities, x);
});
g.AddListener_OnUpdatePatternMatchProbabilities = function(func) {
	LL.scripts.scriptRunner.listeners_onUpdatePatternMatchProbabilities.push(func);
};

export default class ScriptRunner {
	//get Main() { return LL.scripts.scriptRunner; }

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

	listeners_onUpdatePatternMatchProbabilities = [];

	Reset() {
		this.keyDownListeners = [];
		this.keyUpListeners = [];
		this.listeners_onUpdatePatternMatchProbabilities = [];
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
export default class ScriptRunner {
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
		this.keyDownListeners = [];
		this.keyUpListeners = [];
	}
	Init(scripts) {
		var finalScriptsText = "";
		for (let [index, script] of scripts.entries()) {
			finalScriptsText += (index ? "\n\n//==========\n\n" : "") + script;
		}
		try {
			eval(finalScriptsText);
		} catch (error) {
			var stack = error.stack.replace(/\r/g, "")
			stack = stack.substr(0, stack.indexOf("  at ScriptRunner.Init (")) // remove out-of-user-script stack-entries
			stack = stack.replace(/eval \([^)]+?\), (<anonymous>:)/g, (match, sub1)=>sub1);
			alert(`${stack}`);
		}
	}
}
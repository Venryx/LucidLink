import {Log} from "../../Frame/Globals";
import {LL} from "../../LucidLink";

import * as Globals from "../../Frame/Globals";
import * as ScriptGlobals from "../Scripts/ScriptGlobals";
import {GraphRow} from "./Graph/GraphRow";
import {GraphOverlay} from "./Graph/GraphOverlay";
import {Assert} from "../../Frame/General/Assert";
import {WaitXThenRun} from "../../Frame/General/Timers";

// displayer-script globals
// ==========

function AddGraphRow(info) {
	var graphRow = new GraphRow(info);
	LL.tracker.scriptRunner.graphRows.push(graphRow);
}
function AddGraphOverlay(info) {
	Assert(LL.tracker.scriptRunner.graphOverlay == null, "For now, cannot add more than one graph-overlay.");

	var graphOverlay = new GraphOverlay(info);
	LL.tracker.scriptRunner.graphOverlay = graphOverlay;
}

export default class DisplayerScriptRunner {
	constructor() {
		//this.Reset();
		WaitXThenRun(0, ()=>this.Reset()); // call in a bit, so LL is initialized
	}

	// general
	// ==========

	graphRows: GraphRow[] = [];
	graphOverlay: GraphOverlay = null;

	Reset() {
		this.graphRows = [];
		this.graphOverlay = null;
	}

	Apply(scripts) {
		// make exports from Globals and ScriptGlobals directly accessible [eg: Log("something")]
		for (var key in Globals)
			eval(`var ${key} = Globals.${key}`);
		for (var key in ScriptGlobals)
			eval(`var ${key} = ScriptGlobals.${key}`);

		for (let script of scripts) {
			try {
				eval(script.text);
			} catch (error) {
				var stack = error.stack.replace(/\r/g, "")
				stack = stack.substr(0, stack.indexOf("  at DisplayerScriptRunner.Apply (")) // remove out-of-user-script stack-entries
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
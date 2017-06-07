import {Dictionary} from "../../Packages/VDF/VDFExtras";
import {P, T} from "../../Packages/VDF/VDFTypeInfo";
import {Assert} from "../General/Assert";
import {WaitXThenRun_BuiltIn} from "../General/Timers";

export default class BlockRunInfo {
	static fakeBlockRunInfo: BlockRunInfo;
	static _=WaitXThenRun_BuiltIn(0, ()=>BlockRunInfo.fakeBlockRunInfo = new BlockRunInfo(null, "fakeBlockRunInfo", true, -1));

	constructor(...args) {
		if (args.length == 2) var [parent, depth] = args;
		else if (args.length == 4) var [parent, name, method, depth] = args;

		if (depth > 100)
			throw new Error("Cannot profile call-path with a depth greater than 100.");
		this.root = parent != null ? parent.root : this;
		this.parent = parent;
		this.depth = depth;

		this.name = name;
		this.method = method;
	}

	root = null; // for keeping track of what profiler self is part of
	parent = null;
	@P() method = false; // (as opposed to a section)
	depth = -1;
	@P() name = null;
	timer_startTime = -1;
	@P() runTime = 0;
	//@T("Dictionary(string BlockRunInfo)") @P() children = {};
	@T("Dictionary(string BlockRunInfo)") @P() children = new Dictionary<string, BlockRunInfo>("string", "BlockRunInfo");
	currentChild: BlockRunInfo = null;

	GetCurrentDescendant() : BlockRunInfo {
		if (this.currentChild != null)
			return this.currentChild.GetCurrentDescendant();
		return this;
	}

	@P() runCount = 0;
	Start() {
		//Profiler_AllRuns.currentCallPath.Add(this);
		//Profiler_AllRuns.currentCallPath.Count.ShouldBe(depth + 1);
		if (this.parent != null)
			this.parent.currentChild = this;
		this.timer_startTime = new Date().getTime();
		this.runCount++;
		return this;
	}

	// for child methods
	StartMethod(...args): BlockRunInfo {
		if (args[0] instanceof Function) var [method] = args, name = method.GetName();
		else var [name] = args;

		// todo: make-so this handles calls from other threads (e.g. by appending thread-id to name)
		// if fake-block-run-info, just return a fake block-run-info (ie do nothing)
		if (this == BlockRunInfo.fakeBlockRunInfo)
			return BlockRunInfo.fakeBlockRunInfo;
		
		/*while (this.children[name])
			name += "_";*/

		if (!this.children.ContainsKey(name))
			this.children.Add(name, new BlockRunInfo(this, name, true, this.depth + 1));
		this.children.Get(name).Start();
		return this.children.Get(name);
	}

	// for child sections
	Section(name): BlockRunInfo {
		if (this == BlockRunInfo.fakeBlockRunInfo)
			return BlockRunInfo.fakeBlockRunInfo;

		/*if (name == null || name == "") {
			this.End();
			return BlockRunInfo.fakeBlockRunInfo;
		}*/

		this.EndLastSection();
		if (!this.children.ContainsKey(name))
			this.children.Add(name, new BlockRunInfo(this, name, false, this.depth + 1));
		this.children.Get(name).Start();
		return this.children.Get(name);
	}
	EndLastSection() {
		/*var lastRunningSection = children.LastOrDefault(a=>!a.Value.method && a.Value.timer.IsRunning);
		if (lastRunningSection.Value != null)
			lastRunningSection.Value.End();*/
		if (this.currentChild != null)
			this.currentChild.End();
	}

	End<T>(result?: T): T {
		if (this == BlockRunInfo.fakeBlockRunInfo) return result;
		Assert(this.runCount > 0); // confirm that this block we're ending was at some point started
		if (this.timer_startTime == -1) return result; // if already ended, return

		this.EndLastSection();

		//timer.Stop(); // Stop() is called automatically by Reset() below
		//runTime = timer.ElapsedTicks;
		this.runTime += new Date().getTime() - this.timer_startTime; // show in ms
		this.timer_startTime = -1;
		
		//Profiler_AllRuns.currentCallPath.RemoveAt(Profiler_AllRuns.currentCallPath.Count - 1);
		//Profiler_AllRuns.currentCallPath.Count.ShouldBe(depth);
		if (this.parent != null)
			this.parent.currentChild = null;

		return result;
	}
}
global.Extend({BlockRunInfo});
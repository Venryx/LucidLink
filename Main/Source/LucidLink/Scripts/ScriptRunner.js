require("./ScriptGlobals");

export default g.ScriptRunner = class ScriptRunner {
	//get Main() { return LL.scripts.scriptRunner; }

	constructor() {
		this.listeners_whenMusePacketReceived.push(this.OnReceiveMusePacket);

		/*this.channelPoints = [];
		for (let i = 0; i < 4; i++)
			this.channelPoints[i] = Array(1 + this.maxX).fill().map((_, i)=>new Vector2i(i, 0));*/
		this.packets = Array(1 + this.maxX);
	}

	//channelPoints = [];
	packets = [];

	currentIndex = -1;
	currentX = -1;
	maxX = 1000;
	@Bind OnReceiveMusePacket(packet) {
		this.currentIndex++;
		this.currentX = this.currentX < this.maxX ? this.currentX + 1 : 0;

		packet.x = this.currentX;
		var self = this;
		packet.GetPeer = function(offset) {
			self.packets[(this.x + offset).WrapToRange(0, this.maxX)];
		};

		/*for (let ch = 0; ch < 4; ch++)
			this.channelPoints[ch][this.currentX] = packet.eegValues[ch];*/
		this.packets[this.currentX] = packet;

		for (let [index, pattern] of this.patterns.entries()) {
			let tooCloseToOtherMatchAttempt = false;
			for (let x = this.currentX - 1; x > this.currentX - pattern.minStartInterval; x--) {
				let key = `pattern${index}_x${x}`;
				if (this.patternMatchAttempts[x]) {
					tooCloseToOtherMatchAttempt = true;
					break;
				}
			}
			if (tooCloseToOtherMatchAttempt) continue;

			let key = `pattern${index}_x${this.currentX}`;
			let matchAttempt = new PatternMatchAttempt(key, pattern);
			this.patternMatchAttempts[key] = matchAttempt;
		}

		for (let {name: key, value: matchAttempt} of this.patternMatchAttempts.Props) {
			matchAttempt.ProcessPacket(this.currentX, packet);
		}
	}

	patterns = []; // func-based patterns
	patternMatchAttempts = {};
	CancelPatternMatchAttempt(matchAttempt) {
		delete this.patternMatchAttempts[matchAttempt.key];
	}

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
		this.patternMatchAttempts = {};
		
		for (let timer of this.timers)
			timer.Stop();
		this.timers = [];
		this.listeners_whenMusePacketReceived = [];
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
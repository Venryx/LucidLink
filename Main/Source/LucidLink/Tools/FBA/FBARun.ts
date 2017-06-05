import {TimerContext, Sequence, Timer} from "../../../Frame/General/Timers";
import {AudioFileManager} from "../../../Frame/AudioFile";
import {SleepStage} from "../../../Frame/SPBridge";
import Moment from "moment";
import {LL} from "../../../LucidLink";
import {JavaBridge} from "../../../Frame/Globals";
import SPBridge from "../../../Frame/SPBridge";
import {PlayAudioFile} from "../@Shared/Action";
import {Log} from "../../../Packages/VDF/VDF";
import {WaitXThenDo, Speak} from "../../Scripts/ScriptGlobals";

export class ListenersContext {
	listEntries = [] as {list, entry}[];
	AddListEntry(list, entry) {
		list.push(entry);
		this.listEntries.push({list, entry});
	}
	Reset() {
		for (let {list, entry} of this.listEntries) {
			list.Remove(entry);
		}
		this.listEntries.Clear();
	}
}

export default class FBARun {
	timerContext = new TimerContext();
	listenersContext = new ListenersContext();
	audioFileManager = new AudioFileManager();
	
	remSequence: Sequence;
	StopSequence() {
		if (this.remSequence)
			this.remSequence.Stop();
	}
	
	currentSegment_stage = null as SleepStage;
	currentSegment_startTime: Moment.Moment = null;
	Start() {
		let node = LL.tools.fba;
		JavaBridge.Main.SetVolumes(node.normalVolume, node.bluetoothVolume);
		if (node.backgroundMusic_enabled) {
			for (let track of node.backgroundMusic_tracks) {
				var audioFile = this.audioFileManager.GetAudioFile(track);
				audioFile.PlayCount = -1;
				//audioFile.Stop().SetVolume(0);
				audioFile.Play({delay: 0}).SetVolume(node.backgroundMusic_volume);
			}
			new Timer(30, ()=> {
				for (let track of node.backgroundMusic_tracks) {
					var audioFile = this.audioFileManager.GetAudioFile(track);
					audioFile.Play({delay: 0}).SetVolume(node.backgroundMusic_volume);
				}
			}).Start().SetContext(this.timerContext);
		}

		LL.tracker.currentSession.StartSleepSession();

		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveSleepStage, (stage: SleepStage)=> {
			if (stage != this.currentSegment_stage) {
				this.currentSegment_stage = stage as SleepStage;
				this.currentSegment_startTime = Moment();
				//Log("New sleep stage: " + stage)
				this.StopSequence();
				this.triggeredForThisSegment = false;
			}

			var timeInSegment = Moment().diff(this.currentSegment_startTime, "minutes", true);
			let promptStartDelay_final = node.promptStartDelay + (20 / 60); // add 20s, so we don't clash with status-monitor speech
			if (stage == SleepStage.V.Rem && timeInSegment >= promptStartDelay_final && !this.triggeredForThisSegment) {
				this.triggeredForThisSegment = true;
				this.RunActions();

				this.remSequence = new Sequence().SetContext(this.timerContext);
				for (var i = 0; i <= 100; i++) {
					this.remSequence.AddSegment(node.promptInterval * 60, ()=>this.RunActions());
				}
				this.remSequence.Start();
			}
		});

		let lastBreathValueReceiveTime = Date.now();
		this.listenersContext.AddListEntry(SPBridge.listeners_onReceiveBreathValues, (breathValue1: number, breathValue2: number)=> {
			lastBreathValueReceiveTime = Date.now();
		});
		new Timer(10, ()=> {
			// if it's been more than 10 seconds since the last breath-value receive, restart S+ data stream
			let timeSince = Date.now() - lastBreathValueReceiveTime;
			if (timeSince > 10000) {
				Log(`It's been ${Math.round(timeSince / 1000)} seconds since the last breath-value was received, so restarting S+ data stream...`);
				/*SPBridge.StopSession();
				WaitXThenDo(1, ()=> {
					SPBridge.StartSleepSession();
				});*/
				SPBridge.RestartDataStream();
			}
		}).Start().SetContext(this.timerContext);

		this.StartStatusReporter();

		if (g.FBA_PostStart) g.FBA_PostStart();
	}
	StartStatusReporter() {
		let node = LL.tools.fba;
		if (node.statusReporter.reportInterval == 0) return;
		new Timer(node.statusReporter.reportInterval * 60, ()=> {
			let finalMessage = node.statusReporter.reportText;
			finalMessage = ReplaceVariablesInReportText(finalMessage);

			Speak({text: finalMessage, volume: node.statusReporter.volume, pitch: node.statusReporter.pitch});
		}).Start().SetContext(this.timerContext);
	}

	triggeredForThisSegment = false;
	RunActions() {
		let node = LL.tools.fba;
		for (let action of node.promptActions) {
			if (action instanceof PlayAudioFile) {
				action.Run(this.audioFileManager);
			} else {
				action.Run();
			}
		}
	}

	Stop() {
		this.timerContext.Reset();
		this.listenersContext.Reset();
		this.audioFileManager.Reset();
		LL.tracker.currentSession.CurrentSleepSession.End();
		if (g.FBA_PostStop) g.FBA_PostStop();
	}
}

function ReplaceVariablesInReportText(text: string) {
	let result = text;
	result = result; // todo
	return result;
}
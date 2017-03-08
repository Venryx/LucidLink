import {EEGProcessor} from "../../Frame/Patterns/EEGProcessor";
import {BaseComponent as Component, Column, Panel, Row, VButton, RowLR, VText, BaseProps} from "../../Frame/ReactGlobals";
import {colors, styles} from "../../Frame/Styles";
import {Vector2i} from "../../Frame/Graphics/VectorStructs";
import {Observer, observer} from "mobx-react/native";
import Drawer from "react-native-drawer";
import {MKRangeSlider} from "react-native-material-kit";
import DialogAndroid from "react-native-dialogs";
import {Text, Switch, View, ScrollView} from "react-native";
import {LL} from "../../LucidLink";
import Node from "../../Packages/VTree/Node";
import {P} from "../../Packages/VDF/VDFTypeInfo";
import {VSwitch, VSwitch_Auto} from "../../Packages/ReactNativeComponents/VSwitch";
import {NumberPicker_Auto} from "../../Packages/ReactNativeComponents/NumberPicker";
import {autorun} from "mobx";
import {EveryXSecondsDo, GetRandomNumber, Speak, WhenXMinutesIntoSleepStageYDo, CreateSequence} from "../Scripts/ScriptGlobals";
import {Log, Global, JavaBridge, Toast} from "../../Frame/Globals";
import Sound from "react-native-sound";
import {AudioFile, AudioFileManager} from "../../Frame/AudioFile";
import {Sequence, Timer, TimerContext, WaitXThenRun} from "../../Frame/General/Timers";
import {VTextInput, VTextInput_Auto} from "../../Packages/ReactNativeComponents/VTextInput";
import BackgroundMusicConfigUI from "./@Shared/BackgroundMusicSelectorUI";
import SPBridge from "../../Frame/SPBridge";
import {SleepStage} from "../../Frame/SPBridge";
import Moment from "moment";
import V from "../../Packages/V/V";
import {Action, SpeakText, PlayAudioFile} from "./@Shared/Action";

@Global
export class FBA extends Node {
	constructor() {
		super();
		WaitXThenRun(0, ()=> {
			var lastEnabled = null;
			autorun(()=> {
				if (this != LL.tools.fba) return;

				// if enabled-ness changed
				if (this.enabled != lastEnabled) {
					lastEnabled = this.enabled;

					if (this.enabled) {
						this.currentRun = new FBARun();
						this.currentRun.Start();
					} else {
						if (this.currentRun)
							this.currentRun.Stop();
						this.currentRun = null;
					}
				}
			});
		});
	}

	@O enabled = false;
	@O @P() normalVolume = .15;
	@O @P() bluetoothVolume = .15;
	@O @P() promptStartDelay = 3;
	@O @P() promptInterval = 3;

	@O @P() promptActions = [] as Action[];

	@O @P() backgroundMusic_enabled = false;
	@O @P() backgroundMusic_volume = .05;
	@O @P() backgroundMusic_tracks = [];

	currentRun: FBARun;
}

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

class FBARun {
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
			if (stage == SleepStage.V.Rem && timeInSegment >= node.promptStartDelay && !this.triggeredForThisSegment) {
				this.triggeredForThisSegment = true;
				this.RunActions();

				this.remSequence = new Sequence().SetContext(this.timerContext);
				for (var i = 0; i <= 100; i++) {
					this.remSequence.AddSegment(node.promptInterval * 60, ()=>this.RunActions());
				}
				this.remSequence.Start();
			}
		});
		if (g.FBA_PostStart) g.FBA_PostStart();
	}

	triggeredForThisSegment = false;
	RunActions() {
		let node = LL.tools.fba;
		for (let action of node.promptActions) {
			if (action instanceof PlayAudioFile)
				action.Run(this.audioFileManager);
			else
				action.Run();
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

@observer
export class FBAUI extends Component<{}, {}> {
	render() {
		var node = LL.tools.fba;
		return (
			<ScrollView style={{flex: 1, flexDirection: "column"}}>
				<Row style={{flex: 1, flexDirection: "column", padding: 10}}>
					<Row>
						<VText mt={2} mr={10}>Enabled: </VText>
						<VSwitch_Auto path={()=>node.p.enabled}/>
					</Row>
					<Row>
						<VText mt={5} mr={10}>Volume, normal:</VText>
						<NumberPicker_Auto path={()=>node.p.normalVolume} max={1} step={.01} format={a=>(a * 100).toFixed() + "%"}/>
						<VText mt={5} ml={10} mr={10}>Bluetooth:</VText>
						<NumberPicker_Auto path={()=>node.p.bluetoothVolume} max={1} step={.01} format={a=>(a * 100).toFixed() + "%"}/>
					</Row>
					<Row>
						<VText mt={5} mr={10}>Prompt delay from REM onset:</VText>
						<NumberPicker_Auto path={()=>node.p.promptStartDelay} format={a=>a + " minutes"}/>
					</Row>
					<Row>
						<VText mt={5} mr={10}>Prompt interval:</VText>
						<NumberPicker_Auto path={()=>node.p.promptInterval} format={a=>a + " minutes"}/>
					</Row>
					<Row>
						<VText mt={5} mr={10}>Prompt actions:</VText>
					</Row>
					<Row style={{backgroundColor: colors.background_dark, flexDirection: "column", padding: 5}}>
						<Column style={{flex: 1, backgroundColor: colors.background, padding: 10}}>
							{node.promptActions.map((action, index)=> {
								return action.CreateUI(index, ()=>node.promptActions.Remove(action));
							})}
						</Column>
						<Row mt={10} height={45}>
							<VText mt={9}>Add: </VText>
							<VButton text="Speak text" plr={10} style={{height: 40}}
								onPress={()=>node.promptActions.push(new SpeakText())}/>
							<VButton text="Play audio file" ml={5} plr={10} style={{height: 40}}
								onPress={()=>node.promptActions.push(new PlayAudioFile())}/>
						</Row>
					</Row>
					<BackgroundMusicConfigUI node={node}/>

					{/*<VButton text="Test1" ml={5} plr={10} style={{height: 40}}
						onPress={()=> {
							var sequence = new Sequence();
							sequence.AddSegment(3, ()=> {
								Toast("3");
							});
							sequence.AddSegment(3, ()=> {
								Toast("6");
								sequence.Stop();
							});
							sequence.AddSegment(3, ()=> {
								Toast("9");
							});
							sequence.Start();
						}}/>*/}
				</Row>
			</ScrollView>
		);
	}
}
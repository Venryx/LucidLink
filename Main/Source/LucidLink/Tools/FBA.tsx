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
import {Log, Global, JavaBridge} from "../../Frame/Globals";
import Sound from "react-native-sound";
import {AudioFile} from "../../Frame/AudioFile";
import {Sequence, Timer, TimerContext, WaitXThenRun} from "../../Frame/General/Timers";
import {VTextInput, VTextInput_Auto} from "../../Packages/ReactNativeComponents/VTextInput";
import BackgroundMusicConfigUI from "./@Shared/BackgroundMusicSelectorUI";
import SPBridge from "../../Frame/SPBridge";
import {SleepStage} from "../../Frame/SPBridge";
import Moment from "moment";

var audioFiles = audioFiles || {};
function GetAudioFile(name: string, onLoaded?: Function): AudioFile {
	if (audioFiles[name] == null) {
		var audioFileEntry = LL.settings.audioFiles.First(a=>a.name == name);
		if (audioFileEntry == null)
			alert(`Cannot find audio-file entry with name "${name}".`);
		var audioFile = new AudioFile(audioFileEntry.path, onLoaded);
		audioFiles[name] = audioFile;
	}
	return audioFiles[name];
}

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
					}
					else {
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
	@O @P() phrase = "Remember lucid dream.";

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
	CloseAndReset() {
		for (let {list, entry} of this.listEntries) {
			list.Remove(entry);
		}
		this.listEntries.Clear();
	}
}

class FBARun {
	timerContext = new TimerContext();
	listenersContext = new ListenersContext();
	
	remSequence: Sequence;
	StopSequence() {
		if (this.remSequence && this.remSequence.currentSegmentTimeout)
			this.remSequence.Stop();
	}
	
	currentSegment_stage = null as SleepStage;
	currentSegment_startTime: Moment.Moment = null;
	Start() {
		let node = LL.tools.fba;

		JavaBridge.Main.SetVolumes(node.normalVolume, node.bluetoothVolume);
		
		if (node.backgroundMusic_enabled) {
			for (let track of node.backgroundMusic_tracks) {
				var audioFile = GetAudioFile(track);
				audioFile.PlayCount = -1;
				//audioFile.Stop().SetVolume(0);
				audioFile.Play().SetVolume(node.backgroundMusic_volume);
			}
			new Timer(30, ()=> {
				for (let track of node.backgroundMusic_tracks) {
					var audioFile = GetAudioFile(track);
					audioFile.Play().SetVolume(node.backgroundMusic_volume);
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
			}

			var timeInSegment = Moment().diff(this.currentSegment_startTime, "minutes", true);
			if (stage == SleepStage.V.Rem && timeInSegment >= node.promptStartDelay) {
				Speak({text: node.phrase});
				Log(node.phrase);
				this.StopSequence()

				this.remSequence = new Sequence();
				for (var i = 0; i <= 100; i++) {
					this.remSequence.AddSegment(3 * 100, function() {
						Speak({text: node.phrase});
						Log(node.phrase);
					})
				}
				this.remSequence.Start();
			}
		});
		
		if (g.FBA_PostStart) g.FBA_PostStart();
	}
	Stop() {
		this.timerContext.CloseAndReset();
		this.listenersContext.CloseAndReset();

		LL.tracker.currentSession.CurrentSleepSession.End();

		// stop and clear (background-music) audio-files
		for (let audioFile of audioFiles.Props.Select(a=>a.value))
			audioFile.Stop();
		audioFiles = [];
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
					<Row height={35}>
						<VText mt={5} mr={10}>Prompt phrase:</VText>
						<VTextInput_Auto style={{flex: 1, height: 35}} editable={true} path={()=>node.p.phrase}/>
					</Row>

					<BackgroundMusicConfigUI node={node}/>
				</Row>
			</ScrollView>
		);
	}
}
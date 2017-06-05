import {EEGProcessor} from "../../Frame/Patterns/EEGProcessor";
import {BaseComponent as Component, Column, Panel, Row, VButton, RowLR, BaseProps, BaseComponent} from "../../Frame/ReactGlobals";
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
import VText from "../../Frame/Components/VText";
import FBARun from "./FBA/FBARun";

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

	@O @P() commandListener = new FBA_CommandListener();
	@O @P() statusReporter = new FBA_StatusReporter();

	currentRun: FBARun;
}

@Global
export class FBA_CommandListener extends Node {
	@O @P() sequenceDisabler_breathDepthCutoff = 10;
	@O @P() sequenceDisabler_disableLength = 15;
	@O @P() sequenceDisabler_messageSpeakAction = new SpeakText({text: "Movement detected, disabling rem-sequence."});
}

@Global
export class FBA_StatusReporter extends Node {
	@O @P() reportInterval = 1;
	@O @P() reportText = "stage @sleepStage for @sleepStageTime, sequence @remSequenceEnabled"
	@O @P() volume = -.01;
	@O @P() pitch = 1;
}
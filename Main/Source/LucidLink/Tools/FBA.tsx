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
import {EveryXSecondsDo, GetRandomNumber, Speak} from "../Scripts/ScriptGlobals";
import {Log, Global} from "../../Frame/Globals";
import Sound from "react-native-sound";
import {AudioFile} from "../../Frame/AudioFile";
import {WaitXThenRun, Timer} from "../../Frame/General/Timers";
import {VTextInput, VTextInput_Auto} from "../../Packages/ReactNativeComponents/VTextInput";
import BackgroundMusicConfigUI from "./@Shared/BackgroundMusicSelectorUI";

var audioFiles = audioFiles || {};
function GetAudioFile(name) {
	if (audioFiles[name] == null) {
		var audioFileEntry = LL.settings.audioFiles.First(a=>a.name == name);
		if (audioFileEntry == null)
			alert("Cannot find audio-file entry with name '" + name + "'.");
		var baseFile = new Sound(audioFileEntry.path, "", function(error) {
			if (error)
				console.log("Failed to load the sound '" + name + "':", error);
		});
		var audioFile = new AudioFile(baseFile);
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

					if (this.enabled)
						this.Start();
					else
						this.Stop();
				}
			});
		});
	}

	@O enabled = false;

	@O @P() backgroundMusic_enabled = false;
	@O @P() backgroundMusic_volume = .05;
	@O @P() backgroundMusic_tracks = [];

	backgroundMusicRestartTimer: Timer;
	Start() {
		if (this.backgroundMusic_enabled) {
			for (let track of this.backgroundMusic_tracks) {
				var audioFile = GetAudioFile(track);
				audioFile.PlayCount = -1;
				//audioFile.Stop().SetVolume(0);
				// wait a bit (apparently audio files need some time to load)
				WaitXThenRun(1000, ()=> {
					audioFile.Play();
					audioFile.SetVolume(this.backgroundMusic_volume);
				});
			}
			this.backgroundMusicRestartTimer = new Timer(30, ()=> {
				for (let track of this.backgroundMusic_tracks) {
					var audioFile = GetAudioFile(track);
					audioFile.Play();
					audioFile.SetVolume(this.backgroundMusic_volume);
				}
			}).Start();
		}
		if (g.FBA_PostStart) g.FBA_PostStart();
	}
	Stop() {
		if (this.backgroundMusicRestartTimer) {
			this.backgroundMusicRestartTimer.Stop();
			this.backgroundMusicRestartTimer = null;

			// stop and clear (background-music) audio-files
			for (let audioFile of audioFiles.Props.Select(a=>a.value))
				audioFile.Stop();
			audioFiles = [];
		}
	}
}

@observer
export class FBAUI extends Component<{} & BaseProps, {}> {
	render() {
		var node = LL.tools.fba;
		return (
			<ScrollView style={{flex: 1, flexDirection: "column"}}>
				<Row style={{flex: 1, flexDirection: "column", padding: 10}}>
					<Row height={30}>
						<VText mt={2} mr={10}>Enabled: </VText>
						<VSwitch_Auto path={()=>node.p.enabled}/>
					</Row>

					<BackgroundMusicConfigUI node={node}/>
				</Row>
			</ScrollView>
		);
	}
}
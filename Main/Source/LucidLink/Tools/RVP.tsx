import {EEGProcessor} from "../../Frame/Patterns/EEGProcessor";
import {BaseComponent as Component, Column, Panel, Row, VButton, RowLR, VText, VTextInput, VTextInput_Auto} from "../../Frame/ReactGlobals";
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
import {Log, Timer, WaitXThenRun} from "../../Frame/Globals";
import Sound from "react-native-sound";
import {AudioFile} from "../../Frame/AudioFile";

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

export class RVP extends Node {
	constructor() {
		super();
		WaitXThenRun(0, ()=> {
			var lastEnabled = null;
			autorun(()=> {
				if (this != LL.tools.rvp) return;

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
	@O @P() interval = 120;

	/*@O @P() part1_minVolume = .3;
	@O @P() part1_maxVolume = .3;*/
	@O @P() part1_minPitch = .01;
	@O @P() part1_maxPitch = 1.5;

	@O @P() pauseDuration = 3;

	/*@O @P() part2_minVolume = .3;
	@O @P() part2_maxVolume = .3;*/
	@O @P() part2_minPitch = .01;
	@O @P() part2_maxPitch = 1.5;

	@O @P() backgroundMusic_enabled = false;
	@O @P() backgroundMusic_volume = .05;
	@O @P() backgroundMusic_tracks = [];

	@O @P() phrase = "$name says";
	@O @P() names = [
		// well-known people
		"Queen Elizabeth", "Bahrock Obama", "Donald Trump", "Hillary Clinton", "Vladimir Putinn", "The pope",
		// dream-views members
		"Sensei", "Sageous", "Gab", "Hukif",
		// movie characters
		"Mickey Mouse", "Peter Pan", "Kobayashi", "Gon",
		// other things
		"A purple submarine", "A yellow frog", "A blue tomato",
	];

	backgroundMusicRestartTimer: Timer;
	promptTimer: Timer;
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
		this.promptTimer = new Timer(this.interval, ()=> {
			var name = this.names[GetRandomNumber({min: 0, max: this.names.length - 1, mustBeInteger: true})];
			var namePitch = GetRandomNumber({min: .01, max: 2});
			var phraseText_final = this.phrase.replace("$name", name);
			Speak({text: phraseText_final, pitch: namePitch});
			
			WaitXThenRun(3000, function() {
				var odd = GetRandomNumber({min: 0, max: 1}) >= .5;

				var text = "";
				for (var i = 0; i < 4; i++) {
					var base = GetRandomNumber({min: 0, max: 4, mustBeInteger: true}) * 2;
					text += " " + (odd ? 1 + base : base);
				}

				var numberPitch = GetRandomNumber({min: .01, max: 1.5});
				Speak({text: text, pitch: numberPitch});

				Log(`Name: ${name} (${namePitch}) Number: ${text} (${numberPitch})`);
			})
		}).Start();
		if (g.RVP_PostStart) g.RVP_PostStart();
	}
	Stop() {
		if (this.promptTimer == null) return;

		if (this.backgroundMusicRestartTimer) {
			this.backgroundMusicRestartTimer.Stop();
			this.backgroundMusicRestartTimer = null;

			// stop and clear (background-music) audio-files
			for (let audioFile of audioFiles.Props.Select(a=>a.value))
				audioFile.Stop();
			audioFiles = [];
		}
		
		this.promptTimer.Stop();
		this.promptTimer = null;
	}
}
g.Extend({RVP});

@observer
export class RVPUI extends Component<any, any> {
	render() {
		var node = LL.tools.rvp;
		return (
			<ScrollView style={{flex: 1, flexDirection: "column"}}>
				<Row style={{flex: 1, flexDirection: "column", padding: 10}}>
					<Row height={30}>
						<VText mt2 mr10>Enabled: </VText>
						<VSwitch_Auto path={()=>node.p.enabled}/>
					</Row>
					<Row>
						<VText mt5 mr10>Voice-prompt interval: </VText>
						<NumberPicker_Auto path={()=>node.p.interval} min={1} max={10 * 60}
							dialogTitle="Voice-prompt interval"
							dialogMessage="The number of seconds between voice-prompts."/>
						<VText mt5 ml10>seconds</VText>
					</Row>

					{/*<Row>
						<VText mt5 mr10>Part 1 (phrase) volume, min: </VText>
						<NumberPicker_Auto path={()=>node.p.part1_minVolume}
							max={1} step={.01} format={val=>(val * 100).toFixed(0) + "%"}
							dialogTitle="Part 1 (phrase) min-volume"/>
						<VText mt5 ml10 mr10>max: </VText>
						<NumberPicker_Auto path={()=>node.p.part1_maxVolume}
							max={1} step={.01} format={val=>(val * 100).toFixed(0) + "%"}
							dialogTitle="Part 1 (phrase) max-volume"/>
					</Row>*/}
					<Row>
						<VText mt5 mr10>Part 1 (phrase) pitch, min: </VText>
						<NumberPicker_Auto path={()=>node.p.part1_minPitch}
							min={.01} max={2} step={.01} format={val=>(val * 100).toFixed(0) + "%"}
							dialogTitle="Part 1 (phrase) min-pitch"/>
						<VText mt5 ml10 mr10>max: </VText>
						<NumberPicker_Auto path={()=>node.p.part1_maxPitch}
							min={.01} max={2} step={.01} format={val=>(val * 100).toFixed(0) + "%"}
							dialogTitle="Part 1 (phrase) max-pitch"/>
					</Row>

					<Row>
						<VText mt5 mr10>Pause duration: </VText>
						<NumberPicker_Auto path={()=>node.p.pauseDuration} max={60}
							dialogTitle="Pause duration (between part 1 and 2)"/>
						<VText mt5 ml10>seconds</VText>
					</Row>

					{/*<Row>
						<VText mt5 mr10>Part 2 (number) volume, min: </VText>
						<NumberPicker_Auto path={()=>node.p.part2_minVolume}
							max={1} step={.01} format={val=>(val * 100).toFixed(0) + "%"}
							dialogTitle="Part 2 (number) min-volume"/>
						<VText mt5 ml10 mr10>max: </VText>
						<NumberPicker_Auto path={()=>node.p.part2_maxVolume}
							max={1} step={.01} format={val=>(val * 100).toFixed(0) + "%"}
							dialogTitle="Part 2 (number) max-volume"/>
					</Row>*/}
					<Row>
						<VText mt5 mr10>Part 2 (number) pitch, min: </VText>
						<NumberPicker_Auto path={()=>node.p.part2_minPitch}
							min={.01} max={2} step={.01} format={val=>(val * 100).toFixed(0) + "%"}
							dialogTitle="Part 2 (number) min-pitch"/>
						<VText mt5 ml10 mr10>max: </VText>
						<NumberPicker_Auto path={()=>node.p.part2_maxPitch}
							min={.01} max={2} step={.01} format={val=>(val * 100).toFixed(0) + "%"}
							dialogTitle="Part 2 (number) max-pitch"/>
					</Row>

					<Row mt30 height={30}>
						<VText mt1 mr10>Background music: </VText>
						<VSwitch_Auto path={()=>node.p.backgroundMusic_enabled}/>
					</Row>
					{node.backgroundMusic_enabled &&
						<Row style={{backgroundColor: colors.background_dark, flexDirection: "column", padding: 5}}>
							<Row>
								<VText mt5 mr10>Volume: </VText>
								<NumberPicker_Auto path={()=>node.p.backgroundMusic_volume}
									max={1} step={.01} format={val=>(val * 100).toFixed(0) + "%"}
									dialogTitle="Volume (background music)"/>
							</Row>
							<Row>
								<Column style={{flex: 1}}>
									<VText mt5 mr10>Tracks:</VText>
									{node.backgroundMusic_tracks.map((name, index)=> {
										return (
											<Row key={index} height={35}>
												<VTextInput style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
													editable={true} value={name} onChange={text=>node.backgroundMusic_tracks[index] = text}/>
												<VButton text="X" style={{alignItems: "flex-end", marginLeft: 5, width: 28, height: 28}}
													textStyle={{marginBottom: 3}} onPress={()=>node.backgroundMusic_tracks.RemoveAt(index)}/>
											</Row>
										);
									})}
									<Row height={45}>
										<VButton text="Add" style={{width: 100, height: 40}}
											onPress={()=>node.backgroundMusic_tracks.push("")}/>
									</Row>
									<Panel style={{flex: 111222}}/>
								</Column>
							</Row>
						</Row>}

					<Row mt30>
						<VText mt5 mr10>Phrase: </VText>
						<VTextInput_Auto style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
							editable={true} path={()=>node.p.phrase}/>
					</Row>
					<Row>
						<Column style={{flex: 1, backgroundColor: colors.background}}>
							<VText mt5 mr10>Names:</VText>
							{node.names.map((name, index)=> {
								return (
									<Row key={index} height={35}>
										<VTextInput style={{flex: 1, paddingTop: 0, paddingBottom: 0, height: 35}}
											editable={true} value={name} onChange={text=>node.names[index] = text}/>
										<VButton text="X" style={{alignItems: "flex-end", marginLeft: 5, width: 28, height: 28}}
											textStyle={{marginBottom: 3}} onPress={()=>node.names.RemoveAt(index)}/>
									</Row>
								);
							})}
							<Row height={45}>
								<VButton onPress={()=>node.names.push("")} text="Add" style={{width: 100, height: 40}}/>
							</Row>
							<Panel style={{flex: 111222}}/>
						</Column>
					</Row>
				</Row>
			</ScrollView>
		);
	}
}
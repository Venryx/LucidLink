import {_Enum, Enum} from "../../../Frame/General/Enums";
import {Speak, WaitXThenDo} from "../../Scripts/ScriptGlobals";
import {Log, Global, JavaBridge, E, Range} from "../../../Frame/Globals";
import {AudioFileManager} from "../../../Frame/AudioFile";
import V from "../../../Packages/V/V";
import {Row, VButton} from "../../../Frame/ReactGlobals";
import {VTextInput_Auto} from "../../../Packages/ReactNativeComponents/VTextInput";
import Node from "../../../Packages/VTree/Node";
import {NumberPicker_Auto} from "../../../Packages/ReactNativeComponents/NumberPicker";
import {P} from "../../../Packages/VDF/VDFTypeInfo";
import VText from "../../../Frame/Components/VText";
import {View} from "react-native";
import {VSwitch, VSwitch_Auto} from "../../../Packages/ReactNativeComponents/VSwitch";
import {Select_Auto} from "../../../Packages/ReactNativeComponents/Select";

/*var audioFileManager = new AudioFileManager();
var GetAudioFile = V.Bind(audioFileManager.GetAudioFile, audioFileManager);*/

export abstract class Action {
	abstract Run(..._);
	abstract CreateUI(): JSX.Element;
}

@Global
export class Wait extends Node implements Action {
	@O @P() waitTime = 1;

	Run() {}
	CreateUI() {
		return (
			<Row style={{flex: 1}}>
				<VText mt={5} mr={10}>Wait for:</VText>
				<NumberPicker_Auto path={()=>this.p.waitTime} values={Range(0, 1, .1).concat(Range(1, 10, .5)).concat(Range(11, 500))} format={a=>a.toFixed(1) + " minutes"}/>
			</Row>
		);
    }
}

@Global
export class SpeakText extends Node implements Action {
	constructor(options?: Partial<SpeakText>) {
		super();
		this.Extend(options);
	}
	@O @P() text = "Text to speak";
	@O @P() pitch = 1;
	@O @P() volume = -.01;

	Run() {
		Speak({text: this.text, pitch: this.pitch, volume: this.volume});
		Log(this.text);
	}
	CreateUI() {
		return (
			<Row style={{flex: 1}}>
				<VText mt={5} mr={10}>Speak text:</VText>
				<VTextInput_Auto style={{flex: 1, height: 35}} editable={true} path={()=>this.p.text}/>
				<VText mt={5} mr={10}>Volume:</VText>
				<NumberPicker_Auto path={()=>this.p.volume} min={-.01} max={2} step={.01} format={a=>a == -.01 ? "(no change)" : (a * 100).toFixed() + "%"} style={{width: 100}}/>
				<VText mt={5} ml={10} mr={10}>Pitch:</VText>
				<NumberPicker_Auto path={()=>this.p.pitch} max={2} step={.01} format={a=>(a * 100).toFixed() + "%"} style={{width: 100}}/>
			</Row>
		);
    }
}

@Global
export class PlayAudioFile extends Node implements Action {
	@O @P() audioFileName = "waterfall";
	@O @P() volume = 1;

	Run(audioFileManager: AudioFileManager) {
		audioFileManager.GetAudioFile(this.audioFileName).SetVolume(this.volume).Play();
	}
	CreateUI() {
		return (
			<Row style={{flex: 1}}>
				<VText mt={5} mr={10}>Play audio file:</VText>
				<VTextInput_Auto style={{flex: 1, height: 35}} editable={true} path={()=>this.p.audioFileName}/>
				<VText mt={5} mr={10}>Volume:</VText>
				<NumberPicker_Auto path={()=>this.p.volume} max={1} step={.01} format={a=>(a * 100).toFixed() + "%"} style={{width: 100}}/>
			</Row>
		);
    }
}

/*export enum ChangeVolume_Type {
	Both,
	Normal,
	Bluetooth,
}*/
@_Enum export class ChangeVolume_Type extends Enum { static V: ChangeVolume_Type;
	Both = this
	Normal = this
	Bluetooth = this
}

@Global
export class ChangeVolume extends Node implements Action {
	@O @P() increase = true;
	@O @P() type = ChangeVolume_Type.V.Both;
	@O @P() amount = 1;

	Run(audioFileManager: AudioFileManager) {
		let amount_normal = this.type == ChangeVolume_Type.V.Both || this.type == ChangeVolume_Type.V.Normal ? this.amount : -1000;
		let amount_bluetooth = this.type == ChangeVolume_Type.V.Both || this.type == ChangeVolume_Type.V.Bluetooth ? this.amount : -1000;
		JavaBridge.Main[`${this.increase ? "Increase" : "Set"}Volumes`](amount_normal, amount_bluetooth);
	}
	CreateUI() {
		return (
			<Row style={{flex: 1}}>
				<VText mt={5} mr={10}>Change volume. Increase (rather than set):</VText>
				<VSwitch_Auto mt={3} path={()=>this.p.increase}/>
				<VText mt={5} ml={10} mr={10}>Type:</VText>
				<Select_Auto path={()=>this.p.type} options={ChangeVolume_Type} dialogTitle="Change volume - type"/>
				<VText mt={5} ml={10} mr={10}>Amount:</VText>
				<NumberPicker_Auto path={()=>this.p.amount} max={1} step={.01} format={a=>(a * 100).toFixed() + "%"} style={{width: 100}}/>
			</Row>
		);
    }
}

@Global
export class RepeatSteps extends Node implements Action {
	@O @P() firstStep = 1;
	@O @P() lastStep = 1;
	@O @P() repeatCount = 1;

	Run() {}
	CreateUI() {
		return (
			<Row style={{flex: 1}}>
				<VText mt={5} mr={10}>Repeat steps. First step:</VText>
				<NumberPicker_Auto path={()=>this.p.firstStep} min={1} max={100} style={{width: 100}}/>
				<VText mt={5} ml={10} mr={10}>Last step:</VText>
				<NumberPicker_Auto path={()=>this.p.lastStep} min={1} max={100} style={{width: 100}}/>
				<VText mt={5} ml={10} mr={10}>Repeat count:</VText>
				<NumberPicker_Auto path={()=>this.p.repeatCount} min={1} max={1000} style={{width: 100}}/>
			</Row>
		);
    }
}

/*export class RunJSFunction extends Action {
	funcName = "SomeJSFunction";
	Run() {
    }
}*/
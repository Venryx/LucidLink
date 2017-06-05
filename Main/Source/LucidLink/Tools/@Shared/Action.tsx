import {_Enum} from "../../../Frame/General/Enums";
import {Speak, WaitXThenDo} from "../../Scripts/ScriptGlobals";
import {Log, Global, JavaBridge, E} from "../../../Frame/Globals";
import {AudioFileManager} from "../../../Frame/AudioFile";
import V from "../../../Packages/V/V";
import {Row, VButton} from "../../../Frame/ReactGlobals";
import {VTextInput_Auto} from "../../../Packages/ReactNativeComponents/VTextInput";
import Node from "../../../Packages/VTree/Node";
import {NumberPicker_Auto} from "../../../Packages/ReactNativeComponents/NumberPicker";
import {P} from "../../../Packages/VDF/VDFTypeInfo";
import VText from "../../../Frame/Components/VText";

/*var audioFileManager = new AudioFileManager();
var GetAudioFile = V.Bind(audioFileManager.GetAudioFile, audioFileManager);*/

export abstract class Action {
	abstract Run(..._);
	abstract CreateUI(index, onClickRemove: ()=>void): JSX.Element;
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
	CreateUI(index, onClickRemove: ()=>void) {
		let inline = onClickRemove == null;
		return (
			<Row key={index} style={E(inline && {flex: 1})}>
				<VText mt={5} mr={10}>Speak text:</VText>
				<VTextInput_Auto style={{flex: 1, height: 35}} editable={true} path={()=>this.p.text}/>
				<VText mt={5} mr={10}>Volume:</VText>
				<NumberPicker_Auto path={()=>this.p.volume} min={-.01} max={2} step={.01} format={a=>a == -.01 ? "(no change)" : (a * 100).toFixed() + "%"} style={{width: 100}}/>
				<VText mt={5} ml={10} mr={10}>Pitch:</VText>
				<NumberPicker_Auto path={()=>this.p.pitch} max={2} step={.01} format={a=>(a * 100).toFixed() + "%"} style={{width: 100}}/>
				{onClickRemove != null &&
					<VButton text="X" style={{alignItems: "flex-end", marginLeft: 5, width: 28, height: 28}} textStyle={{marginBottom: 3}}
						onPress={onClickRemove}/>}
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
	CreateUI(index, onClickRemove: ()=>void) {
        return (
			<Row key={index}>
				<VText mt={5} mr={10}>Play audio file:</VText>
				<VTextInput_Auto style={{flex: 1, height: 35}} editable={true} path={()=>this.p.audioFileName}/>
				<VText mt={5} mr={10}>Volume:</VText>
				<NumberPicker_Auto path={()=>this.p.volume} max={1} step={.01} format={a=>(a * 100).toFixed() + "%"} style={{width: 100}}/>
				<VButton text="X" style={{alignItems: "flex-end", marginLeft: 5, width: 28, height: 28}} textStyle={{marginBottom: 3}}
					onPress={onClickRemove}/>
			</Row>
		);
    }
}
/*export class RunJSFunction extends Action {
	funcName = "SomeJSFunction";
	Run() {
    }
}*/
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
import { GraphOverlayUI } from "../Tools/SPMonitor";

@observer
export default class FBAUI extends Component<{}, {}> {
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
						<VText mt={5} mr={10}>Start volume. Normal:</VText>
						<NumberPicker_Auto path={()=>node.p.normalVolume} max={1} step={.01} format={a=>(a * 100).toFixed() + "%"}/>
						<VText mt={5} ml={10} mr={10}>Bluetooth:</VText>
						<NumberPicker_Auto path={()=>node.p.bluetoothVolume} max={1} step={.01} format={a=>(a * 100).toFixed() + "%"}/>
					</Row>

					<REMStartSequenceUI/>
					<CommandListenerUI/>
					<StatusReporterUI/>

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

@observer
export class REMStartSequenceUI extends BaseComponent<{}, {}> {
	render() {
		var node = LL.tools.fba;
		return (
			<Column>
				<Row>
					<VText mt={5} mr={10} style={{fontSize: 20, fontWeight: "bold"}}>REM start sequence</VText>
				</Row>
				<Row>
					<VText mt={5} mr={10}>Sequence delay from REM onset:</VText>
					<NumberPicker_Auto path={()=>node.p.promptStartDelay} min={0} max={100} format={a=>a + " minutes"}/>
				</Row>
				<Row>
					<VText mt={5} mr={10}>Sequence repeat interval:</VText>
					<NumberPicker_Auto path={()=>node.p.promptInterval} min={1} max={100} format={a=>a + " minutes"}/>
				</Row>
				<Row>
					<VText mt={5} mr={10}>Sequence actions:</VText>
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
			</Column>
		);
	}
}

@observer
export class CommandListenerUI extends BaseComponent<{}, {}> {
	render() {
		var node = LL.tools.fba.commandListener;
		return (
			<Column>
				<Row>
					<VText mt={5} mr={10} style={{fontSize: 20, fontWeight: "bold"}}>Command listener</VText>
				</Row>
				<Row>
					<VText mt={5}>When breathing-depth drops below </VText>
					<NumberPicker_Auto path={()=>node.p.sequenceDisabler_breathDepthCutoff} min={0} max={50} format={a=>a + "%"}/>
					<VText mt={5}> , reset and disable the rem-start sequence for </VText>
					<NumberPicker_Auto path={()=>node.p.sequenceDisabler_disableLength} min={0} max={100} format={a=>a + " minutes"}/>
					<VText mt={5}>.</VText>
				</Row>
			</Column>
		);
	}
}

@observer
export class StatusReporterUI extends BaseComponent<{}, {}> {
	render() {
		var node = LL.tools.fba.statusReporter;
		return (
			<Column>
				<Row>
					<VText mt={5} mr={10} style={{fontSize: 20, fontWeight: "bold"}}>Status reporter</VText>
				</Row>
				<Row>
					<VText mt={5} mr={10}>Report interval:</VText>
					<NumberPicker_Auto path={()=>node.p.reportInterval} min={0} max={1000} format={a=>a + " minutes"}/>
				</Row>
				<Row>
					<VText mt={5} mr={10}>Report text: (variables: @breathValue, @breathRate, @breathDepth, @temp, @light, @sleepStage, @sleepStageTime, @remSequenceEnabled)</VText>
				</Row>
				<Row>
					<VTextInput_Auto path={()=>node.p.reportText}/>
				</Row>
				<VText mr={10}>Current S+ data:</VText>
				<Row ml={10}>
					<GraphOverlayUI/>
				</Row>
			</Column>
		);
	}
}
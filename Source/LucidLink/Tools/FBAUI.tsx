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
import {Log, Global, JavaBridge, Toast, Notify, Range} from "../../Frame/Globals";
import Sound from "react-native-sound";
import {AudioFile, AudioFileManager} from "../../Frame/AudioFile";
import {Sequence, Timer, TimerContext, WaitXThenRun} from "../../Frame/General/Timers";
import {VTextInput, VTextInput_Auto} from "../../Packages/ReactNativeComponents/VTextInput";
import BackgroundMusicConfigUI from "./@Shared/BackgroundMusicSelectorUI";
import SPBridge from "../../Frame/SPBridge";
import {SleepStage} from "../../Frame/SPBridge";
import Moment from "moment";
import V from "../../Packages/V/V";
import {Action, SpeakText, PlayAudioFile, Wait, RepeatSteps, ChangeVolume} from "./@Shared/Action";
import VText from "../../Frame/Components/VText";
import GraphUI from "./SPMonitor/SPGraphUI";
import FBARun from "./FBA/FBARun";

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
						<VText mt={5} mr={10}>Overall volume start-values. Normal:</VText>
						<NumberPicker_Auto path={()=>node.p.normalVolume} max={1} step={.01} format={a=>(a * 100).toFixed() + "%"}/>
						<VText mt={5} ml={10} mr={10}>Bluetooth:</VText>
						<NumberPicker_Auto path={()=>node.p.bluetoothVolume} max={1} step={.01} format={a=>(a * 100).toFixed() + "%"}/>
					</Row>
					<Row>
						<VText>Note: The FBA engine requires an S+ sleep-monitor device (by ResMed) to function. (connects through bluetooth)</VText>
					</Row>

					<REMStartSequenceUI/>
					<CommandListenerUI/>
					<StatusReporterUI/>
					<CurrentStatusUI/>

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
			<Column mt={30}>
				<Row>
					<VText mt={5} mr={10} style={{fontSize: 20, fontWeight: "bold"}}>REM start sequence</VText>
				</Row>
				<Row>
					<VText mt={5} mr={10}>Sequence delay from REM onset:</VText>
					<NumberPicker_Auto path={()=>node.p.promptStartDelay} min={0} max={100} format={a=>a + " minutes"}/>
					<VText mt={5} ml={5}>+ 20s</VText>
					<View style={{flex: 1}}/>
					<VText mt={2} mr={10}>Test run: </VText>
					<VSwitch_Auto path={()=>node.p.testRun_enabled} onChange={val=> {
						if (val) {
							node.testRun = new FBARun();
							node.testRun.StartREMSequence();
						} else {
							node.testRun.StopREMSequence();
							delete node.testRun;
						}
					}}/>
				</Row>
				{/*<Row>
					<VText mt={5} mr={10}>Sequence repeat interval:</VText>
					<NumberPicker_Auto path={()=>node.p.promptInterval} min={1} max={100} format={a=>a + " minutes"}/>
				</Row>*/}
				<Row>
					<VText mr={10}>Sequence steps:</VText>
				</Row>
				<Row style={{backgroundColor: colors.background_dark, flexDirection: "column", padding: 5}}>
					<Column style={{flex: 1, backgroundColor: colors.background, padding: 10}}>
						{node.promptActions.map((action, index)=> {
							return (
								<Row key={index}>
									<VText mt={5}>{index + 1}) </VText>
									{action.CreateUI()}
									<VButton text="▲" ml={5} style={{height: 28}} textStyle={{marginBottom: 3}} onPress={()=> {
										node.promptActions.RemoveAt(index);
										node.promptActions.Insert(index - 1, action);
									}}/>
									<VButton text="▼" ml={5} style={{height: 28}} textStyle={{marginBottom: 3}} onPress={()=> {
										node.promptActions.RemoveAt(index);
										node.promptActions.Insert(index + 1, action);
									}}/>
									<VButton text="X" ml={5} style={{width: 28, height: 28}} textStyle={{marginBottom: 3}} onPress={()=>node.promptActions.Remove(action)}/>
								</Row>
							);
						})}
					</Column>
					<Row mt={10} height={45}>
						<VText mt={9}>Add step: </VText>
						<VButton text="Wait" plr={10} style={{height: 40}}
							onPress={()=>node.promptActions.push(new Wait())}/>
						<VButton text="Speak text" ml={5} plr={10} style={{height: 40}}
							onPress={()=>node.promptActions.push(new SpeakText())}/>
						<VButton text="Play audio file" ml={5} plr={10} style={{height: 40}}
							onPress={()=>node.promptActions.push(new PlayAudioFile())}/>
						<VButton text="Change volume" ml={5} plr={10} style={{height: 40}}
							onPress={()=>node.promptActions.push(new ChangeVolume())}/>
						<VButton text="Repeat steps" ml={5} plr={10} style={{height: 40}}
							onPress={()=>node.promptActions.push(new RepeatSteps())}/>
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
			<Column mt={30}>
				<Row>
					<VText mt={5} mr={10} style={{fontSize: 20, fontWeight: "bold"}}>Command listener</VText>
				</Row>
				<Row>
					<VText mt={5}>When breathing-depth of last 15s changes by </VText>
					<NumberPicker_Auto path={()=>node.p.sequenceDisabler_minPercentDiff} max={10} step={.01} format={a=>(a * 100).toFixed() + "%"}/>
					<VText mt={5}> from that of previous 15s (or escape key is pressed):</VText>
				</Row>
				<Row>
					<VText mt={5}>1) Reset and disable the rem-start sequence for </VText>
					<NumberPicker_Auto path={()=>node.p.sequenceDisabler_disableLength} max={100} format={a=>a + " minutes"}/>
				</Row>
				<Row>
					<VText mt={5}>2) </VText>
					{node.sequenceDisabler_messageSpeakAction.CreateUI()}
				</Row>
				<Row>
					<VText mt={5}>Min interval between "speak text" calls: </VText>
					<NumberPicker_Auto path={()=>node.p.sequenceDisabler_promptMinInterval} values={Range(0, 1, .1).concat(Range(1, 10, .5)).concat(Range(11, 500))} format={a=>a.toFixed(1) + " minutes"}/>
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
			<Column mt={30}>
				<Row>
					<VText mt={5} mr={10} style={{fontSize: 20, fontWeight: "bold"}}>Status reporter</VText>
				</Row>
				<Row>
					<VText mt={5} mr={10}>Report interval:</VText>
					<NumberPicker_Auto path={()=>node.p.reportInterval} values={Range(0, 1, .1).concat(Range(1, 10, .5)).concat(Range(11, 1000))} format={a=>a.toFixed(1) + " minutes"}/>
				</Row>
				<Row>
					<VButton text="Show list of variables" onPress={()=> {
						/*Notify(`
@temp, @light, ${""
}@breathVal, @breathVal_min, @breathVal_max, @breathVal_avg, ${""
}@breathingDepth_prev, @breathingDepth_last, @breathingRate, ${""
}@sleepStage, @sleepStageTime, @remSequenceEnabled
`.trim());*/
						Notify(`
@temp, @light
@breathVal, @breathVal_min, @breathVal_max, @breathVal_avg
@breathingDepth_prev, @breathingDepth_last, @breathingRate
@sleepStage`.trim()
//@sleepStageTime, @remSequenceEnabled
						);
					}}/>
				</Row>
				<Row>
					<VText mt={10} mr={10}>Report text: </VText>
					<VTextInput_Auto path={()=>node.p.reportText}/>
					<VText mt={5} mr={10}>Volume:</VText>
					<NumberPicker_Auto path={()=>node.p.volume} min={-.01} max={2} step={.01} format={a=>a == -.01 ? "(no change)" : (a * 100).toFixed() + "%"} style={{width: 100}}/>
					<VText mt={5} ml={10} mr={10}>Pitch:</VText>
					<NumberPicker_Auto path={()=>node.p.pitch} max={2} step={.01} format={a=>(a * 100).toFixed() + "%"} style={{width: 100}}/>
				</Row>
			</Column>
		);
	}
}

@observer
export class CurrentStatusUI extends BaseComponent<{}, {}> {
	render() {
		return (
			<Column mt={30}>
				<Row>
					<VText mt={5} mr={10} style={{fontSize: 20, fontWeight: "bold"}}>Current status</VText>
				</Row>
				<Row>
					<GraphUI/>
				</Row>
			</Column>
		);
	}
}
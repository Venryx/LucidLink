import {Timer} from "./General/Timers";
import {JavaBridge, Toast, Log} from "./Globals";
import {Assert} from "./General/Assert";
import {NativeModules, DeviceEventEmitter} from "react-native";
import {Enum, _Enum} from "./General/Enums";

@_Enum export class SleepStage extends Enum { static V: SleepStage;
	Absent = this
    Wake = this
    Light = this
    Deep = this
    Rem = this

	static FromJavaStageValue(stageValue: number) {
		let stageValueToStageMap = {
			1: SleepStage.V.Wake,
			2: SleepStage.V.Absent, 3: SleepStage.V.Absent, 4: SleepStage.V.Absent,
			5: SleepStage.V.Light,
			6: SleepStage.V.Deep,
			7: SleepStage.V.Rem,
		}
		return stageValueToStageMap[stageValue];
	}
	static GetHeightForStage(stage: SleepStage) {
		/*let stageToHeightMap = {
			[SleepStage.V.Absent.name]: 0,
			[SleepStage.V.Wake.name]: 1 / 7,
			[SleepStage.V.Light.name]: 5 / 7,
			[SleepStage.V.Deep.name]: 7 / 7,
			[SleepStage.V.Rem.name]: 3 / 7,
		};*/
		let stageToHeightMap = {
			[SleepStage.V.Absent.name]: 0,
			[SleepStage.V.Wake.name]: 1 / 4,
			[SleepStage.V.Light.name]: 3 / 4,
			[SleepStage.V.Deep.name]: 4 / 4,
			[SleepStage.V.Rem.name]: 2 / 4,
		};
		return stageToHeightMap[stage.name];
	}
	static GetColorForStage(stage: SleepStage) {
		let stageToColorMap = {
			[SleepStage.V.Absent.name]: "rgba(0,0,0,0)",
			[SleepStage.V.Wake.name]: "#d8381e",
			[SleepStage.V.Light.name]: "#41a767",
			[SleepStage.V.Deep.name]: "#009ee2",
			[SleepStage.V.Rem.name]: "#eca100",
		};
		return stageToColorMap[stage.name];
	}
}

// keep values, since must match with Java
/*export enum SleepStage {
    Absent = 2, Unknown = 3, Break = 4,
    Wake = 1,
    LightSleep = 5,
    DeepSleep = 6,
    RemSleep = 7,
}*/

var core = NativeModules.SPlus;
export class SPBridgeClass {
	initialized = false;
	Init() {
		core.Init();

		DeviceEventEmitter.addListener("OnReceiveTemp", (args: any)=> {
			var [tempInCelsius, tempInFarenheit] = args;
			for (let listener of this.listeners_onReceiveTemp)
				listener(tempInCelsius, tempInFarenheit);
		});
		DeviceEventEmitter.addListener("OnReceiveLightValue", (args: any)=> {
			var [lightValue] = args;
			for (let listener of this.listeners_onReceiveLightValue)
				listener(lightValue);
		});
		DeviceEventEmitter.addListener("OnReceiveBreathValue", (args: any)=> {
			var [breathValue] = args;
			for (let listener of this.listeners_onReceiveBreathValue)
				listener(breathValue);
		});
		DeviceEventEmitter.addListener("OnReceiveBreathingRate", (args: any)=> {
			var [breathingRate] = args;
			for (let listener of this.listeners_onReceiveBreathingRate)
				listener(breathingRate);
		});
		DeviceEventEmitter.addListener("OnReceiveSleepStage", (args: any)=> {
			var [stageValue] = args as [number];
			/*var stageEnum_detailed_name =  SleepState_Detailed[stageValue];
			var stageEnum_detailed: SleepState_Detailed = SleepState_Detailed[stageEnum_detailed_name];
			var stageEnum_simple = ConvertDetailedSleepStateToSimpleSleepState(stageEnum_detailed);*/
			var stageEnum = SleepStage.FromJavaStageValue(stageValue);
			for (let listener of this.listeners_onReceiveSleepStage)
				listener(stageEnum);
		});

		this.initialized = true;
		Log("spbridge", `SPBridge initialized.`);
	}

	// raw data
	listeners_onReceiveTemp: ((tempInCelsius: number, tempInFarenheit: number)=>void)[] = [];
	listeners_onReceiveLightValue: ((lightValue: number)=>void)[] = [];
	listeners_onReceiveBreathValue: ((breathValue: number)=>void)[] = [];
	// calculated data
	listeners_onReceiveBreathingRate: ((breathingRate: number)=>void)[] = [];
	listeners_onReceiveSleepStage: ((sleepStage: SleepStage)=>void)[] = [];

	status: "disconnected" | "connecting" | "connected" = "disconnected";
	/*timer: Timer;
	OnChangeStatus(status) {
		this.status = status;

		if (status == "connected") {
			Assert(this.timer == null, "Timer should be null when status has just changed to connected.");
			this.timer = new Timer(1, ()=> {
				var stage = JavaBridge.Main.GetSleepStage() as number;
				Toast(`Stage: ${stage}`);
			});
		} else {
			this.timer.Stop();
			this.timer = null;
		}
	}*/
	
	Connect() {
		core.Connect();
	}
	Disconnect() {
		core.Disconnect();
	}

	SetUserInfo(age, gender: "male" | "female") {
		core.SetUserInfo(age, gender);
	}

	/*async GetSleepStage() {
		return await core.GetSleepStage() as number;
	}*/
	StartRealTimeSession() {
		core.StartRealTimeSession();
	}
	StartSleepSession() {
		core.StartSleepSession();
	}
	StopSession() {
		core.StopSession();
	}
}

var SPBridge = new SPBridgeClass();
export default SPBridge;
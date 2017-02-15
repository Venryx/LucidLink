import {Timer} from "./General/Timers";
import {JavaBridge, Toast, Log} from "./Globals";
import {Assert} from "./General/Assert";
import {NativeModules, DeviceEventEmitter} from "react-native";

// keep values, since must match with Java
/*enum SleepStage_Detailed {
    Wake = 1, // -> Simple.Wake
    Absent = 2, Unknown = 3, Break = 4, // -> Simple.Other
    LightSleep = 5, // -> Simple.Light
    DeepSleep = 6, // -> Simple.Deep
    RemSleep = 7, // -> Simple.REM
}
// keep values, since must match with Java
enum SleepStage_Simple {
    Other = 0,
    Wake = -1,
    REM = 1,
    Light = 2,
    Deep = 3,
}
function ConvertDetailedSleepStateToSimpleSleepState(detailedState: SleepState_Detailed) {
	if ([SleepState_Detailed.Absent, SleepState_Detailed.Unknown, SleepState_Detailed.Break].Contains(detailedState))
		return SleepState_Simple.Other;
	if (detailedState == SleepState_Detailed.Wake)
		return SleepState_Simple.Wake;
	if (detailedState == SleepState_Detailed.RemSleep)
		return SleepState_Simple.REM;
	if (detailedState == SleepState_Detailed.LightSleep)
		return SleepState_Simple.Light;
	//if (detailedState == SleepState_Detailed.DeepSleep)
	return SleepState_Simple.Deep;
}*/

// keep values, since must match with Java
export enum SleepStage {
    Wake = 1,
    Absent = 2, Unknown = 3, Break = 4,
    LightSleep = 5,
    DeepSleep = 6,
    RemSleep = 7,
}

var core = NativeModules.SPlus;
export default class SPBridge {
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
			var [stage] = args as [SleepStage];
			/*var stageEnum_detailed_name =  SleepState_Detailed[stageValue];
			var stageEnum_detailed: SleepState_Detailed = SleepState_Detailed[stageEnum_detailed_name];
			var stageEnum_simple = ConvertDetailedSleepStateToSimpleSleepState(stageEnum_detailed);*/
			var stageEnum_name =  SleepStage[stage];
			for (let listener of this.listeners_onReceiveSleepStage)
				listener(stage);
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
	
	Connect(age, gender: "male" | "female") {
		core.Connect(age, gender == "male" ? 0 : 1);
	}
	Disconnect() {
		core.Disconnect();
	}

	StartSession() {
		core.StartSession();
	}
	StopSession() {
		core.StopSession();
	}

	/*async GetSleepStage() {
		return await core.GetSleepStage() as number;
	}*/
	StartRealTimeStream() {
		core.StartRealTimeStream();
	}
	StartSleep() {
		core.StartSleep();
	}
}
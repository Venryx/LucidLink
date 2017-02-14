import {Timer} from "./General/Timers";
import {JavaBridge, Toast, Log} from "./Globals";
import {Assert} from "./General/Assert";
import {NativeModules, DeviceEventEmitter} from "react-native";

var core = NativeModules.SPlus;
export default class SPBridge {
	initialized = false;
	Init() {
		core.Init();

		DeviceEventEmitter.addListener("OnReceiveTempValue", (args: any)=> {
			var [tempInCelsuis, tempInFarenheit] = args;
			for (let listener of this.listeners_onReceiveTempValue)
				listener(tempInCelsuis, tempInFarenheit);
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

		this.initialized = true;
		Log("spbridge", `SPBridge initialized.`);
	}

	listeners_onReceiveTempValue = [];
	listeners_onReceiveLightValue = [];
	listeners_onReceiveBreathValue = [];

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

	async GetSleepStage() {
		return await core.GetSleepStage();
	}
	StartRealTimeStream() {
		core.StartRealTimeStream();
	}
	StartSleep() {
		core.StartSleep();
	}
}
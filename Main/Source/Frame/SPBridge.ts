import {Timer} from "./General/Timers";
import {JavaBridge, Toast, Log} from "./Globals";
import {Assert} from "./General/Assert";
import {NativeModules} from "react-native";

var core = NativeModules.SPlus;
export default class SPBridge {
	initialized = false;
	Init() {
		core.Init();
		this.initialized = true;
		Log("spbridge", `SPBridge initialized.`);
	}

	status: "disconnected" | "connecting" | "connected" = "disconnected";
	timer: Timer;
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
	}

	timer2: Timer; // temp
	Connect(age, gender: "male" | "female") {
		core.Connect(age, gender == "male" ? 0 : 1);

		// temp
		Toast("ConnectingJS...");
		this.timer2 = new Timer(10, async ()=> {
			var stage = await this.GetSleepStage();
			Toast(stage);
			if (stage != 3)
				alert("Got other result!" + stage);
		}).Start();
	}
	Disconnect() {
		this.timer2.Stop(); // temp
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
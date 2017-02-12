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

	Connect() {
		core.Connect();
	}
	Disconnect() {
		core.Disconnect();
	}
}
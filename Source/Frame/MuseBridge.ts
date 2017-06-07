import {JavaBridge, Log, ToJSON} from './Globals';
import LibMuse from "react-native-libmuse";
import Bind from "autobind-decorator";
import {LL} from "../LucidLink";
import {ProfileMethod} from "./VProfiler";

//declare var LibMuse;

@Bind export default class MuseBridge {
	static initialized = false;
	static Init() {
		LibMuse.AddListener_OnChangeMuseList(MuseBridge.OnChangeMuseList);
		LibMuse.AddListener_OnChangeMuseConnectStatus(MuseBridge.OnChangeMuseConnectStatus);
		LibMuse.AddListener_OnReceiveMuseDataPacket(MuseBridge.OnReceiveMuseDataPacket);
		LibMuse.Init();
		MuseBridge.initialized = true;
		Log("muse link", `LibMuse initialized.`);
	}
	async IfInEmulator_ReplaceLibMuseFuncsWithStubs() {
		var inEmulator = await JavaBridge.Main.IsInEmulator();
		if (inEmulator) {
			for (var key in LibMuse) {
				var value = LibMuse[key];
				if (value instanceof Function)
					LibMuse[key] = ()=>{};
			}
		}
	}

	static museList = [];
	static StopSearch() {
		LibMuse.StopSearch();
	}
	static StartSearch() {
		//LibMuse.StartSearch();
		LibMuse.RestartSearch();
	}
	static OnChangeMuseList(museList) {
		MuseBridge.museList = museList;
		Log("muse link", `Muse list changed: ${ToJSON(museList)}`);

		if (museList.length && MuseBridge.currentMuse == null && LL.tools.monitor.connect)
			MuseBridge.Connect();

		if (LL.tools.monitor.ui) LL.tools.monitor.ui.forceUpdate();
	}

	static currentMuse = null;
	static Connect() {
		LibMuse.Connect(0); // always try to connect to first muse
	}
	static Disconnect() {
		LibMuse.Disconnect();
	}
	static status: "disconnected" | "connecting" | "connected" = "disconnected";
	static OnChangeMuseConnectStatus(status) {
		Log("muse link", `Muse connection status changed: ${status}`);
		if (status == "connected") {
			Log("muse link", "LibMuse connected.");
			MuseBridge.currentMuse = MuseBridge.museList[0];
		}
		else if (status == "disconnected") {
			Log("muse link", "LibMuse disconnected.");
			MuseBridge.currentMuse = null;
			// since we're disconnected now, restart listening (assuming "connect" is enabled)
			if (LL.tools.monitor.connect)
				MuseBridge.StartSearch();
		}
		MuseBridge.status = status;
		if (LL.tools.monitor.ui) LL.tools.monitor.ui.forceUpdate();

		for (let listener of LL.scripts.scriptRunner.listeners_whenChangeMuseConnectStatus)
			listener(status);
	}
	
	static count = 0;
	static lastLogStatsTime = -1;
	static OnReceiveMuseDataPacket(packet) {
		//if (++MuseBridge.count > 300) return; // for testing
		if (!LL.tools.monitor.monitor) return; // quick return, for when "stop sending" update to Java is delayed

		//let p = ProfileMethod("OnReceiveMuseDataPacket");

		//Log(`Type: ${type} ChannelValues: ${ToJSON(channelValues)}`);
		for (let listener of LL.scripts.scriptRunner.listeners_whenMusePacketReceived)
			listener(packet);

		var now = new Date().getTime();
		if (now - MuseBridge.lastLogStatsTime > LL.settings.logStatsEveryXMinutes * 60 * 1000) {
			(async ()=> {
				let usedMemory = await JavaBridge.Main.GetAppUsedMemory();
				let totalMemory = await JavaBridge.Main.GetAppTotalMemory();
				let maxMemory = await JavaBridge.Main.GetAppMaxMemory();
				let usedNativeMemory = await JavaBridge.Main.GetAppNativeUsedMemory();
				Log(`App-heap memory-usage (in mb): ${usedMemory}/${totalMemory} (max: ${maxMemory}) (native usage: ${usedNativeMemory})`);
			})();
			MuseBridge.lastLogStatsTime = now;
		}
		
		//p.End();
	}
}
import LibMuse from "react-native-libmuse";

export default class MuseBridge {
	static museList = [];

	static data = [];

	static started = false;
	static Start() {
		LibMuse.Start();
		LibMuse.AddListener_OnReceiveMuseDataPacket((type, data)=> {
			//Log(`Type: ${type} Data: ${ToJSON(data)}`);
			//alert(`Type: ${type} Data: ${ToJSON(data)}`);
			//Log("muse link", `Type: ${type} Data: ${ToJSON(data)}`);			
			Log("muse link", `Type: ${type} Data: ${data}`);			
		});
		LibMuse.AddListener_OnMuseListChange(museList=> {
			MuseBridge.museList = museList;
			MuseBridge.currentMuse = museList[0];
			Log("muse link", `Muse list changed: ${ToJSON(museList)}`);
			if (LL.monitor.ui)
				LL.monitor.ui.forceUpdate();
		});
		MuseBridge.started = true;
		Log("muse link", `LibMuse started.`);
	}
	static Refresh() {
		LibMuse.Refresh();
	}

	static currentMuse = null;

	static Connect() {
		var museIndex = 0;
		LibMuse.Connect(museIndex);
		Log("muse link", "LibMuse connected.");
	}
	static Disconnect() {
		LibMuse.Disconnect();
		Log("muse link", "LibMuse disconnected.");
	}
}
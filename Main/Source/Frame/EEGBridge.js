import LibMuse from "react-native-libmuse";

export default class EEGBridge {
	static data = [];

	static started = false;
	static Start() {
		LibMuse.Start();
		LibMuse.AddListener_OnReceiveMuseDataPacket((type, data)=> {
			//Log(`Type: ${type} Data: ${ToJSON(data)}`);
			alert(`Type: ${type} Data: ${ToJSON(data)}`);
		});
		LibMuse.AddListener_OnMuseListChange(museList=> {
			alert(`Muse list changed: ${ToJSON(museList)}`);
		});
		EEGBridge.started = true;
		Log("LibMuse started.");
	}
	static Connect() {
		var museIndex = 0;
		LibMuse.Connect(museIndex);
		Log("LibMuse connected.");
	}
	static Disconnect() {
		LibMuse.Disconnect();
		Log("LibMuse disconnected.");
	}
}
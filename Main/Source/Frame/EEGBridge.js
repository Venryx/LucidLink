import LibMuse from "react-native-libmuse";

export default class EEGBridge {
	static data = [];

	static Start() {
		LibMuse.Start();
		LibMuse.AddMuseDataListener((type, data)=> {
			Log(`Type: ${type} Data: ${ToJSON(data)}`);
		});
	}
	static Connect() {
		var museIndex = 0;
		LibMuse.Connect(museIndex);		
	}
	static Disconnect() {
		LibMuse.Disconnect();		
	}
}
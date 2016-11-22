//var LibMuse = require("react-native-libmuse");

export default class EEGBridge {
	data = [];

	Start() {
		LibMuse.Start();
		LibMuse.AddMuseDataListener((type, data)=> {
			Log(`Type: ${type} Data: ${ToJSON(data)}`);
		});
	}
	Connect() {
		var museIndex = 0;
		LibMuse.Connect(museIndex);		
	}
	Disconnect() {
		LibMuse.Disconnect();		
	}
}
import {DeviceEventEmitter} from "react-native";

export function AddEventListener(eventName: string, listener: (data)=>void) {
	DeviceEventEmitter.addListener(eventName, listener);
	DeviceEventEmitter.addListener(eventName + "_Buffered", (argumentSets: Array<any>)=> {
		for (let argumentSet of argumentSets) {
			listener(argumentSet);
		}
	});
}
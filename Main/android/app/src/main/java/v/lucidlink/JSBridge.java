package v.lucidlink;

import android.content.Context;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.ArrayList;
import java.util.List;

import static v.lucidlink.LLHolder.LL;

class Event {
	public Event(String name, Object... args) {
		this.name = name;
		this.args = args;
	}
	String name;
	Object[] args;
}

public class JSBridge {
	static List<Event> bufferedEvents = new ArrayList<>();
	public static void SendEvent(String name, Object... args) {
		Event event = new Event(name, args);
		if (LL.mainModule == null || !LL.reactContext.hasActiveCatalystInstance()) {
			bufferedEvents.add(event);
			return;
		}

		if (bufferedEvents.size() > 0) {
			for (Event bufferedEvent : bufferedEvents)
				SendEvent(bufferedEvent);
			bufferedEvents.clear();
		}
		SendEvent(event);
	}
	static void SendEvent(Event event) {
		WritableArray argsList = Arguments.createArray();
		for (Object arg : event.args)
			V.WritableArray_Add(argsList, arg);

		DeviceEventManagerModule.RCTDeviceEventEmitter jsModuleEventEmitter = LL.reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
		jsModuleEventEmitter.emit(event.name, argsList);
	}
}
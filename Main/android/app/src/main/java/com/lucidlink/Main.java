package com.lucidlink;

import android.os.Build;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.File;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main extends ReactContextBaseJavaModule {
	public static Main main;

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

	ReactApplicationContext reactContext;
    public Main(ReactApplicationContext reactContext) {
        super(reactContext);
		main = this;
		this.reactContext = reactContext;
    }

	public Boolean blockUnusedKeys;
	@ReactMethod public void SetBlockUnusedKeys(Boolean blockUnusedKeys) {
		this.blockUnusedKeys = blockUnusedKeys;
	}

	public void SendEvent(String eventName, Object... args) {
		WritableArray argsList = Arguments.createArray();
		for (Object arg : args) {
			if (arg == null)
				argsList.pushNull();
			else if (arg instanceof Boolean)
				argsList.pushBoolean((Boolean)arg);
			else if (arg instanceof Integer)
				argsList.pushInt((Integer)arg);
			else if (arg instanceof Double)
				argsList.pushDouble((Double)arg);
			else if (arg instanceof String)
				argsList.pushString((String)arg);
			else if (arg instanceof WritableArray)
				argsList.pushArray((WritableArray)arg);
			else {
				//Assert(arg instanceof WritableMap, "Event args must be one of: WritableArray, Boolean")
				if (!(arg instanceof WritableMap))
					throw new RuntimeException("Event args must be one of: Boolean, Integer, Double, String, WritableArray, WritableMap");
				argsList.pushMap((WritableMap)arg);
			}
		}

		DeviceEventManagerModule.RCTDeviceEventEmitter jsModuleEventEmitter = reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
		jsModuleEventEmitter.emit(eventName, argsList);
	}

    @Override
    public String getName() {
        return "Main";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    @ReactMethod
    public void ShowToast(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
    }

	@ReactMethod public void IsInEmulator(Promise promise) {
		boolean result = Build.FINGERPRINT.startsWith("generic")
			|| Build.FINGERPRINT.startsWith("unknown")
			|| Build.MODEL.contains("google_sdk")
			|| Build.MODEL.contains("Emulator")
			|| Build.MODEL.contains("Android SDK built for x86")
			|| Build.MANUFACTURER.contains("Genymotion")
			|| (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
			|| "google_sdk".equals(Build.PRODUCT);
		promise.resolve(result);
	}
}
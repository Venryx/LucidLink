package com.lucidlink;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.lucidlink.Frame.Pattern;
import com.lucidlink.Frame.Vector2i;
import com.v.LibMuse.LibMuse;
import com.v.LibMuse.MainModule;

import java.io.File;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends ReactContextBaseJavaModule {
	public static Main main;

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

    public Main(ReactApplicationContext reactContext) {
        super(reactContext);
		// when the react-native Reload button is pressed, a new Main class instance is created; check if this just happened
		firstLaunch = main == null;
		if (!firstLaunch)
			main.Shutdown();

		main = this;
		this.reactContext = reactContext;
    }
	ReactApplicationContext reactContext;
	public boolean firstLaunch;

	public void SendEvent(String eventName, Object... args) {
		WritableArray argsList = Arguments.createArray();
		for (Object arg : args)
			V.WritableArray_Add(argsList, arg);

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

	// monitor
	public int updateInterval = 1;
	public boolean monitor = true;
	public boolean patternMatch;
	// settings
	public boolean blockUnusedKeys;
	public double patternMatchInterval;
	public double patternMatchOffset;
	public int museEEGPacketBufferSize;
	public double eyeTracker_horizontalSensitivity;
	public double eyeTracker_verticalSensitivity;
	public double eyeTracker_ignoreXMovementUnder;
	public double eyeTracker_ignoreYMovementUnder;
	public double eyeTracker_relaxVSTenseIntensity;
	public double eyeTraceSegmentSize;
	public int eyeTraceSegmentCount;
	@ReactMethod public void SetBasicData(ReadableMap data) {
		// monitor
		this.updateInterval = data.getInt("updateInterval");
		this.monitor = data.getBoolean("monitor");
		this.patternMatch = data.getBoolean("patternMatch");
		// settings
		this.blockUnusedKeys = data.getBoolean("blockUnusedKeys");
		this.patternMatchInterval = data.getDouble("patternMatchInterval");
		V.Assert(this.patternMatchInterval > 0, "Pattern-match-interval must be greater than 0.");
		this.patternMatchOffset = data.getDouble("patternMatchOffset");
		this.museEEGPacketBufferSize = data.getInt("museEEGPacketBufferSize");
		this.eyeTracker_horizontalSensitivity = data.getDouble("eyeTracker_horizontalSensitivity");
		this.eyeTracker_verticalSensitivity = data.getDouble("eyeTracker_verticalSensitivity");
		this.eyeTracker_ignoreXMovementUnder = data.getDouble("eyeTracker_ignoreXMovementUnder");
		this.eyeTracker_ignoreYMovementUnder = data.getDouble("eyeTracker_ignoreYMovementUnder");
		this.eyeTracker_relaxVSTenseIntensity = data.getDouble("eyeTracker_relaxVSTenseIntensity");
		this.eyeTraceSegmentSize = data.getDouble("eyeTraceSegmentSize");
		this.eyeTraceSegmentCount = data.getInt("eyeTraceSegmentCount");
	}

	List<Pattern> patterns;
	@ReactMethod public void SetPatterns(ReadableArray patternMaps) {
		patterns = new ArrayList<Pattern>();
		for (ReadableMap map : V.List_ReadableMaps(patternMaps)) {
			Pattern pattern = Pattern.FromMap(map);
			patterns.add(pattern);
		}
	}

	ChartManager mainChartManager = new ChartManager();

	/*@ReactMethod public void SendFakeMuseDataPacket(ReadableArray args) {
		String type = args.getString(0);
		ReadableArray column = args.getArray(1);
		ArrayList<Double> columnFinal = new ArrayList<>(column.size());
		for (int i = 0; i < column.size(); i++)
			columnFinal.set(i, column.getDouble(i));
		mainChartManager.OnReceiveMuseDataPacket(type, columnFinal);
	}*/

	Timer chartAttachTimer;
	@ReactMethod public void OnTabSelected(int tab) {
		MainActivity.main.runOnUiThread(()-> {
			if (tab == 0) {
				if (chartAttachTimer == null) {
					chartAttachTimer = new Timer();
					chartAttachTimer.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							MainActivity.main.runOnUiThread(()-> {
								//if (!mainChartManager.initialized)
								mainChartManager.TryToInit();

								// if we just succeeded, disable timer and run post-init code
								if (mainChartManager.initialized) {
									chartAttachTimer.cancel();
									mainChartManager.SetChartVisible(true);
								}
							});
						}
					}, 1000, 1000);
				}

				if (mainChartManager.initialized)
					mainChartManager.SetChartVisible(true);
			}
			else {
				if (mainChartManager.initialized)
					mainChartManager.SetChartVisible(false);
			}
		});
	}

	@ReactMethod public void UpdateChartBounds() {
		if (!mainChartManager.initialized) return;
		//V.WaitXThenRun(500, ()-> {
			mainChartManager.UpdateChartBounds();
		//});
	}

	/*@ReactMethod public void OnMonitorChangeVisible(boolean visible) {
	}*/

	@ReactMethod public void PostJSLog(String type, String message) {
		V.Log(type + " [js]", message, false);
	}

	@ReactMethod public void ConvertURIToPath(String uriStr, Promise promise) {
		Uri uri = Uri.parse(uriStr);
		String path = VFile.URIToPath(uri);
		promise.resolve(path);
	}

	/*@ReactMethod public void OnSetPatternMatchProbability(int x, double probability) {
		if (mainChartManager.initialized)
			mainChartManager.OnSetPatternMatchProbability(x, probability);
	}*/

	@ReactMethod public void StartPatternGrab(int minX, int maxX, Promise promise) {
		WritableArray channelPointsGrabbed = Arguments.createArray();
		for (int ch = 0; ch < 4; ch++) {
			Vector2i[] channelPoints = this.mainChartManager.processor.channelPoints.get(ch);

			WritableArray thisChannelPointsGrabbed = Arguments.createArray();
			for (int x = minX; x <= maxX; x++)
				thisChannelPointsGrabbed.pushMap(channelPoints[x].ToMap());
			channelPointsGrabbed.pushArray(thisChannelPointsGrabbed);
		}

		WritableMap result = Arguments.createMap();
		result.putArray("channelPointsGrabbed", channelPointsGrabbed);
		result.putArray("channelBaselines", V.ToWritableArray(V.ToObjectArray(this.mainChartManager.processor.channelBaselines)));
		promise.resolve(result);
	}
	/*@ReactMethod public void GetChannelPoints(Promise promise) {
		WritableArray channel_pointsGrabbed = Arguments.createArray();
		for (int ch = 0; ch < 4; ch++) {
			Vector2i[] channelPoints = this.mainChartManager.processor.channelPoints.get(ch);

			WritableArray channelPointsGrabbed = Arguments.createArray();
			for (int x = minX; x <= maxX; x++)
				channelPointsGrabbed.pushMap(channelPoints[x].ToMap());
			channel_pointsGrabbed.pushArray(channelPointsGrabbed);
		}
		promise.resolve(channel_pointsGrabbed);
	}*/

	@ReactMethod public void CenterEyeTracker() {
		mainChartManager.processor.eyePosX = .5;
		mainChartManager.processor.eyePosY = .5;
	}

	void Shutdown() {
		V.Log("Shutting down...");

		mainChartManager.Shutdown();
	}
}
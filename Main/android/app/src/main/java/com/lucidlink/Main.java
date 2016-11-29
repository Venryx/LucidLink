package com.lucidlink;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.choosemuse.libmuse.Accelerometer;
import com.choosemuse.libmuse.Eeg;
import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseArtifactPacket;
import com.choosemuse.libmuse.MuseDataListener;
import com.choosemuse.libmuse.MuseDataPacket;
import com.choosemuse.libmuse.MuseDataPacketType;
import com.facebook.imagepipeline.producers.Consumer;
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
import com.facebook.react.shell.MainReactPackage;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.v.LibMuse.LibMuse;
import com.v.LibMuse.MainModule;

import java.io.File;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public boolean blockUnusedKeys;
	public int updateInterval = 1;
	public boolean monitor = true;
	@ReactMethod public void SetBasicData(ReadableMap data) {
		this.blockUnusedKeys = data.getBoolean("blockUnusedKeys");
		this.updateInterval = data.getInt("updateInterval");
		this.monitor = data.getBoolean("monitor");
	}

	ChartManager mainChartManager = new ChartManager();

	@ReactMethod public void SendFakeMuseDataPacket(ReadableArray args) {
		String type = args.getString(0);
		ReadableArray column = args.getArray(1);
		ArrayList<Double> columnFinal = new ArrayList<>(column.size());
		for (int i = 0; i < column.size(); i++)
			columnFinal.set(i, column.getDouble(i));
		mainChartManager.OnReceiveMuseDataPacket(type, columnFinal);
	}

	@ReactMethod public void OnTabSelected(int tab) {
		MainActivity.main.runOnUiThread(()-> {
			if (tab == 0) {
				V.WaitXThenRun(1000, ()-> {
					if (!mainChartManager.initialized)
						mainChartManager.TryToInit();
					if (mainChartManager.initialized)
						mainChartManager.SetChartVisible(true);
				});
			}
			else {
				if (mainChartManager.initialized)
					mainChartManager.SetChartVisible(false);
			}
		});
	}

	/*@ReactMethod public void OnMonitorChangeVisible(boolean visible) {
	}*/

	@ReactMethod public void PostJSLog(String type, String message) {
		V.Log(type + " [js]", message, false);
	}

	@ReactMethod public void OnSetPatternMatchProbability(int x, double probability) {
		if (mainChartManager.initialized)
			mainChartManager.OnSetPatternMatchProbability(x, probability);
	}

	void Shutdown() {
		V.Log("Shutting down...");

		mainChartManager.Shutdown();
	}
}
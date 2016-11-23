package com.lucidlink;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
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
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.shell.MainReactPackage;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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

	@ReactMethod public void StartTest1() {
		//final ViewGroup chartHolder = (ViewGroup)V.FindViewByContentDescription(V.GetRootView(), "chart holder");
		final ViewGroup chartHolder = (ViewGroup)V.GetRootView();
		//ShowToast("ChartHolder:" + chartHolder.getId(), 3);

		MainActivity.main.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				V.Toast("Test1");
				LineChart chart = new LineChart(Main.main.reactContext);
				chartHolder.addView(chart, new ViewGroup.LayoutParams(700, 500));
				V.Toast("Child count: " + chartHolder.getChildCount());


				// part 1
				// ==========

				chart.setDrawGridBackground(false);
				chart.getAxisLeft().setDrawGridLines(false);
				chart.getAxisRight().setEnabled(false);
				chart.getXAxis().setDrawGridLines(true);
				chart.getXAxis().setDrawAxisLine(false);
				// dont forget to refresh the drawing
				chart.invalidate();

				// part 2
				// ==========

				ArrayList<Entry> yVals = new ArrayList<Entry>();

				int count = 1000;
				float range = 500f;
				for (int i = 0; i < count; i++) {
					float mult = (range + 1);
					float val = (float) (Math.random() * mult) + 3;// + (float)
					// ((mult *
					// 0.1) / 10);
					yVals.add(new Entry(i * 0.001f, val));
				}

				// create a dataset and give it a type
				LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");

				set1.setColor(Color.BLACK);
				set1.setLineWidth(0.5f);
				set1.setDrawValues(false);
				set1.setDrawCircles(false);
				set1.setMode(LineDataSet.Mode.LINEAR);
				set1.setDrawFilled(false);

				// create a data object with the datasets
				LineData data = new LineData(set1);

				// set data
				chart.setData(data);

				// get the legend (only possible after setting data)
				Legend l = chart.getLegend();
				l.setEnabled(false);

				// redraw
				// ==========

				chart.invalidate();
			}
		});
		/*Handler h=new Handler();
		h.post(new Runnable() {
			public void run() {
				V.Toast("Test2");
				LineChart chart = new LineChart(Main.main.reactContext);
				chartHolder.addView(chart);
			}
		});*/
	}
}
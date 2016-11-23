package com.lucidlink;

import android.graphics.Color;
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

	ChartManager mainChartManager = new ChartManager();
	@ReactMethod public void StartTest1() {
		mainChartManager.Init();
	}

	@ReactMethod public void SendFakeMuseDataPacket(ReadableArray args) {
		String type = args.getString(0);
		ReadableArray column = args.getArray(1);
		ArrayList<Double> columnFinal = new ArrayList<>(column.size());
		for (int i = 0; i < column.size(); i++)
			columnFinal.set(i, column.getDouble(i));
		mainChartManager.OnReceiveMuseDataPacket(type, columnFinal);
	}
}

class ChartManager {
	public void Init() {
		// init ui
		// ==========

		ViewGroup chartHolder = (ViewGroup)V.FindViewByContentDescription(V.GetRootView(), "chart holder");
		int[] chartHolderPos = new int[2];
		chartHolder.getLocationInWindow(chartHolderPos);

		chart = new LineChart(Main.main.reactContext) {
			@Override
			public boolean onInterceptTouchEvent(MotionEvent ev) {
				return false;
			}

			@Override
			public boolean onTouchEvent(MotionEvent event) {
				return false;
			}
		};
		chart.setFocusable(false);
		chart.setFocusableInTouchMode(false);
		chart.setClickable(false);
		final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(chartHolder.getWidth(), chartHolder.getHeight());
		params.leftMargin = chartHolderPos[0];
		params.topMargin = chartHolderPos[1];
		MainActivity.main.runOnUiThread(new Runnable() {
			@Override public void run() {
				V.GetRootView().addView(chart, params);

				chart.setDrawGridBackground(false);
				chart.getAxisLeft().setDrawGridLines(false);
				chart.getAxisRight().setEnabled(false);
				chart.getXAxis().setDrawGridLines(true);
				chart.getXAxis().setDrawAxisLine(false);

				/*chart.setVisibleYRange(-10, 10, YAxis.AxisDependency.LEFT);
				chart.setVisibleYRange(-10, 10, YAxis.AxisDependency.RIGHT);*/

				chart.getAxis(YAxis.AxisDependency.LEFT).setSpaceTop(.2f);
				chart.getAxis(YAxis.AxisDependency.LEFT).setSpaceBottom(.2f);
				chart.getAxis(YAxis.AxisDependency.LEFT).setDrawZeroLine(false);

				// don't forget to refresh the drawing
				chart.invalidate();
			}
		});

		lines = new ArrayList<>();
		for (int i = 0; i < eegCount; i++) {
			// start channels as flat
			ArrayList<Entry> thisChannelPoints = new ArrayList<>();
			for (int i2 = 0; i2 <= maxX; i2++)
				thisChannelPoints.add(new Entry(i2 * stepSizeInPixels, 0));
			channelPoints.add(thisChannelPoints);
			LineDataSet line = new LineDataSet(thisChannelPoints, "Channel " + i);
			line.setAxisDependency(YAxis.AxisDependency.LEFT);

			line.setColor(Color.BLACK);
			line.setLineWidth(0.5f);
			line.setDrawValues(false);
			line.setDrawCircles(false);
			line.setMode(LineDataSet.Mode.LINEAR);
			line.setDrawFilled(false);
			lines.add(line);
		}

		// create a data object with the lines
		data = new LineData(lines);
		MainActivity.main.runOnUiThread(new Runnable() {
			@Override public void run() {
				// set data
				chart.setData(data);

				// get the legend (only possible after setting data)
				Legend l = chart.getLegend();
				l.setEnabled(false);

				chart.invalidate(); // redraw
			}
		});

		// add listener that adds points when muse data is received
		/*MainModule.main.AddEventListener("OnReceiveMuseDataPacket", new MainModule.Func<WritableArray>() {
			public void Run(WritableArray args) {
				ChartManager.this.OnReceiveMuseDataPacket(args);
			}
		});*/
		MainModule.main.RegisterDataListener(new MuseDataListener() {
			@Override
			public void receiveMuseDataPacket(final MuseDataPacket p, final Muse muse) {
				MuseDataPacketType packetType = p.packetType();
				String type;
				if (packetType == MuseDataPacketType.EEG)
					type = "eeg";
				else if (packetType == MuseDataPacketType.ACCELEROMETER)
					type = "accelerometer";
				else if (packetType == MuseDataPacketType.ALPHA_RELATIVE)
					type = "alpha";
				else // currently we just ignore other packet types
					return;
				ArrayList<Double> data = p.values();

				ChartManager.this.OnReceiveMuseDataPacket(type, data);
			}

			@Override
			public void receiveMuseArtifactPacket(final MuseArtifactPacket p, final Muse muse) {}
		});
	}

	LineChart chart;

	//const int eegCount = 6;
	final int eegCount = 4; // only first 4 are real/usable
	int stepSizeInPixels = 1;

	// where eg. [0][1] (channel, x-pos/index) is [1,9] (x-pos, y-pos)z
	ArrayList<ArrayList<Entry>> channelPoints = new ArrayList<>();
	LineData data;
	List<ILineDataSet> lines;

	int lastX = -1;
	int maxX = 500;

	public void OnReceiveMuseDataPacket(String type, ArrayList<Double> column) {
		try {
			if (!type.equals("eeg")) return;

			// add points
			int currentX = lastX < maxX ? lastX + 1 : 0;
			for (int channel = 0; channel < eegCount; channel++) {
				/*Entry entry = data.getDataSetByIndex(channel).getEntryForIndex(currentX);
				entry.setX(currentX * stepSizeInPixels);
				entry.setY((float)(double)column.get(channel));*/
				//data.getDataSetByIndex(channel).removeEntry(currentX);
				data.getDataSetByIndex(channel).removeEntry(currentX);
				data.getDataSetByIndex(channel).addEntryOrdered(new Entry(currentX, (float)(double)column.get(channel)));
			}
			lastX = currentX;

			//V.Toast("Updating...");

			chart.setVisibleXRange(0, maxX);
			//chart.setVisibleYRange(0, 100, YAxis.AxisDependency.LEFT);
			/*chart.getAxisLeft().setSpaceTop(.2f);
			chart.getAxisLeft().setSpaceBottom(.2f);*/

			data.notifyDataChanged();
			chart.notifyDataSetChanged();

			UpdateChart();
		}
		catch (Throwable ex) { V.Log("Error in muse-data receiver: " + ex); }
	}

	//int count = 0;
	void UpdateChart() {
		MainActivity.main.runOnUiThread(new Runnable() {
			@Override public void run() {
				/*count++;

				if (count % 100 == 0) {
					data.notifyDataChanged();
					chart.notifyDataSetChanged();
				}*/

				chart.invalidate(); // redraw
			}
		});
	}
}
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

	void Shutdown() {
		V.Log("Shutting down...");

		mainChartManager.Shutdown();
	}
}

class ChartManager {
	public boolean initialized;
	public void TryToInit() {
		// create new chart-holder (with same pos and size as react-native, placeholder chart-holder)
		// ==========

		ViewGroup chartHolder = (ViewGroup)V.FindViewByContentDescription(V.GetRootView(), "chart holder");
		if (chartHolder == null) {
			V.Log("Could not find chart-holder.");
			return; // must have switched to another tab quickly; just wait till they switch back and we get called again
		}
		V.Log("Creating chart over chart-holder.");

		initialized = true;

		int[] chartHolderPos = new int[2];
		chartHolder.getLocationInWindow(chartHolderPos);

		newChartHolder = new RelativeLayout(MainActivity.main);
		newChartHolder.setLayoutParams(V.CreateFrameLayoutParams(chartHolderPos[0], chartHolderPos[1], chartHolder.getWidth(), chartHolder.getHeight()));
		V.GetRootView().addView(newChartHolder);

		// create chart
		// ==========

		chart = new LineChart(MainActivity.main) {
			@Override public boolean onInterceptTouchEvent(MotionEvent ev) { return false; }
			@Override public boolean onTouchEvent(MotionEvent event) { return false; }
		};
		//chart.setBackgroundColor(Color.parseColor("#303030"));
		chart.setBackgroundColor(Color.rgb(39, 39, 39));
		chart.setFocusable(false);
		chart.setFocusableInTouchMode(false);
		chart.setClickable(false);
		chart.setLayoutParams(V.CreateRelativeLayoutParams(0, 0, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		newChartHolder.addView(chart);

		chart.setDrawGridBackground(false);
		chart.getAxisLeft().setDrawGridLines(false);
		chart.getAxisLeft().setSpaceTop(.2f);
		chart.getAxisLeft().setSpaceBottom(.2f);
		chart.getAxisLeft().setDrawZeroLine(false);
		chart.getAxisLeft().setTextColor(V.textColor);
		chart.getAxisRight().setEnabled(false);
		chart.getXAxis().setDrawGridLines(true);
		chart.getXAxis().setDrawAxisLine(false);

		/*chart.setVisibleXRange(0, maxX); // << WARNING: THIS CAUSES A FREEZE
		chart.setVisibleYRange(-10, 10, YAxis.AxisDependency.LEFT);
		chart.setVisibleYRange(-10, 10, YAxis.AxisDependency.RIGHT);*/
		// this doesn't work for some reason, so use fake-data and then call calcMinMax
		//chart.setVisibleYRange(0, 6000, YAxis.AxisDependency.LEFT);

		// fill chart with fake data
		// ==========

		lines = new ArrayList<>();
		for (int i = 0; i < eegCount; i++) {
			// start channels as flat
			ArrayList<Entry> thisChannelPoints = new ArrayList<>();
			for (int i2 = 0; i2 <= maxX; i2++)
				thisChannelPoints.add(new Entry(i2 * stepSizeInPixels, i == 0 ? 0 : 6000));
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
		data = new LineData(lines);
		chart.setData(data);

		for (int channel = 0; channel < eegCount; channel++)
			data.getDataSetByIndex(channel).calcMinMax();
		data.notifyDataChanged();
		chart.notifyDataSetChanged();

		// hide axis-labels and such
		Description description = new Description();
		description.setText("");
		chart.setDescription(description);
		chart.getAxisLeft().setDrawLabels(false);
		chart.getAxisRight().setDrawLabels(false);
		//chart.getXAxis().setDrawLabels(false);
		chart.getLegend().setEnabled(false);

		chart.invalidate(); // draw

		// set up current-time-marker
		// ==========

		currentTimeMarker = new View(MainActivity.main);
		currentTimeMarker.setBackgroundColor(Color.parseColor("#00FF00"));
		currentTimeMarker.setLayoutParams(V.CreateRelativeLayoutParams(0, 0, 0, 0));
		newChartHolder.addView(currentTimeMarker);

		// set up listeners
		// ==========

		MainModule.main.extraListener = new MuseDataListener() {
			@Override
			public void receiveMuseDataPacket(final MuseDataPacket p, final Muse muse) {
				MainModule.main.dataListenerEnabled = Main.main.monitor;
				if (!Main.main.monitor) return;

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
		};
	}

	public void SetChartVisible(boolean visible) {
		if (visible) {
			chart.setVisibility(View.VISIBLE);
			currentTimeMarker.setVisibility(View.VISIBLE);
		} else {
			//mainChartManager.chart.setVisibility(View.INVISIBLE);
			chart.setVisibility(View.GONE);
			currentTimeMarker.setVisibility(View.GONE);
		}
	}

	ViewGroup newChartHolder;
	LineChart chart;
	View currentTimeMarker;

	//const int eegCount = 6;
	final int eegCount = 4; // only first 4 are the actual EEG sensors
	int stepSizeInPixels = 1;

	// where eg. [0][1] (channel, x-pos/index) is [1,9] (x-pos, y-pos)z
	ArrayList<ArrayList<Entry>> channelPoints = new ArrayList<>();
	LineData data;
	List<ILineDataSet> lines;

	int lastX = -1;
	int maxX = 1000;

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

				/*data.getDataSetByIndex(channel).removeEntry(currentX);
				data.getDataSetByIndex(channel).addEntryOrdered(new Entry(currentX, (float)(double)column.get(channel)));*/

				DataSet dataSet = (DataSet)data.getDataSetByIndex(channel);

				float yBase = 4000 - (channel * 1000); // simulate lines being in different rows
				float yValue = (float)(double)column.get(channel);

				dataSet.getValues().set(currentX, new Entry(currentX, yBase + yValue));
			}
			lastX = currentX;

			if (count == 0) // init stuff that nonetheless needs real data
				chart.setVisibleXRange(0, maxX);

			if (count % Main.main.updateInterval == 0)
				UpdateChart();

			count++;
		}
		catch (Throwable ex) { V.Log("Error in muse-data receiver: " + ex); }
	}

	int count = 0;
	//void UpdateChart(int currentX) {
	void UpdateChart() {
		MainActivity.main.runOnUiThread(() -> {
			chart.invalidate(); // redraw

			int xPos = (int)((lastX / (double)maxX) * newChartHolder.getWidth());
			currentTimeMarker.setLayoutParams(V.CreateRelativeLayoutParams(xPos, 0, 5, ViewGroup.LayoutParams.MATCH_PARENT));
		});
	}

	public void Shutdown() {
		if (!initialized) return;

		MainActivity.main.runOnUiThread(() -> {
			((ViewGroup) newChartHolder.getParent()).removeView(newChartHolder);
		});
	}
}
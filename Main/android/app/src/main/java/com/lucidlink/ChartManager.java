package com.lucidlink;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseArtifactPacket;
import com.choosemuse.libmuse.MuseDataListener;
import com.choosemuse.libmuse.MuseDataPacket;
import com.choosemuse.libmuse.MuseDataPacketType;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.lucidlink.Main;
import com.lucidlink.MainActivity;
import com.lucidlink.V;
import com.v.LibMuse.MainModule;

import java.util.ArrayList;
import java.util.List;

class ChartManager {
	public boolean initialized;
	public void TryToInit() {
		// create new chart-holder (with same pos and size as react-native, placeholder chart-holder)
		// ==========

		ViewGroup chartHolder = (ViewGroup) V.FindViewByContentDescription(V.GetRootView(), "chart holder");
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

			line.setColor(Color.WHITE);
			line.setLineWidth(0.5f);
			line.setDrawValues(false);
			line.setDrawCircles(false);
			line.setMode(LineDataSet.Mode.LINEAR);
			line.setDrawFilled(false);
			lines.add(line);
		}

		// add channel for pattern
		{
			ArrayList<Entry> thisChannelPoints = new ArrayList<>();
			/*for (int i2 = 0; i2 <= maxX; i2++)
				thisChannelPoints.add(new Entry(i2 * stepSizeInPixels, 0));*/
			thisChannelPoints.add(new Entry(0, 0));
			channelPoints.add(thisChannelPoints);
			LineDataSet line = new LineDataSet(thisChannelPoints, "Pattern-match probability");
			line.setAxisDependency(YAxis.AxisDependency.LEFT);

			line.setColor(Color.RED);
			line.setLineWidth(1);
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
		/*chart.getXAxis().setAxisLineColor(V.textColor);
		chart.getXAxis().setGridColor(V.textColor);*/
		chart.getXAxis().setTextColor(V.textColor);
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
	int maxY_fullChart = 6000;

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
	int lastSetPatternMatchProbability_x = -1;
	public void OnSetPatternMatchProbability(int currentX, double probability) {
		DataSet dataSet = (DataSet)data.getDataSetByIndex(4);

		// if we went back to start of chart, clear end of chart's last filling
		if (currentX < lastSetPatternMatchProbability_x) {
			for (int x = lastSetPatternMatchProbability_x + 1; x <= maxX; x++) {
				List<Entry> entriesToRemove = dataSet.getEntriesForXValue(x);
				for (Entry entry : entriesToRemove)
					dataSet.removeEntry(entry);
			}
		}

		for (int x = lastSetPatternMatchProbability_x + 1; x <= currentX; x++) {
			List<Entry> entriesToRemove = dataSet.getEntriesForXValue(x);
			for (Entry entry : entriesToRemove)
				dataSet.removeEntry(entry);
		}

		dataSet.addEntryOrdered(new Entry(currentX, (float)(probability * maxY_fullChart)));

		V.Log("Setting for X: " + currentX + ";" + probability);

		lastSetPatternMatchProbability_x = currentX;

		chart.setVisibleXRange(0, maxX);

		if (count % Main.main.updateInterval == 0)
			UpdateChart();
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
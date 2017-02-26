package v.lucidlink;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import v.LibMuse.VMuseDataPacket;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static v.lucidlink.LLHolder.LL;

class RelativeLayout_NonReactRoot extends RelativeLayout {
	public RelativeLayout_NonReactRoot(Context context) {
		super(context);
	}

	@Override
	public void requestLayout() {
		super.requestLayout();

		// The view relies on a measure + layout pass happening after it calls requestLayout().
		// Without this, the widget never actually changes the selection and doesn't call the
		// appropriate listeners. Since we override onLayout in our ViewGroups, a layout pass never
		// happens after a call to requestLayout, so we simulate one here.
		post(measureAndLayout);
	}

	private final Runnable measureAndLayout = ()-> {
		measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
			MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
		layout(getLeft(), getTop(), getRight(), getBottom());
	};
}
/*class LinearLayout_NonReactRoot extends LinearLayout {
	public LinearLayout_NonReactRoot(Context context) {
		super(context);
	}

	@Override
	public void requestLayout() {
		super.requestLayout();

		// The view relies on a measure + layout pass happening after it calls requestLayout().
		// Without this, the widget never actually changes the selection and doesn't call the
		// appropriate listeners. Since we override onLayout in our ViewGroups, a layout pass never
		// happens after a call to requestLayout, so we simulate one here.
		post(measureAndLayout);
	}

	private final Runnable measureAndLayout = ()-> {
		measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
		layout(getLeft(), getTop(), getRight(), getBottom());
	};
}*/

class ChartManager {
	public ChartManager(EEGProcessor eegProcessor) {
		this.eegProcessor = eegProcessor;
	}
	EEGProcessor eegProcessor;

	public boolean initialized;
	public void TryToInit() {
		// create non-react chart-holder, inside the react-native one (it doesn't lay out children properly)
		// ==========

		ViewGroup chartHolder = (ViewGroup) V.FindViewByContentDescription(V.GetRootView(), "chart holder");
		if (chartHolder == null) {
			V.Log("Could not find chart-holder.");
			return; // must have switched to another tab quickly; just wait till they switch back and we get called again
		}
		V.Log("Creating chart over chart-holder.");

		initialized = true;

		newChartHolder = new RelativeLayout_NonReactRoot(MainActivity.main);
		//newChartHolder.setLayoutParams(V.CreateViewGroupLayoutParams(chartHolder.getWidth(), chartHolder.getHeight()));
		newChartHolder.setLayoutParams(V.CreateViewGroupLayoutParams(V.MATCH_PARENT, V.MATCH_PARENT));
		chartHolder.addView(newChartHolder);
		// actually layout new-chart-holder (react won't do it, since it's not a react view)
		newChartHolder.layout(0, 0, chartHolder.getWidth(), chartHolder.getHeight());

		// create view-distance and chart holder
		// ==========

		viewDistanceAndChartHolder = new LinearLayout(MainActivity.main);
		viewDistanceAndChartHolder.setLayoutParams(V.CreateRelativeLayoutParams(0, 0, V.MATCH_PARENT, V.MATCH_PARENT));
		newChartHolder.addView(viewDistanceAndChartHolder);

		// create view-distance marker
		// ==========

		//viewDistanceBackground = new LinearLayout(MainActivity.main);
		viewDistanceBackground = new RelativeLayout(MainActivity.main);
		viewDistanceBackground.setLayoutParams(V.CreateLinearLayoutParams(0, 0, 50, V.MATCH_PARENT, 0));
		viewDistanceAndChartHolder.addView(viewDistanceBackground);

		viewDistanceForeground = new View(MainActivity.main);
		viewDistanceForeground.setBackgroundColor(Color.parseColor("#0000FF"));
		//viewDistanceForeground.setLayoutParams(V.CreateLinearLayoutParams(0, 0, V.MATCH_PARENT, 0, 0));
		viewDistanceForeground.setLayoutParams(V.CreateRelativeLayoutParams(0, 0, V.MATCH_PARENT, 0));
		viewDistanceBackground.addView(viewDistanceForeground);

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
		//chart.setLayoutParams(V.CreateRelativeLayoutParams(0, 0, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		chart.setLayoutParams(V.CreateLinearLayoutParams(0, 0, 0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
		//newChartHolder.addView(chart);
		viewDistanceAndChartHolder.addView(chart);

		chart.setDrawGridBackground(false);
		chart.getAxisLeft().setDrawGridLines(false);
		chart.getAxisLeft().setSpaceTop(.2f);
		chart.getAxisLeft().setSpaceBottom(.2f);
		chart.getAxisLeft().setDrawZeroLine(false);
		chart.getAxisLeft().setTextColor(V.textColor);
		chart.getAxisRight().setEnabled(false);
		chart.getXAxis().setDrawGridLines(true);
		chart.getXAxis().setDrawAxisLine(false);
		chart.getXAxis().setLabelCount(50);

		/*chart.setVisibleXRange(0, maxX); // << WARNING: THIS CAUSES A FREEZE
		chart.setVisibleYRange(-10, 10, YAxis.AxisDependency.LEFT);
		chart.setVisibleYRange(-10, 10, YAxis.AxisDependency.RIGHT);*/
		// this doesn't work for some reason, so use fake-data and then call calcMinMax
		//chart.setVisibleYRange(0, maxY_fullChart, YAxis.AxisDependency.LEFT);

		// fill chart with fake data
		// ==========

		lines = new ArrayList<>();
		for (int i = 0; i < eegCount; i++) {
			// StartSession channels as flat
			ArrayList<Entry> thisChannelPoints = new ArrayList<>();
			for (int i2 = 0; i2 <= eegProcessor.maxX; i2++)
				thisChannelPoints.add(new Entry(i2 * stepSizeInPixels, i == 0 ? 0 : maxY_fullChart));
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

		chart.invalidate(); // draw chart

		// set up current-time-marker
		// ==========

		currentTimeMarker = new View(MainActivity.main);
		currentTimeMarker.setBackgroundColor(Color.parseColor("#00FF00"));
		currentTimeMarker.setLayoutParams(V.CreateRelativeLayoutParams(0, 0, 0, 0));
		newChartHolder.addView(currentTimeMarker);

		// set up eye-pos-marker
		// ==========

		eyePosMarker = new View(MainActivity.main);
		eyePosMarker.setBackgroundColor(Color.parseColor("#0000FF"));
		eyePosMarker.setLayoutParams(V.CreateRelativeLayoutParams(0, 0, 0, 0));
		newChartHolder.addView(eyePosMarker);

		// set up debug-text
		// ==========

		debugText = new TextView(MainActivity.main);
		//debugText.setBackgroundColor(Color.parseColor("#0000FF"));
		debugText.setTextColor(Color.parseColor("#FFFFFF"));
		debugText.setLayoutParams(V.CreateRelativeLayoutParams(0, 0, V.MATCH_PARENT, V.MATCH_PARENT));
		//debugText.setLayoutParams(V.CreateRelativeLayoutParams(0, 0, 1000, 1000));
		newChartHolder.addView(debugText);

		//handler = new Handler();
	}

	public void UpdateChartBounds() {
		ViewGroup chartHolder = (ViewGroup) V.FindViewByContentDescription(V.GetRootView(), "chart holder");
		if (chartHolder == null) {
			V.Log("Could not find chart-holder.");
			return; // must have switched to another tab quickly; just wait till they switch back and we get called again
		}

		int[] chartHolderPos = new int[2];
		chartHolder.getLocationInWindow(chartHolderPos);

		// moved away from page, so x is negative, so ignore
		if (chartHolderPos[0] < 0) return;

		/*FrameLayout.LayoutParams currentLayout = (FrameLayout.LayoutParams)newChartHolder.getLayoutParams();
		// if chart-bounds are unchanged, do not set
		if (chartHolderPos[0] == currentLayout.leftMargin && chartHolderPos[1] == currentLayout.topMargin
				|| chartHolder.getWidth() == currentLayout.width || chartHolder.getHeight() == currentLayout.height)
			return;*/

		MainActivity.main.runOnUiThread(() -> {
		//handler.post(()-> {
			//newChartHolder.setLayoutParams(V.CreateFrameLayoutParams(chartHolderPos[0], chartHolderPos[1], chartHolder.getWidth(), chartHolder.getHeight()));
			newChartHolder.layout(0, 0, chartHolder.getWidth(), chartHolder.getHeight());
		});
	}

	/*public void SetChartVisible(boolean visible) {
		if (visible) {
			/*chart.setVisibility(View.VISIBLE);
			currentTimeMarker.setVisibility(View.VISIBLE);
			eyePosMarker.setVisibility(View.VISIBLE);*#/
			newChartHolder.setVisibility(View.VISIBLE);
		} else {
			/*chart.setVisibility(View.GONE);
			currentTimeMarker.setVisibility(View.GONE);
			eyePosMarker.setVisibility(View.GONE);*#/
			newChartHolder.setVisibility(View.GONE);
		}
	}*/

	ViewGroup newChartHolder;
	LinearLayout viewDistanceAndChartHolder;
	// view-distance
	RelativeLayout viewDistanceBackground;
	View viewDistanceForeground;
	LineChart chart;
	View currentTimeMarker;
	View eyePosMarker;
	TextView debugText;

	//const int eegCount = 6;
	final int eegCount = 4; // only first 4 are the actual EEG sensors
	int stepSizeInPixels = 1;

	// where eg. [0][1] (channel, x-pos/index) is [1,9] (x-pos, y-pos)z
	ArrayList<ArrayList<Entry>> channelPoints = new ArrayList<>();
	LineData data;
	List<ILineDataSet> lines;


	int heightPerChannel = 500;
	int maxY_fullChart = 3000;

	public void OnReceiveMusePacket(VMuseDataPacket packet) {
		if (packet.Type().equals("eeg")) {
			// add points
			for (int channel = 0; channel < eegCount; channel++) {
				/*Entry entry = data.getDataSetByIndex(channel).getEntryForIndex(currentX);
				entry.setX(currentX * stepSizeInPixels);
				entry.setY((float)(double)column.get(channel));*/

				DataSet dataSet = (DataSet) data.getDataSetByIndex(channel);

				if (channel == 0) dataSet.setVisible(LL.mainModule.channel1);
				else if (channel == 1) dataSet.setVisible(LL.mainModule.channel2);
				else if (channel == 2) dataSet.setVisible(LL.mainModule.channel3);
				else if (channel == 3) dataSet.setVisible(LL.mainModule.channel4);

				float yBase = (heightPerChannel * 4) - (channel * heightPerChannel); // simulate lines being in different rows
				float yValue = (float)packet.eegValues[channel];

				float finalY = yBase + (yValue - 500);
				dataSet.getValues().set(eegProcessor.currentX, new Entry(eegProcessor.currentX, finalY));
			}

			if (eegProcessor.currentIndex == 0) // init stuff that nonetheless needs real data
				chart.setVisibleXRange(0, eegProcessor.maxX);

			MainActivity.main.runOnUiThread(() -> {
				if (eegProcessor.currentIndex % LL.mainModule.updateInterval == 0)
					UpdateChart();
				UpdateEyeTrackerUI(); // update eye-tracker ui to match processor's changes
				UpdateDebugUI();
			});

		} else if (packet.Type().equals("accel")) {
			// todo: add data-set for this
		}
	}


	int lastSetPatternMatchProbability_x = -1;
	public void OnSetPatternMatchProbabilities(int currentX, HashMap<String, Double> probabilities) {
		DataSet dataSet = (DataSet)data.getDataSetByIndex(4);

		// if we went back to StartSession of chart, clear end of chart's last filling
		if (currentX < lastSetPatternMatchProbability_x) {
			for (int x = lastSetPatternMatchProbability_x + 1; x <= eegProcessor.maxX; x++) {
				List entriesToRemove = dataSet.getEntriesForXValue(x);
				for (Object entry : entriesToRemove)
					dataSet.removeEntry((Entry)entry);
			}
		}

		for (int x = lastSetPatternMatchProbability_x + 1; x <= currentX; x++) {
			List entriesToRemove = dataSet.getEntriesForXValue(x);
			for (Object entry : entriesToRemove)
				dataSet.removeEntry((Entry)entry);
		}

		double probability = (Double)probabilities.values().toArray()[0];
		dataSet.addEntryOrdered(new Entry(currentX, (float)(probability * maxY_fullChart)));

		//V.Log("Setting for X: " + currentX + ";" + probability);

		lastSetPatternMatchProbability_x = currentX;

		chart.setVisibleXRange(0, eegProcessor.maxX);

		if (eegProcessor.currentIndex % LL.mainModule.updateInterval == 0)
			UpdateChart();
	}

	//Handler handler; //= new Handler(); // initialized above, as errors if done here
	//void UpdateChart(int currentX) {
	void UpdateChart() {
		//MainActivity.main.runOnUiThread(() -> {
		//handler.post(()-> {
			chart.invalidate(); // redraw

			int xPos = (int)((eegProcessor.currentX / (double)eegProcessor.maxX) * newChartHolder.getWidth());
			currentTimeMarker.setLayoutParams(V.CreateRelativeLayoutParams(xPos, 0, 5, V.MATCH_PARENT));
		//});
	}

	void UpdateEyeTrackerUI() {
		//MainActivity.main.runOnUiThread(() -> {
		//handler.post(()-> {
			int margin = 10;
			int size = 30;
			int xPos = (int)V.Lerp(0 + margin, newChartHolder.getWidth() - margin, eegProcessor.GetXPosForDisplay());
			/*int yPos = (int)V.Lerp(0 + margin, newChartHolder.getHeight() - margin, 1 - processor.GetYPosForDisplay());
			eyePosMarker.setLayoutParams(V.CreateRelativeLayoutParams(xPos - (size / 2), yPos - (size / 2), size, size));*/
			eyePosMarker.setLayoutParams(V.CreateRelativeLayoutParams(xPos - (size / 2), newChartHolder.getHeight() / 2, size, size));

			int yPos = (int)V.Lerp(0 + margin, newChartHolder.getHeight() - margin, 1 - eegProcessor.GetYPosForDisplay());
			//viewDistanceForeground.setLayoutParams(V.CreateLinearLayoutParams(0, yPos, V.MATCH_PARENT, 30, 0));
			viewDistanceForeground.setLayoutParams(V.CreateRelativeLayoutParams(0, yPos, V.MATCH_PARENT, 30));
			viewDistanceForeground.setBackgroundColor(Color.parseColor("#0000FF"));
			/*viewDistanceForeground.invalidate();
			viewDistanceForeground.requestLayout();
			viewDistanceForeground.forceLayout();*/
		//});
	}

	long lastDebugUIUpdateTime = -1;
	void UpdateDebugUI() {
		// only update 10 times per second
		long now = new Date().getTime();
		if (now - lastDebugUIUpdateTime < 100) return;
		lastDebugUIUpdateTime = now;

		//MainActivity.main.runOnUiThread(() -> {
		//handler.post(()-> {
			debugText.setText(""
				//+ "1VS2: " + eegProcessor.channel1VSChannel2Strength_averageOfLastX
				+ "\nEyePos: " + eegProcessor.eyePosX
				+ "\nCenterPos: " + eegProcessor.GetCenterPoint()
				+ "\nBL: " + eegProcessor.channelPoints.get(0)[eegProcessor.currentX].y
				+ "\nFL: " + eegProcessor.channelPoints.get(1)[eegProcessor.currentX].y
				+ "\nFR: " + eegProcessor.channelPoints.get(2)[eegProcessor.currentX].y
				+ "\nBR: " + eegProcessor.channelPoints.get(3)[eegProcessor.currentX].y
			);
		//});
	}

	public void Shutdown() {
		if (!initialized) return;

		MainActivity.main.runOnUiThread(() -> {
		//handler.post(()-> {
			((ViewGroup) newChartHolder.getParent()).removeView(newChartHolder);
		});
	}
}
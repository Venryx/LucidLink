package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.resmed.refresh.bed.LedsState;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.RST_EnvironmentalInfo;
import com.resmed.refresh.model.RST_EnvironmentalInfo.LightLevel;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.MeasureManager;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class SensorCaptureFragment
  extends BaseFragment
  implements BluetoothDataListener
{
  private static int AXIS_X_LENGTH = 300;
  public static boolean isInSensorCaptureFrgament = false;
  private BaseBluetoothActivity baseActivity;
  private int bio_count_current = Integer.MAX_VALUE - AXIS_X_LENGTH;
  private TimeSeries currentTimeSeries;
  private LinearLayout graphLayout;
  private ImageView ivGreenLigth;
  private ImageView ivOrangeLigth;
  private ImageView ivRedLigth;
  private Animation lightAnim;
  private GraphicalView mChartView;
  private XYMultipleSeriesDataset mDataset;
  private XYMultipleSeriesRenderer mRenderer;
  private int maxY = 2800;
  private int minY = 65336;
  private RST_EnvironmentalInfo.LightLevel prevLevel;
  private TextView tempText;
  
  private void createGraphView()
  {
    if (this.mChartView == null)
    {
      this.graphLayout.setBackgroundColor(0);
      this.mDataset = new XYMultipleSeriesDataset();
      this.mRenderer = new XYMultipleSeriesRenderer();
      this.mRenderer.setBackgroundColor(-1);
      this.mRenderer.setXAxisMax(this.bio_count_current);
      this.mRenderer.setXAxisMin(this.bio_count_current + AXIS_X_LENGTH);
      this.mRenderer.setYAxisMax(2800.0D);
      this.mRenderer.setYAxisMin(-200.0D);
      this.mRenderer.setScale(1.0F);
      this.mRenderer.setPanEnabled(false);
      this.mRenderer.setClickEnabled(false);
      this.mRenderer.setApplyBackgroundColor(true);
      this.mRenderer.setBackgroundColor(0);
      this.mRenderer.setMarginsColor(Color.argb(0, 1, 1, 1));
      this.mRenderer.setShowAxes(false);
      this.mRenderer.setShowGrid(false);
      this.mRenderer.setShowLabels(false);
      this.mRenderer.setShowLegend(false);
      this.mRenderer.setZoomEnabled(false);
      this.mRenderer.setExternalZoomEnabled(false);
      this.mRenderer.setZoomEnabled(false, false);
      this.currentTimeSeries = new TimeSeries("");
      this.mDataset.addSeries(this.currentTimeSeries);
      XYSeriesRenderer localXYSeriesRenderer = new XYSeriesRenderer();
      localXYSeriesRenderer.setColor(-1);
      localXYSeriesRenderer.setLineWidth(4.0F);
      localXYSeriesRenderer.setFillPoints(true);
      localXYSeriesRenderer.setDisplayChartValues(false);
      localXYSeriesRenderer.setShowLegendItem(false);
      this.mRenderer.addSeriesRenderer(localXYSeriesRenderer);
      this.mChartView = ChartFactory.getLineChartView(getBaseActivity(), this.mDataset, this.mRenderer);
      this.graphLayout.addView(this.mChartView, new ActionBar.LayoutParams(-2, -2));
    }
    for (;;)
    {
      return;
      this.mChartView.repaint();
    }
  }
  
  public void addToGraph(byte[] paramArrayOfByte)
  {
    if ((this.currentTimeSeries == null) || (paramArrayOfByte.length == 0)) {
      return;
    }
    paramArrayOfByte = PacketsByteValuesReader.readBioData(paramArrayOfByte);
    if (this.bio_count_current <= 0)
    {
      this.bio_count_current = (Integer.MAX_VALUE - AXIS_X_LENGTH);
      this.currentTimeSeries.clear();
    }
    this.bio_count_current -= 1;
    this.currentTimeSeries.add(this.bio_count_current, paramArrayOfByte[0]);
    this.mRenderer.setXAxisMin(this.bio_count_current);
    this.mRenderer.setXAxisMax(this.bio_count_current + AXIS_X_LENGTH);
    if (paramArrayOfByte[0] > this.maxY)
    {
      this.maxY = paramArrayOfByte[0];
      this.mRenderer.setYAxisMax(this.maxY);
    }
    for (;;)
    {
      this.mChartView.repaint();
      break;
      if (paramArrayOfByte[0] < this.minY)
      {
        this.minY = paramArrayOfByte[0];
        this.mRenderer.setYAxisMin(this.minY);
      }
    }
  }
  
  public void handleBreathingRate(Bundle paramBundle) {}
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    switch (paramCONNECTION_STATE)
    {
    }
    for (;;)
    {
      return;
      if (this.baseActivity != null)
      {
        Log.d("com.resmed.refresh.bluetooth", "SendingCommandStartStreamOnConnect");
        paramCONNECTION_STATE = BaseBluetoothActivity.getRpcCommands().startRealTimeStream();
        this.baseActivity.sendRpcToBed(paramCONNECTION_STATE);
      }
    }
  }
  
  public void handleEnvSample(Bundle paramBundle) {}
  
  public void handleReceivedRpc(JsonRPC paramJsonRPC) {}
  
  public void handleSessionRecovered(Bundle paramBundle) {}
  
  public void handleSleepSessionStopped(Bundle paramBundle) {}
  
  public void handleStreamPacket(Bundle paramBundle) {}
  
  public void handleUserSleepState(Bundle paramBundle) {}
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    this.baseActivity = ((BaseBluetoothActivity)paramActivity);
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903159, paramViewGroup, false);
    this.graphLayout = ((LinearLayout)paramLayoutInflater.findViewById(2131100343));
    this.tempText = ((TextView)paramLayoutInflater.findViewById(2131100344));
    this.ivRedLigth = ((ImageView)paramLayoutInflater.findViewById(2131100345));
    this.ivOrangeLigth = ((ImageView)paramLayoutInflater.findViewById(2131100346));
    this.ivGreenLigth = ((ImageView)paramLayoutInflater.findViewById(2131100347));
    this.prevLevel = null;
    this.lightAnim = new AlphaAnimation(0.6F, 1.0F);
    this.lightAnim.setDuration(500L);
    this.lightAnim.setStartOffset(20L);
    this.lightAnim.setRepeatMode(2);
    this.lightAnim.setRepeatCount(-1);
    createGraphView();
    com.resmed.refresh.ui.activity.SleepTimeActivity.isHomeButtonPressSleep = false;
    com.resmed.refresh.ui.activity.MindClearActivity.isHomeButtonPressMindClear = false;
    com.resmed.refresh.ui.activity.RelaxActivity.isHomeButtonPressRelax = false;
    com.resmed.refresh.ui.activity.SmartAlarmActivity.isHomeButtonPressSmartAlaram = false;
    return paramLayoutInflater;
  }
  
  public void onDestroy()
  {
    super.onDestroy();
    Log.d("com.resmed.refresh.bluetooth", "SendingStreamStop");
  }
  
  public void onResume()
  {
    super.onResume();
    JsonRPC localJsonRPC = BaseBluetoothActivity.getRpcCommands().leds(LedsState.OFF);
    this.baseActivity.sendRpcToBed(localJsonRPC);
    isInSensorCaptureFrgament = true;
  }
  
  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    paramView = BaseBluetoothActivity.getRpcCommands().startRealTimeStream();
    this.baseActivity.sendRpcToBed(paramView);
    paramView = BaseBluetoothActivity.getRpcCommands().leds(LedsState.OFF);
    this.baseActivity.sendRpcToBed(paramView);
    isInSensorCaptureFrgament = true;
  }
  
  public void setLightValue(int paramInt)
  {
    RST_EnvironmentalInfo.LightLevel localLightLevel = RST_EnvironmentalInfo.lightLevel(paramInt);
    if (localLightLevel != this.prevLevel)
    {
      this.prevLevel = localLightLevel;
      if (this.lightAnim != null) {
        break label26;
      }
    }
    for (;;)
    {
      return;
      label26:
      switch (localLightLevel)
      {
      default: 
        this.ivRedLigth.setImageResource(2130837765);
        this.ivOrangeLigth.setImageResource(2130837826);
        this.ivGreenLigth.setImageResource(2130837826);
        this.ivRedLigth.startAnimation(this.lightAnim);
        break;
      case kLightLevelMedium: 
        this.ivRedLigth.setImageResource(2130837826);
        this.ivOrangeLigth.setImageResource(2130837826);
        this.ivGreenLigth.setImageResource(2130837870);
        this.ivGreenLigth.startAnimation(this.lightAnim);
        break;
      case kLightLevelLow: 
        this.ivRedLigth.setImageResource(2130837826);
        this.ivOrangeLigth.setImageResource(2130837828);
        this.ivGreenLigth.setImageResource(2130837826);
        this.ivOrangeLigth.startAnimation(this.lightAnim);
      }
    }
  }
  
  public void setTempValue(int paramInt)
  {
    if ((this.tempText != null) && (isAdded())) {
      if (RefreshModelController.getInstance().getUseMetricUnits()) {
        break label64;
      }
    }
    label64:
    for (String str = Math.round(MeasureManager.convertCelsiusToFahrenheit(paramInt)) + getString(2131165309);; str = Math.round(paramInt) + getString(2131165309))
    {
      this.tempText.setText(str);
      return;
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SensorCaptureFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
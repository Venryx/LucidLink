package com.resmed.refresh.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.activity.SleepHistoryCircleHelpActivity;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.utils.Log;
import java.text.SimpleDateFormat;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class CircleGraphFragment
  extends BaseFragment
{
  private static final int ANIMATION_TIME = 1300;
  private static final int BINWEIGHTDEEP = 20;
  private static final int BINWEIGHTDURATION = 40;
  private static final int BINWEIGHTLIGHT = 5;
  private static final int BINWEIGHTONSET = 5;
  private static final int BINWEIGHTREM = 20;
  private static final int BINWEIGHTWAKE = 10;
  private static int[] COLORS;
  private static final int NUMBER_OF_TYPES = 7;
  private GraphAnimation animation;
  private int awakeValue;
  private LinearLayout layoutCircleGraphSleep;
  private LinearLayout llCircleHowScore;
  private GraphicalView mChartView;
  private DefaultRenderer mRenderer = new DefaultRenderer();
  private CategorySeries mSeries = new CategorySeries("");
  private int onsetValue;
  private int[] percentages = new int[7];
  private RST_SleepSessionInfo sessionSelected;
  private View.OnClickListener showHelp = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      paramAnonymousView = new Intent(CircleGraphFragment.this.getActivity(), SleepHistoryCircleHelpActivity.class);
      CircleGraphFragment.this.startActivity(paramAnonymousView);
      CircleGraphFragment.this.getActivity().overridePendingTransition(2130968582, 2130968583);
    }
  };
  private int sleepDeepValue;
  private int sleepLightValue;
  private int sleepRemValue;
  private int totalData;
  private int totalValue;
  private TextView tvCircleGraphDay;
  private TextView tvScoreGraphDeepAchScore;
  private TextView tvScoreGraphDeepYourScore;
  private TextView tvScoreGraphLigthAchScore;
  private TextView tvScoreGraphLigthYourScore;
  private TextView tvScoreGraphOnsetAchScore;
  private TextView tvScoreGraphOnsetYourScore;
  private TextView tvScoreGraphREMAchScore;
  private TextView tvScoreGraphREMYourScore;
  private TextView tvScoreGraphTotalAchScore;
  private TextView tvScoreGraphTotalSleepAchScore;
  private TextView tvScoreGraphTotalSleepYourScore;
  private TextView tvScoreGraphTotalYourScore;
  private TextView tvScoreGraphWakeAchScore;
  private TextView tvScoreGraphWakeYourScore;
  
  private void initGraph()
  {
    long l = getBaseActivity().getIntent().getLongExtra("com.resmed.refresh.ui.uibase.app.sleep_history_id", -1L);
    this.sessionSelected = RefreshModelController.getInstance().localSleepSessionForId(l);
    Object localObject;
    if (this.sessionSelected != null)
    {
      this.totalValue = this.sessionSelected.getBinSleepScoreTst();
      this.sleepDeepValue = this.sessionSelected.getBinSleepScoreDeep();
      this.sleepRemValue = this.sessionSelected.getBinSleepScoreRem();
      this.awakeValue = this.sessionSelected.getBinSleepScoreWaso();
      this.onsetValue = this.sessionSelected.getBinSleepScoreOnset();
      this.sleepLightValue = this.sessionSelected.getBinSleepScoreLight();
      if (this.totalValue > 40) {
        this.totalValue = 40;
      }
      if (this.sleepDeepValue > 20) {
        this.sleepDeepValue = 20;
      }
      if (this.sleepRemValue > 20) {
        this.sleepRemValue = 20;
      }
      if (this.awakeValue > 10) {
        this.awakeValue = 10;
      }
      if (this.onsetValue > 5) {
        this.onsetValue = 5;
      }
      if (this.sleepLightValue > 5) {
        this.sleepLightValue = 5;
      }
      this.totalData = (this.totalValue + this.sleepDeepValue + this.sleepRemValue + this.awakeValue + this.onsetValue + this.sleepLightValue + 6);
      this.percentages[0] = Math.round(this.totalValue * 100 / 40);
      this.percentages[1] = Math.round(this.sleepDeepValue * 100 / 20);
      this.percentages[2] = Math.round(this.sleepRemValue * 100 / 20);
      this.percentages[3] = Math.round(this.awakeValue * 100 / 10);
      this.percentages[4] = Math.round(this.onsetValue * 100 / 5);
      this.percentages[5] = Math.round(this.sleepLightValue * 100 / 5);
      this.percentages[6] = 100;
      Log.d("com.resmed.refresh.ui", "CircleGraphFragment getBinSleepScoreTst = " + this.sessionSelected.getBinSleepScoreTst());
      Log.d("com.resmed.refresh.ui", "CircleGraphFragment BINWEIGHTDURATION = 40");
      Log.d("com.resmed.refresh.ui", "CircleGraphFragment totalValue = " + this.totalValue);
      Log.d("com.resmed.refresh.ui", "CircleGraphFragment percentages[0] = " + this.percentages[0]);
      this.tvScoreGraphTotalSleepYourScore.setText(String.valueOf(this.totalValue));
      this.tvScoreGraphDeepYourScore.setText(String.valueOf(this.sleepDeepValue));
      this.tvScoreGraphREMYourScore.setText(String.valueOf(this.sleepRemValue));
      this.tvScoreGraphWakeYourScore.setText(String.valueOf(this.awakeValue));
      this.tvScoreGraphOnsetYourScore.setText(String.valueOf(this.onsetValue));
      this.tvScoreGraphLigthYourScore.setText(String.valueOf(this.sleepLightValue));
      this.tvScoreGraphTotalYourScore.setText(String.valueOf(this.sessionSelected.getSleepScore()));
      this.tvScoreGraphTotalSleepAchScore.setText(String.valueOf(40));
      this.tvScoreGraphDeepAchScore.setText(String.valueOf(20));
      this.tvScoreGraphREMAchScore.setText(String.valueOf(20));
      this.tvScoreGraphWakeAchScore.setText(String.valueOf(10));
      this.tvScoreGraphOnsetAchScore.setText(String.valueOf(5));
      this.tvScoreGraphLigthAchScore.setText(String.valueOf(5));
      this.tvScoreGraphTotalAchScore.setText(String.valueOf(100));
      this.mRenderer.setStartAngle(270.0F);
      this.mRenderer.setAntialiasing(true);
      this.mRenderer.setDisplayValues(false);
      this.mRenderer.setZoomButtonsVisible(false);
      this.mRenderer.setShowAxes(true);
      this.mRenderer.setShowLegend(false);
      this.mRenderer.setZoomEnabled(false);
      this.mRenderer.setPanEnabled(false);
      this.mRenderer.setTextTypeface(this.akzidenzLight);
      this.mRenderer.setLabelsTextSize(getResources().getDimension(2131034204));
      this.mRenderer.setScale(1.2F);
      this.mRenderer.setBackgroundColor(RefreshApplication.getInstance().getResources().getColor(2131296347));
      this.mRenderer.setXAxisColor(RefreshApplication.getInstance().getResources().getColor(2131296364));
      this.mRenderer.setSliceForZeroValues(true);
      localObject = new SimpleDateFormat("dd MMMM yyyy");
      this.tvCircleGraphDay.setText(((SimpleDateFormat)localObject).format(this.sessionSelected.getStartTime()));
    }
    for (int i = 0;; i++)
    {
      if (i >= 7) {
        return;
      }
      localObject = new SimpleSeriesRenderer();
      ((SimpleSeriesRenderer)localObject).setColor(COLORS[i]);
      ((SimpleSeriesRenderer)localObject).setPercentage(this.percentages[i]);
      ((SimpleSeriesRenderer)localObject).setShowLegendItem(false);
      this.mRenderer.addSeriesRenderer((SimpleSeriesRenderer)localObject);
      Log.d("", "percentage[" + i + "]=" + this.percentages[i]);
    }
  }
  
  private void mapGUI(View paramView)
  {
    this.layoutCircleGraphSleep = ((LinearLayout)paramView.findViewById(2131099927));
    this.tvCircleGraphDay = ((TextView)paramView.findViewById(2131099968));
    COLORS = new int[] { getBaseActivity().getResources().getColor(2131296314), getBaseActivity().getResources().getColor(2131296362), getBaseActivity().getResources().getColor(2131296360), getBaseActivity().getResources().getColor(2131296359), getBaseActivity().getResources().getColor(2131296363), getBaseActivity().getResources().getColor(2131296361), getBaseActivity().getResources().getColor(2131296309) };
    this.llCircleHowScore = ((LinearLayout)paramView.findViewById(2131099969));
    this.tvScoreGraphTotalSleepYourScore = ((TextView)paramView.findViewById(2131099973));
    this.tvScoreGraphTotalSleepAchScore = ((TextView)paramView.findViewById(2131099972));
    this.tvScoreGraphDeepYourScore = ((TextView)paramView.findViewById(2131099975));
    this.tvScoreGraphDeepAchScore = ((TextView)paramView.findViewById(2131099974));
    this.tvScoreGraphREMYourScore = ((TextView)paramView.findViewById(2131099977));
    this.tvScoreGraphREMAchScore = ((TextView)paramView.findViewById(2131099976));
    this.tvScoreGraphWakeYourScore = ((TextView)paramView.findViewById(2131099979));
    this.tvScoreGraphWakeAchScore = ((TextView)paramView.findViewById(2131099978));
    this.tvScoreGraphOnsetYourScore = ((TextView)paramView.findViewById(2131099981));
    this.tvScoreGraphOnsetAchScore = ((TextView)paramView.findViewById(2131099980));
    this.tvScoreGraphLigthYourScore = ((TextView)paramView.findViewById(2131099983));
    this.tvScoreGraphLigthAchScore = ((TextView)paramView.findViewById(2131099982));
    this.tvScoreGraphTotalYourScore = ((TextView)paramView.findViewById(2131099985));
    this.tvScoreGraphTotalAchScore = ((TextView)paramView.findViewById(2131099984));
  }
  
  private void setValueAnimation(final int paramInt)
  {
    new Handler(Looper.getMainLooper()).post(new Runnable()
    {
      public void run()
      {
        CircleGraphFragment.this.mSeries.clear();
        CircleGraphFragment.this.mSeries.add(String.valueOf(CircleGraphFragment.this.totalValue), 40.0D);
        CircleGraphFragment.this.mSeries.add(String.valueOf(CircleGraphFragment.this.sleepDeepValue), 20.0D);
        CircleGraphFragment.this.mSeries.add(String.valueOf(CircleGraphFragment.this.sleepRemValue), 20.0D);
        CircleGraphFragment.this.mSeries.add(String.valueOf(CircleGraphFragment.this.awakeValue), 10.0D);
        CircleGraphFragment.this.mSeries.add(String.valueOf(CircleGraphFragment.this.onsetValue), 5.0D);
        CircleGraphFragment.this.mSeries.add(String.valueOf(CircleGraphFragment.this.sleepLightValue), 5.0D);
        CircleGraphFragment.this.mSeries.add("", paramInt);
        CircleGraphFragment.this.mChartView = ChartFactory.getPieChartView(CircleGraphFragment.this.getBaseActivity(), CircleGraphFragment.this.mSeries, CircleGraphFragment.this.mRenderer);
        CircleGraphFragment.this.layoutCircleGraphSleep.removeAllViews();
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -1);
        CircleGraphFragment.this.layoutCircleGraphSleep.addView(CircleGraphFragment.this.mChartView, localLayoutParams);
        CircleGraphFragment.this.layoutCircleGraphSleep.invalidate();
        CircleGraphFragment.this.mChartView.repaint();
      }
    });
  }
  
  private void setupListeners()
  {
    this.llCircleHowScore.setOnClickListener(this.showHelp);
  }
  
  public View onCreateView(final LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903140, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    initGraph();
    this.animation = new GraphAnimation(null);
    this.animation.setDuration(1300L);
    this.animation.setInterpolator(new AccelerateDecelerateInterpolator());
    setValueAnimation(this.totalData * 10000);
    new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
    {
      public void run()
      {
        paramLayoutInflater.startAnimation(CircleGraphFragment.this.animation);
      }
    }, 500L);
    setupListeners();
    return paramLayoutInflater;
  }
  
  private class GraphAnimation
    extends Animation
  {
    private GraphAnimation() {}
    
    protected void applyTransformation(float paramFloat, Transformation paramTransformation)
    {
      super.applyTransformation(paramFloat, paramTransformation);
      int i = -1;
      if (paramFloat == 0.0F) {
        i = CircleGraphFragment.this.totalData * 10000 - 1;
      }
      for (;;)
      {
        CircleGraphFragment.this.setValueAnimation(i);
        return;
        if (paramFloat < 1.0F)
        {
          CircleGraphFragment.this.mRenderer.setDisplayValues(true);
          i = Math.round(CircleGraphFragment.this.totalData * (1.0F - paramFloat) / paramFloat) - 1;
        }
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\CircleGraphFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
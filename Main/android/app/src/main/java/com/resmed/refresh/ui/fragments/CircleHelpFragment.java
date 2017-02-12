package com.resmed.refresh.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class CircleHelpFragment
  extends BaseFragment
{
  private static final int BINWEIGHTDEEP = 20;
  private static final int BINWEIGHTDURATION = 40;
  private static final int BINWEIGHTLIGHT = 5;
  private static final int BINWEIGHTONSET = 5;
  private static final int BINWEIGHTREM = 20;
  private static final int BINWEIGHTWAKE = 10;
  private static int[] COLORS;
  private static final int NUMBER_OF_TYPES = 6;
  private LinearLayout layoutCircleGraphSleep;
  private GraphicalView mChartView;
  private DefaultRenderer mRenderer = new DefaultRenderer();
  private CategorySeries mSeries = new CategorySeries("");
  
  private void initGraph()
  {
    this.mRenderer.setStartAngle(270.0F);
    this.mRenderer.setAntialiasing(true);
    this.mRenderer.setDisplayValues(true);
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
    for (int i = 0;; i++)
    {
      if (i >= 6)
      {
        this.mSeries.clear();
        this.mSeries.add(String.valueOf(40), 40.0D);
        this.mSeries.add(String.valueOf(20), 20.0D);
        this.mSeries.add(String.valueOf(20), 20.0D);
        this.mSeries.add(String.valueOf(10), 10.0D);
        this.mSeries.add(String.valueOf(5), 5.0D);
        this.mSeries.add(String.valueOf(5), 5.0D);
        this.mChartView = ChartFactory.getPieChartView(getBaseActivity(), this.mSeries, this.mRenderer);
        this.layoutCircleGraphSleep.removeAllViews();
        localObject = new LinearLayout.LayoutParams(-1, -1);
        this.layoutCircleGraphSleep.addView(this.mChartView, (ViewGroup.LayoutParams)localObject);
        this.layoutCircleGraphSleep.invalidate();
        this.mChartView.repaint();
        return;
      }
      Object localObject = new SimpleSeriesRenderer();
      ((SimpleSeriesRenderer)localObject).setColor(COLORS[i]);
      ((SimpleSeriesRenderer)localObject).setPercentage(100);
      ((SimpleSeriesRenderer)localObject).setShowLegendItem(false);
      this.mRenderer.addSeriesRenderer((SimpleSeriesRenderer)localObject);
    }
  }
  
  private void mapGUI(View paramView)
  {
    this.layoutCircleGraphSleep = ((LinearLayout)paramView.findViewById(2131099927));
    COLORS = new int[] { getBaseActivity().getResources().getColor(2131296314), getBaseActivity().getResources().getColor(2131296362), getBaseActivity().getResources().getColor(2131296360), getBaseActivity().getResources().getColor(2131296359), getBaseActivity().getResources().getColor(2131296363), getBaseActivity().getResources().getColor(2131296361) };
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903130, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    initGraph();
    paramLayoutInflater.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        CircleHelpFragment.this.getBaseActivity().finish();
        CircleHelpFragment.this.getBaseActivity().overridePendingTransition(2130968582, 2130968583);
      }
    });
    return paramLayoutInflater;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\CircleHelpFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.ui.fragments;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.graphs.RRHypnoData;
import com.resmed.refresh.model.mappers.HypnoMapper;
import com.resmed.refresh.ui.customview.CustomBarChart;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.CustomDialogBuilder;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.model.YLabel;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;

public class SleepGraphFragment
  extends BaseFragment
{
  private static final int PADDING_LEFT = 200;
  private static final int PADDING_RIGHT = 50;
  private BarChart barChart;
  private View graphLabel1;
  private View graphLabel2;
  private View graphLabel3;
  private View graphLabel4;
  private View graphLabel5;
  private LinearLayout graphLayout;
  private List<RRHypnoData> hypnoData;
  private int indexOnSet;
  private ImageView ivSleepGraphLeft;
  private ImageView ivSleepGraphRigth;
  private GraphicalView mChartView;
  private XYMultipleSeriesRenderer mRenderer;
  private RST_SleepSessionInfo sessionSelected;
  private SparseArray<Integer> timeDivisions;
  private double totalX;
  private TextView tvSleepGraphDate;
  private Typeface typeface;
  private int zOld;
  
  private void createGraphView()
  {
    this.graphLayout.removeAllViews();
    this.mRenderer = getBarDemoRenderer();
    setLabels(((Integer)this.timeDivisions.get(0)).intValue());
    this.barChart = new CustomBarChart(getBarDemoDataset(), this.mRenderer, this.indexOnSet);
    this.mChartView = new GraphicalView(getBaseActivity(), this.barChart);
    this.graphLayout.addView(this.mChartView, new ActionBar.LayoutParams(-2, -2));
    this.mChartView.addZoomListener(new ZoomListener()
    {
      public void zoomApplied(ZoomEvent paramAnonymousZoomEvent)
      {
        if (SleepGraphFragment.this.totalX == -1.0D) {
          SleepGraphFragment.this.totalX = (SleepGraphFragment.this.mRenderer.getXAxisMax() - SleepGraphFragment.this.mRenderer.getXAxisMin());
        }
        int j = (int)Math.floor(SleepGraphFragment.this.totalX / (SleepGraphFragment.this.mRenderer.getXAxisMax() - SleepGraphFragment.this.mRenderer.getXAxisMin()));
        int i = j;
        if (j > 5) {
          i = 5;
        }
        if (i != SleepGraphFragment.this.zOld)
        {
          SleepGraphFragment.this.zOld = i;
          SleepGraphFragment.this.setLabels(((Integer)SleepGraphFragment.this.timeDivisions.get(i)).intValue());
        }
      }
      
      public void zoomReset() {}
    }, true, true);
    this.graphLayout.invalidate();
  }
  
  private XYMultipleSeriesDataset getBarDemoDataset()
  {
    XYSeries localXYSeries = new XYSeries("Sleep");
    localXYSeries.add(0.0D, 0.0D);
    int i = 0;
    XYMultipleSeriesDataset localXYMultipleSeriesDataset;
    if (i >= this.hypnoData.size())
    {
      localXYMultipleSeriesDataset = new XYMultipleSeriesDataset();
      localXYMultipleSeriesDataset.addSeries(localXYSeries);
      this.indexOnSet = localXYSeries.getItemCount();
    }
    label111:
    for (i = 0;; i++)
    {
      if (i >= localXYSeries.getItemCount()) {}
      for (;;)
      {
        return localXYMultipleSeriesDataset;
        localXYSeries.add(i, ((RRHypnoData)this.hypnoData.get(i)).getValueBar());
        i++;
        break;
        if (localXYSeries.getY(i) <= 0.0D) {
          break label111;
        }
        this.indexOnSet = i;
      }
    }
  }
  
  private XYMultipleSeriesRenderer getBarDemoRenderer()
  {
    Resources localResources = RefreshApplication.getInstance().getResources();
    XYSeriesRenderer localXYSeriesRenderer = new XYSeriesRenderer();
    localXYSeriesRenderer.setColor(localResources.getColor(2131296347));
    localXYSeriesRenderer.setFillPoints(true);
    localXYSeriesRenderer.setLineWidth(0.2F);
    XYMultipleSeriesRenderer localXYMultipleSeriesRenderer = new XYMultipleSeriesRenderer();
    localXYMultipleSeriesRenderer.setXAxisMin(-200.0D);
    localXYMultipleSeriesRenderer.setXAxisMax(this.hypnoData.size() + 50);
    localXYMultipleSeriesRenderer.setYAxisMin(-0.5D);
    localXYMultipleSeriesRenderer.setYAxisMax(4.0D);
    localXYMultipleSeriesRenderer.setPanLimits(new double[] { -400.0D, this.hypnoData.size() + 100, 1.0D, 4.0D });
    localXYMultipleSeriesRenderer.setZoomLimits(new double[] { -400.0D, this.hypnoData.size() + 100, 1.0D, 4.0D });
    localXYMultipleSeriesRenderer.setZoomEnabled(true, false);
    localXYMultipleSeriesRenderer.setPanEnabled(true, false);
    localXYMultipleSeriesRenderer.setShowLegend(false);
    localXYMultipleSeriesRenderer.setLabelsTextSize(getBaseActivity().getResources().getDimension(2131034205));
    localXYMultipleSeriesRenderer.setBackgroundColor(localResources.getColor(2131296347));
    localXYMultipleSeriesRenderer.setApplyBackgroundColor(true);
    localXYMultipleSeriesRenderer.setMarginsColor(localResources.getColor(2131296347));
    localXYMultipleSeriesRenderer.setXLabels(0);
    localXYMultipleSeriesRenderer.setXLabelsColor(localResources.getColor(2131296354));
    int[] arrayOfInt = new int[4];
    arrayOfInt[1] = 30;
    arrayOfInt[2] = 20;
    arrayOfInt[3] = 30;
    localXYMultipleSeriesRenderer.setMargins(arrayOfInt);
    localXYMultipleSeriesRenderer.setShowAxes(false);
    localXYMultipleSeriesRenderer.setShowTickMarks(false);
    localXYMultipleSeriesRenderer.setShowGrid(true);
    localXYMultipleSeriesRenderer.setShowCustomTextGridX(true);
    localXYMultipleSeriesRenderer.setGridStroke(localResources.getDimension(2131034238));
    localXYMultipleSeriesRenderer.setGridColor(localResources.getColor(2131296364));
    localXYMultipleSeriesRenderer.setFullScreen(true);
    localXYMultipleSeriesRenderer.setYLabels(5);
    localXYMultipleSeriesRenderer.setYLabelsEnabled(true);
    localXYMultipleSeriesRenderer.addYLabel(Integer.valueOf(4), new YLabel(this.typeface, localResources.getString(2131166017), " ", localResources.getColor(2131296353)));
    localXYMultipleSeriesRenderer.addYLabel(Integer.valueOf(3), new YLabel(this.typeface, localResources.getString(2131166014), RefreshTools.getHourMinsStringForTime(this.sessionSelected.getTotalDeepSleep()), localResources.getColor(2131296362)));
    localXYMultipleSeriesRenderer.addYLabel(Integer.valueOf(2), new YLabel(this.typeface, localResources.getString(2131166015), RefreshTools.getHourMinsStringForTime(this.sessionSelected.getTotalLightSleep()), localResources.getColor(2131296361)));
    localXYMultipleSeriesRenderer.addYLabel(Integer.valueOf(1), new YLabel(this.typeface, localResources.getString(2131166016), RefreshTools.getHourMinsStringForTime(this.sessionSelected.getTotalRem()), localResources.getColor(2131296360)));
    localXYMultipleSeriesRenderer.addYLabel(Integer.valueOf(0), new YLabel(this.typeface, localResources.getString(2131166013), RefreshTools.getHourMinsStringForTime(this.sessionSelected.getTotalWakeTime()), localResources.getColor(2131296359)));
    localXYMultipleSeriesRenderer.addSeriesRenderer(localXYSeriesRenderer);
    return localXYMultipleSeriesRenderer;
  }
  
  private void initData()
  {
    this.typeface = Typeface.createFromAsset(RefreshApplication.getInstance().getAssets(), "AkzidenzGroteskBE-Cn.otf");
    this.timeDivisions = new SparseArray();
    this.timeDivisions.append(0, Integer.valueOf(60));
    this.timeDivisions.append(1, Integer.valueOf(60));
    this.timeDivisions.append(2, Integer.valueOf(30));
    this.timeDivisions.append(3, Integer.valueOf(30));
    this.timeDivisions.append(4, Integer.valueOf(15));
    this.timeDivisions.append(5, Integer.valueOf(15));
    long l = getBaseActivity().getIntent().getLongExtra("com.resmed.refresh.ui.uibase.app.sleep_history_id", -1L);
    loadData(RefreshModelController.getInstance().localSleepSessionForId(l));
  }
  
  private void loadData(RST_SleepSessionInfo paramRST_SleepSessionInfo)
  {
    if (paramRST_SleepSessionInfo == null) {}
    for (;;)
    {
      return;
      this.sessionSelected = paramRST_SleepSessionInfo;
      Log.d("com.resmed.refresh.ids", "SleepGraphFragment initDataView id = " + this.sessionSelected.getId() + " " + this.sessionSelected.getStartTime() + " " + this.sessionSelected.getStopTime());
      new Thread(new Runnable()
      {
        public void run()
        {
          SleepGraphFragment.this.hypnoData = HypnoMapper.getHypnoData(SleepGraphFragment.this.sessionSelected);
          new Handler(Looper.getMainLooper()).post(new Runnable()
          {
            public void run()
            {
              SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
              SleepGraphFragment.this.tvSleepGraphDate.setText(localSimpleDateFormat.format(SleepGraphFragment.this.sessionSelected.getStartTime()));
              SleepGraphFragment.this.createGraphView();
            }
          });
        }
      }).start();
    }
  }
  
  private void mapGUI(View paramView)
  {
    this.graphLayout = ((LinearLayout)paramView.findViewById(2131099990));
    this.ivSleepGraphLeft = ((ImageView)paramView.findViewById(2131099988));
    this.ivSleepGraphRigth = ((ImageView)paramView.findViewById(2131099989));
    this.tvSleepGraphDate = ((TextView)paramView.findViewById(2131099987));
    this.graphLabel1 = paramView.findViewById(2131099991);
    this.graphLabel2 = paramView.findViewById(2131099992);
    this.graphLabel3 = paramView.findViewById(2131099993);
    this.graphLabel4 = paramView.findViewById(2131099994);
    this.graphLabel5 = paramView.findViewById(2131099995);
    this.ivSleepGraphLeft.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        paramAnonymousView = RefreshModelController.getInstance().localPreviousSleepSession(SleepGraphFragment.this.sessionSelected.getStartTime());
        if (paramAnonymousView != null) {
          SleepGraphFragment.this.loadData(paramAnonymousView);
        }
      }
    });
    this.ivSleepGraphRigth.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        paramAnonymousView = RefreshModelController.getInstance().localNextSleepSession(SleepGraphFragment.this.sessionSelected.getStartTime());
        if (paramAnonymousView != null) {
          SleepGraphFragment.this.loadData(paramAnonymousView);
        }
      }
    });
    this.graphLabel2.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SleepGraphFragment.this.showExplanation(2131165395, 2131165396);
      }
    });
    this.graphLabel3.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SleepGraphFragment.this.showExplanation(2131165393, 2131165394);
      }
    });
    this.graphLabel4.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SleepGraphFragment.this.showExplanation(2131165397, 2131165398);
      }
    });
    this.graphLabel5.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SleepGraphFragment.this.showExplanation(2131165424, 2131165425);
      }
    });
    this.totalX = -1.0D;
  }
  
  private void setLabels(int paramInt)
  {
    Calendar localCalendar = Calendar.getInstance();
    Log.e("Labels", " hypnoData.size=" + this.hypnoData.size());
    Object localObject;
    if (this.hypnoData.size() < 2)
    {
      localObject = new SimpleDateFormat("HH");
      localCalendar.setTime(this.sessionSelected.getStartTime());
      this.mRenderer.addXTextLabel(0.0D, ((SimpleDateFormat)localObject).format(localCalendar.getTime()));
      localCalendar.setTime(this.sessionSelected.getStopTime());
      this.mRenderer.addXTextLabel(100.0D, ((SimpleDateFormat)localObject).format(localCalendar.getTime()));
      return;
    }
    int k = (int)(this.hypnoData.size() * 0.5F / paramInt) + 2;
    Log.e("Labels", " hypnoData.size=" + this.hypnoData.size());
    Log.e("Labels", " step=" + paramInt);
    Log.e("Labels", " totalLabels=" + k);
    Log.e("Labels", " sessionSelected.getStartTime=" + this.sessionSelected.getStartTime());
    this.mRenderer.clearXTextLabels();
    int j = 0;
    label635:
    for (int i = 0;; i++)
    {
      if (i >= this.hypnoData.size()) {
        i = j;
      }
      for (;;)
      {
        Log.e("Labels", " start=" + i);
        localCalendar.setTime(((RRHypnoData)this.hypnoData.get(i)).getHour());
        Log.e("Labels", " calendar.setTime=" + localCalendar.getTime());
        for (j = 0; j < k; j++)
        {
          localObject = new SimpleDateFormat("HH:mm").format(localCalendar.getTime()).replace(":00", "");
          double d = j * (paramInt * 60.0F) / 30.0F + i;
          Log.e("Labels", " label=" + (String)localObject + " position = " + d);
          this.mRenderer.addXTextLabel(d, (String)localObject);
          localCalendar.add(12, paramInt);
        }
        break;
        localCalendar.setTime(((RRHypnoData)this.hypnoData.get(i)).getHour());
        Log.e("Labels", " hypnoData.get(i).getHour=" + localCalendar.getTime());
        if (localCalendar.get(12) == 0)
        {
          Log.e("Labels", " case 1 " + localCalendar.getTime());
        }
        else if ((paramInt <= 30) && (localCalendar.get(12) == 30))
        {
          Log.e("Labels", " case 2");
        }
        else
        {
          if ((paramInt > 15) || (localCalendar.get(12) != 15)) {
            break label635;
          }
          Log.e("Labels", " case 3");
        }
      }
    }
  }
  
  private void showExplanation(int paramInt1, int paramInt2)
  {
    getBaseActivity().showFullScreenDialog(new CustomDialogBuilder(getBaseActivity()).title(paramInt1).description(paramInt2).setPositiveButton(2131165296, null), false);
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903141, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    initData();
    return paramLayoutInflater;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SleepGraphFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
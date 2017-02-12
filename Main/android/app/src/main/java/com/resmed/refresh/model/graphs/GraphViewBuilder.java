package com.resmed.refresh.model.graphs;

import android.content.Context;
import android.graphics.Typeface;

import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.mappers.HypnoMapper;
import com.resmed.refresh.ui.customview.CustomBarChart;
import java.util.List;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.model.YLabel;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class GraphViewBuilder
{
  private static final int PADDING_GRAPH_END = 20;
  private BarChart barChart;
  private Context context;
  private int indexOnSet;
  
  public GraphViewBuilder(Context paramContext)
  {
    this.context = paramContext;
  }
  
  private GraphViewBuilder generateView(RST_SleepSessionInfo paramRST_SleepSessionInfo, RST_CallbackItem<Long> paramRST_CallbackItem)
  {
    int i = paramRST_SleepSessionInfo.getSleepStates().size() + 20;
    int j = i * -1 / 4;
    List localList = HypnoMapper.getHypnoData(paramRST_SleepSessionInfo);
    this.barChart = new CustomBarChart(getBarDemoDataset(localList), getBarDemoRenderer(paramRST_SleepSessionInfo, localList, j, i), this.indexOnSet);
    if (paramRST_CallbackItem != null) {
      paramRST_CallbackItem.onResult(new Long(paramRST_SleepSessionInfo.getId()));
    }
    return this;
  }
  
  private XYMultipleSeriesDataset getBarDemoDataset(List<RRHypnoData> paramList)
  {
    localXYSeries = new XYSeries("Sleep");
    for (;;)
    {
      try
      {
        localXYSeries.add(0.0D, 0.0D);
        i = 0;
        int j = paramList.size();
        if (i < j) {
          continue;
        }
      }
      catch (Exception paramList)
      {
        int i;
        double d;
        paramList.printStackTrace();
        continue;
        if (localXYSeries.getY(i) <= 0.0D) {
          continue;
        }
        this.indexOnSet = i;
        continue;
        i++;
        continue;
      }
      paramList = new XYMultipleSeriesDataset();
      paramList.addSeries(localXYSeries);
      this.indexOnSet = localXYSeries.getItemCount();
      i = 0;
      if (i < localXYSeries.getItemCount()) {
        continue;
      }
      return paramList;
      d = i;
      localXYSeries.add(d, ((RRHypnoData)paramList.get(i)).getValueBar());
      i++;
    }
  }
  
  private XYMultipleSeriesRenderer getBarDemoRenderer(RST_SleepSessionInfo paramRST_SleepSessionInfo, List<RRHypnoData> paramList, int paramInt1, int paramInt2)
  {
    paramList = this.context.getResources();
    XYSeriesRenderer localXYSeriesRenderer = new XYSeriesRenderer();
    localXYSeriesRenderer.setColor(paramList.getColor(2131296347));
    paramRST_SleepSessionInfo = new XYMultipleSeriesRenderer();
    paramRST_SleepSessionInfo.setXAxisMin(paramInt1);
    paramRST_SleepSessionInfo.setYAxisMin(-0.5D);
    paramRST_SleepSessionInfo.setYAxisMax(3.0D);
    paramRST_SleepSessionInfo.setXAxisMax(paramInt2);
    paramRST_SleepSessionInfo.setZoomEnabled(false, false);
    paramRST_SleepSessionInfo.setPanEnabled(false, false);
    paramRST_SleepSessionInfo.setShowLegend(false);
    paramRST_SleepSessionInfo.setInScroll(true);
    paramRST_SleepSessionInfo.setLabelsTextSize(paramList.getDimension(2131034204));
    paramRST_SleepSessionInfo.setBackgroundColor(paramList.getColor(2131296347));
    paramRST_SleepSessionInfo.setApplyBackgroundColor(true);
    paramRST_SleepSessionInfo.setMarginsColor(paramList.getColor(2131296347));
    paramInt1 = Math.round(paramList.getDimension(2131034180));
    paramRST_SleepSessionInfo.setMargins(new int[] { paramInt1, paramInt1, Math.round(paramList.getDimension(2131034230)), paramInt1 });
    paramRST_SleepSessionInfo.setYLabels(4);
    paramRST_SleepSessionInfo.setYLabelsEnabled(true);
    paramRST_SleepSessionInfo.setXLabels(0);
    paramRST_SleepSessionInfo.setXLabelsColor(paramList.getColor(2131296364));
    paramRST_SleepSessionInfo.setShowAxes(false);
    paramRST_SleepSessionInfo.setShowTickMarks(false);
    paramRST_SleepSessionInfo.setShowGrid(true);
    paramRST_SleepSessionInfo.setGridStroke(paramList.getDimension(2131034238));
    paramRST_SleepSessionInfo.setGridColor(paramList.getColor(2131296364));
    Typeface localTypeface = Typeface.createFromAsset(this.context.getAssets(), "AkzidenzGroteskBE-Light.otf");
    paramRST_SleepSessionInfo.addYLabel(Integer.valueOf(3), new YLabel(localTypeface, paramList.getString(2131166014), null, paramList.getColor(2131296362)));
    paramRST_SleepSessionInfo.addYLabel(Integer.valueOf(2), new YLabel(localTypeface, paramList.getString(2131166015), null, paramList.getColor(2131296361)));
    paramRST_SleepSessionInfo.addYLabel(Integer.valueOf(1), new YLabel(localTypeface, paramList.getString(2131166016), null, paramList.getColor(2131296360)));
    paramRST_SleepSessionInfo.addYLabel(Integer.valueOf(0), new YLabel(localTypeface, paramList.getString(2131166013), null, paramList.getColor(2131296359)));
    paramRST_SleepSessionInfo.addSeriesRenderer(localXYSeriesRenderer);
    return paramRST_SleepSessionInfo;
  }
  
  public GraphicalView getChartView()
  {
    if (this.barChart == null) {}
    for (GraphicalView localGraphicalView = null;; localGraphicalView = new GraphicalView(this.context, this.barChart)) {
      return localGraphicalView;
    }
  }
  
  public GraphViewBuilder getView(RST_SleepSessionInfo paramRST_SleepSessionInfo)
  {
    return generateView(paramRST_SleepSessionInfo, null);
  }
  
  public GraphViewBuilder getViewAsync(final RST_SleepSessionInfo paramRST_SleepSessionInfo, final RST_CallbackItem<Long> paramRST_CallbackItem)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        GraphViewBuilder.this.generateView(paramRST_SleepSessionInfo, paramRST_CallbackItem);
      }
    }).start();
    return this;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
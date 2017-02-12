package com.resmed.refresh.ui.customview;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.resmed.refresh.model.graphs.RRHypnoBar;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

public class CustomBarChart
  extends BarChart
{
  private static final long serialVersionUID = 1L;
  private int indexOnSet;
  
  public CustomBarChart(XYMultipleSeriesDataset paramXYMultipleSeriesDataset, XYMultipleSeriesRenderer paramXYMultipleSeriesRenderer, int paramInt)
  {
    super(paramXYMultipleSeriesDataset, paramXYMultipleSeriesRenderer, BarChart.Type.DEFAULT);
    this.indexOnSet = paramInt;
  }
  
  protected void drawBar(Canvas paramCanvas, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, int paramInt1, int paramInt2, Paint paramPaint)
  {
    double[] arrayOfDouble = toRealPoint(paramFloat3, paramFloat4);
    RRHypnoBar localRRHypnoBar = RRHypnoBar.fromValue((int)Math.round(arrayOfDouble[1]));
    if (arrayOfDouble[0] < this.indexOnSet) {}
    for (boolean bool = true;; bool = false)
    {
      paramPaint.setColor(RRHypnoBar.getColor(localRRHypnoBar, bool));
      super.drawBar(paramCanvas, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramInt1, paramInt2, paramPaint);
      return;
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\customview\CustomBarChart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
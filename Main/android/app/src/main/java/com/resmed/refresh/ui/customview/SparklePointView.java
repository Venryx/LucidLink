package com.resmed.refresh.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

public class SparklePointView
  extends View
{
  int[] colorsHalo;
  int[] colorsSparkle;
  float height;
  private Paint paintHalo;
  private Paint paintPoint;
  float[] positionsHalo;
  float[] positionsSparkle;
  private float radiusHaloMin;
  private float radiusPointMin;
  private final SparklePointView view = this;
  float width;
  
  public SparklePointView(Context paramContext)
  {
    super(paramContext);
    intiView();
  }
  
  public SparklePointView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    intiView();
  }
  
  public SparklePointView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    intiView();
  }
  
  private void intiView()
  {
    this.paintHalo = new Paint();
    this.paintHalo.setAntiAlias(true);
    this.paintHalo.setAlpha(128);
    this.paintHalo.setStrokeWidth(0.0F);
    this.paintHalo.setStyle(Paint.Style.FILL);
    this.paintHalo.setColor(getResources().getColor(2131296314));
    this.paintPoint = new Paint();
    this.paintPoint.setAntiAlias(true);
    this.paintPoint.setAlpha(128);
    this.paintPoint.setStrokeWidth(0.0F);
    this.paintPoint.setStyle(Paint.Style.FILL);
    this.paintPoint.setColor(getResources().getColor(2131296314));
    this.view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
    {
      public void onGlobalLayout()
      {
        if (Build.VERSION.SDK_INT < 16) {
          SparklePointView.this.view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        for (;;)
        {
          SparklePointView.this.height = (SparklePointView.this.view.getBottom() - SparklePointView.this.view.getTop());
          SparklePointView.this.width = (SparklePointView.this.view.getRight() - SparklePointView.this.view.getLeft());
          SparklePointView.this.radiusPointMin = SparklePointView.this.getResources().getDimension(2131034253);
          SparklePointView.this.radiusHaloMin = SparklePointView.this.getResources().getDimension(2131034255);
          int m = SparklePointView.this.getResources().getColor(2131296300);
          int j = SparklePointView.this.getResources().getColor(2131296301);
          int i = SparklePointView.this.getResources().getColor(2131296308);
          int i3 = SparklePointView.this.getResources().getColor(2131296308);
          int k = SparklePointView.this.getResources().getColor(2131296304);
          int i2 = SparklePointView.this.getResources().getColor(2131296306);
          int i1 = SparklePointView.this.getResources().getColor(2131296307);
          int n = SparklePointView.this.getResources().getColor(2131296308);
          float f3 = SparklePointView.this.height / 2.0F;
          float f2 = SparklePointView.this.width / 2.0F;
          float f1 = SparklePointView.this.radiusPointMin;
          Object localObject = Shader.TileMode.MIRROR;
          localObject = new RadialGradient(f3, f2, f1, new int[] { m, j, i, i3 }, new float[] { 0.0F, 0.66F, 1.0F, 1.0F }, (Shader.TileMode)localObject);
          SparklePointView.this.paintPoint.setShader((Shader)localObject);
          f2 = SparklePointView.this.height / 2.0F;
          f3 = SparklePointView.this.width / 2.0F;
          f1 = SparklePointView.this.radiusHaloMin;
          localObject = Shader.TileMode.MIRROR;
          localObject = new RadialGradient(f2, f3, f1, new int[] { k, i2, i1, n }, new float[] { 0.0F, 0.33F, 0.66F, 1.0F }, (Shader.TileMode)localObject);
          SparklePointView.this.paintHalo.setShader((Shader)localObject);
          return;
          SparklePointView.this.view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
      }
    });
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    if (!isInEditMode())
    {
      paramCanvas.drawCircle(this.height / 2.0F, this.width / 2.0F, this.radiusPointMin, this.paintPoint);
      paramCanvas.drawCircle(this.height / 2.0F, this.width / 2.0F, this.radiusHaloMin, this.paintHalo);
    }
    super.onDraw(paramCanvas);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\customview\SparklePointView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
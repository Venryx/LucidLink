package com.resmed.refresh.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

public class RoundRectangleView
  extends View
{
  private float bottom;
  private float height;
  private float left;
  private float padding;
  private Paint paint;
  private float rigth;
  private float top;
  private View view = this;
  
  public RoundRectangleView(Context paramContext)
  {
    super(paramContext);
    initView(paramContext);
  }
  
  public RoundRectangleView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView(paramContext);
  }
  
  public RoundRectangleView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initView(paramContext);
  }
  
  @SuppressLint({"NewApi"})
  private void initView(Context paramContext)
  {
    this.paint = new Paint();
    this.paint.setAntiAlias(true);
    this.paint.setStrokeWidth(5.0F);
    this.paint.setStyle(Paint.Style.STROKE);
    this.paint.setColor(-1);
    this.padding = paramContext.getResources().getDimension(2131034244);
    this.view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
    {
      public void onGlobalLayout()
      {
        if (Build.VERSION.SDK_INT < 16) {
          RoundRectangleView.this.view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        for (;;)
        {
          RoundRectangleView.this.left = RoundRectangleView.this.padding;
          RoundRectangleView.this.rigth = (RoundRectangleView.this.view.getWidth() - RoundRectangleView.this.padding);
          RoundRectangleView.this.top = RoundRectangleView.this.padding;
          RoundRectangleView.this.bottom = (RoundRectangleView.this.view.getHeight() - RoundRectangleView.this.padding);
          RoundRectangleView.this.height = (RoundRectangleView.this.bottom - RoundRectangleView.this.top);
          return;
          RoundRectangleView.this.view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
      }
    });
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    paramCanvas.drawRoundRect(new RectF(this.left, this.top, this.rigth, this.bottom), this.height / 2.0F, this.height / 2.0F, this.paint);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\customview\RoundRectangleView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
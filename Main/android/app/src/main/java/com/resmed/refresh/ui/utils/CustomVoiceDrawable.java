package com.resmed.refresh.ui.utils;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import com.resmed.refresh.utils.Log;

public class CustomVoiceDrawable
  extends Drawable
{
  private Double amplitude;
  private Paint mPaint = new Paint();
  
  public CustomVoiceDrawable(Double paramDouble)
  {
    this.mPaint.setStrokeWidth(3.0F);
    this.mPaint.setColor(-4473925);
    this.amplitude = paramDouble;
  }
  
  public void draw(Canvas paramCanvas)
  {
    Log.i("com.resmed.refresh.recorder", String.valueOf(this.amplitude));
    if (this.amplitude.doubleValue() < 20.0D) {
      paramCanvas.drawRoundRect(new RectF(50.0F, 90.0F, 75.0F, 135.0F), 10.5F, 10.5F, this.mPaint);
    }
    for (;;)
    {
      return;
      if (this.amplitude.doubleValue() < 40.0D) {
        paramCanvas.drawRoundRect(new RectF(50.0F, 75.0F, 75.0F, 150.0F), 10.5F, 10.5F, this.mPaint);
      } else if (this.amplitude.doubleValue() < 60.0D) {
        paramCanvas.drawRoundRect(new RectF(50.0F, 60.0F, 75.0F, 165.0F), 10.5F, 10.5F, this.mPaint);
      } else if (this.amplitude.doubleValue() < 80.0D) {
        paramCanvas.drawRoundRect(new RectF(50.0F, 45.0F, 75.0F, 180.0F), 10.5F, 10.5F, this.mPaint);
      } else {
        paramCanvas.drawRoundRect(new RectF(50.0F, 30.0F, 75.0F, 195.0F), 10.5F, 10.5F, this.mPaint);
      }
    }
  }
  
  public int getOpacity()
  {
    return -3;
  }
  
  public void setAlpha(int paramInt) {}
  
  public void setColorFilter(ColorFilter paramColorFilter) {}
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.ui.customview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

public class CustomSemiSliderSeekBarThumbDrawable
  extends Drawable
{
  private final Integer THUMB_SIZE = Integer.valueOf(13);
  private Paint circlePaint;
  private Context mContext;
  private float mRadius;
  
  public CustomSemiSliderSeekBarThumbDrawable(Context paramContext, int paramInt)
  {
    this.mContext = paramContext;
    this.mRadius = toPix(this.THUMB_SIZE.intValue());
    setColor(paramInt);
  }
  
  private float toPix(int paramInt)
  {
    return TypedValue.applyDimension(1, paramInt, this.mContext.getResources().getDisplayMetrics());
  }
  
  public final void draw(Canvas paramCanvas)
  {
    int i = getBounds().centerY();
    paramCanvas.drawCircle(getBounds().centerX() + this.mRadius, i, this.mRadius, this.circlePaint);
  }
  
  public int getIntrinsicHeight()
  {
    return (int)(this.mRadius * 2.0F);
  }
  
  public int getIntrinsicWidth()
  {
    return (int)(this.mRadius * 2.0F);
  }
  
  public final int getOpacity()
  {
    return -3;
  }
  
  public float getRadius()
  {
    return this.mRadius;
  }
  
  public final boolean isStateful()
  {
    return true;
  }
  
  protected final boolean onStateChange(int[] paramArrayOfInt)
  {
    invalidateSelf();
    return false;
  }
  
  public void setAlpha(int paramInt) {}
  
  public void setColor(int paramInt)
  {
    this.circlePaint = new Paint(1);
    this.circlePaint.setColor(-1);
    invalidateSelf();
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {}
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\customview\CustomSemiSliderSeekBarThumbDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
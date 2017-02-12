package com.resmed.refresh.ui.customview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import java.util.Iterator;
import java.util.List;

public class CustomSemiSliderSeekBarDrawable
  extends Drawable
{
  private final String COLOR_BACKGROUND_LINE = "#FF777777";
  private final String COLOR_PROGRESS_LINE = "#FFFFFFFF";
  private final Integer MARGIN_BOTTOM_TEXT = Integer.valueOf(25);
  private Paint circleLinePaint;
  private float mDotRadius;
  private List<CustomSemiSliderSeekBar.Dot> mDots;
  private boolean mIsMultiline = true;
  private int mPadding;
  private int mTextHeight;
  private float mTextMargin;
  private int mTextPositionY;
  private int mTextSize;
  private float mThumbRadius;
  private final Drawable myBase;
  private final CustomSemiSliderSeekBar mySlider;
  private Paint selectLinePaint;
  private Paint smallcirclepaint;
  private Paint textSelected;
  private final Paint unselectLinePaint;
  
  public CustomSemiSliderSeekBarDrawable(Drawable paramDrawable, CustomSemiSliderSeekBar paramCustomSemiSliderSeekBar, float paramFloat, List<CustomSemiSliderSeekBar.Dot> paramList, int paramInt1, int paramInt2, int paramInt3)
  {
    this.mySlider = paramCustomSemiSliderSeekBar;
    this.myBase = paramDrawable;
    this.mDots = paramList;
    this.mTextSize = paramInt2;
    this.mPadding = paramInt3;
    this.textSelected = new Paint(1);
    this.textSelected.setTypeface(Typeface.DEFAULT_BOLD);
    this.textSelected.setColor(paramInt1);
    this.textSelected.setAlpha(255);
    this.mThumbRadius = paramFloat;
    paramDrawable = RefreshApplication.getInstance().getResources();
    this.unselectLinePaint = new Paint();
    this.unselectLinePaint.setColor(Color.parseColor("#FF777777"));
    this.unselectLinePaint.setStrokeWidth(paramDrawable.getDimension(2131034237));
    this.selectLinePaint = new Paint();
    this.selectLinePaint.setColor(Color.parseColor("#FFFFFFFF"));
    this.selectLinePaint.setStrokeWidth(paramDrawable.getDimension(2131034237));
    this.circleLinePaint = new Paint(1);
    this.circleLinePaint.setColor(paramInt1);
    paramDrawable = new Rect();
    this.textSelected.setTextSize(this.mTextSize * 2);
    this.textSelected.getTextBounds("M", 0, 1, paramDrawable);
    this.textSelected.setTextSize(this.mTextSize);
    this.mTextHeight = paramDrawable.height();
    this.mDotRadius = toPix(5);
    this.mTextMargin = toPix(this.MARGIN_BOTTOM_TEXT.intValue());
    this.mTextPositionY = (getIntrinsicHeight() - this.mTextHeight / 2);
    this.smallcirclepaint = new Paint();
    this.smallcirclepaint.setStyle(Paint.Style.FILL);
    this.smallcirclepaint.setColor(-7829368);
  }
  
  private void drawPointer(Canvas paramCanvas, CustomSemiSliderSeekBar.Dot paramDot, float paramFloat1, float paramFloat2)
  {
    paramCanvas.drawCircle(paramFloat1, paramFloat2, 20.0F, this.smallcirclepaint);
  }
  
  private void drawText(Canvas paramCanvas, CustomSemiSliderSeekBar.Dot paramDot, float paramFloat1, float paramFloat2)
  {
    Rect localRect = new Rect();
    this.textSelected.getTextBounds(paramDot.text, 0, paramDot.text.length(), localRect);
    paramFloat2 = paramFloat1 - localRect.width() / 2;
    if ((paramDot.id == this.mDots.size() - 1) && (paramFloat2 > getBounds().width() - localRect.width() / 2 - this.mPadding)) {
      paramFloat1 = getBounds().width() - localRect.width() / 2 - this.mPadding;
    }
    for (;;)
    {
      paramCanvas.drawText(paramDot.text, paramFloat1, this.mTextPositionY, this.textSelected);
      return;
      paramFloat1 = paramFloat2;
      if (paramDot.id == 0)
      {
        paramFloat1 = paramFloat2;
        if (paramFloat2 < 0.0F) {
          paramFloat1 = 0.0F;
        }
      }
    }
  }
  
  private float toPix(int paramInt)
  {
    return TypedValue.applyDimension(1, paramInt, this.mySlider.getContext().getResources().getDisplayMetrics());
  }
  
  public final void draw(Canvas paramCanvas)
  {
    int i = getIntrinsicHeight() / 2;
    if (this.mDots.size() == 0)
    {
      paramCanvas.drawLine(0.0F, i, getBounds().right, i, this.unselectLinePaint);
      return;
    }
    Object localObject1 = this.mDots.iterator();
    for (;;)
    {
      if (!((Iterator)localObject1).hasNext())
      {
        localObject2 = this.mDots.iterator();
        while (((Iterator)localObject2).hasNext())
        {
          localObject1 = (CustomSemiSliderSeekBar.Dot)((Iterator)localObject2).next();
          drawPointer(paramCanvas, (CustomSemiSliderSeekBar.Dot)localObject1, ((CustomSemiSliderSeekBar.Dot)localObject1).mX, i);
        }
        break;
      }
      Object localObject2 = (CustomSemiSliderSeekBar.Dot)((Iterator)localObject1).next();
      drawText(paramCanvas, (CustomSemiSliderSeekBar.Dot)localObject2, ((CustomSemiSliderSeekBar.Dot)localObject2).mX, i);
      if (((CustomSemiSliderSeekBar.Dot)localObject2).isSelected)
      {
        paramCanvas.drawLine(((CustomSemiSliderSeekBar.Dot)this.mDots.get(0)).mX, i, ((CustomSemiSliderSeekBar.Dot)localObject2).mX, i, this.selectLinePaint);
        paramCanvas.drawLine(((CustomSemiSliderSeekBar.Dot)localObject2).mX, i, ((CustomSemiSliderSeekBar.Dot)this.mDots.get(this.mDots.size() - 1)).mX, i, this.unselectLinePaint);
      }
      else
      {
        paramCanvas.drawLine(((CustomSemiSliderSeekBar.Dot)localObject2).mX, i, ((CustomSemiSliderSeekBar.Dot)this.mDots.get(this.mDots.size() - 1)).mX, i, this.unselectLinePaint);
      }
    }
  }
  
  public final int getIntrinsicHeight()
  {
    if (this.mIsMultiline) {}
    for (int i = (int)(this.selectLinePaint.getStrokeWidth() + this.mDotRadius + this.mTextHeight * 2 + this.mTextMargin);; i = (int)(this.mThumbRadius + this.mTextMargin + this.mTextHeight + this.mDotRadius)) {
      return i;
    }
  }
  
  public final int getOpacity()
  {
    return -3;
  }
  
  public final boolean isStateful()
  {
    return true;
  }
  
  protected final void onBoundsChange(Rect paramRect)
  {
    this.myBase.setBounds(paramRect);
  }
  
  protected final boolean onStateChange(int[] paramArrayOfInt)
  {
    invalidateSelf();
    return false;
  }
  
  public void setAlpha(int paramInt) {}
  
  public void setBackgroundLineColor(int paramInt)
  {
    this.unselectLinePaint.setColor(paramInt);
    invalidateSelf();
  }
  
  public void setColorFilter(ColorFilter paramColorFilter) {}
  
  public void setProgressLineColor(int paramInt)
  {
    this.selectLinePaint.setColor(paramInt);
    invalidateSelf();
  }
  
  public void setTypeface(Typeface paramTypeface)
  {
    this.textSelected.setTypeface(paramTypeface);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\customview\CustomSemiSliderSeekBarDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
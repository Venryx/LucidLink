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

public class CustomSeekBarDrawable
  extends Drawable
{
  private final String COLOR_BACKGROUND_LINE = "#FF777777";
  private final String COLOR_PROGRESS_LINE = "#FFFFFFFF";
  private final Integer MARGIN_BOTTOM_TEXT = Integer.valueOf(25);
  private Paint circleLinePaint;
  private Paint gray_smallcirclepaint;
  private float mDotRadius;
  private List<CustomSeekBar.Dot> mDots;
  private boolean mIsMultiline = true;
  private int mPadding;
  private int mTextHeight;
  private float mTextMargin;
  private int mTextPositionY;
  private int mTextSize;
  private float mThumbRadius;
  private final Drawable myBase;
  private final CustomSeekBar mySlider;
  private Paint selectLinePaint;
  private Paint textSelected;
  private final Paint unselectLinePaint;
  private Paint white_smallcirclepaint;
  
  public CustomSeekBarDrawable(Drawable paramDrawable, CustomSeekBar paramCustomSeekBar, float paramFloat, List<CustomSeekBar.Dot> paramList, int paramInt1, int paramInt2, int paramInt3)
  {
    this.mySlider = paramCustomSeekBar;
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
    this.gray_smallcirclepaint = new Paint();
    this.gray_smallcirclepaint.setStyle(Paint.Style.FILL);
    this.gray_smallcirclepaint.setColor(-7829368);
    this.white_smallcirclepaint = new Paint();
    this.white_smallcirclepaint.setStyle(Paint.Style.FILL);
    this.white_smallcirclepaint.setColor(-1);
  }
  
  private void drawPointer(Canvas paramCanvas, CustomSeekBar.Dot paramDot, float paramFloat1, float paramFloat2, Paint paramPaint)
  {
    paramCanvas.drawCircle(paramFloat1, paramFloat2, 20.0F, paramPaint);
  }
  
  private void drawText(Canvas paramCanvas, CustomSeekBar.Dot paramDot, float paramFloat1, float paramFloat2)
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
    label40:
    Iterator localIterator;
    if (this.mDots.size() == 0)
    {
      paramCanvas.drawLine(0.0F, i, getBounds().right, i, this.unselectLinePaint);
      return;
    }
    else
    {
      localIterator = this.mDots.iterator();
      label51:
      if (localIterator.hasNext()) {
        break label139;
      }
      localIterator = this.mDots.iterator();
    }
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        localIterator = this.mDots.iterator();
        if (!localIterator.hasNext()) {
          break label40;
        }
        localDot = (CustomSeekBar.Dot)localIterator.next();
        drawPointer(paramCanvas, localDot, localDot.mX, i, this.white_smallcirclepaint);
        if (!localDot.isSelected) {
          break;
        }
        break label40;
        label139:
        localDot = (CustomSeekBar.Dot)localIterator.next();
        drawText(paramCanvas, localDot, localDot.mX, i);
        if (!localDot.isSelected) {
          break label51;
        }
        paramCanvas.drawLine(((CustomSeekBar.Dot)this.mDots.get(0)).mX, i, localDot.mX, i, this.selectLinePaint);
        paramCanvas.drawLine(localDot.mX, i, ((CustomSeekBar.Dot)this.mDots.get(this.mDots.size() - 1)).mX, i, this.unselectLinePaint);
        break label51;
      }
      CustomSeekBar.Dot localDot = (CustomSeekBar.Dot)localIterator.next();
      drawPointer(paramCanvas, localDot, localDot.mX, i, this.gray_smallcirclepaint);
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


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\customview\CustomSeekBarDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
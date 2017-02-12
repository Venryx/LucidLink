package com.resmed.refresh.ui.customview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomSemiSliderSeekBar
  extends SeekBar
{
  private boolean isSelected = false;
  private int mColor = -1;
  private List<Dot> mDots = new ArrayList();
  private AdapterView.OnItemClickListener mItemClickListener;
  private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener;
  private int mPadding = 0;
  private int mTextSize;
  private CustomSemiSliderSeekBarThumbDrawable mThumb;
  private Dot prevSelected = null;
  
  public CustomSemiSliderSeekBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mTextSize = ((int)paramContext.getResources().getDimension(2131034203));
    this.mThumb = new CustomSemiSliderSeekBarThumbDrawable(paramContext, this.mColor);
    setThumb(this.mThumb);
    this.mPadding = getPaddingLeft();
    setPadding(0, 0, 0, 0);
    setProgressDrawable(new CustomSemiSliderSeekBarDrawable(getProgressDrawable(), this, this.mThumb.getRadius(), this.mDots, this.mColor, this.mTextSize, this.mPadding));
  }
  
  private void handleClick(Dot paramDot)
  {
    if ((this.prevSelected == null) || (!this.prevSelected.equals(paramDot)))
    {
      if (this.mItemClickListener != null) {
        this.mItemClickListener.onItemClick(null, this, paramDot.id, paramDot.id);
      }
      this.prevSelected = paramDot;
    }
    if (this.mOnSeekBarChangeListener != null) {
      this.mOnSeekBarChangeListener.onProgressChanged(this, getPositionSelected(), true);
    }
  }
  
  private void initDotsCoordinates()
  {
    float f = (getWidth() - this.mThumb.getRadius() * 2.0F - this.mPadding * 2) / (this.mDots.size() - 1);
    Iterator localIterator = this.mDots.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      Dot localDot = (Dot)localIterator.next();
      localDot.mX = ((int)(this.mThumb.getRadius() + localDot.id * f + this.mPadding));
    }
  }
  
  public int getPositionSelected()
  {
    for (int i = 0;; i++)
    {
      int j;
      if (i >= this.mDots.size()) {
        j = 0;
      }
      do
      {
        return j;
        j = i;
      } while (this.mDots.get(i) == this.prevSelected);
    }
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    try
    {
      Object localObject2;
      if ((this.mThumb != null) && (this.mDots.size() > 1))
      {
        if (!this.isSelected) {
          break label124;
        }
        localObject2 = this.mDots.iterator();
        if (((Iterator)localObject2).hasNext()) {
          break label58;
        }
      }
      for (;;)
      {
        super.onDraw(paramCanvas);
        return;
        label58:
        localObject1 = (Dot)((Iterator)localObject2).next();
        if (!((Dot)localObject1).isSelected) {
          break;
        }
        localObject2 = this.mThumb.copyBounds();
        ((Rect)localObject2).right = ((Dot)localObject1).mX;
        ((Rect)localObject2).left = ((Dot)localObject1).mX;
        this.mThumb.setBounds((Rect)localObject2);
      }
      k = ((Dot)this.mDots.get(1)).mX;
    }
    finally {}
    label124:
    int k;
    int j = ((Dot)this.mDots.get(0)).mX;
    Object localObject1 = this.mThumb.copyBounds();
    if (((Dot)this.mDots.get(this.mDots.size() - 1)).mX - ((Rect)localObject1).centerX() < 0)
    {
      ((Rect)localObject1).right = ((Dot)this.mDots.get(this.mDots.size() - 1)).mX;
      ((Rect)localObject1).left = ((Dot)this.mDots.get(this.mDots.size() - 1)).mX;
      this.mThumb.setBounds((Rect)localObject1);
      localObject1 = this.mDots.iterator();
      for (;;)
      {
        if (!((Iterator)localObject1).hasNext())
        {
          ((Dot)this.mDots.get(this.mDots.size() - 1)).isSelected = true;
          handleClick((Dot)this.mDots.get(this.mDots.size() - 1));
          break;
        }
        ((Dot)((Iterator)localObject1).next()).isSelected = false;
      }
    }
    int i = 0;
    label371:
    if (i < this.mDots.size())
    {
      if (Math.abs(((Dot)this.mDots.get(i)).mX - ((Rect)localObject1).centerX()) > (k - j) / 2) {
        break label509;
      }
      ((Rect)localObject1).right = ((Dot)this.mDots.get(i)).mX;
      ((Rect)localObject1).left = ((Dot)this.mDots.get(i)).mX;
      this.mThumb.setBounds((Rect)localObject1);
      ((Dot)this.mDots.get(i)).isSelected = true;
      handleClick((Dot)this.mDots.get(i));
    }
    for (;;)
    {
      i++;
      break label371;
      break;
      label509:
      ((Dot)this.mDots.get(i)).isSelected = false;
    }
  }
  
  /* Error */
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual 83	com/resmed/refresh/ui/customview/CustomSemiSliderSeekBar:getProgressDrawable	()Landroid/graphics/drawable/Drawable;
    //   6: checkcast 79	com/resmed/refresh/ui/customview/CustomSemiSliderSeekBarDrawable
    //   9: astore 9
    //   11: aload_0
    //   12: getfield 65	com/resmed/refresh/ui/customview/CustomSemiSliderSeekBar:mThumb	Lcom/resmed/refresh/ui/customview/CustomSemiSliderSeekBarThumbDrawable;
    //   15: ifnonnull +88 -> 103
    //   18: iconst_0
    //   19: istore 5
    //   21: iconst_0
    //   22: istore_3
    //   23: iconst_0
    //   24: istore 4
    //   26: aload 9
    //   28: ifnull +21 -> 49
    //   31: aload 9
    //   33: invokevirtual 189	com/resmed/refresh/ui/customview/CustomSemiSliderSeekBarDrawable:getIntrinsicWidth	()I
    //   36: istore_3
    //   37: iload 5
    //   39: aload 9
    //   41: invokevirtual 192	com/resmed/refresh/ui/customview/CustomSemiSliderSeekBarDrawable:getIntrinsicHeight	()I
    //   44: invokestatic 196	java/lang/Math:max	(II)I
    //   47: istore 4
    //   49: aload_0
    //   50: invokevirtual 73	com/resmed/refresh/ui/customview/CustomSemiSliderSeekBar:getPaddingLeft	()I
    //   53: istore 7
    //   55: aload_0
    //   56: invokevirtual 199	com/resmed/refresh/ui/customview/CustomSemiSliderSeekBar:getPaddingRight	()I
    //   59: istore 5
    //   61: aload_0
    //   62: invokevirtual 202	com/resmed/refresh/ui/customview/CustomSemiSliderSeekBar:getPaddingTop	()I
    //   65: istore 6
    //   67: aload_0
    //   68: invokevirtual 205	com/resmed/refresh/ui/customview/CustomSemiSliderSeekBar:getPaddingBottom	()I
    //   71: istore 8
    //   73: aload_0
    //   74: iload_3
    //   75: iload 7
    //   77: iload 5
    //   79: iadd
    //   80: iadd
    //   81: iload_1
    //   82: invokestatic 208	com/resmed/refresh/ui/customview/CustomSemiSliderSeekBar:resolveSize	(II)I
    //   85: iload 4
    //   87: iload 6
    //   89: iload 8
    //   91: iadd
    //   92: iadd
    //   93: iload_2
    //   94: invokestatic 208	com/resmed/refresh/ui/customview/CustomSemiSliderSeekBar:resolveSize	(II)I
    //   97: invokevirtual 211	com/resmed/refresh/ui/customview/CustomSemiSliderSeekBar:setMeasuredDimension	(II)V
    //   100: aload_0
    //   101: monitorexit
    //   102: return
    //   103: aload_0
    //   104: getfield 65	com/resmed/refresh/ui/customview/CustomSemiSliderSeekBar:mThumb	Lcom/resmed/refresh/ui/customview/CustomSemiSliderSeekBarThumbDrawable;
    //   107: invokevirtual 212	com/resmed/refresh/ui/customview/CustomSemiSliderSeekBarThumbDrawable:getIntrinsicHeight	()I
    //   110: istore 5
    //   112: goto -91 -> 21
    //   115: astore 9
    //   117: aload_0
    //   118: monitorexit
    //   119: aload 9
    //   121: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	122	0	this	CustomSemiSliderSeekBar
    //   0	122	1	paramInt1	int
    //   0	122	2	paramInt2	int
    //   22	59	3	i	int
    //   24	69	4	j	int
    //   19	92	5	k	int
    //   65	27	6	m	int
    //   53	27	7	n	int
    //   71	21	8	i1	int
    //   9	31	9	localCustomSemiSliderSeekBarDrawable	CustomSemiSliderSeekBarDrawable
    //   115	5	9	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   2	18	115	finally
    //   31	49	115	finally
    //   49	100	115	finally
    //   103	112	115	finally
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    initDotsCoordinates();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    this.isSelected = false;
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public void setAdapter(List<String> paramList)
  {
    this.mDots.clear();
    int i = 0;
    Iterator localIterator = paramList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        initDotsCoordinates();
        return;
      }
      paramList = (String)localIterator.next();
      Dot localDot = new Dot();
      localDot.text = paramList;
      localDot.id = i;
      this.mDots.add(localDot);
      i++;
    }
  }
  
  public void setBackgroundLineColor(int paramInt)
  {
    ((CustomSemiSliderSeekBarDrawable)getProgressDrawable()).setBackgroundLineColor(paramInt);
  }
  
  public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener)
  {
    this.mItemClickListener = paramOnItemClickListener;
  }
  
  public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener paramOnSeekBarChangeListener)
  {
    this.mOnSeekBarChangeListener = paramOnSeekBarChangeListener;
  }
  
  public void setProgressLineColor(int paramInt)
  {
    ((CustomSemiSliderSeekBarDrawable)getProgressDrawable()).setProgressLineColor(paramInt);
  }
  
  public void setSelection(int paramInt)
  {
    int i = paramInt;
    if (paramInt < 0) {
      i = 0;
    }
    paramInt = i;
    for (;;)
    {
      try
      {
        if (i >= this.mDots.size()) {
          paramInt = this.mDots.size() - 1;
        }
        Iterator localIterator = this.mDots.iterator();
        if (!localIterator.hasNext())
        {
          this.isSelected = true;
          invalidate();
          return;
        }
        Dot localDot = (Dot)localIterator.next();
        if (localDot.id == paramInt)
        {
          localDot.isSelected = true;
          this.prevSelected = localDot;
        }
        else
        {
          ((Dot)localObject).isSelected = false;
        }
      }
      finally {}
    }
  }
  
  public void setThumb(Drawable paramDrawable)
  {
    if ((paramDrawable instanceof CustomSemiSliderSeekBarThumbDrawable)) {
      this.mThumb = ((CustomSemiSliderSeekBarThumbDrawable)paramDrawable);
    }
    super.setThumb(paramDrawable);
  }
  
  public void setTypeface(Typeface paramTypeface)
  {
    ((CustomSemiSliderSeekBarDrawable)getProgressDrawable()).setTypeface(paramTypeface);
  }
  
  public static class Dot
  {
    public int id;
    public boolean isSelected = false;
    public int mX;
    public String text;
    
    public boolean equals(Object paramObject)
    {
      if (((Dot)paramObject).id == this.id) {}
      for (boolean bool = true;; bool = false) {
        return bool;
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\customview\CustomSemiSliderSeekBar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
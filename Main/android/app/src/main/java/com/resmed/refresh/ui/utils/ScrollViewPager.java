package com.resmed.refresh.ui.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import java.lang.reflect.Field;

public class ScrollViewPager
  extends ViewPager
{
  private static final int TIME_ANIMATION = 500;
  
  public ScrollViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setMyScroller();
  }
  
  private void setMyScroller()
  {
    try
    {
      Field localField = ViewPager.class.getDeclaredField("mScroller");
      localField.setAccessible(true);
      MyScroller localMyScroller = new com/resmed/refresh/ui/utils/ScrollViewPager$MyScroller;
      localMyScroller.<init>(this, getContext());
      localField.set(this, localMyScroller);
      return;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        localException.printStackTrace();
      }
    }
  }
  
  public class MyScroller
    extends Scroller
  {
    public MyScroller(Context paramContext)
    {
      super(new DecelerateInterpolator());
    }
    
    public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      super.startScroll(paramInt1, paramInt2, paramInt3, paramInt4, 500);
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
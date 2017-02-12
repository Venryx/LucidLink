package com.resmed.refresh.ui.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import com.resmed.refresh.utils.Log;

public class InfiniteViewPager
  extends ScrollViewPager
{
  public static final String ADAPTER_STATE = "adapter_state";
  public static boolean DEBUG = true;
  public static final int PAGE_COUNT = 3;
  public static final int PAGE_POSITION_CENTER = 1;
  public static final int PAGE_POSITION_LEFT = 0;
  public static final int PAGE_POSITION_RIGHT = 2;
  public static final String SUPER_STATE = "super_state";
  private int mCurrPosition = 1;
  private OnInfinitePageChangeListener mListener;
  
  public InfiniteViewPager(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public InfiniteViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  private void initInfiniteViewPager()
  {
    setCurrentItem(1);
    setOnPageChangeListener(new ViewPager.OnPageChangeListener()
    {
      public void onPageScrollStateChanged(int paramAnonymousInt)
      {
        for (;;)
        {
          try
          {
            if (InfiniteViewPager.this.mListener != null) {
              InfiniteViewPager.this.mListener.onPageScrollStateChanged(paramAnonymousInt);
            }
            InfinitePagerAdapter localInfinitePagerAdapter = (InfinitePagerAdapter)InfiniteViewPager.this.getAdapter();
            if (localInfinitePagerAdapter == null) {
              return;
            }
            if (paramAnonymousInt != 0) {
              continue;
            }
            if (InfiniteViewPager.this.mCurrPosition == 0)
            {
              localInfinitePagerAdapter.movePageContents(1, 2);
              localInfinitePagerAdapter.movePageContents(0, 1);
              localInfinitePagerAdapter.setCurrentIndicator(localInfinitePagerAdapter.getPreviousIndicator());
              localInfinitePagerAdapter.fillPage(0);
              InfiniteViewPager.this.setCurrentItem(1, false);
              if ((InfiniteViewPager.this.mListener == null) || (InfiniteViewPager.this.getAdapter() == null)) {
                continue;
              }
              InfiniteViewPager.this.mListener.onPageDisplayed(localInfinitePagerAdapter.getCurrentIndicator());
              continue;
            }
            if (InfiniteViewPager.this.mCurrPosition != 2) {
              continue;
            }
          }
          finally {}
          ((InfinitePagerAdapter)localObject).movePageContents(1, 0);
          ((InfinitePagerAdapter)localObject).movePageContents(2, 1);
          ((InfinitePagerAdapter)localObject).setCurrentIndicator(((InfinitePagerAdapter)localObject).getNextIndicator());
          ((InfinitePagerAdapter)localObject).fillPage(2);
        }
      }
      
      public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2)
      {
        InfinitePagerAdapter localInfinitePagerAdapter;
        if ((InfiniteViewPager.this.mListener != null) && (InfiniteViewPager.this.getAdapter() != null))
        {
          localInfinitePagerAdapter = (InfinitePagerAdapter)InfiniteViewPager.this.getAdapter();
          if (paramAnonymousInt1 != InfiniteViewPager.this.mCurrPosition) {
            break label66;
          }
        }
        label66:
        for (paramAnonymousInt1 = 1;; paramAnonymousInt1 = -1)
        {
          InfiniteViewPager.this.mListener.onPageScrolled(localInfinitePagerAdapter.getCurrentIndicator(), paramAnonymousFloat, paramAnonymousInt2, paramAnonymousInt1);
          return;
        }
      }
      
      public void onPageSelected(int paramAnonymousInt)
      {
        InfiniteViewPager.this.mCurrPosition = paramAnonymousInt;
        if (InfiniteViewPager.DEBUG) {
          Log.w("com.resmed.refresh.ui", "InfiniteViewPager on page " + paramAnonymousInt);
        }
        if ((InfiniteViewPager.this.mListener != null) && (InfiniteViewPager.this.getAdapter() != null))
        {
          InfinitePagerAdapter localInfinitePagerAdapter = (InfinitePagerAdapter)InfiniteViewPager.this.getAdapter();
          InfiniteViewPager.this.mListener.onPageSelected(localInfinitePagerAdapter.getCurrentIndicator());
        }
      }
    });
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    InfinitePagerAdapter localInfinitePagerAdapter = (InfinitePagerAdapter)getAdapter();
    if (localInfinitePagerAdapter == null)
    {
      if (DEBUG) {
        Log.w("com.resmed.refresh.ui", "InfiniteViewPager onRestoreInstanceState adapter == null");
      }
      super.onRestoreInstanceState(paramParcelable);
    }
    for (;;)
    {
      return;
      if ((paramParcelable instanceof Bundle))
      {
        paramParcelable = (Bundle)paramParcelable;
        localInfinitePagerAdapter.setCurrentIndicator(localInfinitePagerAdapter.convertToIndicator(paramParcelable.getString("adapter_state")));
        if ((this.mListener != null) && (getAdapter() != null)) {
          this.mListener.onPageDisplayed(localInfinitePagerAdapter.getCurrentIndicator());
        }
        super.onRestoreInstanceState(paramParcelable.getParcelable("super_state"));
      }
      else
      {
        super.onRestoreInstanceState(paramParcelable);
      }
    }
  }
  
  public Parcelable onSaveInstanceState()
  {
    InfinitePagerAdapter localInfinitePagerAdapter = (InfinitePagerAdapter)getAdapter();
    Object localObject;
    if (localInfinitePagerAdapter == null)
    {
      Log.d("com.resmed.refresh.ui", "InfiniteViewPager onSaveInstanceState adapter == null");
      localObject = super.onSaveInstanceState();
    }
    for (;;)
    {
      return (Parcelable)localObject;
      localObject = new Bundle();
      ((Bundle)localObject).putParcelable("super_state", super.onSaveInstanceState());
      ((Bundle)localObject).putString("adapter_state", localInfinitePagerAdapter.getStringRepresentation(localInfinitePagerAdapter.getCurrentIndicator()));
    }
  }
  
  public void setAdapter(PagerAdapter paramPagerAdapter)
  {
    if ((paramPagerAdapter instanceof InfinitePagerAdapter))
    {
      super.setAdapter(paramPagerAdapter);
      initInfiniteViewPager();
      return;
    }
    throw new IllegalArgumentException("Adapter should be an instance of InfinitePagerAdapter.");
  }
  
  public final void setCurrentIndicator(Object paramObject)
  {
    Object localObject = getAdapter();
    if (localObject == null) {}
    do
    {
      return;
      localObject = (InfinitePagerAdapter)localObject;
    } while (((InfinitePagerAdapter)localObject).getCurrentIndicator().getClass() != paramObject.getClass());
    ((InfinitePagerAdapter)localObject).reset();
    ((InfinitePagerAdapter)localObject).setCurrentIndicator(paramObject);
    for (int i = 0;; i++)
    {
      if (i >= 3)
      {
        if (this.mListener == null) {
          break;
        }
        this.mListener.onPageDisplayed(paramObject);
        break;
      }
      ((InfinitePagerAdapter)localObject).fillPage(i);
    }
  }
  
  public final void setCurrentItem(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 2)) {
      throw new RuntimeException("Cannot change page index. Only 0, 1 and 2 is allowed.");
    }
    super.setCurrentItem(paramInt);
  }
  
  public final void setOffscreenPageLimit(int paramInt)
  {
    if (paramInt != getOffscreenPageLimit()) {
      throw new RuntimeException("OffscreenPageLimit cannot be changed.");
    }
    super.setOffscreenPageLimit(paramInt);
  }
  
  public void setOnInfinitePageChangeListener(OnInfinitePageChangeListener paramOnInfinitePageChangeListener)
  {
    this.mListener = paramOnInfinitePageChangeListener;
  }
  
  public void showNext()
  {
    super.setCurrentItem(2, true);
  }
  
  public void showPrev()
  {
    super.setCurrentItem(0, true);
  }
  
  public static abstract interface OnInfinitePageChangeListener
  {
    public static final int TO_LEFT = -1;
    public static final int TO_RIGTH = 1;
    
    public abstract void onPageDisplayed(Object paramObject);
    
    public abstract void onPageScrollStateChanged(int paramInt);
    
    public abstract void onPageScrolled(Object paramObject, float paramFloat, int paramInt1, int paramInt2);
    
    public abstract void onPageSelected(Object paramObject);
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
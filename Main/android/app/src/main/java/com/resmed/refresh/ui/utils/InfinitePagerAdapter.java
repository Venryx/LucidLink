package com.resmed.refresh.ui.utils;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.resmed.refresh.utils.Log;
import java.util.Iterator;

public abstract class InfinitePagerAdapter<T>
  extends PagerAdapter
{
  public static final String LOG_TAG = "InfiniteViewPager";
  private T mCurrentIndicator;
  private InfinitePageModel<T>[] mPageModels;
  
  public InfinitePagerAdapter(T paramT)
  {
    this.mCurrentIndicator = paramT;
    this.mPageModels = new InfinitePageModel[3];
  }
  
  private InfinitePageModel<T> createPageModel(int paramInt)
  {
    Object localObject = getIndicatorFromPagePosition(paramInt);
    return new InfinitePageModel(instantiateItem(localObject), localObject);
  }
  
  private T getIndicatorFromPagePosition(int paramInt)
  {
    Object localObject = null;
    switch (paramInt)
    {
    }
    for (;;)
    {
      return (T)localObject;
      localObject = getPreviousIndicator();
      continue;
      localObject = getCurrentIndicator();
      continue;
      localObject = getNextIndicator();
    }
  }
  
  private void printPageModel(String paramString, InfinitePageModel paramInfinitePageModel, int paramInt)
  {
    paramString = String.format("%s: ModelPos %s, indicator %s, Childcount %s viewChildCount %s tag %s", new Object[] { paramString, Integer.valueOf(paramInt), paramInfinitePageModel.getIndicator(), Integer.valueOf(paramInfinitePageModel.getChildren().size()), Integer.valueOf(paramInfinitePageModel.getParentView().getChildCount()), paramInfinitePageModel.getParentView().getTag() });
    Log.d("com.resmed.refresh", "InfinitePageAdapter " + paramString);
  }
  
  private void printPageModels(String paramString)
  {
    for (int i = 0;; i++)
    {
      if (i >= 3) {
        return;
      }
      printPageModel(paramString, this.mPageModels[i], i);
    }
  }
  
  public abstract int compareIndicator(T paramT1, T paramT2);
  
  public T convertToIndicator(String paramString)
  {
    return (T)getCurrentIndicator();
  }
  
  public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
  {
    paramViewGroup.removeView(((InfinitePageModel)paramObject).getParentView());
  }
  
  public abstract boolean equalsIndicators(T paramT1, T paramT2);
  
  void fillPage(int paramInt)
  {
    if (InfiniteViewPager.DEBUG)
    {
      Log.d("com.resmed.refresh", "GrapView setup Page " + paramInt);
      printPageModels("before newPage");
    }
    InfinitePageModel localInfinitePageModel2 = this.mPageModels[paramInt];
    InfinitePageModel localInfinitePageModel1 = createPageModel(paramInt);
    if ((localInfinitePageModel2 == null) || (localInfinitePageModel1 == null))
    {
      Log.d("com.resmed.refresh", "GrapView fillPage no model found " + localInfinitePageModel2 + " " + localInfinitePageModel1);
      return;
    }
    localInfinitePageModel2.removeAllChildren();
    Iterator localIterator = localInfinitePageModel1.getChildren().iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        this.mPageModels[paramInt].setIndicator(localInfinitePageModel1.getIndicator());
        break;
      }
      View localView = (View)localIterator.next();
      localInfinitePageModel1.removeViewFromParent(localView);
      localInfinitePageModel2.addChild(localView);
    }
  }
  
  public final int getCount()
  {
    return 3;
  }
  
  public final T getCurrentIndicator()
  {
    return (T)this.mCurrentIndicator;
  }
  
  public abstract T getNextIndicator();
  
  public abstract T getPreviousIndicator();
  
  public String getStringRepresentation(T paramT)
  {
    return "";
  }
  
  public ViewGroup getViewForIndicator(T paramT)
  {
    Object localObject2 = null;
    for (int i = 0;; i++)
    {
      Object localObject1;
      if (i >= this.mPageModels.length) {
        localObject1 = localObject2;
      }
      for (;;)
      {
        return (ViewGroup)localObject1;
        localObject1 = localObject2;
        if (this.mPageModels[i] != null)
        {
          if (!equalsIndicators(this.mPageModels[i].getIndicator(), paramT)) {
            break;
          }
          localObject1 = this.mPageModels[i].getParentView();
        }
      }
    }
  }
  
  public abstract ViewGroup instantiateItem(T paramT);
  
  public final Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
  {
    if (InfiniteViewPager.DEBUG) {
      Log.d("com.resmed.refresh", "GrapView " + String.format("instantiating position %s", new Object[] { Integer.valueOf(paramInt) }));
    }
    InfinitePageModel localInfinitePageModel = createPageModel(paramInt);
    this.mPageModels[paramInt] = localInfinitePageModel;
    paramViewGroup.addView(localInfinitePageModel.getParentView());
    return localInfinitePageModel;
  }
  
  public final boolean isViewFromObject(View paramView, Object paramObject)
  {
    if (paramView == ((InfinitePageModel)paramObject).getParentView()) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  void movePageContents(int paramInt1, int paramInt2)
  {
    InfinitePageModel localInfinitePageModel1 = this.mPageModels[paramInt1];
    InfinitePageModel localInfinitePageModel2 = this.mPageModels[paramInt2];
    if ((localInfinitePageModel1 == null) || (localInfinitePageModel2 == null))
    {
      Log.d("com.resmed.refresh", "InfinitePageAdapter fillPage no model found " + localInfinitePageModel1 + " " + localInfinitePageModel2);
      return;
    }
    if (InfiniteViewPager.DEBUG)
    {
      Log.d("com.resmed.refresh", "InfinitePageAdapter " + String.format("Moving page %s to %s, indicator from %s to %s", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), localInfinitePageModel1.getIndicator(), localInfinitePageModel2.getIndicator() }));
      printPageModels("before");
    }
    localInfinitePageModel2.removeAllChildren();
    Iterator localIterator = localInfinitePageModel1.getChildren().iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        if (InfiniteViewPager.DEBUG) {
          printPageModels("transfer");
        }
        this.mPageModels[paramInt2].setIndicator(localInfinitePageModel1.getIndicator());
        if (!InfiniteViewPager.DEBUG) {
          break;
        }
        printPageModels("after");
        break;
      }
      View localView = (View)localIterator.next();
      localInfinitePageModel1.removeViewFromParent(localView);
      localInfinitePageModel2.addChild(localView);
    }
  }
  
  void reset()
  {
    InfinitePageModel[] arrayOfInfinitePageModel = this.mPageModels;
    int j = arrayOfInfinitePageModel.length;
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return;
      }
      arrayOfInfinitePageModel[i].removeAllChildren();
    }
  }
  
  void setCurrentIndicator(T paramT)
  {
    this.mCurrentIndicator = paramT;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\utils\InfinitePagerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
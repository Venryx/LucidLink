package com.resmed.refresh.ui.utils;

import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public final class InfinitePageModel<T>
{
  private List<View> mChildren;
  private T mIndicator;
  private ViewGroup mParentView;
  
  public InfinitePageModel(ViewGroup paramViewGroup, T paramT)
  {
    this.mParentView = paramViewGroup;
    this.mIndicator = paramT;
    int j = paramViewGroup.getChildCount();
    this.mChildren = new ArrayList(j);
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return;
      }
      this.mChildren.add(paramViewGroup.getChildAt(i));
    }
  }
  
  private void emptyChildren()
  {
    if (hasChildren()) {
      this.mChildren.clear();
    }
  }
  
  public void addChild(View paramView)
  {
    addViewToParent(paramView);
    this.mChildren.add(paramView);
  }
  
  public void addViewToParent(View paramView)
  {
    this.mParentView.addView(paramView);
  }
  
  public List<View> getChildren()
  {
    return this.mChildren;
  }
  
  public T getIndicator()
  {
    return (T)this.mIndicator;
  }
  
  public ViewGroup getParentView()
  {
    return this.mParentView;
  }
  
  public boolean hasChildren()
  {
    if ((this.mChildren != null) && (this.mChildren.size() != 0)) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public void removeAllChildren()
  {
    this.mParentView.removeAllViews();
    emptyChildren();
  }
  
  public void removeViewFromParent(View paramView)
  {
    this.mParentView.removeView(paramView);
  }
  
  public void setIndicator(T paramT)
  {
    this.mIndicator = paramT;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
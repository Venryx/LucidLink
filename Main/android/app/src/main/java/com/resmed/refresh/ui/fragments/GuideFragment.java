package com.resmed.refresh.ui.fragments;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.resmed.refresh.ui.activity.GuideActivty;
import com.resmed.refresh.ui.adapters.GuidePageAdapter;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.viewpagerindicator.CirclePageIndicator;

public class GuideFragment
  extends BaseFragment
  implements ViewPager.OnPageChangeListener
{
  private CirclePageIndicator cpiGuideTour;
  private ViewPager viewPager;
  private PagerAdapter viewPagerAdapter;
  
  private void mapGUI(View paramView)
  {
    this.viewPager = ((ViewPager)paramView.findViewById(2131099996));
    this.viewPagerAdapter = new GuidePageAdapter(getBaseActivity());
    this.viewPager.setAdapter(this.viewPagerAdapter);
    this.cpiGuideTour = ((CirclePageIndicator)paramView.findViewById(2131099997));
    this.cpiGuideTour.setViewPager(this.viewPager);
    getBaseActivity().showRightButton(2130837770);
  }
  
  private void setupListeners()
  {
    this.cpiGuideTour.setOnPageChangeListener(this);
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903142, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    return paramLayoutInflater;
  }
  
  public void onPageScrollStateChanged(int paramInt) {}
  
  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {}
  
  public void onPageSelected(int paramInt)
  {
    Log.i("PageLoaded => ", "  " + paramInt);
    if ((paramInt != 0) && (paramInt != 1)) {
      if (GuideActivty.displayYoutubeVideo != null) {
        GuideActivty.displayYoutubeVideo.loadUrl("file:///android_asset/nonexistent.html");
      }
    }
    for (;;)
    {
      return;
      if ((paramInt == 1) && (GuideActivty.displayYoutubeVideo != null)) {
        GuideActivty.displayYoutubeVideo.loadData("<html><body style=\"background: transparent; padding:0;margin:0\"><div style=\"position: relative;\"> <iframe style=\"background: #000000; position: absolute; top:0; left:0; width=100%; height=100%\" width=\"320\" height=\"315\" src=\"https://www.youtube.com/embed/R2t0WAfClKo\" frameborder=\"0\" allowfullscreen></iframe></div></body></html>", "text/html", "utf-8");
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\GuideFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
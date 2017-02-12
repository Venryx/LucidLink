package com.resmed.refresh.ui.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import com.resmed.refresh.ui.activity.GuideActivty;

public class GuidePageAdapter
  extends PagerAdapter
{
  private Context context;
  private int[] descriptions = { 2131165400, 2131165402, 2131165404, 2131165406, 2131165408, 2131165410, 2131165412, 2131165414, 2131165416, 2131165418 };
  private int[] images = { 2130837613, 2130837614, 2130837615, 2130837616, 2130837617, 2130837618, 2130837608, 2130837609, 2130837610, 2130837611 };
  private int[] titles = { 2131165399, 2131165401, 2131165403, 2131165405, 2131165407, 2131165409, 2131165411, 2131165413, 2131165415, 2131165417 };
  
  public GuidePageAdapter(Context paramContext)
  {
    this.context = paramContext;
  }
  
  public void destroyItem(View paramView, int paramInt, Object paramObject)
  {
    ((ViewPager)paramView).removeView((ViewGroup)paramObject);
  }
  
  public int getCount()
  {
    return this.descriptions.length + 1 + 1;
  }
  
  public final Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
  {
    ViewGroup localViewGroup;
    if (paramInt == 0)
    {
      localViewGroup = (ViewGroup)((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2130903186, null);
      GuideActivty.displayYoutubeVideo = (WebView)localViewGroup.findViewById(2131100607);
      GuideActivty.displayYoutubeVideo.setWebViewClient(new WebViewClient()
      {
        public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString)
        {
          return false;
        }
      });
      GuideActivty.displayYoutubeVideo.getSettings().setJavaScriptEnabled(true);
      GuideActivty.displayYoutubeVideo.loadData("<html><body style=\"background: transparent; padding:0;margin:0\"><div style=\"position: relative;\"> <iframe style=\"background: #000000; position: absolute; top:0; left:0; width=100%; height=100%\" width=\"320\" height=\"315\" src=\"https://www.youtube.com/embed/R2t0WAfClKo\" frameborder=\"0\" allowfullscreen></iframe></div></body></html>", "text/html", "utf-8");
    }
    for (;;)
    {
      paramViewGroup.addView(localViewGroup);
      return localViewGroup;
      if (paramInt == 1)
      {
        localViewGroup = (ViewGroup)((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2130903187, null);
      }
      else if (paramInt == getCount() - 1)
      {
        localViewGroup = (ViewGroup)((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2130903189, null);
      }
      else
      {
        localViewGroup = (ViewGroup)((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2130903188, null);
        ((ImageView)localViewGroup.findViewById(2131100609)).setImageResource(this.images[(paramInt - 1)]);
        ((TextView)localViewGroup.findViewById(2131100610)).setText(this.titles[(paramInt - 1)]);
        ((TextView)localViewGroup.findViewById(2131100611)).setText(this.descriptions[(paramInt - 1)]);
      }
    }
  }
  
  public final boolean isViewFromObject(View paramView, Object paramObject)
  {
    if (paramView == (View)paramObject) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\adapters\GuidePageAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
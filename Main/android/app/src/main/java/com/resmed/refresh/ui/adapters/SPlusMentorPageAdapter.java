package com.resmed.refresh.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.resmed.refresh.model.RST_AdviceItem;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.utils.InfinitePagerAdapter;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SPlusMentorPageAdapter
  extends InfinitePagerAdapter<Long>
{
  private static final long DAY_IN_MILLS = 86400000L;
  private Context context;
  
  public SPlusMentorPageAdapter(Context paramContext, Long paramLong)
  {
    super(paramLong);
    this.context = paramContext;
  }
  
  public int compareIndicator(Long paramLong1, Long paramLong2)
  {
    int i;
    if (paramLong1.longValue() == paramLong2.longValue()) {
      i = 0;
    }
    for (;;)
    {
      return i;
      if (paramLong1.longValue() > paramLong2.longValue()) {
        i = 1;
      } else {
        i = -1;
      }
    }
  }
  
  public Long convertToIndicator(String paramString)
  {
    return Long.valueOf(RefreshTools.getDateFromString(paramString).getTime());
  }
  
  public boolean equalsIndicators(Long paramLong1, Long paramLong2)
  {
    if (paramLong1.longValue() == paramLong2.longValue()) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public Long getNextIndicator()
  {
    return Long.valueOf(((Long)getCurrentIndicator()).longValue() + 86400000L);
  }
  
  public Long getPreviousIndicator()
  {
    return Long.valueOf(((Long)getCurrentIndicator()).longValue() - 86400000L);
  }
  
  @SuppressLint({"SimpleDateFormat"})
  public String getStringRepresentation(Long paramLong)
  {
    return new SimpleDateFormat("dd MMMM yyyy").format(new Date(paramLong.longValue()));
  }
  
  public ViewGroup instantiateItem(Long paramLong)
  {
    Log.d("InfiniteViewPager", "Smentor PageAdapter. instantiating page " + paramLong.longValue());
    RelativeLayout localRelativeLayout = (RelativeLayout)((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2130903198, null);
    PullToRefreshScrollView localPullToRefreshScrollView = (PullToRefreshScrollView)localRelativeLayout.findViewById(2131100663);
    PullToRefreshListView localPullToRefreshListView = (PullToRefreshListView)localRelativeLayout.findViewById(2131100664);
    localPullToRefreshScrollView.setVisibility(4);
    localPullToRefreshListView.setVisibility(4);
    RefreshModelController.getInstance().adviceForDay(new Date(paramLong.longValue()), new AdviceCallback(paramLong), false);
    localRelativeLayout.setTag(paramLong);
    return localRelativeLayout;
  }
  
  private class AdviceCallback
    implements RST_CallbackItem<RST_Response<List<RST_AdviceItem>>>
  {
    private Long indicator;
    
    public AdviceCallback(Long paramLong)
    {
      this.indicator = paramLong;
    }
    
    public long getIndicator()
    {
      return this.indicator.longValue();
    }
    
    public void onResult(RST_Response<List<RST_AdviceItem>> paramRST_Response)
    {
      if (this.indicator == null) {}
      for (;;)
      {
        return;
        Log.d("com.resmed.refresh.ui", "S+PageAdaptor Indicator" + new Date(this.indicator.longValue()));
        Object localObject2 = SPlusMentorPageAdapter.this.getViewForIndicator(Long.valueOf(this.indicator.longValue()));
        if (localObject2 != null)
        {
          ((ViewGroup)localObject2).findViewById(2131100665).setVisibility(8);
          Object localObject1 = (PullToRefreshScrollView)((ViewGroup)localObject2).findViewById(2131100663);
          PullToRefreshListView localPullToRefreshListView = (PullToRefreshListView)((ViewGroup)localObject2).findViewById(2131100664);
          if (((PullToRefreshScrollView)localObject1).isRefreshing()) {
            ((PullToRefreshScrollView)localObject1).onRefreshComplete();
          }
          if (localPullToRefreshListView.isRefreshing()) {
            localPullToRefreshListView.onRefreshComplete();
          }
          if ((paramRST_Response.isStatus()) && (((List)paramRST_Response.getResponse()).size() > 0))
          {
            ((PullToRefreshScrollView)localObject1).setVisibility(4);
            localPullToRefreshListView.setVisibility(0);
            localObject1 = (LayoutInflater)SPlusMentorPageAdapter.this.context.getSystemService("layout_inflater");
            localObject2 = (ListView)((ViewGroup)localObject2).findViewById(16908298);
            localPullToRefreshListView.setAdapter(new AdvicesAdapter(SPlusMentorPageAdapter.this.context, (ListView)localObject2, (List)paramRST_Response.getResponse(), (LayoutInflater)localObject1));
            localPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener()
            {
              public void onRefresh(PullToRefreshBase<ListView> paramAnonymousPullToRefreshBase)
              {
                RefreshModelController.getInstance().adviceForDay(new Date(jdField_this.getIndicator()), jdField_this, true);
              }
            });
          }
          else
          {
            if (!paramRST_Response.isStatus()) {
              Toast.makeText(RefreshApplication.getInstance(), paramRST_Response.getErrorMessage(), 0).show();
            }
            if (((ListView)localPullToRefreshListView.getRefreshableView()).getAdapter() == null)
            {
              ((PullToRefreshScrollView)localObject1).setVisibility(0);
              localPullToRefreshListView.setVisibility(4);
              ((PullToRefreshScrollView)localObject1).setOnRefreshListener(new PullToRefreshBase.OnRefreshListener()
              {
                public void onRefresh(PullToRefreshBase<ScrollView> paramAnonymousPullToRefreshBase)
                {
                  RefreshModelController.getInstance().adviceForDay(new Date(jdField_this.getIndicator()), jdField_this, true);
                }
              });
            }
          }
        }
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\adapters\SPlusMentorPageAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
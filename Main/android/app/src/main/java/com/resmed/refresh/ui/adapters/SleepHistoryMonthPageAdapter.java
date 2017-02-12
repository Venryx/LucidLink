package com.resmed.refresh.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.activity.SleepHistoryDayActivity;
import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.ui.utils.InfinitePagerAdapter;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SleepHistoryMonthPageAdapter
  extends InfinitePagerAdapter<Long>
  implements AdapterView.OnItemClickListener
{
  private BaseActivity activity;
  private PullToRefreshBase.OnRefreshListener<ListView> listViewPageOnRefresh = new PullToRefreshBase.OnRefreshListener()
  {
    public void onRefresh(final PullToRefreshBase<ListView> paramAnonymousPullToRefreshBase)
    {
      final Calendar localCalendar = Calendar.getInstance();
      localCalendar.setTime(new Date(((Long)SleepHistoryMonthPageAdapter.this.getCurrentIndicator()).longValue()));
      localCalendar.set(11, 0);
      localCalendar.set(12, 0);
      localCalendar.set(13, 0);
      localCalendar.set(14, 0);
      localCalendar.set(5, 1);
      Date localDate1 = localCalendar.getTime();
      localCalendar.set(5, Calendar.getInstance().getActualMaximum(5));
      localCalendar.add(13, -1);
      Date localDate2 = localCalendar.getTime();
      Log.d("com.resmed.refresh.ui", "Month Adapter. onRefresh StartDate " + localDate1 + " EndDate " + localDate2);
      RefreshModelController.getInstance().historicalSleepRecords(localDate1, localDate2, new RST_CallbackItem()
      {
        public void onResult(RST_Response<List<RST_SleepSessionInfo>> paramAnonymous2RST_Response)
        {
          if ((SleepHistoryMonthPageAdapter.this.activity == null) || (SleepHistoryMonthPageAdapter.this.activity.isFinishing())) {}
          for (;;)
          {
            return;
            try
            {
              if (paramAnonymousPullToRefreshBase.isRefreshing()) {
                paramAnonymousPullToRefreshBase.onRefreshComplete();
              }
              if (paramAnonymous2RST_Response.isStatus()) {
                break label84;
              }
              SleepHistoryMonthPageAdapter.this.activity.showErrorDialog(paramAnonymous2RST_Response);
            }
            catch (Exception paramAnonymous2RST_Response)
            {
              Log.w("com.resmed.refresh.ui", "The view is not longer avaible", paramAnonymous2RST_Response);
            }
            continue;
            label84:
            if (((List)paramAnonymous2RST_Response.getResponse()).size() > 0)
            {
              paramAnonymous2RST_Response = RefreshModelController.getInstance().localSleepSessionsInMonth(localCalendar.get(2), localCalendar.get(1));
              PullToRefreshScrollView localPullToRefreshScrollView = (PullToRefreshScrollView)((View)paramAnonymousPullToRefreshBase.getParent()).findViewById(2131100643);
              SleepHistoryMonthPageAdapter.this.updateView(paramAnonymous2RST_Response, (PullToRefreshListView)paramAnonymousPullToRefreshBase, localPullToRefreshScrollView);
            }
          }
        }
      }, true);
    }
  };
  private PullToRefreshBase.OnRefreshListener<ScrollView> noDataPageOnRefresh = new PullToRefreshBase.OnRefreshListener()
  {
    public void onRefresh(final PullToRefreshBase<ScrollView> paramAnonymousPullToRefreshBase)
    {
      final Calendar localCalendar = Calendar.getInstance();
      localCalendar.setTime(new Date(((Long)SleepHistoryMonthPageAdapter.this.getCurrentIndicator()).longValue()));
      localCalendar.set(11, 0);
      localCalendar.set(12, 0);
      localCalendar.set(13, 0);
      localCalendar.set(14, 0);
      localCalendar.set(5, 1);
      Date localDate1 = localCalendar.getTime();
      localCalendar.set(5, Calendar.getInstance().getActualMaximum(5));
      localCalendar.add(13, -1);
      Date localDate2 = localCalendar.getTime();
      Log.d("com.resmed.refresh.ui", "Month Adapter. onRefresh StartDate " + localDate1 + " EndDate " + localDate2);
      RefreshModelController.getInstance().historicalSleepRecords(localDate1, localDate2, new RST_CallbackItem()
      {
        public void onResult(RST_Response<List<RST_SleepSessionInfo>> paramAnonymous2RST_Response)
        {
          if ((SleepHistoryMonthPageAdapter.this.activity == null) || (SleepHistoryMonthPageAdapter.this.activity.isFinishing())) {}
          for (;;)
          {
            return;
            try
            {
              if (paramAnonymousPullToRefreshBase.isRefreshing()) {
                paramAnonymousPullToRefreshBase.onRefreshComplete();
              }
              if (paramAnonymous2RST_Response.isStatus()) {
                break label84;
              }
              SleepHistoryMonthPageAdapter.this.activity.showErrorDialog(paramAnonymous2RST_Response);
            }
            catch (Exception paramAnonymous2RST_Response)
            {
              Log.w("com.resmed.refresh.ui", "The view is not longer avaible", paramAnonymous2RST_Response);
            }
            continue;
            label84:
            if (((List)paramAnonymous2RST_Response.getResponse()).size() > 0)
            {
              List localList = RefreshModelController.getInstance().localSleepSessionsInMonth(localCalendar.get(2), localCalendar.get(1));
              paramAnonymous2RST_Response = (PullToRefreshListView)((View)paramAnonymousPullToRefreshBase.getParent()).findViewById(2131100642);
              SleepHistoryMonthPageAdapter.this.updateView(localList, paramAnonymous2RST_Response, (PullToRefreshScrollView)paramAnonymousPullToRefreshBase);
            }
          }
        }
      }, true);
    }
  };
  
  public SleepHistoryMonthPageAdapter(BaseActivity paramBaseActivity, Long paramLong)
  {
    super(paramLong);
    this.activity = paramBaseActivity;
    Log.d("com.resmed.refresh.ui", "Month Adapter. Init indicator = " + new Date(paramLong.longValue()));
  }
  
  private void updateView(List<RST_SleepSessionInfo> paramList, PullToRefreshListView paramPullToRefreshListView, PullToRefreshScrollView paramPullToRefreshScrollView)
  {
    Object localObject = AnimationUtils.loadAnimation(this.activity, 2130968582);
    if ((paramList != null) && (paramList.size() > 0))
    {
      if (paramPullToRefreshListView.getVisibility() == 8) {
        paramPullToRefreshListView.startAnimation((Animation)localObject);
      }
      localObject = (LayoutInflater)this.activity.getSystemService("layout_inflater");
      paramList = new SleepHistoryMonthListAdapter(this.activity, paramList, (LayoutInflater)localObject);
      paramPullToRefreshListView.setAdapter(paramList);
      paramPullToRefreshListView.setOnScrollListener(paramList);
      paramPullToRefreshListView.setVisibility(0);
      paramPullToRefreshScrollView.setVisibility(8);
      paramPullToRefreshListView.setOnItemClickListener(this);
    }
    for (;;)
    {
      return;
      if (paramPullToRefreshScrollView.getVisibility() == 8) {
        paramPullToRefreshScrollView.startAnimation((Animation)localObject);
      }
      paramPullToRefreshListView.setVisibility(8);
      paramPullToRefreshScrollView.setVisibility(0);
    }
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
    return new Long(RefreshTools.getDateFromString(paramString).getTime());
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
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTimeInMillis(((Long)getCurrentIndicator()).longValue());
    localGregorianCalendar.add(2, 1);
    return Long.valueOf(localGregorianCalendar.getTimeInMillis());
  }
  
  public Long getPreviousIndicator()
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTimeInMillis(((Long)getCurrentIndicator()).longValue());
    localGregorianCalendar.add(2, -1);
    return Long.valueOf(localGregorianCalendar.getTimeInMillis());
  }
  
  @SuppressLint({"SimpleDateFormat"})
  public String getStringRepresentation(Long paramLong)
  {
    return new SimpleDateFormat("MMMM yyyy").format(new Date(paramLong.longValue()));
  }
  
  public ViewGroup instantiateItem(Long paramLong)
  {
    Log.d("com.resmed.refresh.ui", "Month Adapter. instantiateItem = " + new Date(paramLong.longValue()));
    Object localObject = (LayoutInflater)this.activity.getSystemService("layout_inflater");
    ViewGroup localViewGroup = (ViewGroup)((LayoutInflater)localObject).inflate(2130903194, null);
    GregorianCalendar localGregorianCalendar = new GregorianCalendar();
    localGregorianCalendar.setTimeInMillis(paramLong.longValue());
    PullToRefreshListView localPullToRefreshListView = (PullToRefreshListView)localViewGroup.findViewById(2131100642);
    localPullToRefreshListView.setOnRefreshListener(this.listViewPageOnRefresh);
    ProgressBar localProgressBar = (ProgressBar)localViewGroup.findViewById(2131100619);
    PullToRefreshScrollView localPullToRefreshScrollView = (PullToRefreshScrollView)localViewGroup.findViewById(2131100643);
    localPullToRefreshScrollView.setOnRefreshListener(this.noDataPageOnRefresh);
    ListView localListView = (ListView)localPullToRefreshListView.getRefreshableView();
    localObject = (ViewGroup)((LayoutInflater)localObject).inflate(2130903126, null, false);
    WebView localWebView = (WebView)((ViewGroup)localObject).findViewById(2131099892);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<HTML><HEAD><LINK href=\"terms.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
    localStringBuilder.append(this.activity.getString(2131166000));
    localStringBuilder.append(" <a href=\"" + com.resmed.refresh.ui.utils.Consts.REFRESH_URL[4] + "\" >" + this.activity.getString(2131166002) + "</a> ");
    localStringBuilder.append(this.activity.getString(2131166001));
    localStringBuilder.append("</body></HTML>");
    localWebView.loadDataWithBaseURL("file:///android_asset/", localStringBuilder.toString(), "text/html", "utf-8", null);
    localWebView.setBackgroundColor(0);
    localWebView.setVerticalScrollBarEnabled(false);
    localWebView.setWebViewClient(new WebViewClient()
    {
      public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString)
      {
        paramAnonymousWebView = new Intent("android.intent.action.VIEW");
        paramAnonymousWebView.setData(Uri.parse(paramAnonymousString));
        SleepHistoryMonthPageAdapter.this.activity.startActivity(paramAnonymousWebView);
        return true;
      }
    });
    localListView.addFooterView((View)localObject, null, true);
    localProgressBar.setVisibility(8);
    updateView(RefreshModelController.getInstance().localSleepSessionsInMonth(localGregorianCalendar.get(2), localGregorianCalendar.get(1)), localPullToRefreshListView, localPullToRefreshScrollView);
    localViewGroup.setTag(paramLong);
    return localViewGroup;
  }
  
  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    
    if (paramInt >= 0) {}
    try
    {
      paramAdapterView = new java/util/GregorianCalendar;
      paramAdapterView.<init>();
      paramAdapterView.setTimeInMillis(((Long)getCurrentIndicator()).longValue());
      paramAdapterView = RefreshModelController.getInstance().localSleepSessionsInMonth(paramAdapterView.get(2), paramAdapterView.get(1));
      paramView = new java/lang/StringBuilder;
      paramView.<init>("Month Adapter onItemClick(");
      Log.d("com.resmed.refresh.ui", paramInt + ")  ");
      paramView = new android/content/Intent;
      paramView.<init>(this.activity, SleepHistoryDayActivity.class);
      paramView.putExtra("com.resmed.refresh.ui.uibase.app.sleep_history_id", ((RST_SleepSessionInfo)paramAdapterView.get(paramInt)).getId());
      this.activity.startActivity(paramView);
      return;
    }
    catch (NullPointerException paramAdapterView)
    {
      for (;;)
      {
        paramAdapterView.printStackTrace();
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\adapters\SleepHistoryMonthPageAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
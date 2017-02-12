package com.resmed.refresh.ui.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.graphs.GraphViewBuilder;
import com.resmed.refresh.ui.utils.GraphViewListManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SleepHistoryMonthListAdapter
  extends ArrayAdapter<RST_SleepSessionInfo>
  implements RST_CallbackItem<Long>, AbsListView.OnScrollListener
{
  private Context context;
  private List<RST_SleepSessionInfo> items;
  private LayoutInflater li;
  private int scrollState = 0;
  
  public SleepHistoryMonthListAdapter(Context paramContext, List<RST_SleepSessionInfo> paramList, LayoutInflater paramLayoutInflater)
  {
    super(paramContext, 2130903195, paramList);
    this.items = paramList;
    this.li = paramLayoutInflater;
    this.context = paramContext;
  }
  
  public int getCount()
  {
    return this.items.size();
  }
  
  public String getStringRepresentation(Date paramDate)
  {
    return new SimpleDateFormat("EEEE d").format(paramDate);
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    paramViewGroup = paramView;
    paramView = paramViewGroup;
    if (paramViewGroup == null)
    {
      paramView = this.li.inflate(2130903195, null);
      paramViewGroup = new ViewHolder();
      paramViewGroup.tvSleepMonthDay = ((TextView)paramView.findViewById(2131100644));
      paramViewGroup.tvSleepMonthDuration = ((TextView)paramView.findViewById(2131100645));
      paramViewGroup.tvSleepMonthScore = ((TextView)paramView.findViewById(2131100646));
      paramViewGroup.graphView = ((LinearLayout)paramView.findViewById(2131100623));
      paramView.setTag(paramViewGroup);
      paramViewGroup.graphView.setTag(new Integer(paramInt));
    }
    paramViewGroup = (ViewHolder)paramView.getTag();
    Object localObject = (RST_SleepSessionInfo)this.items.get(paramInt);
    paramViewGroup.tvSleepMonthDay.setText(getStringRepresentation(((RST_SleepSessionInfo)localObject).getStartTime()));
    paramViewGroup.tvSleepMonthDuration.setText(((RST_SleepSessionInfo)localObject).getSleepTime());
    paramViewGroup.tvSleepMonthScore.setText(String.valueOf(((RST_SleepSessionInfo)localObject).getSleepScore()));
    localObject = GraphViewListManager.getInstance().getView(this.context, (RST_SleepSessionInfo)localObject, this);
    try
    {
      paramViewGroup.graphView.removeAllViews();
      if (((GraphViewBuilder)localObject).getChartView() != null) {
        paramViewGroup.graphView.addView(((GraphViewBuilder)localObject).getChartView());
      }
      return paramView;
    }
    catch (NullPointerException localNullPointerException)
    {
      for (;;)
      {
        localNullPointerException.printStackTrace();
        paramViewGroup.graphView.removeAllViews();
      }
    }
    catch (Exception localException)
    {
      for (;;)
      {
        localException.printStackTrace();
        paramViewGroup.graphView.removeAllViews();
      }
    }
  }
  
  public void onResult(Long paramLong)
  {
    if ((this != null) && (this.scrollState == 0)) {
      new Handler(Looper.getMainLooper()).post(new Runnable()
      {
        public void run()
        {
          SleepHistoryMonthListAdapter.this.notifyDataSetChanged();
        }
      });
    }
  }
  
  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3) {}
  
  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
  {
    this.scrollState = paramInt;
    if (paramInt == 0) {
      notifyDataSetChanged();
    }
  }
  
  static class ViewHolder
  {
    public LinearLayout graphView;
    public TextView tvSleepMonthDay;
    public TextView tvSleepMonthDuration;
    public TextView tvSleepMonthScore;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\adapters\SleepHistoryMonthListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
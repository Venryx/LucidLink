package com.resmed.refresh.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.resmed.refresh.model.RST_AdviceItem;
import com.resmed.refresh.model.RST_AdviceItem.AdviceType;
import com.resmed.refresh.utils.Log;
import java.util.List;

public class SleepHistoryDayAdvicesListAdapter
  extends ArrayAdapter<RST_AdviceItem>
{
  private List<RST_AdviceItem> items;
  private LayoutInflater li;
  
  public SleepHistoryDayAdvicesListAdapter(Context paramContext, List<RST_AdviceItem> paramList, LayoutInflater paramLayoutInflater)
  {
    super(paramContext, 2130903192, paramList);
    this.items = paramList;
    this.li = paramLayoutInflater;
  }
  
  public int getCount()
  {
    return this.items.size();
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    paramViewGroup = paramView;
    paramView = paramViewGroup;
    if (paramViewGroup == null)
    {
      paramView = this.li.inflate(2130903192, null);
      paramViewGroup = new ViewHolder();
      paramViewGroup.tvSleepHistoryAdviceTitle = ((TextView)paramView.findViewById(2131100616));
      paramViewGroup.tvSleepHistoryAdviceContent = ((TextView)paramView.findViewById(2131100618));
      paramViewGroup.ivSleepHistoryAdviceType = ((ImageView)paramView.findViewById(2131100615));
      paramViewGroup.tvSleepHistoryAdviceSubtitle = ((TextView)paramView.findViewById(2131100617));
      paramView.setTag(paramViewGroup);
    }
    paramViewGroup = (ViewHolder)paramView.getTag();
    Object localObject = (RST_AdviceItem)this.items.get(paramInt);
    paramViewGroup.tvSleepHistoryAdviceTitle.setText(((RST_AdviceItem)localObject).getHeader());
    paramViewGroup.tvSleepHistoryAdviceContent.setText(((RST_AdviceItem)localObject).getContent());
    if (((RST_AdviceItem)this.items.get(paramInt)).getSubtitle().length() > 0)
    {
      paramViewGroup.tvSleepHistoryAdviceSubtitle.setVisibility(0);
      paramViewGroup.tvSleepHistoryAdviceSubtitle.setText(((RST_AdviceItem)this.items.get(paramInt)).getSubtitle());
      paramViewGroup.tvSleepHistoryAdviceContent.setText(((RST_AdviceItem)localObject).getContent());
      Log.d("com.resmed.refresh.ui", "SleepDay List Advice Adapter. Content of advice " + paramInt + ":" + ((RST_AdviceItem)localObject).getContent());
      localObject = RST_AdviceItem.AdviceType.fromValue(((RST_AdviceItem)localObject).getCategory());
      switch (localObject)
      {
      default: 
        paramViewGroup.ivSleepHistoryAdviceType.setImageResource(2130837602);
      }
    }
    for (;;)
    {
      return paramView;
      paramViewGroup.tvSleepHistoryAdviceSubtitle.setVisibility(8);
      break;
      paramViewGroup.ivSleepHistoryAdviceType.setImageResource(2130837603);
    }
  }
  
  static class ViewHolder
  {
    public ImageView ivSleepHistoryAdviceType;
    public TextView tvSleepHistoryAdviceContent;
    public TextView tvSleepHistoryAdviceSubtitle;
    public TextView tvSleepHistoryAdviceTitle;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\adapters\SleepHistoryDayAdvicesListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
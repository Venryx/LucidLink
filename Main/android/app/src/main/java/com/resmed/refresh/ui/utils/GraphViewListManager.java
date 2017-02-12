package com.resmed.refresh.ui.utils;

import android.content.Context;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.graphs.GraphViewBuilder;
import com.resmed.refresh.utils.Log;
import java.util.HashMap;
import java.util.Map;

public class GraphViewListManager
{
  private static GraphViewListManager manager;
  private Map<Long, GraphViewBuilder> viewItems;
  
  public static GraphViewListManager getInstance()
  {
    try
    {
      if (manager == null)
      {
        localGraphViewListManager = new com/resmed/refresh/ui/utils/GraphViewListManager;
        localGraphViewListManager.<init>();
        manager = localGraphViewListManager;
        manager.setup();
      }
      GraphViewListManager localGraphViewListManager = manager;
      return localGraphViewListManager;
    }
    finally {}
  }
  
  private void setup()
  {
    this.viewItems = new HashMap();
  }
  
  public GraphViewBuilder getView(Context paramContext, RST_SleepSessionInfo paramRST_SleepSessionInfo)
  {
    return getView(paramContext, paramRST_SleepSessionInfo, null);
  }
  
  public GraphViewBuilder getView(Context paramContext, RST_SleepSessionInfo paramRST_SleepSessionInfo, RST_CallbackItem<Long> paramRST_CallbackItem)
  {
    Log.d("com.resmed.refresh.ui", "GrapView GraphViewListManager.getView = " + paramRST_SleepSessionInfo.getId() + " SleepScore = " + paramRST_SleepSessionInfo.getSleepScore());
    long l = new Long(paramRST_SleepSessionInfo.getId()).longValue();
    if (paramRST_CallbackItem == null)
    {
      paramContext = new GraphViewBuilder(paramContext).getView(paramRST_SleepSessionInfo);
      this.viewItems.put(Long.valueOf(l), paramContext);
    }
    for (;;)
    {
      return paramContext;
      if (this.viewItems.containsKey(Long.valueOf(l)))
      {
        paramContext = (GraphViewBuilder)this.viewItems.get(Long.valueOf(l));
      }
      else
      {
        this.viewItems.put(Long.valueOf(l), new GraphViewBuilder(paramContext).getViewAsync(paramRST_SleepSessionInfo, paramRST_CallbackItem));
        paramContext = (GraphViewBuilder)this.viewItems.get(Long.valueOf(l));
      }
    }
  }
  
  public void release()
  {
    this.viewItems = null;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
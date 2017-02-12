package com.resmed.refresh.ui.adapters;

import com.resmed.refresh.model.RST_SleepSessionInfo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SleepHistoryItem
{
  private RST_SleepSessionInfo sessionInfo;
  private long time;
  
  private SleepHistoryItem() {}
  
  public SleepHistoryItem(long paramLong)
  {
    this.time = paramLong;
  }
  
  public SleepHistoryItem(long paramLong, RST_SleepSessionInfo paramRST_SleepSessionInfo)
  {
    this.time = paramLong;
    this.sessionInfo = paramRST_SleepSessionInfo;
  }
  
  public SleepHistoryItem(Date paramDate)
  {
    this.time = paramDate.getTime();
  }
  
  public static List<SleepHistoryItem> getList(List<RST_SleepSessionInfo> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    paramList = paramList.iterator();
    for (;;)
    {
      if (!paramList.hasNext()) {
        return localArrayList;
      }
      RST_SleepSessionInfo localRST_SleepSessionInfo = (RST_SleepSessionInfo)paramList.next();
      localArrayList.add(new SleepHistoryItem(localRST_SleepSessionInfo.getStartTime().getTime(), localRST_SleepSessionInfo));
    }
  }
  
  public Date getDateIndicator()
  {
    return new Date(this.time);
  }
  
  public Long getIndicator()
  {
    return Long.valueOf(this.time);
  }
  
  public RST_SleepSessionInfo getSessionInfo()
  {
    return this.sessionInfo;
  }
  
  public long getTime()
  {
    return this.time;
  }
  
  public void setIndicator(long paramLong)
  {
    this.time = paramLong;
  }
  
  public void setIndicator(Date paramDate)
  {
    this.time = paramDate.getTime();
  }
  
  public void setSessionInfo(RST_SleepSessionInfo paramRST_SleepSessionInfo)
  {
    this.sessionInfo = paramRST_SleepSessionInfo;
  }
  
  public void setTime(long paramLong)
  {
    this.time = paramLong;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\adapters\SleepHistoryItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.ui.adapters;

import java.util.Date;

public class SleepHistoryIndicator
{
  private Long indicator;
  
  private SleepHistoryIndicator() {}
  
  public SleepHistoryIndicator(Long paramLong)
  {
    this.indicator = paramLong;
  }
  
  public SleepHistoryIndicator(Date paramDate)
  {
    this.indicator = Long.valueOf(paramDate.getTime());
  }
  
  public Date getDateIndicator()
  {
    return new Date(this.indicator.longValue());
  }
  
  public Long getIndicator()
  {
    return this.indicator;
  }
  
  public void setIndicator(Long paramLong)
  {
    this.indicator = paramLong;
  }
  
  public void setIndicator(Date paramDate)
  {
    this.indicator = Long.valueOf(paramDate.getTime());
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\adapters\SleepHistoryIndicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.model.graphs;

import com.resmed.refresh.model.RST_SleepSessionInfo.SleepSessionUserState;
import java.util.Date;

public class RRHypnoData
{
  private Date hour;
  private RRHypnoBar type;
  
  public Date getHour()
  {
    return this.hour;
  }
  
  public float getValueBar()
  {
    return this.type.getVal();
  }
  
  public void setHour(Date paramDate)
  {
    this.hour = paramDate;
  }
  
  public void setState(int paramInt)
  {
    this.type = RRHypnoBar.fromSleepSessionState(RST_SleepSessionInfo.SleepSessionUserState.fromValue(paramInt));
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
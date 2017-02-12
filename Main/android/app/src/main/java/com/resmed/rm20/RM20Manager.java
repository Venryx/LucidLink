package com.resmed.rm20;

import java.util.Date;

public abstract interface RM20Manager
{
  public abstract int disableSmartAlarm();
  
  public abstract int getEpochCount();
  
  public abstract String getLibVersion();
  
  public abstract int getRealTimeSleepState();
  
  public abstract SmartAlarmInfo getSmartAlarm();
  
  public abstract UserInfo getUserInfo();
  
  public abstract SleepParams resultsForSession();
  
  public abstract int setSmartAlarm(Date paramDate, int paramInt, boolean paramBoolean);
  
  public abstract int startRespRateCallbacks(boolean paramBoolean);
  
  public abstract int startupLibrary(int paramInt1, int paramInt2);
  
  public abstract int stopAndCalculate();
  
  public abstract int writeSampleData(int paramInt1, int paramInt2);
  
  public abstract int writeSamples(int[] paramArrayOfInt1, int[] paramArrayOfInt2);
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\rm20\RM20Manager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.sleepsession;

import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.utils.Log;

public class RST_SleepSession
{
  private static RST_SleepSession sleepSession;
  private boolean analysisRunning;
  private RST_SleepSessionInfo sessionResults;
  private boolean sessionRunning;
  
  public static RST_SleepSession getInstance()
  {
    try
    {
      if (sleepSession == null)
      {
        localRST_SleepSession = new com/resmed/refresh/sleepsession/RST_SleepSession;
        localRST_SleepSession.<init>();
        sleepSession = localRST_SleepSession;
        sleepSession.setup();
      }
      RST_SleepSession localRST_SleepSession = sleepSession;
      return localRST_SleepSession;
    }
    finally {}
  }
  
  private void setup()
  {
    this.sessionRunning = false;
    this.analysisRunning = false;
  }
  
  public RST_SleepSessionInfo currentSleepData()
  {
    return this.sessionResults;
  }
  
  public void delayedSleepCalculation(RST_CallbackItem<RST_Response<RST_SleepSessionInfo>> paramRST_CallbackItem) {}
  
  public boolean isSessionRunning()
  {
    return this.sessionRunning;
  }
  
  public boolean resumeSession()
  {
    this.sessionRunning = true;
    return true;
  }
  
  public void setupProcesserWithStorage(boolean paramBoolean) {}
  
  public void sleepData(RST_CallbackItem<RST_Response<RST_SleepSessionInfo>> paramRST_CallbackItem)
  {
    if (!this.analysisRunning) {
      this.analysisRunning = true;
    }
  }
  
  public boolean startSession(boolean paramBoolean)
  {
    this.sessionRunning = true;
    Log.stop();
    return true;
  }
  
  public boolean stopSession(boolean paramBoolean)
  {
    this.sessionRunning = false;
    Log.record();
    return true;
  }
  
  public boolean suspendSession()
  {
    this.sessionRunning = false;
    return true;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\sleepsession\RST_SleepSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
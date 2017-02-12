package com.resmed.rm20;

public abstract interface RM20Callbacks
{
  public abstract void onRm20Alarm(int paramInt);
  
  public abstract void onRm20RealTimeSleepState(int paramInt1, int paramInt2);
  
  public abstract void onRm20ValidBreathingRate(float paramFloat, int paramInt);
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\rm20\RM20Callbacks.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
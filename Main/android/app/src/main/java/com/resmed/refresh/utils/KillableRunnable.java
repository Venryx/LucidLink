package com.resmed.refresh.utils;

public class KillableRunnable
  implements Runnable
{
  private boolean killMe = false;
  private KillableRunner runner;
  
  public KillableRunnable(KillableRunner paramKillableRunner)
  {
    this.runner = paramKillableRunner;
  }
  
  public void killRunnable()
  {
    this.killMe = true;
  }
  
  public void run()
  {
    if (this.killMe) {}
    for (;;)
    {
      return;
      this.runner.executeOnRun();
    }
  }
  
  public static abstract interface KillableRunner
  {
    public abstract void executeOnRun();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\KillableRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
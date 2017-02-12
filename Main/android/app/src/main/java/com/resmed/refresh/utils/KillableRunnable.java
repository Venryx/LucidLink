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


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
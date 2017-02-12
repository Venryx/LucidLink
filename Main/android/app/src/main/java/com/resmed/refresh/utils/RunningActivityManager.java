package com.resmed.refresh.utils;

import com.resmed.refresh.ui.uibase.base.BaseActivity;
import java.util.ArrayList;
import java.util.List;

public class RunningActivityManager
{
  private static RunningActivityManager instance;
  private List<BaseActivity> activityList = new ArrayList();
  
  public static RunningActivityManager getInstance()
  {
    if (instance == null) {
      instance = new RunningActivityManager();
    }
    return instance;
  }
  
  public BaseActivity getCurrentActivity()
  {
    if (this.activityList.size() > 0) {}
    for (BaseActivity localBaseActivity = (BaseActivity)this.activityList.get(this.activityList.size() - 1);; localBaseActivity = null) {
      return localBaseActivity;
    }
  }
  
  public boolean isApplicationActive()
  {
    if (this.activityList.size() > 0) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public void registerCurrentActivity(BaseActivity paramBaseActivity)
  {
    Log.i("com.resmed.refresh", "register current activity " + paramBaseActivity);
    if (!this.activityList.contains(paramBaseActivity)) {
      this.activityList.add(paramBaseActivity);
    }
  }
  
  public void unregisterCurrentActivity(BaseActivity paramBaseActivity)
  {
    if (this.activityList.contains(paramBaseActivity)) {
      this.activityList.remove(paramBaseActivity);
    }
    Log.i("com.resmed.refresh", "UNregister current activity " + paramBaseActivity + " number of activity registered: " + this.activityList.size());
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\RunningActivityManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
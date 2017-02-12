package com.resmed.refresh.ui.activity;

import android.os.Bundle;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.uibase.base.BaseActivity;

public class LandingActivty
  extends BaseActivity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903076);
    changeRefreshBarVisibility(false, false);
    setTypeRefreshBar(BaseActivity.TypeBar.NO_BED);
  }
  
  protected void onResume()
  {
    super.onResume();
    RefreshModelController.getInstance().setupNotifications(this);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\LandingActivty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
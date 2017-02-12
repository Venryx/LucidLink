package com.resmed.refresh.ui.activity;

import android.os.Bundle;

import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseActivity;

public class ReconnectionProblemActivty
  extends BaseActivity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903085);
    setIsModalActivity(true);
    setTitle(2131165871);
    setTypeRefreshBar(BaseActivity.TypeBar.NO_BED);
  }
  
  protected void updateConnectionIcon()
  {
    try
    {
      super.updateConnectionIcon();
      if (RefreshApplication.getInstance().getConnectionStatus().isSocketConnected()) {
        onBackPressed();
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\ReconnectionProblemActivty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
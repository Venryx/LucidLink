package com.resmed.refresh.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.ui.uibase.base.BaseActivity;

public class SPlusMentorActivity
  extends BaseActivity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (PreferenceManager.getDefaultSharedPreferences(this).getInt("PREF_CONNECTION_STATE", -1) == CONNECTION_STATE.NIGHT_TRACK_ON.ordinal()) {
      finish();
    }
    for (;;)
    {
      return;
      setContentView(2130903099);
      setTypeRefreshBar(BaseActivity.TypeBar.DEFAULT);
      setTitle(2131165962);
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\SPlusMentorActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
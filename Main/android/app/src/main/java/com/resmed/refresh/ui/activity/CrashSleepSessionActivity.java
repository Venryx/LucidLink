package com.resmed.refresh.ui.activity;

import android.os.Bundle;

import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.ui.fragments.CrashFragment;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.utils.Log;

public class CrashSleepSessionActivity
  extends BaseBluetoothActivity
{
  private CrashFragment crashFragment;
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    super.handleConnectionStatus(paramCONNECTION_STATE);
    if (this.crashFragment != null) {
      this.crashFragment.handleConnectionStatus(paramCONNECTION_STATE);
    }
  }
  
  public void handleSessionRecovered(Bundle paramBundle)
  {
    super.handleSessionRecovered(paramBundle);
    Log.d("com.resmed.refresh.ui", " CrashSleepSessionActivity::handleSessionRecovered()");
  }
  
  public void onBackPressed() {}
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903084);
    setTitle(2131166107);
    setIsModalActivity(true);
    setTypeRefreshBar(BaseActivity.TypeBar.NO_BED);
    paramBundle = getSupportFragmentManager().beginTransaction();
    this.crashFragment = new CrashFragment();
    paramBundle.replace(2131099803, this.crashFragment);
    paramBundle.commit();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\CrashSleepSessionActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
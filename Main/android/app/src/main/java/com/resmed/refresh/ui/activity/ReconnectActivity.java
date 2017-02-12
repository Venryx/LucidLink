package com.resmed.refresh.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.ui.fragments.ReconnectFragment;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;

public class ReconnectActivity
  extends BaseBluetoothActivity
{
  private ReconnectFragment reconnectFragment;
  
  public void finish(int paramInt)
  {
    Intent localIntent = new Intent();
    if (getParent() == null) {
      setResult(paramInt, localIntent);
    }
    for (;;)
    {
      super.onBackPressed();
      return;
      getParent().setResult(paramInt, localIntent);
    }
  }
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    super.handleConnectionStatus(paramCONNECTION_STATE);
    if (this.reconnectFragment != null) {
      this.reconnectFragment.handleConnectionStatus(paramCONNECTION_STATE);
    }
  }
  
  public void handleSleepSessionStopped(Bundle paramBundle)
  {
    super.handleSleepSessionStopped(paramBundle);
    if (this.reconnectFragment != null) {
      this.reconnectFragment.handleSleepSessionStopped(paramBundle);
    }
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
    this.reconnectFragment = new ReconnectFragment();
    paramBundle.replace(2131099803, this.reconnectFragment);
    paramBundle.commit();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\ReconnectActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
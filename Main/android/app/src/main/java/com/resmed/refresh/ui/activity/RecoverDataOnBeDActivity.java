package com.resmed.refresh.ui.activity;

import android.os.Bundle;

import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.ui.fragments.RecoverDataFragment;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;

public class RecoverDataOnBeDActivity
  extends BaseBluetoothActivity
{
  private RecoverDataFragment manageDataFragment;
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    super.handleConnectionStatus(paramCONNECTION_STATE);
    if (this.manageDataFragment != null) {
      this.manageDataFragment.handleConnectionStatus(paramCONNECTION_STATE);
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
    this.manageDataFragment = new RecoverDataFragment();
    paramBundle.replace(2131099803, this.manageDataFragment);
    paramBundle.commit();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\RecoverDataOnBeDActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
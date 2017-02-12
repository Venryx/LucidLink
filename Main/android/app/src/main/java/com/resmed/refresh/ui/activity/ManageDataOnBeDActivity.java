package com.resmed.refresh.ui.activity;

import android.os.Bundle;

import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.ui.fragments.ManageDataOnBeDFragment;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;

public class ManageDataOnBeDActivity
  extends BaseBluetoothActivity
  implements BluetoothDataListener
{
  ManageDataOnBeDFragment manageDataFragment;
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    super.handleConnectionStatus(paramCONNECTION_STATE);
    if (this.manageDataFragment != null) {
      this.manageDataFragment.handleConnectionStatus(paramCONNECTION_STATE);
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903084);
    setTitle(2131166107);
    setIsModalActivity(true);
    paramBundle = getSupportFragmentManager().beginTransaction();
    this.manageDataFragment = new ManageDataOnBeDFragment();
    paramBundle.replace(2131099803, this.manageDataFragment);
    paramBundle.commit();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\ManageDataOnBeDActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
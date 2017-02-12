package com.resmed.refresh.ui.activity;

import android.os.Bundle;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;

public class EmailNotValidatedActivity
  extends BaseBluetoothActivity
  implements BluetoothDataListener
{
  public void onBackPressed()
  {
    finish();
    overridePendingTransition(2130968586, 2130968586);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903069);
    setTitle(2131166095);
    setIsModalActivity(true);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\EmailNotValidatedActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
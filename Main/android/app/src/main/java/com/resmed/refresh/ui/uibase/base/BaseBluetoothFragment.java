package com.resmed.refresh.ui.uibase.base;

import android.os.Bundle;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;

public abstract class BaseBluetoothFragment
  extends BaseFragment
{
  public BaseBluetoothActivity getBaseActivity()
  {
    return (BaseBluetoothActivity)getActivity();
  }
  
  public abstract void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE);
  
  public abstract void handleStreamPacket(Bundle paramBundle);
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\uibase\base\BaseBluetoothFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
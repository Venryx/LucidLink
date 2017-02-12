package com.resmed.refresh.bluetooth.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.utils.RefreshBluetoothManager;

public class BluetoothStateChangesReceiver
  extends BroadcastReceiver
{
  private RefreshBluetoothManager bluetoothManager;
  
  public BluetoothStateChangesReceiver(RefreshBluetoothManager paramRefreshBluetoothManager)
  {
    this.bluetoothManager = paramRefreshBluetoothManager;
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    int i;
    if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(paramIntent.getAction()))
    {
      i = paramIntent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
      if (12 != i) {
        break label40;
      }
      this.bluetoothManager.setConnectionStatusAndNotify(CONNECTION_STATE.BLUETOOTH_ON, true);
    }
    for (;;)
    {
      return;
      label40:
      if (10 == i) {
        this.bluetoothManager.setConnectionStatusAndNotify(CONNECTION_STATE.BLUETOOTH_OFF, true);
      }
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
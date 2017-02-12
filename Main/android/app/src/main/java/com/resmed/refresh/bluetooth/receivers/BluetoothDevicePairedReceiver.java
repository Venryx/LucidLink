package com.resmed.refresh.bluetooth.receivers;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshBluetoothManager;

public class BluetoothDevicePairedReceiver
  extends BroadcastReceiver
{
  private RefreshBluetoothManager bluetoothManager;
  
  public BluetoothDevicePairedReceiver(RefreshBluetoothManager paramRefreshBluetoothManager)
  {
    this.bluetoothManager = paramRefreshBluetoothManager;
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    paramContext = paramIntent.getAction();
    Log.d("com.resmed.refresh.bluetooth", " BroadcastReceiver Action : " + paramContext);
    int i;
    if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(paramContext))
    {
      paramContext = (BluetoothDevice)paramIntent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
      i = paramIntent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", -1);
      Log.d("com.resmed.refresh.bluetooth", " BLUETOOTH DEVICE PAIRED! Name : " + paramContext.getName() + " Bound : " + paramContext.getAddress() + " bonded state : " + i);
      if ((paramContext == null) || (12 != i)) {
        break label131;
      }
      this.bluetoothManager.cancelDiscovery();
      this.bluetoothManager.connectDevice(paramContext);
    }
    for (;;)
    {
      return;
      label131:
      if (((paramContext == null) || (11 != i)) && (paramContext != null) && (10 == i)) {
        this.bluetoothManager.connectDevice(paramContext);
      }
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
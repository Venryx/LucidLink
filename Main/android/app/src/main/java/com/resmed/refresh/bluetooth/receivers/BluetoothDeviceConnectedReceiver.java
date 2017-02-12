package com.resmed.refresh.bluetooth.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resmed.refresh.utils.RefreshBluetoothManager;

public class BluetoothDeviceConnectedReceiver
  extends BroadcastReceiver
{
  private RefreshBluetoothManager bluetoothManager;
  
  public BluetoothDeviceConnectedReceiver(RefreshBluetoothManager paramRefreshBluetoothManager)
  {
    this.bluetoothManager = paramRefreshBluetoothManager;
  }
  
  public void onReceive(Context paramContext, Intent paramIntent) {}
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
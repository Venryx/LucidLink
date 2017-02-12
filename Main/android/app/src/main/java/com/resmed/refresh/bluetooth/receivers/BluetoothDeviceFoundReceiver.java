package com.resmed.refresh.bluetooth.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resmed.refresh.utils.RefreshBluetoothManager;

public class BluetoothDeviceFoundReceiver
  extends BroadcastReceiver
{
  private RefreshBluetoothManager bluetoothManager;
  
  public BluetoothDeviceFoundReceiver(RefreshBluetoothManager paramRefreshBluetoothManager)
  {
    this.bluetoothManager = paramRefreshBluetoothManager;
  }
  
  public void onReceive(Context paramContext, Intent paramIntent) {}
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\bluetooth\receivers\BluetoothDeviceFoundReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
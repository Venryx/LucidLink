package com.resmed.refresh.bluetooth.receivers;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshBluetoothManager;

public class BluetoothDeviceDisconnectedReceiver
  extends BroadcastReceiver
{
  private RefreshBluetoothManager bluetoothManager;
  
  public BluetoothDeviceDisconnectedReceiver(RefreshBluetoothManager paramRefreshBluetoothManager)
  {
    this.bluetoothManager = paramRefreshBluetoothManager;
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if ("android.bluetooth.device.action.ACL_DISCONNECTED".equals(paramIntent.getAction()))
    {
      paramContext = (BluetoothDevice)paramIntent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
      if (CONNECTION_STATE.SOCKET_CONNECTED != this.bluetoothManager.getConnectionStatus()) {
        break label76;
      }
    }
    label76:
    for (boolean bool = true;; bool = false)
    {
      Log.d("com.resmed.refresh.bluetooth", " Bluetooth previous connected : " + bool);
      if (bool) {
        this.bluetoothManager.connectDevice(paramContext);
      }
      return;
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\bluetooth\receivers\BluetoothDeviceDisconnectedReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
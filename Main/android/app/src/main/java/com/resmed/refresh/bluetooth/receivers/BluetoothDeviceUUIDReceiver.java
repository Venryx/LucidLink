package com.resmed.refresh.bluetooth.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resmed.refresh.utils.Log;

public class BluetoothDeviceUUIDReceiver
  extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    paramContext = paramIntent.getAction();
    Log.d("com.resmed.refresh.bluetooth", " bluetooth uuid : " + paramIntent.hasExtra("android.bluetooth.device.extra.UUID"));
    if ("android.bluetooth.device.action.UUID".equals(paramContext)) {
      Log.d("com.resmed.refresh.bluetooth", " bluetooth uuid : " + paramIntent.getParcelableArrayExtra("android.bluetooth.device.extra.UUID"));
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.net.push;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GCMBroadcastReceiver
  extends WakefulBroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    startWakefulService(paramContext, paramIntent.setComponent(new ComponentName(paramContext.getPackageName(), GCMNotificationService.class.getName())));
    setResultCode(-1);
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
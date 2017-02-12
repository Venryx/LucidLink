package com.resmed.refresh.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.utils.SmartAlarmDataManager;

public class TimeChangeReceiver
  extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    Log.e("com.resmed.refresh.smartAlarm", "TimeChangeReceiver:" + paramIntent.getAction());
    AppFileLog.addTrace("SmartAlarm TimeChangeReceiver:" + paramIntent.getAction());
    if ((RefreshModelController.getInstance().isLoggedIn()) && (SmartAlarmDataManager.getInstance().getActiveAlarm())) {
      SmartAlarmDataManager.getInstance().onDeviceDateTimeChanged();
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
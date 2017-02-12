package com.resmed.refresh.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.utils.SmartAlarmDataManager;

public class BootReceiver
  extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if (("android.intent.action.BOOT_COMPLETED".equals(paramIntent.getAction())) && (RefreshModelController.getInstance().isLoggedIn()) && (SmartAlarmDataManager.getInstance().getActiveAlarm())) {
      SmartAlarmDataManager.getInstance().onReboot();
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\BootReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
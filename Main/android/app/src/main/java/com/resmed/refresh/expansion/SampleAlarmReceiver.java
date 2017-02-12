package com.resmed.refresh.expansion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;

public class SampleAlarmReceiver
  extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    try
    {
      DownloaderClientMarshaller.startDownloadServiceIfRequired(paramContext, paramIntent, SampleDownloaderService.class);
      return;
    }
    catch (PackageManager.NameNotFoundException paramContext)
    {
      for (;;)
      {
        paramContext.printStackTrace();
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\expansion\SampleAlarmReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
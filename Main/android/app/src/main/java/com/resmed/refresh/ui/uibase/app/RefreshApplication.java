package com.resmed.refresh.ui.uibase.app;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.flurry.android.FlurryAgent;
import com.resmed.refresh.bluetooth.BeDConnectionStatus;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.RefreshSyncService;
import com.resmed.refresh.model.json.LocaleMUnitResponse;
import com.resmed.refresh.ui.activity.DismissAlarmActivty;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.utils.Log;

import java.util.Calendar;
import java.util.Locale;
import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formKey="", mailTo="RstAndroidTest@ResMed.com", mode=ReportingInteractionMode.TOAST, resToastText=2131165352)
public class RefreshApplication
  extends Application
{
  private static int activitiesInForeground = 0;
  private static RefreshApplication instance;
  public static boolean is64bitdevice = false;
  private static boolean isPlayingAlarm = false;
  public static String userCountry = "";
  public static LocaleMUnitResponse userMeasurementUnitMappingObj = Consts.DEFAULT_USER_MEASURMENT_MAPPING;
  protected BeDConnectionStatus connectionStatus;
  
  public static RefreshApplication getInstance()
  {
    try
    {
      RefreshApplication localRefreshApplication = instance;
      return localRefreshApplication;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  private void startSyncService()
  {
    if (!Consts.UNIT_TEST_MODE)
    {
      Calendar localCalendar = Calendar.getInstance();
      localCalendar.setTimeInMillis(System.currentTimeMillis());
      localCalendar.add(12, 5);
      Object localObject = new Intent(getApplicationContext(), RefreshSyncService.class);
      localObject = PendingIntent.getService(getApplicationContext(), 0, (Intent)localObject, 0);
      ((AlarmManager)getSystemService("alarm")).setRepeating(1, localCalendar.getTimeInMillis(), 3600000L, (PendingIntent)localObject);
    }
  }
  
  public void decreaseActivitiesInForeground()
  {
    activitiesInForeground -= 1;
  }
  
  public int getAppVersion()
  {
    int i = 0;
    try
    {
      int j = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionCode;
      i = j;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      for (;;)
      {
        localNameNotFoundException.printStackTrace();
      }
    }
    return i;
  }
  
  public BeDConnectionStatus getConnectionStatus()
  {
    return this.connectionStatus;
  }
  
  public CONNECTION_STATE getCurrentConnectionState()
  {
    return this.connectionStatus.getState();
  }
  
  public void increaseActivitiesInForeground()
  {
    activitiesInForeground += 1;
  }
  
  public boolean isAppInForeground()
  {
    Log.d("com.resmed.refresh.sync", "activitiesInForeground=" + activitiesInForeground);
    if (activitiesInForeground > 0) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public boolean isConnectedToInternet()
  {
    bool2 = false;
    try
    {
      NetworkInfo localNetworkInfo = ((ConnectivityManager)getSystemService("connectivity")).getActiveNetworkInfo();
      bool1 = bool2;
      if (localNetworkInfo != null)
      {
        boolean bool3 = localNetworkInfo.isConnected();
        bool1 = bool2;
        if (bool3) {
          bool1 = true;
        }
      }
    }
    catch (Exception localException)
    {
      for (;;)
      {
        boolean bool1 = bool2;
      }
    }
    return bool1;
  }
  
  public boolean isPlayingAlarm()
  {
    return isPlayingAlarm;
  }
  
  public void onCreate()
  {
    if (Consts.USE_ACRA_REPORTS)
    {
      localObject = ACRA.getNewDefaultConfig(this);
      ((ACRAConfiguration)localObject).setResToastText(2131165352);
      ACRA.setConfig((ACRAConfiguration)localObject);
      ACRA.init(this);
    }
    super.onCreate();
    if (Consts.USE_FLURRY_REPORTS)
    {
      FlurryAgent.setLogEnabled(true);
      FlurryAgent.init(this, Consts.FLURRY_API_KEY);
    }
    Object localObject = getResources().getConfiguration().locale;
    userCountry = ((Locale)localObject).getCountry();
    System.out.println("************Current Locale0 ::" + ((Locale)localObject).toString());
    System.out.println("************Current Locale1 ::" + ((Locale)localObject).getCountry());
    System.out.println("************Current Locale2 ::" + ((Locale)localObject).getDisplayLanguage());
    System.out.println("************Current Locale3 ::" + ((Locale)localObject).getDisplayName());
    System.out.println("************Current Locale4 ::" + ((Locale)localObject).getLanguage());
    if (System.getProperty("os.arch").equalsIgnoreCase("aarch64")) {
      is64bitdevice = true;
    }
    instance = this;
    this.connectionStatus = BeDConnectionStatus.getInstance();
    startSyncService();
  }
  
  public void setCurrentConnectionState(CONNECTION_STATE paramCONNECTION_STATE)
  {
    this.connectionStatus.updateState(paramCONNECTION_STATE);
  }
  
  public void setPlayingAlarm(boolean paramBoolean)
  {
    isPlayingAlarm = paramBoolean;
  }
  
  public void startActivity(Intent paramIntent)
  {
    if (isPlayingAlarm) {
      super.startActivities(new Intent[] { paramIntent, new Intent(this, DismissAlarmActivty.class) });
    }
    for (;;)
    {
      return;
      super.startActivity(paramIntent);
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
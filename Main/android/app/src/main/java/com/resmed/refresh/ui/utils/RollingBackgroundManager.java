package com.resmed.refresh.ui.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.SystemClock;
import com.resmed.refresh.model.RST_LocationManager;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.utils.LocationCallback;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.TimeCalculator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class RollingBackgroundManager
  implements LocationCallback
{
  private static RollingBackgroundManager instance;
  private final String CHANGE_BKG = "change_background";
  private final int DAWN_PERIOD = 0;
  private final int DAWN_SECONDS = 21600;
  private final int DAY_PERIOD = 1;
  private final int DAY_SECONDS = 28800;
  private final int EVENING_PERIOD = 2;
  private final int EVENING_SECONDS = 64800;
  private final int MID_NIGHT_SECONDS = 86400;
  private final int NIGHT_PERIOD = 3;
  private final int NIGHT_SECONDS = 72000;
  private List<BaseActivity> activityList;
  private AlarmManager alarmManager;
  private Context context;
  private String currentBkgImage;
  private int currentPeriod;
  private RST_LocationManager locationManager;
  private PendingIntent pendingIntent;
  private BroadcastReceiver receiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      RollingBackgroundManager.this.notifyAllActivity();
    }
  };
  private final BroadcastReceiver timeChangedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = paramAnonymousIntent.getAction();
      if (((paramAnonymousContext.equals("android.intent.action.TIME_SET")) || (paramAnonymousContext.equals("android.intent.action.TIMEZONE_CHANGED"))) && (RollingBackgroundManager.this.currentBkgImage != RollingBackgroundManager.this.fetchCurrentImage())) {
        RollingBackgroundManager.this.notifyAllActivity();
      }
    }
  };
  
  private RollingBackgroundManager(Context paramContext)
  {
    this.context = paramContext;
    this.activityList = new ArrayList();
    this.pendingIntent = PendingIntent.getBroadcast(paramContext, 123456, new Intent("change_background"), 0);
    this.alarmManager = ((AlarmManager)paramContext.getSystemService("alarm"));
    this.locationManager = RefreshModelController.getInstance().getLocationManager();
    this.locationManager.registerCallback(instance);
    this.currentBkgImage = fetchCurrentImage();
    paramContext.registerReceiver(this.receiver, new IntentFilter("change_background"));
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.intent.action.TIME_TICK");
    localIntentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
    localIntentFilter.addAction("android.intent.action.TIME_SET");
    paramContext.registerReceiver(this.timeChangedReceiver, localIntentFilter);
  }
  
  private String fetchCurrentImage()
  {
    int i = TimeCalculator.getTodaySecondsSpent().intValue();
    String str;
    if ((i >= 21600) && (i < 28800))
    {
      this.currentPeriod = 0;
      setupAlarmManager(i);
      str = DrawableName.DAWN.getFileName();
    }
    for (;;)
    {
      return str;
      if ((i >= 28800) && (i < 64800))
      {
        this.currentPeriod = 1;
        setupAlarmManager(i);
        str = DrawableName.DAY.getFileName();
      }
      else if ((i >= 64800) && (i < 72000))
      {
        this.currentPeriod = 2;
        setupAlarmManager(i);
        str = DrawableName.EVENING.getFileName();
      }
      else
      {
        this.currentPeriod = 3;
        setupAlarmManager(i);
        str = DrawableName.NIGHT.getFileName();
      }
    }
  }
  
  public static RollingBackgroundManager getInstance(Context paramContext)
  {
    if (instance == null) {
      instance = new RollingBackgroundManager(paramContext);
    }
    return instance;
  }
  
  private boolean isPresent(BaseActivity paramBaseActivity)
  {
    boolean bool = false;
    Iterator localIterator = this.activityList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return bool;
      }
      if ((BaseActivity)localIterator.next() == paramBaseActivity) {
        bool = true;
      }
    }
  }
  
  private void notifyAllActivity()
  {
    this.currentBkgImage = fetchCurrentImage();
    Iterator localIterator = this.activityList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      ((BaseActivity)localIterator.next()).onBackgroundChange(this.currentBkgImage);
    }
  }
  
  private void setupAlarmManager(int paramInt)
  {
    int k = 0;
    int i = 0;
    if (this.currentPeriod == 0) {
      i = 28800 - paramInt;
    }
    for (;;)
    {
      System.out.println(SystemClock.elapsedRealtime());
      Calendar localCalendar = Calendar.getInstance();
      localCalendar.setTimeInMillis(System.currentTimeMillis());
      localCalendar.add(13, i);
      Log.i("com.resmed.refresh", "rolling background will change in " + i + " seconds");
      Log.i("com.resmed.refresh", "rolling background will change in " + localCalendar.getTimeInMillis() + " milliseconds");
      this.alarmManager.set(0, localCalendar.getTimeInMillis(), this.pendingIntent);
      return;
      if (this.currentPeriod == 1)
      {
        i = 64800 - paramInt;
      }
      else if (this.currentPeriod == 2)
      {
        i = 72000 - paramInt;
      }
      else if (this.currentPeriod == 3)
      {
        int j = k;
        if (paramInt > 72000)
        {
          j = k;
          if (paramInt < 86400) {
            j = 86400 - paramInt + 21600;
          }
        }
        i = j;
        if (paramInt < 21600)
        {
          i = j;
          if (paramInt > 0)
          {
            i = 21600 - paramInt;
            Log.i("com.resmed.refresh", "Morning time to change is --" + i + " seconds");
          }
        }
      }
    }
  }
  
  public void allProviderAreDisabled() {}
  
  protected void finalize()
  {
    this.alarmManager.cancel(this.pendingIntent);
    this.context.unregisterReceiver(this.receiver);
    this.context.unregisterReceiver(this.timeChangedReceiver);
    this.locationManager.unregisterCallback(instance);
  }
  
  public String getCurrentBackground()
  {
    return this.currentBkgImage;
  }
  
  public void onLocationUpdateReceived(Location paramLocation) {}
  
  public String registerActivity(BaseActivity paramBaseActivity)
  {
    if (!isPresent(paramBaseActivity)) {
      this.activityList.add(paramBaseActivity);
    }
    return this.currentBkgImage;
  }
  
  public void unRegisterActivity(BaseActivity paramBaseActivity)
  {
    this.activityList.remove(paramBaseActivity);
  }
  
  public static enum DrawableName
  {
    DAWN("dawn_bkg"),  DAY("day_bkg"),  EVENING("evening_bkg"),  NIGHT("night_bkg");
    
    private String fileName;
    
    private DrawableName(String paramString1)
    {
      this.fileName = paramString1;
    }
    
    public String getFileName()
    {
      return this.fileName;
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
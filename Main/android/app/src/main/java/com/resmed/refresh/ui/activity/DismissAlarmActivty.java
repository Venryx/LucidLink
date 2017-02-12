package com.resmed.refresh.ui.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import com.resmed.refresh.smartalarm.services.SmartAlarmService;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.utils.SmartAlarmDataManager;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.VolumeChangeCallback;
import com.resmed.refresh.utils.VolumeObserver;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DismissAlarmActivty
  extends Activity
  implements View.OnClickListener, VolumeChangeCallback
{
  public static final int ALARM_MANAGER = 0;
  public static final String MUSIC_VOLUME = "music_volume";
  public static final int RM20 = 1;
  private final SimpleDateFormat _sdfHourTime = new SimpleDateFormat("HH");
  private final SimpleDateFormat _sdfMinuteTime = new SimpleDateFormat("mm");
  private IntentFilter filter;
  private int musicVolume;
  private BroadcastReceiver receiverClockTick = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      DismissAlarmActivty.this.handleClockTick();
    }
  };
  private TextView tvDismissAlarmButton;
  private TextView tvDismissAlarmColon;
  private TextView tvDismissAlarmHour;
  private TextView tvDismissAlarmMinute;
  
  private void mapGUI()
  {
    this.tvDismissAlarmButton = ((TextView)findViewById(2131099771));
    this.tvDismissAlarmHour = ((TextView)findViewById(2131099772));
    this.tvDismissAlarmMinute = ((TextView)findViewById(2131099774));
    this.tvDismissAlarmColon = ((TextView)findViewById(2131099773));
  }
  
  private void playAlarm()
  {
    RefreshApplication.getInstance().setPlayingAlarm(true);
    Intent localIntent = new Intent(this, SmartAlarmService.class);
    Bundle localBundle = new Bundle();
    localBundle.putInt("BUNDLE_SMART_ALARM_ACTION", 0);
    localIntent.putExtras(localBundle);
    startService(localIntent);
  }
  
  private void setupListeners()
  {
    this.tvDismissAlarmButton.setOnClickListener(this);
    VolumeObserver localVolumeObserver = new VolumeObserver(this, this);
    getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, localVolumeObserver);
  }
  
  private void stopAlarm()
  {
    RefreshApplication.getInstance().setPlayingAlarm(false);
    Intent localIntent = new Intent(this, SmartAlarmService.class);
    Bundle localBundle = new Bundle();
    localBundle.putInt("BUNDLE_SMART_ALARM_ACTION", 1);
    localBundle.putInt("music_volume", this.musicVolume);
    localIntent.putExtras(localBundle);
    startService(localIntent);
  }
  
  public void handleClockTick()
  {
    this.tvDismissAlarmHour.setText(this._sdfHourTime.format(new Date()));
    this.tvDismissAlarmMinute.setText(this._sdfMinuteTime.format(new Date()));
  }
  
  public boolean isApplicationSentToBackground(Context paramContext)
  {
    List localList = ((ActivityManager)paramContext.getSystemService("activity")).getRunningTasks(1);
    if ((!localList.isEmpty()) && (!((ActivityManager.RunningTaskInfo)localList.get(0)).topActivity.getPackageName().equals(paramContext.getPackageName()))) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public void onBackPressed()
  {
    Log.d("com.resmed.refresh.smartAlarm", "onBackPressed stopAlarm");
    stopAlarm();
    finish();
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    }
    for (;;)
    {
      return;
      Log.d("com.resmed.refresh.smartAlarm", "onClick stopAlarm");
      stopAlarm();
      finish();
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Log.d("com.resmed.refresh.smartAlarm", "DismissAlarmFragment onCreate");
    paramBundle = SmartAlarmDataManager.getInstance();
    this.musicVolume = ((AudioManager)RefreshApplication.getInstance().getSystemService("audio")).getStreamVolume(3);
    if (!paramBundle.getActiveAlarm()) {
      finish();
    }
    for (;;)
    {
      return;
      if (!paramBundle.isWrongScheduledForWeekend()) {
        break;
      }
      finish();
    }
    AppFileLog.addTrace("SmartAlarm DismissAlarmActivty displayed");
    if (1 == getIntent().getIntExtra("BUNDLE_SMART_ALARM_ACTION", -1))
    {
      Log.d("com.resmed.refresh.smartAlarm", "DismissAlarmFragment RM20 dismissing end of window");
      paramBundle.dismissNextAlarm();
    }
    for (;;)
    {
      getWindow().addFlags(6815872);
      setContentView(2130903068);
      mapGUI();
      setupListeners();
      this.filter = new IntentFilter();
      this.filter.addAction("android.intent.action.TIME_TICK");
      paramBundle = new AlphaAnimation(0.0F, 1.0F);
      paramBundle.setDuration(500L);
      paramBundle.setStartOffset(20L);
      paramBundle.setRepeatMode(2);
      paramBundle.setRepeatCount(-1);
      this.tvDismissAlarmColon.startAnimation(paramBundle);
      handleClockTick();
      playAlarm();
      break;
      Log.d("com.resmed.refresh.smartAlarm", "DismissAlarmFragment Re scheduling next one");
      paramBundle.scheduleAgain();
    }
  }
  
  protected void onResume()
  {
    super.onResume();
    try
    {
      registerReceiver(this.receiverClockTick, this.filter);
      handleClockTick();
      return;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        localException.printStackTrace();
      }
    }
  }
  
  protected void onStop()
  {
    if (isApplicationSentToBackground(this))
    {
      Log.d("com.resmed.refresh.smartAlarm", "onStop stopAlarm");
      stopAlarm();
      finish();
    }
    super.onStop();
    try
    {
      unregisterReceiver(this.receiverClockTick);
      return;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        localException.printStackTrace();
      }
    }
  }
  
  public void onVolumeChange(int paramInt)
  {
    if (paramInt == 0)
    {
      Log.d("com.resmed.refresh.smartAlarm", "DismissAlarmFragment onVolumeOff");
      Log.d("com.resmed.refresh.smartAlarm", "onVolumeChange stopAlarm");
      stopAlarm();
      finish();
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\DismissAlarmActivty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
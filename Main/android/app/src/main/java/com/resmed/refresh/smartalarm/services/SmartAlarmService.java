package com.resmed.refresh.smartalarm.services;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.utils.SmartAlarmDataManager;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.audio.PlaySoundManager;
import com.resmed.refresh.utils.audio.SoundResources.SmartAlarmSound;

public class SmartAlarmService
  extends IntentService
{
  private static final int ALARM_TIMETOUT = 180000;
  public static final int PLAY = 0;
  public static final int SNOOZE = 2;
  public static final int STOP = 1;
  private static final int VOLUME_START = 1;
  private static final int VOLUME_STEP = 10000;
  private static boolean isPlaying = false;
  private int musicVolume;
  private SmartAlarmDataManager smartAlarmDataManager = SmartAlarmDataManager.getInstance();
  final PlaySoundManager soundManager = PlaySoundManager.getInstance();
  private Thread threadVolume;
  
  public SmartAlarmService()
  {
    super("SmartAlarmService");
  }
  
  public SmartAlarmService(String paramString)
  {
    super(paramString);
  }
  
  private void increaseVoume()
  {
    AudioManager localAudioManager = (AudioManager)RefreshApplication.getInstance().getSystemService("audio");
    int i = localAudioManager.getStreamMaxVolume(3);
    int j = localAudioManager.getStreamVolume(3);
    if (j < i)
    {
      j++;
      localAudioManager.setStreamVolume(3, j, 0);
      Log.d("com.resmed.refresh.smartAlarm", "setVolume currentVolume=" + j + " maxVolume=" + i);
    }
  }
  
  private void playAlarm()
  {
    SoundResources.SmartAlarmSound localSmartAlarmSound = this.smartAlarmDataManager.getSoundResource();
    Log.d("com.resmed.refresh.smartAlarm", "playAlarm id " + localSmartAlarmSound.getId());
    ((AudioManager)RefreshApplication.getInstance().getSystemService("audio")).requestAudioFocus(null, 3, 2);
    this.soundManager.playAlarm(localSmartAlarmSound.getAssetFileDescriptor(), 1);
    startVolumeThread();
    isPlaying = true;
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        if (SmartAlarmService.isPlaying) {
          SmartAlarmService.this.stopAudio();
        }
      }
    }, 180000L);
  }
  
  private void startVolumeThread()
  {
    this.threadVolume = new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          Thread.sleep(10000L);
          if ((!SmartAlarmService.isPlaying) || (SmartAlarmService.this.soundManager == null))
          {
            Log.d("com.resmed.refresh.smartAlarm", "SmartAlarmService threadVolume over");
            return;
          }
        }
        catch (Exception localException1)
        {
          for (;;)
          {
            localException1.printStackTrace();
            continue;
            SmartAlarmService.this.increaseVoume();
            try
            {
              Thread.sleep(10000L);
            }
            catch (Exception localException2)
            {
              localException2.printStackTrace();
            }
          }
        }
      }
    });
    this.threadVolume.start();
  }
  
  private void stopAudio()
  {
    isPlaying = false;
    this.soundManager.stopAudio();
    Log.i("com.resmed.refresh.smartAlarm", "stop alarm sound");
    ((AudioManager)RefreshApplication.getInstance().getSystemService("audio")).setStreamVolume(3, this.musicVolume, 0);
  }
  
  protected void onHandleIntent(Intent paramIntent)
  {
    int i = paramIntent.getExtras().getInt("BUNDLE_SMART_ALARM_ACTION");
    Log.d("com.resmed.refresh.smartAlarm", "SmartAlarmService onHandleIntent handle action=" + i);
    this.musicVolume = paramIntent.getExtras().getInt("music_volume");
    switch (i)
    {
    }
    for (;;)
    {
      return;
      playAlarm();
      continue;
      stopAudio();
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\smartalarm\services\SmartAlarmService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
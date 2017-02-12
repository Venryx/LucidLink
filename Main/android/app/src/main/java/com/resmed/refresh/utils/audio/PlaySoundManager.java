package com.resmed.refresh.utils.audio;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.Log;

public class PlaySoundManager
{
  private static final float MIN_VOLUME = 0.4F;
  private static PlaySoundManager playSoundAudioTrack;
  private AssetFileDescriptor currentFileName;
  private MediaPlayer player;
  
  public static PlaySoundManager getInstance()
  {
    if (playSoundAudioTrack == null) {
      playSoundAudioTrack = new PlaySoundManager();
    }
    return playSoundAudioTrack;
  }
  
  private void play(AssetFileDescriptor paramAssetFileDescriptor, int paramInt)
  {
    Log.e("com.resmed.refresh.relax", "playAudio " + paramAssetFileDescriptor);
    if (paramAssetFileDescriptor == null) {}
    for (;;)
    {
      return;
      if (this.player == null)
      {
        this.player = new MediaPlayer();
        this.player.setAudioStreamType(3);
      }
      AudioManager localAudioManager = (AudioManager)RefreshApplication.getInstance().getSystemService("audio");
      int k = localAudioManager.getStreamMaxVolume(3);
      float f = localAudioManager.getStreamVolume(3) / k;
      Log.d("com.resmed.refresh.smartAlarm", "playAudio startVolume=" + paramInt + " currentVolume=" + localAudioManager.getStreamVolume(3) + " currentVolume(float)=" + f + " maxVolume=" + k);
      if (paramInt == -1) {
        if (f < 0.4F)
        {
          int j = Math.round(k * 0.4F);
          int i = j;
          if (j > k) {
            i = k;
          }
          Log.d("com.resmed.refresh.smartAlarm", "CHANGED volume " + i);
          localAudioManager.setStreamVolume(3, i, 0);
          f = localAudioManager.getStreamVolume(3);
        }
      }
      for (;;)
      {
        if (!paramAssetFileDescriptor.equals(this.currentFileName)) {
          break label293;
        }
        try
        {
          this.player.setDataSource(paramAssetFileDescriptor.getFileDescriptor(), paramAssetFileDescriptor.getStartOffset(), paramAssetFileDescriptor.getLength());
          this.player.prepare();
          this.player.setLooping(true);
          this.player.start();
        }
        catch (Exception paramAssetFileDescriptor)
        {
          paramAssetFileDescriptor.printStackTrace();
        }
        break;
        localAudioManager.setStreamVolume(3, paramInt, 0);
      }
      label293:
      this.currentFileName = paramAssetFileDescriptor;
      stopAudio();
      play(paramAssetFileDescriptor, paramInt);
    }
  }
  
  public void finalize()
  {
    stopAudio();
  }
  
  public void playAlarm(AssetFileDescriptor paramAssetFileDescriptor, int paramInt)
  {
    play(paramAssetFileDescriptor, paramInt);
  }
  
  public void playAudio(AssetFileDescriptor paramAssetFileDescriptor)
  {
    play(paramAssetFileDescriptor, -1);
  }
  
  public void stopAudio()
  {
    if (this.player != null)
    {
      this.player.stop();
      this.player.release();
    }
    this.player = null;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\audio\PlaySoundManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
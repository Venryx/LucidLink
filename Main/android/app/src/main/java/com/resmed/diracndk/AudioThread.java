package com.resmed.diracndk;

import android.media.AudioTrack;
import android.util.Log;
import java.util.concurrent.ArrayBlockingQueue;

public class AudioThread
  extends Thread
{
  public ArrayBlockingQueue<AudioPacket> itemsToPlay = new ArrayBlockingQueue(2);
  public AudioTrack mMusicTrack;
  private int mSampleRate = 44100;
  private boolean mStopAudioThreads;
  
  public AudioThread(int paramInt)
  {
    this.mSampleRate = paramInt;
    start();
  }
  
  public void play(AudioPacket paramAudioPacket)
  {
    try
    {
      this.itemsToPlay.put(paramAudioPacket);
      return;
    }
    catch (InterruptedException paramAudioPacket)
    {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Unexpected interruption");
    }
  }
  
  public void run()
  {
    int j = AudioTrack.getMinBufferSize(this.mSampleRate, 12, 2) * 2;
    Log.i("Java_DiracDefaultManager", "bufferSize = " + j);
    DiracFileLog.addTrace("AudioThread run bufferSize:" + j);
    int i = 0;
    this.mMusicTrack = new AudioTrack(3, this.mSampleRate, 12, 2, j, 1);
    DiracFileLog.addTrace("AudioThread mMusicTrack created");
    for (;;)
    {
      if (this.mMusicTrack.getState() == 1) {
        DiracFileLog.addTrace("AudioThread mMusicTrack state:" + this.mMusicTrack.getState());
      }
      try
      {
        this.mMusicTrack.play();
        if (this.mStopAudioThreads)
        {
          DiracFileLog.addTrace("AudioThread mStopAudioThreads:" + this.mStopAudioThreads + " mMusicTrack:" + this.mMusicTrack);
          if (this.mMusicTrack == null) {}
        }
        try
        {
          StringBuilder localStringBuilder1 = new java/lang/StringBuilder;
          localStringBuilder1.<init>("AudioThread mMusicTrack.getState:");
          DiracFileLog.addTrace(this.mMusicTrack.getState());
          if (this.mMusicTrack.getState() == 1)
          {
            this.mMusicTrack.flush();
            this.mMusicTrack.stop();
          }
          return;
        }
        catch (IllegalStateException localIllegalStateException2)
        {
          for (;;)
          {
            AudioPacket localAudioPacket;
            StringBuilder localStringBuilder2 = new java/lang/StringBuilder;
            localStringBuilder2.<init>("AudioThread IllegalStateException:");
            DiracFileLog.addTrace(localIllegalStateException2.getMessage());
            localIllegalStateException2.printStackTrace();
            this.mMusicTrack.release();
            DiracFileLog.addTrace("AudioThread mMusicTrack.release()");
          }
        }
        finally
        {
          this.mMusicTrack.release();
          DiracFileLog.addTrace("AudioThread mMusicTrack.release()");
        }
        try
        {
          Thread.sleep(100L);
        }
        catch (Exception localException1)
        {
          localException1.printStackTrace();
        }
      }
      catch (IllegalStateException localIllegalStateException1)
      {
        for (;;)
        {
          DiracFileLog.addTrace("AudioThread mMusicTrack WORKAROUND 2511 mStopAudioThreads:" + this.mStopAudioThreads + " mMusicTrack:" + this.mMusicTrack);
          this.mStopAudioThreads = true;
          this.mMusicTrack = new AudioTrack(3, this.mSampleRate, 12, 2, j, 1);
          DiracFileLog.addTrace("AudioThread mMusicTrack WORKAROUND 2511 mStopAudioThreads:" + this.mStopAudioThreads + " mMusicTrack:" + this.mMusicTrack);
          continue;
          try
          {
            localAudioPacket = (AudioPacket)this.itemsToPlay.take();
            this.mMusicTrack.write(localAudioPacket.data, 0, (int)localAudioPacket.samples);
            i = 0;
          }
          catch (Exception localException2)
          {
            localException2.printStackTrace();
            i++;
            DiracFileLog.addTrace("AudioThread while (!mStopAudioThreads) Exception:" + localException2.getMessage());
            if (i > 5)
            {
              this.mStopAudioThreads = true;
            }
            else
            {
              this.mMusicTrack = new AudioTrack(3, this.mSampleRate, 12, 2, j, 1);
              try
              {
                Thread.sleep(200L);
              }
              catch (Exception localException3)
              {
                localException3.printStackTrace();
              }
            }
          }
        }
      }
    }
  }
  
  public void stopPlayer()
  {
    this.mStopAudioThreads = true;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\diracndk\AudioThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
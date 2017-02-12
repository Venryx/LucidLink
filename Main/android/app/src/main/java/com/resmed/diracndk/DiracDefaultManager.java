package com.resmed.diracndk;

import android.media.AudioTrack;
import android.util.Log;
import java.io.FileDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DiracDefaultManager
{
  public static final boolean LOGGING = false;
  public static final String TAG = "Java_DiracDefaultManager";
  private static final Set<String> s4models = new HashSet(Arrays.asList(new String[] { "SCH-i545", "SGH-M919", "SPH-L720T", "SPH-L720", "SGH-i537", "SGH-i337", "SM-C105A", "GT-i9500", "GT-i9505G", "SM-C101" }));
  private static final Set<String> s5models = new HashSet(Arrays.asList(new String[] { "SM-G900V", "SM-G900T", "SM-G900P", "SM-G900A", "SM-G900F" }));
  private DiracJNI diracJNI = new DiracJNI();
  public AudioThread mAudioPlayer = new AudioThread(44100);
  private boolean mStopAudioThreads;
  Runnable mStream = new Runnable()
  {
    public void run()
    {
      int i = AudioTrack.getMinBufferSize(44100, 12, 2) * 2;
      for (;;)
      {
        if (DiracDefaultManager.this.mStopAudioThreads)
        {
          DiracFileLog.addTrace("DiracDefaultManager Runnable mStream mStopAudioThreads=" + DiracDefaultManager.this.mStopAudioThreads);
          return;
        }
        AudioPacket localAudioPacket = new AudioPacket(i + 2048);
        localAudioPacket.samples = ((int)DiracDefaultManager.this.processAudioThread(localAudioPacket.data, i));
        if (localAudioPacket.samples == 0L)
        {
          DiracDefaultManager.this.mAudioPlayer.stopPlayer();
          DiracDefaultManager.this.mStopAudioThreads = true;
        }
        else
        {
          DiracDefaultManager.this.mAudioPlayer.play(localAudioPacket);
        }
      }
    }
  };
  private Thread mStreamThread;
  
  public void changeDuration(float paramFloat)
  {
    DiracFileLog.addTrace("DiracDefaultManager changeDuration(" + paramFloat + ")");
    this.diracJNI.changeDuration(paramFloat);
  }
  
  public void changePitch(float paramFloat)
  {
    DiracFileLog.addTrace("DiracDefaultManager changePitch(" + paramFloat + ")");
    this.diracJNI.changePitch(paramFloat);
  }
  
  public int getBestProcessingType()
  {
    return 1;
  }
  
  public int getProcessingType()
  {
    return this.diracJNI.getProcessingType();
  }
  
  public void loopBack()
  {
    DiracFileLog.addTrace("DiracDefaultManager loopBack");
    this.diracJNI.loopBack();
  }
  
  public long processAudioThread(short[] paramArrayOfShort, int paramInt)
  {
    DiracFileLog.addTrace("DiracDefaultManager processAudioThread");
    return this.diracJNI.processAudioThread(paramArrayOfShort, paramInt);
  }
  
  void runAudioThreads()
  {
    this.mStopAudioThreads = false;
    this.mStreamThread = new Thread(this.mStream);
    this.mStreamThread.start();
  }
  
  public void setProcessingType(int paramInt)
  {
    this.diracJNI.setProcessingType(paramInt);
  }
  
  public void setupDirac(FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2)
  {
    DiracFileLog.addTrace("DiracDefaultManager setupDirac(" + paramFileDescriptor.valid() + "," + paramInt1 + "," + paramInt2 + ")");
    this.diracJNI.setupDirac(paramFileDescriptor, paramInt1, paramInt2);
    this.mAudioPlayer = new AudioThread(this.diracJNI.getSampleRate());
    runAudioThreads();
  }
  
  public void startPlaying(String paramString)
  {
    Log.i("J_DiracDefaultManager", "startPlayling");
    DiracFileLog.addTrace("DiracDefaultManager startPlaying fileName:" + paramString);
  }

	void stopAudioThreads() {
		DiracFileLog.addTrace("DiracDefaultManager stopAudioThreads");
		this.mStopAudioThreads = true;
		try {
			if (this.mAudioPlayer.mMusicTrack != null && this.mAudioPlayer.mMusicTrack.getState() == 0) {
				int n = 2;
				while (this.mAudioPlayer.mMusicTrack.getState() != 1 && n > 0) {
					try {
						Thread.sleep(100L);
						--n;
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			if (this.mAudioPlayer.mMusicTrack != null) {
				DiracFileLog.addTrace("DiracDefaultManager stopAudioThreads state:" + this.mAudioPlayer.mMusicTrack.getState());
			}
			if (this.mAudioPlayer.itemsToPlay != null) {
				this.mAudioPlayer.itemsToPlay.clear();
			}
			if (this.mAudioPlayer.mMusicTrack != null && this.mAudioPlayer.mMusicTrack.getState() == 1) {
				this.mAudioPlayer.mMusicTrack.pause();
				this.mAudioPlayer.mMusicTrack.flush();
				this.mAudioPlayer.mMusicTrack.stop();
				this.mAudioPlayer.mMusicTrack.release();
				DiracFileLog.addTrace("DiracDefaultManager stopAudioThreads mAudioPlayer.mMusicTrack != null && STATE_INITIALIZED");
			}
			if (this.mStreamThread != null) {
				this.mStreamThread.join();
			}
		}
		catch (Exception ex2) {
			ex2.printStackTrace();
		}
	}
  
  public void stopPlaying()
  {
    if (!this.mStopAudioThreads) {
      stopAudioThreads();
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\diracndk\DiracDefaultManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
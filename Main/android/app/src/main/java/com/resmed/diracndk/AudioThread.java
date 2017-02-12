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
    catch (InterruptedException ex)
    {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Unexpected interruption");
    }
  }

	public void run() {
		int var1 = 2 * AudioTrack.getMinBufferSize(this.mSampleRate, 12, 2);
		Log.i("J_DiracDefaultManager", "bufferSize = " + var1);
		DiracFileLog.addTrace("AudioThread run bufferSize:" + var1);
		int var3 = 0;
		this.mMusicTrack = new AudioTrack(3, this.mSampleRate, 12, 2, var1, 1);
		DiracFileLog.addTrace("AudioThread mMusicTrack created");

		while(this.mMusicTrack.getState() != 1) {
			try {
				Thread.sleep(100L);
			} catch (Exception var20) {
				var20.printStackTrace();
			}
		}

		DiracFileLog.addTrace("AudioThread mMusicTrack state:" + this.mMusicTrack.getState());

		try {
			this.mMusicTrack.play();
		} catch (IllegalStateException var19) {
			DiracFileLog.addTrace("AudioThread mMusicTrack WORKAROUND 2511 mStopAudioThreads:" + this.mStopAudioThreads + " mMusicTrack:" + this.mMusicTrack);
			this.mStopAudioThreads = true;
			this.mMusicTrack = new AudioTrack(3, this.mSampleRate, 12, 2, var1, 1);
			DiracFileLog.addTrace("AudioThread mMusicTrack WORKAROUND 2511 mStopAudioThreads:" + this.mStopAudioThreads + " mMusicTrack:" + this.mMusicTrack);
			var3 = 0;
		}

		while(!this.mStopAudioThreads) {
			try {
				AudioPacket var8 = (AudioPacket)this.itemsToPlay.take();
				this.mMusicTrack.write(var8.data, 0, (int)var8.samples);
			} catch (Exception var23) {
				var23.printStackTrace();
				++var3;
				DiracFileLog.addTrace("AudioThread while (!mStopAudioThreads) Exception:" + var23.getMessage());
				if(var3 > 5) {
					this.mStopAudioThreads = true;
					continue;
				}

				this.mMusicTrack = new AudioTrack(3, this.mSampleRate, 12, 2, var1, 1);

				try {
					Thread.sleep(200L);
				} catch (Exception var18) {
					var18.printStackTrace();
				}
				continue;
			}

			var3 = 0;
		}

		DiracFileLog.addTrace("AudioThread mStopAudioThreads:" + this.mStopAudioThreads + " mMusicTrack:" + this.mMusicTrack);
		if(this.mMusicTrack != null) {
			try {
				DiracFileLog.addTrace("AudioThread mMusicTrack.getState:" + this.mMusicTrack.getState());
				if(this.mMusicTrack.getState() == 1) {
					this.mMusicTrack.flush();
					this.mMusicTrack.stop();
				}

				return;
			} catch (IllegalStateException var21) {
				DiracFileLog.addTrace("AudioThread IllegalStateException:" + var21.getMessage());
				var21.printStackTrace();
			} finally {
				this.mMusicTrack.release();
				DiracFileLog.addTrace("AudioThread mMusicTrack.release()");
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
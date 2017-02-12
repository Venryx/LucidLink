package com.resmed.refresh.utils.audio;

import android.media.MediaRecorder;
import com.resmed.refresh.utils.Log;
import java.io.File;
import java.io.IOException;

public class AudioDefaultRecorder
  implements AudioRecorder
{
  private Thread amplitudeHandlerThread;
  private boolean isRecording;
  private String mFileName;
  private MediaRecorder mRecorder;
  private AudioSampleReceiver sampleReceiver;
  private int timeIntervalSamples;
  
  public AudioDefaultRecorder(File paramFile, String paramString, int paramInt, AudioSampleReceiver paramAudioSampleReceiver)
  {
    this.mFileName = (paramFile.getAbsolutePath() + "/" + paramString + ".3gp");
    this.timeIntervalSamples = paramInt;
    this.isRecording = false;
    this.sampleReceiver = paramAudioSampleReceiver;
  }
  
  private boolean prepare()
  {
    boolean bool = true;
    this.mRecorder = new MediaRecorder();
    this.mRecorder.setAudioSource(1);
    this.mRecorder.setOutputFormat(1);
    this.mRecorder.setOutputFile(this.mFileName);
    this.mRecorder.setAudioEncoder(1);
    try
    {
      this.mRecorder.prepare();
      return bool;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        Log.w("com.resmed.refresh", "prepare() failed");
        bool = false;
      }
    }
  }
  
  public String getFilePath()
  {
    return this.mFileName;
  }
  
  public boolean isRecording()
  {
    return this.isRecording;
  }
  
  public void startRecording()
  {
    Log.d("com.resmed.refresh.sound", " AudioDefaultRecorder::startRecording()");
    try
    {
      if (prepare())
      {
        this.mRecorder.start();
        this.isRecording = true;
        Thread localThread = new java.lang.Thread;
        Runnable local1 = new com/resmed/refresh/utils/audio/AudioDefaultRecorder$1;
        local1.<init>(this);
        localThread.<init>(local1);
        this.amplitudeHandlerThread = localThread;
        this.amplitudeHandlerThread.start();
      }
      return;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      for (;;)
      {
        localIllegalStateException.printStackTrace();
      }
    }
  }
  
  /* Error */
  public void stop(boolean paramBoolean)
  {
    // Byte code:
    //   0: ldc 113
    //   2: ldc -111
    //   4: invokestatic 118	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   7: pop
    //   8: aload_0
    //   9: iconst_0
    //   10: putfield 62	com/resmed/refresh/utils/audio/AudioDefaultRecorder:isRecording	Z
    //   13: aload_0
    //   14: getfield 73	com/resmed/refresh/utils/audio/AudioDefaultRecorder:mRecorder	Landroid/media/MediaRecorder;
    //   17: astore_2
    //   18: aload_2
    //   19: ifnull +22 -> 41
    //   22: aload_0
    //   23: getfield 73	com/resmed/refresh/utils/audio/AudioDefaultRecorder:mRecorder	Landroid/media/MediaRecorder;
    //   26: invokevirtual 147	android/media/MediaRecorder:stop	()V
    //   29: aload_0
    //   30: getfield 73	com/resmed/refresh/utils/audio/AudioDefaultRecorder:mRecorder	Landroid/media/MediaRecorder;
    //   33: invokevirtual 150	android/media/MediaRecorder:release	()V
    //   36: aload_0
    //   37: aconst_null
    //   38: putfield 73	com/resmed/refresh/utils/audio/AudioDefaultRecorder:mRecorder	Landroid/media/MediaRecorder;
    //   41: aload_0
    //   42: getfield 133	com/resmed/refresh/utils/audio/AudioDefaultRecorder:amplitudeHandlerThread	Ljava/lang/Thread;
    //   45: ifnull +15 -> 60
    //   48: aload_0
    //   49: getfield 133	com/resmed/refresh/utils/audio/AudioDefaultRecorder:amplitudeHandlerThread	Ljava/lang/Thread;
    //   52: invokevirtual 153	java/lang/Thread:interrupt	()V
    //   55: aload_0
    //   56: aconst_null
    //   57: putfield 133	com/resmed/refresh/utils/audio/AudioDefaultRecorder:amplitudeHandlerThread	Ljava/lang/Thread;
    //   60: iload_1
    //   61: ifne +18 -> 79
    //   64: new 32	java/io/File
    //   67: dup
    //   68: aload_0
    //   69: getfield 58	com/resmed/refresh/utils/audio/AudioDefaultRecorder:mFileName	Ljava/lang/String;
    //   72: invokespecial 154	java/io/File:<init>	(Ljava/lang/String;)V
    //   75: invokevirtual 157	java/io/File:delete	()Z
    //   78: pop
    //   79: return
    //   80: astore_2
    //   81: aload_2
    //   82: invokevirtual 158	java/lang/RuntimeException:printStackTrace	()V
    //   85: aload_0
    //   86: getfield 73	com/resmed/refresh/utils/audio/AudioDefaultRecorder:mRecorder	Landroid/media/MediaRecorder;
    //   89: invokevirtual 150	android/media/MediaRecorder:release	()V
    //   92: aload_0
    //   93: aconst_null
    //   94: putfield 73	com/resmed/refresh/utils/audio/AudioDefaultRecorder:mRecorder	Landroid/media/MediaRecorder;
    //   97: goto -56 -> 41
    //   100: astore_2
    //   101: aload_2
    //   102: invokevirtual 159	java/lang/Exception:printStackTrace	()V
    //   105: goto -64 -> 41
    //   108: astore_2
    //   109: aload_0
    //   110: getfield 73	com/resmed/refresh/utils/audio/AudioDefaultRecorder:mRecorder	Landroid/media/MediaRecorder;
    //   113: invokevirtual 150	android/media/MediaRecorder:release	()V
    //   116: aload_0
    //   117: aconst_null
    //   118: putfield 73	com/resmed/refresh/utils/audio/AudioDefaultRecorder:mRecorder	Landroid/media/MediaRecorder;
    //   121: aload_2
    //   122: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	123	0	this	AudioDefaultRecorder
    //   0	123	1	paramBoolean	boolean
    //   17	2	2	localMediaRecorder	MediaRecorder
    //   80	2	2	localRuntimeException	RuntimeException
    //   100	2	2	localException	Exception
    //   108	14	2	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   22	29	80	java/lang/RuntimeException
    //   8	18	100	java/lang/Exception
    //   29	41	100	java/lang/Exception
    //   85	97	100	java/lang/Exception
    //   109	123	100	java/lang/Exception
    //   22	29	108	finally
    //   81	85	108	finally
  }
  
  public static abstract interface AudioSampleReceiver
  {
    public abstract void handleAudioAmplitude(int paramInt);
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
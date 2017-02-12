package com.resmed.refresh.utils.audio;

import android.media.AudioRecord;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PCMDefaultRecorder
  implements AudioRecorder
{
  private static final int RECORDER_AUDIO_ENCODING = 2;
  private static final int RECORDER_CHANNELS = 16;
  private static final int RECORDER_SAMPLERATE = 44100;
  private static PCMDefaultRecorder instance;
  private int BufferElements2Rec = 2048;
  private int BytesPerElement = 2;
  private Thread amplitudeHandlerThread = null;
  private Integer byteWrote;
  private Integer duration;
  private String fileName;
  private boolean isConverted;
  private boolean isPause;
  private boolean isRecording;
  private Double maxSoundLevel;
  private FileOutputStream os;
  private AudioRecord recorder = null;
  private Thread recordingThread = null;
  private AudioSampleReceiver sampleReceiver;
  private String tempFileName;
  private int timeIntervalSamples;
  private File wavTarget;
  
  public static PCMDefaultRecorder getInstance()
  {
    if (instance == null) {
      instance = new PCMDefaultRecorder();
    }
    return instance;
  }
  
  private byte[] short2byte(short[] paramArrayOfShort)
  {
    int j = paramArrayOfShort.length;
    byte[] arrayOfByte = new byte[j * 2];
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return arrayOfByte;
      }
      arrayOfByte[(i * 2)] = ((byte)(paramArrayOfShort[i] & 0xFF));
      arrayOfByte[(i * 2 + 1)] = ((byte)(paramArrayOfShort[i] >> 8));
      paramArrayOfShort[i] = 0;
    }
  }
  
  /* Error */
  private void writeAudioDataToFile()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 58	com/resmed/refresh/utils/audio/PCMDefaultRecorder:BufferElements2Rec	I
    //   6: newarray <illegal type>
    //   8: astore 6
    //   10: aload_0
    //   11: getfield 78	com/resmed/refresh/utils/audio/PCMDefaultRecorder:isRecording	Z
    //   14: ifne +60 -> 74
    //   17: aload_0
    //   18: getfield 56	com/resmed/refresh/utils/audio/PCMDefaultRecorder:recordingThread	Ljava/lang/Thread;
    //   21: ifnull +36 -> 57
    //   24: new 114	java/lang/StringBuilder
    //   27: astore 6
    //   29: aload 6
    //   31: ldc 116
    //   33: invokespecial 119	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   36: ldc 121
    //   38: aload 6
    //   40: aload_0
    //   41: getfield 56	com/resmed/refresh/utils/audio/PCMDefaultRecorder:recordingThread	Ljava/lang/Thread;
    //   44: invokevirtual 127	java/lang/Thread:getName	()Ljava/lang/String;
    //   47: invokevirtual 131	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   50: invokevirtual 134	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   53: invokestatic 140	com/resmed/refresh/utils/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   56: pop
    //   57: aload_0
    //   58: getfield 142	com/resmed/refresh/utils/audio/PCMDefaultRecorder:os	Ljava/io/FileOutputStream;
    //   61: ifnull +10 -> 71
    //   64: aload_0
    //   65: getfield 142	com/resmed/refresh/utils/audio/PCMDefaultRecorder:os	Ljava/io/FileOutputStream;
    //   68: invokevirtual 147	java/io/FileOutputStream:close	()V
    //   71: aload_0
    //   72: monitorexit
    //   73: return
    //   74: new 114	java/lang/StringBuilder
    //   77: astore 7
    //   79: aload 7
    //   81: ldc -107
    //   83: invokespecial 119	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   86: ldc -105
    //   88: aload 7
    //   90: aload_0
    //   91: getfield 56	com/resmed/refresh/utils/audio/PCMDefaultRecorder:recordingThread	Ljava/lang/Thread;
    //   94: invokevirtual 127	java/lang/Thread:getName	()Ljava/lang/String;
    //   97: invokevirtual 131	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: invokevirtual 134	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   103: invokestatic 154	com/resmed/refresh/utils/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   106: pop
    //   107: aload_0
    //   108: getfield 87	com/resmed/refresh/utils/audio/PCMDefaultRecorder:isPause	Z
    //   111: ifne +192 -> 303
    //   114: aload_0
    //   115: getfield 54	com/resmed/refresh/utils/audio/PCMDefaultRecorder:recorder	Landroid/media/AudioRecord;
    //   118: aload 6
    //   120: iconst_0
    //   121: aload_0
    //   122: getfield 58	com/resmed/refresh/utils/audio/PCMDefaultRecorder:BufferElements2Rec	I
    //   125: invokevirtual 158	android/media/AudioRecord:read	([SII)I
    //   128: istore 4
    //   130: dconst_0
    //   131: dstore_1
    //   132: iconst_0
    //   133: istore_3
    //   134: iload_3
    //   135: iload 4
    //   137: if_icmplt +148 -> 285
    //   140: dload_1
    //   141: iload 4
    //   143: i2d
    //   144: ddiv
    //   145: invokestatic 164	java/lang/Math:abs	(D)D
    //   148: invokestatic 170	java/lang/Double:valueOf	(D)Ljava/lang/Double;
    //   151: astore 7
    //   153: aload 7
    //   155: invokevirtual 174	java/lang/Double:doubleValue	()D
    //   158: aload_0
    //   159: getfield 97	com/resmed/refresh/utils/audio/PCMDefaultRecorder:maxSoundLevel	Ljava/lang/Double;
    //   162: invokevirtual 174	java/lang/Double:doubleValue	()D
    //   165: dcmpl
    //   166: ifle +9 -> 175
    //   169: aload_0
    //   170: aload 7
    //   172: putfield 97	com/resmed/refresh/utils/audio/PCMDefaultRecorder:maxSoundLevel	Ljava/lang/Double;
    //   175: getstatic 180	java/lang/System:out	Ljava/io/PrintStream;
    //   178: astore 7
    //   180: new 114	java/lang/StringBuilder
    //   183: astore 8
    //   185: aload 8
    //   187: ldc -74
    //   189: invokespecial 119	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   192: aload 7
    //   194: aload 8
    //   196: aload 6
    //   198: invokevirtual 183	java/lang/Object:toString	()Ljava/lang/String;
    //   201: invokevirtual 131	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   204: invokevirtual 134	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   207: invokevirtual 188	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   210: aload_0
    //   211: aload 6
    //   213: invokespecial 190	com/resmed/refresh/utils/audio/PCMDefaultRecorder:short2byte	([S)[B
    //   216: astore 7
    //   218: aload_0
    //   219: aload_0
    //   220: getfield 192	com/resmed/refresh/utils/audio/PCMDefaultRecorder:byteWrote	Ljava/lang/Integer;
    //   223: invokevirtual 198	java/lang/Integer:intValue	()I
    //   226: aload 7
    //   228: arraylength
    //   229: iadd
    //   230: invokestatic 201	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   233: putfield 192	com/resmed/refresh/utils/audio/PCMDefaultRecorder:byteWrote	Ljava/lang/Integer;
    //   236: aload_0
    //   237: getfield 142	com/resmed/refresh/utils/audio/PCMDefaultRecorder:os	Ljava/io/FileOutputStream;
    //   240: aload 7
    //   242: iconst_0
    //   243: aload_0
    //   244: getfield 58	com/resmed/refresh/utils/audio/PCMDefaultRecorder:BufferElements2Rec	I
    //   247: aload_0
    //   248: getfield 60	com/resmed/refresh/utils/audio/PCMDefaultRecorder:BytesPerElement	I
    //   251: imul
    //   252: invokevirtual 205	java/io/FileOutputStream:write	([BII)V
    //   255: goto -245 -> 10
    //   258: astore 7
    //   260: aload 7
    //   262: invokevirtual 208	java/io/IOException:printStackTrace	()V
    //   265: goto -255 -> 10
    //   268: astore 6
    //   270: aload 6
    //   272: invokevirtual 209	java/lang/Exception:printStackTrace	()V
    //   275: goto -204 -> 71
    //   278: astore 6
    //   280: aload_0
    //   281: monitorexit
    //   282: aload 6
    //   284: athrow
    //   285: aload 6
    //   287: iload_3
    //   288: saload
    //   289: istore 5
    //   291: dload_1
    //   292: iload 5
    //   294: i2d
    //   295: dadd
    //   296: dstore_1
    //   297: iinc 3 1
    //   300: goto -166 -> 134
    //   303: ldc2_w 210
    //   306: invokestatic 215	java/lang/Thread:sleep	(J)V
    //   309: goto -299 -> 10
    //   312: astore 7
    //   314: aload 7
    //   316: invokevirtual 216	java/lang/InterruptedException:printStackTrace	()V
    //   319: goto -309 -> 10
    //   322: astore 6
    //   324: aload 6
    //   326: invokevirtual 208	java/io/IOException:printStackTrace	()V
    //   329: goto -258 -> 71
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	332	0	this	PCMDefaultRecorder
    //   131	166	1	d	double
    //   133	165	3	i	int
    //   128	14	4	j	int
    //   289	4	5	k	int
    //   8	204	6	localObject1	Object
    //   268	3	6	localException	Exception
    //   278	8	6	localObject2	Object
    //   322	3	6	localIOException1	IOException
    //   77	164	7	localObject3	Object
    //   258	3	7	localIOException2	IOException
    //   312	3	7	localInterruptedException	InterruptedException
    //   183	12	8	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   210	255	258	java/io/IOException
    //   2	10	268	java/lang/Exception
    //   10	57	268	java/lang/Exception
    //   57	71	268	java/lang/Exception
    //   74	130	268	java/lang/Exception
    //   140	175	268	java/lang/Exception
    //   175	210	268	java/lang/Exception
    //   210	255	268	java/lang/Exception
    //   260	265	268	java/lang/Exception
    //   303	309	268	java/lang/Exception
    //   314	319	268	java/lang/Exception
    //   324	329	268	java/lang/Exception
    //   2	10	278	finally
    //   10	57	278	finally
    //   57	71	278	finally
    //   74	130	278	finally
    //   140	175	278	finally
    //   175	210	278	finally
    //   210	255	278	finally
    //   260	265	278	finally
    //   270	275	278	finally
    //   303	309	278	finally
    //   314	319	278	finally
    //   324	329	278	finally
    //   303	309	312	java/lang/InterruptedException
    //   57	71	322	java/io/IOException
  }
  
  public Integer getDuration()
  {
    return Integer.valueOf(this.duration.intValue() * 1000);
  }
  
  public String getFilePath()
  {
    Log.e("", "MindClear fileName:" + this.fileName);
    return this.fileName;
  }
  
  public String getTempFilePath()
  {
    return this.tempFileName;
  }
  
  public void pause()
  {
    this.isPause = true;
    Log.i("com.resmed.refresh.recorder", "recorder on pause -> byte wrote: " + this.byteWrote.toString());
  }
  
  public void play()
  {
    this.isPause = false;
  }
  
  public void setupRecorder(String paramString1, String paramString2, int paramInt, AudioSampleReceiver paramAudioSampleReceiver)
  {
    File localFile = RefreshTools.getFilesPath();
    this.tempFileName = (localFile.getAbsolutePath() + "/" + paramString2);
    this.timeIntervalSamples = paramInt;
    this.isRecording = false;
    this.sampleReceiver = paramAudioSampleReceiver;
    this.byteWrote = Integer.valueOf(0);
    this.os = null;
    this.isPause = false;
    this.isConverted = false;
    this.wavTarget = new File(localFile.getAbsolutePath(), paramString1);
    this.fileName = (localFile.getAbsolutePath() + "/" + paramString1);
    this.maxSoundLevel = Double.valueOf(0.0D);
  }
  
  public void startRecording()
  {
    this.recorder = new AudioRecord(1, 44100, 16, 2, this.BufferElements2Rec * this.BytesPerElement);
    try
    {
      this.recorder.startRecording();
      play();
      this.isRecording = true;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        try
        {
          localObject = new java/io/File;
          ((File)localObject).<init>(this.tempFileName);
          bool = ((File)localObject).exists();
          if (bool) {}
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          Object localObject;
          boolean bool;
          localFileNotFoundException.printStackTrace();
          continue;
        }
        try
        {
          bool = ((File)localObject).createNewFile();
          localObject = new java.lang.StringBuilder;
          ((StringBuilder)localObject).<init>("File created? ");
          Log.i("com.resmed.refresh.recorder", bool);
          localObject = new java/io/FileOutputStream;
          ((FileOutputStream)localObject).<init>(this.tempFileName);
          this.os = ((FileOutputStream)localObject);
          this.recordingThread = new Thread(new Runnable()
          {
            public void run()
            {
              PCMDefaultRecorder.this.writeAudioDataToFile();
            }
          });
          this.recordingThread.start();
          this.amplitudeHandlerThread = new Thread(new Runnable()
          {
            public void run()
            {
              for (;;)
              {
                if (!PCMDefaultRecorder.this.isRecording) {
                  return;
                }
                Log.i("com.resmed.refresh.player", "amplitude thread -> current thread in execution: " + PCMDefaultRecorder.this.amplitudeHandlerThread.getName());
                try
                {
                  Thread.sleep(PCMDefaultRecorder.this.timeIntervalSamples);
                  if ((PCMDefaultRecorder.this.isPause) || (PCMDefaultRecorder.this.recorder == null) || (PCMDefaultRecorder.this.sampleReceiver == null)) {
                    continue;
                  }
                  PCMDefaultRecorder.this.sampleReceiver.handleAudioAmplitude(PCMDefaultRecorder.this.maxSoundLevel);
                  PCMDefaultRecorder.this.maxSoundLevel = Double.valueOf(0.0D);
                }
                catch (InterruptedException localInterruptedException)
                {
                  for (;;)
                  {
                    localInterruptedException.printStackTrace();
                  }
                }
              }
            }
          });
          this.amplitudeHandlerThread.start();
          return;
          localException = localException;
          localException.printStackTrace();
        }
        catch (IOException localIOException)
        {
          localIOException.printStackTrace();
        }
      }
    }
  }
  
  public void stop(boolean paramBoolean)
  {
    if (this.recorder != null)
    {
      this.isRecording = false;
      this.recorder.stop();
      this.recorder.release();
      this.recorder = null;
      if (this.recordingThread != null)
      {
        this.recordingThread.interrupt();
        this.recordingThread = null;
        System.gc();
      }
      if (this.amplitudeHandlerThread != null)
      {
        this.amplitudeHandlerThread.interrupt();
        this.amplitudeHandlerThread = null;
        System.gc();
      }
    }
    Log.i("com.resmed.refresh.recorder", "byte wrote: " + this.byteWrote.toString());
    this.duration = Integer.valueOf(this.byteWrote.intValue() / 1 / 2 / 44100);
    Log.i("com.resmed.refresh.recorder", "audio duration: " + this.duration);
    Object localObject;
    File localFile;
    if (!this.isConverted)
    {
      localObject = new WavAudioFormat.Builder();
      ((WavAudioFormat.Builder)localObject).sampleRate(44100);
      ((WavAudioFormat.Builder)localObject).channels(1);
      ((WavAudioFormat.Builder)localObject).sampleSizeInBits(16);
      localObject = ((WavAudioFormat.Builder)localObject).build();
      localFile = new File(this.tempFileName);
    }
    try
    {
      PcmAudioHelper.convertRawToWav((WavAudioFormat)localObject, localFile, this.wavTarget);
      if (localFile.exists()) {
        localFile.delete();
      }
      this.isConverted = true;
      return;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        localIOException.printStackTrace();
      }
    }
  }
  
  public static abstract interface AudioSampleReceiver
  {
    public abstract void handleAudioAmplitude(Double paramDouble);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\audio\PCMDefaultRecorder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
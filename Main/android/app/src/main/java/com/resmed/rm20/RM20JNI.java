package com.resmed.rm20;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RM20JNI
{
  public static final String rm20JniVer = "1.0.2";
  private RM20Callbacks callbacks;
  private File filesFolder;
  
  public RM20JNI(File paramFile, RM20Callbacks paramRM20Callbacks, Context paramContext)
  {
    this.filesFolder = paramFile;
    this.callbacks = paramRM20Callbacks;
  }
  
  public static void loadLibrary(Context paramContext)
  {
    localObject3 = Boolean.valueOf(false);
    Object localObject2 = null;
    AssetManager localAssetManager = paramContext.getResources().getAssets();
    try
    {
      localObject1 = paramContext.openFileInput("libVerRm20.txt");
      BufferedReader localBufferedReader = new java/io/BufferedReader;
      InputStreamReader localInputStreamReader = new java/io/InputStreamReader;
      localInputStreamReader.<init>((InputStream)localObject1);
      localBufferedReader.<init>(localInputStreamReader);
      localObject1 = localBufferedReader.readLine();
      localObject2 = localObject1;
    }
    catch (IOException localIOException1)
    {
      for (;;)
      {
        Object localObject1;
        localIOException1.printStackTrace();
        continue;
        ((FileOutputStream)localObject3).write(localIOException1, 0, i);
      }
    }
    localObject1 = localObject3;
    if (localObject2 != null)
    {
      localObject1 = localObject3;
      if (((String)localObject2).trim().compareTo("1.0.2") == 0) {
        localObject1 = Boolean.valueOf(true);
      }
    }
    if (!((Boolean)localObject1).booleanValue()) {
      localObject1 = new byte['Ѐ'];
    }
    for (;;)
    {
      try
      {
        localObject2 = new java.lang.StringBuilder;
        ((StringBuilder)localObject2).<init>(String.valueOf(Build.CPU_ABI));
        localObject2 = localAssetManager.open("/librm20-jni.so", 3);
        localObject3 = paramContext.openFileOutput("librm20-jni.so", 0);
        i = ((InputStream)localObject2).read((byte[])localObject1);
        if (i <= 0)
        {
          ((InputStream)localObject2).close();
          ((FileOutputStream)localObject3).close();
        }
      }
      catch (IOException localIOException3)
      {
        int i;
        localIOException3.printStackTrace();
        continue;
        localIOException3.write(localIOException1, 0, i);
        continue;
      }
      try
      {
        localObject3 = localAssetManager.open("libVerRm20.txt", 3);
        localObject2 = paramContext.openFileOutput("libVerRm20.txt", 0);
        i = ((InputStream)localObject3).read((byte[])localObject1);
        if (i > 0) {
          continue;
        }
        ((InputStream)localObject3).close();
        ((FileOutputStream)localObject2).close();
      }
      catch (IOException localIOException2)
      {
        localIOException2.printStackTrace();
      }
    }
    System.load(paramContext.getFilesDir() + "/librm20-jni.so");
  }
  
  public native int disableSmartAlarm();
  
  public native int getEpochCount();
  
  public native String getLibVersion();
  
  public native int getRealTimeSleepState();
  
  public native SmartAlarmInfo getSmartAlarm();
  
  public native UserInfo getUserInfo();
  
  public void onLibraryStarted(int paramInt) {}
  
  public void onRealTimeSleepState(int paramInt1, int paramInt2)
  {
    if (this.callbacks != null) {
      this.callbacks.onRm20RealTimeSleepState(paramInt1, paramInt2);
    }
  }
  
  public void onRm20Alarm(int paramInt)
  {
    if (this.callbacks != null) {
      this.callbacks.onRm20Alarm(paramInt);
    }
  }
  
  public void onValidBreathingRate(float paramFloat, int paramInt)
  {
    if (this.callbacks != null) {
      this.callbacks.onRm20ValidBreathingRate(paramFloat, paramInt);
    }
  }
  
  public void onWroteSample(int paramInt) {}
  
  public native SleepParams resultsForSession();
  
  public native int setRespRateCallbacks(boolean paramBoolean);
  
  public native int setSmartAlarm(int paramInt1, int paramInt2, boolean paramBoolean);
  
  public native int startupLibrary(int paramInt1, int paramInt2);
  
  public native int stopAndCalculate();
  
  public native int writeSampleData(int paramInt1, int paramInt2);
  
  public int writeSamples(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int i = -1;
    for (int j = 0;; j++)
    {
      int k;
      if (j >= paramArrayOfInt2.length) {
        k = i;
      }
      do
      {
        return k;
        i = writeSampleData(paramArrayOfInt1[j], paramArrayOfInt2[j]);
        k = i;
      } while (i != 0);
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\rm20\RM20JNI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
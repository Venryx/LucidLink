package com.resmed.edflib;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class EdfLibJNI
{
  public static final String rm60JniVer = "1.0.0";
  private EdfLibCallbackHandler callbacks;
  private File filesFolder;
  
  public EdfLibJNI(File paramFile, EdfLibCallbackHandler paramEdfLibCallbackHandler, Context paramContext)
  {
    this.filesFolder = paramFile;
    this.callbacks = paramEdfLibCallbackHandler;
  }
  
  private void listFiles()
  {
    Object localObject1 = new File(this.filesFolder.getAbsolutePath());
    int j;
    if (((File)localObject1).exists())
    {
      localObject1 = ((File)localObject1).listFiles();
      j = localObject1.length;
    }
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return;
      }
      Object localObject2 = localObject1[i];
      System.out.println("external file : " + ((File)localObject2).getAbsolutePath());
    }
  }
  
  public static void loadLibrary(Context paramContext)
  {
    localObject3 = Boolean.valueOf(false);
    localObject2 = null;
    AssetManager localAssetManager = paramContext.getResources().getAssets();
    try
    {
      FileInputStream localFileInputStream = paramContext.openFileInput("libVerRm60.txt");
      localObject1 = new java/io/BufferedReader;
      InputStreamReader localInputStreamReader = new java/io/InputStreamReader;
      localInputStreamReader.<init>(localFileInputStream);
      ((BufferedReader)localObject1).<init>(localInputStreamReader);
      localObject1 = ((BufferedReader)localObject1).readLine();
      localObject2 = localObject1;
    }
    catch (IOException localIOException1)
    {
      for (;;)
      {
        Object localObject1;
        localIOException1.printStackTrace();
        continue;
        ((FileOutputStream)localObject2).write(localIOException1, 0, i);
      }
    }
    localObject1 = localObject3;
    if (localObject2 != null)
    {
      localObject1 = localObject3;
      if (((String)localObject2).trim().compareTo("1.0.0") == 0) {
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
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>(String.valueOf(Build.CPU_ABI));
        localObject3 = localAssetManager.open("/librstedf.so", 3);
        localObject2 = paramContext.openFileOutput("librstedf.so", 0);
        i = ((InputStream)localObject3).read((byte[])localObject1);
        if (i <= 0)
        {
          ((InputStream)localObject3).close();
          ((FileOutputStream)localObject2).close();
        }
      }
      catch (IOException localIOException3)
      {
        int i;
        localIOException3.printStackTrace();
        continue;
        ((FileOutputStream)localObject3).write(localIOException1, 0, i);
        continue;
      }
      try
      {
        localObject2 = localAssetManager.open("libVerRm60.txt", 3);
        localObject3 = paramContext.openFileOutput("libVerRm60.txt", 0);
        i = ((InputStream)localObject2).read((byte[])localObject1);
        if (i > 0) {
          continue;
        }
        ((InputStream)localObject2).close();
        ((FileOutputStream)localObject3).close();
      }
      catch (IOException localIOException2)
      {
        localIOException2.printStackTrace();
      }
    }
    System.load(paramContext.getFilesDir() + "/librstedf.so");
  }
  
  public native int closeFileEdf(String paramString, String[] paramArrayOfString);
  
  public native int compressFile(String paramString1, String paramString2);
  
  public native int fixEdfFile(String paramString);
  
  public native String getLibVersion();
  
  public void onDigitalSamplesRead(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    if (this.callbacks != null) {
      this.callbacks.onDigitalSamplesRead(paramArrayOfInt1, paramArrayOfInt2);
    }
  }
  
  public void onFileClosed()
  {
    if (this.callbacks != null) {
      this.callbacks.onFileClosed();
    }
  }
  
  public void onFileCompressed(int paramInt)
  {
    if (this.callbacks != null) {
      this.callbacks.onFileCompressed(paramInt);
    }
  }
  
  public void onFileFixed()
  {
    if (this.callbacks != null) {
      this.callbacks.onFileFixed();
    }
  }
  
  public void onFileOpened()
  {
    if (this.callbacks != null) {
      this.callbacks.onFileOpened();
    }
  }
  
  public void onWroteDigitalSamples()
  {
    if (this.callbacks != null) {
      this.callbacks.onWroteDigitalSamples();
    }
  }
  
  public void onWroteMetaData()
  {
    if (this.callbacks != null) {
      this.callbacks.onWroteMetadata();
    }
  }
  
  public native int openFileForMode(String paramString1, String[] paramArrayOfString, String paramString2);
  
  public native int readDigitalSamples(String paramString);
  
  public void setFilesFolder(File paramFile)
  {
    this.filesFolder = paramFile;
  }
  
  public native int writeDigitalSamples(String paramString, int[] paramArrayOfInt1, int[] paramArrayOfInt2, String[] paramArrayOfString);
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\edflib\EdfLibJNI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.edflib;

import android.content.Context;
import java.io.File;

public class EdfFileManager
  implements FileEdfInterface
{
  private EdfLibJNI edfLibJNI;
  private String filePath;
  private String[] pMeta;
  
  public EdfFileManager(File paramFile, String paramString, String[] paramArrayOfString, EdfLibCallbackHandler paramEdfLibCallbackHandler, Context paramContext)
  {
    this.edfLibJNI = new EdfLibJNI(paramFile, paramEdfLibCallbackHandler, paramContext);
    this.filePath = (paramFile.getAbsolutePath() + "/" + paramString);
    this.pMeta = paramArrayOfString;
  }
  
  public EdfFileManager(String paramString, String[] paramArrayOfString, EdfLibCallbackHandler paramEdfLibCallbackHandler, Context paramContext)
  {
    this.edfLibJNI = new EdfLibJNI(null, paramEdfLibCallbackHandler, paramContext);
    this.filePath = paramString;
    this.pMeta = paramArrayOfString;
  }
  
  public int closeFile()
  {
    try
    {
      int i = this.edfLibJNI.closeFileEdf(this.filePath, this.pMeta);
      return i;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public int compressFile(String paramString)
  {
    try
    {
      int i = this.edfLibJNI.compressFile(this.filePath, paramString);
      return i;
    }
    finally
    {
      paramString = finally;
      throw paramString;
    }
  }
  
  public boolean deleteFile()
  {
    try
    {
      File localFile = new java/io/File;
      localFile.<init>(this.filePath);
      boolean bool = localFile.delete();
      return bool;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public int fixEdfFile()
  {
    try
    {
      int i = this.edfLibJNI.fixEdfFile(this.filePath);
      return i;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public File getFile()
  {
    try
    {
      File localFile = new File(this.filePath);
      return localFile;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public String getFilePath()
  {
    return this.filePath;
  }
  
  public String getRM60LibVersion()
  {
    return this.edfLibJNI.getLibVersion();
  }
  
  public int openFileForMode(String paramString)
  {
    try
    {
      int i = this.edfLibJNI.openFileForMode(this.filePath, this.pMeta, paramString);
      return i;
    }
    finally
    {
      paramString = finally;
      throw paramString;
    }
  }
  
  public int readDigitalSamples()
  {
    try
    {
      int i = this.edfLibJNI.readDigitalSamples(this.filePath);
      return i;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public int writeDigitalSamples(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    try
    {
      RstEdfMetaData localRstEdfMetaData = new com/resmed/edflib/RstEdfMetaData;
      localRstEdfMetaData.<init>();
      localRstEdfMetaData.addMetaField(RstEdfMetaData.Enum_EDF_Meta.autoStop, "no");
      int i = this.edfLibJNI.writeDigitalSamples(this.filePath, paramArrayOfInt1, paramArrayOfInt2, localRstEdfMetaData.toArray());
      return i;
    }
    finally
    {
      paramArrayOfInt1 = finally;
      throw paramArrayOfInt1;
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\edflib\EdfFileManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
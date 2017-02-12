package com.resmed.diracndk;

import android.os.Environment;
import java.io.File;
import java.io.FileWriter;

public class DiracFileLog
{
  private static final int MAX_LOG_FILE_SIZE = 5242880;
  private static String filename = "diracLog.txt";
  private static File filesDir;
  private static FileWriter fw;
  private static DiracFileLog instance;
  
  public static void addTrace(String paramString) {}
  
  public static void deleteCurrentFile()
  {
    try
    {
      if (instance == null)
      {
        localObject1 = new com/resmed/diracndk/DiracFileLog;
        ((DiracFileLog)localObject1).<init>();
        instance = (DiracFileLog)localObject1;
      }
      Object localObject1 = new java/io/File;
      ((File)localObject1).<init>(Environment.getExternalStorageDirectory(), filename);
      filesDir = (File)localObject1;
      if ((filesDir.exists()) && (filesDir.length() > 5242880L)) {
        filesDir.delete();
      }
      return;
    }
    finally {}
  }
  
  public static String getAbsolutePath()
  {
    if (filesDir == null) {
      addTrace("");
    }
    return filesDir.getAbsolutePath();
  }
  
  public static String getFilename()
  {
    return filename;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\diracndk\DiracFileLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
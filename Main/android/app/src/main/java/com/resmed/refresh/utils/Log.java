package com.resmed.refresh.utils;

import com.resmed.refresh.sleepsession.RST_SleepSession;
import com.resmed.refresh.ui.utils.Consts;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class Log
{
  private static final long MAX_FILE_SIZE = 4194304L;
  private static String pathToFile;
  
  public static int d(String paramString1, String paramString2)
  {
    if (Consts.SHOW_LOG) {
      android.util.Log.d(paramString1, paramString2);
    }
    if ((Consts.WRITE_LOG) && (pathToFile != null)) {
      write(paramString1, paramString2, null);
    }
    return 0;
  }
  
  public static int d(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (Consts.SHOW_LOG) {
      android.util.Log.d(paramString1, paramString2, paramThrowable);
    }
    if ((Consts.WRITE_LOG) && (pathToFile != null)) {
      write(paramString1, paramString2, paramThrowable);
    }
    return 0;
  }
  
  public static int e(String paramString1, String paramString2)
  {
    if (Consts.SHOW_LOG) {
      android.util.Log.e(paramString1, paramString2);
    }
    if ((Consts.WRITE_LOG) && (pathToFile != null)) {
      write(paramString1, paramString2, null);
    }
    return 0;
  }
  
  public static int e(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (Consts.SHOW_LOG) {
      android.util.Log.e(paramString1, paramString2, paramThrowable);
    }
    if ((Consts.WRITE_LOG) && (pathToFile != null)) {
      write(paramString1, paramString2, paramThrowable);
    }
    return 0;
  }
  
  public static int i(String paramString1, String paramString2)
  {
    if (Consts.SHOW_LOG) {
      android.util.Log.i(paramString1, paramString2);
    }
    if ((Consts.WRITE_LOG) && (pathToFile != null)) {
      write(paramString1, paramString2, null);
    }
    return 0;
  }
  
  public static int i(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (Consts.SHOW_LOG) {
      android.util.Log.i(paramString1, paramString2, paramThrowable);
    }
    if ((Consts.WRITE_LOG) && (pathToFile != null)) {
      write(paramString1, paramString2, paramThrowable);
    }
    return 0;
  }
  
  public static void record()
  {
    Object localObject = Calendar.getInstance();
    localObject = new SimpleDateFormat("yyyyMMdd_HHmm").format(((Calendar)localObject).getTime());
    pathToFile = RefreshTools.getFilesPath() + "/refresh/log/log_" + (String)localObject;
    RefreshTools.createDirectories(new File(pathToFile).getAbsolutePath());
  }
  
  public static void stop()
  {
    pathToFile = null;
  }
  
  public static int v(String paramString1, String paramString2)
  {
    if (Consts.SHOW_LOG) {
      android.util.Log.v(paramString1, paramString2);
    }
    if ((Consts.WRITE_LOG) && (pathToFile != null)) {
      write(paramString1, paramString2, null);
    }
    return 0;
  }
  
  public static int v(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (Consts.SHOW_LOG) {
      android.util.Log.v(paramString1, paramString2, paramThrowable);
    }
    if ((Consts.WRITE_LOG) && (pathToFile != null)) {
      write(paramString1, paramString2, paramThrowable);
    }
    return 0;
  }
  
  public static int w(String paramString1, String paramString2)
  {
    if (Consts.SHOW_LOG) {
      android.util.Log.w(paramString1, paramString2);
    }
    if ((Consts.WRITE_LOG) && (pathToFile != null)) {
      write(paramString1, paramString2, null);
    }
    return 0;
  }
  
  public static int w(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (Consts.SHOW_LOG) {
      android.util.Log.w(paramString1, paramString2, paramThrowable);
    }
    if ((Consts.WRITE_LOG) && (pathToFile != null)) {
      write(paramString1, paramString2, paramThrowable);
    }
    return 0;
  }
  
  private static void write(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (!RST_SleepSession.getInstance().isSessionRunning()) {}
    try
    {
      StringBuffer localStringBuffer = new java.lang.StringBuffer;
      localStringBuffer.<init>();
      Calendar localCalendar = Calendar.getInstance();
      SimpleDateFormat localSimpleDateFormat = new java.text.SimpleDateFormat;
      localSimpleDateFormat.<init>("dd-MM HH:mm:ss.SSS");
      localStringBuffer.append(localSimpleDateFormat.format(localCalendar.getTime()));
      localStringBuffer.append("\t");
      localStringBuffer.append(paramString1);
      localStringBuffer.append("\t");
      localStringBuffer.append(paramString2);
      if (paramThrowable != null)
      {
        localStringBuffer.append("\t");
        localStringBuffer.append(paramThrowable.getMessage());
      }
      localStringBuffer.append("\n");
      paramString2 = new java.io.File;
      paramString2.<init>(pathToFile);
      paramString1 = new java.io.FileWriter;
      paramString1.<init>(paramString2, true);
      paramString1.append(localStringBuffer);
      paramString1.close();
      if (paramString2.length() > 4194304L) {
        record();
      }
      return;
    }
    catch (Exception paramString1)
    {
      for (;;)
      {
        paramString1.printStackTrace();
      }
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
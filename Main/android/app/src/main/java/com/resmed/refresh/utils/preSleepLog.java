package com.resmed.refresh.utils;

import com.resmed.refresh.ui.utils.Consts;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class preSleepLog
{
  private static final int MAX_LOG_FILE_SIZE = 1048576;
  private static String filename = "presleepQuestionAnswers.txt";
  private static File filesDir;
  private static FileWriter fw;
  private static preSleepLog instance;
  
  public static void addTrace(String paramString)
  {
    for (;;)
    {
      Object localObject1;
      Object localObject4;
      StringBuilder localStringBuilder;
      try
      {
        boolean bool = Consts.WRITE_BLUETOOTH_LOG;
        if (!bool) {
          return;
        }
        localObject3 = "";
        localObject1 = localObject3;
        if (instance == null)
        {
          localObject1 = new com/resmed/refresh/utils/preSleepLog;
          ((preSleepLog)localObject1).<init>();
          instance = (preSleepLog)localObject1;
        }
      }
      finally {}
      try
      {
        localObject1 = new java/io/File;
        ((File)localObject1).<init>(RefreshTools.getFilesPath(), filename);
        filesDir = (File)localObject1;
        if (!filesDir.exists()) {
          filesDir.createNewFile();
        }
        localObject1 = "\n\n";
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
        Object localObject2 = localObject3;
        continue;
      }
      try
      {
        localObject4 = Calendar.getInstance();
        localObject3 = new java/text/SimpleDateFormat;
        ((SimpleDateFormat)localObject3).<init>("yyyyMMdd_HH:mm:ss");
        localObject3 = ((SimpleDateFormat)localObject3).format(((Calendar)localObject4).getTime());
        localObject4 = new java/io/FileWriter;
        ((FileWriter)localObject4).<init>(filesDir, true);
        fw = (FileWriter)localObject4;
        localObject4 = fw;
        localStringBuilder = new java.lang.StringBuilder;
        localStringBuilder.<init>(String.valueOf(localObject1));
        ((FileWriter)localObject4).append((String)localObject3 + "\t" + paramString + " \n");
        fw.flush();
        localObject1 = new java.lang.StringBuilder;
        ((StringBuilder)localObject1).<init>(String.valueOf(localObject3));
        Log.d("com.resmed.refresh.filelog", "\t" + paramString);
      }
      catch (IOException paramString)
      {
        paramString.printStackTrace();
      }
    }
  }
  
  /* Error */
  public static void deleteCurrentFile()
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: getstatic 131	com/resmed/refresh/ui/utils/Consts:WRITE_PRESLEEP_LOG	Z
    //   6: istore_0
    //   7: iload_0
    //   8: ifne +7 -> 15
    //   11: ldc 2
    //   13: monitorexit
    //   14: return
    //   15: getstatic 39	com/resmed/refresh/utils/preSleepLog:instance	Lcom/resmed/refresh/utils/preSleepLog;
    //   18: ifnonnull +15 -> 33
    //   21: new 2	com/resmed/refresh/utils/preSleepLog
    //   24: astore_1
    //   25: aload_1
    //   26: invokespecial 40	com/resmed/refresh/utils/preSleepLog:<init>	()V
    //   29: aload_1
    //   30: putstatic 39	com/resmed/refresh/utils/preSleepLog:instance	Lcom/resmed/refresh/utils/preSleepLog;
    //   33: new 42	java/io/File
    //   36: astore_1
    //   37: aload_1
    //   38: invokestatic 48	com/resmed/refresh/utils/RefreshTools:getFilesPath	()Ljava/io/File;
    //   41: getstatic 21	com/resmed/refresh/utils/preSleepLog:filename	Ljava/lang/String;
    //   44: invokespecial 51	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   47: aload_1
    //   48: putstatic 53	com/resmed/refresh/utils/preSleepLog:filesDir	Ljava/io/File;
    //   51: getstatic 53	com/resmed/refresh/utils/preSleepLog:filesDir	Ljava/io/File;
    //   54: invokevirtual 57	java/io/File:exists	()Z
    //   57: ifeq -46 -> 11
    //   60: getstatic 53	com/resmed/refresh/utils/preSleepLog:filesDir	Ljava/io/File;
    //   63: invokevirtual 135	java/io/File:length	()J
    //   66: ldc2_w 136
    //   69: lcmp
    //   70: ifle -59 -> 11
    //   73: getstatic 53	com/resmed/refresh/utils/preSleepLog:filesDir	Ljava/io/File;
    //   76: invokevirtual 140	java/io/File:delete	()Z
    //   79: pop
    //   80: goto -69 -> 11
    //   83: astore_1
    //   84: ldc 2
    //   86: monitorexit
    //   87: aload_1
    //   88: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   6	2	0	bool	boolean
    //   24	24	1	localObject1	Object
    //   83	5	1	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   3	7	83	finally
    //   15	33	83	finally
    //   33	80	83	finally
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


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\preSleepLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
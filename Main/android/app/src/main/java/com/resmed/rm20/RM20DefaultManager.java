package com.resmed.rm20;

import android.content.Context;
import java.io.File;
import java.util.Date;

public class RM20DefaultManager
  implements RM20Manager
{
  private RM20JNI rm20Lib;
  private Thread writerThread;
  
  public RM20DefaultManager(File paramFile, RM20Callbacks paramRM20Callbacks, Context paramContext)
  {
    this.rm20Lib = new RM20JNI(paramFile, paramRM20Callbacks, paramContext);
  }
  
  public int disableSmartAlarm()
  {
    try
    {
      int i = this.rm20Lib.disableSmartAlarm();
      return i;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public int getEpochCount()
  {
    try
    {
      int i = this.rm20Lib.getEpochCount();
      return i;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public String getLibVersion()
  {
    try
    {
      String str = this.rm20Lib.getLibVersion();
      return str;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public int getRealTimeSleepState()
  {
    try
    {
      int i = this.rm20Lib.getRealTimeSleepState();
      return i;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public SmartAlarmInfo getSmartAlarm()
  {
    try
    {
      SmartAlarmInfo localSmartAlarmInfo = this.rm20Lib.getSmartAlarm();
      return localSmartAlarmInfo;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public UserInfo getUserInfo()
  {
    try
    {
      UserInfo localUserInfo = this.rm20Lib.getUserInfo();
      return localUserInfo;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public SleepParams resultsForSession()
  {
    try
    {
      SleepParams localSleepParams = this.rm20Lib.resultsForSession();
      return localSleepParams;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public int setSmartAlarm(Date paramDate, int paramInt, boolean paramBoolean)
  {
    int j = 65535;
    int i = 65535;
    if (paramInt >= 0) {}
    try
    {
      long l = System.currentTimeMillis();
      i = (int)(paramDate.getTime() - l) / 1000 / 30 + this.rm20Lib.getEpochCount();
      j = i - paramInt / 30;
      paramInt = j;
      if (j < 0) {
        paramInt = 0;
      }
      j = i;
      if (i < 0)
      {
        paramInt = 65535;
        j = 65535;
      }
      i = paramInt;
      if (paramInt > 65535)
      {
        i = 65535;
        j = 65535;
      }
      paramInt = j;
      if (j > 65535) {
        paramInt = 65535;
      }
      paramInt = this.rm20Lib.setSmartAlarm(i, paramInt, paramBoolean);
      return paramInt;
    }
    finally {}
  }
  
  public int startRespRateCallbacks(boolean paramBoolean)
  {
    try
    {
      int i = this.rm20Lib.setRespRateCallbacks(paramBoolean);
      return i;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public int startupLibrary(int paramInt1, int paramInt2)
  {
    try
    {
      paramInt1 = this.rm20Lib.startupLibrary(paramInt1, paramInt2);
      return paramInt1;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public int stopAndCalculate()
  {
    try
    {
      int i = this.rm20Lib.stopAndCalculate();
      return i;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public int writeSampleData(int paramInt1, int paramInt2)
  {
    try
    {
      paramInt1 = this.rm20Lib.writeSampleData(paramInt1, paramInt2);
      return paramInt1;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  /* Error */
  public int writeSamples(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_3
    //   4: aload_2
    //   5: arraylength
    //   6: istore 4
    //   8: iload_3
    //   9: iload 4
    //   11: if_icmplt +7 -> 18
    //   14: aload_0
    //   15: monitorexit
    //   16: iconst_0
    //   17: ireturn
    //   18: aload_0
    //   19: aload_1
    //   20: iload_3
    //   21: iaload
    //   22: aload_2
    //   23: iload_3
    //   24: iaload
    //   25: invokevirtual 83	com/resmed/rm20/RM20DefaultManager:writeSampleData	(II)I
    //   28: pop
    //   29: iinc 3 1
    //   32: goto -28 -> 4
    //   35: astore_1
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_1
    //   39: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	40	0	this	RM20DefaultManager
    //   0	40	1	paramArrayOfInt1	int[]
    //   0	40	2	paramArrayOfInt2	int[]
    //   3	27	3	i	int
    //   6	6	4	j	int
    // Exception table:
    //   from	to	target	type
    //   4	8	35	finally
    //   18	29	35	finally
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\rm20\RM20DefaultManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
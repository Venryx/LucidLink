package com.resmed.rm20;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import java.util.Random;

public class IndexActivity
  extends Activity
  implements RM20Callbacks
{
  private RM20JNI rm20Lib;
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.rm20Lib = new RM20JNI(Environment.getExternalStorageDirectory(), this, getApplicationContext());
    this.rm20Lib.startupLibrary(34, 1);
    this.rm20Lib.setRespRateCallbacks(true);
    new Thread(new Runnable()
    {
      public void run()
      {
        Object localObject = new Random();
        int i = 0;
        for (;;)
        {
          if (i >= 10000)
          {
            IndexActivity.this.rm20Lib.getEpochCount();
            IndexActivity.this.rm20Lib.stopAndCalculate();
            localObject = IndexActivity.this.rm20Lib.resultsForSession();
            Log.d(getClass().getName(), " results : " + localObject);
            return;
          }
          int k = ((Random)localObject).nextInt(1000);
          int j = ((Random)localObject).nextInt(1000);
          IndexActivity.this.rm20Lib.writeSampleData(k, j);
          try
          {
            Thread.sleep(5L);
            i++;
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
    }).start();
  }
  
  public void onRm20Alarm(int paramInt)
  {
    Log.d("RM20NDK", " onRm20Alarm");
  }
  
  public void onRm20RealTimeSleepState(int paramInt1, int paramInt2)
  {
    Log.d("RM20NDK", " onRm20RealTimeSleepState");
  }
  
  public void onRm20ValidBreathingRate(float paramFloat, int paramInt)
  {
    Log.d("RM20NDK", " onRm20ValidBreathingRate");
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\rm20\IndexActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.model;

import android.annotation.SuppressLint;
import com.resmed.refresh.utils.Log;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

@SuppressLint({"DefaultLocale"})
public class RST_SleepStats
{
  private int averageBodyScore = 0;
  private int averageInterruptions = 0;
  private int averageMindScore = 0;
  private int averageSleepTimeMins = 0;
  private int averageTimeInBedMins = 0;
  private int averageTimeToSleep = 0;
  private int averageWakeupTime = 0;
  private Date bestNight;
  private int cumulativeSleepScore = 0;
  
  private static String minutesToHoursMins(int paramInt)
  {
    return String.format("%02d:%02d", new Object[] { Integer.valueOf(paramInt / 60), Integer.valueOf(paramInt % 60) });
  }
  
  public String averageSleepTime()
  {
    return minutesToHoursMins(this.averageSleepTimeMins);
  }
  
  public String averageTimeInBed()
  {
    return minutesToHoursMins(this.averageTimeInBedMins);
  }
  
  public String averageTimeToSleep()
  {
    return minutesToHoursMins(this.averageTimeToSleep);
  }
  
  public String averageWakeupTime()
  {
    return minutesToHoursMins(this.averageWakeupTime);
  }
  
  public void calculateScores(List<RST_SleepSessionInfo> paramList)
  {
    int i = 0;
    int i1 = 0;
    int i2 = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    int i3 = 0;
    int i5 = paramList.size();
    int n = 0;
    Log.d("com.resmed.refresh.model", "Calculating sleep stats based on " + paramList.size() + " sessions");
    Iterator localIterator = paramList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        this.averageWakeupTime = (i / i5);
        Log.d("com.resmed.refresh.model", "averageWakeupTime:" + this.averageWakeupTime);
        this.averageTimeInBedMins = (i1 / i5);
        this.averageInterruptions = (i2 / i5);
        this.averageSleepTimeMins = (j / i5);
        this.cumulativeSleepScore = k;
        this.averageMindScore = (m / i5);
        this.averageBodyScore = (i3 / i5);
        return;
      }
      paramList = (RST_SleepSessionInfo)localIterator.next();
      i1 += paramList.getTimeInBed();
      i2 += paramList.getNumberInterruptions();
      int i4 = j + paramList.getTotalSleepTime();
      k += paramList.getSleepScore();
      m += paramList.getMindScore();
      i3 += paramList.getBodyScore();
      j = n;
      if (paramList.getSleepScore() > n)
      {
        j = paramList.getSleepScore();
        this.bestNight = paramList.getStartTime();
      }
      Calendar localCalendar = GregorianCalendar.getInstance();
      localCalendar.setTime(paramList.getStopTime());
      i += localCalendar.get(11) * 60 + localCalendar.get(12);
      n = j;
      j = i4;
    }
  }
  
  public int getAverageBodyScore()
  {
    return this.averageBodyScore;
  }
  
  public int getAverageInterruptions()
  {
    return this.averageInterruptions;
  }
  
  public int getAverageMindScore()
  {
    return this.averageMindScore;
  }
  
  public Date getBestNight()
  {
    return this.bestNight;
  }
  
  public int getCumulativeSleepScore()
  {
    return this.cumulativeSleepScore;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.utils;

import java.util.Calendar;

public class TimeCalculator
{
  private static final Integer SECONDS_PER_DAY = Integer.valueOf(86400);
  private static final Integer SECONDS_PER_HOUR = Integer.valueOf(3600);
  private static final Integer SECONDS_PER_MINUTE = Integer.valueOf(60);
  
  public static Integer calculateRemainingDaySeconds()
  {
    int i = getTodaySecondsSpent().intValue();
    return Integer.valueOf(SECONDS_PER_DAY.intValue() - i);
  }
  
  public static Integer getTodaySecondsSpent()
  {
    Calendar localCalendar = Calendar.getInstance();
    int i = localCalendar.get(11);
    int k = localCalendar.get(12);
    int j = localCalendar.get(13);
    return Integer.valueOf(SECONDS_PER_HOUR.intValue() * i + SECONDS_PER_MINUTE.intValue() * k + j);
  }
  
  public static Calendar rescheduleNextAlarm(long paramLong, int paramInt)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(paramLong);
    localCalendar.add(5, 1);
    switch (paramInt)
    {
    }
    for (;;)
    {
      localCalendar.set(14, 0);
      localCalendar.set(13, 0);
      return localCalendar;
      paramInt = localCalendar.get(7);
      if (paramInt == 1) {
        localCalendar.add(5, 1);
      } else if (paramInt == 7) {
        localCalendar.add(5, 2);
      }
    }
  }
  
  public static Calendar setCurrentAlarm(long paramLong, int paramInt)
  {
    Calendar localCalendar2 = Calendar.getInstance();
    localCalendar2.setTimeInMillis(paramLong);
    Calendar localCalendar1 = Calendar.getInstance();
    localCalendar1.setTimeInMillis(System.currentTimeMillis());
    if (localCalendar1.getTimeInMillis() > localCalendar2.getTimeInMillis()) {
      localCalendar2.set(localCalendar1.get(1), localCalendar1.get(2), localCalendar1.get(5));
    }
    if (localCalendar1.getTimeInMillis() > localCalendar2.getTimeInMillis()) {
      localCalendar2.add(5, 1);
    }
    switch (paramInt)
    {
    }
    for (;;)
    {
      localCalendar2.set(14, 0);
      localCalendar2.set(13, 0);
      return localCalendar2;
      paramInt = localCalendar2.get(7);
      if (paramInt == 1) {
        localCalendar2.add(5, 1);
      } else if (paramInt == 7) {
        localCalendar2.add(5, 2);
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\TimeCalculator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
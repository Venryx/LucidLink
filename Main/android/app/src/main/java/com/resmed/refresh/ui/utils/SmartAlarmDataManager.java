package com.resmed.refresh.ui.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Process;
import android.widget.Toast;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.smartalarm.services.SmartAlarmService;
import com.resmed.refresh.ui.activity.DismissAlarmActivty;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.TimeCalculator;
import com.resmed.refresh.utils.audio.SoundResources.SmartAlarmSound;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SmartAlarmDataManager
{
  private static final Integer DEFAULT_WAKE_UP_TIME = Integer.valueOf(8);
  public static final Integer TIME_TO_TURN_OFF = Integer.valueOf(28800000);
  private static SmartAlarmDataManager smartAlarmDataManager;
  private final Integer REQUEST_CODE = Integer.valueOf(3372637);
  private boolean activeAlarm;
  private long alarmDateTime;
  private boolean alarmReboot;
  int currentVolume;
  private RefreshModelController refreshModelController;
  private int repeatValue;
  private SoundResources.SmartAlarmSound sound;
  private int soundValue;
  private int windowValue;
  
  public static SmartAlarmDataManager getInstance()
  {
    try
    {
      if (smartAlarmDataManager == null)
      {
        localSmartAlarmDataManager = new com/resmed/refresh/ui/utils/SmartAlarmDataManager;
        localSmartAlarmDataManager.<init>();
        smartAlarmDataManager = localSmartAlarmDataManager;
        smartAlarmDataManager.init();
      }
      SmartAlarmDataManager localSmartAlarmDataManager = smartAlarmDataManager;
      return localSmartAlarmDataManager;
    }
    finally {}
  }
  
  private String getRepeatString()
  {
    String str;
    switch (this.repeatValue)
    {
    default: 
      str = "NOSET";
    }
    for (;;)
    {
      return str;
      str = "Once";
      continue;
      str = "Weekday";
      continue;
      str = "Everyday";
    }
  }
  
  private String getStringTimeForNow(long paramLong)
  {
    long l2 = 1000L * 60L;
    long l3 = l2 * 60L;
    long l4 = l3 * 24L;
    long l1 = paramLong / l4;
    l4 = paramLong % l4;
    paramLong = l4 / l3;
    l3 = l4 % l3;
    l2 = l3 / l2;
    Resources localResources = RefreshApplication.getInstance().getResources();
    StringBuilder localStringBuilder = new StringBuilder();
    if (l1 > 0L)
    {
      localStringBuilder.append(l1);
      localStringBuilder.append(" ");
      if (l1 == 1L)
      {
        localStringBuilder.append(localResources.getString(2131166062));
        localStringBuilder.append(" ");
      }
    }
    else
    {
      if (paramLong > 0L)
      {
        localStringBuilder.append(paramLong);
        localStringBuilder.append(" ");
        if (paramLong != 1L) {
          break label235;
        }
        localStringBuilder.append(localResources.getString(2131166064));
        label161:
        localStringBuilder.append(" ");
      }
      localStringBuilder.append(l2);
      localStringBuilder.append(" ");
      if (l2 != 1L) {
        break label251;
      }
      localStringBuilder.append(localResources.getString(2131166066));
    }
    for (;;)
    {
      localStringBuilder.append(" ");
      return localStringBuilder.toString();
      localStringBuilder.append(localResources.getString(2131166063));
      break;
      label235:
      localStringBuilder.append(localResources.getString(2131166065));
      break label161;
      label251:
      localStringBuilder.append(localResources.getString(2131166067));
    }
  }
  
  private void init()
  {
    this.refreshModelController = RefreshModelController.getInstance();
    try
    {
      this.activeAlarm = this.refreshModelController.getActiveAlarm().booleanValue();
      this.repeatValue = this.refreshModelController.getAlarmRepetition();
      this.soundValue = this.refreshModelController.getAlarmSound();
      this.sound = SoundResources.SmartAlarmSound.getSmartAlarmForId(this.soundValue);
      this.windowValue = this.refreshModelController.getAlarmWindow();
      this.alarmReboot = this.refreshModelController.getAlarmReboot();
      this.alarmDateTime = this.refreshModelController.getAlarmDateTime().longValue();
      if (this.alarmDateTime < 0L)
      {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(System.currentTimeMillis());
        localCalendar.set(11, DEFAULT_WAKE_UP_TIME.intValue());
        this.alarmDateTime = localCalendar.getTimeInMillis();
        this.refreshModelController.storeAlarmDateTime(Long.valueOf(this.alarmDateTime));
        Log.d("time date in init --> ", String.valueOf(new Date(this.alarmDateTime)));
      }
      if (this.windowValue == -1)
      {
        this.windowValue = 15;
        this.refreshModelController.storeAlarmWindow(15);
      }
      if (this.repeatValue == -1)
      {
        this.repeatValue = 2131099657;
        this.refreshModelController.storeAlarmRepetition(this.repeatValue);
      }
      if (this.soundValue == -1)
      {
        this.soundValue = 0;
        this.refreshModelController.storeAlarmSound(Integer.valueOf(this.soundValue));
        this.sound = SoundResources.SmartAlarmSound.getSmartAlarmForId(this.soundValue);
      }
      if (this.soundValue == -1)
      {
        this.soundValue = 2131099666;
        this.refreshModelController.storeAlarmRepetition(this.soundValue);
        this.sound = SoundResources.SmartAlarmSound.getSmartAlarmForId(this.soundValue);
      }
      debugSmartAlarm();
      return;
    }
    catch (NullPointerException localNullPointerException)
    {
      for (;;)
      {
        localNullPointerException.printStackTrace();
      }
    }
  }
  
  private boolean isNowInWindow()
  {
    Calendar localCalendar2 = Calendar.getInstance();
    Calendar localCalendar1 = Calendar.getInstance();
    localCalendar1.setTimeInMillis(this.alarmDateTime);
    localCalendar2.set(11, localCalendar1.get(11));
    localCalendar2.set(12, localCalendar1.get(12));
    localCalendar2.set(13, 0);
    localCalendar2.set(14, 0);
    long l2 = System.currentTimeMillis();
    long l3 = TimeCalculator.setCurrentAlarm(localCalendar2.getTimeInMillis(), this.repeatValue).getTimeInMillis();
    long l1 = l3 - this.windowValue * 60 * 1000;
    Log.i("com.resmed.refresh.smartAlarm", "SmartAlarm isNowInWindow now " + new Date(l2));
    Log.i("com.resmed.refresh.smartAlarm", "SmartAlarm isNowInWindow endOfWindow " + new Date(l3));
    Log.i("com.resmed.refresh.smartAlarm", "SmartAlarm isNowInWindow beginingOfWindow " + new Date(l1));
    if ((l2 >= l1) && (l2 <= l3)) {}
    for (boolean bool = true;; bool = false)
    {
      AppFileLog.addTrace("SmartAlarm isNowInWindow? : " + bool);
      return bool;
    }
  }
  
  private void setUpAlarmIntent()
  {
    stopAlarmManager();
    AlarmManager localAlarmManager = (AlarmManager)RefreshApplication.getInstance().getApplicationContext().getSystemService("alarm");
    Object localObject = new Intent(RefreshApplication.getInstance().getApplicationContext(), DismissAlarmActivty.class);
    ((Intent)localObject).putExtra("BUNDLE_SMART_ALARM_ACTION", 0);
    localObject = PendingIntent.getActivity(RefreshApplication.getInstance().getApplicationContext(), this.REQUEST_CODE.intValue(), (Intent)localObject, 268435456);
    localAlarmManager.set(0, this.alarmDateTime, (PendingIntent)localObject);
    Log.i("com.resmed.refresh.smartAlarm", "alarm goes off in " + (this.alarmDateTime - System.currentTimeMillis()) + " secs, at " + new Date(this.alarmDateTime));
    if (this.alarmReboot)
    {
      this.alarmReboot = false;
      this.refreshModelController.storeAlarmReboot(this.alarmReboot);
    }
  }
  
  private void stopAlarmManager()
  {
    Object localObject = new Intent(RefreshApplication.getInstance().getApplicationContext(), SmartAlarmService.class);
    localObject = PendingIntent.getService(RefreshApplication.getInstance().getApplicationContext(), this.REQUEST_CODE.intValue(), (Intent)localObject, 536870912);
    Log.i("com.resmed.refresh.smartAlarm", "stopAlarmManager " + localObject);
    if (localObject != null) {
      ((PendingIntent)localObject).cancel();
    }
  }
  
  public void debugSmartAlarm()
  {
    Log.d("com.resmed.refresh.smartAlarm", "");
    Log.d("com.resmed.refresh.smartAlarm", "-----------------SmartAlarmDataManager-------------------");
    Log.d("com.resmed.refresh.smartAlarm", "-----------Thread " + Process.myTid() + "-----");
    Log.d("com.resmed.refresh.smartAlarm", "active = " + this.activeAlarm);
    Log.d("com.resmed.refresh.smartAlarm", "alarmDateTime = " + this.alarmDateTime);
    Log.d("com.resmed.refresh.smartAlarm", "alarmDateTime = " + new Date(this.alarmDateTime));
    Log.d("com.resmed.refresh.smartAlarm", "windowValue = " + this.windowValue);
    Log.d("com.resmed.refresh.smartAlarm", "repeatValue = " + this.repeatValue);
    Log.d("com.resmed.refresh.smartAlarm", "soundValue = " + this.soundValue);
    Log.d("com.resmed.refresh.smartAlarm", "-----------------SmartAlarmDataManager-------------------");
    Log.d("com.resmed.refresh.smartAlarm", "");
  }
  
  public void dismissNextAlarm()
  {
    Log.i("com.resmed.refresh.smartAlarm", "dismissNextAlarm");
    if (getRepeatValue() == 2131099657)
    {
      Log.i("com.resmed.refresh.smartAlarm", "repeat_once => inactive and stop");
      setActiveAlarmValue(false);
      stopAlarmManager();
    }
    for (;;)
    {
      return;
      Log.i("com.resmed.refresh.smartAlarm", "Dismiss only next one => Calculating calendar");
      this.alarmDateTime = TimeCalculator.rescheduleNextAlarm(this.alarmDateTime, this.repeatValue).getTimeInMillis();
      this.refreshModelController.storeAlarmDateTime(Long.valueOf(this.alarmDateTime));
      AppFileLog.addTrace("SmartAlarm dismissNextAlarm " + new Date(this.alarmDateTime));
      setUpAlarmIntent();
    }
  }
  
  public boolean getActiveAlarm()
  {
    return this.activeAlarm;
  }
  
  public long getAlarmDateTime()
  {
    return this.alarmDateTime;
  }
  
  public boolean getAlarmReboot()
  {
    return this.alarmReboot;
  }
  
  public int getRepeatValue()
  {
    return this.repeatValue;
  }
  
  public SoundResources.SmartAlarmSound getSoundResource()
  {
    return this.sound;
  }
  
  public String getStringAlarmTime()
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(this.alarmDateTime);
    return new SimpleDateFormat("HH:mm").format(new Date(localCalendar.getTimeInMillis()));
  }
  
  public String getStringAlarmTime(int paramInt)
  {
    if (!this.activeAlarm) {}
    Calendar localCalendar;
    SimpleDateFormat localSimpleDateFormat;
    for (String str = RefreshApplication.getInstance().getString(2131166069);; str = localSimpleDateFormat.format(new Date(localCalendar.getTimeInMillis())) + " - " + str)
    {
      return str;
      localCalendar = Calendar.getInstance();
      localCalendar.setTimeInMillis(this.alarmDateTime);
      localSimpleDateFormat = new SimpleDateFormat("HH:mm");
      str = localSimpleDateFormat.format(new Date(localCalendar.getTimeInMillis()));
      localCalendar.add(12, paramInt * -1);
    }
  }
  
  public int getWindowValue()
  {
    return this.windowValue;
  }
  
  public boolean isWrongScheduledForWeekend()
  {
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (this.repeatValue == 2131099658)
    {
      Calendar localCalendar = Calendar.getInstance();
      Log.e("com.resmed.refresh.smartAlarm", "DAY_OF_WEEK:" + localCalendar.get(7));
      if (localCalendar.get(7) != 1)
      {
        bool1 = bool2;
        if (localCalendar.get(7) != 7) {}
      }
      else
      {
        bool1 = true;
        Log.e("com.resmed.refresh.smartAlarm", "isWrong scheduled => avoid to play");
        AppFileLog.addTrace("SmartAlarm alarm in weekday scheduled on weekend!!! " + new Date(this.alarmDateTime));
        this.alarmDateTime = TimeCalculator.setCurrentAlarm(this.alarmDateTime, this.repeatValue).getTimeInMillis();
        this.refreshModelController.storeAlarmDateTime(Long.valueOf(this.alarmDateTime));
        setUpAlarmIntent();
      }
    }
    return bool1;
  }
  
  public void onDeviceDateTimeChanged()
  {
    AppFileLog.addTrace("SmartAlarm onDeviceDateTimeChanged");
    Log.d("com.resmed.refresh.smartAlarm", "SmartAlarm onDeviceDateTimeChanged");
    if ((getActiveAlarm()) && (!isNowInWindow()))
    {
      Calendar localCalendar2 = Calendar.getInstance();
      Calendar localCalendar1 = Calendar.getInstance();
      localCalendar1.setTimeInMillis(this.alarmDateTime);
      localCalendar2.set(11, localCalendar1.get(11));
      localCalendar2.set(12, localCalendar1.get(12));
      localCalendar2.set(13, 0);
      localCalendar2.set(14, 0);
      this.alarmDateTime = TimeCalculator.setCurrentAlarm(localCalendar2.getTimeInMillis(), this.repeatValue).getTimeInMillis();
      this.refreshModelController.storeAlarmDateTime(Long.valueOf(this.alarmDateTime));
      setUpAlarmIntent();
      AppFileLog.addTrace("SmartAlarm onDeviceDateTimeChanged setAlarmTime to " + new Date(this.alarmDateTime) + " (Repeat value: " + getRepeatString() + ", Song: " + this.sound.getName() + ", Active: " + this.activeAlarm + ")");
      Log.i("com.resmed.refresh.smartAlarm", "onDeviceDateTimeChanged setAlarmTime(" + new Date(this.alarmDateTime) + ")");
    }
  }
  
  public void onReboot()
  {
    this.alarmReboot = true;
    this.refreshModelController.storeAlarmReboot(this.alarmReboot);
    AppFileLog.addTrace("SmartAlarm onReboot");
    Log.d("com.resmed.refresh.smartAlarm", "SmartAlarm onReboot");
    if (getRepeatValue() != 2131099657)
    {
      this.alarmDateTime = TimeCalculator.setCurrentAlarm(this.alarmDateTime, this.repeatValue).getTimeInMillis();
      this.refreshModelController.storeAlarmDateTime(Long.valueOf(this.alarmDateTime));
      AppFileLog.addTrace("SmartAlarm setAlarmReboot " + new Date(this.alarmDateTime));
      Log.d("com.resmed.refresh.smartAlarm", "SmartAlarm setAlarmReboot " + new Date(this.alarmDateTime));
      setUpAlarmIntent();
    }
    for (;;)
    {
      return;
      if (this.alarmDateTime < System.currentTimeMillis())
      {
        setActiveAlarmValue(false);
      }
      else if ((this.refreshModelController.getActiveAlarm().booleanValue()) && (getRepeatValue() == 2131099657))
      {
        AppFileLog.addTrace("Rescheduling the alarm for same day while device was swithced off and switched on again");
        this.alarmDateTime = this.refreshModelController.getAlarmDateTime().longValue();
        setUpAlarmIntent();
      }
    }
  }
  
  public void scheduleAgain()
  {
    if (getRepeatValue() == 2131099657)
    {
      setActiveAlarmValue(false);
      stopAlarmManager();
    }
    for (;;)
    {
      return;
      Calendar localCalendar2 = Calendar.getInstance();
      localCalendar2.setTimeInMillis(this.alarmDateTime);
      Calendar localCalendar1 = Calendar.getInstance();
      localCalendar1.setTimeInMillis(System.currentTimeMillis());
      localCalendar1.set(11, localCalendar2.get(11));
      localCalendar1.set(12, localCalendar2.get(12));
      localCalendar1.set(13, 0);
      localCalendar1.set(14, 0);
      this.alarmDateTime = TimeCalculator.setCurrentAlarm(localCalendar2.getTimeInMillis(), this.repeatValue).getTimeInMillis();
      this.refreshModelController.storeAlarmDateTime(Long.valueOf(this.alarmDateTime));
      AppFileLog.addTrace("SmartAlarm scheduleAgain " + new Date(this.alarmDateTime));
      setUpAlarmIntent();
    }
  }
  
  public void setActiveAlarmValue(boolean paramBoolean)
  {
    Log.d("com.resmed.refresh.smartAlarm", "setActiveAlarmValue(value) activeAlarm = " + this.activeAlarm);
    if (paramBoolean != this.activeAlarm)
    {
      this.refreshModelController.storeActiveAlarm(Boolean.valueOf(paramBoolean));
      this.activeAlarm = paramBoolean;
      AppFileLog.addTrace("SmartAlarm setActiveAlarmValue to " + paramBoolean);
      Log.d("com.resmed.refresh.smartAlarm", "setActiveAlarmValue(" + this.activeAlarm + ")");
      if ((paramBoolean) && (this.alarmDateTime < System.currentTimeMillis()))
      {
        this.alarmDateTime = TimeCalculator.setCurrentAlarm(this.alarmDateTime, this.repeatValue).getTimeInMillis();
        this.refreshModelController.storeAlarmDateTime(Long.valueOf(this.alarmDateTime));
        setUpAlarmIntent();
      }
    }
  }
  
  public void setAlarmTime(int paramInt1, int paramInt2)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(this.alarmDateTime);
    if ((localCalendar.get(11) != paramInt1) || (localCalendar.get(12) != paramInt2))
    {
      localCalendar = Calendar.getInstance();
      localCalendar.set(11, paramInt1);
      localCalendar.set(12, paramInt2);
      localCalendar.set(13, 0);
      localCalendar.set(14, 0);
      this.alarmDateTime = TimeCalculator.setCurrentAlarm(localCalendar.getTimeInMillis(), this.repeatValue).getTimeInMillis();
      this.refreshModelController.storeAlarmDateTime(Long.valueOf(this.alarmDateTime));
      setUpAlarmIntent();
      AppFileLog.addTrace("SmartAlarm setAlarmTime to " + new Date(this.alarmDateTime) + " (Repeat value: " + getRepeatString() + ", Song: " + this.sound.getName() + ", Active: " + this.activeAlarm + ")");
      Log.i("com.resmed.refresh.smartAlarm", "setAlarmTime(" + new Date(this.alarmDateTime) + ")");
    }
  }
  
  public void setRepeatValue(int paramInt)
  {
    if (paramInt != this.repeatValue)
    {
      Log.e("com.resmed.refresh.smartAlarm", "setRepeatValue(" + paramInt + ")");
      this.repeatValue = paramInt;
      this.refreshModelController.storeAlarmRepetition(this.repeatValue);
      Calendar localCalendar2 = Calendar.getInstance();
      localCalendar2.setTimeInMillis(this.alarmDateTime);
      Log.e("com.resmed.refresh.smartAlarm", "alarmTimeStored: " + localCalendar2.getTime());
      Calendar localCalendar1 = Calendar.getInstance();
      localCalendar1.set(11, localCalendar2.get(11));
      localCalendar1.set(12, localCalendar2.get(12));
      localCalendar1.set(13, 0);
      localCalendar1.set(14, 0);
      Log.e("com.resmed.refresh.smartAlarm", "alarmDate: " + localCalendar1.getTime());
      this.alarmDateTime = TimeCalculator.setCurrentAlarm(localCalendar1.getTimeInMillis(), this.repeatValue).getTimeInMillis();
      this.refreshModelController.storeAlarmDateTime(Long.valueOf(this.alarmDateTime));
      setUpAlarmIntent();
      AppFileLog.addTrace("SmartAlarm setRepeatValue to " + getRepeatString() + " alarmDateTime:" + new Date(this.alarmDateTime));
      Log.i("com.resmed.refresh.smartAlarm", "setRepeatValue(" + paramInt + ")" + " alarmDateTime:" + new Date(this.alarmDateTime));
    }
  }
  
  public void setSoundValue(int paramInt)
  {
    this.soundValue = paramInt;
    this.refreshModelController.storeAlarmSound(Integer.valueOf(this.soundValue));
    this.sound = SoundResources.SmartAlarmSound.getSmartAlarmForId(this.soundValue);
  }
  
  public void setWindowValue(int paramInt)
  {
    this.refreshModelController.storeAlarmWindow(paramInt);
    this.windowValue = paramInt;
    Log.i("com.resmed.refresh.smartAlarm", "setWindowValue(" + paramInt + ")");
  }
  
  public void showTimeFromNow()
  {
    Object localObject2;
    Context localContext;
    Object localObject1;
    Object localObject3;
    if (this.activeAlarm)
    {
      long l = System.currentTimeMillis();
      localObject2 = new SimpleDateFormat("dd/M/yyyy HH:mm");
      localContext = RefreshApplication.getInstance().getApplicationContext();
      localObject1 = ((SimpleDateFormat)localObject2).format(new Date(this.alarmDateTime));
      localObject3 = ((SimpleDateFormat)localObject2).format(new Date(l));
    }
    try
    {
      localObject1 = ((SimpleDateFormat)localObject2).parse(((String)localObject1).toString());
      localObject3 = ((SimpleDateFormat)localObject2).parse(((String)localObject3).toString());
      localObject2 = new java.lang.StringBuilder;
      ((StringBuilder)localObject2).<init>(String.valueOf(localContext.getString(2131166060)));
      Toast.makeText(localContext, " " + getStringTimeForNow(((Date)localObject1).getTime() - ((Date)localObject3).getTime()) + localContext.getString(2131166061), 1).show();
      return;
    }
    catch (ParseException localParseException)
    {
      for (;;)
      {
        localParseException.printStackTrace();
      }
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
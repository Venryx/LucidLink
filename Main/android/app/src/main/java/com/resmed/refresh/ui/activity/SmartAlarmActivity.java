package com.resmed.refresh.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.resmed.refresh.ui.fragments.SmartAlarmFragment;
import com.resmed.refresh.ui.fragments.SmartAlarmRepeatFragment;
import com.resmed.refresh.ui.fragments.SmartAlarmSoundFragment;
import com.resmed.refresh.ui.fragments.SmartAlarmTimeFragment;
import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.ActiveFragmentInterface;
import com.resmed.refresh.ui.utils.SleepButtonObserver;
import com.resmed.refresh.ui.utils.SmartAlarmDataManager;
import com.resmed.refresh.utils.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SmartAlarmActivity
  extends BaseActivity
  implements SmartAlarmFragment.SmartAlarmBtn, SmartAlarmTimeFragment.SetTimeBtn, ActiveFragmentInterface
{
  public static final String ALARM_WAKE_TIME = "ALARM_WAKE_TIME";
  public static boolean isHomeButtonPressSmartAlaram = false;
  public final int SMART_ALARM_FRAGMENT = 0;
  public final int SMART_ALARM_REPEAT_FRAGMENT = 1;
  public final int SMART_ALARM_SOUND_FRAGMENT = 2;
  public final int SMART_ALARM_TIME_FRAGMENT = 3;
  private int currentFragment;
  private Map<Integer, BaseFragment> fragmentList;
  private List<Integer> fragmentStack;
  private Handler handler;
  private boolean isScreenLock = false;
  private Runnable r;
  private SleepButtonObserver sleepButtonObserver;
  private SmartAlarmDataManager smartAlarmDataManager;
  
  private BaseFragment getFragmentByType(int paramInt)
  {
    Object localObject;
    if (!this.fragmentList.containsKey(Integer.valueOf(paramInt))) {
      switch (paramInt)
      {
      default: 
        localObject = null;
      }
    }
    for (;;)
    {
      return (BaseFragment)localObject;
      localObject = new SmartAlarmFragment();
      this.fragmentList.put(Integer.valueOf(0), localObject);
      continue;
      localObject = new SmartAlarmRepeatFragment();
      this.fragmentList.put(Integer.valueOf(1), localObject);
      continue;
      localObject = new SmartAlarmSoundFragment();
      this.fragmentList.put(Integer.valueOf(2), localObject);
      continue;
      localObject = new SmartAlarmTimeFragment();
      this.fragmentList.put(Integer.valueOf(3), localObject);
      continue;
      localObject = (BaseFragment)this.fragmentList.get(Integer.valueOf(paramInt));
    }
  }
  
  private FragmentManager.OnBackStackChangedListener getListener()
  {
    new FragmentManager.OnBackStackChangedListener()
    {
      public void onBackStackChanged()
      {
        FragmentManager localFragmentManager = SmartAlarmActivity.this.getSupportFragmentManager();
        if (SmartAlarmActivity.this.fragmentStack.size() > localFragmentManager.getBackStackEntryCount() + 1)
        {
          SmartAlarmActivity.this.currentFragment = ((Integer)SmartAlarmActivity.this.fragmentStack.get(SmartAlarmActivity.this.fragmentStack.size() - 2)).intValue();
          SmartAlarmActivity.this.fragmentStack.remove(SmartAlarmActivity.this.fragmentStack.get(SmartAlarmActivity.this.fragmentStack.size() - 1));
          SmartAlarmActivity.this.updateHeaderLabel(SmartAlarmActivity.this.currentFragment);
        }
      }
    };
  }
  
  private boolean isPresent(int paramInt)
  {
    boolean bool = false;
    Iterator localIterator = this.fragmentStack.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return bool;
      }
      if (((Integer)localIterator.next()).intValue() == paramInt) {
        bool = true;
      }
    }
  }
  
  private void startHandler(long paramLong)
  {
    if (!BaseBluetoothActivity.IN_SLEEP_SESSION)
    {
      this.handler.postDelayed(this.r, paramLong);
      System.out.println("1234handler started Mindclear");
    }
  }
  
  private void updateHeaderLabel(int paramInt)
  {
    switch (this.currentFragment)
    {
    }
    for (;;)
    {
      return;
      showRightButton(2130837770);
      continue;
      hideRightButton();
      continue;
      showRightButton(2130837782);
      continue;
      showRightButton(2130837782);
    }
  }
  
  public void cancelClick()
  {
    getSupportFragmentManager().popBackStack();
  }
  
  public void doneClick(int paramInt1, int paramInt2)
  {
    ((SmartAlarmFragment)this.fragmentList.get(Integer.valueOf(0))).updateTimeLabel();
    this.smartAlarmDataManager.setAlarmTime(paramInt1, paramInt2);
    showRightButton(2130837770);
    getSupportFragmentManager().popBackStack();
  }
  
  public BaseFragment getCurrentFragment()
  {
    return (BaseFragment)this.fragmentList.get(this.fragmentStack.get(this.fragmentStack.size() - 1));
  }
  
  public void notifyCurrentFragment(int paramInt)
  {
    this.currentFragment = paramInt;
    updateHeaderLabel(paramInt);
    if (!isPresent(paramInt)) {
      this.fragmentStack.add(Integer.valueOf(paramInt));
    }
  }
  
  public void onBackPressed()
  {
    SmartAlarmDataManager.getInstance().showTimeFromNow();
    super.onBackPressed();
    SleepTimeActivity.isAfterBackPress = true;
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903096);
    isHomeButtonPressSmartAlaram = false;
    this.smartAlarmDataManager = SmartAlarmDataManager.getInstance();
    this.sleepButtonObserver = SleepButtonObserver.getInstance();
    this.fragmentList = new LinkedHashMap();
    setTitle(2131166053);
    if (getIntent().getBooleanExtra("com.resmed.refresh.ui.uibase.app.activity_modal", false)) {
      setIsModalActivity(true);
    }
    for (;;)
    {
      getSupportFragmentManager().addOnBackStackChangedListener(getListener());
      this.fragmentStack = new ArrayList();
      paramBundle = getSupportFragmentManager().beginTransaction();
      paramBundle.replace(2131099838, getFragmentByType(0));
      paramBundle.commit();
      this.handler = new Handler();
      this.r = new Runnable()
      {
        public void run()
        {
          for (;;)
          {
            try
            {
              SleepTimeActivity.isStartTimer = true;
              SleepTimeActivity.isAfterBackPress = false;
              if ((!SmartAlarmActivity.isHomeButtonPressSmartAlaram) && (!SmartAlarmActivity.this.isScreenLock)) {
                continue;
              }
              if ((SmartAlarmActivity.this.isScreenLock) && (SmartAlarmActivity.isHomeButtonPressSmartAlaram))
              {
                SmartAlarmActivity.isHomeButtonPressSmartAlaram = false;
                SmartAlarmActivity.this.isScreenLock = false;
                ((PowerManager)SmartAlarmActivity.this.getApplicationContext().getSystemService("power")).newWakeLock(268435482, "TAG").acquire();
                SmartAlarmActivity.this.getWindow().addFlags(4194304);
                SmartAlarmActivity.this.finish();
                return;
              }
            }
            catch (Exception localException)
            {
              Intent localIntent;
              localException.printStackTrace();
              continue;
              SmartAlarmActivity.this.finish();
              continue;
            }
            SmartAlarmActivity.isHomeButtonPressSmartAlaram = false;
            localIntent = new android/content/Intent;
            localIntent.<init>(SmartAlarmActivity.this.getApplicationContext(), SmartAlarmActivity.class);
            localIntent.addFlags(131072);
            localIntent.addFlags(16777216);
            SmartAlarmActivity.this.startActivity(localIntent);
            SmartAlarmActivity.this.finish();
          }
        }
      };
      startHandler(120000L);
      return;
      setTypeRefreshBar(BaseActivity.TypeBar.DEFAULT);
    }
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    stopHandler();
  }
  
  protected void onPause()
  {
    super.onPause();
    if (!((PowerManager)getSystemService("power")).isScreenOn()) {
      this.isScreenLock = true;
    }
  }
  
  protected void onStop()
  {
    super.onStop();
    isHomeButtonPressSmartAlaram = true;
  }
  
  public void onUserInteraction()
  {
    super.onUserInteraction();
    stopHandler();
    startHandler(120000L);
  }
  
  protected void rightBtnPressed()
  {
    switch (this.currentFragment)
    {
    }
    for (;;)
    {
      return;
      getSupportFragmentManager().popBackStack();
      showRightButton(2130837770);
      ((SmartAlarmFragment)this.fragmentList.get(Integer.valueOf(0))).updateRepeatLabel();
      this.currentFragment = 0;
      continue;
      getSupportFragmentManager().popBackStack();
      showRightButton(2130837770);
      ((SmartAlarmFragment)this.fragmentList.get(Integer.valueOf(0))).updateSoundLabel();
      this.currentFragment = 0;
      continue;
      onBackPressed();
    }
  }
  
  public void setActiveAlarm(Boolean paramBoolean)
  {
    this.sleepButtonObserver.updateSmartAlartmIcon(this.smartAlarmDataManager.getActiveAlarm(), this.smartAlarmDataManager.getStringAlarmTime());
  }
  
  public void setRepeatClick()
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.setCustomAnimations(2130968595, 2130968592, 2130968591, 2130968596);
    localFragmentTransaction.replace(2131099838, getFragmentByType(1));
    localFragmentTransaction.addToBackStack(null);
    localFragmentTransaction.commit();
  }
  
  public void setSoundClick()
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.setCustomAnimations(2130968595, 2130968592, 2130968591, 2130968596);
    localFragmentTransaction.replace(2131099838, getFragmentByType(2));
    localFragmentTransaction.addToBackStack(null);
    localFragmentTransaction.commit();
  }
  
  public void setTimeClick()
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.setCustomAnimations(2130968595, 2130968592, 2130968591, 2130968596);
    localFragmentTransaction.replace(2131099838, getFragmentByType(3));
    localFragmentTransaction.addToBackStack(null);
    localFragmentTransaction.commit();
  }
  
  public void stopHandler()
  {
    Log.d("com.resmed.refresh.pair", "handler started");
    this.handler.removeCallbacks(this.r);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\SmartAlarmActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
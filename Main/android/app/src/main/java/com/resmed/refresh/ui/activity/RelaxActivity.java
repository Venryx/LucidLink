package com.resmed.refresh.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.resmed.refresh.ui.fragments.RelaxFragment;
import com.resmed.refresh.ui.fragments.RelaxSoundFragment;
import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.ActiveFragmentInterface;
import com.resmed.refresh.ui.utils.RelaxDataManager;
import com.resmed.refresh.ui.utils.SleepButtonObserver;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.audio.SoundResources.RelaxSound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RelaxActivity
  extends BaseActivity
  implements RelaxFragment.RelaxBtn, ActiveFragmentInterface
{
  public static boolean isHomeButtonPressRelax = false;
  public final int RELAX_FRAGMENT = 0;
  public final int RELAX_SOUND_FRAGMENT = 1;
  private int currentFragment;
  private Map<Integer, BaseFragment> fragmentList;
  private List<Integer> fragmentStack;
  private Handler handler;
  private boolean isScreenLock = false;
  private Runnable r;
  private RelaxDataManager relaxDataManager;
  private SleepButtonObserver sleepButtonObserver;
  
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
      localObject = new RelaxFragment();
      this.fragmentList.put(Integer.valueOf(0), localObject);
      continue;
      localObject = new RelaxSoundFragment();
      this.fragmentList.put(Integer.valueOf(1), localObject);
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
        FragmentManager localFragmentManager = RelaxActivity.this.getSupportFragmentManager();
        if (RelaxActivity.this.fragmentStack.size() > localFragmentManager.getBackStackEntryCount() + 1)
        {
          RelaxActivity.this.currentFragment = ((Integer)RelaxActivity.this.fragmentStack.get(RelaxActivity.this.fragmentStack.size() - 2)).intValue();
          RelaxActivity.this.fragmentStack.remove(RelaxActivity.this.fragmentStack.get(RelaxActivity.this.fragmentStack.size() - 1));
          RelaxActivity.this.updateHeaderLabel(RelaxActivity.this.currentFragment);
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
      showRightButton(2130837782);
    }
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
    super.onBackPressed();
    SleepTimeActivity.isAfterBackPress = true;
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903086);
    isHomeButtonPressRelax = false;
    this.sleepButtonObserver = SleepButtonObserver.getInstance();
    this.relaxDataManager = RelaxDataManager.getInstance();
    showRightButton(2130837770);
    setTitle(2131165936);
    if (getIntent().getBooleanExtra("com.resmed.refresh.ui.uibase.app.activity_modal", false)) {
      setIsModalActivity(true);
    }
    for (;;)
    {
      getSupportFragmentManager().addOnBackStackChangedListener(getListener());
      this.fragmentList = new LinkedHashMap();
      this.fragmentStack = new ArrayList();
      paramBundle = getSupportFragmentManager().beginTransaction();
      paramBundle.replace(2131099804, getFragmentByType(0));
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
              if ((!RelaxActivity.isHomeButtonPressRelax) && (!RelaxActivity.this.isScreenLock)) {
                continue;
              }
              if ((RelaxActivity.this.isScreenLock) && (RelaxActivity.isHomeButtonPressRelax))
              {
                RelaxActivity.isHomeButtonPressRelax = false;
                RelaxActivity.this.isScreenLock = false;
                ((PowerManager)RelaxActivity.this.getApplicationContext().getSystemService("power")).newWakeLock(268435482, "TAG").acquire();
                RelaxActivity.this.getWindow().addFlags(4194304);
                RelaxActivity.this.finish();
                return;
              }
            }
            catch (Exception localException)
            {
              Intent localIntent;
              localException.printStackTrace();
              continue;
              RelaxActivity.this.finish();
              continue;
            }
            RelaxActivity.isHomeButtonPressRelax = false;
            localIntent = new android/content/Intent;
            localIntent.<init>(RelaxActivity.this.getApplicationContext(), RelaxActivity.class);
            localIntent.addFlags(131072);
            localIntent.addFlags(16777216);
            RelaxActivity.this.startActivity(localIntent);
            RelaxActivity.this.finish();
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
    isHomeButtonPressRelax = true;
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
      super.onBackPressed();
      continue;
      getSupportFragmentManager().popBackStack();
      updateHeaderLabel(0);
      ((RelaxFragment)this.fragmentList.get(Integer.valueOf(0))).updateSoundText();
    }
  }
  
  public void setActiveRelax()
  {
    this.sleepButtonObserver.updateRelaxIcon(this.relaxDataManager.getActiveRelax(), Integer.valueOf(this.relaxDataManager.getRelaxSound().getId()));
  }
  
  public void setRelaxSoundClick()
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.setCustomAnimations(2130968595, 2130968592, 2130968591, 2130968596);
    localFragmentTransaction.replace(2131099804, getFragmentByType(1));
    localFragmentTransaction.addToBackStack(null);
    localFragmentTransaction.commit();
  }
  
  public void stopHandler()
  {
    Log.d("com.resmed.refresh.pair", "handler started");
    this.handler.removeCallbacks(this.r);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\RelaxActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
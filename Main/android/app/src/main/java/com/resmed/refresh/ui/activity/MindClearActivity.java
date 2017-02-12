package com.resmed.refresh.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.PowerManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.resmed.refresh.model.mindclear.MindClearBase;
import com.resmed.refresh.model.mindclear.MindClearManager;
import com.resmed.refresh.model.mindclear.MindClearText;
import com.resmed.refresh.model.mindclear.MindClearVoice;
import com.resmed.refresh.sleepsession.RST_SleepSession;
import com.resmed.refresh.ui.fragments.MindClearFragment;
import com.resmed.refresh.ui.fragments.MindClearNewTextFragment;
import com.resmed.refresh.ui.fragments.MindClearPagerFragment;
import com.resmed.refresh.ui.fragments.MindClearVoiceFragment;
import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.ActiveFragmentInterface;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.utils.InternalFileProvider;
import com.resmed.refresh.utils.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MindClearActivity
  extends BaseActivity
  implements MindClearFragment.MindClearBtn, MindClearVoiceFragment.MindClearVoiceBtn, ActiveFragmentInterface
{
  public static boolean isBackPressed = false;
  public static boolean isHomeButtonPressMindClear = false;
  public final int MIND_CLEAR_FRAGMENT = 0;
  public final int MIND_CLEAR_NOTE_FRAGMENT = 1;
  public final int MIND_CLEAR_TYPE_FRAGMENT = 2;
  public final int MIND_CLEAR_VOICE_FRAGMENT = 3;
  private int currentFragment;
  private Map<Integer, BaseFragment> fragmentList;
  private List<Integer> fragmentStack;
  private Handler handler;
  private boolean isFromHome;
  private boolean isScreenLock = false;
  private Runnable r;
  
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
      localObject = new MindClearFragment();
      this.fragmentList.put(Integer.valueOf(0), localObject);
      continue;
      localObject = new MindClearPagerFragment();
      this.fragmentList.put(Integer.valueOf(1), localObject);
      continue;
      localObject = new MindClearNewTextFragment();
      this.fragmentList.put(Integer.valueOf(2), localObject);
      continue;
      localObject = new MindClearVoiceFragment();
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
        FragmentManager localFragmentManager = MindClearActivity.this.getSupportFragmentManager();
        if (MindClearActivity.this.fragmentStack.size() > localFragmentManager.getBackStackEntryCount() + 1)
        {
          MindClearActivity.this.currentFragment = ((Integer)MindClearActivity.this.fragmentStack.get(MindClearActivity.this.fragmentStack.size() - 2)).intValue();
          MindClearActivity.this.fragmentStack.remove(MindClearActivity.this.fragmentStack.get(MindClearActivity.this.fragmentStack.size() - 1));
          MindClearActivity.this.updateHeaderLabel(MindClearActivity.this.currentFragment);
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
  
  private void launchShareIntent()
  {
    Intent localIntent = new Intent("android.intent.action.SEND");
    Object localObject = (MindClearBase)MindClearManager.getInstance().getMindClearNotes().get(MindClearManager.getInstance().getCurrentPosition());
    Log.e("com.resmed.refresh.mindClear", "\t\t\t\tlaunchShareIntent getCurrentPosition(" + MindClearManager.getInstance().getCurrentPosition() + ")");
    if ((localObject instanceof MindClearText))
    {
      Log.e("com.resmed.refresh.mindClear", "\t\t\t\tlaunchShareIntent instanceof MindClearText");
      localIntent.setType("text/plain");
      localIntent.putExtra("android.intent.extra.SUBJECT", getResources().getString(2131165365));
      localIntent.putExtra("android.intent.extra.TEXT", ((MindClearBase)localObject).getDescription());
      Log.e("", "Sharing text file");
    }
    for (;;)
    {
      localIntent.setType("message/rfc822");
      try
      {
        startActivity(Intent.createChooser(localIntent, getString(2131165366)));
        return;
        localIntent.setType("audio/*");
        Log.e("com.resmed.refresh.mindClear", "\t\t\t\tlaunchShareIntent instanceof MindClearVoice");
        File localFile = new File(((MindClearVoice)localObject).getFileName());
        localObject = Uri.parse(InternalFileProvider.CONTENT_URI + localFile.getName());
        if (Consts.USE_EXTERNAL_STORAGE) {
          localObject = Uri.parse("file://" + localFile);
        }
        Log.e("", "Sharing audio fie:" + localObject);
        localIntent.putExtra("android.intent.extra.STREAM", (Parcelable)localObject);
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
        for (;;)
        {
          localActivityNotFoundException.printStackTrace();
        }
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
      hideRightButton();
      continue;
      showRightButton(2130837782);
      continue;
      showRightButton(2130837785);
    }
  }
  
  public BaseFragment getCurrentFragment()
  {
    return (BaseFragment)this.fragmentList.get(this.fragmentStack.get(this.fragmentStack.size() - 1));
  }
  
  public void goToMindClearTypeFragment()
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.setCustomAnimations(2130968595, 2130968592, 2130968591, 2130968596);
    localFragmentTransaction.replace(2131099769, getFragmentByType(2));
    localFragmentTransaction.addToBackStack(null);
    localFragmentTransaction.commit();
  }
  
  public void goToMindClearVoiceFragment()
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.setCustomAnimations(2130968595, 2130968592, 2130968591, 2130968596);
    localFragmentTransaction.replace(2131099769, getFragmentByType(3));
    localFragmentTransaction.addToBackStack(null);
    localFragmentTransaction.commit();
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
    SleepTimeActivity.isAfterBackPress = true;
    Log.i("com.resmed.refresh.mindClear", "current fragment: " + getCurrentFragment());
    if ((this.fragmentList.containsKey(Integer.valueOf(3))) && ((getCurrentFragment() instanceof MindClearVoiceFragment))) {
      ((MindClearVoiceFragment)getCurrentFragment()).discardRecording(true);
    }
    isBackPressed = true;
    super.onBackPressed();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903067);
    isHomeButtonPressMindClear = false;
    getWindow().addFlags(128);
    this.fragmentList = new LinkedHashMap();
    setTitle(2131165359);
    if (getIntent().getBooleanExtra("com.resmed.refresh.ui.uibase.app.activity_modal", false))
    {
      setIsModalActivity(true);
      if (RST_SleepSession.getInstance().isSessionRunning()) {
        setTypeRefreshBar(BaseActivity.TypeBar.NO_BED);
      }
    }
    getSupportFragmentManager().addOnBackStackChangedListener(getListener());
    this.fragmentStack = new ArrayList();
    paramBundle = getSupportFragmentManager().beginTransaction();
    paramBundle.replace(2131099769, getFragmentByType(0));
    paramBundle.commit();
    this.isFromHome = getIntent().getBooleanExtra("IsFromHome", false);
    this.handler = new Handler();
    this.r = new Runnable()
    {
      public void run()
      {
        for (;;)
        {
          try
          {
            Log.d("com.resmed.refresh.pair", "Timer started");
            SleepTimeActivity.isAfterBackPress = false;
            if ((!MindClearActivity.isHomeButtonPressMindClear) && (!MindClearActivity.this.isScreenLock)) {
              continue;
            }
            if ((MindClearActivity.this.isScreenLock) && (MindClearActivity.isHomeButtonPressMindClear))
            {
              MindClearActivity.isHomeButtonPressMindClear = false;
              MindClearActivity.this.isScreenLock = false;
              ((PowerManager)MindClearActivity.this.getApplicationContext().getSystemService("power")).newWakeLock(268435482, "TAG").acquire();
              MindClearActivity.this.getWindow().addFlags(4194304);
              MindClearActivity.this.finish();
              return;
            }
          }
          catch (Exception localException)
          {
            Intent localIntent;
            localException.printStackTrace();
            continue;
            MindClearActivity.this.finish();
            continue;
          }
          MindClearActivity.isHomeButtonPressMindClear = false;
          localIntent = new android/content/Intent;
          localIntent.<init>(MindClearActivity.this.getApplicationContext(), MindClearActivity.class);
          localIntent.addFlags(131072);
          localIntent.addFlags(16777216);
          MindClearActivity.this.startActivity(localIntent);
          MindClearActivity.this.finish();
        }
      }
    };
    if (!this.isFromHome)
    {
      startHandler(120000L);
      SleepTimeActivity.isStartTimer = true;
    }
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    if (!this.isFromHome) {
      stopHandler();
    }
  }
  
  public void onPause()
  {
    super.onPause();
    MindClearManager.getInstance().storeDataIntoFilePreference();
    isBackPressed = false;
    if (!((PowerManager)getSystemService("power")).isScreenOn()) {
      this.isScreenLock = true;
    }
  }
  
  protected void onStop()
  {
    super.onStop();
    isHomeButtonPressMindClear = true;
  }
  
  public void onUserInteraction()
  {
    super.onUserInteraction();
    if (!this.isFromHome)
    {
      stopHandler();
      startHandler(120000L);
    }
  }
  
  public void performListClick()
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.setCustomAnimations(2130968595, 2130968592, 2130968591, 2130968596);
    localFragmentTransaction.replace(2131099769, getFragmentByType(1));
    localFragmentTransaction.addToBackStack(null);
    localFragmentTransaction.commit();
  }
  
  protected void rightBtnPressed()
  {
    switch (this.currentFragment)
    {
    }
    for (;;)
    {
      return;
      ((MindClearFragment)getSupportFragmentManager().findFragmentById(2131099769)).deleteItems();
      hideRightButton();
      continue;
      MindClearManager.getInstance().addMindClearText();
      ((MindClearNewTextFragment)this.fragmentList.get(Integer.valueOf(2))).hideKeyboard();
      getSupportFragmentManager().popBackStack();
      this.currentFragment = 0;
      continue;
      launchShareIntent();
    }
  }
  
  public void setVoiceButton()
  {
    ((MindClearFragment)this.fragmentList.get(Integer.valueOf(0))).refreshList();
    getSupportFragmentManager().popBackStack();
  }
  
  public void stopHandler()
  {
    Log.d("com.resmed.refresh.pair", "handler started");
    this.handler.removeCallbacks(this.r);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\MindClearActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
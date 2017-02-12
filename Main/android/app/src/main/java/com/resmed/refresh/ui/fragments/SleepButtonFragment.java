package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.resmed.refresh.ui.font.GroteskBold;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.RelaxDataManager;
import com.resmed.refresh.ui.utils.SleepButtonObserver;
import com.resmed.refresh.ui.utils.SleepButtonObserverInterface;
import com.resmed.refresh.ui.utils.SmartAlarmDataManager;
import com.resmed.refresh.utils.audio.SoundResources.RelaxSound;

public class SleepButtonFragment
  extends BaseFragment
  implements SleepButtonObserverInterface
{
  private boolean activeRelax;
  private boolean isPlaying;
  private boolean isTrackScreen;
  private View.OnClickListener mindClearBtnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      SleepButtonFragment.this.setSleepButtonBtn.mindClearClick();
    }
  };
  private Button mindClearButton;
  private View.OnClickListener relaxBtnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if (SleepButtonFragment.this.isTrackScreen) {
        if (SleepButtonFragment.this.isPlaying)
        {
          SleepButtonFragment.this.isPlaying = false;
          SleepButtonFragment.this.relaxButton.setBackgroundResource(2130837835);
          SleepButtonFragment.this.setSleepButtonBtn.relaxClick(1);
          SleepButtonFragment.this.relaxButton.setOnClickListener(null);
          new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
          {
            public void run()
            {
              try
              {
                SleepButtonFragment.this.relaxButton.setOnClickListener(SleepButtonFragment.this.relaxBtnClickListener);
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
          }, 1000L);
        }
      }
      for (;;)
      {
        return;
        SleepButtonFragment.this.isPlaying = true;
        SleepButtonFragment.this.relaxButton.setBackgroundResource(2130837938);
        SleepButtonFragment.this.setSleepButtonBtn.relaxClick(2);
        break;
        SleepButtonFragment.this.setSleepButtonBtn.relaxClick(0);
      }
    }
  };
  private Button relaxButton;
  private RelaxDataManager relaxDataManager;
  private GroteskBold relaxLabel;
  private SetSleepButtonBtn setSleepButtonBtn;
  private SleepButtonObserver sleepButtonObserver;
  private Button smartAlarmButton;
  private View.OnClickListener smartWakeUpBtnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      SleepButtonFragment.this.setSleepButtonBtn.smartAlarmClick();
    }
  };
  private GroteskBold timeAlarmLabel;
  
  private void changeRelaxButtonIcon()
  {
    this.relaxButton.setBackgroundResource(2130837938);
    this.relaxLabel.setVisibility(8);
  }
  
  public void displayPlayIcon()
  {
    this.relaxButton.setBackgroundResource(2130837835);
    this.isPlaying = false;
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.setSleepButtonBtn = ((SetSleepButtonBtn)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new ClassCastException(paramActivity.toString() + " ...you must implement SetSleepButtonBtn from your Activity ;-) !");
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903165, paramViewGroup, false);
    this.timeAlarmLabel = ((GroteskBold)paramLayoutInflater.findViewById(2131100376));
    this.relaxLabel = ((GroteskBold)paramLayoutInflater.findViewById(2131100380));
    this.smartAlarmButton = ((Button)paramLayoutInflater.findViewById(2131100375));
    this.mindClearButton = ((Button)paramLayoutInflater.findViewById(2131100372));
    this.relaxButton = ((Button)paramLayoutInflater.findViewById(2131100379));
    this.smartAlarmButton.setOnClickListener(this.smartWakeUpBtnClickListener);
    this.mindClearButton.setOnClickListener(this.mindClearBtnClickListener);
    this.relaxButton.setOnClickListener(this.relaxBtnClickListener);
    this.sleepButtonObserver = SleepButtonObserver.getInstance();
    this.sleepButtonObserver.register(this);
    boolean bool;
    if (getArguments() != null)
    {
      bool = getArguments().getBoolean("sleepTrackFragment");
      this.isTrackScreen = bool;
      if (getArguments() == null) {
        break label219;
      }
      bool = getArguments().getBoolean("displayRelax");
      label164:
      this.activeRelax = bool;
      if (this.isTrackScreen)
      {
        if (!this.activeRelax) {
          break label225;
        }
        changeRelaxButtonIcon();
        this.isPlaying = true;
        this.relaxButton.setBackgroundResource(2130837938);
      }
    }
    for (;;)
    {
      this.relaxLabel.setVisibility(8);
      return paramLayoutInflater;
      bool = false;
      break;
      label219:
      bool = true;
      break label164;
      label225:
      this.isPlaying = false;
      this.relaxButton.setBackgroundResource(2130837835);
    }
  }
  
  public void onDestroy()
  {
    super.onDestroy();
    this.sleepButtonObserver.unRegister(this);
  }
  
  public void onResume()
  {
    super.onResume();
    SmartAlarmDataManager localSmartAlarmDataManager = SmartAlarmDataManager.getInstance();
    this.relaxDataManager = RelaxDataManager.getInstance();
    if (localSmartAlarmDataManager.getActiveAlarm())
    {
      updateSmartAlartmIcon(true, localSmartAlarmDataManager.getStringAlarmTime());
      if (!this.relaxDataManager.getActiveRelax()) {
        break label72;
      }
      updateRelaxIcon(true, Integer.valueOf(this.relaxDataManager.getRelaxSound().getId()));
    }
    for (;;)
    {
      return;
      updateSmartAlartmIcon(false, localSmartAlarmDataManager.getStringAlarmTime());
      break;
      label72:
      updateRelaxIcon(false, Integer.valueOf(this.relaxDataManager.getRelaxSound().getId()));
    }
  }
  
  public void updateRelaxIcon(final boolean paramBoolean, final Integer paramInteger)
  {
    if ((!this.isTrackScreen) && (getActivity() != null)) {
      getActivity().runOnUiThread(new Runnable()
      {
        public void run()
        {
          int i;
          if (paramBoolean) {
            i = 2130837849;
          }
          for (;;)
          {
            try
            {
              SleepButtonFragment.this.relaxLabel.setText(SoundResources.RelaxSound.getRelaxForId(paramInteger.intValue()).getName());
              SleepButtonFragment.this.relaxButton.setBackgroundResource(i);
              return;
            }
            catch (Exception localException1)
            {
              localException1.printStackTrace();
              continue;
            }
            i = 2130837850;
            try
            {
              SleepButtonFragment.this.relaxLabel.setText("");
            }
            catch (Exception localException2)
            {
              localException2.printStackTrace();
            }
          }
        }
      });
    }
  }
  
  public void updateSmartAlartmIcon(final boolean paramBoolean, final String paramString)
  {
    if (getActivity() != null) {
      getActivity().runOnUiThread(new Runnable()
      {
        public void run()
        {
          int i;
          if (paramBoolean) {
            i = 2130837843;
          }
          for (;;)
          {
            try
            {
              SleepButtonFragment.this.timeAlarmLabel.setText(paramString);
              SleepButtonFragment.this.smartAlarmButton.setBackgroundResource(i);
              return;
            }
            catch (Exception localException1)
            {
              localException1.printStackTrace();
              continue;
            }
            i = 2130837846;
            try
            {
              SleepButtonFragment.this.timeAlarmLabel.setText("");
            }
            catch (Exception localException2)
            {
              localException2.printStackTrace();
            }
          }
        }
      });
    }
  }
  
  public static abstract interface SetSleepButtonBtn
  {
    public abstract void mindClearClick();
    
    public abstract void relaxClick(int paramInt);
    
    public abstract void smartAlarmClick();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SleepButtonFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.resmed.refresh.ui.activity.SmartAlarmActivity;
import com.resmed.refresh.ui.customview.CustomSeekBarThumbDrawable;
import com.resmed.refresh.ui.customview.CustomSwitch;
import com.resmed.refresh.ui.font.GroteskLight;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.ActiveFragmentInterface;
import com.resmed.refresh.ui.utils.SmartAlarmDataManager;
import com.resmed.refresh.ui.utils.SmartAlarmObserver;
import com.resmed.refresh.ui.utils.SmartAlarmObserverInterface;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.audio.SoundResources.SmartAlarmSound;

public class SmartAlarmFragment
  extends BaseFragment
  implements SmartAlarmObserverInterface, View.OnClickListener
{
  private TextView alarmRepetition;
  private TextView alertTime;
  private ActiveFragmentInterface mcActiveFragmentInterface;
  private RelativeLayout repeatSelection;
  private SeekBar seekBar;
  private SmartAlarmBtn setAlarmBtn;
  private RelativeLayout soundSelection;
  private TextView soundSelectionText;
  private CustomSwitch switchAlarm;
  private TextView tvEditSmartAlarm;
  private TextView windowSelection;
  private LinearLayout wrapperAlarmData;
  
  private void attachElements(View paramView)
  {
    this.switchAlarm = ((CustomSwitch)paramView.findViewById(2131100540));
    this.wrapperAlarmData = ((LinearLayout)paramView.findViewById(2131100541));
    this.alertTime = ((TextView)paramView.findViewById(2131100539));
    this.tvEditSmartAlarm = ((TextView)paramView.findViewById(2131100538));
    this.alarmRepetition = ((GroteskLight)paramView.findViewById(2131100549));
    this.repeatSelection = ((RelativeLayout)paramView.findViewById(2131100546));
    this.soundSelection = ((RelativeLayout)paramView.findViewById(2131100550));
    this.seekBar = ((SeekBar)paramView.findViewById(2131100545));
    this.windowSelection = ((TextView)paramView.findViewById(2131100544));
    this.soundSelectionText = ((TextView)paramView.findViewById(2131100553));
  }
  
  private void createListener()
  {
    this.tvEditSmartAlarm.setOnClickListener(this);
    this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
    {
      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        paramAnonymousInt += 15;
        SmartAlarmFragment.this.windowSelection.setText(paramAnonymousInt + " mins");
        SmartAlarmFragment.this.updateTimeRealTimeLabel(paramAnonymousInt);
      }
      
      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {}
      
      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
        int i = Integer.valueOf(paramAnonymousSeekBar.getProgress()).intValue();
        SmartAlarmDataManager.getInstance().setWindowValue(Integer.valueOf(i + 15).intValue());
        SmartAlarmFragment.this.updateTimeLabel();
      }
    });
    this.switchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean)
        {
          Log.d("com.resmed.refresh.smartAlarm", "setOnCheckedChangeListener setActiveAlarmValue = " + SmartAlarmDataManager.getInstance().getAlarmDateTime());
          SmartAlarmDataManager.getInstance().setActiveAlarmValue(true);
          SmartAlarmFragment.this.wrapperAlarmData.setVisibility(0);
          SmartAlarmFragment.this.setAlarmBtn.setActiveAlarm(Boolean.valueOf(true));
          SmartAlarmFragment.this.updateTimeLabel();
          SmartAlarmFragment.this.tvEditSmartAlarm.setVisibility(0);
          SmartAlarmFragment.this.alertTime.setOnClickListener(SmartAlarmFragment.this);
        }
        for (;;)
        {
          return;
          SmartAlarmDataManager.getInstance().setActiveAlarmValue(false);
          SmartAlarmFragment.this.wrapperAlarmData.setVisibility(8);
          SmartAlarmFragment.this.setAlarmBtn.setActiveAlarm(Boolean.valueOf(false));
          SmartAlarmFragment.this.updateTimeLabel();
          SmartAlarmFragment.this.tvEditSmartAlarm.setVisibility(8);
          SmartAlarmFragment.this.alertTime.setOnClickListener(null);
        }
      }
    });
    this.repeatSelection.setOnClickListener(this);
    this.soundSelection.setOnClickListener(this);
  }
  
  private void customizeSeekBarValue()
  {
    this.seekBar.setThumb(new CustomSeekBarThumbDrawable(getActivity(), -1));
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.setAlarmBtn = ((SmartAlarmBtn)paramActivity);
      this.mcActiveFragmentInterface = ((ActiveFragmentInterface)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new ClassCastException(paramActivity.toString() + " ...you must implement SmartAlarmBtn from your Activity ;-) !");
    }
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    }
    for (;;)
    {
      return;
      this.setAlarmBtn.setTimeClick();
      continue;
      this.setAlarmBtn.setRepeatClick();
      continue;
      this.setAlarmBtn.setSoundClick();
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903174, paramViewGroup, false);
    attachElements(paramLayoutInflater);
    createListener();
    customizeSeekBarValue();
    return paramLayoutInflater;
  }
  
  public void onPause()
  {
    super.onPause();
    SmartAlarmObserver.getInstance().unRegister(this);
  }
  
  public void onResume()
  {
    super.onResume();
    this.switchAlarm.setChecked(SmartAlarmDataManager.getInstance().getActiveAlarm());
    if (SmartAlarmDataManager.getInstance().getActiveAlarm())
    {
      this.wrapperAlarmData.setVisibility(0);
      this.tvEditSmartAlarm.setVisibility(0);
    }
    for (;;)
    {
      int i = Integer.valueOf(SmartAlarmDataManager.getInstance().getWindowValue()).intValue();
      this.seekBar.setProgress(Integer.valueOf(i - 15).intValue());
      updateRepeatLabel();
      updateSoundLabel();
      updateTimeLabel();
      SmartAlarmObserver.getInstance().register(this);
      ActiveFragmentInterface localActiveFragmentInterface = this.mcActiveFragmentInterface;
      ((SmartAlarmActivity)getActivity()).getClass();
      localActiveFragmentInterface.notifyCurrentFragment(0);
      return;
      this.wrapperAlarmData.setVisibility(8);
      this.tvEditSmartAlarm.setVisibility(8);
    }
  }
  
  public void updateRepeatLabel()
  {
    switch (Integer.valueOf(SmartAlarmDataManager.getInstance().getRepeatValue()).intValue())
    {
    default: 
      this.alarmRepetition.setText(2131166070);
    }
    for (;;)
    {
      return;
      this.alarmRepetition.setText(2131166070);
      continue;
      this.alarmRepetition.setText(2131166071);
      continue;
      this.alarmRepetition.setText(2131166072);
    }
  }
  
  public void updateSmartAlarmActiveButton(boolean paramBoolean)
  {
    if (getActivity() != null) {
      getActivity().runOnUiThread(new Runnable()
      {
        public void run()
        {
          SmartAlarmFragment.this.switchAlarm.setChecked(false);
        }
      });
    }
  }
  
  public void updateSoundLabel()
  {
    this.soundSelectionText.setText(SmartAlarmDataManager.getInstance().getSoundResource().getName());
  }
  
  public void updateTimeLabel()
  {
    this.alertTime.setText(SmartAlarmDataManager.getInstance().getStringAlarmTime(SmartAlarmDataManager.getInstance().getWindowValue()));
  }
  
  public void updateTimeRealTimeLabel(int paramInt)
  {
    this.alertTime.setText(SmartAlarmDataManager.getInstance().getStringAlarmTime(paramInt));
  }
  
  public static abstract interface SmartAlarmBtn
  {
    public abstract void setActiveAlarm(Boolean paramBoolean);
    
    public abstract void setRepeatClick();
    
    public abstract void setSoundClick();
    
    public abstract void setTimeClick();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SmartAlarmFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
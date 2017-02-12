package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.resmed.refresh.ui.activity.SmartAlarmActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.ActiveFragmentInterface;
import com.resmed.refresh.ui.utils.SmartAlarmDataManager;
import java.util.Calendar;

public class SmartAlarmTimeFragment
  extends BaseFragment
{
  private Button cancelBtn;
  private Button doneBtn;
  private int hourOfDay;
  private ActiveFragmentInterface mcActiveFragmentInterface;
  private int minute;
  private SetTimeBtn setTimeBtn;
  private SmartAlarmDataManager smartAlarmDataManager;
  private TextView time;
  private TimePicker timePicker;
  private String timeText = "";
  
  private void attachElements(View paramView)
  {
    this.cancelBtn = ((Button)paramView.findViewById(2131100557));
    this.doneBtn = ((Button)paramView.findViewById(2131100558));
    this.timePicker = ((TimePicker)paramView.findViewById(2131100559));
    this.time = ((TextView)paramView.findViewById(2131100556));
  }
  
  private void createListener()
  {
    this.timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener()
    {
      public void onTimeChanged(TimePicker paramAnonymousTimePicker, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        SmartAlarmTimeFragment.this.updateTime(paramAnonymousInt1, paramAnonymousInt2);
      }
    });
    View.OnClickListener local2 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        switch (paramAnonymousView.getId())
        {
        }
        for (;;)
        {
          return;
          SmartAlarmTimeFragment.this.setTimeBtn.cancelClick();
          continue;
          SmartAlarmTimeFragment.this.smartAlarmDataManager.setAlarmTime(SmartAlarmTimeFragment.this.hourOfDay, SmartAlarmTimeFragment.this.minute);
          SmartAlarmTimeFragment.this.setTimeBtn.doneClick(SmartAlarmTimeFragment.this.hourOfDay, SmartAlarmTimeFragment.this.minute);
        }
      }
    };
    this.cancelBtn.setOnClickListener(local2);
    this.doneBtn.setOnClickListener(local2);
  }
  
  private void updateTime(int paramInt1, int paramInt2)
  {
    this.hourOfDay = paramInt1;
    this.minute = paramInt2;
    this.timeText = (String.format("%02d", new Object[] { Integer.valueOf(paramInt1) }) + ":" + String.format("%02d", new Object[] { Integer.valueOf(paramInt2) }));
    this.time.setText(this.timeText);
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.setTimeBtn = ((SetTimeBtn)paramActivity);
      this.mcActiveFragmentInterface = ((ActiveFragmentInterface)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new ClassCastException(paramActivity.toString() + " ...you must implement SmartAlarmBtn from your Activity ;-) !");
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903177, paramViewGroup, false);
    attachElements(paramLayoutInflater);
    createListener();
    return paramLayoutInflater;
  }
  
  public void onResume()
  {
    super.onResume();
    this.smartAlarmDataManager = SmartAlarmDataManager.getInstance();
    Object localObject = Calendar.getInstance();
    ((Calendar)localObject).setTimeInMillis(this.smartAlarmDataManager.getAlarmDateTime());
    this.time.setText(this.smartAlarmDataManager.getStringAlarmTime());
    this.timePicker.setIs24HourView(Boolean.valueOf(true));
    this.timePicker.setCurrentHour(Integer.valueOf(((Calendar)localObject).get(11)));
    this.timePicker.setCurrentMinute(Integer.valueOf(((Calendar)localObject).get(12)));
    this.timePicker.setDescendantFocusability(393216);
    localObject = this.mcActiveFragmentInterface;
    ((SmartAlarmActivity)getActivity()).getClass();
    ((ActiveFragmentInterface)localObject).notifyCurrentFragment(3);
  }
  
  public static abstract interface SetTimeBtn
  {
    public abstract void cancelClick();
    
    public abstract void doneClick(int paramInt1, int paramInt2);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SmartAlarmTimeFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
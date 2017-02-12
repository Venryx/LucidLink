package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.UserProfileDataManager;
import com.resmed.refresh.utils.DisplayManager;

public class SettingPickerFragment
  extends BaseFragment
  implements NumberPicker.OnValueChangeListener
{
  private TextView buttonPickerCancel;
  private TextView buttonPickerDone;
  private View fragmentView;
  private NumberPicker numberPicker;
  private LinearLayout pickerContainer;
  private UserProfileDataManager userProfileDataManager;
  private UserSettingsPickerButtons userSettingsPickerButtons;
  
  private void getHeightPicker()
  {
    this.numberPicker.setDisplayedValues(null);
    this.numberPicker.setMaxValue(1);
    this.numberPicker.setMinValue(0);
    this.numberPicker.setValue(((Integer)this.userProfileDataManager.getCurrentValue()).intValue());
    this.numberPicker.setDisplayedValues(UserProfileDataManager.heightUnitArray);
  }
  
  private void getTemperaturePicker()
  {
    this.numberPicker.setDisplayedValues(null);
    this.numberPicker.setMaxValue(1);
    this.numberPicker.setMinValue(0);
    this.numberPicker.setValue(((Integer)this.userProfileDataManager.getCurrentValue()).intValue());
    this.numberPicker.setDisplayedValues(UserProfileDataManager.temperatureUnitArray);
    this.numberPicker.setWrapSelectorWheel(false);
  }
  
  private void getWeightPicker()
  {
    this.numberPicker.setDisplayedValues(null);
    this.numberPicker.setMaxValue(2);
    this.numberPicker.setMinValue(0);
    this.numberPicker.setValue(((Integer)this.userProfileDataManager.getCurrentValue()).intValue());
    this.numberPicker.setDisplayedValues(UserProfileDataManager.weightUnitArray);
    this.numberPicker.setWrapSelectorWheel(false);
  }
  
  private void hideAllPicker()
  {
    this.numberPicker.setVisibility(8);
  }
  
  private void mapGUI(View paramView)
  {
    this.pickerContainer = ((LinearLayout)paramView.findViewById(2131100361));
    this.buttonPickerCancel = ((TextView)paramView.findViewById(2131100326));
    this.buttonPickerDone = ((TextView)paramView.findViewById(2131100327));
    this.numberPicker = ((NumberPicker)paramView.findViewById(2131100329));
    this.numberPicker.setOnValueChangedListener(this);
    this.numberPicker.setDescendantFocusability(393216);
  }
  
  private void setSize()
  {
    FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)this.pickerContainer.getLayoutParams();
    localLayoutParams.height = (DisplayManager.getScreenHeight(getActivity()).intValue() / 2);
    this.pickerContainer.setLayoutParams(localLayoutParams);
  }
  
  private void setupListeners()
  {
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
          SettingPickerFragment.this.userSettingsPickerButtons.closePicker();
          continue;
          SettingPickerFragment.this.userProfileDataManager.performDoneButton();
          SettingPickerFragment.this.userSettingsPickerButtons.closePicker();
        }
      }
    };
    this.buttonPickerCancel.setOnClickListener(local2);
    this.buttonPickerDone.setOnClickListener(local2);
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.userSettingsPickerButtons = ((UserSettingsPickerButtons)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new ClassCastException(paramActivity.toString() + " ...you must implement UserSettingsPickerButtons from your Activity ;-) !");
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.fragmentView = paramLayoutInflater.inflate(2130903162, paramViewGroup, false);
    this.userProfileDataManager = UserProfileDataManager.getInstance();
    mapGUI(this.fragmentView);
    setupListeners();
    setSize();
    this.fragmentView.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        return true;
      }
    });
    return this.fragmentView;
  }
  
  public void onResume()
  {
    super.onResume();
    refreshPicker();
  }
  
  public void onValueChange(NumberPicker paramNumberPicker, int paramInt1, int paramInt2)
  {
    this.userProfileDataManager.setTempValue(Integer.valueOf(paramInt2));
  }
  
  public void refreshPicker()
  {
    hideAllPicker();
    switch (this.userProfileDataManager.getCurrentPicker().intValue())
    {
    }
    for (;;)
    {
      return;
      this.numberPicker.setVisibility(0);
      getWeightPicker();
      continue;
      this.numberPicker.setVisibility(0);
      getHeightPicker();
      continue;
      this.numberPicker.setVisibility(0);
      getTemperaturePicker();
    }
  }
  
  public static abstract interface UserSettingsPickerButtons
  {
    public abstract void closePicker();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SettingPickerFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
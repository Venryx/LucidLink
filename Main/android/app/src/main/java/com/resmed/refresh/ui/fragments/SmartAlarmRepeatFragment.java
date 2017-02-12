package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.resmed.refresh.ui.activity.SmartAlarmActivity;
import com.resmed.refresh.ui.customview.CustomRadioGroup;
import com.resmed.refresh.ui.customview.CustomRadioGroup.Icon;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.ActiveFragmentInterface;
import com.resmed.refresh.ui.utils.RadioButtonHandler;
import com.resmed.refresh.ui.utils.SmartAlarmDataManager;
import java.util.ArrayList;
import java.util.List;

public class SmartAlarmRepeatFragment
  extends BaseFragment
  implements RadioButtonHandler
{
  private ActiveFragmentInterface mcActiveFragmentInterface;
  private List<CustomRadioGroup> radioGroupList;
  private SmartAlarmDataManager smartAlarmDataManager;
  private LinearLayout wrapperRadioButton;
  
  private void addRadioButton()
  {
    this.radioGroupList.add(new CustomRadioGroup(getActivity()).setLabel(getResources().getString(2131166070)).setIcon(CustomRadioGroup.Icon.ALARM_ICON.getIconResource()).setRadioId(2131099657).setHandler(this));
    this.radioGroupList.add(new CustomRadioGroup(getActivity()).setLabel(getResources().getString(2131166071)).setIcon(CustomRadioGroup.Icon.ALARM_ICON.getIconResource()).setRadioId(2131099658).setHandler(this));
    this.radioGroupList.add(new CustomRadioGroup(getActivity()).setLabel(getResources().getString(2131166072)).setIcon(CustomRadioGroup.Icon.ALARM_ICON.getIconResource()).setRadioId(2131099659).setHandler(this));
    for (int i = 0;; i++)
    {
      if (i >= this.radioGroupList.size()) {
        return;
      }
      this.wrapperRadioButton.addView((View)this.radioGroupList.get(i));
    }
  }
  
  private void attachElements(View paramView)
  {
    this.wrapperRadioButton = ((LinearLayout)paramView.findViewById(2131100554));
  }
  
  private void resetRadioButton()
  {
    for (int i = 0;; i++)
    {
      if (i >= this.radioGroupList.size()) {
        return;
      }
      ((CustomRadioGroup)this.radioGroupList.get(i)).setRadioButton(CustomRadioGroup.Icon.RADIO_BUTTON.getIconResource());
    }
  }
  
  private void setRadioChecked(int paramInt)
  {
    switch (paramInt)
    {
    }
    for (;;)
    {
      return;
      this.smartAlarmDataManager.setRepeatValue(2131099657);
      ((CustomRadioGroup)this.radioGroupList.get(0)).setRadioButton(CustomRadioGroup.Icon.RADIO_BUTTON_CHECKED.getIconResource());
      continue;
      this.smartAlarmDataManager.setRepeatValue(2131099658);
      ((CustomRadioGroup)this.radioGroupList.get(1)).setRadioButton(CustomRadioGroup.Icon.RADIO_BUTTON_CHECKED.getIconResource());
      continue;
      this.smartAlarmDataManager.setRepeatValue(2131099659);
      ((CustomRadioGroup)this.radioGroupList.get(2)).setRadioButton(CustomRadioGroup.Icon.RADIO_BUTTON_CHECKED.getIconResource());
    }
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
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
    paramLayoutInflater = paramLayoutInflater.inflate(2130903175, paramViewGroup, false);
    this.radioGroupList = new ArrayList();
    attachElements(paramLayoutInflater);
    addRadioButton();
    return paramLayoutInflater;
  }
  
  public void onRadioClick(int paramInt)
  {
    resetRadioButton();
    setRadioChecked(paramInt);
  }
  
  public void onResume()
  {
    super.onResume();
    this.smartAlarmDataManager = SmartAlarmDataManager.getInstance();
    Integer localInteger = Integer.valueOf(this.smartAlarmDataManager.getRepeatValue());
    if (localInteger.intValue() != -1)
    {
      localObject = localInteger;
      if (localInteger.intValue() != 0) {}
    }
    else
    {
      localObject = Integer.valueOf(2131099657);
    }
    setRadioChecked(((Integer)localObject).intValue());
    Object localObject = this.mcActiveFragmentInterface;
    ((SmartAlarmActivity)getActivity()).getClass();
    ((ActiveFragmentInterface)localObject).notifyCurrentFragment(1);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SmartAlarmRepeatFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
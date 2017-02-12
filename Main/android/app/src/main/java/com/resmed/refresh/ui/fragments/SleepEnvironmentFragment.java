package com.resmed.refresh.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_User;
import com.resmed.refresh.model.RST_UserProfile;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.customview.CustomSeekBar;
import com.resmed.refresh.ui.customview.CustomSemiSliderSeekBar;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.utils.RefreshTools;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class SleepEnvironmentFragment
  extends BaseFragment
  implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener, RST_CallbackItem<RST_Response<RST_UserProfile>>
{
  private CheckBox cbSetupSleepEnvironmentQ30A1;
  private CheckBox cbSetupSleepEnvironmentQ30A2;
  private CheckBox cbSetupSleepEnvironmentQ30A3;
  private CheckBox cbSetupSleepEnvironmentQ30A4;
  private CheckBox cbSetupSleepEnvironmentQ30A5;
  private CheckBox cbSetupSleepEnvironmentQ30A6;
  private CheckBox cbSetupSleepEnvironmentQ35A1;
  private CheckBox cbSetupSleepEnvironmentQ35A10;
  private CheckBox cbSetupSleepEnvironmentQ35A11;
  private CheckBox cbSetupSleepEnvironmentQ35A12;
  private CheckBox cbSetupSleepEnvironmentQ35A2;
  private CheckBox cbSetupSleepEnvironmentQ35A3;
  private CheckBox cbSetupSleepEnvironmentQ35A4;
  private CheckBox cbSetupSleepEnvironmentQ35A5;
  private CheckBox cbSetupSleepEnvironmentQ35A6;
  private CheckBox cbSetupSleepEnvironmentQ35A7;
  private CheckBox cbSetupSleepEnvironmentQ35A8;
  private CheckBox cbSetupSleepEnvironmentQ35A9;
  private CheckBox cbSetupSleepEnvironmentQ36aA1;
  private CheckBox cbSetupSleepEnvironmentQ36aA2;
  private CheckBox cbSetupSleepEnvironmentQ36aA3;
  private CheckBox cbSetupSleepEnvironmentQ36aA4;
  private CheckBox cbSetupSleepEnvironmentQ36aA5;
  private CheckBox cbSetupSleepEnvironmentQ37aA1;
  private CheckBox cbSetupSleepEnvironmentQ37aA2;
  private CheckBox cbSetupSleepEnvironmentQ37aA3;
  private CheckBox cbSetupSleepEnvironmentQ37aA4;
  private CheckBox cbSetupSleepEnvironmentQ38A1;
  private CheckBox cbSetupSleepEnvironmentQ38A2;
  private CheckBox cbSetupSleepEnvironmentQ38A3;
  private CheckBox cbSetupSleepEnvironmentQ38A4;
  private CheckBox cbSetupSleepEnvironmentQ38A5;
  private CheckBox cbSetupSleepEnvironmentQ38A6;
  private CheckBox cbSetupSleepEnvironmentQ39A1;
  private CheckBox cbSetupSleepEnvironmentQ39A2;
  private CheckBox cbSetupSleepEnvironmentQ39A3;
  private CustomSeekBar csbSetupSleepEnvironmentNap_a37;
  private CustomSeekBar csbSetupSleepEnvironment_a33;
  private CustomSeekBar csbSetupSleepEnvironment_a34;
  private CustomSemiSliderSeekBar csssbSetupSleepEnvironmentQ31;
  private CustomSemiSliderSeekBar csssbSetupSleepEnvironmentQ32;
  private CustomSemiSliderSeekBar csssbSetupSleepEnvironmentQ36;
  private boolean disableCheckBoxSelFlg;
  private EditText edittextSetupSleepEnvironmentQ30A5;
  private EditText edittextSetupSleepEnvironmentQ36aA5;
  private EditText edittextSetupSleepEnvironmentQ37aA4;
  private EditText edittextSetupSleepEnvironmentQ38A5;
  private LinearLayout llSetupSleepEnvBack;
  private LinearLayout llSetupSleepEnvNext;
  private LinearLayout llSetupSleepEnviroment_q37a;
  private LinearLayout llSetupSleepEnvironment_q36a;
  public ISleepEnvironmentUpdateProgress updateSleepEnvironmentProg;
  
  private void captureOtherControlBedroomTemperature(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.edittextSetupSleepEnvironmentQ30A5.setVisibility(0);
      this.edittextSetupSleepEnvironmentQ30A5.setText(RefreshModelController.getInstance().getUser().getProfile().getOtherControlBedroomTemperature());
    }
    for (;;)
    {
      return;
      this.edittextSetupSleepEnvironmentQ30A5.setVisibility(8);
      this.edittextSetupSleepEnvironmentQ30A5.setText("");
    }
  }
  
  private void captureOtherReduceAmountOfLightInBedroom(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.edittextSetupSleepEnvironmentQ38A5.setVisibility(0);
      this.edittextSetupSleepEnvironmentQ38A5.setText(RefreshModelController.getInstance().getUser().getProfile().getOtherReduceAmountOfLightInBedroom());
    }
    for (;;)
    {
      return;
      this.edittextSetupSleepEnvironmentQ38A5.setVisibility(8);
      this.edittextSetupSleepEnvironmentQ38A5.setText("");
    }
  }
  
  private void captureOtherSourceOfLightInBedroom(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.edittextSetupSleepEnvironmentQ37aA4.setVisibility(0);
      this.edittextSetupSleepEnvironmentQ37aA4.setText(RefreshModelController.getInstance().getUser().getProfile().getOtherSourceOfLightInBedroom());
    }
    for (;;)
    {
      return;
      this.edittextSetupSleepEnvironmentQ37aA4.setVisibility(8);
      this.edittextSetupSleepEnvironmentQ37aA4.setText("");
    }
  }
  
  private void captureOtherTypeOfSoundUsedInSleep(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.edittextSetupSleepEnvironmentQ36aA5.setVisibility(0);
      this.edittextSetupSleepEnvironmentQ36aA5.setText(RefreshModelController.getInstance().getUser().getProfile().getOtherTypeOfSoundUsedInSleep());
    }
    for (;;)
    {
      return;
      this.edittextSetupSleepEnvironmentQ36aA5.setVisibility(8);
      this.edittextSetupSleepEnvironmentQ36aA5.setText("");
    }
  }
  
  private void getSpecialLightUsedInSleepCheckBoxEnability(int paramInt)
  {
    switch (paramInt)
    {
    }
    for (;;)
    {
      return;
      if (this.cbSetupSleepEnvironmentQ39A3.isChecked())
      {
        this.disableCheckBoxSelFlg = true;
        this.cbSetupSleepEnvironmentQ39A1.setChecked(false);
        this.cbSetupSleepEnvironmentQ39A2.setChecked(false);
        this.disableCheckBoxSelFlg = false;
      }
      else if ((this.cbSetupSleepEnvironmentQ39A1.isChecked()) || (this.cbSetupSleepEnvironmentQ39A2.isChecked()))
      {
        this.cbSetupSleepEnvironmentQ39A3.setChecked(false);
        continue;
        if ((!this.disableCheckBoxSelFlg) && ((this.cbSetupSleepEnvironmentQ39A1.isChecked()) || (this.cbSetupSleepEnvironmentQ39A2.isChecked())))
        {
          this.cbSetupSleepEnvironmentQ39A3.setChecked(false);
          continue;
          if (this.cbSetupSleepEnvironmentQ39A3.isChecked())
          {
            this.disableCheckBoxSelFlg = true;
            this.cbSetupSleepEnvironmentQ39A1.setChecked(false);
            this.cbSetupSleepEnvironmentQ39A2.setChecked(false);
            this.disableCheckBoxSelFlg = false;
          }
        }
      }
    }
  }
  
  private void hideKeyboard()
  {
    try
    {
      InputMethodManager localInputMethodManager = (InputMethodManager)getActivity().getSystemService("input_method");
      if (getActivity().getCurrentFocus().getWindowToken() != null) {
        localInputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 2);
      }
      return;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        Log.e("KeyBoardUtil", localException.toString(), localException);
      }
    }
  }
  
  private void loadGUI(View paramView)
  {
    this.llSetupSleepEnvBack = ((LinearLayout)paramView.findViewById(2131100536));
    this.llSetupSleepEnvNext = ((LinearLayout)paramView.findViewById(2131100537));
    this.cbSetupSleepEnvironmentQ30A1 = ((CheckBox)paramView.findViewById(2131100484));
    this.cbSetupSleepEnvironmentQ30A2 = ((CheckBox)paramView.findViewById(2131100485));
    this.cbSetupSleepEnvironmentQ30A3 = ((CheckBox)paramView.findViewById(2131100486));
    this.cbSetupSleepEnvironmentQ30A4 = ((CheckBox)paramView.findViewById(2131100487));
    this.cbSetupSleepEnvironmentQ30A5 = ((CheckBox)paramView.findViewById(2131100488));
    this.cbSetupSleepEnvironmentQ30A6 = ((CheckBox)paramView.findViewById(2131100489));
    this.edittextSetupSleepEnvironmentQ30A5 = ((EditText)paramView.findViewById(2131100490));
    this.edittextSetupSleepEnvironmentQ30A5.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        paramAnonymousView.setFocusable(true);
        paramAnonymousView.setFocusableInTouchMode(true);
        return false;
      }
    });
    this.csssbSetupSleepEnvironmentQ31 = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100491));
    this.csssbSetupSleepEnvironmentQ31.setProgressLineColor(-7829368);
    this.csssbSetupSleepEnvironmentQ31.setBackgroundLineColor(-7829368);
    this.csssbSetupSleepEnvironmentQ32 = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100492));
    this.csssbSetupSleepEnvironmentQ32.setProgressLineColor(-7829368);
    this.csssbSetupSleepEnvironmentQ32.setBackgroundLineColor(-7829368);
    this.csbSetupSleepEnvironment_a33 = ((CustomSeekBar)paramView.findViewById(2131100493));
    Object localObject = new ArrayList();
    ((List)localObject).add(getString(2131165781));
    ((List)localObject).add(getString(2131165782));
    ((List)localObject).add(getString(2131165783));
    this.csbSetupSleepEnvironment_a33.setAdapter((List)localObject);
    this.csbSetupSleepEnvironment_a34 = ((CustomSeekBar)paramView.findViewById(2131100494));
    localObject = new ArrayList();
    ((List)localObject).add(getString(2131165785));
    ((List)localObject).add(getString(2131165786));
    ((List)localObject).add(getString(2131165787));
    ((List)localObject).add(getString(2131165788));
    this.csbSetupSleepEnvironment_a34.setAdapter((List)localObject);
    this.cbSetupSleepEnvironmentQ35A1 = ((CheckBox)paramView.findViewById(2131100496));
    this.cbSetupSleepEnvironmentQ35A2 = ((CheckBox)paramView.findViewById(2131100497));
    this.cbSetupSleepEnvironmentQ35A3 = ((CheckBox)paramView.findViewById(2131100498));
    this.cbSetupSleepEnvironmentQ35A4 = ((CheckBox)paramView.findViewById(2131100499));
    this.cbSetupSleepEnvironmentQ35A5 = ((CheckBox)paramView.findViewById(2131100500));
    this.cbSetupSleepEnvironmentQ35A6 = ((CheckBox)paramView.findViewById(2131100501));
    this.cbSetupSleepEnvironmentQ35A7 = ((CheckBox)paramView.findViewById(2131100502));
    this.cbSetupSleepEnvironmentQ35A8 = ((CheckBox)paramView.findViewById(2131100503));
    this.cbSetupSleepEnvironmentQ35A9 = ((CheckBox)paramView.findViewById(2131100504));
    this.cbSetupSleepEnvironmentQ35A10 = ((CheckBox)paramView.findViewById(2131100505));
    this.cbSetupSleepEnvironmentQ35A11 = ((CheckBox)paramView.findViewById(2131100506));
    this.cbSetupSleepEnvironmentQ35A12 = ((CheckBox)paramView.findViewById(2131100507));
    this.csssbSetupSleepEnvironmentQ36 = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100508));
    this.csssbSetupSleepEnvironmentQ36.setProgressLineColor(-7829368);
    this.csssbSetupSleepEnvironmentQ36.setBackgroundLineColor(-7829368);
    this.llSetupSleepEnvironment_q36a = ((LinearLayout)paramView.findViewById(2131100509));
    this.cbSetupSleepEnvironmentQ36aA1 = ((CheckBox)paramView.findViewById(2131100510));
    this.cbSetupSleepEnvironmentQ36aA2 = ((CheckBox)paramView.findViewById(2131100511));
    this.cbSetupSleepEnvironmentQ36aA3 = ((CheckBox)paramView.findViewById(2131100512));
    this.cbSetupSleepEnvironmentQ36aA4 = ((CheckBox)paramView.findViewById(2131100513));
    this.cbSetupSleepEnvironmentQ36aA5 = ((CheckBox)paramView.findViewById(2131100514));
    this.edittextSetupSleepEnvironmentQ36aA5 = ((EditText)paramView.findViewById(2131100515));
    this.edittextSetupSleepEnvironmentQ36aA5.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        paramAnonymousView.setFocusable(true);
        paramAnonymousView.setFocusableInTouchMode(true);
        return false;
      }
    });
    this.csbSetupSleepEnvironmentNap_a37 = ((CustomSeekBar)paramView.findViewById(2131100516));
    localObject = new ArrayList();
    ((List)localObject).add(getString(2131165812));
    ((List)localObject).add(getString(2131165813));
    ((List)localObject).add(getString(2131165814));
    this.csbSetupSleepEnvironmentNap_a37.setAdapter((List)localObject);
    this.llSetupSleepEnviroment_q37a = ((LinearLayout)paramView.findViewById(2131100517));
    this.cbSetupSleepEnvironmentQ37aA1 = ((CheckBox)paramView.findViewById(2131100519));
    this.cbSetupSleepEnvironmentQ37aA2 = ((CheckBox)paramView.findViewById(2131100520));
    this.cbSetupSleepEnvironmentQ37aA3 = ((CheckBox)paramView.findViewById(2131100521));
    this.cbSetupSleepEnvironmentQ37aA4 = ((CheckBox)paramView.findViewById(2131100522));
    this.edittextSetupSleepEnvironmentQ37aA4 = ((EditText)paramView.findViewById(2131100523));
    this.edittextSetupSleepEnvironmentQ37aA4.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        paramAnonymousView.setFocusable(true);
        paramAnonymousView.setFocusableInTouchMode(true);
        return false;
      }
    });
    this.cbSetupSleepEnvironmentQ38A1 = ((CheckBox)paramView.findViewById(2131100525));
    this.cbSetupSleepEnvironmentQ38A2 = ((CheckBox)paramView.findViewById(2131100526));
    this.cbSetupSleepEnvironmentQ38A3 = ((CheckBox)paramView.findViewById(2131100527));
    this.cbSetupSleepEnvironmentQ38A4 = ((CheckBox)paramView.findViewById(2131100528));
    this.cbSetupSleepEnvironmentQ38A5 = ((CheckBox)paramView.findViewById(2131100529));
    this.cbSetupSleepEnvironmentQ38A6 = ((CheckBox)paramView.findViewById(2131100530));
    this.edittextSetupSleepEnvironmentQ38A5 = ((EditText)paramView.findViewById(2131100531));
    this.edittextSetupSleepEnvironmentQ38A5.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        paramAnonymousView.setFocusable(true);
        paramAnonymousView.setFocusableInTouchMode(true);
        return false;
      }
    });
    this.cbSetupSleepEnvironmentQ39A1 = ((CheckBox)paramView.findViewById(2131100533));
    this.cbSetupSleepEnvironmentQ39A2 = ((CheckBox)paramView.findViewById(2131100534));
    this.cbSetupSleepEnvironmentQ39A3 = ((CheckBox)paramView.findViewById(2131100535));
    paramView = RefreshModelController.getInstance().getUser();
    localObject = RefreshTools.bitmaskFromInt(paramView.getProfile().getControlBedroomTemperature());
    this.cbSetupSleepEnvironmentQ30A1.setChecked(((BitSet)localObject).get(0));
    this.cbSetupSleepEnvironmentQ30A2.setChecked(((BitSet)localObject).get(1));
    this.cbSetupSleepEnvironmentQ30A3.setChecked(((BitSet)localObject).get(2));
    this.cbSetupSleepEnvironmentQ30A4.setChecked(((BitSet)localObject).get(3));
    this.cbSetupSleepEnvironmentQ30A5.setChecked(((BitSet)localObject).get(4));
    this.cbSetupSleepEnvironmentQ30A6.setChecked(((BitSet)localObject).get(5));
    captureOtherControlBedroomTemperature(this.cbSetupSleepEnvironmentQ30A5.isChecked());
    setControlBedroomTempCheckBoxEnability(-1);
    setCustomSeekBarSemiSliderAdapter(paramView.getProfile().isHumidifierUsed(), this.csssbSetupSleepEnvironmentQ31);
    setCustomSeekBarSemiSliderAdapter(paramView.getProfile().isAirPurifierUsed(), this.csssbSetupSleepEnvironmentQ32);
    this.csbSetupSleepEnvironment_a33.setSelection(paramView.getProfile().getNoiseLevelInBedroom());
    this.csbSetupSleepEnvironment_a34.setSelection(paramView.getProfile().getHowOftenElectronicsUsed());
    localObject = RefreshTools.bitmaskFromInt(paramView.getProfile().getGetWokenUpInNightByAny());
    this.cbSetupSleepEnvironmentQ35A1.setChecked(((BitSet)localObject).get(0));
    this.cbSetupSleepEnvironmentQ35A2.setChecked(((BitSet)localObject).get(1));
    this.cbSetupSleepEnvironmentQ35A3.setChecked(((BitSet)localObject).get(2));
    this.cbSetupSleepEnvironmentQ35A4.setChecked(((BitSet)localObject).get(3));
    this.cbSetupSleepEnvironmentQ35A5.setChecked(((BitSet)localObject).get(4));
    this.cbSetupSleepEnvironmentQ35A6.setChecked(((BitSet)localObject).get(5));
    this.cbSetupSleepEnvironmentQ35A7.setChecked(((BitSet)localObject).get(6));
    this.cbSetupSleepEnvironmentQ35A8.setChecked(((BitSet)localObject).get(7));
    this.cbSetupSleepEnvironmentQ35A9.setChecked(((BitSet)localObject).get(8));
    this.cbSetupSleepEnvironmentQ35A10.setChecked(((BitSet)localObject).get(9));
    this.cbSetupSleepEnvironmentQ35A11.setChecked(((BitSet)localObject).get(10));
    this.cbSetupSleepEnvironmentQ35A12.setChecked(((BitSet)localObject).get(11));
    setGetWokenUpInNightByAnyCheckBoxEnability(-1);
    setCustomSeekBarSemiSliderAdapter(paramView.getProfile().isSoundUsedInSleep(), this.csssbSetupSleepEnvironmentQ36);
    showTypeOfSoundLayout();
    localObject = RefreshTools.bitmaskFromInt(paramView.getProfile().getTypeOfSoundUsedInSleep());
    this.cbSetupSleepEnvironmentQ36aA1.setChecked(((BitSet)localObject).get(0));
    this.cbSetupSleepEnvironmentQ36aA2.setChecked(((BitSet)localObject).get(1));
    this.cbSetupSleepEnvironmentQ36aA3.setChecked(((BitSet)localObject).get(2));
    this.cbSetupSleepEnvironmentQ36aA4.setChecked(((BitSet)localObject).get(3));
    this.cbSetupSleepEnvironmentQ36aA5.setChecked(((BitSet)localObject).get(4));
    captureOtherTypeOfSoundUsedInSleep(this.cbSetupSleepEnvironmentQ36aA5.isChecked());
    this.csbSetupSleepEnvironmentNap_a37.setSelection(paramView.getProfile().getLevelOfLightInBedroom());
    showSourceLightLayout();
    localObject = RefreshTools.bitmaskFromInt(paramView.getProfile().getSourceOfLightInBedroom());
    this.cbSetupSleepEnvironmentQ37aA1.setChecked(((BitSet)localObject).get(0));
    this.cbSetupSleepEnvironmentQ37aA2.setChecked(((BitSet)localObject).get(1));
    this.cbSetupSleepEnvironmentQ37aA3.setChecked(((BitSet)localObject).get(2));
    this.cbSetupSleepEnvironmentQ37aA4.setChecked(((BitSet)localObject).get(3));
    captureOtherSourceOfLightInBedroom(this.cbSetupSleepEnvironmentQ37aA4.isChecked());
    localObject = RefreshTools.bitmaskFromInt(paramView.getProfile().getReduceAmountOfLightInBedroom());
    this.cbSetupSleepEnvironmentQ38A1.setChecked(((BitSet)localObject).get(0));
    this.cbSetupSleepEnvironmentQ38A2.setChecked(((BitSet)localObject).get(1));
    this.cbSetupSleepEnvironmentQ38A3.setChecked(((BitSet)localObject).get(2));
    this.cbSetupSleepEnvironmentQ38A4.setChecked(((BitSet)localObject).get(3));
    this.cbSetupSleepEnvironmentQ38A5.setChecked(((BitSet)localObject).get(4));
    this.cbSetupSleepEnvironmentQ38A6.setChecked(((BitSet)localObject).get(5));
    captureOtherReduceAmountOfLightInBedroom(this.cbSetupSleepEnvironmentQ38A5.isChecked());
    setReduceAmtOfLightInBedCheckBoxEnability(-1);
    paramView = RefreshTools.bitmaskFromInt(paramView.getProfile().getSpecialLightUsedInSleep());
    this.cbSetupSleepEnvironmentQ39A1.setChecked(paramView.get(0));
    this.cbSetupSleepEnvironmentQ39A2.setChecked(paramView.get(1));
    this.cbSetupSleepEnvironmentQ39A3.setChecked(paramView.get(2));
    getSpecialLightUsedInSleepCheckBoxEnability(-1);
    this.cbSetupSleepEnvironmentQ30A1.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ30A2.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ30A3.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ30A4.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ30A5.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ30A6.setOnCheckedChangeListener(this);
    this.csssbSetupSleepEnvironmentQ31.setOnSeekBarChangeListener(this);
    this.csssbSetupSleepEnvironmentQ32.setOnSeekBarChangeListener(this);
    this.csbSetupSleepEnvironment_a33.setOnSeekBarChangeListener(this);
    this.csbSetupSleepEnvironment_a34.setOnSeekBarChangeListener(this);
    this.cbSetupSleepEnvironmentQ35A1.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ35A2.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ35A3.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ35A4.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ35A5.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ35A6.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ35A7.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ35A8.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ35A9.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ35A10.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ35A11.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ35A12.setOnCheckedChangeListener(this);
    this.csssbSetupSleepEnvironmentQ36.setOnSeekBarChangeListener(this);
    this.cbSetupSleepEnvironmentQ36aA1.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ36aA2.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ36aA3.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ36aA4.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ36aA5.setOnCheckedChangeListener(this);
    this.csbSetupSleepEnvironmentNap_a37.setOnSeekBarChangeListener(this);
    this.cbSetupSleepEnvironmentQ37aA1.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ37aA2.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ37aA3.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ37aA4.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ38A1.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ38A2.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ38A3.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ38A4.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ38A5.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ38A6.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ39A1.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ39A2.setOnCheckedChangeListener(this);
    this.cbSetupSleepEnvironmentQ39A3.setOnCheckedChangeListener(this);
    this.llSetupSleepEnvNext.setOnClickListener(this);
    this.llSetupSleepEnvBack.setOnClickListener(this);
  }
  
  private void removeEditTextFocus()
  {
    this.edittextSetupSleepEnvironmentQ30A5.setFocusable(false);
    this.edittextSetupSleepEnvironmentQ30A5.setFocusableInTouchMode(false);
    this.edittextSetupSleepEnvironmentQ36aA5.setFocusable(false);
    this.edittextSetupSleepEnvironmentQ36aA5.setFocusableInTouchMode(false);
    this.edittextSetupSleepEnvironmentQ37aA4.setFocusable(false);
    this.edittextSetupSleepEnvironmentQ37aA4.setFocusableInTouchMode(false);
    this.edittextSetupSleepEnvironmentQ38A5.setFocusable(false);
    this.edittextSetupSleepEnvironmentQ38A5.setFocusableInTouchMode(false);
  }
  
  private void saveDataInDatabase()
  {
    RST_User localRST_User = RefreshModelController.getInstance().getUser();
    localRST_User.getProfile().setOtherControlBedroomTemperature(this.edittextSetupSleepEnvironmentQ30A5.getText().toString().trim());
    localRST_User.getProfile().setOtherTypeOfSoundUsedInSleep(this.edittextSetupSleepEnvironmentQ36aA5.getText().toString().trim());
    localRST_User.getProfile().setOtherSourceOfLightInBedroom(this.edittextSetupSleepEnvironmentQ37aA4.getText().toString().trim());
    localRST_User.getProfile().setOtherReduceAmountOfLightInBedroom(this.edittextSetupSleepEnvironmentQ38A5.getText().toString().trim());
    RefreshModelController.getInstance().save();
  }
  
  private void setControlBedroomTempCheckBoxEnability(int paramInt)
  {
    switch (paramInt)
    {
    }
    for (;;)
    {
      return;
      if (this.cbSetupSleepEnvironmentQ30A6.isChecked())
      {
        this.disableCheckBoxSelFlg = true;
        this.cbSetupSleepEnvironmentQ30A1.setChecked(false);
        this.cbSetupSleepEnvironmentQ30A2.setChecked(false);
        this.cbSetupSleepEnvironmentQ30A3.setChecked(false);
        this.cbSetupSleepEnvironmentQ30A4.setChecked(false);
        this.cbSetupSleepEnvironmentQ30A5.setChecked(false);
        this.disableCheckBoxSelFlg = false;
      }
      else if ((this.cbSetupSleepEnvironmentQ30A1.isChecked()) || (this.cbSetupSleepEnvironmentQ30A2.isChecked()) || (this.cbSetupSleepEnvironmentQ30A3.isChecked()) || (this.cbSetupSleepEnvironmentQ30A4.isChecked()) || (this.cbSetupSleepEnvironmentQ30A5.isChecked()))
      {
        this.cbSetupSleepEnvironmentQ30A6.setChecked(false);
        continue;
        if ((!this.disableCheckBoxSelFlg) && ((this.cbSetupSleepEnvironmentQ30A1.isChecked()) || (this.cbSetupSleepEnvironmentQ30A2.isChecked()) || (this.cbSetupSleepEnvironmentQ30A3.isChecked()) || (this.cbSetupSleepEnvironmentQ30A4.isChecked()) || (this.cbSetupSleepEnvironmentQ30A5.isChecked())))
        {
          this.cbSetupSleepEnvironmentQ30A6.setChecked(false);
          continue;
          if (this.cbSetupSleepEnvironmentQ30A6.isChecked())
          {
            this.disableCheckBoxSelFlg = true;
            this.cbSetupSleepEnvironmentQ30A1.setChecked(false);
            this.cbSetupSleepEnvironmentQ30A2.setChecked(false);
            this.cbSetupSleepEnvironmentQ30A3.setChecked(false);
            this.cbSetupSleepEnvironmentQ30A4.setChecked(false);
            this.cbSetupSleepEnvironmentQ30A5.setChecked(false);
            this.disableCheckBoxSelFlg = false;
          }
        }
      }
    }
  }
  
  private void setCustomSeekBarSemiSliderAdapter(int paramInt, View paramView)
  {
    ArrayList localArrayList = new ArrayList();
    if (paramInt == -1)
    {
      localArrayList.add(getResources().getString(2131165299));
      localArrayList.add("");
      localArrayList.add(getResources().getString(2131165298));
      paramInt = 1;
      switch (paramView.getId())
      {
      }
    }
    for (;;)
    {
      return;
      localArrayList.add(getResources().getString(2131165299));
      localArrayList.add(getResources().getString(2131165298));
      break;
      this.csssbSetupSleepEnvironmentQ31.setAdapter(localArrayList);
      this.csssbSetupSleepEnvironmentQ31.setSelection(paramInt);
      continue;
      this.csssbSetupSleepEnvironmentQ32.setAdapter(localArrayList);
      this.csssbSetupSleepEnvironmentQ32.setSelection(paramInt);
      continue;
      this.csssbSetupSleepEnvironmentQ36.setAdapter(localArrayList);
      this.csssbSetupSleepEnvironmentQ36.setSelection(paramInt);
    }
  }
  
  private void setGetWokenUpInNightByAnyCheckBoxEnability(int paramInt)
  {
    switch (paramInt)
    {
    }
    for (;;)
    {
      return;
      if (this.cbSetupSleepEnvironmentQ35A12.isChecked())
      {
        this.disableCheckBoxSelFlg = true;
        this.cbSetupSleepEnvironmentQ35A1.setChecked(false);
        this.cbSetupSleepEnvironmentQ35A2.setChecked(false);
        this.cbSetupSleepEnvironmentQ35A3.setChecked(false);
        this.cbSetupSleepEnvironmentQ35A4.setChecked(false);
        this.cbSetupSleepEnvironmentQ35A5.setChecked(false);
        this.cbSetupSleepEnvironmentQ35A6.setChecked(false);
        this.cbSetupSleepEnvironmentQ35A7.setChecked(false);
        this.cbSetupSleepEnvironmentQ35A8.setChecked(false);
        this.cbSetupSleepEnvironmentQ35A9.setChecked(false);
        this.cbSetupSleepEnvironmentQ35A10.setChecked(false);
        this.cbSetupSleepEnvironmentQ35A11.setChecked(false);
        this.disableCheckBoxSelFlg = false;
      }
      else if ((this.cbSetupSleepEnvironmentQ35A1.isChecked()) || (this.cbSetupSleepEnvironmentQ35A2.isChecked()) || (this.cbSetupSleepEnvironmentQ35A3.isChecked()) || (this.cbSetupSleepEnvironmentQ35A4.isChecked()) || (this.cbSetupSleepEnvironmentQ35A5.isChecked()) || (this.cbSetupSleepEnvironmentQ35A6.isChecked()) || (this.cbSetupSleepEnvironmentQ35A7.isChecked()) || (this.cbSetupSleepEnvironmentQ35A8.isChecked()) || (this.cbSetupSleepEnvironmentQ35A9.isChecked()) || (this.cbSetupSleepEnvironmentQ35A10.isChecked()) || (this.cbSetupSleepEnvironmentQ35A11.isChecked()))
      {
        this.cbSetupSleepEnvironmentQ35A12.setChecked(false);
        continue;
        if ((!this.disableCheckBoxSelFlg) && ((this.cbSetupSleepEnvironmentQ35A1.isChecked()) || (this.cbSetupSleepEnvironmentQ35A2.isChecked()) || (this.cbSetupSleepEnvironmentQ35A3.isChecked()) || (this.cbSetupSleepEnvironmentQ35A4.isChecked()) || (this.cbSetupSleepEnvironmentQ35A5.isChecked()) || (this.cbSetupSleepEnvironmentQ35A6.isChecked()) || (this.cbSetupSleepEnvironmentQ35A7.isChecked()) || (this.cbSetupSleepEnvironmentQ35A8.isChecked()) || (this.cbSetupSleepEnvironmentQ35A9.isChecked()) || (this.cbSetupSleepEnvironmentQ35A10.isChecked()) || (this.cbSetupSleepEnvironmentQ35A11.isChecked())))
        {
          this.cbSetupSleepEnvironmentQ35A12.setChecked(false);
          continue;
          if (this.cbSetupSleepEnvironmentQ35A12.isChecked())
          {
            this.disableCheckBoxSelFlg = true;
            this.cbSetupSleepEnvironmentQ35A1.setChecked(false);
            this.cbSetupSleepEnvironmentQ35A2.setChecked(false);
            this.cbSetupSleepEnvironmentQ35A3.setChecked(false);
            this.cbSetupSleepEnvironmentQ35A4.setChecked(false);
            this.cbSetupSleepEnvironmentQ35A5.setChecked(false);
            this.cbSetupSleepEnvironmentQ35A6.setChecked(false);
            this.cbSetupSleepEnvironmentQ35A7.setChecked(false);
            this.cbSetupSleepEnvironmentQ35A8.setChecked(false);
            this.cbSetupSleepEnvironmentQ35A9.setChecked(false);
            this.cbSetupSleepEnvironmentQ35A10.setChecked(false);
            this.cbSetupSleepEnvironmentQ35A11.setChecked(false);
            this.disableCheckBoxSelFlg = false;
          }
        }
      }
    }
  }
  
  private void setReduceAmtOfLightInBedCheckBoxEnability(int paramInt)
  {
    switch (paramInt)
    {
    }
    for (;;)
    {
      return;
      if (this.cbSetupSleepEnvironmentQ38A6.isChecked())
      {
        this.disableCheckBoxSelFlg = true;
        this.cbSetupSleepEnvironmentQ38A1.setChecked(false);
        this.cbSetupSleepEnvironmentQ38A2.setChecked(false);
        this.cbSetupSleepEnvironmentQ38A3.setChecked(false);
        this.cbSetupSleepEnvironmentQ38A4.setChecked(false);
        this.cbSetupSleepEnvironmentQ38A5.setChecked(false);
        this.disableCheckBoxSelFlg = false;
      }
      else if ((this.cbSetupSleepEnvironmentQ38A1.isChecked()) || (this.cbSetupSleepEnvironmentQ38A2.isChecked()) || (this.cbSetupSleepEnvironmentQ38A3.isChecked()) || (this.cbSetupSleepEnvironmentQ38A4.isChecked()) || (this.cbSetupSleepEnvironmentQ38A5.isChecked()))
      {
        this.cbSetupSleepEnvironmentQ38A6.setChecked(false);
        continue;
        if ((!this.disableCheckBoxSelFlg) && ((this.cbSetupSleepEnvironmentQ38A1.isChecked()) || (this.cbSetupSleepEnvironmentQ38A2.isChecked()) || (this.cbSetupSleepEnvironmentQ38A3.isChecked()) || (this.cbSetupSleepEnvironmentQ38A4.isChecked()) || (this.cbSetupSleepEnvironmentQ38A5.isChecked())))
        {
          this.cbSetupSleepEnvironmentQ38A6.setChecked(false);
          continue;
          if (this.cbSetupSleepEnvironmentQ38A6.isChecked())
          {
            this.disableCheckBoxSelFlg = true;
            this.cbSetupSleepEnvironmentQ38A1.setChecked(false);
            this.cbSetupSleepEnvironmentQ38A2.setChecked(false);
            this.cbSetupSleepEnvironmentQ38A3.setChecked(false);
            this.cbSetupSleepEnvironmentQ38A4.setChecked(false);
            this.cbSetupSleepEnvironmentQ38A5.setChecked(false);
            this.disableCheckBoxSelFlg = false;
          }
        }
      }
    }
  }
  
  private void showSourceLightLayout()
  {
    if (this.csbSetupSleepEnvironmentNap_a37.getPositionSelected() > 0) {
      this.llSetupSleepEnviroment_q37a.setVisibility(0);
    }
    for (;;)
    {
      return;
      this.llSetupSleepEnviroment_q37a.setVisibility(8);
    }
  }
  
  private void showTypeOfSoundLayout()
  {
    if ((RefreshModelController.getInstance().getUser().getProfile().isSoundUsedInSleep() == -1) || (this.csssbSetupSleepEnvironmentQ36.getPositionSelected() <= 0)) {
      this.llSetupSleepEnvironment_q36a.setVisibility(8);
    }
    for (;;)
    {
      return;
      this.llSetupSleepEnvironment_q36a.setVisibility(0);
    }
  }
  
  public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
  {
    removeEditTextFocus();
    RST_User localRST_User = RefreshModelController.getInstance().getUser();
    switch (paramCompoundButton.getId())
    {
    }
    for (;;)
    {
      RefreshModelController.getInstance().save();
      this.updateSleepEnvironmentProg.updateSleepEnvironmentProgress();
      return;
      setControlBedroomTempCheckBoxEnability(paramCompoundButton.getId());
      paramCompoundButton = new BitSet();
      paramCompoundButton.set(0, this.cbSetupSleepEnvironmentQ30A1.isChecked());
      paramCompoundButton.set(1, this.cbSetupSleepEnvironmentQ30A2.isChecked());
      paramCompoundButton.set(2, this.cbSetupSleepEnvironmentQ30A3.isChecked());
      paramCompoundButton.set(3, this.cbSetupSleepEnvironmentQ30A4.isChecked());
      paramCompoundButton.set(4, this.cbSetupSleepEnvironmentQ30A5.isChecked());
      paramCompoundButton.set(5, this.cbSetupSleepEnvironmentQ30A6.isChecked());
      localRST_User.getProfile().setControlBedroomTemperature(RefreshTools.bitmaskToInt(paramCompoundButton));
      captureOtherControlBedroomTemperature(this.cbSetupSleepEnvironmentQ30A5.isChecked());
      continue;
      setGetWokenUpInNightByAnyCheckBoxEnability(paramCompoundButton.getId());
      paramCompoundButton = new BitSet();
      paramCompoundButton.set(0, this.cbSetupSleepEnvironmentQ35A1.isChecked());
      paramCompoundButton.set(1, this.cbSetupSleepEnvironmentQ35A2.isChecked());
      paramCompoundButton.set(2, this.cbSetupSleepEnvironmentQ35A3.isChecked());
      paramCompoundButton.set(3, this.cbSetupSleepEnvironmentQ35A4.isChecked());
      paramCompoundButton.set(4, this.cbSetupSleepEnvironmentQ35A5.isChecked());
      paramCompoundButton.set(5, this.cbSetupSleepEnvironmentQ35A6.isChecked());
      paramCompoundButton.set(6, this.cbSetupSleepEnvironmentQ35A7.isChecked());
      paramCompoundButton.set(7, this.cbSetupSleepEnvironmentQ35A8.isChecked());
      paramCompoundButton.set(8, this.cbSetupSleepEnvironmentQ35A9.isChecked());
      paramCompoundButton.set(9, this.cbSetupSleepEnvironmentQ35A10.isChecked());
      paramCompoundButton.set(10, this.cbSetupSleepEnvironmentQ35A11.isChecked());
      paramCompoundButton.set(11, this.cbSetupSleepEnvironmentQ35A12.isChecked());
      localRST_User.getProfile().setGetWokenUpInNightByAny(RefreshTools.bitmaskToInt(paramCompoundButton));
      continue;
      paramCompoundButton = new BitSet();
      paramCompoundButton.set(0, this.cbSetupSleepEnvironmentQ36aA1.isChecked());
      paramCompoundButton.set(1, this.cbSetupSleepEnvironmentQ36aA2.isChecked());
      paramCompoundButton.set(2, this.cbSetupSleepEnvironmentQ36aA3.isChecked());
      paramCompoundButton.set(3, this.cbSetupSleepEnvironmentQ36aA4.isChecked());
      paramCompoundButton.set(4, this.cbSetupSleepEnvironmentQ36aA5.isChecked());
      localRST_User.getProfile().setTypeOfSoundUsedInSleep(RefreshTools.bitmaskToInt(paramCompoundButton));
      captureOtherTypeOfSoundUsedInSleep(this.cbSetupSleepEnvironmentQ36aA5.isChecked());
      continue;
      paramCompoundButton = new BitSet();
      paramCompoundButton.set(0, this.cbSetupSleepEnvironmentQ37aA1.isChecked());
      paramCompoundButton.set(1, this.cbSetupSleepEnvironmentQ37aA2.isChecked());
      paramCompoundButton.set(2, this.cbSetupSleepEnvironmentQ37aA3.isChecked());
      paramCompoundButton.set(3, this.cbSetupSleepEnvironmentQ37aA4.isChecked());
      localRST_User.getProfile().setSourceOfLightInBedroom(RefreshTools.bitmaskToInt(paramCompoundButton));
      captureOtherSourceOfLightInBedroom(this.cbSetupSleepEnvironmentQ37aA4.isChecked());
      continue;
      setReduceAmtOfLightInBedCheckBoxEnability(paramCompoundButton.getId());
      paramCompoundButton = new BitSet();
      paramCompoundButton.set(0, this.cbSetupSleepEnvironmentQ38A1.isChecked());
      paramCompoundButton.set(1, this.cbSetupSleepEnvironmentQ38A2.isChecked());
      paramCompoundButton.set(2, this.cbSetupSleepEnvironmentQ38A3.isChecked());
      paramCompoundButton.set(3, this.cbSetupSleepEnvironmentQ38A4.isChecked());
      paramCompoundButton.set(4, this.cbSetupSleepEnvironmentQ38A5.isChecked());
      paramCompoundButton.set(5, this.cbSetupSleepEnvironmentQ38A6.isChecked());
      localRST_User.getProfile().setReduceAmountOfLightInBedroom(RefreshTools.bitmaskToInt(paramCompoundButton));
      captureOtherReduceAmountOfLightInBedroom(this.cbSetupSleepEnvironmentQ38A5.isChecked());
      continue;
      getSpecialLightUsedInSleepCheckBoxEnability(paramCompoundButton.getId());
      paramCompoundButton = new BitSet();
      paramCompoundButton.set(0, this.cbSetupSleepEnvironmentQ39A1.isChecked());
      paramCompoundButton.set(1, this.cbSetupSleepEnvironmentQ39A2.isChecked());
      paramCompoundButton.set(2, this.cbSetupSleepEnvironmentQ39A3.isChecked());
      localRST_User.getProfile().setSpecialLightUsedInSleep(RefreshTools.bitmaskToInt(paramCompoundButton));
    }
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    default: 
      removeEditTextFocus();
      switch (paramView.getId())
      {
      }
      break;
    }
    for (;;)
    {
      return;
      hideKeyboard();
      break;
      hideKeyboard();
      break;
      saveDataInDatabase();
      this.updateSleepEnvironmentProg.screenTransitionFromSleepEnvFrag(-1, -1);
      continue;
      this.updateSleepEnvironmentProg.screenTransitionFromSleepEnvFrag(2131099822, 1);
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903173, paramViewGroup, false);
    loadGUI(paramLayoutInflater);
    this.updateSleepEnvironmentProg.updateSleepEnvironmentProgress();
    return paramLayoutInflater;
  }
  
  public void onPause()
  {
    super.onPause();
    saveDataInDatabase();
    Log.i("************  ", "************In Pasue");
  }
  
  public void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean)
  {
    RST_UserProfile localRST_UserProfile = RefreshModelController.getInstance().getUser().getProfile();
    int i;
    switch (paramSeekBar.getId())
    {
    default: 
    case 2131100493: 
    case 2131100494: 
    case 2131100516: 
    case 2131100491: 
    case 2131100492: 
      for (;;)
      {
        RefreshModelController.getInstance().save();
        this.updateSleepEnvironmentProg.updateSleepEnvironmentProgress();
        return;
        localRST_UserProfile.setNoiseLevelInBedroom(paramInt);
        continue;
        localRST_UserProfile.setHowOftenElectronicsUsed(paramInt);
        continue;
        localRST_UserProfile.setLevelOfLightInBedroom(paramInt);
        showSourceLightLayout();
        continue;
        if (localRST_UserProfile.isHumidifierUsed() == -1)
        {
          if ((paramInt == 0) || (paramInt == 2))
          {
            i = paramInt;
            if (paramInt == 2) {
              i = 1;
            }
            setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupSleepEnvironmentQ31);
            localRST_UserProfile.setHumidifierUsed(i);
          }
        }
        else
        {
          localRST_UserProfile.setHumidifierUsed(paramInt);
          continue;
          if (localRST_UserProfile.isAirPurifierUsed() == -1)
          {
            if ((paramInt == 0) || (paramInt == 2))
            {
              i = paramInt;
              if (paramInt == 2) {
                i = 1;
              }
              setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupSleepEnvironmentQ32);
              localRST_UserProfile.setAirPurifierUsed(i);
            }
          }
          else {
            localRST_UserProfile.setAirPurifierUsed(paramInt);
          }
        }
      }
    }
    if (localRST_UserProfile.isSoundUsedInSleep() == -1) {
      if ((paramInt == 0) || (paramInt == 2))
      {
        i = paramInt;
        if (paramInt == 2) {
          i = 1;
        }
        setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupSleepEnvironmentQ36);
        localRST_UserProfile.setSoundUsedInSleep(i);
      }
    }
    for (;;)
    {
      RefreshModelController.getInstance().save();
      showTypeOfSoundLayout();
      break;
      localRST_UserProfile.setSoundUsedInSleep(paramInt);
    }
  }
  
  public void onResult(RST_Response<RST_UserProfile> paramRST_Response) {}
  
  public void onStartTrackingTouch(SeekBar paramSeekBar) {}
  
  public void onStopTrackingTouch(SeekBar paramSeekBar) {}
  
  public void setSleepEnvironmentUpdateProgressRef(ISleepEnvironmentUpdateProgress paramISleepEnvironmentUpdateProgress)
  {
    this.updateSleepEnvironmentProg = paramISleepEnvironmentUpdateProgress;
  }
  
  public static abstract interface ISleepEnvironmentUpdateProgress
  {
    public abstract void screenTransitionFromSleepEnvFrag(int paramInt1, int paramInt2);
    
    public abstract void updateSleepEnvironmentProgress();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SleepEnvironmentFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
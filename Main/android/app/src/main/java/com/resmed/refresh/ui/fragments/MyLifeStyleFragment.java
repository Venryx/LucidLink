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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class MyLifeStyleFragment
  extends BaseFragment
  implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener, RST_CallbackItem<RST_Response<RST_UserProfile>>
{
  private CheckBox cbsSetupMyLifeQ27A1;
  private CheckBox cbsSetupMyLifeQ27A2;
  private CheckBox cbsSetupMyLifeQ27A3;
  private CheckBox cbsSetupMyLifeQ27A4;
  private CheckBox cbsSetupMyLifeQ27A5;
  private CheckBox cbsSetupMyLifeQ27A6;
  private CheckBox cbsSetupMyLifeQ27A7;
  private CheckBox cbsSetupMyLifeQ27A8;
  private CustomSeekBar csbSetupMyLife_Q1;
  private CustomSeekBar csbSetupMyLife_Q2;
  private CustomSeekBar csbSetupMyLife_Q29;
  private CustomSeekBar csbSetupMyLife_Q4;
  private CustomSeekBar csbSetupMyLife_Q5;
  private CustomSeekBar csbSetupMyLife_Q6;
  private CustomSeekBar csbSetupMyLife_Q7;
  private CustomSeekBar csbSetupMyLife_Q8;
  private CustomSemiSliderSeekBar csssbSetupMyLifeQ26;
  private CustomSemiSliderSeekBar csssbSetupMyLifeQ3;
  private CustomSemiSliderSeekBar csssbSetupMyLifeQ9;
  private EditText edittextSetupMyLifeQ22A7;
  private EditText edittextSetupMyLifeQ24A8;
  private EditText edittextSetupMyLifeQ27A7;
  private List<ImageView> listIvSetupMySleepHowLongUsedMattress;
  private List<ImageView> listIvSetupMySleepOfBrandofmattress;
  private List<ImageView> listIvSetupMySleepOfLongcurrentpillows;
  private List<ImageView> listIvSetupMySleepOfSizemattress;
  private List<ImageView> listIvSetupMySleepOfTypeofmattress;
  private LinearLayout llSetupMyLifeBack;
  private LinearLayout llSetupMyLifeNext;
  private LinearLayout llSetupMyLife_Q3_content;
  private LinearLayout llSetupMyLife_Q5_content;
  private LinearLayout llSetupMyLife_Q7_content;
  private RelativeLayout rlSetupMyLife_Q11_content;
  public IMyLifeStyleUpdateProgress updateMyLifeStyleProg;
  
  private void captureOtherBrandOfMattress(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.edittextSetupMyLifeQ24A8.setVisibility(0);
      this.edittextSetupMyLifeQ24A8.setText(RefreshModelController.getInstance().getUser().getProfile().getOtherMattressBrand());
    }
    for (;;)
    {
      return;
      this.edittextSetupMyLifeQ24A8.setVisibility(8);
      this.edittextSetupMyLifeQ24A8.setText("");
    }
  }
  
  private void captureOtherPillowsType(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.edittextSetupMyLifeQ27A7.setVisibility(0);
      this.edittextSetupMyLifeQ27A7.setText(RefreshModelController.getInstance().getUser().getProfile().getOtherPillowsType());
    }
    for (;;)
    {
      return;
      this.edittextSetupMyLifeQ27A7.setVisibility(8);
      this.edittextSetupMyLifeQ27A7.setText("");
    }
  }
  
  private void captureOtherTypeOfMattress(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.edittextSetupMyLifeQ22A7.setVisibility(0);
      this.edittextSetupMyLifeQ22A7.setText(RefreshModelController.getInstance().getUser().getProfile().getOtherMattressType());
    }
    for (;;)
    {
      return;
      this.edittextSetupMyLifeQ22A7.setVisibility(8);
      this.edittextSetupMyLifeQ22A7.setText("");
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
  
  private void mapGUI(View paramView)
  {
    this.llSetupMyLifeBack = ((LinearLayout)paramView.findViewById(2131100151));
    this.llSetupMyLifeNext = ((LinearLayout)paramView.findViewById(2131100152));
    this.csbSetupMyLife_Q1 = ((CustomSeekBar)paramView.findViewById(2131100044));
    this.csbSetupMyLife_Q2 = ((CustomSeekBar)paramView.findViewById(2131100045));
    this.csbSetupMyLife_Q4 = ((CustomSeekBar)paramView.findViewById(2131100048));
    this.csbSetupMyLife_Q5 = ((CustomSeekBar)paramView.findViewById(2131100049));
    this.csbSetupMyLife_Q6 = ((CustomSeekBar)paramView.findViewById(2131100051));
    this.csbSetupMyLife_Q7 = ((CustomSeekBar)paramView.findViewById(2131100052));
    this.csbSetupMyLife_Q8 = ((CustomSeekBar)paramView.findViewById(2131100054));
    this.csbSetupMyLife_Q29 = ((CustomSeekBar)paramView.findViewById(2131100150));
    this.cbsSetupMyLifeQ27A1 = ((CheckBox)paramView.findViewById(2131100121));
    this.cbsSetupMyLifeQ27A2 = ((CheckBox)paramView.findViewById(2131100123));
    this.cbsSetupMyLifeQ27A3 = ((CheckBox)paramView.findViewById(2131100125));
    this.cbsSetupMyLifeQ27A4 = ((CheckBox)paramView.findViewById(2131100127));
    this.cbsSetupMyLifeQ27A5 = ((CheckBox)paramView.findViewById(2131100129));
    this.cbsSetupMyLifeQ27A6 = ((CheckBox)paramView.findViewById(2131100131));
    this.cbsSetupMyLifeQ27A7 = ((CheckBox)paramView.findViewById(2131100133));
    this.cbsSetupMyLifeQ27A8 = ((CheckBox)paramView.findViewById(2131100135));
    this.listIvSetupMySleepOfTypeofmattress = new ArrayList();
    this.listIvSetupMySleepOfTypeofmattress.add((ImageView)paramView.findViewById(2131100058));
    this.listIvSetupMySleepOfTypeofmattress.add((ImageView)paramView.findViewById(2131100060));
    this.listIvSetupMySleepOfTypeofmattress.add((ImageView)paramView.findViewById(2131100062));
    this.listIvSetupMySleepOfTypeofmattress.add((ImageView)paramView.findViewById(2131100064));
    this.listIvSetupMySleepOfTypeofmattress.add((ImageView)paramView.findViewById(2131100066));
    this.listIvSetupMySleepOfTypeofmattress.add((ImageView)paramView.findViewById(2131100068));
    this.listIvSetupMySleepOfTypeofmattress.add((ImageView)paramView.findViewById(2131100070));
    this.listIvSetupMySleepOfTypeofmattress.add((ImageView)paramView.findViewById(2131100072));
    this.listIvSetupMySleepOfSizemattress = new ArrayList();
    this.listIvSetupMySleepOfSizemattress.add((ImageView)paramView.findViewById(2131100076));
    this.listIvSetupMySleepOfSizemattress.add((ImageView)paramView.findViewById(2131100078));
    this.listIvSetupMySleepOfSizemattress.add((ImageView)paramView.findViewById(2131100080));
    this.listIvSetupMySleepOfSizemattress.add((ImageView)paramView.findViewById(2131100082));
    this.listIvSetupMySleepOfSizemattress.add((ImageView)paramView.findViewById(2131100084));
    this.listIvSetupMySleepOfSizemattress.add((ImageView)paramView.findViewById(2131100086));
    this.listIvSetupMySleepOfBrandofmattress = new ArrayList();
    this.listIvSetupMySleepOfBrandofmattress.add((ImageView)paramView.findViewById(2131100089));
    this.listIvSetupMySleepOfBrandofmattress.add((ImageView)paramView.findViewById(2131100091));
    this.listIvSetupMySleepOfBrandofmattress.add((ImageView)paramView.findViewById(2131100093));
    this.listIvSetupMySleepOfBrandofmattress.add((ImageView)paramView.findViewById(2131100095));
    this.listIvSetupMySleepOfBrandofmattress.add((ImageView)paramView.findViewById(2131100097));
    this.listIvSetupMySleepOfBrandofmattress.add((ImageView)paramView.findViewById(2131100099));
    this.listIvSetupMySleepOfBrandofmattress.add((ImageView)paramView.findViewById(2131100101));
    this.listIvSetupMySleepOfBrandofmattress.add((ImageView)paramView.findViewById(2131100103));
    this.listIvSetupMySleepOfBrandofmattress.add((ImageView)paramView.findViewById(2131100105));
    this.listIvSetupMySleepHowLongUsedMattress = new ArrayList();
    this.listIvSetupMySleepHowLongUsedMattress.add((ImageView)paramView.findViewById(2131100109));
    this.listIvSetupMySleepHowLongUsedMattress.add((ImageView)paramView.findViewById(2131100111));
    this.listIvSetupMySleepHowLongUsedMattress.add((ImageView)paramView.findViewById(2131100113));
    this.listIvSetupMySleepHowLongUsedMattress.add((ImageView)paramView.findViewById(2131100115));
    this.listIvSetupMySleepHowLongUsedMattress.add((ImageView)paramView.findViewById(2131100117));
    this.listIvSetupMySleepOfLongcurrentpillows = new ArrayList();
    this.listIvSetupMySleepOfLongcurrentpillows.add((ImageView)paramView.findViewById(2131100139));
    this.listIvSetupMySleepOfLongcurrentpillows.add((ImageView)paramView.findViewById(2131100141));
    this.listIvSetupMySleepOfLongcurrentpillows.add((ImageView)paramView.findViewById(2131100143));
    this.listIvSetupMySleepOfLongcurrentpillows.add((ImageView)paramView.findViewById(2131100145));
    this.listIvSetupMySleepOfLongcurrentpillows.add((ImageView)paramView.findViewById(2131100147));
    this.listIvSetupMySleepOfLongcurrentpillows.add((ImageView)paramView.findViewById(2131100149));
    this.edittextSetupMyLifeQ22A7 = ((EditText)paramView.findViewById(2131100073));
    this.edittextSetupMyLifeQ22A7.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        paramAnonymousView.setFocusable(true);
        paramAnonymousView.setFocusableInTouchMode(true);
        return false;
      }
    });
    this.edittextSetupMyLifeQ24A8 = ((EditText)paramView.findViewById(2131100106));
    this.edittextSetupMyLifeQ24A8.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        paramAnonymousView.setFocusable(true);
        paramAnonymousView.setFocusableInTouchMode(true);
        return false;
      }
    });
    this.edittextSetupMyLifeQ27A7 = ((EditText)paramView.findViewById(2131100136));
    this.edittextSetupMyLifeQ27A7.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        paramAnonymousView.setFocusable(true);
        paramAnonymousView.setFocusableInTouchMode(true);
        return false;
      }
    });
    paramView.findViewById(2131100057).setOnClickListener(this);
    paramView.findViewById(2131100059).setOnClickListener(this);
    paramView.findViewById(2131100061).setOnClickListener(this);
    paramView.findViewById(2131100063).setOnClickListener(this);
    paramView.findViewById(2131100065).setOnClickListener(this);
    paramView.findViewById(2131100067).setOnClickListener(this);
    paramView.findViewById(2131100069).setOnClickListener(this);
    paramView.findViewById(2131100071).setOnClickListener(this);
    paramView.findViewById(2131100075).setOnClickListener(this);
    paramView.findViewById(2131100077).setOnClickListener(this);
    paramView.findViewById(2131100079).setOnClickListener(this);
    paramView.findViewById(2131100081).setOnClickListener(this);
    paramView.findViewById(2131100083).setOnClickListener(this);
    paramView.findViewById(2131100085).setOnClickListener(this);
    paramView.findViewById(2131100088).setOnClickListener(this);
    paramView.findViewById(2131100090).setOnClickListener(this);
    paramView.findViewById(2131100092).setOnClickListener(this);
    paramView.findViewById(2131100094).setOnClickListener(this);
    paramView.findViewById(2131100096).setOnClickListener(this);
    paramView.findViewById(2131100098).setOnClickListener(this);
    paramView.findViewById(2131100100).setOnClickListener(this);
    paramView.findViewById(2131100102).setOnClickListener(this);
    paramView.findViewById(2131100104).setOnClickListener(this);
    paramView.findViewById(2131100108).setOnClickListener(this);
    paramView.findViewById(2131100110).setOnClickListener(this);
    paramView.findViewById(2131100112).setOnClickListener(this);
    paramView.findViewById(2131100114).setOnClickListener(this);
    paramView.findViewById(2131100116).setOnClickListener(this);
    paramView.findViewById(2131100138).setOnClickListener(this);
    paramView.findViewById(2131100140).setOnClickListener(this);
    paramView.findViewById(2131100142).setOnClickListener(this);
    paramView.findViewById(2131100144).setOnClickListener(this);
    paramView.findViewById(2131100146).setOnClickListener(this);
    paramView.findViewById(2131100148).setOnClickListener(this);
    this.csssbSetupMyLifeQ3 = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100046));
    this.csssbSetupMyLifeQ3.setProgressLineColor(-7829368);
    this.csssbSetupMyLifeQ3.setBackgroundLineColor(-7829368);
    this.csssbSetupMyLifeQ9 = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100055));
    this.csssbSetupMyLifeQ9.setProgressLineColor(-7829368);
    this.csssbSetupMyLifeQ9.setBackgroundLineColor(-7829368);
    this.csssbSetupMyLifeQ26 = ((CustomSemiSliderSeekBar)paramView.findViewById(2131100118));
    this.csssbSetupMyLifeQ26.setProgressLineColor(-7829368);
    this.csssbSetupMyLifeQ26.setBackgroundLineColor(-7829368);
    this.llSetupMyLife_Q3_content = ((LinearLayout)paramView.findViewById(2131100047));
    this.llSetupMyLife_Q5_content = ((LinearLayout)paramView.findViewById(2131100050));
    this.llSetupMyLife_Q7_content = ((LinearLayout)paramView.findViewById(2131100053));
    paramView = new ArrayList();
    paramView.add(getString(2131165600));
    paramView.add(getString(2131165601));
    paramView.add(getString(2131165602));
    paramView.add(getString(2131165603));
    this.csbSetupMyLife_Q1.setAdapter(paramView);
    this.csbSetupMyLife_Q1.setOnSeekBarChangeListener(this);
    paramView = new ArrayList();
    paramView.add(getString(2131165605));
    paramView.add(getString(2131165606));
    paramView.add(getString(2131165607));
    paramView.add(getString(2131165608));
    this.csbSetupMyLife_Q2.setAdapter(paramView);
    this.csbSetupMyLife_Q2.setOnSeekBarChangeListener(this);
    paramView = new ArrayList();
    paramView.add(getString(2131165611));
    paramView.add(getString(2131165612));
    paramView.add(getString(2131165613));
    paramView.add(getString(2131165614));
    paramView.add(getString(2131165615));
    this.csbSetupMyLife_Q4.setAdapter(paramView);
    this.csbSetupMyLife_Q4.setOnSeekBarChangeListener(this);
    paramView = new ArrayList();
    paramView.add(getString(2131165617));
    paramView.add(getString(2131165618));
    paramView.add(getString(2131165619));
    paramView.add(getString(2131165620));
    this.csbSetupMyLife_Q5.setAdapter(paramView);
    this.csbSetupMyLife_Q5.setOnSeekBarChangeListener(this);
    paramView = new ArrayList();
    paramView.add(getString(2131165622));
    paramView.add(getString(2131165623));
    paramView.add(getString(2131165624));
    this.csbSetupMyLife_Q6.setAdapter(paramView);
    this.csbSetupMyLife_Q6.setOnSeekBarChangeListener(this);
    paramView = new ArrayList();
    paramView.add(getString(2131165626));
    paramView.add(getString(2131165627));
    paramView.add(getString(2131165628));
    this.csbSetupMyLife_Q7.setAdapter(paramView);
    this.csbSetupMyLife_Q7.setOnSeekBarChangeListener(this);
    paramView = new ArrayList();
    paramView.add(getString(2131165630));
    paramView.add(getString(2131165631));
    paramView.add(getString(2131165632));
    this.csbSetupMyLife_Q8.setAdapter(paramView);
    this.csbSetupMyLife_Q8.setOnSeekBarChangeListener(this);
    paramView = new ArrayList();
    paramView.add(getString(2131165665));
    paramView.add(getString(2131165666));
    paramView.add(getString(2131165667));
    paramView.add(getString(2131165668));
    paramView.add(getString(2131165669));
    paramView = new ArrayList();
    paramView.add(getString(2131165688));
    paramView.add(getString(2131165689));
    paramView.add(getString(2131165690));
    this.csbSetupMyLife_Q29.setAdapter(paramView);
    this.csbSetupMyLife_Q29.setOnSeekBarChangeListener(this);
    this.csssbSetupMyLifeQ3.setOnSeekBarChangeListener(this);
    this.csssbSetupMyLifeQ9.setOnSeekBarChangeListener(this);
    this.csssbSetupMyLifeQ26.setOnSeekBarChangeListener(this);
    this.llSetupMyLifeNext.setOnClickListener(this);
    this.llSetupMyLifeBack.setOnClickListener(this);
    paramView = RefreshModelController.getInstance().getUser().getProfile();
    setCustomSeekBarSemiSliderAdapter(paramView.getSmoker(), this.csssbSetupMyLifeQ3);
    setCustomSeekBarSemiSliderAdapter(paramView.getExerciseBeforeBed(), this.csssbSetupMyLifeQ9);
    setCustomSeekBarSemiSliderAdapter(paramView.isMattressPadTopperOnBed(), this.csssbSetupMyLifeQ26);
    showSmokerLayouts();
    this.csbSetupMyLife_Q1.setSelection(paramView.getStressedFrequency());
    this.csbSetupMyLife_Q2.setSelection(paramView.getNumCaffeineDrinks());
    this.csbSetupMyLife_Q4.setSelection(paramView.getNumCigarettes());
    this.csbSetupMyLife_Q5.setSelection(paramView.getConsumeAlcohol());
    this.csbSetupMyLife_Q6.setSelection(paramView.getNumDrinks());
    this.csbSetupMyLife_Q7.setSelection(paramView.getNumExercises());
    this.csbSetupMyLife_Q8.setSelection(paramView.getExerciseIntensity());
    this.csbSetupMyLife_Q29.setSelection(paramView.getSleepingPosition());
    if (paramView.getMattressType() != -1) {
      selectTypeOfMattress(paramView.getMattressType());
    }
    if (paramView.getMattressSize() != -1) {
      selectSizeOfMattress(paramView.getMattressSize());
    }
    if (paramView.getMattressBrand() != -1) {
      selectBrandOfMattress(paramView.getMattressBrand());
    }
    if (paramView.getMattressAge() != -1) {
      selectMattressAge(paramView.getMattressAge());
    }
    if (paramView.getPillowsAge() != -1) {
      selectPillowsAge(paramView.getPillowsAge());
    }
    paramView = RefreshTools.bitmaskFromInt(paramView.getPillowsType());
    this.cbsSetupMyLifeQ27A1.setChecked(paramView.get(0));
    this.cbsSetupMyLifeQ27A2.setChecked(paramView.get(1));
    this.cbsSetupMyLifeQ27A3.setChecked(paramView.get(2));
    this.cbsSetupMyLifeQ27A4.setChecked(paramView.get(3));
    this.cbsSetupMyLifeQ27A5.setChecked(paramView.get(4));
    this.cbsSetupMyLifeQ27A6.setChecked(paramView.get(5));
    this.cbsSetupMyLifeQ27A7.setChecked(paramView.get(6));
    this.cbsSetupMyLifeQ27A8.setChecked(paramView.get(7));
    captureOtherPillowsType(this.cbsSetupMyLifeQ27A7.isChecked());
    this.cbsSetupMyLifeQ27A1.setOnCheckedChangeListener(this);
    this.cbsSetupMyLifeQ27A2.setOnCheckedChangeListener(this);
    this.cbsSetupMyLifeQ27A3.setOnCheckedChangeListener(this);
    this.cbsSetupMyLifeQ27A4.setOnCheckedChangeListener(this);
    this.cbsSetupMyLifeQ27A5.setOnCheckedChangeListener(this);
    this.cbsSetupMyLifeQ27A6.setOnCheckedChangeListener(this);
    this.cbsSetupMyLifeQ27A7.setOnCheckedChangeListener(this);
    this.cbsSetupMyLifeQ27A8.setOnCheckedChangeListener(this);
    showLayouts();
  }
  
  private void removeEditTextFocus()
  {
    this.edittextSetupMyLifeQ22A7.setFocusable(false);
    this.edittextSetupMyLifeQ22A7.setFocusableInTouchMode(false);
    this.edittextSetupMyLifeQ24A8.setFocusable(false);
    this.edittextSetupMyLifeQ24A8.setFocusableInTouchMode(false);
    this.edittextSetupMyLifeQ27A7.setFocusable(false);
    this.edittextSetupMyLifeQ27A7.setFocusableInTouchMode(false);
  }
  
  private void saveData()
  {
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    localRefreshModelController.save();
    localRefreshModelController.updateUserProfile(this);
  }
  
  private void saveDataInDatabase()
  {
    RST_User localRST_User = RefreshModelController.getInstance().getUser();
    localRST_User.getProfile().setOtherMattressType(this.edittextSetupMyLifeQ22A7.getText().toString().trim());
    localRST_User.getProfile().setOtherMattressBrand(this.edittextSetupMyLifeQ24A8.getText().toString().trim());
    localRST_User.getProfile().setOtherPillowsType(this.edittextSetupMyLifeQ27A7.getText().toString().trim());
    RefreshModelController.getInstance().save();
  }
  
  private void selectBrandOfMattress(int paramInt)
  {
    int i = 0;
    if (i >= this.listIvSetupMySleepOfBrandofmattress.size()) {
      if (this.listIvSetupMySleepOfBrandofmattress.size() > paramInt)
      {
        ((ImageView)this.listIvSetupMySleepOfBrandofmattress.get(paramInt)).setImageResource(2130837867);
        if (paramInt != 7) {
          break label112;
        }
        captureOtherBrandOfMattress(true);
      }
    }
    for (;;)
    {
      RefreshModelController.getInstance().getUser().getProfile().setMattressBrand(paramInt);
      RefreshModelController.getInstance().save();
      this.updateMyLifeStyleProg.updateMyLifeStyleProgress();
      return;
      ((ImageView)this.listIvSetupMySleepOfBrandofmattress.get(i)).setImageResource(2130837863);
      i++;
      break;
      label112:
      captureOtherBrandOfMattress(false);
    }
  }
  
  private void selectMattressAge(int paramInt)
  {
    for (int i = 0;; i++)
    {
      if (i >= this.listIvSetupMySleepHowLongUsedMattress.size())
      {
        if (this.listIvSetupMySleepHowLongUsedMattress.size() > paramInt) {
          ((ImageView)this.listIvSetupMySleepHowLongUsedMattress.get(paramInt)).setImageResource(2130837867);
        }
        RefreshModelController.getInstance().getUser().getProfile().setMattressAge(paramInt);
        RefreshModelController.getInstance().save();
        this.updateMyLifeStyleProg.updateMyLifeStyleProgress();
        return;
      }
      ((ImageView)this.listIvSetupMySleepHowLongUsedMattress.get(i)).setImageResource(2130837863);
    }
  }
  
  private void selectPillowsAge(int paramInt)
  {
    for (int i = 0;; i++)
    {
      if (i >= this.listIvSetupMySleepOfLongcurrentpillows.size())
      {
        if (this.listIvSetupMySleepOfLongcurrentpillows.size() > paramInt) {
          ((ImageView)this.listIvSetupMySleepOfLongcurrentpillows.get(paramInt)).setImageResource(2130837867);
        }
        RefreshModelController.getInstance().getUser().getProfile().setPillowsAge(paramInt);
        RefreshModelController.getInstance().save();
        this.updateMyLifeStyleProg.updateMyLifeStyleProgress();
        return;
      }
      ((ImageView)this.listIvSetupMySleepOfLongcurrentpillows.get(i)).setImageResource(2130837863);
    }
  }
  
  private void selectSizeOfMattress(int paramInt)
  {
    for (int i = 0;; i++)
    {
      if (i >= this.listIvSetupMySleepOfSizemattress.size())
      {
        if (this.listIvSetupMySleepOfSizemattress.size() > paramInt) {
          ((ImageView)this.listIvSetupMySleepOfSizemattress.get(paramInt)).setImageResource(2130837867);
        }
        RefreshModelController.getInstance().getUser().getProfile().setMattressSize(paramInt);
        RefreshModelController.getInstance().save();
        this.updateMyLifeStyleProg.updateMyLifeStyleProgress();
        return;
      }
      ((ImageView)this.listIvSetupMySleepOfSizemattress.get(i)).setImageResource(2130837863);
    }
  }
  
  private void selectTypeOfMattress(int paramInt)
  {
    int i = 0;
    if (i >= this.listIvSetupMySleepOfTypeofmattress.size()) {
      if (this.listIvSetupMySleepOfTypeofmattress.size() > paramInt)
      {
        ((ImageView)this.listIvSetupMySleepOfTypeofmattress.get(paramInt)).setImageResource(2130837867);
        if (paramInt != 6) {
          break label112;
        }
        captureOtherTypeOfMattress(true);
      }
    }
    for (;;)
    {
      RefreshModelController.getInstance().getUser().getProfile().setMattressType(paramInt);
      RefreshModelController.getInstance().save();
      this.updateMyLifeStyleProg.updateMyLifeStyleProgress();
      return;
      ((ImageView)this.listIvSetupMySleepOfTypeofmattress.get(i)).setImageResource(2130837863);
      i++;
      break;
      label112:
      captureOtherTypeOfMattress(false);
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
      this.csssbSetupMyLifeQ3.setAdapter(localArrayList);
      this.csssbSetupMyLifeQ3.setSelection(paramInt);
      continue;
      this.csssbSetupMyLifeQ9.setAdapter(localArrayList);
      this.csssbSetupMyLifeQ9.setSelection(paramInt);
      continue;
      this.csssbSetupMyLifeQ26.setAdapter(localArrayList);
      this.csssbSetupMyLifeQ26.setSelection(paramInt);
    }
  }
  
  private void showLayouts()
  {
    if (this.csbSetupMyLife_Q5.getPositionSelected() > 1)
    {
      this.llSetupMyLife_Q5_content.setVisibility(0);
      if (this.csbSetupMyLife_Q7.getPositionSelected() <= 0) {
        break label50;
      }
      this.llSetupMyLife_Q7_content.setVisibility(0);
    }
    for (;;)
    {
      return;
      this.llSetupMyLife_Q5_content.setVisibility(8);
      break;
      label50:
      this.llSetupMyLife_Q7_content.setVisibility(8);
    }
  }
  
  private void showSmokerLayouts()
  {
    if ((RefreshModelController.getInstance().getUser().getProfile().getSmoker() == -1) || (this.csssbSetupMyLifeQ3.getPositionSelected() <= 0)) {
      this.llSetupMyLife_Q3_content.setVisibility(8);
    }
    for (;;)
    {
      return;
      this.llSetupMyLife_Q3_content.setVisibility(0);
    }
  }
  
  public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
  {
    removeEditTextFocus();
    RST_UserProfile localRST_UserProfile = RefreshModelController.getInstance().getUser().getProfile();
    switch (paramCompoundButton.getId())
    {
    }
    for (;;)
    {
      RefreshModelController.getInstance().save();
      this.updateMyLifeStyleProg.updateMyLifeStyleProgress();
      return;
      paramCompoundButton = new BitSet();
      paramCompoundButton.set(0, this.cbsSetupMyLifeQ27A1.isChecked());
      paramCompoundButton.set(1, this.cbsSetupMyLifeQ27A2.isChecked());
      paramCompoundButton.set(2, this.cbsSetupMyLifeQ27A3.isChecked());
      paramCompoundButton.set(3, this.cbsSetupMyLifeQ27A4.isChecked());
      paramCompoundButton.set(4, this.cbsSetupMyLifeQ27A5.isChecked());
      paramCompoundButton.set(5, this.cbsSetupMyLifeQ27A6.isChecked());
      paramCompoundButton.set(6, this.cbsSetupMyLifeQ27A7.isChecked());
      paramCompoundButton.set(7, this.cbsSetupMyLifeQ27A8.isChecked());
      localRST_UserProfile.setPillowsType(RefreshTools.bitmaskToInt(paramCompoundButton));
      captureOtherPillowsType(this.cbsSetupMyLifeQ27A7.isChecked());
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
      this.updateMyLifeStyleProg.screenTransitionFromMyLifeStyleFrag(2131099826, 2);
      continue;
      this.updateMyLifeStyleProg.screenTransitionFromMyLifeStyleFrag(2131099818, 1);
      continue;
      selectTypeOfMattress(0);
      continue;
      selectTypeOfMattress(1);
      continue;
      selectTypeOfMattress(2);
      continue;
      selectTypeOfMattress(3);
      continue;
      selectTypeOfMattress(4);
      continue;
      selectTypeOfMattress(5);
      continue;
      selectTypeOfMattress(6);
      continue;
      selectTypeOfMattress(7);
      continue;
      selectSizeOfMattress(0);
      continue;
      selectSizeOfMattress(1);
      continue;
      selectSizeOfMattress(2);
      continue;
      selectSizeOfMattress(3);
      continue;
      selectSizeOfMattress(4);
      continue;
      selectSizeOfMattress(5);
      continue;
      selectBrandOfMattress(0);
      continue;
      selectBrandOfMattress(1);
      continue;
      selectBrandOfMattress(2);
      continue;
      selectBrandOfMattress(3);
      continue;
      selectBrandOfMattress(4);
      continue;
      selectBrandOfMattress(5);
      continue;
      selectBrandOfMattress(6);
      continue;
      selectBrandOfMattress(7);
      continue;
      selectBrandOfMattress(8);
      continue;
      selectMattressAge(0);
      continue;
      selectMattressAge(1);
      continue;
      selectMattressAge(2);
      continue;
      selectMattressAge(3);
      continue;
      selectMattressAge(4);
      continue;
      selectPillowsAge(0);
      continue;
      selectPillowsAge(1);
      continue;
      selectPillowsAge(2);
      continue;
      selectPillowsAge(3);
      continue;
      selectPillowsAge(4);
      continue;
      selectPillowsAge(5);
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903149, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    this.updateMyLifeStyleProg.updateMyLifeStyleProgress();
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
    switch (paramSeekBar.getId())
    {
    }
    for (;;)
    {
      RefreshModelController.getInstance().save();
      this.updateMyLifeStyleProg.updateMyLifeStyleProgress();
      return;
      localRST_UserProfile.setStressedFrequency(paramInt);
      continue;
      localRST_UserProfile.setNumCaffeineDrinks(paramInt);
      continue;
      localRST_UserProfile.setNumCigarettes(paramInt);
      continue;
      localRST_UserProfile.setConsumeAlcohol(paramInt);
      showLayouts();
      continue;
      localRST_UserProfile.setNumDrinks(paramInt);
      continue;
      localRST_UserProfile.setNumExercises(paramInt);
      showLayouts();
      continue;
      localRST_UserProfile.setExerciseIntensity(paramInt);
      continue;
      localRST_UserProfile.setSleepingPosition(paramInt);
      continue;
      int i;
      if (localRST_UserProfile.getSmoker() == -1) {
        if ((paramInt == 0) || (paramInt == 2))
        {
          i = paramInt;
          if (paramInt == 2) {
            i = 1;
          }
          setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupMyLifeQ3);
          localRST_UserProfile.setSmoker(i);
        }
      }
      for (;;)
      {
        RefreshModelController.getInstance().save();
        showSmokerLayouts();
        break;
        localRST_UserProfile.setSmoker(paramInt);
      }
      if (localRST_UserProfile.getExerciseBeforeBed() == -1)
      {
        if ((paramInt == 0) || (paramInt == 2))
        {
          i = paramInt;
          if (paramInt == 2) {
            i = 1;
          }
          setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupMyLifeQ9);
          localRST_UserProfile.setExerciseBeforeBed(i);
        }
      }
      else
      {
        localRST_UserProfile.setExerciseBeforeBed(paramInt);
        continue;
        if (localRST_UserProfile.isMattressPadTopperOnBed() == -1)
        {
          if ((paramInt == 0) || (paramInt == 2))
          {
            i = paramInt;
            if (paramInt == 2) {
              i = 1;
            }
            setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupMyLifeQ26);
            localRST_UserProfile.setMattressPadTopperOnBed(i);
          }
        }
        else {
          localRST_UserProfile.setMattressPadTopperOnBed(paramInt);
        }
      }
    }
  }
  
  public void onResult(RST_Response<RST_UserProfile> paramRST_Response)
  {
    if ((getBaseActivity() == null) || (getBaseActivity().isFinishing()) || (!getBaseActivity().isAppValidated(paramRST_Response.getErrorCode()))) {
      return;
    }
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    if (paramRST_Response.isStatus()) {}
    for (boolean bool = false;; bool = true)
    {
      localRefreshModelController.setProfileUpdatePending(bool);
      break;
    }
  }
  
  public void onStartTrackingTouch(SeekBar paramSeekBar) {}
  
  public void onStopTrackingTouch(SeekBar paramSeekBar) {}
  
  public void setMyLifeStyleUpdateProgressRef(IMyLifeStyleUpdateProgress paramIMyLifeStyleUpdateProgress)
  {
    this.updateMyLifeStyleProg = paramIMyLifeStyleUpdateProgress;
  }
  
  public static abstract interface IMyLifeStyleUpdateProgress
  {
    public abstract void screenTransitionFromMyLifeStyleFrag(int paramInt1, int paramInt2);
    
    public abstract void updateMyLifeStyleProgress();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\MyLifeStyleFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
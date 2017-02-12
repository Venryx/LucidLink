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
import android.widget.SeekBar;

import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_User;
import com.resmed.refresh.model.RST_UserProfile;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.customview.CustomSeekBar;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.utils.RefreshTools;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class SleepAidsQuestionaireFragment
  extends BaseFragment
  implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener, RST_CallbackItem<RST_Response<RST_UserProfile>>
{
  private CheckBox cbsleepaidsQ16A1;
  private CheckBox cbsleepaidsQ16A2;
  private CheckBox cbsleepaidsQ16A3;
  private CheckBox cbsleepaidsQ16A4;
  private CheckBox cbsleepaidsQ16aA1;
  private CheckBox cbsleepaidsQ16aA2;
  private CheckBox cbsleepaidsQ16aA3;
  private CheckBox cbsleepaidsQ16aA4;
  private CheckBox cbsleepaidsQ16aA5;
  private CheckBox cbsleepaidsQ16aA6;
  private CheckBox cbsleepaidsQ16aA7;
  private CheckBox cbsleepaidsQ16dA1;
  private CheckBox cbsleepaidsQ16dA10;
  private CheckBox cbsleepaidsQ16dA11;
  private CheckBox cbsleepaidsQ16dA2;
  private CheckBox cbsleepaidsQ16dA3;
  private CheckBox cbsleepaidsQ16dA4;
  private CheckBox cbsleepaidsQ16dA5;
  private CheckBox cbsleepaidsQ16dA6;
  private CheckBox cbsleepaidsQ16dA7;
  private CheckBox cbsleepaidsQ16dA8;
  private CheckBox cbsleepaidsQ16dA9;
  private CheckBox cbsleepaidsQ16gA1;
  private CheckBox cbsleepaidsQ16gA2;
  private CheckBox cbsleepaidsQ16gA3;
  private CheckBox cbsleepaidsQ16gA4;
  private CheckBox cbsleepaidsQ16gA5;
  private CustomSeekBar csbsleepaidsQ16c;
  private CustomSeekBar csbsleepaidsQ16f;
  private boolean disableCheckBoxSelFlg;
  private EditText edittextsleepaidsQ16aA7;
  private EditText edittextsleepaidsQ16gA5;
  private List<ImageView> listIvSetupSleepaids_q16b;
  private List<ImageView> listIvSetupSleepaids_q16e;
  private List<ImageView> listIvSetupSleepaids_q16h;
  private LinearLayout llSetupSleepAidsBack;
  private LinearLayout llSetupSleepAidsNext;
  private LinearLayout llSetupSleepaids_q16a_q16c;
  private LinearLayout llSetupSleepaids_q16d_q16f;
  private LinearLayout llSetupSleepaids_q16g_q16h;
  public ISleepAidsUpdateProgress updateSleepAidsProg;
  
  private void captureOtherHerbalRemediesType(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.edittextsleepaidsQ16gA5.setVisibility(0);
      this.edittextsleepaidsQ16gA5.setText(RefreshModelController.getInstance().getUser().getProfile().getOtherHerbalRemediesType());
    }
    for (;;)
    {
      return;
      this.edittextsleepaidsQ16gA5.setVisibility(8);
      this.edittextsleepaidsQ16gA5.setText("");
    }
  }
  
  private void captureOtherPrescriptionPillsType(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.edittextsleepaidsQ16aA7.setVisibility(0);
      this.edittextsleepaidsQ16aA7.setText(RefreshModelController.getInstance().getUser().getProfile().getOtherPrescriptionPillsType());
    }
    for (;;)
    {
      return;
      this.edittextsleepaidsQ16aA7.setVisibility(8);
      this.edittextsleepaidsQ16aA7.setText("");
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
    this.llSetupSleepAidsBack = ((LinearLayout)paramView.findViewById(2131100481));
    this.llSetupSleepAidsNext = ((LinearLayout)paramView.findViewById(2131100482));
    this.cbsleepaidsQ16A1 = ((CheckBox)paramView.findViewById(2131100409));
    this.cbsleepaidsQ16A2 = ((CheckBox)paramView.findViewById(2131100410));
    this.cbsleepaidsQ16A3 = ((CheckBox)paramView.findViewById(2131100411));
    this.cbsleepaidsQ16A4 = ((CheckBox)paramView.findViewById(2131100412));
    this.llSetupSleepaids_q16a_q16c = ((LinearLayout)paramView.findViewById(2131100413));
    this.llSetupSleepaids_q16a_q16c.setVisibility(8);
    this.cbsleepaidsQ16aA1 = ((CheckBox)paramView.findViewById(2131100415));
    this.cbsleepaidsQ16aA2 = ((CheckBox)paramView.findViewById(2131100416));
    this.cbsleepaidsQ16aA3 = ((CheckBox)paramView.findViewById(2131100417));
    this.cbsleepaidsQ16aA4 = ((CheckBox)paramView.findViewById(2131100418));
    this.cbsleepaidsQ16aA5 = ((CheckBox)paramView.findViewById(2131100419));
    this.cbsleepaidsQ16aA6 = ((CheckBox)paramView.findViewById(2131100420));
    this.cbsleepaidsQ16aA7 = ((CheckBox)paramView.findViewById(2131100421));
    this.edittextsleepaidsQ16aA7 = ((EditText)paramView.findViewById(2131100422));
    this.edittextsleepaidsQ16aA7.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        paramAnonymousView.setFocusable(true);
        paramAnonymousView.setFocusableInTouchMode(true);
        return false;
      }
    });
    this.listIvSetupSleepaids_q16b = new ArrayList();
    this.listIvSetupSleepaids_q16b.add((ImageView)paramView.findViewById(2131100425));
    this.listIvSetupSleepaids_q16b.add((ImageView)paramView.findViewById(2131100427));
    this.listIvSetupSleepaids_q16b.add((ImageView)paramView.findViewById(2131100429));
    this.listIvSetupSleepaids_q16b.add((ImageView)paramView.findViewById(2131100431));
    this.listIvSetupSleepaids_q16b.add((ImageView)paramView.findViewById(2131100433));
    this.csbsleepaidsQ16c = ((CustomSeekBar)paramView.findViewById(2131100435));
    Object localObject = new ArrayList();
    ((List)localObject).add(getString(2131165728));
    ((List)localObject).add(getString(2131165729));
    ((List)localObject).add(getString(2131165730));
    ((List)localObject).add(getString(2131165731));
    this.csbsleepaidsQ16c.setAdapter((List)localObject);
    this.llSetupSleepaids_q16d_q16f = ((LinearLayout)paramView.findViewById(2131100436));
    this.llSetupSleepaids_q16d_q16f.setVisibility(8);
    this.cbsleepaidsQ16dA1 = ((CheckBox)paramView.findViewById(2131100438));
    this.cbsleepaidsQ16dA2 = ((CheckBox)paramView.findViewById(2131100439));
    this.cbsleepaidsQ16dA3 = ((CheckBox)paramView.findViewById(2131100440));
    this.cbsleepaidsQ16dA4 = ((CheckBox)paramView.findViewById(2131100441));
    this.cbsleepaidsQ16dA5 = ((CheckBox)paramView.findViewById(2131100442));
    this.cbsleepaidsQ16dA6 = ((CheckBox)paramView.findViewById(2131100443));
    this.cbsleepaidsQ16dA7 = ((CheckBox)paramView.findViewById(2131100444));
    this.cbsleepaidsQ16dA8 = ((CheckBox)paramView.findViewById(2131100445));
    this.cbsleepaidsQ16dA9 = ((CheckBox)paramView.findViewById(2131100446));
    this.cbsleepaidsQ16dA10 = ((CheckBox)paramView.findViewById(2131100447));
    this.cbsleepaidsQ16dA11 = ((CheckBox)paramView.findViewById(2131100448));
    this.listIvSetupSleepaids_q16e = new ArrayList();
    this.listIvSetupSleepaids_q16e.add((ImageView)paramView.findViewById(2131100451));
    this.listIvSetupSleepaids_q16e.add((ImageView)paramView.findViewById(2131100453));
    this.listIvSetupSleepaids_q16e.add((ImageView)paramView.findViewById(2131100455));
    this.listIvSetupSleepaids_q16e.add((ImageView)paramView.findViewById(2131100457));
    this.listIvSetupSleepaids_q16e.add((ImageView)paramView.findViewById(2131100459));
    this.csbsleepaidsQ16f = ((CustomSeekBar)paramView.findViewById(2131100461));
    localObject = new ArrayList();
    ((List)localObject).add(getString(2131165751));
    ((List)localObject).add(getString(2131165752));
    ((List)localObject).add(getString(2131165753));
    ((List)localObject).add(getString(2131165754));
    this.csbsleepaidsQ16f.setAdapter((List)localObject);
    this.llSetupSleepaids_q16g_q16h = ((LinearLayout)paramView.findViewById(2131100462));
    this.llSetupSleepaids_q16g_q16h.setVisibility(8);
    this.cbsleepaidsQ16gA1 = ((CheckBox)paramView.findViewById(2131100464));
    this.cbsleepaidsQ16gA2 = ((CheckBox)paramView.findViewById(2131100465));
    this.cbsleepaidsQ16gA3 = ((CheckBox)paramView.findViewById(2131100466));
    this.cbsleepaidsQ16gA4 = ((CheckBox)paramView.findViewById(2131100467));
    this.cbsleepaidsQ16gA5 = ((CheckBox)paramView.findViewById(2131100468));
    this.edittextsleepaidsQ16gA5 = ((EditText)paramView.findViewById(2131100469));
    this.edittextsleepaidsQ16gA5.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        paramAnonymousView.setFocusable(true);
        paramAnonymousView.setFocusableInTouchMode(true);
        return false;
      }
    });
    this.listIvSetupSleepaids_q16h = new ArrayList();
    this.listIvSetupSleepaids_q16h.add((ImageView)paramView.findViewById(2131100472));
    this.listIvSetupSleepaids_q16h.add((ImageView)paramView.findViewById(2131100474));
    this.listIvSetupSleepaids_q16h.add((ImageView)paramView.findViewById(2131100476));
    this.listIvSetupSleepaids_q16h.add((ImageView)paramView.findViewById(2131100478));
    this.listIvSetupSleepaids_q16h.add((ImageView)paramView.findViewById(2131100480));
    localObject = RefreshModelController.getInstance().getUser();
    BitSet localBitSet = RefreshTools.bitmaskFromInt(((RST_User)localObject).getProfile().getUsingSleepAids());
    this.cbsleepaidsQ16A1.setChecked(localBitSet.get(0));
    this.cbsleepaidsQ16A2.setChecked(localBitSet.get(1));
    this.cbsleepaidsQ16A3.setChecked(localBitSet.get(2));
    this.cbsleepaidsQ16A4.setChecked(localBitSet.get(3));
    setUsingSleepAidsCheckBoxEnability(-1);
    localBitSet = RefreshTools.bitmaskFromInt(((RST_User)localObject).getProfile().getPrescriptionPillsType());
    this.cbsleepaidsQ16aA1.setChecked(localBitSet.get(0));
    this.cbsleepaidsQ16aA2.setChecked(localBitSet.get(1));
    this.cbsleepaidsQ16aA3.setChecked(localBitSet.get(2));
    this.cbsleepaidsQ16aA4.setChecked(localBitSet.get(3));
    this.cbsleepaidsQ16aA5.setChecked(localBitSet.get(4));
    this.cbsleepaidsQ16aA6.setChecked(localBitSet.get(5));
    this.cbsleepaidsQ16aA7.setChecked(localBitSet.get(6));
    captureOtherPrescriptionPillsType(this.cbsleepaidsQ16aA7.isChecked());
    if (((RST_User)localObject).getProfile().getPrescriptionPillsFrequency() != -1) {
      selectTakingMedicationForSleep(((RST_User)localObject).getProfile().getPrescriptionPillsFrequency());
    }
    this.csbsleepaidsQ16c.setSelection(((RST_User)localObject).getProfile().getPrescriptionPillsConcern());
    localBitSet = RefreshTools.bitmaskFromInt(((RST_User)localObject).getProfile().getOTCMedicationType());
    this.cbsleepaidsQ16dA1.setChecked(localBitSet.get(0));
    this.cbsleepaidsQ16dA2.setChecked(localBitSet.get(1));
    this.cbsleepaidsQ16dA3.setChecked(localBitSet.get(2));
    this.cbsleepaidsQ16dA4.setChecked(localBitSet.get(3));
    this.cbsleepaidsQ16dA5.setChecked(localBitSet.get(4));
    this.cbsleepaidsQ16dA6.setChecked(localBitSet.get(5));
    this.cbsleepaidsQ16dA7.setChecked(localBitSet.get(6));
    this.cbsleepaidsQ16dA8.setChecked(localBitSet.get(7));
    this.cbsleepaidsQ16dA9.setChecked(localBitSet.get(8));
    this.cbsleepaidsQ16dA10.setChecked(localBitSet.get(9));
    this.cbsleepaidsQ16dA11.setChecked(localBitSet.get(10));
    if (((RST_User)localObject).getProfile().getOTCMedicationFrequency() != -1) {
      selectTakingOTCMedicationForSleep(((RST_User)localObject).getProfile().getOTCMedicationFrequency());
    }
    this.csbsleepaidsQ16f.setSelection(((RST_User)localObject).getProfile().getOTCMedicationConcern());
    localBitSet = RefreshTools.bitmaskFromInt(((RST_User)localObject).getProfile().getHerbalRemediesType());
    this.cbsleepaidsQ16gA1.setChecked(localBitSet.get(0));
    this.cbsleepaidsQ16gA2.setChecked(localBitSet.get(1));
    this.cbsleepaidsQ16gA3.setChecked(localBitSet.get(2));
    this.cbsleepaidsQ16gA4.setChecked(localBitSet.get(3));
    this.cbsleepaidsQ16gA5.setChecked(localBitSet.get(4));
    captureOtherHerbalRemediesType(this.cbsleepaidsQ16gA5.isChecked());
    if (((RST_User)localObject).getProfile().getHerbalRemediesFrequency() != -1) {
      selectTakingHerbalRemediesForSleep(((RST_User)localObject).getProfile().getHerbalRemediesFrequency());
    }
    paramView.findViewById(2131100424).setOnClickListener(this);
    paramView.findViewById(2131100426).setOnClickListener(this);
    paramView.findViewById(2131100428).setOnClickListener(this);
    paramView.findViewById(2131100430).setOnClickListener(this);
    paramView.findViewById(2131100432).setOnClickListener(this);
    paramView.findViewById(2131100450).setOnClickListener(this);
    paramView.findViewById(2131100452).setOnClickListener(this);
    paramView.findViewById(2131100454).setOnClickListener(this);
    paramView.findViewById(2131100456).setOnClickListener(this);
    paramView.findViewById(2131100458).setOnClickListener(this);
    paramView.findViewById(2131100471).setOnClickListener(this);
    paramView.findViewById(2131100473).setOnClickListener(this);
    paramView.findViewById(2131100475).setOnClickListener(this);
    paramView.findViewById(2131100477).setOnClickListener(this);
    paramView.findViewById(2131100479).setOnClickListener(this);
    this.cbsleepaidsQ16A1.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16A2.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16A3.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16A4.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16aA1.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16aA2.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16aA3.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16aA4.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16aA5.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16aA6.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16aA7.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16dA1.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16dA2.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16dA3.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16dA4.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16dA5.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16dA6.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16dA7.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16dA8.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16dA9.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16dA10.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16dA11.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16gA1.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16gA2.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16gA3.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16gA4.setOnCheckedChangeListener(this);
    this.cbsleepaidsQ16gA5.setOnCheckedChangeListener(this);
    this.csbsleepaidsQ16c.setOnSeekBarChangeListener(this);
    this.csbsleepaidsQ16f.setOnSeekBarChangeListener(this);
    showOptionLayout();
    this.llSetupSleepAidsNext.setOnClickListener(this);
    this.llSetupSleepAidsBack.setOnClickListener(this);
  }
  
  private void removeEditTextFocus()
  {
    this.edittextsleepaidsQ16aA7.setFocusable(false);
    this.edittextsleepaidsQ16aA7.setFocusableInTouchMode(false);
    this.edittextsleepaidsQ16gA5.setFocusable(false);
    this.edittextsleepaidsQ16gA5.setFocusableInTouchMode(false);
  }
  
  private void saveDataInDatabase()
  {
    RST_User localRST_User = RefreshModelController.getInstance().getUser();
    localRST_User.getProfile().setOtherPrescriptionPillsType(this.edittextsleepaidsQ16aA7.getText().toString().trim().trim());
    localRST_User.getProfile().setOtherHerbalRemediesType(this.edittextsleepaidsQ16gA5.getText().toString().trim().trim());
    RefreshModelController.getInstance().save();
  }
  
  private void selectTakingHerbalRemediesForSleep(int paramInt)
  {
    for (int i = 0;; i++)
    {
      if (i >= this.listIvSetupSleepaids_q16h.size())
      {
        if (this.listIvSetupSleepaids_q16h.size() > paramInt) {
          ((ImageView)this.listIvSetupSleepaids_q16h.get(paramInt)).setImageResource(2130837867);
        }
        RefreshModelController.getInstance().getUser().getProfile().setHerbalRemediesFrequency(paramInt);
        RefreshModelController.getInstance().save();
        this.updateSleepAidsProg.updateSleepAidsProgress();
        return;
      }
      ((ImageView)this.listIvSetupSleepaids_q16h.get(i)).setImageResource(2130837863);
    }
  }
  
  private void selectTakingMedicationForSleep(int paramInt)
  {
    for (int i = 0;; i++)
    {
      if (i >= this.listIvSetupSleepaids_q16b.size())
      {
        if (this.listIvSetupSleepaids_q16b.size() > paramInt) {
          ((ImageView)this.listIvSetupSleepaids_q16b.get(paramInt)).setImageResource(2130837867);
        }
        RefreshModelController.getInstance().getUser().getProfile().setPrescriptionPillsFrequency(paramInt);
        RefreshModelController.getInstance().save();
        this.updateSleepAidsProg.updateSleepAidsProgress();
        return;
      }
      ((ImageView)this.listIvSetupSleepaids_q16b.get(i)).setImageResource(2130837863);
    }
  }
  
  private void selectTakingOTCMedicationForSleep(int paramInt)
  {
    for (int i = 0;; i++)
    {
      if (i >= this.listIvSetupSleepaids_q16e.size())
      {
        if (this.listIvSetupSleepaids_q16e.size() > paramInt) {
          ((ImageView)this.listIvSetupSleepaids_q16e.get(paramInt)).setImageResource(2130837867);
        }
        RefreshModelController.getInstance().getUser().getProfile().setOTCMedicationFrequency(paramInt);
        RefreshModelController.getInstance().save();
        this.updateSleepAidsProg.updateSleepAidsProgress();
        return;
      }
      ((ImageView)this.listIvSetupSleepaids_q16e.get(i)).setImageResource(2130837863);
    }
  }
  
  private void setUsingSleepAidsCheckBoxEnability(int paramInt)
  {
    switch (paramInt)
    {
    }
    for (;;)
    {
      return;
      if (this.cbsleepaidsQ16A4.isChecked())
      {
        this.disableCheckBoxSelFlg = true;
        this.cbsleepaidsQ16A1.setChecked(false);
        this.cbsleepaidsQ16A2.setChecked(false);
        this.cbsleepaidsQ16A3.setChecked(false);
        this.disableCheckBoxSelFlg = false;
      }
      else if ((this.cbsleepaidsQ16A1.isChecked()) || (this.cbsleepaidsQ16A2.isChecked()) || (this.cbsleepaidsQ16A3.isChecked()))
      {
        this.cbsleepaidsQ16A4.setChecked(false);
        continue;
        if ((!this.disableCheckBoxSelFlg) && ((this.cbsleepaidsQ16A1.isChecked()) || (this.cbsleepaidsQ16A2.isChecked()) || (this.cbsleepaidsQ16A3.isChecked())))
        {
          this.cbsleepaidsQ16A4.setChecked(false);
          continue;
          if (this.cbsleepaidsQ16A4.isChecked())
          {
            this.disableCheckBoxSelFlg = true;
            this.cbsleepaidsQ16A1.setChecked(false);
            this.cbsleepaidsQ16A2.setChecked(false);
            this.cbsleepaidsQ16A3.setChecked(false);
            this.disableCheckBoxSelFlg = false;
          }
        }
      }
    }
  }
  
  private void showOptionLayout()
  {
    if (this.cbsleepaidsQ16A1.isChecked())
    {
      this.llSetupSleepaids_q16a_q16c.setVisibility(0);
      if (!this.cbsleepaidsQ16A2.isChecked()) {
        break label67;
      }
      this.llSetupSleepaids_q16d_q16f.setVisibility(0);
      label36:
      if (!this.cbsleepaidsQ16A3.isChecked()) {
        break label79;
      }
      this.llSetupSleepaids_q16g_q16h.setVisibility(0);
    }
    for (;;)
    {
      return;
      this.llSetupSleepaids_q16a_q16c.setVisibility(8);
      break;
      label67:
      this.llSetupSleepaids_q16d_q16f.setVisibility(8);
      break label36;
      label79:
      this.llSetupSleepaids_q16g_q16h.setVisibility(8);
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
      this.updateSleepAidsProg.updateSleepAidsProgress();
      return;
      setUsingSleepAidsCheckBoxEnability(paramCompoundButton.getId());
      paramCompoundButton = new BitSet();
      paramCompoundButton.set(0, this.cbsleepaidsQ16A1.isChecked());
      paramCompoundButton.set(1, this.cbsleepaidsQ16A2.isChecked());
      paramCompoundButton.set(2, this.cbsleepaidsQ16A3.isChecked());
      paramCompoundButton.set(3, this.cbsleepaidsQ16A4.isChecked());
      localRST_User.getProfile().setUsingSleepAids(RefreshTools.bitmaskToInt(paramCompoundButton));
      showOptionLayout();
      continue;
      paramCompoundButton = new BitSet();
      paramCompoundButton.set(0, this.cbsleepaidsQ16aA1.isChecked());
      paramCompoundButton.set(1, this.cbsleepaidsQ16aA2.isChecked());
      paramCompoundButton.set(2, this.cbsleepaidsQ16aA3.isChecked());
      paramCompoundButton.set(3, this.cbsleepaidsQ16aA4.isChecked());
      paramCompoundButton.set(4, this.cbsleepaidsQ16aA5.isChecked());
      paramCompoundButton.set(5, this.cbsleepaidsQ16aA6.isChecked());
      paramCompoundButton.set(6, this.cbsleepaidsQ16aA7.isChecked());
      localRST_User.getProfile().setPrescriptionPillsType(RefreshTools.bitmaskToInt(paramCompoundButton));
      captureOtherPrescriptionPillsType(this.cbsleepaidsQ16aA7.isChecked());
      continue;
      paramCompoundButton = new BitSet();
      paramCompoundButton.set(0, this.cbsleepaidsQ16dA1.isChecked());
      paramCompoundButton.set(1, this.cbsleepaidsQ16dA2.isChecked());
      paramCompoundButton.set(2, this.cbsleepaidsQ16dA3.isChecked());
      paramCompoundButton.set(3, this.cbsleepaidsQ16dA4.isChecked());
      paramCompoundButton.set(4, this.cbsleepaidsQ16dA5.isChecked());
      paramCompoundButton.set(5, this.cbsleepaidsQ16dA6.isChecked());
      paramCompoundButton.set(6, this.cbsleepaidsQ16dA7.isChecked());
      paramCompoundButton.set(7, this.cbsleepaidsQ16dA8.isChecked());
      paramCompoundButton.set(8, this.cbsleepaidsQ16dA9.isChecked());
      paramCompoundButton.set(9, this.cbsleepaidsQ16dA10.isChecked());
      paramCompoundButton.set(10, this.cbsleepaidsQ16dA11.isChecked());
      localRST_User.getProfile().setOTCMedicationType(RefreshTools.bitmaskToInt(paramCompoundButton));
      continue;
      paramCompoundButton = new BitSet();
      paramCompoundButton.set(0, this.cbsleepaidsQ16gA1.isChecked());
      paramCompoundButton.set(1, this.cbsleepaidsQ16gA2.isChecked());
      paramCompoundButton.set(2, this.cbsleepaidsQ16gA3.isChecked());
      paramCompoundButton.set(3, this.cbsleepaidsQ16gA4.isChecked());
      paramCompoundButton.set(4, this.cbsleepaidsQ16gA5.isChecked());
      localRST_User.getProfile().setHerbalRemediesType(RefreshTools.bitmaskToInt(paramCompoundButton));
      captureOtherHerbalRemediesType(this.cbsleepaidsQ16gA5.isChecked());
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
      this.updateSleepAidsProg.updateSleepAidsProgress();
      return;
      hideKeyboard();
      break;
      hideKeyboard();
      break;
      saveDataInDatabase();
      this.updateSleepAidsProg.screenTransitionFromSleepAidsFrag(2131099822, 2);
      continue;
      this.updateSleepAidsProg.screenTransitionFromSleepAidsFrag(2131099814, 1);
      continue;
      selectTakingMedicationForSleep(0);
      continue;
      selectTakingMedicationForSleep(1);
      continue;
      selectTakingMedicationForSleep(2);
      continue;
      selectTakingMedicationForSleep(3);
      continue;
      selectTakingMedicationForSleep(4);
      continue;
      selectTakingOTCMedicationForSleep(0);
      continue;
      selectTakingOTCMedicationForSleep(1);
      continue;
      selectTakingOTCMedicationForSleep(2);
      continue;
      selectTakingOTCMedicationForSleep(3);
      continue;
      selectTakingOTCMedicationForSleep(4);
      continue;
      selectTakingHerbalRemediesForSleep(0);
      continue;
      selectTakingHerbalRemediesForSleep(1);
      continue;
      selectTakingHerbalRemediesForSleep(2);
      continue;
      selectTakingHerbalRemediesForSleep(3);
      continue;
      selectTakingHerbalRemediesForSleep(4);
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903172, paramViewGroup, false);
    loadGUI(paramLayoutInflater);
    this.updateSleepAidsProg.updateSleepAidsProgress();
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
      this.updateSleepAidsProg.updateSleepAidsProgress();
      return;
      localRST_UserProfile.setPrescriptionPillsConcern(paramInt);
      continue;
      localRST_UserProfile.setOTCMedicationConcern(paramInt);
    }
  }
  
  public void onResult(RST_Response<RST_UserProfile> paramRST_Response) {}
  
  public void onStartTrackingTouch(SeekBar paramSeekBar) {}
  
  public void onStopTrackingTouch(SeekBar paramSeekBar) {}
  
  public void setSleepAidsUpdateProgressRef(ISleepAidsUpdateProgress paramISleepAidsUpdateProgress)
  {
    this.updateSleepAidsProg = paramISleepAidsUpdateProgress;
  }
  
  public static abstract interface ISleepAidsUpdateProgress
  {
    public abstract void screenTransitionFromSleepAidsFrag(int paramInt1, int paramInt2);
    
    public abstract void updateSleepAidsProgress();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SleepAidsQuestionaireFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
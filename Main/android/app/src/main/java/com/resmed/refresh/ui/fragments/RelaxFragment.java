package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.resmed.refresh.ui.activity.RelaxActivity;
import com.resmed.refresh.ui.customview.CustomSwitch;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.ActiveFragmentInterface;
import com.resmed.refresh.ui.utils.RelaxDataManager;
import com.resmed.refresh.utils.audio.SoundResources.RelaxSound;

public class RelaxFragment
  extends BaseFragment
{
  private ActiveFragmentInterface mcActiveFragmentInterface;
  private RelaxBtn relaxBtn;
  private RelaxDataManager relaxDataManager;
  private TextView relaxTitle;
  private TextView selectedSound;
  private LinearLayout soundSelection;
  private CustomSwitch switchRelax;
  private LinearLayout wrapperRelaxData;
  
  private void mapGUI(View paramView)
  {
    this.switchRelax = ((CustomSwitch)paramView.findViewById(2131100333));
    this.wrapperRelaxData = ((LinearLayout)paramView.findViewById(2131100334));
    this.soundSelection = ((LinearLayout)paramView.findViewById(2131100335));
    this.relaxTitle = ((TextView)paramView.findViewById(2131100332));
    this.selectedSound = ((TextView)paramView.findViewById(2131100338));
  }
  
  private void setupListeners()
  {
    this.switchRelax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean)
        {
          RelaxFragment.this.relaxDataManager.setActiveRelax(true);
          RelaxFragment.this.wrapperRelaxData.setVisibility(0);
          RelaxFragment.this.relaxBtn.setActiveRelax();
          RelaxFragment.this.relaxTitle.setText(2131165836);
        }
        for (;;)
        {
          return;
          RelaxFragment.this.relaxDataManager.setActiveRelax(false);
          RelaxFragment.this.wrapperRelaxData.setVisibility(8);
          RelaxFragment.this.relaxBtn.setActiveRelax();
          RelaxFragment.this.relaxTitle.setText(2131165837);
        }
      }
    });
    this.soundSelection.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RelaxFragment.this.relaxBtn.setRelaxSoundClick();
      }
    });
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.relaxBtn = ((RelaxBtn)paramActivity);
      this.mcActiveFragmentInterface = ((ActiveFragmentInterface)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new ClassCastException(paramActivity.toString() + " ...you must implement RelaxBtn from your Activity ;-) !");
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903156, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    return paramLayoutInflater;
  }
  
  public void onResume()
  {
    super.onResume();
    ActiveFragmentInterface localActiveFragmentInterface = this.mcActiveFragmentInterface;
    ((RelaxActivity)getActivity()).getClass();
    localActiveFragmentInterface.notifyCurrentFragment(0);
    this.relaxDataManager = RelaxDataManager.getInstance();
    this.switchRelax.setChecked(this.relaxDataManager.getActiveRelax());
    if (this.relaxDataManager.getActiveRelax())
    {
      this.wrapperRelaxData.setVisibility(0);
      if (!this.relaxDataManager.getActiveRelax()) {
        break label102;
      }
      this.relaxTitle.setText(2131165836);
    }
    for (;;)
    {
      updateSoundText();
      return;
      this.wrapperRelaxData.setVisibility(8);
      break;
      label102:
      this.relaxTitle.setText(2131165837);
    }
  }
  
  public void updateSoundText()
  {
    this.selectedSound.setText(this.relaxDataManager.getRelaxSound().getName());
  }
  
  public static abstract interface RelaxBtn
  {
    public abstract void setActiveRelax();
    
    public abstract void setRelaxSoundClick();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\RelaxFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
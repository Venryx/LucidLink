package com.resmed.refresh.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.resmed.refresh.ui.activity.MindClearActivity;
import com.resmed.refresh.ui.activity.RelaxActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;

public class SleepAidsFragment
  extends BaseFragment
  implements View.OnClickListener
{
  private ImageView ivSleepaidsMindClear;
  private ImageView ivSleepaidsRelax;
  
  private void mapGUI(View paramView)
  {
    this.ivSleepaidsRelax = ((ImageView)paramView.findViewById(2131100369));
    this.ivSleepaidsMindClear = ((ImageView)paramView.findViewById(2131100370));
  }
  
  private void setupListeners()
  {
    this.ivSleepaidsRelax.setOnClickListener(this);
    this.ivSleepaidsMindClear.setOnClickListener(this);
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    }
    for (;;)
    {
      return;
      startActivity(new Intent(getActivity(), RelaxActivity.class));
      continue;
      startActivity(new Intent(getActivity(), MindClearActivity.class));
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903164, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    return paramLayoutInflater;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SleepAidsFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
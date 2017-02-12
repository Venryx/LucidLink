package com.resmed.refresh.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.resmed.refresh.ui.activity.HomeActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;

public class PairingInfoFragment
  extends BaseFragment
  implements View.OnClickListener
{
  private Button laterPairingButton;
  private Button pairNowPairingButton;
  
  private void mapGUI(View paramView)
  {
    this.pairNowPairingButton = ((Button)paramView.findViewById(2131100306));
    this.laterPairingButton = ((Button)paramView.findViewById(2131100307));
  }
  
  private void setupListeners()
  {
    this.pairNowPairingButton.setOnClickListener(this);
    this.laterPairingButton.setOnClickListener(this);
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    }
    for (;;)
    {
      return;
      paramView = new Intent(getActivity(), HomeActivity.class);
      paramView.setFlags(268468224);
      startActivity(paramView);
      getActivity().overridePendingTransition(2130968582, 2130968583);
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903151, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    return paramLayoutInflater;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\PairingInfoFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.resmed.refresh.ui.activity.LoginActivty;
import com.resmed.refresh.ui.uibase.base.BaseFragment;

public class ForgottenPasswordSentFragment
  extends BaseFragment
  implements View.OnClickListener
{
  private Button loginForgottenButton;
  
  private void mapGUI(View paramView)
  {
    this.loginForgottenButton = ((Button)paramView.findViewById(2131099967));
  }
  
  private void setupListeners()
  {
    this.loginForgottenButton.setOnClickListener(this);
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    }
    for (;;)
    {
      return;
      startActivity(new Intent(getActivity(), LoginActivty.class));
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903139, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    return paramLayoutInflater;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\ForgottenPasswordSentFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
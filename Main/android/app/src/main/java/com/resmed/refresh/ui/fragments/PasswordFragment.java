package com.resmed.refresh.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.resmed.refresh.ui.activity.ForgottenPasswordActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;

public class PasswordFragment
  extends BaseFragment
  implements View.OnClickListener
{
  private EditText newPasswordEditText;
  private EditText oldPasswordEditText;
  private Button savePasswordButton;
  
  private boolean checkData()
  {
    if ((this.oldPasswordEditText.getText().toString().length() == 0) || (this.newPasswordEditText.getText().toString().length() == 0)) {}
    for (boolean bool = false;; bool = true) {
      return bool;
    }
  }
  
  private void mapGUI(View paramView)
  {
    this.oldPasswordEditText = ((EditText)paramView.findViewById(2131100308));
    this.newPasswordEditText = ((EditText)paramView.findViewById(2131100309));
    this.savePasswordButton = ((Button)paramView.findViewById(2131100310));
  }
  
  private void save()
  {
    if (!checkData()) {
      Toast.makeText(getActivity(), "Check fields!", 0).show();
    }
    for (;;)
    {
      return;
      getActivity().finish();
    }
  }
  
  private void setupListeners()
  {
    this.savePasswordButton.setOnClickListener(this);
    this.newPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
    {
      public boolean onEditorAction(TextView paramAnonymousTextView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
      {
        boolean bool = false;
        if ((paramAnonymousInt == 4) || (paramAnonymousInt == 6))
        {
          PasswordFragment.this.save();
          bool = true;
        }
        return bool;
      }
    });
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    }
    for (;;)
    {
      return;
      save();
      continue;
      startActivity(new Intent(getActivity(), ForgottenPasswordActivity.class));
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903152, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    return paramLayoutInflater;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\PasswordFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
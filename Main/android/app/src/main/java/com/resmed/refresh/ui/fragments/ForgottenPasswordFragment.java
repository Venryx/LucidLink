package com.resmed.refresh.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_User;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.utils.RefreshTools;

public class ForgottenPasswordFragment
  extends BaseFragment
  implements View.OnClickListener, RST_CallbackItem<RST_Response<RST_User>>
{
  private EditText emailForgottenEditText;
  private ImageView ivClearEmail;
  private Button sendForgottenButton;
  
  private void emailSent()
  {
    getBaseActivity().showProgressDialog(2131165348);
    RefreshModelController.getInstance().userForgotPassword(this.emailForgottenEditText.getText().toString(), this);
  }
  
  private void mapGUI(View paramView)
  {
    this.sendForgottenButton = ((Button)paramView.findViewById(2131099966));
    this.emailForgottenEditText = ((EditText)paramView.findViewById(2131099964));
    this.ivClearEmail = ((ImageView)paramView.findViewById(2131099965));
    this.sendForgottenButton.setTypeface(this.akzidenzLight);
    this.emailForgottenEditText.setTypeface(this.akzidenzLight);
  }
  
  private void resetPassword()
  {
    if (!RefreshTools.validateEmail(this.emailForgottenEditText.getText().toString())) {
      getBaseActivity().showErrorDialog(getString(2131165437));
    }
    for (;;)
    {
      return;
      emailSent();
    }
  }
  
  private void setupListeners()
  {
    this.sendForgottenButton.setOnClickListener(this);
    this.ivClearEmail.setOnClickListener(this);
    this.emailForgottenEditText.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramAnonymousEditable)
      {
        if (paramAnonymousEditable.length() > 0) {
          ForgottenPasswordFragment.this.ivClearEmail.setVisibility(0);
        }
        for (;;)
        {
          return;
          ForgottenPasswordFragment.this.ivClearEmail.setVisibility(4);
        }
      }
      
      public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
      
      public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
    });
    this.emailForgottenEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
    {
      public boolean onEditorAction(TextView paramAnonymousTextView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
      {
        boolean bool = false;
        if ((paramAnonymousInt == 4) || (paramAnonymousInt == 6))
        {
          ForgottenPasswordFragment.this.resetPassword();
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
      RefreshTools.hideKeyBoard(getActivity());
      return;
      resetPassword();
      continue;
      this.emailForgottenEditText.setText("");
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramViewGroup = paramLayoutInflater.inflate(2130903138, paramViewGroup, false);
    mapGUI(paramViewGroup);
    setupListeners();
    paramLayoutInflater = getActivity().getIntent().getStringExtra("com.resmed.refresh.consts.forgotten_password_email");
    if ((paramLayoutInflater != null) && (paramLayoutInflater.length() > 0))
    {
      this.emailForgottenEditText.setText(paramLayoutInflater);
      this.ivClearEmail.setVisibility(0);
    }
    for (;;)
    {
      return paramViewGroup;
      this.ivClearEmail.setVisibility(4);
    }
  }
  
  public void onResult(RST_Response<RST_User> paramRST_Response)
  {
    if ((getBaseActivity() == null) || (getBaseActivity().isFinishing()) || (!getBaseActivity().isAppValidated(paramRST_Response.getErrorCode()))) {}
    for (;;)
    {
      return;
      getBaseActivity().dismissProgressDialog();
      if (!paramRST_Response.isStatus()) {
        break;
      }
      paramRST_Response = new ForgottenPasswordSentFragment();
      FragmentTransaction localFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
      localFragmentTransaction.setCustomAnimations(2130968595, 2130968592, 2130968591, 2130968596);
      localFragmentTransaction.replace(2131099792, paramRST_Response);
      localFragmentTransaction.commit();
    }
    if (!RefreshApplication.getInstance().isConnectedToInternet()) {
      paramRST_Response = getString(2131165350);
    }
    for (;;)
    {
      getBaseActivity().showErrorDialog(paramRST_Response);
      break;
      if (paramRST_Response.getErrorCode() == 400) {
        paramRST_Response = getString(2131165468);
      } else {
        paramRST_Response = paramRST_Response.getErrorMessage();
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\ForgottenPasswordFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
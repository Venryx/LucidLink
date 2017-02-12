package com.resmed.refresh.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_User;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.activity.ForgottenPasswordActivity;
import com.resmed.refresh.ui.activity.HomeActivity;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.CustomDialogBuilder;
import com.resmed.refresh.utils.RefreshTools;

public class LoginFragment
  extends BaseFragment
  implements View.OnClickListener, RST_CallbackItem<RST_Response<RST_User>>
{
  public static boolean isFromLogin = false;
  private ImageView ivClearEmail;
  private TextView loginButton;
  private EditText loginEmailEditText;
  private TextView loginForgottenPasswordTextView;
  private EditText loginPasswordEditText;
  
  private void login()
  {
    if (!RefreshTools.validateEmail(this.loginEmailEditText.getText().toString())) {
      getBaseActivity().showErrorDialog(getString(2131165437));
    }
    for (;;)
    {
      return;
      getBaseActivity().showProgressDialog(2131165348);
      RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
      localRefreshModelController.userLogout();
      localRefreshModelController.userLogin(this.loginEmailEditText.getText().toString(), this.loginPasswordEditText.getText().toString(), this);
    }
  }
  
  private void mapGUI(View paramView)
  {
    this.loginEmailEditText = ((EditText)paramView.findViewById(2131100032));
    this.loginPasswordEditText = ((EditText)paramView.findViewById(2131100033));
    this.loginButton = ((TextView)paramView.findViewById(2131100034));
    this.loginForgottenPasswordTextView = ((TextView)paramView.findViewById(2131100035));
    this.ivClearEmail = ((ImageView)paramView.findViewById(2131099965));
    this.loginEmailEditText.setTypeface(this.akzidenzLight);
    this.loginPasswordEditText.setTypeface(this.akzidenzLight);
    this.loginForgottenPasswordTextView.setPaintFlags(this.loginForgottenPasswordTextView.getPaintFlags() | 0x8);
    paramView = RefreshModelController.getInstance().getLastEmail();
    if ((paramView != null) && (paramView.length() > 0))
    {
      this.loginEmailEditText.setText(paramView);
      this.ivClearEmail.setVisibility(0);
      this.loginEmailEditText.requestFocus();
    }
    for (;;)
    {
      return;
      this.ivClearEmail.setVisibility(4);
    }
  }
  
  private void setupListeners()
  {
    this.loginButton.setOnClickListener(this);
    this.ivClearEmail.setOnClickListener(this);
    this.loginForgottenPasswordTextView.setOnClickListener(this);
    this.loginEmailEditText.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramAnonymousEditable)
      {
        if (paramAnonymousEditable.length() > 0) {
          LoginFragment.this.ivClearEmail.setVisibility(0);
        }
        for (;;)
        {
          return;
          LoginFragment.this.ivClearEmail.setVisibility(4);
        }
      }
      
      public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
      
      public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
    });
    this.loginPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
    {
      public boolean onEditorAction(TextView paramAnonymousTextView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
      {
        boolean bool = false;
        if ((paramAnonymousInt == 4) || (paramAnonymousInt == 6))
        {
          LoginFragment.this.login();
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
      login();
      isFromLogin = true;
      continue;
      paramView = new Intent(getActivity(), ForgottenPasswordActivity.class);
      paramView.putExtra("com.resmed.refresh.consts.forgotten_password_email", this.loginEmailEditText.getText().toString());
      startActivity(paramView);
      continue;
      this.loginEmailEditText.setText("");
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903147, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    return paramLayoutInflater;
  }
  
  public void onResult(RST_Response<RST_User> paramRST_Response)
  {
    if ((getBaseActivity() == null) || (getBaseActivity().isFinishing())) {}
    for (;;)
    {
      return;
      getBaseActivity().dismissProgressDialog();
      if (!paramRST_Response.isStatus()) {
        break;
      }
      paramRST_Response = new Intent(getActivity(), HomeActivity.class);
      paramRST_Response.setFlags(268468224);
      startActivity(paramRST_Response);
      getActivity().overridePendingTransition(2130968582, 2130968583);
    }
    if (RefreshApplication.getInstance().isConnectedToInternet()) {}
    for (int i = 2131165444;; i = 2131165350)
    {
      getBaseActivity().showDialog(new CustomDialogBuilder(getBaseActivity()).title(2131165443).description(i).setPositiveButton(2131165296, null));
      break;
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\LoginFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
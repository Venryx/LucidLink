package com.resmed.refresh.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_UserProfile;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.activity.HomeActivity;
import com.resmed.refresh.ui.font.GroteskLightButton;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.CustomDialogBuilder;

public class EmailNotValidatedFragment
  extends BaseFragment
{
  private boolean busy = false;
  private Button greenBtn;
  private Button redBtn;
  private TextView tvErrorLogoutLink;
  private TextView tvErrorMessage;
  
  private void backToApp()
  {
    startActivity(new Intent(getActivity(), HomeActivity.class));
    getActivity().finish();
    getActivity().overridePendingTransition(2130968586, 2130968588);
  }
  
  private void checkAgain()
  {
    if (!this.busy)
    {
      this.busy = true;
      getBaseActivity().showProgressDialog(2131166103);
      RefreshModelController.getInstance().synchroniseLatestUserProfile(new RST_CallbackItem()
      {
        public void onResult(RST_Response<RST_UserProfile> paramAnonymousRST_Response)
        {
          EmailNotValidatedFragment.this.busy = false;
          if ((EmailNotValidatedFragment.this.getBaseActivity() == null) || (EmailNotValidatedFragment.this.getBaseActivity().isFinishing())) {}
          for (;;)
          {
            return;
            EmailNotValidatedFragment.this.getBaseActivity().dismissProgressDialog();
            if (EmailNotValidatedFragment.this.getBaseActivity().isAppValidated(paramAnonymousRST_Response.getErrorCode(), false)) {
              EmailNotValidatedFragment.this.backToApp();
            } else {
              new Handler().postDelayed(new Runnable()
              {
                public void run()
                {
                  EmailNotValidatedFragment.this.getBaseActivity().showDialog(new CustomDialogBuilder(EmailNotValidatedFragment.this.getBaseActivity()).title(2131166100).description(2131166104).setPositiveButton(2131166105, null));
                }
              }, 350L);
            }
          }
        }
      });
    }
  }
  
  private void mapGUI(View paramView)
  {
    this.greenBtn = ((GroteskLightButton)paramView.findViewById(2131099962));
    this.redBtn = ((GroteskLightButton)paramView.findViewById(2131099963));
    this.tvErrorMessage = ((TextView)paramView.findViewById(2131099959));
    this.tvErrorLogoutLink = ((TextView)paramView.findViewById(2131099960));
    this.tvErrorLogoutLink.setVisibility(0);
  }
  
  private void resendEmail()
  {
    if (!this.busy)
    {
      this.busy = true;
      getBaseActivity().showProgressDialog(2131165348);
      RefreshModelController.getInstance().serviceEmailVerification(new RST_CallbackItem()
      {
        public void onResult(RST_Response<Object> paramAnonymousRST_Response)
        {
          EmailNotValidatedFragment.this.busy = false;
          EmailNotValidatedFragment.this.getBaseActivity().showDialog(new CustomDialogBuilder(EmailNotValidatedFragment.this.getBaseActivity()).title(2131166100).description(2131166101).setPositiveButton(2131166102, null));
        }
      });
    }
  }
  
  private void setLabels()
  {
    this.greenBtn.setText(2131166097);
    this.redBtn.setText(2131166098);
    String str = getString(2131166096) + "\n\n" + RefreshModelController.getInstance().getUser().getEmail();
    this.tvErrorMessage.setText(str);
    this.tvErrorLogoutLink.setText(Html.fromHtml(getString(2131166099)));
    this.tvErrorLogoutLink.setPaintFlags(this.tvErrorLogoutLink.getPaintFlags() | 0x8);
  }
  
  private void setupListeners()
  {
    this.greenBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        EmailNotValidatedFragment.this.resendEmail();
      }
    });
    this.redBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        EmailNotValidatedFragment.this.checkAgain();
      }
    });
    this.tvErrorLogoutLink.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RefreshModelController.getInstance().userLogout();
        paramAnonymousView = new Intent(EmailNotValidatedFragment.this.getActivity(), HomeActivity.class);
        paramAnonymousView.setFlags(268468224);
        EmailNotValidatedFragment.this.startActivity(paramAnonymousView);
        EmailNotValidatedFragment.this.getActivity().overridePendingTransition(2130968582, 2130968583);
      }
    });
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903137, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setLabels();
    setupListeners();
    return paramLayoutInflater;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\EmailNotValidatedFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
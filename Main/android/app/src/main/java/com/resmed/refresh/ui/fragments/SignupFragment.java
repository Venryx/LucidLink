package com.resmed.refresh.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.MotionEventCompat;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_User;
import com.resmed.refresh.model.RST_UserProfile;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.LocaleMUnitResponse;
import com.resmed.refresh.ui.activity.SetupProfileActivity;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.CustomDialogBuilder;
import com.resmed.refresh.utils.RefreshTools;
import java.util.List;
import java.util.Locale;

public class SignupFragment
  extends BaseFragment
  implements View.OnClickListener
{
  private static final int MINIMUN_LENGTH_PASSWORD = 6;
  public static boolean isFromSignUp = false;
  public static boolean isStartedAppFromSignUp = false;
  private RST_CallbackItem<RST_Response<List<LocaleMUnitResponse>>> callbackLocaleMUnitMapping = new RST_CallbackItem()
  {
    public void onResult(RST_Response<List<LocaleMUnitResponse>> paramAnonymousRST_Response)
    {
      if ((SignupFragment.this.getBaseActivity() == null) || (SignupFragment.this.getBaseActivity().isFinishing()) || (!SignupFragment.this.getBaseActivity().isAppValidated(paramAnonymousRST_Response.getErrorCode()))) {}
      for (;;)
      {
        return;
        SignupFragment.this.getBaseActivity().dismissProgressDialog();
        if (paramAnonymousRST_Response.isStatus())
        {
          Locale localLocale = SignupFragment.this.getResources().getConfiguration().locale;
          RefreshApplication.userCountry = localLocale.getCountry();
          paramAnonymousRST_Response = (List)paramAnonymousRST_Response.getResponse();
          int j;
          int i;
          if ((paramAnonymousRST_Response != null) && (paramAnonymousRST_Response.size() > 0))
          {
            j = 0;
            i = 0;
            label106:
            if (i < paramAnonymousRST_Response.size()) {
              break label356;
            }
            i = j;
            label118:
            if (i == 0)
            {
              i = 0;
              label124:
              if (i < paramAnonymousRST_Response.size()) {
                break label407;
              }
            }
          }
          label134:
          RefreshModelController.getInstance().getUser().getSettings().setCountryCode(localLocale.getCountry());
          RefreshModelController.getInstance().getUser().getSettings().setLocale(localLocale.toString());
          RefreshModelController.getInstance().getUser().getSettings().setHeightUnit(RefreshApplication.userMeasurementUnitMappingObj.getHeightUnit());
          RefreshModelController.getInstance().getUser().getSettings().setWeightUnit(RefreshApplication.userMeasurementUnitMappingObj.getWeightUnit());
          RefreshModelController.getInstance().getUser().getSettings().setTemperatureUnit(RefreshApplication.userMeasurementUnitMappingObj.getTemperatureUnit());
          paramAnonymousRST_Response = RefreshModelController.getInstance().getUser().getSettings();
          if (RefreshApplication.userMeasurementUnitMappingObj.getTemperatureUnit() == 1) {}
          for (boolean bool = true;; bool = false)
          {
            paramAnonymousRST_Response.setUseMetricUnits(bool);
            RefreshModelController.getInstance().getUser().getProfile().setHeightUnit(RefreshApplication.userMeasurementUnitMappingObj.getHeightUnit());
            RefreshModelController.getInstance().getUser().getProfile().setWeightUnit(RefreshApplication.userMeasurementUnitMappingObj.getWeightUnit());
            RefreshModelController.getInstance().getUser().getProfile().setTemperatureUnit(RefreshApplication.userMeasurementUnitMappingObj.getTemperatureUnit());
            SignupFragment.this.getBaseActivity().showDialog(new CustomDialogBuilder(SignupFragment.this.getBaseActivity()).title(2131166100).description(2131166101).setPositiveButton(2131166102, new View.OnClickListener()
            {
              public void onClick(View paramAnonymous2View)
              {
                paramAnonymous2View = new Intent(SignupFragment.this.getActivity(), SetupProfileActivity.class);
                SignupFragment.this.startActivity(paramAnonymous2View);
              }
            }), false);
            break;
            label356:
            if (((LocaleMUnitResponse)paramAnonymousRST_Response.get(i)).getLocaleCountry().trim().equalsIgnoreCase(RefreshApplication.userCountry))
            {
              j = 1;
              RefreshApplication.userMeasurementUnitMappingObj = (LocaleMUnitResponse)paramAnonymousRST_Response.get(i);
              i = j;
              break label118;
            }
            i++;
            break label106;
            label407:
            if (((LocaleMUnitResponse)paramAnonymousRST_Response.get(i)).getLocaleCountry().trim().equalsIgnoreCase("DEFAULT"))
            {
              RefreshApplication.userMeasurementUnitMappingObj = (LocaleMUnitResponse)paramAnonymousRST_Response.get(i);
              break label134;
            }
            i++;
            break label124;
          }
        }
        SignupFragment.this.getBaseActivity().showDialog(new CustomDialogBuilder(SignupFragment.this.getBaseActivity()).title(2131166100).description(2131166101).setPositiveButton(2131166102, new View.OnClickListener()
        {
          public void onClick(View paramAnonymous2View)
          {
            paramAnonymous2View = new Intent(SignupFragment.this.getActivity(), SetupProfileActivity.class);
            SignupFragment.this.startActivity(paramAnonymous2View);
          }
        }), false);
      }
    }
  };
  private RST_CallbackItem<RST_Response<RST_User>> callbackSignup = new RST_CallbackItem()
  {
    public void onResult(RST_Response<RST_User> paramAnonymousRST_Response)
    {
      if ((SignupFragment.this.getBaseActivity() == null) || (SignupFragment.this.getBaseActivity().isFinishing()) || (!SignupFragment.this.getBaseActivity().isAppValidated(paramAnonymousRST_Response.getErrorCode()))) {}
      for (;;)
      {
        return;
        if (paramAnonymousRST_Response.isStatus()) {
          RefreshModelController.getInstance().updateUserProfile(SignupFragment.this.callbackUpdateProfile);
        } else {
          SignupFragment.this.getBaseActivity().showErrorDialog(paramAnonymousRST_Response);
        }
      }
    }
  };
  private RST_CallbackItem<RST_Response<RST_UserProfile>> callbackUpdateProfile = new RST_CallbackItem()
  {
    public void onResult(RST_Response<RST_UserProfile> paramAnonymousRST_Response)
    {
      if ((SignupFragment.this.getBaseActivity() == null) || (SignupFragment.this.getBaseActivity().isFinishing()) || (!SignupFragment.this.getBaseActivity().isAppValidated(paramAnonymousRST_Response.getErrorCode()))) {}
      for (;;)
      {
        return;
        if (paramAnonymousRST_Response.isStatus()) {
          RefreshModelController.getInstance().getLocaleMUnitMapping(SignupFragment.this.callbackLocaleMUnitMapping);
        } else {
          SignupFragment.this.getBaseActivity().showErrorDialog(paramAnonymousRST_Response);
        }
      }
    }
  };
  private CheckBox cbSignupTerms;
  private Button signupButton;
  private EditText signupEmailEditText;
  private EditText signupFamilyNameEditText;
  private EditText signupFirstNameEditText;
  private EditText signupPasswordEditText;
  private WebView wvSignupTerms;
  
  private void mapGUI(View paramView)
  {
    this.signupFirstNameEditText = ((EditText)paramView.findViewById(2131100362));
    this.signupFamilyNameEditText = ((EditText)paramView.findViewById(2131100363));
    this.wvSignupTerms = ((WebView)paramView.findViewById(2131100367));
    setupWebView();
    this.signupEmailEditText = ((EditText)paramView.findViewById(2131100364));
    this.signupPasswordEditText = ((EditText)paramView.findViewById(2131100365));
    this.signupButton = ((Button)paramView.findViewById(2131100368));
    this.cbSignupTerms = ((CheckBox)paramView.findViewById(2131100366));
    this.cbSignupTerms.setMovementMethod(LinkMovementMethod.getInstance());
    this.signupFirstNameEditText.setTypeface(this.akzidenzLight);
    this.signupFamilyNameEditText.setTypeface(this.akzidenzLight);
    this.signupEmailEditText.setTypeface(this.akzidenzLight);
    this.signupPasswordEditText.setTypeface(this.akzidenzLight);
    this.cbSignupTerms.setTypeface(this.akzidenzLight);
    if (Build.VERSION.SDK_INT < 17) {
      this.wvSignupTerms.setOnTouchListener(new View.OnTouchListener()
      {
        public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
        {
          int i = MotionEventCompat.getActionMasked(paramAnonymousMotionEvent);
          if ((i == 3) || (i == 1)) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
            {
              public void run()
              {
                SignupFragment.this.setupWebView();
              }
            }, 500L);
          }
          return false;
        }
      });
    }
  }
  
  private void setupListeners()
  {
    this.signupButton.setOnClickListener(this);
    this.signupPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
    {
      public boolean onEditorAction(TextView paramAnonymousTextView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
      {
        boolean bool = false;
        if ((paramAnonymousInt == 4) || (paramAnonymousInt == 6))
        {
          bool = true;
          RefreshTools.hideKeyBoard(SignupFragment.this.getActivity());
        }
        return bool;
      }
    });
  }
  
  private void setupWebView()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<HTML><HEAD><LINK href=\"terms.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
    localStringBuilder.append(getString(2131165456));
    localStringBuilder.append(" <a href=\"" + com.resmed.refresh.ui.utils.Consts.REFRESH_URL[1] + "\" >" + getString(2131165457) + "</a> ");
    localStringBuilder.append(getString(2131165458));
    localStringBuilder.append(" <a href=\"" + com.resmed.refresh.ui.utils.Consts.REFRESH_URL[2] + "\" >" + getString(2131165459) + "</a> ");
    localStringBuilder.append(getString(2131165460));
    localStringBuilder.append("</body></HTML>");
    this.wvSignupTerms.loadDataWithBaseURL("file:///android_asset/", localStringBuilder.toString(), "text/html", "utf-8", null);
    this.wvSignupTerms.setBackgroundColor(0);
  }
  
  private void signup()
  {
    if (!RefreshTools.validateEmail(this.signupEmailEditText.getText().toString())) {
      getBaseActivity().showErrorDialog(getString(2131165437));
    }
    for (;;)
    {
      return;
      if (!validatePassword(this.signupPasswordEditText.getText().toString()))
      {
        getBaseActivity().showErrorDialog(getString(2131165438));
      }
      else if (!this.cbSignupTerms.isChecked())
      {
        getBaseActivity().showErrorDialog(getString(2131165461));
      }
      else if ((this.signupFirstNameEditText.getText().length() == 0) || (this.signupFamilyNameEditText.getText().length() == 0))
      {
        getBaseActivity().showErrorDialog(getString(2131165455));
      }
      else if (!this.cbSignupTerms.isChecked())
      {
        getBaseActivity().showErrorDialog(getString(2131165461));
      }
      else
      {
        String str = getString(2131165453) + "\n\n" + this.signupEmailEditText.getText().toString();
        getBaseActivity().showDialog(new CustomDialogBuilder(getBaseActivity()).title(2131165452).description(str).setNegativeButton(2131165299, null).setPositiveButton(2131165298, new View.OnClickListener()
        {
          public void onClick(View paramAnonymousView)
          {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
            {
              public void run()
              {
                SignupFragment.this.getBaseActivity().showProgressDialog(2131165348);
                RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
                localRefreshModelController.userLogout();
                localRefreshModelController.userRegister(SignupFragment.this.signupEmailEditText.getText().toString(), SignupFragment.this.signupPasswordEditText.getText().toString(), SignupFragment.this.signupFirstNameEditText.getText().toString(), SignupFragment.this.signupFamilyNameEditText.getText().toString(), SignupFragment.this.callbackSignup);
              }
            }, 500L);
          }
        }));
      }
    }
  }
  
  private boolean validatePassword(String paramString)
  {
    return paramString.matches("((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,})");
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
      signup();
      isFromSignUp = true;
      isStartedAppFromSignUp = true;
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903163, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    return paramLayoutInflater;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SignupFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
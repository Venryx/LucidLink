package com.resmed.refresh.model.services;

import android.os.AsyncTask;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_UserProfile;
import com.resmed.refresh.model.json.UserProfile;
import com.resmed.refresh.model.json.UserResponse;
import com.resmed.refresh.model.mappers.UserProfileMapper;
import com.resmed.refresh.net.ResMedServerAPI;
import com.resmed.refresh.net.callbacks.HttpDefaultCallback;
import com.resmed.refresh.ui.fragments.LoginFragment;
import com.resmed.refresh.ui.fragments.SignupFragment;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.Log;

import org.apache.http.HttpResponse;

public class RST_ServiceUserProfile
{
  private RST_CallbackItem<RST_Response<RST_UserProfile>> callback;
  private ResMedServerAPI resMedServerAPI;
  private UserProfile userProfile;
  
  public RST_ServiceUserProfile(ResMedServerAPI paramResMedServerAPI)
  {
    this.resMedServerAPI = paramResMedServerAPI;
  }
  
  public void getUserProfile(RST_CallbackItem<RST_Response<RST_UserProfile>> paramRST_CallbackItem)
  {
    this.callback = paramRST_CallbackItem;
    this.resMedServerAPI.requestProfile(new UserProfileCallback(UserResponse.class));
  }
  
  public void updateUserProfile(UserProfile paramUserProfile, RST_CallbackItem<RST_Response<RST_UserProfile>> paramRST_CallbackItem)
  {
    this.callback = paramRST_CallbackItem;
    this.userProfile = paramUserProfile;
    System.out.println("##### user W H T ::" + paramUserProfile.getWeightUnit() + "::" + paramUserProfile.getHeightUnit() + ":: " + paramUserProfile.getTemperatureUnit());
    this.resMedServerAPI.updateProfile(paramUserProfile, new UserProfileUpdateCallback(UserResponse.class));
  }
  
  private class AsyncServiceGetUserProfile
    extends AsyncTask<RST_ServiceUserProfile.UserProfileCallback, Void, RST_Response<RST_UserProfile>>
  {
    private AsyncServiceGetUserProfile() {}
    
    protected RST_Response<RST_UserProfile> doInBackground(RST_ServiceUserProfile.UserProfileCallback... paramVarArgs)
    {
      Object localObject = paramVarArgs[0];
      paramVarArgs = (UserResponse)((RST_ServiceUserProfile.UserProfileCallback)localObject).getResult();
      UserProfile localUserProfile = paramVarArgs.getUserProfile();
      RST_Response localRST_Response = new RST_Response();
      if ((paramVarArgs != null) && (paramVarArgs.getUserId() != null))
      {
        localObject = UserProfileMapper.processUserProfile(localUserProfile);
        localRST_Response.setStatus(true);
        localRST_Response.setResponse(localObject);
        Log.d("com.resmed.refresh.model", "Get user profile for user " + paramVarArgs.getUserId() + " received");
      }
      for (;;)
      {
        return localRST_Response;
        localRST_Response.setStatus(false);
        localRST_Response.setErrorCode(((RST_ServiceUserProfile.UserProfileCallback)localObject).getError().getErrorCode());
        localRST_Response.setErrorMessage(((RST_ServiceUserProfile.UserProfileCallback)localObject).getError().getErrorMessage());
      }
    }
    
    protected void onPostExecute(RST_Response<RST_UserProfile> paramRST_Response)
    {
      super.onPostExecute(paramRST_Response);
      RST_ServiceUserProfile.this.callback.onResult(paramRST_Response);
    }
  }
  
  private class AsyncServiceUpdateUserProfile
    extends AsyncTask<RST_ServiceUserProfile.UserProfileUpdateCallback, Void, RST_Response<RST_UserProfile>>
  {
    private AsyncServiceUpdateUserProfile() {}
    
    protected RST_Response<RST_UserProfile> doInBackground(RST_ServiceUserProfile.UserProfileUpdateCallback... paramVarArgs)
    {
      Object localObject = paramVarArgs[0];
      paramVarArgs = new RST_Response();
      if (((RST_ServiceUserProfile.UserProfileUpdateCallback)localObject).getError() == null) {}
      for (;;)
      {
        try
        {
          if ((SignupFragment.isFromSignUp) || (LoginFragment.isFromLogin))
          {
            SignupFragment.isFromSignUp = false;
            LoginFragment.isFromLogin = false;
            localObject = UserProfileMapper.processUserProfile(RST_ServiceUserProfile.this.userProfile);
            paramVarArgs.setStatus(true);
            paramVarArgs.setResponse(localObject);
            Log.d("com.resmed.refresh.model", "updated profile for user");
          }
        }
        catch (Exception localException)
        {
          paramVarArgs.setStatus(false);
          paramVarArgs.setErrorCode(500);
          paramVarArgs.setErrorMessage(RefreshApplication.getInstance().getString(2131165352));
          continue;
        }
        return paramVarArgs;
        paramVarArgs.setStatus(false);
        paramVarArgs.setErrorCode(localException.getError().getErrorCode());
        paramVarArgs.setErrorMessage(localException.getError().getErrorMessage());
      }
    }
    
    protected void onPostExecute(RST_Response<RST_UserProfile> paramRST_Response)
    {
      super.onPostExecute(paramRST_Response);
      RST_ServiceUserProfile.this.callback.onResult(paramRST_Response);
    }
  }
  
  private class UserProfileCallback
    extends HttpDefaultCallback<UserResponse>
  {
    public UserProfileCallback()
    {
      super();
    }
    
    public void handleResult(HttpResponse paramHttpResponse)
    {
      super.handleResult(paramHttpResponse);
      new RST_ServiceUserProfile.AsyncServiceGetUserProfile(RST_ServiceUserProfile.this, null).execute(new UserProfileCallback[] { this });
    }
  }
  
  private class UserProfileUpdateCallback
    extends HttpDefaultCallback<UserResponse>
  {
    public UserProfileUpdateCallback()
    {
      super();
    }
    
    public void handleResult(HttpResponse paramHttpResponse)
    {
      super.handleResult(paramHttpResponse);
      new RST_ServiceUserProfile.AsyncServiceUpdateUserProfile(RST_ServiceUserProfile.this, null).execute(new UserProfileUpdateCallback[] { this });
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
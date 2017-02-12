package com.resmed.refresh.model.services;

import android.os.AsyncTask;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_User;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.UserResponse;
import com.resmed.refresh.net.ResMedServerAPI;
import com.resmed.refresh.net.callbacks.HttpDefaultCallback;
import com.resmed.refresh.utils.Log;
import org.apache.http.HttpResponse;

public class RST_ServiceLogin
{
  private RST_CallbackItem<RST_Response<RST_User>> callback;
  private String email;
  private boolean isLogin;
  private String password;
  private ResMedServerAPI resMedServerAPI;
  
  public RST_ServiceLogin(ResMedServerAPI paramResMedServerAPI)
  {
    this.resMedServerAPI = paramResMedServerAPI;
  }
  
  public void userForgotPassword(String paramString, RST_CallbackItem<RST_Response<RST_User>> paramRST_CallbackItem)
  {
    this.callback = paramRST_CallbackItem;
    this.email = paramString;
    this.isLogin = false;
    this.resMedServerAPI.forgotPassword(paramString, new UserProfileCallback());
  }
  
  public void userLogin(String paramString1, String paramString2, RST_CallbackItem<RST_Response<RST_User>> paramRST_CallbackItem)
  {
    this.callback = paramRST_CallbackItem;
    this.email = paramString1;
    this.password = paramString2;
    this.isLogin = true;
    this.resMedServerAPI.login(paramString1, paramString2, new UserProfileCallback());
  }
  
  private class AsyncServiceForgotPassword
    extends AsyncTask<RST_ServiceLogin.UserProfileCallback, Void, RST_Response<RST_User>>
  {
    private AsyncServiceForgotPassword() {}
    
    protected RST_Response<RST_User> doInBackground(RST_ServiceLogin.UserProfileCallback... paramVarArgs)
    {
      RST_ServiceLogin.UserProfileCallback localUserProfileCallback = paramVarArgs[0];
      paramVarArgs = new RST_Response();
      if (localUserProfileCallback.getError() == null) {
        paramVarArgs.setStatus(true);
      }
      for (;;)
      {
        return paramVarArgs;
        paramVarArgs.setStatus(false);
        paramVarArgs.setErrorCode(localUserProfileCallback.getError().getErrorCode());
        paramVarArgs.setErrorMessage(localUserProfileCallback.getError().getErrorMessage());
      }
    }
    
    protected void onPostExecute(RST_Response<RST_User> paramRST_Response)
    {
      super.onPostExecute(paramRST_Response);
      RST_ServiceLogin.this.callback.onResult(paramRST_Response);
    }
  }
  
  private class AsyncServiceLogin
    extends AsyncTask<RST_ServiceLogin.UserProfileCallback, Void, RST_Response<RST_User>>
  {
    private AsyncServiceLogin() {}
    
    protected RST_Response<RST_User> doInBackground(RST_ServiceLogin.UserProfileCallback... paramVarArgs)
    {
      Object localObject = paramVarArgs[0];
      UserResponse localUserResponse = (UserResponse)((RST_ServiceLogin.UserProfileCallback)localObject).getResult();
      paramVarArgs = new RST_Response();
      if ((localUserResponse != null) && (localUserResponse.getUserId() != null))
      {
        localObject = RefreshModelController.getInstance();
        if (((RefreshModelController)localObject).localUserDataForId(localUserResponse.getUserId()) != null) {
          localUserResponse.setUserProfile(null);
        }
        localObject = ((RefreshModelController)localObject).access(RST_ServiceLogin.this.email, RST_ServiceLogin.this.password, localUserResponse);
        Log.d("com.resmed.refresh.model", "Login new userid = " + localUserResponse.getUserId());
        paramVarArgs.setStatus(true);
        paramVarArgs.setResponse(localObject);
      }
      for (;;)
      {
        return paramVarArgs;
        paramVarArgs.setStatus(false);
        paramVarArgs.setErrorCode(((RST_ServiceLogin.UserProfileCallback)localObject).getError().getErrorCode());
        paramVarArgs.setErrorMessage(((RST_ServiceLogin.UserProfileCallback)localObject).getError().getErrorMessage());
      }
    }
    
    protected void onPostExecute(RST_Response<RST_User> paramRST_Response)
    {
      super.onPostExecute(paramRST_Response);
      RST_ServiceLogin.this.callback.onResult(paramRST_Response);
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
      if (RST_ServiceLogin.this.isLogin) {
        new RST_ServiceLogin.AsyncServiceLogin(RST_ServiceLogin.this, null).execute(new UserProfileCallback[] { this });
      }
      for (;;)
      {
        return;
        new RST_ServiceLogin.AsyncServiceForgotPassword(RST_ServiceLogin.this, null).execute(new UserProfileCallback[] { this });
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\services\RST_ServiceLogin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
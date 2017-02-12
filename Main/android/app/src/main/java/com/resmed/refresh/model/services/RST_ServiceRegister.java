package com.resmed.refresh.model.services;

import android.os.AsyncTask;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_User;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.User;
import com.resmed.refresh.model.json.UserResponse;
import com.resmed.refresh.net.ResMedServerAPI;
import com.resmed.refresh.net.callbacks.HttpDefaultCallback;
import com.resmed.refresh.utils.Log;
import org.apache.http.HttpResponse;

public class RST_ServiceRegister
{
  private RST_CallbackItem<RST_Response<RST_User>> callback;
  private ResMedServerAPI resMedServerAPI;
  private User userRequest;
  
  public RST_ServiceRegister(ResMedServerAPI paramResMedServerAPI)
  {
    this.resMedServerAPI = paramResMedServerAPI;
  }
  
  public void userRegister(User paramUser, RST_CallbackItem<RST_Response<RST_User>> paramRST_CallbackItem)
  {
    this.userRequest = paramUser;
    this.callback = paramRST_CallbackItem;
    this.resMedServerAPI.registerUser(paramUser, new UserProfileCallback());
  }
  
  private class AsyncServiceRegister
    extends AsyncTask<RST_ServiceRegister.UserProfileCallback, Void, RST_Response<RST_User>>
  {
    private AsyncServiceRegister() {}
    
    protected RST_Response<RST_User> doInBackground(RST_ServiceRegister.UserProfileCallback... paramVarArgs)
    {
      Object localObject = paramVarArgs[0];
      UserResponse localUserResponse = (UserResponse)((RST_ServiceRegister.UserProfileCallback)localObject).getResult();
      paramVarArgs = new RST_Response();
      if ((localUserResponse != null) && (localUserResponse.getUserId() != null))
      {
        localObject = RefreshModelController.getInstance().access(RST_ServiceRegister.this.userRequest.getFirstName(), RST_ServiceRegister.this.userRequest.getLastName(), RST_ServiceRegister.this.userRequest.getEmail(), RST_ServiceRegister.this.userRequest.getPassword(), localUserResponse);
        paramVarArgs.setStatus(true);
        paramVarArgs.setResponse(localObject);
        Log.d("com.resmed.refresh.model", "Register new userid = " + localUserResponse.getUserId());
      }
      for (;;)
      {
        return paramVarArgs;
        paramVarArgs.setStatus(false);
        paramVarArgs.setErrorCode(((RST_ServiceRegister.UserProfileCallback)localObject).getError().getErrorCode());
        paramVarArgs.setErrorMessage(((RST_ServiceRegister.UserProfileCallback)localObject).getError().getErrorMessage());
      }
    }
    
    protected void onPostExecute(RST_Response<RST_User> paramRST_Response)
    {
      super.onPostExecute(paramRST_Response);
      if (RST_ServiceRegister.this.callback != null) {
        RST_ServiceRegister.this.callback.onResult(paramRST_Response);
      }
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
      new RST_ServiceRegister.AsyncServiceRegister(RST_ServiceRegister.this, null).execute(new UserProfileCallback[] { this });
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
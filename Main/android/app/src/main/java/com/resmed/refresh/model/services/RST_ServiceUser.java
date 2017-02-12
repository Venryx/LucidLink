package com.resmed.refresh.model.services;

import android.os.AsyncTask;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_User;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.User;
import com.resmed.refresh.model.mappers.UserMapper;
import com.resmed.refresh.net.ResMedServerAPI;
import com.resmed.refresh.net.callbacks.HttpDefaultCallback;
import com.resmed.refresh.utils.Log;

import org.apache.http.HttpResponse;

public class RST_ServiceUser
{
  private RST_CallbackItem<RST_Response<RST_User>> callback;
  private ResMedServerAPI resMedServerAPI;
  
  public RST_ServiceUser(ResMedServerAPI paramResMedServerAPI)
  {
    this.resMedServerAPI = paramResMedServerAPI;
  }
  
  public void updateUserData(User paramUser, RST_CallbackItem<RST_Response<RST_User>> paramRST_CallbackItem)
  {
    this.callback = paramRST_CallbackItem;
    this.resMedServerAPI.updateUser(paramUser, new UserCallback(User.class));
  }
  
  private class AsyncServiceUser
    extends AsyncTask<RST_ServiceUser.UserCallback, Void, RST_Response<RST_User>>
  {
    private AsyncServiceUser() {}
    
    protected RST_Response<RST_User> doInBackground(RST_ServiceUser.UserCallback... paramVarArgs)
    {
      Object localObject = paramVarArgs[0];
      paramVarArgs = (User)((RST_ServiceUser.UserCallback)localObject).getResult();
      RST_Response localRST_Response = new RST_Response();
      if (paramVarArgs != null)
      {
        localObject = RefreshModelController.getInstance();
        UserMapper.updateRST_User(((RefreshModelController)localObject).getUser(), paramVarArgs);
        ((RefreshModelController)localObject).save();
        localRST_Response.setStatus(true);
        localRST_Response.setResponse(((RefreshModelController)localObject).getUser());
        Log.d("com.resmed.refresh.model", "Get user data userid = " + paramVarArgs.getUserId());
      }
      for (;;)
      {
        return localRST_Response;
        localRST_Response.setStatus(false);
        localRST_Response.setErrorCode(((RST_ServiceUser.UserCallback)localObject).getError().getErrorCode());
        localRST_Response.setErrorMessage(((RST_ServiceUser.UserCallback)localObject).getError().getErrorMessage());
      }
    }
    
    protected void onPostExecute(RST_Response<RST_User> paramRST_Response)
    {
      super.onPostExecute(paramRST_Response);
      RST_ServiceUser.this.callback.onResult(paramRST_Response);
    }
  }
  
  private class UserCallback
    extends HttpDefaultCallback<User>
  {
    public UserCallback()
    {
      super();
    }
    
    public void handleResult(HttpResponse paramHttpResponse)
    {
      super.handleResult(paramHttpResponse);
      new RST_ServiceUser.AsyncServiceUser(RST_ServiceUser.this, null).execute(new UserCallback[] { this });
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.model.services;

import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.net.ResMedServerAPI;
import com.resmed.refresh.net.callbacks.HttpDefaultCallback;
import com.resmed.refresh.utils.Log;

import org.apache.http.HttpResponse;

public class RST_ServicePushNotifications
{
  private ResMedServerAPI resMedServerAPI;
  
  public RST_ServicePushNotifications(ResMedServerAPI paramResMedServerAPI)
  {
    this.resMedServerAPI = paramResMedServerAPI;
  }
  
  public void registerPushToken(String paramString1, String paramString2)
  {
    this.resMedServerAPI.updateDevicePushToken(paramString1, paramString2, new HttpRegisterTokenCallback(Object.class));
  }
  
  private class HttpRegisterTokenCallback
    extends HttpDefaultCallback<Object>
  {
    public HttpRegisterTokenCallback()
    {
      super();
    }
    
    public void handleResult(HttpResponse paramHttpResponse)
    {
      super.handleResult(paramHttpResponse);
      paramHttpResponse = RefreshModelController.getInstance();
      if (getError() == null)
      {
        Log.d("com.resmed.refresh.model", " http registerPushToken Callback OK ");
        paramHttpResponse.setGMCTokenStatus(true);
      }
      for (;;)
      {
        return;
        Log.d("com.resmed.refresh.model", " http registerPushToken Callback ERROR ");
        paramHttpResponse.setGMCTokenStatus(false);
      }
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
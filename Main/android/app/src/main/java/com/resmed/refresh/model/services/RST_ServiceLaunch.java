package com.resmed.refresh.model.services;

import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.LaunchData;
import com.resmed.refresh.net.ResMedServerAPI;
import com.resmed.refresh.net.callbacks.HttpDefaultCallback;
import com.resmed.refresh.utils.Log;

import org.apache.http.HttpResponse;

public class RST_ServiceLaunch
{
  private ResMedServerAPI resMedServerAPI;
  
  public RST_ServiceLaunch(ResMedServerAPI paramResMedServerAPI)
  {
    this.resMedServerAPI = paramResMedServerAPI;
  }
  
  public void userAppLaunch(String paramString, LaunchData paramLaunchData)
  {
    this.resMedServerAPI.userAppLaunch(paramString, paramLaunchData, new HttpLaunchAppCallback(LaunchData.class));
  }
  
  private class HttpLaunchAppCallback
    extends HttpDefaultCallback<LaunchData>
  {
    public HttpLaunchAppCallback()
    {
      super();
    }
    
    public void handleResult(HttpResponse paramHttpResponse)
    {
      super.handleResult(paramHttpResponse);
      paramHttpResponse = RefreshModelController.getInstance();
      if (getError() == null)
      {
        Log.d("com.resmed.refresh.model", " http userAppLaunch Callback OK ");
        paramHttpResponse.setServiceDidLaunch(true);
      }
      for (;;)
      {
        return;
        Log.d("com.resmed.refresh.model", " http userAppLaunch Callback ERROR ");
        paramHttpResponse.setServiceDidLaunch(false);
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\services\RST_ServiceLaunch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.model.services;

import android.os.AsyncTask;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.json.LocaleMUnitResponse;
import com.resmed.refresh.net.ResMedServerAPI;
import com.resmed.refresh.net.callbacks.HttpDefaultCallback;

import java.util.List;
import org.apache.http.HttpResponse;

public class RST_ServiceLocaleMUnit
{
  private RST_CallbackItem<RST_Response<List<LocaleMUnitResponse>>> callback;
  private ResMedServerAPI resMedServerAPI;
  
  public RST_ServiceLocaleMUnit(ResMedServerAPI paramResMedServerAPI)
  {
    this.resMedServerAPI = paramResMedServerAPI;
  }
  
  public void getlocaleMUnitMapping(RST_CallbackItem<RST_Response<List<LocaleMUnitResponse>>> paramRST_CallbackItem)
  {
    this.callback = paramRST_CallbackItem;
    this.resMedServerAPI.localeMUnitMapping(new LocaleMUnitCallback());
  }
  
  private class AsyncServiceLocaleMUnit
    extends AsyncTask<RST_ServiceLocaleMUnit.LocaleMUnitCallback, Void, RST_Response<List<LocaleMUnitResponse>>>
  {
    private AsyncServiceLocaleMUnit() {}
    
    protected RST_Response<List<LocaleMUnitResponse>> doInBackground(RST_ServiceLocaleMUnit.LocaleMUnitCallback... paramVarArgs)
    {
      Object localObject = paramVarArgs[0];
      paramVarArgs = new RST_Response();
      if ((((RST_ServiceLocaleMUnit.LocaleMUnitCallback)localObject).getResult() != null) && ((((RST_ServiceLocaleMUnit.LocaleMUnitCallback)localObject).getResult() instanceof List)))
      {
        localObject = (List)((RST_ServiceLocaleMUnit.LocaleMUnitCallback)localObject).getResult();
        paramVarArgs.setStatus(true);
        paramVarArgs.setResponse(localObject);
      }
      for (;;)
      {
        return paramVarArgs;
        paramVarArgs.setStatus(false);
        paramVarArgs.setErrorCode(((RST_ServiceLocaleMUnit.LocaleMUnitCallback)localObject).getError().getErrorCode());
        paramVarArgs.setErrorMessage(((RST_ServiceLocaleMUnit.LocaleMUnitCallback)localObject).getError().getErrorMessage());
      }
    }
    
    protected void onPostExecute(RST_Response<List<LocaleMUnitResponse>> paramRST_Response)
    {
      super.onPostExecute(paramRST_Response);
      if (RST_ServiceLocaleMUnit.this.callback != null) {
        RST_ServiceLocaleMUnit.this.callback.onResult(paramRST_Response);
      }
    }
  }
  
  private class LocaleMUnitCallback
    extends HttpDefaultCallback<LocaleMUnitResponse>
  {
    public LocaleMUnitCallback()
    {
      super(true);
    }
    
    public void handleResult(HttpResponse paramHttpResponse)
    {
      super.handleResult(paramHttpResponse);
      new RST_ServiceLocaleMUnit.AsyncServiceLocaleMUnit(RST_ServiceLocaleMUnit.this, null).execute(new LocaleMUnitCallback[] { this });
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\services\RST_ServiceLocaleMUnit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
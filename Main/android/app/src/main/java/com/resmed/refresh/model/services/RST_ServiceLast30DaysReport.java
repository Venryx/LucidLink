package com.resmed.refresh.model.services;

import android.os.AsyncTask;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.json.Last30DaysReportResponse;
import com.resmed.refresh.net.ResMedServerAPI;
import com.resmed.refresh.net.callbacks.HttpDefaultCallback;

import org.apache.http.HttpResponse;

public class RST_ServiceLast30DaysReport
{
  private RST_CallbackItem<RST_Response<Last30DaysReportResponse>> callback;
  private ResMedServerAPI resMedServerAPI;
  
  public RST_ServiceLast30DaysReport(ResMedServerAPI paramResMedServerAPI)
  {
    this.resMedServerAPI = paramResMedServerAPI;
  }
  
  public void getLast30DaysReport(String paramString1, String paramString2, boolean paramBoolean, RST_CallbackItem<RST_Response<Last30DaysReportResponse>> paramRST_CallbackItem)
  {
    this.callback = paramRST_CallbackItem;
    paramString1 = new Last30DaysReportResponse(paramString1, paramString2, paramBoolean);
    this.resMedServerAPI.last30DaysReport(paramString1, new Last30DaysReportCallback());
  }
  
  private class AsyncServiceLast30DaysReport
    extends AsyncTask<RST_ServiceLast30DaysReport.Last30DaysReportCallback, Void, RST_Response<Last30DaysReportResponse>>
  {
    private AsyncServiceLast30DaysReport() {}
    
    protected RST_Response<Last30DaysReportResponse> doInBackground(RST_ServiceLast30DaysReport.Last30DaysReportCallback... paramVarArgs)
    {
      Object localObject = paramVarArgs[0];
      paramVarArgs = new RST_Response();
      if (((RST_ServiceLast30DaysReport.Last30DaysReportCallback)localObject).getResult() != null)
      {
        localObject = (Last30DaysReportResponse)((RST_ServiceLast30DaysReport.Last30DaysReportCallback)localObject).getResult();
        paramVarArgs.setStatus(true);
        paramVarArgs.setResponse(localObject);
      }
      for (;;)
      {
        return paramVarArgs;
        paramVarArgs.setStatus(false);
        paramVarArgs.setErrorCode(((RST_ServiceLast30DaysReport.Last30DaysReportCallback)localObject).getError().getErrorCode());
        paramVarArgs.setErrorMessage(((RST_ServiceLast30DaysReport.Last30DaysReportCallback)localObject).getError().getErrorMessage());
      }
    }
    
    protected void onPostExecute(RST_Response<Last30DaysReportResponse> paramRST_Response)
    {
      super.onPostExecute(paramRST_Response);
      if (RST_ServiceLast30DaysReport.this.callback != null) {
        RST_ServiceLast30DaysReport.this.callback.onResult(paramRST_Response);
      }
    }
  }
  
  private class Last30DaysReportCallback
    extends HttpDefaultCallback<Last30DaysReportResponse>
  {
    public Last30DaysReportCallback()
    {
      super(true);
    }
    
    public void handleResult(HttpResponse paramHttpResponse)
    {
      super.handleResult(paramHttpResponse);
      new RST_ServiceLast30DaysReport.AsyncServiceLast30DaysReport(RST_ServiceLast30DaysReport.this, null).execute(new Last30DaysReportCallback[] { this });
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\services\RST_ServiceLast30DaysReport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
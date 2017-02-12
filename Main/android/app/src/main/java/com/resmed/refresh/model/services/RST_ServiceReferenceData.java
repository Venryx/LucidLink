package com.resmed.refresh.model.services;

import android.os.AsyncTask;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_NightQuestions;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.NightQuestions;
import com.resmed.refresh.model.mappers.NightQuestionsMapper;
import com.resmed.refresh.net.ResMedServerAPI;
import com.resmed.refresh.net.callbacks.HttpDefaultCallback;

import java.util.Date;
import org.apache.http.HttpResponse;

public class RST_ServiceReferenceData
{
  private RST_CallbackItem<RST_Response<RST_NightQuestions>> callback;
  private ResMedServerAPI resMedServerAPI;
  
  public RST_ServiceReferenceData(ResMedServerAPI paramResMedServerAPI)
  {
    this.resMedServerAPI = paramResMedServerAPI;
  }
  
  public void getPreSleepQuestions(RST_CallbackItem<RST_Response<RST_NightQuestions>> paramRST_CallbackItem)
  {
    this.callback = paramRST_CallbackItem;
    this.resMedServerAPI.requestPreSleepQuestions(new SleepQuestionsCallback(NightQuestions.class));
  }
  
  private class AsyncSleepQuestions
    extends AsyncTask<RST_ServiceReferenceData.SleepQuestionsCallback, Void, RST_Response<RST_NightQuestions>>
  {
    private AsyncSleepQuestions() {}
    
    protected RST_Response<RST_NightQuestions> doInBackground(RST_ServiceReferenceData.SleepQuestionsCallback... paramVarArgs)
    {
      Object localObject = paramVarArgs[0];
      paramVarArgs = new RST_Response();
      if (((RST_ServiceReferenceData.SleepQuestionsCallback)localObject).getResult() != null)
      {
        NightQuestions localNightQuestions = (NightQuestions)((RST_ServiceReferenceData.SleepQuestionsCallback)localObject).getResult();
        localObject = RefreshModelController.getInstance();
        RST_NightQuestions localRST_NightQuestions = ((RefreshModelController)localObject).getUser().getNightQuestions();
        if (localRST_NightQuestions != null)
        {
          localRST_NightQuestions.delete();
          ((RefreshModelController)localObject).save();
        }
        NightQuestionsMapper.processNightQuestions(localNightQuestions);
        ((RefreshModelController)localObject).setNigthQuestionsTimestamp(new Date().getTime());
        ((RefreshModelController)localObject).save();
        paramVarArgs.setResponse(((RefreshModelController)localObject).getUser().getNightQuestions());
        paramVarArgs.setStatus(true);
      }
      for (;;)
      {
        return paramVarArgs;
        paramVarArgs.setStatus(false);
        paramVarArgs.setErrorCode(((RST_ServiceReferenceData.SleepQuestionsCallback)localObject).getError().getErrorCode());
        paramVarArgs.setErrorMessage(((RST_ServiceReferenceData.SleepQuestionsCallback)localObject).getError().getErrorMessage());
      }
    }
    
    protected void onPostExecute(RST_Response<RST_NightQuestions> paramRST_Response)
    {
      super.onPostExecute(paramRST_Response);
      RST_ServiceReferenceData.this.callback.onResult(paramRST_Response);
    }
  }
  
  private class SleepQuestionsCallback
    extends HttpDefaultCallback<NightQuestions>
  {
    public SleepQuestionsCallback()
    {
      super();
    }
    
    public void handleResult(HttpResponse paramHttpResponse)
    {
      super.handleResult(paramHttpResponse);
      new RST_ServiceReferenceData.AsyncSleepQuestions(RST_ServiceReferenceData.this, null).execute(new SleepQuestionsCallback[] { this });
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\services\RST_ServiceReferenceData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.model.services;

import android.os.AsyncTask;
import com.resmed.refresh.model.RST_AdviceItem;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.Advice;
import com.resmed.refresh.model.json.Feedback;
import com.resmed.refresh.model.mappers.AdviceMapper;
import com.resmed.refresh.net.ResMedServerAPI;
import com.resmed.refresh.net.callbacks.HttpDefaultCallback;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.RefreshTools;

import java.util.Date;
import java.util.List;
import org.apache.http.HttpResponse;

public class RST_ServiceAdvice
{
  private Date adviceDate;
  private RST_CallbackItem<RST_Response<RST_AdviceItem>> callbackAdvice;
  private RST_CallbackItem<RST_Response<Object>> callbackFeedback;
  private RST_CallbackItem<RST_Response<List<RST_AdviceItem>>> callbackListAdvices;
  private boolean isDateBound;
  private boolean isNetworking;
  private ResMedServerAPI resMedServerAPI;
  
  public RST_ServiceAdvice(ResMedServerAPI paramResMedServerAPI)
  {
    this.resMedServerAPI = paramResMedServerAPI;
    this.isNetworking = false;
    this.isDateBound = false;
  }
  
  private String queryStringForFilter(Date paramDate1, Date paramDate2, boolean paramBoolean)
  {
    this.isDateBound = false;
    String str = "?";
    if (paramBoolean) {
      paramDate1 = "?$top=1&$orderby=StartDate desc";
    }
    for (;;)
    {
      return paramDate1.replace(" ", "%20");
      if ((paramDate1 != null) && (paramDate2 != null))
      {
        str = RefreshTools.getStringFromDate(paramDate1);
        paramDate2 = RefreshTools.getStringFromDate(paramDate2);
        paramDate2 = "?$filter=DateGenerated gt datetime'" + str + "' and DateGenerated lt datetime'" + paramDate2 + "'";
        this.isDateBound = true;
        this.adviceDate = paramDate1;
        paramDate1 = paramDate2;
      }
      else if (paramDate1 != null)
      {
        paramDate1 = RefreshTools.getStringFromDate(paramDate1);
        paramDate1 = "filter=DateGenerated gt datetime'" + paramDate1 + "'";
      }
      else
      {
        paramDate1 = str;
        if (paramDate2 != null)
        {
          paramDate1 = RefreshTools.getStringFromDate(paramDate2);
          paramDate1 = "?$filter=DateGenerated lt datetime'" + paramDate1 + "'";
        }
      }
    }
  }
  
  public void adviceForDates(Date paramDate1, Date paramDate2, RST_CallbackItem<RST_Response<List<RST_AdviceItem>>> paramRST_CallbackItem)
  {
    if (this.isNetworking)
    {
      paramDate1 = new RST_Response();
      paramDate1.setErrorMessage(RefreshApplication.getInstance().getApplicationContext().getString(2131165351));
      paramRST_CallbackItem.onResult(paramDate1);
    }
    for (;;)
    {
      return;
      this.callbackListAdvices = paramRST_CallbackItem;
      paramDate1 = queryStringForFilter(paramDate1, paramDate2, false);
      this.resMedServerAPI.advices(paramDate1, new ListAdvicesCallback(Advice.class));
    }
  }
  
  public void getAdviceItem(long paramLong, RST_CallbackItem<RST_Response<RST_AdviceItem>> paramRST_CallbackItem)
  {
    if (this.isNetworking)
    {
      RST_Response localRST_Response = new RST_Response();
      localRST_Response.setErrorMessage(RefreshApplication.getInstance().getApplicationContext().getString(2131165351));
      paramRST_CallbackItem.onResult(localRST_Response);
    }
    for (;;)
    {
      return;
      this.callbackAdvice = paramRST_CallbackItem;
      this.resMedServerAPI.advice(paramLong, new AdviceCallback(Advice.class));
    }
  }
  
  public void updateFeedback(RST_AdviceItem paramRST_AdviceItem, RST_CallbackItem<RST_Response<Object>> paramRST_CallbackItem)
  {
    if (this.isNetworking)
    {
      paramRST_AdviceItem = new RST_Response();
      paramRST_AdviceItem.setStatus(false);
      paramRST_AdviceItem.setErrorMessage(RefreshApplication.getInstance().getApplicationContext().getString(2131165351));
      paramRST_CallbackItem.onResult(paramRST_AdviceItem);
    }
    for (;;)
    {
      return;
      this.callbackFeedback = paramRST_CallbackItem;
      paramRST_CallbackItem = new Feedback(paramRST_AdviceItem.getId(), paramRST_AdviceItem.getFeedback());
      this.resMedServerAPI.updateFeedback(paramRST_AdviceItem.getId(), paramRST_CallbackItem, new FeedbackCallback(Feedback.class));
    }
  }
  
  private class AdviceCallback
    extends HttpDefaultCallback<Advice>
  {
    public AdviceCallback()
    {
      super();
    }
    
    public void handleResult(HttpResponse paramHttpResponse)
    {
      super.handleResult(paramHttpResponse);
      new RST_ServiceAdvice.AsyncServiceAdvice(RST_ServiceAdvice.this, null).execute(new AdviceCallback[] { this });
    }
  }
  
  private class AsyncServiceAdvice
    extends AsyncTask<RST_ServiceAdvice.AdviceCallback, Void, RST_Response<RST_AdviceItem>>
  {
    private AsyncServiceAdvice() {}
    
    protected RST_Response<RST_AdviceItem> doInBackground(RST_ServiceAdvice.AdviceCallback... paramVarArgs)
    {
      paramVarArgs = paramVarArgs[0];
      RST_Response localRST_Response = new RST_Response();
      Advice localAdvice = (Advice)paramVarArgs.getResult();
      if (localAdvice != null)
      {
        localRST_Response.setResponse(AdviceMapper.processAdviceItem(localAdvice));
        localRST_Response.setStatus(true);
        RefreshModelController.getInstance().save();
      }
      for (;;)
      {
        return localRST_Response;
        localRST_Response.setStatus(false);
        localRST_Response.setErrorCode(paramVarArgs.getError().getErrorCode());
        localRST_Response.setErrorMessage(paramVarArgs.getError().getErrorMessage());
      }
    }
    
    protected void onPostExecute(RST_Response<RST_AdviceItem> paramRST_Response)
    {
      super.onPostExecute(paramRST_Response);
      RST_ServiceAdvice.this.callbackAdvice.onResult(paramRST_Response);
    }
  }
  
  private class AsyncServiceFeedback
    extends AsyncTask<RST_ServiceAdvice.FeedbackCallback, Void, RST_Response<Object>>
  {
    private AsyncServiceFeedback() {}
    
    protected RST_Response<Object> doInBackground(RST_ServiceAdvice.FeedbackCallback... paramVarArgs)
    {
      paramVarArgs = paramVarArgs[0];
      RST_Response localRST_Response = new RST_Response();
      if (paramVarArgs.getError() == null) {
        localRST_Response.setStatus(true);
      }
      for (;;)
      {
        return localRST_Response;
        localRST_Response.setStatus(false);
        localRST_Response.setErrorCode(paramVarArgs.getError().getErrorCode());
        localRST_Response.setErrorMessage(paramVarArgs.getError().getErrorMessage());
      }
    }
    
    protected void onPostExecute(RST_Response<Object> paramRST_Response)
    {
      super.onPostExecute(paramRST_Response);
      RST_ServiceAdvice.this.callbackFeedback.onResult(paramRST_Response);
    }
  }
  
  private class AsyncServiceListAdvice
    extends AsyncTask<RST_ServiceAdvice.ListAdvicesCallback, Void, RST_Response<List<RST_AdviceItem>>>
  {
    private AsyncServiceListAdvice() {}
    
    protected RST_Response<List<RST_AdviceItem>> doInBackground(RST_ServiceAdvice.ListAdvicesCallback... paramVarArgs)
    {
      Object localObject = paramVarArgs[0];
      paramVarArgs = new RST_Response();
      if ((((RST_ServiceAdvice.ListAdvicesCallback)localObject).getResult() != null) && ((((RST_ServiceAdvice.ListAdvicesCallback)localObject).getResult() instanceof List)))
      {
        List localList = (List)((RST_ServiceAdvice.ListAdvicesCallback)localObject).getResult();
        localObject = RefreshModelController.getInstance();
        AdviceMapper.processListAdviceItem(localList);
        ((RefreshModelController)localObject).save();
        if (RST_ServiceAdvice.this.isDateBound)
        {
          paramVarArgs.setResponse(((RefreshModelController)localObject).localAdvicesForDate(RST_ServiceAdvice.this.adviceDate));
          paramVarArgs.setStatus(true);
        }
      }
      for (;;)
      {
        return paramVarArgs;
        paramVarArgs.setResponse(((RefreshModelController)localObject).getUser().getAdvices());
        break;
        paramVarArgs.setStatus(false);
        paramVarArgs.setErrorCode(((RST_ServiceAdvice.ListAdvicesCallback)localObject).getError().getErrorCode());
        paramVarArgs.setErrorMessage(((RST_ServiceAdvice.ListAdvicesCallback)localObject).getError().getErrorMessage());
      }
    }
    
    protected void onPostExecute(RST_Response<List<RST_AdviceItem>> paramRST_Response)
    {
      super.onPostExecute(paramRST_Response);
      RST_ServiceAdvice.this.callbackListAdvices.onResult(paramRST_Response);
    }
  }
  
  private class FeedbackCallback
    extends HttpDefaultCallback<Feedback>
  {
    public FeedbackCallback()
    {
      super();
    }
    
    public void handleResult(HttpResponse paramHttpResponse)
    {
      super.handleResult(paramHttpResponse);
      new RST_ServiceAdvice.AsyncServiceFeedback(RST_ServiceAdvice.this, null).execute(new FeedbackCallback[] { this });
    }
  }
  
  private class ListAdvicesCallback
    extends HttpDefaultCallback<Advice>
  {
    public ListAdvicesCallback()
    {
      super(true);
    }
    
    public void handleResult(HttpResponse paramHttpResponse)
    {
      super.handleResult(paramHttpResponse);
      new RST_ServiceAdvice.AsyncServiceListAdvice(RST_ServiceAdvice.this, null).execute(new ListAdvicesCallback[] { this });
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\services\RST_ServiceAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
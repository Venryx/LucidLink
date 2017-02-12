package com.resmed.refresh.model.services;

import android.os.Handler;
import android.os.Looper;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.net.ResMedServerAPI;
import com.resmed.refresh.net.callbacks.HttpStatusCallback;
import org.apache.http.HttpResponse;

public class RST_ServiceEmailVerification
{
  private RST_CallbackItem<RST_Response<Object>> callback;
  private ResMedServerAPI resMedServerAPI;
  
  public RST_ServiceEmailVerification(ResMedServerAPI paramResMedServerAPI)
  {
    this.resMedServerAPI = paramResMedServerAPI;
  }
  
  public void resendEmailVerification(RST_CallbackItem<RST_Response<Object>> paramRST_CallbackItem)
  {
    this.callback = paramRST_CallbackItem;
    this.resMedServerAPI.sendEmailVerification(new EmailVerificationCallback(null));
  }
  
  private class EmailVerificationCallback
    extends HttpStatusCallback
  {
    private EmailVerificationCallback() {}
    
    public void handleResult(HttpResponse paramHttpResponse)
    {
      super.handleResult(paramHttpResponse);
      new Handler(Looper.getMainLooper()).post(new Runnable()
      {
        public void run()
        {
          RST_ServiceEmailVerification.this.callback.onResult(RST_ServiceEmailVerification.EmailVerificationCallback.this.getResult());
        }
      });
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
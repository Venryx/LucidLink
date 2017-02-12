package com.resmed.refresh.model.services;

import android.os.Handler;
import android.os.Looper;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.net.ResMedServerAPI;
import com.resmed.refresh.net.callbacks.HttpStatusCallback;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.Log;
import java.io.File;
import org.apache.http.HttpResponse;

public class RST_ServiceFileUploader
{
  private RST_CallbackItem<RST_Response<Object>> callback;
  private File fileEDF;
  private File fileLZ4;
  private ResMedServerAPI resMedServerAPI;
  
  public RST_ServiceFileUploader(ResMedServerAPI paramResMedServerAPI)
  {
    this.resMedServerAPI = paramResMedServerAPI;
  }
  
  public void uploadFile(String paramString, RST_SleepSessionInfo paramRST_SleepSessionInfo, RST_CallbackItem<RST_Response<Object>> paramRST_CallbackItem)
  {
    try
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>(" uploadFile(");
      Log.d("com.resmed.refresh.sync", paramString + ", sessionInfoID=" + paramRST_SleepSessionInfo.getId() + ")");
      this.callback = paramRST_CallbackItem;
      paramRST_CallbackItem = new java/io/File;
      paramRST_CallbackItem.<init>(paramString);
      this.fileLZ4 = paramRST_CallbackItem;
      paramRST_CallbackItem = new java/io/File;
      paramRST_CallbackItem.<init>(paramString.replace(".lz4", ".edf"));
      this.fileEDF = paramRST_CallbackItem;
      long l = paramRST_SleepSessionInfo.getId();
      paramRST_CallbackItem = this.resMedServerAPI;
      paramRST_SleepSessionInfo = this.fileLZ4;
      paramString = new com/resmed/refresh/model/services/RST_ServiceFileUploader$FileUploaderCallback;
      paramString.<init>(this, null);
      paramRST_CallbackItem.rawData(l, paramRST_SleepSessionInfo, paramString);
      return;
    }
    catch (Exception paramString)
    {
      for (;;)
      {
        paramString.printStackTrace();
        paramString = new RST_Response();
        paramString.setStatus(false);
        paramString.setErrorMessage(RefreshApplication.getInstance().getString(2131165354));
        this.callback.onResult(paramString);
      }
    }
  }
  
  private class FileUploaderCallback
    extends HttpStatusCallback
  {
    private FileUploaderCallback() {}
    
    public void handleResult(HttpResponse paramHttpResponse)
    {
      super.handleResult(paramHttpResponse);
      if (getResult().isStatus())
      {
        RefreshModelController.getInstance().save();
        if (RST_ServiceFileUploader.this.fileLZ4.exists()) {
          RST_ServiceFileUploader.this.fileLZ4.delete();
        }
        if (RST_ServiceFileUploader.this.fileEDF.exists()) {
          RST_ServiceFileUploader.this.fileEDF.delete();
        }
        Log.d("com.resmed.refresh.sync", " File (" + RST_ServiceFileUploader.this.fileEDF.getAbsolutePath() + ", deleted");
        Log.d("com.resmed.refresh.sync", " File (" + RST_ServiceFileUploader.this.fileLZ4.getAbsolutePath() + ", deleted");
        AppFileLog.addTrace("SYNC uploading file(" + RST_ServiceFileUploader.this.fileLZ4.getAbsolutePath() + ") result ok => Files deleted");
      }
      for (;;)
      {
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
          public void run()
          {
            RST_ServiceFileUploader.this.callback.onResult(RST_ServiceFileUploader.FileUploaderCallback.this.getResult());
          }
        });
        return;
        AppFileLog.addTrace("SYNC uploading file(" + RST_ServiceFileUploader.this.fileLZ4.getAbsolutePath() + ") result:" + getResult().isStatus() + " error:" + getError().getErrorMessage());
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\services\RST_ServiceFileUploader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
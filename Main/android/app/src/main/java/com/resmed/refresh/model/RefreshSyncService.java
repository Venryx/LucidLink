package com.resmed.refresh.model;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import com.resmed.refresh.sleepsession.RST_SleepSession;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.List;

public class RefreshSyncService
  extends Service
{
  private RST_CallbackItem<RST_Response<Object>> callback = new RST_CallbackItem()
  {
    public void onResult(RST_Response<Object> paramAnonymousRST_Response)
    {
      Log.d("com.resmed.refresh.sync", "callback service onResult! " + paramAnonymousRST_Response.isStatus() + " " + paramAnonymousRST_Response.getErrorCode() + " " + paramAnonymousRST_Response.getErrorMessage());
    }
  };
  private RST_CallbackItem<RST_Response<Object>> syncData = new RST_CallbackItem()
  {
    public void onResult(RST_Response<Object> paramAnonymousRST_Response)
    {
      Log.d("com.resmed.refresh.sync", "SyncData service onResult! " + paramAnonymousRST_Response.isStatus() + " " + paramAnonymousRST_Response.getErrorCode() + " " + paramAnonymousRST_Response.getErrorMessage());
      new Thread(new Runnable()
      {
        public void run()
        {
          RefreshSyncService.this.syncData();
        }
      }).start();
    }
  };
  private RST_CallbackItem<RST_Response<RST_UserProfile>> uploadFiles = new RST_CallbackItem()
  {
    public void onResult(RST_Response<RST_UserProfile> paramAnonymousRST_Response)
    {
      Log.d("com.resmed.refresh.sync", "uploadProfile service onResult! " + paramAnonymousRST_Response.isStatus() + " " + paramAnonymousRST_Response.getErrorCode() + " " + paramAnonymousRST_Response.getErrorMessage());
      new Thread(new Runnable()
      {
        public void run()
        {
          RefreshSyncService.this.uploadFiles();
        }
      }).start();
    }
  };
  private RST_CallbackItem<RST_Response<Object>> uploadProfile = new RST_CallbackItem()
  {
    public void onResult(RST_Response<Object> paramAnonymousRST_Response)
    {
      Log.d("com.resmed.refresh.sync", "uploadRecords service onResult! " + paramAnonymousRST_Response.isStatus() + " " + paramAnonymousRST_Response.getErrorCode() + " " + paramAnonymousRST_Response.getErrorMessage());
      new Thread(new Runnable()
      {
        public void run()
        {
          RefreshSyncService.this.uploadProfile();
        }
      }).start();
    }
  };
  
  private boolean availablebleToConnect()
  {
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    RST_SleepSession localRST_SleepSession = RST_SleepSession.getInstance();
    if ((!isAppInForeground()) && (localRefreshModelController.isLoggedIn()) && (localRefreshModelController.getUser() != null) && (checkConnections()) && (!localRST_SleepSession.isSessionRunning())) {}
    for (boolean bool = true;; bool = false)
    {
      if (!bool)
      {
        Object localObject1 = "Not avaiable to connect due to";
        if (isAppInForeground()) {
          localObject1 = "Not avaiable to connect due to" + "\tApp in foreground";
        }
        Object localObject2 = localObject1;
        if (!localRefreshModelController.isLoggedIn()) {
          localObject2 = localObject1 + "\tNot LoggedIn";
        }
        localObject1 = localObject2;
        if (localRefreshModelController.getUser() == null) {
          localObject1 = localObject2 + "\tUser is null";
        }
        localObject2 = localObject1;
        if (!checkConnections()) {
          localObject2 = localObject1 + "\tNot connected to Internet";
        }
        localObject1 = localObject2;
        if (localRST_SleepSession.isSessionRunning()) {
          localObject1 = localObject2 + "\tSleep session running";
        }
        Log.d("com.resmed.refresh.sync", (String)localObject1);
      }
      return bool;
    }
  }
  
  private boolean checkConnections()
  {
    bool2 = false;
    try
    {
      NetworkInfo localNetworkInfo = ((ConnectivityManager)getSystemService("connectivity")).getActiveNetworkInfo();
      bool1 = bool2;
      if (localNetworkInfo != null)
      {
        boolean bool3 = localNetworkInfo.isConnected();
        bool1 = bool2;
        if (bool3) {
          bool1 = true;
        }
      }
    }
    catch (Exception localException)
    {
      for (;;)
      {
        boolean bool1 = bool2;
      }
    }
    return bool1;
  }
  
  private boolean checkWiFiConnection()
  {
    for (bool1 = true;; bool1 = false)
    {
      try
      {
        NetworkInfo localNetworkInfo = ((ConnectivityManager)getSystemService("connectivity")).getNetworkInfo(1);
        if (localNetworkInfo == null) {
          continue;
        }
        boolean bool2 = localNetworkInfo.isConnected();
        if (!bool2) {
          continue;
        }
      }
      catch (Exception localException)
      {
        for (;;)
        {
          bool1 = false;
        }
      }
      return bool1;
    }
  }
  
  private void executeCallback(RST_CallbackItem<RST_Response<Object>> paramRST_CallbackItem)
  {
    RST_Response localRST_Response = new RST_Response();
    localRST_Response.setStatus(false);
    paramRST_CallbackItem.onResult(localRST_Response);
  }
  
  private boolean isAppInForeground()
  {
    return RefreshApplication.getInstance().isAppInForeground();
  }
  
  private void syncData()
  {
    Log.d("com.resmed.refresh.sync", " syncData synchroniseLatestAll");
    if (availablebleToConnect()) {
      RefreshModelController.getInstance().synchroniseLatestAll(this.callback, true);
    }
    Log.d("com.resmed.refresh.sync", "//////////////////////////////");
  }
  
  private void uploadFiles()
  {
    Log.d("com.resmed.refresh.sync", "Uploading files availablebleToConnect=" + availablebleToConnect() + " checkWiFiConnection=" + checkWiFiConnection());
    if ((!availablebleToConnect()) || (!checkWiFiConnection()))
    {
      Log.d("com.resmed.refresh.sync", "uploadFiles not ready");
      syncData();
    }
    File[] arrayOfFile;
    for (;;)
    {
      return;
      arrayOfFile = RefreshTools.getFilesPath().listFiles(new FilenameFilter()
      {
        public boolean accept(File paramAnonymousFile, String paramAnonymousString)
        {
          return paramAnonymousString.contains(".lz4");
        }
      });
      if ((arrayOfFile != null) && (arrayOfFile.length != 0)) {
        break;
      }
      syncData();
    }
    Log.d("com.resmed.refresh.sync", arrayOfFile.length + " files to upload");
    int i = 0;
    label127:
    File localFile;
    if (i < arrayOfFile.length)
    {
      localFile = arrayOfFile[i];
      Log.d("com.resmed.refresh.sync", "File to upload = " + localFile.getName() + ", date = " + new Date(localFile.lastModified()) + ", size: " + localFile.length() + " bytes");
    }
    for (;;)
    {
      try
      {
        l = Long.parseLong(localFile.getName().split("_")[2].replace(".lz4", ""));
        localObject = new java.lang.StringBuilder;
        ((StringBuilder)localObject).<init>("recordId of filename=");
        Log.d("com.resmed.refresh.sync", l);
        RST_SleepSessionInfo localRST_SleepSessionInfo = RefreshModelController.getInstance().localSleepSessionForId(l);
        if ((localRST_SleepSessionInfo == null) || (!localRST_SleepSessionInfo.getUploaded())) {
          continue;
        }
        localObject = new java.lang.StringBuilder;
        ((StringBuilder)localObject).<init>("Uploading file =");
        Log.d("com.resmed.refresh.sync", localFile.getName());
        if (i != localFile.length() - 1L) {
          continue;
        }
        localObject = this.syncData;
        RefreshModelController.getInstance().serviceUpdateFileRecord(localFile.getAbsolutePath(), localRST_SleepSessionInfo, (RST_CallbackItem)localObject);
        i++;
      }
      catch (Exception localException)
      {
        long l;
        Object localObject;
        localException.printStackTrace();
        executeCallback(this.syncData);
        continue;
      }
      break label127;
      break;
      localObject = this.callback;
      continue;
      if (i == localFile.length() - 1L) {
        executeCallback(this.syncData);
      }
      localObject = new java.lang.StringBuilder;
      ((StringBuilder)localObject).<init>("Record ");
      Log.d("com.resmed.refresh.sync", l + " is not uploaded. Stop upload of file " + localFile.getName());
    }
  }
  
  private void uploadProfile()
  {
    Log.d("com.resmed.refresh.sync", " Upload Profile");
    if ((availablebleToConnect()) && (RefreshModelController.getInstance().isProfileUpdatePending()))
    {
      Log.d("com.resmed.refresh.sync", "Controller.uploadProfile");
      RefreshModelController.getInstance().updateUserProfile(this.uploadFiles);
    }
    for (;;)
    {
      return;
      uploadFiles();
    }
  }
  
  private void uploadRecords()
  {
    Log.d("com.resmed.refresh.sync", " RefreshSyncService::uploadRecords() deleting files!");
    Object localObject = RefreshTools.getFilesPath();
    Log.d("com.resmed.refresh.sync", " RefreshSyncService::uploadRecords() Folder Name : " + ((File)localObject).getAbsolutePath());
    localObject = ((File)localObject).listFiles();
    int j = localObject.length;
    i = 0;
    for (;;)
    {
      if (i >= j) {
        Log.d("com.resmed.refresh.sync", "SyncService uploaing records");
      }
      try
      {
        if (!availablebleToConnect()) {
          uploadProfile();
        }
        for (;;)
        {
          return;
          RefreshModelController localRefreshModelController = localObject[i];
          Log.d("com.resmed.refresh.sync", " RefreshSyncService::uploadRecords() file.getName() : " + localRefreshModelController.getName());
          if ((localRefreshModelController.getName().endsWith(".lz4")) || (localRefreshModelController.getName().endsWith(".edf")))
          {
            long l = new Date().getTime() - localRefreshModelController.lastModified();
            Log.d("com.resmed.refresh.sync", " RefreshSyncService::uploadRecords() diff : " + l / 86400000L);
            if (l > 864000000L)
            {
              Log.d("com.resmed.refresh.sync", " RefreshSyncService::uploadRecords() deleting file : " + localRefreshModelController.getName());
              boolean bool = localRefreshModelController.delete();
              Log.d("com.resmed.refresh.sync", " RefreshSyncService::uploadRecords() wasDeleted : " + bool);
            }
          }
          i++;
          break;
          localRefreshModelController = RefreshModelController.getInstance();
          localObject = localRefreshModelController.localSessionsForUpload();
          if ((localObject == null) || (((List)localObject).size() == 0))
          {
            uploadProfile();
            continue;
          }
          try
          {
            Thread.sleep(1500L);
            uploadProfile();
            continue;
            StringBuilder localStringBuilder = new java.lang.StringBuilder;
            localStringBuilder.<init>(String.valueOf(localException1.size()));
            Log.d("com.resmed.refresh.sync", " records to upload");
            i = 0;
            if (i >= localException1.size()) {
              continue;
            }
            localStringBuilder = new java.lang.StringBuilder;
            localStringBuilder.<init>("Uploading record ");
            Log.d("com.resmed.refresh.sync", ((RST_SleepSessionInfo)localException1.get(i)).getId());
            if (i == localException1.size() - 1) {
              localRefreshModelController.serviceUpdateRecord((RST_SleepSessionInfo)localException1.get(i), this.uploadProfile);
            }
            for (;;)
            {
              i++;
              break;
              localRefreshModelController.serviceUpdateRecord((RST_SleepSessionInfo)localException1.get(i), this.callback);
            }
          }
          catch (Exception localException2)
          {
            for (;;)
            {
              localException2.printStackTrace();
            }
          }
        }
      }
      catch (Exception localException1)
      {
        localException1.printStackTrace();
      }
    }
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    Log.d("com.resmed.refresh.sync", " onBind! ");
    return null;
  }
  
  public void onRebind(Intent paramIntent)
  {
    Log.d("com.resmed.refresh.sync", " onRebind! ");
    super.onRebind(paramIntent);
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    Log.d("com.resmed.refresh.sync", "//////////////////////////////");
    Log.d("com.resmed.refresh.sync", " onStartCommand! ");
    new Thread(new Runnable()
    {
      public void run()
      {
        RefreshSyncService.this.uploadRecords();
      }
    }).start();
    return 2;
  }
  
  public void unbindService(ServiceConnection paramServiceConnection)
  {
    Log.d("com.resmed.refresh.sync", " unbindService! ");
    super.unbindService(paramServiceConnection);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RefreshSyncService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
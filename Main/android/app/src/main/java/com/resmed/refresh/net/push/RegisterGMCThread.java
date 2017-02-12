package com.resmed.refresh.net.push;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.Log;
import java.io.IOException;

public class RegisterGMCThread
  extends Thread
{
  private static final String SENDER_ID = "147713031299";
  
  public void run()
  {
    super.run();
    Log.d("com.resmed.refresh.push", "GooglePlayServices RegisterGMCThread start.");
    try
    {
      String str = GoogleCloudMessaging.getInstance(RefreshApplication.getInstance().getApplicationContext()).register(new String[] { "147713031299" });
      StringBuilder localStringBuilder = new java.lang.StringBuilder;
      localStringBuilder.<init>("GoogleCloudMessaging: registration_id=");
      Log.d("com.resmed.refresh.push", str);
      RefreshModelController.getInstance().registerPushNotificationToken(str);
      return;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        Log.d("com.resmed.refresh.push", "Error register GCM:" + localIOException.getMessage());
      }
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
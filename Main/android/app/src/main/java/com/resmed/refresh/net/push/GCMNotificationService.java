package com.resmed.refresh.net.push;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.RST_AdviceItem;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.activity.HomeActivity;
import com.resmed.refresh.ui.activity.SPlusMentorActivity;
import com.resmed.refresh.utils.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class GCMNotificationService
  extends IntentService
  implements RST_CallbackItem<RST_Response<RST_AdviceItem>>
{
  public static final String ACTION_ADVICE = "com.resmed.refresh.ACTION_ADVICE";
  public static final int NOTIFICATION_ID = 1;
  public static final int SNOOZE_DURATION = 54000000;
  
  public GCMNotificationService()
  {
    super("com.resmed.refresh");
  }
  
  private void issueNotification(RST_AdviceItem paramRST_AdviceItem, boolean paramBoolean)
  {
    NotificationManager localNotificationManager = (NotificationManager)getSystemService("notification");
    NotificationCompat.Builder localBuilder = new NotificationCompat.Builder(this).setSmallIcon(2130837933).setContentTitle(getString(2131166081)).setContentText(getString(2131166082)).setDefaults(-1).setAutoCancel(true).setStyle(new NotificationCompat.BigTextStyle().bigText(paramRST_AdviceItem.getContent() + " " + paramRST_AdviceItem.getDetail()));
    Object localObject;
    if (PreferenceManager.getDefaultSharedPreferences(this).getInt("PREF_CONNECTION_STATE", -1) != CONNECTION_STATE.NIGHT_TRACK_ON.ordinal())
    {
      if (!paramBoolean) {
        break label181;
      }
      localObject = TaskStackBuilder.create(this);
      ((TaskStackBuilder)localObject).addParentStack(HomeActivity.class);
      paramRST_AdviceItem = new Intent(this, HomeActivity.class);
      paramRST_AdviceItem.setFlags(268468224);
      ((TaskStackBuilder)localObject).addNextIntent(paramRST_AdviceItem);
      localBuilder.setContentIntent(((TaskStackBuilder)localObject).getPendingIntent(0, 1073741824));
      localNotificationManager.notify(1, localBuilder.build());
    }
    for (;;)
    {
      return;
      label181:
      localObject = new Intent(this, SPlusMentorActivity.class);
      ((Intent)localObject).putExtra("com.resmed.refresh.ui.uibase.app.splus_menotr_day", paramRST_AdviceItem.getTimestamp().getTime());
      TaskStackBuilder localTaskStackBuilder = TaskStackBuilder.create(this);
      localTaskStackBuilder.addParentStack(HomeActivity.class);
      paramRST_AdviceItem = new Intent(this, HomeActivity.class);
      paramRST_AdviceItem.setFlags(268468224);
      localTaskStackBuilder.addNextIntent(paramRST_AdviceItem);
      localTaskStackBuilder.addNextIntent((Intent)localObject);
      localBuilder.setContentIntent(localTaskStackBuilder.getPendingIntent(0, 1073741824));
      localNotificationManager.notify(1, localBuilder.build());
    }
  }
  
  protected void onHandleIntent(Intent paramIntent)
  {
    Object localObject1 = paramIntent.getExtras();
    Object localObject2 = GoogleCloudMessaging.getInstance(this).getMessageType(paramIntent);
    Log.i("com.resmed.refresh.push", "Received PUSH messageType: " + (String)localObject2);
    if ((!((Bundle)localObject1).isEmpty()) && (!"send_error".equals(localObject2)) && (!"deleted_messages".equals(localObject2)) && ("gcm".equals(localObject2)))
    {
      localObject2 = (String)paramIntent.getExtras().get("message");
      Log.i("com.resmed.refresh.push", "Received PUSH: " + (String)localObject2);
    }
    for (;;)
    {
      try
      {
        localObject1 = new org/json/JSONObject;
        ((JSONObject)localObject1).<init>((String)localObject2);
        localObject2 = ((JSONObject)localObject1).getString("U");
        k = ((JSONObject)localObject1).getInt("A");
        j = 0;
        i = j;
      }
      catch (JSONException localJSONException1)
      {
        int k;
        int j;
        int i;
        Object localObject3;
        Log.i("com.resmed.refresh.push", "JSON parse error on PUSH: " + localJSONException1.getLocalizedMessage());
        continue;
        RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>("UserID logged in:");
        Log.i("com.resmed.refresh.push", localRefreshModelController.getUserSessionID());
        if ((!localRefreshModelController.getUserSessionID().equalsIgnoreCase(localParseException)) || (!localRefreshModelController.getUsePushNotifications())) {
          continue;
        }
        long l = Integer.valueOf(k).intValue();
        RST_CallbackItem local1 = new com/resmed/refresh/net/push/GCMNotificationService$1;
        local1.<init>(this);
        localRefreshModelController.adviceForId(l, local1);
        continue;
      }
      try
      {
        if (((JSONObject)localObject1).has("R")) {
          i = ((JSONObject)localObject1).getInt("R");
        }
        if (i == 1)
        {
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>("notification logs1 ");
          Log.i("com.resmed.refresh.push", localObject1);
          String str = ((JSONObject)localObject1).getString("C");
          localObject3 = ((JSONObject)localObject1).getString("D");
          localObject1 = null;
          try
          {
            localObject2 = new java/text/SimpleDateFormat;
            ((SimpleDateFormat)localObject2).<init>("yyyy-MM-dd'T'HH:mm:ss");
            localObject2 = ((SimpleDateFormat)localObject2).parse((String)localObject3);
            localObject1 = localObject2;
          }
          catch (ParseException localParseException)
          {
            localParseException.printStackTrace();
            continue;
          }
          localObject2 = new com/resmed/refresh/model/RST_AdviceItem;
          ((RST_AdviceItem)localObject2).<init>();
          ((RST_AdviceItem)localObject2).setContent(str);
          ((RST_AdviceItem)localObject2).setDetail("");
          ((RST_AdviceItem)localObject2).setTimestamp((Date)localObject1);
          issueNotification((RST_AdviceItem)localObject2, true);
          GCMBroadcastReceiver.completeWakefulIntent(paramIntent);
          return;
        }
      }
      catch (JSONException localJSONException2)
      {
        localObject3 = new java/lang/StringBuilder;
        ((StringBuilder)localObject3).<init>("JSON parse error in isReminder: ");
        Log.i("com.resmed.refresh.push", localJSONException2.getLocalizedMessage());
        i = j;
      }
    }
  }
  
  public void onResult(RST_Response<RST_AdviceItem> paramRST_Response)
  {
    Log.d("com.resmed.refresh.push", "Advice Downloaded");
    if (paramRST_Response.isStatus()) {
      issueNotification((RST_AdviceItem)paramRST_Response.getResponse(), false);
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\net\push\GCMNotificationService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
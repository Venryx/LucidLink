package com.resmed.refresh.net.push;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.RST_AdviceItem;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.ui.activity.HomeActivity;
import com.resmed.refresh.ui.activity.SPlusMentorActivity;
import com.resmed.refresh.utils.Log;

public class GCMNotificationService extends IntentService implements RST_CallbackItem {
	public static final String ACTION_ADVICE = "com.resmed.refresh.ACTION_ADVICE";
	public static final int NOTIFICATION_ID = 1;
	public static final int SNOOZE_DURATION = 54000000;

	public GCMNotificationService() {
		super("com.resmed.refresh");
	}

	// $FF: synthetic method
	static void access$1(GCMNotificationService var0, RST_AdviceItem var1, boolean var2) {
		var0.issueNotification(var1, var2);
	}

	private void issueNotification(RST_AdviceItem var1, boolean var2) {
		NotificationManager var3 = (NotificationManager)this.getSystemService("notification");
		Builder var4 = (new Builder(this)).setSmallIcon(2130837933).setContentTitle(this.getString(2131166081)).setContentText(this.getString(2131166082)).setDefaults(-1).setAutoCancel(true).setStyle((new BigTextStyle()).bigText(var1.getContent() + " " + var1.getDetail()));
		if(PreferenceManager.getDefaultSharedPreferences(this).getInt("PREF_CONNECTION_STATE", -1) != CONNECTION_STATE.NIGHT_TRACK_ON.ordinal()) {
			if(!var2) {
				Intent var5 = new Intent(this, SPlusMentorActivity.class);
				var5.putExtra("com.resmed.refresh.ui.uibase.app.splus_menotr_day", var1.getTimestamp().getTime());
				TaskStackBuilder var7 = TaskStackBuilder.create(this);
				var7.addParentStack(HomeActivity.class);
				Intent var9 = new Intent(this, HomeActivity.class);
				var9.setFlags(268468224);
				var7.addNextIntent(var9);
				var7.addNextIntent(var5);
				var4.setContentIntent(var7.getPendingIntent(0, 1073741824));
				var3.notify(1, var4.build());
				return;
			}

			TaskStackBuilder var14 = TaskStackBuilder.create(this);
			var14.addParentStack(HomeActivity.class);
			Intent var16 = new Intent(this, HomeActivity.class);
			var16.setFlags(268468224);
			var14.addNextIntent(var16);
			var4.setContentIntent(var14.getPendingIntent(0, 1073741824));
			var3.notify(1, var4.build());
		}

	}

	protected void onHandleIntent(Intent param1) {
		// $FF: Couldn't be decompiled
	}

	public void onResult(RST_Response var1) {
		Log.d("com.resmed.refresh.push", "Advice Downloaded");
		if(var1.isStatus()) {
			this.issueNotification((RST_AdviceItem)var1.getResponse(), false);
		}

	}

	@Override
	public void onResult(Object paramT) {

	}
}

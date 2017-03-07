package v.lucidlink;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import vpackages.V;

public class MainService extends Service {
	@Override public IBinder onBind(Intent intent) {
		return null;
	}
	// when app is "swipe-closed" in recent-apps list
	public void onTaskRemoved(Intent rootIntent) {
		V.LogJava("Starting service pre-app-close handler.");

		// keep the same process alive that was used for activity, so the js-context persists and can save its data
		/*Notification note = new Notification();
		note.tickerText = "Shutting down...";
		note.when = System.currentTimeMillis();
		note.flags |= Notification.FLAG_NO_CLEAR;*/
		Notification note = new Notification.Builder(this)
			.setContentTitle("Shutting down...")
			.setContentText("LucidLink is shutting down...")
			.setSmallIcon(R.mipmap.ic_launcher)
			.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).build();
		startForeground(1, note);

		JSBridge.SendEvent("PreAppClose");

		V.WaitXThenRun(3000, ()-> {
			V.LogJava("Stopping service");
			stopSelf();
		});
	}
}
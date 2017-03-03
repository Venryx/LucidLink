package v.lucidlink;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
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
			.setContentText("LucidLink is shutting down...").build();
		startForeground(1, note);

		JSBridge.SendEvent("PreAppClose");
		//stopSelf();

		//SoLoader.init(getApplicationContext(), 0);

		/*try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/

		//startService(new Intent(getApplicationContext(), HeadlessService.class));

		/*try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		stopSelf();*/

		V.WaitXThenRun(5000, ()-> {
			V.LogJava("Stopping service");
			stopSelf();
		});
	}
}
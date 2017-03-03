package v.lucidlink;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.facebook.soloader.SoLoader;

import java.io.IOException;

import vpackages.V;

public class MainService extends Service {
	@Override public IBinder onBind(Intent intent) {
		V.LogJava("Starting service.");
		return null;
	}
	// when app is "swipe-closed" in recent-apps list
	public void onTaskRemoved(Intent rootIntent) {
		V.LogJava("Starting service pre-app-close handler.");
		try {
			SoLoader.init(getApplicationContext(), 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSBridge.SendEvent("PreAppClose");
		//stopSelf();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//startService(new Intent(getApplicationContext(), HeadlessService.class));

		V.LogJava("Stopping service");
		stopSelf();
	}
}
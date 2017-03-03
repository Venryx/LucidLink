package v.lucidlink;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.facebook.soloader.SoLoader;

import vpackages.V;

public class MainService extends Service {
	@Override public IBinder onBind(Intent intent) {
		return null;
	}
	// when app is "swipe-closed" in recent-apps list
	public void onTaskRemoved(Intent rootIntent) {
		V.LogJava("OnTaskRemoved + PreAppClose" + V.GetStackTrace());
		JSBridge.SendEvent("PreAppClose");
		//stopSelf();

		//SoLoader.init(getApplicationContext(), 0);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		startService(new Intent(getApplicationContext(), HeadlessService.class));
	}
}
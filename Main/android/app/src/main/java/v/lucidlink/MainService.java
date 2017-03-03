package v.lucidlink;

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
		V.Log("OnTaskRemoved + PreAppClose" + V.GetStackTrace());
		JSBridge.SendEvent("PreAppClose");
		stopSelf();
	}
}
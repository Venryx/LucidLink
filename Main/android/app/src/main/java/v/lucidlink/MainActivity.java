package v.lucidlink;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.widget.Toast;

import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.ReactMethod;

import v.LibMuse.LibMuse;

public class MainActivity extends ReactActivity {
	public static MainActivity main;
	public MainActivity() {
		super();
		main = this;
		LibMuse.mainActivity = this;
	}

	static final int REQUEST_WRITE_STORAGE = 112;
	public boolean ArePermissionsGranted() {
		return ContextCompat.checkSelfPermission(Main.main.reactContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
	}
	public void EnsurePermissionsGranted() {
		boolean hasPermission = ArePermissionsGranted();
		if (!hasPermission)
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_WRITE_STORAGE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Restart();
			} else {
				V.Toast("The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG);
			}
		}
	}

	public void Restart() {
		//recreate();
		/*Intent intent = getIntent();
		finish();
		startActivity(intent);*/
		/*Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);*/
		LoadBundle();
	}
	void LoadBundleLegacy() {
		final Activity currentActivity = this;
		// The currentActivity can be null if it is backgrounded / destroyed, so we simply no-op to prevent any null pointer exceptions.
		if (currentActivity == null) return;
		currentActivity.runOnUiThread(()->currentActivity.recreate());
	}
	private void LoadBundle() {
		try {
			new Handler(Looper.getMainLooper()).post(()->LoadBundleLegacy());
		} catch (Exception e) {
			LoadBundleLegacy();
		}
	}

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "LucidLink";
    }

	@Override
	public void onBackPressed() {
	}

	@Override public boolean onKeyDown(int keyCode, KeyEvent event)  {
		Main.main.SendEvent("OnKeyDown", keyCode);
		if (Main.main.blockUnusedKeys)
			return true;
		return super.onKeyDown(keyCode, event);
	}
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Main.main.SendEvent("OnKeyUp", keyCode);
		if (Main.main.blockUnusedKeys)
			return true;
		return super.onKeyUp(keyCode, event);
	}
}
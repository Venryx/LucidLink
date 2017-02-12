package v.lucidlink;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.widget.Toast;

import com.facebook.react.ReactActivity;

import SPlus.SPlusModule;
import SPlus.SPlusPackage;
import v.LibMuse.LibMuseModule;

import static android.os.Build.VERSION.SDK;

public class MainActivity extends ReactActivity {
	public static MainActivity main;
	public MainActivity() {
		super();
		main = this;
		LibMuseModule.mainActivity = this;
		SPlusModule.mainActivity = this;
	}

	static final int REQUEST_WRITE_STORAGE = 112;
	public boolean ArePermissionsGranted() {
		// workaround for runtime bug of ContextCompat evaluating to the old version (in S+ jar)
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
			return true;
		return ContextCompat.checkSelfPermission(LL.main.reactContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
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
				recreate();
			} else {
				V.Toast("The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG);
			}
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
		char keyChar = (char)event.getUnicodeChar();
		LL.main.SendEvent("OnKeyDown", keyCode, String.valueOf(keyChar));
		if (LL.main.blockUnusedKeys)
			return true;
		return super.onKeyDown(keyCode, event);
	}
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		char keyChar = (char)event.getUnicodeChar();
		LL.main.SendEvent("OnKeyUp", keyCode, String.valueOf(keyChar));
		if (LL.main.blockUnusedKeys)
			return true;
		return super.onKeyUp(keyCode, event);
	}
}
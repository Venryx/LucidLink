package com.lucidlink;

import android.os.Build;
import android.view.KeyEvent;

import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "LucidLink";
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

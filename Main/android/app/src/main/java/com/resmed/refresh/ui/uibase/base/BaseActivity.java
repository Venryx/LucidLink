package com.resmed.refresh.ui.uibase.base;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.react.ReactActivity;
import com.resmed.refresh.bluetooth.BeDConnectionStatus;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.utils.Log;

//public abstract class BaseActivity extends FragmentActivity {
public abstract class BaseActivity extends ReactActivity {
	public static final int BUTTON_DONE = 2130837770;
	public static final int BUTTON_EDIT = 2130837773;
	public static final int BUTTON_SAVE = 2130837782;
	public static final int BUTTON_SHARE = 2130837785;
	protected Typeface akzidenzBold;
	protected Typeface akzidenzLight;
	private BaseActivity baseActivity;
	private BroadcastReceiver connectionStatusReceiver = new BroadcastReceiver() {
		public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent) {
			CONNECTION_STATE state = (CONNECTION_STATE) paramAnonymousIntent.getExtras().get("EXTRA_RESMED_CONNECTION_STATE");
			Log.d("com.resmed.refresh.pair", " onReceive()  CONNECTION_STATE " + CONNECTION_STATE.toString(state));
			Log.d("com.resmed.refresh.ui", " onReceive()  CONNECTION_STATE " + CONNECTION_STATE.toString(state));
		}
	};
	protected boolean isAvailable;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		this.baseActivity = this;
		Log.d("com.resmed.refresh.pair", " onCreate ref : " + this);
		this.isAvailable = true;
	}

	protected void onPostResume() {
		this.isAvailable = true;
		super.onPostResume();
	}

	protected void onResume() {
		super.onResume();
		//RefreshApplication.getInstance().increaseActivitiesInForeground();
		registerReceiver(this.connectionStatusReceiver, new IntentFilter("ACTION_RESMED_CONNECTION_STATUS"));
	}

	/*protected void onResumeFragments() {
		this.isAvailable = true;
		super.onResumeFragments();
	}*/

	protected void onSaveInstanceState(Bundle paramBundle) {
		this.isAvailable = false;
		super.onSaveInstanceState(paramBundle);
		Log.d("com.resmed.refresh.dialog", "BaseActivity onSaveInstanceState " + this);
	}

	protected void onStop() {
		this.isAvailable = false;
		super.onStop();
	}
}
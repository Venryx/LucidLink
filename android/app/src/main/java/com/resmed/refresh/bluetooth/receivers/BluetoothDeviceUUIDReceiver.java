package com.resmed.refresh.bluetooth.receivers;

import android.content.*;

import com.resmed.refresh.utils.*;

public class BluetoothDeviceUUIDReceiver extends BroadcastReceiver {
	public void onReceive(final Context context, final Intent intent) {
		final String action = intent.getAction();
		Log.d("com.resmed.refresh.bluetooth", " bluetooth uuid : " + intent.hasExtra("android.bluetooth.device.extra.UUID"));
		if ("android.bluetooth.device.action.UUID".equals(action)) {
			Log.d("com.resmed.refresh.bluetooth", " bluetooth uuid : " + intent.getParcelableArrayExtra("android.bluetooth.device.extra.UUID"));
		}
	}
}
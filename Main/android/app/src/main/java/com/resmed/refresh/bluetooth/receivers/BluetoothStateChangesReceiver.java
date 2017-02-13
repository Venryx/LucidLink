package com.resmed.refresh.bluetooth.receivers;
import com.resmed.refresh.utils.*;
import android.content.*;
import com.resmed.refresh.bluetooth.*;

public class BluetoothStateChangesReceiver extends BroadcastReceiver {
	private RefreshBluetoothManager bluetoothManager;

	public BluetoothStateChangesReceiver(final RefreshBluetoothManager bluetoothManager) {
		this.bluetoothManager = bluetoothManager;
	}

	public void onReceive(final Context context, final Intent intent) {
		if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction())) {
			final int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
			if (12 == intExtra) {
				this.bluetoothManager.setConnectionStatusAndNotify(CONNECTION_STATE.BLUETOOTH_ON, true);
			} else if (10 == intExtra) {
				this.bluetoothManager.setConnectionStatusAndNotify(CONNECTION_STATE.BLUETOOTH_OFF, true);
			}
		}
	}
}
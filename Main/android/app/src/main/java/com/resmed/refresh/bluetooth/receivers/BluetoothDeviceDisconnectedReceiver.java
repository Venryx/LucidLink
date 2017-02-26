package com.resmed.refresh.bluetooth.receivers;

import android.content.*;
import android.bluetooth.*;

import com.resmed.refresh.bluetooth.*;
import com.resmed.refresh.utils.*;

import static v.lucidlink.LLHolder.LL;

public class BluetoothDeviceDisconnectedReceiver extends BroadcastReceiver {
	private BluetoothSetup bluetoothManager;

	public BluetoothDeviceDisconnectedReceiver(final BluetoothSetup bluetoothManager) {
		this.bluetoothManager = bluetoothManager;
	}

	public void onReceive(final Context context, final Intent intent) {
		if ("android.bluetooth.device.action.ACL_DISCONNECTED".equals(intent.getAction())) {
			final BluetoothDevice bluetoothDevice = intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
			final boolean b = LL.mainModule.connectionState == CONNECTION_STATE.SOCKET_CONNECTED;
			Log.d("com.resmed.refresh.bluetooth", " Bluetooth previous connected : " + b);
			if (b) {
				this.bluetoothManager.connectDevice(bluetoothDevice);
			}
		}
	}
}
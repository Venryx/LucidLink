package com.resmed.refresh.bluetooth.receivers;

import android.content.*;
import android.bluetooth.*;
import com.resmed.refresh.bluetooth.*;
import com.resmed.refresh.utils.*;

public class BluetoothDeviceDisconnectedReceiver extends BroadcastReceiver
{
	private RefreshBluetoothManager bluetoothManager;

	public BluetoothDeviceDisconnectedReceiver(final RefreshBluetoothManager bluetoothManager) {
		this.bluetoothManager = bluetoothManager;
	}

	public void onReceive(final Context context, final Intent intent) {
		if ("android.bluetooth.device.action.ACL_DISCONNECTED".equals(intent.getAction())) {
			final BluetoothDevice bluetoothDevice = (BluetoothDevice)intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
			final boolean b = CONNECTION_STATE.SOCKET_CONNECTED == this.bluetoothManager.getConnectionStatus();
			Log.d("com.resmed.refresh.bluetooth", " Bluetooth previous connected : " + b);
			if (b) {
				this.bluetoothManager.connectDevice(bluetoothDevice);
			}
		}
	}
}
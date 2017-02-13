package com.resmed.refresh.bluetooth.receivers;

import android.content.*;

import com.resmed.refresh.utils.*;

import android.bluetooth.*;

public class BluetoothDevicePairedReceiver extends BroadcastReceiver {
	private RefreshBluetoothManager bluetoothManager;

	public BluetoothDevicePairedReceiver(final RefreshBluetoothManager bluetoothManager) {
		this.bluetoothManager = bluetoothManager;
	}

	public void onReceive(final Context context, final Intent intent) {
		final String action = intent.getAction();
		Log.d("com.resmed.refresh.bluetooth", " BroadcastReceiver Action : " + action);
		if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(action)) {
			final BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
			final int intExtra = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", -1);
			Log.d("com.resmed.refresh.bluetooth", " BLUETOOTH DEVICE PAIRED! Name : " + bluetoothDevice.getName() + " Bound : " + bluetoothDevice.getAddress() + " bonded state : " + intExtra);
			if (bluetoothDevice != null && 12 == intExtra) {
				this.bluetoothManager.cancelDiscovery();
				this.bluetoothManager.connectDevice(bluetoothDevice);
			} else if ((bluetoothDevice == null || 11 != intExtra) && bluetoothDevice != null && 10 == intExtra) {
				this.bluetoothManager.connectDevice(bluetoothDevice);
			}
		}
	}
}
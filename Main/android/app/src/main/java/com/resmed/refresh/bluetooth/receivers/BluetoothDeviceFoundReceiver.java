package com.resmed.refresh.bluetooth.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.resmed.refresh.bluetooth.BluetoothSetup;

public class BluetoothDeviceFoundReceiver
		extends BroadcastReceiver {
	private BluetoothSetup bluetoothManager;

	public BluetoothDeviceFoundReceiver(BluetoothSetup paramRefreshBluetoothManager) {
		this.bluetoothManager = paramRefreshBluetoothManager;
	}

	public void onReceive(Context paramContext, Intent paramIntent) {
	}
}
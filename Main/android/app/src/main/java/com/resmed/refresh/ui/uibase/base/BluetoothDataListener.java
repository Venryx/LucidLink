package com.resmed.refresh.ui.uibase.base;

import android.os.Bundle;

import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.json.JsonRPC;

public interface BluetoothDataListener {
	void handleBreathingRate(Bundle paramBundle);

	void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE);

	void handleEnvSample(Bundle paramBundle);

	void handleReceivedRpc(JsonRPC paramJsonRPC);

	void handleSleepSessionStopped(Bundle paramBundle);

	void handleStreamPacket(Bundle paramBundle);

	void handleUserSleepState(Bundle paramBundle);
}
package com.resmed.refresh.bed;

import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;

public interface BedCommandsRPCMapper {
	JsonRPC clearBuffers();

	JsonRPC closeSession();

	JsonRPC fillBuffer(int paramInt);

	JsonRPC getBioSensorSerialNumber();

	JsonRPC getOperationalStatus();

	int getRPCid();

	JsonRPC getSampleNumber(boolean paramBoolean);

	JsonRPC getSerialNumber();

	JsonRPC leds(LedsState paramLedsState);

	JsonRPC openSession(String paramString);

	JsonRPC putSerialNumber(String paramString);

	JsonRPC reset();

	JsonRPC resetEngineeringMode();

	JsonRPC setBioSensorSerialNumber(String paramString);

	void setContextBroadcaster(BaseBluetoothActivity paramBaseBluetoothActivity);

	void setRPCid(int paramInt);

	JsonRPC startNightTracking();

	JsonRPC startRealTimeStream();

	JsonRPC stopNightTimeTracking();

	JsonRPC stopRealTimeStream();

	JsonRPC transmitPacket(int paramInt, boolean paramBoolean1, boolean paramBoolean2);

	JsonRPC upgradeFirmware(byte[] paramArrayOfByte, int paramInt, boolean paramBoolean);
}
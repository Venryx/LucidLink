package com.resmed.refresh.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Environment;

import com.resmed.edflib.EdfFileManager;
import com.resmed.edflib.EdfLibCallbackHandler;
import com.resmed.edflib.RstEdfMetaData;
import com.resmed.refresh.bluetooth.exception.BluetoohNotSupportedException;
import com.resmed.refresh.packets.VLP;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.sleepsession.SleepSessionManager;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshBluetoothManager;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;

public class BluetoothManagerMock
		implements RefreshBluetoothManager, EdfLibCallbackHandler {
	private BluetoothAdapter bluetoothAdapter;
	private RefreshBluetoothServiceClient bluetoothService;
	private EdfFileManager edfManager;
	private SleepSessionManager sleepSessionManager;

	public BluetoothManagerMock(RefreshBluetoothServiceClient paramRefreshBluetoothServiceClient)
			throws BluetoohNotSupportedException {
		this.bluetoothService = paramRefreshBluetoothServiceClient;
		this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		RstEdfMetaData localRstEdfMetaData = new RstEdfMetaData();
		this.edfManager = new EdfFileManager(Environment.getExternalStorageDirectory(), "BM1_01-12bits_MIMQ_16Hz.edf", localRstEdfMetaData.toArray(), this, paramRefreshBluetoothServiceClient.getContext());
	}

	public void bluetoothOff() {
	}

	public boolean cancelDiscovery() {
		return true;
	}

	public void cancelReconnection() {
	}

	public void connectDevice(BluetoothDevice paramBluetoothDevice) {
		setConnectionStatusAndNotify(CONNECTION_STATE.SESSION_OPENED, true);
	}

	public void disable() {
	}

	public void discoverResMedDevices(boolean paramBoolean) {
	}

	public void enable() {
	}

	public BluetoothAdapter getBluetoothAdapter() {
		return this.bluetoothAdapter;
	}

	public CONNECTION_STATE getConnectionStatus() {
		return CONNECTION_STATE.SOCKET_CONNECTED;
	}

	public void handleNewPacket(ByteBuffer paramByteBuffer) {
		Log.d("com.resmed.refresh.bluetooth", "BluetoothMock::handleNewPacket() , decobbed data size : " + paramByteBuffer.array().length + "to depacktize :" + Arrays.toString(paramByteBuffer.array()));
		VLP.VLPacket localVLPacket = VLP.getInstance().Depacketize(paramByteBuffer);
		String str = new String(localVLPacket.buffer);
		Log.d("com.resmed.refresh.bluetooth", "BluetoothMock::handleNewPacket() processed new bluetooth data : " + str + "packet type : " + localVLPacket.packetType + " size : " + localVLPacket.buffer.length);
		if (VLPacketType.PACKET_TYPE_RETURN.ordinal() == localVLPacket.packetType) {
			Log.d("com.resmed.refresh.bluetooth", VLPacketType.PACKET_TYPE_RETURN + " ordinal : " + VLPacketType.PACKET_TYPE_RETURN.ordinal());
		}
		this.bluetoothService.handlePacket(localVLPacket);
	}

	public boolean isBluetoothEnabled() {
		return true;
	}

	public boolean isDevicePaired(BluetoothDevice paramBluetoothDevice) {
		return true;
	}

	public void manageConnection(BluetoothSocket paramBluetoothSocket) {
	}

	public void onDigitalSamplesRead(int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
		Log.d("com.resmed.refresh.ui", "BluetoothManagerMock::onDigitalSamplesRead mi_buf : " + Arrays.toString(paramArrayOfInt1) + " mq_buf : " + Arrays.toString(paramArrayOfInt2));
		if (this.sleepSessionManager != null) {
			this.sleepSessionManager.addSamplesMiMq(paramArrayOfInt1, paramArrayOfInt2);
		}
	}

	public void onFileClosed() {
	}

	public void onFileCompressed(int paramInt) {
	}

	public void onFileFixed() {
	}

	public void onFileOpened() {
	}

	public void onWroteDigitalSamples() {
	}

	public void onWroteMetadata() {
	}

	public void pairDevice(BluetoothDevice paramBluetoothDevice) {
	}

	public Set<BluetoothDevice> queryPairedDevices() {
		return null;
	}

	public void setConnectionStatusAndNotify(CONNECTION_STATE paramCONNECTION_STATE, boolean paramBoolean) {
		if (paramBoolean) {
			Intent localIntent = new Intent("ACTION_RESMED_CONNECTION_STATUS");
			localIntent.putExtra("EXTRA_RESMED_CONNECTION_STATE", paramCONNECTION_STATE);
			this.bluetoothService.getContext().sendStickyOrderedBroadcast(localIntent, null, null, -1, null, null);
		}
	}

	public void testStreamData(SleepSessionManager paramSleepSessionManager) {
		this.sleepSessionManager = paramSleepSessionManager;
		this.edfManager.readDigitalSamples();
	}

	public void unpairAll(String paramString) {
	}

	public boolean unpairDevice(BluetoothDevice paramBluetoothDevice) {
		return true;
	}

	public void writeData(byte[] paramArrayOfByte) {
	}
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*
 * Decompiled with CFR 0_115.
 *
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.PendingIntent
 *  android.app.Service
 *  android.bluetooth.BluetoothDevice
 *  android.content.Context
 *  android.content.Intent
 *  android.content.ServiceConnection
 *  android.content.SharedPreferences
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.Message
 *  android.os.Messenger
 *  android.os.RemoteException
 *  android.preference.PreferenceManager
 *  android.support.v4.app.NotificationCompat
 *  android.support.v4.app.NotificationCompat$Builder
 *  com.google.gson.Gson
 *  com.resmed.edflib.EdfLibJNI
 *  com.resmed.refresh.bluetooth.BluetoothSetup
 *  com.resmed.refresh.bluetooth.CONNECTION_STATE
 *  com.resmed.refresh.bluetooth.RefreshBluetoothService
 *  com.resmed.refresh.bluetooth.RefreshBluetoothService$1
 *  com.resmed.refresh.bluetooth.RefreshBluetoothService$BluetoothRequestsHandler
 *  com.resmed.refresh.bluetooth.RefreshBluetoothServiceClient
 *  com.resmed.refresh.bluetooth.exception.BluetoohNotSupportedException
 *  com.resmed.refresh.model.json.JsonRPC
 *  com.resmed.refresh.model.json.ResultRPC
 *  com.resmed.refresh.packets.PacketsByteValuesReader
 *  com.resmed.refresh.packets.VLP
 *  com.resmed.refresh.packets.VLP$VLPacket
 *  com.resmed.refresh.packets.VLPacketType
 *  com.resmed.refresh.sleepsession.SleepSessionManager
 *  com.resmed.refresh.ui.activity.SleepTimeActivity
 *  com.resmed.refresh.utils.AppFileLog
 *  com.resmed.refresh.utils.BluetoothDataSerializeUtil
 *  com.resmed.refresh.utils.Log
 *  com.resmed.refresh.utils.RefreshBluetoothManager
 *  com.resmed.refresh.utils.RefreshTools
 *  com.resmed.rm20.RM20JNI
 */
package com.resmed.refresh.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.resmed.edflib.EdfLibJNI;
import com.resmed.refresh.bluetooth.exception.BluetoohNotSupportedException;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.model.json.ResultRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLP;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.sleepsession.SleepSessionManager;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.BluetoothDataSerializeUtil;
import com.resmed.refresh.utils.LOGGER;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshBluetoothManager;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.rm20.RM20JNI;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Serializable;
import java.util.Date;

import v.lucidlink.LL;
import v.lucidlink.MainActivity;
import v.lucidlink.R;
import v.lucidlink.V;

final class MessageType {
	static final int SendBluetoothMessage = 2;
}

public class RefreshBluetoothService extends Service implements RefreshBluetoothServiceClient {
	public static final String ACTION_ALREADY_PAIRED = "ACTION_ALREADY_PAIRED";
	public static final String REFRESH_BED_NEW_CONN_STATUS = "REFRESH_BED_NEW_CONN_STATUS";
	public static final String REFRESH_BED_NEW_DATA = "REFRESH_BED_NEW_DATA";
	public static final String REFRESH_BED_NEW_DATA_SIZE = "REFRESH_BED_NEW_DATA_SIZE";
	public static final String REFRESH_BED_NEW_DATA_TYPE = "REFRESH_BED_NEW_DATA_TYPE";
	public static final String REFRESH_BT_SERVICE_RUNNING = "REFRESH_BT_SERVICE_RUNNING";
	//public RefreshBluetoothManager bluetoothManager;
	public BluetoothSetup bluetoothManager;
	private boolean isBindToClient;
	private int isDataAvailableOnDevice;
	private boolean isHandleBulkTransfer;

	//private Messenger mClient;
	public void mClient_send(Message message) {
		//mClient.send(message);

		// for some reason the above is not working atm, so just call the method directly
		try {
			//MainActivity.main.mFromService.send(message);
			MainActivity.main.mFromServiceHandler.handleMessage(message);
		} catch (Exception e) {
			//e.printStackTrace();
			throw new Error(e);
		}
	}

	final Messenger mMessenger;
	private SharedPreferences prefs;
	public boolean sessionToBeStopped;
	private SleepSessionManager sleepSessionManager;

	public class BluetoothRequestsHandler extends Handler {
		public void handleMessage(Message msg) {
			RefreshTools.writeTimeStampToFile(RefreshBluetoothService.this.getApplicationContext(), System.currentTimeMillis());
			RefreshBluetoothService.this.checkManager();
			Bundle b;
			switch (msg.what) {
				case 1:
					RefreshBluetoothService.this.bluetoothManager.discoverResMedDevices(true);
					return;
				case MessageType.SendBluetoothMessage:
					Bundle bundle1 = msg.getData();
					if (bundle1 != null && RefreshBluetoothService.this.bluetoothManager != null) {
						byte[] toSend = bundle1.getByteArray("bytes");
						Log.d(LOGGER.TAG_BLUETOOTH, " bytes sending : " + toSend);
						RefreshBluetoothService.this.bluetoothManager.writeData(toSend);
						RefreshBluetoothService.this.isBindToClient = true;
						return;
					}
					return;
				case 4:
					/*RefreshBluetoothService.this.mClient = msg.replyTo;
					V.Log("Registered client:" + RefreshBluetoothService.this.mClient);*/
					Message message = new Message();
					message.what = 4;
					try {
						//RefreshBluetoothService.this.mClient.send(message);
						mClient_send(message);
					} catch (Exception e) {
						e.printStackTrace();
					}
					RefreshBluetoothService.this.sendConnectionStatus(LL.main.connectionState);
					return;
				case 6:
					RefreshBluetoothService.this.sendConnectionStatus(LL.main.connectionState);
					return;
				case 7:
					RefreshBluetoothService.this.bluetoothManager.disable();
					return;
				case 9:
					//RefreshBluetoothService.this.mClient = null;
					return;
				case 10:
					RefreshBluetoothService.this.bluetoothManager.discoverResMedDevices(false);
					return;
				case 11:
					Bundle bundle = msg.getData();
					Parcelable deviceInfo = bundle.getParcelable("deviceInfo");
					boolean makePaired = bundle.getBoolean("makePaired");
					BluetoothDevice bluetoothDevice = (BluetoothDevice) deviceInfo;
					if (RefreshBluetoothService.this.bluetoothManager.isDevicePaired(bluetoothDevice)) {
						Log.d("com.resmed.refresh.pair", (" RefreshBluetoothService connecting to device : " + (Object) bluetoothDevice));
						RefreshBluetoothService.this.bluetoothManager.connectDevice(bluetoothDevice);
						return;
					}
					if (makePaired) {
						Log.d("com.resmed.refresh.pair", (" RefreshBluetoothService pairing to device : " + (Object) bluetoothDevice));
						RefreshBluetoothService.this.bluetoothManager.pairDevice(bluetoothDevice);
						return;
					}
					Log.d("com.resmed.refresh.pair", (" RefreshBluetoothService the device : " + (Object) bluetoothDevice + " has been unpair"));
					Message message4 = new Message();
					message4.what = 8;
					RefreshBluetoothService.this.sendMessageToClient(message4);
					return;
				case 12:
					//RefreshBluetoothService.this.changeToForeground();
					RefreshBluetoothService.this.sleepSessionManager = new SleepSessionManager(RefreshBluetoothService.this);
					long sessionId = -1;
					int age = 44;
					int gender = 0;
					b = msg.getData();
					if (b != null) {
						sessionId = b.getLong("sessionId");
						age = b.getInt("age", 44);
						gender = b.getInt("gender", 0);
					}
					RefreshBluetoothService.this.sleepSessionManager.start(sessionId, age, gender);
					RefreshBluetoothService.this.bluetoothManager.testStreamData(RefreshBluetoothService.this.sleepSessionManager);
					long stored = RefreshBluetoothService.this.prefs.getLong(Consts.PREF_NIGHT_LAST_SESSION_ID, -1);
					SharedPreferences.Editor editor = RefreshBluetoothService.this.prefs.edit();
					editor.putLong(Consts.PREF_NIGHT_LAST_SESSION_ID, sessionId);
					editor.putInt(Consts.PREF_NIGHT_TRACK_STATE, CONNECTION_STATE.NIGHT_TRACK_ON.ordinal());
					editor.putInt(Consts.PREF_AGE, age);
					editor.putInt(Consts.PREF_GENDER, gender);
					editor.commit();
					AppFileLog.addTrace("MSG_SLEEP_SESSION_START PREF_NIGHT_LAST_SESSION_ID was " + stored + " and now is " + sessionId);
					Log.d(LOGGER.TAG_BLUETOOTH, "Bluetooth service current session id : " + sessionId + " stored : " + stored);
					return;
				case 14:
					Log.d(LOGGER.TAG_BLUETOOTH, "Bluetooth service isHandleBulkTransfer : " + RefreshBluetoothService.this.isHandleBulkTransfer);
					RefreshBluetoothService.this.stopCurrentSession();
					RefreshBluetoothService.this.isDataAvailableOnDevice = -1;
					return;
				case 16:
					Bundle bAlarm = msg.getData();
					if (bAlarm != null) {
						long alarmTs = bAlarm.getLong(Consts.BUNDLE_SMART_ALARM_TIMESTAMP);
						int alarmWindowSeconds = bAlarm.getInt(Consts.BUNDLE_SMART_ALARM_WINDOW);
						Log.d(LOGGER.TAG_SMART_ALARM, "did RefreshBluetoothServiceClient::MSG_SLEEP_SET_SMART_ALARM ");
						if (RefreshBluetoothService.this.sleepSessionManager != null) {
							RefreshBluetoothService.this.sleepSessionManager.setRM20AlarmTime(new Date(alarmTs), alarmWindowSeconds);
							return;
						}
						return;
					}
					return;
				case 17:
					if (RefreshBluetoothService.this.sleepSessionManager != null) {
						RefreshBluetoothService.this.sleepSessionManager.requestUserSleepState();
						return;
					}
					return;
				case 21:
					long sessionToRecover = msg.getData().getLong(Consts.PREF_NIGHT_LAST_SESSION_ID, -1);
					if (RefreshBluetoothService.this.prefs == null) {
						RefreshBluetoothService.this.prefs = PreferenceManager.getDefaultSharedPreferences(RefreshBluetoothService.this.getApplicationContext());
					}
					int ageToRecover = RefreshBluetoothService.this.prefs.getInt(Consts.PREF_AGE, 44);
					int genderToRecover = RefreshBluetoothService.this.prefs.getInt(Consts.PREF_GENDER, 0);
					AppFileLog.addTrace("RefreshBluetoothService::MSG_SLEEP_SESSION_RECOVER, sessionToRecover : " + sessionToRecover);
					if (RefreshBluetoothService.this.sleepSessionManager != null) {
						AppFileLog.addTrace("RefreshBluetoothService::MSG_SLEEP_SESSION_RECOVER, sleepSessionManager.getSessionId() : " + RefreshBluetoothService.this.sleepSessionManager.getSessionId());
						AppFileLog.addTrace("RefreshBluetoothService::MSG_SLEEP_SESSION_RECOVER, sleepSessionManager.isActive() : " + RefreshBluetoothService.this.sleepSessionManager.isActive);
					}
					if ((RefreshBluetoothService.this.sleepSessionManager == null || RefreshBluetoothService.this.sleepSessionManager.getSessionId() == sessionToRecover || RefreshBluetoothService.this.sleepSessionManager.isActive) && RefreshBluetoothService.this.sleepSessionManager != null) {
						Message recoverMsg = new Message();
						recoverMsg.what = 21;
						RefreshBluetoothService.this.sendMessageToClient(recoverMsg);
						return;
					}
					RefreshBluetoothService.this.sleepSessionManager = new SleepSessionManager(RefreshBluetoothService.this);
					RefreshBluetoothService.this.sleepSessionManager.recoverSession(sessionToRecover, ageToRecover, genderToRecover);
					return;
				case 22:
					RefreshBluetoothService.this.bluetoothManager.cancelDiscovery();
					return;
				case RefreshBluetoothServiceClient.MSG_BeD_UNPAIR_ALL /*23*/:
					Log.d(LOGGER.TAG_BLUETOOTH, "RefreshBluetoothService::MSG_BeD_UNPAIR_ALL, unpairing all : ");
					RefreshBluetoothService.this.bluetoothManager.unpairAll("S+");
					return;
				case RefreshBluetoothServiceClient.MSG_BLUETOOTH_BULK_TRANSFER_START /*24*/:
					RefreshBluetoothService.this.isHandleBulkTransfer = true;
					Log.d(LOGGER.TAG_BLUETOOTH, " RefreshBluetoothService isHandleBulkTransfer : " + RefreshBluetoothService.this.isHandleBulkTransfer);
					return;
				case RefreshBluetoothServiceClient.MSG_BLUETOOTH_BULK_TRANSFER_STOP /*25*/:
					RefreshBluetoothService.this.isHandleBulkTransfer = false;
					return;
				case RefreshBluetoothServiceClient.MSG_SMARTALARM_UPDATE /*26*/:
					if (RefreshBluetoothService.this.sleepSessionManager != null) {
						Log.d(LOGGER.TAG_SMART_ALARM, "RefreshBluetoothService MSG_SMARTALARM_UPDATE");
						b = msg.getData();
						RefreshBluetoothService.this.sleepSessionManager.updateAlarmSettings(b.getLong(Consts.SMART_ALARM_TIME_VALUE), b.getInt(Consts.SMART_ALARM_WINDOW_VALUE));
						return;
					}
					return;
				case RefreshBluetoothServiceClient.MSG_REQUEST_BeD_AVAILABLE_DATA /*27*/:
					Message bedDataMessage = new Message();
					bedDataMessage.what = 27;
					Bundle data = new Bundle();
					data.putInt(Consts.BUNDLE_BED_AVAILABLE_DATA, RefreshBluetoothService.this.isDataAvailableOnDevice);
					bedDataMessage.setData(data);
					RefreshBluetoothService.this.sendMessageToClient(bedDataMessage);
					return;
				case RefreshBluetoothServiceClient.MSG_RESET_BeD_AVAILABLE_DATA /*28*/:
					RefreshBluetoothService.this.isDataAvailableOnDevice = -1;
					return;
				case RefreshBluetoothServiceClient.MSG_BLUETOOTH_RADIO_RESET /*29*/:
					if (RefreshBluetoothService.this.bluetoothManager != null) {
						RefreshBluetoothService.this.bluetoothManager.bluetoothOff();
						return;
					}
					return;
				default:
					return;
			}
		}
	}


	public static RefreshBluetoothService main;

	public RefreshBluetoothService() {
		main = this;

		V.JavaLog("Starting bluetooth service!");
		this.mMessenger = new Messenger(new BluetoothRequestsHandler());
		this.isBindToClient = true;
		this.isHandleBulkTransfer = false;
		this.sessionToBeStopped = false;
	}

	private void checkManager() {
		if (this.bluetoothManager != null) return;
		try {
			this.bluetoothManager = new BluetoothSetup(this);
			this.bluetoothManager.enable();
			return;
		} catch (BluetoohNotSupportedException var1_1) {
			var1_1.printStackTrace();
			return;
		}
	}

	private void checkNightTrack() {
		this.isBindToClient = false;
		//new Handler().postDelayed((Runnable) new RefreshBluetoothService(), 120000L);
	}

	private void recoverSleepSession(long l) {
		this.sleepSessionManager = new SleepSessionManager(this);
		int n = this.prefs.getInt("PREF_AGE", 44);
		int n2 = this.prefs.getInt("PREF_GENDER", 0);
		this.sleepSessionManager.recoverSession(l, n, n2);
	}

	private void stopCurrentSession() {
		AppFileLog.addTrace("MSG_SLEEP_SESSION_STOP Service received.");
		if (this.sleepSessionManager != null) {
			this.sleepSessionManager.stop();
		}
		this.sleepSessionManager = null;
		this.bluetoothManager.cancelReconnection();
		this.stopForeground(true);
	}

	public Context getContext() {
		return this;
	}

	public void handlePacket(VLP.VLPacket packet) {
		Message newMessage;
		Bundle data;
		AppFileLog.addTrace(" _ ");
		if (VLPacketType.PACKET_TYPE_RETURN.ordinal() == packet.packetType) {
			AppFileLog.addTrace("Service handlePacket VLPacketType.PACKET_TYPE_RETURN");
			String str = new String(packet.buffer);
			AppFileLog.addTrace("Service handlePacket returnStr: " + str.replace(" ", "").replace("\n", " ").replace("\t", " "));
			ResultRPC result = ((JsonRPC) new Gson().fromJson(str, JsonRPC.class)).getResult();
			if (result != null) {
				String payload = result.getPayload();
				if (payload != null) {
					AppFileLog.addTrace("Service handlePacket payload: " + payload);
					if (payload.equalsIgnoreCase("TRUE")) {
						Log.d(LOGGER.TAG_BLUETOOTH, " RefreshBluetoothService isRecovering : ");
						if (this.sleepSessionManager != null) {
							//this.sleepSessionManager.addSamplesMiMq(new File(RefreshTools.getFilesPath(), getString(R.string.bluetooth_buffer_bulk_file)));
							AppFileLog.addTrace("Service handlePacket sleepSessionManager.addSamplesMiMq(filesDir)");
						}
						BluetoothDataSerializeUtil.deleteBulkDataBioFile(getApplicationContext());
						this.isHandleBulkTransfer = false;
						AppFileLog.addTrace("Service handlePacket isHandleBulkTransfer = false");
					}
				}
			}
		} else {
			AppFileLog.addTrace("Service handlePacket is NOT a PACKET_TYPE_RETURN isHandleBulkTransfer=" + this.isHandleBulkTransfer);
			if (this.isHandleBulkTransfer) {
				if (VLPacketType.PACKET_TYPE_BIO_64.ordinal() == packet.packetType || VLPacketType.PACKET_TYPE_BIO_32.ordinal() == packet.packetType) {
					BluetoothDataSerializeUtil.writeBulkBioDataFile(getApplicationContext(), packet.buffer);
					return;
				}
			}
			if (this.sleepSessionManager != null && this.sleepSessionManager.isActive) {
				if (VLPacketType.PACKET_TYPE_BIO_64.ordinal() == packet.packetType) {
					this.sleepSessionManager.addBioData(packet.buffer, packet.packetNo);
				} else {
					if (VLPacketType.PACKET_TYPE_ENV_60.ordinal() == packet.packetType) {
						this.sleepSessionManager.addEnvData(packet.buffer, this.sleepSessionManager.isActive);
					} else {
						if (VLPacketType.PACKET_TYPE_ENV_1.ordinal() == packet.packetType) {
							this.sleepSessionManager.addEnvData(packet.buffer, this.sleepSessionManager.isActive);
						} else {
							if (VLPacketType.PACKET_TYPE_BIO_32.ordinal() == packet.packetType) {
								this.sleepSessionManager.addBioData(packet.buffer, packet.packetNo);
							}
						}
					}
				}
			}
			if (VLPacketType.PACKET_TYPE_NOTE_STORE_FOREIGN.ordinal() == packet.packetType || VLPacketType.PACKET_TYPE_NOTE_STORE_LOCAL.ordinal() == packet.packetType) {
				int storeLocal = PacketsByteValuesReader.getStoreLocalBio(packet.buffer);
				Log.w(LOGGER.TAG_UI, "PACKET_TYPE_NOTE_STORE!!! = " + packet.packetType + " NUMBER OF SAMPLES : " + storeLocal);
				if (storeLocal >= 32) {
					this.isDataAvailableOnDevice = packet.packetType;
					Message mDataAvailableToClient = new Message();
					mDataAvailableToClient.what = 27;
					mDataAvailableToClient.getData().putInt(Consts.BUNDLE_BED_AVAILABLE_DATA, this.isDataAvailableOnDevice);
					sendMessageToClient(mDataAvailableToClient);
				} else {
					AppFileLog.addTrace("Ignoring samples on BeD because : " + storeLocal + " samples < 32");
				}
			}
		}
		int length = packet.buffer.length;
		int nrOfMessages = 1;
		while (length >= 1048000) {
			length /= 2;
			nrOfMessages++;
		}
		byte[] msgBuff = new byte[length];
		AppFileLog.addTrace("Service handlePacket packet length=" + length);
		ByteArrayInputStream bStream = new ByteArrayInputStream(packet.buffer, 0, packet.buffer.length);
		for (int i = 0; i < nrOfMessages; i++) {
			Log.d(LOGGER.TAG_BLUETOOTH, " handlePacket, ready to send to clients. nrOFMessages : " + nrOfMessages);
			int nrBytesRead = bStream.read(msgBuff, length * i, length);
			AppFileLog.addTrace("Service handlePacket nrBytesRead=" + nrBytesRead);
			if (-1 != nrBytesRead) {
				newMessage = new Message();
				newMessage.what = 19;
				data = new Bundle();
				data.putByteArray(REFRESH_BED_NEW_DATA, msgBuff);
				data.putByte(REFRESH_BED_NEW_DATA_TYPE, packet.packetType);
				data.putInt(REFRESH_BED_NEW_DATA_SIZE, packet.packetSize);
				newMessage.setData(data);
				sendMessageToClient(newMessage);
			}
		}
		AppFileLog.addTrace("Service handlePacket Sending MSG_BeD_STREAM_PACKET_PARTIAL_END");
		AppFileLog.addTrace(" _ ");
		newMessage = new Message();
		newMessage.what = 20;
		data = new Bundle();
		data.putByte(REFRESH_BED_NEW_DATA_TYPE, packet.packetType);
		data.putInt(REFRESH_BED_NEW_DATA_SIZE, packet.packetSize);
		newMessage.setData(data);
		sendMessageToClient(newMessage);
	}


	public IBinder onBind(Intent intent) {
		AppFileLog.addTrace("SERVICE onBind");
		Log.d("com.resmed.refresh.bluetooth", " onBind! ");
		return this.mMessenger.getBinder();
	}

	public void onDestroy() {
		AppFileLog.addTrace("SERVICE onDestroy called");
		Log.d("com.resmed.refresh.bluetooth", "::onDestroy, bluetooth");
		super.onDestroy();
	}

	public void onLowMemory() {
		super.onLowMemory();
		AppFileLog.addTrace("SERVICE onLowMemory called");
	}

	public void onRebind(Intent intent) {
		AppFileLog.addTrace("SERVICE onRebind");
		Log.d("com.resmed.refresh.bluetooth", " onRebind! ");
		super.onRebind(intent);
	}

	boolean listening;
	public void StartListening() {
		if (listening) return;
		listening = true;

		V.JavaLog("Start listening;" + MainActivity.main + ";" + this.getApplicationContext() + ";" + LL.main);

		Log.d("com.resmed.refresh.bluetooth", "::onStartCommand, bluetooth");
		EdfLibJNI.loadLibrary(this.getApplicationContext());
		RM20JNI.loadLibrary(this.getApplicationContext());

		try {
			// rather than remember last one, just find first device and connect to it
			main.bluetoothManager = new BluetoothSetup(main);
			main.bluetoothManager.enable();
			main.bluetoothManager.discoverResMedDevices(true);
		} catch (Exception ex) {
			throw new Error(ex);
		}
	}

	public int onStartCommand(Intent intent, int n, int n2) {
		Log.d("com.resmed.refresh.bluetooth", "::onStartCommand, bluetooth");
		EdfLibJNI.loadLibrary(this.getApplicationContext());
		RM20JNI.loadLibrary(this.getApplicationContext());

		/*V.WaitXThenRun(1000, ()-> {
			if (LL.main == null) return;
			StartListening();
		});*/

		/*try {
			this.bluetoothManager = new BluetoothSetup(this);
			this.bluetoothManager.enable();
			this.prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
			int n3 = this.prefs.getInt("PREF_CONNECTION_STATE", -1);
			long l = this.prefs.getLong("PREF_NIGHT_LAST_SESSION_ID", -1);
			AppFileLog.addTrace(("SERVICE onStartCommand LastState: " + n3 + " Last sessionID : " + l + " state : " + n3));
			Log.d("com.resmed.refresh.ui", (" RefreshBluetoothService::onStartCommand conn state : " + n3 + " session id :" + l));
			if (CONNECTION_STATE.NIGHT_TRACK_ON.ordinal() == n3) {
				this.checkNightTrack();
				AppFileLog.addTrace("SERVICE onStartCommand overnight in progress");
				Log.d("com.resmed.refresh.ui", " RefreshBluetoothService::onStartCommand overnight in progress");
				this.recoverSleepSession(l);
				BluetoothDevice bluetoothDevice = BluetoothDataSerializeUtil.readJsonFile(this.getApplicationContext());
				this.bluetoothManager.connectDevice(bluetoothDevice);

				/*Intent intent2 = new Intent(this, SleepTimeActivity.class);
				intent2.setFlags(268468224);
				intent2.putExtra("com.resmed.refresh.consts.recovering_app_from_service", true);
				this.startActivity(intent2);*#/

				Intent intent3 = new Intent("BLUETOOTH_SERVICE_INTENT_RESTART");
				intent3.putExtra("BUNDLE_LAST_CONN_STATE", n3);
				this.getApplicationContext().sendStickyBroadcast(intent3);
				//this.changeToForeground();
				return 1;
			}
			if (CONNECTION_STATE.NIGHT_TRACK_OFF.ordinal() != n3) return 1;
			Intent intent4 = new Intent("BLUETOOTH_SERVICE_INTENT_RESTART");
			intent4.putExtra("BUNDLE_LAST_CONN_STATE", n3);
			this.getApplicationContext().sendStickyBroadcast(intent4);
			AppFileLog.addTrace("SERVICE RESTARTED AFTER SLEEP SESSION WAS STOPPED. How to recover ?");
			return 1;
		} catch (BluetoohNotSupportedException var5_10) {
			var5_10.printStackTrace();
		}
		return 1;*/
		return 1;
	}

	public void ReactToFoundDevice(BluetoothDevice device) {
		try {
			/*this.bluetoothManager = new BluetoothSetup(this);
			this.bluetoothManager.enable();
			this.checkNightTrack();
			AppFileLog.addTrace("SERVICE onStartCommand overnight in progress");
			Log.d("com.resmed.refresh.ui", " RefreshBluetoothService::onStartCommand overnight in progress");
			bluetoothManager.pairDevice(device);
			bluetoothManager.connectDevice(device);
			bluetoothManager.enable();
			this.bluetoothManager.connectDevice(device);*/

			/*Intent intent2 = new Intent(this, SleepTimeActivity.class);
			intent2.setFlags(268468224);
			intent2.putExtra("com.resmed.refresh.consts.recovering_app_from_service", true);
			this.startActivity(intent2);*/

			MainActivity.main.pairAndConnect(device);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendConnectionStatus(CONNECTION_STATE cONNECTION_STATE) {
		Log.d("com.resmed.refresh.pair", ("Service sendConnectionStatus : " + CONNECTION_STATE.toString((CONNECTION_STATE) cONNECTION_STATE)));
		/*if (this.mClient == null) {
			Intent intent = new Intent("ACTION_RESMED_CONNECTION_STATUS");
			intent.putExtra("EXTRA_RESMED_CONNECTION_STATE", (Serializable) cONNECTION_STATE);
			this.sendStickyBroadcast(intent);
			return;
		}*/
		Message message = new Message();
		message.what = 6;
		Bundle bundle = new Bundle();
		bundle.putSerializable("REFRESH_BED_NEW_CONN_STATUS", (Serializable) cONNECTION_STATE);
		message.setData(bundle);
		this.sendMessageToClient(message);
	}

	public void sendMessageToClient(Message message) {
		//V.Log("Sending message to client;" + this.mClient + ";" + message.what);
		V.Log("Sending message to client;" + message.what);

		/*if (this.mClient == null) return;
		try {
			this.mClient.send(message);
		} catch (RemoteException e) {
			e.printStackTrace();
		}*/
		mClient_send(message);
	}

	public void unbindService(ServiceConnection serviceConnection) {
		AppFileLog.addTrace("SERVICE onBind");
		Log.d("com.resmed.refresh.bluetooth", " unBind! ");
		super.unbindService(serviceConnection);
	}
}

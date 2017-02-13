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
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.BluetoothDataSerializeUtil;
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
import v.lucidlink.V;

class BluetoothRequestsHandler extends Handler {
	final /* synthetic */ RefreshBluetoothService service;

	public BluetoothRequestsHandler(RefreshBluetoothService refreshBluetoothService) {
		this.service = refreshBluetoothService;
	}

	public void handleMessage(Message message) {
		V.Log("Handle message:" + message.what);
		RefreshTools.writeTimeStampToFile(this.service.getApplicationContext(), System.currentTimeMillis());
		RefreshBluetoothService.access$0(this.service);
		switch (message.what) {
			default: {
				return;
			}
			case 29: {
				if (this.service.bluetoothManager == null)
					return;
				this.service.bluetoothManager.bluetoothOff();
				return;
			}
			case 27: {
				Message message2 = new Message();
				message2.what = 27;
				Bundle bundle = new Bundle();
				bundle.putInt("BUNDLE_BED_AVAILABLE_DATA", RefreshBluetoothService.access$2(this.service));
				message2.setData(bundle);
				this.service.sendMessageToClient(message2);
				return;
			}
			case 28: {
				RefreshBluetoothService.access$3(this.service, -1);
				return;
			}
			case 24: {
				RefreshBluetoothService.access$4(this.service, true);
				Log.d("com.resmed.refresh.bluetooth", (" RefreshBluetoothService isHandleBulkTransfer : " + RefreshBluetoothService.access$5(this.service)));
				return;
			}
			case 25: {
				RefreshBluetoothService.access$4(this.service, false);
				return;
			}
			case 7: {
				this.service.bluetoothManager.disable();
				return;
			}
			case 6: {
				this.service.sendConnectionStatus(LL.main.connectionState);
				return;
			}
			case 4: {
				RefreshBluetoothService.access$6(this.service, message.replyTo);
				Message message3 = new Message();
				message3.what = 4;
				try {
					RefreshBluetoothService.access$7(this.service).send(message3);
				} catch (RemoteException var48_5) {
					var48_5.printStackTrace();
				}
				this.service.sendConnectionStatus(LL.main.connectionState);
				return;
			}
			case 9: {
				RefreshBluetoothService.access$6(this.service, null);
				return;
			}
			case 1: {
				this.service.bluetoothManager.discoverResMedDevices(true);
				return;
			}
			case 10: {
				this.service.bluetoothManager.discoverResMedDevices(false);
				return;
			}
			case 22: {
				this.service.bluetoothManager.cancelDiscovery();
				return;
			}
			case 11: {
				Bundle bundle = message.getData();
				Parcelable deviceInfo = bundle.getParcelable("deviceInfo");
				boolean makePaired = bundle.getBoolean("makePaired");
				BluetoothDevice bluetoothDevice = (BluetoothDevice) deviceInfo;
				if (this.service.bluetoothManager.isDevicePaired(bluetoothDevice)) {
					Log.d("com.resmed.refresh.pair", (" RefreshBluetoothService connecting to device : " + (Object) bluetoothDevice));
					this.service.bluetoothManager.connectDevice(bluetoothDevice);
					return;
				}
				if (makePaired) {
					Log.d("com.resmed.refresh.pair", (" RefreshBluetoothService pairing to device : " + (Object) bluetoothDevice));
					this.service.bluetoothManager.pairDevice(bluetoothDevice);
					return;
				}
				Log.d("com.resmed.refresh.pair", (" RefreshBluetoothService the device : " + (Object) bluetoothDevice + " has been unpair"));
				Message message4 = new Message();
				message4.what = 8;
				this.service.sendMessageToClient(message4);
				return;
			}
			case 2: {
				Bundle bundle = message.getData();
				if (bundle == null) return;
				if (this.service.bluetoothManager == null)
					return;
				byte[] arrby = bundle.getByteArray("bytes");
				Log.d("com.resmed.refresh.bluetooth", (" bytes sending : " + arrby));
				this.service.bluetoothManager.writeData(arrby);
				RefreshBluetoothService.access$8(this.service, true);
				return;
			}
			case 12: {
				RefreshBluetoothService.access$9(this.service);
				RefreshBluetoothService.access$10(this.service, (SleepSessionManager) new SleepSessionManager(this.service));
				long l = -1;
				int n = 44;
				Bundle bundle = message.getData();
				int n2 = 0;
				if (bundle != null) {
					l = bundle.getLong("sessionId");
					n = bundle.getInt("age", 44);
					n2 = bundle.getInt("gender", 0);
				}

				SleepSessionManager sessionManager = RefreshBluetoothService.access$11(this.service);
				sessionManager.start(l, n, n2);

				this.service.bluetoothManager.testStreamData(RefreshBluetoothService.access$11(this.service));
				long l2 = RefreshBluetoothService.access$12(this.service).getLong("PREF_NIGHT_LAST_SESSION_ID", -1);
				SharedPreferences.Editor editor = RefreshBluetoothService.access$12(this.service).edit();
				editor.putLong("PREF_NIGHT_LAST_SESSION_ID", l);
				editor.putInt("PREF_CONNECTION_STATE", CONNECTION_STATE.NIGHT_TRACK_ON.ordinal());
				editor.putInt("PREF_AGE", n);
				editor.putInt("PREF_GENDER", n2);
				editor.commit();
				AppFileLog.addTrace(("MSG_SLEEP_SESSION_START PREF_NIGHT_LAST_SESSION_ID was " + l2 + " and now is " + l));
				Log.d("com.resmed.refresh.bluetooth", ("Bluetooth service current session id : " + l + " stored : " + l2));
				return;
			}
			case 26: {
				if (RefreshBluetoothService.access$11(this.service) == null)
					return;
				Log.d("com.resmed.refresh.smartAlarm", "RefreshBluetoothService MSG_SMARTALARM_UPDATE");
				Bundle bundle = message.getData();
				long l = bundle.getLong("com.resmed.refresh.consts.smart_alarm_time_value");
				int n = bundle.getInt("com.resmed.refresh.consts.smart_alarm_window_value");
				RefreshBluetoothService.access$11(this.service).updateAlarmSettings(l, n);
				return;
			}
			case 14: {
				Log.d("com.resmed.refresh.bluetooth", ("Bluetooth service isHandleBulkTransfer : " + RefreshBluetoothService.access$5(this.service)));
				RefreshBluetoothService.access$13(this.service);
				RefreshBluetoothService.access$3(this.service, (int) -1);
				return;
			}
			case 16: {
				Bundle bundle = message.getData();
				if (bundle == null) return;
				long l = bundle.getLong("BUNDLE_SMART_ALARM_TIMESTAMP");
				int n = bundle.getInt("BUNDLE_SMART_ALARM_WINDOW");
				Log.d("com.resmed.refresh.smartAlarm", "did RefreshBluetoothServiceClient::MSG_SLEEP_SET_SMART_ALARM ");
				if (RefreshBluetoothService.access$11(this.service) == null)
					return;
				Date date = new Date(l);
				RefreshBluetoothService.access$11(this.service).setRM20AlarmTime(date, n);
				return;
			}
			case 17: {
				if (RefreshBluetoothService.access$11(this.service) == null)
					return;
				RefreshBluetoothService.access$11(this.service).requestUserSleepState();
				return;
			}
			case 21: {
				long l = message.getData().getLong("PREF_NIGHT_LAST_SESSION_ID", -1);
				if (RefreshBluetoothService.access$12(this.service) == null) {
					RefreshBluetoothService.access$14(this.service, (SharedPreferences) PreferenceManager.getDefaultSharedPreferences(this.service.getApplicationContext()));
				}
				int n = RefreshBluetoothService.access$12(this.service).getInt("PREF_AGE", 44);
				int n3 = RefreshBluetoothService.access$12(this.service).getInt("PREF_GENDER", 0);
				AppFileLog.addTrace(("RefreshBluetoothService::MSG_SLEEP_SESSION_RECOVER, sessionToRecover : " + l));
				if (RefreshBluetoothService.access$11(this.service) != null) {
					AppFileLog.addTrace(("RefreshBluetoothService::MSG_SLEEP_SESSION_RECOVER, sleepSessionManager.getSessionId() : " + RefreshBluetoothService.access$11(this.service).getSessionId()));
					AppFileLog.addTrace(("RefreshBluetoothService::MSG_SLEEP_SESSION_RECOVER, sleepSessionManager.isActive() : " + RefreshBluetoothService.access$11(this.service).isActive()));
				}
				if ((RefreshBluetoothService.access$11(this.service) == null || RefreshBluetoothService.access$11(this.service).getSessionId() == l || RefreshBluetoothService.access$11(this.service).isActive()) && RefreshBluetoothService.access$11(this.service) != null) {
					Message message5 = new Message();
					message5.what = 21;
					this.service.sendMessageToClient(message5);
					return;
				}
				RefreshBluetoothService.access$10(this.service, new SleepSessionManager(this.service));
				RefreshBluetoothService.access$11(this.service).recoverSession(l, n, n3);
				return;
			}
			case 23:
		}
		Log.d("com.resmed.refresh.bluetooth", "RefreshBluetoothService::MSG_BeD_UNPAIR_ALL, unpairing all : ");
		this.service.bluetoothManager.unpairAll("S+");
	}
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
	private Messenger mClient;
	final Messenger mMessenger;
	private SharedPreferences prefs;
	public boolean sessionToBeStopped;
	private SleepSessionManager sleepSessionManager;

	public static RefreshBluetoothService main;

	public RefreshBluetoothService() {
		main = this;

		V.JavaLog("Starting bluetooth service!");
		this.mMessenger = new Messenger(new BluetoothRequestsHandler(this));
		this.isBindToClient = true;
		this.isHandleBulkTransfer = false;
		this.sessionToBeStopped = false;
	}

	static /* synthetic */ void access$0(RefreshBluetoothService refreshBluetoothService) {
		refreshBluetoothService.checkManager();
	}

	static /* synthetic */ void access$10(RefreshBluetoothService refreshBluetoothService, SleepSessionManager sleepSessionManager) {
		refreshBluetoothService.sleepSessionManager = sleepSessionManager;
	}

	static /* synthetic */ SleepSessionManager access$11(RefreshBluetoothService refreshBluetoothService) {
		return refreshBluetoothService.sleepSessionManager;
	}

	static /* synthetic */ SharedPreferences access$12(RefreshBluetoothService refreshBluetoothService) {
		return refreshBluetoothService.prefs;
	}

	static /* synthetic */ void access$13(RefreshBluetoothService refreshBluetoothService) {
		refreshBluetoothService.stopCurrentSession();
	}

	static /* synthetic */ void access$14(RefreshBluetoothService refreshBluetoothService, SharedPreferences sharedPreferences) {
		refreshBluetoothService.prefs = sharedPreferences;
	}

	static /* synthetic */ boolean access$15(RefreshBluetoothService refreshBluetoothService) {
		return refreshBluetoothService.isBindToClient;
	}

	static /* synthetic */ int access$2(RefreshBluetoothService refreshBluetoothService) {
		return refreshBluetoothService.isDataAvailableOnDevice;
	}

	static /* synthetic */ void access$3(RefreshBluetoothService refreshBluetoothService, int n) {
		refreshBluetoothService.isDataAvailableOnDevice = n;
	}

	static /* synthetic */ void access$4(RefreshBluetoothService refreshBluetoothService, boolean bl) {
		refreshBluetoothService.isHandleBulkTransfer = bl;
	}

	static /* synthetic */ boolean access$5(RefreshBluetoothService refreshBluetoothService) {
		return refreshBluetoothService.isHandleBulkTransfer;
	}

	static /* synthetic */ void access$6(RefreshBluetoothService refreshBluetoothService, Messenger messenger) {
		refreshBluetoothService.mClient = messenger;
	}

	static /* synthetic */ Messenger access$7(RefreshBluetoothService refreshBluetoothService) {
		return refreshBluetoothService.mClient;
	}

	static /* synthetic */ void access$8(RefreshBluetoothService refreshBluetoothService, boolean bl) {
		refreshBluetoothService.isBindToClient = bl;
	}

	static /* synthetic */ void access$9(RefreshBluetoothService refreshBluetoothService) {
		//refreshBluetoothService.changeToForeground();
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

	public void handlePacket(VLP.VLPacket vLPacket) {
		V.Log("Handling packet...");
		AppFileLog.addTrace(" _ ");
		if (VLPacketType.PACKET_TYPE_RETURN.ordinal() == vLPacket.packetType) {
			String string;
			AppFileLog.addTrace("Service handlePacket VLPacketType.PACKET_TYPE_RETURN");
			String string2 = new String(vLPacket.buffer);
			AppFileLog.addTrace(("Service handlePacket returnStr: " + string2.replace(" ", "").replace("\n", " ").replace("\t", " ")));
			ResultRPC resultRPC = ((JsonRPC) new Gson().fromJson(string2, JsonRPC.class)).getResult();
			if (resultRPC != null && (string = resultRPC.getPayload()) != null) {
				AppFileLog.addTrace(("Service handlePacket payload: " + string));
				if (string.equalsIgnoreCase("TRUE")) {
					Log.d("com.resmed.refresh.bluetooth", " RefreshBluetoothService isRecovering : ");
					if (this.sleepSessionManager != null) {
						File file = new File(RefreshTools.getFilesPath(), this.getString(2131165343));
						this.sleepSessionManager.addSamplesMiMq(file);
						AppFileLog.addTrace("Service handlePacket sleepSessionManager.addSamplesMiMq(filesDir)");
					}
					BluetoothDataSerializeUtil.deleteBulkDataBioFile(this.getApplicationContext());
					this.isHandleBulkTransfer = false;
					AppFileLog.addTrace("Service handlePacket isHandleBulkTransfer = false");
				}
			}
		} else {
			AppFileLog.addTrace(("Service handlePacket is NOT a PACKET_TYPE_RETURN isHandleBulkTransfer=" + this.isHandleBulkTransfer));
			if (!this.isHandleBulkTransfer) {
				if (this.sleepSessionManager != null && this.sleepSessionManager.isActive()) {
					if (VLPacketType.PACKET_TYPE_BIO_64.ordinal() == vLPacket.packetType) {
						this.sleepSessionManager.addBioData(vLPacket.buffer, vLPacket.packetNo);
					} else if (VLPacketType.PACKET_TYPE_ENV_60.ordinal() == vLPacket.packetType) {
						this.sleepSessionManager.addEnvData(vLPacket.buffer, this.sleepSessionManager.isActive());
					} else if (VLPacketType.PACKET_TYPE_ENV_1.ordinal() == vLPacket.packetType) {
						this.sleepSessionManager.addEnvData(vLPacket.buffer, this.sleepSessionManager.isActive());
					} else if (VLPacketType.PACKET_TYPE_BIO_32.ordinal() == vLPacket.packetType) {
						this.sleepSessionManager.addBioData(vLPacket.buffer, vLPacket.packetNo);
					}
				}
				if (VLPacketType.PACKET_TYPE_NOTE_STORE_FOREIGN.ordinal() == vLPacket.packetType || VLPacketType.PACKET_TYPE_NOTE_STORE_LOCAL.ordinal() == vLPacket.packetType) {
					int n = PacketsByteValuesReader.getStoreLocalBio((byte[]) vLPacket.buffer);
					Log.w("com.resmed.refresh.ui", ("PACKET_TYPE_NOTE_STORE!!! = " + vLPacket.packetType + " NUMBER OF SAMPLES : " + n));
					if (n >= 32) {
						this.isDataAvailableOnDevice = vLPacket.packetType;
						Message message = new Message();
						message.what = 27;
						message.getData().putInt("BUNDLE_BED_AVAILABLE_DATA", this.isDataAvailableOnDevice);
						this.sendMessageToClient(message);
					} else {
						AppFileLog.addTrace(("Ignoring samples on BeD because : " + n + " samples < 32"));
					}
				}
			} else if (VLPacketType.PACKET_TYPE_BIO_64.ordinal() == vLPacket.packetType || VLPacketType.PACKET_TYPE_BIO_32.ordinal() == vLPacket.packetType) {
				BluetoothDataSerializeUtil.writeBulkBioDataFile(this.getApplicationContext(), (byte[]) vLPacket.buffer);
				return;
			}
		}
		int n = vLPacket.buffer.length;
		int n2 = 1;
		do {
			if (n < 1048000) break;
			n /= 2;
			++n2;
		} while (true);
		byte[] arrby = new byte[n];
		AppFileLog.addTrace(("Service handlePacket packet length=" + n));
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(vLPacket.buffer, 0, vLPacket.buffer.length);
		int n3 = 0;
		do {
			if (n3 >= n2) {
				AppFileLog.addTrace("Service handlePacket Sending MSG_BeD_STREAM_PACKET_PARTIAL_END");
				AppFileLog.addTrace(" _ ");
				Message message = new Message();
				message.what = 20;
				Bundle bundle = new Bundle();
				bundle.putByte("REFRESH_BED_NEW_DATA_TYPE", vLPacket.packetType);
				bundle.putInt("REFRESH_BED_NEW_DATA_SIZE", vLPacket.packetSize);
				message.setData(bundle);
				this.sendMessageToClient(message);
				return;
			}
			Log.d("com.resmed.refresh.bluetooth", (" handlePacket, ready to send to clients. nrOFMessages : " + n2));
			int n4 = byteArrayInputStream.read(arrby, n * n3, n);
			AppFileLog.addTrace(("Service handlePacket nrBytesRead=" + n4));
			if (-1 != n4) {
				Message message = new Message();
				message.what = 19;
				Bundle bundle = new Bundle();
				bundle.putByteArray("REFRESH_BED_NEW_DATA", arrby);
				bundle.putByte("REFRESH_BED_NEW_DATA_TYPE", vLPacket.packetType);
				bundle.putInt("REFRESH_BED_NEW_DATA_SIZE", vLPacket.packetSize);
				message.setData(bundle);
				this.sendMessageToClient(message);
			}
			++n3;
		} while (true);
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

		V.WaitXThenRun(1000, ()-> {
			StartListening();
		});

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
		if (this.mClient == null) {
			Intent intent = new Intent("ACTION_RESMED_CONNECTION_STATUS");
			intent.putExtra("EXTRA_RESMED_CONNECTION_STATE", (Serializable) cONNECTION_STATE);
			this.sendStickyBroadcast(intent);
			return;
		}
		Message message = new Message();
		message.what = 6;
		Bundle bundle = new Bundle();
		bundle.putSerializable("REFRESH_BED_NEW_CONN_STATUS", (Serializable) cONNECTION_STATE);
		message.setData(bundle);
		this.sendMessageToClient(message);
	}

	public void sendMessageToClient(Message message) {
		if (this.mClient == null) return;
		try {
			this.mClient.send(message);
			return;
		} catch (RemoteException var2_2) {
			return;
		}
	}

	public void unbindService(ServiceConnection serviceConnection) {
		AppFileLog.addTrace("SERVICE onBind");
		Log.d("com.resmed.refresh.bluetooth", " unBind! ");
		super.unbindService(serviceConnection);
	}
}

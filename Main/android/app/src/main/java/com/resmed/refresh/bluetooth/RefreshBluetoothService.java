package com.resmed.refresh.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.resmed.edflib.EdfLibJNI;
import com.resmed.refresh.bluetooth.exception.BluetoothNotSupportedException;
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
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.rm20.RM20JNI;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Date;

import v.lucidlink.LL;
import v.lucidlink.MainActivity;
import v.lucidlink.V;

public class RefreshBluetoothService {
	public final class MessageType {
		public static final int DISCOVER_PAIR_CONNECT_RESMED = 1;
		public static final int SEND_BYTES = 2;
		public static final int START_DISCOVERY = 3;
		public static final int BeD_STREAM_PACKET = 5;
		public static final int BeD_CONNECTION_STATUS = 6;
		public static final int BeD_DISCONNECT = 7;
		public static final int BeD_UNPAIR = 8;
		public static final int DISCOVER_RESMED_DEVICES = 10;
		public static final int BeD_CONNECT_TO_DEVICE = 11;
		public static final int SLEEP_SESSION_START = 12;
		public static final int SLEEP_SESSION_STOP = 14;
		public static final int SLEEP_ENV_SAMPLE = 15;
		public static final int SLEEP_SET_SMART_ALARM = 16;
		public static final int SLEEP_USER_SLEEP_STATE = 17;
		public static final int SLEEP_BREATHING_RATE = 18;
		public static final int BeD_STREAM_PACKET_PARTIAL = 19;
		public static final int BeD_STREAM_PACKET_PARTIAL_END = 20;
		public static final int SLEEP_SESSION_RECOVER = 21;
		public static final int CANCEL_DISCOVERY = 22;
		public static final int BeD_UNPAIR_ALL = 23;
		public static final int BLUETOOTH_BULK_TRANSFER_START = 24;
		public static final int BLUETOOTH_BULK_TRANSFER_STOP = 25;
		public static final int SMARTALARM_UPDATE = 26;
		public static final int REQUEST_BeD_AVAILABLE_DATA = 27;
		public static final int RESET_BeD_AVAILABLE_DATA = 28;
		public static final int BLUETOOTH_RADIO_RESET = 29;
	}

	public static final String ACTION_ALREADY_PAIRED = "ACTION_ALREADY_PAIRED";
	public static final String REFRESH_BED_NEW_CONN_STATUS = "REFRESH_BED_NEW_CONN_STATUS";
	public static final String REFRESH_BED_NEW_DATA = "REFRESH_BED_NEW_DATA";
	public static final String REFRESH_BED_NEW_DATA_SIZE = "REFRESH_BED_NEW_DATA_SIZE";
	public static final String REFRESH_BED_NEW_DATA_TYPE = "REFRESH_BED_NEW_DATA_TYPE";
	public static final String REFRESH_BT_SERVICE_RUNNING = "REFRESH_BT_SERVICE_RUNNING";

	public RefreshBluetoothService() {
		V.JavaLog("Starting bluetooth service!");
		this.mMessageHandler = new BluetoothRequestsHandler();
		this.isHandleBulkTransfer = false;
		this.sessionToBeStopped = false;

		Log.d("com.resmed.refresh.bluetooth", "::onStartCommand, bluetooth");
		EdfLibJNI.loadLibrary(LL.main.reactContext);
		RM20JNI.loadLibrary(LL.main.reactContext);
	}

	//public RefreshBluetoothManager bluetoothManager;
	public BluetoothSetup bluetoothManager;
	private int isDataAvailableOnDevice;
	private boolean isHandleBulkTransfer;

	public BluetoothRequestsHandler mMessageHandler;
	private SharedPreferences prefs;
	public boolean sessionToBeStopped;
	public SleepSessionManager sleepSessionManager;

	// handles requests from our code, concerning bluetooth things (so some send commands to device)
	public class BluetoothRequestsHandler extends Handler {
		public void handleMessage(Message msg) {
			RefreshTools.writeTimeStampToFile(LL.main.reactContext, System.currentTimeMillis());
			RefreshBluetoothService.this.checkManager();
			Bundle b;
			switch (msg.what) {
				case MessageType.DISCOVER_PAIR_CONNECT_RESMED:
					RefreshBluetoothService.this.bluetoothManager.discoverResMedDevices(true);
					return;
				case MessageType.SEND_BYTES:
					Bundle bundle1 = msg.getData();
					if (bundle1 != null && RefreshBluetoothService.this.bluetoothManager != null) {
						byte[] toSend = bundle1.getByteArray("bytes");
						Log.d(LOGGER.TAG_BLUETOOTH, " bytes sending : " + toSend);
						RefreshBluetoothService.this.bluetoothManager.writeData(toSend);
						return;
					}
					return;
				case MessageType.BeD_CONNECTION_STATUS:
					RefreshBluetoothService.this.sendConnectionStatus(LL.main.connectionState);
					return;
				case MessageType.BeD_DISCONNECT:
					RefreshBluetoothService.this.bluetoothManager.disable();
					return;
				case MessageType.DISCOVER_RESMED_DEVICES:
					RefreshBluetoothService.this.bluetoothManager.discoverResMedDevices(false);
					return;
				case MessageType.BeD_CONNECT_TO_DEVICE:
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
					message4.what = MessageType.BeD_UNPAIR;
					RefreshBluetoothService.this.sendMessageToClient(message4);
					return;
				case MessageType.SLEEP_SESSION_START:
					V.Log("Starting session the correct way!");
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

					if (RefreshBluetoothService.this.prefs == null) {
						RefreshBluetoothService.this.prefs = PreferenceManager.getDefaultSharedPreferences(LL.main.reactContext);
					}
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
				case MessageType.SLEEP_SESSION_STOP:
					Log.d(LOGGER.TAG_BLUETOOTH, "Bluetooth service isHandleBulkTransfer : " + RefreshBluetoothService.this.isHandleBulkTransfer);
					RefreshBluetoothService.this.stopCurrentSession();
					RefreshBluetoothService.this.isDataAvailableOnDevice = -1;
					return;
				case MessageType.SLEEP_SET_SMART_ALARM:
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
				case MessageType.SLEEP_USER_SLEEP_STATE:
					if (RefreshBluetoothService.this.sleepSessionManager != null) {
						RefreshBluetoothService.this.sleepSessionManager.requestUserSleepState();
						return;
					}
					return;
				case MessageType.SLEEP_SESSION_RECOVER:
					long sessionToRecover = msg.getData().getLong(Consts.PREF_NIGHT_LAST_SESSION_ID, -1);
					if (RefreshBluetoothService.this.prefs == null) {
						RefreshBluetoothService.this.prefs = PreferenceManager.getDefaultSharedPreferences(LL.main.reactContext);
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
						recoverMsg.what = MessageType.SLEEP_SESSION_RECOVER;
						RefreshBluetoothService.this.sendMessageToClient(recoverMsg);
						return;
					}
					RefreshBluetoothService.this.sleepSessionManager = new SleepSessionManager(RefreshBluetoothService.this);
					RefreshBluetoothService.this.sleepSessionManager.recoverSession(sessionToRecover, ageToRecover, genderToRecover);
					return;
				case MessageType.CANCEL_DISCOVERY:
					RefreshBluetoothService.this.bluetoothManager.cancelDiscovery();
					return;
				case MessageType.BeD_UNPAIR_ALL /*23*/:
					Log.d(LOGGER.TAG_BLUETOOTH, "RefreshBluetoothService::MSG_BeD_UNPAIR_ALL, unpairing all : ");
					RefreshBluetoothService.this.bluetoothManager.unpairAll("S+");
					return;
				case MessageType.BLUETOOTH_BULK_TRANSFER_START /*24*/:
					RefreshBluetoothService.this.isHandleBulkTransfer = true;
					Log.d(LOGGER.TAG_BLUETOOTH, " RefreshBluetoothService isHandleBulkTransfer : " + RefreshBluetoothService.this.isHandleBulkTransfer);
					return;
				case MessageType.BLUETOOTH_BULK_TRANSFER_STOP /*25*/:
					RefreshBluetoothService.this.isHandleBulkTransfer = false;
					return;
				case MessageType.SMARTALARM_UPDATE /*26*/:
					if (RefreshBluetoothService.this.sleepSessionManager != null) {
						Log.d(LOGGER.TAG_SMART_ALARM, "RefreshBluetoothService MSG_SMARTALARM_UPDATE");
						b = msg.getData();
						RefreshBluetoothService.this.sleepSessionManager.updateAlarmSettings(b.getLong(Consts.SMART_ALARM_TIME_VALUE), b.getInt(Consts.SMART_ALARM_WINDOW_VALUE));
						return;
					}
					return;
				case MessageType.REQUEST_BeD_AVAILABLE_DATA /*27*/:
					V.Log("Requesting bed available data");
					Message bedDataMessage = new Message();
					bedDataMessage.what = MessageType.REQUEST_BeD_AVAILABLE_DATA;
					Bundle data = new Bundle();
					data.putInt(Consts.BUNDLE_BED_AVAILABLE_DATA, RefreshBluetoothService.this.isDataAvailableOnDevice);
					bedDataMessage.setData(data);
					RefreshBluetoothService.this.sendMessageToClient(bedDataMessage);
					return;
				case MessageType.RESET_BeD_AVAILABLE_DATA /*28*/:
					RefreshBluetoothService.this.isDataAvailableOnDevice = -1;
					return;
				case MessageType.BLUETOOTH_RADIO_RESET /*29*/:
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

	private void checkManager() {
		if (this.bluetoothManager != null) return;
		try {
			this.bluetoothManager = new BluetoothSetup(this);
			this.bluetoothManager.enable();
			return;
		} catch (BluetoothNotSupportedException var1_1) {
			var1_1.printStackTrace();
			return;
		}
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
	}

	public void handlePacket(VLP.VLPacket packet) {
		V.JavaLog("HandlePacket:" + packet.packetType);

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

						// temp; do not recover data, for now (file-not-written problem)
						/*if (this.sleepSessionManager != null) {
							this.sleepSessionManager.addSamplesMiMq(new File(RefreshTools.getFilesPath(), "bluetooth_buffer_bulk_file"));
							AppFileLog.addTrace("Service handlePacket sleepSessionManager.addSamplesMiMq(filesDir)");
						}*/

						BluetoothDataSerializeUtil.deleteBulkDataBioFile(LL.main.reactContext);
						this.isHandleBulkTransfer = false;
						AppFileLog.addTrace("Service handlePacket isHandleBulkTransfer = false");
					}
				}
			}
		} else {
			//AppFileLog.addTrace("Service handlePacket is NOT a PACKET_TYPE_RETURN isHandleBulkTransfer=" + this.isHandleBulkTransfer);
			if (this.isHandleBulkTransfer) {
				if (VLPacketType.PACKET_TYPE_BIO_64.ordinal() == packet.packetType || VLPacketType.PACKET_TYPE_BIO_32.ordinal() == packet.packetType) {
					BluetoothDataSerializeUtil.writeBulkBioDataFile(LL.main.reactContext, packet.buffer);
					return;
				}
			}
			//V.Log("Checking..." + this.sleepSessionManager + ";" + (this.sleepSessionManager != null ? this.sleepSessionManager.isActive : false));
			if (this.sleepSessionManager != null && this.sleepSessionManager.isActive) {
				if (VLPacketType.PACKET_TYPE_BIO_64.ordinal() == packet.packetType) {
					this.sleepSessionManager.addBioData(packet.buffer, packet.packetNo);
				} else if (VLPacketType.PACKET_TYPE_ENV_60.ordinal() == packet.packetType) {
					this.sleepSessionManager.addEnvData(packet.buffer, this.sleepSessionManager.isActive);
				} else if (VLPacketType.PACKET_TYPE_ENV_1.ordinal() == packet.packetType) {
					this.sleepSessionManager.addEnvData(packet.buffer, this.sleepSessionManager.isActive);
				} else if (VLPacketType.PACKET_TYPE_BIO_32.ordinal() == packet.packetType) {
					this.sleepSessionManager.addBioData(packet.buffer, packet.packetNo);
				}
				// custom
				/*else if (packet.packetType == VLPacketType.PACKET_TYPE_NOTE_BIO_1.ordinal()) {
					V.Log("Adding packet PACKET_TYPE_NOTE_BIO_1!");
					this.sleepSessionManager.addBioData(packet.buffer, packet.packetNo);
				}*/
			}
			if (VLPacketType.PACKET_TYPE_NOTE_STORE_FOREIGN.ordinal() == packet.packetType || VLPacketType.PACKET_TYPE_NOTE_STORE_LOCAL.ordinal() == packet.packetType) {
				int storeLocal = PacketsByteValuesReader.getStoreLocalBio(packet.buffer);
				Log.w(LOGGER.TAG_UI, "PACKET_TYPE_NOTE_STORE!!! = " + packet.packetType + " NUMBER OF SAMPLES : " + storeLocal);
				if (storeLocal >= 32) {
					this.isDataAvailableOnDevice = packet.packetType;
					Message mDataAvailableToClient = new Message();
					mDataAvailableToClient.what = MessageType.REQUEST_BeD_AVAILABLE_DATA;
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
				newMessage.what = MessageType.BeD_STREAM_PACKET_PARTIAL;
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
		newMessage.what = MessageType.BeD_STREAM_PACKET_PARTIAL_END;
		data = new Bundle();
		data.putByte(REFRESH_BED_NEW_DATA_TYPE, packet.packetType);
		data.putInt(REFRESH_BED_NEW_DATA_SIZE, packet.packetSize);
		newMessage.setData(data);
		sendMessageToClient(newMessage);
	}

	boolean listening;
	public void StartListening() {
		if (listening) return;
		listening = true;

		V.JavaLog("Start listening;" + MainActivity.main + ";" + LL.main.reactContext + ";" + LL.main);

		try {
			// rather than remember last one, just find first device and connect to it
			bluetoothManager = new BluetoothSetup(this);
			bluetoothManager.enable();
			bluetoothManager.discoverResMedDevices(true);
		} catch (Exception ex) {
			throw new Error(ex);
		}
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
		message.what = MessageType.BeD_CONNECTION_STATUS;
		Bundle bundle = new Bundle();
		bundle.putSerializable("REFRESH_BED_NEW_CONN_STATUS", (Serializable) cONNECTION_STATE);
		message.setData(bundle);
		this.sendMessageToClient(message);
	}

	public void sendMessageToClient(Message message) {
		//V.Log("Sending message to client;" + message.what);
		MainActivity.main.mFromServiceHandler.handleMessage(message);
	}
}

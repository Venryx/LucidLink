package com.resmed.refresh.ui.uibase.base;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import com.facebook.react.ReactActivity;
import com.google.gson.Gson;
import com.resmed.cobs.COBS;
import com.resmed.refresh.bed.RPCMapper;
import com.resmed.refresh.bluetooth.BluetoothDataWriter;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.bluetooth.RefreshBluetoothService;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.model.json.JsonRPC.ErrorRpc;
import com.resmed.refresh.model.json.JsonRPC.RPCallback;
import com.resmed.refresh.model.json.ResultRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLP;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.BluetoothDataSerializeUtil;
import com.resmed.refresh.utils.LOGGER;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.MeasureManager;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.refresh.utils.RefreshUserPreferencesData;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import SPlus.SPlusModule;

public abstract class BaseBluetoothActivity extends ReactActivity implements BluetoothDataWriter {
	protected void onResume() {
		super.onResume();
		//RefreshApplication.getInstance().increaseActivitiesInForeground();

		//registerReceiver(this.connectionStatusReceiver, new IntentFilter("ACTION_RESMED_CONNECTION_STATUS")); // (registered in PostModuleInit)
	}

	public static boolean CORRECT_FIRMWARE_VERSION = true;
	public static Map CommandStack = new LinkedHashMap();
	public static boolean UPDATING_FIRMWARE = false;
	private JsonRPC bioSensorSerialNrRPC;

	public IncomingHandler mFromServiceHandler = new IncomingHandler();

	public class IncomingHandler extends Handler {
		private List<Byte> partialMsgBuffer = new ArrayList();

		public void handleMessage(Message msg) {
			/*if (msg.what != 19 && msg.what != 20)
				V.Log("BaseBluetoothActivity.handler.handleMessage(meaningful)..." + msg.what);*/
			switch (msg.what) {
				case RefreshBluetoothService.MessageType.BeD_STREAM_PACKET:
					BaseBluetoothActivity.this.handleStreamPacket(msg.getData());
					return;
				case RefreshBluetoothService.MessageType.BeD_CONNECTION_STATUS:
					CONNECTION_STATE state = (CONNECTION_STATE) msg.getData().get(RefreshBluetoothService.REFRESH_BED_NEW_CONN_STATUS);
					Log.d(LOGGER.TAG_PAIR, "BaseActivity MSG_BeD_CONNECTION_STATUS CONNECTION_STATE : " + state);
					BaseBluetoothActivity.this.handleConnectionStatus(state);
					return;
				case RefreshBluetoothService.MessageType.BeD_UNPAIR:
					BluetoothDataSerializeUtil.deleteJsonFile(BaseBluetoothActivity.this.getApplicationContext());
					Log.d(LOGGER.TAG_DIALOG, "MSG_UNPAIR showBeDPickerDialog()");
					//BaseBluetoothActivity.this.showBeDPickerDialog();
					return;
				case RefreshBluetoothService.MessageType.SLEEP_SESSION_STOP:
					BaseBluetoothActivity.this.handleSleepSessionStopped(msg.getData());
					return;
				case RefreshBluetoothService.MessageType.SLEEP_ENV_SAMPLE:
					BaseBluetoothActivity.this.handleEnvSample(msg.getData());
					return;
				case RefreshBluetoothService.MessageType.SLEEP_USER_SLEEP_STATE:
					BaseBluetoothActivity.this.handleUserSleepState(msg.getData());
					return;
				case RefreshBluetoothService.MessageType.SLEEP_BREATHING_RATE:
					BaseBluetoothActivity.this.handleBreathingRate(msg.getData());
					return;
				case RefreshBluetoothService.MessageType.BeD_STREAM_PACKET_PARTIAL:
					handlePartialStreamPacket(msg.getData());
					return;
				case RefreshBluetoothService.MessageType.BeD_STREAM_PACKET_PARTIAL_END:
					handlePartialStreamPacketEnd(msg.getData());
					return;
				/*case RefreshBluetoothService.MessageType.SLEEP_SESSION_RECOVER:
					BaseBluetoothActivity.this.handleSessionRecovered(msg.getData());
					return;*/
				case RefreshBluetoothService.MessageType.REQUEST_BeD_AVAILABLE_DATA /*27*/:
					Bundle bData = msg.getData();
					if (bData != null) {
						BaseBluetoothActivity.this.updateDataStoredFlag(bData.getInt(Consts.BUNDLE_BED_AVAILABLE_DATA, -1));
						return;
					}
					return;
				default:
					super.handleMessage(msg);
					return;
			}
		}

		private void handlePartialStreamPacketEnd(Bundle data) {
			ByteBuffer bBuffer = ByteBuffer.allocate(this.partialMsgBuffer.size());
			for (Byte b : this.partialMsgBuffer) {
				bBuffer.put(b.byteValue());
			}
			this.partialMsgBuffer.clear();
			Bundle fullData = new Bundle();
			fullData.putByteArray(RefreshBluetoothService.REFRESH_BED_NEW_DATA, bBuffer.array());
			if (data != null) {
				byte packetType = data.getByte(RefreshBluetoothService.REFRESH_BED_NEW_DATA_TYPE);
				int packetSize = data.getInt(RefreshBluetoothService.REFRESH_BED_NEW_DATA_SIZE);
				fullData.putByte(RefreshBluetoothService.REFRESH_BED_NEW_DATA_TYPE, packetType);
				fullData.putInt(RefreshBluetoothService.REFRESH_BED_NEW_DATA_SIZE, packetSize);
			}
			BaseBluetoothActivity.this.handleStreamPacket(fullData);

			// for some reason, we also need to send it to handleMessage
			Message message = new Message();
			message.what = data.getByte(RefreshBluetoothService.REFRESH_BED_NEW_DATA_TYPE);
			message.setData(data);
		}

		private void handlePartialStreamPacket(Bundle data) {
			if (data != null) {
				byte[] buff = data.getByteArray(RefreshBluetoothService.REFRESH_BED_NEW_DATA);
				for (byte valueOf : buff) {
					this.partialMsgBuffer.add(valueOf);
				}
			}
		}
	}
	
	protected void handleErrorRPC(JsonRPC var1, ErrorRpc var2) {
		if (var1 != null && var2 != null) {
			RPCallback var3 = var1.getRPCallback();
			if (var3 != null) {
				var3.onError(var2);
			}
		}

	}

	protected void handleHeartBeat(byte[] var1) {
		int var2 = PacketsByteValuesReader.getStoreLocalBio(var1);
		int var3 = PacketsByteValuesReader.getStoreLocalEnv(var1);
		Log.d("com.resmed.refresh.ui", "countBio : " + var2 + " countEnv :" + var3);
	}

	public void sendMessageToService(final int paramInt, final Bundle paramBundle) {
		Message message = new Message();
		message.what = paramInt;
		message.setData(paramBundle);
		sendMessageToService(message);
	}
	public void sendMessageToService(Message message) {
		if (SPlusModule.main.sessionConnector != null) // fix for bug while closing (during dev)
			SPlusModule.main.sessionConnector.service.handleMessage(message);
	}

	private boolean sendRPC(JsonRPC var1) {
		if (RPCMapper.main == null) {
			return false;
		} else {
			Gson var3 = new Gson();
			RPCMapper.main.setRPCid(1 + RPCMapper.main.getRPCid());
			var1.setId(RPCMapper.main.getRPCid());
			AppFileLog.addTrace("OUT NEW RPC ID : " + RPCMapper.main.getRPCid() + " METHOD :" + var1.getMethod() + " PARAMS : " + var1.getParams());
			String var4 = var3.toJson(var1);
			Log.d("com.resmed.refresh.ui", "bluetooth json rpc : " + var4);
			ByteBuffer var6 = VLP.getInstance().Packetize((byte) VLPacketType.PACKET_TYPE_CALL.ordinal(), Integer.valueOf(var1.getId()).byteValue(), var4.getBytes().length, 64, var4.getBytes());
			byte[] var7 = COBS.getInstance().encode(var6.array());
			Log.d("com.resmed.refresh.bluetooth", " COBSJNI, encodedByteBuff : " + Arrays.toString(var7));
			ByteBuffer var9 = ByteBuffer.wrap(var7);
			RPCallback var10 = var1.getRPCallback();
			if (var10 != null) {
				var10.preExecute();
			}

			Bundle data = new Bundle();
			data.putByteArray("bytes", var9.array());
			sendMessageToService(RefreshBluetoothService.MessageType.SEND_BYTES, data);

			RefreshUserPreferencesData var12 = new RefreshUserPreferencesData(this.getApplicationContext());
			var12.setIntegerConfigValue("PREF_LAST_RPC_ID_USED", var1.getId());

			return true;
		}
	}

	/*protected boolean checkForFirmwareUpgrade(String var1) {
		Log.d("com.resmed.refresh.ui", "checkForFirmwareUpgrade firmware version :" + var1);
		String var3 = RefreshTools.getFirmwareBinaryVersion(this.getApplicationContext());
		boolean needsUpdate = RefreshTools.compareFirmwareVersions(var1.replace("Release", "").split(" ")[0], var3) < 0;
		return needsUpdate;
	}*/

	public void handleConnectionStatus(CONNECTION_STATE newState) {
		// also transmit to connector
		if (SPlusModule.main != null && SPlusModule.main.sessionConnector != null)
			SPlusModule.main.sessionConnector.handleConnectionStatus(newState);

		Log.d("com.resmed.refresh.pair", "    BaseBluetoothActivity::handleConnectionStatus() connState=" + newState + " UPDATING_FIRMWARE:" + UPDATING_FIRMWARE);
		if (newState != null && !UPDATING_FIRMWARE) { // && LL.mainModule.connectionState != newState) {
			if (CONNECTION_STATE.SOCKET_BROKEN == newState || CONNECTION_STATE.SOCKET_RECONNECTING == newState) {
				CORRECT_FIRMWARE_VERSION = true;
				UPDATING_FIRMWARE = false;
			}
		}
	}

	// hard-coded for now
	public static String boardVersion = "1445024114";
	public static String firmwareVersion = "Release1.1.7 0.0.0";
	public void handleReceivedRpc(JsonRPC receivedRPC) {
		// also transmit to connector
		if (SPlusModule.main != null && SPlusModule.main.sessionConnector != null)
			SPlusModule.main.sessionConnector.handleReceivedRpc(receivedRPC);

		if (receivedRPC != null) {
			ResultRPC result = receivedRPC.getResult();
			if (result != null) {
				Editor editorPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
				String payload = result.getPayload();
				if (payload.contains("$")) {
					int indexSeparationChar = payload.indexOf("$");
					String boardVersion = payload.substring(0, indexSeparationChar);
					final String firmwareVersion = payload.substring(indexSeparationChar + 1, payload.length() - 1);
					Log.d(LOGGER.TAG_BLUETOOTH, "BaseBluetoothActivity::handlePayload, boardVersion : " + boardVersion + " firmwareVersion : " + firmwareVersion);

					/*RefreshModelController.getInstance().saveBoardVersion(boardVersion);
					RefreshModelController.getInstance().saveFirmwareVersion(firmwareVersion);*/
					/*BaseBluetoothActivity.boardVersion = boardVersion;
					BaseBluetoothActivity.firmwareVersion = firmwareVersion;*/

					if (CORRECT_FIRMWARE_VERSION) {
						/*editorPref.putString(getString(R.string.extra_bed_board_version), boardVersion);
						editorPref.commit();
						this.bioSensorSerialNrRPC = RPCMapper.main.getBioSensorSerialNumber();
						this.bioSensorSerialNrRPC.setRPCallback(new RPCallback() {
							public void preExecute() {
							}

							public void onError(ErrorRpc errRpc) {
							}

							public void execute() {
								Log.d(LOGGER.TAG_UI, "handleReceivedRpc firmwareVersion :" + firmwareVersion);
								if (RefreshTools.checkForFirmwareUpgrade(BaseBluetoothActivity.this, firmwareVersion)) {
									BaseBluetoothActivity.CORRECT_FIRMWARE_VERSION = false;
									BaseBluetoothActivity.this.startActivity(new Intent(BaseBluetoothActivity.this, UpdateOTAActivity.class));
								}
							}
						});
						sendRpcToBed(this.bioSensorSerialNrRPC);*/
					}
				}
				Log.d(LOGGER.TAG_UI, "handleReceivedRpc committing  extra_bed_sensor_serial:" + payload);
				Log.d(LOGGER.TAG_UI, "handleReceivedRpc committing  bioSensorSerialNrRPC:" + this.bioSensorSerialNrRPC + " receivedRPC.getId() :" + receivedRPC.getId() + " CORRECT_FIRMWARE_VERSION " + CORRECT_FIRMWARE_VERSION);
				if (this.bioSensorSerialNrRPC != null && this.bioSensorSerialNrRPC.getId() == receivedRPC.getId() && CORRECT_FIRMWARE_VERSION) {
					/*editorPref.putString(getString(R.string.extra_bed_sensor_serial), payload);
					//RefreshModelController.getInstance().setBedId(payload);
					editorPref.commit();*/
				}
			}
		}
	}

	public void handleSleepSessionStopped(Bundle var1) {
		// also transmit to connector
		if (SPlusModule.main.sessionConnector != null)
			SPlusModule.main.sessionConnector.handleSleepSessionStopped(var1);
	}

	public void handleStreamPacket(Bundle paramBundle) {
		throw new Error("Should be handled in MainActivity.X");
	}

	public void handleEnvSample(Bundle var1) {
		// also transmit to connector
		if (SPlusModule.main.sessionConnector != null)
			SPlusModule.main.sessionConnector.handleEnvSample(var1);

		Log.d("com.resmed.refresh.ui", "handleEnvSample() ");
	}

	// raw data
	protected void handleNoteEnv(byte[] decobbed) {
		int illumValue = PacketsByteValuesReader.readIlluminanceValue(decobbed);
		float tempValue = PacketsByteValuesReader.readTemperatureValue(decobbed);
		this.setLightValue(illumValue);
		this.setTempValue(tempValue);
	}
	public void setTempValue(float value) {
		double degreesCelsuis = value;
		double degreesFarenheit = Math.round(MeasureManager.convertCelsiusToFahrenheit(value));
		//V.Log("TempVal:" + degreesCelsuis + " (" + degreesFarenheit + "f)");
		SPlusModule.main.SendEvent("OnReceiveTemp", degreesCelsuis, degreesFarenheit);
	}
	public void setLightValue(int value) {
		//V.Log("LightLevel:" + value);
		SPlusModule.main.SendEvent("OnReceiveLightValue", value);
	}


	// calculated data
	public void handleBreathingRate(Bundle data) {
		// also transmit to connector
		if (SPlusModule.main.sessionConnector != null)
			SPlusModule.main.sessionConnector.handleBreathingRate(data);

		/*V.Log("Breathing rate: " + var1);
		V.Alert("Breathing rate: " + var1);*/
		//int breathing_secIndex = data.getInt(Consts.BUNDLE_BREATHING_SECINDEX);
		float breathing_rate = data.getFloat(Consts.BUNDLE_BREATHING_RATE);
		SPlusModule.main.SendEvent("OnReceiveBreathingRate", breathing_rate);
	}

	public enum SleepStage {
		Wake(1),
		Absent(2), Unknown(3), Break(4),
		LightSleep(5),
		DeepSleep(6),
		RemSleep(7);

		public int value;
		SleepStage(int value) { this.value = value;}
	}

	public void handleUserSleepState(Bundle bundle) {
		throw new Error("Should be handled in MainActivity.X");
	}

	/*protected static final int REQUEST_ENABLE_BT = 161;
	private static boolean userAllowBluetooth = true;
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, requestCode, data);
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == -1)
				userAllowBluetooth = true;
			else if (resultCode == 0) {
				userAllowBluetooth = false;
				SPlusModule.main.Disconnect();
			}
		}
	}*/

	protected void onCreate(Bundle var1) {
		super.onCreate(var1);
		RPCMapper.main.setContextBroadcaster(this);
	}

	/*public void onPickedDevice(BluetoothDevice var1) {
		Log.d("com.resmed.refresh.pair", "onPickedDevice");
		Log.d("com.resmed.refresh.dialog", "onPickedDevice() connectionProgressDisplayed = true");
		this.connectionProgressDisplayed = true;
		this.pairAndConnect(var1);
	}*/

	protected void onSaveInstanceState(Bundle var1) {
		super.onSaveInstanceState(var1);
		Log.d("com.resmed.refresh.dialog", "BaseBluetoothActivity onSaveInstanceState " + this);
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onStop() {
		super.onStop();
		Log.d("com.resmed.refresh.ui", " BaseBluetoothActivity::onStop() ");
	}

	public void pairAndConnect(BluetoothDevice deviceInfo) {
		Log.d("com.resmed.refresh.pair", "connectAndPair");
		Bundle bundle = new Bundle();
		bundle.putParcelable("deviceInfo", deviceInfo);
		bundle.putBoolean("makePaired", true);
		this.sendMessageToService(RefreshBluetoothService.MessageType.BeD_CONNECT_TO_DEVICE, bundle);
	}

	public boolean sendBytesToBeD(byte[] bytes, VLPacketType var2) {
		int bytesLength = bytes != null ? bytes.length : 0;

		ByteBuffer var4 = VLP.getInstance().Packetize((byte) var2.ordinal(), (byte) 1, bytesLength, 64, bytes);
		ByteBuffer var5 = ByteBuffer.wrap(COBS.getInstance().encode(var4.array()));
		Message message = new Message();
		message.what = RefreshBluetoothService.MessageType.SEND_BYTES;
		Bundle var7 = new Bundle();
		var7.putByteArray("bytes", var5.array());
		message.setData(var7);

		sendMessageToService(message);
		return true;
	}

	public void sendRpcToBed(final JsonRPC jsonRPC) {
		synchronized (this) {
			final boolean sendRPC = this.sendRPC(jsonRPC);
			AppFileLog.addTrace(" BaseBluetoothActivity::sendRpcToBed(rpc) rpc : " + jsonRPC + " wasSent : " + sendRPC);
			Log.d("com.resmed.refresh.ui", "sendRpcToBed wasSent : " + sendRPC);
			if (sendRPC) {
				BaseBluetoothActivity.CommandStack.put(jsonRPC.getId(), jsonRPC);
			}
		}
	}

	public void updateDataStoredFlag(int var1) {
		if (var1 == 0) {
			Message var2 = new Message();
			var2.what = RefreshBluetoothService.MessageType.RESET_BeD_AVAILABLE_DATA;
			sendMessageToService(var2);
		}
		//super.updateDataStoredFlag(var1);
	}
}
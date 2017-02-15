package com.resmed.refresh.ui.uibase.base;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningServiceInfo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.view.Menu;

import com.facebook.react.bridge.Promise;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.resmed.cobs.COBS;
import com.resmed.refresh.bed.BedCommandsRPCMapper;
import com.resmed.refresh.bed.BedDefaultRPCMapper;
import com.resmed.refresh.bluetooth.BluetoothDataWriter;
import com.resmed.refresh.bluetooth.BluetoothSetup;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.bluetooth.RefreshBluetoothService;
import com.resmed.refresh.bluetooth.BluetoothSetup.AlarmReceiver;
import com.resmed.refresh.bluetooth.RefreshBluetoothServiceClient;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.model.json.ResultRPC;
import com.resmed.refresh.model.json.JsonRPC.ErrorRpc;
import com.resmed.refresh.model.json.JsonRPC.RPCallback;
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
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import SPlus.SPlusModule;
import v.lucidlink.LL;
import v.lucidlink.MainActivity;
import v.lucidlink.R;
import v.lucidlink.V;

public class BaseBluetoothActivity extends BaseActivity implements BluetoothDataListener, BluetoothDataWriter {
	public static boolean CORRECT_FIRMWARE_VERSION = true;
	private static Map CommandStack = new LinkedHashMap();
	public static boolean IN_SLEEP_SESSION = false;
	protected static final int REQUEST_ENABLE_BT = 161;
	private static BedCommandsRPCMapper RpcCommands = BedDefaultRPCMapper.getInstance();
	public static boolean UPDATING_FIRMWARE = false;
	private static boolean connectingToBeD = false;
	private static Messenger mService;
	private static boolean userAllowBluetooth = true;
	protected BroadcastReceiver awakePowerSaveReconnect = new BroadcastReceiver() {
		public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent) {
			Log.d("com.resmed.refresh.bluetooth.alarm", "BaseBluetoothActivity#AlarmReconnectReceiver::onReceive()");
			BaseBluetoothActivity.this.bindToService();
		}
	};
	private JsonRPC bioSensorSerialNrRPC;
	protected BroadcastReceiver bluetoothServiceRestartReceiver = new BroadcastReceiver() {
		public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent) {
			BaseBluetoothActivity.this.registerToService();
		}
	};
	private boolean connectionProgressDisplayed = false;
	protected BroadcastReceiver connectionStatusReceiver = new BroadcastReceiver() {
		public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent) {
			CONNECTION_STATE connectionState = (CONNECTION_STATE) paramAnonymousIntent.getExtras().get("EXTRA_RESMED_CONNECTION_STATE");
			BaseBluetoothActivity.this.handleConnectionStatus(connectionState);
		}
	};
	protected Menu mBarMenu;
	protected boolean mBound;
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder) {
			Log.d("com.resmed.refresh.ui", ": bluetooth service bound! : ");
			BaseBluetoothActivity.mService = new Messenger(paramAnonymousIBinder);
			BaseBluetoothActivity.this.mBound = true;
			BaseBluetoothActivity.this.registerToService();
			AppFileLog.addTrace(": bluetooth service bound! : ");
		}

		public void onServiceDisconnected(ComponentName paramAnonymousComponentName) {
			Log.d("com.resmed.refresh.ui", ": bluetooth service unbound! : ");
			BaseBluetoothActivity.mService = null;
			BaseBluetoothActivity.this.mBound = false;
			AppFileLog.addTrace(": bluetooth service unbound! : ");
		}
	};
	private BluetoothDevice mDevice;
	public IncomingHandler mFromServiceHandler = new IncomingHandler();
	public Messenger mFromService = new Messenger(mFromServiceHandler);
	private List<BroadcastReceiver> receivers;

	public class IncomingHandler extends Handler {
		private List<Byte> partialMsgBuffer = new ArrayList();

		public void handleMessage(Message msg) {
			if (msg.what != 19 && msg.what != 20)
				V.Log("BaseBluetoothActivity.handler.handleMessage(meaningful)..." + msg.what);
			switch (msg.what) {
				case RefreshBluetoothService.MessageType.BeD_STREAM_PACKET:
					BaseBluetoothActivity.this.handleStreamPacket(msg.getData());
					return;
				case RefreshBluetoothService.MessageType.BeD_CONNECTION_STATUS:
					CONNECTION_STATE state = (CONNECTION_STATE) msg.getData().get(RefreshBluetoothService.REFRESH_BED_NEW_CONN_STATUS);
					Log.d(LOGGER.TAG_PAIR, "BaseActivity MSG_BeD_CONNECTION_STATUS CONNECTION_STATE : " + CONNECTION_STATE.toString(state));
					BaseBluetoothActivity.this.handleConnectionStatus(state);
					return;
				case RefreshBluetoothService.MessageType.BeD_UNPAIR:
					BluetoothDataSerializeUtil.deleteJsonFile(BaseBluetoothActivity.this.getApplicationContext());
					Log.d(LOGGER.TAG_DIALOG, "MSG_UNPAIR showBeDPickerDialog()");
					BaseBluetoothActivity.this.showBeDPickerDialog();
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
				case RefreshBluetoothService.MessageType.SLEEP_SESSION_RECOVER:
					BaseBluetoothActivity.this.handleSessionRecovered(msg.getData());
					return;
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
					this.partialMsgBuffer.add(Byte.valueOf(valueOf));
				}
			}
		}
	}

	public static BedCommandsRPCMapper getRpcCommands() {
		return RpcCommands;
	}

	private void handleErrorRPC(JsonRPC var1, ErrorRpc var2) {
		if (var1 != null && var2 != null) {
			RPCallback var3 = var1.getRPCallback();
			if (var3 != null) {
				var3.onError(var2);
			}
		}

	}

	private void handleHearBeat(byte[] var1) {
		int var2 = PacketsByteValuesReader.getStoreLocalBio(var1);
		int var3 = PacketsByteValuesReader.getStoreLocalEnv(var1);
		Log.d("com.resmed.refresh.ui", "countBio : " + var2 + " countEnv :" + var3);
	}

	private void sendMessageToService(final int paramInt, final Bundle paramBundle) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000L);
					Message localMessage = new Message();
					localMessage.what = paramInt;
					localMessage.setData(paramBundle);
					BaseBluetoothActivity.this.sendMsgBluetoothService(localMessage);
					return;
				} catch (InterruptedException localInterruptedException) {
					for (; ; ) {
						localInterruptedException.printStackTrace();
					}
				}
			}
		}).start();
	}

	private boolean sendRPC(JsonRPC var1) {
		if (getRpcCommands() == null) {
			return false;
		} else {
			Message var2 = new Message();
			Gson var3 = new Gson();
			getRpcCommands().setRPCid(1 + getRpcCommands().getRPCid());
			var1.setId(Integer.valueOf(getRpcCommands().getRPCid()));
			AppFileLog.addTrace("OUT NEW RPC ID : " + getRpcCommands().getRPCid() + " METHOD :" + var1.getMethod() + " PARAMS : " + var1.getParams());
			String var4 = var3.toJson(var1);
			Log.d("com.resmed.refresh.ui", "bluetooth json rpc : " + var4);
			ByteBuffer var6 = VLP.getInstance().Packetize((byte) VLPacketType.PACKET_TYPE_CALL.ordinal(), (new Integer(var1.getId())).byteValue(), var4.getBytes().length, 64, var4.getBytes());
			byte[] var7 = COBS.getInstance().encode(var6.array());
			Log.d("com.resmed.refresh.bluetooth", " COBSJNI, encodedByteBuff : " + Arrays.toString(var7));
			ByteBuffer var9 = ByteBuffer.wrap(var7);
			RPCallback var10 = var1.getRPCallback();
			if (var10 != null) {
				var10.preExecute();
			}

			Bundle var11 = new Bundle();
			var11.putByteArray("bytes", var9.array());
			var2.setData(var11);
			var2.what = 2;
			RefreshUserPreferencesData var12 = new RefreshUserPreferencesData(this.getApplicationContext());
			var12.setIntegerConfigValue("PREF_LAST_RPC_ID_USED", Integer.valueOf(var1.getId()));

			V.Log("Service exists?:" + mService);

			try {
				if (mService != null) {
					mService.send(var2);
				}

				return true;
			} catch (RemoteException var14) {
				var14.printStackTrace();
				this.bindToService();
				return false;
			}
		}
	}

	private void showConnectionProgress() {
		// $FF: Couldn't be decompiled
	}

	public void bindToService() {
		// some code resulting from the below, relies on LL.main, which isn't initialized yet
		// so be lazy for now, and just wait 3 seconds
		V.WaitXThenRun(3000, ()-> {
			Intent bluetoothManagerService = new Intent(MainActivity.main, RefreshBluetoothService.class);

			Log.d("com.resmed.refresh.ui", "binding to bluetooth service! isRunning :" + this.isBluetoothServiceRunning() + "service intent : " + bluetoothManagerService + " mConnection : " + this.mConnection);
			AppFileLog.addTrace("BaseBluetoothActivity::bindToService!");
			if (!this.mBound) {
				this.getApplicationContext().bindService(bluetoothManagerService, this.mConnection, 1);
			}
		});
	}

	public boolean checkBluetoothEnabled() {
		return this.checkBluetoothEnabled(false);
	}

	public boolean checkBluetoothEnabled(boolean var1) {
		if (var1) {
			userAllowBluetooth = true;
		}

		BluetoothAdapter var2 = BluetoothAdapter.getDefaultAdapter();
		if (!var2.isEnabled() && userAllowBluetooth) {
			Intent var3 = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
			var3.setFlags(2097152);
			this.startActivityForResult(var3, 161);
		} else if (var2.isEnabled()) {
			userAllowBluetooth = true;
		}

		return var2.isEnabled();
	}

	protected boolean checkForFirmwareUpgrade(String var1) {
		Log.d("com.resmed.refresh.ui", "checkForFirmwareUpgrade firmware version :" + var1);
		String var3 = RefreshTools.getFirmwareBinaryVersion(this.getApplicationContext());
		boolean needsUpdate = RefreshTools.compareFirmwareVersions(var1.replace("Release", "").split(" ")[0], var3) < 0;
		return needsUpdate;
	}

	/*public boolean connectToBeD(boolean var1) {
		Log.d("com.resmed.refresh.pair", "connectToBeD(" + var1 + ")");
		BluetoothDevice deviceInfo = BluetoothDataSerializeUtil.readJsonFile(this.getApplicationContext());
		Log.d("com.resmed.refresh.pair", "connectToBeD mDevice : " + deviceInfo);
		if (deviceInfo == null) {
			Log.d("com.resmed.refresh.pair", "connectToBeD no mDevice stored");
			if (var1) {
				Log.d("com.resmed.refresh.dialog", "connectToBeD showBeDPickerDialog()");
				this.showBeDPickerDialog();
			}

			return false;
		} else {
			Log.d("com.resmed.refresh.pair", "connectToBeD sending message to service : ");
			Bundle bundle = new Bundle();
			bundle.putParcelable(this.getString(2131165303), deviceInfo);
			bundle.putBoolean(this.getString(2131165304), false); // makePaired
			//this.sendMessageToService(11, bundle);
			int pairAndConnect = 11;
			this.sendMessageToService(pairAndConnect, bundle);
			if (var1) {
				this.showConnectionProgress();
			}

			return true;
		}
	}*/

	public void disconnectBluetoothConn() {
		Message var1 = new Message();
		var1.what = 7;
		this.sendMsgBluetoothService(var1);
		Message var2 = new Message();
		var2.what = 9;
		this.sendMsgBluetoothService(var2);
		this.handleConnectionStatus(CONNECTION_STATE.SOCKET_NOT_CONNECTED);
	}

	protected void dismissConnectionProgress() {
		// $FF: Couldn't be decompiled
	}

	public void handleConnectionStatus(CONNECTION_STATE newState) {
		// also transmit to connector
		if (SPlusModule.main != null && SPlusModule.main.sessionConnector != null)
			SPlusModule.main.sessionConnector.handleConnectionStatus(newState);

		Log.d("com.resmed.refresh.pair", "    BaseBluetoothActivity::handleConnectionStatus() connState=" + newState + " UPDATING_FIRMWARE:" + UPDATING_FIRMWARE + " mBound : " + this.mBound + " isAvailable:" + this.isAvailable);
		if (newState != null && !UPDATING_FIRMWARE && this.mBound && this.isAvailable) { // && LL.main.connectionState != newState) {
			//this.currentState = newState;
			Log.d("com.resmed.refresh.pair", "    handleConnectionStatus connState=" + newState);
			if (CONNECTION_STATE.SOCKET_CONNECTED == newState) {
				//this.sendRpcToBed(RpcCommands.openSession(RefreshModelController.getInstance().getUserSessionID()));
				//this.sendRpcToBed(RpcCommands.openSession("user1"));
				// use static user-id different than S+ app's one
				sendRpcToBed(BedDefaultRPCMapper.getInstance().openSession("c63eb080-a864-11e3-a5e2-000000000009"));
				// set up receivers, so BluetoothSetup.setConnectionStatusAndNotify gets called whenever the state changes
				RefreshBluetoothService.main.bluetoothManager.AddReceivers();
				V.Log("Starting session!!!");

				//BluetoothDataSerializeUtil.writeJsonFile(this.getApplicationContext(), this.mDevice);
				// todo: if you want to store the bluetooth-device-info for auto-connect next time, do it here
			} /*else if (newState == CONNECTION_STATE.SESSION_OPENED) {
				V.Log("YAY3!!!");
				//MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().startNightTracking());
				MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().startRealTimeStream());
			}*/

			StringBuilder var4 = (new StringBuilder("Should show device paired? ")).append(connectingToBeD).append(" ");
			boolean var5 = CONNECTION_STATE.SESSION_OPENED == newState;

			Log.d("com.resmed.refresh.dialog", var4.append(var5).toString());
			if (connectingToBeD && CONNECTION_STATE.SESSION_OPENED == newState) {
				Log.d("com.resmed.refresh.pair", "handleConnectionStatus SOCKET_CONNECTED connectingToBeD=" + connectingToBeD);
				connectingToBeD = false;
				Log.d("com.resmed.refresh.dialog", "showDialog device paired");
				//this.showDialog((new CustomDialogBuilder(this)).title(2131165891).setPositiveButton(2131165892, (OnClickListener)null), false);
			} else if (this.connectionProgressDisplayed) {
				this.dismissConnectionProgress();
				Log.d("com.resmed.refresh.dialog", "dismissConnectionProgress");
			}

			Log.d("com.resmed.refresh.dialog", "connectionProgressDisplayed=" + this.connectionProgressDisplayed);
			if (CONNECTION_STATE.SOCKET_BROKEN == newState || CONNECTION_STATE.SOCKET_RECONNECTING == newState) {
				CORRECT_FIRMWARE_VERSION = true;
				UPDATING_FIRMWARE = false;
				return;
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
						this.bioSensorSerialNrRPC = getRpcCommands().getBioSensorSerialNumber();
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


	public void handleSessionRecovered(Bundle var1) {
		Log.d("com.resmed.refresh.ui", " BaseBluetoothActivity::handleSessionRecovered()");
	}

	public void handleSleepSessionStopped(Bundle var1) {
		// also transmit to connector
		if (SPlusModule.main.sessionConnector != null)
			SPlusModule.main.sessionConnector.handleSleepSessionStopped(var1);
	}

	private void handleNoteEnv(byte[] decobbed) {
		int illumValue = PacketsByteValuesReader.readIlluminanceValue(decobbed);
		float tempValue = PacketsByteValuesReader.readTemperatureValue(decobbed);
		this.setLightValue(illumValue);
		this.setTempValue(tempValue);
	}
	public void setTempValue(float value) {
		double degreesCelsuis = value;
		double degreesFarenheit = Math.round(MeasureManager.convertCelsiusToFahrenheit(value));
		//V.Log("TempVal:" + degreesCelsuis + " (" + degreesFarenheit + "f)");
		SPlusModule.main.SendEvent("OnReceiveTempValue", degreesCelsuis, degreesFarenheit);
	}
	public void setLightValue(int value) {
		//V.Log("LightLevel:" + value);
		SPlusModule.main.SendEvent("OnReceiveLightValue", value);
	}
	private void handleBioToGraph(byte[] decobbed) {
		if (decobbed.length == 0) return;
		int[] bioData = PacketsByteValuesReader.readBioData(decobbed);
		//V.Log("BreathVal:" + bioData[0]);
		SPlusModule.main.SendEvent("OnReceiveBreathValue", bioData[0]);
	}

	public void handleStreamPacket(Bundle bundle) {
		// also transmit to connector
		if (SPlusModule.main.sessionConnector != null)
			SPlusModule.main.sessionConnector.handleStreamPacket(bundle);

		synchronized (this) {
			byte[] decobbed = bundle.getByteArray(RefreshBluetoothService.REFRESH_BED_NEW_DATA);
			byte packetType = bundle.getByte(RefreshBluetoothService.REFRESH_BED_NEW_DATA_TYPE);
			Log.d(LOGGER.TAG_UI, "handleStreamPacket decobbed : " + Arrays.toString(decobbed) + "packet type : " + packetType);
			if (VLPacketType.PACKET_TYPE_NOTE_ENV_1.ordinal() == packetType) {
				handleNoteEnv(decobbed);
			} else if (VLPacketType.PACKET_TYPE_NOTE_BIO_1.ordinal() == packetType) {
				handleBioToGraph(decobbed);
			} else if (VLPacketType.PACKET_TYPE_ENV_1.ordinal() == packetType) {
				handleNoteEnv(decobbed);
			}


			byte[] bytes = bundle.getByteArray(RefreshBluetoothService.REFRESH_BED_NEW_DATA);
			//byte packetType = bundle.getByte(RefreshBluetoothService.REFRESH_BED_NEW_DATA_TYPE);
			if (bytes.length == 0) return;
			if (VLPacketType.PACKET_TYPE_RETURN.ordinal() == packetType) {
				String strPacket = new String(bytes);
				try {
					JsonRPC receivedRPC = (JsonRPC) new Gson().fromJson(strPacket, JsonRPC.class);
					Log.d(LOGGER.TAG_UI, " handleStreamPacket() rpc id : " + receivedRPC.getId() + " strPacket : " + strPacket);
					handleReceivedRpc(receivedRPC);
					JsonRPC rpcSent = (JsonRPC) CommandStack.remove(Integer.valueOf(receivedRPC.getId()));
					Log.d(LOGGER.TAG_UI, " handleStreamPacket() rpcSent : " + rpcSent);
					if (rpcSent != null) {
						RPCallback callback = rpcSent.getRPCallback();
						Log.d(LOGGER.TAG_UI, " handleStreamPacket() callback : " + callback);
						if (rpcSent.getRPCallback() != null) {
							callback.execute();
						}
						ErrorRpc errorRpc = receivedRPC.getError();
						if (errorRpc != null) {
							handleErrorRPC(rpcSent, errorRpc);
						}
					}
				} catch (JsonSyntaxException e) {
					Log.w(LOGGER.TAG_UI, " strPacket : " + strPacket + "  " + e.getMessage());
				}
			} else if (VLPacketType.PACKET_TYPE_NOTE_HEARTBEAT.ordinal() == packetType) {
				Log.d(LOGGER.TAG_UI, "BaseBluetoothActivity::handleStreamPacket() processed new bluetooth PACKET_TYPE_NOTE_HEARTBEAT : " + Arrays.toString(bytes));
				handleHearBeat(bytes);
			} else if (VLPacketType.PACKET_TYPE_NOTE_BIO_1.ordinal() != packetType) {
				if (VLPacketType.PACKET_TYPE_NOTE_STORE_FOREIGN.ordinal() == packetType || VLPacketType.PACKET_TYPE_NOTE_STORE_LOCAL.ordinal() == packetType) {
					int storeLocal = PacketsByteValuesReader.getStoreLocalBio(bytes);
					Log.w(LOGGER.TAG_UI, "PACKET_TYPE_NOTE_STORE!!! = " + packetType + " NUMBER OF SAMPLES : " + storeLocal);
					if (storeLocal >= 32) {
						updateDataStoredFlag(packetType);
					} else {
						AppFileLog.addTrace("Ignoring samples on BeD because : " + storeLocal + " samples < 32");
					}
				} else {
					Log.d(LOGGER.TAG_UI, "BaseBluetoothActivity::handleStreamPacket() processed new bluetooth PACKET_TYPE_" + packetType + " bytes : " + Arrays.toString(bytes));
				}
			}
		}
	}

	public void handleBreathingRate(Bundle var1) {
		// also transmit to connector
		if (SPlusModule.main.sessionConnector != null)
			SPlusModule.main.sessionConnector.handleBreathingRate(var1);

		V.Log("Breathing rate: " + var1);
		V.Alert("Breathing rate: " + var1);
	}

	public void handleEnvSample(Bundle var1) {
		// also transmit to connector
		if (SPlusModule.main.sessionConnector != null)
			SPlusModule.main.sessionConnector.handleEnvSample(var1);

		Log.d("com.resmed.refresh.ui", "handleEnvSample() ");
	}

	public Promise getSleepStage_currentWaiter;
	public void handleUserSleepState(Bundle bundle) {
		// also transmit to connector
		if (SPlusModule.main.sessionConnector != null)
			SPlusModule.main.sessionConnector.handleUserSleepState(bundle);

		int stage = bundle.getInt("BUNDLE_SLEEP_STATE");
		V.Log("BaseBluetoothActivity.handleUserSleepState. BUNDLE_SLEEP_STATE:" + stage
			+ ";BUNDLE_SLEEP_EPOCH_INDEX:" + bundle.getInt("BUNDLE_SLEEP_EPOCH_INDEX"));
		if (getSleepStage_currentWaiter != null) {
			getSleepStage_currentWaiter.resolve(stage);
			getSleepStage_currentWaiter = null;
		}
	}

	protected boolean isBluetoothServiceRunning() {
		Iterator var1 = ((ActivityManager) this.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE).iterator();

		while (var1.hasNext()) {
			RunningServiceInfo var2 = (RunningServiceInfo) var1.next();
			if (RefreshBluetoothService.class.getName().equals(var2.service.getClassName())) {
				return true;
			}
		}

		return false;
	}

	public boolean isBoundToBluetoothService() {
		return this.mBound;
	}

	public void onActivityResult(int var1, int var2, Intent var3) {
		super.onActivityResult(var1, var1, var3);
		if (var1 == 161) {
			switch (var2) {
				case -1:
					userAllowBluetooth = true;
					return;
				case 0:
					userAllowBluetooth = false;
					this.disconnectBluetoothConn();
					return;
			}
		}

	}

	protected void onCreate(Bundle var1) {
		super.onCreate(var1);
		RpcCommands.setContextBroadcaster(this);
		this.receivers = new ArrayList();
	}

	public void PostModuleInit() {
		boolean serviceRunning = this.isBluetoothServiceRunning();
		V.JavaLog("com.resmed.refresh.ui", ": bluetooth service is running : " + serviceRunning);
		//this.bluetoothManagerService = new Intent(this, RefreshBluetoothService.class);

		// kill the service each time, so we create a new one that's in the same instance as us
		// todo: make so this isn't needed here (wasn't needed for orig code)
		if (serviceRunning) {
			stopService(new Intent(this, RefreshBluetoothService.class));
			serviceRunning = false;
		}
		if (!serviceRunning) {
			/*String var10 = this.getIntent().getStringExtra(this.getString(2131165943));
			String var11 = this.getIntent().getStringExtra(this.getString(2131165944));
			if (var10 != null && var11 != null) {
				this.bluetoothManagerService.putExtra(this.getString(2131165943), var10);
				this.bluetoothManagerService.putExtra(this.getString(2131165944), var11);
			}*/
			Intent bluetoothManagerService = new Intent(MainActivity.main, RefreshBluetoothService.class);
			this.startService(bluetoothManagerService);
		}

		SharedPreferences var4 = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		int var5 = var4.getInt("PREF_CONNECTION_STATE", -1);
		Log.d("com.resmed.refresh.ui", " SleepTimeActivity::onResume conn state : " + var5);
		if (var5 == CONNECTION_STATE.NIGHT_TRACK_ON.ordinal()) {
			int var9 = var4.getInt("PREF_LAST_RPC_ID_USED", -1);
			if (-1 != var9) {
				RpcCommands.setRPCid(var9);
			}
		}

		this.registerReceiver(this.connectionStatusReceiver, new IntentFilter("ACTION_RESMED_CONNECTION_STATUS"));
		this.registerReceiver(this.bluetoothServiceRestartReceiver, new IntentFilter("BLUETOOTH_SERVICE_INTENT_RESTART"));
	}

	protected void onDestroy() {
		super.onDestroy();
		this.unregisterAll();

		//stopService(new Intent(this, RefreshBluetoothService.class));
	}

	protected void onPause() {
		super.onPause();
		this.unBindFromService();
		boolean var1 = ((PowerManager) this.getSystemService("power")).isScreenOn();
		Log.d("com.resmed.refresh.ui", " BaseBluetoothActivity::onPause() isScreenOn : " + var1);
		if (!var1 && IN_SLEEP_SESSION) {
			Calendar var3 = Calendar.getInstance();
			var3.add(13, 600);
			Long var4 = Long.valueOf(var3.getTimeInMillis());
			Intent var5 = new Intent(this.getApplicationContext(), AlarmReceiver.class);
			((AlarmManager) this.getApplicationContext().getSystemService("alarm")).set(0, var4.longValue(), PendingIntent.getBroadcast(this.getApplicationContext(), 0, var5, 134217728));
			this.registerReceiver(this.awakePowerSaveReconnect, new IntentFilter("BLUETOOTH_ALARM_RECONNECT"));
		}
	}

	/*public void onPickedDevice(BluetoothDevice var1) {
		Log.d("com.resmed.refresh.pair", "onPickedDevice");
		Log.d("com.resmed.refresh.dialog", "onPickedDevice() connectionProgressDisplayed = true");
		this.connectionProgressDisplayed = true;
		this.pairAndConnect(var1);
	}*/

	protected void onResume() {
		super.onResume();
		this.bindToService();
	}

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

	public void onTimeoutConnectingDialog() {
		V.Log("Gone: this.showReconnectionScreen();");
	}

	public void pairAndConnect(BluetoothDevice deviceInfo) {
		Log.d("com.resmed.refresh.pair", "connectAndPair");
		this.mDevice = deviceInfo;
		connectingToBeD = true;
		Bundle bundle = new Bundle();
		bundle.putParcelable("deviceInfo", deviceInfo);
		bundle.putBoolean("makePaired", true);
		this.sendMessageToService(11, bundle);
		this.showConnectionProgress();
	}

	public Intent registerReceiver(BroadcastReceiver var1, IntentFilter var2) {
		if (!this.receivers.contains(var1)) {
			this.receivers.add(var1);
			return super.registerReceiver(var1, var2);
		} else {
			return null;
		}
	}

	protected void registerToService() {
		Log.d("com.resmed.refresh.bluetooth", " registerToService : ");

		try {
			Message var5 = Message.obtain(null, 4);
			var5.replyTo = this.mFromService;
			if (mService != null) {
				mService.send(var5);
			}
		} catch (RemoteException var6) {
			Log.d("com.resmed.refresh.bluetooth", " RemoteException : " + var6.getMessage());
		}

		//this.handleConnectionStatus(RefreshApplication.getInstance().getCurrentConnectionState());
		Message var4 = new Message();
		var4.what = 27;
		this.sendMsgBluetoothService(var4);
		V.Log("Send message;");
	}

	public boolean sendBytesToBeD(byte[] var1, VLPacketType var2) {
		int var3 = 0;
		if (var1 != null) {
			var3 = var1.length;
		}

		ByteBuffer var4 = VLP.getInstance().Packetize((byte) var2.ordinal(), (byte) 1, var3, 64, var1);
		ByteBuffer var5 = ByteBuffer.wrap(COBS.getInstance().encode(var4.array()));
		Message var6 = new Message();
		var6.what = 2;
		Bundle var7 = new Bundle();
		var7.putByteArray("bytes", var5.array());
		var6.setData(var7);

		try {
			if (mService != null) {
				mService.send(var6);
			}

			return true;
		} catch (RemoteException var9) {
			var9.printStackTrace();
			return false;
		}
	}

	public void sendMsgBluetoothService(Message var1) {
		Log.d("com.resmed.refresh.ui", " sendMsgBluetoothService msg : " + var1 + "mService : " + mService);
		if (mService != null) {
			try {
				mService.send(var1);
			} catch (RemoteException var9) {
				Log.d("com.resmed.refresh.ui", " RemoteException e : " + var9.getMessage());
				var9.printStackTrace();
				this.bindToService();

				try {
					Thread.sleep(100L);
					mService.send(var1);
				} catch (InterruptedException var7) {
					var7.printStackTrace();
				} catch (RemoteException var8) {
					var8.printStackTrace();
				}
			}
		}
	}

	public void sendRpcToBed(final JsonRPC jsonRPC) {
		synchronized (this) {
			final boolean sendRPC = this.sendRPC(jsonRPC);
			AppFileLog.addTrace(" BaseBluetoothActivity::sendRpcToBed(rpc) rpc : " + jsonRPC + " wasSent : " + sendRPC + " mBound : " + this.mBound);
			Log.d("com.resmed.refresh.ui", "sendRpcToBed wasSent : " + sendRPC);
			if (sendRPC) {
				BaseBluetoothActivity.CommandStack.put(jsonRPC.getId(), jsonRPC);
			}
		}
	}

	public void showBeDPickerDialog() {
		// $FF: Couldn't be decompiled
	}

	public void unBindFromService() {
		AppFileLog.addTrace("BaseBluetoothActivity::unBindingFromService!");
		if (this.mBound) {
			this.getApplicationContext().unbindService(this.mConnection);
			this.mBound = false;
		}
	}

	protected void unregisterAll() {
		Iterator var1 = this.receivers.iterator();

		while (var1.hasNext()) {
			BroadcastReceiver var2 = (BroadcastReceiver) var1.next();

			try {
				this.unregisterReceiver(var2);
			} catch (IllegalArgumentException var8) {
				var8.printStackTrace();
			} finally {
				Log.d("com.resmed.refresh.ui", "unregistered receiver : " + var2);
			}
		}

		this.receivers.clear();
	}

	public void updateDataStoredFlag(int var1) {
		if (var1 == 0) {
			Message var2 = new Message();
			var2.what = 28;
			this.sendMsgBluetoothService(var2);
		}

		//super.updateDataStoredFlag(var1);
	}
}
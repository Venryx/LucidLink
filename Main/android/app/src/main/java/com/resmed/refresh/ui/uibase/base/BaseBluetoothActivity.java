package com.resmed.refresh.ui.uibase.base;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningServiceInfo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.model.json.ResultRPC;
import com.resmed.refresh.model.json.JsonRPC.ErrorRpc;
import com.resmed.refresh.model.json.JsonRPC.RPCallback;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLP;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.BluetoothDataSerializeUtil;
import com.resmed.refresh.utils.Log;
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

import v.lucidlink.V;

class IncomingHandler extends Handler {
	private List partialMsgBuffer;
	// $FF: synthetic field
	final BaseBluetoothActivity this$0;

	public IncomingHandler(BaseBluetoothActivity var1) {
		this.this$0 = var1;
		this.partialMsgBuffer = new ArrayList();
	}

	private void handlePartialStreamPacket(Bundle var1) {
		if (var1 != null) {
			byte[] var2 = var1.getByteArray("REFRESH_BED_NEW_DATA");

			for (int var3 = 0; var3 < var2.length; ++var3) {
				this.partialMsgBuffer.add(Byte.valueOf(var2[var3]));
			}
		}

	}

	private void handlePartialStreamPacketEnd(Bundle var1) {
		ByteBuffer var2 = ByteBuffer.allocate(this.partialMsgBuffer.size());
		Iterator var3 = this.partialMsgBuffer.iterator();

		while (var3.hasNext()) {
			var2.put(((Byte) var3.next()).byteValue());
		}

		this.partialMsgBuffer.clear();
		Bundle var5 = new Bundle();
		var5.putByteArray("REFRESH_BED_NEW_DATA", var2.array());
		if (var1 != null) {
			byte var6 = var1.getByte("REFRESH_BED_NEW_DATA_TYPE");
			int var7 = var1.getInt("REFRESH_BED_NEW_DATA_SIZE");
			var5.putByte("REFRESH_BED_NEW_DATA_TYPE", var6);
			var5.putInt("REFRESH_BED_NEW_DATA_SIZE", var7);
		}

		this.this$0.handleStreamPacket(var5);
	}

	public void handleMessage(Message var1) {
		switch (var1.what) {
			case 5:
				this.this$0.handleStreamPacket(var1.getData());
				return;
			case 6:
				CONNECTION_STATE var4 = (CONNECTION_STATE) var1.getData().get("REFRESH_BED_NEW_CONN_STATUS");
				Log.d("com.resmed.refresh.pair", "BaseActivity MSG_BeD_CONNECTION_STATUS CONNECTION_STATE : " + CONNECTION_STATE.toString(var4));
				this.this$0.handleConnectionStatus(var4);
				return;
			case 7:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 16:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			default:
				super.handleMessage(var1);
				break;
			case 8:
				BluetoothDataSerializeUtil.deleteJsonFile(this.this$0.getApplicationContext());
				Log.d("com.resmed.refresh.dialog", "MSG_UNPAIR showBeDPickerDialog()");
				this.this$0.showBeDPickerDialog();
				return;
			case 14:
				this.this$0.handleSleepSessionStopped(var1.getData());
				return;
			case 15:
				this.this$0.handleEnvSample(var1.getData());
				return;
			case 17:
				this.this$0.handleUserSleepState(var1.getData());
				return;
			case 18:
				this.this$0.handleBreathingRate(var1.getData());
				return;
			case 19:
				this.handlePartialStreamPacket(var1.getData());
				return;
			case 20:
				this.handlePartialStreamPacketEnd(var1.getData());
				return;
			case 21:
				this.this$0.handleSessionRecovered(var1.getData());
				return;
			case 27:
				Bundle var6 = var1.getData();
				if (var6 != null) {
					int var7 = var6.getInt("BUNDLE_BED_AVAILABLE_DATA", -1);
					this.this$0.updateDataStoredFlag(var7);
					return;
				}
		}

	}
}


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
	protected Intent bluetoothManagerService;
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
	private CONNECTION_STATE currentState;
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
	protected Messenger mFromService = new Messenger(new IncomingHandler(this));
	private List receivers;

	// $FF: synthetic method
	static void access$0(Messenger var0) {
		mService = var0;
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
		/*Log.d("com.resmed.refresh.ui", "binding to bluetooth service! isRunning :" + this.isBluetoothServiceRunning() + "service intent : " + this.bluetoothManagerService + " mConnection : " + this.mConnection);
		AppFileLog.addTrace("BaseBluetoothActivity::bindToService!");
		if (!this.mBound) {
			this.getApplicationContext().bindService(this.bluetoothManagerService, this.mConnection, 1);
		}*/
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

	public void handleBreathingRate(Bundle var1) {
		V.Log("Breathing rate: " + var1);
	}

	public void handleConnectionStatus(CONNECTION_STATE var1) {
		Log.d("com.resmed.refresh.pair", "    BaseBluetoothActivity::handleConnectionStatus() connState=" + var1 + " UPDATING_FIRMWARE:" + UPDATING_FIRMWARE + " mBound : " + this.mBound + " isAvailable:" + this.isAvailable + " currentState : " + this.currentState);
		if (var1 != null && !UPDATING_FIRMWARE && this.mBound && this.isAvailable && this.currentState != var1) {
			this.currentState = var1;
			Log.d("com.resmed.refresh.pair", "    handleConnectionStatus connState=" + var1);
			if (CONNECTION_STATE.SOCKET_CONNECTED == var1) {
				//this.sendRpcToBed(RpcCommands.openSession(RefreshModelController.getInstance().getUserSessionID()));
				this.sendRpcToBed(RpcCommands.openSession("user1"));
				BluetoothDataSerializeUtil.writeJsonFile(this.getApplicationContext(), this.mDevice);
			}

			StringBuilder var4 = (new StringBuilder("Should show device paired? ")).append(connectingToBeD).append(" ");
			boolean var5;
			if (CONNECTION_STATE.SESSION_OPENED == var1) {
				var5 = true;
			} else {
				var5 = false;
			}

			Log.d("com.resmed.refresh.dialog", var4.append(var5).toString());
			if (connectingToBeD && CONNECTION_STATE.SESSION_OPENED == var1) {
				Log.d("com.resmed.refresh.pair", "handleConnectionStatus SOCKET_CONNECTED connectingToBeD=" + connectingToBeD);
				connectingToBeD = false;
				Log.d("com.resmed.refresh.dialog", "showDialog device paired");
				//this.showDialog((new CustomDialogBuilder(this)).title(2131165891).setPositiveButton(2131165892, (OnClickListener)null), false);
			} else if (this.connectionProgressDisplayed) {
				this.dismissConnectionProgress();
				Log.d("com.resmed.refresh.dialog", "dismissConnectionProgress");
			}

			Log.d("com.resmed.refresh.dialog", "connectionProgressDisplayed=" + this.connectionProgressDisplayed);
			if (CONNECTION_STATE.SOCKET_BROKEN == var1 || CONNECTION_STATE.SOCKET_RECONNECTING == var1) {
				CORRECT_FIRMWARE_VERSION = true;
				UPDATING_FIRMWARE = false;
				return;
			}
		}

	}

	public void handleEnvSample(Bundle var1) {
		Log.d("com.resmed.refresh.ui", "handleEnvSample() ");

	}

	public void handleReceivedRpc(JsonRPC var1) {
		if (var1 != null) {
			ResultRPC var2 = var1.getResult();
			if (var2 != null) {
				Editor var3 = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext()).edit();
				String var4 = var2.getPayload();
				if (var4.contains("$")) {
					int var9 = var4.indexOf("$");
					String var10 = var4.substring(0, var9);
					String var11 = var4.substring(var9 + 1, -1 + var4.length());
					Log.d("com.resmed.refresh.bluetooth", "BaseBluetoothActivity::handlePayload, boardVersion : " + var10 + " firmwareVersion : " + var11);
					/*RefreshModelController.getInstance().saveBoardVersion(var10);
					RefreshModelController.getInstance().saveFirmwareVersion(var11);*/
					if (CORRECT_FIRMWARE_VERSION) {
						var3.putString(this.getString(2131165305), var10);
						var3.commit();
						this.bioSensorSerialNrRPC = getRpcCommands().getBioSensorSerialNumber();
						this.bioSensorSerialNrRPC.setRPCallback(new RPCallback() {
																	public void execute() {
																		Log.d("com.resmed.refresh.ui", "handleReceivedRpc firmwareVersion :" + var11);
																		if (RefreshTools.checkForFirmwareUpgrade(BaseBluetoothActivity.this, var11)) {
									/*BaseBluetoothActivity.CORRECT_FIRMWARE_VERSION = false;
									Intent var2 = new Intent(BaseBluetoothActivity.this, UpdateOTAActivity.class);
									BaseBluetoothActivity.this.startActivity(var2);*/
																			V.Log("@V(would be updating the firmware here)");
																		}

																	}

																	public void onError(ErrorRpc var1) {
																	}

																	public void preExecute() {
																	}
																}
						);
						this.sendRpcToBed(this.bioSensorSerialNrRPC);
					}
				}

				Log.d("com.resmed.refresh.ui", "handleReceivedRpc committing  extra_bed_sensor_serial:" + var4);
				Log.d("com.resmed.refresh.ui", "handleReceivedRpc committing  bioSensorSerialNrRPC:" + this.bioSensorSerialNrRPC + " receivedRPC.getId() :" + var1.getId() + " CORRECT_FIRMWARE_VERSION " + CORRECT_FIRMWARE_VERSION);
				if (this.bioSensorSerialNrRPC != null && this.bioSensorSerialNrRPC.getId() == var1.getId() && CORRECT_FIRMWARE_VERSION) {
					var3.putString(this.getString(2131165306), var4);
					//RefreshModelController.getInstance().setBedId(var4);
					var3.commit();
				}
			}
		}

	}

	public void handleSessionRecovered(Bundle var1) {
		Log.d("com.resmed.refresh.ui", " BaseBluetoothActivity::handleSessionRecovered()");
	}

	public void handleSleepSessionStopped(Bundle var1) {
	}

	public void handleStreamPacket(Bundle bundle) {
		synchronized (this) {
			byte[] arrby = bundle.getByteArray("REFRESH_BED_NEW_DATA");
			byte by = bundle.getByte("REFRESH_BED_NEW_DATA_TYPE");
			int n = arrby.length;
			if (n == 0) return;
			if (VLPacketType.PACKET_TYPE_RETURN.ordinal() == by) {
				String string = new String(arrby);
				Gson gson = new Gson();
				try {
					JsonRPC.ErrorRpc errorRpc;
					JsonRPC jsonRPC = (JsonRPC)gson.fromJson(string, (Class)JsonRPC.class);
					Log.d("com.resmed.refresh.ui", (" handleStreamPacket() rpc id : " + jsonRPC.getId() + " strPacket : " + string));
					this.handleReceivedRpc(jsonRPC);
					JsonRPC jsonRPC2 = (JsonRPC)CommandStack.remove(jsonRPC.getId());
					Log.d("com.resmed.refresh.ui", (" handleStreamPacket() rpcSent : " + jsonRPC2));
					if (jsonRPC2 == null) return;
					JsonRPC.RPCallback rPCallback = jsonRPC2.getRPCallback();
					Log.d("com.resmed.refresh.ui", (" handleStreamPacket() callback : " + rPCallback));
					if (jsonRPC2.getRPCallback() != null) {
						rPCallback.execute();
					}
					if ((errorRpc = jsonRPC.getError()) == null) return;
					this.handleErrorRPC(jsonRPC2, errorRpc);
				}
				catch (JsonSyntaxException var8_11) {
					Log.w("com.resmed.refresh.ui", (" strPacket : " + string + "  " + var8_11.getMessage()));
				}
			} else if (VLPacketType.PACKET_TYPE_NOTE_HEARTBEAT.ordinal() == by) {
				Log.d("com.resmed.refresh.ui", ("BaseBluetoothActivity::handleStreamPacket() processed new bluetooth PACKET_TYPE_NOTE_HEARTBEAT : " + Arrays.toString(arrby)));
				this.handleHearBeat(arrby);
			} else {
				if (VLPacketType.PACKET_TYPE_NOTE_BIO_1.ordinal() == by) return;
				if (VLPacketType.PACKET_TYPE_NOTE_STORE_FOREIGN.ordinal() == by || VLPacketType.PACKET_TYPE_NOTE_STORE_LOCAL.ordinal() == by) {
					int n2 = PacketsByteValuesReader.getStoreLocalBio(arrby);
					Log.w("com.resmed.refresh.ui", ("PACKET_TYPE_NOTE_STORE!!! = " + by + " NUMBER OF SAMPLES : " + n2));
					if (n2 >= 32) {
						this.updateDataStoredFlag((int)by);
					} else {
						AppFileLog.addTrace(("Ignoring samples on BeD because : " + n2 + " samples < 32"));
					}
				} else {
					Log.d("com.resmed.refresh.ui", ("BaseBluetoothActivity::handleStreamPacket() processed new bluetooth PACKET_TYPE_" + by + " bytes : " + Arrays.toString(arrby)));
				}
			}
		}
	}


	public void handleUserSleepState(Bundle var1) {
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
		this.bluetoothManagerService = new Intent(this, RefreshBluetoothService.class);

		// kill the service each time, so we create a new one that's in the same instance as us
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
			this.startService(this.bluetoothManagerService);
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
			Message var5 = Message.obtain((Handler) null, 4);
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

	public void sendRpcToBed(JsonRPC param1) {
		// $FF: Couldn't be decompiled
	}

	public void showBeDPickerDialog() {
		// $FF: Couldn't be decompiled
	}

	public void unBindFromService() {
		/*AppFileLog.addTrace("BaseBluetoothActivity::unBindingFromService!");
		if (this.mBound) {
			this.getApplicationContext().unbindService(this.mConnection);
			this.mBound = false;
		}*/
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

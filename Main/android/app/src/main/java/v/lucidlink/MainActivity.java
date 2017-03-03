package v.lucidlink;

import android.Manifest;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.resmed.refresh.bed.LedsState;
import com.resmed.refresh.bed.RPCMapper;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.bluetooth.RefreshBluetoothService;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.LOGGER;
import com.resmed.refresh.utils.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import SPlus.SPlusModule;
import v.LibMuse.LibMuseModule;
import vpackages.V;

import static v.lucidlink.LLHolder.LL;

public class MainActivity extends BaseBluetoothActivity {
	public static MainActivity main;
	public MainActivity() {
		super();
		main = this;
		LibMuseModule.mainActivity = this;
	}

	@Override protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		LL = new LucidLink();
		//LL.baseContext = getBaseContext();
		LL.appContext = (MainApplication)getApplicationContext();
		LL.analytics = FirebaseAnalytics.getInstance(this);
		MainActivity.main.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		startService(new Intent(this, MainService.class));
	}
	/*@Override protected void onDestroy() {
		//JSBridge.SendEvent("PreAppClose");
		//SPlusModule.main.ShutDown();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		super.onDestroy();
	}*/

	List<BroadcastReceiver> receivers = new ArrayList<>();
	private void AddAndRegisterReceiver(BroadcastReceiver receiver, IntentFilter filter) {
		MainActivity.main.registerReceiver(receiver, filter);
		receivers.add(receiver);
	}
	public void RemoveReceivers() {
		for (BroadcastReceiver receiver : receivers)
			MainActivity.main.unregisterReceiver(receiver);
		receivers.clear();
	}

	@Override protected void onStart() {
		super.onStart();
		boolean restarting = LL.mainModule != null;
		if (restarting)
			OnModuleInitOrRestart();
	}
	@Override protected void onStop() {
		super.onStop();
		RemoveReceivers();
	}

	public boolean bluetoothConnected;
	void UpdateBluetoothConnected() {
		AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		boolean connected = audioManager.isBluetoothA2dpOn();
		if (connected == bluetoothConnected) return;

		bluetoothConnected = connected;
		LL.mainModule.ApplyVolumeForCurrentType();
		V.Log(bluetoothConnected ? "Bluetooth speaker connected." : "Bluetooth speaker disconnected.");
	}

	public void PostModuleInit() {
		V.LogJava("PostModuleInit");
		OnModuleInitOrRestart();
		UpdateBluetoothConnected();
	}
	void OnModuleInitOrRestart() {
		AddAndRegisterReceiver(new BroadcastReceiver() {
			@Override public void onReceive(Context context, Intent intent) {
				UpdateBluetoothConnected();
				// try again a second later, since audio-manager's state can take a bit to update after a change (such as might have just happened)
				V.WaitXThenRun(1000, ()->UpdateBluetoothConnected());
			}
		}, V.IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED, BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED, BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED));
		AddAndRegisterReceiver(new BroadcastReceiver() {
			public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent) {
				MainActivity.this.handleConnectionStatus((CONNECTION_STATE)paramAnonymousIntent.getExtras().get("EXTRA_RESMED_CONNECTION_STATE"));
			}
		}, new IntentFilter("ACTION_RESMED_CONNECTION_STATUS"));
	}

	static final int REQUEST_WRITE_STORAGE = 112;
	public boolean ArePermissionsGranted() {
		V.Log("Checking if permissions granted. @AndroidVersion:" + Build.VERSION.SDK_INT + " @RefVersion:" + Build.VERSION_CODES.M);
		// workaround for runtime bug of ContextCompat evaluating to the old version (in S+ jar)
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
			return true;
		return ContextCompat.checkSelfPermission(LL.reactContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
	}
	public void EnsurePermissionsGranted() {
		boolean hasPermission = ArePermissionsGranted();
		if (!hasPermission)
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_WRITE_STORAGE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				recreate();
			} else {
				V.Toast("The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG);
			}
		}
	}

	// returns the name of the main component registered from JavaScript (used to schedule rendering of the component)
	@Override
	protected String getMainComponentName() {
		return "LucidLink";
	}

	@Override
	public void onBackPressed() {
	}

	@Override public boolean onKeyDown(int keyCode, KeyEvent event)  {
		char keyChar = (char)event.getUnicodeChar();
		JSBridge.SendEvent("OnKeyDown", keyCode, String.valueOf(keyChar));
		if (LL.mainModule != null && LL.mainModule.blockUnusedKeys)
			return true;
		return super.onKeyDown(keyCode, event);
	}
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		char keyChar = (char)event.getUnicodeChar();
		JSBridge.SendEvent("OnKeyUp", keyCode, String.valueOf(keyChar));
		if (LL.mainModule != null && LL.mainModule.blockUnusedKeys)
			return true;
		return super.onKeyUp(keyCode, event);
	}

	// S+ stuff
	// ==========

	boolean realTimeStreamActive;
	//Timer getSleepStateTimer;
	void OnRealTimeStreamStart() {
		/*V.Assert(getSleepStateTimer == null, "Timer should be null on real-time-stream start!");

		getSleepStateTimer = new Timer();
		int intervalInMS = SPlusModule.main.bioBufferSize * 1000;
		getSleepStateTimer.scheduleAtFixedRate(new TimerTask() {
			@Override public void run() {
				SPlusModule.main.sessionConnector.service.sleepSessionManager.requestUserSleepState();
			}
		}, intervalInMS, intervalInMS);*/

		bioCount = 0;
		lastEpochIndex = -1;
		lastStageValue = -1;
	}
	void OnRealTimeStreamEnd() {
		/*V.Assert(getSleepStateTimer != null, "Timer should not be null on real-time-stream end!");
		getSleepStateTimer.cancel();
		getSleepStateTimer = null;*/
	}

	boolean nightTrackActive;
	//Timer getBioBufferTimer;
	void OnNightTrackStart() {
		/*V.Assert(getBioBufferTimer == null, "Timer should be null on night-track start!");

		getBioBufferTimer = new Timer();
		int intervalInMS = SPlusModule.main.bioBufferSize * 1000;
		getBioBufferTimer.scheduleAtFixedRate(new TimerTask() {
			@Override public void run() {
				SPlusModule.main.sessionConnector.requestSamples_whateverIsLeft();
			}
		}, intervalInMS, intervalInMS);*/

		bioCount = 0;
		lastEpochIndex = -1;
		lastStageValue = -1;
	}
	void OnNightTrackEnd() {
		/*V.Assert(getBioBufferTimer != null, "Timer should not be null on night-track end!");
		getBioBufferTimer.cancel();
		getBioBufferTimer = null;*/
	}

	public void handleConnectionStatus(final CONNECTION_STATE state) {
		LL.mainModule.connectionState = state;
		V.Log("Connection status changed: " + state);

		if (SPlusModule.main != null && SPlusModule.main.sessionConnector != null) {
			if (state == CONNECTION_STATE.SOCKET_CONNECTED || state == CONNECTION_STATE.SOCKET_RECONNECTING
				|| state == CONNECTION_STATE.SESSION_OPENING || state == CONNECTION_STATE.SESSION_OPENED) {
				sendRpcToBed(RPCMapper.main.leds(LedsState.GREEN));
			} else if (state == CONNECTION_STATE.REAL_STREAM_ON || state == CONNECTION_STATE.NIGHT_TRACK_ON) {
				sendRpcToBed(RPCMapper.main.leds(LedsState.OFF));
			}

			if (state == CONNECTION_STATE.REAL_STREAM_ON) {
				if (!realTimeStreamActive) {
					realTimeStreamActive = true;
					OnRealTimeStreamStart();
				}
			} else if (realTimeStreamActive) {
				realTimeStreamActive = false;
				OnRealTimeStreamEnd();
			}

			if (state == CONNECTION_STATE.NIGHT_TRACK_ON) {
				if (!nightTrackActive) {
					nightTrackActive = true;
					OnNightTrackStart();
				}
			} else if (nightTrackActive) {
				nightTrackActive = false;
				OnNightTrackEnd();
			}
		}

		super.handleConnectionStatus(state);
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
					JsonRPC rpcSent = (JsonRPC) BaseBluetoothActivity.CommandStack.remove(receivedRPC.getId());
					Log.d(LOGGER.TAG_UI, " handleStreamPacket() rpcSent : " + rpcSent);
					if (rpcSent != null) {
						JsonRPC.RPCallback callback = rpcSent.getRPCallback();
						Log.d(LOGGER.TAG_UI, " handleStreamPacket() callback : " + callback);
						if (rpcSent.getRPCallback() != null) {
							callback.execute();
						}
						JsonRPC.ErrorRpc errorRpc = receivedRPC.getError();
						if (errorRpc != null) {
							handleErrorRPC(rpcSent, errorRpc);
						}
					}
				} catch (JsonSyntaxException e) {
					Log.w(LOGGER.TAG_UI, " strPacket : " + strPacket + "  " + e.getMessage());
				}
			} else if (VLPacketType.PACKET_TYPE_NOTE_HEARTBEAT.ordinal() == packetType) {
				Log.d(LOGGER.TAG_UI, "BaseBluetoothActivity::handleStreamPacket() processed new bluetooth PACKET_TYPE_NOTE_HEARTBEAT : " + Arrays.toString(bytes));
				handleHeartBeat(bytes);
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
	private void handleBioToGraph(byte[] decobbed) {
		if (decobbed.length == 0) return;
		int[] bioData = PacketsByteValuesReader.readBioData(decobbed);
		//V.Log("BreathVal:" + bioData[0] + ";" + bioData[1] + ";" + bioData.length);
		SPlusModule.main.SendEvent("OnReceiveBreathValue", bioData[0]);
		SPlusModule.main.sessionConnector.service.sleepSessionManager.rm20Manager.writeSampleData(bioData[0], bioData[1]);
		bioCount++;
		// only request user-sleep-state every 16-samples/1-second (sleep-stage is only actually recalculated every epoch/480-samples/30-seconds, but we request
		// ...a refresh every second, since samples can be dropped leading to offsets)
		if (bioCount % 16 == 0)
			SPlusModule.main.sessionConnector.service.sleepSessionManager.requestUserSleepState();
	}

	int bioCount;
	int lastEpochIndex = -1;
	int lastStageValue = -1;
	public void handleUserSleepState(Bundle bundle) {
		// also transmit to connector
		if (SPlusModule.main.sessionConnector != null)
			SPlusModule.main.sessionConnector.handleUserSleepState(bundle);

		int stageValue = bundle.getInt("BUNDLE_SLEEP_STATE");
		int epochIndex = bundle.getInt("BUNDLE_SLEEP_EPOCH_INDEX");

		//if (epochIndex > lastEpochIndex) {
		if (epochIndex > lastEpochIndex || stageValue != lastStageValue) {
			lastEpochIndex = epochIndex;
			lastStageValue = stageValue;
			V.Log("Got sleep-stage: " + stageValue + " (" + SleepStage.values()[stageValue - 1].name() + ") @epoch:" + epochIndex
				+ " @bioCount:" + bioCount);

			/*V.Log("BaseBluetoothActivity.handleUserSleepState. BUNDLE_SLEEP_STATE:" + stage
				+ ";BUNDLE_SLEEP_EPOCH_INDEX:" + bundle.getInt("BUNDLE_SLEEP_EPOCH_INDEX"));
			if (getSleepStage_currentWaiter != null) {
				getSleepStage_currentWaiter.resolve(stage);
				getSleepStage_currentWaiter = null;
			}*/
			SPlusModule.main.SendEvent("OnReceiveSleepStage", stageValue);
		}
	}
}
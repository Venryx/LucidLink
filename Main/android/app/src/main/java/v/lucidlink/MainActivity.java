package v.lucidlink;

import android.Manifest;
import android.bluetooth.BluetoothA2dp;
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

import com.facebook.react.ReactPackage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.resmed.refresh.bed.LedsState;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.bluetooth.RefreshBluetoothService;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.LOGGER;
import com.resmed.refresh.utils.Log;

import java.util.Arrays;
import java.util.List;

import SPlus.SPlusModule;
import v.LibMuse.LibMuseModule;

public class MainActivity extends BaseBluetoothActivity implements BluetoothDataListener {
	public static MainActivity main;
	public MainActivity() {
		super();
		main = this;
		LibMuseModule.mainActivity = this;
		SPlusModule.mainActivity = this;
	}

	public boolean bluetoothConnected;
	@Override protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		MainActivity.main.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// Bluetooth device gained/lost it's state as the media audio device
				if(intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, -1) == BluetoothA2dp.STATE_CONNECTED) {
					MainActivity.this.bluetoothConnected = true;
					V.Toast("A2DP device connected!", Toast.LENGTH_LONG);
					LL.main.ApplyVolumeForCurrentType();
				} else {
					MainActivity.this.bluetoothConnected = false;
					V.Toast("A2DP device disconnected!", Toast.LENGTH_LONG);
					LL.main.ApplyVolumeForCurrentType();
				}
			}
		}, new IntentFilter(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED));
	}
	@Override protected void onDestroy() {
		SPlusModule.main.ShutDown();
	}

	static final int REQUEST_WRITE_STORAGE = 112;
	public boolean ArePermissionsGranted() {
		V.Log("Checking if permissions granted. @AndroidVersion:" + Build.VERSION.SDK_INT + " @RefVersion:" + Build.VERSION_CODES.M);
		// workaround for runtime bug of ContextCompat evaluating to the old version (in S+ jar)
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
			return true;
		return ContextCompat.checkSelfPermission(LL.main.reactContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
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
		if (LL.main == null) super.onKeyDown(keyCode, event);

		char keyChar = (char)event.getUnicodeChar();
		LL.main.SendEvent("OnKeyDown", keyCode, String.valueOf(keyChar));
		if (LL.main.blockUnusedKeys)
			return true;
		return super.onKeyDown(keyCode, event);
	}
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (LL.main == null) super.onKeyUp(keyCode, event);

		char keyChar = (char)event.getUnicodeChar();
		LL.main.SendEvent("OnKeyUp", keyCode, String.valueOf(keyChar));
		if (LL.main.blockUnusedKeys)
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
		LL.main.connectionState = state;
		V.Log("Connection status changed: " + state);

		if (SPlusModule.main != null && SPlusModule.main.sessionConnector != null) {
			if (state == CONNECTION_STATE.SOCKET_CONNECTED || state == CONNECTION_STATE.SOCKET_RECONNECTING
				|| state == CONNECTION_STATE.SESSION_OPENING || state == CONNECTION_STATE.SESSION_OPENED) {
				sendRpcToBed(BaseBluetoothActivity.getRpcCommands().leds(LedsState.GREEN));
			} else if (state == CONNECTION_STATE.REAL_STREAM_ON || state == CONNECTION_STATE.NIGHT_TRACK_ON) {
				sendRpcToBed(BaseBluetoothActivity.getRpcCommands().leds(LedsState.OFF));
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
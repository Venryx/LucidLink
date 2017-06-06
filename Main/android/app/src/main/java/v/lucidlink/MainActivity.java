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

import com.annimon.stream.Stream;
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

import SPlus.SPlusModule;
import v.LibMuse.LibMuseModule;
import vpackages.V;

import static v.lucidlink.LLS.LL;

class BreathValuePair {
	public BreathValuePair(double val1, double val2) {
		this.val1 = val1;
		this.val2 = val2;
	}
	public double val1;
	public double val2;
}

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
	public void AddAndRegisterReceiver(BroadcastReceiver receiver, IntentFilter filter) {
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
		return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
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

		ResetTrackerData();
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

		ResetTrackerData();
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
					JsonRPC receivedRPC = new Gson().fromJson(strPacket, JsonRPC.class);
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
		int[] breathValues = PacketsByteValuesReader.readBioData(decobbed);
		BreathValuePair breathValuePair = new BreathValuePair(breathValues[0], breathValues[1]);
		//V.Log("BreathVal:" + bioData[0] + ";" + bioData[1] + ";" + bioData.length);
		SPlusModule.main.SendEvent_Buffered("OnReceiveBreathValues", 300, breathValues[0], breathValues[1]);
		SPlusModule.main.sessionConnector.service.sleepSessionManager.rm20Manager.writeSampleData(breathValues[0], breathValues[1]);
		breathVals_count++;

		// Only request user-sleep-state every 16-samples/1-second.
		// (Sleep-stage is only actually recalculated every epoch/480-samples/30-seconds, but we
		// 		request a refresh every second, since samples can be dropped leading to offsets)
		if (breathVals_count % 16 == 0)
			SPlusModule.main.sessionConnector.service.sleepSessionManager.requestUserSleepState();

		/*if (breathVals_last15Sec_avg == null) {
			breathVals_last15Sec_avg = breathValuePair;
		} else {
			breathVals_last15Sec_avg = new BreathValuePair(
				(int)((((double)breathVals_last15Sec_avg.val1 * (BREATH_VALUES_PER_AVERAGE_PERIOD - 1)) + breathValuePair.val1) / BREATH_VALUES_PER_AVERAGE_PERIOD),
				(int)((((double)breathVals_last15Sec_avg.val2 * (BREATH_VALUES_PER_AVERAGE_PERIOD - 1)) + breathValuePair.val2) / BREATH_VALUES_PER_AVERAGE_PERIOD)
			);
		}
		SPlusModule.main.SendEvent("OnReceiveBreathValueAverage", breathVals_last15Sec_avg.val1, breathVals_last15Sec_avg.val2);*/

		int newEntryIndex = breathVals_last30Sec_lastEntryIndex < BREATH_VALUES_PER_30S - 1 ? breathVals_last30Sec_lastEntryIndex + 1 : 0;
		breathVals_last30Sec[newEntryIndex] = breathValuePair;
		breathVals_last30Sec_lastEntryIndex = newEntryIndex;

		CalculateBreathValueDerivatives(newEntryIndex);
	}
	void CalculateBreathValueDerivatives(int newEntryIndex) {
		int breathVals_last30Sec_filledCount = (int)Stream.of(breathVals_last30Sec).filter(a->a != null).count();
		BreathValuePair breathVals_last15Sec_min = new BreathValuePair(Integer.MAX_VALUE, Integer.MAX_VALUE);
		BreathValuePair breathVals_last15Sec_max = new BreathValuePair(0, 0);
		//BreathValuePair breathVals_last30Sec_total = new BreathValuePair(0, 0);
		BreathValuePair breathVals_prev15Sec_total = new BreathValuePair(0, 0);
		BreathValuePair breathVals_last15Sec_total = new BreathValuePair(0, 0);
		for (int i = newEntryIndex; i > newEntryIndex - BREATH_VALUES_PER_30S; i--) {
			BreathValuePair pair = breathVals_last30Sec[V.WrapToRange_MaxOut(i, 0, BREATH_VALUES_PER_30S)];
			if (pair == null) continue;

			boolean inCurrentSegment = i > newEntryIndex - BREATH_VALUES_PER_15S;
			if (inCurrentSegment) {
				breathVals_last15Sec_min.val1 = Math.min(breathVals_last15Sec_min.val1, pair.val1);
				breathVals_last15Sec_min.val2 = Math.min(breathVals_last15Sec_min.val2, pair.val2);
				breathVals_last15Sec_max.val1 = Math.max(breathVals_last15Sec_max.val1, pair.val1);
				breathVals_last15Sec_max.val2 = Math.max(breathVals_last15Sec_max.val2, pair.val2);
			}
			BreathValuePair pairToUpdate = inCurrentSegment ? breathVals_last15Sec_total : breathVals_prev15Sec_total;
			pairToUpdate.val1 += pair.val1;
			pairToUpdate.val2 += pair.val2;
		}
		BreathValuePair breathVals_prev15Sec_avg = new BreathValuePair(
			breathVals_prev15Sec_total.val1 / (breathVals_last30Sec_filledCount - BREATH_VALUES_PER_15S),
			breathVals_prev15Sec_total.val2 / (breathVals_last30Sec_filledCount - BREATH_VALUES_PER_15S)
		);
		BreathValuePair breathVals_last15Sec_avg = new BreathValuePair(
			breathVals_last15Sec_total.val1 / Math.min(BREATH_VALUES_PER_15S, breathVals_last30Sec_filledCount),
			breathVals_last15Sec_total.val2 / Math.min(BREATH_VALUES_PER_15S, breathVals_last30Sec_filledCount)
		);
		SPlusModule.main.SendEvent_Buffered("OnReceiveBreathValueMinMaxAndAverages", 300,
			breathVals_last15Sec_min.val1, breathVals_last15Sec_min.val2,
			breathVals_last15Sec_max.val1, breathVals_last15Sec_max.val2,
			breathVals_last15Sec_avg.val1, breathVals_last15Sec_avg.val2
		);

		// if data from last 30-seconds is all filled, calculate breathing-depth
		if (breathVals_last30Sec_filledCount == BREATH_VALUES_PER_30S) {
			BreathValuePair breathVals_prev15Sec_totalDevianceFromAverage = new BreathValuePair(0, 0);
			BreathValuePair breathVals_last15Sec_totalDevianceFromAverage = new BreathValuePair(0, 0);
			for (int i = newEntryIndex; i > newEntryIndex - BREATH_VALUES_PER_30S; i--) {
				BreathValuePair pair = breathVals_last30Sec[V.WrapToRange_MaxOut(i, 0, BREATH_VALUES_PER_30S)];
				boolean inCurrentSegment = i > newEntryIndex - BREATH_VALUES_PER_15S;
				if (inCurrentSegment) {
					breathVals_last15Sec_totalDevianceFromAverage.val1 += V.Distance(pair.val1, breathVals_last15Sec_avg.val1);
					breathVals_last15Sec_totalDevianceFromAverage.val2 += V.Distance(pair.val2, breathVals_last15Sec_avg.val2);
				} else {
					breathVals_prev15Sec_totalDevianceFromAverage.val1 += V.Distance(pair.val1, breathVals_prev15Sec_avg.val1);
					breathVals_prev15Sec_totalDevianceFromAverage.val2 += V.Distance(pair.val2, breathVals_prev15Sec_avg.val2);
				}
			}

			BreathValuePair breathVals_prev15Sec_averageDeviance = new BreathValuePair(
				breathVals_prev15Sec_totalDevianceFromAverage.val1 / BREATH_VALUES_PER_15S,
				breathVals_prev15Sec_totalDevianceFromAverage.val2 / BREATH_VALUES_PER_15S
			);
			BreathValuePair breathVals_last15Sec_averageDeviance = new BreathValuePair(
				breathVals_last15Sec_totalDevianceFromAverage.val1 / BREATH_VALUES_PER_15S,
				breathVals_last15Sec_totalDevianceFromAverage.val2 / BREATH_VALUES_PER_15S
			);

			SPlusModule.main.SendEvent_Buffered("OnReceiveBreathingDepth", 300, breathVals_prev15Sec_averageDeviance.val1, breathVals_last15Sec_averageDeviance.val1);
		}
	}

	final int SAMPLES_PER_SECOND = 16;
	final int BREATH_VALUES_PER_15S = SAMPLES_PER_SECOND * 15; // base breath-value average on the last 15-seconds
	final int BREATH_VALUES_PER_30S = SAMPLES_PER_SECOND * 30; // store data from the last 30-seconds

	// data being tracked
	int breathVals_count;
	BreathValuePair[] breathVals_last30Sec = new BreathValuePair[BREATH_VALUES_PER_30S];
	int breathVals_last30Sec_lastEntryIndex = -1;
	/*BreathValuePair breathVals_last15Sec_min;
	BreathValuePair breathVals_last15Sec_max;
	BreathValuePair breathVals_last15Sec_avg;*/
	int lastEpochIndex = -1;
	int lastStageValue = -1;
	//int[] breathValues = new int[SAMPLES_PER_SECOND * ];
	void ResetTrackerData() {
		breathVals_count = 0;
		breathVals_last30Sec = new BreathValuePair[BREATH_VALUES_PER_30S];
		breathVals_last30Sec_lastEntryIndex = -1;
		/*breathVals_last15Sec_min = new BreathValuePair(0, 0);
		breathVals_last15Sec_max = new BreathValuePair(0, 0);
		breathVals_last15Sec_avg = new BreathValuePair(0, 0);*/
		lastEpochIndex = -1;
		lastStageValue = -1;
	}

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
				+ " @breathVals_count:" + breathVals_count);

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
package com.resmed.refresh.sleepsession;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.google.gson.Gson;
import com.resmed.refresh.bed.BedCommandsRPCMapper;
import com.resmed.refresh.bluetooth.BluetoothSetup;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.bluetooth.RefreshBluetoothService;
import com.resmed.refresh.bluetooth.RefreshBluetoothServiceClient;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.model.json.JsonRPC.ErrorRpc;
import com.resmed.refresh.model.json.JsonRPC.RPCallback;
import com.resmed.refresh.model.json.ResultRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.KillableRunnable;
import com.resmed.refresh.utils.KillableRunnable.KillableRunner;
import com.resmed.refresh.utils.LOGGER;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.refresh.utils.SortedList;
import com.resmed.rm20.SleepParams;

import org.acra.ACRAConstants;

import de.greenrobot.dao.DaoException;
import v.lucidlink.LL;
import v.lucidlink.MainActivity;
import v.lucidlink.V;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class SleepSessionConnector implements BluetoothDataListener {
	private static final int ALARM_REQUEST_CODE = 5123;
	private static final int MAX_SOUND_EVENTS = 5;
	private BaseBluetoothActivity bAct;
	private int bioCurrentTotalCount;
	private int bioOnBeD;
	private KillableRunnable cancelSleepSessionRunnable;
	private boolean closeSleepSession;
	private int envTotalCount;
	private boolean hasSessionStarted;
	private Runnable heartbeatTimeoutRunnable;
	private boolean isClosingSession;
	private boolean isHandlingHeartBeat;
	private boolean isRecovering = false;
	private boolean isSyncAndStop;
	private boolean isWaitLastSamples;
	private Long lastHeartBeatTimestamp;
	private Handler myHeartbeatHandler;
	private JsonRPC pendingBioSamplesRpc;
	private JsonRPC pendingEnvSamplesRpc;
	private int sizeToStore = 5;
	private SortedList<Integer> soundTopAmplitudes;

	public SleepSessionConnector(BaseBluetoothActivity paramBaseBluetoothActivity, int paramInt, boolean paramBoolean) {
		this.isSyncAndStop = paramBoolean;
		this.bAct = paramBaseBluetoothActivity;
		this.bioOnBeD = paramInt;
		this.isClosingSession = false;
		this.hasSessionStarted = false;
		this.myHeartbeatHandler = new Handler();
	}

	private void closeSession() {
		AppFileLog.addTrace("CLOSING SLEEP SESSION, isHandlingHeartBeat :" + this.isHandlingHeartBeat);
		CONNECTION_STATE state = LL.main.connectionState;
		if ((state == CONNECTION_STATE.SOCKET_NOT_CONNECTED) || (state == CONNECTION_STATE.BLUETOOTH_OFF)
				|| (state == CONNECTION_STATE.SOCKET_BROKEN) || (state == CONNECTION_STATE.SOCKET_RECONNECTING))
			return;
		stopSleepSession();
	}

	private int decompressLight(final int n) {
		if (n < 64) {
			return n;
		}
		if (n < 96) {
			return 64 + 2 * (n - 64);
		}
		if (n < 128) {
			return 128 + 4 * (n - 96);
		}
		if (n < 160) {
			return 256 + 8 * (n - 128);
		}
		if (n < 192) {
			return 512 + 16 * (n - 160);
		}
		if (n < 240) {
			return 1024 + 64 * (n - 192);
		}
		if (n < 255) {
			return 4096 + 128 * (n - 240);
		}
		return 6000;
	}

	private void handleEnvData(byte[] paramArrayOfByte, boolean paramBoolean) {
		this.isHandlingHeartBeat = false;
	}

	private void handleSamplesTransmissionCompleteForRPC(JsonRPC receivedRPC) {
		AppFileLog.addTrace("IN : " + receivedRPC.getId() + "  Tx completed " + ("pendingBio is " + (this.pendingBioSamplesRpc == null ? "null" : "NOT null")) + "  " + ("pendingEnv is " + (this.pendingEnvSamplesRpc == null ? "null" : "NOT null")) + " isWaitLastSamples=" + this.isWaitLastSamples);
		if (this.pendingBioSamplesRpc != null && this.pendingBioSamplesRpc.getId() == receivedRPC.getId()) {
			if (!(this.pendingEnvSamplesRpc == null || this.bAct == null)) {
				AppFileLog.addTrace("IN=>OUT : Requesting ENV ID : " + this.pendingEnvSamplesRpc.getId());
				this.bAct.sendRpcToBed(this.pendingEnvSamplesRpc);
			}
			this.pendingBioSamplesRpc = null;
		}
		if (this.pendingEnvSamplesRpc != null && this.pendingEnvSamplesRpc.getId() == receivedRPC.getId()) {
			this.pendingEnvSamplesRpc = null;
			this.isHandlingHeartBeat = false;
			AppFileLog.addTrace("");
			this.myHeartbeatHandler.removeCallbacks(this.heartbeatTimeoutRunnable);
			if (this.isRecovering) {
				this.isRecovering = false;
			}
			this.lastHeartBeatTimestamp = Long.valueOf(System.currentTimeMillis());
			//this.sleepSessionListener.onSessionOk();
			if (this.isClosingSession) {
				this.isWaitLastSamples = true;
				this.bAct.updateDataStoredFlag(0);
			}
		}
		if (this.isWaitLastSamples && this.pendingBioSamplesRpc == null && this.pendingEnvSamplesRpc == null && this.isClosingSession) {
			JsonRPC stopSampleRpc = BaseBluetoothActivity.getRpcCommands().stopNightTimeTracking();
			stopSampleRpc.setRPCallback(new RPCallback() {
				public void preExecute() {
				}

				public void onError(ErrorRpc errRpc) {
				}

				public void execute() {
					if (SleepSessionConnector.this.bAct != null && !SleepSessionConnector.this.bAct.isFinishing()) {
						SleepSessionConnector.this.bAct.sendRpcToBed(BaseBluetoothActivity.getRpcCommands().clearBuffers());
					}
				}
			});
			AppFileLog.addTrace(" SleepSessionConnector last samples! -> stop & clear");
			this.bAct.sendRpcToBed(stopSampleRpc);
			Message msg = new Message();
			msg.what = 14;
			if (this.bAct != null) {
				this.bAct.sendMsgBluetoothService(msg);
				Log.d(LOGGER.TAG_FINISH_SESSION, "MSG_SLEEP_SESSION_STOP BaseBluetoothActivty");
			}
		}
	}

	private void handleHeartBeat(final byte[] array) {
		if (this.bAct != null) {
			Log.d("com.resmed.refresh.sleepFragment", "IN HeartBeat ignored beacuse : isWaitLastSamples=" + this.isWaitLastSamples + "  ||  isHandlingHeartBeat=" + this.isHandlingHeartBeat);
			this.lastHeartBeatTimestamp = System.currentTimeMillis();
			final SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this.bAct.getApplicationContext()).edit();
			edit.putLong("PREF_NIGHT_LAST_TIMESTAMP_ID", (long) this.lastHeartBeatTimestamp);
			edit.commit();
			if (this.isWaitLastSamples || this.isHandlingHeartBeat) {
				AppFileLog.addTrace("IN HeartBeat ignored beacuse : isWaitLastSamples=" + this.isWaitLastSamples + "  ||  isHandlingHeartBeat=" + this.isHandlingHeartBeat);
				return;
			}
			this.isHandlingHeartBeat = true;
			final int storeLocalBio = PacketsByteValuesReader.getStoreLocalBio(array);
			final int storeLocalEnv = PacketsByteValuesReader.getStoreLocalEnv(array);
			V.Log("storeLocalBio:" + storeLocalBio + ";storeLocalEnv:" + storeLocalEnv);
			this.setBioOnBeD(storeLocalBio);
			this.checkReceivingHeartBeat(this.bioCurrentTotalCount += this.bioOnBeD, storeLocalBio);
			AppFileLog.addTrace("");
			AppFileLog.addTrace("IN : HeartBeat Pending BIO : " + storeLocalBio + " ENV : " + storeLocalEnv + "\tbioTotalCount=" + this.bioCurrentTotalCount);
			this.requestSamples(storeLocalBio, storeLocalEnv);
		}
	}

	private void checkReceivingHeartBeat(final int totalBioCountAtHeartBeat, int bioCountForHeartbeat) {
		int delayTime;
		if (bioCountForHeartbeat < ACRAConstants.DEFAULT_SOCKET_TIMEOUT) {
			delayTime = 15000;
		} else if (bioCountForHeartbeat < 50000) {
			delayTime = 100000;
		} else if (bioCountForHeartbeat < 500000) {
			delayTime = 400000;
		} else {
			delayTime = 900000;
		}
		Log.d(LOGGER.TAG_SLEEP_FRAGMENT, "Heartbeat time to be completed : " + delayTime);
		AppFileLog.addTrace("Heartbeat time to be completed : " + delayTime);
		this.heartbeatTimeoutRunnable = () -> {
			AppFileLog.addTrace("Checking HeartBeat lastBioCount=" + totalBioCountAtHeartBeat + " bioTotalCount=" + SleepSessionConnector.this.bioCurrentTotalCount);
			if (SleepSessionConnector.this.bAct != null && !SleepSessionConnector.this.isWaitLastSamples && SleepSessionConnector.this.bioCurrentTotalCount == totalBioCountAtHeartBeat) {
				AppFileLog.addTrace("Timeout waiting for HeartBeat => Binding the service");
				SleepSessionConnector.this.bAct.bindToService();
				SleepSessionConnector.this.pendingBioSamplesRpc = null;
				SleepSessionConnector.this.pendingEnvSamplesRpc = null;
				SleepSessionConnector.this.isHandlingHeartBeat = false;
			}
		};
		this.myHeartbeatHandler.postDelayed(this.heartbeatTimeoutRunnable, (long) delayTime);
	}


	private void requestSamples(int countBio, int countEnv) {
		Log.d(LOGGER.TAG_SLEEP_FRAGMENT, " SleepSessionConnector::requestSamples(" + countBio + ", " + countEnv + ") pendingBioSamplesRpc : " + this.pendingBioSamplesRpc + " pendingEnvSamplesRpc : " + this.pendingEnvSamplesRpc);
		if (this.bAct != null) {
			int nrBioRequest = 65535;
			int nrEnvRequest = 65535;
			if (countBio > 50000) {
				Message msg = new Message();
				msg.what = 24;
				this.bAct.sendMsgBluetoothService(msg);
			} else if (countBio < 1000) {
				nrBioRequest = countBio;
				nrEnvRequest = countEnv;
			}
			if (this.pendingBioSamplesRpc == null && this.pendingEnvSamplesRpc == null) {
				this.pendingBioSamplesRpc = BaseBluetoothActivity.getRpcCommands().transmitPacket(nrBioRequest, false, false);
				this.pendingEnvSamplesRpc = BaseBluetoothActivity.getRpcCommands().transmitPacket(nrEnvRequest, true, false);
				AppFileLog.addTrace("OUT : Requesting BIO : " + countBio + " ENV : " + countEnv);
				this.bAct.sendRpcToBed(this.pendingBioSamplesRpc);
			}
		}
	}

	private void stopStreamAndRequestAllSamples() {
		this.isWaitLastSamples = true;
		if (this.bAct == null) {
			return;
		}
		final JsonRPC stopNightTimeTracking = BaseBluetoothActivity.getRpcCommands().stopNightTimeTracking();
		stopNightTimeTracking.setRPCallback(new JsonRPC.RPCallback() {
			public void execute() {
				SleepSessionConnector.this.requestSamples(65535, 65535);
			}
			public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {
			}
			public void preExecute() {
			}
		});
		this.bAct.sendRpcToBed(stopNightTimeTracking);
	}

	private void syncDataAndStop() {
		Log.d("com.resmed.refresh.sleepFragment", "SleepTrackFragment::syncDataAndStop()");
		this.isWaitLastSamples = true;
		this.isHandlingHeartBeat = true;
		stopSleepSession();
	}

	public void handleAudioAmplitude(final int audioAmplitude) {
		if (this.bAct != null) {
			this.bAct.runOnUiThread(new Runnable() {
				public void run() {
					float f2 = (float) (20.0D * Math.log10(audioAmplitude));
					float f1 = f2;
					if (f2 == Float.NEGATIVE_INFINITY) {
						f1 = 0.0F;
					}
					Log.d("com.resmed.refresh.sound", "SleepSessionConnector::handleAudioAmplitude() amplitude : " + audioAmplitude + " db : " + f1);
					boolean bool = SleepSessionConnector.this.soundTopAmplitudes.insert(Integer.valueOf(audioAmplitude));
					Log.d("com.resmed.refresh.sound", " SleepSessionConnector::handleAudioAmplitude() sound wasInserted : " + bool);
				}
			});
		}
	}

	public void handleBreathingRate(Bundle paramBundle) {
		Log.i("RM20StartMethod", "handleBreathingRate() SleepTrackFragment");
	}

	public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE) {
		if (this.isSyncAndStop) {
		}
		final Handler localHandler = new Handler();
		Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::handleConnectionStatus()  connState : " + paramCONNECTION_STATE);
		Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::handleConnectionStatus()  isHandlingHeartBeat : " + this.isHandlingHeartBeat);
		switch (paramCONNECTION_STATE) {
			case SOCKET_BROKEN:
			case SESSION_OPENING:
			case SOCKET_NOT_CONNECTED:
			default:
				break;
			case REAL_STREAM_ON:
				this.isHandlingHeartBeat = false;
				break;
			case SOCKET_RECONNECTING:
				this.hasSessionStarted = true;
				Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::handleConnectionStatus()  hasSessionStarted : " + this.hasSessionStarted);
				this.isHandlingHeartBeat = false;
				if (this.closeSleepSession) {
					stopSleepSession();
					this.closeSleepSession = false;
				}
				break;
			case SOCKET_CONNECTED:
				Log.d("com.resmed.refresh.sleepFragment", " SleepTrackFragment SESSION_OPENED, isWaitLastSamples : " + this.isWaitLastSamples + " isClosingSession:" + this.isClosingSession + " isSyncAndStop:" + this.isSyncAndStop);
				if ((!this.isWaitLastSamples) && (!this.isClosingSession)) {
					localHandler.postDelayed(() -> {
						//SleepSessionConnector.this.registerForSessionTimeout(localHandler);
						JsonRPC localJsonRPC = BaseBluetoothActivity.getRpcCommands().startNightTracking();
						SleepSessionConnector.this.bAct.sendRpcToBed(localJsonRPC);
					}, 1000L);
				}
				this.isHandlingHeartBeat = false;
				break;
			case SESSION_OPENED:
				this.bAct.pairAndConnect(RefreshBluetoothService.main.bluetoothManager.device);
		}
	}

	public void handleEnvSample(final Bundle bundle) {
		if (bundle != null) {
			final float temp = bundle.getFloat("tempArray");
			final int light_compressed = bundle.getInt("lightArray");
			final float light = (float) this.decompressLight(light_compressed);
			++this.envTotalCount;
			AppFileLog.addTrace("IN handleEnvSample light : " + light + "  temp : " + temp);
			Log.d("com.resmed.refresh.env", "handleEnvSample light : " + light + "  temp : " + temp);
		}
	}

	@Override
	public void handleReceivedRpc(JsonRPC receivedRPC) {
		Log.d(LOGGER.TAG_UI, " SleepSessionConnector::handleReceivedRpc() receivedRPC:" + receivedRPC);
		if (receivedRPC != null) {
			ResultRPC result = receivedRPC.getResult();
			Log.d(LOGGER.TAG_UI, " SleepSessionConnector::handleReceivedRpc() result:" + result);
			if (result != null) {
				//this.countTimeout++;
				String payload = result.getPayload();
				Log.d(LOGGER.TAG_UI, " SleepSessionConnector::handleReceivedRpc() payload:" + payload);
				if (payload.contains("TRUE")) {
					AppFileLog.addTrace("IN : " + receivedRPC.getId() + " PAYLOAD : TERM(TRUE)");
				} else {
					AppFileLog.addTrace("IN : " + receivedRPC.getId() + " PAYLOAD ACK");
				}
				if (payload != null && payload.contains("TRUE")) {
					handleSamplesTransmissionCompleteForRPC(receivedRPC);
					return;
				}
				return;
			}
			ErrorRpc errorRpc = receivedRPC.getError();
			if (errorRpc == null) {
				return;
			}
			if (errorRpc.getCode().intValue() == -12 || errorRpc.getCode().intValue() == -11) {
				handleSamplesTransmissionCompleteForRPC(receivedRPC);
			}
		}
	}


	public void handleSleepSessionStopped(final Bundle bundle) {
		Log.d("com.resmed.refresh.finish", "handleSleepSessionStopped() ");
		AppFileLog.addTrace("STOP handleSleepSessionStopped ");
		//this.bAct.getWindow().clearFlags(128);
		int int1 = 0;
		boolean b = false;
		if (bundle != null) {
			final String string = bundle.getString("sParamsJson");
			final long long1 = bundle.getLong(SleepSessionManager.ParamSessionId, 0L);
			final long long2 = bundle.getLong(SleepSessionManager.ParamsecondsElapsed, 0L);
			final int int2 = bundle.getInt(SleepSessionManager.ParamNumberOfBioSamples, 0);
			int1 = bundle.getInt(SleepSessionManager.ParamAlarmFireEpoch, 0);
			Log.d("com.resmed.refresh.finish", "handleSleepSessionStopped() sParamsJson : " + string + " sessionId : " + long1 + " secondsElapsed : " + long2);
			AppFileLog.addTrace("STOP handleSleepSessionStopped() sessionId : " + long1 + " secondsElapsed : " + long2 + " sParamsJson : " + string);
			AppFileLog.addTrace("Session AlarmFireEpoch: " + int1);
			final SleepParams sleepParams = (SleepParams) new Gson().fromJson(string, (Class) SleepParams.class);
			Log.d("com.resmed.refresh.sleepFragment", " SleepTrackFragment::handleSleepSessionStopped(Bundle data) bioTotalCount : " + this.bioCurrentTotalCount);
			if (int2 < Consts.MIN_SAMPLES_TO_SAVE_RECORD) {
				b = true;
			} else {
				b = false;
			}

			if (sleepParams != null) {
				Log.d("com.resmed.refresh.model", "SleepSession processed-----Env Data");
			}
		}
		if (!b) {
			Log.d("com.resmed.refresh.finish", "progress setted to 99 (sleep data done being retrieved and/or saved to db)");
		}
		this.pendingBioSamplesRpc = null;
		this.pendingEnvSamplesRpc = null;
	}

	public void handleStreamPacket(Bundle paramBundle) {
		V.Log("HandleStreamPacket:" + Arrays.toString(paramBundle.getByteArray("REFRESH_BED_NEW_DATA")));
		byte[] arrayOfByte = paramBundle.getByteArray("REFRESH_BED_NEW_DATA");
		int i = paramBundle.getByte("REFRESH_BED_NEW_DATA_TYPE");
		if (VLPacketType.PACKET_TYPE_NOTE_ILLUMINANCE_CHANGE.ordinal() == i) {
			handleEnvData(arrayOfByte, false);
		}
		if (VLPacketType.PACKET_TYPE_NOTE_HEARTBEAT.ordinal() == i) {
			handleHeartBeat(arrayOfByte);
		} else if (VLPacketType.PACKET_TYPE_ENV_1.ordinal() == i) {
			handleEnvData(arrayOfByte, true);
		} else if (VLPacketType.PACKET_TYPE_ENV_60.ordinal() == i) {
			handleEnvData(arrayOfByte, true);
		}
	}

	public void handleUserSleepState(Bundle paramBundle) {
		Log.i("RM20StartMethod", "handleUserSleepState() SleepTrackFragment");
	}

	public void init(boolean didUserPressedBackToSleep) {
		//this.lastState = RefreshApplication.getInstance().getCurrentConnectionState();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.bAct.getApplicationContext());
		int nightState = prefs.getInt(Consts.PREF_NIGHT_TRACK_STATE, -1);
		AppFileLog.addTrace("SleepSessionConnector recovering state " + nightState + " for session id : " + prefs.getLong(Consts.PREF_NIGHT_LAST_SESSION_ID, -1));
		//this.bAct.registerReceiver(this.reconnectReceiver, new IntentFilter(RefreshBluetoothServiceClient.BLUETOOTH_SERVICE_INTENT_RESTART));
		if (this.isSyncAndStop) {
			//recoverSleepSession(didUserPressedBackToSleep);
			syncDataAndStop();
		} /*else if (nightState == CONNECTION_STATE.NIGHT_TRACK_ON.ordinal()) {
			recoverSleepSession(true);
			RegisterRepeatingAlarmWake(this.bAct);
		}*/ else {
			startSleepSession();
		}
		this.sizeToStore = this.isSyncAndStop ? 640 : 2;
	}

	public boolean isRecovering() {
		return this.isRecovering;
	}

	public void onClickStop() {
		AppFileLog.addTrace("STOP SleepSessionConnector onClickStop");
		if (this.bAct.checkBluetoothEnabled(true)) {
			RST_SleepSession.getInstance().stopSession(true);
			if (this.bAct == null || this.bAct.isBoundToBluetoothService()) {
				this.closeSession();
				return;
			}
			Log.d("com.resmed.refresh.bluetooth", " SleepTrackFragment::onViewCreate is isBoundToBluetoothService :" + this.bAct.isBoundToBluetoothService());
			this.bAct.bindToService();
			new Handler().postDelayed(() -> SleepSessionConnector.this.closeSession(), 200L);
		}
	}

	public void resume() {
		this.pendingBioSamplesRpc = null;
		this.pendingEnvSamplesRpc = null;
		// back on track
	}

	public void setBioOnBeD(final int bioOnBeD) {
		this.bioOnBeD = bioOnBeD;
		int n = 15000;
		if (bioOnBeD < 50000) {
			n = 2000;
		} else if (bioOnBeD > 50000 && bioOnBeD < 200000) {
			n = 5000;
		} else if (bioOnBeD > 300000) {
			n = 10000;
		} else if (bioOnBeD > 500000) {
			n = 20000;
		}
		/*if (this.processRecord != null) {
			this.processRecord.setTickTime((long)n);
		}*/
	}

	public void setSyncing(boolean paramBoolean) {
		this.isSyncAndStop = paramBoolean;
	}

	protected void setupController() {
		Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::setupController()");
		this.isWaitLastSamples = false;
		this.soundTopAmplitudes = new SortedList(5);
		RST_SleepSession.getInstance().startSession(true);
	}

	protected void startSleepSession() {
		final SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this.bAct.getApplicationContext()).edit();
		edit.putInt("PREF_CONNECTION_STATE", CONNECTION_STATE.NIGHT_TRACK_ON.ordinal());
		edit.commit();
		AppFileLog.deleteCurrentFile();
		final Message message = new Message();
		message.what = RefreshBluetoothService.MessageType.SLEEP_SESSION_START;
		message.getData().putLong("sessionId", 1); // todo: make not hard-coded
		message.getData().putInt("age", 20); // todo: make not hard-coded
		message.getData().putInt("gender", 0); // 0 = male, 1 = female // todo: make not hard-coded
		this.bAct.sendMsgBluetoothService(message);
		this.setupController();
	}

	public void stopSleepSession() {
		AppFileLog.addTrace("STOP SLEEP SESSION");
		//preSleepLog.addTrace("STOP SLEEP SESSION");
		Log.d(LOGGER.TAG_FINISH_SESSION, "SleepTrackFragment stopSleepSession isClosingSession? " + this.isClosingSession);
		//this.bAct.getWindow().addFlags(128);
		if (!this.isClosingSession) {
			this.isClosingSession = true;
			Log.d(LOGGER.TAG_FINISH_SESSION, "SleepTrackFragment stopSleepSession()");
			if (!this.isHandlingHeartBeat || this.isSyncAndStop) {
				this.pendingBioSamplesRpc = null;
				this.pendingEnvSamplesRpc = null;
				Log.d(LOGGER.TAG_FINISH_SESSION, "stopStreamAndRequestAllSamples Conditions" + this.isHandlingHeartBeat + ":" + this.isSyncAndStop);
				stopStreamAndRequestAllSamples();
			}
			Editor editor = PreferenceManager.getDefaultSharedPreferences(this.bAct.getApplicationContext()).edit();
			editor.putInt(Consts.PREF_NIGHT_TRACK_STATE, CONNECTION_STATE.NIGHT_TRACK_OFF.ordinal());
			editor.commit();
			/*this.countTimeout = 0;
			this.bAct.showBlockingDialog(new CustomDialogBuilder(this.bAct).title((int) R.string.sleep_time_session_end_title).description((int) R.string.sleep_time_session_end_desc).useProgressBar());
			this.processRecord.start();
			CancelRepeatingAlarmWake(this.bAct);*/
		}
	}
}
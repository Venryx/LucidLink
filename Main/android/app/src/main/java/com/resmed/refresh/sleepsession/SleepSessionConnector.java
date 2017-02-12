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
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.RST_EnvironmentalInfo;
import com.resmed.refresh.model.RST_SleepEvent;
import com.resmed.refresh.model.RST_SleepEvent.SleepEventType;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RST_ValueItem;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.model.json.JsonRPC.ErrorRpc;
import com.resmed.refresh.model.json.JsonRPC.RPCallback;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.KillableRunnable;
import com.resmed.refresh.utils.KillableRunnable.KillableRunner;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.refresh.utils.SortedList;
import com.resmed.rm20.SleepParams;

import de.greenrobot.dao.DaoException;

import java.io.PrintStream;
import java.util.ArrayList;
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
	private CONNECTION_STATE lastState;
	private List<RST_ValueItem> lightValues;
	private Handler myHeartbeatHandler;
	private JsonRPC pendingBioSamplesRpc;
	private JsonRPC pendingEnvSamplesRpc;
	private int sizeToStore = 5;
	private RST_SleepSessionInfo sleepSessionInfo;
	private SleepSessionListener sleepSessionListener;
	private SortedList<Integer> soundTopAmplitudes;
	private List<RST_ValueItem> tempValues;

	public SleepSessionConnector(BaseBluetoothActivity paramBaseBluetoothActivity, int paramInt, boolean paramBoolean, SleepSessionListener paramSleepSessionListener) {
		this.isSyncAndStop = paramBoolean;
		this.bAct = paramBaseBluetoothActivity;
		this.bioOnBeD = paramInt;
		this.isClosingSession = false;
		this.hasSessionStarted = false;
		this.sleepSessionListener = paramSleepSessionListener;
		this.sleepSessionInfo = new RST_SleepSessionInfo();
		this.lightValues = new ArrayList();
		this.tempValues = new ArrayList();
		this.myHeartbeatHandler = new Handler();
	}

	private void checkReceivingHeartBeat(final int n, final int n2) {
		int n3;
		if (n2 < 5000) {
			n3 = 15000;
		}
		else if (n2 < 50000) {
			n3 = 100000;
		}
		else if (n2 < 500000) {
			n3 = 400000;
		}
		else {
			n3 = 900000;
		}
		Log.d("com.resmed.refresh.sleepFragment", "Heartbeat time to be completed : " + n3);
		AppFileLog.addTrace("Heartbeat time to be completed : " + n3);
		this.heartbeatTimeoutRunnable = () -> {
			AppFileLog.addTrace("Checking HeartBeat lastBioCount=" + n + " bioTotalCount=" + SleepSessionConnector.this.bioCurrentTotalCount);
			if ((SleepSessionConnector.this.bAct != null) && (!SleepSessionConnector.this.isWaitLastSamples) && (SleepSessionConnector.this.bioCurrentTotalCount == n)) {
				AppFileLog.addTrace("Timeout waiting for HeartBeat => Binding the service");
				SleepSessionConnector.this.bAct.bindToService();
				SleepSessionConnector.this.pendingBioSamplesRpc = null;
				SleepSessionConnector.this.pendingEnvSamplesRpc = null;
				SleepSessionConnector.this.isHandlingHeartBeat = false;
			}
		};
		this.myHeartbeatHandler.postDelayed(this.heartbeatTimeoutRunnable, (long)n3);
	}

	private void closeSession() {
		AppFileLog.addTrace("CLOSING SLEEP SESSION, isHandlingHeartBeat :" + this.isHandlingHeartBeat);
		if ((this.lastState == CONNECTION_STATE.SOCKET_NOT_CONNECTED) || (this.lastState == CONNECTION_STATE.BLUETOOTH_OFF)
				|| (this.lastState == CONNECTION_STATE.SOCKET_BROKEN) || (this.lastState == CONNECTION_STATE.SOCKET_RECONNECTING))
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

	private void handleHeartBeat(final byte[] array) {
		if (this.bAct != null) {
			Log.d("com.resmed.refresh.sleepFragment", "IN HeartBeat ignored beacuse : isWaitLastSamples=" + this.isWaitLastSamples + "  ||  isHandlingHeartBeat=" + this.isHandlingHeartBeat);
			this.lastHeartBeatTimestamp = System.currentTimeMillis();
			final SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this.bAct.getApplicationContext()).edit();
			edit.putLong("PREF_NIGHT_LAST_TIMESTAMP_ID", (long)this.lastHeartBeatTimestamp);
			edit.commit();
			if (this.isWaitLastSamples || this.isHandlingHeartBeat) {
				AppFileLog.addTrace("IN HeartBeat ignored beacuse : isWaitLastSamples=" + this.isWaitLastSamples + "  ||  isHandlingHeartBeat=" + this.isHandlingHeartBeat);
				return;
			}
			this.isHandlingHeartBeat = true;
			final int storeLocalBio = PacketsByteValuesReader.getStoreLocalBio(array);
			final int storeLocalEnv = PacketsByteValuesReader.getStoreLocalEnv(array);
			this.setBioOnBeD(storeLocalBio);
			this.checkReceivingHeartBeat(this.bioCurrentTotalCount += this.bioOnBeD, storeLocalBio);
			AppFileLog.addTrace("");
			AppFileLog.addTrace("IN : HeartBeat Pending BIO : " + storeLocalBio + " ENV : " + storeLocalEnv + "\tbioTotalCount=" + this.bioCurrentTotalCount);
			this.requestSamples(storeLocalBio, storeLocalEnv);
		}
	}

	private void handleSamplesTransmissionCompleteForRPC(final JsonRPC jsonRPC) {
		final StringBuilder sb = new StringBuilder("pendingBio is ");
		String s;
		if (this.pendingBioSamplesRpc == null) {
			s = "null";
		}
		else {
			s = "NOT null";
		}
		final String string = sb.append(s).toString();
		final StringBuilder sb2 = new StringBuilder("pendingEnv is ");
		String s2;
		if (this.pendingEnvSamplesRpc == null) {
			s2 = "null";
		}
		else {
			s2 = "NOT null";
		}
		AppFileLog.addTrace("IN : " + jsonRPC.getId() + "  Tx completed " + string + "  " + sb2.append(s2).toString() + " isWaitLastSamples=" + this.isWaitLastSamples);
		if (this.pendingBioSamplesRpc != null && this.pendingBioSamplesRpc.getId() == jsonRPC.getId()) {
			if (this.pendingEnvSamplesRpc != null && this.bAct != null) {
				AppFileLog.addTrace("IN=>OUT : Requesting ENV ID : " + this.pendingEnvSamplesRpc.getId());
				this.bAct.sendRpcToBed(this.pendingEnvSamplesRpc);
			}
			this.pendingBioSamplesRpc = null;
		}
		if (this.pendingEnvSamplesRpc != null && this.pendingEnvSamplesRpc.getId() == jsonRPC.getId()) {
			this.pendingEnvSamplesRpc = null;
			this.isHandlingHeartBeat = false;
			AppFileLog.addTrace("");
			this.myHeartbeatHandler.removeCallbacks(this.heartbeatTimeoutRunnable);
			if (this.isRecovering) {
				this.isRecovering = false;
			}
			this.lastHeartBeatTimestamp = System.currentTimeMillis();
			this.sleepSessionListener.onSessionOk();
			if (this.isClosingSession) {
				this.isWaitLastSamples = true;
				this.bAct.updateDataStoredFlag(0);
			}
		}
		if (this.isWaitLastSamples && this.pendingBioSamplesRpc == null && this.pendingEnvSamplesRpc == null && this.isClosingSession) {
			final JsonRPC stopNightTimeTracking = BaseBluetoothActivity.getRpcCommands().stopNightTimeTracking();
			stopNightTimeTracking.setRPCallback(new JsonRPC.RPCallback() {
				public void execute() {
					if ((SleepSessionConnector.this.bAct != null) && (!SleepSessionConnector.this.bAct.isFinishing())) {
						JsonRPC localJsonRPC = BaseBluetoothActivity.getRpcCommands().clearBuffers();
						SleepSessionConnector.this.bAct.sendRpcToBed(localJsonRPC);
					}
				}

				public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {
				}

				public void preExecute() {
				}
			});
			AppFileLog.addTrace(" SleepSessionConnector last samples! -> stop & clear");
			this.bAct.sendRpcToBed(stopNightTimeTracking);
			final Message message = new Message();
			message.what = 14;
			if (this.bAct != null) {
				this.bAct.sendMsgBluetoothService(message);
				Log.d("com.resmed.refresh.finish", "MSG_SLEEP_SESSION_STOP BaseBluetoothActivty");
			}
		}
	}

	private void requestSamples(final int n, final int n2) {
		Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::requestSamples(" + n + ", " + n2 + ") pendingBioSamplesRpc : " + this.pendingBioSamplesRpc + " pendingEnvSamplesRpc : " + this.pendingEnvSamplesRpc);
		if (this.bAct != null) {
			int n3 = 65535;
			int n4 = 65535;
			if (n > 50000) {
				final Message message = new Message();
				message.what = 24;
				this.bAct.sendMsgBluetoothService(message);
			}
			else if (n < 1000) {
				n3 = n;
				n4 = n2;
			}
			if (this.pendingBioSamplesRpc == null && this.pendingEnvSamplesRpc == null) {
				this.pendingBioSamplesRpc = BaseBluetoothActivity.getRpcCommands().transmitPacket(n3, false, false);
				this.pendingEnvSamplesRpc = BaseBluetoothActivity.getRpcCommands().transmitPacket(n4, true, false);
				AppFileLog.addTrace("OUT : Requesting BIO : " + n + " ENV : " + n2);
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

	public void addEnvDataToDB() {
		if (this.sleepSessionInfo != null) {
		}
		try {
			Object localObject = new java.lang.StringBuilder("addEnvDataToDB ");
			Log.d("com.resmed.refresh.env", this.lightValues.size() + " light values and " + this.tempValues.size() + " temp values" + " START " + this.sleepSessionInfo.getStartTime());
			this.sleepSessionInfo.getEnvironmentalInfo().addSessionLightArray(this.lightValues);
			this.sleepSessionInfo.getEnvironmentalInfo().addSessionTemperatureArray(this.tempValues);
			this.sleepSessionInfo.update();
			this.lightValues = new java.util.ArrayList();
			this.tempValues = new java.util.ArrayList();
			return;
		} catch (DaoException localDaoException) {
			for (; ; ) {
				Log.d("com.resmed.refresh.env", localDaoException.getMessage());
				localDaoException.printStackTrace();
			}
		}
	}

	public void handleAudioAmplitude(final int paramInt) {
		if (this.bAct != null) {
			this.bAct.runOnUiThread(new Runnable() {
				public void run() {
					float f2 = (float) (20.0D * Math.log10(paramInt));
					float f1 = f2;
					if (f2 == Float.NEGATIVE_INFINITY) {
						f1 = 0.0F;
					}
					Log.d("com.resmed.refresh.sound", "SleepSessionConnector::handleAudioAmplitude() amplitude : " + paramInt + " db : " + f1);
					boolean bool = SleepSessionConnector.this.soundTopAmplitudes.insert(Integer.valueOf(paramInt));
					Log.d("com.resmed.refresh.sound", " SleepSessionConnector::handleAudioAmplitude() sound wasInserted : " + bool);
					if (bool) {
						Object localObject = new RST_SleepEvent();
						((RST_SleepEvent) localObject).setType(RST_SleepEvent.SleepEventType.kSleepEventTypeSound.ordinal());
						long l = SleepSessionConnector.this.sleepSessionInfo.getStartTime().getTime();
						l = System.currentTimeMillis() - l;
						int i = (int) (l / 1000L / 30L);
						AppFileLog.addTrace(" SleepSessionConnector::handleAudioAmplitude saving sound epochNumber : " + i + " at :" + l);
						((RST_SleepEvent) localObject).setEpochNumber(i);
						((RST_SleepEvent) localObject).setIdSleepSession(Long.valueOf(SleepSessionConnector.this.sleepSessionInfo.getId()));
						((RST_SleepEvent) localObject).setValue(paramInt);
						SleepSessionConnector.this.sleepSessionInfo.addEvent((RST_SleepEvent) localObject);
						localObject = (Integer) SleepSessionConnector.this.soundTopAmplitudes.getLastReplacedElement();
						if (localObject != null) {
							bool = SleepSessionConnector.this.sleepSessionInfo.deleteSoundEvent(((Integer) localObject).intValue());
							Log.d("com.resmed.refresh.sound", " SleepSessionConnector::handleAudioAmplitude() sound wasDeleted : " + bool);
						}
						SleepSessionConnector.this.sleepSessionInfo.update();
					}
				}
			});
		}
	}

	public void handleBreathingRate(Bundle paramBundle) {
		Log.i("RM20StartMethod", "handleBreathingRate() SleepTrackFragment");
	}

	public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE) {
		this.lastState = paramCONNECTION_STATE;
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
				this.bAct.connectToBeD(false);
		}
	}

	public void handleEnvSample(final Bundle bundle) {
		if (bundle != null) {
			final float float1 = bundle.getFloat("tempArray");
			final int int1 = bundle.getInt("lightArray");
			final RST_ValueItem rst_ValueItem = new RST_ValueItem();
			rst_ValueItem.setValue((float)this.decompressLight(int1));
			rst_ValueItem.setOrdr(this.envTotalCount);
			final RST_ValueItem rst_ValueItem2 = new RST_ValueItem();
			rst_ValueItem2.setValue(float1);
			rst_ValueItem2.setOrdr(this.envTotalCount);
			++this.envTotalCount;
			this.lightValues.add(rst_ValueItem);
			this.tempValues.add(rst_ValueItem2);
			AppFileLog.addTrace("IN handleEnvSample light : " + rst_ValueItem.getValue() + "  temp : " + rst_ValueItem2.getValue());
			Log.d("com.resmed.refresh.env", "handleEnvSample light : " + rst_ValueItem.getValue() + "  temp : " + rst_ValueItem2.getValue());
			if (this.lightValues.size() >= this.sizeToStore) {
				this.addEnvDataToDB();
			}
		}
	}

	@Override
	public void handleReceivedRpc(final JsonRPC jsonRPC) {
		Log.d("com.resmed.refresh.ui", " SleepSessionConnector::handleReceivedRpc() receivedRPC:" + jsonRPC);
	}

	public void handleSessionRecovered(Bundle paramBundle) {
		this.sleepSessionListener.onSessionOk();
	}

	public void handleSleepSessionStopped(final Bundle bundle) {
		Log.d("com.resmed.refresh.finish", "handleSleepSessionStopped() ");
		AppFileLog.addTrace("STOP handleSleepSessionStopped ");
		this.bAct.getWindow().clearFlags(128);
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
			final SleepParams sleepParams = (SleepParams)new Gson().fromJson(string, (Class)SleepParams.class);
			Log.d("com.resmed.refresh.sleepFragment", " SleepTrackFragment::handleSleepSessionStopped(Bundle data) bioTotalCount : " + this.bioCurrentTotalCount);
			if (int2 < Consts.MIN_SAMPLES_TO_SAVE_RECORD) {
				b = true;
			}
			else {
				b = false;
			}
			final long n = (System.currentTimeMillis() - this.sleepSessionInfo.getStartTime().getTime()) / 1000L;
			if (n < Consts.MIN_SECS_TO_SAVE_RECORD) {
				AppFileLog.addTrace("ERROR UPLOADING SLEEP SESSION: session time:" + n);
				b = true;
			}
			else if (sleepParams != null) {
				this.sleepSessionInfo.processRM20Data(sleepParams);
				this.sleepSessionInfo.setCompleted(true);
				this.sleepSessionInfo.update();
				Log.d("com.resmed.refresh.model", "SleepSession processed-----Env Data");
				Log.d("com.resmed.refresh.model", "sleepSessionInfo completed? = " + this.sleepSessionInfo.getCompleted());
				int i = 0;
				while (true) {
					try {
						while (i < this.sleepSessionInfo.getEnvironmentalInfo().getSessionLight().size()) {
							Log.d("com.resmed.refresh.model", "Light[" + i + "]=" + ((RST_ValueItem)this.sleepSessionInfo.getEnvironmentalInfo().getSessionLight().get(i)).getValue());
							++i;
						}
						for (int j = 0; j < this.sleepSessionInfo.getEnvironmentalInfo().getSessionTemperature().size(); ++j) {
							Log.d("com.resmed.refresh.model", "Temp[" + j + "]=" + ((RST_ValueItem)this.sleepSessionInfo.getEnvironmentalInfo().getSessionTemperature().get(j)).getValue());
						}
						Log.d("com.resmed.refresh.model", "SleepSession processed-----Env Data");
					}
					catch (Exception ex) {
						continue;
					}
					break;
				}
			}
		}
		if (this.sleepSessionInfo != null) {
			this.sleepSessionInfo.setStopTime(Calendar.getInstance().getTime());
			this.sleepSessionInfo.setAlarmFireEpoch(int1);
			this.sleepSessionInfo.setCompleted(true);
			this.sleepSessionInfo.update();
		}
		if (!b) {
			SleepSessionConnector.this.addEnvDataToDB();
			if ((SleepSessionConnector.this.bAct != null) && (!SleepSessionConnector.this.bAct.isFinishing())) {
				SleepSessionConnector.this.bAct.updateDataStoredFlag(0);
			}
			Log.d("com.resmed.refresh.finish", "progress setted to 99");
		}
		this.pendingBioSamplesRpc = null;
		this.pendingEnvSamplesRpc = null;
	}

	public void handleStreamPacket(Bundle paramBundle) {
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

	public void init(final boolean b) {
		this.lastState = RefreshApplication.getInstance().getCurrentConnectionState();
		final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.bAct.getApplicationContext());
		final int int1 = defaultSharedPreferences.getInt("PREF_CONNECTION_STATE", -1);
		AppFileLog.addTrace("SleepSessionConnector recovering state " + int1 + " for session id : " + defaultSharedPreferences.getLong("PREF_NIGHT_LAST_SESSION_ID", -1L));
		//this.bAct.registerReceiver(this.reconnectReceiver, new IntentFilter("BLUETOOTH_SERVICE_INTENT_RESTART"));
		if (this.isSyncAndStop) {
			this.syncDataAndStop();
		}
		else if (int1 == CONNECTION_STATE.NIGHT_TRACK_ON.ordinal()) {
		}
		else {
			this.startSleepSession();
		}
		int sizeToStore;
		if (this.isSyncAndStop) {
			sizeToStore = 640;
		}
		else {
			sizeToStore = 2;
		}
		this.sizeToStore = sizeToStore;
	}

	public boolean isRecovering() {
		return this.isRecovering;
	}

	public void onClickStop() {
		AppFileLog.addTrace("STOP SleepSessionConnector onClickStop");
		if (this.bAct.checkBluetoothEnabled(true)) {
			this.sleepSessionListener.stopRelax(false);
			this.sleepSessionInfo.setStopTime(Calendar.getInstance().getTime());
			this.sleepSessionInfo.setCompleted(false);
			this.sleepSessionInfo.update();
			RST_SleepSession.getInstance().stopSession(true);
			if (this.bAct == null || this.bAct.isBoundToBluetoothService()) {
				this.closeSession();
				return;
			}
			Log.d("com.resmed.refresh.bluetooth", " SleepTrackFragment::onViewCreate is isBoundToBluetoothService :" + this.bAct.isBoundToBluetoothService());
			this.bAct.bindToService();
			new Handler().postDelayed(()->SleepSessionConnector.this.closeSession(), 200L);
		}
	}

	public void resume() {
		this.pendingBioSamplesRpc = null;
		this.pendingEnvSamplesRpc = null;
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if ((SleepSessionConnector.this.bAct != null) && (!SleepSessionConnector.this.bAct.isFinishing())) {
					SleepSessionConnector.this.sleepSessionListener.onSessionOk();
				}
			}
		}, 9000L);
	}

	public void setBioOnBeD(final int bioOnBeD) {
		this.bioOnBeD = bioOnBeD;
		int n = 15000;
		if (bioOnBeD < 50000) {
			n = 2000;
		}
		else if (bioOnBeD > 50000 && bioOnBeD < 200000) {
			n = 5000;
		}
		else if (bioOnBeD > 300000) {
			n = 10000;
		}
		else if (bioOnBeD > 500000) {
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
		Object localObject = this.sleepSessionInfo.getEvents();
		Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::setupController() events : " + ((List) localObject).size());
		localObject = ((List) localObject).iterator();
		for (; ; ) {
			if (!((Iterator) localObject).hasNext()) {
				Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::setupController() soundTopAmplitudes : " + this.soundTopAmplitudes.size());
				this.pendingBioSamplesRpc = null;
				this.pendingEnvSamplesRpc = null;
				return;
			}
			RST_SleepEvent localRST_SleepEvent = (RST_SleepEvent) ((Iterator) localObject).next();
			if (localRST_SleepEvent.getType() == RST_SleepEvent.SleepEventType.kSleepEventTypeSound.ordinal()) {
				this.soundTopAmplitudes.insert(Integer.valueOf(localRST_SleepEvent.getValue()));
			}
		}
	}

	protected void startSleepSession() {
		final RefreshModelController instance = RefreshModelController.getInstance();
		this.sleepSessionInfo = instance.createSleepSessionInfo();
		final SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this.bAct.getApplicationContext()).edit();
		edit.putInt("PREF_CONNECTION_STATE", CONNECTION_STATE.NIGHT_TRACK_ON.ordinal());
		edit.putLong("PREF_NIGHT_LAST_SESSION_ID", this.sleepSessionInfo.getId());
		edit.commit();
		AppFileLog.deleteCurrentFile();
		AppFileLog.addTrace("START SLEEP SESSION ID : " + this.sleepSessionInfo.getId());
		final Message message = new Message();
		message.what = 12;
		message.getData().putLong("sessionId", this.sleepSessionInfo.getId());
		message.getData().putInt("age", 18); // todo: make not hard-coded
		message.getData().putInt("gender", 0); // 0 = male, 1 = female // todo: make not hard-coded
		this.bAct.sendMsgBluetoothService(message);
		this.setupController();
	}

	public void stopSleepSession() {
		try {
			AppFileLog.addTrace("STOP SLEEP SESSION");
			Object localObject1 = new StringBuilder("SleepTrackFragment stopSleepSession isClosingSession? ");
			Log.d("com.resmed.refresh.finish", "Closing:" + this.isClosingSession);
			this.bAct.getWindow().addFlags(128);
			if (!this.isClosingSession) {
				this.isClosingSession = true;
				Log.d("com.resmed.refresh.finish", "SleepTrackFragment stopSleepSession()");
				if ((!this.isHandlingHeartBeat) || (this.isSyncAndStop)) {
					this.pendingBioSamplesRpc = null;
					this.pendingEnvSamplesRpc = null;
					localObject1 = new StringBuilder("stopStreamAndRequestAllSamples Conditions");
					Log.d("com.resmed.refresh.finish", this.isHandlingHeartBeat + ":" + this.isSyncAndStop);
					stopStreamAndRequestAllSamples();
				}
				localObject1 = PreferenceManager.getDefaultSharedPreferences(this.bAct.getApplicationContext()).edit();
				((SharedPreferences.Editor) localObject1).putInt("PREF_CONNECTION_STATE", CONNECTION_STATE.NIGHT_TRACK_OFF.ordinal());
				((SharedPreferences.Editor) localObject1).commit();
				localObject1 = this.bAct;
			}
			return;
		} finally {
		}
	}

	public static abstract interface SleepSessionListener {
		public abstract void onSessionOk();

		public abstract void onSessionRecovering();

		public abstract void startRelax(boolean paramBoolean);

		public abstract void stopRelax(boolean paramBoolean);
	}
}
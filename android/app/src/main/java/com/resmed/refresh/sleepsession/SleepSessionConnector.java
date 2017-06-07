package com.resmed.refresh.sleepsession;

import android.os.Bundle;
import android.os.Message;

import com.google.gson.Gson;
import com.resmed.refresh.bed.RPCMapper;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.bluetooth.RefreshBluetoothService;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.model.json.JsonRPC.ErrorRpc;
import com.resmed.refresh.model.json.JsonRPC.RPCallback;
import com.resmed.refresh.model.json.ResultRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.LOGGER;
import com.resmed.refresh.utils.Log;
import com.resmed.rm20.SleepParams;

import SPlus.SPlusModule;
import vpackages.V;

public class SleepSessionConnector {
	private BaseBluetoothActivity bAct;
	private boolean isHandlingHeartBeat;
	private boolean isRecovering = false;
	private boolean isSyncAndStop;
	private boolean isWaitLastSamples;
	private JsonRPC pendingBioSamplesRpc;
	private JsonRPC pendingEnvSamplesRpc;

	public RefreshBluetoothService service;

	public SleepSessionConnector(BaseBluetoothActivity paramBaseBluetoothActivity, boolean isSyncing) {
		this.isSyncAndStop = isSyncing;
		this.bAct = paramBaseBluetoothActivity;
		//this.isClosingSession = false;
		service = new RefreshBluetoothService();
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

		// if we're receiving bio samples
		if (this.pendingBioSamplesRpc != null && this.pendingBioSamplesRpc.getId() == receivedRPC.getId()) {
			if (!(this.pendingEnvSamplesRpc == null || this.bAct == null)) {
				AppFileLog.addTrace("IN=>OUT : Requesting ENV ID : " + this.pendingEnvSamplesRpc.getId());
				// request env-samples next
				this.bAct.sendRpcToBed(this.pendingEnvSamplesRpc);
			}
			this.pendingBioSamplesRpc = null;

			//V.Log("BIO transmission complete...");

			// whenever a batch of raw bio-data is received, have the sleep-stage be calculated as well
			if (SPlusModule.main.sessionConnector.service.sleepSessionManager != null) // if this data was from a session we actually started this launch // temp fix
				SPlusModule.main.sessionConnector.service.sleepSessionManager.rm20Manager.getRealTimeSleepState();
		}
		// if we're receiving env-samples
		if (this.pendingEnvSamplesRpc != null && this.pendingEnvSamplesRpc.getId() == receivedRPC.getId()) {
			this.pendingEnvSamplesRpc = null;
			this.isHandlingHeartBeat = false;
			AppFileLog.addTrace("");
			if (this.isRecovering) {
				this.isRecovering = false;
			}
			//this.sleepSessionListener.onSessionOk();
			/*if (this.isClosingSession) {
				this.isWaitLastSamples = true;
				this.bAct.updateDataStoredFlag(0);
			}*/
		}

		// if this is the last of the samples
		if (this.isWaitLastSamples && this.pendingBioSamplesRpc == null && this.pendingEnvSamplesRpc == null) { // && this.isClosingSession) {
			JsonRPC stopSampleRpc = RPCMapper.main.stopNightTimeTracking();
			stopSampleRpc.setRPCallback(new RPCallback() {
				public void preExecute() {}
				public void onError(ErrorRpc errRpc) {}
				public void execute() {
					if (SleepSessionConnector.this.bAct != null && !SleepSessionConnector.this.bAct.isFinishing()) {
						SleepSessionConnector.this.bAct.sendRpcToBed(RPCMapper.main.clearBuffers());
					}
				}
			});
			AppFileLog.addTrace(" SleepSessionConnector last samples! -> stop & clear");
			this.bAct.sendRpcToBed(stopSampleRpc);
			Message msg = new Message();
			msg.what = RefreshBluetoothService.MessageType.SLEEP_SESSION_STOP;
			if (this.bAct != null) {
				this.bAct.sendMessageToService(msg);
				Log.d(LOGGER.TAG_FINISH_SESSION, "MSG_SLEEP_SESSION_STOP BaseBluetoothActivty");
			}
		}
	}

	private void handleHeartBeat(final byte[] array) {
		if (this.bAct != null) {
			Log.d("com.resmed.refresh.sleepFragment", "IN HeartBeat ignored because : isWaitLastSamples=" + this.isWaitLastSamples + "  ||  isHandlingHeartBeat=" + this.isHandlingHeartBeat);
			if (this.isWaitLastSamples || this.isHandlingHeartBeat) {
				AppFileLog.addTrace("IN HeartBeat ignored because : isWaitLastSamples=" + this.isWaitLastSamples + "  ||  isHandlingHeartBeat=" + this.isHandlingHeartBeat);
				return;
			}
			this.isHandlingHeartBeat = true;
			final int storeLocalBio = PacketsByteValuesReader.getStoreLocalBio(array);
			final int storeLocalEnv = PacketsByteValuesReader.getStoreLocalEnv(array);
			V.LogJava("storeLocalBio:" + storeLocalBio + ";storeLocalEnv:" + storeLocalEnv);

			AppFileLog.addTrace("");
			AppFileLog.addTrace("IN : HeartBeat Pending BIO : " + storeLocalBio + " ENV : " + storeLocalEnv);

			this.requestSamples(storeLocalBio, storeLocalEnv);
		}
	}

	public void requestSamples(int countBio, int countEnv) {
		//V.Log("Requesting samples:" + countBio + ";" + countEnv + ";" + V.GetStackTrace());

		Log.d(LOGGER.TAG_SLEEP_FRAGMENT, " SleepSessionConnector::requestSamples(" + countBio + ", " + countEnv + ") pendingBioSamplesRpc : " + this.pendingBioSamplesRpc + " pendingEnvSamplesRpc : " + this.pendingEnvSamplesRpc);
		if (this.bAct != null) {
			int nrBioRequest = 65535;
			int nrEnvRequest = 65535;
			if (countBio > 50000) {
				Message msg = new Message();
				msg.what = RefreshBluetoothService.MessageType.BLUETOOTH_BULK_TRANSFER_START;
				this.bAct.sendMessageToService(msg);
			} else if (countBio < 1000) {
				nrBioRequest = countBio;
				nrEnvRequest = countEnv;
			}
			if (this.pendingBioSamplesRpc == null && this.pendingEnvSamplesRpc == null) {
				this.pendingBioSamplesRpc = RPCMapper.main.transmitPacket(nrBioRequest, false, false);
				this.pendingEnvSamplesRpc = RPCMapper.main.transmitPacket(nrEnvRequest, true, false);
				AppFileLog.addTrace("OUT : Requesting BIO : " + countBio + " ENV : " + countEnv);
				this.bAct.sendRpcToBed(this.pendingBioSamplesRpc);
			}
		}
	}

	public void handleBreathingRate(Bundle breathingRate) {
		Log.i("RM20StartMethod", "handleBreathingRate() SleepTrackFragment");
	}

	public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE) {
		if (this.isSyncAndStop) {
		}
		//final Handler localHandler = new Handler();
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
			/*case SOCKET_RECONNECTING:
				this.hasSessionStarted = true;
				Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::handleConnectionStatus()  hasSessionStarted : " + this.hasSessionStarted);
				this.isHandlingHeartBeat = false;
				if (this.closeSleepSession) {
					stopSleepSession();
					this.closeSleepSession = false;
				}
				break;*/
			case SOCKET_CONNECTED:
				Log.d("com.resmed.refresh.sleepFragment", " SleepTrackFragment SESSION_OPENED, isWaitLastSamples : " + this.isWaitLastSamples
						//+ " isClosingSession:" + this.isClosingSession
						+ " isSyncAndStop:" + this.isSyncAndStop);
				/*if ((!this.isWaitLastSamples) && (!this.isClosingSession)) {
					localHandler.postDelayed(() -> {
						//SleepSessionConnector.this.registerForSessionTimeout(localHandler);
						JsonRPC localJsonRPC = RPCMapper.main.startNightTracking();
						SleepSessionConnector.this.bAct.sendRpcToBed(localJsonRPC);
					}, 1000L);
				}
				this.isHandlingHeartBeat = false;*/
				break;
			case SESSION_OPENED:
				this.bAct.pairAndConnect(SPlusModule.main.sessionConnector.service.bluetoothManager.device);
		}
	}

	public void handleEnvSample(final Bundle bundle) {
		if (bundle != null) {
			final float temp = bundle.getFloat("tempArray");
			final int light_compressed = bundle.getInt("lightArray");
			final float light = (float)this.decompressLight(light_compressed);
			AppFileLog.addTrace("IN handleEnvSample light : " + light + "  temp : " + temp);
			Log.d("com.resmed.refresh.env", "handleEnvSample light : " + light + "  temp : " + temp);
		}
	}

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
				if (payload.contains("TRUE")) {
					handleSamplesTransmissionCompleteForRPC(receivedRPC);
					return;
				}
				return;
			}
			ErrorRpc errorRpc = receivedRPC.getError();
			if (errorRpc == null) {
				return;
			}
			if (errorRpc.getCode() == -12 || errorRpc.getCode() == -11) {
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
			Log.d("com.resmed.refresh.sleepFragment", " SleepTrackFragment::handleSleepSessionStopped(Bundle data)");
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
		//V.Log("HandleStreamPacket:" + Arrays.toString(paramBundle.getByteArray("REFRESH_BED_NEW_DATA")));
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

	public void StartNewSession() {
		AppFileLog.deleteCurrentFile();
		// fake session-id (we don't care about (or even want) the on-device data-recording)
		SPlusModule.main.sessionConnector.service.StartSession(1, SPlusModule.main.age, SPlusModule.main.GenderInt());

		Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::setupController()");
		this.isWaitLastSamples = false;
	}

	/*public void stopSleepSession() {
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
		}
	}*/
}
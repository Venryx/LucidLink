package SPlus;

import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.resmed.refresh.bed.RPCMapper;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.sleepsession.SleepSessionConnector;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import v.lucidlink.MainActivity;
import vpackages.V;

import static v.lucidlink.LLS.LL;

public class SPlusModule extends ReactContextBaseJavaModule {
	//public static Activity mainActivity;
	public static SPlusModule main;
	public SPlusModule(ReactApplicationContext reactContext) {
		super(reactContext);
		main = this;
		this.reactContext = reactContext;

		if (LL.mainModule.headlessLaunch) return;

		/*if (mainActivity == null)
			throw new RuntimeException("SPlusModule.mainActivity not set. (set it in your main-activity's constructor)");*/
		this.sessionConnector = new SleepSessionConnector(MainActivity.main, false);
		//this.sessionConnector.setHasAutoRecoveredFromCrash(this.hasAutoRecoveredFromCrash);
	}
	ReactApplicationContext reactContext;
	@Override public String getName() {
		return "SPlus";
	}

	public void SendEvent(String eventName, Object... args) {
		WritableArray argsList = Arguments.createArray();
		for (Object arg : args)
			V.WritableArray_Add(argsList, arg);
		DeviceEventManagerModule.RCTDeviceEventEmitter jsModuleEventEmitter = reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
		jsModuleEventEmitter.emit(eventName, argsList);
	}
	class CallBuffer {
		CallBuffer(String eventName) {
			this.eventName = eventName;
		}
		String eventName;
		List<WritableArray> callArgumentSets = new ArrayList<>();
		double scheduledTime;
		public void ScheduleToRun(double scheduledTime) {
			this.scheduledTime = scheduledTime;
			V.WaitXThenRun((int)(scheduledTime - new Date().getTime()), ()-> {
				/*for (Object[] callArgumentSet : callArgumentSets) {
					SendEvent(eventName + "_Buffered", callArgumentSet);
				}*/
				SendEvent(eventName + "_Buffered", V.ToWritableArray(callArgumentSets));
				SendEvent_Buffered_buffers.remove(eventName);
			});
		}
	}
	HashMap<String, Double> SendEvent_Buffered_lastSendTimes = new HashMap<>();
	HashMap<String, CallBuffer> SendEvent_Buffered_buffers = new HashMap<>();
	public void SendEvent_Buffered(String eventName, double bufferLengthMS, Object... args) {
		Double lastSendTime = SendEvent_Buffered_lastSendTimes.containsKey(eventName) ? SendEvent_Buffered_lastSendTimes.get(eventName) : 0d;
		Double minNextSendTime = lastSendTime + bufferLengthMS;
		if (minNextSendTime <= new Date().getTime()) {
			SendEvent(eventName, args);
		} else {
			if (!SendEvent_Buffered_buffers.containsKey(eventName)) {
				SendEvent_Buffered_buffers.put(eventName, new CallBuffer(eventName));
			}
			CallBuffer buffer = SendEvent_Buffered_buffers.get(eventName);
			buffer.callArgumentSets.add(V.ToWritableArray(args));
			buffer.ScheduleToRun(minNextSendTime);
		}
	}

	public SleepSessionConnector sessionConnector;

	public boolean connectorActive;
	@ReactMethod public void Connect() {
		if (connectorActive) return;
		connectorActive = true;

		V.Log("Connecting to S+...");
		SPlusModule.main.sessionConnector.service.StartConnector();
	}
	@ReactMethod public void Disconnect() {
		if (!connectorActive) return;
		connectorActive = false;

		//if (SPlusModule.main.sessionConnector.service.sleepSessionManager == null || !SPlusModule.main.sessionConnector.service.sleepSessionManager.isActive) return;
		//baseManager.stop();

		V.Log("Disconnecting from S+...");
		StopSession();
		//this.sessionConnector.stopSleepSession();
		SPlusModule.main.sessionConnector.service.StopConnector();
		MainActivity.main.handleConnectionStatus(CONNECTION_STATE.SOCKET_NOT_CONNECTED);
	}

	@ReactMethod public void ShutDown() {
		Disconnect();
	}

	public int age;
	public String gender; // 0=male, 1=female
	public int GenderInt() { return gender == "male" ? 0 : 1; }

	@ReactMethod public void SetUserInfo(int age, String gender) {
		V.Assert(gender.equals("male") || gender.equals("female"));
		this.age = age;
		this.gender = gender;
	}

	/*@ReactMethod public void GetSleepStage(Promise promise) {
		if (SPlusModule.main.sessionConnector.service.sleepSessionManager == null) {
			promise.resolve(-1);
			return;
		}

		/*int stage = baseManager.rm20Manager.getRealTimeSleepState();
		promise.resolve(stage); // this is not actually the sleep-stage, but rather the success-flag (I think)*#/
		MainActivity.main.getSleepStage_currentWaiter = promise;
		//baseManager.rm20Manager.getRealTimeSleepState();
		SPlusModule.main.sessionConnector.service.sleepSessionManager.rm20Manager.getRealTimeSleepState();
	}*/
	String currentSessionType;
	@ReactMethod public void StartRealTimeSession() {
		StopSession();

		V.Log("Starting real-time session...");
		this.sessionConnector.StartNewSession();
		MainActivity.main.sendRpcToBed(RPCMapper.main.startRealTimeStream());
		currentSessionType = "real time";

		LL.analytics.logEvent("StartRealTimeSession", new Bundle());
	}
	@ReactMethod public void StartSleepSession() {
		StopSession();

		V.Log("Starting sleep session...");
		this.sessionConnector.StartNewSession();

		/*MainActivity.main.sendRpcToBed(RPCMapper.main.startNightTracking());
		currentSessionType = "sleep";*/
		// Actually, use real-time tracking, so we can more quickly react to breathing changes and such.
		// Unfortunately, real-time tracking seems to disconnect/stop after a while, so we have to reconnect/restart whenever that happens.
		MainActivity.main.sendRpcToBed(RPCMapper.main.startRealTimeStream());
		currentSessionType = "real time";

		LL.analytics.logEvent("StartSleepSession", new Bundle());
	}
	@ReactMethod public void StopSession() {
		if (currentSessionType == null) return;

		V.Log("Stopping session...");
		V.Log(V.GetStackTrace());
		if (currentSessionType.equals("real time")) {
			MainActivity.main.sendRpcToBed(RPCMapper.main.stopRealTimeStream()); // quick fix, since lazy
		} else { //if (currentSessionType == "sleep")
			MainActivity.main.sendRpcToBed(RPCMapper.main.stopNightTimeTracking()); // quick fix, since lazy
		}
		MainActivity.main.sendRpcToBed(RPCMapper.main.closeSession());
		sessionConnector.service.sleepSessionManager.stopCalculateAndSendResults();
		currentSessionType = null;
	}

	@ReactMethod public void RestartDataStream() {
		if (currentSessionType.equals("real time")) {
			MainActivity.main.sendRpcToBed(RPCMapper.main.stopRealTimeStream());
			MainActivity.main.sendRpcToBed(RPCMapper.main.startRealTimeStream());
		} else {
			MainActivity.main.sendRpcToBed(RPCMapper.main.stopNightTimeTracking());
			MainActivity.main.sendRpcToBed(RPCMapper.main.startNightTracking());
		}
	}
}
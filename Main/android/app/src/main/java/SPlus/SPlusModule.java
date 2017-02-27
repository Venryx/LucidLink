package SPlus;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.resmed.refresh.bed.RPCMapper;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.sleepsession.SleepSessionConnector;

import v.lucidlink.MainActivity;
import v.lucidlink.V;

import static v.lucidlink.LLHolder.LL;

public class SPlusModule extends ReactContextBaseJavaModule {
	public static Activity mainActivity;
	public static SPlusModule main;
	public SPlusModule(ReactApplicationContext reactContext) {
		super(reactContext);
		main = this;
		this.reactContext = reactContext;

		//Init(); // just call it right away
		if (mainActivity == null)
			throw new RuntimeException("SPlusModule.mainActivity not set. (set it in your main-activity's constructor)");
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

	public SleepSessionConnector sessionConnector;
	/*@ReactMethod public void Init() {
		if (mainActivity == null)
			throw new RuntimeException("SPlusModule.mainActivity not set. (set it in your main-activity's constructor)");
		this.sessionConnector = new SleepSessionConnector(MainActivity.main, false);
		//this.sessionConnector.setHasAutoRecoveredFromCrash(this.hasAutoRecoveredFromCrash);
	}*/

	public boolean connectorActive;
	@ReactMethod public void Connect() {
		if (connectorActive) return;
		connectorActive = true;

		SPlusModule.main.sessionConnector.service.StartConnector();
	}
	@ReactMethod public void Disconnect() {
		if (!connectorActive) return;
		connectorActive = false;

		//if (SPlusModule.main.sessionConnector.service.sleepSessionManager == null || !SPlusModule.main.sessionConnector.service.sleepSessionManager.isActive) return;
		//baseManager.stop();

		StopSession();
		//this.sessionConnector.stopSleepSession();
		SPlusModule.main.sessionConnector.service.StopConnector();
		MainActivity.main.handleConnectionStatus(CONNECTION_STATE.SOCKET_NOT_CONNECTED);
	}

	public void ShutDown() {
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
		MainActivity.main.sendRpcToBed(RPCMapper.main.startNightTracking());
		currentSessionType = "sleep";

		LL.analytics.logEvent("StartSleepSession", new Bundle());
	}
	@ReactMethod public void StopSession() {
		if (currentSessionType == null) return;

		if (currentSessionType.equals("real time"))
			MainActivity.main.sendRpcToBed(RPCMapper.main.stopRealTimeStream()); // quick fix, since lazy
		else //if (currentSessionType == "sleep")
			MainActivity.main.sendRpcToBed(RPCMapper.main.stopNightTimeTracking()); // quick fix, since lazy
		MainActivity.main.sendRpcToBed(RPCMapper.main.closeSession());
		sessionConnector.service.sleepSessionManager.stopCalculateAndSendResults();
		currentSessionType = null;
		V.Log("Stopping session...");
	}
}
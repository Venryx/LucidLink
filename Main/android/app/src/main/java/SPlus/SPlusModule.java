package SPlus;

import android.app.Activity;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.resmed.refresh.bed.RPCMapper;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.sleepsession.SleepSessionConnector;

import v.lucidlink.MainActivity;
import v.lucidlink.V;

public class SPlusModule extends ReactContextBaseJavaModule {
	public static Activity mainActivity;
	public static SPlusModule main;
	public SPlusModule(ReactApplicationContext reactContext) {
		super(reactContext);
		main = this;
		this.reactContext = reactContext;
	}
	ReactApplicationContext reactContext;
	@Override public String getName() {
		return "SPlus";
	}

	public void SendEvent(String eventName, Object... args) {
		WritableArray argsList = Arguments.createArray();
		for (Object arg : args) {
			// for types that are invalid, but can easily be cast to a valid one, do so
			/*if (arg instanceof Float)
				arg = (double)(float)arg;

			if (arg == null)
				argsList.pushNull();
			else if (arg instanceof Boolean)
				argsList.pushBoolean((Boolean) arg);
			else if (arg instanceof Integer)
				argsList.pushInt((Integer) arg);
			else if (arg instanceof Double)
				argsList.pushDouble((Double) arg);
			else if (arg instanceof String)
				argsList.pushString((String) arg);
			else if (arg instanceof WritableArray)
				argsList.pushArray((WritableArray) arg);
			else {
				//Assert(arg instanceof WritableMap, "Event args must be one of: WritableArray, Boolean")
				if (!(arg instanceof WritableMap))
					throw new RuntimeException("Event args must be one of: Boolean, Integer, Double, String, WritableArray, WritableMap");
				argsList.pushMap((WritableMap) arg);
			}*/
			V.WritableArray_Add(argsList, arg);
		}

		DeviceEventManagerModule.RCTDeviceEventEmitter jsModuleEventEmitter = reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
		jsModuleEventEmitter.emit(eventName, argsList);
	}

	public SleepSessionConnector sessionConnector;
	@ReactMethod public void Init() {
		if (mainActivity == null)
			throw new RuntimeException("SPlusModule.mainActivity not set. (set it in your main-activity's constructor)");

		int bioOnBed = 0; // how many packets to buffer before sending from device to here?
		boolean isSyncing = false;
		this.sessionConnector = new SleepSessionConnector(MainActivity.main, bioOnBed, isSyncing);
		//this.sessionConnector.setHasAutoRecoveredFromCrash(this.hasAutoRecoveredFromCrash);
	}

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
	}
	@ReactMethod public void StartSleepSession() {
		StopSession();

		V.Log("Starting sleep session...");
		this.sessionConnector.StartNewSession();
		MainActivity.main.sendRpcToBed(RPCMapper.main.startNightTracking());
		currentSessionType = "sleep";
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
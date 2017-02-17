package SPlus;

import android.app.Activity;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.resmed.refresh.bed.BedDefaultRPCMapper;
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
	/*public void handleStreamPacket(Bundle data) {
        if (this.sessionConnector != null) {
            this.sessionConnector.handleStreamPacket(data);
        }
        byte[] decobbed = data.getByteArray(RefreshBluetoothService.REFRESH_BED_NEW_DATA);
        byte packetType = data.getByte(RefreshBluetoothService.REFRESH_BED_NEW_DATA_TYPE);
        data.getInt(RefreshBluetoothService.REFRESH_BED_NEW_DATA_SIZE);
        Log.d(LOGGER.TAG_UI, " SleepTrackFragment handleStreamPacket decobbed : " + Arrays.toString(decobbed) + " packetType : " + packetType);
        if (VLPacketType.PACKET_TYPE_NOTE_ILLUMINANCE_CHANGE.ordinal() == packetType) {
            handleEnvData(decobbed, false);
        } else if (VLPacketType.PACKET_TYPE_NOTE_HEARTBEAT.ordinal() == packetType) {
            handleHeartBeat(decobbed);
        } else if (VLPacketType.PACKET_TYPE_ENV_1.ordinal() == packetType) {
            handleEnvData(decobbed, true);
        } else if (VLPacketType.PACKET_TYPE_ENV_60.ordinal() == packetType) {
            handleEnvData(decobbed, true);
        }
    }
    private void handleEnvData(byte[] decobbed, boolean persistData) {
        int illumValue = PacketsByteValuesReader.readIlluminanceValue(decobbed);
        float tempValue = PacketsByteValuesReader.readTemperatureValue(decobbed);
        this.mLightLevelValueText.setText(new StringBuilder(String.valueOf(illumValue)).append(" Lux").toString());
        String tempInScale = "";
        if (RefreshModelController.getInstance().getUseMetricUnits()) {
            tempInScale = Math.round(tempValue) + " " + getString(R.string.degrees_celsius);
        } else {
            tempInScale = new StringBuilder(String.valueOf(Math.round(MeasureManager.convertCelsiusToFahrenheit(tempValue)))).append(" ").append(getString(R.string.degrees_farenheit)).toString();
        }
        this.mTemperatureValueText.setText(tempInScale);
    }
    private void handleHeartBeat(byte[] decobbed) {
    }
    public void handleEnvSample(Bundle data) {
        this.sessionConnector.handleEnvSample(data);
    }
    public void handleSleepSessionStopped(Bundle data) {
        Log.d(LOGGER.TAG_FINISH_SESSION, "handleSleepSessionStopped() ");
        AppFileLog.addTrace("STOP handleSleepSessionStopped ");
        this.sessionConnector.handleSleepSessionStopped(data);
    }
    public void handleBreathingRate(Bundle data) {
        Log.i("RM20StartMethod", "handleBreathingRate() SleepTrackFragment");
        this.sessionConnector.handleBreathingRate(data);
    }
    public void handleUserSleepState(Bundle data) {
        Log.i("RM20StartMethod", "handleUserSleepState() SleepTrackFragment");
        Log.i(LOGGER.TAG_RELAX, "handleUserSleepState() SleepTrackFragment " + data);
        this.sessionConnector.handleUserSleepState(data);
    }*/

	@ReactMethod public void Connect() {
		SPlusModule.main.sessionConnector.service.StartListening();

		//int sessionID = 70;
		//baseManager.rm20Manager.rm20Lib.loadLibrary(reactContext);
		//baseManager.start(sessionID, age, gender);

		/*this.sessionConnector.init(false);
		BaseBluetoothActivity.IN_SLEEP_SESSION = true;*/
	}
	@ReactMethod public void Disconnect() {
		//if (SPlusModule.main.sessionConnector.service.sleepSessionManager == null || !SPlusModule.main.sessionConnector.service.sleepSessionManager.isActive) return;
		//baseManager.stop();

		MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().stopRealTimeStream()); // quick fix, since lazy
		MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().stopNightTimeTracking()); // quick fix, since lazy
		MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().closeSession());
		//this.sessionConnector.stopSleepSession();
		//MainActivity.main.handleConnectionStatus(CONNECTION_STATE.SOCKET_CONNECTED); // make lights green again right now, since response might not arrive in time
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
	@ReactMethod public void StartRealTimeStream() {
		V.Log("Starting real-time stream...");
		//this.sessionConnector.EnsureSleepSessionStarted();
		MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().stopRealTimeStream()); // quick fix, since lazy
		MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().stopNightTimeTracking()); // quick fix, since lazy
		MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().startRealTimeStream());
	}
	@ReactMethod public void StartSleep() {
		V.Log("Starting sleep stream...");
		this.sessionConnector.EnsureSleepSessionStarted();
		MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().stopRealTimeStream()); // quick fix, since lazy
		MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().stopNightTimeTracking()); // quick fix, since lazy
		MainActivity.main.sendRpcToBed(BedDefaultRPCMapper.getInstance().startNightTracking());
	}
}
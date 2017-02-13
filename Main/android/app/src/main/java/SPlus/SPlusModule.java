package SPlus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.resmed.refresh.bed.BedCommandsRPCMapper;
import com.resmed.refresh.bed.BedDefaultRPCMapper;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.bluetooth.RefreshBluetoothService;
import com.resmed.refresh.bluetooth.RefreshBluetoothServiceClient;
import com.resmed.refresh.packets.VLP;
import com.resmed.refresh.sleepsession.SleepSessionConnector;
import com.resmed.refresh.sleepsession.SleepSessionManager;
import com.resmed.rm20.IndexActivity;
import com.resmed.rm20.RM20Callbacks;
import com.resmed.rm20.RM20JNI;
import com.resmed.rm20.SleepParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import v.lucidlink.MainActivity;
import v.lucidlink.V;

enum MessageType {
	None(0),
	DidFinishEdfRecovery(21),
	StopCalculateAndSendResults(14),
	AddEnvData(15),
	OnRm20RealTimeSleepState(17),
	OnRm20ValidBreathingRate(18);

	MessageType(int value) { this.value = value; }
	public int value;

	/*public static MessageType GetValue(int val) {
		MessageType[] entries = MessageType.values();
		for(int i = 0; i < entries.length; i++){
			if(entries[i].value == val)
				return entries[i];
		}
		return MessageType.None;
	}*/
	private static final Map<Integer, MessageType> intToTypeMap = new HashMap<>();
	static {
		for (MessageType type : MessageType.values()) {
			intToTypeMap.put(type.value, type);
		}
	}
	public static MessageType GetEntry(int val) {
		MessageType type = intToTypeMap.get(Integer.valueOf(val));
		if (type == null)
			return MessageType.None;
		return type;
	}
}

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
			}
		}

		DeviceEventManagerModule.RCTDeviceEventEmitter jsModuleEventEmitter = reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
		jsModuleEventEmitter.emit(eventName, argsList);
	}

	public SleepSessionManager baseManager;

	private static BedCommandsRPCMapper RpcCommands = BedDefaultRPCMapper.getInstance();
	@ReactMethod public void Init() {
		if (mainActivity == null)
			throw new RuntimeException("SPlusModule.mainActivity not set. (set it in your main-activity's constructor)");

		/*RefreshApplication refreshApp = new RefreshApplication();
		RefreshApplication.instance = refreshApp;*/




		//RefreshBluetoothService bluetoothService = new RefreshBluetoothService();
		/*Intent myIntent = new Intent(reactContext, RefreshBluetoothService.class);
		reactContext.startService(myIntent);

		RpcCommands.setContextBroadcaster(reactContext);
		com.resmed.refresh.utils.Log.d("com.resmed.refresh.ui", ": bluetooth service is running : " + var2);
		Intent intent = new Intent(reactContext, RefreshBluetoothService.class);
		intent.putExtra("PREF_CONNECTION_STATE", CONNECTION_STATE.NIGHT_TRACK_ON);
		intent.putExtra("PREF_NIGHT_LAST_SESSION_ID", 0);
		reactContext.startService(intent);

		SharedPreferences var4 = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		int var5 = var4.getInt("PREF_CONNECTION_STATE", -1);
		com.resmed.refresh.utils.Log.d("com.resmed.refresh.ui", " SleepTimeActivity::onResume conn state : " + var5);
		if(var5 == CONNECTION_STATE.NIGHT_TRACK_ON.ordinal()) {
			int var9 = var4.getInt("PREF_LAST_RPC_ID_USED", -1);
			if(-1 != var9) {
				RpcCommands.setRPCid(var9);
			}
		}

		this.registerReceiver(this.connectionStatusReceiver, new IntentFilter("ACTION_RESMED_CONNECTION_STATUS"));
		this.registerReceiver(this.bluetoothServiceRestartReceiver, new IntentFilter("BLUETOOTH_SERVICE_INTENT_RESTART"));*/




		V.Log("Test2");
		baseManager = new SleepSessionManager(new RefreshBluetoothServiceClient() {
			@Override public Context getContext() {
				//return bluetoothService.getContext();
				return reactContext;
			}
			@Override public void handlePacket(VLP.VLPacket vlPacket) {
				V.Log("in handlePacket...");
				//bluetoothService.handlePacket(vlPacket);
			}

			@Override
			public void sendConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE) {
				V.Log("in sendConnectionStatus");
			}

			@Override public void sendMessageToClient(Message message) {
				V.Toast("Received message!" + message.getData());
				MessageType type = MessageType.GetEntry(message.what);
				if (type == MessageType.OnRm20RealTimeSleepState) {
					V.Toast("Received OnRm20RealTimeSleepState message!" + message.getData());
				}

				//bluetoothService.sendMessageToClient(message);
			}
		});
		V.Log("Test3");

		/*this.rm20Lib = new RM20JNI(Environment.getExternalStorageDirectory(), new RM20Callbacks() {
			@Override
			public void onRm20RealTimeSleepState(int paramInt1, int paramInt2) {
				V.Log("Received onRm20RealTimeSleepState data!" + paramInt1 + ";" + paramInt2);
			}

			@Override
			public void onRm20ValidBreathingRate(float paramFloat, int paramInt) {
				V.Log("Received onRm20ValidBreathingRate data!" + paramFloat + ";" + paramInt);
			}
		}, MainActivity.main.getApplicationContext());
		this.rm20Lib.loadLibrary(MainActivity.main.getApplicationContext());
		V.Log("Loading...");
		V.WaitXThenRun(5000, ()-> {
			V.Log("Starting...");
			this.rm20Lib.startupLibrary(34, 1);
			this.rm20Lib.setRespRateCallbacks(true);

			new Thread(new Runnable() {
				@Override
				public void run() {
					Random var1 = new Random();

					for(int var2 = 0; var2 < 10000; ++var2) {
						int var3 = var1.nextInt(1000);
						int var4 = var1.nextInt(1000);
						SPlusModule.this.rm20Lib.writeSampleData(var3, var4);

						try {
							Thread.sleep(5L);
						} catch (InterruptedException var10) {
							var10.printStackTrace();
						}
					}

					SPlusModule.this.rm20Lib.getEpochCount();
					SPlusModule.this.rm20Lib.stopAndCalculate();
					SleepParams var9 = SPlusModule.this.rm20Lib.resultsForSession();
					V.Log(this.getClass().getName(), " results : " + var9);
				}
			}).start();
		});*/
	}
	//protected RM20JNI rm20Lib;

	@ReactMethod public void Connect() {
		V.Log("Connecting...");
		int sessionID = 70;
		int age = 20;
		int gender = 0; // 0: male, 1: female
		//baseManager.rm20Manager.rm20Lib.loadLibrary(reactContext);
		baseManager.start(sessionID, age, gender);
	}
	@ReactMethod public void Disconnect() {
		baseManager.stop();
	}

	@ReactMethod public void GetSleepStage(Promise promise) {
		int stage = baseManager.rm20Manager.getRealTimeSleepState();
		promise.resolve(stage);
	}
}
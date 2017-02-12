package SPlus;

import android.app.Activity;
import android.content.Context;
import android.os.Message;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.bluetooth.RefreshBluetoothService;
import com.resmed.refresh.bluetooth.RefreshBluetoothServiceClient;
import com.resmed.refresh.packets.VLP;
import com.resmed.refresh.sleepsession.SleepSessionManager;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.RefreshTools;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import v.lucidlink.Frame.VReflection;
import v.lucidlink.LucidLinkModule;
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
	@ReactMethod public void Init() {
		if (mainActivity == null)
			throw new RuntimeException("SPlusModule.mainActivity not set. (set it in your main-activity's constructor)");

		// some monkey-patching
		//RefreshApplication fakeRefreshApp = new RefreshApplication();
		RefreshApplication fakeRefreshApp;
		try {
			ClassPool cp = ClassPool.getDefault();
			cp.appendClassPath("libs/*");
			cp.appendClassPath(new ClassClassPath(RefreshApplication.class));
			CtClass cc = cp.get("com.resmed.refresh.ui.uibase.app.RefreshApplication");
			CtMethod m = cc.getDeclaredMethod("getFilesDir");
			m.insertBefore("{ System.out.println(\"Test4129038594\"); }");
			Class c = cc.toClass();
			fakeRefreshApp = (RefreshApplication)c.newInstance();
		} catch (Exception e) {
			throw new Error(e);
		}

		VReflection.SetField_Static(RefreshApplication.class, "instance", fakeRefreshApp);
		V.Log("Test1:" + RefreshApplication.getInstance());



		//V.Log("Test1A:" + VReflection.GetFields(RefreshApplication.class));
		V.Log("Test1A:" + RefreshApplication.getInstance().getFilesDir());
		V.Log("Test1B:" + RefreshTools.getFilesPath());

		RefreshBluetoothService bluetoothService = new RefreshBluetoothService();
		V.Log("Test2");
		baseManager = new SleepSessionManager(new RefreshBluetoothServiceClient() {
			@Override public Context getContext() {
				return bluetoothService.getContext();
			}
			@Override public void handlePacket(VLP.VLPacket vlPacket) {
				bluetoothService.handlePacket(vlPacket);
			}
			@Override public void sendConnectionStatus(CONNECTION_STATE connection_state) {
				bluetoothService.sendConnectionStatus(connection_state);
			}
			@Override public void sendMessageToClient(Message message) {
				MessageType type = MessageType.GetEntry(message.what);
				if (type == MessageType.OnRm20RealTimeSleepState) {
				}

				bluetoothService.sendMessageToClient(message);
			}
		});
		V.Log("Test3");
	}


	@ReactMethod void Connect() {
		int sessionID = -1;
		baseManager.start(sessionID, -1, -1);
	}
	@ReactMethod void Disconnect() {
		baseManager.stop();
	}

	@ReactMethod void GetSleepStage(Promise promise) {
		int stage = baseManager.rm20ManagerInstance().getRealTimeSleepState();
		promise.resolve(stage);
	}
}
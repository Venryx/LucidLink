package v.lucidlink;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Debug;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.views.textinput.ReactEditText;
import v.lucidlink.Frame.Vector2i;
import v.lucidlink.Frame.VolumeManager;
import vpackages.V;
import vpackages.VFile;

import com.lugg.ReactSnackbar.ReactSnackbarModule;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static v.lucidlink.LLS.LL;

public class LucidLinkModule extends ReactContextBaseJavaModule {
    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

    public LucidLinkModule(ReactApplicationContext reactContext) {
        super(reactContext);

		// when the react-native Reload button is pressed, a new LucidLinkModule class instance is created; check if this just happened
		firstLaunch = LL.mainModule == null;
		if (!firstLaunch)
			LL.mainModule.Shutdown();

		LL.reactContext = reactContext;
		LL.mainModule = this;

		MainActivity.main.EnsurePermissionsGranted();
		MainActivity.main.PostModuleInit();
    }
    public boolean headlessLaunch;
	public boolean firstLaunch;

	@ReactMethod public void ArePermissionsGranted(Promise promise) {
		promise.resolve(MainActivity.main.ArePermissionsGranted());
	}

    @Override public String getName() {
        return "LucidLink";
    }

    @Override public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

	Toast lastToast;
    @ReactMethod public void ShowToast(String message, int duration) {
		MainActivity.main.runOnUiThread(()-> {
			if (lastToast != null)
				lastToast.cancel();
			Toast toast = Toast.makeText(getReactApplicationContext(), message, duration);
			toast.show();
			lastToast = toast;
		});
    }
	/*TextView toastView;
	@ReactMethod
	public void ShowToast(String message, int duration) {
		if (toastView == null) {
			toastView = new TextView(MainActivity.main);
			toastView.setLayoutParams(V.CreateFrameLayoutParams(MainA));
			V.GetRootView().addView(toastView);
		}
		Toast toast = Toast.makeText(getReactApplicationContext(), message, duration);
		toast.show();
		lastToast = toast;
	}*/

	@ReactMethod public void Notify(String message, String lengthStr) {
		int length = -1;
		if (lengthStr.equals("Short"))
			length = Snackbar.LENGTH_SHORT;
		else if (lengthStr.equals("Long"))
			length = Snackbar.LENGTH_LONG;
		else if (lengthStr.equals("Persistent"))
			length = Snackbar.LENGTH_INDEFINITE;
		else
			V.Assert(false, "Length-string is invalid. (" + lengthStr + ")");

		//MainApplication.GetPackageOfType(ReactSnackbarPackage.class).show(message, Snackbar.LENGTH_SHORT, true, Color.parseColor("#FFFFFF"), "HI", null);
		LL.reactContext.getNativeModule(ReactSnackbarModule.class).show(message, length, true, Color.parseColor("#FFFFFF"), "HI", null);
	}

	@ReactMethod public void IsInEmulator(Promise promise) {
		boolean result = Build.FINGERPRINT.startsWith("generic")
			|| Build.FINGERPRINT.startsWith("unknown")
			|| Build.MODEL.contains("google_sdk")
			|| Build.MODEL.contains("Emulator")
			|| Build.MODEL.contains("Android SDK built for x86")
			|| Build.MANUFACTURER.contains("Genymotion")
			|| (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
			|| "google_sdk".equals(Build.PRODUCT);
		promise.resolve(result);
	}

	// settings
	public boolean blockUnusedKeys;

	@ReactMethod public void SetBasicData(ReadableMap data) {
		// settings
		this.blockUnusedKeys = data.getBoolean("blockUnusedKeys");
	}

	// special
	//public BluetoothDevice targetDevice;
	public CONNECTION_STATE connectionState = CONNECTION_STATE.SOCKET_NOT_CONNECTED;
	public boolean IsSocketConnected() {
		switch(connectionState) {
			case SOCKET_CONNECTED:
			case SESSION_OPENING:
			case SESSION_OPENED:
			case REAL_STREAM_OFF:
			case REAL_STREAM_ON:
			case NIGHT_TRACK_OFF:
			case NIGHT_TRACK_ON:
				return true;
			default:
				return false;
		}
	}

	@ReactMethod public void OnTabSelected(int tab) {
	}

	@ReactMethod public void AddChart() {
		SetUpUITimer();
	}
	void SetUpUITimer() {
		Timer uiModifierTimer = new Timer();
		uiModifierTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				MainActivity.main.runOnUiThread(()-> {
					List<View> textInputs = V.FindViews(V.GetRootView(),
							view->view.getContentDescription() != null && view.getContentDescription().toString().contains("@ConvertStartSpacesToTabs"));
					for (ReactEditText input : Stream.of(textInputs).toArray(ReactEditText[]::new)) {
						if (Objects.equals(input.getTag(), "modified")) continue;

						//V.ConvertTextInputTabsToSpans(input);

						input.addTextChangedListener(new TextWatcher() {
							int insertPos = 0, insertCount = 0;
							public void beforeTextChanged(CharSequence s, int editStart, int count, int after) {}
							public void onTextChanged(CharSequence s, int insertPos, int removeCount, int insertCount) {
								this.insertPos = insertPos;
								this.insertCount = insertCount;

								//V.Toast("Adding: " + (s != null ? s.toString() : "[null]") + ";" + insertPos + ";" + removeCount + ";" + insertCount);

								if (removeCount == 0 && insertCount == 1) { // if added one character
									boolean addedSpace = input.getSelectionStart() > 0 && s.charAt(input.getSelectionStart() - 1) == ' ';
									boolean hasOnlySpacesBefore = true;
									for (int i = insertPos; i >= 0; i--) {
										if (s.charAt(i) == '\r' || s.charAt(i) == '\n') break;
										if (s.charAt(i) != ' ' && s.charAt(i) != '\t')
											hasOnlySpacesBefore = false;
									}

									if (addedSpace && hasOnlySpacesBefore) {
										String textToInsert = "\t";
										input.getText().replace(insertPos, insertPos + 1, textToInsert, 0, textToInsert.length());
									}
								}
							}
							public void afterTextChanged(Editable view) {
								//V.ConvertTextInputTabsToSpans(input, this.insertPos, this.insertPos + this.insertCount);
							}
						});

						input.setTag("modified");
					}
				});
			}
		}, 1000, 1000);
	}

	/*@ReactMethod public void OnMonitorChangeVisible(boolean visible) {
	}*/

	@ReactMethod public void PostJSLog(String type, String message) {
		V.Log(type + " [js]", message, false);
	}

	@ReactMethod public void ConvertURIToPath(String uriStr, Promise promise) {
		Uri uri = Uri.parse(uriStr);
		String path = VFile.URIToPath(uri);
		promise.resolve(path);
	}

	@ReactMethod public void SetVolumes(double normalVolume, double bluetoothVolume) {
		AudioManager audioManager = (AudioManager)MainActivity.main.getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		if (normalVolume != -1000) {
			VolumeManager.SetVolume(VolumeManager.VolumeChannel.Normal, (int)(normalVolume * maxVolume));
		}
		if (bluetoothVolume != -1000) {
			VolumeManager.SetVolume(VolumeManager.VolumeChannel.Bluetooth, (int)(bluetoothVolume * maxVolume));
		}
	}
	@ReactMethod public void IncreaseVolumes(double normalVolumeChange, double bluetoothVolumeChange) {
		AudioManager audioManager = (AudioManager)MainActivity.main.getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		if (normalVolumeChange != -1000) {
			VolumeManager.IncreaseVolume(VolumeManager.VolumeChannel.Normal, (int)(normalVolumeChange * maxVolume));
		}
		if (bluetoothVolumeChange != -1000) {
			VolumeManager.IncreaseVolume(VolumeManager.VolumeChannel.Bluetooth, (int)(bluetoothVolumeChange * maxVolume));
		}
	}

	@ReactMethod public void GetAppUsedMemory(Promise promise) {
		final Runtime runtime = Runtime.getRuntime();
		final int usedMemInMB = (int)((runtime.totalMemory() - runtime.freeMemory()) / 1048576L);
		promise.resolve(usedMemInMB);
	}
	/*@ReactMethod public void GetAppFreeMemory(Promise promise) {
		final Runtime runtime = Runtime.getRuntime();
		final int usedMemInMB = (int)((runtime.totalMemory() - runtime.freeMemory()) / 1048576L);
		final int maxHeapSizeInMB = runtime.maxMemory() / 1048576L;
		final int appFreeMemory = maxHeapSizeInMB - usedMemInMB;
		promise.resolve(appFreeMemory);
	}*/
	@ReactMethod public void GetAppTotalMemory(Promise promise) {
		final int maxHeapSizeInMB = (int)(Runtime.getRuntime().maxMemory() / 1048576L);
		promise.resolve(maxHeapSizeInMB);
	}
	@ReactMethod public void GetAppMaxMemory(Promise promise) {
		final int resultInMB = (int)(Runtime.getRuntime().maxMemory() / 1048576L);
		promise.resolve(resultInMB);
	}
	@ReactMethod public void GetAppNativeUsedMemory(Promise promise) {
		final int resultInMB = (int)(Debug.getNativeHeapAllocatedSize() / 1048576L);
		promise.resolve(resultInMB);
	}

	void Shutdown() {
		V.Log("Shutting down...");
	}
}
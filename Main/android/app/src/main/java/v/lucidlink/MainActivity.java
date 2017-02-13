package v.lucidlink;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Toast;

import com.facebook.react.ReactActivity;
import com.google.gson.Gson;
import com.resmed.cobs.COBS;
import com.resmed.refresh.bed.BedCommandsRPCMapper;
import com.resmed.refresh.bed.BedDefaultRPCMapper;
import com.resmed.refresh.bed.LedsState;
import com.resmed.refresh.bluetooth.BluetoothDataWriter;
import com.resmed.refresh.bluetooth.BluetoothSetup;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.bluetooth.RefreshBluetoothService;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.model.json.ResultRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLP;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.sleepsession.SleepSessionConnector;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.BluetoothDataSerializeUtil;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.refresh.utils.RefreshUserPreferencesData;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import SPlus.SPlusModule;
import SPlus.SPlusPackage;
import v.LibMuse.LibMuseModule;

import static android.os.Build.VERSION.SDK;

public class MainActivity extends BaseBluetoothActivity implements BluetoothDataListener, BluetoothDataWriter {
	public static MainActivity main;
	public MainActivity() {
		super();
		main = this;
		LibMuseModule.mainActivity = this;
		SPlusModule.mainActivity = this;
	}

	static final int REQUEST_WRITE_STORAGE = 112;
	public boolean ArePermissionsGranted() {
		// workaround for runtime bug of ContextCompat evaluating to the old version (in S+ jar)
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
			return true;
		return ContextCompat.checkSelfPermission(LL.main.reactContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
	}
	public void EnsurePermissionsGranted() {
		boolean hasPermission = ArePermissionsGranted();
		if (!hasPermission)
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_WRITE_STORAGE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				recreate();
			} else {
				V.Toast("The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG);
			}
		}
	}

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "LucidLink";
    }

	@Override
	public void onBackPressed() {
	}

	@Override public boolean onKeyDown(int keyCode, KeyEvent event)  {
		char keyChar = (char)event.getUnicodeChar();
		LL.main.SendEvent("OnKeyDown", keyCode, String.valueOf(keyChar));
		if (LL.main.blockUnusedKeys)
			return true;
		return super.onKeyDown(keyCode, event);
	}
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		char keyChar = (char)event.getUnicodeChar();
		LL.main.SendEvent("OnKeyUp", keyCode, String.valueOf(keyChar));
		if (LL.main.blockUnusedKeys)
			return true;
		return super.onKeyUp(keyCode, event);
	}

	// extra stuff
	// ==========

	private void disconnectFromBeD(final boolean b) {
		Log.d("com.resmed.refresh.pair", "disconnectFromBeD(" + b + ")");
		final BluetoothDevice jsonFile = BluetoothDataSerializeUtil.readJsonFile(this.getApplicationContext());
		Log.d("com.resmed.refresh.pair", " HomeActivity::last conn state : " + LL.main.connectionState);
		if (LL.main.connectionState == CONNECTION_STATE.SESSION_OPENED || LL.main.connectionState == CONNECTION_STATE.SESSION_OPENING
				|| LL.main.connectionState == CONNECTION_STATE.SOCKET_CONNECTED || LL.main.connectionState == CONNECTION_STATE.SOCKET_RECONNECTING) {
			Log.d("com.resmed.refresh.pair", "CONNECTION_STATE = " + LL.main.connectionState);
			final JsonRPC closeSession = getRpcCommands().closeSession();
			if (b) {
				closeSession.setRPCallback(new JsonRPC.RPCallback() {
					public void execute() {
						/*Log.d("com.resmed.refresh.pair", "jsonRPC.RPCallback.execute");
						try {
							if (jsonFile != null) {
								MainActivity.this.sendRpcToBed(HomeActivity.getRpcCommands().openSession(RefreshModelController.getInstance().getUserSessionID()));
								Log.d("com.resmed.refresh.pair", "jsonRPC.sendRpcToBed.openSession(" + RefreshModelController.getInstance().getUserSessionID() + ")");
							}
						}
						catch (Exception ex) {
							ex.printStackTrace();
						}*/
					}

				public void onError(final JsonRPC.ErrorRpc jsonRPC$ErrorRpc) {
				}

				public void preExecute() {
				}
				});
			}
			this.sendRpcToBed(closeSession);
		}
	}

	public void handleConnectionStatus(final CONNECTION_STATE newState) {
		super.handleConnectionStatus(newState);
		V.Log("in handleConnectionStatus");
		/*if (this.homeFragment instanceof BluetoothDataListener) {
			((BluetoothDataListener)this.homeFragment).handleConnectionStatus(connection_STATE);
		}*/
	}

	protected void onCreate(final Bundle bundle) {
		super.onCreate(bundle);
		/*if (Consts.USE_EXTERNAL_STORAGE) {
			RefreshTools.exportDataBaseToSD();
		}
		//SleepSessionConnector.CancelRepeatingAlarmWake(this.getApplicationContext());
		final RefreshModelController instance = RefreshModelController.getInstance();
		instance.setupNotifications(this);
		final int int1 = PreferenceManager.getDefaultSharedPreferences((Context)this).getInt("PREF_CONNECTION_STATE", -1);
		if (!instance.isLoggedIn()) {
			Log.d("com.resmed.refresh.pair", "Not logged in => Landing screen");
			instance.setHasBeenLogout(true);
			this.disconnectFromBeD(false);
			this.startActivity(new Intent((Context)this, (Class)LandingActivty.class));
			this.finish();
			return;
		}
		if (int1 == CONNECTION_STATE.NIGHT_TRACK_ON.ordinal()) {
			AppFileLog.addTrace("HomeActivty Recovering a NIGHT_TRACK_ON");
			this.startActivity(new Intent((Context)this, (Class)SleepTimeActivity.class));
			this.overridePendingTransition(2130968586, 2130968586);
			return;
		}
		if (instance.getHasToValidateEmail()) {
			final Intent intent = new Intent((Context)this, (Class)EmailNotValidatedActivity.class);
			intent.setFlags(268468224);
			this.startActivity(intent);
			this.overridePendingTransition(2130968586, 2130968586);
			return;
		}
		if (instance.getHasBeenLogout()) {
			instance.setHasBeenLogout(false);
			this.disconnectFromBeD(true);
		}
		this.setContentView(2130903074);
		this.setTypeRefreshBar(BaseActivity$TypeBar.HOME_BAR);
		this.setTitle(2131165945);
		this.homeFragment = new HomeFragment();
		final FragmentTransaction beginTransaction = this.getSupportFragmentManager().beginTransaction();
		beginTransaction.replace(2131099795, (Fragment)this.homeFragment, "fragment_home");
		beginTransaction.commit();*/
	}

	/*protected void onResume() {
		super.onResume();
		//this.connectToBeD(false);
		//RefreshModelController.getInstance().setupNotifications(this);
		final CONNECTION_STATE state = LL.main.connectionState;
		if (state == CONNECTION_STATE.SESSION_OPENED || state == CONNECTION_STATE.SESSION_OPENING
				|| state == CONNECTION_STATE.SOCKET_CONNECTED || state == CONNECTION_STATE.SOCKET_RECONNECTING) {
			final JsonRPC leds = BaseBluetoothActivity.getRpcCommands().leds(LedsState.GREEN);
			if (leds != null) {
				this.sendRpcToBed(leds);
			}
		}
	}*/

	public void onStart() {
		super.onStart();
	}

	public void showBeDPickerDialog() {
	}
}
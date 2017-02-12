package com.resmed.refresh.bluetooth;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshUserPreferencesData;

public class BeDConnectionStatus {
	// $FF: synthetic field
	private static int[] $SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE;
	public static final String ACTION_RESMED_CONNECTION_STATUS = "ACTION_RESMED_CONNECTION_STATUS";
	public static final String EXTRA_RESMED_CONNECTION_STATE = "EXTRA_RESMED_CONNECTION_STATE";
	private static BeDConnectionStatus Instance = null;
	private boolean bedHasOtherUserData;
	private boolean bedHasUserData;
	private int dataStoredFlag;
	private boolean isBluetoothOn;
	private boolean isConnected;
	private boolean isNightTracking;
	private boolean isSessionOpened;
	private boolean isStreaming;
	private CONNECTION_STATE state;

	// $FF: synthetic method
	static int[] $SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE() {
		int[] var0 = $SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE;
		if(var0 != null) {
			return var0;
		} else {
			int[] var1 = new int[CONNECTION_STATE.values().length];

			try {
				var1[CONNECTION_STATE.BLUETOOTH_OFF.ordinal()] = 4;
			} catch (NoSuchFieldError var25) {
				;
			}

			try {
				var1[CONNECTION_STATE.BLUETOOTH_ON.ordinal()] = 3;
			} catch (NoSuchFieldError var24) {
				;
			}

			try {
				var1[CONNECTION_STATE.NIGHT_TRACK_OFF.ordinal()] = 2;
			} catch (NoSuchFieldError var23) {
				;
			}

			try {
				var1[CONNECTION_STATE.NIGHT_TRACK_ON.ordinal()] = 12;
			} catch (NoSuchFieldError var22) {
				;
			}

			try {
				var1[CONNECTION_STATE.REAL_STREAM_OFF.ordinal()] = 1;
			} catch (NoSuchFieldError var21) {
				;
			}

			try {
				var1[CONNECTION_STATE.REAL_STREAM_ON.ordinal()] = 11;
			} catch (NoSuchFieldError var20) {
				;
			}

			try {
				var1[CONNECTION_STATE.SESSION_OPENED.ordinal()] = 10;
			} catch (NoSuchFieldError var19) {
				;
			}

			try {
				var1[CONNECTION_STATE.SESSION_OPENING.ordinal()] = 9;
			} catch (NoSuchFieldError var18) {
				;
			}

			try {
				var1[CONNECTION_STATE.SOCKET_BROKEN.ordinal()] = 5;
			} catch (NoSuchFieldError var17) {
				;
			}

			try {
				var1[CONNECTION_STATE.SOCKET_CONNECTED.ordinal()] = 8;
			} catch (NoSuchFieldError var16) {
				;
			}

			try {
				var1[CONNECTION_STATE.SOCKET_NOT_CONNECTED.ordinal()] = 7;
			} catch (NoSuchFieldError var15) {
				;
			}

			try {
				var1[CONNECTION_STATE.SOCKET_RECONNECTING.ordinal()] = 6;
			} catch (NoSuchFieldError var14) {
				;
			}

			$SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE = var1;
			return var1;
		}
	}

	private BeDConnectionStatus(CONNECTION_STATE var1) {
		this.state = var1;
		this.updateState(var1);
	}

	public static BeDConnectionStatus getInstance() {
		if(Instance == null) {
			SharedPreferences var0 = PreferenceManager.getDefaultSharedPreferences(RefreshApplication.getInstance());
			CONNECTION_STATE var1 = CONNECTION_STATE.SOCKET_NOT_CONNECTED;
			if(var0.getInt("PREF_CONNECTION_STATE", -1) == CONNECTION_STATE.NIGHT_TRACK_ON.ordinal()) {
				var1 = CONNECTION_STATE.NIGHT_TRACK_ON;
			}

			Instance = new BeDConnectionStatus(var1);
		}

		return Instance;
	}

	public static boolean isSocketConnected(CONNECTION_STATE var0) {
		switch($SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE()[var0.ordinal()]) {
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				return false;
			default:
				return true;
		}
	}

	public int getBackgroundButtonConnected() {
		return this.isSocketConnected()?2130837877:2130837879;
	}

	public int getBackgroundButtonStreaming() {
		return this.isReady()?2130837877:2130837879;
	}

	public int getBeDDataFlag() {
		return this.dataStoredFlag;
	}

	public int getBeDIconConnected() {
		if(!this.isSocketConnected()) {
			Log.d("com.resmed.refresh.pair", " updateConnectionIcon red");
			return 2130837909;
		} else {
			return this.dataStoredFlag > 0?2130837900:2130837903;
		}
	}

	public int getBeDIconStreaming() {
		if(!this.isSocketConnected()) {
			Log.d("com.resmed.refresh.pair", " updateConnectionIcon red");
			return 2130837909;
		} else if(this.dataStoredFlag > 0) {
			return 2130837900;
		} else if(this.isReady()) {
			Log.d("com.resmed.refresh.pair", " updateConnectionIcon green");
			return 2130837903;
		} else {
			Log.d("com.resmed.refresh.pair", " updateConnectionIcon red 2");
			return 2130837909;
		}
	}

	public CONNECTION_STATE getState() {
		return this.state;
	}

	public boolean isBedHasOtherUserData() {
		return this.bedHasOtherUserData;
	}

	public boolean isBedHasUserData() {
		return this.bedHasUserData;
	}

	public boolean isBluetoothOn() {
		return this.isBluetoothOn;
	}

	public boolean isNightTracking() {
		return this.isNightTracking;
	}

	public boolean isReady() {
		switch($SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE()[this.state.ordinal()]) {
			case 1:
			case 2:
			case 8:
			case 10:
			case 11:
			case 12:
				return true;
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 9:
			default:
				return false;
		}
	}

	public boolean isSessionOpened() {
		return this.isSessionOpened;
	}

	public boolean isSocketConnected() {
		Log.e("com.resmed.refresh.pair", "BeDConnectionStatus isSocketConnected state:" + this.state);
		switch($SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE()[this.state.ordinal()]) {
			case 1:
			case 2:
			case 10:
			case 11:
			case 12:
				return true;
			default:
				return false;
		}
	}

	public boolean isStreaming() {
		return this.isStreaming;
	}

	public void updateBeDDataFlag(int var1) {
		Log.d("com.resmed.refresh.ui", "updateDataStoredFlag(" + var1 + ")");
		this.dataStoredFlag = var1;
		RefreshUserPreferencesData var3 = new RefreshUserPreferencesData(RefreshApplication.getInstance().getApplicationContext());
		var3.setIntegerConfigValue("PREF_DATA_ON_BED_NOTIFICATION_ID", Integer.valueOf(var1));
		Log.d("com.resmed.refresh.ui", "prefs updateDataStoredFlag(" + var3.getIntegerConfigValue("PREF_DATA_ON_BED_NOTIFICATION_ID") + ")");
	}

	public void updateState(CONNECTION_STATE var1) {
		if(var1 != null) {
			Log.d("com.resmed.refresh.bluetooth", "::updateState(change) CONNECTION_STATE :" + var1);
			this.state = var1;
			switch($SWITCH_TABLE$com$resmed$refresh$bluetooth$CONNECTION_STATE()[var1.ordinal()]) {
				case 1:
					this.isStreaming = false;
					return;
				case 2:
					this.isNightTracking = false;
					return;
				case 3:
					this.isBluetoothOn = true;
					return;
				case 4:
					this.isBluetoothOn = false;
					this.isSessionOpened = false;
					this.isNightTracking = false;
					this.isConnected = false;
					return;
				case 5:
				case 6:
				case 9:
				default:
					return;
				case 7:
					this.isConnected = false;
					this.isSessionOpened = false;
					return;
				case 8:
					this.isBluetoothOn = true;
					this.isConnected = true;
					return;
				case 10:
					this.isBluetoothOn = true;
					this.isConnected = true;
					this.isSessionOpened = true;
					return;
				case 11:
					this.isStreaming = true;
					return;
				case 12:
					this.isNightTracking = true;
			}
		}
	}
}

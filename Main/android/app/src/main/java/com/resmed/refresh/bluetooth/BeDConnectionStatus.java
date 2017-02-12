package com.resmed.refresh.bluetooth;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshUserPreferencesData;

public class BeDConnectionStatus
{
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
  
  private BeDConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    this.state = paramCONNECTION_STATE;
    updateState(paramCONNECTION_STATE);
  }
  
  public static BeDConnectionStatus getInstance()
  {
    if (Instance == null)
    {
      SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(RefreshApplication.getInstance());
      CONNECTION_STATE localCONNECTION_STATE = CONNECTION_STATE.SOCKET_NOT_CONNECTED;
      if (localSharedPreferences.getInt("PREF_CONNECTION_STATE", -1) == CONNECTION_STATE.NIGHT_TRACK_ON.ordinal()) {
        localCONNECTION_STATE = CONNECTION_STATE.NIGHT_TRACK_ON;
      }
      Instance = new BeDConnectionStatus(localCONNECTION_STATE);
    }
    return Instance;
  }
  
  public static boolean isSocketConnected(CONNECTION_STATE paramCONNECTION_STATE)
  {
    switch (paramCONNECTION_STATE)
    {
    }
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public int getBackgroundButtonConnected()
  {
    if (isSocketConnected()) {}
    for (int i = 2130837877;; i = 2130837879) {
      return i;
    }
  }
  
  public int getBackgroundButtonStreaming()
  {
    if (isReady()) {}
    for (int i = 2130837877;; i = 2130837879) {
      return i;
    }
  }
  
  public int getBeDDataFlag()
  {
    return this.dataStoredFlag;
  }
  
  public int getBeDIconConnected()
  {
    int i;
    if (!isSocketConnected())
    {
      Log.d("com.resmed.refresh.pair", " updateConnectionIcon red");
      i = 2130837909;
    }
    for (;;)
    {
      return i;
      if (this.dataStoredFlag > 0) {
        i = 2130837900;
      } else {
        i = 2130837903;
      }
    }
  }
  
  public int getBeDIconStreaming()
  {
    int i = 2130837909;
    if (!isSocketConnected()) {
      Log.d("com.resmed.refresh.pair", " updateConnectionIcon red");
    }
    for (;;)
    {
      return i;
      if (this.dataStoredFlag > 0)
      {
        i = 2130837900;
      }
      else if (isReady())
      {
        Log.d("com.resmed.refresh.pair", " updateConnectionIcon green");
        i = 2130837903;
      }
      else
      {
        Log.d("com.resmed.refresh.pair", " updateConnectionIcon red 2");
      }
    }
  }
  
  public CONNECTION_STATE getState()
  {
    return this.state;
  }
  
  public boolean isBedHasOtherUserData()
  {
    return this.bedHasOtherUserData;
  }
  
  public boolean isBedHasUserData()
  {
    return this.bedHasUserData;
  }
  
  public boolean isBluetoothOn()
  {
    return this.isBluetoothOn;
  }
  
  public boolean isNightTracking()
  {
    return this.isNightTracking;
  }
  
  public boolean isReady()
  {
    switch (this.state)
    {
    }
    for (boolean bool = false;; bool = true) {
      return bool;
    }
  }
  
  public boolean isSessionOpened()
  {
    return this.isSessionOpened;
  }
  
  public boolean isSocketConnected()
  {
    Log.e("com.resmed.refresh.pair", "BeDConnectionStatus isSocketConnected state:" + this.state);
    switch (this.state)
    {
    }
    for (boolean bool = false;; bool = true) {
      return bool;
    }
  }
  
  public boolean isStreaming()
  {
    return this.isStreaming;
  }
  
  public void updateBeDDataFlag(int paramInt)
  {
    Log.d("com.resmed.refresh.ui", "updateDataStoredFlag(" + paramInt + ")");
    this.dataStoredFlag = paramInt;
    RefreshUserPreferencesData localRefreshUserPreferencesData = new RefreshUserPreferencesData(RefreshApplication.getInstance().getApplicationContext());
    localRefreshUserPreferencesData.setIntegerConfigValue("PREF_DATA_ON_BED_NOTIFICATION_ID", Integer.valueOf(paramInt));
    Log.d("com.resmed.refresh.ui", "prefs updateDataStoredFlag(" + localRefreshUserPreferencesData.getIntegerConfigValue("PREF_DATA_ON_BED_NOTIFICATION_ID") + ")");
  }
  
  public void updateState(CONNECTION_STATE paramCONNECTION_STATE)
  {
    if (paramCONNECTION_STATE == null) {}
    for (;;)
    {
      return;
      Log.d("com.resmed.refresh.bluetooth", "::updateState(change) CONNECTION_STATE :" + paramCONNECTION_STATE);
      this.state = paramCONNECTION_STATE;
      switch (paramCONNECTION_STATE)
      {
      case REAL_STREAM_OFF: 
      case REAL_STREAM_ON: 
      case SOCKET_BROKEN: 
      default: 
        break;
      case BLUETOOTH_OFF: 
        this.isStreaming = false;
        break;
      case SOCKET_CONNECTED: 
        this.isBluetoothOn = true;
        this.isConnected = true;
        this.isSessionOpened = true;
        break;
      case NIGHT_TRACK_ON: 
        this.isBluetoothOn = false;
        this.isSessionOpened = false;
        this.isNightTracking = false;
        this.isConnected = false;
        break;
      case NIGHT_TRACK_OFF: 
        this.isBluetoothOn = true;
        break;
      case SESSION_OPENING: 
        this.isBluetoothOn = true;
        this.isConnected = true;
        break;
      case SESSION_OPENED: 
        this.isConnected = false;
        this.isSessionOpened = false;
        break;
      case SOCKET_NOT_CONNECTED: 
        this.isStreaming = true;
        break;
      case SOCKET_RECONNECTING: 
        this.isNightTracking = true;
        break;
      case BLUETOOTH_ON: 
        this.isNightTracking = false;
      }
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
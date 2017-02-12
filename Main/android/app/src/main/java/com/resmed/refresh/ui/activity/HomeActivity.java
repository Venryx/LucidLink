package com.resmed.refresh.ui.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.resmed.refresh.bed.LedsState;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.sleepsession.SleepSessionConnector;
import com.resmed.refresh.ui.fragments.HomeFragment;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.BluetoothDataSerializeUtil;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;

public class HomeActivity
  extends BaseBluetoothActivity
{
  private HomeFragment homeFragment;
  
  private void disconnectFromBeD(boolean paramBoolean)
  {
    Log.d("com.resmed.refresh.pair", "disconnectFromBeD(" + paramBoolean + ")");
    final BluetoothDevice localBluetoothDevice = BluetoothDataSerializeUtil.readJsonFile(getApplicationContext());
    Object localObject = RefreshApplication.getInstance().getCurrentConnectionState();
    Log.d("com.resmed.refresh.pair", " HomeActivity::last conn state : " + localObject);
    if ((localObject == CONNECTION_STATE.SESSION_OPENED) || (localObject == CONNECTION_STATE.SESSION_OPENING) || (localObject == CONNECTION_STATE.SOCKET_CONNECTED) || (localObject == CONNECTION_STATE.SOCKET_RECONNECTING))
    {
      Log.d("com.resmed.refresh.pair", "CONNECTION_STATE = " + localObject);
      localObject = getRpcCommands().closeSession();
      if (paramBoolean) {
        ((JsonRPC)localObject).setRPCallback(new JsonRPC.RPCallback()
        {
          public void execute()
          {
            Log.d("com.resmed.refresh.pair", "jsonRPC.RPCallback.execute");
            try
            {
              if (localBluetoothDevice != null)
              {
                Object localObject = HomeActivity.getRpcCommands().openSession(RefreshModelController.getInstance().getUserSessionID());
                HomeActivity.this.sendRpcToBed((JsonRPC)localObject);
                localObject = new java.lang.StringBuilder;
                ((StringBuilder)localObject).<init>("jsonRPC.sendRpcToBed.openSession(");
                Log.d("com.resmed.refresh.pair", RefreshModelController.getInstance().getUserSessionID() + ")");
              }
              return;
            }
            catch (Exception localException)
            {
              for (;;)
              {
                localException.printStackTrace();
              }
            }
          }
          
          public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {}
          
          public void preExecute() {}
        });
      }
      sendRpcToBed((JsonRPC)localObject);
    }
  }
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    super.handleConnectionStatus(paramCONNECTION_STATE);
    if ((this.homeFragment instanceof BluetoothDataListener)) {
      this.homeFragment.handleConnectionStatus(paramCONNECTION_STATE);
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (Consts.USE_EXTERNAL_STORAGE) {
      RefreshTools.exportDataBaseToSD();
    }
    SleepSessionConnector.CancelRepeatingAlarmWake(getApplicationContext());
    paramBundle = RefreshModelController.getInstance();
    paramBundle.setupNotifications(this);
    int i = PreferenceManager.getDefaultSharedPreferences(this).getInt("PREF_CONNECTION_STATE", -1);
    if (!paramBundle.isLoggedIn())
    {
      Log.d("com.resmed.refresh.pair", "Not logged in => Landing screen");
      paramBundle.setHasBeenLogout(true);
      disconnectFromBeD(false);
      startActivity(new Intent(this, LandingActivty.class));
      finish();
    }
    for (;;)
    {
      return;
      if (i == CONNECTION_STATE.NIGHT_TRACK_ON.ordinal())
      {
        AppFileLog.addTrace("HomeActivty Recovering a NIGHT_TRACK_ON");
        startActivity(new Intent(this, SleepTimeActivity.class));
        overridePendingTransition(2130968586, 2130968586);
      }
      else if (paramBundle.getHasToValidateEmail())
      {
        paramBundle = new Intent(this, EmailNotValidatedActivity.class);
        paramBundle.setFlags(268468224);
        startActivity(paramBundle);
        overridePendingTransition(2130968586, 2130968586);
      }
      else
      {
        if (paramBundle.getHasBeenLogout())
        {
          paramBundle.setHasBeenLogout(false);
          disconnectFromBeD(true);
        }
        setContentView(2130903074);
        setTypeRefreshBar(BaseActivity.TypeBar.HOME_BAR);
        setTitle(2131165945);
        this.homeFragment = new HomeFragment();
        paramBundle = getSupportFragmentManager().beginTransaction();
        paramBundle.replace(2131099795, this.homeFragment, "fragment_home");
        paramBundle.commit();
      }
    }
  }
  
  protected void onResume()
  {
    super.onResume();
    connectToBeD(false);
    RefreshModelController.getInstance().setupNotifications(this);
    Object localObject = RefreshApplication.getInstance().getCurrentConnectionState();
    if ((localObject == CONNECTION_STATE.SESSION_OPENED) || (localObject == CONNECTION_STATE.SESSION_OPENING) || (localObject == CONNECTION_STATE.SOCKET_CONNECTED) || (localObject == CONNECTION_STATE.SOCKET_RECONNECTING))
    {
      localObject = BaseBluetoothActivity.getRpcCommands().leds(LedsState.GREEN);
      if (localObject != null) {
        sendRpcToBed((JsonRPC)localObject);
      }
    }
  }
  
  public void onStart()
  {
    super.onStart();
  }
  
  public void showBeDPickerDialog() {}
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\HomeActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
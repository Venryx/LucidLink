package com.resmed.refresh.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.ui.activity.HomeActivity;
import com.resmed.refresh.ui.activity.ReconnectionProblemActivty;
import com.resmed.refresh.ui.font.GroteskLight;
import com.resmed.refresh.ui.font.GroteskLightButton;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.Log;

public class ReconnectFragment
  extends BaseFragment
  implements BluetoothDataListener
{
  private boolean connecting = false;
  private GroteskLightButton greenBtn;
  private CONNECTION_STATE lastState = CONNECTION_STATE.SOCKET_BROKEN;
  private GroteskLight message;
  private LinearLayout progress;
  private GroteskLightButton redBtn;
  
  private void checkConnection()
  {
    if ((this.connecting) && (getBaseActivity() != null) && (!getBaseActivity().isFinishing()))
    {
      Intent localIntent = new Intent(getBaseActivity(), ReconnectionProblemActivty.class);
      getBaseActivity().startActivity(localIntent);
      getBaseActivity().overridePendingTransition(2130968597, 2130968586);
    }
  }
  
  private void laterSync()
  {
    Object localObject = PreferenceManager.getDefaultSharedPreferences(getBaseActivity().getApplicationContext()).edit();
    ((SharedPreferences.Editor)localObject).putInt("PREF_CONNECTION_STATE", CONNECTION_STATE.NIGHT_TRACK_OFF.ordinal());
    ((SharedPreferences.Editor)localObject).commit();
    localObject = new Intent(getActivity(), HomeActivity.class);
    ((Intent)localObject).setFlags(268468224);
    startActivity((Intent)localObject);
    getActivity().overridePendingTransition(2130968582, 2130968583);
  }
  
  private void mapGUI(View paramView)
  {
    this.greenBtn = ((GroteskLightButton)paramView.findViewById(2131099962));
    this.redBtn = ((GroteskLightButton)paramView.findViewById(2131099963));
    this.message = ((GroteskLight)paramView.findViewById(2131099959));
    this.progress = ((LinearLayout)paramView.findViewById(2131099961));
  }
  
  private void setLabels()
  {
    this.greenBtn.setText(2131166111);
    this.redBtn.setText(2131166110);
    this.message.setText(2131166108);
  }
  
  private void setupListener()
  {
    this.redBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        ReconnectFragment.this.laterSync();
      }
    });
    this.greenBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        ReconnectFragment.this.syncSession();
      }
    });
  }
  
  private void syncSession()
  {
    Log.d("com.resmed.refresh.pair", "ReconnectFragment::syncSession()");
    BaseBluetoothActivity localBaseBluetoothActivity = (BaseBluetoothActivity)getBaseActivity();
    Log.d("com.resmed.refresh.pair", " Bluetooth check");
    if (localBaseBluetoothActivity.checkBluetoothEnabled(true))
    {
      Log.d("com.resmed.refresh.pair", "ReconnectFragment::syncSession() #1");
      if (localBaseBluetoothActivity != null)
      {
        Log.d("com.resmed.refresh.pair", "ReconnectFragment::syncSession() #2");
        localBaseBluetoothActivity.connectToBeD(false);
      }
      this.progress.setVisibility(0);
      if ((this.lastState != CONNECTION_STATE.NIGHT_TRACK_ON) && (!this.connecting))
      {
        this.connecting = true;
        new Handler().postDelayed(new Runnable()
        {
          public void run()
          {
            ReconnectFragment.this.checkConnection();
          }
        }, 15000L);
      }
    }
  }
  
  public void handleBreathingRate(Bundle paramBundle) {}
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    this.lastState = paramCONNECTION_STATE;
    switch (paramCONNECTION_STATE)
    {
    }
    do
    {
      return;
      this.connecting = false;
    } while ((getActivity() == null) || (getActivity().isFinishing()));
    paramCONNECTION_STATE = new Intent();
    if (getActivity().getParent() == null) {
      getActivity().setResult(-1, paramCONNECTION_STATE);
    }
    for (;;)
    {
      getActivity().finish();
      break;
      getActivity().getParent().setResult(-1, paramCONNECTION_STATE);
    }
  }
  
  public void handleEnvSample(Bundle paramBundle) {}
  
  public void handleReceivedRpc(JsonRPC paramJsonRPC) {}
  
  public void handleSessionRecovered(Bundle paramBundle) {}
  
  public void handleSleepSessionStopped(Bundle paramBundle)
  {
    Log.d("com.resmed.refresh.finish", "handleSleepSessionStopped() ");
  }
  
  public void handleStreamPacket(Bundle paramBundle) {}
  
  public void handleUserSleepState(Bundle paramBundle) {}
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903137, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListener();
    setLabels();
    AppFileLog.addTrace("RECONNECT FRAGMENT");
    return paramLayoutInflater;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\ReconnectFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
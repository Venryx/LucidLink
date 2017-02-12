package com.resmed.refresh.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;
import com.resmed.refresh.utils.BluetoothDataSerializeUtil;
import com.resmed.refresh.utils.Log;

public class SensorSetupFragment
  extends BaseFragment
  implements BluetoothDataListener
{
  private static final int TIMEOUT_TO_CONNECT = 15000;
  private boolean checkingReconnection = false;
  private CONNECTION_STATE currentState;
  private Button mStartButton;
  
  private void checkReconnection()
  {
    if (this.checkingReconnection) {}
    for (;;)
    {
      return;
      this.checkingReconnection = true;
      Log.d("com.resmed.refresh.pair", "checkReconnection currentState=" + this.currentState);
      if (BluetoothDataSerializeUtil.readJsonFile(getActivity()) != null) {
        new Handler().postDelayed(new Runnable()
        {
          public void run()
          {
            Log.d("com.resmed.refresh.pair", "checkReconnection currentState=" + SensorSetupFragment.this.currentState);
            BaseBluetoothActivity localBaseBluetoothActivity = (BaseBluetoothActivity)SensorSetupFragment.this.getBaseActivity();
            if ((!RefreshApplication.getInstance().getConnectionStatus().isSocketConnected()) && (localBaseBluetoothActivity != null) && (!localBaseBluetoothActivity.isFinishing()) && (localBaseBluetoothActivity.checkBluetoothEnabled(true))) {
              localBaseBluetoothActivity.showBeDPickerDialog();
            }
            SensorSetupFragment.this.checkingReconnection = false;
          }
        }, 15000L);
      }
    }
  }
  
  public void handleBreathingRate(Bundle paramBundle) {}
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    this.currentState = paramCONNECTION_STATE;
    paramCONNECTION_STATE = (BaseBluetoothActivity)getBaseActivity();
    CONNECTION_STATE localCONNECTION_STATE;
    if ((paramCONNECTION_STATE != null) && (!paramCONNECTION_STATE.isFinishing()))
    {
      localCONNECTION_STATE = RefreshApplication.getInstance().getCurrentConnectionState();
      switch (this.currentState)
      {
      default: 
        checkReconnection();
      }
    }
    for (;;)
    {
      return;
      paramCONNECTION_STATE.checkBluetoothEnabled();
      continue;
      if (localCONNECTION_STATE.ordinal() < CONNECTION_STATE.SESSION_OPENED.ordinal()) {
        paramCONNECTION_STATE.sendRpcToBed(BaseBluetoothActivity.getRpcCommands().startRealTimeStream());
      }
    }
  }
  
  public void handleEnvSample(Bundle paramBundle) {}
  
  public void handleReceivedRpc(JsonRPC paramJsonRPC) {}
  
  public void handleSessionRecovered(Bundle paramBundle) {}
  
  public void handleSleepSessionStopped(Bundle paramBundle) {}
  
  public void handleStreamPacket(Bundle paramBundle) {}
  
  public void handleUserSleepState(Bundle paramBundle) {}
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903160, paramViewGroup, false);
    this.mStartButton = ((Button)paramLayoutInflater.findViewById(2131100348));
    this.mStartButton.setTypeface(this.akzidenzLight);
    this.mStartButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        paramAnonymousView = (BaseBluetoothActivity)SensorSetupFragment.this.getBaseActivity();
        if (paramAnonymousView.checkBluetoothEnabled(true))
        {
          SensorSetupFragment.this.currentState = RefreshApplication.getInstance().getCurrentConnectionState();
          paramAnonymousView.showBeDPickerDialog();
        }
      }
    });
    return paramLayoutInflater;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SensorSetupFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
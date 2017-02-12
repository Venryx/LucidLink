package com.resmed.refresh.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.ui.font.GroteskLight;
import com.resmed.refresh.ui.font.GroteskLightButton;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;

public class ManageDataOnBeDFragment
  extends BaseFragment
  implements BluetoothDataListener
{
  private GroteskLightButton greenBtn;
  private CONNECTION_STATE lastState = CONNECTION_STATE.SOCKET_BROKEN;
  private GroteskLight message;
  private LinearLayout progress;
  private GroteskLightButton redBtn;
  private boolean waitingClearData = false;
  
  private void delete()
  {
    final BaseBluetoothActivity localBaseBluetoothActivity = (BaseBluetoothActivity)getActivity();
    if ((localBaseBluetoothActivity != null) && (!this.waitingClearData))
    {
      this.waitingClearData = true;
      JsonRPC localJsonRPC = BaseBluetoothActivity.getRpcCommands().stopNightTimeTracking();
      localJsonRPC.setRPCallback(new JsonRPC.RPCallback()
      {
        public void execute()
        {
          JsonRPC localJsonRPC = BaseBluetoothActivity.getRpcCommands().clearBuffers();
          localBaseBluetoothActivity.sendRpcToBed(localJsonRPC);
        }
        
        public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {}
        
        public void preExecute() {}
      });
      localBaseBluetoothActivity.sendRpcToBed(localJsonRPC);
    }
  }
  
  private void later()
  {
    getBaseActivity().onBackPressed();
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
    this.greenBtn.setText(2131166117);
    this.redBtn.setText(2131166118);
    this.message.setText(2131166116);
  }
  
  private void setupListener()
  {
    this.greenBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        ManageDataOnBeDFragment.this.delete();
      }
    });
    this.redBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        ManageDataOnBeDFragment.this.later();
      }
    });
  }
  
  public void handleBreathingRate(Bundle paramBundle) {}
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    this.lastState = paramCONNECTION_STATE;
    BaseBluetoothActivity localBaseBluetoothActivity = (BaseBluetoothActivity)getActivity();
    if ((localBaseBluetoothActivity == null) || (localBaseBluetoothActivity.isFinishing())) {}
    for (;;)
    {
      return;
      if ((this.waitingClearData) && (paramCONNECTION_STATE == CONNECTION_STATE.SESSION_OPENED))
      {
        localBaseBluetoothActivity.updateDataStoredFlag(0);
        later();
        this.waitingClearData = false;
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
    paramLayoutInflater = paramLayoutInflater.inflate(2130903137, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListener();
    setLabels();
    return paramLayoutInflater;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\ManageDataOnBeDFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
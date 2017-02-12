package com.resmed.refresh.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.ui.activity.ReconnectionProblemActivty;
import com.resmed.refresh.ui.activity.RecoverDataOnBeDActivity;
import com.resmed.refresh.ui.font.GroteskLight;
import com.resmed.refresh.ui.font.GroteskLightButton;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;
import com.resmed.refresh.utils.AppFileLog;
import java.util.Timer;
import java.util.TimerTask;

public class CrashFragment
  extends BaseFragment
  implements BluetoothDataListener
{
  private Timer connectingTimeoutTimer;
  private GroteskLightButton greenBtn;
  private boolean isAbleToResume;
  private GroteskLight message;
  private LinearLayout reconnectProgressLayout;
  private GroteskLightButton redBtn;
  private boolean showBeDDialog = false;
  
  private void mapGUI(View paramView)
  {
    this.greenBtn = ((GroteskLightButton)paramView.findViewById(2131099962));
    this.redBtn = ((GroteskLightButton)paramView.findViewById(2131099963));
    this.message = ((GroteskLight)paramView.findViewById(2131099959));
    this.reconnectProgressLayout = ((LinearLayout)paramView.findViewById(2131099961));
  }
  
  private void setLabels()
  {
    this.greenBtn.setText(2131166112);
    this.redBtn.setText(2131166113);
    this.message.setText(2131166109);
  }
  
  private void setupListeners()
  {
    this.greenBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(final View paramAnonymousView)
      {
        paramAnonymousView = (BaseBluetoothActivity)CrashFragment.this.getBaseActivity();
        if (CrashFragment.this.showBeDDialog) {
          paramAnonymousView.showBeDPickerDialog();
        }
        for (;;)
        {
          if ((paramAnonymousView.checkBluetoothEnabled(true)) && (CrashFragment.this.isAbleToResume))
          {
            CrashFragment.this.connectingTimeoutTimer.cancel();
            CrashFragment.this.getActivity().finish();
            CrashFragment.this.getActivity().overridePendingTransition(2130968586, 2130968588);
          }
          return;
          CrashFragment.this.reconnectProgressLayout.setVisibility(0);
          CrashFragment.this.connectingTimeoutTimer.schedule(new TimerTask()
          {
            public void run()
            {
              paramAnonymousView.runOnUiThread(new Runnable()
              {
                public void run()
                {
                  CrashFragment.this.showBeDDialog = true;
                  CrashFragment.this.reconnectProgressLayout.setVisibility(4);
                  Intent localIntent = new Intent(this.val$baseBluetoothAct, ReconnectionProblemActivty.class);
                  this.val$baseBluetoothAct.startActivity(localIntent);
                  this.val$baseBluetoothAct.overridePendingTransition(2130968597, 2130968586);
                }
              });
            }
          }, 60000L);
        }
      }
    });
    this.redBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        BaseBluetoothActivity localBaseBluetoothActivity = (BaseBluetoothActivity)CrashFragment.this.getBaseActivity();
        CrashFragment.this.connectingTimeoutTimer.cancel();
        paramAnonymousView = new Intent(localBaseBluetoothActivity, RecoverDataOnBeDActivity.class);
        paramAnonymousView.putExtra("com.resmed.refresh.ui.uibase.app.came_from_sleepsession", true);
        CrashFragment.this.startActivity(paramAnonymousView);
        localBaseBluetoothActivity.finish();
      }
    });
  }
  
  public void handleBreathingRate(Bundle paramBundle) {}
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    if (paramCONNECTION_STATE.ordinal() <= CONNECTION_STATE.SESSION_OPENING.ordinal()) {
      this.isAbleToResume = false;
    }
    for (;;)
    {
      return;
      this.isAbleToResume = true;
      this.connectingTimeoutTimer.cancel();
      if (getBaseActivity() != null)
      {
        getActivity().finish();
        getActivity().overridePendingTransition(2130968586, 2130968588);
      }
    }
  }
  
  public void handleEnvSample(Bundle paramBundle) {}
  
  public void handleReceivedRpc(JsonRPC paramJsonRPC) {}
  
  public void handleSessionRecovered(Bundle paramBundle) {}
  
  public void handleSleepSessionStopped(Bundle paramBundle) {}
  
  public void handleStreamPacket(Bundle paramBundle) {}
  
  public void handleUserSleepState(Bundle paramBundle) {}
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903137, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    AppFileLog.addTrace("CRASH FRAGMENT");
    setupListeners();
    setLabels();
    this.connectingTimeoutTimer = new Timer();
    return paramLayoutInflater;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\CrashFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
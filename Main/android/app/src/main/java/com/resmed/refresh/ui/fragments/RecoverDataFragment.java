package com.resmed.refresh.ui.fragments;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.gson.Gson;
import com.resmed.refresh.bluetooth.BeDConnectionStatus;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.ui.activity.HomeActivity;
import com.resmed.refresh.ui.activity.ReconnectionProblemActivty;
import com.resmed.refresh.ui.activity.SleepTimeActivity;
import com.resmed.refresh.ui.font.GroteskLight;
import com.resmed.refresh.ui.font.GroteskLightButton;
import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.Log;
import com.resmed.rm20.SleepParams;
import java.util.Timer;
import java.util.TimerTask;

public class RecoverDataFragment
  extends BaseFragment
  implements BluetoothDataListener
{
  private boolean cameFromSleepSession = false;
  private Timer connectingTimeoutTimer;
  private GroteskLightButton greenBtn;
  private boolean isAbleToResume;
  private GroteskLight message;
  private LinearLayout reconnectProgressLayout;
  private GroteskLightButton redBtn;
  private boolean showBeDDialog = false;
  private RST_SleepSessionInfo uncompletedSession;
  private boolean userClickedSync = false;
  
  private void finish()
  {
    if (this.cameFromSleepSession)
    {
      Intent localIntent = new Intent(getActivity(), HomeActivity.class);
      localIntent.setFlags(268468224);
      startActivity(localIntent);
      getActivity().overridePendingTransition(2130968591, 2130968596);
    }
    for (;;)
    {
      return;
      getBaseActivity().finish();
      getBaseActivity().overridePendingTransition(2130968586, 2130968588);
    }
  }
  
  private void laterSync()
  {
    this.userClickedSync = false;
    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(getBaseActivity().getApplicationContext()).edit();
    localEditor.putInt("PREF_CONNECTION_STATE", CONNECTION_STATE.NIGHT_TRACK_OFF.ordinal());
    localEditor.commit();
    finish();
  }
  
  private void mapGUI(View paramView)
  {
    this.greenBtn = ((GroteskLightButton)paramView.findViewById(2131099962));
    this.redBtn = ((GroteskLightButton)paramView.findViewById(2131099963));
    this.message = ((GroteskLight)paramView.findViewById(2131099959));
    this.reconnectProgressLayout = ((LinearLayout)paramView.findViewById(2131099961));
  }
  
  private void setLabels()
  {
    this.greenBtn.setText(2131166120);
    this.redBtn.setText(2131166121);
    this.message.setText(2131166119);
  }
  
  private void setupListener()
  {
    this.greenBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RecoverDataFragment.this.syncSession();
      }
    });
    this.redBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RecoverDataFragment.this.laterSync();
      }
    });
  }
  
  private void syncLastSession(BaseActivity paramBaseActivity)
  {
    this.userClickedSync = false;
    paramBaseActivity.dismissDialog();
    this.connectingTimeoutTimer.cancel();
    paramBaseActivity = new Intent(getActivity(), SleepTimeActivity.class);
    paramBaseActivity.putExtra("PREF_SYNC_DATA_PROCESS", true);
    paramBaseActivity.setFlags(268468224);
    paramBaseActivity.putExtra("com.resmed.refresh.consts.recovering_ui_thread_from_crash", 0);
    startActivity(paramBaseActivity);
    getActivity().overridePendingTransition(2130968591, 2130968596);
    getBaseActivity().finish();
  }
  
  private void syncSession()
  {
    final BaseBluetoothActivity localBaseBluetoothActivity = (BaseBluetoothActivity)getBaseActivity();
    if (BluetoothAdapter.getDefaultAdapter().isEnabled())
    {
      this.userClickedSync = true;
      if (this.showBeDDialog)
      {
        localBaseBluetoothActivity.showBeDPickerDialog();
        Log.d("com.resmed.refresh.pair", " Bluetooth check");
        boolean bool = BeDConnectionStatus.getInstance().isSocketConnected();
        if ((this.isAbleToResume) && (bool))
        {
          JsonRPC localJsonRPC = BaseBluetoothActivity.getRpcCommands().stopNightTimeTracking();
          localJsonRPC.setRPCallback(new JsonRPC.RPCallback()
          {
            public void execute()
            {
              RecoverDataFragment.this.syncLastSession(localBaseBluetoothActivity);
            }
            
            public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc)
            {
              RecoverDataFragment.this.syncLastSession(localBaseBluetoothActivity);
            }
            
            public void preExecute() {}
          });
          localBaseBluetoothActivity.sendRpcToBed(localJsonRPC);
        }
      }
    }
    for (;;)
    {
      return;
      this.reconnectProgressLayout.setVisibility(0);
      this.connectingTimeoutTimer.schedule(new TimerTask()
      {
        public void run()
        {
          localBaseBluetoothActivity.runOnUiThread(new Runnable()
          {
            public void run()
            {
              RecoverDataFragment.this.showBeDDialog = true;
              RecoverDataFragment.this.reconnectProgressLayout.setVisibility(4);
              Intent localIntent = new Intent(this.val$bActivity, ReconnectionProblemActivty.class);
              this.val$bActivity.startActivity(localIntent);
              this.val$bActivity.overridePendingTransition(2130968597, 2130968586);
            }
          });
        }
      }, 60000L);
      break;
      localBaseBluetoothActivity.checkBluetoothEnabled(true);
    }
  }
  
  public void handleBreathingRate(Bundle paramBundle) {}
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    if (paramCONNECTION_STATE.ordinal() < CONNECTION_STATE.SESSION_OPENING.ordinal()) {
      this.isAbleToResume = false;
    }
    for (;;)
    {
      return;
      this.isAbleToResume = true;
      if (this.userClickedSync)
      {
        this.userClickedSync = false;
        BaseBluetoothActivity localBaseBluetoothActivity = (BaseBluetoothActivity)getBaseActivity();
        if (localBaseBluetoothActivity != null)
        {
          localBaseBluetoothActivity.dismissDialog();
          this.connectingTimeoutTimer.cancel();
          paramCONNECTION_STATE = new Intent(getActivity(), SleepTimeActivity.class);
          paramCONNECTION_STATE.putExtra("PREF_SYNC_DATA_PROCESS", true);
          paramCONNECTION_STATE.setFlags(268468224);
          paramCONNECTION_STATE.putExtra("com.resmed.refresh.consts.recovering_ui_thread_from_crash", 0);
          startActivity(paramCONNECTION_STATE);
          localBaseBluetoothActivity.overridePendingTransition(2130968591, 2130968596);
          localBaseBluetoothActivity.updateDataStoredFlag(0);
          localBaseBluetoothActivity.finish();
        }
      }
    }
  }
  
  public void handleEnvSample(Bundle paramBundle) {}
  
  public void handleReceivedRpc(JsonRPC paramJsonRPC) {}
  
  public void handleSessionRecovered(Bundle paramBundle) {}
  
  public void handleSleepSessionStopped(Bundle paramBundle)
  {
    Log.d("com.resmed.refresh.finish", "handleSleepSessionStopped() ");
    if (paramBundle != null)
    {
      String str = paramBundle.getString("sParamsJson");
      long l1 = paramBundle.getLong("sessionId");
      long l2 = paramBundle.getLong("secondsElapsed");
      Log.d("com.resmed.refresh.finish", "handleSleepSessionStopped() sParamsJson : " + str + " sessionId : " + l1 + " secondsElapsed : " + l2);
      paramBundle = (SleepParams)new Gson().fromJson(str, SleepParams.class);
      if (l2 >= Consts.MIN_SECS_TO_SAVE_RECORD) {
        break label122;
      }
      this.uncompletedSession.delete();
    }
    for (;;)
    {
      return;
      label122:
      if (paramBundle != null)
      {
        this.uncompletedSession.processRM20Data(paramBundle);
        this.uncompletedSession.update();
      }
    }
  }
  
  public void handleStreamPacket(Bundle paramBundle) {}
  
  public void handleUserSleepState(Bundle paramBundle) {}
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    paramBundle = getArguments();
    if (paramBundle != null)
    {
      this.cameFromSleepSession = paramBundle.getBoolean("com.resmed.refresh.ui.uibase.app.came_from_sleepsession");
      AppFileLog.addTrace("RECOVER DATA FRAGMENT");
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903137, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListener();
    setLabels();
    this.cameFromSleepSession = getActivity().getIntent().getBooleanExtra("com.resmed.refresh.ui.uibase.app.came_from_sleepsession", false);
    this.connectingTimeoutTimer = new Timer();
    return paramLayoutInflater;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\RecoverDataFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
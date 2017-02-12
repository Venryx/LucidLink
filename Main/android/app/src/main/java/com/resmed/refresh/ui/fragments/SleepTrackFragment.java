package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings.System;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.sleepsession.SleepSessionConnector;
import com.resmed.refresh.ui.activity.ReconnectActivity;
import com.resmed.refresh.ui.activity.SleepTimeActivity;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;
import com.resmed.refresh.ui.utils.CustomDialogBuilder;
import com.resmed.refresh.ui.utils.RelaxDataManager;
import com.resmed.refresh.ui.utils.SmartAlarmDataManager;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.DefaultRelaxSleep;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.MeasureManager;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.refresh.utils.RelaxSoundManager.UserStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SleepTrackFragment
  extends BaseFragment
  implements BluetoothDataListener, SleepSessionConnector.SleepSessionListener
{
  public static boolean recoverFromCrash;
  private final SimpleDateFormat _sdfHourTime = new SimpleDateFormat("HH");
  private final SimpleDateFormat _sdfMinuteTime = new SimpleDateFormat("mm");
  private Handler audioHandler;
  private BaseBluetoothActivity bAct;
  private BroadcastReceiver batteryPluggedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      SleepTrackFragment.this.isPlugIn = RefreshTools.isPluggedIn(SleepTrackFragment.this.getBaseActivity().getApplicationContext());
      int j = paramAnonymousIntent.getIntExtra("level", -1);
      int i = paramAnonymousIntent.getIntExtra("scale", -1);
      paramAnonymousContext = SleepTrackFragment.this;
      boolean bool;
      if ((j == i) && (j != -1))
      {
        bool = true;
        paramAnonymousContext.fullyCharged = bool;
        Log.e("com.resmed.refresh.sleepFragment", "Battery change fullyCharged:" + SleepTrackFragment.this.fullyCharged + " is plugged:" + SleepTrackFragment.this.isPlugIn);
        if ((SleepTrackFragment.this.isPlugIn) && (!SleepTrackFragment.this.userChangedScreenToOff)) {
          break label246;
        }
        SleepTrackFragment.this.sessionConnector.stopAudioRecording(false);
        SleepTrackFragment.this.sessionConnector.disableAudioRecording();
        if ((SleepTrackFragment.this.startAudioRunnable != null) && (SleepTrackFragment.this.audioHandler != null))
        {
          Log.d("com.resmed.refresh.sleepFragment", " cancelling audio ! ");
          SleepTrackFragment.this.audioHandler.removeCallbacks(SleepTrackFragment.this.startAudioRunnable);
        }
        if ((SleepTrackFragment.this.getActivity() != null) && (!SleepTrackFragment.this.getActivity().isFinishing())) {
          SleepTrackFragment.this.getActivity().getWindow().clearFlags(128);
        }
        Log.d(".ota", " SleepTrackFragment$BroadcastReceiver::onReceiver() battery unplugged !! ");
      }
      for (;;)
      {
        return;
        bool = false;
        break;
        label246:
        if ((SleepTrackFragment.this.getActivity() != null) && (!SleepTrackFragment.this.getActivity().isFinishing())) {
          SleepTrackFragment.this.getActivity().getWindow().addFlags(128);
        }
        Log.d(".ota", " SleepTrackFragment$BroadcastReceiver::onReceiver() battery plugged ");
      }
    }
  };
  private boolean closeSleepSession = false;
  private boolean fullyCharged;
  private int hasAutoRecoveredFromCrash;
  private int initialSettingAutoBrigthness;
  private int initialSettingBrightness;
  private WindowManager.LayoutParams initialWindowBrigthness;
  private boolean isPlugIn;
  private CONNECTION_STATE lastState;
  private LinearLayout llMask;
  private TextView mClockColonText;
  private TextView mClockHourText;
  private TextView mClockMinuteText;
  private TextView mLightLevelValueText;
  private TextView mStatusText;
  private TextView mStopText;
  private TextView mTemperatureValueText;
  private BroadcastReceiver screenOffReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      Log.e("com.resmed.refresh.sleepFragment", "Screen OFF isPlugIn:" + SleepTrackFragment.this.isPlugIn);
      if (SleepTrackFragment.this.isPlugIn) {
        SleepTrackFragment.this.userChangedScreenToOff = true;
      }
    }
  };
  private BroadcastReceiver screenOnReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      Log.e("com.resmed.refresh.sleepFragment", "Screen ON fullyCharged:" + SleepTrackFragment.this.fullyCharged);
      if (!SleepTrackFragment.this.fullyCharged) {
        SleepTrackFragment.this.userChangedScreenToOff = false;
      }
    }
  };
  private SleepSessionConnector sessionConnector;
  private RST_SleepSessionInfo sleepSessionInfo = new RST_SleepSessionInfo();
  private Runnable startAudioRunnable;
  private View.OnClickListener stopListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      SleepTrackFragment.this.dimScreen(false);
      AppFileLog.addTrace("STOP Session pressed by user");
      if (SleepTrackFragment.this.hasToShowDismissNextAlarmDialog()) {
        SleepTrackFragment.this.getBaseActivity().showDialog(new CustomDialogBuilder(SleepTrackFragment.this.getBaseActivity()).title(2131166053).description(2131166080).setPositiveButton(2131165298, new View.OnClickListener()
        {
          public void onClick(View paramAnonymous2View)
          {
            SmartAlarmDataManager.getInstance().dismissNextAlarm();
            SleepTrackFragment.this.sessionConnector.onClickStop();
          }
        }).setNegativeButton(2131165299, new View.OnClickListener()
        {
          public void onClick(View paramAnonymous2View)
          {
            SleepTrackFragment.this.sessionConnector.onClickStop();
          }
        }), false);
      }
      for (;;)
      {
        return;
        SleepTrackFragment.this.sessionConnector.onClickStop();
      }
    }
  };
  private boolean userChangedScreenToOff;
  
  public SleepTrackFragment()
  {
    new ArrayList();
    new ArrayList();
  }
  
  private void dimScreen(boolean paramBoolean)
  {
    WindowManager.LayoutParams localLayoutParams2 = getActivity().getWindow().getAttributes();
    int j = this.initialSettingBrightness;
    int k = this.initialSettingAutoBrigthness;
    WindowManager.LayoutParams localLayoutParams1;
    if (paramBoolean)
    {
      localLayoutParams2.dimAmount = 1.0F;
      int i = j;
      if (j > 50) {
        i = 0;
      }
      if (this.initialWindowBrigthness.screenBrightness > 0.2D) {
        localLayoutParams2.screenBrightness = 0.2F;
      }
      int m = 0;
      Log.d("com.resmed.refresh.sleepFragment", " SleepTrackFragment::dimScreen() settingsBrightness:" + i);
      getActivity().getWindow().addFlags(4194304);
      j = i;
      localLayoutParams1 = localLayoutParams2;
      k = m;
      if (Build.VERSION.SDK_INT <= 16)
      {
        this.llMask.setVisibility(0);
        k = m;
        localLayoutParams1 = localLayoutParams2;
        j = i;
      }
    }
    for (;;)
    {
      Settings.System.putInt(getActivity().getContentResolver(), "screen_brightness_mode", k);
      Settings.System.putInt(getActivity().getContentResolver(), "screen_brightness", j);
      getActivity().getWindow().setAttributes(localLayoutParams1);
      return;
      this.llMask.setVisibility(4);
      localLayoutParams1 = this.initialWindowBrigthness;
      Log.d("com.resmed.refresh.sleepFragment", " SleepTrackFragment::dimScreen() recovering prevoius brigthness:" + this.initialSettingBrightness + " initialWindowBrigthness=" + this.initialWindowBrigthness.screenBrightness);
    }
  }
  
  private void handleEnvData(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    int i = PacketsByteValuesReader.readIlluminanceValue(paramArrayOfByte);
    float f = PacketsByteValuesReader.readTemperatureValue(paramArrayOfByte);
    this.mLightLevelValueText.setText(i + " Lux");
    if (!RefreshModelController.getInstance().getUseMetricUnits()) {}
    for (paramArrayOfByte = Math.round(MeasureManager.convertCelsiusToFahrenheit(f)) + " " + getString(2131165308);; paramArrayOfByte = Math.round(f) + " " + getString(2131165307))
    {
      this.mTemperatureValueText.setText(paramArrayOfByte);
      return;
    }
  }
  
  private void handleHeartBeat(byte[] paramArrayOfByte) {}
  
  private boolean hasToShowDismissNextAlarmDialog()
  {
    long l = SmartAlarmDataManager.getInstance().getAlarmDateTime() - new Date().getTime();
    Log.d("com.resmed.refresh.smartAlarm", "now is " + new Date() + " and the alarm is at " + new Date(SmartAlarmDataManager.getInstance().getAlarmDateTime()) + " ActiveAlarm=" + SmartAlarmDataManager.getInstance().getActiveAlarm());
    Log.d("com.resmed.refresh.smartAlarm", "timeToWakeUp = " + l + " ActiveAlarm=" + SmartAlarmDataManager.getInstance().getActiveAlarm());
    AppFileLog.addTrace("SmartAlarm timeToWakeUp = " + l + " ActiveAlarm=" + SmartAlarmDataManager.getInstance().getActiveAlarm());
    if ((SmartAlarmDataManager.getInstance().getActiveAlarm()) && (l > 0L) && (l < SmartAlarmDataManager.TIME_TO_TURN_OFF.intValue())) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  private void showConnectionProblemOnStopSession()
  {
    Intent localIntent = new Intent(getActivity(), ReconnectActivity.class);
    localIntent.putExtra("com.resmed.refresh.ui.uibase.app.sleep_history_id", this.sleepSessionInfo.getId());
    startActivityForResult(localIntent, 1);
  }
  
  public void handleBreathingRate(Bundle paramBundle)
  {
    Log.i("RM20StartMethod", "handleBreathingRate() SleepTrackFragment");
    this.sessionConnector.handleBreathingRate(paramBundle);
  }
  
  public void handleClockTick()
  {
    try
    {
      TextView localTextView = this.mClockHourText;
      SimpleDateFormat localSimpleDateFormat = this._sdfHourTime;
      Date localDate = new java/util/Date;
      localDate.<init>();
      localTextView.setText(localSimpleDateFormat.format(localDate));
      localTextView = this.mClockMinuteText;
      localSimpleDateFormat = this._sdfMinuteTime;
      localDate = new java/util/Date;
      localDate.<init>();
      localTextView.setText(localSimpleDateFormat.format(localDate));
      return;
    }
    catch (NullPointerException localNullPointerException)
    {
      for (;;)
      {
        localNullPointerException.printStackTrace();
      }
    }
  }
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    if (this.sessionConnector != null) {
      this.sessionConnector.handleConnectionStatus(paramCONNECTION_STATE);
    }
    Object localObject = getView();
    if (localObject == null) {}
    for (;;)
    {
      return;
      ImageView localImageView = (ImageView)((View)localObject).findViewById(2131100400);
      localImageView.setAnimation(null);
      localObject = new AlphaAnimation(0.0F, 1.0F);
      ((Animation)localObject).setDuration(500L);
      ((Animation)localObject).setStartOffset(20L);
      ((Animation)localObject).setRepeatMode(2);
      ((Animation)localObject).setRepeatCount(-1);
      switch (paramCONNECTION_STATE)
      {
      case SESSION_OPENING: 
      case SOCKET_NOT_CONNECTED: 
      default: 
        break;
      case REAL_STREAM_OFF: 
        localImageView.setImageResource(2130837910);
        this.mStopText.setBackgroundResource(2130837879);
        break;
      case SOCKET_BROKEN: 
        localImageView.setImageResource(2130837904);
        localImageView.startAnimation((Animation)localObject);
        break;
      case REAL_STREAM_ON: 
        localImageView.setImageResource(2130837910);
        this.mStopText.setBackgroundResource(2130837879);
        localImageView.startAnimation((Animation)localObject);
        onSessionOk();
        break;
      case SOCKET_RECONNECTING: 
        localImageView.setImageResource(2130837904);
        this.mStopText.setBackgroundResource(2130837877);
        if (recoverFromCrash)
        {
          System.out.println("***************backtouserrecoverFromCrash=> " + recoverFromCrash);
          paramCONNECTION_STATE = RefreshApplication.getInstance().getCurrentConnectionState();
          localObject = (BaseBluetoothActivity)getBaseActivity();
          if ((paramCONNECTION_STATE != null) && (localObject != null)) {
            ((BaseBluetoothActivity)localObject).sendRpcToBed(BaseBluetoothActivity.getRpcCommands().stopRealTimeStream());
          }
          recoverFromCrash = false;
        }
        break;
      case SOCKET_CONNECTED: 
        localImageView.setImageResource(2130837904);
        break;
      case SESSION_OPENED: 
        onSessionOk();
        localImageView.setImageResource(2130837910);
        this.mStopText.setBackgroundResource(2130837879);
      }
    }
  }
  
  public void handleEnvSample(Bundle paramBundle)
  {
    this.sessionConnector.handleEnvSample(paramBundle);
  }
  
  public void handleReceivedRpc(JsonRPC paramJsonRPC)
  {
    if (this.sessionConnector != null) {
      this.sessionConnector.handleReceivedRpc(paramJsonRPC);
    }
  }
  
  public void handleSessionRecovered(Bundle paramBundle)
  {
    Log.d("com.resmed.refresh.ui", " SleepTrackFragment::handleSessionRecovered()");
    if (this.sessionConnector != null) {
      this.sessionConnector.handleSessionRecovered(paramBundle);
    }
  }
  
  public void handleSleepSessionStopped(Bundle paramBundle)
  {
    Log.d("com.resmed.refresh.finish", "handleSleepSessionStopped() ");
    AppFileLog.addTrace("STOP handleSleepSessionStopped ");
    this.sessionConnector.handleSleepSessionStopped(paramBundle);
  }
  
  public void handleStreamPacket(Bundle paramBundle)
  {
    if (this.sessionConnector != null) {
      this.sessionConnector.handleStreamPacket(paramBundle);
    }
    byte[] arrayOfByte = paramBundle.getByteArray("REFRESH_BED_NEW_DATA");
    int i = paramBundle.getByte("REFRESH_BED_NEW_DATA_TYPE");
    paramBundle.getInt("REFRESH_BED_NEW_DATA_SIZE");
    Log.d("com.resmed.refresh.ui", " SleepTrackFragment handleStreamPacket decobbed : " + Arrays.toString(arrayOfByte) + " packetType : " + i);
    if (VLPacketType.PACKET_TYPE_NOTE_ILLUMINANCE_CHANGE.ordinal() == i) {
      handleEnvData(arrayOfByte, false);
    }
    for (;;)
    {
      return;
      if (VLPacketType.PACKET_TYPE_NOTE_HEARTBEAT.ordinal() == i) {
        handleHeartBeat(arrayOfByte);
      } else if (VLPacketType.PACKET_TYPE_ENV_1.ordinal() == i) {
        handleEnvData(arrayOfByte, true);
      } else if (VLPacketType.PACKET_TYPE_ENV_60.ordinal() == i) {
        handleEnvData(arrayOfByte, true);
      }
    }
  }
  
  public void handleUserSleepState(Bundle paramBundle)
  {
    Log.i("RM20StartMethod", "handleUserSleepState() SleepTrackFragment");
    Log.i("com.resmed.refresh.relax", "handleUserSleepState() SleepTrackFragment " + paramBundle);
    this.sessionConnector.handleUserSleepState(paramBundle);
  }
  
  public boolean isUserAwake()
  {
    return false;
  }
  
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 == 1)
    {
      if (this.lastState != CONNECTION_STATE.NIGHT_TRACK_ON) {
        break label30;
      }
      this.sessionConnector.stopSleepSession();
    }
    for (;;)
    {
      return;
      label30:
      this.closeSleepSession = true;
      new Handler().postDelayed(new Runnable()
      {
        public void run()
        {
          if (SleepTrackFragment.this.closeSleepSession) {
            SleepTrackFragment.this.getBaseActivity().showDialog(new CustomDialogBuilder(SleepTrackFragment.this.getBaseActivity()).title(2131165301).description(2131166114).setPositiveButton(2131165296, new View.OnClickListener()
            {
              public void onClick(View paramAnonymous2View)
              {
                SleepTrackFragment.this.showConnectionProblemOnStopSession();
              }
            }), false);
          }
        }
      }, 10000L);
    }
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    this.bAct = ((BaseBluetoothActivity)paramActivity);
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903170, paramViewGroup, false);
    paramViewGroup = getArguments();
    boolean bool = false;
    this.hasAutoRecoveredFromCrash = -1;
    int i = 0;
    if (paramViewGroup != null)
    {
      bool = paramViewGroup.getBoolean("shouldSync");
      i = paramViewGroup.getInt("bioOnBeD");
      this.hasAutoRecoveredFromCrash = paramViewGroup.getInt("hasRecoveredFromCrash");
    }
    this.initialWindowBrigthness = getActivity().getWindow().getAttributes();
    try
    {
      this.initialSettingBrightness = Settings.System.getInt(getActivity().getContentResolver(), "screen_brightness");
      this.initialSettingAutoBrigthness = Settings.System.getInt(getActivity().getContentResolver(), "screen_brightness_mode");
      Log.d("com.resmed.refresh.sleepFragment", " SleepTrackFragment::initialBrightness : " + this.initialWindowBrigthness + " Window screenBrightness=" + this.initialWindowBrigthness.screenBrightness);
      Log.d("com.resmed.refresh.sleepFragment", " SleepTrackFragment::onCreateView, isSyncing : " + bool + " bioOnBeD : " + i);
      this.sessionConnector = new SleepSessionConnector(this.bAct, i, bool, this);
      this.sessionConnector.setHasAutoRecoveredFromCrash(this.hasAutoRecoveredFromCrash);
      if (bool)
      {
        paramLayoutInflater.setVisibility(4);
        RelaxDataManager.getInstance().setActiveRelax(false);
      }
      this.mLightLevelValueText = ((TextView)paramLayoutInflater.findViewById(2131100402));
      this.mTemperatureValueText = ((TextView)paramLayoutInflater.findViewById(2131100403));
      this.mClockHourText = ((TextView)paramLayoutInflater.findViewById(2131100397));
      this.mClockMinuteText = ((TextView)paramLayoutInflater.findViewById(2131100399));
      this.mClockColonText = ((TextView)paramLayoutInflater.findViewById(2131100398));
      this.mStopText = ((TextView)paramLayoutInflater.findViewById(2131100405));
      this.mStatusText = ((TextView)paramLayoutInflater.findViewById(2131100406));
      this.llMask = ((LinearLayout)paramLayoutInflater.findViewById(2131100407));
      this.mStopText.setOnClickListener(this.stopListener);
      this.mStopText.setVisibility(4);
      this.mStatusText.setText(2131165878);
      this.mStatusText.setVisibility(0);
      paramBundle = RelaxDataManager.getInstance();
      FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
      SleepButtonFragment localSleepButtonFragment = new SleepButtonFragment();
      paramViewGroup = new Bundle();
      if ((paramBundle.getActiveRelax()) && (RefreshModelController.getInstance().getPlayAutoRelax()))
      {
        bool = true;
        paramViewGroup.putBoolean("displayRelax", bool);
        paramViewGroup.putBoolean("sleepTrackFragment", true);
        localSleepButtonFragment.setArguments(paramViewGroup);
        localFragmentTransaction.add(2131100404, localSleepButtonFragment, "SleepButtonFragmentTrack");
        localFragmentTransaction.commit();
        getBaseActivity().registerReceiver(this.batteryPluggedReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        getBaseActivity().registerReceiver(this.screenOnReceiver, new IntentFilter("android.intent.action.SCREEN_ON"));
        getBaseActivity().registerReceiver(this.screenOffReceiver, new IntentFilter("android.intent.action.SCREEN_OFF"));
        return paramLayoutInflater;
      }
    }
    catch (Settings.SettingNotFoundException paramViewGroup)
    {
      for (;;)
      {
        this.initialSettingBrightness = 128;
        paramViewGroup.printStackTrace();
        continue;
        bool = false;
      }
    }
  }
  
  public void onDestroy()
  {
    super.onDestroy();
    try
    {
      getActivity().unregisterReceiver(this.batteryPluggedReceiver);
      getActivity().unregisterReceiver(this.screenOnReceiver);
      getActivity().unregisterReceiver(this.screenOffReceiver);
      stopRelax(false);
      BaseBluetoothActivity.IN_SLEEP_SESSION = false;
      getBaseActivity().finish();
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      for (;;) {}
    }
  }
  
  public void onPause()
  {
    super.onPause();
    dimScreen(false);
    if (this.sessionConnector != null) {
      this.sessionConnector.stopAudioRecording(false);
    }
  }
  
  public void onResume()
  {
    super.onResume();
    dimScreen(true);
    BaseBluetoothActivity localBaseBluetoothActivity = (BaseBluetoothActivity)getBaseActivity();
    if ((localBaseBluetoothActivity != null) && (!localBaseBluetoothActivity.isBoundToBluetoothService()))
    {
      AppFileLog.addTrace("BINDING TO SERVICE");
      localBaseBluetoothActivity.bindToService();
    }
    if (this.sessionConnector != null) {
      this.sessionConnector.resume();
    }
    Log.d("com.resmed.refresh.sleepFragment", " SleepTrackFragment::onResume() is relax active : " + RelaxDataManager.getInstance().getActiveRelax() + " hasRecoveredFromCrash :" + this.hasAutoRecoveredFromCrash);
    if ((this.hasAutoRecoveredFromCrash != 1) && (RefreshModelController.getInstance().getPlayAutoRelax()))
    {
      setEnableRelaxButton(false);
      new Handler().postDelayed(new Runnable()
      {
        public void run()
        {
          SleepTrackFragment.this.setEnableRelaxButton(true);
          SleepTrackFragment.this.startRelax(false);
        }
      }, 500L);
    }
    for (;;)
    {
      return;
      setEnableRelaxButton(true);
    }
  }
  
  public void onSessionOk()
  {
    if ((getActivity() == null) || (!getBaseActivity().isActivityReadyToCommit())) {}
    for (;;)
    {
      return;
      this.mStopText.setEnabled(true);
      this.mStopText.setVisibility(0);
      this.mStopText.setText(getString(2131166093));
      this.mStatusText.setVisibility(4);
    }
  }
  
  public void onSessionRecovering()
  {
    this.mStopText.setEnabled(false);
    this.mStopText.setVisibility(4);
    this.mStopText.setText(getString(2131165877));
    this.mStatusText.setVisibility(0);
  }
  
  public void onStart()
  {
    super.onStart();
    this.audioHandler = new Handler();
    this.startAudioRunnable = new Runnable()
    {
      public void run()
      {
        Log.d("com.resmed.refresh.sleepFragment", " AudioDefaultRecorder starting audio ! bAct : " + SleepTrackFragment.this.bAct + " bAct.isFinishing() : " + SleepTrackFragment.this.bAct.isFinishing());
        if ((SleepTrackFragment.this.bAct != null) && (!SleepTrackFragment.this.bAct.isFinishing()) && (SleepTrackFragment.this.sessionConnector != null))
        {
          boolean bool = RefreshTools.isPluggedIn(SleepTrackFragment.this.bAct.getApplicationContext());
          Log.d("com.resmed.refresh.sleepFragment", " AudioDefaultRecorder isPlugged : " + bool);
          if (!bool) {
            break label157;
          }
          SleepTrackFragment.this.sessionConnector.enableAudioRecording();
          SleepTrackFragment.this.sessionConnector.startAudioRecording(false);
          SleepTrackFragment.this.getActivity().getWindow().addFlags(128);
        }
        for (;;)
        {
          return;
          label157:
          SleepTrackFragment.this.sessionConnector.disableAudioRecording();
          SleepTrackFragment.this.getActivity().getWindow().clearFlags(128);
        }
      }
    };
    this.audioHandler.postDelayed(this.startAudioRunnable, 600000L);
  }
  
  public void onStop()
  {
    super.onStop();
    if ((this.startAudioRunnable != null) && (this.audioHandler != null))
    {
      Log.d("com.resmed.refresh.sleepFragment", " cancelling audio ! ");
      this.audioHandler.removeCallbacks(this.startAudioRunnable);
    }
  }
  
  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    this.lastState = RefreshApplication.getInstance().getCurrentConnectionState();
    paramView = PreferenceManager.getDefaultSharedPreferences(getBaseActivity().getApplicationContext());
    int i = paramView.getInt("PREF_CONNECTION_STATE", -1);
    long l = paramView.getLong("PREF_NIGHT_LAST_SESSION_ID", -1L);
    AppFileLog.addTrace("SLEEPTRACKFRAGMENT recovering state " + i + " for session id : " + l);
    boolean bool = getActivity().getIntent().getBooleanExtra("com.resmed.refresh.consts.user_press_back_to_sleep_tag", false);
    this.sessionConnector.init(bool);
    if (this.sessionConnector.isRecovering()) {
      this.mStatusText.setText(2131165877);
    }
    handleClockTick();
    paramView = new AlphaAnimation(0.0F, 1.0F);
    paramView.setDuration(500L);
    paramView.setStartOffset(20L);
    paramView.setRepeatMode(2);
    paramView.setRepeatCount(-1);
    this.mClockColonText.startAnimation(paramView);
    BaseBluetoothActivity.IN_SLEEP_SESSION = true;
  }
  
  public void recoverSessionId(long paramLong)
  {
    Log.d("com.resmed.refresh.sleepFragment", "SleepTrackFragment::recoverSessionId, sessionId : " + paramLong);
    this.sessionConnector.recoverSessionId(paramLong);
  }
  
  public void setEnableRelaxButton(boolean paramBoolean)
  {
    SleepTimeActivity localSleepTimeActivity = (SleepTimeActivity)getActivity();
    if (localSleepTimeActivity != null) {
      localSleepTimeActivity.setEnableRelaxButton(paramBoolean);
    }
  }
  
  public void setUserBreathRate(double paramDouble)
  {
    Object localObject = (SleepTimeActivity)getActivity();
    if (localObject != null)
    {
      localObject = ((SleepTimeActivity)localObject).getDefaultRelaxSleep();
      if (localObject != null) {
        ((DefaultRelaxSleep)localObject).setBreathingRate(paramDouble);
      }
    }
  }
  
  public void setUserStatus(RelaxSoundManager.UserStatus paramUserStatus)
  {
    Object localObject = (SleepTimeActivity)getActivity();
    if (localObject != null)
    {
      localObject = ((SleepTimeActivity)localObject).getDefaultRelaxSleep();
      if (localObject != null) {
        ((DefaultRelaxSleep)localObject).notifyUserStatus(paramUserStatus);
      }
    }
  }
  
  public void startRelax(boolean paramBoolean)
  {
    SleepTimeActivity localSleepTimeActivity = (SleepTimeActivity)getActivity();
    if (localSleepTimeActivity != null) {
      localSleepTimeActivity.startRelax(paramBoolean);
    }
  }
  
  public void stopRelax(boolean paramBoolean)
  {
    SleepTimeActivity localSleepTimeActivity = (SleepTimeActivity)getActivity();
    if (localSleepTimeActivity != null) {
      localSleepTimeActivity.stopRelax(paramBoolean);
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SleepTrackFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
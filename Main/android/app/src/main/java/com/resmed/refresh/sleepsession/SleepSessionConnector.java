package com.resmed.refresh.sleepsession;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;

import com.google.gson.Gson;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.RST_SleepEvent;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RST_ValueItem;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.ui.activity.CrashSleepSessionActivity;
import com.resmed.refresh.ui.activity.HomeActivity;
import com.resmed.refresh.ui.activity.ReconnectActivity;
import com.resmed.refresh.ui.activity.RecoverDataOnBeDActivity;
import com.resmed.refresh.ui.activity.SleepTimeActivity;
import com.resmed.refresh.ui.fragments.SleepTrackFragment;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.ui.utils.CustomDialogBuilder;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.KillableRunnable;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.refresh.utils.SortedList;
import com.resmed.refresh.utils.audio.AudioDefaultRecorder;
import com.resmed.refresh.utils.preSleepLog;
import com.resmed.rm20.SleepParams;
import de.greenrobot.dao.DaoException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class SleepSessionConnector
  implements BluetoothDataListener, AudioDefaultRecorder.AudioSampleReceiver
{
  private static final int ALARM_REQUEST_CODE = 5123;
  private static final int MAX_SOUND_EVENTS = 5;
  private AudioDefaultRecorder audioRecorder;
  private BaseBluetoothActivity bAct;
  private int bioCurrentTotalCount;
  private int bioOnBeD;
  private KillableRunnable cancelSleepSessionRunnable;
  private boolean closeSleepSession;
  private int countTimeout;
  private int envTotalCount;
  private int hasAutoRecoveredFromCrash;
  private boolean hasSessionStarted;
  private Runnable heartbeatTimeoutRunnable;
  private boolean isAudioActive = false;
  private boolean isClosingSession;
  private boolean isHandlingHeartBeat;
  private boolean isRecovering = false;
  private boolean isSyncAndStop;
  private boolean isWaitLastSamples;
  private Long lastHeartBeatTimestamp;
  private CONNECTION_STATE lastState;
  private List<RST_ValueItem> lightValues;
  private Handler myHeartbeatHandler;
  private JsonRPC pendingBioSamplesRpc;
  private JsonRPC pendingEnvSamplesRpc;
  private ProgressThread processRecord;
  private BroadcastReceiver reconnectReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = paramAnonymousIntent.getExtras();
      if (paramAnonymousContext == null) {}
      for (;;)
      {
        return;
        int i = paramAnonymousContext.getInt("BUNDLE_LAST_CONN_STATE");
        if ((SleepSessionConnector.this.isClosingSession) && (CONNECTION_STATE.NIGHT_TRACK_OFF.ordinal() == i) && (!SleepSessionConnector.this.isSyncAndStop))
        {
          if (SleepSessionConnector.this.processRecord != null) {
            SleepSessionConnector.this.processRecord.interrupt();
          }
          SleepSessionConnector.this.bAct.showDialog(new CustomDialogBuilder(SleepSessionConnector.this.bAct).title(2131165867).description(2131165868).setPositiveButton(2131165296, new View.OnClickListener()
          {
            public void onClick(View paramAnonymous2View)
            {
              paramAnonymous2View = new Intent(SleepSessionConnector.this.bAct, HomeActivity.class);
              paramAnonymous2View.setFlags(268468224);
              SleepSessionConnector.this.bAct.startActivity(paramAnonymous2View);
              SleepSessionConnector.this.bAct.overridePendingTransition(2130968591, 2130968596);
            }
          }), false);
        }
      }
    }
  };
  private int sizeToStore = 5;
  private RST_SleepSessionInfo sleepSessionInfo;
  private SleepSessionListener sleepSessionListener;
  private SortedList<Integer> soundTopAmplitudes;
  private List<RST_ValueItem> tempValues;
  private TimeOutThread timeoutThread;
  
  public SleepSessionConnector(BaseBluetoothActivity paramBaseBluetoothActivity, int paramInt, boolean paramBoolean, SleepSessionListener paramSleepSessionListener)
  {
    this.isSyncAndStop = paramBoolean;
    this.bAct = paramBaseBluetoothActivity;
    this.bioOnBeD = paramInt;
    this.isClosingSession = false;
    this.hasSessionStarted = false;
    this.sleepSessionListener = paramSleepSessionListener;
    this.sleepSessionInfo = new RST_SleepSessionInfo();
    this.lightValues = new ArrayList();
    this.tempValues = new ArrayList();
    this.processRecord = new ProgressThread();
    this.myHeartbeatHandler = new Handler();
  }
  
  public static void CancelRepeatingAlarmWake(Context paramContext)
  {
    Log.d("com.resmed.refresh.bluetooth.alarm", " SleepSessionConnector$AlarmReceiver::cancelRepeatingAlarmWake() ");
    if (paramContext != null)
    {
      PendingIntent localPendingIntent = PendingIntent.getBroadcast(paramContext, 5123, new Intent(paramContext, AlarmReceiver.class), 0);
      ((AlarmManager)paramContext.getSystemService("alarm")).cancel(localPendingIntent);
    }
  }
  
  private static void RegisterRepeatingAlarmWake(Context paramContext)
  {
    Log.d("com.resmed.refresh.bluetooth.alarm", " SleepSessionConnector$AlarmReceiver::registerRepeatingAlarmWake() ");
    if (paramContext != null)
    {
      AlarmManager localAlarmManager = (AlarmManager)paramContext.getSystemService("alarm");
      paramContext = PendingIntent.getBroadcast(paramContext, 5123, new Intent(paramContext, AlarmReceiver.class), 0);
      localAlarmManager.set(2, SystemClock.elapsedRealtime() + 900000L, paramContext);
    }
  }
  
  private void checkReceivingHeartBeat(final int paramInt1, int paramInt2)
  {
    if (paramInt2 < 5000) {
      paramInt2 = 15000;
    }
    for (;;)
    {
      Log.d("com.resmed.refresh.sleepFragment", "Heartbeat time to be completed : " + paramInt2);
      AppFileLog.addTrace("Heartbeat time to be completed : " + paramInt2);
      this.heartbeatTimeoutRunnable = new Runnable()
      {
        public void run()
        {
          AppFileLog.addTrace("Checking HeartBeat lastBioCount=" + paramInt1 + " bioTotalCount=" + SleepSessionConnector.this.bioCurrentTotalCount);
          if ((SleepSessionConnector.this.bAct != null) && (!SleepSessionConnector.this.isWaitLastSamples) && (SleepSessionConnector.this.bioCurrentTotalCount == paramInt1))
          {
            AppFileLog.addTrace("Timeout waiting for HeartBeat => Binding the service");
            SleepSessionConnector.this.bAct.bindToService();
            SleepSessionConnector.this.pendingBioSamplesRpc = null;
            SleepSessionConnector.this.pendingEnvSamplesRpc = null;
            SleepSessionConnector.this.isHandlingHeartBeat = false;
          }
        }
      };
      this.myHeartbeatHandler.postDelayed(this.heartbeatTimeoutRunnable, paramInt2);
      return;
      if (paramInt2 < 50000) {
        paramInt2 = 100000;
      } else if (paramInt2 < 500000) {
        paramInt2 = 400000;
      } else {
        paramInt2 = 900000;
      }
    }
  }
  
  private void closeSession()
  {
    AppFileLog.addTrace("CLOSING SLEEP SESSION, isHandlingHeartBeat :" + this.isHandlingHeartBeat);
    long l = 0L;
    if (this.lastHeartBeatTimestamp != null) {
      l = System.currentTimeMillis() - this.lastHeartBeatTimestamp.longValue();
    }
    if ((this.lastHeartBeatTimestamp == null) || (l > 80000L))
    {
      AppFileLog.addTrace("hearbeat timeout! lastHeartBeatTimestamp : " + this.lastHeartBeatTimestamp);
      Object localObject = new Message();
      ((Message)localObject).what = 7;
      this.bAct.sendMsgBluetoothService((Message)localObject);
      localObject = new Intent(this.bAct, RecoverDataOnBeDActivity.class);
      ((Intent)localObject).setFlags(268468224);
      ((Intent)localObject).putExtra("com.resmed.refresh.ui.uibase.app.came_from_sleepsession", true);
      this.bAct.startActivity((Intent)localObject);
      this.bAct.overridePendingTransition(2130968582, 2130968583);
    }
    for (;;)
    {
      return;
      if ((this.lastState == CONNECTION_STATE.SOCKET_NOT_CONNECTED) || (this.lastState == CONNECTION_STATE.BLUETOOTH_OFF) || (this.lastState == CONNECTION_STATE.SOCKET_BROKEN) || (this.lastState == CONNECTION_STATE.SOCKET_RECONNECTING)) {
        showConnectionProblemOnStopSession();
      } else {
        stopSleepSession();
      }
    }
  }
  
  private int decompressLight(int paramInt)
  {
    if (paramInt < 64) {}
    for (;;)
    {
      return paramInt;
      if (paramInt < 96) {
        paramInt = (paramInt - 64) * 2 + 64;
      } else if (paramInt < 128) {
        paramInt = (paramInt - 96) * 4 + 128;
      } else if (paramInt < 160) {
        paramInt = (paramInt - 128) * 8 + 256;
      } else if (paramInt < 192) {
        paramInt = (paramInt - 160) * 16 + 512;
      } else if (paramInt < 240) {
        paramInt = (paramInt - 192) * 64 + 1024;
      } else if (paramInt < 255) {
        paramInt = (paramInt - 240) * 128 + 4096;
      } else {
        paramInt = 6000;
      }
    }
  }
  
  private void handleEnvData(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    this.isHandlingHeartBeat = false;
  }
  
  private void handleHeartBeat(byte[] paramArrayOfByte)
  {
    if (this.bAct == null) {}
    for (;;)
    {
      return;
      Log.d("com.resmed.refresh.sleepFragment", "IN HeartBeat ignored beacuse : isWaitLastSamples=" + this.isWaitLastSamples + "  ||  isHandlingHeartBeat=" + this.isHandlingHeartBeat);
      this.lastHeartBeatTimestamp = Long.valueOf(System.currentTimeMillis());
      SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this.bAct.getApplicationContext()).edit();
      localEditor.putLong("PREF_NIGHT_LAST_TIMESTAMP_ID", this.lastHeartBeatTimestamp.longValue());
      localEditor.commit();
      if ((this.isWaitLastSamples) || (this.isHandlingHeartBeat))
      {
        AppFileLog.addTrace("IN HeartBeat ignored beacuse : isWaitLastSamples=" + this.isWaitLastSamples + "  ||  isHandlingHeartBeat=" + this.isHandlingHeartBeat);
      }
      else
      {
        this.isHandlingHeartBeat = true;
        int i = PacketsByteValuesReader.getStoreLocalBio(paramArrayOfByte);
        int j = PacketsByteValuesReader.getStoreLocalEnv(paramArrayOfByte);
        setBioOnBeD(i);
        this.bioCurrentTotalCount += this.bioOnBeD;
        checkReceivingHeartBeat(this.bioCurrentTotalCount, i);
        AppFileLog.addTrace("");
        AppFileLog.addTrace("IN : HeartBeat Pending BIO : " + i + " ENV : " + j + "\tbioTotalCount=" + this.bioCurrentTotalCount);
        requestSamples(i, j);
        if ((this.audioRecorder != null) && (this.audioRecorder.isRecording())) {
          startAudioRecording(false);
        }
      }
    }
  }
  
  private void handleSamplesTransmissionCompleteForRPC(JsonRPC paramJsonRPC)
  {
    Object localObject = new StringBuilder("pendingBio is ");
    StringBuilder localStringBuilder;
    if (this.pendingBioSamplesRpc == null)
    {
      str = "null";
      localObject = str;
      localStringBuilder = new StringBuilder("pendingEnv is ");
      if (this.pendingEnvSamplesRpc != null) {
        break label406;
      }
    }
    label406:
    for (String str = "null";; str = "NOT null")
    {
      str = str;
      AppFileLog.addTrace("IN : " + paramJsonRPC.getId() + "  Tx completed " + (String)localObject + "  " + str + " isWaitLastSamples=" + this.isWaitLastSamples);
      if ((this.pendingBioSamplesRpc != null) && (this.pendingBioSamplesRpc.getId() == paramJsonRPC.getId()))
      {
        if ((this.pendingEnvSamplesRpc != null) && (this.bAct != null))
        {
          AppFileLog.addTrace("IN=>OUT : Requesting ENV ID : " + this.pendingEnvSamplesRpc.getId());
          this.bAct.sendRpcToBed(this.pendingEnvSamplesRpc);
        }
        this.pendingBioSamplesRpc = null;
      }
      if ((this.pendingEnvSamplesRpc != null) && (this.pendingEnvSamplesRpc.getId() == paramJsonRPC.getId()))
      {
        this.pendingEnvSamplesRpc = null;
        this.isHandlingHeartBeat = false;
        AppFileLog.addTrace("");
        this.myHeartbeatHandler.removeCallbacks(this.heartbeatTimeoutRunnable);
        if (this.isRecovering) {
          this.isRecovering = false;
        }
        this.lastHeartBeatTimestamp = Long.valueOf(System.currentTimeMillis());
        this.sleepSessionListener.onSessionOk();
        if (this.isClosingSession)
        {
          this.isWaitLastSamples = true;
          this.bAct.updateDataStoredFlag(0);
        }
      }
      if ((this.isWaitLastSamples) && (this.pendingBioSamplesRpc == null) && (this.pendingEnvSamplesRpc == null) && (this.isClosingSession))
      {
        paramJsonRPC = BaseBluetoothActivity.getRpcCommands().stopNightTimeTracking();
        paramJsonRPC.setRPCallback(new JsonRPC.RPCallback()
        {
          public void execute()
          {
            if ((SleepSessionConnector.this.bAct != null) && (!SleepSessionConnector.this.bAct.isFinishing()))
            {
              JsonRPC localJsonRPC = BaseBluetoothActivity.getRpcCommands().clearBuffers();
              SleepSessionConnector.this.bAct.sendRpcToBed(localJsonRPC);
            }
          }
          
          public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {}
          
          public void preExecute() {}
        });
        AppFileLog.addTrace(" SleepSessionConnector last samples! -> stop & clear");
        this.bAct.sendRpcToBed(paramJsonRPC);
        paramJsonRPC = new Message();
        paramJsonRPC.what = 14;
        if (this.bAct != null)
        {
          this.bAct.sendMsgBluetoothService(paramJsonRPC);
          Log.d("com.resmed.refresh.finish", "MSG_SLEEP_SESSION_STOP BaseBluetoothActivty");
        }
      }
      return;
      str = "NOT null";
      break;
    }
  }
  
  private void registerForSessionTimeout(final Handler paramHandler)
  {
    Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::registerForSessionTimeout");
    if (this.cancelSleepSessionRunnable == null)
    {
      Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::registerForSessionTimeout #1");
      this.cancelSleepSessionRunnable = new KillableRunnable(new KillableRunnable.KillableRunner()
      {
        public void executeOnRun()
        {
          Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::registerForSessionTimeout hasSessionStarted : " + SleepSessionConnector.this.hasSessionStarted);
          if ((SleepSessionConnector.this.bAct != null) && (!SleepSessionConnector.this.hasSessionStarted))
          {
            paramHandler.removeCallbacks(SleepSessionConnector.this.cancelSleepSessionRunnable);
            SleepSessionConnector.this.bAct.showDialog(new CustomDialogBuilder(SleepSessionConnector.this.bAct).title(2131165869).description(2131165870).setPositiveButton(2131165296, new View.OnClickListener()
            {
              public void onClick(View paramAnonymous2View)
              {
                paramAnonymous2View = PreferenceManager.getDefaultSharedPreferences(SleepSessionConnector.this.bAct.getApplicationContext()).edit();
                paramAnonymous2View.putInt("PREF_CONNECTION_STATE", CONNECTION_STATE.NIGHT_TRACK_OFF.ordinal());
                paramAnonymous2View.commit();
                paramAnonymous2View = new Intent(SleepSessionConnector.this.bAct, HomeActivity.class);
                paramAnonymous2View.setFlags(268468224);
                paramAnonymous2View.putExtra("com.resmed.refresh.consts.recovering_ui_thread_from_crash", 0);
                SleepSessionConnector.this.bAct.startActivity(paramAnonymous2View);
                SleepSessionConnector.this.bAct.overridePendingTransition(2130968591, 2130968596);
              }
            }), false);
          }
          for (;;)
          {
            return;
            paramHandler.postDelayed(new Runnable()
            {
              public void run()
              {
                if ((!SleepSessionConnector.this.isHandlingHeartBeat) && (!SleepSessionConnector.this.isSyncAndStop) && (SleepSessionConnector.this.bAct != null) && (!SleepSessionConnector.this.bAct.isFinishing())) {
                  SleepSessionConnector.this.requestSamples(1, 1);
                }
              }
            }, 3000L);
          }
        }
      });
      paramHandler.postDelayed(this.cancelSleepSessionRunnable, 3000L);
    }
  }
  
  private void requestSamples(int paramInt1, int paramInt2)
  {
    Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::requestSamples(" + paramInt1 + ", " + paramInt2 + ") pendingBioSamplesRpc : " + this.pendingBioSamplesRpc + " pendingEnvSamplesRpc : " + this.pendingEnvSamplesRpc);
    int i;
    int j;
    if (this.bAct != null)
    {
      i = 65535;
      j = 65535;
      if (paramInt1 <= 50000) {
        break label195;
      }
      Message localMessage = new Message();
      localMessage.what = 24;
      this.bAct.sendMsgBluetoothService(localMessage);
    }
    for (;;)
    {
      if ((this.pendingBioSamplesRpc == null) && (this.pendingEnvSamplesRpc == null))
      {
        this.pendingBioSamplesRpc = BaseBluetoothActivity.getRpcCommands().transmitPacket(i, false, false);
        this.pendingEnvSamplesRpc = BaseBluetoothActivity.getRpcCommands().transmitPacket(j, true, false);
        AppFileLog.addTrace("OUT : Requesting BIO : " + paramInt1 + " ENV : " + paramInt2);
        this.bAct.sendRpcToBed(this.pendingBioSamplesRpc);
      }
      return;
      label195:
      if (paramInt1 < 1000)
      {
        i = paramInt1;
        j = paramInt2;
      }
    }
  }
  
  private void showConnectionProblemOnStopSession()
  {
    Intent localIntent = new Intent(this.bAct, ReconnectActivity.class);
    localIntent.putExtra("com.resmed.refresh.ui.uibase.app.sleep_history_id", this.sleepSessionInfo.getId());
    this.bAct.startActivityForResult(localIntent, 1);
  }
  
  private void stopStreamAndRequestAllSamples()
  {
    this.isWaitLastSamples = true;
    if (this.bAct == null) {}
    for (;;)
    {
      return;
      JsonRPC localJsonRPC = BaseBluetoothActivity.getRpcCommands().stopNightTimeTracking();
      localJsonRPC.setRPCallback(new JsonRPC.RPCallback()
      {
        public void execute()
        {
          SleepSessionConnector.this.requestSamples(65535, 65535);
        }
        
        public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {}
        
        public void preExecute() {}
      });
      this.bAct.sendRpcToBed(localJsonRPC);
    }
  }
  
  private void syncDataAndStop()
  {
    Log.d("com.resmed.refresh.sleepFragment", "SleepTrackFragment::syncDataAndStop()");
    this.isWaitLastSamples = true;
    this.isHandlingHeartBeat = true;
    stopSleepSession();
  }
  
  public void addEnvDataToDB()
  {
    if (this.sleepSessionInfo != null) {}
    try
    {
      Object localObject = new java.lang.StringBuilder;
      ((StringBuilder)localObject).<init>("addEnvDataToDB ");
      Log.d("com.resmed.refresh.env", this.lightValues.size() + " light values and " + this.tempValues.size() + " temp values" + " START " + this.sleepSessionInfo.getStartTime());
      this.sleepSessionInfo.getEnvironmentalInfo().addSessionLightArray(this.lightValues);
      this.sleepSessionInfo.getEnvironmentalInfo().addSessionTemperatureArray(this.tempValues);
      this.sleepSessionInfo.update();
      localObject = new java/util/ArrayList;
      ((ArrayList)localObject).<init>();
      this.lightValues = ((List)localObject);
      localObject = new java/util/ArrayList;
      ((ArrayList)localObject).<init>();
      this.tempValues = ((List)localObject);
      return;
    }
    catch (DaoException localDaoException)
    {
      for (;;)
      {
        Log.d("com.resmed.refresh.env", localDaoException.getMessage());
        localDaoException.printStackTrace();
      }
    }
  }
  
  public void disableAudioRecording()
  {
    this.isAudioActive = false;
  }
  
  public void enableAudioRecording()
  {
    this.isAudioActive = true;
  }
  
  public void handleAudioAmplitude(final int paramInt)
  {
    if (this.bAct != null) {
      this.bAct.runOnUiThread(new Runnable()
      {
        public void run()
        {
          float f2 = (float)(20.0D * Math.log10(paramInt));
          float f1 = f2;
          if (f2 == Float.NEGATIVE_INFINITY) {
            f1 = 0.0F;
          }
          Log.d("com.resmed.refresh.sound", "SleepSessionConnector::handleAudioAmplitude() amplitude : " + paramInt + " db : " + f1);
          boolean bool = SleepSessionConnector.this.soundTopAmplitudes.insert(Integer.valueOf(paramInt));
          Log.d("com.resmed.refresh.sound", " SleepSessionConnector::handleAudioAmplitude() sound wasInserted : " + bool);
          if (bool)
          {
            Object localObject = new RST_SleepEvent();
            ((RST_SleepEvent)localObject).setType(RST_SleepEvent.SleepEventType.kSleepEventTypeSound.ordinal());
            long l = SleepSessionConnector.this.sleepSessionInfo.getStartTime().getTime();
            l = System.currentTimeMillis() - l;
            int i = (int)(l / 1000L / 30L);
            AppFileLog.addTrace(" SleepSessionConnector::handleAudioAmplitude saving sound epochNumber : " + i + " at :" + l);
            ((RST_SleepEvent)localObject).setEpochNumber(i);
            ((RST_SleepEvent)localObject).setIdSleepSession(Long.valueOf(SleepSessionConnector.this.sleepSessionInfo.getId()));
            ((RST_SleepEvent)localObject).setValue(paramInt);
            SleepSessionConnector.this.sleepSessionInfo.addEvent((RST_SleepEvent)localObject);
            localObject = (Integer)SleepSessionConnector.this.soundTopAmplitudes.getLastReplacedElement();
            if (localObject != null)
            {
              bool = SleepSessionConnector.this.sleepSessionInfo.deleteSoundEvent(((Integer)localObject).intValue());
              Log.d("com.resmed.refresh.sound", " SleepSessionConnector::handleAudioAmplitude() sound wasDeleted : " + bool);
            }
            SleepSessionConnector.this.sleepSessionInfo.update();
          }
        }
      });
    }
  }
  
  public void handleBreathingRate(Bundle paramBundle)
  {
    Log.i("RM20StartMethod", "handleBreathingRate() SleepTrackFragment");
  }
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    this.lastState = paramCONNECTION_STATE;
    if (this.isSyncAndStop) {}
    for (;;)
    {
      return;
      final Handler localHandler = new Handler();
      Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::handleConnectionStatus()  connState : " + paramCONNECTION_STATE);
      Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::handleConnectionStatus()  isHandlingHeartBeat : " + this.isHandlingHeartBeat);
      switch (paramCONNECTION_STATE)
      {
      case SOCKET_BROKEN: 
      case SESSION_OPENING: 
      case SOCKET_NOT_CONNECTED: 
      default: 
        break;
      case REAL_STREAM_ON: 
        this.isHandlingHeartBeat = false;
        break;
      case SOCKET_RECONNECTING: 
        this.hasSessionStarted = true;
        Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::handleConnectionStatus()  hasSessionStarted : " + this.hasSessionStarted);
        this.isHandlingHeartBeat = false;
        if (this.closeSleepSession)
        {
          stopSleepSession();
          this.closeSleepSession = false;
        }
        break;
      case SOCKET_CONNECTED: 
        Log.d("com.resmed.refresh.sleepFragment", " SleepTrackFragment SESSION_OPENED, isWaitLastSamples : " + this.isWaitLastSamples + " isClosingSession:" + this.isClosingSession + " isSyncAndStop:" + this.isSyncAndStop);
        if ((!this.isWaitLastSamples) && (!this.isClosingSession)) {
          localHandler.postDelayed(new Runnable()
          {
            public void run()
            {
              SleepSessionConnector.this.registerForSessionTimeout(localHandler);
              JsonRPC localJsonRPC = BaseBluetoothActivity.getRpcCommands().startNightTracking();
              SleepSessionConnector.this.bAct.sendRpcToBed(localJsonRPC);
            }
          }, 1000L);
        }
        this.isHandlingHeartBeat = false;
        break;
      case SESSION_OPENED: 
        this.bAct.connectToBeD(false);
      }
    }
  }
  
  public void handleEnvSample(Bundle paramBundle)
  {
    if (paramBundle == null) {}
    for (;;)
    {
      return;
      float f = paramBundle.getFloat("tempArray");
      int i = paramBundle.getInt("lightArray");
      paramBundle = new RST_ValueItem();
      paramBundle.setValue(decompressLight(i));
      paramBundle.setOrdr(this.envTotalCount);
      RST_ValueItem localRST_ValueItem = new RST_ValueItem();
      localRST_ValueItem.setValue(f);
      localRST_ValueItem.setOrdr(this.envTotalCount);
      this.envTotalCount += 1;
      this.lightValues.add(paramBundle);
      this.tempValues.add(localRST_ValueItem);
      AppFileLog.addTrace("IN handleEnvSample light : " + paramBundle.getValue() + "  temp : " + localRST_ValueItem.getValue());
      Log.d("com.resmed.refresh.env", "handleEnvSample light : " + paramBundle.getValue() + "  temp : " + localRST_ValueItem.getValue());
      if (this.lightValues.size() >= this.sizeToStore) {
        addEnvDataToDB();
      }
    }
  }
  
  public void handleReceivedRpc(JsonRPC paramJsonRPC)
  {
    Log.d("com.resmed.refresh.ui", " SleepSessionConnector::handleReceivedRpc() receivedRPC:" + paramJsonRPC);
    Object localObject;
    if (paramJsonRPC != null)
    {
      localObject = paramJsonRPC.getResult();
      Log.d("com.resmed.refresh.ui", " SleepSessionConnector::handleReceivedRpc() result:" + localObject);
      if (localObject == null) {
        break label191;
      }
      this.countTimeout += 1;
      localObject = ((ResultRPC)localObject).getPayload();
      Log.d("com.resmed.refresh.ui", " SleepSessionConnector::handleReceivedRpc() payload:" + (String)localObject);
      if (!((String)localObject).contains("TRUE")) {
        break label159;
      }
      AppFileLog.addTrace("IN : " + paramJsonRPC.getId() + " PAYLOAD : TERM(TRUE)");
      if ((localObject != null) && (((String)localObject).contains("TRUE"))) {
        handleSamplesTransmissionCompleteForRPC(paramJsonRPC);
      }
    }
    for (;;)
    {
      return;
      label159:
      AppFileLog.addTrace("IN : " + paramJsonRPC.getId() + " PAYLOAD ACK");
      break;
      label191:
      localObject = paramJsonRPC.getError();
      if ((localObject != null) && ((((JsonRPC.ErrorRpc)localObject).getCode().intValue() == -12) || (((JsonRPC.ErrorRpc)localObject).getCode().intValue() == -11))) {
        handleSamplesTransmissionCompleteForRPC(paramJsonRPC);
      }
    }
  }
  
  public void handleSessionRecovered(Bundle paramBundle)
  {
    this.sleepSessionListener.onSessionOk();
  }
  
  public void handleSleepSessionStopped(Bundle paramBundle)
  {
    Log.d("com.resmed.refresh.finish", "handleSleepSessionStopped() ");
    AppFileLog.addTrace("STOP handleSleepSessionStopped ");
    this.bAct.getWindow().clearFlags(128);
    int k = 0;
    int j = 0;
    int i;
    int m;
    if (paramBundle != null)
    {
      String str = paramBundle.getString("sParamsJson");
      long l1 = paramBundle.getLong(SleepSessionManager.ParamSessionId, 0L);
      long l2 = paramBundle.getLong(SleepSessionManager.ParamsecondsElapsed, 0L);
      i = paramBundle.getInt(SleepSessionManager.ParamNumberOfBioSamples, 0);
      m = paramBundle.getInt(SleepSessionManager.ParamAlarmFireEpoch, 0);
      Log.d("com.resmed.refresh.finish", "handleSleepSessionStopped() sParamsJson : " + str + " sessionId : " + l1 + " secondsElapsed : " + l2);
      AppFileLog.addTrace("STOP handleSleepSessionStopped() sessionId : " + l1 + " secondsElapsed : " + l2 + " sParamsJson : " + str);
      AppFileLog.addTrace("Session AlarmFireEpoch: " + m);
      paramBundle = (SleepParams)new Gson().fromJson(str, SleepParams.class);
      Log.d("com.resmed.refresh.sleepFragment", " SleepTrackFragment::handleSleepSessionStopped(Bundle data) bioTotalCount : " + this.bioCurrentTotalCount);
      if (i >= Consts.MIN_SAMPLES_TO_SAVE_RECORD) {
        break label495;
      }
      i = 1;
      l1 = (System.currentTimeMillis() - this.sleepSessionInfo.getStartTime().getTime()) / 1000L;
      if (l1 < Consts.MIN_SECS_TO_SAVE_RECORD)
      {
        AppFileLog.addTrace("ERROR UPLOADING SLEEP SESSION: session time:" + l1);
        i = 1;
      }
      if (i == 0) {
        break label500;
      }
      this.processRecord.interrupt();
      this.bAct.dismissDialog();
      this.bAct.showDialog(new CustomDialogBuilder(this.bAct).title(2131165861).description(2131165862).setPositiveButton(2131165296, new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          paramAnonymousView = new Intent(SleepSessionConnector.this.bAct, HomeActivity.class);
          paramAnonymousView.setFlags(268468224);
          SleepSessionConnector.this.bAct.startActivity(paramAnonymousView);
          SleepSessionConnector.this.bAct.overridePendingTransition(2130968582, 2130968583);
        }
      }), false);
      j = m;
      k = i;
      if (this.sleepSessionInfo != null)
      {
        this.sleepSessionInfo.delete();
        k = i;
        j = m;
      }
    }
    label495:
    label500:
    do
    {
      if (this.sleepSessionInfo != null)
      {
        this.sleepSessionInfo.setStopTime(Calendar.getInstance().getTime());
        this.sleepSessionInfo.setAlarmFireEpoch(j);
        this.sleepSessionInfo.setCompleted(true);
        this.sleepSessionInfo.update();
      }
      if ((this.processRecord != null) && (k == 0))
      {
        this.processRecord.finish();
        Log.d("com.resmed.refresh.finish", "progress setted to 99");
      }
      this.pendingBioSamplesRpc = null;
      this.pendingEnvSamplesRpc = null;
      if (this.timeoutThread != null) {
        this.timeoutThread.finish();
      }
      stopAudioRecording(false);
      return;
      i = 0;
      break;
      j = m;
      k = i;
    } while (paramBundle == null);
    this.sleepSessionInfo.processRM20Data(paramBundle);
    this.sleepSessionInfo.setCompleted(true);
    this.sleepSessionInfo.update();
    Log.d("com.resmed.refresh.model", "SleepSession processed-----Env Data");
    Log.d("com.resmed.refresh.model", "sleepSessionInfo completed? = " + this.sleepSessionInfo.getCompleted());
    j = 0;
    for (;;)
    {
      try
      {
        if (j < this.sleepSessionInfo.getEnvironmentalInfo().getSessionLight().size()) {
          continue;
        }
        j = 0;
        k = this.sleepSessionInfo.getEnvironmentalInfo().getSessionTemperature().size();
        if (j < k) {
          continue;
        }
      }
      catch (Exception paramBundle)
      {
        continue;
      }
      Log.d("com.resmed.refresh.model", "SleepSession processed-----Env Data");
      j = m;
      k = i;
      break;
      paramBundle = new java.lang.StringBuilder;
      paramBundle.<init>("Light[");
      Log.d("com.resmed.refresh.model", j + "]=" + ((RST_ValueItem)this.sleepSessionInfo.getEnvironmentalInfo().getSessionLight().get(j)).getValue());
      j++;
      continue;
      paramBundle = new java.lang.StringBuilder;
      paramBundle.<init>("Temp[");
      Log.d("com.resmed.refresh.model", j + "]=" + ((RST_ValueItem)this.sleepSessionInfo.getEnvironmentalInfo().getSessionTemperature().get(j)).getValue());
      j++;
    }
  }
  
  public void handleStreamPacket(Bundle paramBundle)
  {
    byte[] arrayOfByte = paramBundle.getByteArray("REFRESH_BED_NEW_DATA");
    int i = paramBundle.getByte("REFRESH_BED_NEW_DATA_TYPE");
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
  }
  
  public void init(boolean paramBoolean)
  {
    this.lastState = RefreshApplication.getInstance().getCurrentConnectionState();
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.bAct.getApplicationContext());
    int i = localSharedPreferences.getInt("PREF_CONNECTION_STATE", -1);
    long l = localSharedPreferences.getLong("PREF_NIGHT_LAST_SESSION_ID", -1L);
    AppFileLog.addTrace("SleepSessionConnector recovering state " + i + " for session id : " + l);
    this.bAct.registerReceiver(this.reconnectReceiver, new IntentFilter("BLUETOOTH_SERVICE_INTENT_RESTART"));
    if (this.isSyncAndStop)
    {
      recoverSleepSession(paramBoolean);
      syncDataAndStop();
      if (!this.isSyncAndStop) {
        break label165;
      }
    }
    label165:
    for (i = 640;; i = 2)
    {
      this.sizeToStore = i;
      return;
      if (i == CONNECTION_STATE.NIGHT_TRACK_ON.ordinal())
      {
        recoverSleepSession(true);
        RegisterRepeatingAlarmWake(this.bAct);
        break;
      }
      startSleepSession();
      break;
    }
  }
  
  public boolean isRecovering()
  {
    return this.isRecovering;
  }
  
  public void onClickStop()
  {
    AppFileLog.addTrace("STOP SleepSessionConnector onClickStop");
    if (this.bAct.checkBluetoothEnabled(true))
    {
      this.sleepSessionListener.stopRelax(false);
      this.sleepSessionInfo.setStopTime(Calendar.getInstance().getTime());
      this.sleepSessionInfo.setCompleted(false);
      this.sleepSessionInfo.update();
      RST_SleepSession.getInstance().stopSession(true);
      if ((this.bAct == null) || (this.bAct.isBoundToBluetoothService())) {
        break label140;
      }
      Log.d("com.resmed.refresh.bluetooth", " SleepTrackFragment::onViewCreate is isBoundToBluetoothService :" + this.bAct.isBoundToBluetoothService());
      this.bAct.bindToService();
      new Handler().postDelayed(new Runnable()
      {
        public void run()
        {
          SleepSessionConnector.this.closeSession();
        }
      }, 200L);
    }
    for (;;)
    {
      return;
      label140:
      closeSession();
    }
  }
  
  public void recoverSessionId(long paramLong)
  {
    Log.d("com.resmed.refresh.sleepFragment", "SleepTrackFragment::recoverSessionId, sessionId : " + paramLong);
    this.sleepSessionInfo = RefreshModelController.getInstance().localSleepSessionForId(paramLong);
  }
  
  protected void recoverSleepSession(boolean paramBoolean)
  {
    SleepTrackFragment.recoverFromCrash = true;
    System.out.println("In SleepSessionConnector backto*************=> " + SleepTrackFragment.recoverFromCrash);
    this.isRecovering = true;
    this.sleepSessionListener.onSessionRecovering();
    Object localObject = PreferenceManager.getDefaultSharedPreferences(this.bAct.getApplicationContext());
    SharedPreferences.Editor localEditor = ((SharedPreferences)localObject).edit();
    localEditor.putInt("PREF_CONNECTION_STATE", CONNECTION_STATE.NIGHT_TRACK_ON.ordinal());
    localEditor.commit();
    long l = ((SharedPreferences)localObject).getLong("PREF_NIGHT_LAST_TIMESTAMP_ID", System.currentTimeMillis());
    l = (System.currentTimeMillis() - l) / 1000L;
    localObject = RefreshModelController.getInstance();
    this.sleepSessionInfo = ((RefreshModelController)localObject).localLatestUnfinishedSession();
    Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::recoverSleepSession  sleepSessionInfo : " + this.sleepSessionInfo);
    if (this.sleepSessionInfo == null)
    {
      AppFileLog.addTrace("No SLeep Session Found in Recovering Mode");
      this.sleepSessionInfo = ((RefreshModelController)localObject).createSleepSessionInfo();
    }
    localObject = Long.valueOf(this.sleepSessionInfo.getId());
    Log.d("com.resmed.refresh.sleepFragment", "SleepSessionConnector::recoverSleepSession sleepSessionId : " + localObject);
    AppFileLog.addTrace("RECOVERING sleepsession " + localObject + " state : " + this.lastState);
    Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::recoverSleepSession : totalTime : " + l + " userSelectsBackToSleep:" + paramBoolean + " isSyncAndStop:" + this.isSyncAndStop);
    if (((l > 1800L) && (!paramBoolean) && (!this.isSyncAndStop)) || (this.hasAutoRecoveredFromCrash == -1))
    {
      Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::recoverSleepSession : RECOVERING ERROR TIMEOUT_SLEEP_SESSION totalTime=" + l + " userSelectsBackToSleep=" + paramBoolean);
      AppFileLog.addTrace("RECOVERING ERROR TIMEOUT_SLEEP_SESSION totalTime=" + l + " userSelectsBackToSleep=" + paramBoolean);
      localObject = new Intent(this.bAct, CrashSleepSessionActivity.class);
      this.bAct.startActivity((Intent)localObject);
      this.bAct.overridePendingTransition(2130968597, 2130968586);
    }
    if (!this.isSyncAndStop) {
      setupController();
    }
    if (this.hasAutoRecoveredFromCrash != 1)
    {
      l = this.sleepSessionInfo.getId();
      localObject = new Message();
      ((Message)localObject).what = 21;
      ((Message)localObject).getData().putLong("PREF_NIGHT_LAST_SESSION_ID", Long.valueOf(l).longValue());
      this.bAct.sendMsgBluetoothService((Message)localObject);
    }
  }
  
  public void resume()
  {
    this.pendingBioSamplesRpc = null;
    this.pendingEnvSamplesRpc = null;
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        if ((SleepSessionConnector.this.bAct != null) && (!SleepSessionConnector.this.bAct.isFinishing())) {
          SleepSessionConnector.this.sleepSessionListener.onSessionOk();
        }
      }
    }, 9000L);
  }
  
  public void setBioOnBeD(int paramInt)
  {
    this.bioOnBeD = paramInt;
    int i = 15000;
    if (paramInt < 50000) {
      i = 2000;
    }
    for (;;)
    {
      if (this.processRecord != null) {
        this.processRecord.setTickTime(i);
      }
      return;
      if ((paramInt > 50000) && (paramInt < 200000)) {
        i = 5000;
      } else if (paramInt > 300000) {
        i = 10000;
      } else if (paramInt > 500000) {
        i = 20000;
      }
    }
  }
  
  public void setHasAutoRecoveredFromCrash(int paramInt)
  {
    this.hasAutoRecoveredFromCrash = paramInt;
  }
  
  public void setSyncing(boolean paramBoolean)
  {
    this.isSyncAndStop = paramBoolean;
  }
  
  protected void setupController()
  {
    Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::setupController()");
    this.isWaitLastSamples = false;
    this.soundTopAmplitudes = new SortedList(5);
    RST_SleepSession.getInstance().startSession(true);
    Object localObject = this.sleepSessionInfo.getEvents();
    Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::setupController() events : " + ((List)localObject).size());
    localObject = ((List)localObject).iterator();
    for (;;)
    {
      if (!((Iterator)localObject).hasNext())
      {
        Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::setupController() soundTopAmplitudes : " + this.soundTopAmplitudes.size());
        this.audioRecorder = new AudioDefaultRecorder(RefreshTools.getFilesPath(), "sampleSound", 3000, this);
        this.pendingBioSamplesRpc = null;
        this.pendingEnvSamplesRpc = null;
        return;
      }
      RST_SleepEvent localRST_SleepEvent = (RST_SleepEvent)((Iterator)localObject).next();
      if (localRST_SleepEvent.getType() == RST_SleepEvent.SleepEventType.kSleepEventTypeSound.ordinal()) {
        this.soundTopAmplitudes.insert(Integer.valueOf(localRST_SleepEvent.getValue()));
      }
    }
  }
  
  public void startAudioRecording(boolean paramBoolean)
  {
    Log.d("com.resmed.refresh.sleepFragment", " SleepSessionConnector::startAudioRecording(boolean keepPreviousData) audioRecorder:" + this.audioRecorder + " isAudioActive:" + this.isAudioActive);
    if ((this.audioRecorder != null) && (this.isAudioActive))
    {
      this.audioRecorder.stop(paramBoolean);
      this.audioRecorder.startRecording();
    }
  }
  
  protected void startSleepSession()
  {
    Object localObject2 = RefreshModelController.getInstance();
    this.sleepSessionInfo = ((RefreshModelController)localObject2).createSleepSessionInfo();
    Object localObject1 = PreferenceManager.getDefaultSharedPreferences(this.bAct.getApplicationContext()).edit();
    ((SharedPreferences.Editor)localObject1).putInt("PREF_CONNECTION_STATE", CONNECTION_STATE.NIGHT_TRACK_ON.ordinal());
    ((SharedPreferences.Editor)localObject1).putLong("PREF_NIGHT_LAST_SESSION_ID", this.sleepSessionInfo.getId());
    ((SharedPreferences.Editor)localObject1).commit();
    AppFileLog.deleteCurrentFile();
    AppFileLog.addTrace("START SLEEP SESSION ID : " + this.sleepSessionInfo.getId());
    localObject1 = new Message();
    ((Message)localObject1).what = 12;
    ((Message)localObject1).getData().putLong("sessionId", this.sleepSessionInfo.getId());
    localObject2 = ((RefreshModelController)localObject2).getUser();
    int j;
    int i;
    if (localObject2 != null)
    {
      localObject2 = ((RST_User)localObject2).getProfile();
      if (localObject2 != null)
      {
        j = RefreshTools.calculateAge(((RST_UserProfile)localObject2).getDateOfBirth());
        localObject2 = ((RST_UserProfile)localObject2).getNewGender();
        i = 0;
        if (localObject2 != null)
        {
          if ((((String)localObject2).trim().length() != 0) && (!((String)localObject2).equalsIgnoreCase("M"))) {
            break label243;
          }
          i = 0;
        }
      }
    }
    for (;;)
    {
      ((Message)localObject1).getData().putInt("age", j);
      ((Message)localObject1).getData().putInt("gender", i);
      this.bAct.sendMsgBluetoothService((Message)localObject1);
      setupController();
      RegisterRepeatingAlarmWake(this.bAct);
      return;
      label243:
      if (((String)localObject2).equalsIgnoreCase("F")) {
        i = 1;
      } else {
        i = 0;
      }
    }
  }
  
  public void stopAudioRecording(boolean paramBoolean)
  {
    if ((this.audioRecorder != null) && (this.isAudioActive)) {
      this.audioRecorder.stop(paramBoolean);
    }
  }
  
  public void stopSleepSession()
  {
    try
    {
      AppFileLog.addTrace("STOP SLEEP SESSION");
      preSleepLog.addTrace("STOP SLEEP SESSION");
      Object localObject1 = new java.lang.StringBuilder;
      ((StringBuilder)localObject1).<init>("SleepTrackFragment stopSleepSession isClosingSession? ");
      Log.d("com.resmed.refresh.finish", this.isClosingSession);
      this.bAct.getWindow().addFlags(128);
      if (!this.isClosingSession)
      {
        this.isClosingSession = true;
        Log.d("com.resmed.refresh.finish", "SleepTrackFragment stopSleepSession()");
        if ((!this.isHandlingHeartBeat) || (this.isSyncAndStop))
        {
          this.pendingBioSamplesRpc = null;
          this.pendingEnvSamplesRpc = null;
          localObject1 = new java.lang.StringBuilder;
          ((StringBuilder)localObject1).<init>("stopStreamAndRequestAllSamples Conditions");
          Log.d("com.resmed.refresh.finish", this.isHandlingHeartBeat + ":" + this.isSyncAndStop);
          stopStreamAndRequestAllSamples();
        }
        localObject1 = PreferenceManager.getDefaultSharedPreferences(this.bAct.getApplicationContext()).edit();
        ((SharedPreferences.Editor)localObject1).putInt("PREF_CONNECTION_STATE", CONNECTION_STATE.NIGHT_TRACK_OFF.ordinal());
        ((SharedPreferences.Editor)localObject1).commit();
        this.countTimeout = 0;
        localObject1 = this.bAct;
        CustomDialogBuilder localCustomDialogBuilder = new com/resmed/refresh/ui/utils/CustomDialogBuilder;
        localCustomDialogBuilder.<init>(this.bAct);
        ((BaseBluetoothActivity)localObject1).showBlockingDialog(localCustomDialogBuilder.title(2131165863).description(2131165864).useProgressBar());
        this.processRecord.start();
        CancelRepeatingAlarmWake(this.bAct);
      }
      return;
    }
    finally {}
  }
  
  public static class AlarmReceiver
    extends BroadcastReceiver
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      ((PowerManager)paramContext.getSystemService("power")).newWakeLock(1, "MyWakeLock").acquire(10000L);
      Log.d("com.resmed.refresh.bluetooth.alarm", " SleepSessionConnector::onReceive() AlarmReceiver context : " + paramContext + " is " + paramContext.getPackageName() + " running : " + RefreshTools.isAppRunning(paramContext, paramContext.getPackageName()));
      SleepSessionConnector.RegisterRepeatingAlarmWake(paramContext);
      long l2 = System.currentTimeMillis();
      paramIntent = RefreshTools.readTimeStampToFile(paramContext);
      AppFileLog.addTrace("AlarmReceiver checking bound between MAIN THREAD & SERVICE! currentMillis : " + l2 + " savedLastTimestamp : " + paramIntent);
      long l1;
      if (paramIntent != null)
      {
        Log.d("com.resmed.refresh.bluetooth.alarm", " SleepSessionConnector::onReceive() AlarmReceiver currentMillis: " + l2);
        Log.d("com.resmed.refresh.bluetooth.alarm", " SleepSessionConnector::onReceive() AlarmReceiver LAST_CLIENT_REQUEST_RECEIVED_TIMESTAMP : " + paramIntent);
        l1 = l2 - paramIntent.longValue();
        Log.d("com.resmed.refresh.bluetooth.alarm", " SleepSessionConnector::onReceive() AlarmReceiver diffLastClientMsgTimeStamp : " + l1);
        if (l1 < 1200000L) {
          break label271;
        }
        AppFileLog.addTrace("RELAUNCHING MAIN APP DUE TO TIMEOUT BOUND BETWEEN MAIN THREAD & SERVICE! currentMillis : " + l2 + " savedLastTimestamp : " + paramIntent + " diff " + l1);
        paramIntent = new Intent(paramContext, SleepTimeActivity.class);
        paramIntent.setFlags(268468224);
        paramIntent.putExtra("com.resmed.refresh.consts.recovering_ui_thread_from_crash", 1);
        paramContext.startActivity(paramIntent);
      }
      for (;;)
      {
        return;
        label271:
        AppFileLog.addTrace("Activity & Service bound still valid !  : " + l2 + " savedLastTimestamp : " + paramIntent + " diff " + l1);
      }
    }
  }
  
  private class ProgressThread
    extends Thread
  {
    private double progress = 0.0D;
    private long tickTime;
    
    public ProgressThread()
    {
      Log.d("com.resmed.refresh.finish", "ProgressThread created " + getId());
      this.tickTime = 8000L;
    }
    
    private void setProgress(final double paramDouble)
    {
      this.progress = paramDouble;
      if ((SleepSessionConnector.this.bAct != null) && (!SleepSessionConnector.this.bAct.isFinishing())) {
        SleepSessionConnector.this.bAct.runOnUiThread(new Runnable()
        {
          public void run()
          {
            SleepSessionConnector.this.bAct.setDialogProgress((int)paramDouble);
          }
        });
      }
    }
    
    public void finish()
    {
      try
      {
        setProgress(99.0D);
        this.tickTime = 1000L;
        return;
      }
      finally
      {
        localObject = finally;
        throw ((Throwable)localObject);
      }
    }
    
    public void run()
    {
      for (;;)
      {
        if (this.progress >= 100.0D)
        {
          SleepSessionConnector.this.addEnvDataToDB();
          if ((SleepSessionConnector.this.bAct != null) && (!SleepSessionConnector.this.bAct.isFinishing()))
          {
            SleepSessionConnector.this.bAct.updateDataStoredFlag(0);
            Log.d("com.resmed.refresh.model", "Stop Session lauching sync service");
            if (SleepSessionConnector.this.sleepSessionInfo != null) {
              RefreshModelController.getInstance().serviceUpdateRecord(SleepSessionConnector.this.sleepSessionInfo, new RST_CallbackItem()
              {
                public void onResult(RST_Response<Object> paramAnonymousRST_Response) {}
              });
            }
            Intent localIntent = new Intent(SleepSessionConnector.this.bAct, HomeActivity.class);
            localIntent.setFlags(268468224);
            SleepSessionConnector.this.bAct.startActivity(localIntent);
            SleepSessionConnector.this.bAct.overridePendingTransition(2130968591, 2130968596);
          }
          return;
        }
        try
        {
          Thread.sleep(this.tickTime);
          this.progress += 1.0D;
          setProgress(this.progress);
          Log.d("com.resmed.refresh.finish", "ProgressThread " + getId() + " progress = " + this.progress);
        }
        catch (Exception localException)
        {
          for (;;) {}
        }
      }
    }
    
    public void setTickTime(long paramLong)
    {
      try
      {
        this.tickTime = paramLong;
        return;
      }
      finally
      {
        localObject = finally;
        throw ((Throwable)localObject);
      }
    }
  }
  
  public static abstract interface SleepSessionListener
  {
    public abstract void onSessionOk();
    
    public abstract void onSessionRecovering();
    
    public abstract void startRelax(boolean paramBoolean);
    
    public abstract void stopRelax(boolean paramBoolean);
  }
  
  private class TimeOutThread
    extends Thread
  {
    private boolean active = true;
    private int count = 0;
    
    private TimeOutThread() {}
    
    public void finish()
    {
      this.active = false;
    }
    
    public void run()
    {
      try
      {
        Thread.sleep(10000L);
        for (;;)
        {
          if (!this.active) {
            return;
          }
          if ((this.count == SleepSessionConnector.this.countTimeout) && (SleepSessionConnector.this.processRecord != null))
          {
            this.count = SleepSessionConnector.this.countTimeout;
            AppFileLog.addTrace("TIMEOUT on ProgressThread");
            if ((SleepSessionConnector.this.bAct != null) && (!SleepSessionConnector.this.bAct.isFinishing()))
            {
              Intent localIntent = new Intent(SleepSessionConnector.this.bAct, RecoverDataOnBeDActivity.class);
              localIntent.putExtra("com.resmed.refresh.ui.uibase.app.came_from_sleepsession", true);
              SleepSessionConnector.this.bAct.startActivity(localIntent);
              SleepSessionConnector.this.bAct.finish();
            }
          }
          try
          {
            Thread.sleep(10000L);
          }
          catch (Exception localException1) {}
        }
      }
      catch (Exception localException2)
      {
        for (;;) {}
      }
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
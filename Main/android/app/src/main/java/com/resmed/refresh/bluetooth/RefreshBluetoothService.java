package com.resmed.refresh.bluetooth;

import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.resmed.edflib.EdfLibJNI;
import com.resmed.refresh.bluetooth.exception.BluetoohNotSupportedException;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.model.json.ResultRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.sleepsession.SleepSessionManager;
import com.resmed.refresh.ui.activity.SleepTimeActivity;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.BluetoothDataSerializeUtil;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshBluetoothManager;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.rm20.RM20JNI;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;

public class RefreshBluetoothService
  extends Service
  implements RefreshBluetoothServiceClient
{
  public static final String ACTION_ALREADY_PAIRED = "ACTION_ALREADY_PAIRED";
  public static final String REFRESH_BED_NEW_CONN_STATUS = "REFRESH_BED_NEW_CONN_STATUS";
  public static final String REFRESH_BED_NEW_DATA = "REFRESH_BED_NEW_DATA";
  public static final String REFRESH_BED_NEW_DATA_SIZE = "REFRESH_BED_NEW_DATA_SIZE";
  public static final String REFRESH_BED_NEW_DATA_TYPE = "REFRESH_BED_NEW_DATA_TYPE";
  public static final String REFRESH_BT_SERVICE_RUNNING = "REFRESH_BT_SERVICE_RUNNING";
  private RefreshBluetoothManager bluetoothManager;
  private boolean isBindToClient = true;
  private int isDataAvailableOnDevice;
  private boolean isHandleBulkTransfer = false;
  private Messenger mClient;
  final Messenger mMessenger = new Messenger(new BluetoothRequestsHandler());
  private SharedPreferences prefs;
  public boolean sessionToBeStopped = false;
  private SleepSessionManager sleepSessionManager;
  
  private void changeToForeground()
  {
    Object localObject = new Intent(this, SleepTimeActivity.class);
    ((Intent)localObject).setFlags(603979776);
    localObject = PendingIntent.getActivity(this, 0, (Intent)localObject, 0);
    startForeground(65531, new NotificationCompat.Builder(this).setContentIntent((PendingIntent)localObject).setSmallIcon(2130837933).setPriority(-2).setContentTitle(getString(2131165865)).setContentText(getString(2131165866)).build());
  }
  
  private void checkManager()
  {
    if (this.bluetoothManager == null) {}
    try
    {
      BluetoothSetup localBluetoothSetup = new com/resmed/refresh/bluetooth/BluetoothSetup;
      localBluetoothSetup.<init>(this);
      this.bluetoothManager = localBluetoothSetup;
      this.bluetoothManager.enable();
      return;
    }
    catch (BluetoohNotSupportedException localBluetoohNotSupportedException)
    {
      for (;;)
      {
        localBluetoohNotSupportedException.printStackTrace();
      }
    }
  }
  
  private void checkNightTrack()
  {
    this.isBindToClient = false;
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        AppFileLog.addTrace("Checking Night Track");
        if (!RefreshBluetoothService.this.isBindToClient)
        {
          AppFileLog.addTrace("Timeout waiting for communitaction with client => New Intent");
          Intent localIntent = new Intent(RefreshBluetoothService.this.getApplicationContext(), SleepTimeActivity.class);
          localIntent.setFlags(268468224);
          localIntent.putExtra("com.resmed.refresh.consts.recovering_app_from_service", true);
          RefreshBluetoothService.this.startActivity(localIntent);
        }
      }
    }, 120000L);
  }
  
  private void recoverSleepSession(long paramLong)
  {
    this.sleepSessionManager = new SleepSessionManager(this);
    int j = this.prefs.getInt("PREF_AGE", 44);
    int i = this.prefs.getInt("PREF_GENDER", 0);
    this.sleepSessionManager.recoverSession(paramLong, j, i);
  }
  
  private void stopCurrentSession()
  {
    AppFileLog.addTrace("MSG_SLEEP_SESSION_STOP Service received.");
    if (this.sleepSessionManager != null) {
      this.sleepSessionManager.stop();
    }
    this.sleepSessionManager = null;
    this.bluetoothManager.cancelReconnection();
    stopForeground(true);
  }
  
  public Context getContext()
  {
    return this;
  }
  
  public void handlePacket(VLP.VLPacket paramVLPacket)
  {
    AppFileLog.addTrace(" _ ");
    Object localObject1;
    int j;
    int i;
    label234:
    byte[] arrayOfByte;
    Object localObject2;
    if (VLPacketType.PACKET_TYPE_RETURN.ordinal() == paramVLPacket.packetType)
    {
      AppFileLog.addTrace("Service handlePacket VLPacketType.PACKET_TYPE_RETURN");
      localObject1 = new String(paramVLPacket.buffer);
      AppFileLog.addTrace("Service handlePacket returnStr: " + ((String)localObject1).replace(" ", "").replace("\n", " ").replace("\t", " "));
      localObject1 = ((JsonRPC)new Gson().fromJson((String)localObject1, JsonRPC.class)).getResult();
      if (localObject1 != null)
      {
        localObject1 = ((ResultRPC)localObject1).getPayload();
        if (localObject1 != null)
        {
          AppFileLog.addTrace("Service handlePacket payload: " + (String)localObject1);
          if (((String)localObject1).equalsIgnoreCase("TRUE"))
          {
            Log.d("com.resmed.refresh.bluetooth", " RefreshBluetoothService isRecovering : ");
            if (this.sleepSessionManager != null)
            {
              localObject1 = new File(RefreshTools.getFilesPath(), getString(2131165343));
              this.sleepSessionManager.addSamplesMiMq((File)localObject1);
              AppFileLog.addTrace("Service handlePacket sleepSessionManager.addSamplesMiMq(filesDir)");
            }
            BluetoothDataSerializeUtil.deleteBulkDataBioFile(getApplicationContext());
            this.isHandleBulkTransfer = false;
            AppFileLog.addTrace("Service handlePacket isHandleBulkTransfer = false");
          }
        }
      }
      j = paramVLPacket.buffer.length;
      i = 1;
      if (j >= 1048000) {
        break label735;
      }
      arrayOfByte = new byte[j];
      AppFileLog.addTrace("Service handlePacket packet length=" + j);
      localObject2 = new ByteArrayInputStream(paramVLPacket.buffer, 0, paramVLPacket.buffer.length);
    }
    for (int k = 0;; k++)
    {
      if (k >= i)
      {
        AppFileLog.addTrace("Service handlePacket Sending MSG_BeD_STREAM_PACKET_PARTIAL_END");
        AppFileLog.addTrace(" _ ");
        localObject2 = new Message();
        ((Message)localObject2).what = 20;
        localObject1 = new Bundle();
        ((Bundle)localObject1).putByte("REFRESH_BED_NEW_DATA_TYPE", paramVLPacket.packetType);
        ((Bundle)localObject1).putInt("REFRESH_BED_NEW_DATA_SIZE", paramVLPacket.packetSize);
        ((Message)localObject2).setData((Bundle)localObject1);
        sendMessageToClient((Message)localObject2);
        for (;;)
        {
          return;
          AppFileLog.addTrace("Service handlePacket is NOT a PACKET_TYPE_RETURN isHandleBulkTransfer=" + this.isHandleBulkTransfer);
          if (!this.isHandleBulkTransfer)
          {
            if ((this.sleepSessionManager != null) && (this.sleepSessionManager.isActive()))
            {
              if (VLPacketType.PACKET_TYPE_BIO_64.ordinal() != paramVLPacket.packetType) {
                break label566;
              }
              this.sleepSessionManager.addBioData(paramVLPacket.buffer, paramVLPacket.packetNo);
            }
            for (;;)
            {
              if ((VLPacketType.PACKET_TYPE_NOTE_STORE_FOREIGN.ordinal() != paramVLPacket.packetType) && (VLPacketType.PACKET_TYPE_NOTE_STORE_LOCAL.ordinal() != paramVLPacket.packetType)) {
                break label663;
              }
              i = PacketsByteValuesReader.getStoreLocalBio(paramVLPacket.buffer);
              Log.w("com.resmed.refresh.ui", "PACKET_TYPE_NOTE_STORE!!! = " + paramVLPacket.packetType + " NUMBER OF SAMPLES : " + i);
              if (i < 32) {
                break label665;
              }
              this.isDataAvailableOnDevice = paramVLPacket.packetType;
              localObject1 = new Message();
              ((Message)localObject1).what = 27;
              ((Message)localObject1).getData().putInt("BUNDLE_BED_AVAILABLE_DATA", this.isDataAvailableOnDevice);
              sendMessageToClient((Message)localObject1);
              break;
              label566:
              if (VLPacketType.PACKET_TYPE_ENV_60.ordinal() == paramVLPacket.packetType) {
                this.sleepSessionManager.addEnvData(paramVLPacket.buffer, this.sleepSessionManager.isActive());
              } else if (VLPacketType.PACKET_TYPE_ENV_1.ordinal() == paramVLPacket.packetType) {
                this.sleepSessionManager.addEnvData(paramVLPacket.buffer, this.sleepSessionManager.isActive());
              } else if (VLPacketType.PACKET_TYPE_BIO_32.ordinal() == paramVLPacket.packetType) {
                this.sleepSessionManager.addBioData(paramVLPacket.buffer, paramVLPacket.packetNo);
              }
            }
            label663:
            break;
            label665:
            AppFileLog.addTrace("Ignoring samples on BeD because : " + i + " samples < 32");
            break;
          }
          if ((VLPacketType.PACKET_TYPE_BIO_64.ordinal() != paramVLPacket.packetType) && (VLPacketType.PACKET_TYPE_BIO_32.ordinal() != paramVLPacket.packetType)) {
            break;
          }
          BluetoothDataSerializeUtil.writeBulkBioDataFile(getApplicationContext(), paramVLPacket.buffer);
        }
        label735:
        j /= 2;
        i++;
        break label234;
      }
      Log.d("com.resmed.refresh.bluetooth", " handlePacket, ready to send to clients. nrOFMessages : " + i);
      int m = ((ByteArrayInputStream)localObject2).read(arrayOfByte, j * k, j);
      AppFileLog.addTrace("Service handlePacket nrBytesRead=" + m);
      if (-1 != m)
      {
        Message localMessage = new Message();
        localMessage.what = 19;
        localObject1 = new Bundle();
        ((Bundle)localObject1).putByteArray("REFRESH_BED_NEW_DATA", arrayOfByte);
        ((Bundle)localObject1).putByte("REFRESH_BED_NEW_DATA_TYPE", paramVLPacket.packetType);
        ((Bundle)localObject1).putInt("REFRESH_BED_NEW_DATA_SIZE", paramVLPacket.packetSize);
        localMessage.setData((Bundle)localObject1);
        sendMessageToClient(localMessage);
      }
    }
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    AppFileLog.addTrace("SERVICE onBind");
    Log.d("com.resmed.refresh.bluetooth", " onBind! ");
    return this.mMessenger.getBinder();
  }
  
  public void onDestroy()
  {
    AppFileLog.addTrace("SERVICE onDestroy called");
    Log.d("com.resmed.refresh.bluetooth", "::onDestroy, bluetooth");
    super.onDestroy();
  }
  
  public void onLowMemory()
  {
    super.onLowMemory();
    AppFileLog.addTrace("SERVICE onLowMemory called");
  }
  
  public void onRebind(Intent paramIntent)
  {
    AppFileLog.addTrace("SERVICE onRebind");
    Log.d("com.resmed.refresh.bluetooth", " onRebind! ");
    super.onRebind(paramIntent);
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    Log.d("com.resmed.refresh.bluetooth", "::onStartCommand, bluetooth");
    EdfLibJNI.loadLibrary(getApplicationContext());
    RM20JNI.loadLibrary(getApplicationContext());
    try
    {
      paramIntent = new com/resmed/refresh/bluetooth/BluetoothSetup;
      paramIntent.<init>(this);
      this.bluetoothManager = paramIntent;
      this.bluetoothManager.enable();
      this.prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
      paramInt1 = this.prefs.getInt("PREF_CONNECTION_STATE", -1);
      long l = this.prefs.getLong("PREF_NIGHT_LAST_SESSION_ID", -1L);
      paramIntent = new java/lang/StringBuilder;
      paramIntent.<init>("SERVICE onStartCommand LastState: ");
      AppFileLog.addTrace(paramInt1 + " Last sessionID : " + l + " state : " + paramInt1);
      paramIntent = new java/lang/StringBuilder;
      paramIntent.<init>(" RefreshBluetoothService::onStartCommand conn state : ");
      Log.d("com.resmed.refresh.ui", paramInt1 + " session id :" + l);
      if (CONNECTION_STATE.NIGHT_TRACK_ON.ordinal() == paramInt1)
      {
        checkNightTrack();
        AppFileLog.addTrace("SERVICE onStartCommand overnight in progress");
        Log.d("com.resmed.refresh.ui", " RefreshBluetoothService::onStartCommand overnight in progress");
        recoverSleepSession(l);
        paramIntent = BluetoothDataSerializeUtil.readJsonFile(getApplicationContext());
        this.bluetoothManager.connectDevice(paramIntent);
        paramIntent = new android/content/Intent;
        paramIntent.<init>(this, SleepTimeActivity.class);
        paramIntent.setFlags(268468224);
        paramIntent.putExtra("com.resmed.refresh.consts.recovering_app_from_service", true);
        startActivity(paramIntent);
        paramIntent = new android/content/Intent;
        paramIntent.<init>("BLUETOOTH_SERVICE_INTENT_RESTART");
        paramIntent.putExtra("BUNDLE_LAST_CONN_STATE", paramInt1);
        getApplicationContext().sendStickyBroadcast(paramIntent);
        changeToForeground();
      }
      for (;;)
      {
        return 1;
        if (CONNECTION_STATE.NIGHT_TRACK_OFF.ordinal() == paramInt1)
        {
          paramIntent = new android/content/Intent;
          paramIntent.<init>("BLUETOOTH_SERVICE_INTENT_RESTART");
          paramIntent.putExtra("BUNDLE_LAST_CONN_STATE", paramInt1);
          getApplicationContext().sendStickyBroadcast(paramIntent);
          AppFileLog.addTrace("SERVICE RESTARTED AFTER SLEEP SESSION WAS STOPPED. How to recover ?");
        }
      }
    }
    catch (BluetoohNotSupportedException paramIntent)
    {
      for (;;)
      {
        paramIntent.printStackTrace();
      }
    }
  }
  
  public void sendConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    Log.d("com.resmed.refresh.pair", "Service sendConnectionStatus : " + CONNECTION_STATE.toString(paramCONNECTION_STATE));
    Object localObject;
    if (this.mClient == null)
    {
      localObject = new Intent("ACTION_RESMED_CONNECTION_STATUS");
      ((Intent)localObject).putExtra("EXTRA_RESMED_CONNECTION_STATE", paramCONNECTION_STATE);
      sendStickyBroadcast((Intent)localObject);
    }
    for (;;)
    {
      return;
      Message localMessage = new Message();
      localMessage.what = 6;
      localObject = new Bundle();
      ((Bundle)localObject).putSerializable("REFRESH_BED_NEW_CONN_STATUS", paramCONNECTION_STATE);
      localMessage.setData((Bundle)localObject);
      sendMessageToClient(localMessage);
    }
  }
  
  public void sendMessageToClient(Message paramMessage)
  {
    if (this.mClient != null) {}
    try
    {
      this.mClient.send(paramMessage);
      return;
    }
    catch (RemoteException paramMessage)
    {
      for (;;) {}
    }
  }
  
  public void unbindService(ServiceConnection paramServiceConnection)
  {
    AppFileLog.addTrace("SERVICE onBind");
    Log.d("com.resmed.refresh.bluetooth", " unBind! ");
    super.unbindService(paramServiceConnection);
  }
  
  public class BluetoothRequestsHandler
    extends Handler
  {
    public BluetoothRequestsHandler() {}
    
    public void handleMessage(Message paramMessage)
    {
      RefreshTools.writeTimeStampToFile(RefreshBluetoothService.this.getApplicationContext(), System.currentTimeMillis());
      RefreshBluetoothService.this.checkManager();
      switch (paramMessage.what)
      {
      }
      for (;;)
      {
        return;
        if (RefreshBluetoothService.this.bluetoothManager != null)
        {
          RefreshBluetoothService.this.bluetoothManager.bluetoothOff();
          continue;
          paramMessage = new Message();
          paramMessage.what = 27;
          Object localObject = new Bundle();
          ((Bundle)localObject).putInt("BUNDLE_BED_AVAILABLE_DATA", RefreshBluetoothService.this.isDataAvailableOnDevice);
          paramMessage.setData((Bundle)localObject);
          RefreshBluetoothService.this.sendMessageToClient(paramMessage);
          continue;
          RefreshBluetoothService.this.isDataAvailableOnDevice = -1;
          continue;
          RefreshBluetoothService.this.isHandleBulkTransfer = true;
          Log.d("com.resmed.refresh.bluetooth", " RefreshBluetoothService isHandleBulkTransfer : " + RefreshBluetoothService.this.isHandleBulkTransfer);
          continue;
          RefreshBluetoothService.this.isHandleBulkTransfer = false;
          continue;
          RefreshBluetoothService.this.bluetoothManager.disable();
          continue;
          RefreshBluetoothService.this.sendConnectionStatus(RefreshBluetoothService.this.bluetoothManager.getConnectionStatus());
          continue;
          RefreshBluetoothService.this.mClient = paramMessage.replyTo;
          paramMessage = new Message();
          paramMessage.what = 4;
          try
          {
            RefreshBluetoothService.this.mClient.send(paramMessage);
            RefreshBluetoothService.this.sendConnectionStatus(RefreshBluetoothService.this.bluetoothManager.getConnectionStatus());
          }
          catch (RemoteException paramMessage)
          {
            for (;;)
            {
              paramMessage.printStackTrace();
            }
          }
          RefreshBluetoothService.this.mClient = null;
          continue;
          RefreshBluetoothService.this.bluetoothManager.discoverResMedDevices(true);
          continue;
          RefreshBluetoothService.this.bluetoothManager.discoverResMedDevices(false);
          continue;
          RefreshBluetoothService.this.bluetoothManager.cancelDiscovery();
          continue;
          paramMessage = paramMessage.getData();
          localObject = paramMessage.getParcelable(RefreshBluetoothService.this.getContext().getString(2131165303));
          boolean bool = paramMessage.getBoolean(RefreshBluetoothService.this.getContext().getString(2131165304));
          paramMessage = (BluetoothDevice)localObject;
          if (RefreshBluetoothService.this.bluetoothManager.isDevicePaired(paramMessage))
          {
            Log.d("com.resmed.refresh.pair", " RefreshBluetoothService connecting to device : " + paramMessage);
            RefreshBluetoothService.this.bluetoothManager.connectDevice(paramMessage);
          }
          else if (bool)
          {
            Log.d("com.resmed.refresh.pair", " RefreshBluetoothService pairing to device : " + paramMessage);
            RefreshBluetoothService.this.bluetoothManager.pairDevice(paramMessage);
          }
          else
          {
            Log.d("com.resmed.refresh.pair", " RefreshBluetoothService the device : " + paramMessage + " has been unpair");
            paramMessage = new Message();
            paramMessage.what = 8;
            RefreshBluetoothService.this.sendMessageToClient(paramMessage);
            continue;
            paramMessage = paramMessage.getData();
            if ((paramMessage != null) && (RefreshBluetoothService.this.bluetoothManager != null))
            {
              paramMessage = paramMessage.getByteArray("bytes");
              Log.d("com.resmed.refresh.bluetooth", " bytes sending : " + paramMessage);
              RefreshBluetoothService.this.bluetoothManager.writeData(paramMessage);
              RefreshBluetoothService.this.isBindToClient = true;
              continue;
              RefreshBluetoothService.this.changeToForeground();
              RefreshBluetoothService.this.sleepSessionManager = new SleepSessionManager(RefreshBluetoothService.this);
              long l1 = -1L;
              int i = 44;
              int j = 0;
              paramMessage = paramMessage.getData();
              if (paramMessage != null)
              {
                l1 = paramMessage.getLong("sessionId");
                i = paramMessage.getInt("age", 44);
                j = paramMessage.getInt("gender", 0);
              }
              RefreshBluetoothService.this.sleepSessionManager.start(l1, i, j);
              RefreshBluetoothService.this.bluetoothManager.testStreamData(RefreshBluetoothService.this.sleepSessionManager);
              long l2 = RefreshBluetoothService.this.prefs.getLong("PREF_NIGHT_LAST_SESSION_ID", -1L);
              paramMessage = RefreshBluetoothService.this.prefs.edit();
              paramMessage.putLong("PREF_NIGHT_LAST_SESSION_ID", l1);
              paramMessage.putInt("PREF_CONNECTION_STATE", CONNECTION_STATE.NIGHT_TRACK_ON.ordinal());
              paramMessage.putInt("PREF_AGE", i);
              paramMessage.putInt("PREF_GENDER", j);
              paramMessage.commit();
              AppFileLog.addTrace("MSG_SLEEP_SESSION_START PREF_NIGHT_LAST_SESSION_ID was " + l2 + " and now is " + l1);
              Log.d("com.resmed.refresh.bluetooth", "Bluetooth service current session id : " + l1 + " stored : " + l2);
              continue;
              if (RefreshBluetoothService.this.sleepSessionManager != null)
              {
                Log.d("com.resmed.refresh.smartAlarm", "RefreshBluetoothService MSG_SMARTALARM_UPDATE");
                paramMessage = paramMessage.getData();
                l1 = paramMessage.getLong("com.resmed.refresh.consts.smart_alarm_time_value");
                i = paramMessage.getInt("com.resmed.refresh.consts.smart_alarm_window_value");
                RefreshBluetoothService.this.sleepSessionManager.updateAlarmSettings(l1, i);
                continue;
                Log.d("com.resmed.refresh.bluetooth", "Bluetooth service isHandleBulkTransfer : " + RefreshBluetoothService.this.isHandleBulkTransfer);
                RefreshBluetoothService.this.stopCurrentSession();
                RefreshBluetoothService.this.isDataAvailableOnDevice = -1;
                continue;
                paramMessage = paramMessage.getData();
                if (paramMessage != null)
                {
                  l1 = paramMessage.getLong("BUNDLE_SMART_ALARM_TIMESTAMP");
                  i = paramMessage.getInt("BUNDLE_SMART_ALARM_WINDOW");
                  Log.d("com.resmed.refresh.smartAlarm", "did RefreshBluetoothServiceClient::MSG_SLEEP_SET_SMART_ALARM ");
                  if (RefreshBluetoothService.this.sleepSessionManager != null)
                  {
                    paramMessage = new Date(l1);
                    RefreshBluetoothService.this.sleepSessionManager.setRM20AlarmTime(paramMessage, i);
                    continue;
                    if (RefreshBluetoothService.this.sleepSessionManager != null)
                    {
                      RefreshBluetoothService.this.sleepSessionManager.requestUserSleepState();
                      continue;
                      l1 = paramMessage.getData().getLong("PREF_NIGHT_LAST_SESSION_ID", -1L);
                      if (RefreshBluetoothService.this.prefs == null) {
                        RefreshBluetoothService.this.prefs = PreferenceManager.getDefaultSharedPreferences(RefreshBluetoothService.this.getApplicationContext());
                      }
                      i = RefreshBluetoothService.this.prefs.getInt("PREF_AGE", 44);
                      j = RefreshBluetoothService.this.prefs.getInt("PREF_GENDER", 0);
                      AppFileLog.addTrace("RefreshBluetoothService::MSG_SLEEP_SESSION_RECOVER, sessionToRecover : " + l1);
                      if (RefreshBluetoothService.this.sleepSessionManager != null)
                      {
                        AppFileLog.addTrace("RefreshBluetoothService::MSG_SLEEP_SESSION_RECOVER, sleepSessionManager.getSessionId() : " + RefreshBluetoothService.this.sleepSessionManager.getSessionId());
                        AppFileLog.addTrace("RefreshBluetoothService::MSG_SLEEP_SESSION_RECOVER, sleepSessionManager.isActive() : " + RefreshBluetoothService.this.sleepSessionManager.isActive());
                      }
                      if (((RefreshBluetoothService.this.sleepSessionManager != null) && (RefreshBluetoothService.this.sleepSessionManager.getSessionId() != l1) && (!RefreshBluetoothService.this.sleepSessionManager.isActive())) || (RefreshBluetoothService.this.sleepSessionManager == null))
                      {
                        RefreshBluetoothService.this.sleepSessionManager = new SleepSessionManager(RefreshBluetoothService.this);
                        RefreshBluetoothService.this.sleepSessionManager.recoverSession(l1, i, j);
                      }
                      else
                      {
                        paramMessage = new Message();
                        paramMessage.what = 21;
                        RefreshBluetoothService.this.sendMessageToClient(paramMessage);
                        continue;
                        Log.d("com.resmed.refresh.bluetooth", "RefreshBluetoothService::MSG_BeD_UNPAIR_ALL, unpairing all : ");
                        RefreshBluetoothService.this.bluetoothManager.unpairAll("S+");
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\bluetooth\RefreshBluetoothService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
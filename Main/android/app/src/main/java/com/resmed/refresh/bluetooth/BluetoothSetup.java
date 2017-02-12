package com.resmed.refresh.bluetooth;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.SystemClock;
import com.resmed.cobs.COBS;
import com.resmed.refresh.bluetooth.exception.BluetoohNotSupportedException;
import com.resmed.refresh.bluetooth.receivers.BluetoothDeviceFoundReceiver;
import com.resmed.refresh.bluetooth.receivers.BluetoothDevicePairedReceiver;
import com.resmed.refresh.bluetooth.receivers.BluetoothStateChangesReceiver;
import com.resmed.refresh.packets.VLP;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.sleepsession.SleepSessionManager;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshBluetoothManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothSetup
  implements RefreshBluetoothManager
{
  public static final String BLUETOOTH_ALARM_RECONNECT = "BLUETOOTH_ALARM_RECONNECT";
  private static final int BT_RECONNECT_REQUEST_CODE = 6367;
  public static final String BeD_INCOMING_DATA = "RESMED_BED_INCOMING_DATA";
  public static final String BeD_INCOMING_DATA_COBS = "BeD_INCOMING_DATA_COBS";
  public static final String BeD_INCOMING_DATA_EXTRA = "RESMED_BED_INCOMING_DATA_EXTRA";
  public static final String BeD_INCOMING_DATA_TYPE_EXTRA = "RESMED_BED_INCOMING_DATA_TYPE_EXTRA";
  public static final int REQUEST_ENABLE_BT = 1;
  private static int count = 1;
  private BroadcastReceiver alarmWakeReconnectReceiver;
  private BluetoothAdapter bluetoothAdapter;
  private RefreshBluetoothServiceClient bluetoothService;
  private BroadcastReceiver bluetoothStateChangesReceiver;
  private CONNECTION_STATE connectionStatus;
  private BluetoothDevice device;
  private BroadcastReceiver deviceFoundReceiver;
  private BroadcastReceiver devicePairedReceiver;
  private ConnectThread mConnectThread;
  private ConnectedThread mConnectedThread;
  private Thread mReconnectionThread;
  private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
  
  public BluetoothSetup(RefreshBluetoothServiceClient paramRefreshBluetoothServiceClient)
    throws BluetoohNotSupportedException
  {
    this.bluetoothService = paramRefreshBluetoothServiceClient;
    this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    this.connectionStatus = CONNECTION_STATE.SOCKET_NOT_CONNECTED;
    this.deviceFoundReceiver = new BluetoothDeviceFoundReceiver(this);
    this.devicePairedReceiver = new BluetoothDevicePairedReceiver(this);
    this.bluetoothStateChangesReceiver = new BluetoothStateChangesReceiver(this);
    this.alarmWakeReconnectReceiver = new AlarmReconnectReceiver(this);
    if (this.bluetoothAdapter == null) {
      throw new BluetoohNotSupportedException();
    }
  }
  
  private static void CancelRepeatingReconnectAlarmWake(Context paramContext)
  {
    if (paramContext != null)
    {
      PendingIntent localPendingIntent = PendingIntent.getBroadcast(paramContext, 6367, new Intent(paramContext, AlarmReceiver.class), 0);
      ((AlarmManager)paramContext.getSystemService("alarm")).cancel(localPendingIntent);
    }
  }
  
  private static void RegisterRepeatingReconnectAlarmWake(Context paramContext)
  {
    AppFileLog.addTrace(" BluetoothSetup::RegisterRepeatingReconnectAlarmWake ALARM FOR RECONNECTION to RUN IN : 15000");
    Log.d("com.resmed.refresh.bluetooth", " BluetoothSetup::RegisterRepeatingReconnectAlarmWake AlarmReceiver::context :" + paramContext);
    if (paramContext != null)
    {
      count = 1;
      PendingIntent localPendingIntent = PendingIntent.getBroadcast(paramContext, 6367, new Intent(paramContext, AlarmReceiver.class), 0);
      ((AlarmManager)paramContext.getSystemService("alarm")).set(2, SystemClock.elapsedRealtime() + 15000L, localPendingIntent);
    }
  }
  
  private static void RegisterRepeatingReconnectAlarmWakeWithTimerReset(Context paramContext, long paramLong)
  {
    AppFileLog.addTrace(" BluetoothSetup::RegisterRepeatingReconnectAlarmWakeWithTimerReset ALARM FOR RECONNECTION to RUN IN : 15000");
    Log.d("com.resmed.refresh.bluetooth", " BluetoothSetup::RegisterRepeatingReconnectAlarmWakeWithTimerReset AlarmReceiver::context :" + paramContext);
    if (paramContext != null)
    {
      PendingIntent localPendingIntent = PendingIntent.getBroadcast(paramContext, 6367, new Intent(paramContext, AlarmReceiver.class), 0);
      ((AlarmManager)paramContext.getSystemService("alarm")).set(2, SystemClock.elapsedRealtime() + paramLong, localPendingIntent);
    }
  }
  
  private void reconnection()
  {
    for (;;)
    {
      try
      {
        Log.d("com.resmed.refresh.bluetooth", " RECONNECTION : ");
        setConnectionStatusAndNotify(CONNECTION_STATE.SOCKET_RECONNECTING, true);
        if (this.mReconnectionThread != null)
        {
          this.mReconnectionThread.interrupt();
          this.mReconnectionThread = null;
        }
        if (this.mConnectThread != null)
        {
          this.mConnectThread.interrupt();
          this.mConnectThread = null;
        }
        Context localContext1 = this.bluetoothService.getContext().getApplicationContext();
        if (BeDConnectionStatus.getInstance().isSocketConnected()) {
          break label233;
        }
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>("BluetoothSetup$AlarmReceiver alarm fired to RECONNECT ! Count : ");
        AppFileLog.addTrace(count + ":isSocketConnected:" + BeDConnectionStatus.getInstance().isSocketConnected());
        if (count < 31)
        {
          if (count > 20)
          {
            RegisterRepeatingReconnectAlarmWakeWithTimerReset(localContext1, 3600000L);
            count += 1;
          }
        }
        else {
          return;
        }
        if (count > 15)
        {
          RegisterRepeatingReconnectAlarmWakeWithTimerReset(localContext1, 1800000L);
          continue;
        }
        if (count <= 10) {
          break label192;
        }
      }
      finally {}
      RegisterRepeatingReconnectAlarmWakeWithTimerReset(localContext2, 600000L);
      continue;
      label192:
      if (count > 5)
      {
        RegisterRepeatingReconnectAlarmWakeWithTimerReset(localContext2, 300000L);
      }
      else if (count > 1)
      {
        RegisterRepeatingReconnectAlarmWakeWithTimerReset(localContext2, 60000L);
      }
      else
      {
        RegisterRepeatingReconnectAlarmWake(localContext2);
        continue;
        label233:
        count = 1;
      }
    }
  }
  
  private void unregisterClientReceivers(BroadcastReceiver... paramVarArgs)
  {
    int j = paramVarArgs.length;
    int i = 0;
    for (;;)
    {
      if (i >= j) {
        return;
      }
      BroadcastReceiver localBroadcastReceiver = paramVarArgs[i];
      try
      {
        this.bluetoothService.getContext().unregisterReceiver(localBroadcastReceiver);
        Log.d("com.resmed.refresh.bluetooth", "receivers unregistered");
        i++;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        for (;;)
        {
          localIllegalArgumentException.printStackTrace();
          Log.d("com.resmed.refresh.bluetooth", "receivers unregistered");
        }
      }
      finally
      {
        Log.d("com.resmed.refresh.bluetooth", "receivers unregistered");
      }
    }
  }
  
  public void bluetoothOff()
  {
    if (this.bluetoothAdapter != null) {
      this.bluetoothAdapter.disable();
    }
  }
  
  public boolean cancelDiscovery()
  {
    return this.bluetoothAdapter.cancelDiscovery();
  }
  
  public void cancelReconnection()
  {
    count = 1;
    CancelRepeatingReconnectAlarmWake(this.bluetoothService.getContext());
  }
  
  /* Error */
  public void connectDevice(BluetoothDevice paramBluetoothDevice)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new 161	java/lang/StringBuilder
    //   5: astore_3
    //   6: aload_3
    //   7: ldc_w 304
    //   10: invokespecial 165	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   13: ldc -97
    //   15: aload_3
    //   16: aload_0
    //   17: getfield 97	com/resmed/refresh/bluetooth/BluetoothSetup:connectionStatus	Lcom/resmed/refresh/bluetooth/CONNECTION_STATE;
    //   20: invokevirtual 169	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   23: invokevirtual 173	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   26: invokestatic 179	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   29: pop
    //   30: new 161	java/lang/StringBuilder
    //   33: astore_3
    //   34: aload_3
    //   35: ldc_w 306
    //   38: invokespecial 165	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   41: aload_3
    //   42: aload_1
    //   43: invokevirtual 169	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   46: ldc_w 308
    //   49: invokevirtual 264	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   52: aload_0
    //   53: getfield 201	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread;
    //   56: invokevirtual 169	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   59: ldc_w 310
    //   62: invokevirtual 264	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   65: aload_0
    //   66: getfield 312	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectedThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectedThread;
    //   69: invokevirtual 169	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   72: invokevirtual 173	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   75: invokestatic 157	com/resmed/refresh/utils/AppFileLog:addTrace	(Ljava/lang/String;)V
    //   78: aload_0
    //   79: getfield 201	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread;
    //   82: ifnull +31 -> 113
    //   85: new 161	java/lang/StringBuilder
    //   88: astore_3
    //   89: aload_3
    //   90: ldc_w 314
    //   93: invokespecial 165	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   96: aload_3
    //   97: aload_0
    //   98: getfield 201	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread;
    //   101: invokevirtual 317	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:isAlive	()Z
    //   104: invokevirtual 267	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
    //   107: invokevirtual 173	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   110: invokestatic 157	com/resmed/refresh/utils/AppFileLog:addTrace	(Ljava/lang/String;)V
    //   113: aload_0
    //   114: getfield 312	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectedThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectedThread;
    //   117: ifnull +31 -> 148
    //   120: new 161	java/lang/StringBuilder
    //   123: astore_3
    //   124: aload_3
    //   125: ldc_w 319
    //   128: invokespecial 165	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   131: aload_3
    //   132: aload_0
    //   133: getfield 312	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectedThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectedThread;
    //   136: invokevirtual 320	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectedThread:isAlive	()Z
    //   139: invokevirtual 267	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
    //   142: invokevirtual 173	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   145: invokestatic 157	com/resmed/refresh/utils/AppFileLog:addTrace	(Ljava/lang/String;)V
    //   148: aload_0
    //   149: getfield 312	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectedThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectedThread;
    //   152: ifnull +13 -> 165
    //   155: aload_0
    //   156: getfield 312	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectedThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectedThread;
    //   159: invokevirtual 320	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectedThread:isAlive	()Z
    //   162: ifne +26 -> 188
    //   165: aload_1
    //   166: ifnull +22 -> 188
    //   169: aload_0
    //   170: getfield 201	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread;
    //   173: ifnull +18 -> 191
    //   176: aload_0
    //   177: getfield 201	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread;
    //   180: invokevirtual 317	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:isAlive	()Z
    //   183: istore_2
    //   184: iload_2
    //   185: ifeq +6 -> 191
    //   188: aload_0
    //   189: monitorexit
    //   190: return
    //   191: aload_0
    //   192: aload_1
    //   193: putfield 212	com/resmed/refresh/bluetooth/BluetoothSetup:device	Landroid/bluetooth/BluetoothDevice;
    //   196: aload_0
    //   197: getfield 201	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread;
    //   200: ifnull +32 -> 232
    //   203: aload_0
    //   204: getfield 201	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread;
    //   207: invokevirtual 317	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:isAlive	()Z
    //   210: ifne +22 -> 232
    //   213: aload_0
    //   214: getfield 201	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread;
    //   217: invokevirtual 322	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:cancel	()V
    //   220: aload_0
    //   221: getfield 201	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread;
    //   224: invokevirtual 235	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:interrupt	()V
    //   227: aload_0
    //   228: aconst_null
    //   229: putfield 201	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread;
    //   232: aload_0
    //   233: getfield 312	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectedThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectedThread;
    //   236: ifnull +22 -> 258
    //   239: aload_0
    //   240: getfield 312	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectedThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectedThread;
    //   243: invokevirtual 323	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectedThread:cancel	()V
    //   246: aload_0
    //   247: getfield 312	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectedThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectedThread;
    //   250: invokevirtual 324	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectedThread:interrupt	()V
    //   253: aload_0
    //   254: aconst_null
    //   255: putfield 312	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectedThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectedThread;
    //   258: aload_0
    //   259: getfield 229	com/resmed/refresh/bluetooth/BluetoothSetup:mReconnectionThread	Ljava/lang/Thread;
    //   262: ifnull +15 -> 277
    //   265: aload_0
    //   266: getfield 229	com/resmed/refresh/bluetooth/BluetoothSetup:mReconnectionThread	Ljava/lang/Thread;
    //   269: invokevirtual 234	java/lang/Thread:interrupt	()V
    //   272: aload_0
    //   273: aconst_null
    //   274: putfield 229	com/resmed/refresh/bluetooth/BluetoothSetup:mReconnectionThread	Ljava/lang/Thread;
    //   277: aload_0
    //   278: aload_1
    //   279: invokevirtual 328	com/resmed/refresh/bluetooth/BluetoothSetup:isDevicePaired	(Landroid/bluetooth/BluetoothDevice;)Z
    //   282: istore_2
    //   283: new 161	java/lang/StringBuilder
    //   286: astore_1
    //   287: aload_1
    //   288: ldc_w 330
    //   291: invokespecial 165	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   294: ldc -97
    //   296: aload_1
    //   297: iload_2
    //   298: invokevirtual 267	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
    //   301: invokevirtual 173	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   304: invokestatic 179	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   307: pop
    //   308: new 14	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread
    //   311: astore_1
    //   312: aload_1
    //   313: aload_0
    //   314: aload_0
    //   315: getfield 212	com/resmed/refresh/bluetooth/BluetoothSetup:device	Landroid/bluetooth/BluetoothDevice;
    //   318: aload_0
    //   319: getfield 80	com/resmed/refresh/bluetooth/BluetoothSetup:uuid	Ljava/util/UUID;
    //   322: invokespecial 333	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:<init>	(Lcom/resmed/refresh/bluetooth/BluetoothSetup;Landroid/bluetooth/BluetoothDevice;Ljava/util/UUID;)V
    //   325: aload_0
    //   326: aload_1
    //   327: putfield 201	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread;
    //   330: aload_0
    //   331: getfield 201	com/resmed/refresh/bluetooth/BluetoothSetup:mConnectThread	Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread;
    //   334: invokevirtual 336	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:start	()V
    //   337: goto -149 -> 188
    //   340: astore_1
    //   341: aload_0
    //   342: monitorexit
    //   343: aload_1
    //   344: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	345	0	this	BluetoothSetup
    //   0	345	1	paramBluetoothDevice	BluetoothDevice
    //   183	115	2	bool	boolean
    //   5	127	3	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   2	113	340	finally
    //   113	148	340	finally
    //   148	165	340	finally
    //   169	184	340	finally
    //   191	232	340	finally
    //   232	258	340	finally
    //   258	277	340	finally
    //   277	337	340	finally
  }
  
  public void disable()
  {
    try
    {
      unregisterClientReceivers(new BroadcastReceiver[] { this.deviceFoundReceiver, this.devicePairedReceiver, this.bluetoothStateChangesReceiver });
      this.connectionStatus = CONNECTION_STATE.SOCKET_NOT_CONNECTED;
      setConnectionStatusAndNotify(this.connectionStatus, true);
      if (this.mConnectThread != null)
      {
        this.mConnectThread.cancel();
        this.mConnectThread.interrupt();
        this.mConnectThread = null;
      }
      if (this.mConnectedThread != null)
      {
        this.mConnectedThread.cancel();
        this.mConnectedThread.interrupt();
        this.mConnectedThread = null;
      }
      if (this.mReconnectionThread != null)
      {
        this.mReconnectionThread.interrupt();
        this.mReconnectionThread = null;
      }
      this.bluetoothAdapter.cancelDiscovery();
      return;
    }
    finally {}
  }
  
  public void discoverResMedDevices(boolean paramBoolean)
  {
    for (;;)
    {
      Object localObject3;
      try
      {
        Object localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>("connection status : ");
        Log.d("com.resmed.refresh.bluetooth", this.connectionStatus);
        if (this.mReconnectionThread != null)
        {
          this.mReconnectionThread.interrupt();
          this.mReconnectionThread = null;
        }
        if (this.mConnectThread != null)
        {
          this.mConnectThread.cancel();
          this.mConnectThread.interrupt();
          this.mConnectThread = null;
        }
        cancelDiscovery();
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>(" this.connectionStatus : ");
        Log.d("com.resmed.refresh.bluetooth", this.connectionStatus);
        if (CONNECTION_STATE.SOCKET_NOT_CONNECTED != this.connectionStatus)
        {
          localObject1 = CONNECTION_STATE.SOCKET_RECONNECTING;
          localObject3 = this.connectionStatus;
          if ((localObject1 != localObject3) && (paramBoolean)) {
            return;
          }
        }
        localObject1 = queryPairedDevices().iterator();
        if (!((Iterator)localObject1).hasNext())
        {
          this.bluetoothAdapter.startDiscovery();
          continue;
        }
        localObject3 = (BluetoothDevice)((Iterator)localObject2).next();
      }
      finally {}
      Object localObject4 = new java/lang/StringBuilder;
      ((StringBuilder)localObject4).<init>("paired device : ");
      Log.d("com.resmed.refresh.bluetooth", ((BluetoothDevice)localObject3).getName() + " address : " + ((BluetoothDevice)localObject3).getAddress());
      if ((this.device != null) && (((BluetoothDevice)localObject3).getName().equals(this.device.getName())) && (((BluetoothDevice)localObject3).getAddress().equals(this.device.getAddress())) && (paramBoolean))
      {
        connectDevice((BluetoothDevice)localObject3);
      }
      else
      {
        localObject4 = new android/content/Intent;
        ((Intent)localObject4).<init>("ACTION_ALREADY_PAIRED");
        ((Intent)localObject4).putExtra("android.bluetooth.device.extra.DEVICE", (Parcelable)localObject3);
        this.bluetoothService.getContext().sendStickyBroadcast((Intent)localObject4);
      }
    }
  }
  
  public void enable()
  {
    IntentFilter localIntentFilter = new IntentFilter("android.bluetooth.device.action.FOUND");
    this.bluetoothService.getContext().registerReceiver(this.deviceFoundReceiver, localIntentFilter);
    localIntentFilter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
    this.bluetoothService.getContext().registerReceiver(this.bluetoothStateChangesReceiver, localIntentFilter);
    localIntentFilter = new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED");
    this.bluetoothService.getContext().registerReceiver(this.devicePairedReceiver, localIntentFilter);
    localIntentFilter = new IntentFilter("BLUETOOTH_ALARM_RECONNECT");
    this.bluetoothService.getContext().registerReceiver(this.alarmWakeReconnectReceiver, localIntentFilter);
  }
  
  public BluetoothAdapter getBluetoothAdapter()
  {
    return this.bluetoothAdapter;
  }
  
  public CONNECTION_STATE getConnectionStatus()
  {
    try
    {
      Object localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>(" connection status in getConnectionStatus: ");
      Log.e("com.resmed.refresh.bluetooth", this.connectionStatus);
      localObject1 = this.connectionStatus;
      return (CONNECTION_STATE)localObject1;
    }
    finally
    {
      localObject2 = finally;
      throw ((Throwable)localObject2);
    }
  }
  
  public void handleNewPacket(ByteBuffer paramByteBuffer)
  {
    Log.d("com.resmed.refresh.bluetooth", "BluetoothSetup::handleNewPacket() , decobbed data size : " + paramByteBuffer.array().length + "to depacktize :" + Arrays.toString(paramByteBuffer.array()));
    VLP.VLPacket localVLPacket = VLP.getInstance().Depacketize(paramByteBuffer);
    Object localObject = new String(localVLPacket.buffer);
    Log.d("com.resmed.refresh.bluetooth", "BluetoothSetup::handleNewPacket() processed new bluetooth data : " + (String)localObject + "packet type : " + localVLPacket.packetType + " size : " + localVLPacket.buffer.length);
    if (VLPacketType.PACKET_TYPE_RETURN.ordinal() == localVLPacket.packetType)
    {
      Log.d("com.resmed.refresh.bluetooth", VLPacketType.PACKET_TYPE_RETURN + " ordinal : " + VLPacketType.PACKET_TYPE_RETURN.ordinal());
      localObject = new Intent("RESMED_BED_INCOMING_DATA");
      ((Intent)localObject).putExtra("RESMED_BED_INCOMING_DATA_TYPE_EXTRA", localVLPacket.packetType);
      ((Intent)localObject).putExtra("RESMED_BED_INCOMING_DATA_EXTRA", localVLPacket.buffer);
      ((Intent)localObject).putExtra("BeD_INCOMING_DATA_COBS", paramByteBuffer.array());
      this.bluetoothService.getContext().sendOrderedBroadcast((Intent)localObject, null);
    }
    this.bluetoothService.handlePacket(localVLPacket);
  }
  
  public boolean isBluetoothEnabled()
  {
    return this.bluetoothAdapter.isEnabled();
  }
  
  public boolean isDevicePaired(BluetoothDevice paramBluetoothDevice)
  {
    boolean bool = false;
    if (paramBluetoothDevice == null)
    {
      bool = false;
      return bool;
    }
    Iterator localIterator = queryPairedDevices().iterator();
    label21:
    if (!localIterator.hasNext()) {}
    for (;;)
    {
      Log.d("com.resmed.refresh.bluetooth", " RefreshBluetoothService device : " + paramBluetoothDevice + " is Paired : " + bool);
      break;
      BluetoothDevice localBluetoothDevice = (BluetoothDevice)localIterator.next();
      Log.d("com.resmed.refresh.bluetooth", "paired device : " + localBluetoothDevice.getName() + " address : " + localBluetoothDevice.getAddress());
      if ((paramBluetoothDevice == null) || (!localBluetoothDevice.getName().equals(paramBluetoothDevice.getName())) || (!localBluetoothDevice.getAddress().equals(paramBluetoothDevice.getAddress()))) {
        break label21;
      }
      bool = true;
    }
  }
  
  public void manageConnection(BluetoothSocket paramBluetoothSocket)
  {
    for (;;)
    {
      try
      {
        Log.d("com.resmed.refresh.bluetooth", " manage connection ! ");
        if (this.mConnectThread != null)
        {
          this.mConnectThread.cancel();
          this.mConnectThread.interrupt();
          this.mConnectThread = null;
        }
        if (this.mConnectedThread != null)
        {
          this.mConnectedThread.cancel();
          this.mConnectedThread.interrupt();
          this.mConnectedThread = null;
        }
        if (this.mReconnectionThread != null)
        {
          this.mReconnectionThread.interrupt();
          this.mReconnectionThread = null;
        }
        if (paramBluetoothSocket != null) {
          try
          {
            ConnectedThread localConnectedThread = new com/resmed/refresh/bluetooth/BluetoothSetup$ConnectedThread;
            localConnectedThread.<init>(this, paramBluetoothSocket);
            this.mConnectedThread = localConnectedThread;
            this.mConnectedThread.start();
            return;
          }
          catch (IOException paramBluetoothSocket)
          {
            paramBluetoothSocket.printStackTrace();
            continue;
          }
        }
        setConnectionStatusAndNotify(CONNECTION_STATE.SOCKET_RECONNECTING, false);
      }
      finally {}
    }
  }
  
  public void pairDevice(BluetoothDevice paramBluetoothDevice)
  {
    if (10 == paramBluetoothDevice.getBondState())
    {
      Log.d("com.resmed.refresh.bluetooth", "bluetooth device bonding");
      paramBluetoothDevice.createBond();
    }
  }
  
  public Set<BluetoothDevice> queryPairedDevices()
  {
    return this.bluetoothAdapter.getBondedDevices();
  }
  
  /* Error */
  public void setConnectionStatusAndNotify(CONNECTION_STATE paramCONNECTION_STATE, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new 161	java/lang/StringBuilder
    //   5: astore 4
    //   7: aload 4
    //   9: ldc_w 528
    //   12: invokespecial 165	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   15: ldc -97
    //   17: aload 4
    //   19: aload_1
    //   20: invokevirtual 169	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   23: ldc_w 530
    //   26: invokevirtual 264	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   29: aload_0
    //   30: getfield 97	com/resmed/refresh/bluetooth/BluetoothSetup:connectionStatus	Lcom/resmed/refresh/bluetooth/CONNECTION_STATE;
    //   33: invokevirtual 169	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   36: invokevirtual 173	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   39: invokestatic 179	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   42: pop
    //   43: new 161	java/lang/StringBuilder
    //   46: astore 4
    //   48: aload 4
    //   50: ldc_w 528
    //   53: invokespecial 165	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   56: aload 4
    //   58: aload_1
    //   59: invokevirtual 169	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   62: ldc_w 530
    //   65: invokevirtual 264	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: aload_0
    //   69: getfield 97	com/resmed/refresh/bluetooth/BluetoothSetup:connectionStatus	Lcom/resmed/refresh/bluetooth/CONNECTION_STATE;
    //   72: invokevirtual 169	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   75: invokevirtual 173	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   78: invokestatic 157	com/resmed/refresh/utils/AppFileLog:addTrace	(Ljava/lang/String;)V
    //   81: iconst_0
    //   82: istore_3
    //   83: new 161	java/lang/StringBuilder
    //   86: astore 4
    //   88: aload 4
    //   90: ldc_w 532
    //   93: invokespecial 165	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   96: ldc -97
    //   98: aload 4
    //   100: invokestatic 250	com/resmed/refresh/bluetooth/BeDConnectionStatus:getInstance	()Lcom/resmed/refresh/bluetooth/BeDConnectionStatus;
    //   103: invokevirtual 535	com/resmed/refresh/bluetooth/BeDConnectionStatus:getState	()Lcom/resmed/refresh/bluetooth/CONNECTION_STATE;
    //   106: invokevirtual 169	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   109: invokevirtual 173	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   112: invokestatic 422	com/resmed/refresh/utils/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   115: pop
    //   116: invokestatic 540	com/resmed/refresh/ui/uibase/app/RefreshApplication:getInstance	()Lcom/resmed/refresh/ui/uibase/app/RefreshApplication;
    //   119: invokestatic 546	android/preference/PreferenceManager:getDefaultSharedPreferences	(Landroid/content/Context;)Landroid/content/SharedPreferences;
    //   122: astore 5
    //   124: aload_1
    //   125: getstatic 549	com/resmed/refresh/bluetooth/CONNECTION_STATE:SOCKET_BROKEN	Lcom/resmed/refresh/bluetooth/CONNECTION_STATE;
    //   128: if_acmpeq +10 -> 138
    //   131: aload_1
    //   132: getstatic 552	com/resmed/refresh/bluetooth/CONNECTION_STATE:BLUETOOTH_ON	Lcom/resmed/refresh/bluetooth/CONNECTION_STATE;
    //   135: if_acmpne +136 -> 271
    //   138: new 161	java/lang/StringBuilder
    //   141: astore 4
    //   143: aload 4
    //   145: ldc_w 554
    //   148: invokespecial 165	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   151: aload 4
    //   153: aload 5
    //   155: ldc_w 556
    //   158: iconst_m1
    //   159: invokeinterface 562 3 0
    //   164: invokevirtual 259	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   167: invokevirtual 173	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   170: invokestatic 157	com/resmed/refresh/utils/AppFileLog:addTrace	(Ljava/lang/String;)V
    //   173: iconst_1
    //   174: istore_3
    //   175: aload_0
    //   176: aload_1
    //   177: putfield 97	com/resmed/refresh/bluetooth/BluetoothSetup:connectionStatus	Lcom/resmed/refresh/bluetooth/CONNECTION_STATE;
    //   180: aload_1
    //   181: getstatic 552	com/resmed/refresh/bluetooth/CONNECTION_STATE:BLUETOOTH_ON	Lcom/resmed/refresh/bluetooth/CONNECTION_STATE;
    //   184: if_acmpne +7 -> 191
    //   187: iconst_1
    //   188: putstatic 63	com/resmed/refresh/bluetooth/BluetoothSetup:count	I
    //   191: aload_1
    //   192: getstatic 565	com/resmed/refresh/bluetooth/CONNECTION_STATE:SOCKET_CONNECTED	Lcom/resmed/refresh/bluetooth/CONNECTION_STATE;
    //   195: if_acmpne +19 -> 214
    //   198: aload_0
    //   199: getfield 82	com/resmed/refresh/bluetooth/BluetoothSetup:bluetoothService	Lcom/resmed/refresh/bluetooth/RefreshBluetoothServiceClient;
    //   202: invokeinterface 241 1 0
    //   207: ifnull +7 -> 214
    //   210: iconst_1
    //   211: putstatic 63	com/resmed/refresh/bluetooth/BluetoothSetup:count	I
    //   214: iload_2
    //   215: ifeq +45 -> 260
    //   218: new 125	android/content/Intent
    //   221: astore 4
    //   223: aload 4
    //   225: ldc_w 567
    //   228: invokespecial 390	android/content/Intent:<init>	(Ljava/lang/String;)V
    //   231: aload 4
    //   233: ldc_w 569
    //   236: aload_1
    //   237: invokevirtual 572	android/content/Intent:putExtra	(Ljava/lang/String;Ljava/io/Serializable;)Landroid/content/Intent;
    //   240: pop
    //   241: aload_0
    //   242: getfield 82	com/resmed/refresh/bluetooth/BluetoothSetup:bluetoothService	Lcom/resmed/refresh/bluetooth/RefreshBluetoothServiceClient;
    //   245: invokeinterface 241 1 0
    //   250: aload 4
    //   252: aconst_null
    //   253: aconst_null
    //   254: iconst_m1
    //   255: aconst_null
    //   256: aconst_null
    //   257: invokevirtual 576	android/content/Context:sendStickyOrderedBroadcast	(Landroid/content/Intent;Landroid/content/BroadcastReceiver;Landroid/os/Handler;ILjava/lang/String;Landroid/os/Bundle;)V
    //   260: iload_3
    //   261: ifeq +7 -> 268
    //   264: aload_0
    //   265: invokespecial 578	com/resmed/refresh/bluetooth/BluetoothSetup:reconnection	()V
    //   268: aload_0
    //   269: monitorexit
    //   270: return
    //   271: aload_0
    //   272: aload_1
    //   273: putfield 97	com/resmed/refresh/bluetooth/BluetoothSetup:connectionStatus	Lcom/resmed/refresh/bluetooth/CONNECTION_STATE;
    //   276: goto -96 -> 180
    //   279: astore_1
    //   280: aload_0
    //   281: monitorexit
    //   282: aload_1
    //   283: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	284	0	this	BluetoothSetup
    //   0	284	1	paramCONNECTION_STATE	CONNECTION_STATE
    //   0	284	2	paramBoolean	boolean
    //   82	179	3	i	int
    //   5	246	4	localObject	Object
    //   122	32	5	localSharedPreferences	android.content.SharedPreferences
    // Exception table:
    //   from	to	target	type
    //   2	81	279	finally
    //   83	138	279	finally
    //   138	173	279	finally
    //   175	180	279	finally
    //   180	191	279	finally
    //   191	214	279	finally
    //   218	260	279	finally
    //   264	268	279	finally
    //   271	276	279	finally
  }
  
  public void testStreamData(SleepSessionManager paramSleepSessionManager) {}
  
  public void unpairAll(String paramString)
  {
    Iterator localIterator = queryPairedDevices().iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      BluetoothDevice localBluetoothDevice = (BluetoothDevice)localIterator.next();
      Log.d("com.resmed.refresh.bluetooth", localBluetoothDevice.getName() + " to unpair  : " + localBluetoothDevice.getName());
      if (localBluetoothDevice.getName().contains(paramString))
      {
        boolean bool = unpairDevice(localBluetoothDevice);
        Log.d("com.resmed.refresh.bluetooth", localBluetoothDevice.getName() + " was unpaired ? : " + bool);
      }
    }
  }
  
  public boolean unpairDevice(BluetoothDevice paramBluetoothDevice)
  {
    Boolean localBoolean = Boolean.valueOf(false);
    try
    {
      paramBluetoothDevice = (Boolean)paramBluetoothDevice.getClass().getMethod("removeBond", null).invoke(paramBluetoothDevice, null);
      return paramBluetoothDevice.booleanValue();
    }
    catch (Exception paramBluetoothDevice)
    {
      for (;;)
      {
        Log.d("com.resmed.refresh.bluetooth", paramBluetoothDevice.getMessage());
        paramBluetoothDevice = localBoolean;
      }
    }
  }
  
  public void writeData(byte[] paramArrayOfByte)
  {
    try
    {
      if (this.mConnectedThread != null) {
        this.mConnectedThread.write(paramArrayOfByte);
      }
      return;
    }
    finally
    {
      paramArrayOfByte = finally;
      throw paramArrayOfByte;
    }
  }
  
  public static class AlarmReceiver
    extends BroadcastReceiver
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      Log.d("com.resmed.refresh.bluetooth", " BluetoothSetup$AlarmReceiver::onReceive()");
      AppFileLog.addTrace(" BluetoothSetup$AlarmReceiver alarm fired to RECONNECT ! ");
      AppFileLog.addTrace("BluetoothSetup$AlarmReceiver alarm fired to RECONNECT ! Bluetooth Status : " + BluetoothAdapter.getDefaultAdapter().isEnabled());
      ((PowerManager)paramContext.getSystemService("power")).newWakeLock(1, "MyWakeLock").acquire(10000L);
      if ((BluetoothAdapter.getDefaultAdapter().isEnabled()) && (!BeDConnectionStatus.getInstance().isSocketConnected()))
      {
        AppFileLog.addTrace("BluetoothSetup$AlarmReceiver alarm fired to RECONNECT ! Count : " + BluetoothSetup.count + ":isSocketConnected:" + BeDConnectionStatus.getInstance().isSocketConnected());
        BluetoothSetup.count += 1;
        if (BluetoothSetup.count < 31)
        {
          if (BluetoothSetup.count <= 20) {
            break label156;
          }
          BluetoothSetup.RegisterRepeatingReconnectAlarmWakeWithTimerReset(paramContext, 3600000L);
        }
      }
      for (;;)
      {
        paramContext.sendBroadcast(new Intent("BLUETOOTH_ALARM_RECONNECT"));
        return;
        label156:
        if (BluetoothSetup.count > 15)
        {
          BluetoothSetup.RegisterRepeatingReconnectAlarmWakeWithTimerReset(paramContext, 1800000L);
        }
        else if (BluetoothSetup.count > 10)
        {
          BluetoothSetup.RegisterRepeatingReconnectAlarmWakeWithTimerReset(paramContext, 600000L);
        }
        else if (BluetoothSetup.count > 5)
        {
          BluetoothSetup.RegisterRepeatingReconnectAlarmWakeWithTimerReset(paramContext, 300000L);
        }
        else if (BluetoothSetup.count > 1)
        {
          BluetoothSetup.RegisterRepeatingReconnectAlarmWakeWithTimerReset(paramContext, 60000L);
          continue;
          BluetoothSetup.count = 1;
        }
      }
    }
  }
  
  public class AlarmReconnectReceiver
    extends BroadcastReceiver
  {
    private RefreshBluetoothManager bluetoothManager;
    
    public AlarmReconnectReceiver(RefreshBluetoothManager paramRefreshBluetoothManager)
    {
      this.bluetoothManager = paramRefreshBluetoothManager;
    }
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      Log.d("com.resmed.refresh.bluetooth.alarm", "BluetoothSetup$AlarmReceiverReconnect::onReceive()");
      if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
        this.bluetoothManager.connectDevice(BluetoothSetup.this.device);
      }
    }
  }
  
  public class ConnectThread
    extends Thread
  {
    private BluetoothSocket mmSocket;
    private UUID uuid;
    
    public ConnectThread(BluetoothDevice paramBluetoothDevice, UUID paramUUID)
    {
      this.uuid = paramUUID;
      Log.d("com.resmed.refresh.bluetooth", "PAIRED DEVICE UUID : " + this.uuid);
      this.mmSocket = makeSocket(paramBluetoothDevice, paramUUID);
    }
    
    private BluetoothSocket makeFallbackSocket(BluetoothSocket paramBluetoothSocket)
      throws Exception
    {
      AppFileLog.addTrace("BluetoothSetup$ConnectThread::makeFallbackSocket()");
      return (BluetoothSocket)paramBluetoothSocket.getRemoteDevice().getClass().getMethod("createInsecureRfcommSocket", new Class[] { Integer.TYPE }).invoke(paramBluetoothSocket.getRemoteDevice(), new Object[] { Integer.valueOf(1) });
    }
    
    private BluetoothSocket makeSocket(BluetoothDevice paramBluetoothDevice, UUID paramUUID)
    {
      Object localObject = null;
      try
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>("Build.VERSION.SDK_INT : ");
        Log.d("com.resmed.refresh.bluetooth", Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= 17) {}
        for (paramBluetoothDevice = paramBluetoothDevice.createRfcommSocketToServiceRecord(paramUUID);; paramBluetoothDevice = (BluetoothSocket)paramBluetoothDevice.getClass().getMethod("createInsecureRfcommSocket", new Class[] { Integer.TYPE }).invoke(paramBluetoothDevice, new Object[] { Integer.valueOf(1) })) {
          return paramBluetoothDevice;
        }
      }
      catch (IOException paramBluetoothDevice)
      {
        for (;;)
        {
          paramBluetoothDevice.printStackTrace();
          paramBluetoothDevice = (BluetoothDevice)localObject;
        }
      }
      catch (NoSuchMethodException paramBluetoothDevice)
      {
        for (;;)
        {
          paramBluetoothDevice.printStackTrace();
          paramBluetoothDevice = (BluetoothDevice)localObject;
        }
      }
      catch (IllegalAccessException paramBluetoothDevice)
      {
        for (;;)
        {
          paramBluetoothDevice.printStackTrace();
          paramBluetoothDevice = (BluetoothDevice)localObject;
        }
      }
      catch (IllegalArgumentException paramBluetoothDevice)
      {
        for (;;)
        {
          paramBluetoothDevice.printStackTrace();
          paramBluetoothDevice = (BluetoothDevice)localObject;
        }
      }
      catch (InvocationTargetException paramBluetoothDevice)
      {
        for (;;)
        {
          paramBluetoothDevice.printStackTrace();
          paramBluetoothDevice = (BluetoothDevice)localObject;
        }
      }
    }
    
    public void cancel()
    {
      try
      {
        this.mmSocket.close();
        return;
      }
      catch (IOException localIOException)
      {
        for (;;) {}
      }
    }
    
    /* Error */
    public void run()
    {
      // Byte code:
      //   0: aload_0
      //   1: aload_0
      //   2: invokevirtual 144	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:getName	()Ljava/lang/String;
      //   5: invokevirtual 147	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:setName	(Ljava/lang/String;)V
      //   8: aload_0
      //   9: getfield 17	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:this$0	Lcom/resmed/refresh/bluetooth/BluetoothSetup;
      //   12: invokevirtual 151	com/resmed/refresh/bluetooth/BluetoothSetup:cancelDiscovery	()Z
      //   15: istore_1
      //   16: new 26	java/lang/StringBuilder
      //   19: astore_2
      //   20: aload_2
      //   21: ldc -103
      //   23: invokespecial 31	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   26: ldc -101
      //   28: aload_2
      //   29: iload_1
      //   30: invokevirtual 158	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
      //   33: ldc -96
      //   35: invokevirtual 163	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   38: aload_0
      //   39: getfield 51	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:mmSocket	Landroid/bluetooth/BluetoothSocket;
      //   42: invokevirtual 35	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   45: invokevirtual 39	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   48: invokestatic 45	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   51: pop
      //   52: aload_0
      //   53: getfield 51	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:mmSocket	Landroid/bluetooth/BluetoothSocket;
      //   56: invokevirtual 166	android/bluetooth/BluetoothSocket:connect	()V
      //   59: aload_0
      //   60: getfield 17	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:this$0	Lcom/resmed/refresh/bluetooth/BluetoothSetup;
      //   63: astore_3
      //   64: aload_3
      //   65: monitorenter
      //   66: aload_0
      //   67: getfield 17	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:this$0	Lcom/resmed/refresh/bluetooth/BluetoothSetup;
      //   70: aconst_null
      //   71: invokestatic 170	com/resmed/refresh/bluetooth/BluetoothSetup:access$0	(Lcom/resmed/refresh/bluetooth/BluetoothSetup;Lcom/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread;)V
      //   74: aload_3
      //   75: monitorexit
      //   76: aload_0
      //   77: getfield 51	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:mmSocket	Landroid/bluetooth/BluetoothSocket;
      //   80: ifnull +31 -> 111
      //   83: ldc -101
      //   85: new 26	java/lang/StringBuilder
      //   88: dup
      //   89: ldc -84
      //   91: invokespecial 31	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   94: aload_0
      //   95: getfield 51	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:mmSocket	Landroid/bluetooth/BluetoothSocket;
      //   98: invokevirtual 175	android/bluetooth/BluetoothSocket:isConnected	()Z
      //   101: invokevirtual 158	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
      //   104: invokevirtual 39	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   107: invokestatic 45	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   110: pop
      //   111: aload_0
      //   112: getfield 17	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:this$0	Lcom/resmed/refresh/bluetooth/BluetoothSetup;
      //   115: aload_0
      //   116: getfield 51	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:mmSocket	Landroid/bluetooth/BluetoothSocket;
      //   119: invokevirtual 179	com/resmed/refresh/bluetooth/BluetoothSetup:manageConnection	(Landroid/bluetooth/BluetoothSocket;)V
      //   122: return
      //   123: astore_2
      //   124: new 26	java/lang/StringBuilder
      //   127: astore_3
      //   128: aload_3
      //   129: ldc -75
      //   131: invokespecial 31	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   134: ldc 24
      //   136: aload_3
      //   137: aload_0
      //   138: getfield 51	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:mmSocket	Landroid/bluetooth/BluetoothSocket;
      //   141: invokevirtual 35	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   144: ldc -73
      //   146: invokevirtual 163	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   149: invokevirtual 39	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   152: invokestatic 45	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   155: pop
      //   156: new 26	java/lang/StringBuilder
      //   159: astore_3
      //   160: aload_3
      //   161: ldc -71
      //   163: invokespecial 31	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   166: aload_3
      //   167: aload_2
      //   168: invokevirtual 188	java/lang/NullPointerException:getMessage	()Ljava/lang/String;
      //   171: invokevirtual 163	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   174: invokevirtual 39	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   177: invokestatic 63	com/resmed/refresh/utils/AppFileLog:addTrace	(Ljava/lang/String;)V
      //   180: aload_2
      //   181: invokevirtual 189	java/lang/NullPointerException:printStackTrace	()V
      //   184: aload_0
      //   185: aload_0
      //   186: aload_0
      //   187: getfield 51	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:mmSocket	Landroid/bluetooth/BluetoothSocket;
      //   190: invokespecial 191	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:makeFallbackSocket	(Landroid/bluetooth/BluetoothSocket;)Landroid/bluetooth/BluetoothSocket;
      //   193: putfield 51	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:mmSocket	Landroid/bluetooth/BluetoothSocket;
      //   196: aload_0
      //   197: getfield 51	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:mmSocket	Landroid/bluetooth/BluetoothSocket;
      //   200: invokevirtual 166	android/bluetooth/BluetoothSocket:connect	()V
      //   203: goto -144 -> 59
      //   206: astore_3
      //   207: aload_3
      //   208: invokevirtual 192	java/lang/Exception:printStackTrace	()V
      //   211: new 102	java/io/IOException
      //   214: astore_3
      //   215: aload_3
      //   216: aload_2
      //   217: invokevirtual 188	java/lang/NullPointerException:getMessage	()Ljava/lang/String;
      //   220: invokespecial 193	java/io/IOException:<init>	(Ljava/lang/String;)V
      //   223: aload_3
      //   224: athrow
      //   225: astore_2
      //   226: aload_2
      //   227: invokevirtual 130	java/io/IOException:printStackTrace	()V
      //   230: new 26	java/lang/StringBuilder
      //   233: dup
      //   234: ldc -61
      //   236: invokespecial 31	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   239: aload_2
      //   240: invokevirtual 199	java/io/IOException:getStackTrace	()[Ljava/lang/StackTraceElement;
      //   243: invokevirtual 35	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   246: invokevirtual 39	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   249: invokestatic 63	com/resmed/refresh/utils/AppFileLog:addTrace	(Ljava/lang/String;)V
      //   252: aload_0
      //   253: getfield 51	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:mmSocket	Landroid/bluetooth/BluetoothSocket;
      //   256: invokevirtual 138	android/bluetooth/BluetoothSocket:close	()V
      //   259: aload_0
      //   260: getfield 17	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:this$0	Lcom/resmed/refresh/bluetooth/BluetoothSetup;
      //   263: getstatic 205	com/resmed/refresh/bluetooth/CONNECTION_STATE:SOCKET_BROKEN	Lcom/resmed/refresh/bluetooth/CONNECTION_STATE;
      //   266: iconst_0
      //   267: invokevirtual 209	com/resmed/refresh/bluetooth/BluetoothSetup:setConnectionStatusAndNotify	(Lcom/resmed/refresh/bluetooth/CONNECTION_STATE;Z)V
      //   270: aload_0
      //   271: getfield 17	com/resmed/refresh/bluetooth/BluetoothSetup$ConnectThread:this$0	Lcom/resmed/refresh/bluetooth/BluetoothSetup;
      //   274: aconst_null
      //   275: invokevirtual 179	com/resmed/refresh/bluetooth/BluetoothSetup:manageConnection	(Landroid/bluetooth/BluetoothSocket;)V
      //   278: goto -156 -> 122
      //   281: astore_2
      //   282: aload_2
      //   283: invokevirtual 130	java/io/IOException:printStackTrace	()V
      //   286: goto -27 -> 259
      //   289: astore_2
      //   290: aload_3
      //   291: monitorexit
      //   292: aload_2
      //   293: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	294	0	this	ConnectThread
      //   15	15	1	bool	boolean
      //   19	10	2	localStringBuilder	StringBuilder
      //   123	94	2	localNullPointerException	NullPointerException
      //   225	15	2	localIOException1	IOException
      //   281	2	2	localIOException2	IOException
      //   289	4	2	localObject1	Object
      //   206	2	3	localException	Exception
      //   214	77	3	localIOException3	IOException
      // Exception table:
      //   from	to	target	type
      //   52	59	123	java/lang/NullPointerException
      //   184	203	206	java/lang/Exception
      //   8	52	225	java/io/IOException
      //   52	59	225	java/io/IOException
      //   124	184	225	java/io/IOException
      //   184	203	225	java/io/IOException
      //   207	225	225	java/io/IOException
      //   252	259	281	java/io/IOException
      //   66	76	289	finally
      //   290	292	289	finally
    }
  }
  
  public class ConnectedThread
    extends Thread
  {
    private final int BUF_SIZE = 1024;
    private boolean isConnected = false;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final BluetoothSocket mmSocket;
    
    public ConnectedThread(BluetoothSocket paramBluetoothSocket)
      throws IOException
    {
      this.mmSocket = paramBluetoothSocket;
      this.mmInStream = paramBluetoothSocket.getInputStream();
      this.mmOutStream = paramBluetoothSocket.getOutputStream();
      Log.d("com.resmed.refresh.bluetooth", "Bluetooth ConnectedThread, streams available!");
    }
    
    private void processNewData(List<Byte> paramList)
    {
      if ((paramList.isEmpty()) || (paramList.size() == 1)) {
        return;
      }
      Log.d("com.resmed.refresh.bluetooth", "processNewData bluetooth data, COBS, : " + Arrays.toString(paramList.toArray()) + " size : " + paramList.toArray().length);
      ByteBuffer localByteBuffer = ByteBuffer.allocate(paramList.size());
      paramList = paramList.iterator();
      for (;;)
      {
        if (!paramList.hasNext())
        {
          paramList = localByteBuffer.array();
          Log.d("com.resmed.refresh.bluetooth", " bCobbed len : " + paramList.length);
          paramList = COBS.getInstance().decode(paramList);
          Log.d("com.resmed.refresh.bluetooth", "processNewData bluetooth data, DECOBBED, : " + Arrays.toString(paramList) + " size : " + paramList.length);
          if (paramList.length <= 1) {
            break;
          }
          paramList = ByteBuffer.wrap(paramList);
          BluetoothSetup.this.handleNewPacket(paramList);
          break;
        }
        localByteBuffer.put(((Byte)paramList.next()).byteValue());
      }
    }
    
    public void cancel()
    {
      try
      {
        Log.d("com.resmed.refresh.bluetooth", "closing bluetooth connection! ");
        this.mmSocket.close();
        return;
      }
      catch (IOException localIOException)
      {
        for (;;)
        {
          localIOException.printStackTrace();
        }
      }
    }
    
    public void run()
    {
      setName(getName());
      ArrayList localArrayList = new ArrayList();
      byte[] arrayOfByte = new byte['Ѐ'];
      Log.d("com.resmed.refresh.bluetooth", "bluetooth ConnectedThread, run()!");
      for (;;)
      {
        try
        {
          if (!this.isConnected)
          {
            this.mmInStream.available();
            this.isConnected = true;
            BluetoothSetup.this.setConnectionStatusAndNotify(CONNECTION_STATE.SOCKET_CONNECTED, true);
            BluetoothSetup.count = 1;
            BluetoothSetup.CancelRepeatingReconnectAlarmWake(BluetoothSetup.this.bluetoothService.getContext());
          }
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>(" BluetoothSetup$ConnectedThread::run() available bytes : ");
          Log.d("com.resmed.refresh.bluetooth", this.mmInStream.available());
          int j = this.mmInStream.read(arrayOfByte);
          if (j <= 0) {
            continue;
          }
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>("bluetooth ConnectedThread::run() bytes read : ");
          Log.d("com.resmed.refresh.bluetooth", j + " byte array :" + Arrays.toString(arrayOfByte));
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>(" BluetoothSetup$ConnectedThread::run() bytes read : ");
          AppFileLog.addTrace(j);
          if (j > 1024) {
            continue;
          }
          i = 0;
          if (i < j) {}
        }
        catch (IOException localIOException1)
        {
          int i;
          BluetoothSetup.this.setConnectionStatusAndNotify(CONNECTION_STATE.SOCKET_BROKEN, true);
          Log.d("com.resmed.refresh.bluetooth", "bluetooth ConnectedThread, IOException :" + localIOException1);
          localIOException1.printStackTrace();
          return;
        }
        try
        {
          arrayOfByte = new byte['Ѐ'];
        }
        catch (IOException localIOException2)
        {
          continue;
          continue;
        }
        localArrayList.add(Byte.valueOf(arrayOfByte[i]));
        if (arrayOfByte[i] != 0) {
          break label305;
        }
        processNewData(localArrayList);
        localArrayList.clear();
        localArrayList = new ArrayList();
        i++;
      }
    }
    
    public void write(byte[] paramArrayOfByte)
    {
      try
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>(" bluetooth bytes to write : ");
        Log.d("com.resmed.refresh.bluetooth", paramArrayOfByte);
        this.mmOutStream.write(paramArrayOfByte);
        this.mmOutStream.flush();
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>(" bluetooth bytes written : ");
        Log.d("com.resmed.refresh.bluetooth", paramArrayOfByte);
        return;
      }
      catch (IOException paramArrayOfByte)
      {
        for (;;)
        {
          paramArrayOfByte.printStackTrace();
        }
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\bluetooth\BluetoothSetup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
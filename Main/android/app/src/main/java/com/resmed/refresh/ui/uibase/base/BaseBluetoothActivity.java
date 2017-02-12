package com.resmed.refresh.ui.uibase.base;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.view.Menu;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.resmed.cobs.COBS;
import com.resmed.refresh.bed.BedCommandsRPCMapper;
import com.resmed.refresh.bed.BedDefaultRPCMapper;
import com.resmed.refresh.bluetooth.BluetoothDataWriter;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.bluetooth.RefreshBluetoothService;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.model.json.ResultRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLP;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.ui.activity.ReconnectionProblemActivty;
import com.resmed.refresh.ui.activity.UpdateOTAActivity;
import com.resmed.refresh.ui.fragments.BedConnectingDialog;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.utils.CustomDialogBuilder;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.BluetoothDataSerializeUtil;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.refresh.utils.RefreshUserPreferencesData;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseBluetoothActivity
  extends BaseActivity
  implements BluetoothDataListener, BluetoothDataWriter, BeDPickerDialog.OnPickedDevice, BedConnectingDialog.OnTimeoutConnectingDialog
{
  public static boolean CORRECT_FIRMWARE_VERSION = true;
  private static Map<Integer, JsonRPC> CommandStack = new LinkedHashMap();
  public static boolean IN_SLEEP_SESSION = false;
  protected static final int REQUEST_ENABLE_BT = 161;
  private static BedCommandsRPCMapper RpcCommands = BedDefaultRPCMapper.getInstance();
  public static boolean UPDATING_FIRMWARE = false;
  private static boolean connectingToBeD = false;
  private static Messenger mService;
  private static boolean userAllowBluetooth = true;
  protected BroadcastReceiver awakePowerSaveReconnect = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      Log.d("com.resmed.refresh.bluetooth.alarm", "BaseBluetoothActivity#AlarmReconnectReceiver::onReceive()");
      BaseBluetoothActivity.this.bindToService();
    }
  };
  private JsonRPC bioSensorSerialNrRPC;
  protected Intent bluetoothManagerService;
  protected BroadcastReceiver bluetoothServiceRestartReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      BaseBluetoothActivity.this.registerToService();
    }
  };
  private boolean connectionProgressDisplayed = false;
  protected BroadcastReceiver connectionStatusReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = (CONNECTION_STATE)paramAnonymousIntent.getExtras().get("EXTRA_RESMED_CONNECTION_STATE");
      BaseBluetoothActivity.this.handleConnectionStatus(paramAnonymousContext);
    }
  };
  private CONNECTION_STATE currentState;
  protected Menu mBarMenu;
  protected boolean mBound;
  private ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      Log.d("com.resmed.refresh.ui", ": bluetooth service bound! : ");
      BaseBluetoothActivity.mService = new Messenger(paramAnonymousIBinder);
      BaseBluetoothActivity.this.mBound = true;
      BaseBluetoothActivity.this.registerToService();
      AppFileLog.addTrace(": bluetooth service bound! : ");
    }
    
    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      Log.d("com.resmed.refresh.ui", ": bluetooth service unbound! : ");
      BaseBluetoothActivity.mService = null;
      BaseBluetoothActivity.this.mBound = false;
      AppFileLog.addTrace(": bluetooth service unbound! : ");
    }
  };
  private BluetoothDevice mDevice;
  protected Messenger mFromService = new Messenger(new IncomingHandler());
  private List<BroadcastReceiver> receivers;
  
  static
  {
    IN_SLEEP_SESSION = false;
  }
  
  public static BedCommandsRPCMapper getRpcCommands()
  {
    return RpcCommands;
  }
  
  private void handleErrorRPC(JsonRPC paramJsonRPC, JsonRPC.ErrorRpc paramErrorRpc)
  {
    if ((paramJsonRPC != null) && (paramErrorRpc != null))
    {
      paramJsonRPC = paramJsonRPC.getRPCallback();
      if (paramJsonRPC != null) {
        paramJsonRPC.onError(paramErrorRpc);
      }
    }
  }
  
  private void handleHearBeat(byte[] paramArrayOfByte)
  {
    int i = PacketsByteValuesReader.getStoreLocalBio(paramArrayOfByte);
    int j = PacketsByteValuesReader.getStoreLocalEnv(paramArrayOfByte);
    Log.d("com.resmed.refresh.ui", "countBio : " + i + " countEnv :" + j);
  }
  
  private void sendMessageToService(final int paramInt, final Bundle paramBundle)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          Thread.sleep(1000L);
          Message localMessage = new Message();
          localMessage.what = paramInt;
          localMessage.setData(paramBundle);
          BaseBluetoothActivity.this.sendMsgBluetoothService(localMessage);
          return;
        }
        catch (InterruptedException localInterruptedException)
        {
          for (;;)
          {
            localInterruptedException.printStackTrace();
          }
        }
      }
    }).start();
  }
  
  private boolean sendRPC(JsonRPC paramJsonRPC)
  {
    boolean bool;
    if (getRpcCommands() == null) {
      bool = false;
    }
    for (;;)
    {
      return bool;
      Message localMessage = new Message();
      Object localObject1 = new Gson();
      getRpcCommands().setRPCid(getRpcCommands().getRPCid() + 1);
      paramJsonRPC.setId(Integer.valueOf(getRpcCommands().getRPCid()));
      AppFileLog.addTrace("OUT NEW RPC ID : " + getRpcCommands().getRPCid() + " METHOD :" + paramJsonRPC.getMethod() + " PARAMS : " + paramJsonRPC.getParams());
      localObject1 = ((Gson)localObject1).toJson(paramJsonRPC);
      Log.d("com.resmed.refresh.ui", "bluetooth json rpc : " + (String)localObject1);
      localObject1 = VLP.getInstance().Packetize((byte)VLPacketType.PACKET_TYPE_CALL.ordinal(), new Integer(paramJsonRPC.getId()).byteValue(), ((String)localObject1).getBytes().length, 64, ((String)localObject1).getBytes());
      localObject1 = COBS.getInstance().encode(((ByteBuffer)localObject1).array());
      Log.d("com.resmed.refresh.bluetooth", " COBSJNI, encodedByteBuff : " + Arrays.toString((byte[])localObject1));
      localObject1 = ByteBuffer.wrap((byte[])localObject1);
      Object localObject2 = paramJsonRPC.getRPCallback();
      if (localObject2 != null) {
        ((JsonRPC.RPCallback)localObject2).preExecute();
      }
      localObject2 = new Bundle();
      ((Bundle)localObject2).putByteArray("bytes", ((ByteBuffer)localObject1).array());
      localMessage.setData((Bundle)localObject2);
      localMessage.what = 2;
      new RefreshUserPreferencesData(getApplicationContext()).setIntegerConfigValue("PREF_LAST_RPC_ID_USED", Integer.valueOf(paramJsonRPC.getId()));
      try
      {
        if (mService != null) {
          mService.send(localMessage);
        }
        bool = true;
      }
      catch (RemoteException paramJsonRPC)
      {
        paramJsonRPC.printStackTrace();
        bindToService();
        bool = false;
      }
    }
  }
  
  private void showConnectionProgress()
  {
    try
    {
      if (isActivityReadyToCommit())
      {
        Log.d("com.resmed.refresh.dialog", "showConnectionProgress()");
        FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
        Object localObject2 = getFragmentManager().findFragmentByTag("dialog");
        if (localObject2 != null)
        {
          ((DialogFragment)localObject2).dismiss();
          Log.d("com.resmed.refresh.pair", "ft.remove(dialog");
        }
        getFragmentManager().executePendingTransactions();
        localFragmentTransaction.addToBackStack(null);
        localObject2 = BedConnectingDialog.newInstance();
        ((BedConnectingDialog)localObject2).setOnTimeoutConnectingDialog(this);
        ((DialogFragment)localObject2).show(localFragmentTransaction, "dialog");
        Log.d("com.resmed.refresh.dialog", "showConnectionProgress() connectionProgressDisplayed = true");
        this.connectionProgressDisplayed = true;
      }
      return;
    }
    finally {}
  }
  
  private void showReconnectionScreen()
  {
    if ((!isFinishing()) && (checkBluetoothEnabled()))
    {
      startActivity(new Intent(this, ReconnectionProblemActivty.class));
      overridePendingTransition(2130968597, 2130968586);
    }
  }
  
  public void bindToService()
  {
    Log.d("com.resmed.refresh.ui", "binding to bluetooth service! isRunning :" + isBluetoothServiceRunning() + "service intent : " + this.bluetoothManagerService + " mConnection : " + this.mConnection);
    AppFileLog.addTrace("BaseBluetoothActivity::bindToService!");
    if (!this.mBound) {
      getApplicationContext().bindService(this.bluetoothManagerService, this.mConnection, 1);
    }
  }
  
  public boolean checkBluetoothEnabled()
  {
    return checkBluetoothEnabled(false);
  }
  
  public boolean checkBluetoothEnabled(boolean paramBoolean)
  {
    if (paramBoolean) {
      userAllowBluetooth = true;
    }
    BluetoothAdapter localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if ((!localBluetoothAdapter.isEnabled()) && (userAllowBluetooth))
    {
      Intent localIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
      localIntent.setFlags(2097152);
      startActivityForResult(localIntent, 161);
    }
    for (;;)
    {
      return localBluetoothAdapter.isEnabled();
      if (localBluetoothAdapter.isEnabled()) {
        userAllowBluetooth = true;
      }
    }
  }
  
  protected boolean checkForFirmwareUpgrade(String paramString)
  {
    Log.d("com.resmed.refresh.ui", "checkForFirmwareUpgrade firmware version :" + paramString);
    String str = RefreshTools.getFirmwareBinaryVersion(getApplicationContext());
    if (RefreshTools.compareFirmwareVersions(paramString.replace("Release", "").split(" ")[0], str) < 0) {}
    for (boolean bool = true;; bool = false)
    {
      if (bool)
      {
        CORRECT_FIRMWARE_VERSION = false;
        startActivity(new Intent(this, UpdateOTAActivity.class));
      }
      return bool;
    }
  }
  
  public boolean connectToBeD(boolean paramBoolean)
  {
    boolean bool2 = false;
    Log.d("com.resmed.refresh.pair", "connectToBeD(" + paramBoolean + ")");
    BluetoothDevice localBluetoothDevice = BluetoothDataSerializeUtil.readJsonFile(getApplicationContext());
    Log.d("com.resmed.refresh.pair", "connectToBeD mDevice : " + localBluetoothDevice);
    if (localBluetoothDevice == null)
    {
      Log.d("com.resmed.refresh.pair", "connectToBeD no mDevice stored");
      bool1 = bool2;
      if (paramBoolean)
      {
        Log.d("com.resmed.refresh.dialog", "connectToBeD showBeDPickerDialog()");
        showBeDPickerDialog();
      }
    }
    for (boolean bool1 = bool2;; bool1 = true)
    {
      return bool1;
      Log.d("com.resmed.refresh.pair", "connectToBeD sending message to service : ");
      Bundle localBundle = new Bundle();
      localBundle.putParcelable(getString(2131165303), localBluetoothDevice);
      localBundle.putBoolean(getString(2131165304), false);
      sendMessageToService(11, localBundle);
      if (paramBoolean) {
        showConnectionProgress();
      }
    }
  }
  
  public void disconnectBluetoothConn()
  {
    Message localMessage = new Message();
    localMessage.what = 7;
    sendMsgBluetoothService(localMessage);
    localMessage = new Message();
    localMessage.what = 9;
    sendMsgBluetoothService(localMessage);
    handleConnectionStatus(CONNECTION_STATE.SOCKET_NOT_CONNECTED);
  }
  
  protected void dismissConnectionProgress()
  {
    try
    {
      Log.d("com.resmed.refresh.dialog", "RefreshDialogFragment dismissConnectionProgress");
      if (isActivityReadyToCommit())
      {
        Fragment localFragment = getFragmentManager().findFragmentByTag("dialog");
        if (localFragment != null) {
          ((DialogFragment)localFragment).dismiss();
        }
        Log.d("com.resmed.refresh.dialog", "dismissConnectionProgress() connectionProgressDisplayed = false");
        this.connectionProgressDisplayed = false;
      }
      return;
    }
    finally {}
  }
  
  public void handleBreathingRate(Bundle paramBundle) {}
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    Log.d("com.resmed.refresh.pair", "    BaseBluetoothActivity::handleConnectionStatus() connState=" + paramCONNECTION_STATE + " UPDATING_FIRMWARE:" + UPDATING_FIRMWARE + " mBound : " + this.mBound + " isAvailable:" + this.isAvailable + " currentState : " + this.currentState);
    if ((paramCONNECTION_STATE == null) || (UPDATING_FIRMWARE) || (!this.mBound) || (!this.isAvailable) || (this.currentState == paramCONNECTION_STATE)) {
      return;
    }
    this.currentState = paramCONNECTION_STATE;
    Log.d("com.resmed.refresh.pair", "    handleConnectionStatus connState=" + paramCONNECTION_STATE);
    RefreshApplication.getInstance().setCurrentConnectionState(paramCONNECTION_STATE);
    if (CONNECTION_STATE.SOCKET_CONNECTED == paramCONNECTION_STATE)
    {
      sendRpcToBed(RpcCommands.openSession(RefreshModelController.getInstance().getUserSessionID()));
      BluetoothDataSerializeUtil.writeJsonFile(getApplicationContext(), this.mDevice);
    }
    StringBuilder localStringBuilder = new StringBuilder("Should show device paired? ").append(connectingToBeD).append(" ");
    boolean bool;
    if (CONNECTION_STATE.SESSION_OPENED == paramCONNECTION_STATE)
    {
      bool = true;
      label213:
      Log.d("com.resmed.refresh.dialog", bool);
      if ((!connectingToBeD) || (CONNECTION_STATE.SESSION_OPENED != paramCONNECTION_STATE) || (!isActivityReadyToCommit())) {
        break label393;
      }
      Log.d("com.resmed.refresh.pair", "handleConnectionStatus SOCKET_CONNECTED connectingToBeD=" + connectingToBeD);
      connectingToBeD = false;
      Log.d("com.resmed.refresh.dialog", "showDialog device paired");
      showDialog(new CustomDialogBuilder(this).title(2131165891).setPositiveButton(2131165892, null), false);
    }
    for (;;)
    {
      Log.d("com.resmed.refresh.dialog", "isReady = " + RefreshApplication.getInstance().getConnectionStatus().isReady() + " connectionProgressDisplayed=" + this.connectionProgressDisplayed);
      updateConnectionIcon();
      if ((CONNECTION_STATE.SOCKET_BROKEN != paramCONNECTION_STATE) && (CONNECTION_STATE.SOCKET_RECONNECTING != paramCONNECTION_STATE)) {
        break;
      }
      CORRECT_FIRMWARE_VERSION = true;
      UPDATING_FIRMWARE = false;
      break;
      bool = false;
      break label213;
      label393:
      if ((RefreshApplication.getInstance().getConnectionStatus().isReady()) && (this.connectionProgressDisplayed))
      {
        dismissConnectionProgress();
        Log.d("com.resmed.refresh.dialog", "dismissConnectionProgress");
      }
    }
  }
  
  public void handleEnvSample(Bundle paramBundle)
  {
    Log.d("com.resmed.refresh.ui", "handleEnvSample() ");
  }
  
  public void handleReceivedRpc(JsonRPC paramJsonRPC)
  {
    if (paramJsonRPC != null)
    {
      final Object localObject = paramJsonRPC.getResult();
      if (localObject != null)
      {
        SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        String str2 = ((ResultRPC)localObject).getPayload();
        if (str2.contains("$"))
        {
          int i = str2.indexOf("$");
          String str1 = str2.substring(0, i);
          localObject = str2.substring(i + 1, str2.length() - 1);
          Log.d("com.resmed.refresh.bluetooth", "BaseBluetoothActivity::handlePayload, boardVersion : " + str1 + " firmwareVersion : " + (String)localObject);
          RefreshModelController.getInstance().saveBoardVersion(str1);
          RefreshModelController.getInstance().saveFirmwareVersion((String)localObject);
          if (CORRECT_FIRMWARE_VERSION)
          {
            localEditor.putString(getString(2131165305), str1);
            localEditor.commit();
            this.bioSensorSerialNrRPC = getRpcCommands().getBioSensorSerialNumber();
            this.bioSensorSerialNrRPC.setRPCallback(new JsonRPC.RPCallback()
            {
              public void execute()
              {
                Log.d("com.resmed.refresh.ui", "handleReceivedRpc firmwareVersion :" + localObject);
                if (RefreshTools.checkForFirmwareUpgrade(BaseBluetoothActivity.this, localObject))
                {
                  BaseBluetoothActivity.CORRECT_FIRMWARE_VERSION = false;
                  Intent localIntent = new Intent(BaseBluetoothActivity.this, UpdateOTAActivity.class);
                  BaseBluetoothActivity.this.startActivity(localIntent);
                }
              }
              
              public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {}
              
              public void preExecute() {}
            });
            sendRpcToBed(this.bioSensorSerialNrRPC);
          }
        }
        Log.d("com.resmed.refresh.ui", "handleReceivedRpc committing  extra_bed_sensor_serial:" + str2);
        Log.d("com.resmed.refresh.ui", "handleReceivedRpc committing  bioSensorSerialNrRPC:" + this.bioSensorSerialNrRPC + " receivedRPC.getId() :" + paramJsonRPC.getId() + " CORRECT_FIRMWARE_VERSION " + CORRECT_FIRMWARE_VERSION);
        if ((this.bioSensorSerialNrRPC != null) && (this.bioSensorSerialNrRPC.getId() == paramJsonRPC.getId()) && (CORRECT_FIRMWARE_VERSION))
        {
          localEditor.putString(getString(2131165306), str2);
          RefreshModelController.getInstance().setBedId(str2);
          localEditor.commit();
        }
      }
    }
  }
  
  public void handleSessionRecovered(Bundle paramBundle)
  {
    Log.d("com.resmed.refresh.ui", " BaseBluetoothActivity::handleSessionRecovered()");
  }
  
  public void handleSleepSessionStopped(Bundle paramBundle) {}
  
  public void handleStreamPacket(Bundle paramBundle)
  {
    for (;;)
    {
      int i;
      int j;
      try
      {
        Object localObject1 = paramBundle.getByteArray("REFRESH_BED_NEW_DATA");
        i = paramBundle.getByte("REFRESH_BED_NEW_DATA_TYPE");
        j = localObject1.length;
        if (j == 0) {
          return;
        }
        if (VLPacketType.PACKET_TYPE_RETURN.ordinal() == i)
        {
          paramBundle = new java/lang/String;
          paramBundle.<init>((byte[])localObject1);
          localObject1 = new com/google/gson/Gson;
          ((Gson)localObject1).<init>();
          try
          {
            localObject1 = (JsonRPC)((Gson)localObject1).fromJson(paramBundle, JsonRPC.class);
            localObject2 = new java.lang.StringBuilder;
            ((StringBuilder)localObject2).<init>(" handleStreamPacket() rpc id : ");
            Log.d("com.resmed.refresh.ui", ((JsonRPC)localObject1).getId() + " strPacket : " + paramBundle);
            handleReceivedRpc((JsonRPC)localObject1);
            localObject2 = (JsonRPC)CommandStack.remove(Integer.valueOf(((JsonRPC)localObject1).getId()));
            Object localObject3 = new java.lang.StringBuilder;
            ((StringBuilder)localObject3).<init>(" handleStreamPacket() rpcSent : ");
            Log.d("com.resmed.refresh.ui", localObject2);
            if (localObject2 == null) {
              continue;
            }
            localObject3 = ((JsonRPC)localObject2).getRPCallback();
            StringBuilder localStringBuilder = new java.lang.StringBuilder;
            localStringBuilder.<init>(" handleStreamPacket() callback : ");
            Log.d("com.resmed.refresh.ui", localObject3);
            if (((JsonRPC)localObject2).getRPCallback() != null) {
              ((JsonRPC.RPCallback)localObject3).execute();
            }
            localObject1 = ((JsonRPC)localObject1).getError();
            if (localObject1 == null) {
              continue;
            }
            handleErrorRPC((JsonRPC)localObject2, (JsonRPC.ErrorRpc)localObject1);
          }
          catch (JsonSyntaxException localJsonSyntaxException)
          {
            Object localObject2 = new java.lang.StringBuilder;
            ((StringBuilder)localObject2).<init>(" strPacket : ");
            Log.w("com.resmed.refresh.ui", paramBundle + "  " + localJsonSyntaxException.getMessage());
          }
          continue;
        }
        if (VLPacketType.PACKET_TYPE_NOTE_HEARTBEAT.ordinal() != i) {
          break label350;
        }
      }
      finally {}
      paramBundle = new java.lang.StringBuilder;
      paramBundle.<init>("BaseBluetoothActivity::handleStreamPacket() processed new bluetooth PACKET_TYPE_NOTE_HEARTBEAT : ");
      Log.d("com.resmed.refresh.ui", Arrays.toString(localJsonSyntaxException));
      handleHearBeat(localJsonSyntaxException);
      continue;
      label350:
      if (VLPacketType.PACKET_TYPE_NOTE_BIO_1.ordinal() != i) {
        if ((VLPacketType.PACKET_TYPE_NOTE_STORE_FOREIGN.ordinal() == i) || (VLPacketType.PACKET_TYPE_NOTE_STORE_LOCAL.ordinal() == i))
        {
          j = PacketsByteValuesReader.getStoreLocalBio(localJsonSyntaxException);
          paramBundle = new java.lang.StringBuilder;
          paramBundle.<init>("PACKET_TYPE_NOTE_STORE!!! = ");
          Log.w("com.resmed.refresh.ui", i + " NUMBER OF SAMPLES : " + j);
          if (j >= 32)
          {
            updateDataStoredFlag(i);
          }
          else
          {
            paramBundle = new java.lang.StringBuilder;
            paramBundle.<init>("Ignoring samples on BeD because : ");
            AppFileLog.addTrace(j + " samples < 32");
          }
        }
        else
        {
          paramBundle = new java.lang.StringBuilder;
          paramBundle.<init>("BaseBluetoothActivity::handleStreamPacket() processed new bluetooth PACKET_TYPE_");
          Log.d("com.resmed.refresh.ui", i + " bytes : " + Arrays.toString(localJsonSyntaxException));
        }
      }
    }
  }
  
  public void handleUserSleepState(Bundle paramBundle) {}
  
  protected boolean isBluetoothServiceRunning()
  {
    Iterator localIterator = ((ActivityManager)getSystemService("activity")).getRunningServices(Integer.MAX_VALUE).iterator();
    if (!localIterator.hasNext()) {}
    for (boolean bool = false;; bool = true)
    {
      return bool;
      ActivityManager.RunningServiceInfo localRunningServiceInfo = (ActivityManager.RunningServiceInfo)localIterator.next();
      if (!RefreshBluetoothService.class.getName().equals(localRunningServiceInfo.service.getClassName())) {
        break;
      }
    }
  }
  
  public boolean isBoundToBluetoothService()
  {
    return this.mBound;
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt1, paramIntent);
    if (paramInt1 == 161) {
      switch (paramInt2)
      {
      }
    }
    for (;;)
    {
      return;
      userAllowBluetooth = true;
      continue;
      userAllowBluetooth = false;
      disconnectBluetoothConn();
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    RpcCommands.setContextBroadcaster(this);
    this.receivers = new ArrayList();
    boolean bool = isBluetoothServiceRunning();
    Log.d("com.resmed.refresh.ui", ": bluetooth service is running : " + bool);
    this.bluetoothManagerService = new Intent(this, RefreshBluetoothService.class);
    if (!bool)
    {
      paramBundle = getIntent().getStringExtra(getString(2131165943));
      String str = getIntent().getStringExtra(getString(2131165944));
      if ((paramBundle != null) && (str != null))
      {
        this.bluetoothManagerService.putExtra(getString(2131165943), paramBundle);
        this.bluetoothManagerService.putExtra(getString(2131165944), str);
      }
      startService(this.bluetoothManagerService);
    }
    paramBundle = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    int i = paramBundle.getInt("PREF_CONNECTION_STATE", -1);
    Log.d("com.resmed.refresh.ui", " SleepTimeActivity::onResume conn state : " + i);
    if (i == CONNECTION_STATE.NIGHT_TRACK_ON.ordinal())
    {
      i = paramBundle.getInt("PREF_LAST_RPC_ID_USED", -1);
      if (-1 != i) {
        RpcCommands.setRPCid(i);
      }
    }
    registerReceiver(this.connectionStatusReceiver, new IntentFilter("ACTION_RESMED_CONNECTION_STATUS"));
    registerReceiver(this.bluetoothServiceRestartReceiver, new IntentFilter("BLUETOOTH_SERVICE_INTENT_RESTART"));
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    unregisterAll();
  }
  
  protected void onPause()
  {
    super.onPause();
    unBindFromService();
    boolean bool = ((PowerManager)getSystemService("power")).isScreenOn();
    Log.d("com.resmed.refresh.ui", " BaseBluetoothActivity::onPause() isScreenOn : " + bool);
    if ((!bool) && (IN_SLEEP_SESSION))
    {
      Object localObject = Calendar.getInstance();
      ((Calendar)localObject).add(13, 600);
      long l = ((Calendar)localObject).getTimeInMillis();
      localObject = new Intent(getApplicationContext(), BluetoothSetup.AlarmReceiver.class);
      ((AlarmManager)getApplicationContext().getSystemService("alarm")).set(0, Long.valueOf(l).longValue(), PendingIntent.getBroadcast(getApplicationContext(), 0, (Intent)localObject, 134217728));
      registerReceiver(this.awakePowerSaveReconnect, new IntentFilter("BLUETOOTH_ALARM_RECONNECT"));
    }
  }
  
  public void onPickedDevice(BluetoothDevice paramBluetoothDevice)
  {
    Log.d("com.resmed.refresh.pair", "onPickedDevice");
    Log.d("com.resmed.refresh.dialog", "onPickedDevice() connectionProgressDisplayed = true");
    this.connectionProgressDisplayed = true;
    pairAndConnect(paramBluetoothDevice);
  }
  
  protected void onResume()
  {
    super.onResume();
    bindToService();
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    Log.d("com.resmed.refresh.dialog", "BaseBluetoothActivity onSaveInstanceState " + this);
  }
  
  protected void onStart()
  {
    super.onStart();
  }
  
  protected void onStop()
  {
    super.onStop();
    Log.d("com.resmed.refresh.ui", " BaseBluetoothActivity::onStop() ");
  }
  
  public void onTimeoutConnectingDialog()
  {
    showReconnectionScreen();
  }
  
  protected void pairAndConnect(BluetoothDevice paramBluetoothDevice)
  {
    Log.d("com.resmed.refresh.pair", "connectAndPair");
    this.mDevice = paramBluetoothDevice;
    connectingToBeD = true;
    Bundle localBundle = new Bundle();
    localBundle.putParcelable(getString(2131165303), paramBluetoothDevice);
    localBundle.putBoolean(getString(2131165304), true);
    sendMessageToService(11, localBundle);
    showConnectionProgress();
  }
  
  public Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter)
  {
    if (!this.receivers.contains(paramBroadcastReceiver)) {
      this.receivers.add(paramBroadcastReceiver);
    }
    for (paramBroadcastReceiver = super.registerReceiver(paramBroadcastReceiver, paramIntentFilter);; paramBroadcastReceiver = null) {
      return paramBroadcastReceiver;
    }
  }
  
  protected void registerToService()
  {
    Log.d("com.resmed.refresh.bluetooth", " registerToService : ");
    try
    {
      Message localMessage = Message.obtain(null, 4);
      localMessage.replyTo = this.mFromService;
      if (mService != null) {
        mService.send(localMessage);
      }
      handleConnectionStatus(RefreshApplication.getInstance().getCurrentConnectionState());
      localMessage = new Message();
      localMessage.what = 27;
      sendMsgBluetoothService(localMessage);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      for (;;)
      {
        Log.d("com.resmed.refresh.bluetooth", " RemoteException : " + localRemoteException.getMessage());
      }
    }
  }
  
  public boolean sendBytesToBeD(byte[] paramArrayOfByte, VLPacketType paramVLPacketType)
  {
    boolean bool2 = true;
    int i = 0;
    if (paramArrayOfByte != null) {
      i = paramArrayOfByte.length;
    }
    paramArrayOfByte = VLP.getInstance().Packetize((byte)paramVLPacketType.ordinal(), (byte)1, i, 64, paramArrayOfByte);
    paramVLPacketType = ByteBuffer.wrap(COBS.getInstance().encode(paramArrayOfByte.array()));
    paramArrayOfByte = new Message();
    paramArrayOfByte.what = 2;
    Bundle localBundle = new Bundle();
    localBundle.putByteArray("bytes", paramVLPacketType.array());
    paramArrayOfByte.setData(localBundle);
    bool1 = bool2;
    try
    {
      if (mService != null)
      {
        mService.send(paramArrayOfByte);
        bool1 = bool2;
      }
    }
    catch (RemoteException paramArrayOfByte)
    {
      for (;;)
      {
        paramArrayOfByte.printStackTrace();
        bool1 = false;
      }
    }
    return bool1;
  }
  
  public void sendMsgBluetoothService(Message paramMessage)
  {
    Log.d("com.resmed.refresh.ui", " sendMsgBluetoothService msg : " + paramMessage + "mService : " + mService);
    if (mService == null) {}
    for (;;)
    {
      return;
      try
      {
        mService.send(paramMessage);
      }
      catch (RemoteException localRemoteException)
      {
        Log.d("com.resmed.refresh.ui", " RemoteException e : " + localRemoteException.getMessage());
        localRemoteException.printStackTrace();
        bindToService();
        try
        {
          Thread.sleep(100L);
          mService.send(paramMessage);
        }
        catch (InterruptedException paramMessage)
        {
          paramMessage.printStackTrace();
        }
        catch (RemoteException paramMessage)
        {
          paramMessage.printStackTrace();
        }
      }
    }
  }
  
  public void sendRpcToBed(JsonRPC paramJsonRPC)
  {
    try
    {
      boolean bool = sendRPC(paramJsonRPC);
      StringBuilder localStringBuilder = new java.lang.StringBuilder;
      localStringBuilder.<init>(" BaseBluetoothActivity::sendRpcToBed(rpc) rpc : ");
      AppFileLog.addTrace(paramJsonRPC + " wasSent : " + bool + " mBound : " + this.mBound);
      localStringBuilder = new java.lang.StringBuilder;
      localStringBuilder.<init>("sendRpcToBed wasSent : ");
      Log.d("com.resmed.refresh.ui", bool);
      if (bool) {
        CommandStack.put(Integer.valueOf(paramJsonRPC.getId()), paramJsonRPC);
      }
      return;
    }
    finally {}
  }
  
  /* Error */
  public void showBeDPickerDialog()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: invokestatic 470	android/bluetooth/BluetoothAdapter:getDefaultAdapter	()Landroid/bluetooth/BluetoothAdapter;
    //   5: invokevirtual 473	android/bluetooth/BluetoothAdapter:isEnabled	()Z
    //   8: ifeq +210 -> 218
    //   11: ldc_w 361
    //   14: ldc_w 1112
    //   17: invokestatic 178	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   20: pop
    //   21: aload_0
    //   22: invokevirtual 367	com/resmed/refresh/ui/uibase/base/BaseBluetoothActivity:getFragmentManager	()Landroid/app/FragmentManager;
    //   25: invokevirtual 373	android/app/FragmentManager:beginTransaction	()Landroid/app/FragmentTransaction;
    //   28: astore_2
    //   29: aload_0
    //   30: invokevirtual 367	com/resmed/refresh/ui/uibase/base/BaseBluetoothActivity:getFragmentManager	()Landroid/app/FragmentManager;
    //   33: ldc_w 375
    //   36: invokevirtual 379	android/app/FragmentManager:findFragmentByTag	(Ljava/lang/String;)Landroid/app/Fragment;
    //   39: astore_1
    //   40: aload_1
    //   41: ifnull +20 -> 61
    //   44: aload_1
    //   45: checkcast 381	android/app/DialogFragment
    //   48: invokevirtual 384	android/app/DialogFragment:dismiss	()V
    //   51: ldc_w 361
    //   54: ldc_w 1114
    //   57: invokestatic 178	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   60: pop
    //   61: aload_0
    //   62: invokevirtual 367	com/resmed/refresh/ui/uibase/base/BaseBluetoothActivity:getFragmentManager	()Landroid/app/FragmentManager;
    //   65: invokevirtual 391	android/app/FragmentManager:executePendingTransactions	()Z
    //   68: pop
    //   69: aload_2
    //   70: aconst_null
    //   71: invokevirtual 397	android/app/FragmentTransaction:addToBackStack	(Ljava/lang/String;)Landroid/app/FragmentTransaction;
    //   74: pop
    //   75: invokestatic 1119	com/resmed/refresh/ui/fragments/BeDPickerDialog:newInstance	()Lcom/resmed/refresh/ui/fragments/BeDPickerDialog;
    //   78: astore_1
    //   79: new 154	java/lang/StringBuilder
    //   82: astore_3
    //   83: aload_3
    //   84: ldc_w 1121
    //   87: invokespecial 159	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   90: ldc_w 1123
    //   93: aload_3
    //   94: aload_1
    //   95: invokevirtual 239	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   98: invokevirtual 172	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   101: invokestatic 178	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   104: pop
    //   105: aload_1
    //   106: checkcast 1116	com/resmed/refresh/ui/fragments/BeDPickerDialog
    //   109: aload_0
    //   110: invokevirtual 1127	com/resmed/refresh/ui/fragments/BeDPickerDialog:setOnPickedDevice	(Lcom/resmed/refresh/ui/fragments/BeDPickerDialog$OnPickedDevice;)V
    //   113: new 154	java/lang/StringBuilder
    //   116: astore_3
    //   117: aload_3
    //   118: ldc_w 1129
    //   121: invokespecial 159	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   124: ldc_w 1123
    //   127: aload_3
    //   128: aload_1
    //   129: invokevirtual 239	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   132: ldc_w 1131
    //   135: invokevirtual 168	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   138: aload_2
    //   139: invokevirtual 239	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   142: invokevirtual 172	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   145: invokestatic 178	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   148: pop
    //   149: aload_0
    //   150: iconst_1
    //   151: putfield 107	com/resmed/refresh/ui/uibase/base/BaseBluetoothActivity:connectionProgressDisplayed	Z
    //   154: aload_1
    //   155: aload_2
    //   156: ldc_w 375
    //   159: invokevirtual 411	android/app/DialogFragment:show	(Landroid/app/FragmentTransaction;Ljava/lang/String;)I
    //   162: pop
    //   163: new 154	java/lang/StringBuilder
    //   166: astore_2
    //   167: aload_2
    //   168: ldc_w 1133
    //   171: invokespecial 159	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   174: ldc_w 1123
    //   177: aload_2
    //   178: aload_1
    //   179: invokevirtual 239	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   182: invokevirtual 172	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   185: invokestatic 178	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   188: pop
    //   189: new 199	android/os/Message
    //   192: astore_1
    //   193: aload_1
    //   194: invokespecial 200	android/os/Message:<init>	()V
    //   197: aload_1
    //   198: bipush 10
    //   200: putfield 329	android/os/Message:what	I
    //   203: getstatic 123	com/resmed/refresh/ui/uibase/base/BaseBluetoothActivity:mService	Landroid/os/Messenger;
    //   206: astore_2
    //   207: aload_2
    //   208: ifnull +10 -> 218
    //   211: getstatic 123	com/resmed/refresh/ui/uibase/base/BaseBluetoothActivity:mService	Landroid/os/Messenger;
    //   214: aload_1
    //   215: invokevirtual 348	android/os/Messenger:send	(Landroid/os/Message;)V
    //   218: aload_0
    //   219: monitorexit
    //   220: return
    //   221: astore_2
    //   222: aload_2
    //   223: invokevirtual 1134	java/lang/Exception:printStackTrace	()V
    //   226: goto -63 -> 163
    //   229: astore_1
    //   230: aload_0
    //   231: monitorexit
    //   232: aload_1
    //   233: athrow
    //   234: astore_1
    //   235: aload_1
    //   236: invokevirtual 351	android/os/RemoteException:printStackTrace	()V
    //   239: goto -21 -> 218
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	242	0	this	BaseBluetoothActivity
    //   39	176	1	localObject1	Object
    //   229	4	1	localObject2	Object
    //   234	2	1	localRemoteException	RemoteException
    //   28	180	2	localObject3	Object
    //   221	2	2	localException	Exception
    //   82	46	3	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   154	163	221	java/lang/Exception
    //   2	40	229	finally
    //   44	61	229	finally
    //   61	154	229	finally
    //   154	163	229	finally
    //   163	207	229	finally
    //   211	218	229	finally
    //   222	226	229	finally
    //   235	239	229	finally
    //   211	218	234	android/os/RemoteException
  }
  
  public void unBindFromService()
  {
    AppFileLog.addTrace("BaseBluetoothActivity::unBindingFromService!");
    if (this.mBound)
    {
      getApplicationContext().unbindService(this.mConnection);
      this.mBound = false;
    }
  }
  
  protected void unregisterAll()
  {
    Iterator localIterator = this.receivers.iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        this.receivers.clear();
        return;
      }
      BroadcastReceiver localBroadcastReceiver = (BroadcastReceiver)localIterator.next();
      try
      {
        unregisterReceiver(localBroadcastReceiver);
        Log.d("com.resmed.refresh.ui", "unregistered receiver : " + localBroadcastReceiver);
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        localIllegalArgumentException.printStackTrace();
        Log.d("com.resmed.refresh.ui", "unregistered receiver : " + localBroadcastReceiver);
      }
      finally
      {
        Log.d("com.resmed.refresh.ui", "unregistered receiver : " + localBroadcastReceiver);
      }
    }
  }
  
  public void updateDataStoredFlag(int paramInt)
  {
    if (paramInt == 0)
    {
      Message localMessage = new Message();
      localMessage.what = 28;
      sendMsgBluetoothService(localMessage);
    }
    super.updateDataStoredFlag(paramInt);
  }
  
  public class IncomingHandler
    extends Handler
  {
    private List<Byte> partialMsgBuffer = new ArrayList();
    
    public IncomingHandler() {}
    
    private void handlePartialStreamPacket(Bundle paramBundle)
    {
      if (paramBundle != null) {
        paramBundle = paramBundle.getByteArray("REFRESH_BED_NEW_DATA");
      }
      for (int i = 0;; i++)
      {
        if (i >= paramBundle.length) {
          return;
        }
        this.partialMsgBuffer.add(Byte.valueOf(paramBundle[i]));
      }
    }
    
    private void handlePartialStreamPacketEnd(Bundle paramBundle)
    {
      ByteBuffer localByteBuffer = ByteBuffer.allocate(this.partialMsgBuffer.size());
      Object localObject = this.partialMsgBuffer.iterator();
      for (;;)
      {
        if (!((Iterator)localObject).hasNext())
        {
          this.partialMsgBuffer.clear();
          localObject = new Bundle();
          ((Bundle)localObject).putByteArray("REFRESH_BED_NEW_DATA", localByteBuffer.array());
          if (paramBundle != null)
          {
            byte b = paramBundle.getByte("REFRESH_BED_NEW_DATA_TYPE");
            int i = paramBundle.getInt("REFRESH_BED_NEW_DATA_SIZE");
            ((Bundle)localObject).putByte("REFRESH_BED_NEW_DATA_TYPE", b);
            ((Bundle)localObject).putInt("REFRESH_BED_NEW_DATA_SIZE", i);
          }
          BaseBluetoothActivity.this.handleStreamPacket((Bundle)localObject);
          return;
        }
        localByteBuffer.put(((Byte)((Iterator)localObject).next()).byteValue());
      }
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      case 7: 
      case 9: 
      case 10: 
      case 11: 
      case 12: 
      case 13: 
      case 16: 
      case 22: 
      case 23: 
      case 24: 
      case 25: 
      case 26: 
      default: 
        super.handleMessage(paramMessage);
      }
      for (;;)
      {
        return;
        paramMessage = paramMessage.getData();
        if (paramMessage != null)
        {
          int i = paramMessage.getInt("BUNDLE_BED_AVAILABLE_DATA", -1);
          BaseBluetoothActivity.this.updateDataStoredFlag(i);
          continue;
          BaseBluetoothActivity.this.handleStreamPacket(paramMessage.getData());
          continue;
          handlePartialStreamPacket(paramMessage.getData());
          continue;
          handlePartialStreamPacketEnd(paramMessage.getData());
          continue;
          paramMessage = (CONNECTION_STATE)paramMessage.getData().get("REFRESH_BED_NEW_CONN_STATUS");
          Log.d("com.resmed.refresh.pair", "BaseActivity MSG_BeD_CONNECTION_STATUS CONNECTION_STATE : " + CONNECTION_STATE.toString(paramMessage));
          BaseBluetoothActivity.this.handleConnectionStatus(paramMessage);
          continue;
          BluetoothDataSerializeUtil.deleteJsonFile(BaseBluetoothActivity.this.getApplicationContext());
          Log.d("com.resmed.refresh.dialog", "MSG_UNPAIR showBeDPickerDialog()");
          BaseBluetoothActivity.this.showBeDPickerDialog();
          continue;
          BaseBluetoothActivity.this.handleSleepSessionStopped(paramMessage.getData());
          continue;
          BaseBluetoothActivity.this.handleEnvSample(paramMessage.getData());
          continue;
          BaseBluetoothActivity.this.handleBreathingRate(paramMessage.getData());
          continue;
          BaseBluetoothActivity.this.handleUserSleepState(paramMessage.getData());
          continue;
          BaseBluetoothActivity.this.handleSessionRecovered(paramMessage.getData());
        }
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\uibase\base\BaseBluetoothActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
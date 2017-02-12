package com.resmed.refresh.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.ui.fragments.SleepButtonFragment;
import com.resmed.refresh.ui.fragments.SleepTimeSetupFragment;
import com.resmed.refresh.ui.fragments.SleepTrackFragment;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.utils.CustomDialogBuilder;
import com.resmed.refresh.ui.utils.RelaxDataManager;
import com.resmed.refresh.ui.utils.SmartAlarmDataManager;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.DefaultRelaxSleep;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RelaxPlayerCallback;
import com.resmed.refresh.utils.RelaxSoundManager.UserStatus;

import java.util.Arrays;

public class SleepTimeActivity
  extends BaseBluetoothActivity
  implements SleepTimeSetupFragment.SleepTimeSetupListeners, SleepButtonFragment.SetSleepButtonBtn, RelaxPlayerCallback
{
  public static final int CODE_RESTART_RELAX = 31;
  public static final int CODE_SMARTALARM_CHANGED = 32;
  public static final int RECOVERY_AUTOMATIC = 1;
  public static final int RECOVERY_USER = 0;
  public static boolean isAfterBackPress = false;
  public static boolean isHomeButtonPressSleep = false;
  public static boolean isStartTimer = false;
  private int bioOnBeD;
  private BroadcastReceiver clockTickReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      Log.d("com.resmed.refresh.ui", "UPDATE CLOCK######");
      if (SleepTimeActivity.this.sleepTrackFragment != null) {
        SleepTimeActivity.this.sleepTrackFragment.handleClockTick();
      }
    }
  };
  private DefaultRelaxSleep defaultRelaxSleep;
  private boolean enableRelaxButton;
  private Handler handler;
  private int hasRecoveredFromCrash;
  private boolean isScreenLock = false;
  private boolean isUserAwake = false;
  private Runnable r;
  private boolean shouldSync;
  private SleepTimeSetupFragment sleepTimeSetupFragment;
  private SleepTrackFragment sleepTrackFragment;
  private boolean wasPlayingRelax;
  
  private void clearFragment()
  {
    try
    {
      FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
      Fragment localFragment = getSupportFragmentManager().findFragmentByTag("SleepTimeSetupFragment");
      if (localFragment != null) {
        localFragmentTransaction.remove(localFragment);
      }
      getSupportFragmentManager().executePendingTransactions();
      return;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        localException.printStackTrace();
      }
    }
  }
  
  private void startHandler(long paramLong)
  {
    if (!BaseBluetoothActivity.IN_SLEEP_SESSION)
    {
      System.out.println("1234handler started SleepTimeActivity");
      this.handler.postDelayed(this.r, paramLong);
    }
  }
  
  private void stopRelaxWithDelay()
  {
    this.wasPlayingRelax = this.defaultRelaxSleep.isPlaying();
    stopRelax(false);
  }
  
  public DefaultRelaxSleep getDefaultRelaxSleep()
  {
    return this.defaultRelaxSleep;
  }
  
  public void handleBreathingRate(Bundle paramBundle)
  {
    super.handleBreathingRate(paramBundle);
    if (paramBundle != null)
    {
      float f = paramBundle.getFloat("BUNDLE_BREATHING_RATE");
      paramBundle.getInt("BUNDLE_BREATHING_SECINDEX");
      if (this.sleepTrackFragment != null) {
        this.sleepTrackFragment.setUserBreathRate(f);
      }
    }
  }
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    super.handleConnectionStatus(paramCONNECTION_STATE);
    AppFileLog.addTrace("CONNECTION : New Connection Status " + CONNECTION_STATE.toString(paramCONNECTION_STATE));
    if (this.sleepTimeSetupFragment != null) {
      this.sleepTimeSetupFragment.handleConnectionStatus(paramCONNECTION_STATE);
    }
    if (this.sleepTrackFragment != null) {
      this.sleepTrackFragment.handleConnectionStatus(paramCONNECTION_STATE);
    }
  }
  
  public void handleEnvSample(Bundle paramBundle)
  {
    super.handleEnvSample(paramBundle);
    if (this.sleepTrackFragment != null) {
      this.sleepTrackFragment.handleEnvSample(paramBundle);
    }
  }
  
  public void handleReceivedRpc(JsonRPC paramJsonRPC)
  {
    super.handleReceivedRpc(paramJsonRPC);
    if (this.sleepTrackFragment != null) {
      this.sleepTrackFragment.handleReceivedRpc(paramJsonRPC);
    }
  }
  
  public void handleSessionRecovered(Bundle paramBundle)
  {
    super.handleSessionRecovered(paramBundle);
    Log.d("com.resmed.refresh.ui", " SleepTimeActivity::handleSessionRecovered()");
    if (this.sleepTrackFragment != null) {
      this.sleepTrackFragment.handleSessionRecovered(paramBundle);
    }
  }
  
  public void handleSleepSessionStopped(Bundle paramBundle)
  {
    super.handleSleepSessionStopped(paramBundle);
    if (this.sleepTrackFragment != null) {
      this.sleepTrackFragment.handleSleepSessionStopped(paramBundle);
    }
  }
  
  public void handleStreamPacket(Bundle paramBundle)
  {
    super.handleStreamPacket(paramBundle);
    byte[] arrayOfByte = paramBundle.getByteArray("REFRESH_BED_NEW_DATA");
    int i = paramBundle.getByte("REFRESH_BED_NEW_DATA_TYPE");
    if ((VLPacketType.PACKET_TYPE_NOTE_STORE_FOREIGN.ordinal() == i) || (VLPacketType.PACKET_TYPE_NOTE_STORE_LOCAL.ordinal() == i))
    {
      this.bioOnBeD = PacketsByteValuesReader.getStoreLocalBio(arrayOfByte);
      AppFileLog.addTrace("SleepTimeActivity::handleStreamPacket() bytes : " + Arrays.toString(arrayOfByte));
      AppFileLog.addTrace("SleepTimeActivity::handleStreamPacket() Received stored bio count : " + this.bioOnBeD);
    }
    if (this.sleepTimeSetupFragment != null) {
      this.sleepTimeSetupFragment.handleStreamPacket(paramBundle);
    }
    if (this.sleepTrackFragment != null) {
      this.sleepTrackFragment.handleStreamPacket(paramBundle);
    }
  }
  
  public void handleUserSleepState(Bundle paramBundle)
  {
    super.handleUserSleepState(paramBundle);
    if (paramBundle != null)
    {
      int i = paramBundle.getInt("BUNDLE_SLEEP_STATE");
      paramBundle.getInt("BUNDLE_SLEEP_EPOCH_INDEX");
      Log.d("com.resmed.refresh.rm20_callback", " handleUserSleepState(data), sleepState : " + i);
      Log.d("com.resmed.refresh.relax", " handleUserSleepState(data), sleepState : " + i);
      if (i != 1) {
        break label89;
      }
      this.sleepTrackFragment.setUserStatus(RelaxSoundManager.UserStatus.USER_AWAKE);
    }
    for (;;)
    {
      return;
      label89:
      this.sleepTrackFragment.setUserStatus(RelaxSoundManager.UserStatus.USER_ASLEEP);
    }
  }
  
  public boolean isUserAwake()
  {
    return this.isUserAwake;
  }
  
  public void mindClearClick()
  {
    stopRelaxWithDelay();
    Intent localIntent = new Intent(this, MindClearActivity.class);
    localIntent.putExtra("com.resmed.refresh.ui.uibase.app.activity_modal", true);
    localIntent.putExtra("IsFromHome", false);
    startActivityForResult(localIntent, 31);
    overridePendingTransition(2130968597, 2130968586);
    RefreshModelController.getInstance().updateFlurryEvents("PreSleep_mindClear");
  }
  
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    Log.d("com.resmed.refresh.smartAlarm", "SleepTimeActivity onActivityResult");
    SmartAlarmDataManager.getInstance().debugSmartAlarm();
    if (paramInt1 == 32)
    {
      paramIntent = new Message();
      paramIntent.what = 26;
      Bundle localBundle = new Bundle(2);
      localBundle.putLong("com.resmed.refresh.consts.smart_alarm_time_value", SmartAlarmDataManager.getInstance().getAlarmDateTime());
      localBundle.putInt("com.resmed.refresh.consts.smart_alarm_window_value", SmartAlarmDataManager.getInstance().getWindowValue());
      paramIntent.setData(localBundle);
      sendMsgBluetoothService(paramIntent);
      Log.d("com.resmed.refresh.smartAlarm", "SleepTimeActivity onActivityResult sending message");
      if (this.wasPlayingRelax) {
        startRelax(true);
      }
    }
    for (;;)
    {
      return;
      if ((paramInt1 == 31) && (this.wasPlayingRelax)) {
        startRelax(true);
      }
    }
  }
  
  public void onBackPressed()
  {
    if (this.sleepTrackFragment != null) {}
    for (;;)
    {
      return;
      super.onBackPressed();
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Log.d("com.resmed.refresh.smartAlarm", "SleepTimeActivity onCreate");
    boolean bool = RefreshApplication.getInstance().getConnectionStatus().isSocketConnected();
    isHomeButtonPressSleep = false;
    if ((!BaseBluetoothActivity.CORRECT_FIRMWARE_VERSION) && (bool))
    {
      startActivity(new Intent(this, UpdateOTAActivity.class));
      finish();
    }
    paramBundle = new Message();
    paramBundle.what = 6;
    sendMsgBluetoothService(paramBundle);
    getIntent().getBooleanExtra("com.resmed.refresh.consts.recovering_app_from_service", false);
    this.hasRecoveredFromCrash = getIntent().getIntExtra("com.resmed.refresh.consts.recovering_ui_thread_from_crash", -1);
    setContentView(2130903095);
    setTypeRefreshBar(BaseActivity.TypeBar.DEFAULT);
    setTitle(2131165831);
    this.shouldSync = false;
    paramBundle = getIntent();
    if (paramBundle != null) {
      this.shouldSync = paramBundle.getBooleanExtra("PREF_SYNC_DATA_PROCESS", false);
    }
    if (!this.shouldSync)
    {
      clearFragment();
      this.sleepTimeSetupFragment = new SleepTimeSetupFragment();
      paramBundle = getSupportFragmentManager().beginTransaction();
      paramBundle.add(2131099837, this.sleepTimeSetupFragment, "SleepTimeSetupFragment");
      paramBundle.commit();
    }
    this.defaultRelaxSleep = new DefaultRelaxSleep(this, this);
    paramBundle = RelaxDataManager.getInstance();
    Log.d("com.resmed.refresh.sleepFragment", " SleepTimeActivity::onCreate getActiveRelax() : " + paramBundle.getActiveRelax());
    this.handler = new Handler();
    this.r = new Runnable()
    {
      public void run()
      {
        for (;;)
        {
          try
          {
            if ((!SleepTimeActivity.isHomeButtonPressSleep) && (!SleepTimeActivity.this.isScreenLock)) {
              continue;
            }
            if ((SleepTimeActivity.this.isScreenLock) && (SleepTimeActivity.isHomeButtonPressSleep))
            {
              SleepTimeActivity.isHomeButtonPressSleep = false;
              SleepTimeActivity.this.isScreenLock = false;
              ((PowerManager)SleepTimeActivity.this.getApplicationContext().getSystemService("power")).newWakeLock(268435482, "TAG").acquire();
              SleepTimeActivity.this.getWindow().addFlags(4194304);
              SleepTimeActivity.this.startHandler(5000L);
              return;
            }
          }
          catch (Exception localException)
          {
            Intent localIntent;
            localException.printStackTrace();
            continue;
            SleepTimeActivity.this.sleepTimeSetupFragment.nextactivitycall();
            continue;
          }
          SleepTimeActivity.isHomeButtonPressSleep = false;
          RefreshModelController.getInstance().updateFlurryEvents("Home_sleep");
          localIntent = new android/content/Intent;
          localIntent.<init>(SleepTimeActivity.this.getApplicationContext(), SleepTimeActivity.class);
          localIntent.addFlags(131072);
          localIntent.addFlags(16777216);
          SleepTimeActivity.this.startActivity(localIntent);
          SleepTimeActivity.this.startHandler(5000L);
        }
      }
    };
    startHandler(120000L);
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    stopHandler();
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem.getItemId() == 16908332) {
      finish();
    }
    for (boolean bool = true;; bool = super.onOptionsItemSelected(paramMenuItem)) {
      return bool;
    }
  }
  
  protected void onPause()
  {
    super.onPause();
    if (!((PowerManager)getSystemService("power")).isScreenOn()) {
      this.isScreenLock = true;
    }
  }
  
  public void onRelaxComplete()
  {
    SleepButtonFragment localSleepButtonFragment = (SleepButtonFragment)getSupportFragmentManager().findFragmentByTag("SleepButtonFragmentTrack");
    if ((localSleepButtonFragment != null) && (localSleepButtonFragment.isAdded()))
    {
      localSleepButtonFragment.displayPlayIcon();
      RefreshModelController.getInstance().storePlayAutoRelax(false);
    }
  }
  
  protected void onResume()
  {
    super.onResume();
    if (this.sleepTrackFragment != null) {
      this.sleepTrackFragment.handleClockTick();
    }
    for (;;)
    {
      Intent localIntent = registerReceiver(this.clockTickReceiver, new IntentFilter("android.intent.action.TIME_TICK"));
      Log.i("com.resmed.refresh", "registered? " + localIntent);
      if ((!isAfterBackPress) && (isStartTimer))
      {
        isStartTimer = false;
        isAfterBackPress = false;
        if (this.sleepTimeSetupFragment != null) {
          this.handler.postDelayed(this.r, 5000L);
        }
      }
      return;
      int i = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("PREF_CONNECTION_STATE", -1);
      Log.d("com.resmed.refresh.ui", " SleepTimeActivity::onResume conn state : " + i);
      if ((i == CONNECTION_STATE.NIGHT_TRACK_ON.ordinal()) || (this.shouldSync)) {
        sleepTrackClick(this.shouldSync);
      }
    }
  }
  
  protected void onStop()
  {
    super.onStop();
    isHomeButtonPressSleep = true;
  }
  
  public void onUserInteraction()
  {
    super.onUserInteraction();
    stopHandler();
    startHandler(120000L);
  }
  
  public void relaxClick(int paramInt)
  {
    Log.e("com.resmed.refresh.relax", "relaxClick " + paramInt + " enableRelaxButton = " + this.enableRelaxButton);
    switch (paramInt)
    {
    }
    for (;;)
    {
      RefreshModelController.getInstance().updateFlurryEvents("PreSleep_Relax");
      return;
      Intent localIntent = new Intent(this, RelaxActivity.class);
      localIntent.putExtra("com.resmed.refresh.ui.uibase.app.activity_modal", true);
      startActivity(localIntent);
      overridePendingTransition(2130968597, 2130968586);
      continue;
      if (this.enableRelaxButton)
      {
        RefreshModelController.getInstance().storePlayAutoRelax(false);
        stopRelax(true);
      }
      else
      {
        this.enableRelaxButton = true;
        continue;
        if (this.enableRelaxButton)
        {
          RefreshModelController.getInstance().storePlayAutoRelax(true);
          startRelax(true);
        }
        else
        {
          this.enableRelaxButton = true;
        }
      }
    }
  }
  
  public void requestUserStatus()
  {
    Message localMessage = new Message();
    localMessage.what = 17;
    sendMsgBluetoothService(localMessage);
  }
  
  public void sendRpcToBed(JsonRPC paramJsonRPC)
  {
    super.sendRpcToBed(paramJsonRPC);
    AppFileLog.addTrace("OUT : " + paramJsonRPC.getMethod() + " ID : " + paramJsonRPC.getId() + " PARAMS : " + paramJsonRPC.getParams());
  }
  
  public void setEnableRelaxButton(boolean paramBoolean)
  {
    this.enableRelaxButton = paramBoolean;
  }
  
  public void showDialog(CustomDialogBuilder paramCustomDialogBuilder)
  {
    super.showDialog(paramCustomDialogBuilder);
  }
  
  public void sleepTrackClick(boolean paramBoolean)
  {
    if (this.sleepTrackFragment == null)
    {
      localObject = new Bundle();
      ((Bundle)localObject).putBoolean("shouldSync", paramBoolean);
      ((Bundle)localObject).putInt("bioOnBeD", this.bioOnBeD);
      ((Bundle)localObject).putInt("hasRecoveredFromCrash", this.hasRecoveredFromCrash);
      this.sleepTrackFragment = new SleepTrackFragment();
      this.sleepTrackFragment.setArguments((Bundle)localObject);
    }
    changeRefreshBarVisibility(false, false);
    Object localObject = getSupportFragmentManager().beginTransaction();
    ((FragmentTransaction)localObject).setCustomAnimations(2130968582, 2130968588);
    ((FragmentTransaction)localObject).replace(2131099837, this.sleepTrackFragment);
    ((FragmentTransaction)localObject).commit();
  }
  
  public void smartAlarmClick()
  {
    stopRelaxWithDelay();
    Intent localIntent = new Intent(this, SmartAlarmActivity.class);
    localIntent.putExtra("com.resmed.refresh.ui.uibase.app.activity_modal", true);
    startActivityForResult(localIntent, 32);
    Log.d("com.resmed.refresh.smartAlarm", "SleepTime startActivityForResult smartAlarm");
    overridePendingTransition(2130968597, 2130968586);
    RefreshModelController.getInstance().updateFlurryEvents("PreSleep_smartAlarm");
  }
  
  public void startRelax(boolean paramBoolean)
  {
    RelaxDataManager localRelaxDataManager = RelaxDataManager.getInstance();
    Log.d("com.resmed.refresh.sleepFragment", " relaxDataManager.getActiveRelax() : " + localRelaxDataManager.getActiveRelax() + " forcePlay:" + paramBoolean);
    if (((paramBoolean) || ((localRelaxDataManager.getActiveRelax()) && (RefreshModelController.getInstance().getPlayAutoRelax()))) && (!this.defaultRelaxSleep.isPlaying())) {
      this.defaultRelaxSleep.startPlayer(localRelaxDataManager.getRelaxSound());
    }
  }
  
  public void stopHandler()
  {
    this.handler.removeCallbacks(this.r);
  }
  
  public void stopRelax(boolean paramBoolean)
  {
    if (this.defaultRelaxSleep != null) {
      this.defaultRelaxSleep.stopPlayer();
    }
  }
  
  protected void updateConnectionIcon()
  {
    super.updateConnectionIcon();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\SleepTimeActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
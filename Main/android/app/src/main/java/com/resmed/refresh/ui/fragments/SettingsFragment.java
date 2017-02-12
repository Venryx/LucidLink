package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.resmed.refresh.bluetooth.BeDConnectionStatus;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_UserProfile;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.activity.AcknowledgementsActivty;
import com.resmed.refresh.ui.activity.HomeActivity;
import com.resmed.refresh.ui.customview.CustomSwitch;
import com.resmed.refresh.ui.font.GroteskLight;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.ui.utils.CustomDialogBuilder;
import com.resmed.refresh.ui.utils.UserProfileDataManager;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.InternalFileProvider;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.refresh.utils.preSleepLog;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class SettingsFragment
  extends BaseFragment
  implements View.OnClickListener, RST_CallbackItem<RST_Response<RST_UserProfile>>
{
  private CustomSwitch switchSettingsGeolocation;
  private CustomSwitch switchSettingsNotifications;
  private GroteskLight tvSettingsHeightUnitValue;
  private GroteskLight tvSettingsTemperatureUnitValue;
  private GroteskLight tvSettingsWeightUnitValue;
  private UserSettingsButtons userButtonsListener;
  private UserProfileDataManager userProfileDataManager;
  
  private void clearBuffer()
  {
    getBaseActivity().showDialog(new CustomDialogBuilder(getBaseActivity()).title(2131166050).description(2131166042).setNegativeButton(2131165297, null).setPositiveButton(2131165296, new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        paramAnonymousView = BaseBluetoothActivity.getRpcCommands();
        final BaseBluetoothActivity localBaseBluetoothActivity = (BaseBluetoothActivity)SettingsFragment.this.getBaseActivity();
        if (localBaseBluetoothActivity != null)
        {
          BeDConnectionStatus localBeDConnectionStatus = RefreshApplication.getInstance().getConnectionStatus();
          localBaseBluetoothActivity.updateDataStoredFlag(0);
          int i = 2131166043;
          if (!localBeDConnectionStatus.isSessionOpened()) {
            i = 2131166049;
          }
          localBaseBluetoothActivity.sendRpcToBed(paramAnonymousView.clearBuffers());
          SettingsFragment.this.getBaseActivity().showDialog(new CustomDialogBuilder(SettingsFragment.this.getBaseActivity()).title(i).setPositiveButton(2131165296, new View.OnClickListener()
          {
            public void onClick(View paramAnonymous2View)
            {
              paramAnonymous2View = new Message();
              paramAnonymous2View.what = 27;
              localBaseBluetoothActivity.sendMsgBluetoothService(paramAnonymous2View);
            }
          }));
        }
      }
    }));
  }
  
  private void clearHistory()
  {
    getBaseActivity().showDialog(new CustomDialogBuilder(getBaseActivity()).title(2131166044).description(2131166045).setNegativeButton(2131165297, null).setPositiveButton(2131165296, new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        new SettingsFragment.ClearHistory(SettingsFragment.this, null).execute(new Void[0]);
      }
    }));
  }
  
  private void initDataView()
  {
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    this.switchSettingsGeolocation.setChecked(localRefreshModelController.getLocationPermission());
    this.switchSettingsNotifications.setChecked(localRefreshModelController.getUsePushNotifications());
  }
  
  private String isBluetoothEnabled()
  {
    BluetoothAdapter localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    String str = "Off";
    if (localBluetoothAdapter.isEnabled()) {
      str = "On";
    }
    return str;
  }
  
  private String isSoundOnDevice()
  {
    AudioManager localAudioManager = (AudioManager)getActivity().getSystemService("audio");
    String str = "Off";
    if (localAudioManager.getRingerMode() == 2) {
      str = "On";
    }
    return str;
  }
  
  private void restart()
  {
    Intent localIntent = new Intent(getActivity(), HomeActivity.class);
    localIntent.setFlags(268468224);
    startActivity(localIntent);
    getActivity().overridePendingTransition(2130968582, 2130968583);
  }
  
  private void sendBugReport()
  {
    localIntent = new Intent("android.intent.action.SEND_MULTIPLE");
    localIntent.putExtra("android.intent.extra.EMAIL", new String[] { Consts.EMAIL_BUG_REPORT });
    localIntent.putExtra("android.intent.extra.SUBJECT", "S+ by ResMed Android Bug Report");
    StringBuffer localStringBuffer = new StringBuffer();
    localObject1 = "";
    try
    {
      localObject2 = getActivity().getPackageName();
      localObject2 = getActivity().getPackageManager().getPackageInfo((String)localObject2, 0).versionName;
      localObject1 = localObject2;
    }
    catch (Exception localException)
    {
      try
      {
        Object localObject4;
        Object localObject3;
        for (;;)
        {
          Object localObject2;
          String str5;
          String str4;
          String str6;
          Object localObject5;
          startActivity(Intent.createChooser(localIntent, getString(2131166048)));
          return;
          localException = localException;
          localException.printStackTrace();
          continue;
          localObject3 = "Off";
          continue;
          localObject4 = "Off";
          continue;
          String str1 = "Off";
          continue;
          String str2 = "Off";
          continue;
          String str3 = "Off";
          continue;
          localObject1 = new File(RefreshTools.getFilesPath(), AppFileLog.getFilename());
          continue;
          localObject3 = Uri.parse(InternalFileProvider.CONTENT_URI + ((File)localObject1).getName());
          if (Consts.USE_EXTERNAL_STORAGE) {
            localObject3 = Uri.parse("file://" + localObject1);
          }
          Log.e("com.resmed.refresh", "ParcelFileDescriptor uri:" + ((Uri)localObject3).toString());
          ((ArrayList)localObject4).add(localObject3);
        }
        localObject1 = Uri.parse(InternalFileProvider.CONTENT_URI + ((File)localObject3).getName());
        if (!Consts.USE_EXTERNAL_STORAGE) {
          break label955;
        }
        localObject1 = Uri.parse("file://" + localObject3);
        Log.e("com.resmed.refresh", "ParcelFileDescriptor uri:" + ((Uri)localObject1).toString());
        ((ArrayList)localObject4).add(localObject1);
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
        for (;;)
        {
          localActivityNotFoundException.printStackTrace();
        }
      }
    }
    str5 = Build.MANUFACTURER;
    str4 = Build.MODEL;
    str6 = Build.VERSION.RELEASE;
    if (BeDConnectionStatus.getInstance().getState() == CONNECTION_STATE.REAL_STREAM_ON)
    {
      localObject2 = "On";
      if (BeDConnectionStatus.getInstance().getState() != CONNECTION_STATE.NIGHT_TRACK_ON) {
        break label767;
      }
      localObject4 = "On";
      if (!BeDConnectionStatus.getInstance().isSocketConnected()) {
        break label773;
      }
      str1 = "On";
      localObject5 = RefreshModelController.getInstance();
      if (!((RefreshModelController)localObject5).getLocationPermission()) {
        break label780;
      }
      str2 = "On";
      if (!((RefreshModelController)localObject5).getUsePushNotifications()) {
        break label787;
      }
      str3 = "On";
      localObject5 = ((RefreshModelController)localObject5).getUser().getEmail();
      localStringBuffer.append("---------------------------------------\n").append("Diagnostics\n").append("Version: " + (String)localObject1 + "\n").append("Device: Android " + str5 + " " + str4 + "\n").append("OS: Android " + str6 + "\n").append("Bluetooth: " + isBluetoothEnabled() + "\n").append("Bluetooth streaming: " + (String)localObject2 + "\n").append("Bluetooth night monitoring: " + (String)localObject4 + "\n").append("Accesory connected: " + str1 + "\n").append("RM20 Version: 1.0.2\n").append("Firmware: " + RefreshModelController.getInstance().getFirmwareVersion() + "\n").append("Serial: " + RefreshModelController.getInstance().getBoardVersion() + "\n").append("Location: " + str2 + "\n").append("Push: " + str3 + "\n").append("Sound: " + isSoundOnDevice() + "\n").append("Environment: " + Consts.ENVIRONMENTAL_NAMES[5] + "\n").append("Username: " + (String)localObject5 + " \n").append("\n\n\n");
      localIntent.putExtra("android.intent.extra.TEXT", localStringBuffer.toString());
      localIntent.putExtra("android.intent.extra.SUBJECT", "S+ Bug Report");
      localObject4 = new ArrayList();
      if (!Consts.TESTING_DIRAC) {
        break label794;
      }
      localObject1 = new File(Environment.getExternalStorageDirectory(), "diracLog.txt");
      if ((((File)localObject1).exists()) && (((File)localObject1).canRead())) {
        break label811;
      }
      Log.d("com.resmed.refresh.filelog", "Failed to attach file as does not exist");
      localObject2 = new File(RefreshTools.getFilesPath(), preSleepLog.getFilename());
      if ((((File)localObject2).exists()) && (((File)localObject2).canRead())) {
        break label901;
      }
      Log.d("com.resmed.refresh.filelog", "Failed to attach file as does not exist");
      localIntent.putExtra("android.intent.extra.STREAM", (Serializable)localObject4);
      localIntent.setType("message/rfc822");
    }
  }
  
  private void setupGUI(View paramView)
  {
    paramView.findViewById(2131100357).setOnClickListener(this);
    paramView.findViewById(2131100358).setOnClickListener(this);
    paramView.findViewById(2131100359).setOnClickListener(this);
    paramView.findViewById(2131100360).setOnClickListener(this);
    this.switchSettingsNotifications = ((CustomSwitch)paramView.findViewById(2131100350));
    this.switchSettingsGeolocation = ((CustomSwitch)paramView.findViewById(2131100349));
    this.tvSettingsWeightUnitValue = ((GroteskLight)paramView.findViewById(2131100352));
    this.tvSettingsHeightUnitValue = ((GroteskLight)paramView.findViewById(2131100354));
    this.tvSettingsTemperatureUnitValue = ((GroteskLight)paramView.findViewById(2131100356));
    this.tvSettingsWeightUnitValue.setOnClickListener(this);
    this.tvSettingsHeightUnitValue.setOnClickListener(this);
    this.tvSettingsTemperatureUnitValue.setOnClickListener(this);
    this.switchSettingsNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        RefreshModelController.getInstance().setUsePushNotifications(paramAnonymousBoolean);
      }
    });
    this.switchSettingsGeolocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        RefreshModelController.getInstance().changeLocationUpdates(paramAnonymousBoolean);
      }
    });
  }
  
  private void showAcknowledgements()
  {
    startActivity(new Intent(getActivity(), AcknowledgementsActivty.class));
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.userButtonsListener = ((UserSettingsButtons)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new ClassCastException(paramActivity.toString() + " ...you must implement ProfileButtons from your Activity ;-) !");
    }
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    }
    for (;;)
    {
      paramView.requestFocus();
      return;
      sendBugReport();
      continue;
      clearHistory();
      continue;
      showAcknowledgements();
      continue;
      clearBuffer();
      continue;
      this.userProfileDataManager.setCurrentPicker(UserProfileDataManager.PICKER_WEIGHT_UNIT);
      this.userButtonsListener.openPicker();
      continue;
      this.userProfileDataManager.setCurrentPicker(UserProfileDataManager.PICKER_HEIGHT_UNIT);
      this.userButtonsListener.openPicker();
      continue;
      this.userProfileDataManager.setCurrentPicker(UserProfileDataManager.PICKER_TEMP_UNIT);
      this.userButtonsListener.openPicker();
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903161, paramViewGroup, false);
    this.userProfileDataManager = UserProfileDataManager.getInstance();
    setupGUI(paramLayoutInflater);
    initDataView();
    return paramLayoutInflater;
  }
  
  public void onResult(RST_Response<RST_UserProfile> paramRST_Response)
  {
    if ((getBaseActivity() == null) || (getBaseActivity().isFinishing()) || (!getBaseActivity().isAppValidated(paramRST_Response.getErrorCode()))) {
      return;
    }
    System.out.println("result " + paramRST_Response.getStatus());
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    if (paramRST_Response.isStatus()) {}
    for (boolean bool = false;; bool = true)
    {
      localRefreshModelController.setProfileUpdatePending(bool);
      break;
    }
  }
  
  public void onResume()
  {
    super.onResume();
    setWeightUnitUI();
    setHeightUnitUI();
    setTempratureUnitUI();
  }
  
  public void onStop()
  {
    super.onStop();
    RefreshModelController.getInstance().getUser().update();
    RefreshModelController.getInstance().updateUserProfile(this);
  }
  
  public void setHeightUnitUI()
  {
    this.tvSettingsHeightUnitValue.setText(this.userProfileDataManager.getLabelForHeightUnit());
  }
  
  public void setTempratureUnitUI()
  {
    this.tvSettingsTemperatureUnitValue.setText(this.userProfileDataManager.getLabelForTemperatureUnit());
  }
  
  public void setWeightUnitUI()
  {
    this.tvSettingsWeightUnitValue.setText(this.userProfileDataManager.getLabelForWeightUnit());
  }
  
  private class ClearHistory
    extends AsyncTask<Void, Integer, Boolean>
  {
    private ClearHistory() {}
    
    protected Boolean doInBackground(Void... paramVarArgs)
    {
      try
      {
        RefreshModelController.getInstance().clearHistory();
        paramVarArgs = Boolean.valueOf(true);
      }
      catch (Exception paramVarArgs)
      {
        for (;;)
        {
          paramVarArgs.printStackTrace();
          paramVarArgs = Boolean.valueOf(false);
        }
      }
      return paramVarArgs;
    }
    
    protected void onPostExecute(Boolean paramBoolean)
    {
      super.onPostExecute(paramBoolean);
      SettingsFragment.this.getBaseActivity().dismissProgressDialog();
      Toast.makeText(SettingsFragment.this.getBaseActivity(), 2131166047, 0).show();
      SettingsFragment.this.restart();
    }
    
    protected void onPreExecute()
    {
      super.onPreExecute();
      try
      {
        Thread.sleep(200L);
        SettingsFragment.this.getBaseActivity().showProgressDialog(2131166046);
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
  }
  
  public static abstract interface UserSettingsButtons
  {
    public abstract void openPicker();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SettingsFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
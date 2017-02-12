package com.resmed.refresh.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.ui.fragments.SensorCaptureFragment;
import com.resmed.refresh.ui.fragments.SensorSetupFragment;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.utils.Log;
import java.util.Arrays;

public class SensorSetupActivity
  extends BaseBluetoothActivity
{
  private SensorCaptureFragment sensorCaptureFragment;
  private SensorSetupFragment sensorSetupFragment;
  
  private void handleBioToGraph(byte[] paramArrayOfByte)
  {
    if (this.sensorCaptureFragment != null) {
      this.sensorCaptureFragment.addToGraph(paramArrayOfByte);
    }
  }
  
  private void handleNoteEnv(byte[] paramArrayOfByte)
  {
    int i = PacketsByteValuesReader.readIlluminanceValue(paramArrayOfByte);
    float f = PacketsByteValuesReader.readTemperatureValue(paramArrayOfByte);
    if (this.sensorCaptureFragment != null)
    {
      this.sensorCaptureFragment.setLightValue(i);
      this.sensorCaptureFragment.setTempValue(Math.round(f));
    }
  }
  
  private void replaceFragment(Fragment paramFragment)
  {
    if ((this != null) && (!isFinishing()))
    {
      FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
      localFragmentTransaction.replace(2131099805, paramFragment);
      localFragmentTransaction.commitAllowingStateLoss();
    }
  }
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    super.handleConnectionStatus(paramCONNECTION_STATE);
    if (CONNECTION_STATE.SESSION_OPENED == paramCONNECTION_STATE) {
      if (this.sensorCaptureFragment == null)
      {
        this.sensorCaptureFragment = new SensorCaptureFragment();
        replaceFragment(this.sensorCaptureFragment);
      }
    }
    for (;;)
    {
      return;
      if ((CONNECTION_STATE.SOCKET_NOT_CONNECTED == paramCONNECTION_STATE) || (CONNECTION_STATE.SOCKET_BROKEN == paramCONNECTION_STATE) || (CONNECTION_STATE.SOCKET_RECONNECTING == paramCONNECTION_STATE) || (CONNECTION_STATE.BLUETOOTH_OFF == paramCONNECTION_STATE))
      {
        this.sensorSetupFragment = new SensorSetupFragment();
        replaceFragment(this.sensorSetupFragment);
        this.sensorCaptureFragment = null;
      }
    }
  }
  
  public void handleStreamPacket(Bundle paramBundle)
  {
    super.handleStreamPacket(paramBundle);
    byte[] arrayOfByte = paramBundle.getByteArray("REFRESH_BED_NEW_DATA");
    int i = paramBundle.getByte("REFRESH_BED_NEW_DATA_TYPE");
    Log.d("com.resmed.refresh.ui", "handleStreamPacket decobbed : " + Arrays.toString(arrayOfByte) + "packet type : " + i);
    if (VLPacketType.PACKET_TYPE_NOTE_ENV_1.ordinal() == i) {
      handleNoteEnv(arrayOfByte);
    }
    for (;;)
    {
      return;
      if (VLPacketType.PACKET_TYPE_NOTE_BIO_1.ordinal() == i) {
        handleBioToGraph(arrayOfByte);
      } else if (VLPacketType.PACKET_TYPE_ENV_1.ordinal() == i) {
        handleNoteEnv(arrayOfByte);
      }
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903087);
    boolean bool = RefreshApplication.getInstance().getConnectionStatus().isSocketConnected();
    if ((!BaseBluetoothActivity.CORRECT_FIRMWARE_VERSION) && (bool))
    {
      startActivity(new Intent(this, UpdateOTAActivity.class));
      finish();
    }
    if (getIntent().getBooleanExtra("com.resmed.refresh.ui.uibase.app.activity_modal", false)) {
      setIsModalActivity(true);
    }
    for (;;)
    {
      FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
      this.sensorSetupFragment = new SensorSetupFragment();
      paramBundle = this.sensorSetupFragment;
      if (bool)
      {
        this.sensorCaptureFragment = new SensorCaptureFragment();
        paramBundle = this.sensorCaptureFragment;
      }
      localFragmentTransaction.add(2131099805, paramBundle);
      localFragmentTransaction.commit();
      setTitle(2131165469);
      showRightButton(2130837770);
      return;
      setTypeRefreshBar(BaseActivity.TypeBar.DEFAULT);
    }
  }
  
  protected void onResume()
  {
    super.onResume();
    checkBluetoothEnabled();
  }
  
  protected void onSaveInstanceState(Bundle paramBundle) {}
  
  protected void rightBtnPressed()
  {
    finish();
    overridePendingTransition(2130968586, 2130968598);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\SensorSetupActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
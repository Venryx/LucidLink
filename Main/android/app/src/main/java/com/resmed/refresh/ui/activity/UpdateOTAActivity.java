package com.resmed.refresh.ui.activity;

import android.os.Bundle;

import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.ui.fragments.UpdateOTAFragment;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;

public class UpdateOTAActivity
  extends BaseBluetoothActivity
{
  private boolean allowBackPressed = true;
  private UpdateOTAFragment frag;
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    super.handleConnectionStatus(paramCONNECTION_STATE);
    if ((this.frag != null) && (!BaseBluetoothActivity.UPDATING_FIRMWARE)) {
      this.frag.handleConnectionStatus(paramCONNECTION_STATE);
    }
  }
  
  public void handleReceivedRpc(JsonRPC paramJsonRPC)
  {
    if (this.frag != null) {
      this.frag.handleReceivedRpc(paramJsonRPC);
    }
  }
  
  public void handleStreamPacket(Bundle paramBundle)
  {
    if (this.frag != null) {
      this.frag.handleStreamPacket(paramBundle);
    }
  }
  
  public void onBackPressed()
  {
    if (this.allowBackPressed) {
      super.onBackPressed();
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903101);
    setTitle(2131165310);
    this.frag = ((UpdateOTAFragment)getSupportFragmentManager().findFragmentById(2131099794));
    changeRefreshBarVisibility(false, false);
  }
  
  protected void onResume()
  {
    super.onResume();
    sendRpcToBed(getRpcCommands().stopRealTimeStream());
  }
  
  public void setAllowBackPressed(boolean paramBoolean)
  {
    this.allowBackPressed = paramBoolean;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\UpdateOTAActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
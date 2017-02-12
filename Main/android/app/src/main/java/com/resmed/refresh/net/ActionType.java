package com.resmed.refresh.net;

public enum ActionType
{
  LOG_BLUETOOTH_CONNECTED("logBluetoothConnected"),  LOG_BLUETOOTH_DISCONNECTED("logBluetoothDisconnected"),  LOG_BLUETOOTH_SLEEP_SESSION("logStartedSleepSession"),  LOG_BLUETOOTH_STOPPED_SLEEP_SESSION("logStoppedSleepSession");
  
  private String description;
  
  private ActionType(String paramString1)
  {
    this.description = paramString1;
  }
  
  public String toString()
  {
    return this.description;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\net\ActionType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
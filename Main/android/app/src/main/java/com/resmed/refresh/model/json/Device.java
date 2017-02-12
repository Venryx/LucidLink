package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

public class Device
{
  @SerializedName("AppVersion")
  private String appVersion;
  @SerializedName("DeviceId")
  private String deviceId;
  @SerializedName("DeviceModel")
  private String deviceModel;
  @SerializedName("DeviceName")
  private String deviceName;
  @SerializedName("DeviceOS")
  private String deviceOS;
  @SerializedName("DeviceOSVersion")
  private String deviceOSVersion;
  
  public String getAppVersion()
  {
    return this.appVersion;
  }
  
  public String getDeviceId()
  {
    return this.deviceId;
  }
  
  public String getDeviceModel()
  {
    return this.deviceModel;
  }
  
  public String getDeviceName()
  {
    return this.deviceName;
  }
  
  public String getDeviceOS()
  {
    return this.deviceOS;
  }
  
  public String getDeviceOSVersion()
  {
    return this.deviceOSVersion;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\json\Device.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
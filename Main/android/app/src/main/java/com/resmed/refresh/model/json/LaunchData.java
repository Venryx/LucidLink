package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

public class LaunchData
{
  @SerializedName("AppVersion")
  private String appVersion;
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
  
  public void setAppVersion(String paramString)
  {
    this.appVersion = paramString;
  }
  
  public void setDeviceModel(String paramString)
  {
    this.deviceModel = paramString;
  }
  
  public void setDeviceName(String paramString)
  {
    this.deviceName = paramString;
  }
  
  public void setDeviceOS(String paramString)
  {
    this.deviceOS = paramString;
  }
  
  public void setDeviceOSVersion(String paramString)
  {
    this.deviceOSVersion = paramString;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
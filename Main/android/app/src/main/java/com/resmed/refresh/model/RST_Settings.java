package com.resmed.refresh.model;

public class RST_Settings
{
  private String countryCode;
  private boolean firmwareVersion;
  private int heightUnit;
  private Long id;
  private String locale;
  private boolean locationPermission;
  private boolean pushNotifications;
  private boolean tac1;
  private boolean tac2;
  private boolean tac3;
  private int temperatureUnit;
  private boolean useMetricUnits;
  private int weightUnit;
  
  public RST_Settings() {}
  
  public RST_Settings(Long paramLong)
  {
    this.id = paramLong;
  }
  
  public RST_Settings(Long paramLong, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, boolean paramBoolean7, int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2)
  {
    this.id = paramLong;
    this.firmwareVersion = paramBoolean1;
    this.locationPermission = paramBoolean2;
    this.useMetricUnits = paramBoolean3;
    this.pushNotifications = paramBoolean4;
    this.tac1 = paramBoolean5;
    this.tac2 = paramBoolean6;
    this.tac3 = paramBoolean7;
    this.weightUnit = paramInt2;
    this.heightUnit = paramInt1;
    this.temperatureUnit = paramInt3;
    this.locale = paramString1;
    this.countryCode = paramString2;
  }
  
  public String getCountryCode()
  {
    return this.countryCode;
  }
  
  public boolean getFirmwareVersion()
  {
    return this.firmwareVersion;
  }
  
  public int getHeightUnit()
  {
    return this.heightUnit;
  }
  
  public Long getId()
  {
    return this.id;
  }
  
  public String getLocale()
  {
    return this.locale;
  }
  
  public boolean getLocationPermission()
  {
    return this.locationPermission;
  }
  
  public boolean getPushNotifications()
  {
    return this.pushNotifications;
  }
  
  public boolean getTac1()
  {
    return this.tac1;
  }
  
  public boolean getTac2()
  {
    return this.tac2;
  }
  
  public boolean getTac3()
  {
    return this.tac3;
  }
  
  public int getTemperatureUnit()
  {
    return this.temperatureUnit;
  }
  
  public boolean getUseMetricUnits()
  {
    return this.useMetricUnits;
  }
  
  public int getWeightUnit()
  {
    return this.weightUnit;
  }
  
  public void setCountryCode(String paramString)
  {
    this.countryCode = paramString;
  }
  
  public void setFirmwareVersion(boolean paramBoolean)
  {
    this.firmwareVersion = paramBoolean;
  }
  
  public void setHeightUnit(int paramInt)
  {
    this.heightUnit = paramInt;
  }
  
  public void setId(Long paramLong)
  {
    this.id = paramLong;
  }
  
  public void setLocale(String paramString)
  {
    this.locale = paramString;
  }
  
  public void setLocationPermission(boolean paramBoolean)
  {
    this.locationPermission = paramBoolean;
  }
  
  public void setPushNotifications(boolean paramBoolean)
  {
    this.pushNotifications = paramBoolean;
  }
  
  public void setTac1(boolean paramBoolean)
  {
    this.tac1 = paramBoolean;
  }
  
  public void setTac2(boolean paramBoolean)
  {
    this.tac2 = paramBoolean;
  }
  
  public void setTac3(boolean paramBoolean)
  {
    this.tac3 = paramBoolean;
  }
  
  public void setTemperatureUnit(int paramInt)
  {
    this.temperatureUnit = paramInt;
  }
  
  public void setUseMetricUnits(boolean paramBoolean)
  {
    this.useMetricUnits = paramBoolean;
  }
  
  public void setWeightUnit(int paramInt)
  {
    this.weightUnit = paramInt;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
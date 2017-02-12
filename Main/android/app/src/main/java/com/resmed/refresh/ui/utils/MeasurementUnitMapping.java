package com.resmed.refresh.ui.utils;

public class MeasurementUnitMapping
{
  private int heightUnitOff = 0;
  private int heightUnitOn = 1;
  private int imperialSettings = 1;
  private String locale = "en_US";
  private String localeCountryCode = "US";
  private int weightUnitOff = 0;
  private int weightUnitOn = 1;
  
  public MeasurementUnitMapping(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this.localeCountryCode = paramString1;
    this.locale = paramString2;
    this.heightUnitOn = paramInt1;
    this.weightUnitOn = paramInt2;
    this.imperialSettings = paramInt3;
    this.heightUnitOff = paramInt4;
    this.weightUnitOff = paramInt5;
  }
  
  public int getHeightUnitOff()
  {
    return this.heightUnitOff;
  }
  
  public int getHeightUnitOn()
  {
    return this.heightUnitOn;
  }
  
  public int getImperialSettings()
  {
    return this.imperialSettings;
  }
  
  public String getLocale()
  {
    return this.locale;
  }
  
  public String getLocaleCountryCode()
  {
    return this.localeCountryCode;
  }
  
  public int getWeightUnitOff()
  {
    return this.weightUnitOff;
  }
  
  public int getWeightUnitOn()
  {
    return this.weightUnitOn;
  }
  
  public void setHeightUnitOff(int paramInt)
  {
    this.heightUnitOff = paramInt;
  }
  
  public void setHeightUnitOn(int paramInt)
  {
    this.heightUnitOn = paramInt;
  }
  
  public void setImperialSettings(int paramInt)
  {
    this.imperialSettings = paramInt;
  }
  
  public void setLocale(String paramString)
  {
    this.locale = paramString;
  }
  
  public void setLocaleCountryCode(String paramString)
  {
    this.localeCountryCode = paramString;
  }
  
  public void setWeightUnitOff(int paramInt)
  {
    this.weightUnitOff = paramInt;
  }
  
  public void setWeightUnitOn(int paramInt)
  {
    this.weightUnitOn = paramInt;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

public class LocaleMUnitResponse
{
  @SerializedName("HeightUnit")
  private int heightUnit = 0;
  @SerializedName("LocaleCountry")
  private String localeCountry = "US";
  @SerializedName("TemperatureUnit")
  private int temperatureUnit = 0;
  @SerializedName("WeightUnit")
  private int weightUnit = 0;
  
  public int getHeightUnit()
  {
    return this.heightUnit;
  }
  
  public String getLocaleCountry()
  {
    return this.localeCountry;
  }
  
  public int getTemperatureUnit()
  {
    return this.temperatureUnit;
  }
  
  public int getWeightUnit()
  {
    return this.weightUnit;
  }
  
  public void setHeightUnit(int paramInt)
  {
    this.heightUnit = paramInt;
  }
  
  public void setLocaleCountry(String paramString)
  {
    this.localeCountry = paramString;
  }
  
  public void setTemperatureUnit(int paramInt)
  {
    this.temperatureUnit = paramInt;
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
package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

public class Location
{
  @SerializedName("Direction")
  private String direction;
  @SerializedName("Latitude")
  private String latitude;
  @SerializedName("Longitude")
  private String longitude;
  @SerializedName("TimeZone")
  private String timeZone;
  
  public String getDirection()
  {
    return this.direction;
  }
  
  public String getLatitude()
  {
    return this.latitude;
  }
  
  public String getLongitude()
  {
    return this.longitude;
  }
  
  public String getTimeZone()
  {
    return this.timeZone;
  }
  
  public void setDirection(String paramString)
  {
    this.direction = paramString;
  }
  
  public void setLatitude(String paramString)
  {
    this.latitude = paramString;
  }
  
  public void setLongitude(String paramString)
  {
    this.longitude = paramString;
  }
  
  public void setTimeZone(String paramString)
  {
    this.timeZone = paramString;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\json\Location.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
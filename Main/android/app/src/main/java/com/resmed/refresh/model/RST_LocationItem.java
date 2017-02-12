package com.resmed.refresh.model;

public class RST_LocationItem
{
  private float direction;
  private Long id;
  private float latitude;
  private String locale;
  private float longitude;
  private int timezone;
  
  public RST_LocationItem() {}
  
  public RST_LocationItem(Long paramLong)
  {
    this.id = paramLong;
  }
  
  public RST_LocationItem(Long paramLong, float paramFloat1, float paramFloat2, float paramFloat3, String paramString, int paramInt)
  {
    this.id = paramLong;
    this.direction = paramFloat1;
    this.latitude = paramFloat2;
    this.longitude = paramFloat3;
    this.locale = paramString;
    this.timezone = paramInt;
  }
  
  public float getDirection()
  {
    return this.direction;
  }
  
  public Long getId()
  {
    return this.id;
  }
  
  public float getLatitude()
  {
    return this.latitude;
  }
  
  public String getLocale()
  {
    return this.locale;
  }
  
  public float getLongitude()
  {
    return this.longitude;
  }
  
  public int getTimezone()
  {
    return this.timezone;
  }
  
  public void setDirection(float paramFloat)
  {
    this.direction = paramFloat;
  }
  
  public void setId(Long paramLong)
  {
    this.id = paramLong;
  }
  
  public void setLatitude(float paramFloat)
  {
    this.latitude = paramFloat;
  }
  
  public void setLocale(String paramString)
  {
    this.locale = paramString;
  }
  
  public void setLongitude(float paramFloat)
  {
    this.longitude = paramFloat;
  }
  
  public void setTimezone(int paramInt)
  {
    this.timezone = paramInt;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RST_LocationItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
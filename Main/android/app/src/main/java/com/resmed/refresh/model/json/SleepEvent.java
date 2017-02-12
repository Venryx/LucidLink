package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

public class SleepEvent
{
  @SerializedName("SampleNumber")
  private int sampleNumber;
  @SerializedName("Type")
  private int type;
  @SerializedName("Value")
  private int value;
  
  public Integer getSampleNumber()
  {
    return Integer.valueOf(this.sampleNumber);
  }
  
  public int getType()
  {
    return this.type;
  }
  
  public Integer getValue()
  {
    return Integer.valueOf(this.value);
  }
  
  public void setSampleNumber(int paramInt)
  {
    this.sampleNumber = paramInt;
  }
  
  public void setType(int paramInt)
  {
    this.type = paramInt;
  }
  
  public void setValue(int paramInt)
  {
    this.value = paramInt;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\json\SleepEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
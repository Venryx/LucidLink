package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

public class HypnogramInfo
{
  @SerializedName("EpochNumber")
  private int epochNumber;
  
  public int getEpochNumber()
  {
    return this.epochNumber;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\json\HypnogramInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
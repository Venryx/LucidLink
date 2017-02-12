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


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SyncData
{
  @SerializedName("Advices")
  private List<Advice> advices;
  @SerializedName("Records")
  private List<Record> records;
  @SerializedName("UserProfile")
  private UserProfile userProfile;
  
  public List<Advice> getAdvices()
  {
    return this.advices;
  }
  
  public List<Record> getRecords()
  {
    return this.records;
  }
  
  public UserProfile getUserProfile()
  {
    return this.userProfile;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\json\SyncData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
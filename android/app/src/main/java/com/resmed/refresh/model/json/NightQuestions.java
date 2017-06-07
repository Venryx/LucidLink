package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NightQuestions
{
  @SerializedName("PreSleepQuestions")
  private List<PreSleepQuestion> PreSleepQuestion;
  @SerializedName("ContentVersion")
  private int contentVersion;
  
  public int getContentVersion()
  {
    return this.contentVersion;
  }
  
  public List<PreSleepQuestion> getPreSleepQuestion()
  {
    return this.PreSleepQuestion;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
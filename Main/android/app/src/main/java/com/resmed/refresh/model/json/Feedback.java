package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

public class Feedback
{
  @SerializedName("AdviceId")
  private Long adviceId;
  @SerializedName("UserFeedback")
  private Integer userFeedback;
  
  public Feedback(long paramLong, int paramInt)
  {
    this.adviceId = Long.valueOf(paramLong);
    this.userFeedback = Integer.valueOf(paramInt);
  }
  
  public Long getAdviceId()
  {
    return this.adviceId;
  }
  
  public Integer getUserFeedback()
  {
    return this.userFeedback;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\json\Feedback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
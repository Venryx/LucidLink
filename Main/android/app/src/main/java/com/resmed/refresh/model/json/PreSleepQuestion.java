package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PreSleepQuestion
{
  @SerializedName("AnswerId")
  private int answerId;
  @SerializedName("Id")
  private int backendId;
  @SerializedName("DisplayAnswers")
  private List<DisplayAnswers> displayAnswers;
  @SerializedName("DisplayText")
  private String displayText = "";
  @SerializedName("QuestionId")
  private int questionId;
  
  public int getAnswerId()
  {
    return this.answerId;
  }
  
  public int getBackendId()
  {
    return this.backendId;
  }
  
  public List<DisplayAnswers> getDisplayAnswers()
  {
    return this.displayAnswers;
  }
  
  public String getDisplayText()
  {
    return this.displayText;
  }
  
  public int getQuestionId()
  {
    return this.questionId;
  }
  
  public void setAnswerId(int paramInt)
  {
    this.answerId = paramInt;
  }
  
  public void setQuestionId(int paramInt)
  {
    this.questionId = paramInt;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\json\PreSleepQuestion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.model.mindclear;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MindClearText
  extends MindClearBase
{
  @Expose
  @SerializedName("Text")
  private String text;
  
  public MindClearText(String paramString, long paramLong)
  {
    this.text = paramString;
    this.timeStamp = paramLong;
    this.read = false;
    this.type = "TEXT";
  }
  
  public String getDescription()
  {
    return this.text;
  }
  
  public void setText(String paramString)
  {
    this.text = paramString;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\mindclear\MindClearText.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
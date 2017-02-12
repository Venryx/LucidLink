package com.resmed.refresh.model.mindclear;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.resmed.refresh.utils.RefreshTools;
import java.io.File;

public class MindClearVoice
  extends MindClearBase
{
  @Expose
  @SerializedName("Duration")
  private Integer duration;
  @Expose
  @SerializedName("FileName")
  private String fileName;
  
  private MindClearVoice() {}
  
  public MindClearVoice(String paramString, long paramLong, Integer paramInteger)
  {
    this.fileName = paramString;
    this.timeStamp = paramLong;
    this.duration = paramInteger;
    this.type = "VOICE";
  }
  
  public void deleteFileAudio()
  {
    new File(this.fileName).delete();
  }
  
  public String getDescription()
  {
    return RefreshTools.getMinsSecsStringForTime(this.duration.intValue());
  }
  
  public String getFileName()
  {
    return this.fileName;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\mindclear\MindClearVoice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
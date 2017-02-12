package com.resmed.refresh.model.mindclear;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MindClearItems
{
  @Expose(deserialize=false, serialize=false)
  private static RefreshModelController refreshModelController;
  @Expose(deserialize=false, serialize=false)
  private final String BASE_MINDCLEAR_VOICE_FILENAME = "mindclear_voice_";
  @Expose(deserialize=false, serialize=false)
  private final String BASE_MINDCLEAR_VOICE_FILENAME_TEMP = "mindclear_voice_temp";
  @Expose(deserialize=false, serialize=false)
  private final Integer DEFAULT_FILE_NAME_PROGRESSION = Integer.valueOf(0);
  @Expose(deserialize=false, serialize=false)
  private Integer currentPosition;
  @Expose
  @SerializedName("Header")
  public long fileNameProgression = this.DEFAULT_FILE_NAME_PROGRESSION.intValue();
  @Expose
  @SerializedName("Data")
  private List<MindClearBase> mindClearItems = new ArrayList();
  
  private long getTimeStamp()
  {
    Calendar localCalendar = Calendar.getInstance();
    return Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(localCalendar.getTime()));
  }
  
  public void addElement(MindClearBase paramMindClearBase)
  {
    this.mindClearItems.add(paramMindClearBase);
  }
  
  public String getFileName()
  {
    this.fileNameProgression = getTimeStamp();
    File localFile = new File(RefreshTools.getFilesPath().getAbsolutePath());
    if (!localFile.exists()) {
      localFile.mkdir();
    }
    Log.d("com.resmed.refresh.mindClear", "Mind clear file exits : " + localFile.exists());
    return "mindclear_voice_" + this.fileNameProgression + ".wav";
  }
  
  public List<MindClearBase> getMindClearNotes()
  {
    return this.mindClearItems;
  }
  
  public String getTempFileName()
  {
    File localFile = new File(RefreshTools.getFilesPath().getAbsolutePath());
    if (!localFile.exists()) {
      localFile.mkdir();
    }
    return "mindclear_voice_" + this.fileNameProgression + ".pcm";
  }
  
  public void setMindCleatNotes(List<MindClearBase> paramList)
  {
    this.mindClearItems = paramList;
  }
  
  public void updateItem(int paramInt, MindClearBase paramMindClearBase)
  {
    this.mindClearItems.remove(this.mindClearItems.get(paramInt));
    this.mindClearItems.add(paramMindClearBase);
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
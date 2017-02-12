package com.resmed.refresh.model.mindclear;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class MindClearBase
{
  @Expose
  @SerializedName("Read")
  protected boolean read;
  @Expose
  @SerializedName("Timestamp")
  protected long timeStamp;
  @Expose
  @SerializedName("Type")
  protected String type;
  
  public String getDateTime()
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(this.timeStamp);
    return new SimpleDateFormat("dd MMM HH:mm").format(localCalendar.getTime());
  }
  
  public abstract String getDescription();
  
  public long getTimestamp()
  {
    return this.timeStamp;
  }
  
  public boolean isRead()
  {
    return this.read;
  }
  
  public void setRead()
  {
    this.read = true;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
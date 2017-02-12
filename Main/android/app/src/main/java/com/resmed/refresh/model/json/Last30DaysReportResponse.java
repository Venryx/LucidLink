package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

public class Last30DaysReportResponse
{
  @SerializedName("EmailCopyIsRequested")
  private boolean emailCopyIsRequested;
  @SerializedName("NumberOfRecords")
  private int numberOfRecords;
  @SerializedName("NumberOfSuitableRecords")
  private int numberOfSuitableRecords;
  @SerializedName("RequestIsAccepted")
  private boolean requestIsAccepted;
  @SerializedName("UtcFromDateTime")
  private String utcFromDateTime;
  @SerializedName("UtcToDateTime")
  private String utcToDateTime;
  
  public Last30DaysReportResponse(String paramString1, String paramString2, boolean paramBoolean)
  {
    this.utcFromDateTime = paramString1;
    this.utcToDateTime = paramString2;
    this.emailCopyIsRequested = paramBoolean;
  }
  
  public int getNumberOfRecords()
  {
    return this.numberOfRecords;
  }
  
  public int getNumberOfSuitableRecords()
  {
    return this.numberOfSuitableRecords;
  }
  
  public String getUtcFromDateTime()
  {
    return this.utcFromDateTime;
  }
  
  public String getUtcToDateTime()
  {
    return this.utcToDateTime;
  }
  
  public boolean isEmailCopyIsRequested()
  {
    return this.emailCopyIsRequested;
  }
  
  public boolean isRequestIsAccepted()
  {
    return this.requestIsAccepted;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
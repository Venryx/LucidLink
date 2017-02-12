package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Record
{
  @SerializedName("Advices")
  private List<Advice> advices;
  @SerializedName("EndDate")
  private String endDate = "";
  @SerializedName("FirmwareVersion")
  private String firmwareVersion;
  @SerializedName("Hypnogram")
  private List<Integer> hypnogram;
  @SerializedName("HypnogramSamplePeriod")
  private int hypnogramSamplePeriod;
  @SerializedName("Light")
  private List<Float> light;
  @SerializedName("LightSamplePeriod")
  private int lightSamplePeriod;
  @SerializedName("Location")
  private Location location;
  @SerializedName("Noise")
  private List<Float> noise;
  @SerializedName("NoiseSamplePeriod")
  private int noiseSamplePeriod;
  @SerializedName("PreSleepQuestions")
  private List<PreSleepQuestion> preSleepQuestions;
  @SerializedName("RecordId")
  private Long recordId;
  @SerializedName("RM20Version")
  private String rm20Version;
  @SerializedName("SleepEvents")
  private List<SleepEvent> sleepEvents;
  @SerializedName("StartDate")
  private String startDate = "";
  @SerializedName("SynopsisData")
  private SynopsisData synopsisData = new SynopsisData();
  @SerializedName("Temperature")
  private List<Float> temperature;
  @SerializedName("TemperatureSamplePeriod")
  private int temperatureSamplePeriod;
  @SerializedName("UserProfile")
  private UserProfile userProfile;
  
  public List<Advice> getAdvices()
  {
    return this.advices;
  }
  
  public String getEndDate()
  {
    return this.endDate;
  }
  
  public List<Integer> getHypnogram()
  {
    return this.hypnogram;
  }
  
  public int getHypnogramSamplePeriod()
  {
    return this.hypnogramSamplePeriod;
  }
  
  public List<Float> getLight()
  {
    return this.light;
  }
  
  public int getLightSamplePeriod()
  {
    return this.lightSamplePeriod;
  }
  
  public Location getLocation()
  {
    return this.location;
  }
  
  public List<Float> getNoise()
  {
    return this.noise;
  }
  
  public int getNoiseSamplePeriod()
  {
    return this.noiseSamplePeriod;
  }
  
  public List<PreSleepQuestion> getPreSleepQuestions()
  {
    return this.preSleepQuestions;
  }
  
  public Long getRecordId()
  {
    return this.recordId;
  }
  
  public List<SleepEvent> getSleepEvents()
  {
    return this.sleepEvents;
  }
  
  public String getStartDate()
  {
    return this.startDate;
  }
  
  public SynopsisData getSynopsisData()
  {
    return this.synopsisData;
  }
  
  public List<Float> getTemperature()
  {
    return this.temperature;
  }
  
  public int getTemperatureSamplePeriod()
  {
    return this.temperatureSamplePeriod;
  }
  
  public UserProfile getUserProfile()
  {
    return this.userProfile;
  }
  
  public void setAdvices(List<Advice> paramList)
  {
    this.advices = paramList;
  }
  
  public void setEndDate(String paramString)
  {
    this.endDate = paramString;
  }
  
  public void setFirmwareVersion(String paramString)
  {
    this.firmwareVersion = paramString;
  }
  
  public void setHypnogram(List<Integer> paramList)
  {
    this.hypnogram = paramList;
  }
  
  public void setHypnogramSamplePeriod(int paramInt)
  {
    this.hypnogramSamplePeriod = paramInt;
  }
  
  public void setLight(List<Float> paramList)
  {
    this.light = paramList;
  }
  
  public void setLightSamplePeriod(int paramInt)
  {
    this.lightSamplePeriod = paramInt;
  }
  
  public void setLocation(Location paramLocation)
  {
    this.location = paramLocation;
  }
  
  public void setNoise(List<Float> paramList)
  {
    this.noise = paramList;
  }
  
  public void setNoiseSamplePeriod(int paramInt)
  {
    this.noiseSamplePeriod = paramInt;
  }
  
  public void setPreSleepQuestions(List<PreSleepQuestion> paramList)
  {
    this.preSleepQuestions = paramList;
  }
  
  public void setRecordId(Long paramLong)
  {
    this.recordId = paramLong;
  }
  
  public void setRm20Version(String paramString)
  {
    this.rm20Version = paramString;
  }
  
  public void setSleepEvents(List<SleepEvent> paramList)
  {
    this.sleepEvents = paramList;
  }
  
  public void setStartDate(String paramString)
  {
    this.startDate = paramString;
  }
  
  public void setSynopsisData(SynopsisData paramSynopsisData)
  {
    this.synopsisData = paramSynopsisData;
  }
  
  public void setTemperature(List<Float> paramList)
  {
    this.temperature = paramList;
  }
  
  public void setTemperatureSamplePeriod(int paramInt)
  {
    this.temperatureSamplePeriod = paramInt;
  }
  
  public void setUserProfile(UserProfile paramUserProfile)
  {
    this.userProfile = paramUserProfile;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\json\Record.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
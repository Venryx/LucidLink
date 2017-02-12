package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

public class SynopsisData
{
  @SerializedName("BinSleepScoreDeep")
  private int BinSleepScoreDeep;
  @SerializedName("BinSleepScoreLight")
  private int BinSleepScoreLight;
  @SerializedName("BinSleepScoreOnset")
  private int BinSleepScoreOnset;
  @SerializedName("BinSleepScoreRem")
  private int BinSleepScoreRem;
  @SerializedName("BinSleepScoreTst")
  private int BinSleepScoreTst;
  @SerializedName("BinSleepScoreWaso")
  private int BinSleepScoreWaso;
  @SerializedName("AlarmFireEpoch")
  private int alarmFireEpoch;
  @SerializedName("BodyScore")
  private int bodyScore;
  @SerializedName("DeepSleepDuration")
  private int deepSleepDuration;
  @SerializedName("LightSleepDuration")
  private int lightSleepDuration;
  @SerializedName("MindScore")
  private int mindScore;
  @SerializedName("NumberOfInterruptions")
  private int numberOfInterruptions;
  @SerializedName("REMDuration")
  private int remDuration;
  @SerializedName("SignalQualityIsValid")
  private int signalQualityIsValid = 0;
  @SerializedName("SignalQualityMean")
  private float signalQualityMean;
  @SerializedName("SignalQualityPercBin1")
  private float signalQualityPercBin1;
  @SerializedName("SignalQualityPercBin2")
  private float signalQualityPercBin2;
  @SerializedName("SignalQualityPercBin3")
  private float signalQualityPercBin3;
  @SerializedName("SignalQualityPercBin4")
  private float signalQualityPercBin4;
  @SerializedName("SignalQualityPercBin5")
  private float signalQualityPercBin5;
  @SerializedName("SignalQualityStd")
  private float signalQualityStd;
  @SerializedName("SignalQualityValue")
  private int signalQualityValue;
  @SerializedName("SleepScore")
  private int sleepScore;
  @SerializedName("TimeInBed")
  private int timeInBed;
  @SerializedName("TimeToSleep")
  private int timeToSleep;
  @SerializedName("TotalRecordingTime")
  private int totalRecordingTime;
  @SerializedName("TotalSleepTime")
  private int totalSleepTime;
  @SerializedName("TotalWakeTime")
  private int totalWakeTime;
  
  public int getAlarmFireEpoch()
  {
    return this.alarmFireEpoch;
  }
  
  public int getBinSleepScoreDeep()
  {
    return this.BinSleepScoreDeep;
  }
  
  public int getBinSleepScoreLight()
  {
    return this.BinSleepScoreLight;
  }
  
  public int getBinSleepScoreOnset()
  {
    return this.BinSleepScoreOnset;
  }
  
  public int getBinSleepScoreRem()
  {
    return this.BinSleepScoreRem;
  }
  
  public int getBinSleepScoreTst()
  {
    return this.BinSleepScoreTst;
  }
  
  public int getBinSleepScoreWaso()
  {
    return this.BinSleepScoreWaso;
  }
  
  public int getBodyScore()
  {
    return this.bodyScore;
  }
  
  public int getDeepSleepDuration()
  {
    return this.deepSleepDuration;
  }
  
  public int getLightSleepDuration()
  {
    return this.lightSleepDuration;
  }
  
  public int getMindScore()
  {
    return this.mindScore;
  }
  
  public int getNumberOfInterruptions()
  {
    return this.numberOfInterruptions;
  }
  
  public int getRemDuration()
  {
    return this.remDuration;
  }
  
  public float getSignalQualityMean()
  {
    return this.signalQualityMean;
  }
  
  public float getSignalQualityPercBin1()
  {
    return this.signalQualityPercBin1;
  }
  
  public float getSignalQualityPercBin2()
  {
    return this.signalQualityPercBin2;
  }
  
  public float getSignalQualityPercBin3()
  {
    return this.signalQualityPercBin3;
  }
  
  public float getSignalQualityPercBin4()
  {
    return this.signalQualityPercBin4;
  }
  
  public float getSignalQualityPercBin5()
  {
    return this.signalQualityPercBin5;
  }
  
  public float getSignalQualityStd()
  {
    return this.signalQualityStd;
  }
  
  public int getSignalQualityValue()
  {
    return this.signalQualityValue;
  }
  
  public int getSleepScore()
  {
    return this.sleepScore;
  }
  
  public int getTimeInBed()
  {
    return this.timeInBed;
  }
  
  public int getTimeToSleep()
  {
    return this.timeToSleep;
  }
  
  public int getTotalRecordingTime()
  {
    return this.totalRecordingTime;
  }
  
  public int getTotalSleepTime()
  {
    return this.totalSleepTime;
  }
  
  public int getTotalWakeTime()
  {
    return this.totalWakeTime;
  }
  
  public void setAlarmFireEpoch(int paramInt)
  {
    this.alarmFireEpoch = paramInt;
  }
  
  public void setBinSleepScoreDeep(int paramInt)
  {
    this.BinSleepScoreDeep = paramInt;
  }
  
  public void setBinSleepScoreLight(int paramInt)
  {
    this.BinSleepScoreLight = paramInt;
  }
  
  public void setBinSleepScoreOnset(int paramInt)
  {
    this.BinSleepScoreOnset = paramInt;
  }
  
  public void setBinSleepScoreRem(int paramInt)
  {
    this.BinSleepScoreRem = paramInt;
  }
  
  public void setBinSleepScoreTst(int paramInt)
  {
    this.BinSleepScoreTst = paramInt;
  }
  
  public void setBinSleepScoreWaso(int paramInt)
  {
    this.BinSleepScoreWaso = paramInt;
  }
  
  public void setBodyScore(int paramInt)
  {
    this.bodyScore = paramInt;
  }
  
  public void setDeepSleepDuration(int paramInt)
  {
    this.deepSleepDuration = paramInt;
  }
  
  public void setLightSleepDuration(int paramInt)
  {
    this.lightSleepDuration = paramInt;
  }
  
  public void setMindScore(int paramInt)
  {
    this.mindScore = paramInt;
  }
  
  public void setNumberOfInterruptions(int paramInt)
  {
    this.numberOfInterruptions = paramInt;
  }
  
  public void setRemDuration(int paramInt)
  {
    this.remDuration = paramInt;
  }
  
  public void setSignalQualityIsValid(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = 1;; i = 0)
    {
      this.signalQualityIsValid = i;
      return;
    }
  }
  
  public void setSignalQualityMean(float paramFloat)
  {
    this.signalQualityMean = paramFloat;
  }
  
  public void setSignalQualityPercBin1(float paramFloat)
  {
    this.signalQualityPercBin1 = paramFloat;
  }
  
  public void setSignalQualityPercBin2(float paramFloat)
  {
    this.signalQualityPercBin2 = paramFloat;
  }
  
  public void setSignalQualityPercBin3(float paramFloat)
  {
    this.signalQualityPercBin3 = paramFloat;
  }
  
  public void setSignalQualityPercBin4(float paramFloat)
  {
    this.signalQualityPercBin4 = paramFloat;
  }
  
  public void setSignalQualityPercBin5(float paramFloat)
  {
    this.signalQualityPercBin5 = paramFloat;
  }
  
  public void setSignalQualityStd(float paramFloat)
  {
    this.signalQualityStd = paramFloat;
  }
  
  public void setSignalQualityValue(int paramInt)
  {
    this.signalQualityValue = paramInt;
  }
  
  public void setSleepScore(int paramInt)
  {
    this.sleepScore = paramInt;
  }
  
  public void setTimeInBed(int paramInt)
  {
    this.timeInBed = paramInt;
  }
  
  public void setTimeToSleep(int paramInt)
  {
    this.timeToSleep = paramInt;
  }
  
  public void setTotalRecordingTime(int paramInt)
  {
    this.totalRecordingTime = paramInt;
  }
  
  public void setTotalSleepTime(int paramInt)
  {
    this.totalSleepTime = paramInt;
  }
  
  public void setTotalWakeTime(int paramInt)
  {
    this.totalWakeTime = paramInt;
  }
  
  public boolean signalQualityIsValid()
  {
    boolean bool = true;
    if (this.signalQualityIsValid == 1) {}
    for (;;)
    {
      return bool;
      bool = false;
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
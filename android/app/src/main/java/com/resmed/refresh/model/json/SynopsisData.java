package com.resmed.refresh.model.json;

import com.google.gson.annotations.*;

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
	private int signalQualityIsValid;
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

	public SynopsisData() {
		this.signalQualityIsValid = 0;
	}

	public int getAlarmFireEpoch() {
		return this.alarmFireEpoch;
	}

	public int getBinSleepScoreDeep() {
		return this.BinSleepScoreDeep;
	}

	public int getBinSleepScoreLight() {
		return this.BinSleepScoreLight;
	}

	public int getBinSleepScoreOnset() {
		return this.BinSleepScoreOnset;
	}

	public int getBinSleepScoreRem() {
		return this.BinSleepScoreRem;
	}

	public int getBinSleepScoreTst() {
		return this.BinSleepScoreTst;
	}

	public int getBinSleepScoreWaso() {
		return this.BinSleepScoreWaso;
	}

	public int getBodyScore() {
		return this.bodyScore;
	}

	public int getDeepSleepDuration() {
		return this.deepSleepDuration;
	}

	public int getLightSleepDuration() {
		return this.lightSleepDuration;
	}

	public int getMindScore() {
		return this.mindScore;
	}

	public int getNumberOfInterruptions() {
		return this.numberOfInterruptions;
	}

	public int getRemDuration() {
		return this.remDuration;
	}

	public float getSignalQualityMean() {
		return this.signalQualityMean;
	}

	public float getSignalQualityPercBin1() {
		return this.signalQualityPercBin1;
	}

	public float getSignalQualityPercBin2() {
		return this.signalQualityPercBin2;
	}

	public float getSignalQualityPercBin3() {
		return this.signalQualityPercBin3;
	}

	public float getSignalQualityPercBin4() {
		return this.signalQualityPercBin4;
	}

	public float getSignalQualityPercBin5() {
		return this.signalQualityPercBin5;
	}

	public float getSignalQualityStd() {
		return this.signalQualityStd;
	}

	public int getSignalQualityValue() {
		return this.signalQualityValue;
	}

	public int getSleepScore() {
		return this.sleepScore;
	}

	public int getTimeInBed() {
		return this.timeInBed;
	}

	public int getTimeToSleep() {
		return this.timeToSleep;
	}

	public int getTotalRecordingTime() {
		return this.totalRecordingTime;
	}

	public int getTotalSleepTime() {
		return this.totalSleepTime;
	}

	public int getTotalWakeTime() {
		return this.totalWakeTime;
	}

	public void setAlarmFireEpoch(final int alarmFireEpoch) {
		this.alarmFireEpoch = alarmFireEpoch;
	}

	public void setBinSleepScoreDeep(final int binSleepScoreDeep) {
		this.BinSleepScoreDeep = binSleepScoreDeep;
	}

	public void setBinSleepScoreLight(final int binSleepScoreLight) {
		this.BinSleepScoreLight = binSleepScoreLight;
	}

	public void setBinSleepScoreOnset(final int binSleepScoreOnset) {
		this.BinSleepScoreOnset = binSleepScoreOnset;
	}

	public void setBinSleepScoreRem(final int binSleepScoreRem) {
		this.BinSleepScoreRem = binSleepScoreRem;
	}

	public void setBinSleepScoreTst(final int binSleepScoreTst) {
		this.BinSleepScoreTst = binSleepScoreTst;
	}

	public void setBinSleepScoreWaso(final int binSleepScoreWaso) {
		this.BinSleepScoreWaso = binSleepScoreWaso;
	}

	public void setBodyScore(final int bodyScore) {
		this.bodyScore = bodyScore;
	}

	public void setDeepSleepDuration(final int deepSleepDuration) {
		this.deepSleepDuration = deepSleepDuration;
	}

	public void setLightSleepDuration(final int lightSleepDuration) {
		this.lightSleepDuration = lightSleepDuration;
	}

	public void setMindScore(final int mindScore) {
		this.mindScore = mindScore;
	}

	public void setNumberOfInterruptions(final int numberOfInterruptions) {
		this.numberOfInterruptions = numberOfInterruptions;
	}

	public void setRemDuration(final int remDuration) {
		this.remDuration = remDuration;
	}

	public void setSignalQualityIsValid(final boolean b) {
		int signalQualityIsValid;
		if (b) {
			signalQualityIsValid = 1;
		}
		else {
			signalQualityIsValid = 0;
		}
		this.signalQualityIsValid = signalQualityIsValid;
	}

	public void setSignalQualityMean(final float signalQualityMean) {
		this.signalQualityMean = signalQualityMean;
	}

	public void setSignalQualityPercBin1(final float signalQualityPercBin1) {
		this.signalQualityPercBin1 = signalQualityPercBin1;
	}

	public void setSignalQualityPercBin2(final float signalQualityPercBin2) {
		this.signalQualityPercBin2 = signalQualityPercBin2;
	}

	public void setSignalQualityPercBin3(final float signalQualityPercBin3) {
		this.signalQualityPercBin3 = signalQualityPercBin3;
	}

	public void setSignalQualityPercBin4(final float signalQualityPercBin4) {
		this.signalQualityPercBin4 = signalQualityPercBin4;
	}

	public void setSignalQualityPercBin5(final float signalQualityPercBin5) {
		this.signalQualityPercBin5 = signalQualityPercBin5;
	}

	public void setSignalQualityStd(final float signalQualityStd) {
		this.signalQualityStd = signalQualityStd;
	}

	public void setSignalQualityValue(final int signalQualityValue) {
		this.signalQualityValue = signalQualityValue;
	}

	public void setSleepScore(final int sleepScore) {
		this.sleepScore = sleepScore;
	}

	public void setTimeInBed(final int timeInBed) {
		this.timeInBed = timeInBed;
	}

	public void setTimeToSleep(final int timeToSleep) {
		this.timeToSleep = timeToSleep;
	}

	public void setTotalRecordingTime(final int totalRecordingTime) {
		this.totalRecordingTime = totalRecordingTime;
	}

	public void setTotalSleepTime(final int totalSleepTime) {
		this.totalSleepTime = totalSleepTime;
	}

	public void setTotalWakeTime(final int totalWakeTime) {
		this.totalWakeTime = totalWakeTime;
	}

	public boolean signalQualityIsValid() {
		return this.signalQualityIsValid == 1;
	}
}
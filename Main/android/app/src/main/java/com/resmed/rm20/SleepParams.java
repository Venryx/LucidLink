package com.resmed.rm20;

import java.util.Arrays;

public class SleepParams {
	public int absentEpochs;
	public int binDeep;
	public int binLightDuration;
	public int binRem;
	public int binSleepOnset;
	public int binTst;
	public int binWaso;
	public int bodyScore;
	public int deepEpochs;
	public int epochLen;
	public int finalWakeTime;
	private int[] hypnogram;
	public int lightEpochs;
	public int mindScore;
	public int numAwakenings;
	public int numEpochs;
	public int remEpochs;
	public SignalQuality signalQuality;
	public int sleepDuration;
	public int sleepEff;
	public int sleepOnset;
	public int sleepScore;
	public int timeInBed;
	public int timeToSleep;
	public int unknownEpochs;
	public int wakeEpochs;
	public int waso;

	public int[] getHypnogram() {
		return this.hypnogram;
	}

	public void setHypnogram(int[] paramArrayOfInt) {
		this.hypnogram = paramArrayOfInt;
	}

	public String toString() {
		return "epochLen       : " + this.epochLen + "\n numEpochs      : " + this.numEpochs
			+ "\n unknownEpochs  : " + this.unknownEpochs + "\n absentEpochs   : " + this.absentEpochs
			+ "\n finalWakeTime  : " + this.finalWakeTime + "\n sleepEff \t\t : " + this.sleepEff
			+ "\n sleepOnset \t : " + this.sleepOnset + "\n timeToSleep \t : " + this.timeToSleep
			+ "\n timeInBed \t : " + this.timeInBed + "\n sleepDuration  : " + this.sleepDuration + "\n numAwakenings  : "
			+ this.numAwakenings + "\n lightEpochs \t : " + this.lightEpochs + "\n remEpochs \t : " + this.remEpochs
			+ "\n deepEpochs \t : " + this.deepEpochs + "\n bodyScore \t : " + this.bodyScore + "\n mindScore \t : "
			+ this.mindScore + "\n sleepScore \t : " + this.sleepScore + "\n binSleepOnset  : " + this.binSleepOnset
			+ "\n binSleepOnset  : " + this.binLightDuration + "\n binWaso\t\t : " + this.binWaso + "\n binTst \t\t : " + this.binTst
			+ "\n binDeep \t\t : " + this.binDeep + "\n binRem \t\t : " + this.binRem + "\n wakeEpochs \t : " + this.wakeEpochs
			+ "\n waso \t \t\t : " + this.waso + "\n hypnogram \t : " + Arrays.toString(this.hypnogram);
	}
}
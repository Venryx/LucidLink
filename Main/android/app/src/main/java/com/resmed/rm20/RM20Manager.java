package com.resmed.rm20;

import java.util.Date;

public interface RM20Manager {
	int disableSmartAlarm();

	int getEpochCount();

	String getLibVersion();

	int getRealTimeSleepState();

	SmartAlarmInfo getSmartAlarm();

	UserInfo getUserInfo();

	SleepParams resultsForSession();

	int setSmartAlarm(Date paramDate, int paramInt, boolean paramBoolean);

	int startRespRateCallbacks(boolean paramBoolean);

	int startupLibrary(int paramInt1, int paramInt2);

	int stopAndCalculate();

	int writeSampleData(int paramInt1, int paramInt2);

	int writeSamples(int[] paramArrayOfInt1, int[] paramArrayOfInt2);
}
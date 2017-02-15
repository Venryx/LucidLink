package com.resmed.rm20;

import android.content.Context;
import java.io.File;
import java.util.Date;

public class RM20DefaultManager implements RM20Manager {
	private RM20JNI rm20Lib;
	private Thread writerThread;

	public RM20DefaultManager(File filesFolder, RM20Callbacks callbacks, Context context) {
		this.rm20Lib = new RM20JNI(filesFolder, callbacks, context);
	}

	public synchronized String getLibVersion() {
		return this.rm20Lib.getLibVersion();
	}

	public synchronized int getRealTimeSleepState() {
		return this.rm20Lib.getRealTimeSleepState();
	}

	public synchronized int writeSampleData(int uiMI, int uiMQ) {
		return this.rm20Lib.writeSampleData(uiMI, uiMQ);
	}

	public synchronized int writeSamples(int[] uiMI, int[] uiMQ) {
		for (int i = 0; i < uiMQ.length; i++) {
			writeSampleData(uiMI[i], uiMQ[i]);
		}
		return 0;
	}

	public synchronized int stopAndCalculate() {
		return this.rm20Lib.stopAndCalculate();
	}

	public synchronized int getEpochCount() {
		return this.rm20Lib.getEpochCount();
	}

	public synchronized SmartAlarmInfo getSmartAlarm() {
		return this.rm20Lib.getSmartAlarm();
	}

	public synchronized int setSmartAlarm(Date alarmDate, int alarmWindowSeconds, boolean enableCallbacks) {
		int alarmWinStartEpc;
		int alarmWinEndEpc;
		alarmWinStartEpc = 65535;
		alarmWinEndEpc = 65535;
		if (alarmWindowSeconds >= 0) {
			alarmWinEndEpc = ((((int) (alarmDate.getTime() - System.currentTimeMillis())) / 1000) / 30) + this.rm20Lib.getEpochCount();
			alarmWinStartEpc = alarmWinEndEpc - (alarmWindowSeconds / 30);
		}
		if (alarmWinStartEpc < 0) {
			alarmWinStartEpc = 0;
		}
		if (alarmWinEndEpc < 0) {
			alarmWinStartEpc = 65535;
			alarmWinEndEpc = 65535;
		}
		if (alarmWinStartEpc > 65535) {
			alarmWinStartEpc = 65535;
			alarmWinEndEpc = 65535;
		}
		if (alarmWinEndEpc > 65535) {
			alarmWinEndEpc = 65535;
		}
		return this.rm20Lib.setSmartAlarm(alarmWinStartEpc, alarmWinEndEpc, enableCallbacks);
	}

	public synchronized int startRespRateCallbacks(boolean enableCallbacks) {
		return this.rm20Lib.setRespRateCallbacks(enableCallbacks);
	}

	public synchronized SleepParams resultsForSession() {
		return this.rm20Lib.resultsForSession();
	}

	public synchronized UserInfo getUserInfo() {
		return this.rm20Lib.getUserInfo();
	}

	public synchronized int startupLibrary(int age, int gender) {
		return this.rm20Lib.startupLibrary(age, gender);
	}

	public synchronized int disableSmartAlarm() {
		return this.rm20Lib.disableSmartAlarm();
	}
}
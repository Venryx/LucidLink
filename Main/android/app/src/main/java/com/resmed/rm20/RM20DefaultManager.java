package com.resmed.rm20;

import java.io.*;
import android.content.*;
import java.util.*;

import v.lucidlink.V;

public class RM20DefaultManager implements RM20Manager
{
	public RM20JNI rm20Lib;
	private Thread writerThread;

	public RM20DefaultManager(final File file, final RM20Callbacks rm20Callbacks, final Context context) {
		this.rm20Lib = new RM20JNI(file, rm20Callbacks, context);
	}

	public int disableSmartAlarm() {
		synchronized (this) {
			return this.rm20Lib.disableSmartAlarm();
		}
	}

	public int getEpochCount() {
		synchronized (this) {
			return this.rm20Lib.getEpochCount();
		}
	}

	public String getLibVersion() {
		synchronized (this) {
			return this.rm20Lib.getLibVersion();
		}
	}

	public int getRealTimeSleepState() {
		synchronized (this) {
			return this.rm20Lib.getRealTimeSleepState();
		}
	}

	public SmartAlarmInfo getSmartAlarm() {
		synchronized (this) {
			return this.rm20Lib.getSmartAlarm();
		}
	}

	public UserInfo getUserInfo() {
		synchronized (this) {
			return this.rm20Lib.getUserInfo();
		}
	}

	public SleepParams resultsForSession() {
		synchronized (this) {
			return this.rm20Lib.resultsForSession();
		}
	}

	public int setSmartAlarm(final Date date, final int n, final boolean b) {
		// monitorenter(this)
		int n2 = 65535;
		int n3 = 65535;
		while (true) {
			Label_0082: {
				if (n < 0) {
					break Label_0082;
				}
				try {
					n3 = (int)(date.getTime() - System.currentTimeMillis()) / 1000 / 30 + this.rm20Lib.getEpochCount();
					n2 = n3 - n / 30;
					//break Label_0082;
					return this.rm20Lib.setSmartAlarm(n2, n3, b);
				}
				finally {
				}
				// monitorexit(this)
			}
			if (n2 < 0) {
				n2 = 0;
			}
			if (n3 < 0) {
				n2 = 65535;
				n3 = 65535;
			}
			if (n2 > 65535) {
				n2 = 65535;
				n3 = 65535;
			}
			if (n3 > 65535) {
				n3 = 65535;
				continue;
			}
			continue;
		}
	}

	public int startRespRateCallbacks(final boolean respRateCallbacks) {
		synchronized (this) {
			return this.rm20Lib.setRespRateCallbacks(respRateCallbacks);
		}
	}

	public int startupLibrary(final int age, final int gender) {
		synchronized (this) {
			return this.rm20Lib.startupLibrary(age, gender);
		}
	}

	public int stopAndCalculate() {
		synchronized (this) {
			return this.rm20Lib.stopAndCalculate();
		}
	}

	public int writeSampleData(final int n, final int n2) {
		synchronized (this) {
			V.Log("Writing sample data!");
			return this.rm20Lib.writeSampleData(n, n2);
		}
	}

	public int writeSamples(final int[] array, final int[] array2) {
		// monitorenter(this)
		int i = 0;
		try {
			while (i < array2.length) {
				this.writeSampleData(array[i], array2[i]);
				++i;
			}
			return 0;
		}
		finally {
		}
		// monitorexit(this)
	}
}

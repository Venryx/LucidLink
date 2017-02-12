package com.resmed.refresh.sleepsession;

import com.resmed.refresh.model.*;
import com.resmed.refresh.utils.*;

public class RST_SleepSession
{
	private static RST_SleepSession sleepSession;
	private boolean analysisRunning;
	private boolean sessionRunning;

	public static RST_SleepSession getInstance() {
		synchronized (RST_SleepSession.class) {
			if (RST_SleepSession.sleepSession == null) {
				(RST_SleepSession.sleepSession = new RST_SleepSession()).setup();
			}
			return RST_SleepSession.sleepSession;
		}
	}

	private void setup() {
		this.sessionRunning = false;
		this.analysisRunning = false;
	}

	public boolean isSessionRunning() {
		return this.sessionRunning;
	}

	public boolean resumeSession() {
		return this.sessionRunning = true;
	}

	public void setupProcesserWithStorage(final boolean b) {
	}

	public boolean startSession(final boolean b) {
		this.sessionRunning = true;
		Log.stop();
		return true;
	}

	public boolean stopSession(final boolean b) {
		this.sessionRunning = false;
		Log.record();
		return true;
	}

	public boolean suspendSession() {
		this.sessionRunning = false;
		return true;
	}
}

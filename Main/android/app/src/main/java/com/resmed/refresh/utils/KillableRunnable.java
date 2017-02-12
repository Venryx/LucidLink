package com.resmed.refresh.utils;

public class KillableRunnable implements Runnable {
	public interface KillableRunner {
		void executeOnRun();
	}

	private boolean killMe;
	private KillableRunnable.KillableRunner runner;

	public KillableRunnable(final KillableRunnable.KillableRunner runner) {
		this.killMe = false;
		this.runner = runner;
	}

	public void killRunnable() {
		this.killMe = true;
	}

	@Override
	public void run() {
		if (this.killMe) {
			return;
		}
		this.runner.executeOnRun();
	}
}
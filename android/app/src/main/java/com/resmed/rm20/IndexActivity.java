package com.resmed.rm20;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import java.util.Random;

public class IndexActivity extends Activity implements RM20Callbacks {
	private RM20JNI rm20Lib;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RM20JNI.loadLibrary(getApplicationContext()); // custom

		this.rm20Lib = new RM20JNI(Environment.getExternalStorageDirectory(), this, getApplicationContext());
		this.rm20Lib.startupLibrary(34, 1);
		this.rm20Lib.setRespRateCallbacks(true);
		new Thread(new Runnable() {
			public void run() {
				Random rand = new Random();
				for (int i = 0; i < 10000; i++) {
					//IndexActivity.this.rm20Lib.writeSampleData(rand.nextInt(1000), rand.nextInt(1000));
					IndexActivity.this.rm20Lib.writeSampleData(1000 + rand.nextInt(300), 1000 + rand.nextInt(300));
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				IndexActivity.this.rm20Lib.getEpochCount();
				IndexActivity.this.rm20Lib.stopAndCalculate();
				Log.d(getClass().getName(), " results : " + IndexActivity.this.rm20Lib.resultsForSession());
			}
		}).start();
	}

	public void onRm20Alarm(int fireEpoch) {
		Log.d("RM20NDK", " onRm20Alarm");
	}

	public void onRm20ValidBreathingRate(float rate, int secIndex) {
		Log.d("RM20NDK", " onRm20ValidBreathingRate");
	}

	public void onRm20RealTimeSleepState(int pSleepState, int pEpochIdx) {
		Log.d("RM20NDK", " onRm20RealTimeSleepState");
	}
}
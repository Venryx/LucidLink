package com.resmed.rm20;

import android.app.*;
import android.os.*;
import android.util.*;

import java.util.Random;

public class IndexActivity extends Activity implements RM20Callbacks {
	protected RM20JNI rm20Lib;

	public void onCreate(final Bundle bundle) {
		super.onCreate(bundle);
		this.rm20Lib = new RM20JNI(Environment.getExternalStorageDirectory(), this, this.getApplicationContext());
		this.rm20Lib.startupLibrary(34, 1);
		this.rm20Lib.setRespRateCallbacks(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				final Random random = new Random();
				int i = 0;
				while (i < 10000) {
					IndexActivity.this.rm20Lib.writeSampleData(random.nextInt(1000), random.nextInt(1000));
					while (true) {
						try {
							Thread.sleep(5L);
							++i;
						}
						catch (InterruptedException ex) {
							ex.printStackTrace();
							continue;
						}
						break;
					}
				}
				IndexActivity.this.rm20Lib.getEpochCount();
				IndexActivity.this.rm20Lib.stopAndCalculate();
				Log.d(this.getClass().getName(), " results : " + IndexActivity.this.rm20Lib.resultsForSession());
			}
		}).start();
	}

	public void onRm20Alarm(final int n) {
		Log.d("RM20NDK", " onRm20Alarm");
	}

	public void onRm20RealTimeSleepState(final int n, final int n2) {
		Log.d("RM20NDK", " onRm20RealTimeSleepState");
	}

	public void onRm20ValidBreathingRate(final float n, final int n2) {
		Log.d("RM20NDK", " onRm20ValidBreathingRate");
	}
}

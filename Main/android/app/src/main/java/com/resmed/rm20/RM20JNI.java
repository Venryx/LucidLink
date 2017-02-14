/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.AssetManager
 *  android.content.res.Resources
 *  android.os.Build
 *  com.resmed.rm20.RM20Callbacks
 *  com.resmed.rm20.RM20JNI
 *  com.resmed.rm20.SleepParams
 *  com.resmed.rm20.SmartAlarmInfo
 *  com.resmed.rm20.UserInfo
 */
package com.resmed.rm20;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import com.resmed.rm20.RM20Callbacks;
import com.resmed.rm20.SleepParams;
import com.resmed.rm20.SmartAlarmInfo;
import com.resmed.rm20.UserInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import v.lucidlink.V;

public class RM20JNI {
	public static final String rm20JniVer = "1.0.2";
	private RM20Callbacks callbacks;
	private File filesFolder;

	public RM20JNI(File file, RM20Callbacks rM20Callbacks, Context context) {
		this.filesFolder = file;
		this.callbacks = rM20Callbacks;
	}

	public static void loadLibrary(Context context) {
		/*String string = null;
		try {
			string = new BufferedReader(new InputStreamReader(context.openFileInput("libVerRm20.txt"))).readLine();
		}
		catch (IOException var3_5) {
			var3_5.printStackTrace();
		}
		Boolean libExistsAndUpToDate = false;
		if (string != null && string.trim().compareTo("1.0.2") == 0) {
			libExistsAndUpToDate = true;
		}

		if (!libExistsAndUpToDate) {
			AssetManager assetManager = context.getResources().getAssets();

			byte[] libBytes = new byte[1024];
			try {
				InputStream inputStream = assetManager.open(String.valueOf(Build.CPU_ABI) + "/librm20-jni.so", 3);
				FileOutputStream fileOutputStream = context.openFileOutput("librm20-jni.so", 0);
				do {
					int n;
					if ((n = inputStream.read(libBytes)) <= 0) {
						inputStream.close();
						fileOutputStream.close();
						break;
					}
					fileOutputStream.write(libBytes, 0, n);
				} while (true);
			}
			catch (IOException var6_10) {
				var6_10.printStackTrace();
			}

			try {
				InputStream inputStream = assetManager.open("libVerRm20.txt", 3);
				FileOutputStream fileOutputStream = context.openFileOutput("libVerRm20.txt", 0);
				do {
					int n;
					if ((n = inputStream.read(libBytes)) <= 0) {
						inputStream.close();
						fileOutputStream.close();
						break;
					}
					fileOutputStream.write(libBytes, 0, n);
				} while (true);
			}
			catch (IOException var7_14) {
				var7_14.printStackTrace();
			}
		}*/

		//System.load(context.getFilesDir() + "/librm20-jni.so");
		System.load(context.getApplicationInfo().nativeLibraryDir + "/librm20-jni.so");
	}

	public native int disableSmartAlarm();

	public native int getEpochCount();

	public native String getLibVersion();

	public native int getRealTimeSleepState();

	public native SmartAlarmInfo getSmartAlarm();

	public native UserInfo getUserInfo();

	public void onLibraryStarted(int n) {
		V.Log("Library started: " + n);
	}

	public void onRealTimeSleepState(int n, int n2) {
		V.Log("onRealTimeSleepState");
		if (this.callbacks == null) return;
		this.callbacks.onRm20RealTimeSleepState(n, n2);
	}

	public void onRm20Alarm(int n) {
		V.Log("onRm20Alarm");
		/*if (this.callbacks == null) return;
		this.callbacks.onRm20Alarm(n);*/
	}

	public void onValidBreathingRate(float f, int n) {
		V.Log("On breath in jni:" + f + ";" + n);
		V.Toast("Got \"valid\" breath data!" + f + ";" + n);
		if (this.callbacks == null) return;
		this.callbacks.onRm20ValidBreathingRate(f, n);
	}

	public void onWroteSample(int n) {
		V.Log("Test3: " + n);
	}

	public native SleepParams resultsForSession();

	public native int setRespRateCallbacks(boolean var1);

	public native int setSmartAlarm(int var1, int var2, boolean var3);

	// starts streaming the raw data over (I think you have to connect through bluetooth first)
	public native int startupLibrary(int var1, int var2);

	public native int stopAndCalculate();

	public native int writeSampleData(int var1, int var2);

	public int writeSamples(int[] arrn, int[] arrn2) {
		int n = -1;
		int n2 = 0;
		while (n2 < arrn2.length) {
			n = this.writeSampleData(arrn[n2], arrn2[n2]);
			if (n != 0) return n;
			++n2;
		}
		return n;
	}
}

package com.resmed.refresh.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;

import java.io.File;

@SuppressLint({"SimpleDateFormat"})
public class RefreshTools {
	protected static final char[] hexArray;

	static {
		hexArray = "0123456789ABCDEF".toCharArray();
	}

	public static boolean createDirectories(final String s) {
		try {
			final String[] split = s.split("/");
			String string = "";
			for (int i = 0; i < -1 + split.length; ++i) {
				if (split[i].length() > 0) {
					string = String.valueOf(string) + "/" + split[i];
				}
				new File(string).mkdirs();
			}
			return true;
		}
		catch (Exception ex) {
			Log.d("com.resmed.refresh.model", "Error creating directories", ex);
			return false;
		}
	}

	public static float getBatteryLevel(final Context context) {
		final Intent registerReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
		final int intExtra = registerReceiver.getIntExtra("level", -1);
		final int intExtra2 = registerReceiver.getIntExtra("scale", -1);
		if (intExtra == -1 || intExtra2 == -1) {
			return 50.0f;
		}
		return 100.0f * (intExtra / intExtra2);
	}

	public static File getFilesPath() {
		/*if (Consts.USE_EXTERNAL_STORAGE) {
			return Environment.getExternalStorageDirectory();
		}
		return RefreshApplication.getInstance().getFilesDir();*/
		return Environment.getExternalStorageDirectory();
	}
}
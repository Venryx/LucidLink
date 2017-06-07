package com.resmed.refresh.utils;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;

public class DisplayManager
{
	public static Integer getScreenHeight(final Context context) {
		final Display defaultDisplay = ((WindowManager)context.getSystemService("window")).getDefaultDisplay();
		final Point point = new Point();
		if (Build.VERSION.SDK_INT >= 13) {
			defaultDisplay.getSize(point);
			return point.y;
		}
		return defaultDisplay.getHeight();
	}

	public static Integer getScreenWidth(final Context context) {
		final Display defaultDisplay = ((WindowManager)context.getSystemService("window")).getDefaultDisplay();
		final Point point = new Point();
		if (Build.VERSION.SDK_INT >= 13) {
			defaultDisplay.getSize(point);
			return point.x;
		}
		return defaultDisplay.getWidth();
	}
}

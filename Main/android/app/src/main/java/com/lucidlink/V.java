package com.lucidlink;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.choosemuse.libmuse.LibmuseVersion;
import com.facebook.imagepipeline.producers.Consumer;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class V {
	public static void Log(String message) {
		Log("default", message);
	}
	public static void Log(String tag, String message) {
		Log.i(tag, message);
		Main.main.SendEvent("PostJavaLog", tag, message);
	}

	public static void Toast(String message) {
		Main.main.ShowToast(message, 3);
	}
	public static void Toast(String message, int duration) {
		Main.main.ShowToast(message, duration);
	}

	public static void WaitXThenRun(int waitMS, Runnable runnable) {
		Handler handler = new Handler();
		handler.postDelayed(runnable, waitMS);
	}

	public static ViewGroup GetRootView() {
		return (ViewGroup)MainActivity.main.getWindow().getDecorView().getRootView();
	}
	public static LinearLayout GetRootLinearLayout() {
		return (LinearLayout)((ViewGroup)MainActivity.main.getWindow().getDecorView().getRootView()).getChildAt(0);
	}

	public static FrameLayout.LayoutParams CreateFrameLayoutParams(int marginLeft, int marginTop, int width, int height) {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
		params.leftMargin = marginLeft;
		params.topMargin = marginTop;
		return params;
	}
	public static RelativeLayout.LayoutParams CreateRelativeLayoutParams(int marginLeft, int marginTop, int width, int height) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		params.leftMargin = marginLeft;
		params.topMargin = marginTop;
		return params;
	}

	/*public static ArrayList<View> FindDescendants(View v) {
		ArrayList<View> visited = new ArrayList<View>();
		ArrayList<View> unvisited = new ArrayList<View>();
		unvisited.add(v);

		while (!unvisited.isEmpty()) {
			View child = unvisited.remove(0);
			visited.add(child);
			if (!(child instanceof ViewGroup)) continue;
			ViewGroup group = (ViewGroup) child;
			final int childCount = group.getChildCount();
			for (int i=0; i<childCount; i++)
				unvisited.add(group.getChildAt(i));
		}

		return visited;
	}*/

	public static View FindViewByContentDescription(View root, String contentDescription) {
		List<View> visited = new ArrayList<View>();
		List<View> unvisited = new ArrayList<View>();
		unvisited.add(root);
		
		while (!unvisited.isEmpty()) {
			View child = unvisited.remove(0);
			visited.add(child);
			
			if (child.getContentDescription() != null && child.getContentDescription().toString().equals(contentDescription))
				return child;

			if (!(child instanceof ViewGroup)) continue;
			ViewGroup group = (ViewGroup) child;
			final int childCount = group.getChildCount();
			for (int i=0; i<childCount; i++)
				unvisited.add(group.getChildAt(i));
		}

		//return visited;
		return null;
	}
}

class VFile {
	public static String ReadAllText(File file) {
		try {
			FileInputStream stream = new FileInputStream(file);
			StringBuilder result = new StringBuilder(512);
			Reader r = new InputStreamReader(stream, "UTF-8");
			int c;
			while ((c = r.read()) != -1) {
				result.append((char) c);
			}
			return result.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
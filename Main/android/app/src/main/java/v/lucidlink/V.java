package v.lucidlink;

import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.style.ReplacementSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.annimon.stream.Stream;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class V {
	// ui
	// ==========

	public static int MATCH_PARENT = -1;
	public static int WRAP_CONTENT = -2;

	public static List<View> GetChildren(ViewGroup parent) {
		List<View> result = new ArrayList<>();
		for (int i = 0; i < parent.getChildCount(); i++)
			result.add(parent.getChildAt(i));
		return result;
	}

	/*static final String INDEX_CHAR = " ";
	static final int TAB_NUMBER = 4;
	public static void ConvertTextInputTabsToSpans(EditText input) {
		ConvertTextInputTabsToSpans(input, 0, input.getEditableText().length());
	}
	public static void ConvertTextInputTabsToSpans(EditText input, int start, int end) {
		String str = input.getEditableText().toString();
		float tabWidth = input.getPaint().measureText(INDEX_CHAR) * TAB_NUMBER;
		while (start < end)     {
			int index = str.indexOf("\t", start);
			if (index < 0) break;
			input.getEditableText().setSpan(new CustomTabWidthSpan(Float.valueOf(tabWidth).intValue()), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			start = index + 1;
		}
	}*/
	static class CustomTabWidthSpan extends ReplacementSpan {
		CustomTabWidthSpan(int tabWidth){
			this.tabWidth = tabWidth;
		}
		int tabWidth = 0;
		@Override public int getSize(Paint p1, CharSequence p2, int p3, int p4, Paint.FontMetricsInt p5) {
			return tabWidth;
		}
		@Override public void draw(Canvas p1, CharSequence p2, int p3, int p4, float p5, int p6, int p7, int p8, Paint p9) {}
	}

	// general
	// ==========

	public static int textColor = Color.rgb(255, 255, 255);

	public static void Log(String message) {
		Log("default", message);
	}
	public static void Log(String tag, String message) { Log(tag, message, true); }
	public static void Log(String tag, String message, boolean sendToJS) {
		//Log.i(tag, message);
		JavaLog(tag, message);
		//if (sendToJS)
		if (sendToJS) {
			if (LL.main != null && LL.main.reactContext.hasActiveCatalystInstance())
				LL.main.SendEvent("PostJavaLog", tag, message);
			else
				JavaLog(tag, "Could not log to JS, because react was not yet fully initialized.");
		}
	}

	// logcat has a message length limit, so cut long messages into pieces that are displayable
	public static void JavaLog(String message) { JavaLog("default", message); }
	public static void JavaLog(String tag, String message) {
		//Log.i(tag, "Length: " + message.length());
		if (message.length() > 4000) {
			Log.i(tag, message.substring(0, 4000));
			JavaLog(tag, message.substring(4000));
		} else {
			Log.i(tag, message);
		}
	}

	public static void Assert(boolean condition) {
		Assert(condition, "");
	}
	public static void Assert(boolean condition, String message) {
		if (condition) return;
		throw new Error("Assert failed) " + message);
	}

	public static <T> boolean HasIndex(List<T> list, int index) {
		return index >= 0 && index < list.size();
	}

	public static double EnsureNormalDouble(double value, double fallback) {
		if (Double.isNaN(value) || Double.isInfinite(value))
			return fallback;
		return value;
	}

	public static <T extends Object> List<T> List(Stream<T> stream) {
		List<T> result = new ArrayList<>();
		for (Object item : stream.toArray())
			result.add((T)item);
		return result;
	}
	public static List<ReadableMap> List_ReadableMaps(ReadableArray array) {
		List<ReadableMap> result = new ArrayList<>();
		for (int i = 0; i < array.size(); i++)
			result.add(array.getMap(i));
		return result;
	}

	public static Object[] ToObjectArray(Object val){
		int length = Array.getLength(val);
		Object[] outputArray = new Object[length];
		for(int i = 0; i < length; ++i)
			outputArray[i] = Array.get(val, i);
		return outputArray;
	}

	static final double fakeNaN = -1000000000;
	public static WritableArray ToWritableArray(double[] array) {
		WritableArray result = Arguments.createArray();
		for (double val : array) {
			if (Double.isNaN(val))
				result.pushDouble(fakeNaN);
			else
				result.pushDouble(val);
		}
		return result;
	}
	public static <T> WritableArray ToWritableArray(T[] array) {
		WritableArray result = Arguments.createArray();
		for (T item : array)
			WritableArray_Add(result, item);
		return result;
	}
	public static <T extends Object> WritableArray ToWritableArray(List<T> array) {
		WritableArray result = Arguments.createArray();
		for (T item : array)
			WritableArray_Add(result, item);
		return result;
	}
	public static <K extends String, V extends Object> WritableMap ToWritableMap(HashMap<K, V> map) {
		WritableMap result = Arguments.createMap();
		for (Map.Entry<K, V> entry : map.entrySet())
			WritableMap_Add(result, entry.getKey(), entry.getValue());
		return result;
	}

	public static void WritableArray_Add(WritableArray array, Object obj) {
		if (obj == null)
			array.pushNull();
		else if (obj instanceof Boolean)
			array.pushBoolean((Boolean)obj);
		else if (obj instanceof Integer)
			array.pushInt((Integer)obj);
		else if (obj instanceof Double)
			array.pushDouble((Double)obj);
		else if (obj instanceof String)
			array.pushString((String)obj);
		else if (obj instanceof WritableArray)
			array.pushArray((WritableArray)obj);
		else {
			//Assert(arg instanceof WritableMap, "Event args must be one of: WritableArray, Boolean")
			if (!(obj instanceof WritableMap))
				throw new RuntimeException("Event args must be one of: Boolean, Integer, Double, String, WritableArray, WritableMap (not " + obj.getClass().getSimpleName() + ")");
			array.pushMap((WritableMap)obj);
		}
	}
	public static void WritableMap_Add(WritableMap map, String key, Object obj) {
		if (obj == null)
			map.putNull(key);
		else if (obj instanceof Boolean)
			map.putBoolean(key, (Boolean)obj);
		else if (obj instanceof Integer)
			map.putInt(key, (Integer)obj);
		else if (obj instanceof Double)
			map.putDouble(key, (Double)obj);
		else if (obj instanceof String)
			map.putString(key, (String)obj);
		else if (obj instanceof WritableArray)
			map.putArray(key, (WritableArray)obj);
		else {
			//Assert(arg instanceof WritableMap, "Event args must be one of: WritableArray, Boolean")
			if (!(obj instanceof WritableMap))
				throw new RuntimeException("Event args must be one of: Boolean, Integer, Double, String, WritableArray, WritableMap (not " + obj.getClass().getSimpleName() + ")");
			map.putMap(key, (WritableMap)obj);
		}
	}

	public static String GetStackTrace(Throwable ex) {
		StringWriter writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer));
		//return writer.toString().replace("\\n", "\n");
		return writer.toString();
	}

	public static void Toast(String message) {
		LL.main.ShowToast(message, 0);
	}
	public static void Toast(String message, int duration) {
		LL.main.ShowToast(message, duration);
	}

	public static void Notify(String message) {
		LL.main.Notify(message, "Long");
	}
	public static void Notify(String message, String lengthStr) {
		LL.main.Notify(message, lengthStr);
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

	public static ViewGroup.LayoutParams CreateViewGroupLayoutParams(int width, int height) {
		return new ViewGroup.LayoutParams(width, height);
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
	public static LinearLayout.LayoutParams CreateLinearLayoutParams(int marginLeft, int marginTop, int width, int height, int weight) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
		params.leftMargin = marginLeft;
		params.topMargin = marginTop;
		params.weight = weight;
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

	/*public static View FindViewByContentDescription(View root, String contentDescription) {
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
	}*/
	public static View FindViewByContentDescription(View root, String contentDescription) {
		return FindView(root, view->view.getContentDescription() != null && view.getContentDescription().toString().equals(contentDescription));
	}

	public interface Action {
		void Run();
	}
	public interface Func<A1, R> {
		R Run(A1 arg1);
	}
	public static View FindView(View root, Func<View, Boolean> matchFunc) {
		List<View> views = FindViews(root, matchFunc, 1);
		if (views.size() == 0) return null;
		return views.get(0);
	}
	public static List<View> FindViews(View root, Func<View, Boolean> matchFunc) {
		return FindViews(root, matchFunc, -1);
	}
	public static List<View> FindViews(View root, Func<View, Boolean> matchFunc, int maxCount) {
		List<View> result = new ArrayList<>();

		List<View> visited = new ArrayList<>();
		List<View> unvisited = new ArrayList<>();
		unvisited.add(root);

		while (!unvisited.isEmpty()) {
			View child = unvisited.remove(0);
			visited.add(child);

			if (matchFunc.Run(child)) {
				result.add(child);
				if (maxCount != -1 && result.size() >= maxCount) break;
			}

			if (!(child instanceof ViewGroup)) continue;
			ViewGroup group = (ViewGroup) child;
			final int childCount = group.getChildCount();
			for (int i=0; i<childCount; i++)
				unvisited.add(group.getChildAt(i));
		}

		return result;
	}

	public static int Distance(int a, int b) {
		return Math.abs(a - b);
	}
	public static double Distance(double a, double b) {
		return Math.abs(a - b);
	}

	public static double Average(double... vals) {
		double total = 0;
		for (double arg : vals)
			total += arg;
		return total / vals.length;
	}
	public static double Median(double... vals) {
		if (vals.length % 2 == 0)
			return (vals[vals.length / 2] + vals[vals.length / 2 - 1]) / 2;
		return vals[vals.length / 2];
	}

	// just use the word 'percent', even though value is represented as fraction (e.g. 0.5, rather than 50[%])
	public static double Lerp(double from, double to, double percentFromXToY) { return from + ((to - from) * percentFromXToY); }
	public static double GetPercentFromXToY(double start, double end, double val) { return GetPercentFromXToY(start, end, val, true); }
	public static double GetPercentFromXToY(double start, double end, double val, boolean clampResultTo0Through1) {
		// distance-from-x / distance-from-x-required-for-result-'1'
		double result = (val - start) / (end - start);
		if (clampResultTo0Through1)
			//result = result.KeepBetween(0, 1);
			result = KeepXBetween(result, 0, 1);
		return result;
	}

	/*public static List<Double> GetXToY(double minX, double maxOutY) {
		return GetXToY(minX, maxOutY, 1);
	}
	public static List<Double> GetXToY(double minX, double maxOutY, double interval) {
		List<Double> result = new ArrayList<>();
		for (double val = minX; val <= maxOutY; val += interval) {
			result.add(val);
		}
		return result;
	}
	public static List<Double> GetXToYOut(double minX, double maxOutY) {
		return GetXToYOut(minX, maxOutY, 1);
	}
	public static List<Double> GetXToYOut(double minX, double maxOutY, double interval) {
		List<Double> result = new ArrayList<>();
		for (double val = minX; val < maxOutY; val += interval) {
			result.add(val);
		}
		return result;
	}*/

	public static double KeepXBetween(double val, double min, double max) {
		if (val < min) return min;
		if (val > max) return max;
		return val;
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

	public static String URIToPath(Uri uri) {
		String[] filePathColumn = {MediaStore.Images.Media.DATA};
		Cursor cursor = MainActivity.main.getContentResolver().query(uri, filePathColumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String filePath = cursor.getString(columnIndex);
		cursor.close();
		return filePath;
	}
}
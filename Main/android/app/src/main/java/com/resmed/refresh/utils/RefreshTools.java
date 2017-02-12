package com.resmed.refresh.utils;

import android.annotation.*;
import android.graphics.*;
import com.resmed.refresh.model.*;
import com.resmed.refresh.model.mappers.*;
import com.resmed.refresh.model.graphs.*;
import com.resmed.refresh.ui.uibase.app.*;
import android.os.*;
import java.nio.channels.*;
import android.content.*;
import java.text.*;
import com.resmed.refresh.ui.utils.*;
import java.util.*;
import android.content.res.*;
import android.view.inputmethod.*;
import android.app.*;
import android.widget.*;
import android.view.*;
import android.util.*;
import java.io.*;

@SuppressLint({ "SimpleDateFormat" })
public class RefreshTools
{
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String DATE_FORMAT_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	protected static final char[] hexArray;

	static {
		hexArray = "0123456789ABCDEF".toCharArray();
	}

	public static String BitMapToString(final Bitmap bitmap) {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, (OutputStream)byteArrayOutputStream);
		return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
	}

	public static Bitmap StringToBitMap(final String s) {
		try {
			final byte[] decode = Base64.decode(s, 0);
			return BitmapFactory.decodeByteArray(decode, 0, decode.length);
		}
		catch (Exception ex) {
			ex.getMessage();
			return null;
		}
	}

	public static BitSet bitmaskFromInt(final int n) {
		final BitSet set = new BitSet();
		for (int i = 0; i < 32; ++i) {
			if ((0x1 & n >> i) == 0x1) {
				set.set(i);
			}
		}
		return set;
	}

	public static int bitmaskToInt(final BitSet set) {
		int n = 0;
		int nextSetBit = -1;
		while (true) {
			nextSetBit = set.nextSetBit(nextSetBit + 1);
			if (nextSetBit == -1) {
				break;
			}
			n |= 1 << nextSetBit;
		}
		return n;
	}

	public static String bytesToHex(final List<Byte> list) {
		final char[] array = new char[2 * list.size()];
		for (int i = 0; i < list.size(); ++i) {
			final int n = 0xFF & list.get(i);
			array[i * 2] = RefreshTools.hexArray[n >>> 4];
			array[1 + i * 2] = RefreshTools.hexArray[n & 0xF];
		}
		return new String(array);
	}

	public static String bytesToHex(final byte[] array) {
		final char[] array2 = new char[2 * array.length];
		for (int i = 0; i < array.length; ++i) {
			final int n = 0xFF & array[i];
			array2[i * 2] = RefreshTools.hexArray[n >>> 4];
			array2[1 + i * 2] = RefreshTools.hexArray[n & 0xF];
		}
		return new String(array2);
	}

	public static int calculateAge(final Date time) {
		final Calendar instance = Calendar.getInstance();
		instance.setTime(time);
		final Calendar instance2 = Calendar.getInstance();
		int n = instance2.get(1) - instance.get(1);
		if (instance2.get(2) < instance.get(2)) {
			--n;
		}
		else if (instance2.get(2) == instance.get(2) && instance2.get(5) < instance.get(5)) {
			return n - 1;
		}
		return n;
	}

	public static boolean checkForFirmwareUpgrade(final Context context, final String s) {
		Log.d("com.resmed.refresh.ui", "checkForFirmwareUpgrade firmware version :" + s);
		return compareFirmwareVersions(s.replace("Release", "").split(" ")[0], getFirmwareBinaryVersion(context)) < 0;
	}

	public static int compareFirmwareVersions(final String p0, final String p1) {
		//
		// This method could not be decompiled.
		//
		// Original Bytecode:
		//
		//     0: ldc             "com.resmed.refresh.ui"
		//     2: new             Ljava/lang/StringBuilder;
		//     5: dup
		//     6: ldc             "RefreshTools::compareVersions first : "
		//     8: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//    11: aload_0
		//    12: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//    15: ldc             " second : "
		//    17: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//    20: aload_1
		//    21: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//    24: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//    27: invokestatic    com/resmed/refresh/utils/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
		//    30: pop
		//    31: aload_0
		//    32: ifnull          39
		//    35: aload_1
		//    36: ifnonnull       43
		//    39: iconst_0
		//    40: istore_3
		//    41: iload_3
		//    42: ireturn
		//    43: aload_0
		//    44: ldc             "\\."
		//    46: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
		//    49: astore          4
		//    51: aload_1
		//    52: ldc             "\\."
		//    54: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
		//    57: astore          5
		//    59: ldc             "com.resmed.refresh.ui"
		//    61: new             Ljava/lang/StringBuilder;
		//    64: dup
		//    65: ldc             "RefreshTools::compareVersions versionNumsFirst : "
		//    67: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//    70: aload           4
		//    72: invokestatic    java.util.Arrays.toString:([Ljava/lang/Object;)Ljava/lang/String;
		//    75: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//    78: ldc             " second : "
		//    80: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//    83: aload           5
		//    85: invokestatic    java.util.Arrays.toString:([Ljava/lang/Object;)Ljava/lang/String;
		//    88: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//    91: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//    94: invokestatic    com/resmed/refresh/utils/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
		//    97: pop
		//    98: aload           4
		//   100: arraylength
		//   101: ifeq            110
		//   104: aload           5
		//   106: arraylength
		//   107: ifne            112
		//   110: iconst_0
		//   111: ireturn
		//   112: aload           4
		//   114: iconst_0
		//   115: aaload
		//   116: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
		//   119: istore          8
		//   121: aload           5
		//   123: iconst_0
		//   124: aaload
		//   125: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
		//   128: istore          9
		//   130: ldc             "com.resmed.refresh.ui"
		//   132: new             Ljava/lang/StringBuilder;
		//   135: dup
		//   136: ldc             "RefreshTools::compareVersions firstVersionFirstInt : "
		//   138: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//   141: iload           8
		//   143: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
		//   146: ldc             " secondVersionFirstInt : "
		//   148: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//   151: iload           9
		//   153: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
		//   156: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//   159: invokestatic    com/resmed/refresh/utils/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
		//   162: pop
		//   163: iload           8
		//   165: iload           9
		//   167: invokestatic    java/lang/Integer.compare:(II)I
		//   170: istore_3
		//   171: iload_3
		//   172: ifne            41
		//   175: aload           4
		//   177: iconst_1
		//   178: aaload
		//   179: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
		//   182: istore          12
		//   184: aload           5
		//   186: iconst_1
		//   187: aaload
		//   188: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
		//   191: istore          13
		//   193: ldc             "com.resmed.refresh.ui"
		//   195: new             Ljava/lang/StringBuilder;
		//   198: dup
		//   199: ldc             "RefreshTools::compareVersions firstVersionSecondInt : "
		//   201: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//   204: iload           12
		//   206: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
		//   209: ldc             " secondVersionSecondInt : "
		//   211: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//   214: iload           13
		//   216: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
		//   219: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//   222: invokestatic    com/resmed/refresh/utils/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
		//   225: pop
		//   226: iload           12
		//   228: iload           13
		//   230: invokestatic    java/lang/Integer.compare:(II)I
		//   233: istore_3
		//   234: iload_3
		//   235: ifne            41
		//   238: aload           4
		//   240: iconst_2
		//   241: aaload
		//   242: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
		//   245: istore          16
		//   247: aload           5
		//   249: iconst_2
		//   250: aaload
		//   251: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
		//   254: istore          17
		//   256: ldc             "com.resmed.refresh.ui"
		//   258: new             Ljava/lang/StringBuilder;
		//   261: dup
		//   262: ldc             "RefreshTools::compareVersions firstVersionThirdInt : "
		//   264: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
		//   267: iload           16
		//   269: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
		//   272: ldc             " secondVersionThirdInt : "
		//   274: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
		//   277: iload           17
		//   279: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
		//   282: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
		//   285: invokestatic    com/resmed/refresh/utils/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
		//   288: pop
		//   289: iload           16
		//   291: iload           17
		//   293: invokestatic    java/lang/Integer.compare:(II)I
		//   296: istore_3
		//   297: iload_3
		//   298: ifne            41
		//   301: iconst_0
		//   302: ireturn
		//   303: astore          7
		//   305: iconst_0
		//   306: ireturn
		//   307: astore          11
		//   309: iconst_0
		//   310: ireturn
		//   311: astore          15
		//   313: iconst_0
		//   314: ireturn
		//    Exceptions:
		//  Try           Handler
		//  Start  End    Start  End    Type
		//  -----  -----  -----  -----  ---------------------
		//  112    130    303    307    Ljava/lang/Exception;
		//  175    193    307    311    Ljava/lang/Exception;
		//  238    256    311    315    Ljava/lang/Exception;
		//
		// The error that occurred was:
		//
		// java.lang.IndexOutOfBoundsException: Index: 155, Size: 155
		//     at java.util.ArrayList.rangeCheck(Unknown Source)
		//     at java.util.ArrayList.get(Unknown Source)
		//     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3303)
		//     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
		//     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
		//     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
		//     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
		//     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
		//     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
		//     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
		//     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
		//     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
		//     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
		//     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
		//     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
		//     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.decompileClassNode(ProcyonDecompiler.java:120)
		//     at the.bytecode.club.bytecodeviewer.gui.ClassViewer$13.doShit(ClassViewer.java:624)
		//     at the.bytecode.club.bytecodeviewer.gui.PaneUpdaterThread.run(PaneUpdaterThread.java:16)
		//
		throw new IllegalStateException("An error occurred while decompiling this method.");
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
			Log.d("com.resmed.refresh.model", "Error creating directories", (Throwable)ex);
			return false;
		}
	}

	public static List<Date> dateRangesForDay(final Date date) {
		final Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		final Calendar instance2 = Calendar.getInstance();
		instance2.setTime(date);
		instance.set(11, 0);
		instance.set(12, 0);
		instance.set(13, 0);
		instance2.set(11, 23);
		instance2.set(12, 59);
		instance2.set(13, 59);
		final ArrayList<Date> list = new ArrayList<Date>();
		list.add(instance.getTime());
		list.add(instance2.getTime());
		return list;
	}

	public static List<Date> dateRangesForExactTime(final Date date) {
		final long time = date.getTime();
		final Date date2 = new Date(time - 1000L);
		final Date date3 = new Date(time + 1000L);
		final ArrayList<Date> list = new ArrayList<Date>();
		list.add(date2);
		list.add(date3);
		return list;
	}

	public static void debugSleepSessions() {
		final RefreshModelController instance = RefreshModelController.getInstance();
		if (instance.getUser() != null) {
			for (final RST_SleepSessionInfo rst_SleepSessionInfo : instance.localSleepSessionsAll()) {
				final List hypnoData = HypnoMapper.getHypnoData(rst_SleepSessionInfo);
				final int round = Math.round((rst_SleepSessionInfo.getStopTime().getTime() - rst_SleepSessionInfo.getStartTime().getTime()) / 60000L);
				if (rst_SleepSessionInfo.getTotalSleepTime() > 60 && round > 0) {
					final String string = String.valueOf(getHourStringForTime(rst_SleepSessionInfo.getTotalSleepTime())) + getMinsStringForTime(rst_SleepSessionInfo.getTotalSleepTime());
					final String string2 = String.valueOf(getHourStringForTime(round)) + getMinsStringForTime(round);
					while (true) {
						try {
							final float n = (hypnoData.get(1).getHour().getTime() - hypnoData.get(0).getHour().getTime()) / 1000.0f;
							Log.d("sessions", String.valueOf(rst_SleepSessionInfo.getId()) + "\tTotalTime = " + string + "\tsleepTime = " + string2 + "\tTimeBetweenSamples = " + n + "\t" + rst_SleepSessionInfo.getStartTime() + "\t" + rst_SleepSessionInfo.getStopTime() + "\t" + rst_SleepSessionInfo.getCompleted());
						}
						catch (Exception ex) {
							ex.printStackTrace();
							final float n = 0.0f;
							continue;
						}
						break;
					}
				}
			}
		}
	}

	public static boolean deleteTimeStampFile(final Context context) {
		synchronized (RefreshTools.class) {
			return new File(getFilesPath(), context.getString(2131165344)).delete();
		}
	}

	public static boolean exportAllFilesToSD() {
		Block_0: {
			//break Block_0;
			Label_0220:
			while (true) {
				int i;
				int length;
				do {
					Label_0050: {
						//break Label_0050;
						try {
							final File filesDir = RefreshApplication.getInstance().getFilesDir();
							final String string = String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/Refresh/export/";
							final File[] listFiles = filesDir.listFiles();
							length = listFiles.length;
							i = 0;
							//continue Label_0220;
							final File file = listFiles[i];
							Log.d("com.resmed.refresh.model", " exportAllFilesToSD:: file.getName() : " + file.getName());
							final File file2 = new File(String.valueOf(string) + file.getName().replace(RefreshApplication.getInstance().getFilesDir().getAbsolutePath(), ""));
							createDirectories(file2.getAbsolutePath());
							final FileChannel channel = new FileInputStream(file).getChannel();
							final FileChannel channel2 = new FileOutputStream(file2.getAbsolutePath()).getChannel();
							channel2.transferFrom(channel, 0L, channel.size());
							channel.close();
							channel2.close();
							++i;
							continue Label_0220;
						}
						catch (Exception ex) {
							ex.printStackTrace();
							Log.d("com.resmed.refresh.model", "Error in exportAllFilesToSD", (Throwable)ex);
							return false;
						}
					}
					continue Label_0220;
				} while (i < length);
				break;
			}
		}
		return true;
	}

	public static boolean exportDataBaseToSD() {
		try {
			final String string = String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/Refresh/refresh-db.db";
			Log.d("com.resmed.refresh.model", "exportDataBaseToSD db_path=" + "data/data/com.resmed.refresh/databases/refresh-db");
			Log.d("com.resmed.refresh.model", "exportDataBaseToSD pathToCopy=" + string);
			final File file = new File("data/data/com.resmed.refresh/databases/refresh-db");
			final File file2 = new File(string);
			if (!file.exists()) {
				Log.w("Copy DB", "Data base doesn't exits!");
				return false;
			}
			createDirectories(string);
			final FileChannel channel = new FileInputStream(file).getChannel();
			final FileChannel channel2 = new FileOutputStream(file2).getChannel();
			channel2.transferFrom(channel, 0L, channel.size());
			channel.close();
			channel2.close();
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Log.d("com.resmed.refresh.model", "Error in copyDataBaseFromData", (Throwable)ex);
			return false;
		}
	}

	public static File findFileByName(final File file, final String s) {
		if (file.isDirectory()) {
			for (final File file2 : file.listFiles()) {
				Log.d("com.resmed.refresh.bluetooth", " SleepSessionManager a file : " + file2);
				if (file2.isFile()) {
					final String name = file2.getName();
					Log.d("com.resmed.refresh.bluetooth", " SleepSessionManager edf file : " + file2);
					if (name.endsWith(s)) {
						return file2;
					}
				}
			}
			return null;
		}
		return null;
	}

	public static float getBatteryLevel(final Context context) {
		final Intent registerReceiver = context.registerReceiver((BroadcastReceiver)null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
		final int intExtra = registerReceiver.getIntExtra("level", -1);
		final int intExtra2 = registerReceiver.getIntExtra("scale", -1);
		if (intExtra == -1 || intExtra2 == -1) {
			return 50.0f;
		}
		return 100.0f * (intExtra / intExtra2);
	}

	public static Date getDateFromString(final String s) {
		try {
			SimpleDateFormat simpleDateFormat;
			if (s.contains("Z")) {
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			}
			else {
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			}
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			final Calendar instance = Calendar.getInstance();
			instance.setTimeZone(TimeZone.getTimeZone("UTC"));
			instance.setTime(simpleDateFormat.parse(s));
			return instance.getTime();
		}
		catch (ParseException ex) {
			return new Date();
		}
	}

	public static File getFilesPath() {
		if (Consts.USE_EXTERNAL_STORAGE) {
			return Environment.getExternalStorageDirectory();
		}
		return RefreshApplication.getInstance().getFilesDir();
	}

	public static String getFirmwareBinaryVersion(final Context context) {
		while (true) {
			String s = "1.0.5";
			final AssetManager assets = context.getResources().getAssets();
			while (true) {
				int n;
				try {
					final String[] list = assets.list("");
					n = 0;
					if (n >= list.length) {
						return s;
					}
					Log.d("com.resmed.refresh.ui", "getFirmwareBinary; fileName = " + list[n]);
					if (list[n].contains("BeD_")) {
						final String[] split = list[n].split("_");
						Log.d("com.resmed.refresh.ui", "checkForFirmwareUpgrade tokens :" + Arrays.toString(split));
						if (split.length >= 3) {
							s = split[2];
						}
						else {
							s = list[n];
						}
						Log.d("com.resmed.refresh.ui", "checkForFirmwareUpgrade firmware version :" + s);
					}
				}
				catch (IOException ex) {
					ex.printStackTrace();
					return s;
				}
				++n;
				continue;
			}
		}
	}

	public static String getHourMinsStringForTime(final int n) {
		return String.valueOf(n / 60) + ":" + String.format("%02d", n % 60);
	}

	public static String getHourStringForTime(final int n) {
		final int n2 = n / 60;
		if (n2 == 0) {
			return "";
		}
		return String.format("%02dh", n2);
	}

	public static String getMinsSecsStringForTime(final int n) {
		final int round = Math.round(n / 1000);
		return String.valueOf(String.format("%02d", round / 60)) + ":" + String.format("%02d", round % 60);
	}

	public static String getMinsStringForTime(final int n) {
		return String.format("%02dm", n % 60);
	}

	public static String getStringFromDate(final Date time) {
		final Calendar instance = Calendar.getInstance();
		instance.setTimeZone(TimeZone.getTimeZone("UTC"));
		instance.setTime(time);
		final Date time2 = instance.getTime();
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return simpleDateFormat.format(time2);
	}

	public static void hideKeyBoard(final Activity activity) {
		try {
			((InputMethodManager)activity.getSystemService("input_method")).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 2);
		}
		catch (Exception ex) {}
	}

	public static boolean isAppRunning(final Context context, final String s) {
		final List runningAppProcesses = ((ActivityManager)context.getSystemService("activity")).getRunningAppProcesses();
		for (int i = 0; i < runningAppProcesses.size(); ++i) {
			if (runningAppProcesses.get(i).processName.equals(s)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isPluggedIn(final Context context) {
		boolean b = true;
		final int intExtra = context.registerReceiver((BroadcastReceiver)null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("plugged", -1);
		if (intExtra != (b ? 1 : 0) && intExtra != 2) {
			b = false;
		}
		return b;
	}

	public static Long readTimeStampToFile(final Context context) {
		synchronized (RefreshTools.class) {
			final File file = new File(getFilesPath(), context.getString(2131165344));
			Long value;
			try {
				final boolean exists = file.exists();
				value = null;
				if (exists) {
					final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
					final String line = bufferedReader.readLine();
					bufferedReader.close();
					value = null;
					if (line != null) {
						value = Long.parseLong(line.trim());
					}
				}
				return value;
			}
			catch (IOException ex) {
				ex.printStackTrace();
				value = null;
				return value;
			}
			return value;
		}
	}

	public static void setListViewHeightBasedOnChildren(final ListView listView) {
		final ListAdapter adapter = listView.getAdapter();
		if (adapter == null) {
			return;
		}
		int n = 0;
		for (int i = 0; i < adapter.getCount(); ++i) {
			final View view = adapter.getView(i, (View)null, (ViewGroup)listView);
			view.measure(0, 0);
			n += view.getMeasuredHeight();
		}
		final ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
		layoutParams.height = n + listView.getDividerHeight() * (-1 + adapter.getCount());
		listView.setLayoutParams(layoutParams);
	}

	public static boolean validateEmail(final String s) {
		return s != null && s.length() != 0 && Patterns.EMAIL_ADDRESS.matcher(s).matches();
	}

	public static void writeStringAsFile(final String s, final String s2) {
		try {
			final File file = new File(Environment.getExternalStorageDirectory() + "/refresh", s2);
			createDirectories(file.getAbsolutePath());
			Log.d("com.resmed.refresh.model", "writeStringAsFile path file:" + file.getAbsolutePath());
			final FileWriter fileWriter = new FileWriter(file);
			Log.d("com.resmed.refresh.model", "writeStringAsFile path file:" + Environment.getExternalStorageDirectory() + "/refresh/" + s2);
			fileWriter.write(s);
			fileWriter.close();
		}
		catch (IOException ex) {
			Log.d("com.resmed.refresh.model", "ERROR", (Throwable)ex);
		}
	}

	public static boolean writeTimeStampToFile(final Context context, final long n) {
		synchronized (RefreshTools.class) {
			final File file = new File(getFilesPath(), context.getString(2131165344));
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				final FileWriter fileWriter = new FileWriter(file, false);
				fileWriter.write(String.valueOf(Long.toString(n)) + " \n");
				fileWriter.flush();
				fileWriter.close();
				return true;
			}
			catch (IOException ex) {
				ex.printStackTrace();
				final boolean b = false;
			}
		}
	}
}

/*
 * Decompiled with CFR 0_115.
 *
 * Could not load the following classes:
 *  com.resmed.refresh.ui.utils.Consts
 *  com.resmed.refresh.utils.AppFileLog
 *  com.resmed.refresh.utils.Log
 *  com.resmed.refresh.utils.RefreshTools
 */
package com.resmed.refresh.utils;

import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * Exception performing whole class analysis ignored.
 */
public class AppFileLog {
	private static final int MAX_LOG_FILE_SIZE = 1048576;
	private static String filename = "bluetoothLog.txt";
	private static File filesDir;
	private static FileWriter fw;
	private static AppFileLog instance;

	static {
	}

	public AppFileLog() {
	}

	/*
	 * Enabled unnecessary exception pruning
	 */
	public static void addTrace(String string) {
		synchronized (AppFileLog.class) {
			boolean bl = Consts.WRITE_BLUETOOTH_LOG;
			if (!bl) return;
			String string2 = "";
			if (instance == null) {
				instance = new AppFileLog();
				try {
					filesDir = new File(RefreshTools.getFilesPath(), filename);
					if (!filesDir.exists()) {
						filesDir.createNewFile();
					}
					string2 = "\n\n";
				}
				catch (IOException var9_6) {
					var9_6.printStackTrace();
				}
			}
			try {
				Calendar calendar = Calendar.getInstance();
				String string3 = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(calendar.getTime());
				fw = new FileWriter(filesDir, true);
				fw.append(String.valueOf(string2) + string3 + "\t" + string + " \n");
				fw.flush();
				Log.d((String)"com.resmed.refresh.filelog", (String)(String.valueOf(string3) + "\t" + string));
			}
			catch (IOException var4_5) {
				var4_5.printStackTrace();
			}
			return;
		}
	}

	public static void deleteCurrentFile() {
		synchronized (AppFileLog.class) {
			boolean bl = Consts.WRITE_BLUETOOTH_LOG;
			if (!bl) {
				return;
			}
			if (instance == null) {
				instance = new AppFileLog();
			}
			if (!(AppFileLog.filesDir = new File(RefreshTools.getFilesPath(), filename)).exists()) return;
			if (filesDir.length() <= 0x100000) return;
			filesDir.delete();
			return;
		}
	}

	public static String getAbsolutePath() {
		if (filesDir != null) return filesDir.getAbsolutePath();
		AppFileLog.addTrace((String)"");
		return filesDir.getAbsolutePath();
	}

	public static String getFilename() {
		return filename;
	}
}

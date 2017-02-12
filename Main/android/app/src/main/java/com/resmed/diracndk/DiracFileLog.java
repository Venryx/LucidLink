package com.resmed.diracndk;

import java.io.*;
import android.os.*;

public class DiracFileLog
{
	private static final int MAX_LOG_FILE_SIZE = 5242880;
	private static String filename;
	private static File filesDir;
	private static FileWriter fw;
	private static DiracFileLog instance;

	static {
		DiracFileLog.filename = "diracLog.txt";
	}

	public static void addTrace(final String s) {
	}
	// monitorenter(DiracFileLog.class)
	// monitorexit(DiracFileLog.class)

	public static void deleteCurrentFile() {
		synchronized (DiracFileLog.class) {
			if (DiracFileLog.instance == null) {
				DiracFileLog.instance = new DiracFileLog();
			}
			DiracFileLog.filesDir = new File(Environment.getExternalStorageDirectory(), DiracFileLog.filename);
			if (DiracFileLog.filesDir.exists() && DiracFileLog.filesDir.length() > 5242880L) {
				DiracFileLog.filesDir.delete();
			}
		}
	}

	public static String getAbsolutePath() {
		if (DiracFileLog.filesDir == null) {
			addTrace("");
		}
		return DiracFileLog.filesDir.getAbsolutePath();
	}

	public static String getFilename() {
		return DiracFileLog.filename;
	}
}

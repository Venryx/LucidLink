/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.AssetManager
 *  android.content.res.Resources
 *  android.os.Build
 *  com.resmed.edflib.EdfLibCallbackHandler
 *  com.resmed.edflib.EdfLibJNI
 */
package com.resmed.edflib;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import com.resmed.edflib.EdfLibCallbackHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;

public class EdfLibJNI {
	public static final String rm60JniVer = "1.0.0";
	private EdfLibCallbackHandler callbacks;
	private File filesFolder;

	public EdfLibJNI(File file, EdfLibCallbackHandler edfLibCallbackHandler, Context context) {
		this.filesFolder = file;
		this.callbacks = edfLibCallbackHandler;
	}

	private void listFiles() {
		File file = new File(this.filesFolder.getAbsolutePath());
		if (!file.exists()) return;
		File[] arrfile = file.listFiles();
		int n = arrfile.length;
		int n2 = 0;
		while (n2 < n) {
			File file2 = arrfile[n2];
			System.out.println("external file : " + file2.getAbsolutePath());
			++n2;
		}
		return;
	}

	public static void loadLibrary(Context context) {
		String string;
		Boolean bl = false;
		AssetManager assetManager = context.getResources().getAssets();
		try {
			String string2;
			string = string2 = new BufferedReader(new InputStreamReader(context.openFileInput("libVerRm60.txt"))).readLine();
		}
		catch (IOException var3_5) {
			var3_5.printStackTrace();
			string = null;
		}
		if (string != null && string.trim().compareTo("1.0.0") == 0) {
			bl = true;
		}
		if (!bl.booleanValue()) {
			byte[] arrby;
			arrby = new byte[1024];
			try {
				InputStream inputStream = assetManager.open(String.valueOf(Build.CPU_ABI) + "/librstedf.so", 3);
				FileOutputStream fileOutputStream = context.openFileOutput("librstedf.so", 0);
				do {
					int n;
					if ((n = inputStream.read(arrby)) <= 0) {
						inputStream.close();
						fileOutputStream.close();
						break;
					}
					fileOutputStream.write(arrby, 0, n);
				} while (true);
			}
			catch (IOException var6_10) {
				var6_10.printStackTrace();
			}
			try {
				InputStream inputStream = assetManager.open("libVerRm60.txt", 3);
				FileOutputStream fileOutputStream = context.openFileOutput("libVerRm60.txt", 0);
				do {
					int n;
					if ((n = inputStream.read(arrby)) <= 0) {
						inputStream.close();
						fileOutputStream.close();
						break;
					}
					fileOutputStream.write(arrby, 0, n);
				} while (true);
			}
			catch (IOException var7_14) {
				var7_14.printStackTrace();
			}
		}
		System.load(context.getFilesDir() + "/librstedf.so");
	}

	public native int closeFileEdf(String var1, String[] var2);

	public native int compressFile(String var1, String var2);

	public native int fixEdfFile(String var1);

	public native String getLibVersion();

	public void onDigitalSamplesRead(int[] arrn, int[] arrn2) {
		if (this.callbacks == null) return;
		this.callbacks.onDigitalSamplesRead(arrn, arrn2);
	}

	public void onFileClosed() {
		if (this.callbacks == null) return;
		this.callbacks.onFileClosed();
	}

	public void onFileCompressed(int n) {
		if (this.callbacks == null) return;
		this.callbacks.onFileCompressed(n);
	}

	public void onFileFixed() {
		if (this.callbacks == null) return;
		this.callbacks.onFileFixed();
	}

	public void onFileOpened() {
		if (this.callbacks == null) return;
		this.callbacks.onFileOpened();
	}

	public void onWroteDigitalSamples() {
		if (this.callbacks == null) return;
		this.callbacks.onWroteDigitalSamples();
	}

	public void onWroteMetaData() {
		if (this.callbacks == null) return;
		this.callbacks.onWroteMetadata();
	}

	public native int openFileForMode(String var1, String[] var2, String var3);

	public native int readDigitalSamples(String var1);

	public void setFilesFolder(File file) {
		this.filesFolder = file;
	}

	public native int writeDigitalSamples(String var1, int[] var2, int[] var3, String[] var4);
}

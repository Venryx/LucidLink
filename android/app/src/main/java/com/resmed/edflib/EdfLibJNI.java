package com.resmed.edflib;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EdfLibJNI {
	public static final String rm60JniVer = "1.0.0";
	private EdfLibCallbackHandler callbacks;
	private File filesFolder;

	public native int closeFileEdf(String str, String[] strArr);

	public native int compressFile(String str, String str2);

	public native int fixEdfFile(String str);

	public native String getLibVersion();

	public native int openFileForMode(String str, String[] strArr, String str2);

	public native int readDigitalSamples(String str);

	public native int writeDigitalSamples(String str, int[] iArr, int[] iArr2, String[] strArr);

	public EdfLibJNI(File filesFolder, EdfLibCallbackHandler callbacks, Context context) {
		this.filesFolder = filesFolder;
		this.callbacks = callbacks;
	}

	public static void loadLibrary(Context context) {
		/*Boolean bLibAlreadyCopied = Boolean.valueOf(false);
		String fileLibVer = null;
		AssetManager assetMgr = context.getResources().getAssets();
		try {
			fileLibVer = new BufferedReader(new InputStreamReader(context.openFileInput("libVerRm60.txt"))).readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (fileLibVer != null && fileLibVer.trim().compareTo(rm60JniVer) == 0) {
			bLibAlreadyCopied = Boolean.valueOf(true);
		}
		if (!bLibAlreadyCopied.booleanValue()) {
			InputStream is;
			FileOutputStream os;
			int iBytesRead;
			byte[] buffer = new byte[1024];
			try {
				is = assetMgr.open(Build.CPU_ABI + "/librstedf.so", 3);
				os = context.openFileOutput("librstedf.so", 0);
				while (true) {
					iBytesRead = is.read(buffer);
					if (iBytesRead <= 0) {
						break;
					}
					os.write(buffer, 0, iBytesRead);
				}
				is.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				is = assetMgr.open("libVerRm60.txt", 3);
				os = context.openFileOutput("libVerRm60.txt", 0);
				while (true) {
					iBytesRead = is.read(buffer);
					if (iBytesRead <= 0) {
						break;
					}
					os.write(buffer, 0, iBytesRead);
				}
				is.close();
				os.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		System.load(context.getFilesDir() + "/librstedf.so");*/
		System.load(context.getApplicationInfo().nativeLibraryDir + "/librstedf.so");
	}

	public void onDigitalSamplesRead(int[] mi_buf, int[] mq_buf) {
		if (this.callbacks != null) {
			this.callbacks.onDigitalSamplesRead(mi_buf, mq_buf);
		}
	}

	public void onFileOpened() {
		if (this.callbacks != null) {
			this.callbacks.onFileOpened();
		}
	}

	public void onFileClosed() {
		if (this.callbacks != null) {
			this.callbacks.onFileClosed();
		}
	}

	public void onFileCompressed(int result) {
		if (this.callbacks != null) {
			this.callbacks.onFileCompressed(result);
		}
	}

	public void onWroteMetaData() {
		if (this.callbacks != null) {
			this.callbacks.onWroteMetadata();
		}
	}

	public void onWroteDigitalSamples() {
		if (this.callbacks != null) {
			this.callbacks.onWroteDigitalSamples();
		}
	}

	public void onFileFixed() {
		if (this.callbacks != null) {
			this.callbacks.onFileFixed();
		}
	}

	private void listFiles() {
		File dirs = new File(this.filesFolder.getAbsolutePath());
		if (dirs.exists()) {
			for (File file : dirs.listFiles()) {
				System.out.println("external file : " + file.getAbsolutePath());
			}
		}
	}

	public void setFilesFolder(File filesFolder) {
		this.filesFolder = filesFolder;
	}
}
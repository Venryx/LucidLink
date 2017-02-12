package com.resmed.refresh.expansion;

import android.content.*;
import java.io.*;
import com.google.android.vending.expansion.downloader.*;
import com.android.vending.expansion.zipfile.*;

public class ExpansionUtil
{
	public static FileDescriptor getFDFromExpansion(final Context context, final String s) {
		final String packageName = context.getPackageName();
		try {
			return new ZipResourceFile(Helpers.getExpansionAPKFileName(context, true, context.getPackageManager().getPackageInfo(packageName, 0).versionCode)).getAssetFileDescriptor(s).getFileDescriptor();
		}
		catch (Exception ex) {
			return null;
		}
	}
}

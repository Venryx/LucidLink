package com.resmed.refresh.utils;

import android.net.*;
import java.net.*;
import android.content.*;
import android.os.*;
import java.io.*;
import android.database.*;

public class InternalFileProvider extends ContentProvider
{
	public static final Uri CONTENT_URI;
	private static final String[] OPENABLE_PROJECTION;

	static {
		CONTENT_URI = Uri.parse("content://com.resmed.refresh/");
		OPENABLE_PROJECTION = new String[] { "_display_name", "_size" };
	}

	public int delete(final Uri uri, final String s, final String[] array) {
		return 0;
	}

	protected long getDataLength(final Uri uri) {
		return new File(this.getContext().getFilesDir(), uri.getPath()).length();
	}

	protected String getFileName(final Uri uri) {
		return uri.getLastPathSegment();
	}

	public String getType(final Uri uri) {
		return URLConnection.guessContentTypeFromName(uri.toString());
	}

	public Uri insert(final Uri uri, final ContentValues contentValues) {
		return null;
	}

	public boolean onCreate() {
		return false;
	}

	public ParcelFileDescriptor openFile(final Uri uri, final String s) throws FileNotFoundException {
		final File file = new File(this.getContext().getFilesDir(), uri.getPath());
		Log.e("", "ParcelFileDescriptor file:" + file.getAbsolutePath());
		return ParcelFileDescriptor.open(file, 268435456);
	}

	public Cursor query(final Uri uri, String[] openable_PROJECTION, final String s, final String[] array, final String s2) {
		if (openable_PROJECTION == null) {
			openable_PROJECTION = InternalFileProvider.OPENABLE_PROJECTION;
		}
		final MatrixCursor matrixCursor = new MatrixCursor(openable_PROJECTION, 1);
		final MatrixCursor.RowBuilder row = matrixCursor.newRow();
		for (final String s3 : openable_PROJECTION) {
			if ("_display_name".equals(s3)) {
				row.add((Object)this.getFileName(uri));
			}
			else if ("_size".equals(s3)) {
				row.add((Object)this.getDataLength(uri));
			}
			else {
				row.add((Object)null);
			}
		}
		return (Cursor)matrixCursor;
	}

	public int update(final Uri uri, final ContentValues contentValues, final String s, final String[] array) {
		return 0;
	}
}

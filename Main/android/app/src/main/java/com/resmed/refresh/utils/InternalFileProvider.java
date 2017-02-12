package com.resmed.refresh.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MatrixCursor.RowBuilder;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLConnection;

public class InternalFileProvider
  extends ContentProvider
{
  public static final Uri CONTENT_URI = Uri.parse("content://com.resmed.refresh/");
  private static final String[] OPENABLE_PROJECTION = { "_display_name", "_size" };
  
  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    return 0;
  }
  
  protected long getDataLength(Uri paramUri)
  {
    return new File(getContext().getFilesDir(), paramUri.getPath()).length();
  }
  
  protected String getFileName(Uri paramUri)
  {
    return paramUri.getLastPathSegment();
  }
  
  public String getType(Uri paramUri)
  {
    return URLConnection.guessContentTypeFromName(paramUri.toString());
  }
  
  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    return null;
  }
  
  public boolean onCreate()
  {
    return false;
  }
  
  public ParcelFileDescriptor openFile(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    paramUri = new File(getContext().getFilesDir(), paramUri.getPath());
    Log.e("", "ParcelFileDescriptor file:" + paramUri.getAbsolutePath());
    return ParcelFileDescriptor.open(paramUri, 268435456);
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    paramString1 = paramArrayOfString1;
    if (paramArrayOfString1 == null) {
      paramString1 = OPENABLE_PROJECTION;
    }
    paramArrayOfString2 = new MatrixCursor(paramString1, 1);
    paramString2 = paramArrayOfString2.newRow();
    int j = paramString1.length;
    int i = 0;
    if (i >= j) {
      return paramArrayOfString2;
    }
    paramArrayOfString1 = paramString1[i];
    if ("_display_name".equals(paramArrayOfString1)) {
      paramString2.add(getFileName(paramUri));
    }
    for (;;)
    {
      i++;
      break;
      if ("_size".equals(paramArrayOfString1)) {
        paramString2.add(Long.valueOf(getDataLength(paramUri)));
      } else {
        paramString2.add(null);
      }
    }
  }
  
  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    return 0;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package vpackages;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import v.lucidlink.MainActivity;

public class VFile {
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
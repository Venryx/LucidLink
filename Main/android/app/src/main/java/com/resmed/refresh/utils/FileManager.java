package com.resmed.refresh.utils;

import java.io.*;
import java.util.*;
import java.nio.*;

public class FileManager
{
	public static void deleteFile(final File file) {
		if (file.exists()) {
			file.delete();
		}
	}

	public static byte[] getByteArrayFromFile(final File file) {
		try {
			final FileInputStream fileInputStream = new FileInputStream(file);
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			final byte[] array = new byte[8192];
			while (true) {
				final int read = fileInputStream.read(array);
				if (read == -1) {
					break;
				}
				byteArrayOutputStream.write(array, 0, read);
			}
			return byteArrayOutputStream.toByteArray();
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		catch (IOException ex2) {
			ex2.printStackTrace();
			//goto Label_0062;
		}
		return null;
	}

	private static void getFileFromByteArray(final byte[] array, final File file) {
		try {
			final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
			bufferedOutputStream.write(array);
			bufferedOutputStream.flush();
			bufferedOutputStream.close();
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		catch (IOException ex2) {
			ex2.printStackTrace();
		}
	}

	public static void mergeFiles(final Vector<String> vector, final File file) {
		final Vector<byte[]> vector2 = new Vector<byte[]>();
		int n = 0;
		for (int i = 0; i < vector.size(); ++i) {
			final byte[] byteArrayFromFile = getByteArrayFromFile(new File(vector.get(i)));
			vector2.add(byteArrayFromFile);
			n += byteArrayFromFile.length;
		}
		final byte[] array = new byte[n];
		final ByteBuffer wrap = ByteBuffer.wrap(array);
		for (int j = 0; j < vector2.size(); ++j) {
			wrap.put(vector2.get(j));
		}
		getFileFromByteArray(array, file);
	}
}

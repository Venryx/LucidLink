/*
 * Decompiled with CFR 0_115.
 *
 * Could not load the following classes:
 *  com.resmed.refresh.packets.PacketsByteValuesReader
 */
package com.resmed.refresh.packets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/*
 * Exception performing whole class analysis ignored.
 */
public abstract class PacketsByteValuesReader {
	public static final int BIO_IGNORE_PATTERN = 64250;
	public static final int ENV_IGNORE_PATTERN = 5376;

	public PacketsByteValuesReader() {
	}

	public static int decompress_light(int n) {
		if (n < 64) {
			return n;
		}
		if (n < 96) {
			return 64 + 2 * (n - 64);
		}
		if (n < 128) {
			return 128 + 4 * (n - 96);
		}
		if (n < 160) {
			return 256 + 8 * (n - 128);
		}
		if (n < 192) {
			return 512 + 16 * (n - 160);
		}
		if (n < 240) {
			return 1024 + 64 * (n - 192);
		}
		if (n >= 255) return 6000;
		return 4096 + 128 * (n - 240);
	}

	public static int getStoreLocalBio(byte[] arrby) {
		int n = arrby.length;
		int n2 = 0;
		if (n <= 7) return n2;
		return 255 & arrby[0] | (255 & arrby[1]) << 8 | (255 & arrby[2]) << 16 | (255 & arrby[3]) << 24;
	}

	public static int getStoreLocalEnv(byte[] arrby) {
		int n = arrby.length;
		int n2 = 0;
		if (n <= 7) return n2;
		return 255 & arrby[4] | (255 & arrby[5]) << 8 | (255 & arrby[6]) << 16 | (255 & arrby[7]) << 24;
	}

	public static int[] readBioData(byte[] arrby) {
		int[] arrn = new int[2];
		if (arrby.length <= 1) return arrn;
		arrn[0] = 255 & arrby[0];
		arrn[0] = arrn[0] | (255 & arrby[1]) << 8;
		if (arrby.length <= 3) return arrn;
		arrn[1] = 255 & arrby[2];
		arrn[1] = (255 & arrby[3]) << 8;
		return arrn;
	}

	public static int[][] readBioData(byte[] arrby, int n) {
		int[] arrn = new int[]{2, n - 8};
		int[][] arrn2 = (int[][])Array.newInstance(Integer.TYPE, arrn);
		int n2 = 0;
		int n3 = 0;
		for (int i = -1 + arrby.length; i >= 0; ++n3, --i) {
			if (arrby[i] != 0) break;
		}
		int n4 = 0;
		do {
			if (n4 >= arrby.length - n3) break;
			try {
				int n5 = 255 & arrby[n4] | (255 & arrby[n4 + 1]) << 8;
				int n6 = arrby.length;
				int n7 = n4 + 2;
				int n8 = 0;
				if (n6 > n7) {
					int n9 = arrby.length;
					int n10 = n4 + 3;
					n8 = 0;
					if (n9 > n10) {
						n8 = 255 & arrby[n4 + 2] | (255 & arrby[n4 + 3]) << 8;
					}
				}
				arrn2[0][n2] = n5;
				arrn2[1][n2] = n8;
				if (n5 != 64250 && n8 != 64250) {
					++n2;
				}
			}
			catch (ArrayIndexOutOfBoundsException var11_8) {
				var11_8.printStackTrace();
			}
			n4 += 4;
		} while (true);
		int[] arrn3 = new int[]{2, n2};
		int[][] arrn4 = (int[][])Array.newInstance(Integer.TYPE, arrn3);
		int n11 = 0;
		while (n11 < n2) {
			arrn4[0][n11] = arrn2[0][n11];
			arrn4[1][n11] = arrn2[1][n11];
			++n11;
		}
		return arrn4;
	}

	public static int readIlluminanceValue(byte[] arrby) {
		if (arrby.length >= 2) return PacketsByteValuesReader.decompress_light((int)(255 & arrby[1]));
		return 0;
	}

	public static int[] readIlluminanceValues(byte[] arrby) {
		ArrayList<Byte> arrayList = new ArrayList<Byte>();
		int n = 0;
		do {
			if (n >= arrby.length) break;
			if (n % 2 != 0 && (255 & arrby[n]) != 250) {
				arrayList.add(Byte.valueOf(arrby[n]));
			}
			++n;
		} while (true);
		int[] arrn = new int[arrayList.size()];
		int n2 = 0;
		while (n2 < arrayList.size()) {
			arrn[n2] = Math.round(255 & ((Byte)arrayList.get(n2)).byteValue());
			++n2;
		}
		return arrn;
	}

	public static float readTemperatureValue(byte[] arrby) {
		if (arrby.length <= 0) return 0.0f;
		return (float)(255 & arrby[0]) / 4.0f;
	}

	public static float[] readTemperatureValues(byte[] arrby) {
		ArrayList<Byte> arrayList = new ArrayList<Byte>();
		int n = 0;
		do {
			if (n >= arrby.length) break;
			if (n % 2 == 0 && (255 & arrby[n]) != 250) {
				arrayList.add(Byte.valueOf(arrby[n]));
			}
			++n;
		} while (true);
		float[] arrf = new float[arrayList.size()];
		int n2 = 0;
		while (n2 < arrayList.size()) {
			arrf[n2] = (float)(255 & ((Byte)arrayList.get(n2)).byteValue()) / 4.0f;
			++n2;
		}
		return arrf;
	}

	public static void saveToEDF(byte[] arrby, FileOutputStream fileOutputStream) {
		try {
			fileOutputStream.write(arrby);
			fileOutputStream.close();
			return;
		}
		catch (IOException var2_2) {
			var2_2.printStackTrace();
			return;
		}
	}
}

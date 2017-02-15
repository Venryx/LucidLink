package com.resmed.refresh.packets;

import android.support.v4.view.MotionEventCompat;
import com.google.android.vending.expansion.downloader.impl.DownloaderService;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public abstract class PacketsByteValuesReader {
	public static final int BIO_IGNORE_PATTERN = 64250;
	public static final int ENV_IGNORE_PATTERN = 5376;

	public static int[] readBioData(byte[] decobbed) {
		int[] bioBytes = new int[2];
		if (decobbed.length > 1) {
			bioBytes[0] = decobbed[0] & MotionEventCompat.ACTION_MASK;
			bioBytes[0] = bioBytes[0] | ((decobbed[1] & MotionEventCompat.ACTION_MASK) << 8);
			if (decobbed.length > 3) {
				bioBytes[1] = decobbed[2] & MotionEventCompat.ACTION_MASK;
				bioBytes[1] = (decobbed[3] & MotionEventCompat.ACTION_MASK) << 8;
			}
		}
		return bioBytes;
	}

	public static int[][] readBioData(byte[] decobbed, int packetSize) {
		int[][] tempChannelData = (int[][]) Array.newInstance(Integer.TYPE, new int[]{2, packetSize - 8});
		int indexSamples = 0;
		int nrBytesToIgnore = 0;
		for (int j = decobbed.length - 1; j >= 0; j--) {
			nrBytesToIgnore++;
			if (decobbed[j] != (byte) 0) {
				break;
			}
		}
		int i = 0;
		while (i < decobbed.length - nrBytesToIgnore) {
			int bioMQValue = 0;
			try {
				int bioMIValue = (decobbed[i] & MotionEventCompat.ACTION_MASK) | ((decobbed[i + 1] & MotionEventCompat.ACTION_MASK) << 8);
				if (decobbed.length > i + 2 && decobbed.length > i + 3) {
					bioMQValue = (decobbed[i + 2] & MotionEventCompat.ACTION_MASK) | ((decobbed[i + 3] & MotionEventCompat.ACTION_MASK) << 8);
				}
				tempChannelData[0][indexSamples] = bioMIValue;
				tempChannelData[1][indexSamples] = bioMQValue;
				if (!(bioMIValue == BIO_IGNORE_PATTERN || bioMQValue == BIO_IGNORE_PATTERN)) {
					indexSamples++;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			i += 4;
		}
		int[][] validSamples = (int[][]) Array.newInstance(Integer.TYPE, new int[]{2, indexSamples});
		for (i = 0; i < indexSamples; i++) {
			validSamples[0][i] = tempChannelData[0][i];
			validSamples[1][i] = tempChannelData[1][i];
		}
		return validSamples;
	}

	public static int getStoreLocalEnv(byte[] bytes) {
		if (bytes.length > 7) {
			return (((bytes[4] & MotionEventCompat.ACTION_MASK) | ((bytes[5] & MotionEventCompat.ACTION_MASK) << 8)) | ((bytes[6] & MotionEventCompat.ACTION_MASK) << 16)) | ((bytes[7] & MotionEventCompat.ACTION_MASK) << 24);
		}
		return 0;
	}

	public static int getStoreLocalBio(byte[] bytes) {
		if (bytes.length > 7) {
			return (((bytes[0] & MotionEventCompat.ACTION_MASK) | ((bytes[1] & MotionEventCompat.ACTION_MASK) << 8)) | ((bytes[2] & MotionEventCompat.ACTION_MASK) << 16)) | ((bytes[3] & MotionEventCompat.ACTION_MASK) << 24);
		}
		return 0;
	}

	public static int readIlluminanceValue(byte[] bytes) {
		if (bytes.length < 2) {
			return 0;
		}
		return decompress_light(bytes[1] & MotionEventCompat.ACTION_MASK);
	}

	public static float readTemperatureValue(byte[] bytes) {
		if (bytes.length > 0) {
			return ((float) (bytes[0] & MotionEventCompat.ACTION_MASK)) / 4.0f;
		}
		return 0.0f;
	}

	public static float[] readTemperatureValues(byte[] bytes) {
		List<Byte> bList = new ArrayList();
		int i = 0;
		while (i < bytes.length) {
			if (i % 2 == 0 && (bytes[i] & MotionEventCompat.ACTION_MASK) != 250) {
				bList.add(Byte.valueOf(bytes[i]));
			}
			i++;
		}
		float[] bArray = new float[bList.size()];
		for (int x = 0; x < bList.size(); x++) {
			bArray[x] = ((float) (((Byte) bList.get(x)).byteValue() & MotionEventCompat.ACTION_MASK)) / 4.0f;
		}
		return bArray;
	}

	public static int[] readIlluminanceValues(byte[] bytes) {
		List<Byte> bList = new ArrayList();
		int i = 0;
		while (i < bytes.length) {
			if (!(i % 2 == 0 || (bytes[i] & MotionEventCompat.ACTION_MASK) == 250)) {
				bList.add(Byte.valueOf(bytes[i]));
			}
			i++;
		}
		int[] bArray = new int[bList.size()];
		for (int x = 0; x < bList.size(); x++) {
			bArray[x] = Math.round((float) (((Byte) bList.get(x)).byteValue() & MotionEventCompat.ACTION_MASK));
		}
		return bArray;
	}

	public static void saveToEDF(byte[] decobbed, FileOutputStream output) {
		try {
			output.write(decobbed);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int decompress_light(int input) {
		if (input < 64) {
			return input;
		}
		if (input < 96) {
			return ((input - 64) * 2) + 64;
		}
		if (input < 128) {
			return ((input - 96) * 4) + 128;
		}
		if (input < 160) {
			return ((input - 128) * 8) + 256;
		}
		if (input < DownloaderService.STATUS_RUNNING) {
			return ((input - 160) * 16) + 512;
		}
		if (input < 240) {
			return ((input - 192) * 64) + 1024;
		}
		if (input < MotionEventCompat.ACTION_MASK) {
			return ((input - 240) * 128) + 4096;
		}
		return 6000;
	}
}
package com.resmed.cobs;

public class COBS {
	private static COBS Instance = null;

	public native byte[] decode(byte[] bArr);

	public native byte[] encode(byte[] bArr);

	static {
		System.loadLibrary("cobs");
	}

	private COBS() {
	}

	public static COBS getInstance() {
		if (Instance == null) {
			Instance = new COBS();
		}
		return Instance;
	}
}
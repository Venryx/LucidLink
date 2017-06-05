package com.resmed.refresh.packets;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class VLP {
	public static int CHECKSUMBYTE = 1;
	public static int VLPHEADER = 8;
	public static VLP vlpObject = null;

	public static VLP getInstance() {
		if (vlpObject == null) {
			vlpObject = new VLP();
		}
		return vlpObject;
	}

	public VLP.VLPacket Depacketize(final ByteBuffer byteBuffer) {
		final VLP.VLPacket vlPacket = new VLP.VLPacket();
		//try {
		final byte[] copyOfRange = Arrays.copyOfRange(byteBuffer.array(), 0, byteBuffer.capacity());
		final int packetSize = (0xFF & copyOfRange[3]) << 8 | (0xFF & copyOfRange[2]);
		final int vlpheader = VLP.VLPHEADER;
		final int n = packetSize - VLP.CHECKSUMBYTE;
		if (n > vlpheader) {
			vlPacket.buffer = Arrays.copyOfRange(copyOfRange, vlpheader, n);
		} else {
			vlPacket.buffer = new byte[0];
		}
		if (vlPacket.buffer.length > 0) {
			vlPacket.packetType = copyOfRange[0];
			vlPacket.packetNo = copyOfRange[1];
			vlPacket.packetSize = packetSize;
			vlPacket.sampleCount = (copyOfRange[7] << 24 | copyOfRange[6] << 16 | copyOfRange[5] << 8 | copyOfRange[4]);
			vlPacket.checkSum = copyOfRange[packetSize - 1];
			return vlPacket;
		}
		/*} catch (Exception ex) {
			final ErrorReporter errorReporter = ACRA.getErrorReporter();
			errorReporter.putCustomData("packetData", Arrays.toString(byteBuffer.array()));
			errorReporter.handleException((Throwable)ex);
		}*/
		return vlPacket;
	}

	public ByteBuffer Packetize(byte var1, byte var2, int var3, int var4, byte[] var5) {
		int var6 = 0;
		if (var5 != null) {
			var6 = var5.length;
		}

		byte[] var7 = new byte[var6 + VLPHEADER + CHECKSUMBYTE];
		byte var8 = 0;
		int var9 = var3 + VLPHEADER + CHECKSUMBYTE;
		var7[0] = var1;
		var7[1] = var2;
		var7[2] = (byte) (var9 & 255);
		var7[3] = (byte) (255 & var9 >> 8);
		var7[4] = (byte) (var4 & 255);
		var7[5] = (byte) (255 & var4 >> 8);
		var7[6] = (byte) (255 & var4 >> 16);
		var7[7] = (byte) (255 & var4 >> 24);
		if (var5 != null) {
			byte[] var11 = Arrays.copyOf(var5, var6);

			for (int var12 = 0; var12 < var6; ++var12) {
				var7[var12 + VLPHEADER] = var11[var12];
			}
		}

		for (int var10 = 0; var10 < var9 - 1; ++var10) {
			var8 += var7[var10];
		}

		var7[var9 - 1] = (byte) (1 + ~var8);
		return ByteBuffer.wrap(var7);
	}


	public class VLPacket {
		public byte[] buffer;
		public byte checkSum;
		public byte packetNo;
		public int packetSize;
		public byte packetType;
		public long sampleCount;

		public VLPacket() {
		}
	}
}
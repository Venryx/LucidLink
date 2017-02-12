package com.resmed.refresh.utils;

import com.resmed.refresh.bluetooth.*;
import java.io.*;
import com.resmed.refresh.packets.*;
import android.os.*;

public class StreamFirmwareAsyncTask extends AsyncTask<Void, Integer, Integer>
{
	public interface StreamingManager
	{
		void onStreamFinished(final Integer p0);

		void onStreamStarted();
	}

	private static int PACKETSIZE;
	private BluetoothDataWriter btWriter;
	private byte[] bytes;
	private Handler handleProgress;
	private Boolean packetConfirmed;
	private StreamFirmwareAsyncTask.StreamingManager sManager;

	static {
		StreamFirmwareAsyncTask.PACKETSIZE = 128;
	}

	public StreamFirmwareAsyncTask(final byte[] bytes, final Handler handleProgress, final BluetoothDataWriter btWriter, final StreamFirmwareAsyncTask.StreamingManager sManager) {
		this.packetConfirmed = false;
		this.bytes = bytes;
		this.handleProgress = handleProgress;
		this.btWriter = btWriter;
		this.sManager = sManager;
	}

	protected /* varargs */ Integer doInBackground(Void ... arrvoid) {
		Log.d((String)"com.resmed.refresh.bluetooth", (String)(" StreamFirmwareAsyncTask::doInBackground() params to be sent :" + this.bytes.length));
		byte[] arrby = new byte[PACKETSIZE];
		byte[] arrby2 = new byte[this.bytes.length];
		int n = 0;
		do {
			if (n >= this.bytes.length) break;
			arrby2[n] = this.bytes[n];
			++n;
		} while (true);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby2, 0, this.bytes.length);
		int n2 = 0;
		int n3 = 0;
		block9 : do {
			if (n3 == -1) {
				Log.d((String)"com.resmed.refresh.bluetooth", (String)" StreamFirmwareAsyncTask::doInBackground()  : ");
				this.btWriter.sendBytesToBeD(null, VLPacketType.PACKET_TYPE_FW_LOAD_END);
				return n2;
			}
			n3 = byteArrayInputStream.read(arrby, 0, PACKETSIZE);
			Boolean bl = this.packetConfirmed;
			// MONITORENTER : bl
			this.packetConfirmed = false;
			// MONITOREXIT : bl
			this.btWriter.sendBytesToBeD(arrby, VLPacketType.PACKET_TYPE_APP_LOAD);
			arrby = new byte[PACKETSIZE];
			this.publishProgress(new Integer[]{n2 += n3});
			long l = System.currentTimeMillis();
			do {
				Boolean bl2 = this.packetConfirmed;
				// MONITORENTER : bl2
				if (this.packetConfirmed.booleanValue()) {
					// MONITOREXIT : bl2
					continue block9;
				}
				// MONITOREXIT : bl2
				if (System.currentTimeMillis() - l >= 15000) {
					n3 = -1;
					continue block9;
				}
				try {
					Thread.sleep(2);
					continue;
				}
				catch (InterruptedException var17_12) {
					var17_12.printStackTrace();
					continue;
				}
			} while (true);
		} while (true);
	}


	protected void onPostExecute(final Integer n) {
		super.onPostExecute(n);
		Log.d("com.resmed.refresh.bluetooth", " StreamFirmwareAsyncTask::onPostExecute() : ");
		this.sManager.onStreamFinished(n);
		this.sManager = null;
		this.btWriter = null;
		this.handleProgress = null;
	}

	protected void onPreExecute() {
		super.onPreExecute();
		Log.d("com.resmed.refresh.bluetooth", " StreamFirmwareAsyncTask::onPreExecute() bytes : " + this.bytes.length);
		final Message message = new Message();
		message.arg1 = this.bytes.length;
		message.what = 1;
		this.handleProgress.sendMessage(message);
		this.sManager.onStreamStarted();
	}

	protected void onProgressUpdate(final Integer... array) {
		super.onProgressUpdate(array);
		final Message message = new Message();
		message.arg1 = array[0];
		message.what = 2;
		this.handleProgress.sendMessage(message);
		Log.e("", "Sending data " + message.arg1);
	}

	public void releaseNextPacket() {
		synchronized (this.packetConfirmed) {
			this.packetConfirmed = true;
		}
	}
}

package com.resmed.refresh.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import com.resmed.refresh.bluetooth.BluetoothDataWriter;
import com.resmed.refresh.packets.VLPacketType;
import java.io.ByteArrayInputStream;

public class StreamFirmwareAsyncTask
  extends AsyncTask<Void, Integer, Integer>
{
  private static int PACKETSIZE = 128;
  private BluetoothDataWriter btWriter;
  private byte[] bytes;
  private Handler handleProgress;
  private Boolean packetConfirmed = Boolean.valueOf(false);
  private StreamingManager sManager;
  
  public StreamFirmwareAsyncTask(byte[] paramArrayOfByte, Handler paramHandler, BluetoothDataWriter paramBluetoothDataWriter, StreamingManager paramStreamingManager)
  {
    this.bytes = paramArrayOfByte;
    this.handleProgress = paramHandler;
    this.btWriter = paramBluetoothDataWriter;
    this.sManager = paramStreamingManager;
  }
  
  protected Integer doInBackground(Void... paramVarArgs)
  {
    Log.d("com.resmed.refresh.bluetooth", " StreamFirmwareAsyncTask::doInBackground() params to be sent :" + this.bytes.length);
    paramVarArgs = new byte[PACKETSIZE];
    Object localObject = new byte[this.bytes.length];
    int j;
    for (int i = 0;; i++)
    {
      if (i >= this.bytes.length)
      {
        localObject = new ByteArrayInputStream((byte[])localObject, 0, this.bytes.length);
        j = 0;
        i = 0;
        if (i != -1) {
          break;
        }
        Log.d("com.resmed.refresh.bluetooth", " StreamFirmwareAsyncTask::doInBackground()  : ");
        this.btWriter.sendBytesToBeD(null, VLPacketType.PACKET_TYPE_FW_LOAD_END);
        return Integer.valueOf(j);
      }
      localObject[i] = this.bytes[i];
    }
    i = ((ByteArrayInputStream)localObject).read(paramVarArgs, 0, PACKETSIZE);
    for (;;)
    {
      long l;
      synchronized (this.packetConfirmed)
      {
        this.packetConfirmed = Boolean.valueOf(false);
        this.btWriter.sendBytesToBeD(paramVarArgs, VLPacketType.PACKET_TYPE_APP_LOAD);
        paramVarArgs = new byte[PACKETSIZE];
        j += i;
        publishProgress(new Integer[] { Integer.valueOf(j) });
        l = System.currentTimeMillis();
        synchronized (this.packetConfirmed)
        {
          if (!this.packetConfirmed.booleanValue()) {}
        }
      }
      if (System.currentTimeMillis() - l >= 15000L)
      {
        i = -1;
        break;
      }
      try
      {
        Thread.sleep(2L);
      }
      catch (InterruptedException localInterruptedException)
      {
        localInterruptedException.printStackTrace();
      }
    }
  }
  
  protected void onPostExecute(Integer paramInteger)
  {
    super.onPostExecute(paramInteger);
    Log.d("com.resmed.refresh.bluetooth", " StreamFirmwareAsyncTask::onPostExecute() : ");
    this.sManager.onStreamFinished(paramInteger);
    this.sManager = null;
    this.btWriter = null;
    this.handleProgress = null;
  }
  
  protected void onPreExecute()
  {
    super.onPreExecute();
    Log.d("com.resmed.refresh.bluetooth", " StreamFirmwareAsyncTask::onPreExecute() bytes : " + this.bytes.length);
    Message localMessage = new Message();
    localMessage.arg1 = this.bytes.length;
    localMessage.what = 1;
    this.handleProgress.sendMessage(localMessage);
    this.sManager.onStreamStarted();
  }
  
  protected void onProgressUpdate(Integer... paramVarArgs)
  {
    super.onProgressUpdate(paramVarArgs);
    Message localMessage = new Message();
    localMessage.arg1 = paramVarArgs[0].intValue();
    localMessage.what = 2;
    this.handleProgress.sendMessage(localMessage);
    Log.e("", "Sending data " + localMessage.arg1);
  }
  
  public void releaseNextPacket()
  {
    synchronized (this.packetConfirmed)
    {
      this.packetConfirmed = Boolean.valueOf(true);
      return;
    }
  }
  
  public static abstract interface StreamingManager
  {
    public abstract void onStreamFinished(Integer paramInteger);
    
    public abstract void onStreamStarted();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\StreamFirmwareAsyncTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
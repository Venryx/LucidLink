package com.resmed.cobs;

import android.app.Activity;
import android.os.Bundle;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class IndexActivity
  extends Activity
{
  private COBS cobs;
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.cobs = COBS.getInstance();
    paramBundle = VLP.getInstance();
    System.out.println(" INPUT : " + "{\"jsonrpc\":\"2.0\",\"result\":{\"type\":1,\"payload\":0},\"id\":1}");
    ByteBuffer localByteBuffer = paramBundle.Packetize((byte)1, (byte)1, "{\"jsonrpc\":\"2.0\",\"result\":{\"type\":1,\"payload\":0},\"id\":1}".getBytes().length, 64, "{\"jsonrpc\":\"2.0\",\"result\":{\"type\":1,\"payload\":0},\"id\":1}".getBytes());
    localByteBuffer = ByteBuffer.wrap(this.cobs.encode(localByteBuffer.array()));
    System.out.println(" COBS ENCODE : " + Arrays.toString(localByteBuffer.array()) + " length :" + localByteBuffer.array().length);
    paramBundle = new String(paramBundle.Depacketize(ByteBuffer.wrap(this.cobs.decode(localByteBuffer.array()))).buffer);
    System.out.println(" OUTPUT : " + paramBundle);
  }
  
  public static class VLP
  {
    public static final int CHECKSUMBYTE = 1;
    public static final int VLPHEADER = 8;
    public static VLP vlpObject = null;
    
    public static VLP getInstance()
    {
      if (vlpObject == null) {
        vlpObject = new VLP();
      }
      return vlpObject;
    }
    
    public VLPacket Depacketize(ByteBuffer paramByteBuffer)
    {
      VLPacket localVLPacket = new VLPacket();
      int j = paramByteBuffer.capacity();
      paramByteBuffer = Arrays.copyOfRange(paramByteBuffer.array(), 0, j);
      int i = j - 1;
      if (i > 8) {}
      for (localVLPacket.buffer = Arrays.copyOfRange(paramByteBuffer, 8, i);; localVLPacket.buffer = new byte[0])
      {
        if (localVLPacket.buffer.length > 0)
        {
          localVLPacket.packetType = paramByteBuffer[0];
          localVLPacket.packetNo = paramByteBuffer[1];
          localVLPacket.packetSize = j;
          localVLPacket.sampleCount = (paramByteBuffer[7] << 24 | paramByteBuffer[6] << 16 | paramByteBuffer[5] << 8 | paramByteBuffer[4]);
          localVLPacket.checkSum = paramByteBuffer[(j - 1)];
        }
        return localVLPacket;
      }
    }
    
    public ByteBuffer Packetize(byte paramByte1, byte paramByte2, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
    {
      int i = 0;
      if (paramArrayOfByte != null) {
        i = paramArrayOfByte.length;
      }
      byte[] arrayOfByte = new byte[i + 8 + 1];
      int j = 0;
      int k = paramInt1 + 9;
      arrayOfByte[0] = paramByte1;
      arrayOfByte[1] = paramByte2;
      arrayOfByte[2] = ((byte)(k & 0xFF));
      arrayOfByte[3] = ((byte)(k >> 8 & 0xFF));
      arrayOfByte[4] = ((byte)(paramInt2 & 0xFF));
      arrayOfByte[5] = ((byte)(paramInt2 >> 8 & 0xFF));
      arrayOfByte[6] = ((byte)(paramInt2 >> 16 & 0xFF));
      arrayOfByte[7] = ((byte)(paramInt2 >> 24 & 0xFF));
      if (paramArrayOfByte != null)
      {
        paramArrayOfByte = Arrays.copyOf(paramArrayOfByte, i);
        paramInt1 = 0;
        if (paramInt1 < i) {}
      }
      else
      {
        paramInt1 = 0;
        paramInt2 = j;
      }
      for (;;)
      {
        if (paramInt1 >= k - 1)
        {
          arrayOfByte[(k - 1)] = ((byte)((paramInt2 ^ 0xFFFFFFFF) + 1));
          return ByteBuffer.wrap(arrayOfByte);
          arrayOfByte[(paramInt1 + 8)] = paramArrayOfByte[paramInt1];
          paramInt1++;
          break;
        }
        paramInt2 = (byte)(arrayOfByte[paramInt1] + paramInt2);
        paramInt1++;
      }
    }
    
    public class VLPacket
    {
      public byte[] buffer;
      public byte checkSum;
      public byte packetNo;
      public int packetSize;
      public byte packetType;
      public long sampleCount;
      
      public VLPacket() {}
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\cobs\IndexActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
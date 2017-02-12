package com.resmed.refresh.packets;

import java.nio.ByteBuffer;
import java.util.Arrays;
import org.acra.ACRA;
import org.acra.ErrorReporter;

public class VLP
{
  public static int CHECKSUMBYTE = 1;
  public static int VLPHEADER = 8;
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
    for (;;)
    {
      try
      {
        byte[] arrayOfByte = Arrays.copyOfRange(paramByteBuffer.array(), 0, paramByteBuffer.capacity());
        int k = (arrayOfByte[3] & 0xFF) << 8 | arrayOfByte[2] & 0xFF;
        int i = VLPHEADER;
        int j = k - CHECKSUMBYTE;
        if (j <= i) {
          continue;
        }
        localVLPacket.buffer = Arrays.copyOfRange(arrayOfByte, i, j);
        if (localVLPacket.buffer.length > 0)
        {
          localVLPacket.packetType = arrayOfByte[0];
          localVLPacket.packetNo = arrayOfByte[1];
          localVLPacket.packetSize = k;
          localVLPacket.sampleCount = (arrayOfByte[7] << 24 | arrayOfByte[6] << 16 | arrayOfByte[5] << 8 | arrayOfByte[4]);
          localVLPacket.checkSum = arrayOfByte[(k - 1)];
        }
      }
      catch (Exception localException)
      {
        ErrorReporter localErrorReporter = ACRA.getErrorReporter();
        localErrorReporter.putCustomData("packetData", Arrays.toString(paramByteBuffer.array()));
        localErrorReporter.handleException(localException);
        continue;
      }
      return localVLPacket;
      localVLPacket.buffer = new byte[0];
    }
  }
  
  public ByteBuffer Packetize(byte paramByte1, byte paramByte2, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    int i = 0;
    if (paramArrayOfByte != null) {
      i = paramArrayOfByte.length;
    }
    byte[] arrayOfByte = new byte[VLPHEADER + i + CHECKSUMBYTE];
    int j = 0;
    int k = paramInt1 + (VLPHEADER + CHECKSUMBYTE);
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
        arrayOfByte[(VLPHEADER + paramInt1)] = paramArrayOfByte[paramInt1];
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


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\packets\VLP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
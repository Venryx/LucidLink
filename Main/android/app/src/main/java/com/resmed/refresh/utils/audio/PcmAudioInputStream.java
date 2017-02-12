package com.resmed.refresh.utils.audio;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PcmAudioInputStream
  extends InputStream
  implements Closeable
{
  private static final int BYTE_BUFFER_SIZE = 4096;
  final DataInputStream dis;
  final PcmAudioFormat format;
  
  public PcmAudioInputStream(PcmAudioFormat paramPcmAudioFormat, InputStream paramInputStream)
  {
    this.format = paramPcmAudioFormat;
    this.dis = new DataInputStream(paramInputStream);
  }
  
  private void validateReadCount(int paramInt)
  {
    if (paramInt % this.format.getBytePerSample() != 0) {
      throw new IllegalStateException("unexpected amounts of bytes read from the input stream. Byte count must be an order of:" + this.format.getBytePerSample());
    }
  }
  
  public void close()
    throws IOException
  {
    this.dis.close();
  }
  
  public int read()
    throws IOException
  {
    return this.dis.read();
  }
  
  public int[] readAll()
    throws IOException
  {
    byte[] arrayOfByte = IOs.readAsByteArray(this.dis);
    return Bytes.toIntArray(arrayOfByte, arrayOfByte.length, this.format.getBytePerSample(), this.format.isBigEndian());
  }
  
  public int[] readSamplesAsIntArray(int paramInt)
    throws IOException
  {
    byte[] arrayOfByte = new byte[this.format.getBytePerSample() * paramInt];
    return Bytes.toIntArray(arrayOfByte, this.dis.read(arrayOfByte), this.format.getBytePerSample(), this.format.isBigEndian());
  }
  
  public int[] readSamplesAsIntArray(int paramInt1, int paramInt2)
    throws IOException
  {
    skipSamples(this.format.getBytePerSample() * paramInt1);
    return readSamplesAsIntArray(paramInt2 - paramInt1);
  }
  
  public byte[] readSamplesByteArray(int paramInt)
    throws IOException
  {
    byte[] arrayOfByte2 = new byte[this.format.getBytePerSample() * paramInt];
    paramInt = this.dis.read(arrayOfByte2);
    byte[] arrayOfByte1;
    if (paramInt != arrayOfByte2.length)
    {
      validateReadCount(paramInt);
      arrayOfByte1 = new byte[paramInt];
      System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, paramInt);
    }
    for (;;)
    {
      return arrayOfByte1;
      arrayOfByte1 = arrayOfByte2;
    }
  }
  
  public double[] readSamplesNormalized()
    throws IOException
  {
    int[] arrayOfInt = readAll();
    double[] arrayOfDouble = new double[arrayOfInt.length];
    int j = this.format.getSampleSizeInBits();
    for (int i = 0;; i++)
    {
      if (i >= arrayOfDouble.length) {
        return arrayOfDouble;
      }
      arrayOfDouble[i] = (arrayOfInt[i] / (Integer.MAX_VALUE >>> 31 - j));
    }
  }
  
  public double[] readSamplesNormalized(int paramInt)
    throws IOException
  {
    int[] arrayOfInt = readSamplesAsIntArray(paramInt);
    double[] arrayOfDouble = new double[arrayOfInt.length];
    int i = this.format.getSampleSizeInBits();
    for (paramInt = 0;; paramInt++)
    {
      if (paramInt >= arrayOfDouble.length) {
        return arrayOfDouble;
      }
      arrayOfDouble[paramInt] = (arrayOfInt[paramInt] / (Integer.MAX_VALUE >>> 31 - i));
    }
  }
  
  public short[] readSamplesShortArray(int paramInt)
    throws IOException
  {
    byte[] arrayOfByte = readSamplesByteArray(paramInt);
    return Bytes.toShortArray(arrayOfByte, arrayOfByte.length, this.format.isBigEndian());
  }
  
  public int skipSamples(int paramInt)
    throws IOException
  {
    return (int)this.dis.skip(this.format.getBytePerSample() * paramInt) / this.format.getBytePerSample();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\audio\PcmAudioInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.utils.audio;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class WavFileWriter
  implements Closeable
{
  private final File file;
  private final WavAudioFormat pcmAudioFormat;
  private final PcmAudioOutputStream pos;
  private int totalSampleBytesWritten = 0;
  
  public WavFileWriter(WavAudioFormat paramWavAudioFormat, File paramFile)
    throws IOException
  {
    if (paramWavAudioFormat.isBigEndian()) {
      throw new IllegalArgumentException("Wav file cannot contain bigEndian sample data.");
    }
    if ((paramWavAudioFormat.getSampleSizeInBits() > 8) && (!paramWavAudioFormat.isSigned())) {
      throw new IllegalArgumentException("Wav file cannot contain unsigned data for this sampleSize:" + paramWavAudioFormat.getSampleSizeInBits());
    }
    this.pcmAudioFormat = paramWavAudioFormat;
    this.file = paramFile;
    this.pos = new PcmAudioOutputStream(paramWavAudioFormat, paramFile);
    this.pos.write(new RiffHeaderData(paramWavAudioFormat, 0).asByteArray());
  }
  
  private void checkLimit(int paramInt1, int paramInt2)
  {
    long l = paramInt1 + paramInt2;
    if (l >= 2147483647L) {
      throw new IllegalStateException("Size of bytes is too big:" + l);
    }
  }
  
  public void close()
    throws IOException
  {
    this.pos.close();
    PcmAudioHelper.modifyRiffSizeData(this.file, this.totalSampleBytesWritten);
  }
  
  public int getTotalSampleBytesWritten()
  {
    return this.totalSampleBytesWritten;
  }
  
  public PcmAudioFormat getWavFormat()
  {
    return this.pcmAudioFormat;
  }
  
  public WavFileWriter write(byte[] paramArrayOfByte)
    throws IOException
  {
    checkLimit(this.totalSampleBytesWritten, paramArrayOfByte.length);
    this.pos.write(paramArrayOfByte);
    this.totalSampleBytesWritten += paramArrayOfByte.length;
    return this;
  }
  
  public WavFileWriter write(int[] paramArrayOfInt)
    throws IOException
  {
    int i = this.pcmAudioFormat.getBytePerSample();
    checkLimit(this.totalSampleBytesWritten, paramArrayOfInt.length * i);
    this.pos.write(paramArrayOfInt);
    this.totalSampleBytesWritten += paramArrayOfInt.length * i;
    return this;
  }
  
  public WavFileWriter write(short[] paramArrayOfShort)
    throws IOException
  {
    checkLimit(this.totalSampleBytesWritten, paramArrayOfShort.length * 2);
    this.pos.write(paramArrayOfShort);
    this.totalSampleBytesWritten += paramArrayOfShort.length * 2;
    return this;
  }
  
  public WavFileWriter writeStereo(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    throws IOException
  {
    if (paramArrayOfInt1.length != paramArrayOfInt2.length) {
      throw new IllegalArgumentException("channels must have equeal amount of data.");
    }
    int j = this.pcmAudioFormat.getBytePerSample();
    for (int i = 0;; i++)
    {
      if (i >= paramArrayOfInt1.length) {
        return this;
      }
      this.pos.write(Bytes.toByteArray(paramArrayOfInt1[i], j, false));
      this.pos.write(Bytes.toByteArray(paramArrayOfInt2[i], j, false));
      this.totalSampleBytesWritten += j * 2;
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
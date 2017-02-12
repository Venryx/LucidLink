package com.resmed.refresh.utils.audio;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PcmAudioOutputStream
  extends OutputStream
  implements Closeable
{
  final DataOutputStream dos;
  final PcmAudioFormat format;
  
  public PcmAudioOutputStream(PcmAudioFormat paramPcmAudioFormat, DataOutputStream paramDataOutputStream)
  {
    this.format = paramPcmAudioFormat;
    this.dos = paramDataOutputStream;
  }
  
  public PcmAudioOutputStream(PcmAudioFormat paramPcmAudioFormat, File paramFile)
    throws IOException
  {
    this.format = paramPcmAudioFormat;
    this.dos = new DataOutputStream(new FileOutputStream(paramFile));
  }
  
  public void close()
  {
    IOs.closeSilently(new Closeable[] { this.dos });
  }
  
  public void write(int paramInt)
    throws IOException
  {
    this.dos.write(paramInt);
  }
  
  public void write(int[] paramArrayOfInt)
    throws IOException
  {
    this.dos.write(Bytes.toByteArray(paramArrayOfInt, paramArrayOfInt.length, this.format.getBytePerSample(), this.format.isBigEndian()));
  }
  
  public void write(short[] paramArrayOfShort)
    throws IOException
  {
    this.dos.write(Bytes.toByteArray(paramArrayOfShort, paramArrayOfShort.length, this.format.isBigEndian()));
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
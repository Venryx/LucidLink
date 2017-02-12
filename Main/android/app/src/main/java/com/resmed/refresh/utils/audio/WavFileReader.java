package com.resmed.refresh.utils.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WavFileReader
{
  private final File file;
  private final RiffHeaderData riffHeaderData;
  
  public WavFileReader(File paramFile)
    throws IOException
  {
    this.file = paramFile;
    this.riffHeaderData = new RiffHeaderData(paramFile);
  }
  
  public WavFileReader(String paramString)
    throws IOException
  {
    this(new File(paramString));
  }
  
  private void validateFrameBoundaries(int paramInt1, int paramInt2)
  {
    if (paramInt1 < 0) {
      throw new IllegalArgumentException("Start Frame cannot be negative:" + paramInt1);
    }
    if (paramInt2 < paramInt1) {
      throw new IllegalArgumentException("Start Frame cannot be after end frame. Start:" + paramInt1 + ", end:" + paramInt2);
    }
    if (paramInt2 > this.riffHeaderData.getSampleCount()) {
      throw new IllegalArgumentException("Frame count out of bounds. Max sample count:" + this.riffHeaderData.getSampleCount() + " but frame is:" + paramInt2);
    }
  }
  
  public int[] getAllSamples()
    throws IOException
  {
    PcmAudioInputStream localPcmAudioInputStream = getStream();
    try
    {
      int[] arrayOfInt = localPcmAudioInputStream.readAll();
      return arrayOfInt;
    }
    finally
    {
      localPcmAudioInputStream.close();
    }
  }
  
  public File getFile()
  {
    return this.file;
  }
  
  public PcmAudioFormat getFormat()
  {
    return this.riffHeaderData.getFormat();
  }
  
  public int getSampleCount()
  {
    return this.riffHeaderData.getSampleCount();
  }
  
  public int[] getSamplesAsInts(int paramInt1, int paramInt2)
    throws IOException
  {
    validateFrameBoundaries(paramInt1, paramInt2);
    PcmAudioInputStream localPcmAudioInputStream = getStream();
    try
    {
      localPcmAudioInputStream.skipSamples(paramInt1);
      int[] arrayOfInt = localPcmAudioInputStream.readSamplesAsIntArray(paramInt2 - paramInt1);
      return arrayOfInt;
    }
    finally
    {
      localPcmAudioInputStream.close();
    }
  }
  
  public short[] getSamplesAsShorts(int paramInt1, int paramInt2)
    throws IOException
  {
    validateFrameBoundaries(paramInt1, paramInt2);
    PcmAudioInputStream localPcmAudioInputStream = getStream();
    try
    {
      localPcmAudioInputStream.skipSamples(paramInt1);
      short[] arrayOfShort = localPcmAudioInputStream.readSamplesShortArray(paramInt2 - paramInt1);
      return arrayOfShort;
    }
    finally
    {
      localPcmAudioInputStream.close();
    }
  }
  
  public PcmAudioInputStream getStream()
    throws IOException
  {
    PcmAudioInputStream localPcmAudioInputStream = new PcmAudioInputStream(this.riffHeaderData.getFormat(), new FileInputStream(this.file));
    if (localPcmAudioInputStream.skip(44L) < 44L) {
      throw new IllegalArgumentException("cannot skip necessary amount of bytes from underlying stream.");
    }
    return localPcmAudioInputStream;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\audio\WavFileReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
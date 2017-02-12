package com.resmed.refresh.utils.audio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PcmAudioHelper
{
  public static void convertRawToWav(WavAudioFormat paramWavAudioFormat, File paramFile1, File paramFile2)
    throws IOException
  {
    DataOutputStream localDataOutputStream = new DataOutputStream(new FileOutputStream(paramFile2));
    localDataOutputStream.write(new RiffHeaderData(paramWavAudioFormat, 0).asByteArray());
    paramWavAudioFormat = new DataInputStream(new FileInputStream(paramFile1));
    paramFile1 = new byte['က'];
    int i = 0;
    for (;;)
    {
      int j = paramWavAudioFormat.read(paramFile1);
      if (j == -1)
      {
        localDataOutputStream.close();
        modifyRiffSizeData(paramFile2, i);
        return;
      }
      i += j;
      localDataOutputStream.write(paramFile1, 0, j);
    }
  }
  
  public static void convertWavToRaw(File paramFile1, File paramFile2)
    throws IOException
  {
    IOs.copy(new WavFileReader(paramFile1).getStream(), new FileOutputStream(paramFile2));
  }
  
  public static void generateSilenceWavFile(WavAudioFormat paramWavAudioFormat, File paramFile, double paramDouble)
    throws IOException
  {
    paramFile = new WavFileWriter(paramWavAudioFormat, paramFile);
    paramWavAudioFormat = new int[(int)(paramWavAudioFormat.getSampleRate() * paramDouble)];
    try
    {
      paramFile.write(paramWavAudioFormat);
      return;
    }
    finally
    {
      paramFile.close();
    }
  }
  
  static void modifyRiffSizeData(File paramFile, int paramInt)
    throws IOException
  {
    paramFile = new RandomAccessFile(paramFile, "rw");
    paramFile.seek(4L);
    paramFile.write(Bytes.toByteArray(paramInt + 36, false));
    paramFile.seek(40L);
    paramFile.write(Bytes.toByteArray(paramInt, false));
    paramFile.close();
  }
  
  public static double[] readAllFromWavNormalized(String paramString)
    throws IOException
  {
    return new WavFileReader(new File(paramString)).getStream().readSamplesNormalized();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\audio\PcmAudioHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
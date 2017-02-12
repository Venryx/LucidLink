package com.resmed.refresh.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Vector;

public class FileManager
{
  public static void deleteFile(File paramFile)
  {
    if (paramFile.exists()) {
      paramFile.delete();
    }
  }
  
  public static byte[] getByteArrayFromFile(File paramFile)
  {
    for (;;)
    {
      try
      {
        FileInputStream localFileInputStream = new java/io/FileInputStream;
        localFileInputStream.<init>(paramFile);
        localByteArrayOutputStream = new java/io/ByteArrayOutputStream;
        localByteArrayOutputStream.<init>();
        paramFile = new byte[' '];
        i = localFileInputStream.read(paramFile);
        if (i != -1) {
          continue;
        }
        paramFile = localByteArrayOutputStream.toByteArray();
      }
      catch (FileNotFoundException paramFile)
      {
        ByteArrayOutputStream localByteArrayOutputStream;
        int i;
        paramFile.printStackTrace();
        paramFile = null;
        continue;
      }
      catch (IOException paramFile)
      {
        paramFile.printStackTrace();
        continue;
      }
      return paramFile;
      localByteArrayOutputStream.write(paramFile, 0, i);
    }
  }
  
  private static void getFileFromByteArray(byte[] paramArrayOfByte, File paramFile)
  {
    try
    {
      BufferedOutputStream localBufferedOutputStream = new java/io/BufferedOutputStream;
      FileOutputStream localFileOutputStream = new java/io/FileOutputStream;
      localFileOutputStream.<init>(paramFile);
      localBufferedOutputStream.<init>(localFileOutputStream);
      localBufferedOutputStream.write(paramArrayOfByte);
      localBufferedOutputStream.flush();
      localBufferedOutputStream.close();
      return;
    }
    catch (FileNotFoundException paramArrayOfByte)
    {
      for (;;)
      {
        paramArrayOfByte.printStackTrace();
      }
    }
    catch (IOException paramArrayOfByte)
    {
      for (;;)
      {
        paramArrayOfByte.printStackTrace();
      }
    }
  }
  
  public static void mergeFiles(Vector<String> paramVector, File paramFile)
  {
    Vector localVector = new Vector();
    int j = 0;
    int i = 0;
    byte[] arrayOfByte;
    if (i >= paramVector.size())
    {
      arrayOfByte = new byte[j];
      paramVector = ByteBuffer.wrap(arrayOfByte);
    }
    for (i = 0;; i++)
    {
      if (i >= localVector.size())
      {
        getFileFromByteArray(arrayOfByte, paramFile);
        return;
        arrayOfByte = getByteArrayFromFile(new File((String)paramVector.get(i)));
        localVector.add(arrayOfByte);
        j += arrayOfByte.length;
        i++;
        break;
      }
      paramVector.put((byte[])localVector.get(i));
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\FileManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
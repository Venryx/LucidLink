package com.resmed.refresh.packets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public abstract class PacketsByteValuesReader
{
  public static final int BIO_IGNORE_PATTERN = 64250;
  public static final int ENV_IGNORE_PATTERN = 5376;
  
  public static int decompress_light(int paramInt)
  {
    if (paramInt < 64) {}
    for (;;)
    {
      return paramInt;
      if (paramInt < 96) {
        paramInt = (paramInt - 64) * 2 + 64;
      } else if (paramInt < 128) {
        paramInt = (paramInt - 96) * 4 + 128;
      } else if (paramInt < 160) {
        paramInt = (paramInt - 128) * 8 + 256;
      } else if (paramInt < 192) {
        paramInt = (paramInt - 160) * 16 + 512;
      } else if (paramInt < 240) {
        paramInt = (paramInt - 192) * 64 + 1024;
      } else if (paramInt < 255) {
        paramInt = (paramInt - 240) * 128 + 4096;
      } else {
        paramInt = 6000;
      }
    }
  }
  
  public static int getStoreLocalBio(byte[] paramArrayOfByte)
  {
    int i = 0;
    if (paramArrayOfByte.length > 7) {
      i = paramArrayOfByte[0] & 0xFF | (paramArrayOfByte[1] & 0xFF) << 8 | (paramArrayOfByte[2] & 0xFF) << 16 | (paramArrayOfByte[3] & 0xFF) << 24;
    }
    return i;
  }
  
  public static int getStoreLocalEnv(byte[] paramArrayOfByte)
  {
    int i = 0;
    if (paramArrayOfByte.length > 7) {
      i = paramArrayOfByte[4] & 0xFF | (paramArrayOfByte[5] & 0xFF) << 8 | (paramArrayOfByte[6] & 0xFF) << 16 | (paramArrayOfByte[7] & 0xFF) << 24;
    }
    return i;
  }
  
  public static int[] readBioData(byte[] paramArrayOfByte)
  {
    int[] arrayOfInt = new int[2];
    if (paramArrayOfByte.length > 1)
    {
      paramArrayOfByte[0] &= 0xFF;
      arrayOfInt[0] |= (paramArrayOfByte[1] & 0xFF) << 8;
      if (paramArrayOfByte.length > 3)
      {
        arrayOfInt[1] = (paramArrayOfByte[2] & 0xFF);
        arrayOfInt[1] = ((paramArrayOfByte[3] & 0xFF) << 8);
      }
    }
    return arrayOfInt;
  }
  
  public static int[][] readBioData(byte[] paramArrayOfByte, int paramInt)
  {
    int[][] arrayOfInt = (int[][])Array.newInstance(Integer.TYPE, new int[] { 2, paramInt - 8 });
    int k = 0;
    paramInt = 0;
    int j = paramArrayOfByte.length - 1;
    label39:
    int i;
    if (j < 0)
    {
      j = 0;
      i = k;
      label44:
      if (j < paramArrayOfByte.length - paramInt) {
        break label102;
      }
      paramArrayOfByte = (int[][])Array.newInstance(Integer.TYPE, new int[] { 2, i });
    }
    for (paramInt = 0;; paramInt++)
    {
      label102:
      int m;
      if (paramInt >= i)
      {
        return paramArrayOfByte;
        i = paramInt + 1;
        paramInt = i;
        if (paramArrayOfByte[j] != 0) {
          break label39;
        }
        j--;
        paramInt = i;
        break;
        m = 0;
        int n = paramArrayOfByte[j] & 0xFF | (paramArrayOfByte[(j + 1)] & 0xFF) << 8;
        k = m;
        try
        {
          if (paramArrayOfByte.length > j + 2)
          {
            k = m;
            if (paramArrayOfByte.length > j + 3) {
              k = paramArrayOfByte[(j + 2)] & 0xFF | (paramArrayOfByte[(j + 3)] & 0xFF) << 8;
            }
          }
          arrayOfInt[0][i] = n;
          arrayOfInt[1][i] = k;
          m = i;
          if (n != 64250)
          {
            m = i;
            if (k != 64250) {
              m = i + 1;
            }
          }
          j += 4;
        }
        catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
        {
          for (;;)
          {
            localArrayIndexOutOfBoundsException.printStackTrace();
            m = i;
          }
        }
        i = m;
        break label44;
      }
      paramArrayOfByte[0][paramInt] = arrayOfInt[0][paramInt];
      paramArrayOfByte[1][paramInt] = arrayOfInt[1][paramInt];
    }
  }
  
  public static int readIlluminanceValue(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length < 2) {}
    for (int i = 0;; i = decompress_light(paramArrayOfByte[1] & 0xFF)) {
      return i;
    }
  }
  
  public static int[] readIlluminanceValues(byte[] paramArrayOfByte)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if (i >= paramArrayOfByte.length) {
      paramArrayOfByte = new int[localArrayList.size()];
    }
    for (i = 0;; i++)
    {
      if (i >= localArrayList.size())
      {
        return paramArrayOfByte;
        if ((i % 2 != 0) && ((paramArrayOfByte[i] & 0xFF) != 250)) {
          localArrayList.add(Byte.valueOf(paramArrayOfByte[i]));
        }
        i++;
        break;
      }
      paramArrayOfByte[i] = Math.round(((Byte)localArrayList.get(i)).byteValue() & 0xFF);
    }
  }
  
  public static float readTemperatureValue(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length > 0) {}
    for (float f = (paramArrayOfByte[0] & 0xFF) / 4.0F;; f = 0.0F) {
      return f;
    }
  }
  
  public static float[] readTemperatureValues(byte[] paramArrayOfByte)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if (i >= paramArrayOfByte.length) {
      paramArrayOfByte = new float[localArrayList.size()];
    }
    for (i = 0;; i++)
    {
      if (i >= localArrayList.size())
      {
        return paramArrayOfByte;
        if ((i % 2 == 0) && ((paramArrayOfByte[i] & 0xFF) != 250)) {
          localArrayList.add(Byte.valueOf(paramArrayOfByte[i]));
        }
        i++;
        break;
      }
      paramArrayOfByte[i] = ((((Byte)localArrayList.get(i)).byteValue() & 0xFF) / 4.0F);
    }
  }
  
  public static void saveToEDF(byte[] paramArrayOfByte, FileOutputStream paramFileOutputStream)
  {
    try
    {
      paramFileOutputStream.write(paramArrayOfByte);
      paramFileOutputStream.close();
      return;
    }
    catch (IOException paramArrayOfByte)
    {
      for (;;)
      {
        paramArrayOfByte.printStackTrace();
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\packets\PacketsByteValuesReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.utils.audio;

class Bytes
{
  private static int determineSize(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt1 < paramInt3) || (paramInt1 > paramInt2)) {
      throw new IllegalArgumentException("amount of bytes to read cannot be smaller than " + paramInt3 + " or larger than array length. Amount is:" + paramInt1);
    }
    if (paramInt1 < paramInt2) {}
    while (paramInt1 % paramInt3 != 0)
    {
      throw new IllegalArgumentException("array size must be an order of " + paramInt3 + ". The size is:" + paramInt2);
      paramInt1 = paramInt2;
    }
    return paramInt1;
  }
  
  public static byte[] toByteArray(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    byte[] arrayOfByte;
    switch (paramInt2)
    {
    default: 
      throw new IllegalArgumentException("1,2,3 or 4 size values are allowed. size:" + paramInt2);
    case 1: 
      arrayOfByte = new byte[1];
      arrayOfByte[0] = ((byte)paramInt1);
    }
    for (;;)
    {
      return arrayOfByte;
      if (paramBoolean)
      {
        arrayOfByte = new byte[2];
        arrayOfByte[0] = ((byte)(paramInt1 >>> 8 & 0xFF));
        arrayOfByte[1] = ((byte)(paramInt1 & 0xFF));
      }
      else
      {
        arrayOfByte = new byte[2];
        arrayOfByte[0] = ((byte)(paramInt1 & 0xFF));
        arrayOfByte[1] = ((byte)(paramInt1 >>> 8 & 0xFF));
        continue;
        if (paramBoolean)
        {
          arrayOfByte = new byte[3];
          arrayOfByte[0] = ((byte)(paramInt1 >>> 16 & 0xFF));
          arrayOfByte[1] = ((byte)(paramInt1 >>> 8 & 0xFF));
          arrayOfByte[2] = ((byte)(paramInt1 & 0xFF));
        }
        else
        {
          arrayOfByte = new byte[3];
          arrayOfByte[0] = ((byte)(paramInt1 & 0xFF));
          arrayOfByte[1] = ((byte)(paramInt1 >>> 8 & 0xFF));
          arrayOfByte[2] = ((byte)(paramInt1 >>> 16 & 0xFF));
          continue;
          arrayOfByte = toByteArray(paramInt1, paramBoolean);
        }
      }
    }
  }
  
  public static byte[] toByteArray(int paramInt, boolean paramBoolean)
  {
    byte[] arrayOfByte = new byte[4];
    if (paramBoolean)
    {
      arrayOfByte[0] = ((byte)(paramInt >>> 24));
      arrayOfByte[1] = ((byte)(paramInt >>> 16 & 0xFF));
      arrayOfByte[2] = ((byte)(paramInt >>> 8 & 0xFF));
      arrayOfByte[3] = ((byte)(paramInt & 0xFF));
    }
    for (;;)
    {
      return arrayOfByte;
      arrayOfByte[0] = ((byte)(paramInt & 0xFF));
      arrayOfByte[1] = ((byte)(paramInt >>> 8 & 0xFF));
      arrayOfByte[2] = ((byte)(paramInt >>> 16 & 0xFF));
      arrayOfByte[3] = ((byte)(paramInt >>> 24));
    }
  }
  
  public static byte[] toByteArray(short paramShort, boolean paramBoolean)
  {
    byte[] arrayOfByte = new byte[2];
    if (paramBoolean)
    {
      arrayOfByte[0] = ((byte)(paramShort >>> 8));
      arrayOfByte[1] = ((byte)(paramShort & 0xFF));
    }
    for (;;)
    {
      return arrayOfByte;
      arrayOfByte[0] = ((byte)(paramShort & 0xFF));
      arrayOfByte[1] = ((byte)(paramShort >>> 8 & 0xFF));
    }
  }
  
  public static byte[] toByteArray(int... paramVarArgs)
  {
    byte[] arrayOfByte = new byte[paramVarArgs.length];
    for (int i = 0;; i++)
    {
      if (i >= paramVarArgs.length) {
        return arrayOfByte;
      }
      arrayOfByte[i] = ((byte)(paramVarArgs[i] & 0xFF));
    }
  }
  
  public static byte[] toByteArray(int[] paramArrayOfInt, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if ((paramInt2 < 1) || (paramInt2 > 4)) {
      throw new IllegalArgumentException("bytePerInteger parameter can only be 1,2,3 or 4. But it is:" + paramInt2);
    }
    if ((paramInt1 > paramArrayOfInt.length) || (paramInt1 < 0)) {
      throw new IllegalArgumentException("Amount cannot be negative or more than input array length. Amount:" + paramInt1);
    }
    byte[] arrayOfByte;
    if (paramInt1 < paramArrayOfInt.length) {
      arrayOfByte = new byte[paramInt1 * paramInt2];
    }
    for (int i = 0;; i++)
    {
      if (i >= paramInt1)
      {
        return arrayOfByte;
        paramInt1 = paramArrayOfInt.length;
        break;
      }
      System.arraycopy(toByteArray(paramArrayOfInt[i], paramInt2, paramBoolean), 0, arrayOfByte, i * paramInt2, paramInt2);
    }
  }
  
  public static byte[] toByteArray(short[] paramArrayOfShort, int paramInt, boolean paramBoolean)
  {
    if (paramInt < paramArrayOfShort.length) {}
    byte[] arrayOfByte;
    int k;
    for (;;)
    {
      arrayOfByte = new byte[paramInt * 2];
      k = 0;
      if (k < paramInt) {
        break;
      }
      return arrayOfByte;
      paramInt = paramArrayOfShort.length;
    }
    int j = (byte)(paramArrayOfShort[k] >>> 8);
    int i = (byte)(paramArrayOfShort[k] & 0xFF);
    if (paramBoolean)
    {
      arrayOfByte[(k * 2)] = j;
      arrayOfByte[(k * 2 + 1)] = i;
    }
    for (;;)
    {
      k++;
      break;
      arrayOfByte[(k * 2)] = i;
      arrayOfByte[(k * 2 + 1)] = j;
    }
  }
  
  public static int toInt(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (paramByte1 = paramByte1 << 24 & 0xFF000000 | paramByte2 << 16 & 0xFF0000 | paramByte3 << 8 & 0xFF00 | paramByte4 & 0xFF;; paramByte1 = paramByte4 << 24 & 0xFF000000 | paramByte3 << 16 & 0xFF0000 | paramByte2 << 8 & 0xFF00 | paramByte1 & 0xFF) {
      return paramByte1;
    }
  }
  
  public static int toInt(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    int i;
    switch (paramArrayOfByte.length)
    {
    default: 
      throw new IllegalArgumentException("1,2,3 or 4 byte arrays allowed. size:" + paramArrayOfByte.length);
    case 1: 
      i = paramArrayOfByte[0] & 0xFF;
    }
    for (;;)
    {
      return i;
      if (paramBoolean)
      {
        i = paramArrayOfByte[0] << 8 & 0xFF00 | paramArrayOfByte[1] & 0xFF;
      }
      else
      {
        i = paramArrayOfByte[1] << 8 & 0xFF00 | paramArrayOfByte[0] & 0xFF;
        continue;
        if (paramBoolean)
        {
          i = paramArrayOfByte[0] << 16 & 0xFF0000 | paramArrayOfByte[1] << 8 & 0xFF00 | paramArrayOfByte[2] & 0xFF;
        }
        else
        {
          i = paramArrayOfByte[2] << 16 & 0xFF0000 | paramArrayOfByte[1] << 8 & 0xFF00 | paramArrayOfByte[0] & 0xFF;
          continue;
          if (paramBoolean) {
            i = paramArrayOfByte[0] << 24 & 0xFF000000 | paramArrayOfByte[1] << 16 & 0xFF0000 | paramArrayOfByte[2] << 8 & 0xFF00 | paramArrayOfByte[3] & 0xFF;
          } else {
            i = paramArrayOfByte[3] << 24 & 0xFF000000 | paramArrayOfByte[2] << 16 & 0xFF0000 | paramArrayOfByte[1] << 8 & 0xFF00 | paramArrayOfByte[0] & 0xFF;
          }
        }
      }
    }
  }
  
  public static int[] toIntArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int k = determineSize(paramInt1, paramArrayOfByte.length, paramInt2);
    int[] arrayOfInt = new int[k / paramInt2];
    byte[] arrayOfByte = new byte[paramInt2];
    int i = 0;
    paramInt1 = 0;
    if (i >= k) {
      return arrayOfInt;
    }
    System.arraycopy(paramArrayOfByte, i, arrayOfByte, 0, paramInt2);
    int j;
    if (paramBoolean)
    {
      j = paramInt1 + 1;
      arrayOfInt[paramInt1] = toInt(arrayOfByte, true);
    }
    for (paramInt1 = j;; paramInt1 = j)
    {
      i += paramInt2;
      break;
      j = paramInt1 + 1;
      arrayOfInt[paramInt1] = toInt(arrayOfByte, false);
    }
  }
  
  public static int[] toIntArray(byte[] paramArrayOfByte, int paramInt, boolean paramBoolean)
  {
    int k = determineSize(paramInt, paramArrayOfByte.length, 4);
    int[] arrayOfInt = new int[k / 4];
    int i = 0;
    paramInt = 0;
    if (i >= k) {
      return arrayOfInt;
    }
    int j;
    if (paramBoolean)
    {
      j = paramInt + 1;
      arrayOfInt[paramInt] = toInt(paramArrayOfByte[i], paramArrayOfByte[(i + 1)], paramArrayOfByte[(i + 2)], paramArrayOfByte[(i + 3)], true);
    }
    for (paramInt = j;; paramInt = j)
    {
      i += 4;
      break;
      j = paramInt + 1;
      arrayOfInt[paramInt] = toInt(paramArrayOfByte[(i + 3)], paramArrayOfByte[(i + 2)], paramArrayOfByte[(i + 1)], paramArrayOfByte[i], true);
    }
  }
  
  public static short[] toShortArray(byte[] paramArrayOfByte, int paramInt, boolean paramBoolean)
  {
    int k = determineSize(paramInt, paramArrayOfByte.length, 2);
    short[] arrayOfShort = new short[k / 2];
    int i = 0;
    paramInt = 0;
    if (i >= k) {
      return arrayOfShort;
    }
    int j;
    if (paramBoolean)
    {
      j = paramInt + 1;
      arrayOfShort[paramInt] = ((short)(paramArrayOfByte[i] << 8 & 0xFF00 | paramArrayOfByte[(i + 1)] & 0xFF));
    }
    for (paramInt = j;; paramInt = j)
    {
      i += 2;
      break;
      j = paramInt + 1;
      arrayOfShort[paramInt] = ((short)(paramArrayOfByte[(i + 1)] << 8 & 0xFF00 | paramArrayOfByte[i] & 0xFF));
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\audio\Bytes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
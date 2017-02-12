package com.resmed.refresh.utils.audio;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

class IOs
{
  public static void closeSilently(Closeable... paramVarArgs)
  {
    if (paramVarArgs == null) {
      return;
    }
    int j = paramVarArgs.length;
    int i = 0;
    while (i < j)
    {
      Closeable localCloseable = paramVarArgs[i];
      if (localCloseable != null) {}
      try
      {
        localCloseable.close();
        i++;
      }
      catch (IOException localIOException)
      {
        for (;;)
        {
          System.err.println("IO Exception during closing stream (" + localCloseable + ")." + localIOException);
        }
      }
    }
  }
  
  public static void copy(InputStream paramInputStream, OutputStream paramOutputStream)
    throws IOException
  {
    copy(paramInputStream, paramOutputStream, false);
  }
  
  /* Error */
  static void copy(InputStream paramInputStream, OutputStream paramOutputStream, boolean paramBoolean)
    throws IOException
  {
    // Byte code:
    //   0: sipush 4096
    //   3: newarray <illegal type>
    //   5: astore 4
    //   7: aload_0
    //   8: aload 4
    //   10: invokevirtual 61	java/io/InputStream:read	([B)I
    //   13: istore_3
    //   14: iload_3
    //   15: iconst_m1
    //   16: if_icmpne +30 -> 46
    //   19: iconst_1
    //   20: anewarray 15	java/io/Closeable
    //   23: dup
    //   24: iconst_0
    //   25: aload_0
    //   26: aastore
    //   27: invokestatic 63	com/resmed/refresh/utils/audio/IOs:closeSilently	([Ljava/io/Closeable;)V
    //   30: iload_2
    //   31: ifne +14 -> 45
    //   34: iconst_1
    //   35: anewarray 15	java/io/Closeable
    //   38: dup
    //   39: iconst_0
    //   40: aload_1
    //   41: aastore
    //   42: invokestatic 63	com/resmed/refresh/utils/audio/IOs:closeSilently	([Ljava/io/Closeable;)V
    //   45: return
    //   46: aload_1
    //   47: aload 4
    //   49: iconst_0
    //   50: iload_3
    //   51: invokevirtual 69	java/io/OutputStream:write	([BII)V
    //   54: goto -47 -> 7
    //   57: astore 4
    //   59: iconst_1
    //   60: anewarray 15	java/io/Closeable
    //   63: dup
    //   64: iconst_0
    //   65: aload_0
    //   66: aastore
    //   67: invokestatic 63	com/resmed/refresh/utils/audio/IOs:closeSilently	([Ljava/io/Closeable;)V
    //   70: iload_2
    //   71: ifne +14 -> 85
    //   74: iconst_1
    //   75: anewarray 15	java/io/Closeable
    //   78: dup
    //   79: iconst_0
    //   80: aload_1
    //   81: aastore
    //   82: invokestatic 63	com/resmed/refresh/utils/audio/IOs:closeSilently	([Ljava/io/Closeable;)V
    //   85: aload 4
    //   87: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	88	0	paramInputStream	InputStream
    //   0	88	1	paramOutputStream	OutputStream
    //   0	88	2	paramBoolean	boolean
    //   13	38	3	i	int
    //   5	43	4	arrayOfByte	byte[]
    //   57	29	4	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   0	7	57	finally
    //   7	14	57	finally
    //   46	54	57	finally
  }
  
  public static byte[] readAsByteArray(InputStream paramInputStream)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    if (paramInputStream == null) {
      try
      {
        NullPointerException localNullPointerException = new java.lang.NullPointerException;
        localNullPointerException.<init>("input stream cannot be null.");
        throw localNullPointerException;
      }
      finally
      {
        closeSilently(new Closeable[] { paramInputStream, localByteArrayOutputStream });
      }
    }
    byte[] arrayOfByte = new byte['က'];
    for (;;)
    {
      int i = paramInputStream.read(arrayOfByte, 0, arrayOfByte.length);
      if (i == -1)
      {
        arrayOfByte = localByteArrayOutputStream.toByteArray();
        closeSilently(new Closeable[] { paramInputStream, localByteArrayOutputStream });
        return arrayOfByte;
      }
      localByteArrayOutputStream.write(arrayOfByte, 0, i);
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
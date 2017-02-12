package com.resmed.refresh.utils.audio;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class RiffHeaderData
{
  public static final int PCM_RIFF_HEADER_SIZE = 44;
  public static final int RIFF_CHUNK_SIZE_INDEX = 4;
  public static final int RIFF_SUBCHUNK2_SIZE_INDEX = 40;
  private final PcmAudioFormat format;
  private final int totalSamplesInByte;
  
  public RiffHeaderData(PcmAudioFormat paramPcmAudioFormat, int paramInt)
  {
    this.format = paramPcmAudioFormat;
    this.totalSamplesInByte = paramInt;
  }
  
  /* Error */
  public RiffHeaderData(DataInputStream paramDataInputStream)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 19	java/lang/Object:<init>	()V
    //   4: iconst_4
    //   5: newarray <illegal type>
    //   7: astore 5
    //   9: iconst_2
    //   10: newarray <illegal type>
    //   12: astore 6
    //   14: aload_1
    //   15: bipush 22
    //   17: invokevirtual 33	java/io/DataInputStream:skipBytes	(I)I
    //   20: pop
    //   21: aload_1
    //   22: aload 6
    //   24: invokevirtual 37	java/io/DataInputStream:readFully	([B)V
    //   27: aload 6
    //   29: iconst_0
    //   30: invokestatic 43	com/resmed/refresh/utils/audio/Bytes:toInt	([BZ)I
    //   33: istore_2
    //   34: aload_1
    //   35: aload 5
    //   37: invokevirtual 37	java/io/DataInputStream:readFully	([B)V
    //   40: aload 5
    //   42: iconst_0
    //   43: invokestatic 43	com/resmed/refresh/utils/audio/Bytes:toInt	([BZ)I
    //   46: istore 4
    //   48: aload_1
    //   49: bipush 6
    //   51: invokevirtual 33	java/io/DataInputStream:skipBytes	(I)I
    //   54: pop
    //   55: aload_1
    //   56: aload 6
    //   58: invokevirtual 37	java/io/DataInputStream:readFully	([B)V
    //   61: aload 6
    //   63: iconst_0
    //   64: invokestatic 43	com/resmed/refresh/utils/audio/Bytes:toInt	([BZ)I
    //   67: istore_3
    //   68: aload_1
    //   69: iconst_4
    //   70: invokevirtual 33	java/io/DataInputStream:skipBytes	(I)I
    //   73: pop
    //   74: aload_1
    //   75: aload 5
    //   77: invokevirtual 37	java/io/DataInputStream:readFully	([B)V
    //   80: aload_0
    //   81: aload 5
    //   83: iconst_0
    //   84: invokestatic 43	com/resmed/refresh/utils/audio/Bytes:toInt	([BZ)I
    //   87: putfield 23	com/resmed/refresh/utils/audio/RiffHeaderData:totalSamplesInByte	I
    //   90: new 45	com/resmed/refresh/utils/audio/WavAudioFormat$Builder
    //   93: astore 5
    //   95: aload 5
    //   97: invokespecial 46	com/resmed/refresh/utils/audio/WavAudioFormat$Builder:<init>	()V
    //   100: aload_0
    //   101: aload 5
    //   103: iload_2
    //   104: invokevirtual 50	com/resmed/refresh/utils/audio/WavAudioFormat$Builder:channels	(I)Lcom/resmed/refresh/utils/audio/WavAudioFormat$Builder;
    //   107: iload 4
    //   109: invokevirtual 53	com/resmed/refresh/utils/audio/WavAudioFormat$Builder:sampleRate	(I)Lcom/resmed/refresh/utils/audio/WavAudioFormat$Builder;
    //   112: iload_3
    //   113: invokevirtual 56	com/resmed/refresh/utils/audio/WavAudioFormat$Builder:sampleSizeInBits	(I)Lcom/resmed/refresh/utils/audio/WavAudioFormat$Builder;
    //   116: invokevirtual 60	com/resmed/refresh/utils/audio/WavAudioFormat$Builder:build	()Lcom/resmed/refresh/utils/audio/WavAudioFormat;
    //   119: putfield 21	com/resmed/refresh/utils/audio/RiffHeaderData:format	Lcom/resmed/refresh/utils/audio/PcmAudioFormat;
    //   122: iconst_1
    //   123: anewarray 62	java/io/Closeable
    //   126: dup
    //   127: iconst_0
    //   128: aload_1
    //   129: aastore
    //   130: invokestatic 68	com/resmed/refresh/utils/audio/IOs:closeSilently	([Ljava/io/Closeable;)V
    //   133: return
    //   134: astore 5
    //   136: iconst_1
    //   137: anewarray 62	java/io/Closeable
    //   140: dup
    //   141: iconst_0
    //   142: aload_1
    //   143: aastore
    //   144: invokestatic 68	com/resmed/refresh/utils/audio/IOs:closeSilently	([Ljava/io/Closeable;)V
    //   147: aload 5
    //   149: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	150	0	this	RiffHeaderData
    //   0	150	1	paramDataInputStream	DataInputStream
    //   33	71	2	i	int
    //   67	46	3	j	int
    //   46	62	4	k	int
    //   7	95	5	localObject1	Object
    //   134	14	5	localObject2	Object
    //   12	50	6	arrayOfByte	byte[]
    // Exception table:
    //   from	to	target	type
    //   4	122	134	finally
  }
  
  public RiffHeaderData(File paramFile)
    throws IOException
  {
    this(new DataInputStream(new FileInputStream(paramFile)));
  }
  
  /* Error */
  public byte[] asByteArray()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 5
    //   3: aconst_null
    //   4: astore 6
    //   6: aload 5
    //   8: astore_3
    //   9: new 83	java/io/ByteArrayOutputStream
    //   12: astore 4
    //   14: aload 5
    //   16: astore_3
    //   17: aload 4
    //   19: invokespecial 84	java/io/ByteArrayOutputStream:<init>	()V
    //   22: aload 4
    //   24: ldc 85
    //   26: iconst_1
    //   27: invokestatic 89	com/resmed/refresh/utils/audio/Bytes:toByteArray	(IZ)[B
    //   30: invokevirtual 92	java/io/ByteArrayOutputStream:write	([B)V
    //   33: aload 4
    //   35: aload_0
    //   36: getfield 23	com/resmed/refresh/utils/audio/RiffHeaderData:totalSamplesInByte	I
    //   39: bipush 36
    //   41: iadd
    //   42: iconst_0
    //   43: invokestatic 89	com/resmed/refresh/utils/audio/Bytes:toByteArray	(IZ)[B
    //   46: invokevirtual 92	java/io/ByteArrayOutputStream:write	([B)V
    //   49: aload 4
    //   51: ldc 93
    //   53: iconst_1
    //   54: invokestatic 89	com/resmed/refresh/utils/audio/Bytes:toByteArray	(IZ)[B
    //   57: invokevirtual 92	java/io/ByteArrayOutputStream:write	([B)V
    //   60: aload 4
    //   62: ldc 94
    //   64: iconst_1
    //   65: invokestatic 89	com/resmed/refresh/utils/audio/Bytes:toByteArray	(IZ)[B
    //   68: invokevirtual 92	java/io/ByteArrayOutputStream:write	([B)V
    //   71: aload 4
    //   73: bipush 16
    //   75: iconst_0
    //   76: invokestatic 89	com/resmed/refresh/utils/audio/Bytes:toByteArray	(IZ)[B
    //   79: invokevirtual 92	java/io/ByteArrayOutputStream:write	([B)V
    //   82: aload 4
    //   84: iconst_1
    //   85: iconst_0
    //   86: invokestatic 97	com/resmed/refresh/utils/audio/Bytes:toByteArray	(SZ)[B
    //   89: invokevirtual 92	java/io/ByteArrayOutputStream:write	([B)V
    //   92: aload_0
    //   93: getfield 21	com/resmed/refresh/utils/audio/RiffHeaderData:format	Lcom/resmed/refresh/utils/audio/PcmAudioFormat;
    //   96: invokevirtual 103	com/resmed/refresh/utils/audio/PcmAudioFormat:getChannels	()I
    //   99: istore_1
    //   100: aload 4
    //   102: iload_1
    //   103: i2s
    //   104: iconst_0
    //   105: invokestatic 97	com/resmed/refresh/utils/audio/Bytes:toByteArray	(SZ)[B
    //   108: invokevirtual 92	java/io/ByteArrayOutputStream:write	([B)V
    //   111: aload_0
    //   112: getfield 21	com/resmed/refresh/utils/audio/RiffHeaderData:format	Lcom/resmed/refresh/utils/audio/PcmAudioFormat;
    //   115: invokevirtual 106	com/resmed/refresh/utils/audio/PcmAudioFormat:getSampleRate	()I
    //   118: istore_2
    //   119: aload 4
    //   121: iload_2
    //   122: iconst_0
    //   123: invokestatic 89	com/resmed/refresh/utils/audio/Bytes:toByteArray	(IZ)[B
    //   126: invokevirtual 92	java/io/ByteArrayOutputStream:write	([B)V
    //   129: aload 4
    //   131: iload_1
    //   132: iload_2
    //   133: imul
    //   134: aload_0
    //   135: getfield 21	com/resmed/refresh/utils/audio/RiffHeaderData:format	Lcom/resmed/refresh/utils/audio/PcmAudioFormat;
    //   138: invokevirtual 109	com/resmed/refresh/utils/audio/PcmAudioFormat:getBytePerSample	()I
    //   141: imul
    //   142: iconst_0
    //   143: invokestatic 89	com/resmed/refresh/utils/audio/Bytes:toByteArray	(IZ)[B
    //   146: invokevirtual 92	java/io/ByteArrayOutputStream:write	([B)V
    //   149: aload 4
    //   151: aload_0
    //   152: getfield 21	com/resmed/refresh/utils/audio/RiffHeaderData:format	Lcom/resmed/refresh/utils/audio/PcmAudioFormat;
    //   155: invokevirtual 109	com/resmed/refresh/utils/audio/PcmAudioFormat:getBytePerSample	()I
    //   158: iload_1
    //   159: imul
    //   160: i2s
    //   161: iconst_0
    //   162: invokestatic 97	com/resmed/refresh/utils/audio/Bytes:toByteArray	(SZ)[B
    //   165: invokevirtual 92	java/io/ByteArrayOutputStream:write	([B)V
    //   168: aload 4
    //   170: aload_0
    //   171: getfield 21	com/resmed/refresh/utils/audio/RiffHeaderData:format	Lcom/resmed/refresh/utils/audio/PcmAudioFormat;
    //   174: invokevirtual 112	com/resmed/refresh/utils/audio/PcmAudioFormat:getSampleSizeInBits	()I
    //   177: i2s
    //   178: iconst_0
    //   179: invokestatic 97	com/resmed/refresh/utils/audio/Bytes:toByteArray	(SZ)[B
    //   182: invokevirtual 92	java/io/ByteArrayOutputStream:write	([B)V
    //   185: aload 4
    //   187: ldc 113
    //   189: iconst_1
    //   190: invokestatic 89	com/resmed/refresh/utils/audio/Bytes:toByteArray	(IZ)[B
    //   193: invokevirtual 92	java/io/ByteArrayOutputStream:write	([B)V
    //   196: aload 4
    //   198: aload_0
    //   199: getfield 23	com/resmed/refresh/utils/audio/RiffHeaderData:totalSamplesInByte	I
    //   202: iconst_0
    //   203: invokestatic 89	com/resmed/refresh/utils/audio/Bytes:toByteArray	(IZ)[B
    //   206: invokevirtual 92	java/io/ByteArrayOutputStream:write	([B)V
    //   209: aload 4
    //   211: invokevirtual 115	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   214: astore_3
    //   215: iconst_1
    //   216: anewarray 62	java/io/Closeable
    //   219: dup
    //   220: iconst_0
    //   221: aload 4
    //   223: aastore
    //   224: invokestatic 68	com/resmed/refresh/utils/audio/IOs:closeSilently	([Ljava/io/Closeable;)V
    //   227: aload_3
    //   228: areturn
    //   229: astore 5
    //   231: aload 6
    //   233: astore 4
    //   235: aload 4
    //   237: astore_3
    //   238: aload 5
    //   240: invokevirtual 118	java/io/IOException:printStackTrace	()V
    //   243: aload 4
    //   245: astore_3
    //   246: iconst_0
    //   247: newarray <illegal type>
    //   249: astore 5
    //   251: iconst_1
    //   252: anewarray 62	java/io/Closeable
    //   255: dup
    //   256: iconst_0
    //   257: aload 4
    //   259: aastore
    //   260: invokestatic 68	com/resmed/refresh/utils/audio/IOs:closeSilently	([Ljava/io/Closeable;)V
    //   263: aload 5
    //   265: astore_3
    //   266: goto -39 -> 227
    //   269: astore 5
    //   271: aload_3
    //   272: astore 4
    //   274: iconst_1
    //   275: anewarray 62	java/io/Closeable
    //   278: dup
    //   279: iconst_0
    //   280: aload 4
    //   282: aastore
    //   283: invokestatic 68	com/resmed/refresh/utils/audio/IOs:closeSilently	([Ljava/io/Closeable;)V
    //   286: aload 5
    //   288: athrow
    //   289: astore_3
    //   290: aload_3
    //   291: astore 5
    //   293: goto -19 -> 274
    //   296: astore_3
    //   297: aload_3
    //   298: astore 5
    //   300: goto -65 -> 235
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	303	0	this	RiffHeaderData
    //   99	61	1	i	int
    //   118	16	2	j	int
    //   8	264	3	localObject1	Object
    //   289	2	3	localObject2	Object
    //   296	2	3	localIOException1	IOException
    //   12	269	4	localObject3	Object
    //   1	14	5	localObject4	Object
    //   229	10	5	localIOException2	IOException
    //   249	15	5	arrayOfByte	byte[]
    //   269	18	5	localObject5	Object
    //   291	8	5	localObject6	Object
    //   4	228	6	localObject7	Object
    // Exception table:
    //   from	to	target	type
    //   9	14	229	java/io/IOException
    //   17	22	229	java/io/IOException
    //   9	14	269	finally
    //   17	22	269	finally
    //   238	243	269	finally
    //   246	251	269	finally
    //   22	215	289	finally
    //   22	215	296	java/io/IOException
  }
  
  public PcmAudioFormat getFormat()
  {
    return this.format;
  }
  
  public int getSampleCount()
  {
    return this.totalSamplesInByte / this.format.getBytePerSample();
  }
  
  public int getTotalSamplesInByte()
  {
    return this.totalSamplesInByte;
  }
  
  public double timeSeconds()
  {
    return this.totalSamplesInByte / this.format.getBytePerSample() / this.format.getSampleRate();
  }
  
  public String toString()
  {
    return "[ Format: " + this.format.toString() + " , totalSamplesInByte:" + this.totalSamplesInByte + "]";
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\audio\RiffHeaderData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
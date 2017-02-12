package com.resmed.refresh.utils;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import com.google.gson.Gson;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BluetoothDataSerializeUtil
{
  public static boolean clearJSONFile(Context paramContext)
  {
    new File(RefreshTools.getFilesPath(), paramContext.getString(2131165342)).delete();
    return true;
  }
  
  public static boolean deleteBulkDataBioFile(Context paramContext)
  {
    return new File(RefreshTools.getFilesPath(), paramContext.getString(2131165343)).delete();
  }
  
  public static boolean deleteJsonFile(Context paramContext)
  {
    try
    {
      File localFile1 = RefreshTools.getFilesPath();
      File localFile2 = new java/io/File;
      localFile2.<init>(localFile1, paramContext.getString(2131165342));
      localFile2.delete();
      bool = true;
    }
    catch (Exception paramContext)
    {
      for (;;)
      {
        paramContext.printStackTrace();
        boolean bool = false;
      }
    }
    return bool;
  }
  
  /* Error */
  public static int[][] readBulkBioDataFile(Context paramContext)
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: invokestatic 19	com/resmed/refresh/utils/RefreshTools:getFilesPath	()Ljava/io/File;
    //   6: astore 5
    //   8: new 13	java/io/File
    //   11: astore 4
    //   13: aload 4
    //   15: aload 5
    //   17: aload_0
    //   18: ldc 35
    //   20: invokevirtual 26	android/content/Context:getString	(I)Ljava/lang/String;
    //   23: invokespecial 29	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   26: new 49	java/util/ArrayList
    //   29: astore 9
    //   31: aload 9
    //   33: invokespecial 50	java/util/ArrayList:<init>	()V
    //   36: new 49	java/util/ArrayList
    //   39: astore 8
    //   41: aload 8
    //   43: invokespecial 50	java/util/ArrayList:<init>	()V
    //   46: aconst_null
    //   47: astore 6
    //   49: aconst_null
    //   50: astore 5
    //   52: aconst_null
    //   53: astore 7
    //   55: aload 5
    //   57: astore_0
    //   58: new 52	java/io/FileInputStream
    //   61: astore 10
    //   63: aload 5
    //   65: astore_0
    //   66: aload 10
    //   68: aload 4
    //   70: invokespecial 55	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   73: aload 5
    //   75: astore_0
    //   76: new 57	java/io/BufferedReader
    //   79: astore 4
    //   81: aload 5
    //   83: astore_0
    //   84: new 59	java/io/InputStreamReader
    //   87: astore 11
    //   89: aload 5
    //   91: astore_0
    //   92: aload 11
    //   94: aload 10
    //   96: invokespecial 62	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   99: aload 5
    //   101: astore_0
    //   102: aload 4
    //   104: aload 11
    //   106: invokespecial 65	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   109: iconst_0
    //   110: istore_1
    //   111: aload 4
    //   113: invokevirtual 69	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   116: astore_0
    //   117: aload_0
    //   118: ifnonnull +89 -> 207
    //   121: aload 4
    //   123: ifnull +241 -> 364
    //   126: aload 4
    //   128: invokevirtual 72	java/io/BufferedReader:close	()V
    //   131: aload 9
    //   133: invokeinterface 78 1 0
    //   138: newarray <illegal type>
    //   140: astore_0
    //   141: aload 8
    //   143: invokeinterface 78 1 0
    //   148: newarray <illegal type>
    //   150: astore 4
    //   152: iconst_0
    //   153: istore_1
    //   154: iload_1
    //   155: aload 9
    //   157: invokeinterface 78 1 0
    //   162: if_icmplt +205 -> 367
    //   165: aload_0
    //   166: arraylength
    //   167: istore_1
    //   168: getstatic 84	java/lang/Integer:TYPE	Ljava/lang/Class;
    //   171: iconst_2
    //   172: newarray <illegal type>
    //   174: dup
    //   175: iconst_0
    //   176: iconst_2
    //   177: iastore
    //   178: dup
    //   179: iconst_1
    //   180: iload_1
    //   181: iastore
    //   182: invokestatic 90	java/lang/reflect/Array:newInstance	(Ljava/lang/Class;[I)Ljava/lang/Object;
    //   185: checkcast 92	[[I
    //   188: astore 5
    //   190: aload 5
    //   192: iconst_0
    //   193: aload_0
    //   194: aastore
    //   195: aload 5
    //   197: iconst_1
    //   198: aload 4
    //   200: aastore
    //   201: ldc 2
    //   203: monitorexit
    //   204: aload 5
    //   206: areturn
    //   207: iinc 1 1
    //   210: aload_0
    //   211: invokevirtual 97	java/lang/String:trim	()Ljava/lang/String;
    //   214: ldc 99
    //   216: invokevirtual 103	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   219: astore_0
    //   220: aload_0
    //   221: iconst_0
    //   222: aaload
    //   223: invokestatic 107	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   226: istore_2
    //   227: aload_0
    //   228: iconst_1
    //   229: aaload
    //   230: invokestatic 107	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   233: istore_3
    //   234: aload 9
    //   236: iload_2
    //   237: invokestatic 111	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   240: invokeinterface 115 2 0
    //   245: pop
    //   246: aload 8
    //   248: iload_3
    //   249: invokestatic 111	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   252: invokeinterface 115 2 0
    //   257: pop
    //   258: goto -147 -> 111
    //   261: astore 5
    //   263: aload 4
    //   265: astore_0
    //   266: aload 5
    //   268: invokevirtual 116	java/io/FileNotFoundException:printStackTrace	()V
    //   271: aload 4
    //   273: ifnull -142 -> 131
    //   276: aload 4
    //   278: invokevirtual 72	java/io/BufferedReader:close	()V
    //   281: goto -150 -> 131
    //   284: astore_0
    //   285: aload_0
    //   286: invokevirtual 117	java/io/IOException:printStackTrace	()V
    //   289: goto -158 -> 131
    //   292: astore_0
    //   293: ldc 2
    //   295: monitorexit
    //   296: aload_0
    //   297: athrow
    //   298: astore 5
    //   300: aload 6
    //   302: astore 4
    //   304: aload 4
    //   306: astore_0
    //   307: aload 5
    //   309: invokevirtual 117	java/io/IOException:printStackTrace	()V
    //   312: aload 4
    //   314: ifnull -183 -> 131
    //   317: aload 4
    //   319: invokevirtual 72	java/io/BufferedReader:close	()V
    //   322: goto -191 -> 131
    //   325: astore_0
    //   326: aload_0
    //   327: invokevirtual 117	java/io/IOException:printStackTrace	()V
    //   330: goto -199 -> 131
    //   333: astore 4
    //   335: aload_0
    //   336: astore 5
    //   338: aload 5
    //   340: ifnull +8 -> 348
    //   343: aload 5
    //   345: invokevirtual 72	java/io/BufferedReader:close	()V
    //   348: aload 4
    //   350: athrow
    //   351: astore_0
    //   352: aload_0
    //   353: invokevirtual 117	java/io/IOException:printStackTrace	()V
    //   356: goto -8 -> 348
    //   359: astore_0
    //   360: aload_0
    //   361: invokevirtual 117	java/io/IOException:printStackTrace	()V
    //   364: goto -233 -> 131
    //   367: aload_0
    //   368: iload_1
    //   369: aload 9
    //   371: iload_1
    //   372: invokeinterface 121 2 0
    //   377: checkcast 80	java/lang/Integer
    //   380: invokevirtual 124	java/lang/Integer:intValue	()I
    //   383: iastore
    //   384: aload 4
    //   386: iload_1
    //   387: aload 8
    //   389: iload_1
    //   390: invokeinterface 121 2 0
    //   395: checkcast 80	java/lang/Integer
    //   398: invokevirtual 124	java/lang/Integer:intValue	()I
    //   401: iastore
    //   402: iinc 1 1
    //   405: goto -251 -> 154
    //   408: astore_0
    //   409: aload 4
    //   411: astore 5
    //   413: aload_0
    //   414: astore 4
    //   416: goto -78 -> 338
    //   419: astore_0
    //   420: aload_0
    //   421: astore 5
    //   423: goto -119 -> 304
    //   426: astore 5
    //   428: aload 7
    //   430: astore 4
    //   432: goto -169 -> 263
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	435	0	paramContext	Context
    //   110	293	1	i	int
    //   226	11	2	j	int
    //   233	16	3	k	int
    //   11	307	4	localObject1	Object
    //   333	77	4	localObject2	Object
    //   414	17	4	localObject3	Object
    //   6	199	5	localObject4	Object
    //   261	6	5	localFileNotFoundException1	FileNotFoundException
    //   298	10	5	localIOException	IOException
    //   336	86	5	localObject5	Object
    //   426	1	5	localFileNotFoundException2	FileNotFoundException
    //   47	254	6	localObject6	Object
    //   53	376	7	localObject7	Object
    //   39	349	8	localArrayList1	java.util.ArrayList
    //   29	341	9	localArrayList2	java.util.ArrayList
    //   61	34	10	localFileInputStream	java.io.FileInputStream
    //   87	18	11	localInputStreamReader	java.io.InputStreamReader
    // Exception table:
    //   from	to	target	type
    //   111	117	261	java/io/FileNotFoundException
    //   210	258	261	java/io/FileNotFoundException
    //   276	281	284	java/io/IOException
    //   3	46	292	finally
    //   126	131	292	finally
    //   131	152	292	finally
    //   154	190	292	finally
    //   276	281	292	finally
    //   285	289	292	finally
    //   317	322	292	finally
    //   326	330	292	finally
    //   343	348	292	finally
    //   348	351	292	finally
    //   352	356	292	finally
    //   360	364	292	finally
    //   367	402	292	finally
    //   58	63	298	java/io/IOException
    //   66	73	298	java/io/IOException
    //   76	81	298	java/io/IOException
    //   84	89	298	java/io/IOException
    //   92	99	298	java/io/IOException
    //   102	109	298	java/io/IOException
    //   317	322	325	java/io/IOException
    //   58	63	333	finally
    //   66	73	333	finally
    //   76	81	333	finally
    //   84	89	333	finally
    //   92	99	333	finally
    //   102	109	333	finally
    //   266	271	333	finally
    //   307	312	333	finally
    //   343	348	351	java/io/IOException
    //   126	131	359	java/io/IOException
    //   111	117	408	finally
    //   210	258	408	finally
    //   111	117	419	java/io/IOException
    //   210	258	419	java/io/IOException
    //   58	63	426	java/io/FileNotFoundException
    //   66	73	426	java/io/FileNotFoundException
    //   76	81	426	java/io/FileNotFoundException
    //   84	89	426	java/io/FileNotFoundException
    //   92	99	426	java/io/FileNotFoundException
    //   102	109	426	java/io/FileNotFoundException
  }
  
  /* Error */
  public static BluetoothDevice readJsonFile(Context paramContext)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 4
    //   3: aconst_null
    //   4: astore 5
    //   6: aconst_null
    //   7: astore 6
    //   9: aconst_null
    //   10: astore_3
    //   11: invokestatic 19	com/resmed/refresh/utils/RefreshTools:getFilesPath	()Ljava/io/File;
    //   14: astore 8
    //   16: aload 6
    //   18: astore_2
    //   19: new 13	java/io/File
    //   22: astore 7
    //   24: aload 6
    //   26: astore_2
    //   27: aload 7
    //   29: aload 8
    //   31: aload_0
    //   32: ldc 20
    //   34: invokevirtual 26	android/content/Context:getString	(I)Ljava/lang/String;
    //   37: invokespecial 29	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   40: aload 6
    //   42: astore_2
    //   43: new 52	java/io/FileInputStream
    //   46: astore_0
    //   47: aload 6
    //   49: astore_2
    //   50: aload_0
    //   51: aload 7
    //   53: invokespecial 55	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   56: new 128	java/lang/StringBuffer
    //   59: astore_2
    //   60: aload_2
    //   61: ldc -126
    //   63: invokespecial 133	java/lang/StringBuffer:<init>	(Ljava/lang/String;)V
    //   66: sipush 1024
    //   69: newarray <illegal type>
    //   71: astore 4
    //   73: aload_0
    //   74: aload 4
    //   76: invokevirtual 137	java/io/FileInputStream:read	([B)I
    //   79: istore_1
    //   80: iload_1
    //   81: iconst_m1
    //   82: if_icmpne +37 -> 119
    //   85: new 139	com/google/gson/Gson
    //   88: astore_3
    //   89: aload_3
    //   90: invokespecial 140	com/google/gson/Gson:<init>	()V
    //   93: aload_3
    //   94: aload_2
    //   95: invokevirtual 143	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   98: ldc -111
    //   100: invokevirtual 149	com/google/gson/Gson:fromJson	(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;
    //   103: checkcast 145	android/bluetooth/BluetoothDevice
    //   106: astore_2
    //   107: aload_0
    //   108: ifnull +168 -> 276
    //   111: aload_0
    //   112: invokevirtual 150	java/io/FileInputStream:close	()V
    //   115: aload_2
    //   116: astore_0
    //   117: aload_0
    //   118: areturn
    //   119: new 94	java/lang/String
    //   122: astore_3
    //   123: aload_3
    //   124: aload 4
    //   126: iconst_0
    //   127: iload_1
    //   128: invokespecial 153	java/lang/String:<init>	([BII)V
    //   131: aload_2
    //   132: aload_3
    //   133: invokevirtual 157	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   136: pop
    //   137: goto -64 -> 73
    //   140: astore_2
    //   141: aload_0
    //   142: astore_3
    //   143: aload_2
    //   144: astore_0
    //   145: aload_3
    //   146: astore_2
    //   147: aload_0
    //   148: invokevirtual 116	java/io/FileNotFoundException:printStackTrace	()V
    //   151: aconst_null
    //   152: astore_2
    //   153: aload_2
    //   154: astore_0
    //   155: aload_3
    //   156: ifnull -39 -> 117
    //   159: aload_3
    //   160: invokevirtual 150	java/io/FileInputStream:close	()V
    //   163: aload_2
    //   164: astore_0
    //   165: goto -48 -> 117
    //   168: astore_0
    //   169: aload_0
    //   170: invokevirtual 117	java/io/IOException:printStackTrace	()V
    //   173: aload_2
    //   174: astore_0
    //   175: goto -58 -> 117
    //   178: astore_0
    //   179: aload 4
    //   181: astore_3
    //   182: aload_3
    //   183: astore_2
    //   184: aload_0
    //   185: invokevirtual 117	java/io/IOException:printStackTrace	()V
    //   188: aconst_null
    //   189: astore_2
    //   190: aload_2
    //   191: astore_0
    //   192: aload_3
    //   193: ifnull -76 -> 117
    //   196: aload_3
    //   197: invokevirtual 150	java/io/FileInputStream:close	()V
    //   200: aload_2
    //   201: astore_0
    //   202: goto -85 -> 117
    //   205: astore_0
    //   206: aload_0
    //   207: invokevirtual 117	java/io/IOException:printStackTrace	()V
    //   210: aload_2
    //   211: astore_0
    //   212: goto -95 -> 117
    //   215: astore_0
    //   216: aload 5
    //   218: astore_3
    //   219: aload_3
    //   220: astore_2
    //   221: aload_0
    //   222: invokevirtual 41	java/lang/Exception:printStackTrace	()V
    //   225: aconst_null
    //   226: astore_2
    //   227: aload_2
    //   228: astore_0
    //   229: aload_3
    //   230: ifnull -113 -> 117
    //   233: aload_3
    //   234: invokevirtual 150	java/io/FileInputStream:close	()V
    //   237: aload_2
    //   238: astore_0
    //   239: goto -122 -> 117
    //   242: astore_0
    //   243: aload_0
    //   244: invokevirtual 117	java/io/IOException:printStackTrace	()V
    //   247: aload_2
    //   248: astore_0
    //   249: goto -132 -> 117
    //   252: astore_0
    //   253: aload_2
    //   254: ifnull +7 -> 261
    //   257: aload_2
    //   258: invokevirtual 150	java/io/FileInputStream:close	()V
    //   261: aload_0
    //   262: athrow
    //   263: astore_2
    //   264: aload_2
    //   265: invokevirtual 117	java/io/IOException:printStackTrace	()V
    //   268: goto -7 -> 261
    //   271: astore_0
    //   272: aload_0
    //   273: invokevirtual 117	java/io/IOException:printStackTrace	()V
    //   276: aload_2
    //   277: astore_0
    //   278: goto -161 -> 117
    //   281: astore_3
    //   282: aload_0
    //   283: astore_2
    //   284: aload_3
    //   285: astore_0
    //   286: goto -33 -> 253
    //   289: astore_3
    //   290: aload_0
    //   291: astore_2
    //   292: aload_3
    //   293: astore_0
    //   294: goto -41 -> 253
    //   297: astore_2
    //   298: aload_0
    //   299: astore_3
    //   300: aload_2
    //   301: astore_0
    //   302: goto -83 -> 219
    //   305: astore_2
    //   306: aload_0
    //   307: astore_3
    //   308: aload_2
    //   309: astore_0
    //   310: goto -91 -> 219
    //   313: astore_2
    //   314: aload_0
    //   315: astore_3
    //   316: aload_2
    //   317: astore_0
    //   318: goto -136 -> 182
    //   321: astore_2
    //   322: aload_0
    //   323: astore_3
    //   324: aload_2
    //   325: astore_0
    //   326: goto -144 -> 182
    //   329: astore_0
    //   330: goto -185 -> 145
    //   333: astore_2
    //   334: aload_0
    //   335: astore_3
    //   336: aload_2
    //   337: astore_0
    //   338: goto -193 -> 145
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	341	0	paramContext	Context
    //   79	49	1	i	int
    //   18	114	2	localObject1	Object
    //   140	4	2	localFileNotFoundException1	FileNotFoundException
    //   146	112	2	localObject2	Object
    //   263	14	2	localIOException1	IOException
    //   283	9	2	localContext1	Context
    //   297	4	2	localException1	Exception
    //   305	4	2	localException2	Exception
    //   313	4	2	localIOException2	IOException
    //   321	4	2	localIOException3	IOException
    //   333	4	2	localFileNotFoundException2	FileNotFoundException
    //   10	224	3	localObject3	Object
    //   281	4	3	localObject4	Object
    //   289	4	3	localObject5	Object
    //   299	37	3	localContext2	Context
    //   1	179	4	arrayOfByte	byte[]
    //   4	213	5	localObject6	Object
    //   7	41	6	localObject7	Object
    //   22	30	7	localFile1	File
    //   14	16	8	localFile2	File
    // Exception table:
    //   from	to	target	type
    //   66	73	140	java/io/FileNotFoundException
    //   73	80	140	java/io/FileNotFoundException
    //   85	107	140	java/io/FileNotFoundException
    //   119	137	140	java/io/FileNotFoundException
    //   159	163	168	java/io/IOException
    //   19	24	178	java/io/IOException
    //   27	40	178	java/io/IOException
    //   43	47	178	java/io/IOException
    //   50	56	178	java/io/IOException
    //   196	200	205	java/io/IOException
    //   19	24	215	java/lang/Exception
    //   27	40	215	java/lang/Exception
    //   43	47	215	java/lang/Exception
    //   50	56	215	java/lang/Exception
    //   233	237	242	java/io/IOException
    //   19	24	252	finally
    //   27	40	252	finally
    //   43	47	252	finally
    //   50	56	252	finally
    //   147	151	252	finally
    //   184	188	252	finally
    //   221	225	252	finally
    //   257	261	263	java/io/IOException
    //   111	115	271	java/io/IOException
    //   56	66	281	finally
    //   66	73	289	finally
    //   73	80	289	finally
    //   85	107	289	finally
    //   119	137	289	finally
    //   56	66	297	java/lang/Exception
    //   66	73	305	java/lang/Exception
    //   73	80	305	java/lang/Exception
    //   85	107	305	java/lang/Exception
    //   119	137	305	java/lang/Exception
    //   56	66	313	java/io/IOException
    //   66	73	321	java/io/IOException
    //   73	80	321	java/io/IOException
    //   85	107	321	java/io/IOException
    //   119	137	321	java/io/IOException
    //   19	24	329	java/io/FileNotFoundException
    //   27	40	329	java/io/FileNotFoundException
    //   43	47	329	java/io/FileNotFoundException
    //   50	56	329	java/io/FileNotFoundException
    //   56	66	333	java/io/FileNotFoundException
  }
  
  public static boolean writeBulkBioDataFile(Context paramContext, byte[] paramArrayOfByte)
  {
    bool = true;
    for (;;)
    {
      try
      {
        File localFile = RefreshTools.getFilesPath();
        localObject = new java/io/File;
        ((File)localObject).<init>(localFile, paramContext.getString(2131165343));
        try
        {
          if (!((File)localObject).exists()) {
            ((File)localObject).createNewFile();
          }
          paramContext = new java/io/FileWriter;
          paramContext.<init>((File)localObject, true);
          paramArrayOfByte = PacketsByteValuesReader.readBioData(paramArrayOfByte, paramArrayOfByte.length);
          i = 0;
          if (i < paramArrayOfByte[0].length) {
            continue;
          }
          paramContext.flush();
          paramContext.close();
        }
        catch (FileNotFoundException paramContext)
        {
          int i;
          int k;
          int j;
          paramContext.printStackTrace();
          bool = false;
          continue;
        }
        catch (IOException paramContext)
        {
          paramContext.printStackTrace();
          continue;
        }
        return bool;
      }
      finally {}
      k = paramArrayOfByte[0][i];
      j = paramArrayOfByte[1][i];
      Object localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>(String.valueOf(k));
      paramContext.append(" " + j + "\n");
      i++;
    }
  }
  
  public static boolean writeJsonFile(Context paramContext, BluetoothDevice paramBluetoothDevice)
  {
    boolean bool = false;
    if (paramBluetoothDevice == null) {}
    for (;;)
    {
      return bool;
      String str = new Gson().toJson(paramBluetoothDevice);
      try
      {
        File localFile = RefreshTools.getFilesPath();
        paramBluetoothDevice = new java/io/File;
        paramBluetoothDevice.<init>(localFile, paramContext.getString(2131165342));
        if (!paramBluetoothDevice.exists()) {
          paramBluetoothDevice.createNewFile();
        }
        paramContext = new java/io/FileOutputStream;
        paramContext.<init>(paramBluetoothDevice);
      }
      catch (FileNotFoundException paramContext)
      {
        try
        {
          paramContext.write(str.getBytes());
          paramContext.flush();
          paramContext.close();
          bool = true;
        }
        catch (IOException paramContext)
        {
          for (;;) {}
        }
        catch (FileNotFoundException paramContext)
        {
          for (;;) {}
        }
        paramContext = paramContext;
        paramContext.printStackTrace();
      }
      catch (IOException paramContext)
      {
        paramContext.printStackTrace();
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\BluetoothDataSerializeUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
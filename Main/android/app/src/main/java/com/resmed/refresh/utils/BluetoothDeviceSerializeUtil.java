package com.resmed.refresh.utils;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BluetoothDeviceSerializeUtil
{
  /* Error */
  public static BluetoothDevice readJsonFile(Context paramContext)
  {
    // Byte code:
    //   0: new 17	java/io/File
    //   3: dup
    //   4: invokestatic 23	com/resmed/refresh/utils/RefreshTools:getFilesPath	()Ljava/io/File;
    //   7: aload_0
    //   8: ldc 24
    //   10: invokevirtual 30	android/content/Context:getString	(I)Ljava/lang/String;
    //   13: invokespecial 33	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   16: astore 7
    //   18: aconst_null
    //   19: astore 6
    //   21: aconst_null
    //   22: astore_3
    //   23: aconst_null
    //   24: astore 5
    //   26: aconst_null
    //   27: astore 4
    //   29: aload_3
    //   30: astore_2
    //   31: new 35	java/io/FileInputStream
    //   34: astore_0
    //   35: aload_3
    //   36: astore_2
    //   37: aload_0
    //   38: aload 7
    //   40: invokespecial 38	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   43: new 40	java/lang/StringBuffer
    //   46: astore_2
    //   47: aload_2
    //   48: ldc 42
    //   50: invokespecial 45	java/lang/StringBuffer:<init>	(Ljava/lang/String;)V
    //   53: sipush 1024
    //   56: newarray <illegal type>
    //   58: astore_3
    //   59: aload_0
    //   60: aload_3
    //   61: invokevirtual 49	java/io/FileInputStream:read	([B)I
    //   64: istore_1
    //   65: iload_1
    //   66: iconst_m1
    //   67: if_icmpne +39 -> 106
    //   70: new 51	com/google/gson/Gson
    //   73: astore_3
    //   74: aload_3
    //   75: invokespecial 52	com/google/gson/Gson:<init>	()V
    //   78: aload_3
    //   79: aload_2
    //   80: invokevirtual 56	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   83: ldc 58
    //   85: invokevirtual 62	com/google/gson/Gson:fromJson	(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;
    //   88: checkcast 58	android/bluetooth/BluetoothDevice
    //   91: astore_2
    //   92: aload_0
    //   93: ifnull +133 -> 226
    //   96: aload_0
    //   97: invokevirtual 65	java/io/FileInputStream:close	()V
    //   100: invokestatic 71	android/bluetooth/BluetoothAdapter:getDefaultAdapter	()Landroid/bluetooth/BluetoothAdapter;
    //   103: pop
    //   104: aload_2
    //   105: areturn
    //   106: new 73	java/lang/String
    //   109: astore 5
    //   111: aload 5
    //   113: aload_3
    //   114: iconst_0
    //   115: iload_1
    //   116: invokespecial 76	java/lang/String:<init>	([BII)V
    //   119: aload_2
    //   120: aload 5
    //   122: invokevirtual 80	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   125: pop
    //   126: goto -67 -> 59
    //   129: astore_3
    //   130: aload_0
    //   131: astore_2
    //   132: aload_3
    //   133: invokevirtual 83	java/io/FileNotFoundException:printStackTrace	()V
    //   136: aload 4
    //   138: astore_2
    //   139: aload_0
    //   140: ifnull -40 -> 100
    //   143: aload_0
    //   144: invokevirtual 65	java/io/FileInputStream:close	()V
    //   147: aload 4
    //   149: astore_2
    //   150: goto -50 -> 100
    //   153: astore_0
    //   154: aload_0
    //   155: invokevirtual 84	java/io/IOException:printStackTrace	()V
    //   158: aload 4
    //   160: astore_2
    //   161: goto -61 -> 100
    //   164: astore_3
    //   165: aload 6
    //   167: astore_0
    //   168: aload_0
    //   169: astore_2
    //   170: aload_3
    //   171: invokevirtual 84	java/io/IOException:printStackTrace	()V
    //   174: aload 4
    //   176: astore_2
    //   177: aload_0
    //   178: ifnull -78 -> 100
    //   181: aload_0
    //   182: invokevirtual 65	java/io/FileInputStream:close	()V
    //   185: aload 4
    //   187: astore_2
    //   188: goto -88 -> 100
    //   191: astore_0
    //   192: aload_0
    //   193: invokevirtual 84	java/io/IOException:printStackTrace	()V
    //   196: aload 4
    //   198: astore_2
    //   199: goto -99 -> 100
    //   202: astore_0
    //   203: aload_2
    //   204: ifnull +7 -> 211
    //   207: aload_2
    //   208: invokevirtual 65	java/io/FileInputStream:close	()V
    //   211: aload_0
    //   212: athrow
    //   213: astore_2
    //   214: aload_2
    //   215: invokevirtual 84	java/io/IOException:printStackTrace	()V
    //   218: goto -7 -> 211
    //   221: astore_0
    //   222: aload_0
    //   223: invokevirtual 84	java/io/IOException:printStackTrace	()V
    //   226: goto -126 -> 100
    //   229: astore_3
    //   230: aload_0
    //   231: astore_2
    //   232: aload_3
    //   233: astore_0
    //   234: goto -31 -> 203
    //   237: astore_3
    //   238: aload_0
    //   239: astore_2
    //   240: aload_3
    //   241: astore_0
    //   242: goto -39 -> 203
    //   245: astore_3
    //   246: goto -78 -> 168
    //   249: astore_3
    //   250: goto -82 -> 168
    //   253: astore_3
    //   254: aload 5
    //   256: astore_0
    //   257: goto -127 -> 130
    //   260: astore_3
    //   261: goto -131 -> 130
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	264	0	paramContext	Context
    //   64	52	1	i	int
    //   30	178	2	localObject1	Object
    //   213	2	2	localIOException1	IOException
    //   231	9	2	localContext	Context
    //   22	92	3	localObject2	Object
    //   129	4	3	localFileNotFoundException1	FileNotFoundException
    //   164	7	3	localIOException2	IOException
    //   229	4	3	localObject3	Object
    //   237	4	3	localObject4	Object
    //   245	1	3	localIOException3	IOException
    //   249	1	3	localIOException4	IOException
    //   253	1	3	localFileNotFoundException2	FileNotFoundException
    //   260	1	3	localFileNotFoundException3	FileNotFoundException
    //   27	170	4	localObject5	Object
    //   24	231	5	str	String
    //   19	147	6	localObject6	Object
    //   16	23	7	localFile	File
    // Exception table:
    //   from	to	target	type
    //   53	59	129	java/io/FileNotFoundException
    //   59	65	129	java/io/FileNotFoundException
    //   70	92	129	java/io/FileNotFoundException
    //   106	126	129	java/io/FileNotFoundException
    //   143	147	153	java/io/IOException
    //   31	35	164	java/io/IOException
    //   37	43	164	java/io/IOException
    //   181	185	191	java/io/IOException
    //   31	35	202	finally
    //   37	43	202	finally
    //   132	136	202	finally
    //   170	174	202	finally
    //   207	211	213	java/io/IOException
    //   96	100	221	java/io/IOException
    //   43	53	229	finally
    //   53	59	237	finally
    //   59	65	237	finally
    //   70	92	237	finally
    //   106	126	237	finally
    //   43	53	245	java/io/IOException
    //   53	59	249	java/io/IOException
    //   59	65	249	java/io/IOException
    //   70	92	249	java/io/IOException
    //   106	126	249	java/io/IOException
    //   31	35	253	java/io/FileNotFoundException
    //   37	43	253	java/io/FileNotFoundException
    //   43	53	260	java/io/FileNotFoundException
  }
  
  public static boolean writeJsonFile(Context paramContext, BluetoothDevice paramBluetoothDevice)
  {
    String str = new Gson().toJson(paramBluetoothDevice);
    try
    {
      paramBluetoothDevice = new java.io.File;
      paramBluetoothDevice.<init>(RefreshTools.getFilesPath(), paramContext.getString(2131165342));
      paramContext = new java.io.FileOutputStream;
      paramContext.<init>(paramBluetoothDevice);
      paramContext.write(str.getBytes());
      paramContext.flush();
      paramContext.close();
      bool = true;
    }
    catch (FileNotFoundException paramContext)
    {
      for (;;)
      {
        paramContext.printStackTrace();
        boolean bool = false;
      }
    }
    catch (IOException paramContext)
    {
      for (;;)
      {
        paramContext.printStackTrace();
      }
    }
    return bool;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
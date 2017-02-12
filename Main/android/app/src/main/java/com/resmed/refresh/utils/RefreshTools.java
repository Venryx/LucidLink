package com.resmed.refresh.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.graphs.RRHypnoData;
import com.resmed.refresh.model.mappers.HypnoMapper;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.utils.Consts;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

@SuppressLint({"SimpleDateFormat"})
public class RefreshTools
{
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
  private static final String DATE_FORMAT_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  protected static final char[] hexArray = "0123456789ABCDEF".toCharArray();
  
  public static String BitMapToString(Bitmap paramBitmap)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, localByteArrayOutputStream);
    return Base64.encodeToString(localByteArrayOutputStream.toByteArray(), 0);
  }
  
  public static Bitmap StringToBitMap(String paramString)
  {
    try
    {
      paramString = Base64.decode(paramString, 0);
      paramString = BitmapFactory.decodeByteArray(paramString, 0, paramString.length);
      return paramString;
    }
    catch (Exception paramString)
    {
      for (;;)
      {
        paramString.getMessage();
        paramString = null;
      }
    }
  }
  
  public static BitSet bitmaskFromInt(int paramInt)
  {
    BitSet localBitSet = new BitSet();
    for (int i = 0;; i++)
    {
      if (i >= 32) {
        return localBitSet;
      }
      if ((paramInt >> i & 0x1) == 1) {
        localBitSet.set(i);
      }
    }
  }
  
  public static int bitmaskToInt(BitSet paramBitSet)
  {
    int i = 0;
    int j = -1;
    for (;;)
    {
      j = paramBitSet.nextSetBit(j + 1);
      if (j == -1) {
        return i;
      }
      i |= 1 << j;
    }
  }
  
  public static String bytesToHex(List<Byte> paramList)
  {
    char[] arrayOfChar = new char[paramList.size() * 2];
    for (int i = 0;; i++)
    {
      if (i >= paramList.size()) {
        return new String(arrayOfChar);
      }
      int j = ((Byte)paramList.get(i)).byteValue() & 0xFF;
      arrayOfChar[(i * 2)] = hexArray[(j >>> 4)];
      arrayOfChar[(i * 2 + 1)] = hexArray[(j & 0xF)];
    }
  }
  
  public static String bytesToHex(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length * 2];
    for (int i = 0;; i++)
    {
      if (i >= paramArrayOfByte.length) {
        return new String(arrayOfChar);
      }
      int j = paramArrayOfByte[i] & 0xFF;
      arrayOfChar[(i * 2)] = hexArray[(j >>> 4)];
      arrayOfChar[(i * 2 + 1)] = hexArray[(j & 0xF)];
    }
  }
  
  public static int calculateAge(Date paramDate)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime(paramDate);
    paramDate = Calendar.getInstance();
    int j = paramDate.get(1) - localCalendar.get(1);
    int i;
    if (paramDate.get(2) < localCalendar.get(2)) {
      i = j - 1;
    }
    for (;;)
    {
      return i;
      i = j;
      if (paramDate.get(2) == localCalendar.get(2))
      {
        i = j;
        if (paramDate.get(5) < localCalendar.get(5)) {
          i = j - 1;
        }
      }
    }
  }
  
  public static boolean checkForFirmwareUpgrade(Context paramContext, String paramString)
  {
    Log.d("com.resmed.refresh.ui", "checkForFirmwareUpgrade firmware version :" + paramString);
    paramContext = getFirmwareBinaryVersion(paramContext);
    if (compareFirmwareVersions(paramString.replace("Release", "").split(" ")[0], paramContext) < 0) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  /* Error */
  public static int compareFirmwareVersions(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: ldc -122
    //   2: new 136	java/lang/StringBuilder
    //   5: dup
    //   6: ldc -79
    //   8: invokespecial 141	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   11: aload_0
    //   12: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: ldc -77
    //   17: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   20: aload_1
    //   21: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   24: invokevirtual 148	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   27: invokestatic 154	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   30: pop
    //   31: aload_0
    //   32: ifnull +7 -> 39
    //   35: aload_1
    //   36: ifnonnull +7 -> 43
    //   39: iconst_0
    //   40: istore_2
    //   41: iload_2
    //   42: ireturn
    //   43: aload_0
    //   44: ldc -75
    //   46: invokevirtual 172	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   49: astore_0
    //   50: aload_1
    //   51: ldc -75
    //   53: invokevirtual 172	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   56: astore_1
    //   57: ldc -122
    //   59: new 136	java/lang/StringBuilder
    //   62: dup
    //   63: ldc -73
    //   65: invokespecial 141	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   68: aload_0
    //   69: invokestatic 188	java/util/Arrays:toString	([Ljava/lang/Object;)Ljava/lang/String;
    //   72: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   75: ldc -77
    //   77: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   80: aload_1
    //   81: invokestatic 188	java/util/Arrays:toString	([Ljava/lang/Object;)Ljava/lang/String;
    //   84: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   87: invokevirtual 148	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   90: invokestatic 154	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   93: pop
    //   94: aload_0
    //   95: arraylength
    //   96: ifeq +8 -> 104
    //   99: aload_1
    //   100: arraylength
    //   101: ifne +8 -> 109
    //   104: iconst_0
    //   105: istore_2
    //   106: goto -65 -> 41
    //   109: aload_0
    //   110: iconst_0
    //   111: aaload
    //   112: invokestatic 194	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   115: istore_2
    //   116: aload_1
    //   117: iconst_0
    //   118: aaload
    //   119: invokestatic 194	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   122: istore_3
    //   123: ldc -122
    //   125: new 136	java/lang/StringBuilder
    //   128: dup
    //   129: ldc -60
    //   131: invokespecial 141	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   134: iload_2
    //   135: invokevirtual 199	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   138: ldc -55
    //   140: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   143: iload_3
    //   144: invokevirtual 199	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   147: invokevirtual 148	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   150: invokestatic 154	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   153: pop
    //   154: iload_2
    //   155: iload_3
    //   156: invokestatic 205	java/lang/Integer:compare	(II)I
    //   159: istore_3
    //   160: iload_3
    //   161: istore_2
    //   162: iload_3
    //   163: ifne -122 -> 41
    //   166: aload_0
    //   167: iconst_1
    //   168: aaload
    //   169: invokestatic 194	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   172: istore_2
    //   173: aload_1
    //   174: iconst_1
    //   175: aaload
    //   176: invokestatic 194	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   179: istore_3
    //   180: ldc -122
    //   182: new 136	java/lang/StringBuilder
    //   185: dup
    //   186: ldc -49
    //   188: invokespecial 141	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   191: iload_2
    //   192: invokevirtual 199	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   195: ldc -47
    //   197: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   200: iload_3
    //   201: invokevirtual 199	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   204: invokevirtual 148	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   207: invokestatic 154	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   210: pop
    //   211: iload_2
    //   212: iload_3
    //   213: invokestatic 205	java/lang/Integer:compare	(II)I
    //   216: istore_3
    //   217: iload_3
    //   218: istore_2
    //   219: iload_3
    //   220: ifne -179 -> 41
    //   223: aload_0
    //   224: iconst_2
    //   225: aaload
    //   226: invokestatic 194	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   229: istore_2
    //   230: aload_1
    //   231: iconst_2
    //   232: aaload
    //   233: invokestatic 194	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   236: istore_3
    //   237: ldc -122
    //   239: new 136	java/lang/StringBuilder
    //   242: dup
    //   243: ldc -45
    //   245: invokespecial 141	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   248: iload_2
    //   249: invokevirtual 199	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   252: ldc -43
    //   254: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   257: iload_3
    //   258: invokevirtual 199	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   261: invokevirtual 148	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   264: invokestatic 154	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   267: pop
    //   268: iload_2
    //   269: iload_3
    //   270: invokestatic 205	java/lang/Integer:compare	(II)I
    //   273: istore_3
    //   274: iload_3
    //   275: istore_2
    //   276: iload_3
    //   277: ifne -236 -> 41
    //   280: iconst_0
    //   281: istore_2
    //   282: goto -241 -> 41
    //   285: astore_0
    //   286: iconst_0
    //   287: istore_2
    //   288: goto -247 -> 41
    //   291: astore_0
    //   292: iconst_0
    //   293: istore_2
    //   294: goto -253 -> 41
    //   297: astore_0
    //   298: iconst_0
    //   299: istore_2
    //   300: goto -259 -> 41
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	303	0	paramString1	String
    //   0	303	1	paramString2	String
    //   40	260	2	i	int
    //   122	155	3	j	int
    // Exception table:
    //   from	to	target	type
    //   109	123	285	java/lang/Exception
    //   166	180	291	java/lang/Exception
    //   223	237	297	java/lang/Exception
  }
  
  public static boolean createDirectories(String paramString)
  {
    for (;;)
    {
      try
      {
        arrayOfString = paramString.split("/");
        localObject = "";
        i = 0;
        if (i < arrayOfString.length - 1) {
          continue;
        }
        bool = true;
      }
      catch (Exception paramString)
      {
        String[] arrayOfString;
        Object localObject;
        int i;
        Log.d("com.resmed.refresh.model", "Error creating directories", paramString);
        boolean bool = false;
        continue;
      }
      return bool;
      paramString = (String)localObject;
      if (arrayOfString[i].length() > 0)
      {
        paramString = new java/lang/StringBuilder;
        paramString.<init>(String.valueOf(localObject));
        paramString = "/" + arrayOfString[i];
      }
      localObject = new java/io/File;
      ((File)localObject).<init>(paramString);
      ((File)localObject).mkdirs();
      i++;
      localObject = paramString;
    }
  }
  
  public static List<Date> dateRangesForDay(Date paramDate)
  {
    Calendar localCalendar2 = GregorianCalendar.getInstance();
    localCalendar2.setTime(paramDate);
    Calendar localCalendar1 = GregorianCalendar.getInstance();
    localCalendar1.setTime(paramDate);
    localCalendar2.set(11, 0);
    localCalendar2.set(12, 0);
    localCalendar2.set(13, 0);
    localCalendar1.set(11, 23);
    localCalendar1.set(12, 59);
    localCalendar1.set(13, 59);
    paramDate = new ArrayList();
    paramDate.add(localCalendar2.getTime());
    paramDate.add(localCalendar1.getTime());
    return paramDate;
  }
  
  public static List<Date> dateRangesForExactTime(Date paramDate)
  {
    long l = paramDate.getTime();
    Date localDate = new Date(l - 1000L);
    paramDate = new Date(l + 1000L);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(localDate);
    localArrayList.add(paramDate);
    return localArrayList;
  }
  
  public static void debugSleepSessions()
  {
    Object localObject = RefreshModelController.getInstance();
    if (((RefreshModelController)localObject).getUser() == null) {}
    for (;;)
    {
      return;
      Iterator localIterator = ((RefreshModelController)localObject).localSleepSessionsAll().iterator();
      while (localIterator.hasNext())
      {
        localObject = (RST_SleepSessionInfo)localIterator.next();
        List localList = HypnoMapper.getHypnoData((RST_SleepSessionInfo)localObject);
        int i = Math.round((float)((((RST_SleepSessionInfo)localObject).getStopTime().getTime() - ((RST_SleepSessionInfo)localObject).getStartTime().getTime()) / 60000L));
        String str1;
        String str2;
        float f;
        if ((((RST_SleepSessionInfo)localObject).getTotalSleepTime() > 60) && (i > 0))
        {
          str1 = getHourStringForTime(((RST_SleepSessionInfo)localObject).getTotalSleepTime()) + getMinsStringForTime(((RST_SleepSessionInfo)localObject).getTotalSleepTime());
          str2 = getHourStringForTime(i) + getMinsStringForTime(i);
          f = 0.0F;
        }
        try
        {
          long l1 = ((RRHypnoData)localList.get(1)).getHour().getTime();
          long l2 = ((RRHypnoData)localList.get(0)).getHour().getTime();
          f = (float)(l1 - l2) / 1000.0F;
        }
        catch (Exception localException)
        {
          for (;;)
          {
            localException.printStackTrace();
          }
        }
        Log.d("sessions", ((RST_SleepSessionInfo)localObject).getId() + "\tTotalTime = " + str1 + "\tsleepTime = " + str2 + "\tTimeBetweenSamples = " + f + "\t" + ((RST_SleepSessionInfo)localObject).getStartTime() + "\t" + ((RST_SleepSessionInfo)localObject).getStopTime() + "\t" + ((RST_SleepSessionInfo)localObject).getCompleted());
      }
    }
  }
  
  public static boolean deleteTimeStampFile(Context paramContext)
  {
    try
    {
      File localFile = new java/io/File;
      localFile.<init>(getFilesPath(), paramContext.getString(2131165344));
      boolean bool = localFile.delete();
      return bool;
    }
    finally
    {
      paramContext = finally;
      throw paramContext;
    }
  }
  
  public static boolean exportAllFilesToSD()
  {
    for (;;)
    {
      try
      {
        localObject2 = RefreshApplication.getInstance().getFilesDir();
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()));
        localObject1 = "/Refresh/export/";
        localObject2 = ((File)localObject2).listFiles();
        int j = localObject2.length;
        i = 0;
        if (i < j) {
          continue;
        }
        bool = true;
      }
      catch (Exception localException)
      {
        Object localObject2;
        Object localObject1;
        int i;
        Object localObject3;
        Object localObject4;
        Object localObject5;
        localException.printStackTrace();
        Log.d("com.resmed.refresh.model", "Error in exportAllFilesToSD", localException);
        boolean bool = false;
        continue;
      }
      return bool;
      localObject3 = localObject2[i];
      localObject4 = new java/lang/StringBuilder;
      ((StringBuilder)localObject4).<init>(" exportAllFilesToSD:: file.getName() : ");
      Log.d("com.resmed.refresh.model", ((File)localObject3).getName());
      localObject4 = ((File)localObject3).getName().replace(RefreshApplication.getInstance().getFilesDir().getAbsolutePath(), "");
      localObject5 = new java/lang/StringBuilder;
      ((StringBuilder)localObject5).<init>(String.valueOf(localObject1));
      localObject5 = (String)localObject4;
      localObject4 = new java/io/File;
      ((File)localObject4).<init>((String)localObject5);
      createDirectories(((File)localObject4).getAbsolutePath());
      localObject5 = new java/io/FileInputStream;
      ((FileInputStream)localObject5).<init>((File)localObject3);
      localObject3 = ((FileInputStream)localObject5).getChannel();
      localObject5 = new java/io/FileOutputStream;
      ((FileOutputStream)localObject5).<init>(((File)localObject4).getAbsolutePath());
      localObject4 = ((FileOutputStream)localObject5).getChannel();
      ((FileChannel)localObject4).transferFrom((ReadableByteChannel)localObject3, 0L, ((FileChannel)localObject3).size());
      ((FileChannel)localObject3).close();
      ((FileChannel)localObject4).close();
      i++;
    }
  }
  
  public static boolean exportDataBaseToSD()
  {
    for (;;)
    {
      try
      {
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()));
        localObject2 = "/Refresh/refresh-db.db";
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>("exportDataBaseToSD db_path=");
        Log.d("com.resmed.refresh.model", "data/data/com.resmed.refresh/databases/refresh-db");
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>("exportDataBaseToSD pathToCopy=");
        Log.d("com.resmed.refresh.model", (String)localObject2);
        localObject3 = new java/io/File;
        ((File)localObject3).<init>("data/data/com.resmed.refresh/databases/refresh-db");
        localObject1 = new java/io/File;
        ((File)localObject1).<init>((String)localObject2);
        if (((File)localObject3).exists()) {
          continue;
        }
        Log.w("Copy DB", "Data base doesn't exits!");
        bool = false;
      }
      catch (Exception localException)
      {
        Object localObject1;
        Object localObject2;
        Object localObject3;
        localException.printStackTrace();
        Log.d("com.resmed.refresh.model", "Error in copyDataBaseFromData", localException);
        boolean bool = false;
        continue;
      }
      return bool;
      createDirectories((String)localObject2);
      localObject2 = new java/io/FileInputStream;
      ((FileInputStream)localObject2).<init>((File)localObject3);
      localObject2 = ((FileInputStream)localObject2).getChannel();
      localObject3 = new java/io/FileOutputStream;
      ((FileOutputStream)localObject3).<init>((File)localObject1);
      localObject1 = ((FileOutputStream)localObject3).getChannel();
      ((FileChannel)localObject1).transferFrom((ReadableByteChannel)localObject2, 0L, ((FileChannel)localObject2).size());
      ((FileChannel)localObject2).close();
      ((FileChannel)localObject1).close();
      bool = true;
    }
  }
  
  public static File findFileByName(File paramFile, String paramString)
  {
    if (!paramFile.isDirectory())
    {
      paramFile = null;
      return paramFile;
    }
    File[] arrayOfFile = paramFile.listFiles();
    int j = arrayOfFile.length;
    for (int i = 0;; i++)
    {
      if (i >= j)
      {
        paramFile = null;
        break;
      }
      paramFile = arrayOfFile[i];
      Log.d("com.resmed.refresh.bluetooth", " SleepSessionManager a file : " + paramFile);
      if (paramFile.isFile())
      {
        String str = paramFile.getName();
        Log.d("com.resmed.refresh.bluetooth", " SleepSessionManager edf file : " + paramFile);
        if (str.endsWith(paramString)) {
          break;
        }
      }
    }
  }
  
  public static float getBatteryLevel(Context paramContext)
  {
    paramContext = paramContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
    int j = paramContext.getIntExtra("level", -1);
    int i = paramContext.getIntExtra("scale", -1);
    if ((j == -1) || (i == -1)) {}
    for (float f = 50.0F;; f = j / i * 100.0F) {
      return f;
    }
  }
  
  public static Date getDateFromString(String paramString)
  {
    for (;;)
    {
      try
      {
        if (!paramString.contains("Z")) {
          continue;
        }
        localSimpleDateFormat = new java/text/SimpleDateFormat;
        localSimpleDateFormat.<init>("yyyy-MM-dd'T'HH:mm:ss'Z'");
        localSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        localCalendar.setTime(localSimpleDateFormat.parse(paramString));
        paramString = localCalendar.getTime();
      }
      catch (ParseException paramString)
      {
        SimpleDateFormat localSimpleDateFormat;
        paramString = new Date();
        continue;
      }
      return paramString;
      localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    }
  }
  
  public static File getFilesPath()
  {
    if (Consts.USE_EXTERNAL_STORAGE) {}
    for (File localFile = Environment.getExternalStorageDirectory();; localFile = RefreshApplication.getInstance().getFilesDir()) {
      return localFile;
    }
  }
  
  public static String getFirmwareBinaryVersion(Context paramContext)
  {
    Object localObject1 = "1.0.5";
    Object localObject2 = paramContext.getResources().getAssets();
    paramContext = (Context)localObject1;
    try
    {
      arrayOfString1 = ((AssetManager)localObject2).list("");
      i = 0;
      paramContext = (Context)localObject1;
      if (i < arrayOfString1.length) {
        break label37;
      }
      paramContext = (Context)localObject1;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        String[] arrayOfString1;
        int i;
        label37:
        String[] arrayOfString2;
        localIOException.printStackTrace();
      }
    }
    return paramContext;
    paramContext = (Context)localObject1;
    localObject2 = new java/lang/StringBuilder;
    paramContext = (Context)localObject1;
    ((StringBuilder)localObject2).<init>("getFirmwareBinary; fileName = ");
    paramContext = (Context)localObject1;
    Log.d("com.resmed.refresh.ui", arrayOfString1[i]);
    localObject2 = localObject1;
    paramContext = (Context)localObject1;
    if (arrayOfString1[i].contains("BeD_"))
    {
      paramContext = (Context)localObject1;
      arrayOfString2 = arrayOfString1[i].split("_");
      paramContext = (Context)localObject1;
      localObject2 = new java/lang/StringBuilder;
      paramContext = (Context)localObject1;
      ((StringBuilder)localObject2).<init>("checkForFirmwareUpgrade tokens :");
      paramContext = (Context)localObject1;
      Log.d("com.resmed.refresh.ui", Arrays.toString(arrayOfString2));
      paramContext = (Context)localObject1;
      if (arrayOfString2.length < 3) {
        break label191;
      }
    }
    label191:
    for (localObject1 = arrayOfString2[2];; localObject1 = arrayOfString1[i])
    {
      paramContext = (Context)localObject1;
      localObject2 = new java/lang/StringBuilder;
      paramContext = (Context)localObject1;
      ((StringBuilder)localObject2).<init>("checkForFirmwareUpgrade firmware version :");
      paramContext = (Context)localObject1;
      Log.d("com.resmed.refresh.ui", (String)localObject1);
      localObject2 = localObject1;
      i++;
      localObject1 = localObject2;
      break;
    }
  }
  
  public static String getHourMinsStringForTime(int paramInt)
  {
    return paramInt / 60 + ":" + String.format("%02d", new Object[] { Integer.valueOf(paramInt % 60) });
  }
  
  public static String getHourStringForTime(int paramInt)
  {
    paramInt /= 60;
    if (paramInt == 0) {}
    for (String str = "";; str = String.format("%02dh", new Object[] { Integer.valueOf(paramInt) })) {
      return str;
    }
  }
  
  public static String getMinsSecsStringForTime(int paramInt)
  {
    paramInt = Math.round(paramInt / 1000);
    return String.format("%02d", new Object[] { Integer.valueOf(paramInt / 60) }) + ":" + String.format("%02d", new Object[] { Integer.valueOf(paramInt % 60) });
  }
  
  public static String getMinsStringForTime(int paramInt)
  {
    return String.format("%02dm", new Object[] { Integer.valueOf(paramInt % 60) });
  }
  
  public static String getStringFromDate(Date paramDate)
  {
    Object localObject = Calendar.getInstance();
    ((Calendar)localObject).setTimeZone(TimeZone.getTimeZone("UTC"));
    ((Calendar)localObject).setTime(paramDate);
    paramDate = ((Calendar)localObject).getTime();
    localObject = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    ((SimpleDateFormat)localObject).setTimeZone(TimeZone.getTimeZone("UTC"));
    return ((SimpleDateFormat)localObject).format(paramDate);
  }
  
  public static void hideKeyBoard(Activity paramActivity)
  {
    try
    {
      ((InputMethodManager)paramActivity.getSystemService("input_method")).hideSoftInputFromWindow(paramActivity.getCurrentFocus().getWindowToken(), 2);
      return;
    }
    catch (Exception paramActivity)
    {
      for (;;) {}
    }
  }
  
  public static boolean isAppRunning(Context paramContext, String paramString)
  {
    paramContext = ((ActivityManager)paramContext.getSystemService("activity")).getRunningAppProcesses();
    for (int i = 0;; i++)
    {
      if (i >= paramContext.size()) {}
      for (boolean bool = false;; bool = true)
      {
        return bool;
        if (!((ActivityManager.RunningAppProcessInfo)paramContext.get(i)).processName.equals(paramString)) {
          break;
        }
      }
    }
  }
  
  public static boolean isPluggedIn(Context paramContext)
  {
    boolean bool2 = true;
    int i = paramContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("plugged", -1);
    boolean bool1 = bool2;
    if (i != 1)
    {
      bool1 = bool2;
      if (i != 2) {
        bool1 = false;
      }
    }
    return bool1;
  }
  
  public static Long readTimeStampToFile(Context paramContext)
  {
    localObject1 = null;
    for (;;)
    {
      try
      {
        localObject3 = new java/io/File;
        ((File)localObject3).<init>(getFilesPath(), paramContext.getString(2131165344));
        try
        {
          boolean bool = ((File)localObject3).exists();
          if (bool) {
            continue;
          }
          paramContext = (Context)localObject1;
        }
        catch (IOException paramContext)
        {
          Object localObject2;
          long l;
          paramContext.printStackTrace();
          paramContext = (Context)localObject1;
          continue;
        }
        return paramContext;
      }
      finally {}
      localObject2 = new java/io/FileInputStream;
      ((FileInputStream)localObject2).<init>((File)localObject3);
      paramContext = new java/io/BufferedReader;
      Object localObject3 = new java/io/InputStreamReader;
      ((InputStreamReader)localObject3).<init>((InputStream)localObject2);
      paramContext.<init>((Reader)localObject3);
      localObject2 = paramContext.readLine();
      paramContext.close();
      paramContext = (Context)localObject1;
      if (localObject2 != null)
      {
        l = Long.parseLong(((String)localObject2).trim());
        paramContext = Long.valueOf(l);
      }
    }
  }
  
  public static void setListViewHeightBasedOnChildren(ListView paramListView)
  {
    ListAdapter localListAdapter = paramListView.getAdapter();
    if (localListAdapter == null) {
      return;
    }
    int j = 0;
    for (int i = 0;; i++)
    {
      if (i >= localListAdapter.getCount())
      {
        localObject = paramListView.getLayoutParams();
        ((ViewGroup.LayoutParams)localObject).height = (paramListView.getDividerHeight() * (localListAdapter.getCount() - 1) + j);
        paramListView.setLayoutParams((ViewGroup.LayoutParams)localObject);
        break;
      }
      Object localObject = localListAdapter.getView(i, null, paramListView);
      ((View)localObject).measure(0, 0);
      j += ((View)localObject).getMeasuredHeight();
    }
  }
  
  public static boolean validateEmail(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0)) {}
    for (boolean bool = false;; bool = Patterns.EMAIL_ADDRESS.matcher(paramString).matches()) {
      return bool;
    }
  }
  
  public static void writeStringAsFile(String paramString1, String paramString2)
  {
    try
    {
      Object localObject1 = new java/io/File;
      Object localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((File)localObject1).<init>(Environment.getExternalStorageDirectory() + "/refresh", paramString2);
      createDirectories(((File)localObject1).getAbsolutePath());
      localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>("writeStringAsFile path file:");
      Log.d("com.resmed.refresh.model", ((File)localObject1).getAbsolutePath());
      localObject2 = new java/io/FileWriter;
      ((FileWriter)localObject2).<init>((File)localObject1);
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>("writeStringAsFile path file:");
      Log.d("com.resmed.refresh.model", Environment.getExternalStorageDirectory() + "/refresh/" + paramString2);
      ((FileWriter)localObject2).write(paramString1);
      ((FileWriter)localObject2).close();
      return;
    }
    catch (IOException paramString1)
    {
      for (;;)
      {
        Log.d("com.resmed.refresh.model", "ERROR", paramString1);
      }
    }
  }
  
  public static boolean writeTimeStampToFile(Context paramContext, long paramLong)
  {
    boolean bool = false;
    try
    {
      Object localObject = new java/io/File;
      ((File)localObject).<init>(getFilesPath(), paramContext.getString(2131165344));
      try
      {
        if (!((File)localObject).exists()) {
          ((File)localObject).createNewFile();
        }
        paramContext = new java/io/FileWriter;
        paramContext.<init>((File)localObject, false);
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>(String.valueOf(Long.toString(paramLong)));
        paramContext.write(" \n");
        paramContext.flush();
        paramContext.close();
        bool = true;
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
    finally {}
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\RefreshTools.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
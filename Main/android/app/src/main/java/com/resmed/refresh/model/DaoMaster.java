package com.resmed.refresh.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

public class DaoMaster
  extends AbstractDaoMaster
{
  public static final int SCHEMA_VERSION = 10;
  
  public DaoMaster(SQLiteDatabase paramSQLiteDatabase)
  {
    super(paramSQLiteDatabase, 10);
    registerDaoClass(RST_UserDao.class);
    registerDaoClass(RST_UserProfileDao.class);
    registerDaoClass(RST_SettingsDao.class);
    registerDaoClass(RST_SleepSessionInfoDao.class);
    registerDaoClass(RST_EnvironmentalInfoDao.class);
    registerDaoClass(RST_LocationItemDao.class);
    registerDaoClass(RST_AdviceItemDao.class);
    registerDaoClass(RST_ButtonDao.class);
    registerDaoClass(RST_SleepEventDao.class);
    registerDaoClass(RST_NightQuestionsDao.class);
    registerDaoClass(RST_QuestionItemDao.class);
    registerDaoClass(RST_DisplayItemDao.class);
    registerDaoClass(RST_ValueItemDao.class);
  }
  
  public static void createAllTables(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    RST_UserDao.createTable(paramSQLiteDatabase, paramBoolean);
    RST_UserProfileDao.createTable(paramSQLiteDatabase, paramBoolean);
    RST_SettingsDao.createTable(paramSQLiteDatabase, paramBoolean);
    RST_SleepSessionInfoDao.createTable(paramSQLiteDatabase, paramBoolean);
    RST_EnvironmentalInfoDao.createTable(paramSQLiteDatabase, paramBoolean);
    RST_LocationItemDao.createTable(paramSQLiteDatabase, paramBoolean);
    RST_AdviceItemDao.createTable(paramSQLiteDatabase, paramBoolean);
    RST_ButtonDao.createTable(paramSQLiteDatabase, paramBoolean);
    RST_SleepEventDao.createTable(paramSQLiteDatabase, paramBoolean);
    RST_NightQuestionsDao.createTable(paramSQLiteDatabase, paramBoolean);
    RST_QuestionItemDao.createTable(paramSQLiteDatabase, paramBoolean);
    RST_DisplayItemDao.createTable(paramSQLiteDatabase, paramBoolean);
    RST_ValueItemDao.createTable(paramSQLiteDatabase, paramBoolean);
  }
  
  public static void dropAllTables(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    RST_UserDao.dropTable(paramSQLiteDatabase, paramBoolean);
    RST_UserProfileDao.dropTable(paramSQLiteDatabase, paramBoolean);
    RST_SettingsDao.dropTable(paramSQLiteDatabase, paramBoolean);
    RST_SleepSessionInfoDao.dropTable(paramSQLiteDatabase, paramBoolean);
    RST_EnvironmentalInfoDao.dropTable(paramSQLiteDatabase, paramBoolean);
    RST_LocationItemDao.dropTable(paramSQLiteDatabase, paramBoolean);
    RST_AdviceItemDao.dropTable(paramSQLiteDatabase, paramBoolean);
    RST_ButtonDao.dropTable(paramSQLiteDatabase, paramBoolean);
    RST_SleepEventDao.dropTable(paramSQLiteDatabase, paramBoolean);
    RST_NightQuestionsDao.dropTable(paramSQLiteDatabase, paramBoolean);
    RST_QuestionItemDao.dropTable(paramSQLiteDatabase, paramBoolean);
    RST_DisplayItemDao.dropTable(paramSQLiteDatabase, paramBoolean);
    RST_ValueItemDao.dropTable(paramSQLiteDatabase, paramBoolean);
  }
  
  public DaoSession newSession()
  {
    return new DaoSession(this.db, IdentityScopeType.Session, this.daoConfigMap);
  }
  
  public DaoSession newSession(IdentityScopeType paramIdentityScopeType)
  {
    return new DaoSession(this.db, paramIdentityScopeType, this.daoConfigMap);
  }
  
  public static class DevOpenHelper
    extends DaoMaster.OpenHelper
  {
    public DevOpenHelper(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory)
    {
      super(paramString, paramCursorFactory);
    }
    
    /* Error */
    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      // Byte code:
      //   0: ldc 18
      //   2: new 20	java/lang/StringBuilder
      //   5: dup
      //   6: ldc 22
      //   8: invokespecial 25	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   11: iload_2
      //   12: invokevirtual 29	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   15: ldc 31
      //   17: invokevirtual 34	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   20: iload_3
      //   21: invokevirtual 29	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   24: invokevirtual 38	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   27: invokestatic 44	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
      //   30: pop
      //   31: iload_2
      //   32: iconst_3
      //   33: if_icmpgt +15 -> 48
      //   36: aload_1
      //   37: ldc 46
      //   39: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   42: aload_1
      //   43: ldc 53
      //   45: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   48: iload_2
      //   49: iconst_4
      //   50: if_icmpgt +9 -> 59
      //   53: aload_1
      //   54: ldc 55
      //   56: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   59: iload_2
      //   60: iconst_5
      //   61: if_icmpgt +15 -> 76
      //   64: aload_1
      //   65: ldc 57
      //   67: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   70: aload_1
      //   71: ldc 59
      //   73: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   76: iload_2
      //   77: bipush 7
      //   79: if_icmpgt +63 -> 142
      //   82: aload_1
      //   83: ldc 61
      //   85: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   88: aload_1
      //   89: ldc 63
      //   91: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   94: aload_1
      //   95: ldc 65
      //   97: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   100: aload_1
      //   101: ldc 67
      //   103: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   106: aload_1
      //   107: ldc 69
      //   109: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   112: aload_1
      //   113: ldc 71
      //   115: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   118: aload_1
      //   119: ldc 73
      //   121: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   124: aload_1
      //   125: ldc 75
      //   127: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   130: aload_1
      //   131: ldc 77
      //   133: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   136: aload_1
      //   137: ldc 79
      //   139: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   142: iload_2
      //   143: bipush 9
      //   145: if_icmpgt +321 -> 466
      //   148: aload_1
      //   149: ldc 81
      //   151: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   154: aload_1
      //   155: ldc 83
      //   157: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   160: aload_1
      //   161: ldc 85
      //   163: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   166: aload_1
      //   167: ldc 87
      //   169: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   172: aload_1
      //   173: ldc 89
      //   175: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   178: aload_1
      //   179: ldc 91
      //   181: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   184: aload_1
      //   185: ldc 93
      //   187: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   190: aload_1
      //   191: ldc 95
      //   193: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   196: aload_1
      //   197: ldc 97
      //   199: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   202: aload_1
      //   203: ldc 99
      //   205: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   208: aload_1
      //   209: ldc 101
      //   211: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   214: aload_1
      //   215: ldc 103
      //   217: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   220: aload_1
      //   221: ldc 105
      //   223: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   226: aload_1
      //   227: ldc 107
      //   229: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   232: aload_1
      //   233: ldc 109
      //   235: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   238: aload_1
      //   239: ldc 111
      //   241: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   244: aload_1
      //   245: ldc 113
      //   247: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   250: aload_1
      //   251: ldc 115
      //   253: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   256: aload_1
      //   257: ldc 117
      //   259: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   262: aload_1
      //   263: ldc 119
      //   265: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   268: aload_1
      //   269: ldc 121
      //   271: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   274: aload_1
      //   275: ldc 123
      //   277: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   280: aload_1
      //   281: ldc 125
      //   283: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   286: aload_1
      //   287: ldc 127
      //   289: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   292: aload_1
      //   293: ldc -127
      //   295: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   298: aload_1
      //   299: ldc -125
      //   301: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   304: aload_1
      //   305: ldc -123
      //   307: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   310: aload_1
      //   311: ldc -121
      //   313: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   316: aload_1
      //   317: ldc -119
      //   319: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   322: aload_1
      //   323: ldc -117
      //   325: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   328: aload_1
      //   329: ldc -115
      //   331: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   334: aload_1
      //   335: ldc -113
      //   337: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   340: aload_1
      //   341: ldc -111
      //   343: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   346: aload_1
      //   347: ldc -109
      //   349: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   352: aload_1
      //   353: ldc -107
      //   355: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   358: aload_1
      //   359: ldc -105
      //   361: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   364: aload_1
      //   365: ldc -103
      //   367: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   370: aload_1
      //   371: ldc -101
      //   373: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   376: aload_1
      //   377: ldc -99
      //   379: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   382: aload_1
      //   383: ldc -97
      //   385: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   388: aload_1
      //   389: ldc -95
      //   391: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   394: aload_1
      //   395: ldc -93
      //   397: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   400: aload_1
      //   401: ldc -91
      //   403: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   406: aload_1
      //   407: ldc -89
      //   409: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   412: aload_1
      //   413: ldc -87
      //   415: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   418: aload_1
      //   419: ldc -85
      //   421: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   424: aload_1
      //   425: ldc -83
      //   427: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   430: aload_1
      //   431: ldc -81
      //   433: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   436: aload_1
      //   437: ldc -79
      //   439: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   442: aload_1
      //   443: ldc -77
      //   445: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   448: aload_1
      //   449: ldc -75
      //   451: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   454: aload_1
      //   455: ldc -73
      //   457: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   460: aload_1
      //   461: ldc -71
      //   463: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   466: iload_2
      //   467: bipush 10
      //   469: if_icmpgt +75 -> 544
      //   472: aload_1
      //   473: ldc -69
      //   475: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   478: aload_1
      //   479: ldc -67
      //   481: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   484: aload_1
      //   485: ldc -65
      //   487: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   490: aload_1
      //   491: ldc -63
      //   493: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   496: aload_1
      //   497: ldc -61
      //   499: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   502: aload_1
      //   503: ldc -59
      //   505: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   508: aload_1
      //   509: ldc -57
      //   511: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   514: aload_1
      //   515: ldc -55
      //   517: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   520: aload_1
      //   521: ldc -53
      //   523: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   526: aload_1
      //   527: ldc -51
      //   529: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   532: aload_1
      //   533: ldc -49
      //   535: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   538: aload_1
      //   539: ldc -47
      //   541: invokevirtual 51	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
      //   544: return
      //   545: astore_1
      //   546: goto -2 -> 544
      //   549: astore_1
      //   550: goto -6 -> 544
      //   553: astore 4
      //   555: goto -89 -> 466
      //   558: astore 4
      //   560: goto -418 -> 142
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	563	0	this	DevOpenHelper
      //   0	563	1	paramSQLiteDatabase	SQLiteDatabase
      //   0	563	2	paramInt1	int
      //   0	563	3	paramInt2	int
      //   553	1	4	localException1	Exception
      //   558	1	4	localException2	Exception
      // Exception table:
      //   from	to	target	type
      //   472	532	545	java/lang/Exception
      //   532	544	549	java/lang/Exception
      //   148	466	553	java/lang/Exception
      //   82	142	558	java/lang/Exception
    }
  }
  
  public static abstract class OpenHelper
    extends SQLiteOpenHelper
  {
    public OpenHelper(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory)
    {
      super(paramString, paramCursorFactory, 10);
    }
    
    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      Log.i("greenDAO", "Creating tables for schema version 10");
      DaoMaster.createAllTables(paramSQLiteDatabase, false);
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\DaoMaster.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
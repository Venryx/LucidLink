package com.resmed.refresh.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.identityscope.IdentityScope;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RST_SleepSessionInfoDao
  extends AbstractDao<RST_SleepSessionInfo, Long>
{
  public static final String TABLENAME = "RST__SLEEP_SESSION_INFO";
  private DaoSession daoSession;
  private Query<RST_SleepSessionInfo> rST_User_SleepSessionsQuery;
  private String selectDeep;
  
  public RST_SleepSessionInfoDao(DaoConfig paramDaoConfig)
  {
    super(paramDaoConfig);
  }
  
  public RST_SleepSessionInfoDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession)
  {
    super(paramDaoConfig, paramDaoSession);
    this.daoSession = paramDaoSession;
  }
  
  public static void createTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = "IF NOT EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE " + str + "'RST__SLEEP_SESSION_INFO' (" + "'ID' INTEGER PRIMARY KEY NOT NULL UNIQUE ," + "'ALARM_FIRE_EPOCH' INTEGER NOT NULL ," + "'BIN_SLEEP_SCORE_DEEP' INTEGER NOT NULL ," + "'BIN_SLEEP_SCORE_LIGHT' INTEGER NOT NULL ," + "'BIN_SLEEP_SCORE_ONSET' INTEGER NOT NULL ," + "'BIN_SLEEP_SCORE_REM' INTEGER NOT NULL ," + "'BIN_SLEEP_SCORE_TST' INTEGER NOT NULL ," + "'BIN_SLEEP_SCORE_WASO' INTEGER NOT NULL ," + "'BODY_SCORE' INTEGER NOT NULL ," + "'COMPLETED' INTEGER NOT NULL ," + "'MIND_SCORE' INTEGER NOT NULL ," + "'NUMBER_INTERRUPTIONS' INTEGER NOT NULL ," + "'RECORDING_PERIOD' INTEGER NOT NULL ," + "'SIGNAL_QUALITY_IS_VALID' INTEGER NOT NULL ," + "'SIGNAL_QUALITY_MEAN' REAL NOT NULL ," + "'SIGNAL_QUALITY_PERC_BIN1' REAL NOT NULL ," + "'SIGNAL_QUALITY_PERC_BIN2' REAL NOT NULL ," + "'SIGNAL_QUALITY_PERC_BIN3' REAL NOT NULL ," + "'SIGNAL_QUALITY_PERC_BIN4' REAL NOT NULL ," + "'SIGNAL_QUALITY_PERC_BIN5' REAL NOT NULL ," + "'SIGNAL_QUALITY_STD' REAL NOT NULL ," + "'SIGNAL_QUALITY_VALUE' INTEGER NOT NULL ," + "'SLEEP_SCORE' INTEGER NOT NULL ," + "'START_TIME' INTEGER NOT NULL ," + "'STOP_TIME' INTEGER NOT NULL ," + "'TIME_IN_BED' INTEGER NOT NULL ," + "'TIME_TO_SLEEP' INTEGER NOT NULL ," + "'TOTAL_DEEP_SLEEP' INTEGER NOT NULL ," + "'TOTAL_LIGHT_SLEEP' INTEGER NOT NULL ," + "'TOTAL_REM' INTEGER NOT NULL ," + "'TOTAL_SLEEP_TIME' INTEGER NOT NULL ," + "'TOTAL_WAKE_TIME' INTEGER NOT NULL ," + "'UPLOADED' INTEGER NOT NULL ," + "'WASO_TIME' INTEGER NOT NULL ," + "'QUESTION_IDS' TEXT NOT NULL ," + "'ANSWER_VALUES' TEXT NOT NULL ," + "'ID_USER' TEXT," + "'ID_ENVIRONMENT' INTEGER);");
      return;
    }
  }
  
  public static void dropTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder("DROP TABLE ");
    if (paramBoolean) {}
    for (String str = "IF EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL(str + "'RST__SLEEP_SESSION_INFO'");
      return;
    }
  }
  
  public List<RST_SleepSessionInfo> _queryRST_User_SleepSessions(String paramString)
  {
    try
    {
      if (this.rST_User_SleepSessionsQuery == null)
      {
        localObject = queryBuilder();
        ((QueryBuilder)localObject).where(Properties.IdUser.eq(null), new WhereCondition[0]);
        this.rST_User_SleepSessionsQuery = ((QueryBuilder)localObject).build();
      }
      Object localObject = this.rST_User_SleepSessionsQuery.forCurrentThread();
      ((Query)localObject).setParameter(0, paramString);
      return ((Query)localObject).list();
    }
    finally {}
  }
  
  protected void attachEntity(RST_SleepSessionInfo paramRST_SleepSessionInfo)
  {
    super.attachEntity(paramRST_SleepSessionInfo);
    paramRST_SleepSessionInfo.__setDaoSession(this.daoSession);
  }
  
  protected void bindValues(SQLiteStatement paramSQLiteStatement, RST_SleepSessionInfo paramRST_SleepSessionInfo)
  {
    long l2 = 1L;
    paramSQLiteStatement.clearBindings();
    paramSQLiteStatement.bindLong(1, paramRST_SleepSessionInfo.getId());
    paramSQLiteStatement.bindLong(2, paramRST_SleepSessionInfo.getAlarmFireEpoch());
    paramSQLiteStatement.bindLong(3, paramRST_SleepSessionInfo.getBinSleepScoreDeep());
    paramSQLiteStatement.bindLong(4, paramRST_SleepSessionInfo.getBinSleepScoreLight());
    paramSQLiteStatement.bindLong(5, paramRST_SleepSessionInfo.getBinSleepScoreOnset());
    paramSQLiteStatement.bindLong(6, paramRST_SleepSessionInfo.getBinSleepScoreRem());
    paramSQLiteStatement.bindLong(7, paramRST_SleepSessionInfo.getBinSleepScoreTst());
    paramSQLiteStatement.bindLong(8, paramRST_SleepSessionInfo.getBinSleepScoreWaso());
    paramSQLiteStatement.bindLong(9, paramRST_SleepSessionInfo.getBodyScore());
    if (paramRST_SleepSessionInfo.getCompleted())
    {
      l1 = 1L;
      paramSQLiteStatement.bindLong(10, l1);
      paramSQLiteStatement.bindLong(11, paramRST_SleepSessionInfo.getMindScore());
      paramSQLiteStatement.bindLong(12, paramRST_SleepSessionInfo.getNumberInterruptions());
      paramSQLiteStatement.bindLong(13, paramRST_SleepSessionInfo.getRecordingPeriod());
      if (!paramRST_SleepSessionInfo.getSignalQualityIsValid()) {
        break label459;
      }
      l1 = 1L;
      label158:
      paramSQLiteStatement.bindLong(14, l1);
      paramSQLiteStatement.bindDouble(15, paramRST_SleepSessionInfo.getSignalQualityMean());
      paramSQLiteStatement.bindDouble(16, paramRST_SleepSessionInfo.getSignalQualityPercBin1());
      paramSQLiteStatement.bindDouble(17, paramRST_SleepSessionInfo.getSignalQualityPercBin2());
      paramSQLiteStatement.bindDouble(18, paramRST_SleepSessionInfo.getSignalQualityPercBin3());
      paramSQLiteStatement.bindDouble(19, paramRST_SleepSessionInfo.getSignalQualityPercBin4());
      paramSQLiteStatement.bindDouble(20, paramRST_SleepSessionInfo.getSignalQualityPercBin5());
      paramSQLiteStatement.bindDouble(21, paramRST_SleepSessionInfo.getSignalQualityStd());
      paramSQLiteStatement.bindLong(22, paramRST_SleepSessionInfo.getSignalQualityValue());
      paramSQLiteStatement.bindLong(23, paramRST_SleepSessionInfo.getSleepScore());
      paramSQLiteStatement.bindLong(24, paramRST_SleepSessionInfo.getStartTime().getTime());
      paramSQLiteStatement.bindLong(25, paramRST_SleepSessionInfo.getStopTime().getTime());
      paramSQLiteStatement.bindLong(26, paramRST_SleepSessionInfo.getTimeInBed());
      paramSQLiteStatement.bindLong(27, paramRST_SleepSessionInfo.getTimeToSleep());
      paramSQLiteStatement.bindLong(28, paramRST_SleepSessionInfo.getTotalDeepSleep());
      paramSQLiteStatement.bindLong(29, paramRST_SleepSessionInfo.getTotalLightSleep());
      paramSQLiteStatement.bindLong(30, paramRST_SleepSessionInfo.getTotalRem());
      paramSQLiteStatement.bindLong(31, paramRST_SleepSessionInfo.getTotalSleepTime());
      paramSQLiteStatement.bindLong(32, paramRST_SleepSessionInfo.getTotalWakeTime());
      if (!paramRST_SleepSessionInfo.getUploaded()) {
        break label464;
      }
    }
    label459:
    label464:
    for (long l1 = l2;; l1 = 0L)
    {
      paramSQLiteStatement.bindLong(33, l1);
      paramSQLiteStatement.bindLong(34, paramRST_SleepSessionInfo.getWasoTime());
      paramSQLiteStatement.bindString(35, paramRST_SleepSessionInfo.getQuestionIds());
      paramSQLiteStatement.bindString(36, paramRST_SleepSessionInfo.getAnswerValues());
      String str = paramRST_SleepSessionInfo.getIdUser();
      if (str != null) {
        paramSQLiteStatement.bindString(37, str);
      }
      paramRST_SleepSessionInfo = paramRST_SleepSessionInfo.getIdEnvironment();
      if (paramRST_SleepSessionInfo != null) {
        paramSQLiteStatement.bindLong(38, paramRST_SleepSessionInfo.longValue());
      }
      return;
      l1 = 0L;
      break;
      l1 = 0L;
      break label158;
    }
  }
  
  public Long getKey(RST_SleepSessionInfo paramRST_SleepSessionInfo)
  {
    if (paramRST_SleepSessionInfo != null) {}
    for (paramRST_SleepSessionInfo = Long.valueOf(paramRST_SleepSessionInfo.getId());; paramRST_SleepSessionInfo = null) {
      return paramRST_SleepSessionInfo;
    }
  }
  
  protected String getSelectDeep()
  {
    if (this.selectDeep == null)
    {
      StringBuilder localStringBuilder = new StringBuilder("SELECT ");
      SqlUtils.appendColumns(localStringBuilder, "T", getAllColumns());
      localStringBuilder.append(',');
      SqlUtils.appendColumns(localStringBuilder, "T0", this.daoSession.getRST_UserDao().getAllColumns());
      localStringBuilder.append(',');
      SqlUtils.appendColumns(localStringBuilder, "T1", this.daoSession.getRST_EnvironmentalInfoDao().getAllColumns());
      localStringBuilder.append(" FROM RST__SLEEP_SESSION_INFO T");
      localStringBuilder.append(" LEFT JOIN RST__USER T0 ON T.'ID_USER'=T0.'ID'");
      localStringBuilder.append(" LEFT JOIN RST__ENVIRONMENTAL_INFO T1 ON T.'ID_ENVIRONMENT'=T1.'_id'");
      localStringBuilder.append(' ');
      this.selectDeep = localStringBuilder.toString();
    }
    return this.selectDeep;
  }
  
  protected boolean isEntityUpdateable()
  {
    return true;
  }
  
  public List<RST_SleepSessionInfo> loadAllDeepFromCursor(Cursor paramCursor)
  {
    int i = paramCursor.getCount();
    ArrayList localArrayList = new ArrayList(i);
    if (paramCursor.moveToFirst()) {
      if (this.identityScope != null)
      {
        this.identityScope.lock();
        this.identityScope.reserveRoom(i);
      }
    }
    try
    {
      boolean bool;
      do
      {
        localArrayList.add(loadCurrentDeep(paramCursor, false));
        bool = paramCursor.moveToNext();
      } while (bool);
      return localArrayList;
    }
    finally
    {
      if (this.identityScope != null) {
        this.identityScope.unlock();
      }
    }
  }
  
  protected RST_SleepSessionInfo loadCurrentDeep(Cursor paramCursor, boolean paramBoolean)
  {
    RST_SleepSessionInfo localRST_SleepSessionInfo = (RST_SleepSessionInfo)loadCurrent(paramCursor, 0, paramBoolean);
    int i = getAllColumns().length;
    localRST_SleepSessionInfo.setRST_User((RST_User)loadCurrentOther(this.daoSession.getRST_UserDao(), paramCursor, i));
    int j = this.daoSession.getRST_UserDao().getAllColumns().length;
    localRST_SleepSessionInfo.setEnvironmentalInfo((RST_EnvironmentalInfo)loadCurrentOther(this.daoSession.getRST_EnvironmentalInfoDao(), paramCursor, i + j));
    return localRST_SleepSessionInfo;
  }
  
  public RST_SleepSessionInfo loadDeep(Long paramLong)
  {
    IllegalStateException localIllegalStateException = null;
    assertSinglePk();
    if (paramLong == null) {
      paramLong = localIllegalStateException;
    }
    for (;;)
    {
      return paramLong;
      Object localObject = new StringBuilder(getSelectDeep());
      ((StringBuilder)localObject).append("WHERE ");
      SqlUtils.appendColumnsEqValue((StringBuilder)localObject, "T", getPkColumns());
      localObject = ((StringBuilder)localObject).toString();
      paramLong = paramLong.toString();
      localObject = this.db.rawQuery((String)localObject, new String[] { paramLong });
      try
      {
        boolean bool = ((Cursor)localObject).moveToFirst();
        if (!bool)
        {
          ((Cursor)localObject).close();
          paramLong = localIllegalStateException;
          continue;
        }
        if (!((Cursor)localObject).isLast())
        {
          localIllegalStateException = new java/lang/IllegalStateException;
          paramLong = new java/lang/StringBuilder;
          paramLong.<init>("Expected unique result, but count was ");
          localIllegalStateException.<init>(((Cursor)localObject).getCount());
          throw localIllegalStateException;
        }
      }
      finally
      {
        ((Cursor)localObject).close();
      }
      paramLong = loadCurrentDeep((Cursor)localObject, true);
      ((Cursor)localObject).close();
    }
  }
  
  protected List<RST_SleepSessionInfo> loadDeepAllAndCloseCursor(Cursor paramCursor)
  {
    try
    {
      List localList = loadAllDeepFromCursor(paramCursor);
      return localList;
    }
    finally
    {
      paramCursor.close();
    }
  }
  
  public List<RST_SleepSessionInfo> queryDeep(String paramString, String... paramVarArgs)
  {
    return loadDeepAllAndCloseCursor(this.db.rawQuery(getSelectDeep() + paramString, paramVarArgs));
  }
  
  public RST_SleepSessionInfo readEntity(Cursor paramCursor, int paramInt)
  {
    long l = paramCursor.getLong(paramInt + 0);
    int i3 = paramCursor.getInt(paramInt + 1);
    int i12 = paramCursor.getInt(paramInt + 2);
    int i8 = paramCursor.getInt(paramInt + 3);
    int k = paramCursor.getInt(paramInt + 4);
    int i15 = paramCursor.getInt(paramInt + 5);
    int i14 = paramCursor.getInt(paramInt + 6);
    int i9 = paramCursor.getInt(paramInt + 7);
    int i4 = paramCursor.getInt(paramInt + 8);
    boolean bool1;
    int n;
    int i5;
    int i11;
    boolean bool2;
    label170:
    float f3;
    float f7;
    float f4;
    float f6;
    float f5;
    float f1;
    float f2;
    int m;
    int i1;
    Date localDate2;
    Date localDate1;
    int i2;
    int i16;
    int i6;
    int i7;
    int j;
    int i13;
    int i10;
    boolean bool3;
    label415:
    int i;
    String str2;
    String str3;
    String str1;
    if (paramCursor.getShort(paramInt + 9) != 0)
    {
      bool1 = true;
      n = paramCursor.getInt(paramInt + 10);
      i5 = paramCursor.getInt(paramInt + 11);
      i11 = paramCursor.getInt(paramInt + 12);
      if (paramCursor.getShort(paramInt + 13) == 0) {
        break label570;
      }
      bool2 = true;
      f3 = paramCursor.getFloat(paramInt + 14);
      f7 = paramCursor.getFloat(paramInt + 15);
      f4 = paramCursor.getFloat(paramInt + 16);
      f6 = paramCursor.getFloat(paramInt + 17);
      f5 = paramCursor.getFloat(paramInt + 18);
      f1 = paramCursor.getFloat(paramInt + 19);
      f2 = paramCursor.getFloat(paramInt + 20);
      m = paramCursor.getInt(paramInt + 21);
      i1 = paramCursor.getInt(paramInt + 22);
      localDate2 = new Date(paramCursor.getLong(paramInt + 23));
      localDate1 = new Date(paramCursor.getLong(paramInt + 24));
      i2 = paramCursor.getInt(paramInt + 25);
      i16 = paramCursor.getInt(paramInt + 26);
      i6 = paramCursor.getInt(paramInt + 27);
      i7 = paramCursor.getInt(paramInt + 28);
      j = paramCursor.getInt(paramInt + 29);
      i13 = paramCursor.getInt(paramInt + 30);
      i10 = paramCursor.getInt(paramInt + 31);
      if (paramCursor.getShort(paramInt + 32) == 0) {
        break label576;
      }
      bool3 = true;
      i = paramCursor.getInt(paramInt + 33);
      str2 = paramCursor.getString(paramInt + 34);
      str3 = paramCursor.getString(paramInt + 35);
      if (!paramCursor.isNull(paramInt + 36)) {
        break label582;
      }
      str1 = null;
      label467:
      if (!paramCursor.isNull(paramInt + 37)) {
        break label597;
      }
    }
    label570:
    label576:
    label582:
    label597:
    for (paramCursor = null;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 37)))
    {
      return new RST_SleepSessionInfo(l, i3, i12, i8, k, i15, i14, i9, i4, bool1, n, i5, i11, bool2, f3, f7, f4, f6, f5, f1, f2, m, i1, localDate2, localDate1, i2, i16, i6, i7, j, i13, i10, bool3, i, str2, str3, str1, paramCursor);
      bool1 = false;
      break;
      bool2 = false;
      break label170;
      bool3 = false;
      break label415;
      str1 = paramCursor.getString(paramInt + 36);
      break label467;
    }
  }
  
  public void readEntity(Cursor paramCursor, RST_SleepSessionInfo paramRST_SleepSessionInfo, int paramInt)
  {
    Object localObject = null;
    boolean bool2 = true;
    paramRST_SleepSessionInfo.setId(paramCursor.getLong(paramInt + 0));
    paramRST_SleepSessionInfo.setAlarmFireEpoch(paramCursor.getInt(paramInt + 1));
    paramRST_SleepSessionInfo.setBinSleepScoreDeep(paramCursor.getInt(paramInt + 2));
    paramRST_SleepSessionInfo.setBinSleepScoreLight(paramCursor.getInt(paramInt + 3));
    paramRST_SleepSessionInfo.setBinSleepScoreOnset(paramCursor.getInt(paramInt + 4));
    paramRST_SleepSessionInfo.setBinSleepScoreRem(paramCursor.getInt(paramInt + 5));
    paramRST_SleepSessionInfo.setBinSleepScoreTst(paramCursor.getInt(paramInt + 6));
    paramRST_SleepSessionInfo.setBinSleepScoreWaso(paramCursor.getInt(paramInt + 7));
    paramRST_SleepSessionInfo.setBodyScore(paramCursor.getInt(paramInt + 8));
    boolean bool1;
    label206:
    label495:
    String str;
    if (paramCursor.getShort(paramInt + 9) != 0)
    {
      bool1 = true;
      paramRST_SleepSessionInfo.setCompleted(bool1);
      paramRST_SleepSessionInfo.setMindScore(paramCursor.getInt(paramInt + 10));
      paramRST_SleepSessionInfo.setNumberInterruptions(paramCursor.getInt(paramInt + 11));
      paramRST_SleepSessionInfo.setRecordingPeriod(paramCursor.getInt(paramInt + 12));
      if (paramCursor.getShort(paramInt + 13) == 0) {
        break label593;
      }
      bool1 = true;
      paramRST_SleepSessionInfo.setSignalQualityIsValid(bool1);
      paramRST_SleepSessionInfo.setSignalQualityMean(paramCursor.getFloat(paramInt + 14));
      paramRST_SleepSessionInfo.setSignalQualityPercBin1(paramCursor.getFloat(paramInt + 15));
      paramRST_SleepSessionInfo.setSignalQualityPercBin2(paramCursor.getFloat(paramInt + 16));
      paramRST_SleepSessionInfo.setSignalQualityPercBin3(paramCursor.getFloat(paramInt + 17));
      paramRST_SleepSessionInfo.setSignalQualityPercBin4(paramCursor.getFloat(paramInt + 18));
      paramRST_SleepSessionInfo.setSignalQualityPercBin5(paramCursor.getFloat(paramInt + 19));
      paramRST_SleepSessionInfo.setSignalQualityStd(paramCursor.getFloat(paramInt + 20));
      paramRST_SleepSessionInfo.setSignalQualityValue(paramCursor.getInt(paramInt + 21));
      paramRST_SleepSessionInfo.setSleepScore(paramCursor.getInt(paramInt + 22));
      paramRST_SleepSessionInfo.setStartTime(new Date(paramCursor.getLong(paramInt + 23)));
      paramRST_SleepSessionInfo.setStopTime(new Date(paramCursor.getLong(paramInt + 24)));
      paramRST_SleepSessionInfo.setTimeInBed(paramCursor.getInt(paramInt + 25));
      paramRST_SleepSessionInfo.setTimeToSleep(paramCursor.getInt(paramInt + 26));
      paramRST_SleepSessionInfo.setTotalDeepSleep(paramCursor.getInt(paramInt + 27));
      paramRST_SleepSessionInfo.setTotalLightSleep(paramCursor.getInt(paramInt + 28));
      paramRST_SleepSessionInfo.setTotalRem(paramCursor.getInt(paramInt + 29));
      paramRST_SleepSessionInfo.setTotalSleepTime(paramCursor.getInt(paramInt + 30));
      paramRST_SleepSessionInfo.setTotalWakeTime(paramCursor.getInt(paramInt + 31));
      if (paramCursor.getShort(paramInt + 32) == 0) {
        break label599;
      }
      bool1 = bool2;
      paramRST_SleepSessionInfo.setUploaded(bool1);
      paramRST_SleepSessionInfo.setWasoTime(paramCursor.getInt(paramInt + 33));
      paramRST_SleepSessionInfo.setQuestionIds(paramCursor.getString(paramInt + 34));
      paramRST_SleepSessionInfo.setAnswerValues(paramCursor.getString(paramInt + 35));
      if (!paramCursor.isNull(paramInt + 36)) {
        break label605;
      }
      str = null;
      label559:
      paramRST_SleepSessionInfo.setIdUser(str);
      if (!paramCursor.isNull(paramInt + 37)) {
        break label620;
      }
    }
    label593:
    label599:
    label605:
    label620:
    for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 37)))
    {
      paramRST_SleepSessionInfo.setIdEnvironment(paramCursor);
      return;
      bool1 = false;
      break;
      bool1 = false;
      break label206;
      bool1 = false;
      break label495;
      str = paramCursor.getString(paramInt + 36);
      break label559;
    }
  }
  
  public Long readKey(Cursor paramCursor, int paramInt)
  {
    return Long.valueOf(paramCursor.getLong(paramInt + 0));
  }
  
  protected Long updateKeyAfterInsert(RST_SleepSessionInfo paramRST_SleepSessionInfo, long paramLong)
  {
    paramRST_SleepSessionInfo.setId(paramLong);
    return Long.valueOf(paramLong);
  }
  
  public static class Properties
  {
    public static final Property AlarmFireEpoch;
    public static final Property AnswerValues;
    public static final Property BinSleepScoreDeep;
    public static final Property BinSleepScoreLight;
    public static final Property BinSleepScoreOnset;
    public static final Property BinSleepScoreRem;
    public static final Property BinSleepScoreTst;
    public static final Property BinSleepScoreWaso;
    public static final Property BodyScore;
    public static final Property Completed;
    public static final Property Id = new Property(0, Long.TYPE, "id", true, "ID");
    public static final Property IdEnvironment = new Property(37, Long.class, "idEnvironment", false, "ID_ENVIRONMENT");
    public static final Property IdUser;
    public static final Property MindScore;
    public static final Property NumberInterruptions;
    public static final Property QuestionIds;
    public static final Property RecordingPeriod;
    public static final Property SignalQualityIsValid;
    public static final Property SignalQualityMean;
    public static final Property SignalQualityPercBin1;
    public static final Property SignalQualityPercBin2;
    public static final Property SignalQualityPercBin3;
    public static final Property SignalQualityPercBin4;
    public static final Property SignalQualityPercBin5;
    public static final Property SignalQualityStd;
    public static final Property SignalQualityValue;
    public static final Property SleepScore;
    public static final Property StartTime;
    public static final Property StopTime;
    public static final Property TimeInBed;
    public static final Property TimeToSleep;
    public static final Property TotalDeepSleep;
    public static final Property TotalLightSleep;
    public static final Property TotalRem;
    public static final Property TotalSleepTime;
    public static final Property TotalWakeTime;
    public static final Property Uploaded;
    public static final Property WasoTime;
    
    static
    {
      AlarmFireEpoch = new Property(1, Integer.TYPE, "alarmFireEpoch", false, "ALARM_FIRE_EPOCH");
      BinSleepScoreDeep = new Property(2, Integer.TYPE, "binSleepScoreDeep", false, "BIN_SLEEP_SCORE_DEEP");
      BinSleepScoreLight = new Property(3, Integer.TYPE, "binSleepScoreLight", false, "BIN_SLEEP_SCORE_LIGHT");
      BinSleepScoreOnset = new Property(4, Integer.TYPE, "binSleepScoreOnset", false, "BIN_SLEEP_SCORE_ONSET");
      BinSleepScoreRem = new Property(5, Integer.TYPE, "binSleepScoreRem", false, "BIN_SLEEP_SCORE_REM");
      BinSleepScoreTst = new Property(6, Integer.TYPE, "binSleepScoreTst", false, "BIN_SLEEP_SCORE_TST");
      BinSleepScoreWaso = new Property(7, Integer.TYPE, "binSleepScoreWaso", false, "BIN_SLEEP_SCORE_WASO");
      BodyScore = new Property(8, Integer.TYPE, "bodyScore", false, "BODY_SCORE");
      Completed = new Property(9, Boolean.TYPE, "completed", false, "COMPLETED");
      MindScore = new Property(10, Integer.TYPE, "mindScore", false, "MIND_SCORE");
      NumberInterruptions = new Property(11, Integer.TYPE, "numberInterruptions", false, "NUMBER_INTERRUPTIONS");
      RecordingPeriod = new Property(12, Integer.TYPE, "recordingPeriod", false, "RECORDING_PERIOD");
      SignalQualityIsValid = new Property(13, Boolean.TYPE, "signalQualityIsValid", false, "SIGNAL_QUALITY_IS_VALID");
      SignalQualityMean = new Property(14, Float.TYPE, "signalQualityMean", false, "SIGNAL_QUALITY_MEAN");
      SignalQualityPercBin1 = new Property(15, Float.TYPE, "signalQualityPercBin1", false, "SIGNAL_QUALITY_PERC_BIN1");
      SignalQualityPercBin2 = new Property(16, Float.TYPE, "signalQualityPercBin2", false, "SIGNAL_QUALITY_PERC_BIN2");
      SignalQualityPercBin3 = new Property(17, Float.TYPE, "signalQualityPercBin3", false, "SIGNAL_QUALITY_PERC_BIN3");
      SignalQualityPercBin4 = new Property(18, Float.TYPE, "signalQualityPercBin4", false, "SIGNAL_QUALITY_PERC_BIN4");
      SignalQualityPercBin5 = new Property(19, Float.TYPE, "signalQualityPercBin5", false, "SIGNAL_QUALITY_PERC_BIN5");
      SignalQualityStd = new Property(20, Float.TYPE, "signalQualityStd", false, "SIGNAL_QUALITY_STD");
      SignalQualityValue = new Property(21, Integer.TYPE, "signalQualityValue", false, "SIGNAL_QUALITY_VALUE");
      SleepScore = new Property(22, Integer.TYPE, "sleepScore", false, "SLEEP_SCORE");
      StartTime = new Property(23, Date.class, "startTime", false, "START_TIME");
      StopTime = new Property(24, Date.class, "stopTime", false, "STOP_TIME");
      TimeInBed = new Property(25, Integer.TYPE, "timeInBed", false, "TIME_IN_BED");
      TimeToSleep = new Property(26, Integer.TYPE, "timeToSleep", false, "TIME_TO_SLEEP");
      TotalDeepSleep = new Property(27, Integer.TYPE, "totalDeepSleep", false, "TOTAL_DEEP_SLEEP");
      TotalLightSleep = new Property(28, Integer.TYPE, "totalLightSleep", false, "TOTAL_LIGHT_SLEEP");
      TotalRem = new Property(29, Integer.TYPE, "totalRem", false, "TOTAL_REM");
      TotalSleepTime = new Property(30, Integer.TYPE, "totalSleepTime", false, "TOTAL_SLEEP_TIME");
      TotalWakeTime = new Property(31, Integer.TYPE, "totalWakeTime", false, "TOTAL_WAKE_TIME");
      Uploaded = new Property(32, Boolean.TYPE, "uploaded", false, "UPLOADED");
      WasoTime = new Property(33, Integer.TYPE, "wasoTime", false, "WASO_TIME");
      QuestionIds = new Property(34, String.class, "questionIds", false, "QUESTION_IDS");
      AnswerValues = new Property(35, String.class, "answerValues", false, "ANSWER_VALUES");
      IdUser = new Property(36, String.class, "idUser", false, "ID_USER");
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RST_SleepSessionInfoDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
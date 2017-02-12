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
import java.util.List;

public class RST_ValueItemDao
  extends AbstractDao<RST_ValueItem, Long>
{
  public static final String TABLENAME = "RST__VALUE_ITEM";
  private DaoSession daoSession;
  private Query<RST_ValueItem> rST_EnvironmentalInfo_SessionLightQuery;
  private Query<RST_ValueItem> rST_EnvironmentalInfo_SessionSoundQuery;
  private Query<RST_ValueItem> rST_EnvironmentalInfo_SessionTemperatureQuery;
  private Query<RST_ValueItem> rST_SleepSessionInfo_SleepSignalRatingsQuery;
  private Query<RST_ValueItem> rST_SleepSessionInfo_SleepStatesQuery;
  private String selectDeep;
  
  public RST_ValueItemDao(DaoConfig paramDaoConfig)
  {
    super(paramDaoConfig);
  }
  
  public RST_ValueItemDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession)
  {
    super(paramDaoConfig, paramDaoSession);
    this.daoSession = paramDaoSession;
  }
  
  public static void createTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = "IF NOT EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE " + str + "'RST__VALUE_ITEM' (" + "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + "'VALUE' REAL NOT NULL ," + "'ORDR' INTEGER NOT NULL ," + "'ID_ENVIRONMENTAL_INFO_L' INTEGER," + "'ID_ENVIRONMENTAL_INFO_T' INTEGER," + "'ID_ENVIRONMENTAL_INFO_S' INTEGER," + "'ID_SLEEP_STATES' INTEGER," + "'ID_SLEEP_SIGNAL_RATINGS' INTEGER);");
      return;
    }
  }
  
  public static void dropTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder("DROP TABLE ");
    if (paramBoolean) {}
    for (String str = "IF EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL(str + "'RST__VALUE_ITEM'");
      return;
    }
  }
  
  public List<RST_ValueItem> _queryRST_EnvironmentalInfo_SessionLight(Long paramLong)
  {
    try
    {
      if (this.rST_EnvironmentalInfo_SessionLightQuery == null)
      {
        localObject = queryBuilder();
        ((QueryBuilder)localObject).where(Properties.IdEnvironmentalInfoL.eq(null), new WhereCondition[0]);
        ((QueryBuilder)localObject).orderRaw("ORDR ASC");
        this.rST_EnvironmentalInfo_SessionLightQuery = ((QueryBuilder)localObject).build();
      }
      Object localObject = this.rST_EnvironmentalInfo_SessionLightQuery.forCurrentThread();
      ((Query)localObject).setParameter(0, paramLong);
      return ((Query)localObject).list();
    }
    finally {}
  }
  
  public List<RST_ValueItem> _queryRST_EnvironmentalInfo_SessionSound(Long paramLong)
  {
    try
    {
      if (this.rST_EnvironmentalInfo_SessionSoundQuery == null)
      {
        localObject = queryBuilder();
        ((QueryBuilder)localObject).where(Properties.IdEnvironmentalInfoS.eq(null), new WhereCondition[0]);
        ((QueryBuilder)localObject).orderRaw("ORDR ASC");
        this.rST_EnvironmentalInfo_SessionSoundQuery = ((QueryBuilder)localObject).build();
      }
      Object localObject = this.rST_EnvironmentalInfo_SessionSoundQuery.forCurrentThread();
      ((Query)localObject).setParameter(0, paramLong);
      return ((Query)localObject).list();
    }
    finally {}
  }
  
  public List<RST_ValueItem> _queryRST_EnvironmentalInfo_SessionTemperature(Long paramLong)
  {
    try
    {
      if (this.rST_EnvironmentalInfo_SessionTemperatureQuery == null)
      {
        localObject = queryBuilder();
        ((QueryBuilder)localObject).where(Properties.IdEnvironmentalInfoT.eq(null), new WhereCondition[0]);
        ((QueryBuilder)localObject).orderRaw("ORDR ASC");
        this.rST_EnvironmentalInfo_SessionTemperatureQuery = ((QueryBuilder)localObject).build();
      }
      Object localObject = this.rST_EnvironmentalInfo_SessionTemperatureQuery.forCurrentThread();
      ((Query)localObject).setParameter(0, paramLong);
      return ((Query)localObject).list();
    }
    finally {}
  }
  
  public List<RST_ValueItem> _queryRST_SleepSessionInfo_SleepSignalRatings(Long paramLong)
  {
    try
    {
      if (this.rST_SleepSessionInfo_SleepSignalRatingsQuery == null)
      {
        localObject = queryBuilder();
        ((QueryBuilder)localObject).where(Properties.IdSleepSignalRatings.eq(null), new WhereCondition[0]);
        ((QueryBuilder)localObject).orderRaw("ORDR ASC");
        this.rST_SleepSessionInfo_SleepSignalRatingsQuery = ((QueryBuilder)localObject).build();
      }
      Object localObject = this.rST_SleepSessionInfo_SleepSignalRatingsQuery.forCurrentThread();
      ((Query)localObject).setParameter(0, paramLong);
      return ((Query)localObject).list();
    }
    finally {}
  }
  
  public List<RST_ValueItem> _queryRST_SleepSessionInfo_SleepStates(Long paramLong)
  {
    try
    {
      if (this.rST_SleepSessionInfo_SleepStatesQuery == null)
      {
        localObject = queryBuilder();
        ((QueryBuilder)localObject).where(Properties.IdSleepStates.eq(null), new WhereCondition[0]);
        ((QueryBuilder)localObject).orderRaw("ORDR ASC");
        this.rST_SleepSessionInfo_SleepStatesQuery = ((QueryBuilder)localObject).build();
      }
      Object localObject = this.rST_SleepSessionInfo_SleepStatesQuery.forCurrentThread();
      ((Query)localObject).setParameter(0, paramLong);
      return ((Query)localObject).list();
    }
    finally {}
  }
  
  protected void attachEntity(RST_ValueItem paramRST_ValueItem)
  {
    super.attachEntity(paramRST_ValueItem);
    paramRST_ValueItem.__setDaoSession(this.daoSession);
  }
  
  protected void bindValues(SQLiteStatement paramSQLiteStatement, RST_ValueItem paramRST_ValueItem)
  {
    paramSQLiteStatement.clearBindings();
    Long localLong = paramRST_ValueItem.getId();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(1, localLong.longValue());
    }
    paramSQLiteStatement.bindDouble(2, paramRST_ValueItem.getValue());
    paramSQLiteStatement.bindLong(3, paramRST_ValueItem.getOrdr());
    localLong = paramRST_ValueItem.getIdEnvironmentalInfoL();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(4, localLong.longValue());
    }
    localLong = paramRST_ValueItem.getIdEnvironmentalInfoT();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(5, localLong.longValue());
    }
    localLong = paramRST_ValueItem.getIdEnvironmentalInfoS();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(6, localLong.longValue());
    }
    localLong = paramRST_ValueItem.getIdSleepStates();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(7, localLong.longValue());
    }
    paramRST_ValueItem = paramRST_ValueItem.getIdSleepSignalRatings();
    if (paramRST_ValueItem != null) {
      paramSQLiteStatement.bindLong(8, paramRST_ValueItem.longValue());
    }
  }
  
  public Long getKey(RST_ValueItem paramRST_ValueItem)
  {
    if (paramRST_ValueItem != null) {}
    for (paramRST_ValueItem = paramRST_ValueItem.getId();; paramRST_ValueItem = null) {
      return paramRST_ValueItem;
    }
  }
  
  protected String getSelectDeep()
  {
    if (this.selectDeep == null)
    {
      StringBuilder localStringBuilder = new StringBuilder("SELECT ");
      SqlUtils.appendColumns(localStringBuilder, "T", getAllColumns());
      localStringBuilder.append(',');
      SqlUtils.appendColumns(localStringBuilder, "T0", this.daoSession.getRST_EnvironmentalInfoDao().getAllColumns());
      localStringBuilder.append(',');
      SqlUtils.appendColumns(localStringBuilder, "T1", this.daoSession.getRST_EnvironmentalInfoDao().getAllColumns());
      localStringBuilder.append(',');
      SqlUtils.appendColumns(localStringBuilder, "T2", this.daoSession.getRST_EnvironmentalInfoDao().getAllColumns());
      localStringBuilder.append(',');
      SqlUtils.appendColumns(localStringBuilder, "T3", this.daoSession.getRST_SleepSessionInfoDao().getAllColumns());
      localStringBuilder.append(',');
      SqlUtils.appendColumns(localStringBuilder, "T4", this.daoSession.getRST_SleepSessionInfoDao().getAllColumns());
      localStringBuilder.append(" FROM RST__VALUE_ITEM T");
      localStringBuilder.append(" LEFT JOIN RST__ENVIRONMENTAL_INFO T0 ON T.'ID_ENVIRONMENTAL_INFO_L'=T0.'_id'");
      localStringBuilder.append(" LEFT JOIN RST__ENVIRONMENTAL_INFO T1 ON T.'ID_ENVIRONMENTAL_INFO_T'=T1.'_id'");
      localStringBuilder.append(" LEFT JOIN RST__ENVIRONMENTAL_INFO T2 ON T.'ID_ENVIRONMENTAL_INFO_S'=T2.'_id'");
      localStringBuilder.append(" LEFT JOIN RST__SLEEP_SESSION_INFO T3 ON T.'ID_SLEEP_STATES'=T3.'ID'");
      localStringBuilder.append(" LEFT JOIN RST__SLEEP_SESSION_INFO T4 ON T.'ID_SLEEP_SIGNAL_RATINGS'=T4.'ID'");
      localStringBuilder.append(' ');
      this.selectDeep = localStringBuilder.toString();
    }
    return this.selectDeep;
  }
  
  protected boolean isEntityUpdateable()
  {
    return true;
  }
  
  public List<RST_ValueItem> loadAllDeepFromCursor(Cursor paramCursor)
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
  
  protected RST_ValueItem loadCurrentDeep(Cursor paramCursor, boolean paramBoolean)
  {
    RST_ValueItem localRST_ValueItem = (RST_ValueItem)loadCurrent(paramCursor, 0, paramBoolean);
    int i = getAllColumns().length;
    localRST_ValueItem.setLight((RST_EnvironmentalInfo)loadCurrentOther(this.daoSession.getRST_EnvironmentalInfoDao(), paramCursor, i));
    i += this.daoSession.getRST_EnvironmentalInfoDao().getAllColumns().length;
    localRST_ValueItem.setTemperature((RST_EnvironmentalInfo)loadCurrentOther(this.daoSession.getRST_EnvironmentalInfoDao(), paramCursor, i));
    i += this.daoSession.getRST_EnvironmentalInfoDao().getAllColumns().length;
    localRST_ValueItem.setSound((RST_EnvironmentalInfo)loadCurrentOther(this.daoSession.getRST_EnvironmentalInfoDao(), paramCursor, i));
    i += this.daoSession.getRST_EnvironmentalInfoDao().getAllColumns().length;
    localRST_ValueItem.setSleepStates((RST_SleepSessionInfo)loadCurrentOther(this.daoSession.getRST_SleepSessionInfoDao(), paramCursor, i));
    int j = this.daoSession.getRST_SleepSessionInfoDao().getAllColumns().length;
    localRST_ValueItem.setSleepSignalRatings((RST_SleepSessionInfo)loadCurrentOther(this.daoSession.getRST_SleepSessionInfoDao(), paramCursor, i + j));
    return localRST_ValueItem;
  }
  
  public RST_ValueItem loadDeep(Long paramLong)
  {
    StringBuilder localStringBuilder = null;
    assertSinglePk();
    if (paramLong == null) {
      paramLong = localStringBuilder;
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
          paramLong = localStringBuilder;
          continue;
        }
        if (!((Cursor)localObject).isLast())
        {
          paramLong = new java/lang/IllegalStateException;
          localStringBuilder = new java.lang.StringBuilder;
          localStringBuilder.<init>("Expected unique result, but count was ");
          paramLong.<init>(((Cursor)localObject).getCount());
          throw paramLong;
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
  
  protected List<RST_ValueItem> loadDeepAllAndCloseCursor(Cursor paramCursor)
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
  
  public List<RST_ValueItem> queryDeep(String paramString, String... paramVarArgs)
  {
    return loadDeepAllAndCloseCursor(this.db.rawQuery(getSelectDeep() + paramString, paramVarArgs));
  }
  
  public RST_ValueItem readEntity(Cursor paramCursor, int paramInt)
  {
    Object localObject = null;
    Long localLong1;
    float f;
    int i;
    Long localLong2;
    label54:
    Long localLong3;
    label69:
    Long localLong4;
    label84:
    Long localLong5;
    if (paramCursor.isNull(paramInt + 0))
    {
      localLong1 = null;
      f = paramCursor.getFloat(paramInt + 1);
      i = paramCursor.getInt(paramInt + 2);
      if (!paramCursor.isNull(paramInt + 3)) {
        break label155;
      }
      localLong2 = null;
      if (!paramCursor.isNull(paramInt + 4)) {
        break label172;
      }
      localLong3 = null;
      if (!paramCursor.isNull(paramInt + 5)) {
        break label189;
      }
      localLong4 = null;
      if (!paramCursor.isNull(paramInt + 6)) {
        break label206;
      }
      localLong5 = null;
      label100:
      if (!paramCursor.isNull(paramInt + 7)) {
        break label224;
      }
    }
    label155:
    label172:
    label189:
    label206:
    label224:
    for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 7)))
    {
      return new RST_ValueItem(localLong1, f, i, localLong2, localLong3, localLong4, localLong5, paramCursor);
      localLong1 = Long.valueOf(paramCursor.getLong(paramInt + 0));
      break;
      localLong2 = Long.valueOf(paramCursor.getLong(paramInt + 3));
      break label54;
      localLong3 = Long.valueOf(paramCursor.getLong(paramInt + 4));
      break label69;
      localLong4 = Long.valueOf(paramCursor.getLong(paramInt + 5));
      break label84;
      localLong5 = Long.valueOf(paramCursor.getLong(paramInt + 6));
      break label100;
    }
  }
  
  public void readEntity(Cursor paramCursor, RST_ValueItem paramRST_ValueItem, int paramInt)
  {
    Object localObject = null;
    Long localLong;
    if (paramCursor.isNull(paramInt + 0))
    {
      localLong = null;
      paramRST_ValueItem.setId(localLong);
      paramRST_ValueItem.setValue(paramCursor.getFloat(paramInt + 1));
      paramRST_ValueItem.setOrdr(paramCursor.getInt(paramInt + 2));
      if (!paramCursor.isNull(paramInt + 3)) {
        break label174;
      }
      localLong = null;
      label65:
      paramRST_ValueItem.setIdEnvironmentalInfoL(localLong);
      if (!paramCursor.isNull(paramInt + 4)) {
        break label191;
      }
      localLong = null;
      label86:
      paramRST_ValueItem.setIdEnvironmentalInfoT(localLong);
      if (!paramCursor.isNull(paramInt + 5)) {
        break label208;
      }
      localLong = null;
      label107:
      paramRST_ValueItem.setIdEnvironmentalInfoS(localLong);
      if (!paramCursor.isNull(paramInt + 6)) {
        break label225;
      }
      localLong = null;
      label129:
      paramRST_ValueItem.setIdSleepStates(localLong);
      if (!paramCursor.isNull(paramInt + 7)) {
        break label243;
      }
    }
    label174:
    label191:
    label208:
    label225:
    label243:
    for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 7)))
    {
      paramRST_ValueItem.setIdSleepSignalRatings(paramCursor);
      return;
      localLong = Long.valueOf(paramCursor.getLong(paramInt + 0));
      break;
      localLong = Long.valueOf(paramCursor.getLong(paramInt + 3));
      break label65;
      localLong = Long.valueOf(paramCursor.getLong(paramInt + 4));
      break label86;
      localLong = Long.valueOf(paramCursor.getLong(paramInt + 5));
      break label107;
      localLong = Long.valueOf(paramCursor.getLong(paramInt + 6));
      break label129;
    }
  }
  
  public Long readKey(Cursor paramCursor, int paramInt)
  {
    if (paramCursor.isNull(paramInt + 0)) {}
    for (paramCursor = null;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 0))) {
      return paramCursor;
    }
  }
  
  protected Long updateKeyAfterInsert(RST_ValueItem paramRST_ValueItem, long paramLong)
  {
    paramRST_ValueItem.setId(Long.valueOf(paramLong));
    return Long.valueOf(paramLong);
  }
  
  public static class Properties
  {
    public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    public static final Property IdEnvironmentalInfoL;
    public static final Property IdEnvironmentalInfoS;
    public static final Property IdEnvironmentalInfoT;
    public static final Property IdSleepSignalRatings = new Property(7, Long.class, "idSleepSignalRatings", false, "ID_SLEEP_SIGNAL_RATINGS");
    public static final Property IdSleepStates;
    public static final Property Ordr;
    public static final Property Value = new Property(1, Float.TYPE, "value", false, "VALUE");
    
    static
    {
      Ordr = new Property(2, Integer.TYPE, "ordr", false, "ORDR");
      IdEnvironmentalInfoL = new Property(3, Long.class, "idEnvironmentalInfoL", false, "ID_ENVIRONMENTAL_INFO_L");
      IdEnvironmentalInfoT = new Property(4, Long.class, "idEnvironmentalInfoT", false, "ID_ENVIRONMENTAL_INFO_T");
      IdEnvironmentalInfoS = new Property(5, Long.class, "idEnvironmentalInfoS", false, "ID_ENVIRONMENTAL_INFO_S");
      IdSleepStates = new Property(6, Long.class, "idSleepStates", false, "ID_SLEEP_STATES");
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RST_ValueItemDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
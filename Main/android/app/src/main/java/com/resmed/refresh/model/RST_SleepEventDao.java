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

public class RST_SleepEventDao
  extends AbstractDao<RST_SleepEvent, Long>
{
  public static final String TABLENAME = "RST__SLEEP_EVENT";
  private DaoSession daoSession;
  private Query<RST_SleepEvent> rST_SleepSessionInfo_EventsQuery;
  private String selectDeep;
  
  public RST_SleepEventDao(DaoConfig paramDaoConfig)
  {
    super(paramDaoConfig);
  }
  
  public RST_SleepEventDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession)
  {
    super(paramDaoConfig, paramDaoSession);
    this.daoSession = paramDaoSession;
  }
  
  public static void createTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = "IF NOT EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE " + str + "'RST__SLEEP_EVENT' (" + "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + "'EPOCH_NUMBER' INTEGER NOT NULL ," + "'TYPE' INTEGER NOT NULL ," + "'VALUE' INTEGER NOT NULL ," + "'ID_SLEEP_SESSION' INTEGER);");
      return;
    }
  }
  
  public static void dropTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder("DROP TABLE ");
    if (paramBoolean) {}
    for (String str = "IF EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL(str + "'RST__SLEEP_EVENT'");
      return;
    }
  }
  
  public List<RST_SleepEvent> _queryRST_SleepSessionInfo_Events(Long paramLong)
  {
    try
    {
      if (this.rST_SleepSessionInfo_EventsQuery == null)
      {
        localObject = queryBuilder();
        ((QueryBuilder)localObject).where(Properties.IdSleepSession.eq(null), new WhereCondition[0]);
        this.rST_SleepSessionInfo_EventsQuery = ((QueryBuilder)localObject).build();
      }
      Object localObject = this.rST_SleepSessionInfo_EventsQuery.forCurrentThread();
      ((Query)localObject).setParameter(0, paramLong);
      return ((Query)localObject).list();
    }
    finally {}
  }
  
  protected void attachEntity(RST_SleepEvent paramRST_SleepEvent)
  {
    super.attachEntity(paramRST_SleepEvent);
    paramRST_SleepEvent.__setDaoSession(this.daoSession);
  }
  
  protected void bindValues(SQLiteStatement paramSQLiteStatement, RST_SleepEvent paramRST_SleepEvent)
  {
    paramSQLiteStatement.clearBindings();
    Long localLong = paramRST_SleepEvent.getId();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(1, localLong.longValue());
    }
    paramSQLiteStatement.bindLong(2, paramRST_SleepEvent.getEpochNumber());
    paramSQLiteStatement.bindLong(3, paramRST_SleepEvent.getType());
    paramSQLiteStatement.bindLong(4, paramRST_SleepEvent.getValue());
    paramRST_SleepEvent = paramRST_SleepEvent.getIdSleepSession();
    if (paramRST_SleepEvent != null) {
      paramSQLiteStatement.bindLong(5, paramRST_SleepEvent.longValue());
    }
  }
  
  public Long getKey(RST_SleepEvent paramRST_SleepEvent)
  {
    if (paramRST_SleepEvent != null) {}
    for (paramRST_SleepEvent = paramRST_SleepEvent.getId();; paramRST_SleepEvent = null) {
      return paramRST_SleepEvent;
    }
  }
  
  protected String getSelectDeep()
  {
    if (this.selectDeep == null)
    {
      StringBuilder localStringBuilder = new StringBuilder("SELECT ");
      SqlUtils.appendColumns(localStringBuilder, "T", getAllColumns());
      localStringBuilder.append(',');
      SqlUtils.appendColumns(localStringBuilder, "T0", this.daoSession.getRST_SleepSessionInfoDao().getAllColumns());
      localStringBuilder.append(" FROM RST__SLEEP_EVENT T");
      localStringBuilder.append(" LEFT JOIN RST__SLEEP_SESSION_INFO T0 ON T.'ID_SLEEP_SESSION'=T0.'ID'");
      localStringBuilder.append(' ');
      this.selectDeep = localStringBuilder.toString();
    }
    return this.selectDeep;
  }
  
  protected boolean isEntityUpdateable()
  {
    return true;
  }
  
  public List<RST_SleepEvent> loadAllDeepFromCursor(Cursor paramCursor)
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
  
  protected RST_SleepEvent loadCurrentDeep(Cursor paramCursor, boolean paramBoolean)
  {
    RST_SleepEvent localRST_SleepEvent = (RST_SleepEvent)loadCurrent(paramCursor, 0, paramBoolean);
    int i = getAllColumns().length;
    localRST_SleepEvent.setRST_SleepSessionInfo((RST_SleepSessionInfo)loadCurrentOther(this.daoSession.getRST_SleepSessionInfoDao(), paramCursor, i));
    return localRST_SleepEvent;
  }
  
  public RST_SleepEvent loadDeep(Long paramLong)
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
  
  protected List<RST_SleepEvent> loadDeepAllAndCloseCursor(Cursor paramCursor)
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
  
  public List<RST_SleepEvent> queryDeep(String paramString, String... paramVarArgs)
  {
    return loadDeepAllAndCloseCursor(this.db.rawQuery(getSelectDeep() + paramString, paramVarArgs));
  }
  
  public RST_SleepEvent readEntity(Cursor paramCursor, int paramInt)
  {
    Object localObject = null;
    Long localLong;
    int k;
    int j;
    int i;
    if (paramCursor.isNull(paramInt + 0))
    {
      localLong = null;
      k = paramCursor.getInt(paramInt + 1);
      j = paramCursor.getInt(paramInt + 2);
      i = paramCursor.getInt(paramInt + 3);
      if (!paramCursor.isNull(paramInt + 4)) {
        break label98;
      }
    }
    label98:
    for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 4)))
    {
      return new RST_SleepEvent(localLong, k, j, i, paramCursor);
      localLong = Long.valueOf(paramCursor.getLong(paramInt + 0));
      break;
    }
  }
  
  public void readEntity(Cursor paramCursor, RST_SleepEvent paramRST_SleepEvent, int paramInt)
  {
    Object localObject = null;
    Long localLong;
    if (paramCursor.isNull(paramInt + 0))
    {
      localLong = null;
      paramRST_SleepEvent.setId(localLong);
      paramRST_SleepEvent.setEpochNumber(paramCursor.getInt(paramInt + 1));
      paramRST_SleepEvent.setType(paramCursor.getInt(paramInt + 2));
      paramRST_SleepEvent.setValue(paramCursor.getInt(paramInt + 3));
      if (!paramCursor.isNull(paramInt + 4)) {
        break label101;
      }
    }
    label101:
    for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 4)))
    {
      paramRST_SleepEvent.setIdSleepSession(paramCursor);
      return;
      localLong = Long.valueOf(paramCursor.getLong(paramInt + 0));
      break;
    }
  }
  
  public Long readKey(Cursor paramCursor, int paramInt)
  {
    if (paramCursor.isNull(paramInt + 0)) {}
    for (paramCursor = null;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 0))) {
      return paramCursor;
    }
  }
  
  protected Long updateKeyAfterInsert(RST_SleepEvent paramRST_SleepEvent, long paramLong)
  {
    paramRST_SleepEvent.setId(Long.valueOf(paramLong));
    return Long.valueOf(paramLong);
  }
  
  public static class Properties
  {
    public static final Property EpochNumber;
    public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    public static final Property IdSleepSession = new Property(4, Long.class, "idSleepSession", false, "ID_SLEEP_SESSION");
    public static final Property Type;
    public static final Property Value;
    
    static
    {
      EpochNumber = new Property(1, Integer.TYPE, "epochNumber", false, "EPOCH_NUMBER");
      Type = new Property(2, Integer.TYPE, "type", false, "TYPE");
      Value = new Property(3, Integer.TYPE, "value", false, "VALUE");
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RST_SleepEventDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.identityscope.IdentityScope;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;
import java.util.ArrayList;
import java.util.List;

public class RST_EnvironmentalInfoDao
  extends AbstractDao<RST_EnvironmentalInfo, Long>
{
  public static final String TABLENAME = "RST__ENVIRONMENTAL_INFO";
  private DaoSession daoSession;
  private String selectDeep;
  
  public RST_EnvironmentalInfoDao(DaoConfig paramDaoConfig)
  {
    super(paramDaoConfig);
  }
  
  public RST_EnvironmentalInfoDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession)
  {
    super(paramDaoConfig, paramDaoSession);
    this.daoSession = paramDaoSession;
  }
  
  public static void createTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = "IF NOT EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE " + str + "'RST__ENVIRONMENTAL_INFO' (" + "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + "'CURRENT_TEMPERATURE' REAL NOT NULL ," + "'CURRENT_LIGHT' REAL NOT NULL ," + "'CURRENT_SOUND' REAL NOT NULL ," + "'ID_SESSION' INTEGER);");
      return;
    }
  }
  
  public static void dropTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder("DROP TABLE ");
    if (paramBoolean) {}
    for (String str = "IF EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL(str + "'RST__ENVIRONMENTAL_INFO'");
      return;
    }
  }
  
  protected void attachEntity(RST_EnvironmentalInfo paramRST_EnvironmentalInfo)
  {
    super.attachEntity(paramRST_EnvironmentalInfo);
    paramRST_EnvironmentalInfo.__setDaoSession(this.daoSession);
  }
  
  protected void bindValues(SQLiteStatement paramSQLiteStatement, RST_EnvironmentalInfo paramRST_EnvironmentalInfo)
  {
    paramSQLiteStatement.clearBindings();
    Long localLong = paramRST_EnvironmentalInfo.getId();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(1, localLong.longValue());
    }
    paramSQLiteStatement.bindDouble(2, paramRST_EnvironmentalInfo.getCurrentTemperature());
    paramSQLiteStatement.bindDouble(3, paramRST_EnvironmentalInfo.getCurrentLight());
    paramSQLiteStatement.bindDouble(4, paramRST_EnvironmentalInfo.getCurrentSound());
    paramRST_EnvironmentalInfo = paramRST_EnvironmentalInfo.getIdSession();
    if (paramRST_EnvironmentalInfo != null) {
      paramSQLiteStatement.bindLong(5, paramRST_EnvironmentalInfo.longValue());
    }
  }
  
  public Long getKey(RST_EnvironmentalInfo paramRST_EnvironmentalInfo)
  {
    if (paramRST_EnvironmentalInfo != null) {}
    for (paramRST_EnvironmentalInfo = paramRST_EnvironmentalInfo.getId();; paramRST_EnvironmentalInfo = null) {
      return paramRST_EnvironmentalInfo;
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
      localStringBuilder.append(" FROM RST__ENVIRONMENTAL_INFO T");
      localStringBuilder.append(" LEFT JOIN RST__SLEEP_SESSION_INFO T0 ON T.'ID_SESSION'=T0.'ID'");
      localStringBuilder.append(' ');
      this.selectDeep = localStringBuilder.toString();
    }
    return this.selectDeep;
  }
  
  protected boolean isEntityUpdateable()
  {
    return true;
  }
  
  public List<RST_EnvironmentalInfo> loadAllDeepFromCursor(Cursor paramCursor)
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
  
  protected RST_EnvironmentalInfo loadCurrentDeep(Cursor paramCursor, boolean paramBoolean)
  {
    RST_EnvironmentalInfo localRST_EnvironmentalInfo = (RST_EnvironmentalInfo)loadCurrent(paramCursor, 0, paramBoolean);
    int i = getAllColumns().length;
    localRST_EnvironmentalInfo.setSession((RST_SleepSessionInfo)loadCurrentOther(this.daoSession.getRST_SleepSessionInfoDao(), paramCursor, i));
    return localRST_EnvironmentalInfo;
  }
  
  public RST_EnvironmentalInfo loadDeep(Long paramLong)
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
          paramLong = new java.lang.StringBuilder;
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
  
  protected List<RST_EnvironmentalInfo> loadDeepAllAndCloseCursor(Cursor paramCursor)
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
  
  public List<RST_EnvironmentalInfo> queryDeep(String paramString, String... paramVarArgs)
  {
    return loadDeepAllAndCloseCursor(this.db.rawQuery(getSelectDeep() + paramString, paramVarArgs));
  }
  
  public RST_EnvironmentalInfo readEntity(Cursor paramCursor, int paramInt)
  {
    Object localObject = null;
    Long localLong;
    float f3;
    float f1;
    float f2;
    if (paramCursor.isNull(paramInt + 0))
    {
      localLong = null;
      f3 = paramCursor.getFloat(paramInt + 1);
      f1 = paramCursor.getFloat(paramInt + 2);
      f2 = paramCursor.getFloat(paramInt + 3);
      if (!paramCursor.isNull(paramInt + 4)) {
        break label98;
      }
    }
    label98:
    for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 4)))
    {
      return new RST_EnvironmentalInfo(localLong, f3, f1, f2, paramCursor);
      localLong = Long.valueOf(paramCursor.getLong(paramInt + 0));
      break;
    }
  }
  
  public void readEntity(Cursor paramCursor, RST_EnvironmentalInfo paramRST_EnvironmentalInfo, int paramInt)
  {
    Object localObject = null;
    Long localLong;
    if (paramCursor.isNull(paramInt + 0))
    {
      localLong = null;
      paramRST_EnvironmentalInfo.setId(localLong);
      paramRST_EnvironmentalInfo.setCurrentTemperature(paramCursor.getFloat(paramInt + 1));
      paramRST_EnvironmentalInfo.setCurrentLight(paramCursor.getFloat(paramInt + 2));
      paramRST_EnvironmentalInfo.setCurrentSound(paramCursor.getFloat(paramInt + 3));
      if (!paramCursor.isNull(paramInt + 4)) {
        break label101;
      }
    }
    label101:
    for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 4)))
    {
      paramRST_EnvironmentalInfo.setIdSession(paramCursor);
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
  
  protected Long updateKeyAfterInsert(RST_EnvironmentalInfo paramRST_EnvironmentalInfo, long paramLong)
  {
    paramRST_EnvironmentalInfo.setId(Long.valueOf(paramLong));
    return Long.valueOf(paramLong);
  }
  
  public static class Properties
  {
    public static final Property CurrentLight = new Property(2, Float.TYPE, "currentLight", false, "CURRENT_LIGHT");
    public static final Property CurrentSound = new Property(3, Float.TYPE, "currentSound", false, "CURRENT_SOUND");
    public static final Property CurrentTemperature;
    public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    public static final Property IdSession = new Property(4, Long.class, "idSession", false, "ID_SESSION");
    
    static
    {
      CurrentTemperature = new Property(1, Float.TYPE, "currentTemperature", false, "CURRENT_TEMPERATURE");
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RST_EnvironmentalInfoDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
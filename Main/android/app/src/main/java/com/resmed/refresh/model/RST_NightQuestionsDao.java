package com.resmed.refresh.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class RST_NightQuestionsDao
  extends AbstractDao<RST_NightQuestions, Long>
{
  public static final String TABLENAME = "RST__NIGHT_QUESTIONS";
  private DaoSession daoSession;
  
  public RST_NightQuestionsDao(DaoConfig paramDaoConfig)
  {
    super(paramDaoConfig);
  }
  
  public RST_NightQuestionsDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession)
  {
    super(paramDaoConfig, paramDaoSession);
    this.daoSession = paramDaoSession;
  }
  
  public static void createTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = "IF NOT EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE " + str + "'RST__NIGHT_QUESTIONS' (" + "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + "'VERSION' INTEGER NOT NULL );");
      return;
    }
  }
  
  public static void dropTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder("DROP TABLE ");
    if (paramBoolean) {}
    for (String str = "IF EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL(str + "'RST__NIGHT_QUESTIONS'");
      return;
    }
  }
  
  protected void attachEntity(RST_NightQuestions paramRST_NightQuestions)
  {
    super.attachEntity(paramRST_NightQuestions);
    paramRST_NightQuestions.__setDaoSession(this.daoSession);
  }
  
  protected void bindValues(SQLiteStatement paramSQLiteStatement, RST_NightQuestions paramRST_NightQuestions)
  {
    paramSQLiteStatement.clearBindings();
    Long localLong = paramRST_NightQuestions.getId();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(1, localLong.longValue());
    }
    paramSQLiteStatement.bindLong(2, paramRST_NightQuestions.getVersion());
  }
  
  public Long getKey(RST_NightQuestions paramRST_NightQuestions)
  {
    if (paramRST_NightQuestions != null) {}
    for (paramRST_NightQuestions = paramRST_NightQuestions.getId();; paramRST_NightQuestions = null) {
      return paramRST_NightQuestions;
    }
  }
  
  protected boolean isEntityUpdateable()
  {
    return true;
  }
  
  public RST_NightQuestions readEntity(Cursor paramCursor, int paramInt)
  {
    if (paramCursor.isNull(paramInt + 0)) {}
    for (Long localLong = null;; localLong = Long.valueOf(paramCursor.getLong(paramInt + 0))) {
      return new RST_NightQuestions(localLong, paramCursor.getInt(paramInt + 1));
    }
  }
  
  public void readEntity(Cursor paramCursor, RST_NightQuestions paramRST_NightQuestions, int paramInt)
  {
    if (paramCursor.isNull(paramInt + 0)) {}
    for (Long localLong = null;; localLong = Long.valueOf(paramCursor.getLong(paramInt + 0)))
    {
      paramRST_NightQuestions.setId(localLong);
      paramRST_NightQuestions.setVersion(paramCursor.getInt(paramInt + 1));
      return;
    }
  }
  
  public Long readKey(Cursor paramCursor, int paramInt)
  {
    if (paramCursor.isNull(paramInt + 0)) {}
    for (paramCursor = null;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 0))) {
      return paramCursor;
    }
  }
  
  protected Long updateKeyAfterInsert(RST_NightQuestions paramRST_NightQuestions, long paramLong)
  {
    paramRST_NightQuestions.setId(Long.valueOf(paramLong));
    return Long.valueOf(paramLong);
  }
  
  public static class Properties
  {
    public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    public static final Property Version = new Property(1, Integer.TYPE, "version", false, "VERSION");
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RST_NightQuestionsDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
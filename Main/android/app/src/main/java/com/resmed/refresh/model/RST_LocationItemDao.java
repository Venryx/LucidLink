package com.resmed.refresh.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class RST_LocationItemDao
  extends AbstractDao<RST_LocationItem, Long>
{
  public static final String TABLENAME = "RST__LOCATION_ITEM";
  
  public RST_LocationItemDao(DaoConfig paramDaoConfig)
  {
    super(paramDaoConfig);
  }
  
  public RST_LocationItemDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession)
  {
    super(paramDaoConfig, paramDaoSession);
  }
  
  public static void createTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = "IF NOT EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE " + str + "'RST__LOCATION_ITEM' (" + "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + "'DIRECTION' REAL NOT NULL ," + "'LATITUDE' REAL NOT NULL ," + "'LONGITUDE' REAL NOT NULL ," + "'LOCALE' TEXT NOT NULL ," + "'TIMEZONE' INTEGER NOT NULL );");
      return;
    }
  }
  
  public static void dropTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder("DROP TABLE ");
    if (paramBoolean) {}
    for (String str = "IF EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL(str + "'RST__LOCATION_ITEM'");
      return;
    }
  }
  
  protected void bindValues(SQLiteStatement paramSQLiteStatement, RST_LocationItem paramRST_LocationItem)
  {
    paramSQLiteStatement.clearBindings();
    Long localLong = paramRST_LocationItem.getId();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(1, localLong.longValue());
    }
    paramSQLiteStatement.bindDouble(2, paramRST_LocationItem.getDirection());
    paramSQLiteStatement.bindDouble(3, paramRST_LocationItem.getLatitude());
    paramSQLiteStatement.bindDouble(4, paramRST_LocationItem.getLongitude());
    paramSQLiteStatement.bindString(5, paramRST_LocationItem.getLocale());
    paramSQLiteStatement.bindLong(6, paramRST_LocationItem.getTimezone());
  }
  
  public Long getKey(RST_LocationItem paramRST_LocationItem)
  {
    if (paramRST_LocationItem != null) {}
    for (paramRST_LocationItem = paramRST_LocationItem.getId();; paramRST_LocationItem = null) {
      return paramRST_LocationItem;
    }
  }
  
  protected boolean isEntityUpdateable()
  {
    return true;
  }
  
  public RST_LocationItem readEntity(Cursor paramCursor, int paramInt)
  {
    if (paramCursor.isNull(paramInt + 0)) {}
    for (Long localLong = null;; localLong = Long.valueOf(paramCursor.getLong(paramInt + 0))) {
      return new RST_LocationItem(localLong, paramCursor.getFloat(paramInt + 1), paramCursor.getFloat(paramInt + 2), paramCursor.getFloat(paramInt + 3), paramCursor.getString(paramInt + 4), paramCursor.getInt(paramInt + 5));
    }
  }
  
  public void readEntity(Cursor paramCursor, RST_LocationItem paramRST_LocationItem, int paramInt)
  {
    if (paramCursor.isNull(paramInt + 0)) {}
    for (Long localLong = null;; localLong = Long.valueOf(paramCursor.getLong(paramInt + 0)))
    {
      paramRST_LocationItem.setId(localLong);
      paramRST_LocationItem.setDirection(paramCursor.getFloat(paramInt + 1));
      paramRST_LocationItem.setLatitude(paramCursor.getFloat(paramInt + 2));
      paramRST_LocationItem.setLongitude(paramCursor.getFloat(paramInt + 3));
      paramRST_LocationItem.setLocale(paramCursor.getString(paramInt + 4));
      paramRST_LocationItem.setTimezone(paramCursor.getInt(paramInt + 5));
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
  
  protected Long updateKeyAfterInsert(RST_LocationItem paramRST_LocationItem, long paramLong)
  {
    paramRST_LocationItem.setId(Long.valueOf(paramLong));
    return Long.valueOf(paramLong);
  }
  
  public static class Properties
  {
    public static final Property Direction;
    public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    public static final Property Latitude;
    public static final Property Locale = new Property(4, String.class, "locale", false, "LOCALE");
    public static final Property Longitude;
    public static final Property Timezone = new Property(5, Integer.TYPE, "timezone", false, "TIMEZONE");
    
    static
    {
      Direction = new Property(1, Float.TYPE, "direction", false, "DIRECTION");
      Latitude = new Property(2, Float.TYPE, "latitude", false, "LATITUDE");
      Longitude = new Property(3, Float.TYPE, "longitude", false, "LONGITUDE");
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RST_LocationItemDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
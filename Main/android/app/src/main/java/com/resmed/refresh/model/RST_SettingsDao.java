package com.resmed.refresh.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class RST_SettingsDao
  extends AbstractDao<RST_Settings, Long>
{
  public static final String TABLENAME = "RST__SETTINGS";
  
  public RST_SettingsDao(DaoConfig paramDaoConfig)
  {
    super(paramDaoConfig);
  }
  
  public RST_SettingsDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession)
  {
    super(paramDaoConfig, paramDaoSession);
  }
  
  public static void createTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = "IF NOT EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE " + str + "'RST__SETTINGS' (" + "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + "'FIRMWARE_VERSION' INTEGER NOT NULL ," + "'LOCATION_PERMISSION' INTEGER NOT NULL ," + "'USE_METRIC_UNITS' INTEGER NOT NULL ," + "'PUSH_NOTIFICATIONS' INTEGER NOT NULL ," + "'TAC1' INTEGER NOT NULL ," + "'TAC2' INTEGER NOT NULL ," + "'TAC3' INTEGER NOT NULL," + "'HEIGHT_UNIT' INTEGER NOT NULL, " + "'WEIGHT_UNIT' INTEGER NOT NULL," + "'TEMPERATURE_UNIT' INTEGER NOT NULL," + "'LOCALE' TEXT NOT NULL," + "'COUNTRY_CODE' TEXT NOT NULL" + ");");
      return;
    }
  }
  
  public static void dropTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder("DROP TABLE ");
    if (paramBoolean) {}
    for (String str = "IF EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL(str + "'RST__SETTINGS'");
      return;
    }
  }
  
  protected void bindValues(SQLiteStatement paramSQLiteStatement, RST_Settings paramRST_Settings)
  {
    long l2 = 1L;
    paramSQLiteStatement.clearBindings();
    Long localLong = paramRST_Settings.getId();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(1, localLong.longValue());
    }
    long l1;
    if (paramRST_Settings.getFirmwareVersion())
    {
      l1 = 1L;
      paramSQLiteStatement.bindLong(2, l1);
      if (!paramRST_Settings.getLocationPermission()) {
        break label210;
      }
      l1 = 1L;
      label52:
      paramSQLiteStatement.bindLong(3, l1);
      if (!paramRST_Settings.getUseMetricUnits()) {
        break label215;
      }
      l1 = 1L;
      label67:
      paramSQLiteStatement.bindLong(4, l1);
      if (!paramRST_Settings.getPushNotifications()) {
        break label220;
      }
      l1 = 1L;
      label82:
      paramSQLiteStatement.bindLong(5, l1);
      if (!paramRST_Settings.getTac1()) {
        break label225;
      }
      l1 = 1L;
      label97:
      paramSQLiteStatement.bindLong(6, l1);
      if (!paramRST_Settings.getTac2()) {
        break label230;
      }
      l1 = 1L;
      label113:
      paramSQLiteStatement.bindLong(7, l1);
      if (!paramRST_Settings.getTac3()) {
        break label235;
      }
      l1 = l2;
      label130:
      paramSQLiteStatement.bindLong(8, l1);
      paramSQLiteStatement.bindLong(9, paramRST_Settings.getHeightUnit());
      paramSQLiteStatement.bindLong(10, paramRST_Settings.getWeightUnit());
      paramSQLiteStatement.bindLong(11, paramRST_Settings.getTemperatureUnit());
      if (paramRST_Settings.getLocale() == null) {
        break label240;
      }
      paramSQLiteStatement.bindString(12, paramRST_Settings.getLocale());
      label187:
      if (paramRST_Settings.getCountryCode() == null) {
        break label251;
      }
      paramSQLiteStatement.bindString(13, paramRST_Settings.getCountryCode());
    }
    for (;;)
    {
      return;
      l1 = 0L;
      break;
      label210:
      l1 = 0L;
      break label52;
      label215:
      l1 = 0L;
      break label67;
      label220:
      l1 = 0L;
      break label82;
      label225:
      l1 = 0L;
      break label97;
      label230:
      l1 = 0L;
      break label113;
      label235:
      l1 = 0L;
      break label130;
      label240:
      paramSQLiteStatement.bindString(12, "");
      break label187;
      label251:
      paramSQLiteStatement.bindString(13, "");
    }
  }
  
  public Long getKey(RST_Settings paramRST_Settings)
  {
    if (paramRST_Settings != null) {}
    for (paramRST_Settings = paramRST_Settings.getId();; paramRST_Settings = null) {
      return paramRST_Settings;
    }
  }
  
  protected boolean isEntityUpdateable()
  {
    return true;
  }
  
  public RST_Settings readEntity(Cursor paramCursor, int paramInt)
  {
    Long localLong;
    boolean bool1;
    label30:
    boolean bool2;
    label45:
    boolean bool3;
    label60:
    boolean bool4;
    label75:
    boolean bool5;
    label90:
    boolean bool6;
    label106:
    boolean bool7;
    label122:
    int k;
    int i;
    int j;
    String str;
    if (paramCursor.isNull(paramInt + 0))
    {
      localLong = null;
      if (paramCursor.getShort(paramInt + 1) == 0) {
        break label239;
      }
      bool1 = true;
      if (paramCursor.getShort(paramInt + 2) == 0) {
        break label245;
      }
      bool2 = true;
      if (paramCursor.getShort(paramInt + 3) == 0) {
        break label251;
      }
      bool3 = true;
      if (paramCursor.getShort(paramInt + 4) == 0) {
        break label257;
      }
      bool4 = true;
      if (paramCursor.getShort(paramInt + 5) == 0) {
        break label263;
      }
      bool5 = true;
      if (paramCursor.getShort(paramInt + 6) == 0) {
        break label269;
      }
      bool6 = true;
      if (paramCursor.getShort(paramInt + 7) == 0) {
        break label275;
      }
      bool7 = true;
      k = paramCursor.getInt(paramInt + 8);
      i = paramCursor.getInt(paramInt + 9);
      j = paramCursor.getInt(paramInt + 10);
      if (!paramCursor.isNull(paramInt + 11)) {
        break label281;
      }
      str = "";
      label174:
      if (!paramCursor.isNull(paramInt + 12)) {
        break label296;
      }
    }
    label239:
    label245:
    label251:
    label257:
    label263:
    label269:
    label275:
    label281:
    label296:
    for (paramCursor = "";; paramCursor = paramCursor.getString(paramInt + 12))
    {
      return new RST_Settings(localLong, bool1, bool2, bool3, bool4, bool5, bool6, bool7, k, i, j, str, paramCursor);
      localLong = Long.valueOf(paramCursor.getLong(paramInt + 0));
      break;
      bool1 = false;
      break label30;
      bool2 = false;
      break label45;
      bool3 = false;
      break label60;
      bool4 = false;
      break label75;
      bool5 = false;
      break label90;
      bool6 = false;
      break label106;
      bool7 = false;
      break label122;
      str = paramCursor.getString(paramInt + 11);
      break label174;
    }
  }
  
  public void readEntity(Cursor paramCursor, RST_Settings paramRST_Settings, int paramInt)
  {
    boolean bool2 = true;
    Object localObject;
    boolean bool1;
    if (paramCursor.isNull(paramInt + 0))
    {
      localObject = null;
      paramRST_Settings.setId((Long)localObject);
      if (paramCursor.getShort(paramInt + 1) == 0) {
        break label278;
      }
      bool1 = true;
      label39:
      paramRST_Settings.setFirmwareVersion(bool1);
      if (paramCursor.getShort(paramInt + 2) == 0) {
        break label284;
      }
      bool1 = true;
      label60:
      paramRST_Settings.setLocationPermission(bool1);
      if (paramCursor.getShort(paramInt + 3) == 0) {
        break label290;
      }
      bool1 = true;
      label81:
      paramRST_Settings.setUseMetricUnits(bool1);
      if (paramCursor.getShort(paramInt + 4) == 0) {
        break label296;
      }
      bool1 = true;
      label102:
      paramRST_Settings.setPushNotifications(bool1);
      if (paramCursor.getShort(paramInt + 5) == 0) {
        break label302;
      }
      bool1 = true;
      label123:
      paramRST_Settings.setTac1(bool1);
      if (paramCursor.getShort(paramInt + 6) == 0) {
        break label308;
      }
      bool1 = true;
      label145:
      paramRST_Settings.setTac2(bool1);
      if (paramCursor.getShort(paramInt + 7) == 0) {
        break label314;
      }
      bool1 = bool2;
      label168:
      paramRST_Settings.setTac3(bool1);
      paramRST_Settings.setHeightUnit(paramCursor.getInt(paramInt + 8));
      paramRST_Settings.setWeightUnit(paramCursor.getInt(paramInt + 9));
      paramRST_Settings.setTemperatureUnit(paramCursor.getInt(paramInt + 10));
      if (!paramCursor.isNull(paramInt + 11)) {
        break label320;
      }
      localObject = "";
      label233:
      paramRST_Settings.setLocale((String)localObject);
      if (!paramCursor.isNull(paramInt + 12)) {
        break label335;
      }
    }
    label278:
    label284:
    label290:
    label296:
    label302:
    label308:
    label314:
    label320:
    label335:
    for (paramCursor = "";; paramCursor = paramCursor.getString(paramInt + 12))
    {
      paramRST_Settings.setCountryCode(paramCursor);
      return;
      localObject = Long.valueOf(paramCursor.getLong(paramInt + 0));
      break;
      bool1 = false;
      break label39;
      bool1 = false;
      break label60;
      bool1 = false;
      break label81;
      bool1 = false;
      break label102;
      bool1 = false;
      break label123;
      bool1 = false;
      break label145;
      bool1 = false;
      break label168;
      localObject = paramCursor.getString(paramInt + 11);
      break label233;
    }
  }
  
  public Long readKey(Cursor paramCursor, int paramInt)
  {
    if (paramCursor.isNull(paramInt + 0)) {}
    for (paramCursor = null;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 0))) {
      return paramCursor;
    }
  }
  
  protected Long updateKeyAfterInsert(RST_Settings paramRST_Settings, long paramLong)
  {
    paramRST_Settings.setId(Long.valueOf(paramLong));
    return Long.valueOf(paramLong);
  }
  
  public static class Properties
  {
    public static final Property FirmwareVersion;
    public static final Property HeightUnit;
    public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    public static final Property LocationPermission;
    public static final Property PushNotifications;
    public static final Property Tac1;
    public static final Property Tac2;
    public static final Property Tac3;
    public static final Property TemperatureUnit;
    public static final Property UseMetricUnits;
    public static final Property WeightUnit;
    public static final Property countryCode = new Property(12, String.class, "countryCode", false, "COUNTRY_CODE");
    public static final Property locale;
    
    static
    {
      FirmwareVersion = new Property(1, Boolean.TYPE, "firmwareVersion", false, "FIRMWARE_VERSION");
      LocationPermission = new Property(2, Boolean.TYPE, "locationPermission", false, "LOCATION_PERMISSION");
      UseMetricUnits = new Property(3, Boolean.TYPE, "useMetricUnits", false, "USE_METRIC_UNITS");
      PushNotifications = new Property(4, Boolean.TYPE, "pushNotifications", false, "PUSH_NOTIFICATIONS");
      Tac1 = new Property(5, Boolean.TYPE, "tac1", false, "TAC1");
      Tac2 = new Property(6, Boolean.TYPE, "tac2", false, "TAC2");
      Tac3 = new Property(7, Boolean.TYPE, "tac3", false, "TAC3");
      HeightUnit = new Property(8, Integer.TYPE, "heightUnit", false, "HEIGHT_UNIT");
      WeightUnit = new Property(9, Integer.TYPE, "weightUnit", false, "WEIGHT_UNIT");
      TemperatureUnit = new Property(10, Integer.TYPE, "temperatureUnit", false, "TEMPERATURE_UNIT");
      locale = new Property(11, String.class, "locale", false, "LOCALE");
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RST_SettingsDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
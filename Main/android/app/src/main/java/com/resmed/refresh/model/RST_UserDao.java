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

public class RST_UserDao
  extends AbstractDao<RST_User, String>
{
  public static final String TABLENAME = "RST__USER";
  private DaoSession daoSession;
  private String selectDeep;
  
  public RST_UserDao(DaoConfig paramDaoConfig)
  {
    super(paramDaoConfig);
  }
  
  public RST_UserDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession)
  {
    super(paramDaoConfig, paramDaoSession);
    this.daoSession = paramDaoSession;
  }
  
  public static void createTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = "IF NOT EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE " + str + "'RST__USER' (" + "'ID' TEXT PRIMARY KEY NOT NULL UNIQUE ," + "'EMAIL' TEXT NOT NULL ," + "'FAMILY_NAME' TEXT NOT NULL ," + "'FIRST_NAME' TEXT NOT NULL ," + "'PASSWORD' TEXT NOT NULL ," + "'PROFILE_ID' INTEGER," + "'SETTINGS_ID' INTEGER," + "'LOCATION_ID' INTEGER," + "'ID_NIGHT_QUESTION' INTEGER);");
      return;
    }
  }
  
  public static void dropTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder("DROP TABLE ");
    if (paramBoolean) {}
    for (String str = "IF EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL(str + "'RST__USER'");
      return;
    }
  }
  
  protected void attachEntity(RST_User paramRST_User)
  {
    super.attachEntity(paramRST_User);
    paramRST_User.__setDaoSession(this.daoSession);
  }
  
  protected void bindValues(SQLiteStatement paramSQLiteStatement, RST_User paramRST_User)
  {
    paramSQLiteStatement.clearBindings();
    paramSQLiteStatement.bindString(1, paramRST_User.getId());
    paramSQLiteStatement.bindString(2, paramRST_User.getEmail());
    paramSQLiteStatement.bindString(3, paramRST_User.getFamilyName());
    paramSQLiteStatement.bindString(4, paramRST_User.getFirstName());
    paramSQLiteStatement.bindString(5, paramRST_User.getPassword());
    Long localLong = paramRST_User.getProfileId();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(6, localLong.longValue());
    }
    localLong = paramRST_User.getSettingsId();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(7, localLong.longValue());
    }
    localLong = paramRST_User.getLocationId();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(8, localLong.longValue());
    }
    paramRST_User = paramRST_User.getIdNightQuestion();
    if (paramRST_User != null) {
      paramSQLiteStatement.bindLong(9, paramRST_User.longValue());
    }
  }
  
  public String getKey(RST_User paramRST_User)
  {
    if (paramRST_User != null) {}
    for (paramRST_User = paramRST_User.getId();; paramRST_User = null) {
      return paramRST_User;
    }
  }
  
  protected String getSelectDeep()
  {
    if (this.selectDeep == null)
    {
      StringBuilder localStringBuilder = new StringBuilder("SELECT ");
      SqlUtils.appendColumns(localStringBuilder, "T", getAllColumns());
      localStringBuilder.append(',');
      SqlUtils.appendColumns(localStringBuilder, "T0", this.daoSession.getRST_UserProfileDao().getAllColumns());
      localStringBuilder.append(',');
      SqlUtils.appendColumns(localStringBuilder, "T1", this.daoSession.getRST_SettingsDao().getAllColumns());
      localStringBuilder.append(',');
      SqlUtils.appendColumns(localStringBuilder, "T2", this.daoSession.getRST_LocationItemDao().getAllColumns());
      localStringBuilder.append(',');
      SqlUtils.appendColumns(localStringBuilder, "T3", this.daoSession.getRST_NightQuestionsDao().getAllColumns());
      localStringBuilder.append(" FROM RST__USER T");
      localStringBuilder.append(" LEFT JOIN RST__USER_PROFILE T0 ON T.'PROFILE_ID'=T0.'_id'");
      localStringBuilder.append(" LEFT JOIN RST__SETTINGS T1 ON T.'SETTINGS_ID'=T1.'_id'");
      localStringBuilder.append(" LEFT JOIN RST__LOCATION_ITEM T2 ON T.'LOCATION_ID'=T2.'_id'");
      localStringBuilder.append(" LEFT JOIN RST__NIGHT_QUESTIONS T3 ON T.'ID_NIGHT_QUESTION'=T3.'_id'");
      localStringBuilder.append(' ');
      this.selectDeep = localStringBuilder.toString();
    }
    return this.selectDeep;
  }
  
  protected boolean isEntityUpdateable()
  {
    return true;
  }
  
  public List<RST_User> loadAllDeepFromCursor(Cursor paramCursor)
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
  
  protected RST_User loadCurrentDeep(Cursor paramCursor, boolean paramBoolean)
  {
    RST_User localRST_User = (RST_User)loadCurrent(paramCursor, 0, paramBoolean);
    int i = getAllColumns().length;
    localRST_User.setProfile((RST_UserProfile)loadCurrentOther(this.daoSession.getRST_UserProfileDao(), paramCursor, i));
    i += this.daoSession.getRST_UserProfileDao().getAllColumns().length;
    localRST_User.setSettings((RST_Settings)loadCurrentOther(this.daoSession.getRST_SettingsDao(), paramCursor, i));
    i += this.daoSession.getRST_SettingsDao().getAllColumns().length;
    localRST_User.setLocation((RST_LocationItem)loadCurrentOther(this.daoSession.getRST_LocationItemDao(), paramCursor, i));
    int j = this.daoSession.getRST_LocationItemDao().getAllColumns().length;
    localRST_User.setNightQuestions((RST_NightQuestions)loadCurrentOther(this.daoSession.getRST_NightQuestionsDao(), paramCursor, i + j));
    return localRST_User;
  }
  
  public RST_User loadDeep(Long paramLong)
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
  
  protected List<RST_User> loadDeepAllAndCloseCursor(Cursor paramCursor)
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
  
  public List<RST_User> queryDeep(String paramString, String... paramVarArgs)
  {
    return loadDeepAllAndCloseCursor(this.db.rawQuery(getSelectDeep() + paramString, paramVarArgs));
  }
  
  public RST_User readEntity(Cursor paramCursor, int paramInt)
  {
    Object localObject = null;
    String str1 = paramCursor.getString(paramInt + 0);
    String str2 = paramCursor.getString(paramInt + 1);
    String str5 = paramCursor.getString(paramInt + 2);
    String str4 = paramCursor.getString(paramInt + 3);
    String str3 = paramCursor.getString(paramInt + 4);
    Long localLong1;
    Long localLong2;
    label88:
    Long localLong3;
    if (paramCursor.isNull(paramInt + 5))
    {
      localLong1 = null;
      if (!paramCursor.isNull(paramInt + 6)) {
        break label160;
      }
      localLong2 = null;
      if (!paramCursor.isNull(paramInt + 7)) {
        break label178;
      }
      localLong3 = null;
      label104:
      if (!paramCursor.isNull(paramInt + 8)) {
        break label196;
      }
    }
    label160:
    label178:
    label196:
    for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 8)))
    {
      return new RST_User(str1, str2, str5, str4, str3, localLong1, localLong2, localLong3, paramCursor);
      localLong1 = Long.valueOf(paramCursor.getLong(paramInt + 5));
      break;
      localLong2 = Long.valueOf(paramCursor.getLong(paramInt + 6));
      break label88;
      localLong3 = Long.valueOf(paramCursor.getLong(paramInt + 7));
      break label104;
    }
  }
  
  public void readEntity(Cursor paramCursor, RST_User paramRST_User, int paramInt)
  {
    Object localObject = null;
    paramRST_User.setId(paramCursor.getString(paramInt + 0));
    paramRST_User.setEmail(paramCursor.getString(paramInt + 1));
    paramRST_User.setFamilyName(paramCursor.getString(paramInt + 2));
    paramRST_User.setFirstName(paramCursor.getString(paramInt + 3));
    paramRST_User.setPassword(paramCursor.getString(paramInt + 4));
    Long localLong;
    if (paramCursor.isNull(paramInt + 5))
    {
      localLong = null;
      paramRST_User.setProfileId(localLong);
      if (!paramCursor.isNull(paramInt + 6)) {
        break label172;
      }
      localLong = null;
      label105:
      paramRST_User.setSettingsId(localLong);
      if (!paramCursor.isNull(paramInt + 7)) {
        break label190;
      }
      localLong = null;
      label127:
      paramRST_User.setLocationId(localLong);
      if (!paramCursor.isNull(paramInt + 8)) {
        break label208;
      }
    }
    label172:
    label190:
    label208:
    for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 8)))
    {
      paramRST_User.setIdNightQuestion(paramCursor);
      return;
      localLong = Long.valueOf(paramCursor.getLong(paramInt + 5));
      break;
      localLong = Long.valueOf(paramCursor.getLong(paramInt + 6));
      break label105;
      localLong = Long.valueOf(paramCursor.getLong(paramInt + 7));
      break label127;
    }
  }
  
  public String readKey(Cursor paramCursor, int paramInt)
  {
    return paramCursor.getString(paramInt + 0);
  }
  
  protected String updateKeyAfterInsert(RST_User paramRST_User, long paramLong)
  {
    return paramRST_User.getId();
  }
  
  public static class Properties
  {
    public static final Property Email;
    public static final Property FamilyName;
    public static final Property FirstName;
    public static final Property Id = new Property(0, String.class, "id", true, "ID");
    public static final Property IdNightQuestion = new Property(8, Long.class, "idNightQuestion", false, "ID_NIGHT_QUESTION");
    public static final Property LocationId;
    public static final Property Password;
    public static final Property ProfileId;
    public static final Property SettingsId;
    
    static
    {
      Email = new Property(1, String.class, "email", false, "EMAIL");
      FamilyName = new Property(2, String.class, "familyName", false, "FAMILY_NAME");
      FirstName = new Property(3, String.class, "firstName", false, "FIRST_NAME");
      Password = new Property(4, String.class, "password", false, "PASSWORD");
      ProfileId = new Property(5, Long.class, "profileId", false, "PROFILE_ID");
      SettingsId = new Property(6, Long.class, "settingsId", false, "SETTINGS_ID");
      LocationId = new Property(7, Long.class, "locationId", false, "LOCATION_ID");
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RST_UserDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
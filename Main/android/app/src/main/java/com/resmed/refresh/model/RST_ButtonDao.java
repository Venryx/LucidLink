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

public class RST_ButtonDao
  extends AbstractDao<RST_Button, Long>
{
  public static final String TABLENAME = "RST__BUTTON";
  private DaoSession daoSession;
  private Query<RST_Button> rST_AdviceItem_ButtonsQuery;
  private String selectDeep;
  
  public RST_ButtonDao(DaoConfig paramDaoConfig)
  {
    super(paramDaoConfig);
  }
  
  public RST_ButtonDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession)
  {
    super(paramDaoConfig, paramDaoSession);
    this.daoSession = paramDaoSession;
  }
  
  public static void createTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = "IF NOT EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE " + str + "'RST__BUTTON' (" + "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + "'BUTTON_ID' INTEGER NOT NULL ," + "'ORDR' INTEGER NOT NULL ," + "'TITLE' TEXT NOT NULL ," + "'ICON' TEXT NOT NULL ," + "'COLOUR' TEXT NOT NULL ," + "'ID_ADVICE_BUTTON' INTEGER);");
      return;
    }
  }
  
  public static void dropTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder("DROP TABLE ");
    if (paramBoolean) {}
    for (String str = "IF EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL(str + "'RST__BUTTON'");
      return;
    }
  }
  
  public List<RST_Button> _queryRST_AdviceItem_Buttons(Long paramLong)
  {
    try
    {
      if (this.rST_AdviceItem_ButtonsQuery == null)
      {
        localObject = queryBuilder();
        ((QueryBuilder)localObject).where(Properties.IdAdviceButton.eq(null), new WhereCondition[0]);
        ((QueryBuilder)localObject).orderRaw("ORDR ASC");
        this.rST_AdviceItem_ButtonsQuery = ((QueryBuilder)localObject).build();
      }
      Object localObject = this.rST_AdviceItem_ButtonsQuery.forCurrentThread();
      ((Query)localObject).setParameter(0, paramLong);
      return ((Query)localObject).list();
    }
    finally {}
  }
  
  protected void attachEntity(RST_Button paramRST_Button)
  {
    super.attachEntity(paramRST_Button);
    paramRST_Button.__setDaoSession(this.daoSession);
  }
  
  protected void bindValues(SQLiteStatement paramSQLiteStatement, RST_Button paramRST_Button)
  {
    paramSQLiteStatement.clearBindings();
    Long localLong = paramRST_Button.getId();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(1, localLong.longValue());
    }
    paramSQLiteStatement.bindLong(2, paramRST_Button.getButtonId());
    paramSQLiteStatement.bindLong(3, paramRST_Button.getOrdr());
    paramSQLiteStatement.bindString(4, paramRST_Button.getTitle());
    paramSQLiteStatement.bindString(5, paramRST_Button.getIcon());
    paramSQLiteStatement.bindString(6, paramRST_Button.getColour());
    paramRST_Button = paramRST_Button.getIdAdviceButton();
    if (paramRST_Button != null) {
      paramSQLiteStatement.bindLong(7, paramRST_Button.longValue());
    }
  }
  
  public Long getKey(RST_Button paramRST_Button)
  {
    if (paramRST_Button != null) {}
    for (paramRST_Button = paramRST_Button.getId();; paramRST_Button = null) {
      return paramRST_Button;
    }
  }
  
  protected String getSelectDeep()
  {
    if (this.selectDeep == null)
    {
      StringBuilder localStringBuilder = new StringBuilder("SELECT ");
      SqlUtils.appendColumns(localStringBuilder, "T", getAllColumns());
      localStringBuilder.append(',');
      SqlUtils.appendColumns(localStringBuilder, "T0", this.daoSession.getRST_AdviceItemDao().getAllColumns());
      localStringBuilder.append(" FROM RST__BUTTON T");
      localStringBuilder.append(" LEFT JOIN RST__ADVICE_ITEM T0 ON T.'ID_ADVICE_BUTTON'=T0.'ID'");
      localStringBuilder.append(' ');
      this.selectDeep = localStringBuilder.toString();
    }
    return this.selectDeep;
  }
  
  protected boolean isEntityUpdateable()
  {
    return true;
  }
  
  public List<RST_Button> loadAllDeepFromCursor(Cursor paramCursor)
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
  
  protected RST_Button loadCurrentDeep(Cursor paramCursor, boolean paramBoolean)
  {
    RST_Button localRST_Button = (RST_Button)loadCurrent(paramCursor, 0, paramBoolean);
    int i = getAllColumns().length;
    localRST_Button.setRST_AdviceItem((RST_AdviceItem)loadCurrentOther(this.daoSession.getRST_AdviceItemDao(), paramCursor, i));
    return localRST_Button;
  }
  
  public RST_Button loadDeep(Long paramLong)
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
		Cursor query = this.db.rawQuery((String)localObject, new String[] { paramLong });
      try
      {
        boolean bool = query.moveToFirst();
        if (!bool)
        {
			query.close();
          paramLong = localStringBuilder;
          continue;
        }
        if (!query.isLast())
        {
          paramLong = new java.lang.IllegalStateException(query.getCount());
          localStringBuilder = new java.lang.StringBuilder("Expected unique result, but count was ");
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
  
  protected List<RST_Button> loadDeepAllAndCloseCursor(Cursor paramCursor)
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
  
  public List<RST_Button> queryDeep(String paramString, String... paramVarArgs)
  {
    return loadDeepAllAndCloseCursor(this.db.rawQuery(getSelectDeep() + paramString, paramVarArgs));
  }
  
  public RST_Button readEntity(Cursor paramCursor, int paramInt)
  {
    Object localObject = null;
    Long localLong;
    long l;
    int i;
    String str2;
    String str1;
    String str3;
    if (paramCursor.isNull(paramInt + 0))
    {
      localLong = null;
      l = paramCursor.getLong(paramInt + 1);
      i = paramCursor.getInt(paramInt + 2);
      str2 = paramCursor.getString(paramInt + 3);
      str1 = paramCursor.getString(paramInt + 4);
      str3 = paramCursor.getString(paramInt + 5);
      if (!paramCursor.isNull(paramInt + 6)) {
        break label125;
      }
    }
    label125:
    for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 6)))
    {
      return new RST_Button(localLong, l, i, str2, str1, str3, paramCursor);
      localLong = Long.valueOf(paramCursor.getLong(paramInt + 0));
      break;
    }
  }
  
  public void readEntity(Cursor paramCursor, RST_Button paramRST_Button, int paramInt)
  {
    Object localObject = null;
    Long localLong;
    if (paramCursor.isNull(paramInt + 0))
    {
      localLong = null;
      paramRST_Button.setId(localLong);
      paramRST_Button.setButtonId(paramCursor.getLong(paramInt + 1));
      paramRST_Button.setOrdr(paramCursor.getInt(paramInt + 2));
      paramRST_Button.setTitle(paramCursor.getString(paramInt + 3));
      paramRST_Button.setIcon(paramCursor.getString(paramInt + 4));
      paramRST_Button.setColour(paramCursor.getString(paramInt + 5));
      if (!paramCursor.isNull(paramInt + 6)) {
        break label128;
      }
    }
    label128:
    for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 6)))
    {
      paramRST_Button.setIdAdviceButton(paramCursor);
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
  
  protected Long updateKeyAfterInsert(RST_Button paramRST_Button, long paramLong)
  {
    paramRST_Button.setId(Long.valueOf(paramLong));
    return Long.valueOf(paramLong);
  }
  
  public static class Properties
  {
    public static final Property ButtonId;
    public static final Property Colour = new Property(5, String.class, "colour", false, "COLOUR");
    public static final Property Icon;
    public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    public static final Property IdAdviceButton = new Property(6, Long.class, "idAdviceButton", false, "ID_ADVICE_BUTTON");
    public static final Property Ordr;
    public static final Property Title;
    
    static
    {
      ButtonId = new Property(1, Long.TYPE, "buttonId", false, "BUTTON_ID");
      Ordr = new Property(2, Integer.TYPE, "ordr", false, "ORDR");
      Title = new Property(3, String.class, "title", false, "TITLE");
      Icon = new Property(4, String.class, "icon", false, "ICON");
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
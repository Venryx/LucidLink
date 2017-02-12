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

public class RST_AdviceItemDao
  extends AbstractDao<RST_AdviceItem, Long>
{
  public static final String TABLENAME = "RST__ADVICE_ITEM";
  private DaoSession daoSession;
  private Query<RST_AdviceItem> rST_User_AdvicesQuery;
  private String selectDeep;
  
  public RST_AdviceItemDao(DaoConfig paramDaoConfig)
  {
    super(paramDaoConfig);
  }
  
  public RST_AdviceItemDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession)
  {
    super(paramDaoConfig, paramDaoSession);
    this.daoSession = paramDaoSession;
  }
  
  public static void createTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = "IF NOT EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE " + str + "'RST__ADVICE_ITEM' (" + "'ID' INTEGER PRIMARY KEY NOT NULL ," + "'SESSION_ID' INTEGER NOT NULL ," + "'HEADER' TEXT NOT NULL ," + "'TYPE' TEXT NOT NULL ," + "'TIMESTAMP' INTEGER NOT NULL ," + "'ORDR' INTEGER NOT NULL ," + "'CATEGORY' INTEGER NOT NULL ," + "'DETAIL' TEXT NOT NULL ," + "'CONTENT' TEXT NOT NULL ," + "'HTML_CONTENT' TEXT NOT NULL ," + "'ICON' TEXT NOT NULL ," + "'FEEDBACK' INTEGER NOT NULL ," + "'READ' INTEGER NOT NULL ," + "'SUBTITLE' TEXT NOT NULL ," + "'ARTICLE_URL' TEXT NOT NULL ," + "'ID_USER' TEXT," + "'ID_ADVICE_ITEM' INTEGER);");
      return;
    }
  }
  
  public static void dropTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder("DROP TABLE ");
    if (paramBoolean) {}
    for (String str = "IF EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL(str + "'RST__ADVICE_ITEM'");
      return;
    }
  }
  
  public List<RST_AdviceItem> _queryRST_User_Advices(String paramString)
  {
    try
    {
      if (this.rST_User_AdvicesQuery == null)
      {
        localObject = queryBuilder();
        ((QueryBuilder)localObject).where(Properties.IdUser.eq(null), new WhereCondition[0]);
        ((QueryBuilder)localObject).orderRaw("TIMESTAMP ASC");
        this.rST_User_AdvicesQuery = ((QueryBuilder)localObject).build();
      }
      Object localObject = this.rST_User_AdvicesQuery.forCurrentThread();
      ((Query)localObject).setParameter(0, paramString);
      return ((Query)localObject).list();
    }
    finally {}
  }
  
  protected void attachEntity(RST_AdviceItem paramRST_AdviceItem)
  {
    super.attachEntity(paramRST_AdviceItem);
    paramRST_AdviceItem.__setDaoSession(this.daoSession);
  }
  
  protected void bindValues(SQLiteStatement paramSQLiteStatement, RST_AdviceItem paramRST_AdviceItem)
  {
    paramSQLiteStatement.clearBindings();
    paramSQLiteStatement.bindLong(1, paramRST_AdviceItem.getId());
    paramSQLiteStatement.bindLong(2, paramRST_AdviceItem.getSessionId());
    paramSQLiteStatement.bindString(3, paramRST_AdviceItem.getHeader());
    paramSQLiteStatement.bindString(4, paramRST_AdviceItem.getType());
    paramSQLiteStatement.bindLong(5, paramRST_AdviceItem.getTimestamp().getTime());
    paramSQLiteStatement.bindLong(6, paramRST_AdviceItem.getOrdr());
    paramSQLiteStatement.bindLong(7, paramRST_AdviceItem.getCategory());
    paramSQLiteStatement.bindString(8, paramRST_AdviceItem.getDetail());
    paramSQLiteStatement.bindString(9, paramRST_AdviceItem.getContent());
    paramSQLiteStatement.bindString(10, paramRST_AdviceItem.getHtmlContent());
    paramSQLiteStatement.bindString(11, paramRST_AdviceItem.getIcon());
    paramSQLiteStatement.bindLong(12, paramRST_AdviceItem.getFeedback());
    if (paramRST_AdviceItem.getRead()) {}
    for (long l = 1L;; l = 0L)
    {
      paramSQLiteStatement.bindLong(13, l);
      paramSQLiteStatement.bindString(14, paramRST_AdviceItem.getSubtitle());
      paramSQLiteStatement.bindString(15, paramRST_AdviceItem.getArticleUrl());
      String str = paramRST_AdviceItem.getIdUser();
      if (str != null) {
        paramSQLiteStatement.bindString(16, str);
      }
      paramRST_AdviceItem = paramRST_AdviceItem.getIdAdviceItem();
      if (paramRST_AdviceItem != null) {
        paramSQLiteStatement.bindLong(17, paramRST_AdviceItem.longValue());
      }
      return;
    }
  }
  
  public Long getKey(RST_AdviceItem paramRST_AdviceItem)
  {
    if (paramRST_AdviceItem != null) {}
    for (paramRST_AdviceItem = Long.valueOf(paramRST_AdviceItem.getId());; paramRST_AdviceItem = null) {
      return paramRST_AdviceItem;
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
      SqlUtils.appendColumns(localStringBuilder, "T1", this.daoSession.getRST_SleepEventDao().getAllColumns());
      localStringBuilder.append(" FROM RST__ADVICE_ITEM T");
      localStringBuilder.append(" LEFT JOIN RST__USER T0 ON T.'ID_USER'=T0.'ID'");
      localStringBuilder.append(" LEFT JOIN RST__SLEEP_EVENT T1 ON T.'ID_ADVICE_ITEM'=T1.'_id'");
      localStringBuilder.append(' ');
      this.selectDeep = localStringBuilder.toString();
    }
    return this.selectDeep;
  }
  
  protected boolean isEntityUpdateable()
  {
    return true;
  }
  
  public List<RST_AdviceItem> loadAllDeepFromCursor(Cursor paramCursor)
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
  
  protected RST_AdviceItem loadCurrentDeep(Cursor paramCursor, boolean paramBoolean)
  {
    RST_AdviceItem localRST_AdviceItem = (RST_AdviceItem)loadCurrent(paramCursor, 0, paramBoolean);
    int i = getAllColumns().length;
    localRST_AdviceItem.setRST_User((RST_User)loadCurrentOther(this.daoSession.getRST_UserDao(), paramCursor, i));
    int j = this.daoSession.getRST_UserDao().getAllColumns().length;
    localRST_AdviceItem.setHypnogramInfo((RST_SleepEvent)loadCurrentOther(this.daoSession.getRST_SleepEventDao(), paramCursor, i + j));
    return localRST_AdviceItem;
  }
  
  public RST_AdviceItem loadDeep(Long paramLong)
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
          localStringBuilder = new java/lang/StringBuilder;
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
  
  protected List<RST_AdviceItem> loadDeepAllAndCloseCursor(Cursor paramCursor)
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
  
  public List<RST_AdviceItem> queryDeep(String paramString, String... paramVarArgs)
  {
    return loadDeepAllAndCloseCursor(this.db.rawQuery(getSelectDeep() + paramString, paramVarArgs));
  }
  
  public RST_AdviceItem readEntity(Cursor paramCursor, int paramInt)
  {
    long l1 = paramCursor.getLong(paramInt + 0);
    long l2 = paramCursor.getLong(paramInt + 1);
    String str3 = paramCursor.getString(paramInt + 2);
    String str5 = paramCursor.getString(paramInt + 3);
    Date localDate = new Date(paramCursor.getLong(paramInt + 4));
    int j = paramCursor.getInt(paramInt + 5);
    int k = paramCursor.getInt(paramInt + 6);
    String str7 = paramCursor.getString(paramInt + 7);
    String str8 = paramCursor.getString(paramInt + 8);
    String str6 = paramCursor.getString(paramInt + 9);
    String str9 = paramCursor.getString(paramInt + 10);
    int i = paramCursor.getInt(paramInt + 11);
    boolean bool;
    String str4;
    String str2;
    String str1;
    if (paramCursor.getShort(paramInt + 12) != 0)
    {
      bool = true;
      str4 = paramCursor.getString(paramInt + 13);
      str2 = paramCursor.getString(paramInt + 14);
      if (!paramCursor.isNull(paramInt + 15)) {
        break label261;
      }
      str1 = null;
      label200:
      if (!paramCursor.isNull(paramInt + 16)) {
        break label276;
      }
    }
    label261:
    label276:
    for (paramCursor = null;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 16)))
    {
      return new RST_AdviceItem(l1, l2, str3, str5, localDate, j, k, str7, str8, str6, str9, i, bool, str4, str2, str1, paramCursor);
      bool = false;
      break;
      str1 = paramCursor.getString(paramInt + 15);
      break label200;
    }
  }
  
  public void readEntity(Cursor paramCursor, RST_AdviceItem paramRST_AdviceItem, int paramInt)
  {
    Object localObject = null;
    paramRST_AdviceItem.setId(paramCursor.getLong(paramInt + 0));
    paramRST_AdviceItem.setSessionId(paramCursor.getLong(paramInt + 1));
    paramRST_AdviceItem.setHeader(paramCursor.getString(paramInt + 2));
    paramRST_AdviceItem.setType(paramCursor.getString(paramInt + 3));
    paramRST_AdviceItem.setTimestamp(new Date(paramCursor.getLong(paramInt + 4)));
    paramRST_AdviceItem.setOrdr(paramCursor.getInt(paramInt + 5));
    paramRST_AdviceItem.setCategory(paramCursor.getInt(paramInt + 6));
    paramRST_AdviceItem.setDetail(paramCursor.getString(paramInt + 7));
    paramRST_AdviceItem.setContent(paramCursor.getString(paramInt + 8));
    paramRST_AdviceItem.setHtmlContent(paramCursor.getString(paramInt + 9));
    paramRST_AdviceItem.setIcon(paramCursor.getString(paramInt + 10));
    paramRST_AdviceItem.setFeedback(paramCursor.getInt(paramInt + 11));
    boolean bool;
    String str;
    if (paramCursor.getShort(paramInt + 12) != 0)
    {
      bool = true;
      paramRST_AdviceItem.setRead(bool);
      paramRST_AdviceItem.setSubtitle(paramCursor.getString(paramInt + 13));
      paramRST_AdviceItem.setArticleUrl(paramCursor.getString(paramInt + 14));
      if (!paramCursor.isNull(paramInt + 15)) {
        break label272;
      }
      str = null;
      label238:
      paramRST_AdviceItem.setIdUser(str);
      if (!paramCursor.isNull(paramInt + 16)) {
        break label287;
      }
    }
    label272:
    label287:
    for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 16)))
    {
      paramRST_AdviceItem.setIdAdviceItem(paramCursor);
      return;
      bool = false;
      break;
      str = paramCursor.getString(paramInt + 15);
      break label238;
    }
  }
  
  public Long readKey(Cursor paramCursor, int paramInt)
  {
    return Long.valueOf(paramCursor.getLong(paramInt + 0));
  }
  
  protected Long updateKeyAfterInsert(RST_AdviceItem paramRST_AdviceItem, long paramLong)
  {
    paramRST_AdviceItem.setId(paramLong);
    return Long.valueOf(paramLong);
  }
  
  public static class Properties
  {
    public static final Property ArticleUrl;
    public static final Property Category;
    public static final Property Content;
    public static final Property Detail;
    public static final Property Feedback;
    public static final Property Header;
    public static final Property HtmlContent;
    public static final Property Icon;
    public static final Property Id = new Property(0, Long.TYPE, "id", true, "ID");
    public static final Property IdAdviceItem = new Property(16, Long.class, "idAdviceItem", false, "ID_ADVICE_ITEM");
    public static final Property IdUser;
    public static final Property Ordr;
    public static final Property Read;
    public static final Property SessionId = new Property(1, Long.TYPE, "sessionId", false, "SESSION_ID");
    public static final Property Subtitle;
    public static final Property Timestamp;
    public static final Property Type;
    
    static
    {
      Header = new Property(2, String.class, "header", false, "HEADER");
      Type = new Property(3, String.class, "type", false, "TYPE");
      Timestamp = new Property(4, Date.class, "timestamp", false, "TIMESTAMP");
      Ordr = new Property(5, Integer.TYPE, "ordr", false, "ORDR");
      Category = new Property(6, Integer.TYPE, "category", false, "CATEGORY");
      Detail = new Property(7, String.class, "detail", false, "DETAIL");
      Content = new Property(8, String.class, "content", false, "CONTENT");
      HtmlContent = new Property(9, String.class, "htmlContent", false, "HTML_CONTENT");
      Icon = new Property(10, String.class, "icon", false, "ICON");
      Feedback = new Property(11, Integer.TYPE, "feedback", false, "FEEDBACK");
      Read = new Property(12, Boolean.TYPE, "read", false, "READ");
      Subtitle = new Property(13, String.class, "subtitle", false, "SUBTITLE");
      ArticleUrl = new Property(14, String.class, "articleUrl", false, "ARTICLE_URL");
      IdUser = new Property(15, String.class, "idUser", false, "ID_USER");
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RST_AdviceItemDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
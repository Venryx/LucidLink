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

public class RST_QuestionItemDao
  extends AbstractDao<RST_QuestionItem, Long>
{
  public static final String TABLENAME = "RST__QUESTION_ITEM";
  private DaoSession daoSession;
  private Query<RST_QuestionItem> rST_NightQuestions_QuestionsQuery;
  private String selectDeep;
  
  public RST_QuestionItemDao(DaoConfig paramDaoConfig)
  {
    super(paramDaoConfig);
  }
  
  public RST_QuestionItemDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession)
  {
    super(paramDaoConfig, paramDaoSession);
    this.daoSession = paramDaoSession;
  }
  
  public static void createTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = "IF NOT EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE " + str + "'RST__QUESTION_ITEM' (" + "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + "'QUESTION_ID' INTEGER NOT NULL ," + "'TEXT' TEXT NOT NULL ," + "'ORDR' INTEGER NOT NULL ," + "'ANSWER' INTEGER NOT NULL ," + "'ID_NIGHT_QUESTION' INTEGER);");
      return;
    }
  }
  
  public static void dropTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder("DROP TABLE ");
    if (paramBoolean) {}
    for (String str = "IF EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL(str + "'RST__QUESTION_ITEM'");
      return;
    }
  }
  
  public List<RST_QuestionItem> _queryRST_NightQuestions_Questions(Long paramLong)
  {
    try
    {
      if (this.rST_NightQuestions_QuestionsQuery == null)
      {
        localObject = queryBuilder();
        ((QueryBuilder)localObject).where(Properties.IdNightQuestion.eq(null), new WhereCondition[0]);
        ((QueryBuilder)localObject).orderRaw("ORDR ASC");
        this.rST_NightQuestions_QuestionsQuery = ((QueryBuilder)localObject).build();
      }
      Object localObject = this.rST_NightQuestions_QuestionsQuery.forCurrentThread();
      ((Query)localObject).setParameter(0, paramLong);
      return ((Query)localObject).list();
    }
    finally {}
  }
  
  protected void attachEntity(RST_QuestionItem paramRST_QuestionItem)
  {
    super.attachEntity(paramRST_QuestionItem);
    paramRST_QuestionItem.__setDaoSession(this.daoSession);
  }
  
  protected void bindValues(SQLiteStatement paramSQLiteStatement, RST_QuestionItem paramRST_QuestionItem)
  {
    paramSQLiteStatement.clearBindings();
    Long localLong = paramRST_QuestionItem.getId();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(1, localLong.longValue());
    }
    paramSQLiteStatement.bindLong(2, paramRST_QuestionItem.getQuestionId());
    paramSQLiteStatement.bindString(3, paramRST_QuestionItem.getText());
    paramSQLiteStatement.bindLong(4, paramRST_QuestionItem.getOrdr());
    paramSQLiteStatement.bindLong(5, paramRST_QuestionItem.getAnswer());
    paramRST_QuestionItem = paramRST_QuestionItem.getIdNightQuestion();
    if (paramRST_QuestionItem != null) {
      paramSQLiteStatement.bindLong(6, paramRST_QuestionItem.longValue());
    }
  }
  
  public Long getKey(RST_QuestionItem paramRST_QuestionItem)
  {
    if (paramRST_QuestionItem != null) {}
    for (paramRST_QuestionItem = paramRST_QuestionItem.getId();; paramRST_QuestionItem = null) {
      return paramRST_QuestionItem;
    }
  }
  
  protected String getSelectDeep()
  {
    if (this.selectDeep == null)
    {
      StringBuilder localStringBuilder = new StringBuilder("SELECT ");
      SqlUtils.appendColumns(localStringBuilder, "T", getAllColumns());
      localStringBuilder.append(',');
      SqlUtils.appendColumns(localStringBuilder, "T0", this.daoSession.getRST_NightQuestionsDao().getAllColumns());
      localStringBuilder.append(" FROM RST__QUESTION_ITEM T");
      localStringBuilder.append(" LEFT JOIN RST__NIGHT_QUESTIONS T0 ON T.'ID_NIGHT_QUESTION'=T0.'_id'");
      localStringBuilder.append(' ');
      this.selectDeep = localStringBuilder.toString();
    }
    return this.selectDeep;
  }
  
  protected boolean isEntityUpdateable()
  {
    return true;
  }
  
  public List<RST_QuestionItem> loadAllDeepFromCursor(Cursor paramCursor)
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
  
  protected RST_QuestionItem loadCurrentDeep(Cursor paramCursor, boolean paramBoolean)
  {
    RST_QuestionItem localRST_QuestionItem = (RST_QuestionItem)loadCurrent(paramCursor, 0, paramBoolean);
    int i = getAllColumns().length;
    localRST_QuestionItem.setRST_NightQuestions((RST_NightQuestions)loadCurrentOther(this.daoSession.getRST_NightQuestionsDao(), paramCursor, i));
    return localRST_QuestionItem;
  }
  
  public RST_QuestionItem loadDeep(Long paramLong)
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
  
  protected List<RST_QuestionItem> loadDeepAllAndCloseCursor(Cursor paramCursor)
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
  
  public List<RST_QuestionItem> queryDeep(String paramString, String... paramVarArgs)
  {
    return loadDeepAllAndCloseCursor(this.db.rawQuery(getSelectDeep() + paramString, paramVarArgs));
  }
  
  public RST_QuestionItem readEntity(Cursor paramCursor, int paramInt)
  {
    Object localObject = null;
    Long localLong;
    int k;
    String str;
    int i;
    int j;
    if (paramCursor.isNull(paramInt + 0))
    {
      localLong = null;
      k = paramCursor.getInt(paramInt + 1);
      str = paramCursor.getString(paramInt + 2);
      i = paramCursor.getInt(paramInt + 3);
      j = paramCursor.getInt(paramInt + 4);
      if (!paramCursor.isNull(paramInt + 5)) {
        break label111;
      }
    }
    label111:
    for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 5)))
    {
      return new RST_QuestionItem(localLong, k, str, i, j, paramCursor);
      localLong = Long.valueOf(paramCursor.getLong(paramInt + 0));
      break;
    }
  }
  
  public void readEntity(Cursor paramCursor, RST_QuestionItem paramRST_QuestionItem, int paramInt)
  {
    Object localObject = null;
    Long localLong;
    if (paramCursor.isNull(paramInt + 0))
    {
      localLong = null;
      paramRST_QuestionItem.setId(localLong);
      paramRST_QuestionItem.setQuestionId(paramCursor.getInt(paramInt + 1));
      paramRST_QuestionItem.setText(paramCursor.getString(paramInt + 2));
      paramRST_QuestionItem.setOrdr(paramCursor.getInt(paramInt + 3));
      paramRST_QuestionItem.setAnswer(paramCursor.getInt(paramInt + 4));
      if (!paramCursor.isNull(paramInt + 5)) {
        break label114;
      }
    }
    label114:
    for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 5)))
    {
      paramRST_QuestionItem.setIdNightQuestion(paramCursor);
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
  
  protected Long updateKeyAfterInsert(RST_QuestionItem paramRST_QuestionItem, long paramLong)
  {
    paramRST_QuestionItem.setId(Long.valueOf(paramLong));
    return Long.valueOf(paramLong);
  }
  
  public static class Properties
  {
    public static final Property Answer = new Property(4, Integer.TYPE, "answer", false, "ANSWER");
    public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    public static final Property IdNightQuestion = new Property(5, Long.class, "idNightQuestion", false, "ID_NIGHT_QUESTION");
    public static final Property Ordr;
    public static final Property QuestionId = new Property(1, Integer.TYPE, "questionId", false, "QUESTION_ID");
    public static final Property Text = new Property(2, String.class, "text", false, "TEXT");
    
    static
    {
      Ordr = new Property(3, Integer.TYPE, "ordr", false, "ORDR");
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RST_QuestionItemDao.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
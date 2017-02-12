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

public class RST_DisplayItemDao
		extends AbstractDao<RST_DisplayItem, Long>
{
	public static final String TABLENAME = "RST__DISPLAY_ITEM";
	private DaoSession daoSession;
	private Query<RST_DisplayItem> rST_QuestionItem_DisplayQuery;
	private String selectDeep;

	public RST_DisplayItemDao(DaoConfig paramDaoConfig)
	{
		super(paramDaoConfig);
	}

	public RST_DisplayItemDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession)
	{
		super(paramDaoConfig, paramDaoSession);
		this.daoSession = paramDaoSession;
	}

	public static void createTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
	{
		if (paramBoolean) {}
		for (String str = "IF NOT EXISTS ";; str = "")
		{
			paramSQLiteDatabase.execSQL("CREATE TABLE " + str + "'RST__DISPLAY_ITEM' (" + "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + "'DISPLAY_ID' INTEGER NOT NULL ," + "'VALUE' TEXT NOT NULL ," + "'ORDR' INTEGER NOT NULL ," + "'ID_DISPLAY' INTEGER);");
			return;
		}
	}

	public static void dropTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
	{
		StringBuilder localStringBuilder = new StringBuilder("DROP TABLE ");
		if (paramBoolean) {}
		for (String str = "IF EXISTS ";; str = "")
		{
			paramSQLiteDatabase.execSQL(str + "'RST__DISPLAY_ITEM'");
			return;
		}
	}

	public List<RST_DisplayItem> _queryRST_QuestionItem_Display(Long paramLong)
	{
		try
		{
			if (this.rST_QuestionItem_DisplayQuery == null)
			{
				localObject = queryBuilder();
				((QueryBuilder)localObject).where(Properties.IdDisplay.eq(null), new WhereCondition[0]);
				((QueryBuilder)localObject).orderRaw("ORDR ASC");
				this.rST_QuestionItem_DisplayQuery = ((QueryBuilder)localObject).build();
			}
			Object localObject = this.rST_QuestionItem_DisplayQuery.forCurrentThread();
			((Query)localObject).setParameter(0, paramLong);
			return ((Query)localObject).list();
		}
		finally {}
	}

	protected void attachEntity(RST_DisplayItem paramRST_DisplayItem)
	{
		super.attachEntity(paramRST_DisplayItem);
		paramRST_DisplayItem.__setDaoSession(this.daoSession);
	}

	protected void bindValues(SQLiteStatement paramSQLiteStatement, RST_DisplayItem paramRST_DisplayItem)
	{
		paramSQLiteStatement.clearBindings();
		Long localLong = paramRST_DisplayItem.getId();
		if (localLong != null) {
			paramSQLiteStatement.bindLong(1, localLong.longValue());
		}
		paramSQLiteStatement.bindLong(2, paramRST_DisplayItem.getDisplayId());
		paramSQLiteStatement.bindString(3, paramRST_DisplayItem.getValue());
		paramSQLiteStatement.bindLong(4, paramRST_DisplayItem.getOrdr());
		paramRST_DisplayItem = paramRST_DisplayItem.getIdDisplay();
		if (paramRST_DisplayItem != null) {
			paramSQLiteStatement.bindLong(5, paramRST_DisplayItem.longValue());
		}
	}

	public Long getKey(RST_DisplayItem paramRST_DisplayItem)
	{
		if (paramRST_DisplayItem != null) {}
		for (paramRST_DisplayItem = paramRST_DisplayItem.getId();; paramRST_DisplayItem = null) {
			return paramRST_DisplayItem;
		}
	}

	protected String getSelectDeep()
	{
		if (this.selectDeep == null)
		{
			StringBuilder localStringBuilder = new StringBuilder("SELECT ");
			SqlUtils.appendColumns(localStringBuilder, "T", getAllColumns());
			localStringBuilder.append(',');
			SqlUtils.appendColumns(localStringBuilder, "T0", this.daoSession.getRST_QuestionItemDao().getAllColumns());
			localStringBuilder.append(" FROM RST__DISPLAY_ITEM T");
			localStringBuilder.append(" LEFT JOIN RST__QUESTION_ITEM T0 ON T.'ID_DISPLAY'=T0.'_id'");
			localStringBuilder.append(' ');
			this.selectDeep = localStringBuilder.toString();
		}
		return this.selectDeep;
	}

	protected boolean isEntityUpdateable()
	{
		return true;
	}

	public List<RST_DisplayItem> loadAllDeepFromCursor(Cursor paramCursor)
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

	protected RST_DisplayItem loadCurrentDeep(Cursor paramCursor, boolean paramBoolean)
	{
		RST_DisplayItem localRST_DisplayItem = (RST_DisplayItem)loadCurrent(paramCursor, 0, paramBoolean);
		int i = getAllColumns().length;
		localRST_DisplayItem.setQuestion((RST_QuestionItem)loadCurrentOther(this.daoSession.getRST_QuestionItemDao(), paramCursor, i));
		return localRST_DisplayItem;
	}

	public RST_DisplayItem loadDeep(Long paramLong)
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
					paramLong = new java.lang.IllegalStateException(((Cursor)localObject).getCount());
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

	protected List<RST_DisplayItem> loadDeepAllAndCloseCursor(Cursor paramCursor)
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

	public List<RST_DisplayItem> queryDeep(String paramString, String... paramVarArgs)
	{
		return loadDeepAllAndCloseCursor(this.db.rawQuery(getSelectDeep() + paramString, paramVarArgs));
	}

	public RST_DisplayItem readEntity(Cursor paramCursor, int paramInt)
	{
		Object localObject = null;
		Long localLong;
		int i;
		String str;
		int j;
		if (paramCursor.isNull(paramInt + 0))
		{
			localLong = null;
			i = paramCursor.getInt(paramInt + 1);
			str = paramCursor.getString(paramInt + 2);
			j = paramCursor.getInt(paramInt + 3);
			if (!paramCursor.isNull(paramInt + 4)) {
				break label98;
			}
		}
		label98:
		for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 4)))
		{
			return new RST_DisplayItem(localLong, i, str, j, paramCursor);
			localLong = Long.valueOf(paramCursor.getLong(paramInt + 0));
			break;
		}
	}

	public void readEntity(Cursor paramCursor, RST_DisplayItem paramRST_DisplayItem, int paramInt)
	{
		Object localObject = null;
		Long localLong;
		if (paramCursor.isNull(paramInt + 0))
		{
			localLong = null;
			paramRST_DisplayItem.setId(localLong);
			paramRST_DisplayItem.setDisplayId(paramCursor.getInt(paramInt + 1));
			paramRST_DisplayItem.setValue(paramCursor.getString(paramInt + 2));
			paramRST_DisplayItem.setOrdr(paramCursor.getInt(paramInt + 3));
			if (!paramCursor.isNull(paramInt + 4)) {
				break label101;
			}
		}
		label101:
		for (paramCursor = (Cursor)localObject;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 4)))
		{
			paramRST_DisplayItem.setIdDisplay(paramCursor);
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

	protected Long updateKeyAfterInsert(RST_DisplayItem paramRST_DisplayItem, long paramLong)
	{
		paramRST_DisplayItem.setId(Long.valueOf(paramLong));
		return Long.valueOf(paramLong);
	}

	public static class Properties
	{
		public static final Property DisplayId;
		public static final Property Id = new Property(0, Long.class, "id", true, "_id");
		public static final Property IdDisplay = new Property(4, Long.class, "idDisplay", false, "ID_DISPLAY");
		public static final Property Ordr;
		public static final Property Value;

		static
		{
			DisplayId = new Property(1, Integer.TYPE, "displayId", false, "DISPLAY_ID");
			Value = new Property(2, String.class, "value", false, "VALUE");
			Ordr = new Property(3, Integer.TYPE, "ordr", false, "ORDR");
		}
	}
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
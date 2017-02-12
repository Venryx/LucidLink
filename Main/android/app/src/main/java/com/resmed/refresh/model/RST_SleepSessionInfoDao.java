/*
 * Decompiled with CFR 0_115.
 *
 * Could not load the following classes:
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteStatement
 *  com.resmed.refresh.model.DaoSession
 *  com.resmed.refresh.model.RST_EnvironmentalInfo
 *  com.resmed.refresh.model.RST_EnvironmentalInfoDao
 *  com.resmed.refresh.model.RST_SleepSessionInfo
 *  com.resmed.refresh.model.RST_SleepSessionInfoDao
 *  com.resmed.refresh.model.RST_SleepSessionInfoDao$Properties
 *  com.resmed.refresh.model.RST_User
 *  com.resmed.refresh.model.RST_UserDao
 *  de.greenrobot.dao.AbstractDao
 *  de.greenrobot.dao.AbstractDaoSession
 *  de.greenrobot.dao.Property
 *  de.greenrobot.dao.identityscope.IdentityScope
 *  de.greenrobot.dao.internal.DaoConfig
 *  de.greenrobot.dao.internal.SqlUtils
 *  de.greenrobot.dao.query.Query
 *  de.greenrobot.dao.query.QueryBuilder
 *  de.greenrobot.dao.query.WhereCondition
 */
package com.resmed.refresh.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.resmed.refresh.model.DaoSession;
import com.resmed.refresh.model.RST_EnvironmentalInfo;
import com.resmed.refresh.model.RST_EnvironmentalInfoDao;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RST_SleepSessionInfoDao;
import com.resmed.refresh.model.RST_User;
import com.resmed.refresh.model.RST_UserDao;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
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

public class RST_SleepSessionInfoDao
		extends AbstractDao<RST_SleepSessionInfo, Long> {
	public static final String TABLENAME = "RST__SLEEP_SESSION_INFO";
	private DaoSession daoSession;
	private Query<RST_SleepSessionInfo> rST_User_SleepSessionsQuery;
	private String selectDeep;

	public RST_SleepSessionInfoDao(DaoConfig daoConfig) {
		super(daoConfig);
	}

	public RST_SleepSessionInfoDao(DaoConfig daoConfig, DaoSession daoSession) {
		super(daoConfig, (AbstractDaoSession)daoSession);
		this.daoSession = daoSession;
	}

	public static void createTable(SQLiteDatabase sQLiteDatabase, boolean bl) {
		String string = bl ? "IF NOT EXISTS " : "";
		sQLiteDatabase.execSQL("CREATE TABLE " + string + "'RST__SLEEP_SESSION_INFO' (" + "'ID' INTEGER PRIMARY KEY NOT NULL UNIQUE ," + "'ALARM_FIRE_EPOCH' INTEGER NOT NULL ," + "'BIN_SLEEP_SCORE_DEEP' INTEGER NOT NULL ," + "'BIN_SLEEP_SCORE_LIGHT' INTEGER NOT NULL ," + "'BIN_SLEEP_SCORE_ONSET' INTEGER NOT NULL ," + "'BIN_SLEEP_SCORE_REM' INTEGER NOT NULL ," + "'BIN_SLEEP_SCORE_TST' INTEGER NOT NULL ," + "'BIN_SLEEP_SCORE_WASO' INTEGER NOT NULL ," + "'BODY_SCORE' INTEGER NOT NULL ," + "'COMPLETED' INTEGER NOT NULL ," + "'MIND_SCORE' INTEGER NOT NULL ," + "'NUMBER_INTERRUPTIONS' INTEGER NOT NULL ," + "'RECORDING_PERIOD' INTEGER NOT NULL ," + "'SIGNAL_QUALITY_IS_VALID' INTEGER NOT NULL ," + "'SIGNAL_QUALITY_MEAN' REAL NOT NULL ," + "'SIGNAL_QUALITY_PERC_BIN1' REAL NOT NULL ," + "'SIGNAL_QUALITY_PERC_BIN2' REAL NOT NULL ," + "'SIGNAL_QUALITY_PERC_BIN3' REAL NOT NULL ," + "'SIGNAL_QUALITY_PERC_BIN4' REAL NOT NULL ," + "'SIGNAL_QUALITY_PERC_BIN5' REAL NOT NULL ," + "'SIGNAL_QUALITY_STD' REAL NOT NULL ," + "'SIGNAL_QUALITY_VALUE' INTEGER NOT NULL ," + "'SLEEP_SCORE' INTEGER NOT NULL ," + "'START_TIME' INTEGER NOT NULL ," + "'STOP_TIME' INTEGER NOT NULL ," + "'TIME_IN_BED' INTEGER NOT NULL ," + "'TIME_TO_SLEEP' INTEGER NOT NULL ," + "'TOTAL_DEEP_SLEEP' INTEGER NOT NULL ," + "'TOTAL_LIGHT_SLEEP' INTEGER NOT NULL ," + "'TOTAL_REM' INTEGER NOT NULL ," + "'TOTAL_SLEEP_TIME' INTEGER NOT NULL ," + "'TOTAL_WAKE_TIME' INTEGER NOT NULL ," + "'UPLOADED' INTEGER NOT NULL ," + "'WASO_TIME' INTEGER NOT NULL ," + "'QUESTION_IDS' TEXT NOT NULL ," + "'ANSWER_VALUES' TEXT NOT NULL ," + "'ID_USER' TEXT," + "'ID_ENVIRONMENT' INTEGER);");
	}

	public static void dropTable(SQLiteDatabase sQLiteDatabase, boolean bl) {
		StringBuilder stringBuilder = new StringBuilder("DROP TABLE ");
		String string = bl ? "IF EXISTS " : "";
		sQLiteDatabase.execSQL(stringBuilder.append(string).append("'RST__SLEEP_SESSION_INFO'").toString());
	}

	/*
	 * Enabled unnecessary exception pruning
	 */
	public List<RST_SleepSessionInfo> _queryRST_User_SleepSessions(String string) {
		synchronized (this) {
			if (this.rST_User_SleepSessionsQuery == null) {
				QueryBuilder queryBuilder = this.queryBuilder();
				queryBuilder.where(RST_AdviceItemDao.Properties.IdUser.eq((Object)null), new WhereCondition[0]);
				this.rST_User_SleepSessionsQuery = queryBuilder.build();
			}
		}
		Query query = this.rST_User_SleepSessionsQuery.forCurrentThread();
		query.setParameter(0, (Object)string);
		return query.list();
	}

	protected void attachEntity(RST_SleepSessionInfo rST_SleepSessionInfo) {
		super.attachEntity(rST_SleepSessionInfo);
		rST_SleepSessionInfo.__setDaoSession(this.daoSession);
	}

	protected void bindValues(SQLiteStatement sQLiteStatement, RST_SleepSessionInfo rST_SleepSessionInfo) {
		Long l;
		long l2 = 1;
		sQLiteStatement.clearBindings();
		sQLiteStatement.bindLong(1, rST_SleepSessionInfo.getId());
		sQLiteStatement.bindLong(2, (long)rST_SleepSessionInfo.getAlarmFireEpoch());
		sQLiteStatement.bindLong(3, (long)rST_SleepSessionInfo.getBinSleepScoreDeep());
		sQLiteStatement.bindLong(4, (long)rST_SleepSessionInfo.getBinSleepScoreLight());
		sQLiteStatement.bindLong(5, (long)rST_SleepSessionInfo.getBinSleepScoreOnset());
		sQLiteStatement.bindLong(6, (long)rST_SleepSessionInfo.getBinSleepScoreRem());
		sQLiteStatement.bindLong(7, (long)rST_SleepSessionInfo.getBinSleepScoreTst());
		sQLiteStatement.bindLong(8, (long)rST_SleepSessionInfo.getBinSleepScoreWaso());
		sQLiteStatement.bindLong(9, (long)rST_SleepSessionInfo.getBodyScore());
		long l3 = rST_SleepSessionInfo.getCompleted() ? l2 : 0;
		sQLiteStatement.bindLong(10, l3);
		sQLiteStatement.bindLong(11, (long)rST_SleepSessionInfo.getMindScore());
		sQLiteStatement.bindLong(12, (long)rST_SleepSessionInfo.getNumberInterruptions());
		sQLiteStatement.bindLong(13, (long)rST_SleepSessionInfo.getRecordingPeriod());
		long l4 = rST_SleepSessionInfo.getSignalQualityIsValid() ? l2 : 0;
		sQLiteStatement.bindLong(14, l4);
		sQLiteStatement.bindDouble(15, (double)rST_SleepSessionInfo.getSignalQualityMean());
		sQLiteStatement.bindDouble(16, (double)rST_SleepSessionInfo.getSignalQualityPercBin1());
		sQLiteStatement.bindDouble(17, (double)rST_SleepSessionInfo.getSignalQualityPercBin2());
		sQLiteStatement.bindDouble(18, (double)rST_SleepSessionInfo.getSignalQualityPercBin3());
		sQLiteStatement.bindDouble(19, (double)rST_SleepSessionInfo.getSignalQualityPercBin4());
		sQLiteStatement.bindDouble(20, (double)rST_SleepSessionInfo.getSignalQualityPercBin5());
		sQLiteStatement.bindDouble(21, (double)rST_SleepSessionInfo.getSignalQualityStd());
		sQLiteStatement.bindLong(22, (long)rST_SleepSessionInfo.getSignalQualityValue());
		sQLiteStatement.bindLong(23, (long)rST_SleepSessionInfo.getSleepScore());
		sQLiteStatement.bindLong(24, rST_SleepSessionInfo.getStartTime().getTime());
		sQLiteStatement.bindLong(25, rST_SleepSessionInfo.getStopTime().getTime());
		sQLiteStatement.bindLong(26, (long)rST_SleepSessionInfo.getTimeInBed());
		sQLiteStatement.bindLong(27, (long)rST_SleepSessionInfo.getTimeToSleep());
		sQLiteStatement.bindLong(28, (long)rST_SleepSessionInfo.getTotalDeepSleep());
		sQLiteStatement.bindLong(29, (long)rST_SleepSessionInfo.getTotalLightSleep());
		sQLiteStatement.bindLong(30, (long)rST_SleepSessionInfo.getTotalRem());
		sQLiteStatement.bindLong(31, (long)rST_SleepSessionInfo.getTotalSleepTime());
		sQLiteStatement.bindLong(32, (long)rST_SleepSessionInfo.getTotalWakeTime());
		if (!rST_SleepSessionInfo.getUploaded()) {
			l2 = 0;
		}
		sQLiteStatement.bindLong(33, l2);
		sQLiteStatement.bindLong(34, (long)rST_SleepSessionInfo.getWasoTime());
		sQLiteStatement.bindString(35, rST_SleepSessionInfo.getQuestionIds());
		sQLiteStatement.bindString(36, rST_SleepSessionInfo.getAnswerValues());
		String string = rST_SleepSessionInfo.getIdUser();
		if (string != null) {
			sQLiteStatement.bindString(37, string);
		}
		if ((l = rST_SleepSessionInfo.getIdEnvironment()) == null) return;
		sQLiteStatement.bindLong(38, l.longValue());
	}

	public Long getKey(RST_SleepSessionInfo rST_SleepSessionInfo) {
		if (rST_SleepSessionInfo == null) return null;
		return rST_SleepSessionInfo.getId();
	}

	protected String getSelectDeep() {
		if (this.selectDeep != null) return this.selectDeep;
		StringBuilder stringBuilder = new StringBuilder("SELECT ");
		SqlUtils.appendColumns((StringBuilder)stringBuilder, (String)"T", (String[])this.getAllColumns());
		stringBuilder.append(',');
		SqlUtils.appendColumns((StringBuilder)stringBuilder, (String)"T0", (String[])this.daoSession.getRST_UserDao().getAllColumns());
		stringBuilder.append(',');
		SqlUtils.appendColumns((StringBuilder)stringBuilder, (String)"T1", (String[])this.daoSession.getRST_EnvironmentalInfoDao().getAllColumns());
		stringBuilder.append(" FROM RST__SLEEP_SESSION_INFO T");
		stringBuilder.append(" LEFT JOIN RST__USER T0 ON T.'ID_USER'=T0.'ID'");
		stringBuilder.append(" LEFT JOIN RST__ENVIRONMENTAL_INFO T1 ON T.'ID_ENVIRONMENT'=T1.'_id'");
		stringBuilder.append(' ');
		this.selectDeep = stringBuilder.toString();
		return this.selectDeep;
	}

	protected boolean isEntityUpdateable() {
		return true;
	}

	public List<RST_SleepSessionInfo> loadAllDeepFromCursor(Cursor cursor) {
		int n = cursor.getCount();
		ArrayList<RST_SleepSessionInfo> arrayList = new ArrayList<RST_SleepSessionInfo>(n);
		if (!cursor.moveToFirst()) return arrayList;
		if (this.identityScope != null) {
			this.identityScope.lock();
			this.identityScope.reserveRoom(n);
		}
		try {
			boolean bl;
			do {
				arrayList.add(this.loadCurrentDeep(cursor, false));
			} while (bl = cursor.moveToNext());
			if (this.identityScope == null) return arrayList;
		}
		catch (Throwable var4_5) {
			if (this.identityScope == null) throw var4_5;
			this.identityScope.unlock();
			throw var4_5;
		}
		this.identityScope.unlock();
		return arrayList;
	}

	protected RST_SleepSessionInfo loadCurrentDeep(Cursor cursor, boolean bl) {
		RST_SleepSessionInfo rST_SleepSessionInfo = (RST_SleepSessionInfo)this.loadCurrent(cursor, 0, bl);
		int n = this.getAllColumns().length;
		rST_SleepSessionInfo.setRST_User((RST_User)this.loadCurrentOther((AbstractDao)this.daoSession.getRST_UserDao(), cursor, n));
		int n2 = n + this.daoSession.getRST_UserDao().getAllColumns().length;
		rST_SleepSessionInfo.setEnvironmentalInfo((RST_EnvironmentalInfo)this.loadCurrentOther((AbstractDao)this.daoSession.getRST_EnvironmentalInfoDao(), cursor, n2));
		return rST_SleepSessionInfo;
	}

	/*
	 * Enabled unnecessary exception pruning
	 */
	public RST_SleepSessionInfo loadDeep(Long l) {
		Cursor cursor;
		block6 : {
			this.assertSinglePk();
			if (l == null) {
				return null;
			}
			StringBuilder stringBuilder = new StringBuilder(this.getSelectDeep());
			stringBuilder.append("WHERE ");
			SqlUtils.appendColumnsEqValue((StringBuilder)stringBuilder, (String)"T", (String[])this.getPkColumns());
			String string = stringBuilder.toString();
			String[] arrstring = new String[]{l.toString()};
			cursor = this.db.rawQuery(string, arrstring);
			boolean bl = cursor.moveToFirst();
			if (bl) break block6;
			cursor.close();
			return null;
		}
		try {
			if (cursor.isLast()) return this.loadCurrentDeep(cursor, true);
			throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
		}
		finally {
			cursor.close();
		}
	}

	protected List<RST_SleepSessionInfo> loadDeepAllAndCloseCursor(Cursor cursor) {
		try {
			List list = this.loadAllDeepFromCursor(cursor);
			return list;
		}
		finally {
			cursor.close();
		}
	}

	public /* varargs */ List<RST_SleepSessionInfo> queryDeep(String string, String ... arrstring) {
		return this.loadDeepAllAndCloseCursor(this.db.rawQuery(String.valueOf(this.getSelectDeep()) + string, arrstring));
	}

	public RST_SleepSessionInfo readEntity(Cursor cursor, int n) {
		Long l;
		long l2 = cursor.getLong(n + 0);
		int n2 = cursor.getInt(n + 1);
		int n3 = cursor.getInt(n + 2);
		int n4 = cursor.getInt(n + 3);
		int n5 = cursor.getInt(n + 4);
		int n6 = cursor.getInt(n + 5);
		int n7 = cursor.getInt(n + 6);
		int n8 = cursor.getInt(n + 7);
		int n9 = cursor.getInt(n + 8);
		boolean bl = cursor.getShort(n + 9) != 0;
		int n10 = cursor.getInt(n + 10);
		int n11 = cursor.getInt(n + 11);
		int n12 = cursor.getInt(n + 12);
		boolean bl2 = cursor.getShort(n + 13) != 0;
		float f = cursor.getFloat(n + 14);
		float f2 = cursor.getFloat(n + 15);
		float f3 = cursor.getFloat(n + 16);
		float f4 = cursor.getFloat(n + 17);
		float f5 = cursor.getFloat(n + 18);
		float f6 = cursor.getFloat(n + 19);
		float f7 = cursor.getFloat(n + 20);
		int n13 = cursor.getInt(n + 21);
		int n14 = cursor.getInt(n + 22);
		Date date = new Date(cursor.getLong(n + 23));
		Date date2 = new Date(cursor.getLong(n + 24));
		int n15 = cursor.getInt(n + 25);
		int n16 = cursor.getInt(n + 26);
		int n17 = cursor.getInt(n + 27);
		int n18 = cursor.getInt(n + 28);
		int n19 = cursor.getInt(n + 29);
		int n20 = cursor.getInt(n + 30);
		int n21 = cursor.getInt(n + 31);
		boolean bl3 = cursor.getShort(n + 32) != 0;
		int n22 = cursor.getInt(n + 33);
		String string = cursor.getString(n + 34);
		String string2 = cursor.getString(n + 35);
		String string3 = cursor.isNull(n + 36) ? null : cursor.getString(n + 36);
		if (cursor.isNull(n + 37)) {
			l = null;
			return new RST_SleepSessionInfo(l2, n2, n3, n4, n5, n6, n7, n8, n9, bl, n10, n11, n12, bl2, f, f2, f3, f4, f5, f6, f7, n13, n14, date, date2, n15, n16, n17, n18, n19, n20, n21, bl3, n22, string, string2, string3, l);
		}
		l = cursor.getLong(n + 37);
		return new RST_SleepSessionInfo(l2, n2, n3, n4, n5, n6, n7, n8, n9, bl, n10, n11, n12, bl2, f, f2, f3, f4, f5, f6, f7, n13, n14, date, date2, n15, n16, n17, n18, n19, n20, n21, bl3, n22, string, string2, string3, l);
	}

	public void readEntity(Cursor cursor, RST_SleepSessionInfo rST_SleepSessionInfo, int n) {
		boolean bl = true;
		rST_SleepSessionInfo.setId(cursor.getLong(n + 0));
		rST_SleepSessionInfo.setAlarmFireEpoch(cursor.getInt(n + 1));
		rST_SleepSessionInfo.setBinSleepScoreDeep(cursor.getInt(n + 2));
		rST_SleepSessionInfo.setBinSleepScoreLight(cursor.getInt(n + 3));
		rST_SleepSessionInfo.setBinSleepScoreOnset(cursor.getInt(n + 4));
		rST_SleepSessionInfo.setBinSleepScoreRem(cursor.getInt(n + 5));
		rST_SleepSessionInfo.setBinSleepScoreTst(cursor.getInt(n + 6));
		rST_SleepSessionInfo.setBinSleepScoreWaso(cursor.getInt(n + 7));
		rST_SleepSessionInfo.setBodyScore(cursor.getInt(n + 8));
		boolean bl2 = cursor.getShort(n + 9) != 0 ? bl : false;
		rST_SleepSessionInfo.setCompleted(bl2);
		rST_SleepSessionInfo.setMindScore(cursor.getInt(n + 10));
		rST_SleepSessionInfo.setNumberInterruptions(cursor.getInt(n + 11));
		rST_SleepSessionInfo.setRecordingPeriod(cursor.getInt(n + 12));
		boolean bl3 = cursor.getShort(n + 13) != 0 ? bl : false;
		rST_SleepSessionInfo.setSignalQualityIsValid(bl3);
		rST_SleepSessionInfo.setSignalQualityMean(cursor.getFloat(n + 14));
		rST_SleepSessionInfo.setSignalQualityPercBin1(cursor.getFloat(n + 15));
		rST_SleepSessionInfo.setSignalQualityPercBin2(cursor.getFloat(n + 16));
		rST_SleepSessionInfo.setSignalQualityPercBin3(cursor.getFloat(n + 17));
		rST_SleepSessionInfo.setSignalQualityPercBin4(cursor.getFloat(n + 18));
		rST_SleepSessionInfo.setSignalQualityPercBin5(cursor.getFloat(n + 19));
		rST_SleepSessionInfo.setSignalQualityStd(cursor.getFloat(n + 20));
		rST_SleepSessionInfo.setSignalQualityValue(cursor.getInt(n + 21));
		rST_SleepSessionInfo.setSleepScore(cursor.getInt(n + 22));
		rST_SleepSessionInfo.setStartTime(new Date(cursor.getLong(n + 23)));
		rST_SleepSessionInfo.setStopTime(new Date(cursor.getLong(n + 24)));
		rST_SleepSessionInfo.setTimeInBed(cursor.getInt(n + 25));
		rST_SleepSessionInfo.setTimeToSleep(cursor.getInt(n + 26));
		rST_SleepSessionInfo.setTotalDeepSleep(cursor.getInt(n + 27));
		rST_SleepSessionInfo.setTotalLightSleep(cursor.getInt(n + 28));
		rST_SleepSessionInfo.setTotalRem(cursor.getInt(n + 29));
		rST_SleepSessionInfo.setTotalSleepTime(cursor.getInt(n + 30));
		rST_SleepSessionInfo.setTotalWakeTime(cursor.getInt(n + 31));
		if (cursor.getShort(n + 32) == 0) {
			bl = false;
		}
		rST_SleepSessionInfo.setUploaded(bl);
		rST_SleepSessionInfo.setWasoTime(cursor.getInt(n + 33));
		rST_SleepSessionInfo.setQuestionIds(cursor.getString(n + 34));
		rST_SleepSessionInfo.setAnswerValues(cursor.getString(n + 35));
		String string = cursor.isNull(n + 36) ? null : cursor.getString(n + 36);
		rST_SleepSessionInfo.setIdUser(string);
		boolean bl4 = cursor.isNull(n + 37);
		Long l = null;
		if (!bl4) {
			l = cursor.getLong(n + 37);
		}
		rST_SleepSessionInfo.setIdEnvironment(l);
	}

	public Long readKey(Cursor cursor, int n) {
		return cursor.getLong(n + 0);
	}

	protected Long updateKeyAfterInsert(RST_SleepSessionInfo rST_SleepSessionInfo, long l) {
		rST_SleepSessionInfo.setId(l);
		return l;
	}
}

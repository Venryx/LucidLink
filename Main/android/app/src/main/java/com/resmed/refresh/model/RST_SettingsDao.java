package com.resmed.refresh.model;

import de.greenrobot.dao.internal.*;
import de.greenrobot.dao.*;
import android.database.sqlite.*;
import android.database.*;

public class RST_SettingsDao extends AbstractDao<RST_Settings, Long>
{
	public static final String TABLENAME = "RST__SETTINGS";

	public RST_SettingsDao(final DaoConfig daoConfig) {
		super(daoConfig);
	}

	public RST_SettingsDao(final DaoConfig daoConfig, final DaoSession daoSession) {
		super(daoConfig, (AbstractDaoSession)daoSession);
	}

	public static void createTable(final SQLiteDatabase sqLiteDatabase, final boolean b) {
		String s;
		if (b) {
			s = "IF NOT EXISTS ";
		}
		else {
			s = "";
		}
		sqLiteDatabase.execSQL("CREATE TABLE " + s + "'RST__SETTINGS' (" + "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + "'FIRMWARE_VERSION' INTEGER NOT NULL ," + "'LOCATION_PERMISSION' INTEGER NOT NULL ," + "'USE_METRIC_UNITS' INTEGER NOT NULL ," + "'PUSH_NOTIFICATIONS' INTEGER NOT NULL ," + "'TAC1' INTEGER NOT NULL ," + "'TAC2' INTEGER NOT NULL ," + "'TAC3' INTEGER NOT NULL," + "'HEIGHT_UNIT' INTEGER NOT NULL, " + "'WEIGHT_UNIT' INTEGER NOT NULL," + "'TEMPERATURE_UNIT' INTEGER NOT NULL," + "'LOCALE' TEXT NOT NULL," + "'COUNTRY_CODE' TEXT NOT NULL" + ");");
	}

	public static void dropTable(final SQLiteDatabase sqLiteDatabase, final boolean b) {
		final StringBuilder sb = new StringBuilder("DROP TABLE ");
		String s;
		if (b) {
			s = "IF EXISTS ";
		}
		else {
			s = "";
		}
		sqLiteDatabase.execSQL(sb.append(s).append("'RST__SETTINGS'").toString());
	}

	protected void bindValues(final SQLiteStatement sqLiteStatement, final RST_Settings rst_Settings) {
		long n = 1L;
		sqLiteStatement.clearBindings();
		final Long id = rst_Settings.getId();
		if (id != null) {
			sqLiteStatement.bindLong(1, (long)id);
		}
		long n2;
		if (rst_Settings.getFirmwareVersion()) {
			n2 = n;
		}
		else {
			n2 = 0L;
		}
		sqLiteStatement.bindLong(2, n2);
		long n3;
		if (rst_Settings.getLocationPermission()) {
			n3 = n;
		}
		else {
			n3 = 0L;
		}
		sqLiteStatement.bindLong(3, n3);
		long n4;
		if (rst_Settings.getUseMetricUnits()) {
			n4 = n;
		}
		else {
			n4 = 0L;
		}
		sqLiteStatement.bindLong(4, n4);
		long n5;
		if (rst_Settings.getPushNotifications()) {
			n5 = n;
		}
		else {
			n5 = 0L;
		}
		sqLiteStatement.bindLong(5, n5);
		long n6;
		if (rst_Settings.getTac1()) {
			n6 = n;
		}
		else {
			n6 = 0L;
		}
		sqLiteStatement.bindLong(6, n6);
		long n7;
		if (rst_Settings.getTac2()) {
			n7 = n;
		}
		else {
			n7 = 0L;
		}
		sqLiteStatement.bindLong(7, n7);
		if (!rst_Settings.getTac3()) {
			n = 0L;
		}
		sqLiteStatement.bindLong(8, n);
		sqLiteStatement.bindLong(9, (long)rst_Settings.getHeightUnit());
		sqLiteStatement.bindLong(10, (long)rst_Settings.getWeightUnit());
		sqLiteStatement.bindLong(11, (long)rst_Settings.getTemperatureUnit());
		if (rst_Settings.getLocale() != null) {
			sqLiteStatement.bindString(12, rst_Settings.getLocale());
		}
		else {
			sqLiteStatement.bindString(12, "");
		}
		if (rst_Settings.getCountryCode() != null) {
			sqLiteStatement.bindString(13, rst_Settings.getCountryCode());
			return;
		}
		sqLiteStatement.bindString(13, "");
	}

	public Long getKey(final RST_Settings rst_Settings) {
		if (rst_Settings != null) {
			return rst_Settings.getId();
		}
		return null;
	}

	protected boolean isEntityUpdateable() {
		return true;
	}

	public RST_Settings readEntity(final Cursor cursor, final int n) {
		Long value;
		if (cursor.isNull(n + 0)) {
			value = null;
		}
		else {
			value = cursor.getLong(n + 0);
		}
		final boolean b = cursor.getShort(n + 1) != 0;
		final boolean b2 = cursor.getShort(n + 2) != 0;
		final boolean b3 = cursor.getShort(n + 3) != 0;
		final boolean b4 = cursor.getShort(n + 4) != 0;
		final boolean b5 = cursor.getShort(n + 5) != 0;
		final boolean b6 = cursor.getShort(n + 6) != 0;
		final boolean b7 = cursor.getShort(n + 7) != 0;
		final int int1 = cursor.getInt(n + 8);
		final int int2 = cursor.getInt(n + 9);
		final int int3 = cursor.getInt(n + 10);
		String string;
		if (cursor.isNull(n + 11)) {
			string = "";
		}
		else {
			string = cursor.getString(n + 11);
		}
		String string2;
		if (cursor.isNull(n + 12)) {
			string2 = "";
		}
		else {
			string2 = cursor.getString(n + 12);
		}
		return new RST_Settings(value, b, b2, b3, b4, b5, b6, b7, int1, int2, int3, string, string2);
	}

	public void readEntity(final Cursor cursor, final RST_Settings rst_Settings, final int n) {
		boolean tac3 = true;
		Long value;
		if (cursor.isNull(n + 0)) {
			value = null;
		}
		else {
			value = cursor.getLong(n + 0);
		}
		rst_Settings.setId(value);
		rst_Settings.setFirmwareVersion(cursor.getShort(n + 1) != 0 && tac3);
		rst_Settings.setLocationPermission(cursor.getShort(n + 2) != 0 && tac3);
		rst_Settings.setUseMetricUnits(cursor.getShort(n + 3) != 0 && tac3);
		rst_Settings.setPushNotifications(cursor.getShort(n + 4) != 0 && tac3);
		rst_Settings.setTac1(cursor.getShort(n + 5) != 0 && tac3);
		rst_Settings.setTac2(cursor.getShort(n + 6) != 0 && tac3);
		if (cursor.getShort(n + 7) == 0) {
			tac3 = false;
		}
		rst_Settings.setTac3(tac3);
		rst_Settings.setHeightUnit(cursor.getInt(n + 8));
		rst_Settings.setWeightUnit(cursor.getInt(n + 9));
		rst_Settings.setTemperatureUnit(cursor.getInt(n + 10));
		String string;
		if (cursor.isNull(n + 11)) {
			string = "";
		}
		else {
			string = cursor.getString(n + 11);
		}
		rst_Settings.setLocale(string);
		String string2;
		if (cursor.isNull(n + 12)) {
			string2 = "";
		}
		else {
			string2 = cursor.getString(n + 12);
		}
		rst_Settings.setCountryCode(string2);
	}

	public Long readKey(final Cursor cursor, final int n) {
		if (cursor.isNull(n + 0)) {
			return null;
		}
		return cursor.getLong(n + 0);
	}

	protected Long updateKeyAfterInsert(final RST_Settings rst_Settings, final long n) {
		rst_Settings.setId(n);
		return n;
	}
}

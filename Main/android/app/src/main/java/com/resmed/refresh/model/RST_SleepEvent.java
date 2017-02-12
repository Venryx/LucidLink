/*
 * Decompiled with CFR 0_115.
 *
 * Could not load the following classes:
 *  com.resmed.refresh.model.DaoSession
 *  com.resmed.refresh.model.RST_SleepEvent
 *  com.resmed.refresh.model.RST_SleepEventDao
 *  com.resmed.refresh.model.RST_SleepSessionInfo
 *  com.resmed.refresh.model.RST_SleepSessionInfoDao
 *  de.greenrobot.dao.DaoException
 */
package com.resmed.refresh.model;

import com.resmed.refresh.model.DaoSession;
import com.resmed.refresh.model.RST_SleepEventDao;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RST_SleepSessionInfoDao;
import de.greenrobot.dao.DaoException;

public class RST_SleepEvent {
	public enum SleepEventType {
		kSleepEventTypeLight,
		kSleepEventTypeOther,
		kSleepEventTypeSound,
		kSleepEventTypeTemp;

		static {
			SleepEventType[] var0 = new SleepEventType[]{kSleepEventTypeSound, kSleepEventTypeLight, kSleepEventTypeTemp, kSleepEventTypeOther};
		}
	}


	private transient DaoSession daoSession;
	private int epochNumber;
	private Long id;
	private Long idSleepSession;
	private transient RST_SleepEventDao myDao;
	private RST_SleepSessionInfo rST_SleepSessionInfo;
	private Long rST_SleepSessionInfo__resolvedKey;
	private int type;
	private int value;

	public RST_SleepEvent() {
	}

	public RST_SleepEvent(Long l) {
		this.id = l;
	}

	public RST_SleepEvent(Long l, int n, int n2, int n3, Long l2) {
		this.id = l;
		this.epochNumber = n;
		this.type = n2;
		this.value = n3;
		this.idSleepSession = l2;
	}

	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		RST_SleepEventDao rST_SleepEventDao = daoSession != null ? daoSession.getRST_SleepEventDao() : null;
		this.myDao = rST_SleepEventDao;
	}

	public void delete() {
		if (this.myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		this.myDao.delete(this);
	}

	public int getEpochNumber() {
		return this.epochNumber;
	}

	public Long getId() {
		return this.id;
	}

	public Long getIdSleepSession() {
		return this.idSleepSession;
	}

	/*
	 * Enabled unnecessary exception pruning
	 */
	public RST_SleepSessionInfo getRST_SleepSessionInfo() {
		Long l = this.idSleepSession;
		if (this.rST_SleepSessionInfo__resolvedKey != null) {
			if (this.rST_SleepSessionInfo__resolvedKey.equals(l)) return this.rST_SleepSessionInfo;
		}
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		RST_SleepSessionInfo rST_SleepSessionInfo = (RST_SleepSessionInfo)this.daoSession.getRST_SleepSessionInfoDao().load(l);
		synchronized (this) {
			this.rST_SleepSessionInfo = rST_SleepSessionInfo;
			this.rST_SleepSessionInfo__resolvedKey = l;
			return this.rST_SleepSessionInfo;
		}
	}

	public int getType() {
		return this.type;
	}

	public int getValue() {
		return this.value;
	}

	public void refresh() {
		if (this.myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		this.myDao.refresh(this);
	}

	public void setEpochNumber(int n) {
		this.epochNumber = n;
	}

	public void setId(Long l) {
		this.id = l;
	}

	public void setIdSleepSession(Long l) {
		this.idSleepSession = l;
	}

	public void setRST_SleepSessionInfo(RST_SleepSessionInfo rST_SleepSessionInfo) {
		synchronized (this) {
			this.rST_SleepSessionInfo = rST_SleepSessionInfo;
			Long l = rST_SleepSessionInfo == null ? null : Long.valueOf(rST_SleepSessionInfo.getId());
			this.rST_SleepSessionInfo__resolvedKey = this.idSleepSession = l;
			return;
		}
	}

	public void setType(int n) {
		this.type = n;
	}

	public void setValue(int n) {
		this.value = n;
	}

	public void update() {
		if (this.myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		this.myDao.update(this);
	}
}

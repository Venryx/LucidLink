/*
 * Decompiled with CFR 0_115.
 *
 * Could not load the following classes:
 *  com.resmed.refresh.model.DaoSession
 *  com.resmed.refresh.model.RST_EnvironmentalInfo
 *  com.resmed.refresh.model.RST_EnvironmentalInfo$LightLevel
 *  com.resmed.refresh.model.RST_EnvironmentalInfoDao
 *  com.resmed.refresh.model.RST_SleepSessionInfo
 *  com.resmed.refresh.model.RST_SleepSessionInfoDao
 *  com.resmed.refresh.model.RST_ValueItem
 *  com.resmed.refresh.model.RST_ValueItemDao
 *  de.greenrobot.dao.DaoException
 */
package com.resmed.refresh.model;

import de.greenrobot.dao.DaoException;

import java.util.Iterator;
import java.util.List;

public class RST_EnvironmentalInfo {
	public enum LightLevel {
		kLightLevelHigh,
		kLightLevelLow,
		kLightLevelMedium;

		static {
			LightLevel[] var0 = new LightLevel[]{kLightLevelLow, kLightLevelMedium, kLightLevelHigh};
		}
	}


	private float currentLight;
	private float currentSound;
	private float currentTemperature;
	private transient DaoSession daoSession;
	private Long id;
	private Long idSession;
	private transient RST_EnvironmentalInfoDao myDao;
	private RST_SleepSessionInfo session;
	private List<RST_ValueItem> sessionLight;
	private List<RST_ValueItem> sessionSound;
	private List<RST_ValueItem> sessionTemperature;
	private Long session__resolvedKey;

	public RST_EnvironmentalInfo() {
	}

	public RST_EnvironmentalInfo(Long l) {
		this.id = l;
	}

	public RST_EnvironmentalInfo(Long l, float f, float f2, float f3, Long l2) {
		this.id = l;
		this.currentTemperature = f;
		this.currentLight = f2;
		this.currentSound = f3;
		this.idSession = l2;
	}

	public static String formattedLight(int n) {
		return String.valueOf(n);
	}

	public static String formattedTemp(int n) {
		return String.valueOf(n);
	}

	public static LightLevel lightLevel(int n) {
		if (n < 20) {
			return LightLevel.kLightLevelLow;
		}
		if (n >= 50) return LightLevel.kLightLevelHigh;
		return LightLevel.kLightLevelMedium;
	}

	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		RST_EnvironmentalInfoDao rST_EnvironmentalInfoDao = daoSession != null ? daoSession.getRST_EnvironmentalInfoDao() : null;
		this.myDao = rST_EnvironmentalInfoDao;
	}

	public void addSessionLightArray(List<RST_ValueItem> list) {
		if (list.size() <= 0) return;
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.sessionLight == null) {
			this.getSessionLight();
		}
		RST_ValueItemDao rST_ValueItemDao = this.daoSession.getRST_ValueItemDao();
		int n = 0;
		do {
			if (n >= list.size()) {
				rST_ValueItemDao.insertInTx(list);
				this.sessionLight.addAll(list);
				this.setCurrentLight(list.get(-1 + list.size()).getValue());
				this.update();
				return;
			}
			list.get(n).setLight(this);
			++n;
		} while (true);
	}

	public void addSessionLightValue(RST_ValueItem rST_ValueItem) {
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.sessionLight == null) {
			this.getSessionLight();
		}
		RST_ValueItemDao rST_ValueItemDao = this.daoSession.getRST_ValueItemDao();
		rST_ValueItem.setLight(this);
		rST_ValueItemDao.insertInTx(new RST_ValueItem[]{rST_ValueItem});
		this.sessionLight.add(rST_ValueItem);
		this.setCurrentLight(rST_ValueItem.getValue());
		this.update();
	}

	public void addSessionSoundArray(List<RST_ValueItem> list) {
		if (list.size() <= 0) return;
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.sessionSound == null) {
			this.getSessionSound();
		}
		RST_ValueItemDao rST_ValueItemDao = this.daoSession.getRST_ValueItemDao();
		int n = 0;
		do {
			if (n >= list.size()) {
				rST_ValueItemDao.insertInTx(list);
				this.sessionSound.addAll(list);
				this.setCurrentSound(list.get(-1 + list.size()).getValue());
				this.update();
				return;
			}
			list.get(n).setSound(this);
			++n;
		} while (true);
	}

	public void addSessionSoundValue(RST_ValueItem rST_ValueItem) {
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.sessionSound == null) {
			this.getSessionSound();
		}
		RST_ValueItemDao rST_ValueItemDao = this.daoSession.getRST_ValueItemDao();
		rST_ValueItem.setSound(this);
		rST_ValueItemDao.insert(rST_ValueItem);
		this.sessionSound.add(rST_ValueItem);
		this.setCurrentSound(rST_ValueItem.getValue());
		this.update();
	}

	public void addSessionTemperatureArray(List<RST_ValueItem> list) {
		if (list.size() <= 0) return;
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.sessionTemperature == null) {
			this.getSessionTemperature();
		}
		RST_ValueItemDao rST_ValueItemDao = this.daoSession.getRST_ValueItemDao();
		int n = 0;
		do {
			if (n >= list.size()) {
				rST_ValueItemDao.insertInTx(list);
				this.sessionTemperature.addAll(list);
				this.setCurrentTemperature(list.get(-1 + list.size()).getValue());
				this.update();
				return;
			}
			list.get(n).setTemperature(this);
			++n;
		} while (true);
	}

	public void addSessionTemperatureValue(RST_ValueItem rST_ValueItem) {
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.sessionTemperature == null) {
			this.getSessionTemperature();
		}
		RST_ValueItemDao rST_ValueItemDao = this.daoSession.getRST_ValueItemDao();
		rST_ValueItem.setTemperature(this);
		rST_ValueItemDao.insertInTx(new RST_ValueItem[]{rST_ValueItem});
		this.sessionTemperature.add(rST_ValueItem);
		this.setCurrentTemperature(rST_ValueItem.getValue());
		this.update();
	}

	public void delete() {
		if (this.myDao == null) throw new DaoException("Entity is detached from DAO context");
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.sessionLight == null) {
			this.getSessionLight();
		}
		Iterator iterator = this.sessionLight.iterator();
		do {
			if (!iterator.hasNext()) {
				if (this.sessionTemperature == null) {
					this.getSessionTemperature();
					break;
				}
				break;
			}
			((RST_ValueItem)iterator.next()).delete();
		} while (true);
		Iterator iterator2 = this.sessionTemperature.iterator();
		do {
			if (!iterator2.hasNext()) {
				if (this.sessionSound == null) {
					this.getSessionTemperature();
					break;
				}
				break;
			}
			((RST_ValueItem)iterator2.next()).delete();
		} while (true);
		Iterator iterator3 = this.sessionSound.iterator();
		do {
			if (!iterator3.hasNext()) {
				this.myDao.delete(this);
				return;
			}
			((RST_ValueItem)iterator3.next()).delete();
		} while (true);
	}

	public float getCurrentLight() {
		return this.currentLight;
	}

	public float getCurrentSound() {
		return this.currentSound;
	}

	public float getCurrentTemperature() {
		return this.currentTemperature;
	}

	public Long getId() {
		return this.id;
	}

	public Long getIdSession() {
		return this.idSession;
	}

	/*
	 * Enabled unnecessary exception pruning
	 */
	public RST_SleepSessionInfo getSession() {
		Long l = this.idSession;
		if (this.session__resolvedKey != null) {
			if (this.session__resolvedKey.equals(l)) return this.session;
		}
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		RST_SleepSessionInfo rST_SleepSessionInfo = (RST_SleepSessionInfo)this.daoSession.getRST_SleepSessionInfoDao().load(l);
		synchronized (this) {
			this.session = rST_SleepSessionInfo;
			this.session__resolvedKey = l;
			return this.session;
		}
	}

	/*
	 * Enabled unnecessary exception pruning
	 */
	public List<RST_ValueItem> getSessionLight() {
		if (this.sessionLight != null) return this.sessionLight;
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		List list = this.daoSession.getRST_ValueItemDao()._queryRST_EnvironmentalInfo_SessionLight(this.id);
		synchronized (this) {
			if (this.sessionLight != null) return this.sessionLight;
			this.sessionLight = list;
			return this.sessionLight;
		}
	}

	/*
	 * Enabled unnecessary exception pruning
	 */
	public List<RST_ValueItem> getSessionSound() {
		if (this.sessionSound != null) return this.sessionSound;
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		List list = this.daoSession.getRST_ValueItemDao()._queryRST_EnvironmentalInfo_SessionSound(this.id);
		synchronized (this) {
			if (this.sessionSound != null) return this.sessionSound;
			this.sessionSound = list;
			return this.sessionSound;
		}
	}

	/*
	 * Enabled unnecessary exception pruning
	 */
	public List<RST_ValueItem> getSessionTemperature() {
		if (this.sessionTemperature != null) return this.sessionTemperature;
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		List list = this.daoSession.getRST_ValueItemDao()._queryRST_EnvironmentalInfo_SessionTemperature(this.id);
		synchronized (this) {
			if (this.sessionTemperature != null) return this.sessionTemperature;
			this.sessionTemperature = list;
			return this.sessionTemperature;
		}
	}

	public void refresh() {
		if (this.myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		this.myDao.refresh(this);
	}

	public void resetSessionLight() {
		synchronized (this) {
			this.sessionLight = null;
			return;
		}
	}

	public void resetSessionSound() {
		synchronized (this) {
			this.sessionSound = null;
			return;
		}
	}

	public void resetSessionTemperature() {
		synchronized (this) {
			this.sessionTemperature = null;
			return;
		}
	}

	public void setCurrentLight(float f) {
		this.currentLight = f;
	}

	public void setCurrentSound(float f) {
		this.currentSound = f;
	}

	public void setCurrentTemperature(float f) {
		this.currentTemperature = f;
	}

	public void setId(Long l) {
		this.id = l;
	}

	public void setIdSession(Long l) {
		this.idSession = l;
	}

	public void setSession(RST_SleepSessionInfo rST_SleepSessionInfo) {
		synchronized (this) {
			this.session = rST_SleepSessionInfo;
			Long l = rST_SleepSessionInfo == null ? null : Long.valueOf(rST_SleepSessionInfo.getId());
			this.session__resolvedKey = this.idSession = l;
			return;
		}
	}

	public void setup() {
		this.currentTemperature = 0.0f;
		this.currentLight = 0.0f;
		this.currentSound = 0.0f;
	}

	public void update() {
		if (this.myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		this.myDao.update(this);
	}
}

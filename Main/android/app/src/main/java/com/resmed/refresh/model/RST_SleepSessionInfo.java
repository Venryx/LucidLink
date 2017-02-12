/*
 * Decompiled with CFR 0_115.
 *
 * Could not load the following classes:
 *  android.content.res.Resources
 *  com.resmed.refresh.model.DaoSession
 *  com.resmed.refresh.model.RST_EnvironmentalInfo
 *  com.resmed.refresh.model.RST_EnvironmentalInfoDao
 *  com.resmed.refresh.model.RST_NightQuestions
 *  com.resmed.refresh.model.RST_QuestionItem
 *  com.resmed.refresh.model.RST_SleepEvent
 *  com.resmed.refresh.model.RST_SleepEvent$SleepEventType
 *  com.resmed.refresh.model.RST_SleepEventDao
 *  com.resmed.refresh.model.RST_SleepSessionInfo
 *  com.resmed.refresh.model.RST_SleepSessionInfoDao
 *  com.resmed.refresh.model.RST_User
 *  com.resmed.refresh.model.RST_UserDao
 *  com.resmed.refresh.model.RST_ValueItem
 *  com.resmed.refresh.model.RST_ValueItemDao
 *  com.resmed.refresh.ui.uibase.app.RefreshApplication
 *  com.resmed.refresh.utils.AppFileLog
 *  com.resmed.refresh.utils.Log
 *  com.resmed.refresh.utils.RefreshTools
 *  com.resmed.rm20.SignalQuality
 *  com.resmed.rm20.SleepParams
 *  de.greenrobot.dao.DaoException
 */
package com.resmed.refresh.model;

import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.rm20.SignalQuality;
import com.resmed.rm20.SleepParams;
import de.greenrobot.dao.DaoException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class RST_SleepSessionInfo {
	public static final int STEP_SECS = 30;
	private int alarmFireEpoch;
	private String answerValues;
	private int binSleepScoreDeep;
	private int binSleepScoreLight;
	private int binSleepScoreOnset;
	private int binSleepScoreRem;
	private int binSleepScoreTst;
	private int binSleepScoreWaso;
	private int bodyScore;
	private boolean completed;
	private transient DaoSession daoSession;
	private RST_EnvironmentalInfo environmentalInfo;
	private Long environmentalInfo__resolvedKey;
	private List<RST_SleepEvent> events;
	private long id;
	private Long idEnvironment;
	private String idUser;
	private int mindScore;
	private transient RST_SleepSessionInfoDao myDao;
	private int numberInterruptions;
	private String questionIds;
	private RST_User rST_User;
	private String rST_User__resolvedKey;
	private int recordingPeriod;
	private boolean signalQualityIsValid;
	private float signalQualityMean;
	private float signalQualityPercBin1;
	private float signalQualityPercBin2;
	private float signalQualityPercBin3;
	private float signalQualityPercBin4;
	private float signalQualityPercBin5;
	private float signalQualityStd;
	private int signalQualityValue;
	private int sleepScore;
	private List<RST_ValueItem> sleepSignalRatings;
	private List<RST_ValueItem> sleepStates;
	private Date startTime;
	private Date stopTime;
	private String timeDisplayed;
	private int timeInBed;
	private int timeToSleep;
	private int totalDeepSleep;
	private int totalLightSleep;
	private int totalRem;
	private int totalSleepTime;
	private int totalWakeTime;
	private boolean uploaded;
	private int wasoTime;

	public RST_SleepSessionInfo() {
	}

	public RST_SleepSessionInfo(long l) {
		this.id = l;
	}

	public RST_SleepSessionInfo(long l, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl, int n9, int n10, int n11, boolean bl2, float f, float f2, float f3, float f4, float f5, float f6, float f7, int n12, int n13, Date date, Date date2, int n14, int n15, int n16, int n17, int n18, int n19, int n20, boolean bl3, int n21, String string, String string2, String string3, Long l2) {
		this.id = l;
		this.alarmFireEpoch = n;
		this.binSleepScoreDeep = n2;
		this.binSleepScoreLight = n3;
		this.binSleepScoreOnset = n4;
		this.binSleepScoreRem = n5;
		this.binSleepScoreTst = n6;
		this.binSleepScoreWaso = n7;
		this.bodyScore = n8;
		this.completed = bl;
		this.mindScore = n9;
		this.numberInterruptions = n10;
		this.recordingPeriod = n11;
		this.signalQualityIsValid = bl2;
		this.signalQualityMean = f;
		this.signalQualityPercBin1 = f2;
		this.signalQualityPercBin2 = f3;
		this.signalQualityPercBin3 = f4;
		this.signalQualityPercBin4 = f5;
		this.signalQualityPercBin5 = f6;
		this.signalQualityStd = f7;
		this.signalQualityValue = n12;
		this.sleepScore = n13;
		this.startTime = date;
		this.stopTime = date2;
		this.timeInBed = n14;
		this.timeToSleep = n15;
		this.totalDeepSleep = n16;
		this.totalLightSleep = n17;
		this.totalRem = n18;
		this.totalSleepTime = n19;
		this.totalWakeTime = n20;
		this.uploaded = bl3;
		this.wasoTime = n21;
		this.questionIds = string;
		this.answerValues = string2;
		this.idUser = string3;
		this.idEnvironment = l2;
	}

	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		RST_SleepSessionInfoDao rST_SleepSessionInfoDao = daoSession != null ? daoSession.getRST_SleepSessionInfoDao() : null;
		this.myDao = rST_SleepSessionInfoDao;
	}

	public void addEvent(RST_SleepEvent rST_SleepEvent) {
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.events == null) {
			this.getEvents();
		}
		RST_SleepEventDao rST_SleepEventDao = this.daoSession.getRST_SleepEventDao();
		rST_SleepEvent.setRST_SleepSessionInfo(this);
		rST_SleepEventDao.insertOrReplace(rST_SleepEvent);
		this.events.add(rST_SleepEvent);
	}

	public void addEventArray(List<RST_SleepEvent> list) {
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.events == null) {
			this.getEvents();
		}
		RST_SleepEventDao rST_SleepEventDao = this.daoSession.getRST_SleepEventDao();
		int n = 0;
		do {
			if (n >= list.size()) {
				rST_SleepEventDao.insertOrReplaceInTx(list);
				this.events.addAll(list);
				return;
			}
			list.get(n).setRST_SleepSessionInfo(this);
			++n;
		} while (true);
	}

	public void addNightQuestions(RST_NightQuestions rST_NightQuestions) {
		StringBuffer stringBuffer = new StringBuffer();
		StringBuffer stringBuffer2 = new StringBuffer();
		try {
			Iterator iterator = rST_NightQuestions.getQuestions().iterator();
			do {
				if (!iterator.hasNext()) {
					this.setQuestionIds(stringBuffer.toString());
					this.setAnswerValues(stringBuffer2.toString());
					AppFileLog.addTrace((String)("NightQuestions adding string values questions:" + this.questionIds + " answers:" + this.answerValues));
					return;
				}
				RST_QuestionItem rST_QuestionItem = (RST_QuestionItem)iterator.next();
				stringBuffer.append(rST_QuestionItem.getQuestionId()).append(",");
				stringBuffer2.append(rST_QuestionItem.getAnswer()).append(",");
			} while (true);
		}
		catch (Exception var4_6) {
			this.setQuestionIds("");
			this.setAnswerValues("");
			return;
		}
	}

	public void addSleepSignalRatingsArray(List<RST_ValueItem> list) {
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.sleepSignalRatings == null) {
			this.getSleepSignalRatings();
		}
		RST_ValueItemDao rST_ValueItemDao = this.daoSession.getRST_ValueItemDao();
		int n = 0;
		do {
			if (n >= list.size()) {
				rST_ValueItemDao.insertInTx(list);
				this.sleepSignalRatings.addAll(list);
				return;
			}
			list.get(n).setSleepSignalRatings(this);
			++n;
		} while (true);
	}

	public void addSleepSignalRatingsValue(RST_ValueItem rST_ValueItem) {
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.sleepSignalRatings == null) {
			this.getSleepSignalRatings();
		}
		RST_ValueItemDao rST_ValueItemDao = this.daoSession.getRST_ValueItemDao();
		rST_ValueItem.setSleepSignalRatings(this);
		rST_ValueItemDao.insert(rST_ValueItem);
		this.sleepSignalRatings.add(rST_ValueItem);
	}

	public void addSleepStateArray(List<RST_ValueItem> list) {
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.sleepStates == null) {
			this.getSleepStates();
		}
		RST_ValueItemDao rST_ValueItemDao = this.daoSession.getRST_ValueItemDao();
		int n = 0;
		do {
			if (n >= list.size()) {
				rST_ValueItemDao.insertOrReplaceInTx(list);
				this.resetSleepStates();
				this.getSleepStates();
				return;
			}
			list.get(n).setSleepStates(this);
			++n;
		} while (true);
	}

	public void addSleepStateValue(RST_ValueItem rST_ValueItem) {
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.sleepStates == null) {
			this.getSleepStates();
		}
		RST_ValueItemDao rST_ValueItemDao = this.daoSession.getRST_ValueItemDao();
		rST_ValueItem.setSleepStates(this);
		rST_ValueItemDao.insertOrReplace(rST_ValueItem);
		this.sleepStates.add(rST_ValueItem);
	}

	public void delete() {
		if (this.myDao == null) throw new DaoException("Entity is detached from DAO context");
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.sleepStates == null) {
			this.getSleepStates();
		}
		Iterator iterator = this.sleepStates.iterator();
		do {
			if (!iterator.hasNext()) {
				if (this.sleepSignalRatings == null) {
					this.getSleepSignalRatings();
					break;
				}
				break;
			}
			((RST_ValueItem)iterator.next()).delete();
		} while (true);
		Iterator iterator2 = this.sleepSignalRatings.iterator();
		do {
			if (!iterator2.hasNext()) {
				if (this.events == null) {
					this.getEvents();
					break;
				}
				break;
			}
			((RST_ValueItem)iterator2.next()).delete();
		} while (true);
		Iterator iterator3 = this.events.iterator();
		do {
			if (!iterator3.hasNext()) {
				this.myDao.delete(this);
				return;
			}
			((RST_SleepEvent)iterator3.next()).delete();
		} while (true);
	}

	public boolean deleteSoundEvent(int n) {
		RST_SleepEvent rST_SleepEvent;
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.events == null) {
			this.getEvents();
		}
		Iterator iterator = this.events.iterator();
		do {
			if (iterator.hasNext()) continue;
			return false;
		} while ((rST_SleepEvent = (RST_SleepEvent)iterator.next()).getType() != RST_SleepEvent.SleepEventType.kSleepEventTypeSound.ordinal() || n != rST_SleepEvent.getValue());
		this.daoSession.getRST_SleepEventDao().delete(rST_SleepEvent);
		this.events = null;
		this.getEvents();
		return true;
	}

	public int getAlarmFireEpoch() {
		return this.alarmFireEpoch;
	}

	public String getAnswerValues() {
		return this.answerValues;
	}

	public int getBinSleepScoreDeep() {
		return this.binSleepScoreDeep;
	}

	public int getBinSleepScoreLight() {
		return this.binSleepScoreLight;
	}

	public int getBinSleepScoreOnset() {
		return this.binSleepScoreOnset;
	}

	public int getBinSleepScoreRem() {
		return this.binSleepScoreRem;
	}

	public int getBinSleepScoreTst() {
		return this.binSleepScoreTst;
	}

	public int getBinSleepScoreWaso() {
		return this.binSleepScoreWaso;
	}

	public int getBodyScore() {
		return this.bodyScore;
	}

	public boolean getCompleted() {
		return this.completed;
	}

	/*
	 * Enabled unnecessary exception pruning
	 */
	public RST_EnvironmentalInfo getEnvironmentalInfo() {
		Long l = this.idEnvironment;
		if (this.environmentalInfo__resolvedKey != null) {
			if (this.environmentalInfo__resolvedKey.equals(l)) return this.environmentalInfo;
		}
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		RST_EnvironmentalInfo rST_EnvironmentalInfo = (RST_EnvironmentalInfo)this.daoSession.getRST_EnvironmentalInfoDao().load(l);
		synchronized (this) {
			this.environmentalInfo = rST_EnvironmentalInfo;
			this.environmentalInfo__resolvedKey = l;
			return this.environmentalInfo;
		}
	}

	/*
	 * Enabled unnecessary exception pruning
	 */
	public List<RST_SleepEvent> getEvents() {
		if (this.events != null) return this.events;
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		List list = this.daoSession.getRST_SleepEventDao()._queryRST_SleepSessionInfo_Events(Long.valueOf(this.id));
		synchronized (this) {
			if (this.events != null) return this.events;
			this.events = list;
			return this.events;
		}
	}

	public long getId() {
		return this.id;
	}

	public Long getIdEnvironment() {
		return this.idEnvironment;
	}

	public String getIdUser() {
		return this.idUser;
	}

	public int getMindScore() {
		return this.mindScore;
	}

	public int getNumberInterruptions() {
		return this.numberInterruptions;
	}

	public String getQuestionIds() {
		return this.questionIds;
	}

	/*
	 * Enabled unnecessary exception pruning
	 */
	public RST_User getRST_User() {
		String string = this.idUser;
		if (this.rST_User__resolvedKey != null) {
			if (this.rST_User__resolvedKey == string) return this.rST_User;
		}
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		RST_User rST_User = (RST_User)this.daoSession.getRST_UserDao().load(string);
		synchronized (this) {
			this.rST_User = rST_User;
			this.rST_User__resolvedKey = string;
			return this.rST_User;
		}
	}

	public int getRecordingPeriod() {
		return this.recordingPeriod;
	}

	public boolean getSignalQualityIsValid() {
		return this.signalQualityIsValid;
	}

	public float getSignalQualityMean() {
		return this.signalQualityMean;
	}

	public float getSignalQualityPercBin1() {
		return this.signalQualityPercBin1;
	}

	public float getSignalQualityPercBin2() {
		return this.signalQualityPercBin2;
	}

	public float getSignalQualityPercBin3() {
		return this.signalQualityPercBin3;
	}

	public float getSignalQualityPercBin4() {
		return this.signalQualityPercBin4;
	}

	public float getSignalQualityPercBin5() {
		return this.signalQualityPercBin5;
	}

	public float getSignalQualityStd() {
		return this.signalQualityStd;
	}

	public int getSignalQualityValue() {
		return this.signalQualityValue;
	}

	public int getSleepScore() {
		return this.sleepScore;
	}

	/*
	 * Enabled unnecessary exception pruning
	 */
	public List<RST_ValueItem> getSleepSignalRatings() {
		if (this.sleepSignalRatings != null) return this.sleepSignalRatings;
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		List list = this.daoSession.getRST_ValueItemDao()._queryRST_SleepSessionInfo_SleepSignalRatings(Long.valueOf(this.id));
		synchronized (this) {
			if (this.sleepSignalRatings != null) return this.sleepSignalRatings;
			this.sleepSignalRatings = list;
			return this.sleepSignalRatings;
		}
	}

	/*
	 * Enabled unnecessary exception pruning
	 */
	public List<RST_ValueItem> getSleepStates() {
		if (this.sleepStates != null) return this.sleepStates;
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		List list = this.daoSession.getRST_ValueItemDao()._queryRST_SleepSessionInfo_SleepStates(Long.valueOf(this.id));
		synchronized (this) {
			if (this.sleepStates != null) return this.sleepStates;
			this.sleepStates = list;
			return this.sleepStates;
		}
	}

	public String getSleepTime() {
		if (this.timeDisplayed != null) return this.timeDisplayed;
		this.timeDisplayed = String.valueOf(RefreshApplication.getInstance().getResources().getString(2131165983)) + " " + RefreshTools.getHourStringForTime((int)this.totalSleepTime) + " " + RefreshTools.getMinsStringForTime((int)this.totalSleepTime);
		return this.timeDisplayed;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public Date getStopTime() {
		return this.stopTime;
	}

	public int getTimeInBed() {
		return this.timeInBed;
	}

	public int getTimeToSleep() {
		return this.timeToSleep;
	}

	public int getTotalDeepSleep() {
		return this.totalDeepSleep;
	}

	public int getTotalLightSleep() {
		return this.totalLightSleep;
	}

	public int getTotalRem() {
		return this.totalRem;
	}

	public int getTotalSleepTime() {
		return this.totalSleepTime;
	}

	public int getTotalWakeTime() {
		return this.totalWakeTime;
	}

	public boolean getUploaded() {
		return this.uploaded;
	}

	public int getWasoTime() {
		return this.wasoTime;
	}

	public void processRM20Data(SleepParams sleepParams) {
		boolean bl;
		SignalQuality signalQuality;
		block3 : {
			bl = true;
			int[] arrn = sleepParams.getHypnogram();
			ArrayList<RST_ValueItem> arrayList = new ArrayList<RST_ValueItem>();
			Log.d((String)"com.resmed.refresh.model", (String)("Hypnogram size = " + arrn.length));
			String string = "hypnogram = {";
			int n = 0;
			do {
				if (n >= arrn.length) {
					Log.d((String)"com.resmed.refresh.model", (String)(String.valueOf(string) + "}"));
					this.addSleepStateArray(arrayList);
					this.setBinSleepScoreDeep(sleepParams.binDeep);
					this.setBinSleepScoreLight(sleepParams.binLightDuration);
					this.setBinSleepScoreOnset(sleepParams.binSleepOnset);
					this.setBinSleepScoreRem(sleepParams.binRem);
					this.setBinSleepScoreTst(sleepParams.binTst);
					this.setBinSleepScoreWaso(sleepParams.binWaso);
					this.setMindScore(sleepParams.mindScore);
					this.setBodyScore(sleepParams.bodyScore);
					this.setSleepScore(sleepParams.sleepScore);
					this.setRecordingPeriod(sleepParams.numEpochs / 2);
					this.setTimeToSleep(sleepParams.timeToSleep / 2);
					this.setTotalSleepTime(sleepParams.sleepDuration / 2);
					this.setNumberInterruptions(sleepParams.numAwakenings);
					this.setTimeToSleep(sleepParams.timeToSleep / 2);
					this.setTimeInBed(sleepParams.timeInBed / 2);
					this.setTotalRem(sleepParams.remEpochs / 2);
					this.setTotalLightSleep(sleepParams.lightEpochs / 2);
					this.setTotalDeepSleep(sleepParams.deepEpochs / 2);
					this.setTotalWakeTime(sleepParams.wakeEpochs / 2);
					this.setWasoTime(0);
					this.setCompleted(bl);
					signalQuality = sleepParams.signalQuality;
					if (signalQuality.getIsValid() != (bl ? 1 : 0)) break;
					break block3;
				}
				if (arrn[n] >= 0 && arrn[n] < 8) {
					RST_ValueItem rST_ValueItem = new RST_ValueItem();
					rST_ValueItem.setOrdr(n);
					rST_ValueItem.setValue((float)arrn[n]);
					string = String.valueOf(string) + ", " + arrn[n];
					arrayList.add(rST_ValueItem);
				}
				++n;
			} while (true);
			bl = false;
		}
		this.setSignalQualityIsValid(bl);
		this.setSignalQualityMean(signalQuality.getMeanSigQuality());
		this.setSignalQualityValue(signalQuality.getSigQuality());
		this.setSignalQualityStd(signalQuality.getStdSigQuality());
		this.setSignalQualityPercBin1(signalQuality.getPercBin1());
		this.setSignalQualityPercBin2(signalQuality.getPercBin2());
		this.setSignalQualityPercBin3(signalQuality.getPercBin3());
		this.setSignalQualityPercBin4(signalQuality.getPercBin4());
		this.setSignalQualityPercBin5(signalQuality.getPercBin5());
		this.update();
	}

	public void refresh() {
		if (this.myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		this.myDao.refresh(this);
	}

	public void resetEvents() {
		synchronized (this) {
			this.events = null;
			return;
		}
	}

	public void resetSleepSignalRatings() {
		synchronized (this) {
			this.sleepSignalRatings = null;
			return;
		}
	}

	public void resetSleepStates() {
		synchronized (this) {
			this.sleepStates = null;
			return;
		}
	}

	public void setAlarmFireEpoch(int n) {
		this.alarmFireEpoch = n;
	}

	public void setAnswerValues(String string) {
		this.answerValues = string;
	}

	public void setBinSleepScoreDeep(int n) {
		this.binSleepScoreDeep = n;
	}

	public void setBinSleepScoreLight(int n) {
		this.binSleepScoreLight = n;
	}

	public void setBinSleepScoreOnset(int n) {
		this.binSleepScoreOnset = n;
	}

	public void setBinSleepScoreRem(int n) {
		this.binSleepScoreRem = n;
	}

	public void setBinSleepScoreTst(int n) {
		this.binSleepScoreTst = n;
	}

	public void setBinSleepScoreWaso(int n) {
		this.binSleepScoreWaso = n;
	}

	public void setBodyScore(int n) {
		this.bodyScore = n;
	}

	public void setCompleted(boolean bl) {
		this.completed = bl;
	}

	public void setEnvironmentalInfo(RST_EnvironmentalInfo rST_EnvironmentalInfo) {
		synchronized (this) {
			this.environmentalInfo = rST_EnvironmentalInfo;
			Long l = rST_EnvironmentalInfo == null ? null : rST_EnvironmentalInfo.getId();
			this.environmentalInfo__resolvedKey = this.idEnvironment = l;
			return;
		}
	}

	public void setId(long l) {
		this.id = l;
	}

	public void setIdEnvironment(Long l) {
		this.idEnvironment = l;
	}

	public void setIdUser(String string) {
		this.idUser = string;
	}

	public void setMindScore(int n) {
		this.mindScore = n;
	}

	public void setNumberInterruptions(int n) {
		this.numberInterruptions = n;
	}

	public void setQuestionIds(String string) {
		this.questionIds = string;
	}

	public void setRST_User(RST_User rST_User) {
		synchronized (this) {
			this.rST_User = rST_User;
			String string = rST_User == null ? null : rST_User.getId();
			this.rST_User__resolvedKey = this.idUser = string;
			return;
		}
	}

	public void setRecordingPeriod(int n) {
		this.recordingPeriod = n;
	}

	public void setSignalQualityIsValid(boolean bl) {
		this.signalQualityIsValid = bl;
	}

	public void setSignalQualityMean(float f) {
		this.signalQualityMean = f;
	}

	public void setSignalQualityPercBin1(float f) {
		this.signalQualityPercBin1 = f;
	}

	public void setSignalQualityPercBin2(float f) {
		this.signalQualityPercBin2 = f;
	}

	public void setSignalQualityPercBin3(float f) {
		this.signalQualityPercBin3 = f;
	}

	public void setSignalQualityPercBin4(float f) {
		this.signalQualityPercBin4 = f;
	}

	public void setSignalQualityPercBin5(float f) {
		this.signalQualityPercBin5 = f;
	}

	public void setSignalQualityStd(float f) {
		this.signalQualityStd = f;
	}

	public void setSignalQualityValue(int n) {
		this.signalQualityValue = n;
	}

	public void setSleepScore(int n) {
		this.sleepScore = n;
	}

	public void setStartTime(Date date) {
		this.startTime = date;
	}

	public void setStopTime(Date date) {
		this.stopTime = date;
	}

	public void setTimeInBed(int n) {
		this.timeInBed = n;
	}

	public void setTimeToSleep(int n) {
		this.timeToSleep = n;
	}

	public void setTotalDeepSleep(int n) {
		this.totalDeepSleep = n;
	}

	public void setTotalLightSleep(int n) {
		this.totalLightSleep = n;
	}

	public void setTotalRem(int n) {
		this.totalRem = n;
	}

	public void setTotalSleepTime(int n) {
		this.totalSleepTime = n;
	}

	public void setTotalWakeTime(int n) {
		this.totalWakeTime = n;
	}

	public void setUploaded(boolean bl) {
		this.uploaded = bl;
	}

	public void setWasoTime(int n) {
		this.wasoTime = n;
	}

	public void setup() {
		this.sleepScore = 0;
		this.bodyScore = 0;
		this.mindScore = 0;
		this.uploaded = false;
		this.completed = false;
		long l = 978307200000L + 1000 * this.id;
		this.startTime = new Date(l);
		this.stopTime = new Date(10000 + l);
		this.questionIds = "";
		this.answerValues = "";
	}

	public void update() {
		if (this.myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		this.myDao.update(this);
	}
}

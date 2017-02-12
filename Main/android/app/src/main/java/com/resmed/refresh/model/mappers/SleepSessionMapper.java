/*
 * Decompiled with CFR 0_115.
 *
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.resmed.refresh.model.RST_EnvironmentalInfo
 *  com.resmed.refresh.model.RST_LocationItem
 *  com.resmed.refresh.model.RST_NightQuestions
 *  com.resmed.refresh.model.RST_SleepEvent
 *  com.resmed.refresh.model.RST_SleepSessionInfo
 *  com.resmed.refresh.model.RST_ValueItem
 *  com.resmed.refresh.model.RefreshModelController
 *  com.resmed.refresh.model.json.Location
 *  com.resmed.refresh.model.json.Record
 *  com.resmed.refresh.model.json.SleepEvent
 *  com.resmed.refresh.model.json.SynopsisData
 *  com.resmed.refresh.model.mappers.LocationMapper
 *  com.resmed.refresh.model.mappers.NightQuestionsMapper
 *  com.resmed.refresh.model.mappers.SleepSessionMapper
 *  com.resmed.refresh.utils.AppFileLog
 *  com.resmed.refresh.utils.Log
 *  com.resmed.refresh.utils.RefreshTools
 *  com.resmed.refresh.utils.preSleepLog
 */
package com.resmed.refresh.model.mappers;

import com.google.gson.Gson;
import com.resmed.refresh.model.RST_EnvironmentalInfo;
import com.resmed.refresh.model.RST_SleepEvent;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RST_ValueItem;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.Record;
import com.resmed.refresh.model.json.SleepEvent;
import com.resmed.refresh.model.json.SynopsisData;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.refresh.utils.preSleepLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/*
 * Exception performing whole class analysis ignored.
 */
public class SleepSessionMapper {
	public SleepSessionMapper() {
	}

	public static Record getRecord(RST_SleepSessionInfo rST_SleepSessionInfo, RST_NightQuestions rST_NightQuestions) {
		Record record = new Record();
		record.setRecordId(Long.valueOf(rST_SleepSessionInfo.getId()));
		SynopsisData synopsisData = new SynopsisData();
		synopsisData.setAlarmFireEpoch(rST_SleepSessionInfo.getAlarmFireEpoch());
		synopsisData.setBinSleepScoreDeep(rST_SleepSessionInfo.getBinSleepScoreDeep());
		synopsisData.setBinSleepScoreLight(rST_SleepSessionInfo.getBinSleepScoreLight());
		synopsisData.setBinSleepScoreOnset(rST_SleepSessionInfo.getBinSleepScoreOnset());
		synopsisData.setBinSleepScoreRem(rST_SleepSessionInfo.getBinSleepScoreRem());
		synopsisData.setBinSleepScoreTst(rST_SleepSessionInfo.getBinSleepScoreTst());
		synopsisData.setBinSleepScoreWaso(rST_SleepSessionInfo.getBinSleepScoreWaso());
		synopsisData.setBodyScore(rST_SleepSessionInfo.getBodyScore());
		synopsisData.setDeepSleepDuration(rST_SleepSessionInfo.getTotalDeepSleep());
		synopsisData.setLightSleepDuration(rST_SleepSessionInfo.getTotalLightSleep());
		synopsisData.setMindScore(rST_SleepSessionInfo.getMindScore());
		synopsisData.setNumberOfInterruptions(rST_SleepSessionInfo.getNumberInterruptions());
		synopsisData.setRemDuration(rST_SleepSessionInfo.getTotalRem());
		synopsisData.setSignalQualityIsValid(rST_SleepSessionInfo.getSignalQualityIsValid());
		synopsisData.setSignalQualityMean(rST_SleepSessionInfo.getSignalQualityMean());
		synopsisData.setSignalQualityPercBin1(rST_SleepSessionInfo.getSignalQualityPercBin1());
		synopsisData.setSignalQualityPercBin2(rST_SleepSessionInfo.getSignalQualityPercBin2());
		synopsisData.setSignalQualityPercBin3(rST_SleepSessionInfo.getSignalQualityPercBin3());
		synopsisData.setSignalQualityPercBin4(rST_SleepSessionInfo.getSignalQualityPercBin4());
		synopsisData.setSignalQualityPercBin5(rST_SleepSessionInfo.getSignalQualityPercBin5());
		synopsisData.setSignalQualityStd(rST_SleepSessionInfo.getSignalQualityStd());
		synopsisData.setSignalQualityValue(rST_SleepSessionInfo.getSignalQualityValue());
		synopsisData.setSleepScore(rST_SleepSessionInfo.getSleepScore());
		synopsisData.setTimeInBed(rST_SleepSessionInfo.getTimeInBed());
		synopsisData.setTimeToSleep(rST_SleepSessionInfo.getTimeToSleep());
		synopsisData.setTotalRecordingTime(rST_SleepSessionInfo.getRecordingPeriod());
		synopsisData.setTotalSleepTime(rST_SleepSessionInfo.getTotalSleepTime());
		synopsisData.setTotalWakeTime(rST_SleepSessionInfo.getTotalWakeTime());
		record.setSynopsisData(synopsisData);
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		Iterator iterator = rST_SleepSessionInfo.getSleepStates().iterator();
		do {
			if (!iterator.hasNext()) break;
			arrayList.add((int)((RST_ValueItem)iterator.next()).getValue());
		} while (true);
		record.setHypnogram(arrayList);
		ArrayList<Float> arrayList2 = new ArrayList<Float>();
		Iterator iterator2 = rST_SleepSessionInfo.getEnvironmentalInfo().getSessionTemperature().iterator();
		do {
			if (!iterator2.hasNext()) break;
			arrayList2.add(Float.valueOf(((RST_ValueItem)iterator2.next()).getValue()));
		} while (true);
		record.setTemperature(arrayList2);
		ArrayList<Float> arrayList3 = new ArrayList<Float>();
		Iterator iterator3 = rST_SleepSessionInfo.getEnvironmentalInfo().getSessionSound().iterator();
		do {
			if (!iterator3.hasNext()) break;
			arrayList3.add(Float.valueOf(((RST_ValueItem)iterator3.next()).getValue()));
		} while (true);
		record.setNoise(arrayList3);
		ArrayList<Float> arrayList4 = new ArrayList<Float>();
		Iterator iterator4 = rST_SleepSessionInfo.getEnvironmentalInfo().getSessionLight().iterator();
		do {
			if (!iterator4.hasNext()) {
				record.setLight(arrayList4);
				record.setTemperatureSamplePeriod(30);
				record.setLightSamplePeriod(30);
				record.setHypnogramSamplePeriod(30);
				AppFileLog.addTrace((String)"Location - creating record to upload. Adding location");
				RST_LocationItem rST_LocationItem = RefreshModelController.getInstance().getLastLocation();
				if (rST_LocationItem == null) {
					rST_LocationItem = RefreshModelController.getInstance().createLocationItem(-1.0f, -1.0f);
				}
				record.setLocation(LocationMapper.getLocation((RST_LocationItem)rST_LocationItem));
				record.getLocation().setTimeZone(String.valueOf(RefreshModelController.getInstance().userTimezoneOffset()));
				AppFileLog.addTrace((String)("Location - Adding location (" + record.getLocation().getLatitude() + "," + record.getLocation().getLongitude() + ") to RecordID:" + record.getRecordId()));
				if (RefreshModelController.getInstance().getLocationPermission()) break;
				record.getLocation().setLatitude("-1");
				record.getLocation().setLongitude("-1");
				AppFileLog.addTrace((String)"Location - Location Permission is OFF, changing location to -1,-1");
			}
			arrayList4.add(Float.valueOf(((RST_ValueItem)iterator4.next()).getValue()));
		} while (true);
		try {
			AppFileLog.addTrace((String)"Location - Location Permission is ON");
		}
		catch (NullPointerException var16_13) {
			AppFileLog.addTrace((String)("Location - NullPointerException location set to -1,-1 ERROR:" + var16_13.getMessage()));
			record.setLocation(LocationMapper.getLocation((RST_LocationItem)RefreshModelController.getInstance().createLocationItem(-1.0f, -1.0f)));
			var16_13.printStackTrace();
		}
		ArrayList<SleepEvent> arrayList5 = new ArrayList<SleepEvent>();
		Iterator iterator5 = rST_SleepSessionInfo.getEvents().iterator();
		do {
			if (!iterator5.hasNext()) {
				record.setSleepEvents(arrayList5);
				record.setStartDate(RefreshTools.getStringFromDate((Date)rST_SleepSessionInfo.getStartTime()));
				record.setEndDate(RefreshTools.getStringFromDate((Date)rST_SleepSessionInfo.getStopTime()));
				record.setPreSleepQuestions(NightQuestionsMapper.getPreSleepQuestions((String)rST_SleepSessionInfo.getQuestionIds(), (String)rST_SleepSessionInfo.getAnswerValues()));
				AppFileLog.addTrace((String)("NightQuestions mapping questions for recordId:" + record.getRecordId() + " questions:" + rST_SleepSessionInfo.getQuestionIds() + " answers:" + rST_SleepSessionInfo.getAnswerValues()));
				preSleepLog.addTrace((String)("Sleep Session Stopped: NightQuestions mapping questions for recordId:" + record.getRecordId() + " questions:" + rST_SleepSessionInfo.getQuestionIds() + " answers:" + rST_SleepSessionInfo.getAnswerValues()));
				record.setFirmwareVersion(RefreshModelController.getInstance().getFirmwareVersion());
				record.setRm20Version(RefreshModelController.getInstance().getRM20LibraryVersion());
				String string = new Gson().toJson((Object)record);
				AppFileLog.addTrace((String)("Sending record:" + string));
				return record;
			}
			RST_SleepEvent rST_SleepEvent = (RST_SleepEvent)iterator5.next();
			SleepEvent sleepEvent = new SleepEvent();
			sleepEvent.setSampleNumber(rST_SleepEvent.getEpochNumber());
			sleepEvent.setType(rST_SleepEvent.getType());
			sleepEvent.setValue(rST_SleepEvent.getValue());
			arrayList5.add(sleepEvent);
			Log.d((String)"com.resmed.refresh.sync", (String)("Record ID " + record.getRecordId() + " Event::EpochNumber=" + sleepEvent.getSampleNumber() + " Type=" + sleepEvent.getType() + " Value=" + sleepEvent.getValue()));
		} while (true);
	}

	public static List<RST_SleepSessionInfo> processListSleepSession(List<Record> list) {
		ArrayList<RST_SleepSessionInfo> arrayList = new ArrayList<RST_SleepSessionInfo>();
		Iterator<Record> iterator = list.iterator();
		while (iterator.hasNext()) {
			arrayList.add(SleepSessionMapper.processSleepSession((Record)iterator.next()));
		}
		return arrayList;
	}

	public static RST_SleepSessionInfo processSleepSession(final Record record) {
		synchronized (SleepSessionMapper.class) {
			final RefreshModelController instance = RefreshModelController.getInstance();
			final RST_SleepSessionInfo localSleepSessionForId = RefreshModelController.getInstance().localSleepSessionForId((long)record.getRecordId());
			RST_SleepSessionInfo rst_SleepSessionInfo;
			if (localSleepSessionForId != null) {
				rst_SleepSessionInfo = localSleepSessionForId;
			}
			else {
				final RST_SleepSessionInfo sleepSessionInfo = instance.createSleepSessionInfo((long)record.getRecordId());
				final RST_EnvironmentalInfo environmentalInfo = instance.createEnvironmentalInfo();
				sleepSessionInfo.setEnvironmentalInfo(environmentalInfo);
				final ArrayList<RST_ValueItem> list = new ArrayList<RST_ValueItem>();
				for (int i = 0; i < record.getLight().size(); ++i) {
					final RST_ValueItem rst_ValueItem = new RST_ValueItem();
					rst_ValueItem.setValue((float)record.getLight().get(i));
					rst_ValueItem.setOrdr(i);
					list.add(rst_ValueItem);
				}
				environmentalInfo.addSessionLightArray((List)list);
				final ArrayList<RST_ValueItem> list2 = new ArrayList<RST_ValueItem>();
				if (record.getNoise() != null) {
					for (int j = 0; j < record.getNoise().size(); ++j) {
						final RST_ValueItem rst_ValueItem2 = new RST_ValueItem();
						rst_ValueItem2.setValue((float)record.getNoise().get(j));
						rst_ValueItem2.setOrdr(j);
						list2.add(rst_ValueItem2);
					}
				}
				environmentalInfo.addSessionSoundArray((List)list2);
				final ArrayList<RST_ValueItem> list3 = new ArrayList<RST_ValueItem>();
				for (int k = 0; k < record.getTemperature().size(); ++k) {
					final RST_ValueItem rst_ValueItem3 = new RST_ValueItem();
					rst_ValueItem3.setValue((float)record.getTemperature().get(k));
					rst_ValueItem3.setOrdr(k);
					list3.add(rst_ValueItem3);
				}
				environmentalInfo.addSessionTemperatureArray((List)list3);
				environmentalInfo.update();
				sleepSessionInfo.setId((long)record.getRecordId());
				sleepSessionInfo.setAlarmFireEpoch(record.getSynopsisData().getAlarmFireEpoch());
				sleepSessionInfo.setBodyScore(record.getSynopsisData().getBodyScore());
				sleepSessionInfo.setMindScore(record.getSynopsisData().getMindScore());
				sleepSessionInfo.setNumberInterruptions(record.getSynopsisData().getNumberOfInterruptions());
				sleepSessionInfo.setRecordingPeriod(record.getSynopsisData().getTotalRecordingTime());
				sleepSessionInfo.setSleepScore(record.getSynopsisData().getSleepScore());
				sleepSessionInfo.setStartTime(RefreshTools.getDateFromString(record.getStartDate()));
				sleepSessionInfo.setStopTime(RefreshTools.getDateFromString(record.getEndDate()));
				sleepSessionInfo.setTimeInBed(record.getSynopsisData().getTimeInBed());
				sleepSessionInfo.setTimeToSleep(record.getSynopsisData().getTimeToSleep());
				sleepSessionInfo.setTotalSleepTime(record.getSynopsisData().getTotalSleepTime());
				sleepSessionInfo.setTotalWakeTime(record.getSynopsisData().getTotalWakeTime());
				sleepSessionInfo.setTotalRem(record.getSynopsisData().getRemDuration());
				sleepSessionInfo.setTotalDeepSleep(record.getSynopsisData().getDeepSleepDuration());
				sleepSessionInfo.setTotalLightSleep(record.getSynopsisData().getLightSleepDuration());
				sleepSessionInfo.setSignalQualityIsValid(record.getSynopsisData().signalQualityIsValid());
				sleepSessionInfo.setSignalQualityValue(record.getSynopsisData().getSignalQualityValue());
				sleepSessionInfo.setSignalQualityMean(record.getSynopsisData().getSignalQualityMean());
				sleepSessionInfo.setSignalQualityPercBin1(record.getSynopsisData().getSignalQualityPercBin1());
				sleepSessionInfo.setSignalQualityPercBin2(record.getSynopsisData().getSignalQualityPercBin2());
				sleepSessionInfo.setSignalQualityPercBin3(record.getSynopsisData().getSignalQualityPercBin3());
				sleepSessionInfo.setSignalQualityPercBin4(record.getSynopsisData().getSignalQualityPercBin4());
				sleepSessionInfo.setSignalQualityPercBin5(record.getSynopsisData().getSignalQualityPercBin5());
				sleepSessionInfo.setSignalQualityStd(record.getSynopsisData().getSignalQualityStd());
				sleepSessionInfo.setBinSleepScoreTst(record.getSynopsisData().getBinSleepScoreTst());
				sleepSessionInfo.setBinSleepScoreDeep(record.getSynopsisData().getBinSleepScoreDeep());
				sleepSessionInfo.setBinSleepScoreLight(record.getSynopsisData().getBinSleepScoreLight());
				sleepSessionInfo.setBinSleepScoreOnset(record.getSynopsisData().getBinSleepScoreOnset());
				sleepSessionInfo.setBinSleepScoreRem(record.getSynopsisData().getBinSleepScoreRem());
				sleepSessionInfo.setBinSleepScoreWaso(record.getSynopsisData().getBinSleepScoreWaso());
				sleepSessionInfo.setUploaded(true);
				sleepSessionInfo.setCompleted(true);
				final ArrayList<RST_ValueItem> list4 = new ArrayList<RST_ValueItem>();
				for (int l = 0; l < record.getHypnogram().size(); ++l) {
					final RST_ValueItem rst_ValueItem4 = new RST_ValueItem();
					rst_ValueItem4.setValue((float)record.getHypnogram().get(l));
					rst_ValueItem4.setOrdr(l);
					list4.add(rst_ValueItem4);
				}
				sleepSessionInfo.addSleepStateArray((List)list4);
				for (int n = 0; n < record.getSleepEvents().size(); ++n) {
					final RST_SleepEvent rst_SleepEvent = new RST_SleepEvent();
					rst_SleepEvent.setEpochNumber((int)record.getSleepEvents().get(n).getSampleNumber());
					rst_SleepEvent.setValue((int)record.getSleepEvents().get(n).getValue());
					rst_SleepEvent.setType(record.getSleepEvents().get(n).getType());
					rst_SleepEvent.setRST_SleepSessionInfo(sleepSessionInfo);
					sleepSessionInfo.addEvent(rst_SleepEvent);
				}
				sleepSessionInfo.update();
				Log.d("com.resmed.refresh.ids", "SleepSessionMapper processing record id = " + sleepSessionInfo.getId() + " startDate = " + sleepSessionInfo.getStartTime());
				rst_SleepSessionInfo = sleepSessionInfo;
			}
			return rst_SleepSessionInfo;
		}
	}
}

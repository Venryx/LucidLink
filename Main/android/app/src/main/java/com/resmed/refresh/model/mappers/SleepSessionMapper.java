package com.resmed.refresh.model.mappers;

import com.google.gson.Gson;
import com.resmed.refresh.model.RST_EnvironmentalInfo;
import com.resmed.refresh.model.RST_NightQuestions;
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
import java.util.Iterator;
import java.util.List;

public class SleepSessionMapper
{
  public static Record getRecord(RST_SleepSessionInfo paramRST_SleepSessionInfo, RST_NightQuestions paramRST_NightQuestions)
  {
    localRecord = new Record();
    localRecord.setRecordId(Long.valueOf(paramRST_SleepSessionInfo.getId()));
    paramRST_NightQuestions = new SynopsisData();
    paramRST_NightQuestions.setAlarmFireEpoch(paramRST_SleepSessionInfo.getAlarmFireEpoch());
    paramRST_NightQuestions.setBinSleepScoreDeep(paramRST_SleepSessionInfo.getBinSleepScoreDeep());
    paramRST_NightQuestions.setBinSleepScoreLight(paramRST_SleepSessionInfo.getBinSleepScoreLight());
    paramRST_NightQuestions.setBinSleepScoreOnset(paramRST_SleepSessionInfo.getBinSleepScoreOnset());
    paramRST_NightQuestions.setBinSleepScoreRem(paramRST_SleepSessionInfo.getBinSleepScoreRem());
    paramRST_NightQuestions.setBinSleepScoreTst(paramRST_SleepSessionInfo.getBinSleepScoreTst());
    paramRST_NightQuestions.setBinSleepScoreWaso(paramRST_SleepSessionInfo.getBinSleepScoreWaso());
    paramRST_NightQuestions.setBodyScore(paramRST_SleepSessionInfo.getBodyScore());
    paramRST_NightQuestions.setDeepSleepDuration(paramRST_SleepSessionInfo.getTotalDeepSleep());
    paramRST_NightQuestions.setLightSleepDuration(paramRST_SleepSessionInfo.getTotalLightSleep());
    paramRST_NightQuestions.setMindScore(paramRST_SleepSessionInfo.getMindScore());
    paramRST_NightQuestions.setNumberOfInterruptions(paramRST_SleepSessionInfo.getNumberInterruptions());
    paramRST_NightQuestions.setRemDuration(paramRST_SleepSessionInfo.getTotalRem());
    paramRST_NightQuestions.setSignalQualityIsValid(paramRST_SleepSessionInfo.getSignalQualityIsValid());
    paramRST_NightQuestions.setSignalQualityMean(paramRST_SleepSessionInfo.getSignalQualityMean());
    paramRST_NightQuestions.setSignalQualityPercBin1(paramRST_SleepSessionInfo.getSignalQualityPercBin1());
    paramRST_NightQuestions.setSignalQualityPercBin2(paramRST_SleepSessionInfo.getSignalQualityPercBin2());
    paramRST_NightQuestions.setSignalQualityPercBin3(paramRST_SleepSessionInfo.getSignalQualityPercBin3());
    paramRST_NightQuestions.setSignalQualityPercBin4(paramRST_SleepSessionInfo.getSignalQualityPercBin4());
    paramRST_NightQuestions.setSignalQualityPercBin5(paramRST_SleepSessionInfo.getSignalQualityPercBin5());
    paramRST_NightQuestions.setSignalQualityStd(paramRST_SleepSessionInfo.getSignalQualityStd());
    paramRST_NightQuestions.setSignalQualityValue(paramRST_SleepSessionInfo.getSignalQualityValue());
    paramRST_NightQuestions.setSleepScore(paramRST_SleepSessionInfo.getSleepScore());
    paramRST_NightQuestions.setTimeInBed(paramRST_SleepSessionInfo.getTimeInBed());
    paramRST_NightQuestions.setTimeToSleep(paramRST_SleepSessionInfo.getTimeToSleep());
    paramRST_NightQuestions.setTotalRecordingTime(paramRST_SleepSessionInfo.getRecordingPeriod());
    paramRST_NightQuestions.setTotalSleepTime(paramRST_SleepSessionInfo.getTotalSleepTime());
    paramRST_NightQuestions.setTotalWakeTime(paramRST_SleepSessionInfo.getTotalWakeTime());
    localRecord.setSynopsisData(paramRST_NightQuestions);
    paramRST_NightQuestions = new ArrayList();
    localObject = paramRST_SleepSessionInfo.getSleepStates().iterator();
    if (!((Iterator)localObject).hasNext())
    {
      localRecord.setHypnogram(paramRST_NightQuestions);
      paramRST_NightQuestions = new ArrayList();
      localObject = paramRST_SleepSessionInfo.getEnvironmentalInfo().getSessionTemperature().iterator();
      label309:
      if (((Iterator)localObject).hasNext()) {
        break label808;
      }
      localRecord.setTemperature(paramRST_NightQuestions);
      localObject = new ArrayList();
      paramRST_NightQuestions = paramRST_SleepSessionInfo.getEnvironmentalInfo().getSessionSound().iterator();
      label344:
      if (paramRST_NightQuestions.hasNext()) {
        break label833;
      }
      localRecord.setNoise((List)localObject);
      paramRST_NightQuestions = new ArrayList();
      localObject = paramRST_SleepSessionInfo.getEnvironmentalInfo().getSessionLight().iterator();
      label379:
      if (((Iterator)localObject).hasNext()) {
        break label858;
      }
      localRecord.setLight(paramRST_NightQuestions);
      localRecord.setTemperatureSamplePeriod(30);
      localRecord.setLightSamplePeriod(30);
      localRecord.setHypnogramSamplePeriod(30);
      AppFileLog.addTrace("Location - creating record to upload. Adding location");
    }
    for (;;)
    {
      try
      {
        localObject = RefreshModelController.getInstance().getLastLocation();
        paramRST_NightQuestions = (RST_NightQuestions)localObject;
        if (localObject == null) {
          paramRST_NightQuestions = RefreshModelController.getInstance().createLocationItem(-1.0F, -1.0F);
        }
        localRecord.setLocation(LocationMapper.getLocation(paramRST_NightQuestions));
        localRecord.getLocation().setTimeZone(String.valueOf(RefreshModelController.getInstance().userTimezoneOffset()));
        paramRST_NightQuestions = new java.lang.StringBuilder;
        paramRST_NightQuestions.<init>("Location - Adding location (");
        AppFileLog.addTrace(localRecord.getLocation().getLatitude() + "," + localRecord.getLocation().getLongitude() + ") to RecordID:" + localRecord.getRecordId());
        if (RefreshModelController.getInstance().getLocationPermission()) {
          continue;
        }
        localRecord.getLocation().setLatitude("-1");
        localRecord.getLocation().setLongitude("-1");
        AppFileLog.addTrace("Location - Location Permission is OFF, changing location to -1,-1");
      }
      catch (NullPointerException paramRST_NightQuestions)
      {
        ArrayList localArrayList;
        label808:
        label833:
        label858:
        AppFileLog.addTrace("Location - NullPointerException location set to -1,-1 ERROR:" + paramRST_NightQuestions.getMessage());
        localRecord.setLocation(LocationMapper.getLocation(RefreshModelController.getInstance().createLocationItem(-1.0F, -1.0F)));
        paramRST_NightQuestions.printStackTrace();
        continue;
        RST_SleepEvent localRST_SleepEvent = (RST_SleepEvent)paramRST_NightQuestions.next();
        localObject = new SleepEvent();
        ((SleepEvent)localObject).setSampleNumber(localRST_SleepEvent.getEpochNumber());
        ((SleepEvent)localObject).setType(localRST_SleepEvent.getType());
        ((SleepEvent)localObject).setValue(localRST_SleepEvent.getValue());
        localArrayList.add(localObject);
        Log.d("com.resmed.refresh.sync", "Record ID " + localRecord.getRecordId() + " Event::EpochNumber=" + ((SleepEvent)localObject).getSampleNumber() + " Type=" + ((SleepEvent)localObject).getType() + " Value=" + ((SleepEvent)localObject).getValue());
        continue;
      }
      localArrayList = new ArrayList();
      paramRST_NightQuestions = paramRST_SleepSessionInfo.getEvents().iterator();
      if (paramRST_NightQuestions.hasNext()) {
        continue;
      }
      localRecord.setSleepEvents(localArrayList);
      localRecord.setStartDate(RefreshTools.getStringFromDate(paramRST_SleepSessionInfo.getStartTime()));
      localRecord.setEndDate(RefreshTools.getStringFromDate(paramRST_SleepSessionInfo.getStopTime()));
      localRecord.setPreSleepQuestions(NightQuestionsMapper.getPreSleepQuestions(paramRST_SleepSessionInfo.getQuestionIds(), paramRST_SleepSessionInfo.getAnswerValues()));
      AppFileLog.addTrace("NightQuestions mapping questions for recordId:" + localRecord.getRecordId() + " questions:" + paramRST_SleepSessionInfo.getQuestionIds() + " answers:" + paramRST_SleepSessionInfo.getAnswerValues());
      preSleepLog.addTrace("Sleep Session Stopped: NightQuestions mapping questions for recordId:" + localRecord.getRecordId() + " questions:" + paramRST_SleepSessionInfo.getQuestionIds() + " answers:" + paramRST_SleepSessionInfo.getAnswerValues());
      localRecord.setFirmwareVersion(RefreshModelController.getInstance().getFirmwareVersion());
      localRecord.setRm20Version(RefreshModelController.getInstance().getRM20LibraryVersion());
      paramRST_SleepSessionInfo = new Gson().toJson(localRecord);
      AppFileLog.addTrace("Sending record:" + paramRST_SleepSessionInfo);
      return localRecord;
      paramRST_NightQuestions.add(Integer.valueOf((int)((RST_ValueItem)((Iterator)localObject).next()).getValue()));
      break;
      paramRST_NightQuestions.add(Float.valueOf(((RST_ValueItem)((Iterator)localObject).next()).getValue()));
      break label309;
      ((List)localObject).add(Float.valueOf(((RST_ValueItem)paramRST_NightQuestions.next()).getValue()));
      break label344;
      paramRST_NightQuestions.add(Float.valueOf(((RST_ValueItem)((Iterator)localObject).next()).getValue()));
      break label379;
      AppFileLog.addTrace("Location - Location Permission is ON");
    }
  }
  
  public static List<RST_SleepSessionInfo> processListSleepSession(List<Record> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    paramList = paramList.iterator();
    for (;;)
    {
      if (!paramList.hasNext()) {
        return localArrayList;
      }
      localArrayList.add(processSleepSession((Record)paramList.next()));
    }
  }
  
  /* Error */
  public static RST_SleepSessionInfo processSleepSession(Record paramRecord)
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: invokestatic 282	com/resmed/refresh/model/RefreshModelController:getInstance	()Lcom/resmed/refresh/model/RefreshModelController;
    //   6: astore_3
    //   7: invokestatic 282	com/resmed/refresh/model/RefreshModelController:getInstance	()Lcom/resmed/refresh/model/RefreshModelController;
    //   10: aload_0
    //   11: invokevirtual 342	com/resmed/refresh/model/json/Record:getRecordId	()Ljava/lang/Long;
    //   14: invokevirtual 525	java/lang/Long:longValue	()J
    //   17: invokevirtual 529	com/resmed/refresh/model/RefreshModelController:localSleepSessionForId	(J)Lcom/resmed/refresh/model/RST_SleepSessionInfo;
    //   20: astore_2
    //   21: aload_2
    //   22: ifnull +10 -> 32
    //   25: aload_2
    //   26: astore_0
    //   27: ldc 2
    //   29: monitorexit
    //   30: aload_0
    //   31: areturn
    //   32: aload_3
    //   33: aload_0
    //   34: invokevirtual 342	com/resmed/refresh/model/json/Record:getRecordId	()Ljava/lang/Long;
    //   37: invokevirtual 525	java/lang/Long:longValue	()J
    //   40: invokevirtual 532	com/resmed/refresh/model/RefreshModelController:createSleepSessionInfo	(J)Lcom/resmed/refresh/model/RST_SleepSessionInfo;
    //   43: astore_2
    //   44: aload_3
    //   45: invokevirtual 535	com/resmed/refresh/model/RefreshModelController:createEnvironmentalInfo	()Lcom/resmed/refresh/model/RST_EnvironmentalInfo;
    //   48: astore_3
    //   49: aload_2
    //   50: aload_3
    //   51: invokevirtual 539	com/resmed/refresh/model/RST_SleepSessionInfo:setEnvironmentalInfo	(Lcom/resmed/refresh/model/RST_EnvironmentalInfo;)V
    //   54: new 215	java/util/ArrayList
    //   57: astore 5
    //   59: aload 5
    //   61: invokespecial 216	java/util/ArrayList:<init>	()V
    //   64: iconst_0
    //   65: istore_1
    //   66: iload_1
    //   67: aload_0
    //   68: invokevirtual 542	com/resmed/refresh/model/json/Record:getLight	()Ljava/util/List;
    //   71: invokeinterface 545 1 0
    //   76: if_icmplt +527 -> 603
    //   79: aload_3
    //   80: aload 5
    //   82: invokevirtual 548	com/resmed/refresh/model/RST_EnvironmentalInfo:addSessionLightArray	(Ljava/util/List;)V
    //   85: new 215	java/util/ArrayList
    //   88: astore 5
    //   90: aload 5
    //   92: invokespecial 216	java/util/ArrayList:<init>	()V
    //   95: aload_0
    //   96: invokevirtual 551	com/resmed/refresh/model/json/Record:getNoise	()Ljava/util/List;
    //   99: ifnull +18 -> 117
    //   102: iconst_0
    //   103: istore_1
    //   104: iload_1
    //   105: aload_0
    //   106: invokevirtual 551	com/resmed/refresh/model/json/Record:getNoise	()Ljava/util/List;
    //   109: invokeinterface 545 1 0
    //   114: if_icmplt +542 -> 656
    //   117: aload_3
    //   118: aload 5
    //   120: invokevirtual 554	com/resmed/refresh/model/RST_EnvironmentalInfo:addSessionSoundArray	(Ljava/util/List;)V
    //   123: new 215	java/util/ArrayList
    //   126: astore 5
    //   128: aload 5
    //   130: invokespecial 216	java/util/ArrayList:<init>	()V
    //   133: iconst_0
    //   134: istore_1
    //   135: iload_1
    //   136: aload_0
    //   137: invokevirtual 557	com/resmed/refresh/model/json/Record:getTemperature	()Ljava/util/List;
    //   140: invokeinterface 545 1 0
    //   145: if_icmplt +564 -> 709
    //   148: aload_3
    //   149: aload 5
    //   151: invokevirtual 560	com/resmed/refresh/model/RST_EnvironmentalInfo:addSessionTemperatureArray	(Ljava/util/List;)V
    //   154: aload_3
    //   155: invokevirtual 563	com/resmed/refresh/model/RST_EnvironmentalInfo:update	()V
    //   158: aload_2
    //   159: aload_0
    //   160: invokevirtual 342	com/resmed/refresh/model/json/Record:getRecordId	()Ljava/lang/Long;
    //   163: invokevirtual 525	java/lang/Long:longValue	()J
    //   166: invokevirtual 567	com/resmed/refresh/model/RST_SleepSessionInfo:setId	(J)V
    //   169: aload_2
    //   170: aload_0
    //   171: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   174: invokevirtual 572	com/resmed/refresh/model/json/SynopsisData:getAlarmFireEpoch	()I
    //   177: invokevirtual 573	com/resmed/refresh/model/RST_SleepSessionInfo:setAlarmFireEpoch	(I)V
    //   180: aload_2
    //   181: aload_0
    //   182: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   185: invokevirtual 574	com/resmed/refresh/model/json/SynopsisData:getBodyScore	()I
    //   188: invokevirtual 575	com/resmed/refresh/model/RST_SleepSessionInfo:setBodyScore	(I)V
    //   191: aload_2
    //   192: aload_0
    //   193: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   196: invokevirtual 576	com/resmed/refresh/model/json/SynopsisData:getMindScore	()I
    //   199: invokevirtual 577	com/resmed/refresh/model/RST_SleepSessionInfo:setMindScore	(I)V
    //   202: aload_2
    //   203: aload_0
    //   204: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   207: invokevirtual 580	com/resmed/refresh/model/json/SynopsisData:getNumberOfInterruptions	()I
    //   210: invokevirtual 583	com/resmed/refresh/model/RST_SleepSessionInfo:setNumberInterruptions	(I)V
    //   213: aload_2
    //   214: aload_0
    //   215: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   218: invokevirtual 586	com/resmed/refresh/model/json/SynopsisData:getTotalRecordingTime	()I
    //   221: invokevirtual 589	com/resmed/refresh/model/RST_SleepSessionInfo:setRecordingPeriod	(I)V
    //   224: aload_2
    //   225: aload_0
    //   226: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   229: invokevirtual 590	com/resmed/refresh/model/json/SynopsisData:getSleepScore	()I
    //   232: invokevirtual 591	com/resmed/refresh/model/RST_SleepSessionInfo:setSleepScore	(I)V
    //   235: aload_2
    //   236: aload_0
    //   237: invokevirtual 594	com/resmed/refresh/model/json/Record:getStartDate	()Ljava/lang/String;
    //   240: invokestatic 598	com/resmed/refresh/utils/RefreshTools:getDateFromString	(Ljava/lang/String;)Ljava/util/Date;
    //   243: invokevirtual 602	com/resmed/refresh/model/RST_SleepSessionInfo:setStartTime	(Ljava/util/Date;)V
    //   246: aload_2
    //   247: aload_0
    //   248: invokevirtual 605	com/resmed/refresh/model/json/Record:getEndDate	()Ljava/lang/String;
    //   251: invokestatic 598	com/resmed/refresh/utils/RefreshTools:getDateFromString	(Ljava/lang/String;)Ljava/util/Date;
    //   254: invokevirtual 608	com/resmed/refresh/model/RST_SleepSessionInfo:setStopTime	(Ljava/util/Date;)V
    //   257: aload_2
    //   258: aload_0
    //   259: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   262: invokevirtual 609	com/resmed/refresh/model/json/SynopsisData:getTimeInBed	()I
    //   265: invokevirtual 610	com/resmed/refresh/model/RST_SleepSessionInfo:setTimeInBed	(I)V
    //   268: aload_2
    //   269: aload_0
    //   270: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   273: invokevirtual 611	com/resmed/refresh/model/json/SynopsisData:getTimeToSleep	()I
    //   276: invokevirtual 612	com/resmed/refresh/model/RST_SleepSessionInfo:setTimeToSleep	(I)V
    //   279: aload_2
    //   280: aload_0
    //   281: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   284: invokevirtual 613	com/resmed/refresh/model/json/SynopsisData:getTotalSleepTime	()I
    //   287: invokevirtual 614	com/resmed/refresh/model/RST_SleepSessionInfo:setTotalSleepTime	(I)V
    //   290: aload_2
    //   291: aload_0
    //   292: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   295: invokevirtual 615	com/resmed/refresh/model/json/SynopsisData:getTotalWakeTime	()I
    //   298: invokevirtual 616	com/resmed/refresh/model/RST_SleepSessionInfo:setTotalWakeTime	(I)V
    //   301: aload_2
    //   302: aload_0
    //   303: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   306: invokevirtual 619	com/resmed/refresh/model/json/SynopsisData:getRemDuration	()I
    //   309: invokevirtual 622	com/resmed/refresh/model/RST_SleepSessionInfo:setTotalRem	(I)V
    //   312: aload_2
    //   313: aload_0
    //   314: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   317: invokevirtual 625	com/resmed/refresh/model/json/SynopsisData:getDeepSleepDuration	()I
    //   320: invokevirtual 628	com/resmed/refresh/model/RST_SleepSessionInfo:setTotalDeepSleep	(I)V
    //   323: aload_2
    //   324: aload_0
    //   325: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   328: invokevirtual 631	com/resmed/refresh/model/json/SynopsisData:getLightSleepDuration	()I
    //   331: invokevirtual 634	com/resmed/refresh/model/RST_SleepSessionInfo:setTotalLightSleep	(I)V
    //   334: aload_2
    //   335: aload_0
    //   336: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   339: invokevirtual 637	com/resmed/refresh/model/json/SynopsisData:signalQualityIsValid	()Z
    //   342: invokevirtual 638	com/resmed/refresh/model/RST_SleepSessionInfo:setSignalQualityIsValid	(Z)V
    //   345: aload_2
    //   346: aload_0
    //   347: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   350: invokevirtual 639	com/resmed/refresh/model/json/SynopsisData:getSignalQualityValue	()I
    //   353: invokevirtual 640	com/resmed/refresh/model/RST_SleepSessionInfo:setSignalQualityValue	(I)V
    //   356: aload_2
    //   357: aload_0
    //   358: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   361: invokevirtual 641	com/resmed/refresh/model/json/SynopsisData:getSignalQualityMean	()F
    //   364: invokevirtual 642	com/resmed/refresh/model/RST_SleepSessionInfo:setSignalQualityMean	(F)V
    //   367: aload_2
    //   368: aload_0
    //   369: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   372: invokevirtual 643	com/resmed/refresh/model/json/SynopsisData:getSignalQualityPercBin1	()F
    //   375: invokevirtual 644	com/resmed/refresh/model/RST_SleepSessionInfo:setSignalQualityPercBin1	(F)V
    //   378: aload_2
    //   379: aload_0
    //   380: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   383: invokevirtual 645	com/resmed/refresh/model/json/SynopsisData:getSignalQualityPercBin2	()F
    //   386: invokevirtual 646	com/resmed/refresh/model/RST_SleepSessionInfo:setSignalQualityPercBin2	(F)V
    //   389: aload_2
    //   390: aload_0
    //   391: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   394: invokevirtual 647	com/resmed/refresh/model/json/SynopsisData:getSignalQualityPercBin3	()F
    //   397: invokevirtual 648	com/resmed/refresh/model/RST_SleepSessionInfo:setSignalQualityPercBin3	(F)V
    //   400: aload_2
    //   401: aload_0
    //   402: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   405: invokevirtual 649	com/resmed/refresh/model/json/SynopsisData:getSignalQualityPercBin4	()F
    //   408: invokevirtual 650	com/resmed/refresh/model/RST_SleepSessionInfo:setSignalQualityPercBin4	(F)V
    //   411: aload_2
    //   412: aload_0
    //   413: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   416: invokevirtual 651	com/resmed/refresh/model/json/SynopsisData:getSignalQualityPercBin5	()F
    //   419: invokevirtual 652	com/resmed/refresh/model/RST_SleepSessionInfo:setSignalQualityPercBin5	(F)V
    //   422: aload_2
    //   423: aload_0
    //   424: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   427: invokevirtual 653	com/resmed/refresh/model/json/SynopsisData:getSignalQualityStd	()F
    //   430: invokevirtual 654	com/resmed/refresh/model/RST_SleepSessionInfo:setSignalQualityStd	(F)V
    //   433: aload_2
    //   434: aload_0
    //   435: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   438: invokevirtual 655	com/resmed/refresh/model/json/SynopsisData:getBinSleepScoreTst	()I
    //   441: invokevirtual 656	com/resmed/refresh/model/RST_SleepSessionInfo:setBinSleepScoreTst	(I)V
    //   444: aload_2
    //   445: aload_0
    //   446: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   449: invokevirtual 657	com/resmed/refresh/model/json/SynopsisData:getBinSleepScoreDeep	()I
    //   452: invokevirtual 658	com/resmed/refresh/model/RST_SleepSessionInfo:setBinSleepScoreDeep	(I)V
    //   455: aload_2
    //   456: aload_0
    //   457: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   460: invokevirtual 659	com/resmed/refresh/model/json/SynopsisData:getBinSleepScoreLight	()I
    //   463: invokevirtual 660	com/resmed/refresh/model/RST_SleepSessionInfo:setBinSleepScoreLight	(I)V
    //   466: aload_2
    //   467: aload_0
    //   468: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   471: invokevirtual 661	com/resmed/refresh/model/json/SynopsisData:getBinSleepScoreOnset	()I
    //   474: invokevirtual 662	com/resmed/refresh/model/RST_SleepSessionInfo:setBinSleepScoreOnset	(I)V
    //   477: aload_2
    //   478: aload_0
    //   479: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   482: invokevirtual 663	com/resmed/refresh/model/json/SynopsisData:getBinSleepScoreRem	()I
    //   485: invokevirtual 664	com/resmed/refresh/model/RST_SleepSessionInfo:setBinSleepScoreRem	(I)V
    //   488: aload_2
    //   489: aload_0
    //   490: invokevirtual 571	com/resmed/refresh/model/json/Record:getSynopsisData	()Lcom/resmed/refresh/model/json/SynopsisData;
    //   493: invokevirtual 665	com/resmed/refresh/model/json/SynopsisData:getBinSleepScoreWaso	()I
    //   496: invokevirtual 666	com/resmed/refresh/model/RST_SleepSessionInfo:setBinSleepScoreWaso	(I)V
    //   499: aload_2
    //   500: iconst_1
    //   501: invokevirtual 669	com/resmed/refresh/model/RST_SleepSessionInfo:setUploaded	(Z)V
    //   504: aload_2
    //   505: iconst_1
    //   506: invokevirtual 672	com/resmed/refresh/model/RST_SleepSessionInfo:setCompleted	(Z)V
    //   509: new 215	java/util/ArrayList
    //   512: astore_3
    //   513: aload_3
    //   514: invokespecial 216	java/util/ArrayList:<init>	()V
    //   517: iconst_0
    //   518: istore_1
    //   519: iload_1
    //   520: aload_0
    //   521: invokevirtual 675	com/resmed/refresh/model/json/Record:getHypnogram	()Ljava/util/List;
    //   524: invokeinterface 545 1 0
    //   529: if_icmplt +233 -> 762
    //   532: aload_2
    //   533: aload_3
    //   534: invokevirtual 678	com/resmed/refresh/model/RST_SleepSessionInfo:addSleepStateArray	(Ljava/util/List;)V
    //   537: iconst_0
    //   538: istore_1
    //   539: iload_1
    //   540: aload_0
    //   541: invokevirtual 681	com/resmed/refresh/model/json/Record:getSleepEvents	()Ljava/util/List;
    //   544: invokeinterface 545 1 0
    //   549: if_icmplt +266 -> 815
    //   552: aload_2
    //   553: invokevirtual 682	com/resmed/refresh/model/RST_SleepSessionInfo:update	()V
    //   556: new 319	java/lang/StringBuilder
    //   559: astore_0
    //   560: aload_0
    //   561: ldc_w 684
    //   564: invokespecial 323	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   567: ldc_w 686
    //   570: aload_0
    //   571: aload_2
    //   572: invokevirtual 22	com/resmed/refresh/model/RST_SleepSessionInfo:getId	()J
    //   575: invokevirtual 689	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   578: ldc_w 691
    //   581: invokevirtual 331	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   584: aload_2
    //   585: invokevirtual 371	com/resmed/refresh/model/RST_SleepSessionInfo:getStartTime	()Ljava/util/Date;
    //   588: invokevirtual 345	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   591: invokevirtual 348	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   594: invokestatic 514	com/resmed/refresh/utils/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   597: pop
    //   598: aload_2
    //   599: astore_0
    //   600: goto -573 -> 27
    //   603: new 439	com/resmed/refresh/model/RST_ValueItem
    //   606: astore 4
    //   608: aload 4
    //   610: invokespecial 692	com/resmed/refresh/model/RST_ValueItem:<init>	()V
    //   613: aload 4
    //   615: aload_0
    //   616: invokevirtual 542	com/resmed/refresh/model/json/Record:getLight	()Ljava/util/List;
    //   619: iload_1
    //   620: invokeinterface 696 2 0
    //   625: checkcast 453	java/lang/Float
    //   628: invokevirtual 699	java/lang/Float:floatValue	()F
    //   631: invokevirtual 701	com/resmed/refresh/model/RST_ValueItem:setValue	(F)V
    //   634: aload 4
    //   636: iload_1
    //   637: invokevirtual 704	com/resmed/refresh/model/RST_ValueItem:setOrdr	(I)V
    //   640: aload 5
    //   642: aload 4
    //   644: invokeinterface 451 2 0
    //   649: pop
    //   650: iinc 1 1
    //   653: goto -587 -> 66
    //   656: new 439	com/resmed/refresh/model/RST_ValueItem
    //   659: astore 4
    //   661: aload 4
    //   663: invokespecial 692	com/resmed/refresh/model/RST_ValueItem:<init>	()V
    //   666: aload 4
    //   668: aload_0
    //   669: invokevirtual 551	com/resmed/refresh/model/json/Record:getNoise	()Ljava/util/List;
    //   672: iload_1
    //   673: invokeinterface 696 2 0
    //   678: checkcast 453	java/lang/Float
    //   681: invokevirtual 699	java/lang/Float:floatValue	()F
    //   684: invokevirtual 701	com/resmed/refresh/model/RST_ValueItem:setValue	(F)V
    //   687: aload 4
    //   689: iload_1
    //   690: invokevirtual 704	com/resmed/refresh/model/RST_ValueItem:setOrdr	(I)V
    //   693: aload 5
    //   695: aload 4
    //   697: invokeinterface 451 2 0
    //   702: pop
    //   703: iinc 1 1
    //   706: goto -602 -> 104
    //   709: new 439	com/resmed/refresh/model/RST_ValueItem
    //   712: astore 4
    //   714: aload 4
    //   716: invokespecial 692	com/resmed/refresh/model/RST_ValueItem:<init>	()V
    //   719: aload 4
    //   721: aload_0
    //   722: invokevirtual 557	com/resmed/refresh/model/json/Record:getTemperature	()Ljava/util/List;
    //   725: iload_1
    //   726: invokeinterface 696 2 0
    //   731: checkcast 453	java/lang/Float
    //   734: invokevirtual 699	java/lang/Float:floatValue	()F
    //   737: invokevirtual 701	com/resmed/refresh/model/RST_ValueItem:setValue	(F)V
    //   740: aload 4
    //   742: iload_1
    //   743: invokevirtual 704	com/resmed/refresh/model/RST_ValueItem:setOrdr	(I)V
    //   746: aload 5
    //   748: aload 4
    //   750: invokeinterface 451 2 0
    //   755: pop
    //   756: iinc 1 1
    //   759: goto -624 -> 135
    //   762: new 439	com/resmed/refresh/model/RST_ValueItem
    //   765: astore 4
    //   767: aload 4
    //   769: invokespecial 692	com/resmed/refresh/model/RST_ValueItem:<init>	()V
    //   772: aload 4
    //   774: aload_0
    //   775: invokevirtual 675	com/resmed/refresh/model/json/Record:getHypnogram	()Ljava/util/List;
    //   778: iload_1
    //   779: invokeinterface 696 2 0
    //   784: checkcast 444	java/lang/Integer
    //   787: invokevirtual 707	java/lang/Integer:intValue	()I
    //   790: i2f
    //   791: invokevirtual 701	com/resmed/refresh/model/RST_ValueItem:setValue	(F)V
    //   794: aload 4
    //   796: iload_1
    //   797: invokevirtual 704	com/resmed/refresh/model/RST_ValueItem:setOrdr	(I)V
    //   800: aload_3
    //   801: aload 4
    //   803: invokeinterface 451 2 0
    //   808: pop
    //   809: iinc 1 1
    //   812: goto -293 -> 519
    //   815: new 468	com/resmed/refresh/model/RST_SleepEvent
    //   818: astore_3
    //   819: aload_3
    //   820: invokespecial 708	com/resmed/refresh/model/RST_SleepEvent:<init>	()V
    //   823: aload_3
    //   824: aload_0
    //   825: invokevirtual 681	com/resmed/refresh/model/json/Record:getSleepEvents	()Ljava/util/List;
    //   828: iload_1
    //   829: invokeinterface 696 2 0
    //   834: checkcast 470	com/resmed/refresh/model/json/SleepEvent
    //   837: invokevirtual 498	com/resmed/refresh/model/json/SleepEvent:getSampleNumber	()Ljava/lang/Integer;
    //   840: invokevirtual 707	java/lang/Integer:intValue	()I
    //   843: invokevirtual 711	com/resmed/refresh/model/RST_SleepEvent:setEpochNumber	(I)V
    //   846: aload_3
    //   847: aload_0
    //   848: invokevirtual 681	com/resmed/refresh/model/json/Record:getSleepEvents	()Ljava/util/List;
    //   851: iload_1
    //   852: invokeinterface 696 2 0
    //   857: checkcast 470	com/resmed/refresh/model/json/SleepEvent
    //   860: invokevirtual 508	com/resmed/refresh/model/json/SleepEvent:getValue	()Ljava/lang/Integer;
    //   863: invokevirtual 707	java/lang/Integer:intValue	()I
    //   866: invokevirtual 712	com/resmed/refresh/model/RST_SleepEvent:setValue	(I)V
    //   869: aload_3
    //   870: aload_0
    //   871: invokevirtual 681	com/resmed/refresh/model/json/Record:getSleepEvents	()Ljava/util/List;
    //   874: iload_1
    //   875: invokeinterface 696 2 0
    //   880: checkcast 470	com/resmed/refresh/model/json/SleepEvent
    //   883: invokevirtual 501	com/resmed/refresh/model/json/SleepEvent:getType	()I
    //   886: invokevirtual 713	com/resmed/refresh/model/RST_SleepEvent:setType	(I)V
    //   889: aload_3
    //   890: aload_2
    //   891: invokevirtual 717	com/resmed/refresh/model/RST_SleepEvent:setRST_SleepSessionInfo	(Lcom/resmed/refresh/model/RST_SleepSessionInfo;)V
    //   894: aload_2
    //   895: aload_3
    //   896: invokevirtual 721	com/resmed/refresh/model/RST_SleepSessionInfo:addEvent	(Lcom/resmed/refresh/model/RST_SleepEvent;)V
    //   899: iinc 1 1
    //   902: goto -363 -> 539
    //   905: astore_0
    //   906: ldc 2
    //   908: monitorexit
    //   909: aload_0
    //   910: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	911	0	paramRecord	Record
    //   65	835	1	i	int
    //   20	875	2	localRST_SleepSessionInfo	RST_SleepSessionInfo
    //   6	890	3	localObject	Object
    //   606	196	4	localRST_ValueItem	RST_ValueItem
    //   57	690	5	localArrayList	ArrayList
    // Exception table:
    //   from	to	target	type
    //   3	21	905	finally
    //   32	64	905	finally
    //   66	102	905	finally
    //   104	117	905	finally
    //   117	133	905	finally
    //   135	517	905	finally
    //   519	537	905	finally
    //   539	598	905	finally
    //   603	650	905	finally
    //   656	703	905	finally
    //   709	756	905	finally
    //   762	809	905	finally
    //   815	899	905	finally
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\mappers\SleepSessionMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
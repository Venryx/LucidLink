package com.resmed.refresh.net;

import com.google.gson.Gson;
import com.resmed.refresh.model.json.Feedback;
import com.resmed.refresh.model.json.Last30DaysReportResponse;
import com.resmed.refresh.model.json.LaunchData;
import com.resmed.refresh.model.json.Record;
import com.resmed.refresh.model.json.User;
import com.resmed.refresh.model.json.UserProfile;
import com.resmed.refresh.net.http.HttpCallback;
import com.resmed.refresh.net.http.HttpConnector;
import com.resmed.refresh.utils.Log;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.message.BasicNameValuePair;

public class ResMedConnectorAPI
  implements ResMedServerAPI
{
  public static final int ERROR_CODE_APP_DEPRECATED = 602;
  public static final int ERROR_CODE_NOT_VALIDATED = 601;
  public static final int ERROR_CODE_UNAUTHORISED = 401;
  public static final int IGNORE_CALLBACK_CODE = 757;
  private static final String RESMED_EDF_ENDPOINTS = com.resmed.refresh.ui.utils.Consts.REFRESH_URL[5];
  private static final String RESMED_USER_API_BASE = com.resmed.refresh.ui.utils.Consts.REFRESH_URL[0];
  private HttpConnector httpConnector;
  
  public ResMedConnectorAPI(HttpConnector paramHttpConnector)
  {
    this.httpConnector = paramHttpConnector;
  }
  
  public void advice(long paramLong, HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(RESMED_USER_API_BASE + "advices/" + paramLong, paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void advices(HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(RESMED_USER_API_BASE + "advices", paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void advices(String paramString, HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(RESMED_USER_API_BASE + "advices" + paramString, paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void feedback(List<Feedback> paramList, HttpCallback paramHttpCallback)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    localLinkedHashMap.put("Data", paramList);
    paramList = new Gson().toJson(localLinkedHashMap);
    this.httpConnector.put(RESMED_USER_API_BASE + "feedback", paramHttpCallback, paramList);
  }
  
  public void forgotPassword(String paramString, HttpCallback paramHttpCallback)
  {
    this.httpConnector.post(RESMED_USER_API_BASE + "email/changeforgottenpassword/" + paramString, paramHttpCallback, paramString, new String[0]);
  }
  
  public String getSyncAdvicesUrl(long paramLong)
  {
    return RESMED_USER_API_BASE + "sync/advices/" + paramLong;
  }
  
  public String getSyncAllUrl(long paramLong)
  {
    return RESMED_USER_API_BASE + "sync/all/" + paramLong;
  }
  
  public String getSyncRecordsUrl(long paramLong)
  {
    return RESMED_USER_API_BASE + "sync/records/" + paramLong;
  }
  
  public String getSyncUserUrl()
  {
    return RESMED_USER_API_BASE + "sync/userprofile";
  }
  
  public void last30DaysReport(Last30DaysReportResponse paramLast30DaysReportResponse, HttpCallback paramHttpCallback)
  {
    Log.d("com.resmed.refresh.net", "ResMedConnector::last 30 days report : from" + paramLast30DaysReportResponse.getUtcFromDateTime() + " to : " + paramLast30DaysReportResponse.getUtcToDateTime() + " isemailreq : " + paramLast30DaysReportResponse.isEmailCopyIsRequested());
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    localLinkedHashMap.put("Data", paramLast30DaysReportResponse);
    paramLast30DaysReportResponse = new Gson().toJson(localLinkedHashMap);
    Log.d("com.resmed.refresh.net", "ResMedConnector::last 30 days report Json=>  :" + paramLast30DaysReportResponse);
    this.httpConnector.post(RESMED_USER_API_BASE + "/sleepreports", paramHttpCallback, paramLast30DaysReportResponse, null);
  }
  
  public void localeMUnitMapping(HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(RESMED_USER_API_BASE + "LocaleMUnit", paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void login(String paramString1, String paramString2, HttpCallback paramHttpCallback)
  {
    Log.d("com.resmed.refresh.net", "ResMedConnector::login username : " + paramString1 + " password : " + paramString2);
    this.httpConnector.post(RESMED_USER_API_BASE + "login", paramHttpCallback, null, new String[] { paramString1, paramString2 });
  }
  
  public void newUser(HttpCallback paramHttpCallback) {}
  
  public void rawData(long paramLong, File paramFile, HttpCallback paramHttpCallback)
  {
    this.httpConnector.put(RESMED_EDF_ENDPOINTS + paramLong, paramHttpCallback, paramFile);
  }
  
  public void registerUser(User paramUser, HttpCallback paramHttpCallback)
  {
    Log.d("com.resmed.refresh.net", "ResMedConnector::register username : " + paramUser.getUsername() + " password : " + paramUser.getPassword());
    this.httpConnector.post(RESMED_USER_API_BASE + "register", paramHttpCallback, null, new String[] { paramUser.getUsername(), paramUser.getPassword(), null, paramUser.getFirstName(), paramUser.getLastName() });
  }
  
  public void requestActionLog(ActionType paramActionType, String paramString, HttpCallback paramHttpCallback)
  {
    LinkedHashMap localLinkedHashMap2 = new LinkedHashMap();
    LinkedHashMap localLinkedHashMap1 = new LinkedHashMap();
    localLinkedHashMap1.put("ActionType", paramActionType.toString());
    localLinkedHashMap1.put("Description", paramString);
    localLinkedHashMap2.put("Data", localLinkedHashMap1);
    paramActionType = new Gson().toJson(localLinkedHashMap2);
    this.httpConnector.post(RESMED_USER_API_BASE + "actionlog", paramHttpCallback, paramActionType, new String[0]);
  }
  
  public void requestDevice(long paramLong, HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(RESMED_USER_API_BASE + "device/" + paramLong, paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void requestDevicePushToken(String paramString1, String paramString2, HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(RESMED_USER_API_BASE + "device/" + paramString1 + "/push/" + paramString2, paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void requestFeedback(long paramLong, HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(RESMED_USER_API_BASE + "feedback/" + paramLong, paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void requestPreSleepData(HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(RESMED_USER_API_BASE + "referencedata/presleep", paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void requestPreSleepQuestions(HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(RESMED_USER_API_BASE + "metadata/presleep", paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void requestProfile(HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(RESMED_USER_API_BASE + "userprofile", paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void requestRecord(long paramLong, HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(RESMED_USER_API_BASE + "records/" + paramLong, paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void requestRecords(HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(RESMED_USER_API_BASE + "records", paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void requestRecords(String paramString, HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(RESMED_USER_API_BASE + "records" + paramString, paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void requestSyncAdvices(long paramLong, HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(getSyncAdvicesUrl(paramLong), paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void requestSyncAll(long paramLong, HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(getSyncAllUrl(paramLong), paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void requestSyncRecords(long paramLong, HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(getSyncRecordsUrl(paramLong), paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void requestSyncUser(HttpCallback paramHttpCallback)
  {
    this.httpConnector.get(getSyncUserUrl(), paramHttpCallback, new BasicNameValuePair[0]);
  }
  
  public void sendEmailVerification(HttpCallback paramHttpCallback)
  {
    this.httpConnector.post(RESMED_USER_API_BASE + "email/emailaddressverification", paramHttpCallback, null, new String[0]);
  }
  
  public void updateDevicePushToken(String paramString1, String paramString2, HttpCallback paramHttpCallback)
  {
    this.httpConnector.put(RESMED_USER_API_BASE + "device/" + paramString1 + "/push/" + paramString2, paramHttpCallback, "");
  }
  
  public void updateFeedback(long paramLong, Feedback paramFeedback, HttpCallback paramHttpCallback)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    localLinkedHashMap.put("Data", paramFeedback);
    paramFeedback = new Gson().toJson(localLinkedHashMap);
    this.httpConnector.put(RESMED_USER_API_BASE + "feedback/" + paramLong, paramHttpCallback, paramFeedback);
  }
  
  public void updateProfile(UserProfile paramUserProfile, HttpCallback paramHttpCallback)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    localLinkedHashMap.put("Data", paramUserProfile);
    paramUserProfile = new Gson().toJson(localLinkedHashMap);
    Log.d("com.resmed.refresh.net", " http json for updateProfile : " + paramUserProfile);
    this.httpConnector.put(RESMED_USER_API_BASE + "userprofile", paramHttpCallback, paramUserProfile);
  }
  
  public void updateRecord(Record paramRecord, HttpCallback paramHttpCallback)
  {
    Object localObject = new LinkedHashMap();
    ((Map)localObject).put("Data", paramRecord);
    localObject = new Gson().toJson(localObject);
    Log.d("com.resmed.refresh.net", " http json for updateRecord : " + (String)localObject);
    this.httpConnector.put(RESMED_USER_API_BASE + "records/" + paramRecord.getRecordId(), paramHttpCallback, (String)localObject);
  }
  
  public void updateUser(User paramUser, HttpCallback paramHttpCallback)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    localLinkedHashMap.put("Data", paramUser);
    paramUser = new Gson().toJson(localLinkedHashMap);
    Log.d("com.resmed.refresh.net", " http json for register : " + paramUser);
    this.httpConnector.put(RESMED_USER_API_BASE + "user", paramHttpCallback, paramUser);
  }
  
  public void userAppLaunch(String paramString, LaunchData paramLaunchData, HttpCallback paramHttpCallback)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    localLinkedHashMap.put("Data", paramLaunchData);
    paramLaunchData = new Gson().toJson(localLinkedHashMap);
    Log.d("com.resmed.refresh.net", " http json for userAppLaunch : " + paramLaunchData);
    this.httpConnector.put(RESMED_USER_API_BASE + "launch/" + paramString, paramHttpCallback, paramLaunchData);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\net\ResMedConnectorAPI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.net;

import com.resmed.refresh.model.json.Feedback;
import com.resmed.refresh.model.json.Last30DaysReportResponse;
import com.resmed.refresh.model.json.LaunchData;
import com.resmed.refresh.model.json.Record;
import com.resmed.refresh.model.json.User;
import com.resmed.refresh.model.json.UserProfile;
import com.resmed.refresh.net.http.HttpCallback;
import java.io.File;
import java.util.List;

public abstract interface ResMedServerAPI
{
  public abstract void advice(long paramLong, HttpCallback paramHttpCallback);
  
  public abstract void advices(HttpCallback paramHttpCallback);
  
  public abstract void advices(String paramString, HttpCallback paramHttpCallback);
  
  public abstract void feedback(List<Feedback> paramList, HttpCallback paramHttpCallback);
  
  public abstract void forgotPassword(String paramString, HttpCallback paramHttpCallback);
  
  public abstract String getSyncAdvicesUrl(long paramLong);
  
  public abstract String getSyncAllUrl(long paramLong);
  
  public abstract String getSyncRecordsUrl(long paramLong);
  
  public abstract String getSyncUserUrl();
  
  public abstract void last30DaysReport(Last30DaysReportResponse paramLast30DaysReportResponse, HttpCallback paramHttpCallback);
  
  public abstract void localeMUnitMapping(HttpCallback paramHttpCallback);
  
  public abstract void login(String paramString1, String paramString2, HttpCallback paramHttpCallback);
  
  public abstract void newUser(HttpCallback paramHttpCallback);
  
  public abstract void rawData(long paramLong, File paramFile, HttpCallback paramHttpCallback);
  
  public abstract void registerUser(User paramUser, HttpCallback paramHttpCallback);
  
  public abstract void requestActionLog(ActionType paramActionType, String paramString, HttpCallback paramHttpCallback);
  
  public abstract void requestDevice(long paramLong, HttpCallback paramHttpCallback);
  
  public abstract void requestDevicePushToken(String paramString1, String paramString2, HttpCallback paramHttpCallback);
  
  public abstract void requestFeedback(long paramLong, HttpCallback paramHttpCallback);
  
  public abstract void requestPreSleepData(HttpCallback paramHttpCallback);
  
  public abstract void requestPreSleepQuestions(HttpCallback paramHttpCallback);
  
  public abstract void requestProfile(HttpCallback paramHttpCallback);
  
  public abstract void requestRecord(long paramLong, HttpCallback paramHttpCallback);
  
  public abstract void requestRecords(HttpCallback paramHttpCallback);
  
  public abstract void requestRecords(String paramString, HttpCallback paramHttpCallback);
  
  public abstract void requestSyncAdvices(long paramLong, HttpCallback paramHttpCallback);
  
  public abstract void requestSyncAll(long paramLong, HttpCallback paramHttpCallback);
  
  public abstract void requestSyncRecords(long paramLong, HttpCallback paramHttpCallback);
  
  public abstract void requestSyncUser(HttpCallback paramHttpCallback);
  
  public abstract void sendEmailVerification(HttpCallback paramHttpCallback);
  
  public abstract void updateDevicePushToken(String paramString1, String paramString2, HttpCallback paramHttpCallback);
  
  public abstract void updateFeedback(long paramLong, Feedback paramFeedback, HttpCallback paramHttpCallback);
  
  public abstract void updateProfile(UserProfile paramUserProfile, HttpCallback paramHttpCallback);
  
  public abstract void updateRecord(Record paramRecord, HttpCallback paramHttpCallback);
  
  public abstract void updateUser(User paramUser, HttpCallback paramHttpCallback);
  
  public abstract void userAppLaunch(String paramString, LaunchData paramLaunchData, HttpCallback paramHttpCallback);
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\net\ResMedServerAPI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
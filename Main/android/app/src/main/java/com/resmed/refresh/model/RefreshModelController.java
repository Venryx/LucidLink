package com.resmed.refresh.model;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Base64;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;
import com.resmed.refresh.model.json.Last30DaysReportResponse;
import com.resmed.refresh.model.json.LaunchData;
import com.resmed.refresh.model.json.LocaleMUnitResponse;
import com.resmed.refresh.model.json.User;
import com.resmed.refresh.model.json.UserResponse;
import com.resmed.refresh.model.mappers.SleepSessionMapper;
import com.resmed.refresh.model.mappers.UserMapper;
import com.resmed.refresh.model.mappers.UserProfileMapper;
import com.resmed.refresh.model.mindclear.MindClearManager;
import com.resmed.refresh.model.services.RST_ServiceAccountSync;
import com.resmed.refresh.model.services.RST_ServiceAdvice;
import com.resmed.refresh.model.services.RST_ServiceEmailVerification;
import com.resmed.refresh.model.services.RST_ServiceFileUploader;
import com.resmed.refresh.model.services.RST_ServiceLast30DaysReport;
import com.resmed.refresh.model.services.RST_ServiceLaunch;
import com.resmed.refresh.model.services.RST_ServiceLocaleMUnit;
import com.resmed.refresh.model.services.RST_ServiceLogin;
import com.resmed.refresh.model.services.RST_ServicePushNotifications;
import com.resmed.refresh.model.services.RST_ServiceReferenceData;
import com.resmed.refresh.model.services.RST_ServiceRegister;
import com.resmed.refresh.model.services.RST_ServiceSleepRecord;
import com.resmed.refresh.model.services.RST_ServiceUser;
import com.resmed.refresh.model.services.RST_ServiceUserProfile;
import com.resmed.refresh.net.ResMedConnectorAPI;
import com.resmed.refresh.net.ResMedServerAPI;
import com.resmed.refresh.net.http.HttpDefaultConnector;
import com.resmed.refresh.net.push.RegisterGMCThread;
import com.resmed.refresh.net.security.Sha;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.ui.utils.UserProfileDataManager;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.refresh.utils.RefreshUserPreferencesData;
import com.resmed.refresh.utils.preSleepLog;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class RefreshModelController
{
  private static final String ACTIVE_ALARM = "com.resmed.refresh.model.active_alarm";
  private static final String ACTIVE_RELAX = "com.resmed.refresh.model.active_relax";
  private static final String ALARM_IS_PLAYING = "com.resmed.refresh.model.alarm_is_playing";
  private static final String BED_BOARD_VERSION_TAG = "com.resmed.refresh.model.bed_board_version_tag";
  private static final String BED_FIRMWARE_VERSION_TAG = "com.resmed.refresh.model.bed_firmware_version_tag";
  private static final String BED_SERIAL = "com.resmed.refresh.model.bedserial";
  private static final String CLEAR_MIND = "com.resmed.refresh.model.clear_mind";
  private static final String DEFAULT_BRIGHTNESS_VALUE_TAG = "com.resmed.refresh.model.default_brigthness_value_tag";
  private static final String DEVICE_ID_TAG = "com.resmed.refresh.model.device_id_tag";
  private static final String EXPANSION_VALIDATED_TAG = "com.resmed.refresh.model.expansion_validated_tag";
  private static final long EXPIRATION_TIMESTAMP = 28800000L;
  private static final String GMC_APPVERSION_TAG = "com.resmed.refresh.model.gmc_appversion_tag";
  private static final String GMC_TOKEN_STATUS_TAG = "com.resmed.refresh.model.gmc_token_status_tag";
  private static final String GMC_TOKEN_TAG = "com.resmed.refresh.model.gmc_token_tag";
  private static final String HAS_TO_VALIDATE_EMAIL = "com.resmed.refresh.model.has_to_validate_email";
  private static final String LAST_EMAIL_LOGGED_IN_TAG = "com.resmed.refresh.model.last_email_logged_in_tag";
  private static final String LAUNCHED_BEFORE_TAG = "com.resmed.refresh.model.first_launch";
  private static final String NIGTH_QUESTIONS_TIMESTAMP_TAG = "com.resmed.refresh.model.night_questions_timestamp_tag";
  private static final String PLAY_AUTO_RELAX = "com.resmed.refresh.model.play_auto_relax";
  private static final String PROFILE_UPDATE_PENDING_TAG = "com.resmed.refresh.model.profile_update_pending_tag";
  private static final String REPEAT_ALARM = "com.resmed.refresh.model.repeat_alarm";
  private static final String RM20_LIBRARY_VERSION_TAG = "com.resmed.refresh.model.rm20_library_version_tag";
  private static final String RST_KEY_TAG = "com.resmed.refresh.model.rst_key_tag";
  private static final String RST_TOKEN_TAG = "com.resmed.refresh.model.session_token_tag";
  private static final String SERVICE_DID_LAUNCH_TAG = "com.resmed.refresh.model.service_did_launch_tag";
  private static final String SMART_ALARM_RM20_TOKEN = "com.resmed.refresh.model.smart_alarm_rm20_token";
  private static final String SMART_ALARM_VALID_TOKEN = "com.resmed.refresh.model.smart_alarm_valid_token";
  private static final String SOUND_ALARM = "com.resmed.refresh.model.sound_alarm";
  private static final String SOUND_RELAX = "com.resmed.refresh.model.sound_relax";
  private static final String TIME_ALARM_DATE = "com.resmed.refresh.model.time_alarm_date";
  private static final String TIME_ALARM_REBOOT = "com.resmed.refresh.model.time_alarm_reboot";
  private static final String USER_HAS_BEEN_LOGOUT_TAG = "com.resmed.refresh.model.user_has_been_logout_tag";
  private static final String USER_ID_TAG = "com.resmed.refresh.model.user_id_tag";
  private static final String WINDOW_ALARM = "com.resmed.refresh.model.window_alarm";
  private static RefreshModelController controller;
  private DaoSession daoSession;
  private RST_LocationManager locationManager;
  private ResMedServerAPI resMedServerAPI;
  private RST_User user;
  private RefreshUserPreferencesData userPreferencesData;
  
  private boolean checkPlayServices(Activity paramActivity)
  {
    int i = GooglePlayServicesUtil.isGooglePlayServicesAvailable(RefreshApplication.getInstance().getApplicationContext());
    Log.d("com.resmed.refresh.push", "GooglePlayServices resultCode = " + i);
    if (i != 0) {
      if (GooglePlayServicesUtil.isUserRecoverableError(i)) {
        GooglePlayServicesUtil.getErrorDialog(i, paramActivity, 9000).show();
      }
    }
    for (boolean bool = false;; bool = true)
    {
      return bool;
      Log.d("com.resmed.refresh.push", "GooglePlayServices This device is not supported.");
      break;
    }
  }
  
  private void clearRSTKey()
  {
    this.userPreferencesData.removeStringConfigValue("com.resmed.refresh.model.rst_key_tag");
  }
  
  private void clearSessionToken()
  {
    this.userPreferencesData.removeStringConfigValue("com.resmed.refresh.model.session_token_tag");
  }
  
  private void clearUserId()
  {
    this.userPreferencesData.removeStringConfigValue("com.resmed.refresh.model.user_id_tag");
    Log.e("userID", "clearUserId:" + this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.user_id_tag"));
  }
  
  private RST_User createUserItem(String paramString)
  {
    boolean bool = true;
    paramString = new RST_User(paramString);
    paramString.setFirstName("");
    paramString.setFamilyName("");
    paramString.setEmail("");
    paramString.setPassword("");
    this.daoSession.getRST_UserDao().insert(paramString);
    paramString.setAndSaveProfile(createUserProfileItem());
    Object localObject = createSettingsItem();
    ((RST_Settings)localObject).setPushNotifications(true);
    ((RST_Settings)localObject).setLocationPermission(true);
    if (RefreshApplication.userMeasurementUnitMappingObj.getTemperatureUnit() == 1) {}
    for (;;)
    {
      ((RST_Settings)localObject).setUseMetricUnits(bool);
      paramString.setAndSaveSettings((RST_Settings)localObject);
      localObject = this.locationManager.getLastLocation();
      paramString.setAndSaveLocation(createLocationItem((float)((LatLng)localObject).latitude, (float)((LatLng)localObject).longitude));
      paramString.update();
      return paramString;
      bool = false;
    }
  }
  
  private String generateDeviceID()
  {
    Object localObject = null;
    if (0 == 0)
    {
      localObject = (TelephonyManager)RefreshApplication.getInstance().getSystemService("phone");
      ((TelephonyManager)localObject).getDeviceId();
      localObject = ((TelephonyManager)localObject).getDeviceId();
      localObject = new UUID(Settings.Secure.getString(RefreshApplication.getInstance().getContentResolver(), "android_id").hashCode(), ((String)localObject).hashCode() << 32).toString().toUpperCase();
    }
    return (String)localObject;
  }
  
  private String generateToken(String paramString1, String paramString2)
  {
    String str = "";
    try
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>(String.valueOf(paramString2));
      paramString1 = Sha.hash256(paramString1).toUpperCase();
      return paramString1;
    }
    catch (NoSuchAlgorithmException paramString1)
    {
      for (;;)
      {
        paramString1.printStackTrace();
        paramString1 = str;
      }
    }
  }
  
  private int getGMCAppVersion()
  {
    return this.userPreferencesData.getIntegerConfigValue("com.resmed.refresh.model.gmc_appversion_tag");
  }
  
  private String getGMCToken()
  {
    return this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.gmc_token_tag");
  }
  
  public static RefreshModelController getInstance()
  {
    try
    {
      if (controller == null)
      {
        localRefreshModelController = new com/resmed/refresh/model/RefreshModelController;
        localRefreshModelController.<init>();
        controller = localRefreshModelController;
        controller.setup();
      }
      RefreshModelController localRefreshModelController = controller;
      return localRefreshModelController;
    }
    finally {}
  }
  
  private String getRSTKey()
  {
    return this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.rst_key_tag");
  }
  
  private String getRSTToken()
  {
    return this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.session_token_tag");
  }
  
  private String getRegistrationId(Context paramContext)
  {
    if (getGMCToken() == null)
    {
      setGMCToken("");
      setGMCAppVersion(0);
    }
    if (getGMCAppVersion() == RefreshApplication.getInstance().getAppVersion()) {}
    for (paramContext = getGMCToken();; paramContext = "") {
      return paramContext;
    }
  }
  
  private String getUserId()
  {
    Log.e("userID", "getUserId:" + this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.user_id_tag"));
    return this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.user_id_tag");
  }
  
  public static boolean isPluggedIn()
  {
    boolean bool2 = true;
    int i = RefreshApplication.getInstance().getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("plugged", -1);
    boolean bool1 = bool2;
    if (i != 1)
    {
      bool1 = bool2;
      if (i != 2) {
        bool1 = false;
      }
    }
    return bool1;
  }
  
  private List<RST_SleepSessionInfo> localSleepSessionsAccesor(int paramInt, boolean paramBoolean1, boolean paramBoolean2, Date paramDate1, Date paramDate2, long paramLong, boolean paramBoolean3)
  {
    if (this.user == null)
    {
      paramDate1 = null;
      return paramDate1;
    }
    RST_SleepSessionInfoDao localRST_SleepSessionInfoDao = this.daoSession.getRST_SleepSessionInfoDao();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(RST_SleepSessionInfoDao.Properties.IdUser.eq(this.user.getId()));
    localArrayList.add(RST_SleepSessionInfoDao.Properties.Id.lt(Long.valueOf(1000000000L)));
    if (paramDate1 != null) {
      localArrayList.add(RST_SleepSessionInfoDao.Properties.StartTime.gt(paramDate1));
    }
    if (paramDate2 != null) {
      localArrayList.add(RST_SleepSessionInfoDao.Properties.StartTime.lt(paramDate2));
    }
    if (paramLong > -1L) {
      localArrayList.add(RST_SleepSessionInfoDao.Properties.Id.eq(Long.valueOf(paramLong)));
    }
    if (paramBoolean3) {
      localArrayList.add(RST_SleepSessionInfoDao.Properties.Completed.eq(Integer.valueOf(1)));
    }
    paramDate1 = localRST_SleepSessionInfoDao.queryBuilder();
    if (localArrayList.size() == 1)
    {
      paramDate1.where((WhereCondition)localArrayList.get(0), new WhereCondition[0]);
      label204:
      if (paramBoolean1)
      {
        if (paramBoolean2) {
          break label300;
        }
        paramDate1.orderAsc(new Property[] { RST_SleepSessionInfoDao.Properties.Id });
      }
    }
    for (;;)
    {
      if (paramInt > 0) {
        paramDate1.limit(paramInt);
      }
      paramDate1 = paramDate1.list();
      break;
      if (localArrayList.size() <= 1) {
        break label204;
      }
      paramDate1.where((WhereCondition)localArrayList.remove(0), (WhereCondition[])localArrayList.toArray(new WhereCondition[localArrayList.size()]));
      break label204;
      label300:
      paramDate1.orderDesc(new Property[] { RST_SleepSessionInfoDao.Properties.Id });
    }
  }
  
  private RST_User localUserData()
  {
    String str = getUserId();
    if (str != null)
    {
      setDeviceId();
      this.user = localUserDataForId(str);
    }
    return this.user;
  }
  
  private void serviceForgotPassword(String paramString, RST_CallbackItem<RST_Response<RST_User>> paramRST_CallbackItem)
  {
    new RST_ServiceLogin(this.resMedServerAPI).userForgotPassword(paramString, paramRST_CallbackItem);
  }
  
  private void serviceLast30DaysReport(String paramString1, String paramString2, boolean paramBoolean, RST_CallbackItem<RST_Response<Last30DaysReportResponse>> paramRST_CallbackItem)
  {
    new RST_ServiceLast30DaysReport(this.resMedServerAPI).getLast30DaysReport(paramString1, paramString2, paramBoolean, paramRST_CallbackItem);
  }
  
  private void serviceLocaleMUnitMapping(RST_CallbackItem<RST_Response<List<LocaleMUnitResponse>>> paramRST_CallbackItem)
  {
    new RST_ServiceLocaleMUnit(this.resMedServerAPI).getlocaleMUnitMapping(paramRST_CallbackItem);
  }
  
  private void servicePreSleepQuestions(RST_CallbackItem<RST_Response<RST_NightQuestions>> paramRST_CallbackItem)
  {
    new RST_ServiceReferenceData(this.resMedServerAPI).getPreSleepQuestions(paramRST_CallbackItem);
  }
  
  private void serviceRegisterPushNotificationToken(final String paramString)
  {
    new Timer().schedule(new TimerTask()
    {
      public void run()
      {
        RefreshModelController.this.serviceRegisterPushNotificationTokenDelayed(paramString);
      }
    }, 3000L);
  }
  
  private void serviceRegisterPushNotificationTokenDelayed(String paramString)
  {
    new RST_ServicePushNotifications(this.resMedServerAPI).registerPushToken(getDeviceId(), paramString);
  }
  
  private void serviceUpdateFeedback(RST_AdviceItem paramRST_AdviceItem, RST_CallbackItem<RST_Response<Object>> paramRST_CallbackItem)
  {
    new RST_ServiceAdvice(this.resMedServerAPI).updateFeedback(paramRST_AdviceItem, paramRST_CallbackItem);
  }
  
  private void serviceUpdateUser(RST_CallbackItem<RST_Response<RST_User>> paramRST_CallbackItem)
  {
    new RST_ServiceUser(this.resMedServerAPI).updateUserData(UserMapper.getUser(this.user), paramRST_CallbackItem);
  }
  
  private void serviceUpdateUserProfile(RST_CallbackItem<RST_Response<RST_UserProfile>> paramRST_CallbackItem)
  {
    new RST_ServiceUserProfile(this.resMedServerAPI).updateUserProfile(UserMapper.getProfile(this.user), paramRST_CallbackItem);
  }
  
  private void serviceUserLogin(String paramString1, String paramString2, RST_CallbackItem<RST_Response<RST_User>> paramRST_CallbackItem)
  {
    new RST_ServiceLogin(this.resMedServerAPI).userLogin(paramString1, paramString2, paramRST_CallbackItem);
  }
  
  private void serviceUserRegister(User paramUser, RST_CallbackItem<RST_Response<RST_User>> paramRST_CallbackItem)
  {
    new RST_ServiceRegister(this.resMedServerAPI).userRegister(paramUser, paramRST_CallbackItem);
  }
  
  private void setDeviceId()
  {
    if (this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.device_id_tag") == null) {
      this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.device_id_tag", generateDeviceID());
    }
  }
  
  private void setGMCAppVersion(int paramInt)
  {
    this.userPreferencesData.setIntegerConfigValue("com.resmed.refresh.model.gmc_appversion_tag", Integer.valueOf(paramInt));
  }
  
  private void setGMCToken(String paramString)
  {
    this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.gmc_token_tag", paramString);
  }
  
  private void setLocationPermission(boolean paramBoolean)
  {
    this.user.getSettings().setLocationPermission(paramBoolean);
    save();
  }
  
  private void setRSTKey(String paramString)
  {
    this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.rst_key_tag", paramString);
  }
  
  private void setRSTToken(String paramString)
  {
    this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.session_token_tag", paramString);
  }
  
  private void storeUserId(String paramString)
  {
    Log.e("userID", "storeUserId:" + paramString);
    this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.user_id_tag", paramString);
  }
  
  public RST_User access(String paramString1, String paramString2, UserResponse paramUserResponse)
  {
    return access(paramUserResponse.getFirstName(), paramUserResponse.getLastName(), paramString1, paramString2, paramUserResponse);
  }
  
  public RST_User access(String paramString1, String paramString2, String paramString3, String paramString4, UserResponse paramUserResponse)
  {
    RST_User localRST_User2 = localUserDataForId(paramUserResponse.getUserId());
    RST_User localRST_User1 = localRST_User2;
    if (localRST_User2 == null) {
      localRST_User1 = createUserItem(paramUserResponse.getUserId());
    }
    localRST_User1.setEmail(paramString3);
    setLastEmail(paramString3);
    if ((paramString1 != null) && (paramString2 != null))
    {
      localRST_User1.setFirstName(paramString1);
      localRST_User1.setFamilyName(paramString2);
    }
    this.user = localRST_User1;
    paramString1 = paramUserResponse.getUserProfile();
    if (paramString1 != null) {
      UserProfileMapper.processUserProfile(paramString1);
    }
    paramString1 = generateToken(paramUserResponse.getUserId(), paramString4);
    storeUserId(paramUserResponse.getUserId());
    setRSTKey(paramUserResponse.getRstKey());
    setRSTToken(paramString1);
    save();
    return this.user;
  }
  
  public void adviceForDay(final Date paramDate, final RST_CallbackItem<RST_Response<List<RST_AdviceItem>>> paramRST_CallbackItem, boolean paramBoolean)
  {
    List localList = RefreshTools.dateRangesForDay(paramDate);
    if (paramBoolean) {
      serviceGetLatestAdvice((Date)localList.get(0), (Date)localList.get(1), paramRST_CallbackItem);
    }
    for (;;)
    {
      return;
      paramDate = localAdvicesForDate(paramDate);
      if ((paramBoolean) || (paramDate == null) || (paramDate.size() == 0)) {
        serviceGetLatestAdvice((Date)localList.get(0), (Date)localList.get(1), paramRST_CallbackItem);
      } else {
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
          public void run()
          {
            RST_Response localRST_Response = new RST_Response();
            localRST_Response.setStatus(true);
            localRST_Response.setResponse(paramDate);
            paramRST_CallbackItem.onResult(localRST_Response);
          }
        });
      }
    }
  }
  
  public void adviceForId(long paramLong, final RST_CallbackItem<RST_Response<RST_AdviceItem>> paramRST_CallbackItem)
  {
    final RST_AdviceItem localRST_AdviceItem = localAdviceForId(paramLong);
    if (localRST_AdviceItem == null) {
      serviceGetAdviceForId(paramLong, paramRST_CallbackItem);
    }
    for (;;)
    {
      return;
      new Handler(Looper.getMainLooper()).post(new Runnable()
      {
        public void run()
        {
          RST_Response localRST_Response = new RST_Response();
          localRST_Response.setStatus(true);
          localRST_Response.setResponse(localRST_AdviceItem);
          paramRST_CallbackItem.onResult(localRST_Response);
        }
      });
    }
  }
  
  public void adviceForSessionId(long paramLong, final RST_CallbackItem<RST_Response<List<RST_AdviceItem>>> paramRST_CallbackItem)
  {
    new Handler(Looper.getMainLooper()).post(new Runnable()
    {
      public void run()
      {
        RST_Response localRST_Response = new RST_Response();
        List localList = RefreshModelController.this.localAdvices();
        localRST_Response.setStatus(true);
        localRST_Response.setResponse(localList);
        paramRST_CallbackItem.onResult(localRST_Response);
      }
    });
  }
  
  public void changeLocationUpdates(boolean paramBoolean)
  {
    setLocationPermission(paramBoolean);
    this.locationManager.setLocationUpdatesListener(paramBoolean);
  }
  
  public void clearAdviceHistory()
  {
    Iterator localIterator = this.user.getAdvices().iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        this.user.resetAdvices();
        return;
      }
      ((RST_AdviceItem)localIterator.next()).delete();
    }
  }
  
  public void clearDB()
  {
    this.daoSession.clear();
    this.user = getUser();
  }
  
  public void clearHistory()
  {
    clearSleepHistory();
    clearAdviceHistory();
  }
  
  public void clearSleepHistory()
  {
    Object localObject = this.user.getSleepSessions();
    Log.d("com.resmed.refresh.model", ((List)localObject).size() + " sleep records to delete");
    localObject = ((List)localObject).iterator();
    for (;;)
    {
      if (!((Iterator)localObject).hasNext())
      {
        this.user.resetSleepSessions();
        return;
      }
      RST_SleepSessionInfo localRST_SleepSessionInfo = (RST_SleepSessionInfo)((Iterator)localObject).next();
      String str = "Sleep record " + localRST_SleepSessionInfo.getId();
      localRST_SleepSessionInfo.delete();
      Log.d("com.resmed.refresh.model", str + " deleted");
    }
  }
  
  public RST_AdviceItem createAdviceItem(long paramLong)
  {
    RST_AdviceItem localRST_AdviceItem = new RST_AdviceItem(paramLong);
    localRST_AdviceItem.setTimestamp(new Date());
    localRST_AdviceItem.setContent("");
    localRST_AdviceItem.setDetail("");
    localRST_AdviceItem.setHeader("");
    localRST_AdviceItem.setHtmlContent("");
    localRST_AdviceItem.setIcon("");
    localRST_AdviceItem.setType("");
    localRST_AdviceItem.setSubtitle("");
    localRST_AdviceItem.setArticleUrl("");
    localRST_AdviceItem.setFeedback(-1);
    localRST_AdviceItem.setRead(false);
    this.user.addAdviceValue(localRST_AdviceItem);
    localRST_AdviceItem.setAndSaveHypnogramInfo(createSleepEventItem());
    localRST_AdviceItem.update();
    return localRST_AdviceItem;
  }
  
  public RST_EnvironmentalInfo createEnvironmentalInfo()
  {
    RST_EnvironmentalInfo localRST_EnvironmentalInfo = new RST_EnvironmentalInfo();
    localRST_EnvironmentalInfo.setup();
    this.daoSession.getRST_EnvironmentalInfoDao().insert(localRST_EnvironmentalInfo);
    return localRST_EnvironmentalInfo;
  }
  
  public RST_LocationItem createLocationItem(float paramFloat1, float paramFloat2)
  {
    RST_LocationItem localRST_LocationItem = new RST_LocationItem();
    localRST_LocationItem.setLatitude(paramFloat1);
    localRST_LocationItem.setLongitude(paramFloat2);
    int i = userTimezoneOffset();
    Log.d("com.resmed.refresh.sleepFragment", " LocationMapper:: RefreshModelController::createLocationItem, offset : " + i);
    localRST_LocationItem.setTimezone(userTimezoneOffset());
    localRST_LocationItem.setLocale(Locale.getDefault().getISO3Language());
    return localRST_LocationItem;
  }
  
  public RST_NightQuestions createNightQuestionsItem()
  {
    if (this.user.getNightQuestions() != null) {
      this.user.getNightQuestions().delete();
    }
    RST_NightQuestions localRST_NightQuestions = new RST_NightQuestions();
    this.daoSession.getRST_NightQuestionsDao().insert(localRST_NightQuestions);
    this.user.setNightQuestions(localRST_NightQuestions);
    return localRST_NightQuestions;
  }
  
  public RST_QuestionItem createQuestionItem(int paramInt)
  {
    RST_QuestionItem localRST_QuestionItem = new RST_QuestionItem();
    localRST_QuestionItem.setId(Long.valueOf(paramInt));
    localRST_QuestionItem.setQuestionId(paramInt);
    localRST_QuestionItem.setText("");
    this.user.getNightQuestions().addQuestionItem(localRST_QuestionItem);
    return localRST_QuestionItem;
  }
  
  public RST_Settings createSettingsItem()
  {
    RST_Settings localRST_Settings = new RST_Settings();
    localRST_Settings.setTac1(true);
    localRST_Settings.setTac2(true);
    localRST_Settings.setTac3(true);
    return localRST_Settings;
  }
  
  public RST_SleepEvent createSleepEventItem()
  {
    return new RST_SleepEvent();
  }
  
  public RST_SleepSessionInfo createSleepSessionInfo()
  {
    return createSleepSessionInfo((new Date().getTime() - 978307200000L) / 1000L);
  }
  
  public RST_SleepSessionInfo createSleepSessionInfo(long paramLong)
  {
    RST_SleepSessionInfo localRST_SleepSessionInfo = new RST_SleepSessionInfo(paramLong);
    localRST_SleepSessionInfo.setup();
    RST_EnvironmentalInfo localRST_EnvironmentalInfo = createEnvironmentalInfo();
    localRST_SleepSessionInfo.setEnvironmentalInfo(localRST_EnvironmentalInfo);
    localRST_EnvironmentalInfo.setIdSession(Long.valueOf(paramLong));
    localRST_SleepSessionInfo.addNightQuestions(localNightQuestions());
    localRST_SleepSessionInfo.setStartTime(new Date());
    localRST_SleepSessionInfo.setCompleted(false);
    localRST_SleepSessionInfo.setUploaded(false);
    this.user.addSleepSessionInfo(localRST_SleepSessionInfo);
    return localRST_SleepSessionInfo;
  }
  
  public RST_UserProfile createUserProfileItem()
  {
    RST_UserProfile localRST_UserProfile = new RST_UserProfile();
    localRST_UserProfile.setDateOfBirth(new Date(0L));
    return localRST_UserProfile;
  }
  
  public RST_ValueItem createValueItem(int paramInt1, int paramInt2)
  {
    RST_ValueItem localRST_ValueItem = new RST_ValueItem();
    localRST_ValueItem.setValue(paramInt1);
    localRST_ValueItem.setOrdr(paramInt2);
    return localRST_ValueItem;
  }
  
  public RST_User debugCreateUserItem()
  {
    this.user = createUserItem(UUID.randomUUID().toString());
    save();
    storeUserId(this.user.getId());
    return this.user;
  }
  
  public void debugResetAll()
  {
    if (this.user != null) {
      this.user.delete();
    }
    this.userPreferencesData.clearAll();
  }
  
  public Boolean getActiveAlarm()
  {
    try
    {
      boolean bool = this.userPreferencesData.getBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.active_alarm");
      localBoolean1 = Boolean.valueOf(bool);
    }
    catch (NullPointerException localNullPointerException)
    {
      for (;;)
      {
        Boolean localBoolean1;
        Boolean localBoolean2 = Boolean.valueOf(false);
      }
    }
    return localBoolean1;
  }
  
  public boolean getActiveRelax()
  {
    return this.userPreferencesData.getBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.active_relax");
  }
  
  public Long getAlarmDateTime()
  {
    return Long.valueOf(this.userPreferencesData.getLongUserValue(this.user.getId(), "com.resmed.refresh.model.time_alarm_date"));
  }
  
  public boolean getAlarmIsPlaying()
  {
    return this.userPreferencesData.getBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.alarm_is_playing");
  }
  
  public boolean getAlarmReboot()
  {
    return this.userPreferencesData.getBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.time_alarm_reboot");
  }
  
  public int getAlarmRepetition()
  {
    return this.userPreferencesData.getIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.repeat_alarm");
  }
  
  public int getAlarmSound()
  {
    return this.userPreferencesData.getIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.sound_alarm");
  }
  
  public int getAlarmWindow()
  {
    return this.userPreferencesData.getIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.window_alarm");
  }
  
  public String getAuthentication()
  {
    Object localObject = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    String str3 = ((Calendar)localObject).getTimeInMillis() / 1000L;
    String str1 = getRSTKey();
    String str4 = getRSTToken();
    String str2 = getUserId();
    if ((str1 == null) || (str4 == null) || (str2 == null)) {
      localObject = null;
    }
    for (;;)
    {
      return (String)localObject;
      localObject = "";
      try
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>(String.valueOf(str4));
        str1 = Sha.hmacSha256(str1, str3);
        localObject = str1;
      }
      catch (Exception localException)
      {
        for (;;)
        {
          localException.printStackTrace();
        }
      }
      localObject = Base64.encodeToString((str2 + ":" + (String)localObject + ":" + str3).getBytes(), 0);
      localObject = ("Basic " + (String)localObject).replaceAll("\n", "").trim();
      Log.e("userID", "getAuthentication:" + (String)localObject);
    }
  }
  
  public String getBedId()
  {
    return this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.bedserial");
  }
  
  public String getBoardVersion()
  {
    String str2 = this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.bed_board_version_tag");
    String str1;
    if (str2 != null)
    {
      str1 = str2;
      if (str2.length() != 0) {}
    }
    else
    {
      str1 = "revX";
    }
    return str1;
  }
  
  public String getClearMindData()
  {
    return this.userPreferencesData.getStringUserValue(getUserId(), "com.resmed.refresh.model.clear_mind");
  }
  
  public int getDefaultBrightness()
  {
    return this.userPreferencesData.getIntegerConfigValue("com.resmed.refresh.model.default_brigthness_value_tag");
  }
  
  public String getDeviceId()
  {
    setDeviceId();
    return this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.device_id_tag");
  }
  
  public boolean getExpansionValidated()
  {
    return this.userPreferencesData.getBooleanConfigValue("com.resmed.refresh.model.expansion_validated_tag");
  }
  
  public String getFirmwareVersion()
  {
    String str2 = this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.bed_firmware_version_tag");
    String str1;
    if (str2 != null)
    {
      str1 = str2;
      if (str2.length() != 0) {}
    }
    else
    {
      str1 = "x.x";
    }
    return str1;
  }
  
  public boolean getGMCTokenStatus()
  {
    return this.userPreferencesData.getBooleanConfigValue("com.resmed.refresh.model.gmc_token_status_tag");
  }
  
  public boolean getHasBeenLogout()
  {
    return this.userPreferencesData.getBooleanConfigValue("com.resmed.refresh.model.user_has_been_logout_tag");
  }
  
  public boolean getHasToValidateEmail()
  {
    if (this.user == null) {}
    for (boolean bool = false;; bool = this.userPreferencesData.getBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.has_to_validate_email")) {
      return bool;
    }
  }
  
  public String getLastEmail()
  {
    return this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.last_email_logged_in_tag");
  }
  
  public RST_LocationItem getLastLocation()
  {
    RST_LocationItem localRST_LocationItem;
    if ((this.locationManager != null) && (this.user != null))
    {
      LatLng localLatLng = this.locationManager.getLastLocation();
      localRST_LocationItem = this.user.getLocation();
      localRST_LocationItem.setLatitude((float)localLatLng.latitude);
      localRST_LocationItem.setLongitude((float)localLatLng.longitude);
      this.user.setAndSaveLocation(localRST_LocationItem);
    }
    for (;;)
    {
      return localRST_LocationItem;
      localRST_LocationItem = null;
    }
  }
  
  public void getLocaleMUnitMapping(RST_CallbackItem<RST_Response<List<LocaleMUnitResponse>>> paramRST_CallbackItem)
  {
    serviceLocaleMUnitMapping(paramRST_CallbackItem);
  }
  
  public RST_LocationManager getLocationManager()
  {
    return this.locationManager;
  }
  
  public boolean getLocationPermission()
  {
    if ((this.user == null) || (!isLoggedIn())) {}
    for (boolean bool = false;; bool = this.user.getSettings().getLocationPermission()) {
      return bool;
    }
  }
  
  public long getNigthQuestionsTimestamp()
  {
    return this.userPreferencesData.getLongConfigValue("com.resmed.refresh.model.night_questions_timestamp_tag", 0L);
  }
  
  public boolean getPlayAutoRelax()
  {
    return this.userPreferencesData.getBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.play_auto_relax");
  }
  
  public String getRM20LibraryVersion()
  {
    String str2 = this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.rm20_library_version_tag");
    String str1;
    if (str2 != null)
    {
      str1 = str2;
      if (str2.length() != 0) {}
    }
    else
    {
      str1 = "x.x";
    }
    return str1;
  }
  
  public Integer getRelaxSound()
  {
    return Integer.valueOf(this.userPreferencesData.getIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.sound_relax"));
  }
  
  public ResMedServerAPI getResMedServerAPI()
  {
    return this.resMedServerAPI;
  }
  
  public String getSmartAlarmRM20Token()
  {
    return this.userPreferencesData.getStringUserValue(this.user.getId(), "com.resmed.refresh.model.smart_alarm_rm20_token");
  }
  
  public String getSmartAlarmValidToken()
  {
    return this.userPreferencesData.getStringUserValue(this.user.getId(), "com.resmed.refresh.model.smart_alarm_valid_token");
  }
  
  public boolean getTermsAndConditions()
  {
    return this.user.getSettings().getTac1();
  }
  
  public boolean getUseMetricUnits()
  {
    return this.user.getSettings().getUseMetricUnits();
  }
  
  public boolean getUsePushNotifications()
  {
    return this.user.getSettings().getPushNotifications();
  }
  
  public RST_User getUser()
  {
    return this.user;
  }
  
  public String getUserSessionID()
  {
    if (this.user == null) {}
    for (String str = "c63eb080-a864-11e3-a5e2-0800200c9a66";; str = this.user.getId()) {
      return str;
    }
  }
  
  public boolean haveAdvice()
  {
    boolean bool = true;
    List localList = localAdvices(1);
    if ((localList != null) && (localList.size() > 0)) {}
    for (;;)
    {
      return bool;
      bool = false;
    }
  }
  
  public void historicalSleepRecords(final Date paramDate1, final Date paramDate2, final RST_CallbackItem<RST_Response<List<RST_SleepSessionInfo>>> paramRST_CallbackItem, boolean paramBoolean)
  {
    final List localList = localSleepSessionsBetweenDates(paramDate1, paramDate2);
    if ((paramBoolean) || (localList == null) || (localList.size() == 0)) {
      serviceGetHistoricalRecords(paramDate1, paramDate2, new RST_CallbackItem()
      {
        public void onResult(RST_Response<List<RST_SleepSessionInfo>> paramAnonymousRST_Response)
        {
          paramAnonymousRST_Response = new RST_Response();
          List localList = RefreshModelController.this.localSleepSessionsBetweenDates(paramDate1, paramDate2);
          paramAnonymousRST_Response.setStatus(true);
          paramAnonymousRST_Response.setResponse(localList);
          paramRST_CallbackItem.onResult(paramAnonymousRST_Response);
        }
      });
    }
    for (;;)
    {
      return;
      new Handler(Looper.getMainLooper()).post(new Runnable()
      {
        public void run()
        {
          RST_Response localRST_Response = new RST_Response();
          localRST_Response.setStatus(true);
          localRST_Response.setResponse(localList);
          paramRST_CallbackItem.onResult(localRST_Response);
        }
      });
    }
  }
  
  public boolean isFirstLaunch()
  {
    boolean bool1 = true;
    boolean bool2 = this.userPreferencesData.getBooleanConfigValue("com.resmed.refresh.model.first_launch");
    this.userPreferencesData.setBooleanConfigValue("com.resmed.refresh.model.first_launch", true);
    if (bool2) {
      bool1 = false;
    }
    return bool1;
  }
  
  public boolean isLoggedIn()
  {
    if ((getRSTToken() != null) && (getRSTToken().length() > 0)) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public boolean isProfileUpdatePending()
  {
    return this.userPreferencesData.getBooleanConfigValue("com.resmed.refresh.model.profile_update_pending_tag");
  }
  
  public void latestAdviceList(final RST_CallbackItem<RST_Response<List<RST_AdviceItem>>> paramRST_CallbackItem, boolean paramBoolean)
  {
    final List localList = localAdvices(4);
    if ((paramBoolean) || (localList == null) || (localList.size() == 0)) {
      synchroniseLatestAdvice(paramRST_CallbackItem);
    }
    for (;;)
    {
      return;
      new Handler(Looper.getMainLooper()).post(new Runnable()
      {
        public void run()
        {
          RST_Response localRST_Response = new RST_Response();
          localRST_Response.setStatus(true);
          localRST_Response.setResponse(localList);
          paramRST_CallbackItem.onResult(localRST_Response);
        }
      });
    }
  }
  
  public void latestNightQuestions(RST_CallbackItem<RST_Response<RST_NightQuestions>> paramRST_CallbackItem)
  {
    servicePreSleepQuestions(paramRST_CallbackItem);
  }
  
  public RST_AdviceItem localAdviceForId(long paramLong)
  {
    String str = getUserId();
    return (RST_AdviceItem)this.daoSession.getRST_AdviceItemDao().queryBuilder().where(RST_AdviceItemDao.Properties.IdUser.eq(str), new WhereCondition[] { RST_AdviceItemDao.Properties.Id.eq(Long.valueOf(paramLong)) }).unique();
  }
  
  public List<RST_AdviceItem> localAdvices()
  {
    return localAdvices(Integer.MAX_VALUE);
  }
  
  public List<RST_AdviceItem> localAdvices(int paramInt)
  {
    String str = getUserId();
    return this.daoSession.getRST_AdviceItemDao().queryBuilder().where(RST_AdviceItemDao.Properties.IdUser.eq(str), new WhereCondition[0]).orderDesc(new Property[] { RST_AdviceItemDao.Properties.Timestamp }).limit(paramInt).list();
  }
  
  public List<RST_AdviceItem> localAdvicesForDate(Date paramDate)
  {
    List localList = RefreshTools.dateRangesForDay(paramDate);
    paramDate = this.daoSession.getRST_AdviceItemDao().queryBuilder();
    paramDate.where(RST_AdviceItemDao.Properties.IdUser.eq(this.user.getId()), new WhereCondition[] { paramDate.and(RST_AdviceItemDao.Properties.Timestamp.gt(localList.get(0)), RST_AdviceItemDao.Properties.Timestamp.lt(localList.get(1)), new WhereCondition[0]) });
    return paramDate.list();
  }
  
  public List<RST_AdviceItem> localAdvicesForSession(long paramLong)
  {
    QueryBuilder localQueryBuilder = this.daoSession.getRST_AdviceItemDao().queryBuilder();
    localQueryBuilder.where(RST_AdviceItemDao.Properties.IdUser.eq(this.user.getId()), new WhereCondition[] { RST_AdviceItemDao.Properties.SessionId.eq(Long.valueOf(paramLong)) });
    return localQueryBuilder.list();
  }
  
  public List<RST_AdviceItem> localAdvicesNotRead()
  {
    String str = getUserId();
    return this.daoSession.getRST_AdviceItemDao().queryBuilder().where(RST_AdviceItemDao.Properties.IdUser.eq(str), new WhereCondition[] { RST_AdviceItemDao.Properties.Read.eq(Boolean.valueOf(false)) }).list();
  }
  
  public RST_AdviceItem localLatestAdvice()
  {
    Object localObject = getUserId();
    localObject = this.daoSession.getRST_AdviceItemDao().queryBuilder().where(RST_AdviceItemDao.Properties.IdUser.eq(localObject), new WhereCondition[0]).orderDesc(new Property[] { RST_AdviceItemDao.Properties.Timestamp }).list();
    if ((localObject != null) && (((List)localObject).size() > 0)) {}
    for (localObject = (RST_AdviceItem)((List)localObject).get(0);; localObject = null) {
      return (RST_AdviceItem)localObject;
    }
  }
  
  public RST_SleepSessionInfo localLatestSleepSession()
  {
    Object localObject = localSleepSessionsAccesor(1, true, true, null, null, -1L, true);
    if ((localObject == null) || (((List)localObject).size() == 0)) {}
    for (localObject = null;; localObject = (RST_SleepSessionInfo)((List)localObject).get(0)) {
      return (RST_SleepSessionInfo)localObject;
    }
  }
  
  public RST_SleepSessionInfo localLatestUnfinishedSession()
  {
    Object localObject2 = this.daoSession.getRST_SleepSessionInfoDao();
    Object localObject1 = new ArrayList();
    ((List)localObject1).add(RST_SleepSessionInfoDao.Properties.IdUser.eq(this.user.getId()));
    ((List)localObject1).add(RST_SleepSessionInfoDao.Properties.Id.lt(Long.valueOf(1000000000L)));
    ((List)localObject1).add(RST_SleepSessionInfoDao.Properties.Completed.eq(Integer.valueOf(0)));
    localObject2 = ((RST_SleepSessionInfoDao)localObject2).queryBuilder();
    ((QueryBuilder)localObject2).where((WhereCondition)((List)localObject1).remove(0), (WhereCondition[])((List)localObject1).toArray(new WhereCondition[((List)localObject1).size()]));
    ((QueryBuilder)localObject2).orderDesc(new Property[] { RST_SleepSessionInfoDao.Properties.StartTime });
    localObject1 = ((QueryBuilder)localObject2).list();
    if ((localObject1 != null) && (((List)localObject1).size() != 0)) {}
    for (localObject1 = (RST_SleepSessionInfo)((List)localObject1).get(0);; localObject1 = null) {
      return (RST_SleepSessionInfo)localObject1;
    }
  }
  
  public RST_SleepSessionInfo localNextSleepSession(Date paramDate)
  {
    paramDate = localSleepSessionsBetweenDates(paramDate, new Date(Long.MAX_VALUE));
    if ((paramDate == null) || (paramDate.size() == 0)) {}
    for (paramDate = null;; paramDate = (RST_SleepSessionInfo)paramDate.get(0)) {
      return paramDate;
    }
  }
  
  public RST_NightQuestions localNightQuestions()
  {
    return this.user.getNightQuestions();
  }
  
  public RST_SleepSessionInfo localPreviousSleepSession(Date paramDate)
  {
    paramDate = localSleepSessionsBetweenDates(new Date(0L), paramDate);
    if ((paramDate == null) || (paramDate.size() == 0)) {}
    for (paramDate = null;; paramDate = (RST_SleepSessionInfo)paramDate.get(paramDate.size() - 1)) {
      return paramDate;
    }
  }
  
  public List<RST_SleepSessionInfo> localSessionsForUpload()
  {
    Object localObject = this.daoSession.getRST_SleepSessionInfoDao();
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(RST_SleepSessionInfoDao.Properties.IdUser.eq(this.user.getId()));
    localArrayList.add(RST_SleepSessionInfoDao.Properties.Id.lt(Long.valueOf(1000000000L)));
    localArrayList.add(RST_SleepSessionInfoDao.Properties.Completed.eq(Integer.valueOf(1)));
    localArrayList.add(RST_SleepSessionInfoDao.Properties.Uploaded.eq(Integer.valueOf(0)));
    localObject = ((RST_SleepSessionInfoDao)localObject).queryBuilder();
    ((QueryBuilder)localObject).where((WhereCondition)localArrayList.remove(0), (WhereCondition[])localArrayList.toArray(new WhereCondition[localArrayList.size()]));
    ((QueryBuilder)localObject).orderDesc(new Property[] { RST_SleepSessionInfoDao.Properties.StartTime });
    return ((QueryBuilder)localObject).list();
  }
  
  public RST_SleepSessionInfo localSleepSessionForId(long paramLong)
  {
    Object localObject = localSleepSessionsAccesor(1, true, false, null, null, paramLong, false);
    if ((localObject == null) || (((List)localObject).size() == 0)) {}
    for (localObject = null;; localObject = (RST_SleepSessionInfo)((List)localObject).get(0)) {
      return (RST_SleepSessionInfo)localObject;
    }
  }
  
  public RST_SleepSessionInfo localSleepSessionInDay(Date paramDate)
  {
    paramDate = RefreshTools.dateRangesForDay(paramDate);
    paramDate = localSleepSessionsBetweenDates((Date)paramDate.get(0), (Date)paramDate.get(1));
    if ((paramDate == null) || (paramDate.size() == 0)) {}
    for (paramDate = null;; paramDate = (RST_SleepSessionInfo)paramDate.get(0)) {
      return paramDate;
    }
  }
  
  public List<RST_SleepSessionInfo> localSleepSessionsAll()
  {
    return localSleepSessionsAccesor(-1, true, false, null, null, -1L, true);
  }
  
  public List<RST_SleepSessionInfo> localSleepSessionsBetweenDates(Date paramDate1, Date paramDate2)
  {
    return localSleepSessionsAccesor(-1, true, false, paramDate1, paramDate2, -1L, true);
  }
  
  public List<RST_SleepSessionInfo> localSleepSessionsBetweenDates(Date paramDate1, Date paramDate2, boolean paramBoolean)
  {
    return localSleepSessionsAccesor(-1, true, paramBoolean, paramDate1, paramDate2, -1L, true);
  }
  
  public List<RST_SleepSessionInfo> localSleepSessionsInMonth(int paramInt1, int paramInt2)
  {
    Object localObject1 = new GregorianCalendar();
    ((Calendar)localObject1).set(1, paramInt2);
    ((Calendar)localObject1).set(2, paramInt1);
    ((Calendar)localObject1).set(5, 1);
    ((Calendar)localObject1).set(11, 0);
    ((Calendar)localObject1).set(12, 0);
    ((Calendar)localObject1).set(13, 0);
    ((Calendar)localObject1).set(14, 0);
    Object localObject2 = ((Calendar)localObject1).getTime();
    ((Calendar)localObject1).set(5, ((Calendar)localObject1).getActualMaximum(5) + 1);
    ((Calendar)localObject1).add(13, 1);
    localObject2 = localSleepSessionsBetweenDates((Date)localObject2, ((Calendar)localObject1).getTime(), true);
    if (localObject2 != null)
    {
      localObject1 = localObject2;
      if (((List)localObject2).size() != 0) {}
    }
    else
    {
      localObject1 = null;
    }
    return (List<RST_SleepSessionInfo>)localObject1;
  }
  
  public List<RST_AdviceItem> localTasks()
  {
    String str = getUserId();
    return this.daoSession.getRST_AdviceItemDao().queryBuilder().where(RST_AdviceItemDao.Properties.IdUser.eq(str), new WhereCondition[] { RST_AdviceItemDao.Properties.Type.eq(Integer.valueOf(RST_AdviceItem.AdviceType.kAdviceTypeTask.ordinal())) }).where(RST_AdviceItemDao.Properties.IdUser.eq(str), new WhereCondition[0]).orderDesc(new Property[] { RST_AdviceItemDao.Properties.Timestamp }).list();
  }
  
  public RST_User localUserDataForId(String paramString)
  {
    if (paramString == null) {}
    for (paramString = null;; paramString = (RST_User)this.daoSession.getRST_UserDao().queryBuilder().where(RST_UserDao.Properties.Id.eq(paramString), new WhereCondition[0]).unique()) {
      return paramString;
    }
  }
  
  public List<RST_ValueItem> numberArrayFromValueSet(List<Integer> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0;; i++)
    {
      if (i >= paramList.size()) {
        return localArrayList;
      }
      RST_ValueItem localRST_ValueItem = new RST_ValueItem();
      localRST_ValueItem.setOrdr(i);
      localRST_ValueItem.setValue(((Integer)paramList.get(i)).intValue());
      localArrayList.add(localRST_ValueItem);
    }
  }
  
  public RST_SleepSessionInfo oldestSessionForUpload()
  {
    Object localObject = localSessionsForUpload();
    if ((localObject == null) || (((List)localObject).size() == 0)) {}
    for (localObject = null;; localObject = (RST_SleepSessionInfo)localSessionsForUpload().get(0)) {
      return (RST_SleepSessionInfo)localObject;
    }
  }
  
  public void registerPushNotificationToken(String paramString)
  {
    setGMCToken(paramString);
    setGMCAppVersion(RefreshApplication.getInstance().getAppVersion());
    serviceRegisterPushNotificationToken(paramString);
  }
  
  public void save()
  {
    Object localObject1;
    Object localObject2;
    if (this.user != null)
    {
      this.daoSession.getRST_UserDao().update(this.user);
      this.daoSession.getRST_SettingsDao().update(this.user.getSettings());
      this.daoSession.getRST_UserProfileDao().update(this.user.getProfile());
      this.daoSession.getRST_LocationItemDao().update(this.user.getLocation());
      if (this.user.getAdvices() != null)
      {
        localObject1 = this.user.getAdvices().iterator();
        if (((Iterator)localObject1).hasNext()) {
          break label219;
        }
      }
      this.user.resetAdvices();
      preSleepLog.addTrace("RefershModelController:save ******");
      if (this.user.getNightQuestions() != null)
      {
        this.daoSession.getRST_NightQuestionsDao().update(this.user.getNightQuestions());
        localObject2 = this.user.getNightQuestions().getQuestions().iterator();
      }
    }
    for (;;)
    {
      if (!((Iterator)localObject2).hasNext())
      {
        this.user.getNightQuestions().resetQuestions();
        this.user.resetSleepSessions();
        System.out.println("##### in Save ::" + this.user.getSettings().getWeightUnit());
        return;
        label219:
        localObject2 = (RST_AdviceItem)((Iterator)localObject1).next();
        this.daoSession.getRST_AdviceItemDao().update(localObject2);
        break;
      }
      localObject1 = (RST_QuestionItem)((Iterator)localObject2).next();
      preSleepLog.addTrace("RefershModelController:save QuestionItem Question= " + ((RST_QuestionItem)localObject1).getText() + "QuestionItem Question index= " + ((RST_QuestionItem)localObject1).getId() + " answer=" + ((RST_QuestionItem)localObject1).getAnswer());
      this.daoSession.getRST_QuestionItemDao().update(localObject1);
    }
  }
  
  public void saveBoardVersion(String paramString)
  {
    this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.bed_board_version_tag", paramString);
  }
  
  public void saveFirmwareVersion(String paramString)
  {
    this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.bed_firmware_version_tag", paramString);
  }
  
  public void saveRM20LibraryVersion(String paramString)
  {
    this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.rm20_library_version_tag", paramString);
  }
  
  public void sendLast30DayReport(String paramString1, String paramString2, boolean paramBoolean, RST_CallbackItem<RST_Response<Last30DaysReportResponse>> paramRST_CallbackItem)
  {
    serviceLast30DaysReport(paramString1, paramString2, paramBoolean, paramRST_CallbackItem);
  }
  
  public boolean serviceDidLaunch()
  {
    return this.userPreferencesData.getBooleanConfigValue("com.resmed.refresh.model.service_did_launch_tag");
  }
  
  public void serviceEmailVerification(RST_CallbackItem<RST_Response<Object>> paramRST_CallbackItem)
  {
    new RST_ServiceEmailVerification(this.resMedServerAPI).resendEmailVerification(paramRST_CallbackItem);
  }
  
  public void serviceGetAdviceForId(long paramLong, RST_CallbackItem<RST_Response<RST_AdviceItem>> paramRST_CallbackItem)
  {
    new RST_ServiceAdvice(this.resMedServerAPI).getAdviceItem(paramLong, paramRST_CallbackItem);
  }
  
  public void serviceGetHistoricalRecords(Date paramDate1, Date paramDate2, RST_CallbackItem<RST_Response<List<RST_SleepSessionInfo>>> paramRST_CallbackItem)
  {
    new RST_ServiceSleepRecord(this.resMedServerAPI).getAllRecords(paramDate1, paramDate2, false, paramRST_CallbackItem);
  }
  
  public void serviceGetLatestAdvice(Date paramDate1, Date paramDate2, RST_CallbackItem<RST_Response<List<RST_AdviceItem>>> paramRST_CallbackItem)
  {
    new RST_ServiceAdvice(this.resMedServerAPI).adviceForDates(paramDate1, paramDate2, paramRST_CallbackItem);
  }
  
  public void serviceGetLatestRecord(RST_CallbackItem<RST_Response<List<RST_SleepSessionInfo>>> paramRST_CallbackItem)
  {
    new RST_ServiceSleepRecord(this.resMedServerAPI).getLatestRecord(paramRST_CallbackItem);
  }
  
  public void serviceGetRecord(long paramLong, RST_CallbackItem<RST_Response<RST_SleepSessionInfo>> paramRST_CallbackItem)
  {
    new RST_ServiceSleepRecord(this.resMedServerAPI).getRecord(paramLong, paramRST_CallbackItem);
  }
  
  public void serviceLaunch()
  {
    Object localObject2;
    LaunchData localLaunchData;
    if ((this.user != null) && (!serviceDidLaunch()))
    {
      localObject2 = RefreshApplication.getInstance().getApplicationContext();
      localLaunchData = new LaunchData();
      localObject1 = "";
    }
    try
    {
      localObject2 = ((Context)localObject2).getPackageManager().getPackageInfo(((Context)localObject2).getPackageName(), 0).versionName;
      localObject1 = localObject2;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      for (;;)
      {
        localNameNotFoundException.printStackTrace();
      }
    }
    localLaunchData.setAppVersion((String)localObject1);
    localLaunchData.setDeviceModel(Build.MODEL);
    localLaunchData.setDeviceName(Build.DEVICE);
    localLaunchData.setDeviceOS("Android");
    localLaunchData.setDeviceOSVersion(Build.VERSION.RELEASE);
    Object localObject1 = getDeviceId();
    new RST_ServiceLaunch(this.resMedServerAPI).userAppLaunch((String)localObject1, localLaunchData);
  }
  
  public void serviceUpdateFileRecord(String paramString, RST_SleepSessionInfo paramRST_SleepSessionInfo, RST_CallbackItem<RST_Response<Object>> paramRST_CallbackItem)
  {
    new RST_ServiceFileUploader(this.resMedServerAPI).uploadFile(paramString, paramRST_SleepSessionInfo, paramRST_CallbackItem);
  }
  
  public void serviceUpdateRecord(RST_SleepSessionInfo paramRST_SleepSessionInfo, RST_CallbackItem<RST_Response<Object>> paramRST_CallbackItem)
  {
    new RST_ServiceSleepRecord(this.resMedServerAPI).updateRecord(SleepSessionMapper.getRecord(paramRST_SleepSessionInfo, this.user.getNightQuestions()), paramRST_CallbackItem);
  }
  
  public void setBedId(String paramString)
  {
    this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.bedserial", paramString);
  }
  
  public void setDefaultBrightness(int paramInt)
  {
    this.userPreferencesData.setIntegerConfigValue("com.resmed.refresh.model.default_brigthness_value_tag", Integer.valueOf(paramInt));
  }
  
  public void setExpansionValidated(boolean paramBoolean)
  {
    this.userPreferencesData.setBooleanConfigValue("com.resmed.refresh.model.expansion_validated_tag", paramBoolean);
  }
  
  public void setGMCTokenStatus(boolean paramBoolean)
  {
    this.userPreferencesData.setBooleanConfigValue("com.resmed.refresh.model.gmc_token_status_tag", paramBoolean);
  }
  
  public void setHasBeenLogout(boolean paramBoolean)
  {
    this.userPreferencesData.setBooleanConfigValue("com.resmed.refresh.model.user_has_been_logout_tag", paramBoolean);
  }
  
  public void setHasToValidateEmail(boolean paramBoolean)
  {
    if (this.user != null) {
      this.userPreferencesData.setBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.has_to_validate_email", paramBoolean);
    }
  }
  
  public void setLastEmail(String paramString)
  {
    this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.last_email_logged_in_tag", paramString);
  }
  
  public void setNigthQuestionsTimestamp(long paramLong)
  {
    this.userPreferencesData.setLongConfigValue("com.resmed.refresh.model.night_questions_timestamp_tag", paramLong);
  }
  
  public void setProfileUpdatePending(boolean paramBoolean)
  {
    this.userPreferencesData.setBooleanConfigValue("com.resmed.refresh.model.profile_update_pending_tag", paramBoolean);
  }
  
  public void setServiceDidLaunch(boolean paramBoolean)
  {
    this.userPreferencesData.setBooleanConfigValue("com.resmed.refresh.model.service_did_launch_tag", paramBoolean);
  }
  
  public void setUseMetricUnits(boolean paramBoolean)
  {
    if (this.user != null)
    {
      this.user.getSettings().setUseMetricUnits(paramBoolean);
      save();
    }
  }
  
  public void setUsePushNotifications(boolean paramBoolean)
  {
    this.user.getSettings().setPushNotifications(paramBoolean);
    save();
  }
  
  public void setup()
  {
    Object localObject = RefreshApplication.getInstance().getApplicationContext();
    this.resMedServerAPI = new ResMedConnectorAPI(new HttpDefaultConnector());
    this.userPreferencesData = new RefreshUserPreferencesData((Context)localObject);
    this.locationManager = new RST_LocationManager();
    localObject = new DaoMaster.DevOpenHelper((Context)localObject, "refresh-db", null).getWritableDatabase();
    this.daoSession = new DaoMaster((SQLiteDatabase)localObject).newSession();
    DaoMaster.createAllTables((SQLiteDatabase)localObject, true);
    localUserData();
    serviceLaunch();
    this.locationManager.setLocationUpdatesListener(getLocationPermission());
  }
  
  public void setupNotifications(Activity paramActivity)
  {
    if (checkPlayServices(paramActivity))
    {
      paramActivity = getRegistrationId(paramActivity);
      if (!paramActivity.equals("")) {
        break label34;
      }
      new RegisterGMCThread().start();
    }
    for (;;)
    {
      return;
      label34:
      if (!getGMCTokenStatus()) {
        serviceRegisterPushNotificationToken(paramActivity);
      }
    }
  }
  
  public void storeActiveAlarm(Boolean paramBoolean)
  {
    this.userPreferencesData.setBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.active_alarm", paramBoolean.booleanValue());
  }
  
  public void storeActiveRelax(boolean paramBoolean)
  {
    this.userPreferencesData.setBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.active_relax", paramBoolean);
  }
  
  public void storeAlarmDateTime(Long paramLong)
  {
    this.userPreferencesData.setLongUserValue(this.user.getId(), "com.resmed.refresh.model.time_alarm_date", paramLong.longValue());
  }
  
  public void storeAlarmIsPlaying(boolean paramBoolean)
  {
    this.userPreferencesData.setBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.alarm_is_playing", paramBoolean);
  }
  
  public void storeAlarmReboot(boolean paramBoolean)
  {
    this.userPreferencesData.setBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.time_alarm_reboot", paramBoolean);
  }
  
  public void storeAlarmRepetition(int paramInt)
  {
    this.userPreferencesData.setIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.repeat_alarm", paramInt);
  }
  
  public void storeAlarmSound(Integer paramInteger)
  {
    this.userPreferencesData.setIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.sound_alarm", paramInteger.intValue());
  }
  
  public void storeAlarmWindow(int paramInt)
  {
    this.userPreferencesData.setIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.window_alarm", paramInt);
  }
  
  public void storeClearMindData(String paramString)
  {
    this.userPreferencesData.setStringUserValue(getUserId(), "com.resmed.refresh.model.clear_mind", paramString);
  }
  
  public void storePlayAutoRelax(boolean paramBoolean)
  {
    this.userPreferencesData.setBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.play_auto_relax", paramBoolean);
  }
  
  public void storeRelaxSound(Integer paramInteger)
  {
    this.userPreferencesData.setIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.sound_relax", paramInteger.intValue());
  }
  
  public void storeSmartAlarmRM20Token(String paramString)
  {
    this.userPreferencesData.setStringUserValue(this.user.getId(), "com.resmed.refresh.model.smart_alarm_rm20_token", paramString);
  }
  
  public void storeSmartAlarmValidToken(String paramString)
  {
    this.userPreferencesData.setStringUserValue(this.user.getId(), "com.resmed.refresh.model.smart_alarm_valid_token", paramString);
  }
  
  public void synchroniseLatestAdvice(RST_CallbackItem<RST_Response<List<RST_AdviceItem>>> paramRST_CallbackItem)
  {
    RST_ServiceAccountSync localRST_ServiceAccountSync = new RST_ServiceAccountSync(this.resMedServerAPI);
    RST_AdviceItem localRST_AdviceItem = localLatestAdvice();
    if (localRST_AdviceItem == null) {}
    for (int i = 0;; i = (int)localRST_AdviceItem.getId())
    {
      localRST_ServiceAccountSync.syncLatestAdvice(i, paramRST_CallbackItem);
      return;
    }
  }
  
  public void synchroniseLatestAll(final RST_CallbackItem<RST_Response<Object>> paramRST_CallbackItem, boolean paramBoolean)
  {
    RST_SleepSessionInfo localRST_SleepSessionInfo = localLatestSleepSession();
    int i;
    if (localRST_SleepSessionInfo == null)
    {
      i = 0;
      long l = i;
      RST_AdviceItem localRST_AdviceItem = localLatestAdvice();
      if ((!paramBoolean) && (localRST_AdviceItem != null) && (localRST_SleepSessionInfo != null)) {
        break label65;
      }
      new RST_ServiceAccountSync(this.resMedServerAPI).syncLatestAll(l, paramRST_CallbackItem);
    }
    for (;;)
    {
      return;
      i = (int)localRST_SleepSessionInfo.getId();
      break;
      label65:
      new Handler(Looper.getMainLooper()).post(new Runnable()
      {
        public void run()
        {
          RST_Response localRST_Response = new RST_Response();
          localRST_Response.setStatus(true);
          paramRST_CallbackItem.onResult(localRST_Response);
        }
      });
    }
  }
  
  public void synchroniseLatestSleepSessions(RST_CallbackItem<RST_Response<List<RST_SleepSessionInfo>>> paramRST_CallbackItem)
  {
    RST_ServiceAccountSync localRST_ServiceAccountSync = new RST_ServiceAccountSync(this.resMedServerAPI);
    RST_SleepSessionInfo localRST_SleepSessionInfo = localLatestSleepSession();
    if (localRST_SleepSessionInfo == null) {}
    for (long l = 0L;; l = localRST_SleepSessionInfo.getId())
    {
      localRST_ServiceAccountSync.syncLatestSleepRecord(l, paramRST_CallbackItem);
      return;
    }
  }
  
  public void synchroniseLatestUserProfile(RST_CallbackItem<RST_Response<RST_UserProfile>> paramRST_CallbackItem)
  {
    new RST_ServiceAccountSync(this.resMedServerAPI).syncLatestUserProfile(paramRST_CallbackItem);
  }
  
  public void updateFeedback(RST_AdviceItem paramRST_AdviceItem, RST_CallbackItem<RST_Response<Object>> paramRST_CallbackItem)
  {
    serviceUpdateFeedback(paramRST_AdviceItem, paramRST_CallbackItem);
  }
  
  public void updateFlurryEvents(String paramString)
  {
    HashMap localHashMap = new HashMap();
    if (RefreshApplication.is64bitdevice) {
      localHashMap.put("DeviceArchitecture", "64bit_androidDevice");
    }
    for (;;)
    {
      if (Consts.USE_FLURRY_REPORTS)
      {
        FlurryAgent.logEvent(paramString, localHashMap, true);
        FlurryAgent.endTimedEvent(paramString);
      }
      return;
      localHashMap.put("DeviceArchitecture", "32bit_androidDevice");
    }
  }
  
  public void updateLocation()
  {
    this.locationManager.updateLocation();
  }
  
  public void updateLocationUser(Location paramLocation)
  {
    AppFileLog.addTrace("Location - updateLocationUser user:" + this.user + " location:" + paramLocation);
    if ((this.user != null) && (paramLocation != null))
    {
      RST_LocationItem localRST_LocationItem2 = this.user.getLocation();
      RST_LocationItem localRST_LocationItem1 = localRST_LocationItem2;
      if (localRST_LocationItem2 == null) {
        localRST_LocationItem1 = createLocationItem(-1.0F, -1.0F);
      }
      localRST_LocationItem1.setLocale(Locale.getDefault().getISO3Language());
      localRST_LocationItem1.setLatitude((float)paramLocation.getLatitude());
      localRST_LocationItem1.setLongitude((float)paramLocation.getLongitude());
      localRST_LocationItem1.setTimezone(userTimezoneOffset());
      this.user.setAndSaveLocation(localRST_LocationItem1);
      this.daoSession.getRST_UserDao().update(this.user);
      AppFileLog.addTrace("Location - Controller set location to (" + localRST_LocationItem1.getLatitude() + "," + localRST_LocationItem1.getLongitude() + ")");
      Log.d("com.resmed.refresh.model", "Location of user " + this.user.getId() + " updated to (" + paramLocation.getLatitude() + ", " + paramLocation.getLongitude() + ")");
    }
  }
  
  public void updateUser(RST_CallbackItem<RST_Response<RST_User>> paramRST_CallbackItem)
  {
    this.daoSession.update(this.user);
    serviceUpdateUser(paramRST_CallbackItem);
  }
  
  public void updateUserProfile(RST_CallbackItem<RST_Response<RST_UserProfile>> paramRST_CallbackItem)
  {
    Object localObject2 = RefreshApplication.getInstance().getApplicationContext().getResources().getConfiguration().locale;
    Object localObject1 = ((Locale)localObject2).toString();
    localObject2 = ((Locale)localObject2).getCountry();
    try
    {
      this.user.getProfile().setLocale((String)localObject1);
      this.user.getProfile().setCountryCode((String)localObject2);
      this.user.getSettings().setLocale((String)localObject1);
      this.user.getSettings().setCountryCode((String)localObject2);
      this.daoSession.update(this.user);
      this.daoSession.update(this.user.getProfile());
      localObject1 = System.out;
      localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>("###### updateUserProfile : ");
      ((PrintStream)localObject1).println(this.user.getSettings().getWeightUnit());
      serviceUpdateUserProfile(paramRST_CallbackItem);
      return;
    }
    catch (Exception paramRST_CallbackItem)
    {
      for (;;)
      {
        paramRST_CallbackItem.printStackTrace();
      }
    }
  }
  
  public void userForgotPassword(String paramString, RST_CallbackItem<RST_Response<RST_User>> paramRST_CallbackItem)
  {
    serviceForgotPassword(paramString, paramRST_CallbackItem);
  }
  
  public void userLogin(String paramString1, String paramString2, RST_CallbackItem<RST_Response<RST_User>> paramRST_CallbackItem)
  {
    serviceUserLogin(paramString1, paramString2, paramRST_CallbackItem);
  }
  
  public void userLogout()
  {
    if ((controller != null) && (controller.getUser() != null))
    {
      RST_NightQuestions localRST_NightQuestions = controller.getUser().getNightQuestions();
      if (localRST_NightQuestions != null)
      {
        localRST_NightQuestions.delete();
        controller.save();
      }
    }
    this.user = null;
    UserProfileDataManager.logout();
    MindClearManager.getInstance().logout();
    clearSessionToken();
    clearRSTKey();
    clearUserId();
  }
  
  public void userRegister(User paramUser, RST_CallbackItem<RST_Response<RST_User>> paramRST_CallbackItem)
  {
    serviceUserRegister(paramUser, paramRST_CallbackItem);
  }
  
  public void userRegister(String paramString1, String paramString2, String paramString3, String paramString4, RST_CallbackItem<RST_Response<RST_User>> paramRST_CallbackItem)
  {
    User localUser = new User();
    localUser.setEmail(paramString1);
    localUser.setUsername(paramString1);
    localUser.setPassword(paramString2);
    localUser.setFirstName(paramString3);
    localUser.setLastName(paramString4);
    userRegister(localUser, paramRST_CallbackItem);
  }
  
  public int userTimezoneOffset()
  {
    return new GregorianCalendar().getTimeZone().getOffset(System.currentTimeMillis()) / 60000;
  }
  
  public boolean validNightQuestions()
  {
    RST_NightQuestions localRST_NightQuestions = this.user.getNightQuestions();
    long l = new Date().getTime();
    if ((localRST_NightQuestions != null) && (localRST_NightQuestions.getQuestions() != null) && (localRST_NightQuestions.getQuestions().size() > 0) && (l - getNigthQuestionsTimestamp() < 28800000L)) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RefreshModelController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
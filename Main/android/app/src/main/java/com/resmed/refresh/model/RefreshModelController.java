package com.resmed.refresh.model;

import android.app.*;

import com.resmed.refresh.model.json.Location;
import com.resmed.refresh.ui.uibase.app.*;
import com.google.android.gms.common.*;
import com.google.android.gms.maps.model.*;
import android.telephony.*;
import android.provider.*;
import com.resmed.refresh.net.security.*;
import java.security.*;
import android.content.*;
import de.greenrobot.dao.*;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.query.*;
import android.util.*;
import com.resmed.refresh.model.json.*;
import android.os.*;
import android.content.pm.*;
import com.resmed.refresh.model.mappers.*;
import com.resmed.refresh.net.*;
import com.resmed.refresh.net.http.*;
import android.database.sqlite.*;
import com.resmed.refresh.net.push.*;
import com.resmed.refresh.model.services.*;
import com.flurry.android.*;
import java.util.*;
import android.location.*;
import com.resmed.refresh.utils.*;
import com.resmed.refresh.ui.utils.*;
import com.resmed.refresh.model.mindclear.*;
import com.resmed.refresh.utils.Log;

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

	private boolean checkPlayServices(final Activity activity) {
		final int googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(RefreshApplication.getInstance().getApplicationContext());
		Log.d("com.resmed.refresh.push", "GooglePlayServices resultCode = " + googlePlayServicesAvailable);
		if (googlePlayServicesAvailable != 0) {
			if (GooglePlayServicesUtil.isUserRecoverableError(googlePlayServicesAvailable)) {
				GooglePlayServicesUtil.getErrorDialog(googlePlayServicesAvailable, activity, 9000).show();
			}
			else {
				Log.d("com.resmed.refresh.push", "GooglePlayServices This device is not supported.");
			}
			return false;
		}
		return true;
	}

	private void clearRSTKey() {
		this.userPreferencesData.removeStringConfigValue("com.resmed.refresh.model.rst_key_tag");
	}

	private void clearSessionToken() {
		this.userPreferencesData.removeStringConfigValue("com.resmed.refresh.model.session_token_tag");
	}

	private void clearUserId() {
		this.userPreferencesData.removeStringConfigValue("com.resmed.refresh.model.user_id_tag");
		Log.e("userID", "clearUserId:" + this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.user_id_tag"));
	}

	private RST_User createUserItem(final String s) {
		boolean useMetricUnits = true;
		final RST_User rst_User = new RST_User(s);
		rst_User.setFirstName("");
		rst_User.setFamilyName("");
		rst_User.setEmail("");
		rst_User.setPassword("");
		this.daoSession.getRST_UserDao().insert((Object)rst_User);
		rst_User.setAndSaveProfile(this.createUserProfileItem());
		final RST_Settings settingsItem = this.createSettingsItem();
		settingsItem.setPushNotifications(useMetricUnits);
		settingsItem.setLocationPermission(useMetricUnits);
		if (RefreshApplication.userMeasurementUnitMappingObj.getTemperatureUnit() != (useMetricUnits ? 1 : 0)) {
			useMetricUnits = false;
		}
		settingsItem.setUseMetricUnits(useMetricUnits);
		rst_User.setAndSaveSettings(settingsItem);
		final LatLng lastLocation = this.locationManager.getLastLocation();
		rst_User.setAndSaveLocation(this.createLocationItem((float)lastLocation.latitude, (float)lastLocation.longitude));
		rst_User.update();
		return rst_User;
	}

	private String generateDeviceID() {
		String upperCase = null;
		if (!false) {
			final TelephonyManager telephonyManager = (TelephonyManager)RefreshApplication.getInstance().getSystemService("phone");
			telephonyManager.getDeviceId();
			upperCase = new UUID(new StringBuilder().append(Settings$Secure.getString(RefreshApplication.getInstance().getContentResolver(), "android_id")).toString().hashCode(), new StringBuilder().append(telephonyManager.getDeviceId()).toString().hashCode() << 32).toString().toUpperCase();
		}
		return upperCase;
	}

	private String generateToken(final String s, final String s2) {
		try {
			return Sha.hash256(String.valueOf(s2) + s).toUpperCase();
		}
		catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			return "";
		}
	}

	private int getGMCAppVersion() {
		return this.userPreferencesData.getIntegerConfigValue("com.resmed.refresh.model.gmc_appversion_tag");
	}

	private String getGMCToken() {
		return this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.gmc_token_tag");
	}

	public static RefreshModelController getInstance() {
		synchronized (RefreshModelController.class) {
			if (RefreshModelController.controller == null) {
				(RefreshModelController.controller = new RefreshModelController()).setup();
			}
			return RefreshModelController.controller;
		}
	}

	private String getRSTKey() {
		return this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.rst_key_tag");
	}

	private String getRSTToken() {
		return this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.session_token_tag");
	}

	private String getRegistrationId(final Context context) {
		if (this.getGMCToken() == null) {
			this.setGMCToken("");
			this.setGMCAppVersion(0);
		}
		if (this.getGMCAppVersion() == RefreshApplication.getInstance().getAppVersion()) {
			return this.getGMCToken();
		}
		return "";
	}

	private String getUserId() {
		Log.e("userID", "getUserId:" + this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.user_id_tag"));
		return this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.user_id_tag");
	}

	public static boolean isPluggedIn() {
		boolean b = true;
		final int intExtra = RefreshApplication.getInstance().getApplicationContext().registerReceiver((BroadcastReceiver)null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("plugged", -1);
		if (intExtra != (b ? 1 : 0) && intExtra != 2) {
			b = false;
		}
		return b;
	}

	private List<RST_SleepSessionInfo> localSleepSessionsAccesor(final int n, final boolean b, final boolean b2, final Date date, final Date date2, final long n2, final boolean b3) {
		if (this.user == null) {
			return null;
		}
		final RST_SleepSessionInfoDao rst_SleepSessionInfoDao = this.daoSession.getRST_SleepSessionInfoDao();
		final ArrayList<WhereCondition> list = new ArrayList<WhereCondition>();
		list.add(RST_SleepSessionInfoDao$Properties.IdUser.eq((Object)this.user.getId()));
		list.add(RST_SleepSessionInfoDao$Properties.Id.lt((Object)1000000000L));
		if (date != null) {
			list.add(RST_SleepSessionInfoDao$Properties.StartTime.gt((Object)date));
		}
		if (date2 != null) {
			list.add(RST_SleepSessionInfoDao$Properties.StartTime.lt((Object)date2));
		}
		if (n2 > -1L) {
			list.add(RST_SleepSessionInfoDao$Properties.Id.eq((Object)n2));
		}
		if (b3) {
			list.add(RST_SleepSessionInfoDao$Properties.Completed.eq((Object)1));
		}
		final QueryBuilder queryBuilder = rst_SleepSessionInfoDao.queryBuilder();
		if (list.size() == 1) {
			queryBuilder.where((WhereCondition)list.get(0), new WhereCondition[0]);
		}
		else if (list.size() > 1) {
			queryBuilder.where((WhereCondition)list.remove(0), (WhereCondition[])list.toArray(new WhereCondition[list.size()]));
		}
		if (b) {
			if (!b2) {
				queryBuilder.orderAsc(new Property[] { RST_SleepSessionInfoDao$Properties.Id });
			}
			else {
				queryBuilder.orderDesc(new Property[] { RST_SleepSessionInfoDao$Properties.Id });
			}
		}
		if (n > 0) {
			queryBuilder.limit(n);
		}
		return (List<RST_SleepSessionInfo>)queryBuilder.list();
	}

	private RST_User localUserData() {
		final String userId = this.getUserId();
		if (userId != null) {
			this.setDeviceId();
			this.user = this.localUserDataForId(userId);
		}
		return this.user;
	}

	private void serviceForgotPassword(final String s, final RST_CallbackItem<RST_Response<RST_User>> rst_CallbackItem) {
		new RST_ServiceLogin(this.resMedServerAPI).userForgotPassword(s, (RST_CallbackItem)rst_CallbackItem);
	}

	private void serviceLast30DaysReport(final String s, final String s2, final boolean b, final RST_CallbackItem<RST_Response<Last30DaysReportResponse>> rst_CallbackItem) {
		new RST_ServiceLast30DaysReport(this.resMedServerAPI).getLast30DaysReport(s, s2, b, (RST_CallbackItem)rst_CallbackItem);
	}

	private void serviceLocaleMUnitMapping(final RST_CallbackItem<RST_Response<List<LocaleMUnitResponse>>> rst_CallbackItem) {
		new RST_ServiceLocaleMUnit(this.resMedServerAPI).getlocaleMUnitMapping((RST_CallbackItem)rst_CallbackItem);
	}

	private void servicePreSleepQuestions(final RST_CallbackItem<RST_Response<RST_NightQuestions>> rst_CallbackItem) {
		new RST_ServiceReferenceData(this.resMedServerAPI).getPreSleepQuestions((RST_CallbackItem)rst_CallbackItem);
	}

	private void serviceRegisterPushNotificationToken(final String s) {
		new Timer().schedule((TimerTask)new RefreshModelController.RefreshModelController$7(this, s), 3000L);
	}

	private void serviceRegisterPushNotificationTokenDelayed(final String s) {
		new RST_ServicePushNotifications(this.resMedServerAPI).registerPushToken(this.getDeviceId(), s);
	}

	private void serviceUpdateFeedback(final RST_AdviceItem rst_AdviceItem, final RST_CallbackItem<RST_Response<Object>> rst_CallbackItem) {
		new RST_ServiceAdvice(this.resMedServerAPI).updateFeedback(rst_AdviceItem, (RST_CallbackItem)rst_CallbackItem);
	}

	private void serviceUpdateUser(final RST_CallbackItem<RST_Response<RST_User>> rst_CallbackItem) {
		new RST_ServiceUser(this.resMedServerAPI).updateUserData(UserMapper.getUser(this.user), (RST_CallbackItem)rst_CallbackItem);
	}

	private void serviceUpdateUserProfile(final RST_CallbackItem<RST_Response<RST_UserProfile>> rst_CallbackItem) {
		new RST_ServiceUserProfile(this.resMedServerAPI).updateUserProfile(UserMapper.getProfile(this.user), (RST_CallbackItem)rst_CallbackItem);
	}

	private void serviceUserLogin(final String s, final String s2, final RST_CallbackItem<RST_Response<RST_User>> rst_CallbackItem) {
		new RST_ServiceLogin(this.resMedServerAPI).userLogin(s, s2, (RST_CallbackItem)rst_CallbackItem);
	}

	private void serviceUserRegister(final User user, final RST_CallbackItem<RST_Response<RST_User>> rst_CallbackItem) {
		new RST_ServiceRegister(this.resMedServerAPI).userRegister(user, (RST_CallbackItem)rst_CallbackItem);
	}

	private void setDeviceId() {
		if (this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.device_id_tag") == null) {
			this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.device_id_tag", this.generateDeviceID());
		}
	}

	private void setGMCAppVersion(final int n) {
		this.userPreferencesData.setIntegerConfigValue("com.resmed.refresh.model.gmc_appversion_tag", n);
	}

	private void setGMCToken(final String s) {
		this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.gmc_token_tag", s);
	}

	private void setLocationPermission(final boolean locationPermission) {
		this.user.getSettings().setLocationPermission(locationPermission);
		this.save();
	}

	private void setRSTKey(final String s) {
		this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.rst_key_tag", s);
	}

	private void setRSTToken(final String s) {
		this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.session_token_tag", s);
	}

	private void storeUserId(final String s) {
		Log.e("userID", "storeUserId:" + s);
		this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.user_id_tag", s);
	}

	public RST_User access(final String s, final String s2, final UserResponse userResponse) {
		return this.access(userResponse.getFirstName(), userResponse.getLastName(), s, s2, userResponse);
	}

	public RST_User access(final String firstName, final String familyName, final String s, final String s2, final UserResponse userResponse) {
		RST_User user = this.localUserDataForId(userResponse.getUserId());
		if (user == null) {
			user = this.createUserItem(userResponse.getUserId());
		}
		user.setEmail(s);
		this.setLastEmail(s);
		if (firstName != null && familyName != null) {
			user.setFirstName(firstName);
			user.setFamilyName(familyName);
		}
		this.user = user;
		final UserProfile userProfile = userResponse.getUserProfile();
		if (userProfile != null) {
			UserProfileMapper.processUserProfile(userProfile);
		}
		final String generateToken = this.generateToken(userResponse.getUserId(), s2);
		this.storeUserId(userResponse.getUserId());
		this.setRSTKey(userResponse.getRstKey());
		this.setRSTToken(generateToken);
		this.save();
		return this.user;
	}

	public void adviceForDay(final Date date, final RST_CallbackItem<RST_Response<List<RST_AdviceItem>>> rst_CallbackItem, final boolean b) {
		final List dateRangesForDay = RefreshTools.dateRangesForDay(date);
		if (b) {
			this.serviceGetLatestAdvice(dateRangesForDay.get(0), dateRangesForDay.get(1), rst_CallbackItem);
			return;
		}
		final List<RST_AdviceItem> localAdvicesForDate = this.localAdvicesForDate(date);
		if (b || localAdvicesForDate == null || localAdvicesForDate.size() == 0) {
			this.serviceGetLatestAdvice(dateRangesForDay.get(0), dateRangesForDay.get(1), rst_CallbackItem);
			return;
		}
		new Handler(Looper.getMainLooper()).post((Runnable)new RefreshModelController.RefreshModelController$4(this, (List)localAdvicesForDate, (RST_CallbackItem)rst_CallbackItem));
	}

	public void adviceForId(final long n, final RST_CallbackItem<RST_Response<RST_AdviceItem>> rst_CallbackItem) {
		final RST_AdviceItem localAdviceForId = this.localAdviceForId(n);
		if (localAdviceForId == null) {
			this.serviceGetAdviceForId(n, rst_CallbackItem);
			return;
		}
		new Handler(Looper.getMainLooper()).post((Runnable)new RefreshModelController.RefreshModelController$1(this, localAdviceForId, (RST_CallbackItem)rst_CallbackItem));
	}

	public void adviceForSessionId(final long n, final RST_CallbackItem<RST_Response<List<RST_AdviceItem>>> rst_CallbackItem) {
		new Handler(Looper.getMainLooper()).post((Runnable)new RefreshModelController.RefreshModelController$2(this, (RST_CallbackItem)rst_CallbackItem));
	}

	public void changeLocationUpdates(final boolean b) {
		this.setLocationPermission(b);
		this.locationManager.setLocationUpdatesListener(b);
	}

	public void clearAdviceHistory() {
		final Iterator<RST_AdviceItem> iterator = this.user.getAdvices().iterator();
		while (iterator.hasNext()) {
			iterator.next().delete();
		}
		this.user.resetAdvices();
	}

	public void clearDB() {
		this.daoSession.clear();
		this.user = this.getUser();
	}

	public void clearHistory() {
		this.clearSleepHistory();
		this.clearAdviceHistory();
	}

	public void clearSleepHistory() {
		final List sleepSessions = this.user.getSleepSessions();
		Log.d("com.resmed.refresh.model", String.valueOf(sleepSessions.size()) + " sleep records to delete");
		for (final RST_SleepSessionInfo rst_SleepSessionInfo : sleepSessions) {
			final String string = "Sleep record " + rst_SleepSessionInfo.getId();
			rst_SleepSessionInfo.delete();
			Log.d("com.resmed.refresh.model", String.valueOf(string) + " deleted");
		}
		this.user.resetSleepSessions();
	}

	public RST_AdviceItem createAdviceItem(final long n) {
		final RST_AdviceItem rst_AdviceItem = new RST_AdviceItem(n);
		rst_AdviceItem.setTimestamp(new Date());
		rst_AdviceItem.setContent("");
		rst_AdviceItem.setDetail("");
		rst_AdviceItem.setHeader("");
		rst_AdviceItem.setHtmlContent("");
		rst_AdviceItem.setIcon("");
		rst_AdviceItem.setType("");
		rst_AdviceItem.setSubtitle("");
		rst_AdviceItem.setArticleUrl("");
		rst_AdviceItem.setFeedback(-1);
		rst_AdviceItem.setRead(false);
		this.user.addAdviceValue(rst_AdviceItem);
		rst_AdviceItem.setAndSaveHypnogramInfo(this.createSleepEventItem());
		rst_AdviceItem.update();
		return rst_AdviceItem;
	}

	public RST_EnvironmentalInfo createEnvironmentalInfo() {
		final RST_EnvironmentalInfo rst_EnvironmentalInfo = new RST_EnvironmentalInfo();
		rst_EnvironmentalInfo.setup();
		this.daoSession.getRST_EnvironmentalInfoDao().insert((Object)rst_EnvironmentalInfo);
		return rst_EnvironmentalInfo;
	}

	public RST_LocationItem createLocationItem(final float latitude, final float longitude) {
		final RST_LocationItem rst_LocationItem = new RST_LocationItem();
		rst_LocationItem.setLatitude(latitude);
		rst_LocationItem.setLongitude(longitude);
		Log.d("com.resmed.refresh.sleepFragment", " LocationMapper:: RefreshModelController::createLocationItem, offset : " + this.userTimezoneOffset());
		rst_LocationItem.setTimezone(this.userTimezoneOffset());
		rst_LocationItem.setLocale(Locale.getDefault().getISO3Language());
		return rst_LocationItem;
	}

	public RST_NightQuestions createNightQuestionsItem() {
		if (this.user.getNightQuestions() != null) {
			this.user.getNightQuestions().delete();
		}
		final RST_NightQuestions nightQuestions = new RST_NightQuestions();
		this.daoSession.getRST_NightQuestionsDao().insert((Object)nightQuestions);
		this.user.setNightQuestions(nightQuestions);
		return nightQuestions;
	}

	public RST_QuestionItem createQuestionItem(final int questionId) {
		final RST_QuestionItem rst_QuestionItem = new RST_QuestionItem();
		rst_QuestionItem.setId((long)questionId);
		rst_QuestionItem.setQuestionId(questionId);
		rst_QuestionItem.setText("");
		this.user.getNightQuestions().addQuestionItem(rst_QuestionItem);
		return rst_QuestionItem;
	}

	public RST_Settings createSettingsItem() {
		final RST_Settings rst_Settings = new RST_Settings();
		rst_Settings.setTac1(true);
		rst_Settings.setTac2(true);
		rst_Settings.setTac3(true);
		return rst_Settings;
	}

	public RST_SleepEvent createSleepEventItem() {
		return new RST_SleepEvent();
	}

	public RST_SleepSessionInfo createSleepSessionInfo() {
		return this.createSleepSessionInfo((new Date().getTime() - 978307200000L) / 1000L);
	}

	public RST_SleepSessionInfo createSleepSessionInfo(final long n) {
		final RST_SleepSessionInfo rst_SleepSessionInfo = new RST_SleepSessionInfo(n);
		rst_SleepSessionInfo.setup();
		final RST_EnvironmentalInfo environmentalInfo = this.createEnvironmentalInfo();
		rst_SleepSessionInfo.setEnvironmentalInfo(environmentalInfo);
		environmentalInfo.setIdSession(n);
		rst_SleepSessionInfo.addNightQuestions(this.localNightQuestions());
		rst_SleepSessionInfo.setStartTime(new Date());
		rst_SleepSessionInfo.setCompleted(false);
		rst_SleepSessionInfo.setUploaded(false);
		this.user.addSleepSessionInfo(rst_SleepSessionInfo);
		return rst_SleepSessionInfo;
	}

	public RST_UserProfile createUserProfileItem() {
		final RST_UserProfile rst_UserProfile = new RST_UserProfile();
		rst_UserProfile.setDateOfBirth(new Date(0L));
		return rst_UserProfile;
	}

	public RST_ValueItem createValueItem(final int n, final int ordr) {
		final RST_ValueItem rst_ValueItem = new RST_ValueItem();
		rst_ValueItem.setValue((float)n);
		rst_ValueItem.setOrdr(ordr);
		return rst_ValueItem;
	}

	public RST_User debugCreateUserItem() {
		this.user = this.createUserItem(UUID.randomUUID().toString());
		this.save();
		this.storeUserId(this.user.getId());
		return this.user;
	}

	public void debugResetAll() {
		if (this.user != null) {
			this.user.delete();
		}
		this.userPreferencesData.clearAll();
	}

	public Boolean getActiveAlarm() {
		try {
			return this.userPreferencesData.getBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.active_alarm");
		}
		catch (NullPointerException ex) {
			return false;
		}
	}

	public boolean getActiveRelax() {
		return this.userPreferencesData.getBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.active_relax");
	}

	public Long getAlarmDateTime() {
		return this.userPreferencesData.getLongUserValue(this.user.getId(), "com.resmed.refresh.model.time_alarm_date");
	}

	public boolean getAlarmIsPlaying() {
		return this.userPreferencesData.getBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.alarm_is_playing");
	}

	public boolean getAlarmReboot() {
		return this.userPreferencesData.getBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.time_alarm_reboot");
	}

	public int getAlarmRepetition() {
		return this.userPreferencesData.getIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.repeat_alarm");
	}

	public int getAlarmSound() {
		return this.userPreferencesData.getIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.sound_alarm");
	}

	public int getAlarmWindow() {
		return this.userPreferencesData.getIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.window_alarm");
	}

	public String getAuthentication() {
		final String string = new StringBuilder().append(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000L).toString();
		final String rstKey = this.getRSTKey();
		final String rstToken = this.getRSTToken();
		final String userId = this.getUserId();
		if (rstKey == null || rstToken == null || userId == null) {
			return null;
		}
		String hmacSha256 = "";
		while (true) {
			try {
				hmacSha256 = Sha.hmacSha256(rstKey, String.valueOf(rstToken) + string);
				final String trim = ("Basic " + Base64.encodeToString((String.valueOf(userId) + ":" + hmacSha256 + ":" + string).getBytes(), 0)).replaceAll("\n", "").trim();
				Log.e("userID", "getAuthentication:" + trim);
				return trim;
			}
			catch (Exception ex) {
				ex.printStackTrace();
				continue;
			}
			break;
		}
	}

	public String getBedId() {
		return this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.bedserial");
	}

	public String getBoardVersion() {
		String stringConfigValue = this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.bed_board_version_tag");
		if (stringConfigValue == null || stringConfigValue.length() == 0) {
			stringConfigValue = "revX";
		}
		return stringConfigValue;
	}

	public String getClearMindData() {
		return this.userPreferencesData.getStringUserValue(this.getUserId(), "com.resmed.refresh.model.clear_mind");
	}

	public int getDefaultBrightness() {
		return this.userPreferencesData.getIntegerConfigValue("com.resmed.refresh.model.default_brigthness_value_tag");
	}

	public String getDeviceId() {
		this.setDeviceId();
		return this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.device_id_tag");
	}

	public boolean getExpansionValidated() {
		return this.userPreferencesData.getBooleanConfigValue("com.resmed.refresh.model.expansion_validated_tag");
	}

	public String getFirmwareVersion() {
		String stringConfigValue = this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.bed_firmware_version_tag");
		if (stringConfigValue == null || stringConfigValue.length() == 0) {
			stringConfigValue = "x.x";
		}
		return stringConfigValue;
	}

	public boolean getGMCTokenStatus() {
		return this.userPreferencesData.getBooleanConfigValue("com.resmed.refresh.model.gmc_token_status_tag");
	}

	public boolean getHasBeenLogout() {
		return this.userPreferencesData.getBooleanConfigValue("com.resmed.refresh.model.user_has_been_logout_tag");
	}

	public boolean getHasToValidateEmail() {
		return this.user != null && this.userPreferencesData.getBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.has_to_validate_email");
	}

	public String getLastEmail() {
		return this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.last_email_logged_in_tag");
	}

	public RST_LocationItem getLastLocation() {
		if (this.locationManager != null && this.user != null) {
			final LatLng lastLocation = this.locationManager.getLastLocation();
			final RST_LocationItem location = this.user.getLocation();
			location.setLatitude((float)lastLocation.latitude);
			location.setLongitude((float)lastLocation.longitude);
			this.user.setAndSaveLocation(location);
			return location;
		}
		return null;
	}

	public void getLocaleMUnitMapping(final RST_CallbackItem<RST_Response<List<LocaleMUnitResponse>>> rst_CallbackItem) {
		this.serviceLocaleMUnitMapping(rst_CallbackItem);
	}

	public RST_LocationManager getLocationManager() {
		return this.locationManager;
	}

	public boolean getLocationPermission() {
		return this.user != null && this.isLoggedIn() && this.user.getSettings().getLocationPermission();
	}

	public long getNigthQuestionsTimestamp() {
		return this.userPreferencesData.getLongConfigValue("com.resmed.refresh.model.night_questions_timestamp_tag", 0L);
	}

	public boolean getPlayAutoRelax() {
		return this.userPreferencesData.getBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.play_auto_relax");
	}

	public String getRM20LibraryVersion() {
		String stringConfigValue = this.userPreferencesData.getStringConfigValue("com.resmed.refresh.model.rm20_library_version_tag");
		if (stringConfigValue == null || stringConfigValue.length() == 0) {
			stringConfigValue = "x.x";
		}
		return stringConfigValue;
	}

	public Integer getRelaxSound() {
		return this.userPreferencesData.getIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.sound_relax");
	}

	public ResMedServerAPI getResMedServerAPI() {
		return this.resMedServerAPI;
	}

	public String getSmartAlarmRM20Token() {
		return this.userPreferencesData.getStringUserValue(this.user.getId(), "com.resmed.refresh.model.smart_alarm_rm20_token");
	}

	public String getSmartAlarmValidToken() {
		return this.userPreferencesData.getStringUserValue(this.user.getId(), "com.resmed.refresh.model.smart_alarm_valid_token");
	}

	public boolean getTermsAndConditions() {
		return this.user.getSettings().getTac1();
	}

	public boolean getUseMetricUnits() {
		return this.user.getSettings().getUseMetricUnits();
	}

	public boolean getUsePushNotifications() {
		return this.user.getSettings().getPushNotifications();
	}

	public RST_User getUser() {
		return this.user;
	}

	public String getUserSessionID() {
		if (this.user == null) {
			return "c63eb080-a864-11e3-a5e2-0800200c9a66";
		}
		return this.user.getId();
	}

	public boolean haveAdvice() {
		final List<RST_AdviceItem> localAdvices = this.localAdvices(1);
		return localAdvices != null && localAdvices.size() > 0;
	}

	public void historicalSleepRecords(final Date date, final Date date2, final RST_CallbackItem<RST_Response<List<RST_SleepSessionInfo>>> rst_CallbackItem, final boolean b) {
		final List<RST_SleepSessionInfo> localSleepSessionsBetweenDates = this.localSleepSessionsBetweenDates(date, date2);
		if (b || localSleepSessionsBetweenDates == null || localSleepSessionsBetweenDates.size() == 0) {
			this.serviceGetHistoricalRecords(date, date2, (RST_CallbackItem<RST_Response<List<RST_SleepSessionInfo>>>)new RefreshModelController.RefreshModelController$5(this, date, date2, (RST_CallbackItem)rst_CallbackItem));
			return;
		}
		new Handler(Looper.getMainLooper()).post((Runnable)new RefreshModelController.RefreshModelController$6(this, (List)localSleepSessionsBetweenDates, (RST_CallbackItem)rst_CallbackItem));
	}

	public boolean isFirstLaunch() {
		boolean b = true;
		final boolean booleanConfigValue = this.userPreferencesData.getBooleanConfigValue("com.resmed.refresh.model.first_launch");
		this.userPreferencesData.setBooleanConfigValue("com.resmed.refresh.model.first_launch", b);
		if (booleanConfigValue) {
			b = false;
		}
		return b;
	}

	public boolean isLoggedIn() {
		return this.getRSTToken() != null && this.getRSTToken().length() > 0;
	}

	public boolean isProfileUpdatePending() {
		return this.userPreferencesData.getBooleanConfigValue("com.resmed.refresh.model.profile_update_pending_tag");
	}

	public void latestAdviceList(final RST_CallbackItem<RST_Response<List<RST_AdviceItem>>> rst_CallbackItem, final boolean b) {
		final List<RST_AdviceItem> localAdvices = this.localAdvices(4);
		if (b || localAdvices == null || localAdvices.size() == 0) {
			this.synchroniseLatestAdvice(rst_CallbackItem);
			return;
		}
		new Handler(Looper.getMainLooper()).post((Runnable)new RefreshModelController.RefreshModelController$3(this, (List)localAdvices, (RST_CallbackItem)rst_CallbackItem));
	}

	public void latestNightQuestions(final RST_CallbackItem<RST_Response<RST_NightQuestions>> rst_CallbackItem) {
		this.servicePreSleepQuestions(rst_CallbackItem);
	}

	public RST_AdviceItem localAdviceForId(final long n) {
		return (RST_AdviceItem)this.daoSession.getRST_AdviceItemDao().queryBuilder().where(RST_AdviceItemDao$Properties.IdUser.eq((Object)this.getUserId()), new WhereCondition[] { RST_AdviceItemDao$Properties.Id.eq((Object)n) }).unique();
	}

	public List<RST_AdviceItem> localAdvices() {
		return this.localAdvices(Integer.MAX_VALUE);
	}

	public List<RST_AdviceItem> localAdvices(final int n) {
		return (List<RST_AdviceItem>)this.daoSession.getRST_AdviceItemDao().queryBuilder().where(RST_AdviceItemDao$Properties.IdUser.eq((Object)this.getUserId()), new WhereCondition[0]).orderDesc(new Property[] { RST_AdviceItemDao$Properties.Timestamp }).limit(n).list();
	}

	public List<RST_AdviceItem> localAdvicesForDate(final Date date) {
		final List dateRangesForDay = RefreshTools.dateRangesForDay(date);
		final QueryBuilder queryBuilder = this.daoSession.getRST_AdviceItemDao().queryBuilder();
		queryBuilder.where(RST_AdviceItemDao$Properties.IdUser.eq((Object)this.user.getId()), new WhereCondition[] { queryBuilder.and(RST_AdviceItemDao$Properties.Timestamp.gt(dateRangesForDay.get(0)), RST_AdviceItemDao$Properties.Timestamp.lt(dateRangesForDay.get(1)), new WhereCondition[0]) });
		return (List<RST_AdviceItem>)queryBuilder.list();
	}

	public List<RST_AdviceItem> localAdvicesForSession(final long n) {
		final QueryBuilder queryBuilder = this.daoSession.getRST_AdviceItemDao().queryBuilder();
		queryBuilder.where(RST_AdviceItemDao$Properties.IdUser.eq((Object)this.user.getId()), new WhereCondition[] { RST_AdviceItemDao$Properties.SessionId.eq((Object)n) });
		return (List<RST_AdviceItem>)queryBuilder.list();
	}

	public List<RST_AdviceItem> localAdvicesNotRead() {
		return (List<RST_AdviceItem>)this.daoSession.getRST_AdviceItemDao().queryBuilder().where(RST_AdviceItemDao$Properties.IdUser.eq((Object)this.getUserId()), new WhereCondition[] { RST_AdviceItemDao$Properties.Read.eq((Object)false) }).list();
	}

	public RST_AdviceItem localLatestAdvice() {
		final List list = this.daoSession.getRST_AdviceItemDao().queryBuilder().where(RST_AdviceItemDao$Properties.IdUser.eq((Object)this.getUserId()), new WhereCondition[0]).orderDesc(new Property[] { RST_AdviceItemDao$Properties.Timestamp }).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public RST_SleepSessionInfo localLatestSleepSession() {
		final List<RST_SleepSessionInfo> localSleepSessionsAccesor = this.localSleepSessionsAccesor(1, true, true, null, null, -1L, true);
		if (localSleepSessionsAccesor == null || localSleepSessionsAccesor.size() == 0) {
			return null;
		}
		return localSleepSessionsAccesor.get(0);
	}

	public RST_SleepSessionInfo localLatestUnfinishedSession() {
		final RST_SleepSessionInfoDao rst_SleepSessionInfoDao = this.daoSession.getRST_SleepSessionInfoDao();
		final ArrayList<WhereCondition> list = new ArrayList<WhereCondition>();
		list.add(RST_SleepSessionInfoDao$Properties.IdUser.eq((Object)this.user.getId()));
		list.add(RST_SleepSessionInfoDao$Properties.Id.lt((Object)1000000000L));
		list.add(RST_SleepSessionInfoDao$Properties.Completed.eq((Object)0));
		final QueryBuilder queryBuilder = rst_SleepSessionInfoDao.queryBuilder();
		queryBuilder.where((WhereCondition)list.remove(0), (WhereCondition[])list.toArray(new WhereCondition[list.size()]));
		queryBuilder.orderDesc(new Property[] { RST_SleepSessionInfoDao$Properties.StartTime });
		final List list2 = queryBuilder.list();
		if (list2 != null && list2.size() != 0) {
			return list2.get(0);
		}
		return null;
	}

	public RST_SleepSessionInfo localNextSleepSession(final Date date) {
		final List<RST_SleepSessionInfo> localSleepSessionsBetweenDates = this.localSleepSessionsBetweenDates(date, new Date(Long.MAX_VALUE));
		if (localSleepSessionsBetweenDates == null || localSleepSessionsBetweenDates.size() == 0) {
			return null;
		}
		return localSleepSessionsBetweenDates.get(0);
	}

	public RST_NightQuestions localNightQuestions() {
		return this.user.getNightQuestions();
	}

	public RST_SleepSessionInfo localPreviousSleepSession(final Date date) {
		final List<RST_SleepSessionInfo> localSleepSessionsBetweenDates = this.localSleepSessionsBetweenDates(new Date(0L), date);
		if (localSleepSessionsBetweenDates == null || localSleepSessionsBetweenDates.size() == 0) {
			return null;
		}
		return localSleepSessionsBetweenDates.get(-1 + localSleepSessionsBetweenDates.size());
	}

	public List<RST_SleepSessionInfo> localSessionsForUpload() {
		final RST_SleepSessionInfoDao rst_SleepSessionInfoDao = this.daoSession.getRST_SleepSessionInfoDao();
		final ArrayList<WhereCondition> list = new ArrayList<WhereCondition>();
		list.add(RST_SleepSessionInfoDao$Properties.IdUser.eq((Object)this.user.getId()));
		list.add(RST_SleepSessionInfoDao$Properties.Id.lt((Object)1000000000L));
		list.add(RST_SleepSessionInfoDao$Properties.Completed.eq((Object)1));
		list.add(RST_SleepSessionInfoDao$Properties.Uploaded.eq((Object)0));
		final QueryBuilder queryBuilder = rst_SleepSessionInfoDao.queryBuilder();
		queryBuilder.where((WhereCondition)list.remove(0), (WhereCondition[])list.toArray(new WhereCondition[list.size()]));
		queryBuilder.orderDesc(new Property[] { RST_SleepSessionInfoDao$Properties.StartTime });
		return (List<RST_SleepSessionInfo>)queryBuilder.list();
	}

	public RST_SleepSessionInfo localSleepSessionForId(final long n) {
		final List<RST_SleepSessionInfo> localSleepSessionsAccesor = this.localSleepSessionsAccesor(1, true, false, null, null, n, false);
		if (localSleepSessionsAccesor == null || localSleepSessionsAccesor.size() == 0) {
			return null;
		}
		return localSleepSessionsAccesor.get(0);
	}

	public RST_SleepSessionInfo localSleepSessionInDay(final Date date) {
		final List dateRangesForDay = RefreshTools.dateRangesForDay(date);
		final List<RST_SleepSessionInfo> localSleepSessionsBetweenDates = this.localSleepSessionsBetweenDates(dateRangesForDay.get(0), dateRangesForDay.get(1));
		if (localSleepSessionsBetweenDates == null || localSleepSessionsBetweenDates.size() == 0) {
			return null;
		}
		return localSleepSessionsBetweenDates.get(0);
	}

	public List<RST_SleepSessionInfo> localSleepSessionsAll() {
		return this.localSleepSessionsAccesor(-1, true, false, null, null, -1L, true);
	}

	public List<RST_SleepSessionInfo> localSleepSessionsBetweenDates(final Date date, final Date date2) {
		return this.localSleepSessionsAccesor(-1, true, false, date, date2, -1L, true);
	}

	public List<RST_SleepSessionInfo> localSleepSessionsBetweenDates(final Date date, final Date date2, final boolean b) {
		return this.localSleepSessionsAccesor(-1, true, b, date, date2, -1L, true);
	}

	public List<RST_SleepSessionInfo> localSleepSessionsInMonth(final int n, final int n2) {
		final GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.set(1, n2);
		gregorianCalendar.set(2, n);
		gregorianCalendar.set(5, 1);
		gregorianCalendar.set(11, 0);
		gregorianCalendar.set(12, 0);
		gregorianCalendar.set(13, 0);
		gregorianCalendar.set(14, 0);
		final Date time = gregorianCalendar.getTime();
		gregorianCalendar.set(5, 1 + gregorianCalendar.getActualMaximum(5));
		gregorianCalendar.add(13, 1);
		List<RST_SleepSessionInfo> localSleepSessionsBetweenDates = this.localSleepSessionsBetweenDates(time, gregorianCalendar.getTime(), true);
		if (localSleepSessionsBetweenDates == null || localSleepSessionsBetweenDates.size() == 0) {
			localSleepSessionsBetweenDates = null;
		}
		return localSleepSessionsBetweenDates;
	}

	public List<RST_AdviceItem> localTasks() {
		final String userId = this.getUserId();
		return (List<RST_AdviceItem>)this.daoSession.getRST_AdviceItemDao().queryBuilder().where(RST_UserDao.Properties.IdUser.eq((Object)userId), new WhereCondition[] { RST_AdviceItemDao$Properties.Type.eq((Object)RST_AdviceItem$AdviceType.kAdviceTypeTask.ordinal()) }).where(RST_AdviceItemDao$Properties.IdUser.eq((Object)userId), new WhereCondition[0]).orderDesc(new Property[] { RST_AdviceItemDao$Properties.Timestamp }).list();
	}

	public RST_User localUserDataForId(final String s) {
		if (s == null) {
			return null;
		}
		return (RST_User)this.daoSession.getRST_UserDao().queryBuilder().where(RST_UserDao.Properties.Id.eq((Object)s), new WhereCondition[0]).unique();
	}

	public List<RST_ValueItem> numberArrayFromValueSet(final List<Integer> list) {
		final ArrayList<RST_ValueItem> list2 = new ArrayList<RST_ValueItem>();
		for (int i = 0; i < list.size(); ++i) {
			final RST_ValueItem rst_ValueItem = new RST_ValueItem();
			rst_ValueItem.setOrdr(i);
			rst_ValueItem.setValue((float)list.get(i));
			list2.add(rst_ValueItem);
		}
		return list2;
	}

	public RST_SleepSessionInfo oldestSessionForUpload() {
		final List<RST_SleepSessionInfo> localSessionsForUpload = this.localSessionsForUpload();
		if (localSessionsForUpload == null || localSessionsForUpload.size() == 0) {
			return null;
		}
		return this.localSessionsForUpload().get(0);
	}

	public void registerPushNotificationToken(final String gmcToken) {
		this.setGMCToken(gmcToken);
		this.setGMCAppVersion(RefreshApplication.getInstance().getAppVersion());
		this.serviceRegisterPushNotificationToken(gmcToken);
	}

	public void save() {
		if (this.user != null) {
			this.daoSession.getRST_UserDao().update(this.user);
			this.daoSession.getRST_SettingsDao().update(this.user.getSettings());
			this.daoSession.getRST_UserProfileDao().update(this.user.getProfile());
			this.daoSession.getRST_LocationItemDao().update(this.user.getLocation());
			if (this.user.getAdvices() != null) {
				final Iterator<RST_AdviceItem> iterator = this.user.getAdvices().iterator();
				while (iterator.hasNext()) {
					this.daoSession.getRST_AdviceItemDao().update(iterator.next());
				}
			}
			this.user.resetAdvices();
			preSleepLog.addTrace("RefershModelController:save ******");
			if (this.user.getNightQuestions() != null) {
				this.daoSession.getRST_NightQuestionsDao().update((Object)this.user.getNightQuestions());
				for (final RST_QuestionItem rst_QuestionItem : this.user.getNightQuestions().getQuestions()) {
					preSleepLog.addTrace("RefershModelController:save QuestionItem Question= " + rst_QuestionItem.getText() + "QuestionItem Question index= " + rst_QuestionItem.getId() + " answer=" + rst_QuestionItem.getAnswer());
					this.daoSession.getRST_QuestionItemDao().update(rst_QuestionItem);
				}
				this.user.getNightQuestions().resetQuestions();
			}
			this.user.resetSleepSessions();
			System.out.println("##### in Save ::" + this.user.getSettings().getWeightUnit());
		}
	}

	public void saveBoardVersion(final String s) {
		this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.bed_board_version_tag", s);
	}

	public void saveFirmwareVersion(final String s) {
		this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.bed_firmware_version_tag", s);
	}

	public void saveRM20LibraryVersion(final String s) {
		this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.rm20_library_version_tag", s);
	}

	public void sendLast30DayReport(final String s, final String s2, final boolean b, final RST_CallbackItem<RST_Response<Last30DaysReportResponse>> rst_CallbackItem) {
		this.serviceLast30DaysReport(s, s2, b, rst_CallbackItem);
	}

	public boolean serviceDidLaunch() {
		return this.userPreferencesData.getBooleanConfigValue("com.resmed.refresh.model.service_did_launch_tag");
	}

	public void serviceEmailVerification(final RST_CallbackItem<RST_Response<Object>> rst_CallbackItem) {
		new RST_ServiceEmailVerification(this.resMedServerAPI).resendEmailVerification((RST_CallbackItem)rst_CallbackItem);
	}

	public void serviceGetAdviceForId(final long n, final RST_CallbackItem<RST_Response<RST_AdviceItem>> rst_CallbackItem) {
		new RST_ServiceAdvice(this.resMedServerAPI).getAdviceItem(n, (RST_CallbackItem)rst_CallbackItem);
	}

	public void serviceGetHistoricalRecords(final Date date, final Date date2, final RST_CallbackItem<RST_Response<List<RST_SleepSessionInfo>>> rst_CallbackItem) {
		new RST_ServiceSleepRecord(this.resMedServerAPI).getAllRecords(date, date2, false, (RST_CallbackItem)rst_CallbackItem);
	}

	public void serviceGetLatestAdvice(final Date date, final Date date2, final RST_CallbackItem<RST_Response<List<RST_AdviceItem>>> rst_CallbackItem) {
		new RST_ServiceAdvice(this.resMedServerAPI).adviceForDates(date, date2, (RST_CallbackItem)rst_CallbackItem);
	}

	public void serviceGetLatestRecord(final RST_CallbackItem<RST_Response<List<RST_SleepSessionInfo>>> rst_CallbackItem) {
		new RST_ServiceSleepRecord(this.resMedServerAPI).getLatestRecord((RST_CallbackItem)rst_CallbackItem);
	}

	public void serviceGetRecord(final long n, final RST_CallbackItem<RST_Response<RST_SleepSessionInfo>> rst_CallbackItem) {
		new RST_ServiceSleepRecord(this.resMedServerAPI).getRecord(n, (RST_CallbackItem)rst_CallbackItem);
	}

	public void serviceLaunch() {
		if (this.user == null || this.serviceDidLaunch()) {
			return;
		}
		final Context applicationContext = RefreshApplication.getInstance().getApplicationContext();
		final LaunchData launchData = new LaunchData();
		String versionName = "";
		while (true) {
			try {
				versionName = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0).versionName;
				launchData.setAppVersion(versionName);
				launchData.setDeviceModel(Build.MODEL);
				launchData.setDeviceName(Build.DEVICE);
				launchData.setDeviceOS("Android");
				launchData.setDeviceOSVersion(Build$VERSION.RELEASE);
				new RST_ServiceLaunch(this.resMedServerAPI).userAppLaunch(this.getDeviceId(), launchData);
			}
			catch (PackageManager$NameNotFoundException ex) {
				ex.printStackTrace();
				continue;
			}
			break;
		}
	}

	public void serviceUpdateFileRecord(final String s, final RST_SleepSessionInfo rst_SleepSessionInfo, final RST_CallbackItem<RST_Response<Object>> rst_CallbackItem) {
		new RST_ServiceFileUploader(this.resMedServerAPI).uploadFile(s, rst_SleepSessionInfo, (RST_CallbackItem)rst_CallbackItem);
	}

	public void serviceUpdateRecord(final RST_SleepSessionInfo rst_SleepSessionInfo, final RST_CallbackItem<RST_Response<Object>> rst_CallbackItem) {
		new RST_ServiceSleepRecord(this.resMedServerAPI).updateRecord(SleepSessionMapper.getRecord(rst_SleepSessionInfo, this.user.getNightQuestions()), (RST_CallbackItem)rst_CallbackItem);
	}

	public void setBedId(final String s) {
		this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.bedserial", s);
	}

	public void setDefaultBrightness(final int n) {
		this.userPreferencesData.setIntegerConfigValue("com.resmed.refresh.model.default_brigthness_value_tag", n);
	}

	public void setExpansionValidated(final boolean b) {
		this.userPreferencesData.setBooleanConfigValue("com.resmed.refresh.model.expansion_validated_tag", b);
	}

	public void setGMCTokenStatus(final boolean b) {
		this.userPreferencesData.setBooleanConfigValue("com.resmed.refresh.model.gmc_token_status_tag", b);
	}

	public void setHasBeenLogout(final boolean b) {
		this.userPreferencesData.setBooleanConfigValue("com.resmed.refresh.model.user_has_been_logout_tag", b);
	}

	public void setHasToValidateEmail(final boolean b) {
		if (this.user != null) {
			this.userPreferencesData.setBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.has_to_validate_email", b);
		}
	}

	public void setLastEmail(final String s) {
		this.userPreferencesData.setStringConfigValue("com.resmed.refresh.model.last_email_logged_in_tag", s);
	}

	public void setNigthQuestionsTimestamp(final long n) {
		this.userPreferencesData.setLongConfigValue("com.resmed.refresh.model.night_questions_timestamp_tag", n);
	}

	public void setProfileUpdatePending(final boolean b) {
		this.userPreferencesData.setBooleanConfigValue("com.resmed.refresh.model.profile_update_pending_tag", b);
	}

	public void setServiceDidLaunch(final boolean b) {
		this.userPreferencesData.setBooleanConfigValue("com.resmed.refresh.model.service_did_launch_tag", b);
	}

	public void setUseMetricUnits(final boolean useMetricUnits) {
		if (this.user != null) {
			this.user.getSettings().setUseMetricUnits(useMetricUnits);
			this.save();
		}
	}

	public void setUsePushNotifications(final boolean pushNotifications) {
		this.user.getSettings().setPushNotifications(pushNotifications);
		this.save();
	}

	public void setup() {
		final Context applicationContext = RefreshApplication.getInstance().getApplicationContext();
		this.resMedServerAPI = (ResMedServerAPI)new ResMedConnectorAPI((HttpConnector)new HttpDefaultConnector());
		this.userPreferencesData = new RefreshUserPreferencesData(applicationContext);
		this.locationManager = new RST_LocationManager();
		final SQLiteDatabase writableDatabase = new DaoMaster$DevOpenHelper(applicationContext, "refresh-db", (SQLiteDatabase$CursorFactory)null).getWritableDatabase();
		this.daoSession = new DaoMaster(writableDatabase).newSession();
		DaoMaster.createAllTables(writableDatabase, true);
		this.localUserData();
		this.serviceLaunch();
		this.locationManager.setLocationUpdatesListener(this.getLocationPermission());
	}

	public void setupNotifications(final Activity activity) {
		if (this.checkPlayServices(activity)) {
			final String registrationId = this.getRegistrationId((Context)activity);
			if (registrationId.equals("")) {
				new RegisterGMCThread().start();
			}
			else if (!this.getGMCTokenStatus()) {
				this.serviceRegisterPushNotificationToken(registrationId);
			}
		}
	}

	public void storeActiveAlarm(final Boolean b) {
		this.userPreferencesData.setBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.active_alarm", (boolean)b);
	}

	public void storeActiveRelax(final boolean b) {
		this.userPreferencesData.setBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.active_relax", b);
	}

	public void storeAlarmDateTime(final Long n) {
		this.userPreferencesData.setLongUserValue(this.user.getId(), "com.resmed.refresh.model.time_alarm_date", (long)n);
	}

	public void storeAlarmIsPlaying(final boolean b) {
		this.userPreferencesData.setBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.alarm_is_playing", b);
	}

	public void storeAlarmReboot(final boolean b) {
		this.userPreferencesData.setBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.time_alarm_reboot", b);
	}

	public void storeAlarmRepetition(final int n) {
		this.userPreferencesData.setIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.repeat_alarm", n);
	}

	public void storeAlarmSound(final Integer n) {
		this.userPreferencesData.setIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.sound_alarm", (int)n);
	}

	public void storeAlarmWindow(final int n) {
		this.userPreferencesData.setIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.window_alarm", n);
	}

	public void storeClearMindData(final String s) {
		this.userPreferencesData.setStringUserValue(this.getUserId(), "com.resmed.refresh.model.clear_mind", s);
	}

	public void storePlayAutoRelax(final boolean b) {
		this.userPreferencesData.setBooleanUserValue(this.user.getId(), "com.resmed.refresh.model.play_auto_relax", b);
	}

	public void storeRelaxSound(final Integer n) {
		this.userPreferencesData.setIntegerUserValue(this.user.getId(), "com.resmed.refresh.model.sound_relax", (int)n);
	}

	public void storeSmartAlarmRM20Token(final String s) {
		this.userPreferencesData.setStringUserValue(this.user.getId(), "com.resmed.refresh.model.smart_alarm_rm20_token", s);
	}

	public void storeSmartAlarmValidToken(final String s) {
		this.userPreferencesData.setStringUserValue(this.user.getId(), "com.resmed.refresh.model.smart_alarm_valid_token", s);
	}

	public void synchroniseLatestAdvice(final RST_CallbackItem<RST_Response<List<RST_AdviceItem>>> rst_CallbackItem) {
		final RST_ServiceAccountSync rst_ServiceAccountSync = new RST_ServiceAccountSync(this.resMedServerAPI);
		final RST_AdviceItem localLatestAdvice = this.localLatestAdvice();
		int n;
		if (localLatestAdvice == null) {
			n = 0;
		}
		else {
			n = (int)localLatestAdvice.getId();
		}
		rst_ServiceAccountSync.syncLatestAdvice(n, (RST_CallbackItem)rst_CallbackItem);
	}

	public void synchroniseLatestAll(final RST_CallbackItem<RST_Response<Object>> rst_CallbackItem, final boolean b) {
		final RST_SleepSessionInfo localLatestSleepSession = this.localLatestSleepSession();
		int n;
		if (localLatestSleepSession == null) {
			n = 0;
		}
		else {
			n = (int)localLatestSleepSession.getId();
		}
		final long n2 = n;
		final RST_AdviceItem localLatestAdvice = this.localLatestAdvice();
		if (b || localLatestAdvice == null || localLatestSleepSession == null) {
			new RST_ServiceAccountSync(this.resMedServerAPI).syncLatestAll(n2, (RST_CallbackItem)rst_CallbackItem);
			return;
		}
		new Handler(Looper.getMainLooper()).post((Runnable)new RefreshModelController.RefreshModelController$8(this, (RST_CallbackItem)rst_CallbackItem));
	}

	public void synchroniseLatestSleepSessions(final RST_CallbackItem<RST_Response<List<RST_SleepSessionInfo>>> rst_CallbackItem) {
		final RST_ServiceAccountSync rst_ServiceAccountSync = new RST_ServiceAccountSync(this.resMedServerAPI);
		final RST_SleepSessionInfo localLatestSleepSession = this.localLatestSleepSession();
		long id;
		if (localLatestSleepSession == null) {
			id = 0L;
		}
		else {
			id = localLatestSleepSession.getId();
		}
		rst_ServiceAccountSync.syncLatestSleepRecord(id, (RST_CallbackItem)rst_CallbackItem);
	}

	public void synchroniseLatestUserProfile(final RST_CallbackItem<RST_Response<RST_UserProfile>> rst_CallbackItem) {
		new RST_ServiceAccountSync(this.resMedServerAPI).syncLatestUserProfile((RST_CallbackItem)rst_CallbackItem);
	}

	public void updateFeedback(final RST_AdviceItem rst_AdviceItem, final RST_CallbackItem<RST_Response<Object>> rst_CallbackItem) {
		this.serviceUpdateFeedback(rst_AdviceItem, rst_CallbackItem);
	}

	public void updateFlurryEvents(final String s) {
		final HashMap<String, String> hashMap = new HashMap<String, String>();
		if (RefreshApplication.is64bitdevice) {
			hashMap.put("DeviceArchitecture", "64bit_androidDevice");
		}
		else {
			hashMap.put("DeviceArchitecture", "32bit_androidDevice");
		}
		if (Consts.USE_FLURRY_REPORTS) {
			FlurryAgent.logEvent(s, (Map)hashMap, true);
			FlurryAgent.endTimedEvent(s);
		}
	}

	public void updateLocation() {
		this.locationManager.updateLocation();
	}

	public void updateLocationUser(final Location location) {
		AppFileLog.addTrace("Location - updateLocationUser user:" + this.user + " location:" + location);
		if (this.user != null && location != null) {
			RST_LocationItem andSaveLocation = this.user.getLocation();
			if (andSaveLocation == null) {
				andSaveLocation = this.createLocationItem(-1.0f, -1.0f);
			}
			andSaveLocation.setLocale(Locale.getDefault().getISO3Language());
			andSaveLocation.setLatitude((float)location.getLatitude());
			andSaveLocation.setLongitude((float)location.getLongitude());
			andSaveLocation.setTimezone(this.userTimezoneOffset());
			this.user.setAndSaveLocation(andSaveLocation);
			this.daoSession.getRST_UserDao().update(this.user);
			AppFileLog.addTrace("Location - Controller set location to (" + andSaveLocation.getLatitude() + "," + andSaveLocation.getLongitude() + ")");
			Log.d("com.resmed.refresh.model", "Location of user " + this.user.getId() + " updated to (" + location.getLatitude() + ", " + location.getLongitude() + ")");
		}
	}

	public void updateUser(final RST_CallbackItem<RST_Response<RST_User>> rst_CallbackItem) {
		this.daoSession.update((Object)this.user);
		this.serviceUpdateUser(rst_CallbackItem);
	}

	public void updateUserProfile(final RST_CallbackItem<RST_Response<RST_UserProfile>> rst_CallbackItem) {
		final Locale locale = RefreshApplication.getInstance().getApplicationContext().getResources().getConfiguration().locale;
		final String string = locale.toString();
		final String country = locale.getCountry();
		try {
			this.user.getProfile().setLocale(string);
			this.user.getProfile().setCountryCode(country);
			this.user.getSettings().setLocale(string);
			this.user.getSettings().setCountryCode(country);
			this.daoSession.update((Object)this.user);
			this.daoSession.update((Object)this.user.getProfile());
			System.out.println("###### updateUserProfile : " + this.user.getSettings().getWeightUnit());
			this.serviceUpdateUserProfile(rst_CallbackItem);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void userForgotPassword(final String s, final RST_CallbackItem<RST_Response<RST_User>> rst_CallbackItem) {
		this.serviceForgotPassword(s, rst_CallbackItem);
	}

	public void userLogin(final String s, final String s2, final RST_CallbackItem<RST_Response<RST_User>> rst_CallbackItem) {
		this.serviceUserLogin(s, s2, rst_CallbackItem);
	}

	public void userLogout() {
		if (RefreshModelController.controller != null && RefreshModelController.controller.getUser() != null) {
			final RST_NightQuestions nightQuestions = RefreshModelController.controller.getUser().getNightQuestions();
			if (nightQuestions != null) {
				nightQuestions.delete();
				RefreshModelController.controller.save();
			}
		}
		this.user = null;
		UserProfileDataManager.logout();
		MindClearManager.getInstance().logout();
		this.clearSessionToken();
		this.clearRSTKey();
		this.clearUserId();
	}

	public void userRegister(final User user, final RST_CallbackItem<RST_Response<RST_User>> rst_CallbackItem) {
		this.serviceUserRegister(user, rst_CallbackItem);
	}

	public void userRegister(final String s, final String password, final String firstName, final String lastName, final RST_CallbackItem<RST_Response<RST_User>> rst_CallbackItem) {
		final User user = new User();
		user.setEmail(s);
		user.setUsername(s);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		this.userRegister(user, rst_CallbackItem);
	}

	public int userTimezoneOffset() {
		return new GregorianCalendar().getTimeZone().getOffset(System.currentTimeMillis()) / 60000;
	}

	public boolean validNightQuestions() {
		final RST_NightQuestions nightQuestions = this.user.getNightQuestions();
		final long time = new Date().getTime();
		return nightQuestions != null && nightQuestions.getQuestions() != null && nightQuestions.getQuestions().size() > 0 && time - this.getNigthQuestionsTimestamp() < 28800000L;
	}
}

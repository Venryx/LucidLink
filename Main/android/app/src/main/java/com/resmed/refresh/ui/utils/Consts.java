package com.resmed.refresh.ui.utils;

import com.resmed.refresh.model.json.LocaleMUnitResponse;

public class Consts
{
  public static final String ACTIVITY_IS_MODAL = "com.resmed.refresh.ui.uibase.app.activity_modal";
  public static final long ACTIVITY_SERVICE_BOUND_TIMEOUT = 1200000L;
  public static final int AGE_DEFAULT = 44;
  public static final long ALARM_KEEP_APP_ALIVE_DELAY = 900000L;
  public static final long ALARM_RECONNECT_BLUETOOTH = 60000L;
  public static final int BED_FILL_BUFFER_FOR_SECONDS = 7200;
  public static final boolean BED_FILL_BUFFER_ON_OPEN_SESSION = false;
  public static final boolean BED_TRANSMIT_PACKET_KEEP_DATA = false;
  public static final int BIO_IGNORE_MAXIMUN = 4095;
  public static final boolean BLUETOOTH_MOCKING = false;
  public static final int BLUETOOTH_RECONNECT_POWERSAVE_DELAY = 600;
  public static final long BLUETOOTH_WAIT_AFTER_OTA = 5000L;
  public static final long BLUETOOTH_WAIT_BEFORE_RECONNECT = 15000L;
  public static final String BROADCAST_RM20_BREATH_RATE_ACTION = "rm20BreathRateAction";
  public static final String BROADCAST_RM20_WAKE_UP_ACTION = "rm20WakeUpAction";
  public static final String BROADCAST_SLEEP_STATE = "BROADCAST_SLEEP_STATE";
  public static final String BUNDLE_BED_AVAILABLE_DATA = "BUNDLE_BED_AVAILABLE_DATA";
  public static final String BUNDLE_BREATHING_RATE = "BUNDLE_BREATHING_RATE";
  public static final String BUNDLE_BREATHING_SECINDEX = "BUNDLE_BREATHING_SECINDEX";
  public static final String BUNDLE_LAST_CONN_STATE = "BUNDLE_LAST_CONN_STATE";
  public static final String BUNDLE_SLEEP_EPOCH_INDEX = "BUNDLE_SLEEP_EPOCH_INDEX";
  public static final String BUNDLE_SLEEP_STATE = "BUNDLE_SLEEP_STATE";
  public static final String BUNDLE_SMART_ALARM_ACTION = "BUNDLE_SMART_ALARM_ACTION";
  public static final String BUNDLE_SMART_ALARM_CALLER = "BUNDLE_SMART_ALARM_ACTION";
  public static final String BUNDLE_SMART_ALARM_TIMESTAMP = "BUNDLE_SMART_ALARM_TIMESTAMP";
  public static final String BUNDLE_SMART_ALARM_WINDOW = "BUNDLE_SMART_ALARM_WINDOW";
  public static final String CAME_FROM_HOMESCREEN = "com.resmed.refresh.ui.uibase.app.came_from_home";
  public static final String CAME_FROM_LANDING = "com.resmed.refresh.ui.uibase.app.came_from_landing";
  public static final String CAME_FROM_SETTINGS = "com.resmed.refresh.ui.uibase.app.came_from_settings";
  public static final String CAME_FROM_SLEEP_SESSION = "com.resmed.refresh.ui.uibase.app.came_from_sleepsession";
  public static final long CPU_TIME_ON_WAKE_LOCK = 10000L;
  public static final long DEFAULT_DELAY_BETWEEN_RPC_CALLS = 1000L;
  public static LocaleMUnitResponse DEFAULT_USER_MEASURMENT_MAPPING;
  public static final int DELAY_START_RECORDING_AMPLITUDE = 600000;
  public static final String EDITING_PROFILE = "com.resmed.refresh.ui.uibase.app.editing_profile";
  public static String EMAIL_BUG_REPORT;
  public static final int ENVIRONMENT = 5;
  public static final String[] ENVIRONMENTAL_NAMES = null;
  private static final String[][] ENVIRONMENTAL_URL = null;
  private static final int ENVIRONMENT_DEVELOPMENT = 0;
  private static final int ENVIRONMENT_PRODUCTION = 5;
  private static final int ENVIRONMENT_STAGE = 4;
  private static final int ENVIRONMENT_SWTEST = 2;
  private static final int ENVIRONMENT_SYSTEST = 1;
  private static final int ENVIRONMENT_UAT = 3;
  public static final String FIRMWARE_DEFAULT_VERSION_TO_USE = "1.0.5";
  public static String FLURRY_API_KEY;
  public static final boolean FORCE_OTA_UPGRADE = false;
  public static final String FORGOTTEN_PASSWORD_EMAIL = "com.resmed.refresh.consts.forgotten_password_email";
  public static final int GENDER_DEFAULT = 0;
  public static final String LAUNCH_FROM_ALARMSERVICE = "com.resmed.refresh.consts.launch_from_alarm_service";
  public static final int MIN_EDF_FILE_VALID_BYTE_SIZE = 12000;
  public static final int MIN_SAMPLES_TO_SAVE_RECORD;
  public static final long MIN_SECS_TO_SAVE_RECORD = -1;
  public static final int NAV_LEFT_RIGHT = 1;
  public static final int NAV_NO = -1;
  public static final int NAV_RIGHT_LEFT = 2;
  public static final String NOTIFICATION_ADVICEID = "com.resmed.refresh.ui.uibase.app.notification_advice_id";
  public static final int NO_SELECTION = -1;
  public static final String PREF_AGE = "PREF_AGE";
  public static final String PREF_DATA_ON_BED = "PREF_DATA_ON_BED_NOTIFICATION_ID";
  public static final String PREF_GENDER = "PREF_GENDER";
  public static final String PREF_LAST_RPC_ID_USED = "PREF_LAST_RPC_ID_USED";
  public static final String PREF_NIGHT_LAST_SESSION_ID = "PREF_NIGHT_LAST_SESSION_ID";
  public static final String PREF_NIGHT_LAST_TIMESTAMP = "PREF_NIGHT_LAST_TIMESTAMP_ID";
  public static final String PREF_NIGHT_TRACK_STATE = "PREF_CONNECTION_STATE";
  public static final String PREF_SESSION_BIO_COUNT = "PREF_SESSION_BIO_COUNT";
  public static final String PREF_SYNC_DATA_PROCESS = "PREF_SYNC_DATA_PROCESS";
  public static final int RECONNECTION_CODE = 1;
  public static final String RECOVERING_APP_FROM_SERVICE = "com.resmed.refresh.consts.recovering_app_from_service";
  public static final String RECOVERING_UI_THREAD_FROM_CRASH = "com.resmed.refresh.consts.recovering_ui_thread_from_crash";
  public static final String[] REFRESH_URL = null;
  public static int RELAX_TIME_DIVIDER = 0;
  public static final String RPC_STREAMING_MODE = "REAL";
  public static final int SERVICE_IPC_MAX_TRANSACTION = 1048000;
  public static final boolean SHOW_LOG;
  public static final String SLEEP_HISTORY_ID = "com.resmed.refresh.ui.uibase.app.sleep_history_id";
  public static final long SLEEP_SUB_TIMER_INTERVAL = 5000L;
  public static final long SLEEP_TIMER_INTERVAL = 120000L;
  public static final String SMART_ALARM_PLAYING = "com.resmed.refresh.consts.smart_alarm_playing";
  public static final String SMART_ALARM_TIME_VALUE = "com.resmed.refresh.consts.smart_alarm_time_value";
  public static final String SMART_ALARM_WINDOW_VALUE = "com.resmed.refresh.consts.smart_alarm_window_value";
  public static final String SPLUS_MENTOR_DAY = "com.resmed.refresh.ui.uibase.app.splus_menotr_day";
  public static final int SYNC_TIMER_RETRY = 3600000;
  public static final boolean TESTING = false;
  public static boolean TESTING_DIRAC = false;
  public static boolean TESTING_RELAX = false;
  public static boolean TEST_SHORT_RECORD = false;
  public static final int TIMEOUT_CONNECT_BED = 60000;
  public static final int TIMEOUT_SLEEP_SESSION = 1800;
  public static final int TRANSMIT_PACKET_ALL_SAMPLES = 65535;
  public static boolean UNIT_TEST_MODE = false;
  public static final int URL_INDEX_API_BASE = 0;
  public static final int URL_INDEX_EDF_ENDPOINTS = 5;
  public static final int URL_INDEX_FAQ = 3;
  public static final int URL_INDEX_MORE_INFO = 4;
  public static final int URL_INDEX_PRIVACY_AND_POLICY_LINK = 2;
  public static final int URL_INDEX_TERMS_AND_CONDITIONS = 1;
  public static final String USER_PRESS_BACK_TO_SLEEP = "com.resmed.refresh.consts.user_press_back_to_sleep_tag";
  public static boolean USE_ACRA_REPORTS = false;
  public static boolean USE_EXTERNAL_STORAGE = false;
  public static boolean USE_FLURRY_REPORTS = false;
  public static final int VAL = -1;
  public static final int VERSION_NUMBER_EXPANSION_FILES = 51;
  public static final boolean WRITE_BLUETOOTH_LOG;
  public static final boolean WRITE_LOG;
  public static final boolean WRITE_PRESLEEP_LOG;
  public static final String frameVideo = "<html><body style=\"background: transparent; padding:0;margin:0\"><div style=\"position: relative;\"> <iframe style=\"background: #000000; position: absolute; top:0; left:0; width=100%; height=100%\" width=\"320\" height=\"315\" src=\"https://www.youtube.com/embed/R2t0WAfClKo\" frameborder=\"0\" allowfullscreen></iframe></div></body></html>";
  
  static
  {
    int j = 1;
    UNIT_TEST_MODE = false;
    TESTING_RELAX = false;
    TESTING_DIRAC = false;
    TEST_SHORT_RECORD = false;
    USE_EXTERNAL_STORAGE = UNIT_TEST_MODE;
    USE_ACRA_REPORTS = false;
    USE_FLURRY_REPORTS = true;
    FLURRY_API_KEY = "SNRHPKG82CQN5ND2V74R";
    DEFAULT_USER_MEASURMENT_MAPPING = new LocaleMUnitResponse();
    SHOW_LOG = UNIT_TEST_MODE;
    WRITE_LOG = UNIT_TEST_MODE;
    WRITE_BLUETOOTH_LOG = UNIT_TEST_MODE;
    WRITE_PRESLEEP_LOG = UNIT_TEST_MODE;
    if (TESTING_RELAX)
    {
      i = 20;
      RELAX_TIME_DIVIDER = i;
      ENVIRONMENTAL_NAMES = new String[] { "DEVTEST", "SYSTEST", "SWTEST", "UAT", "STAGE", "PRODUCTION" };
      EMAIL_BUG_REPORT = "Bug_Report_Android@mysplus.com";
      String[] arrayOfString1 = { "https://refreshuserdataapi-systest.sleeprefresh.net/api/", "http://www.mysplus.com/Legal/TermsAndConditions", "http://www.mysplus.com/Legal/PrivacyPolicy", "http://www.mysplus.com/mobile/faq", "http://www.mysplus.com", "https://refreshbulkdataapi-systest.sleeprefresh.net/api/rawdata/" };
      String[] arrayOfString2 = { "https://refreshuserdataapi.sleeprefresh.net/api/", "http://www.mysplus.com/Legal/TermsAndConditions", "http://www.mysplus.com/Legal/PrivacyPolicy", "http://www.mysplus.com/mobile/faq", "http://www.mysplus.com", "https://refreshbulkdataapi.sleeprefresh.net/api/rawdata/" };
      ENVIRONMENTAL_URL = new String[][] { { "https://refreshuserdataapi-devtest.sleeprefresh.net/api/", "http://www.mysplus.com/Legal/TermsAndConditions", "http://www.mysplus.com/Legal/PrivacyPolicy", "http://www.mysplus.com/mobile/faq", "http://www.mysplus.com", "https://refreshbulkdataapi-devtest.sleeprefresh.net/api/rawdata/" }, arrayOfString1, { "https://refreshwebdashboard-swtest.sleeprefresh.net/api/", "http://www.mysplus.com/Legal/TermsAndConditions", "http://www.mysplus.com/Legal/PrivacyPolicy", "http://www.mysplus.com/mobile/faq", "http://www.mysplus.com", "https://refreshbulkdataapi-swtest.sleeprefresh.net/api/rawdata/" }, { "https://refreshuserdataapi-uat.sleeprefresh.net/api/", "http://www.mysplus.com/Legal/TermsAndConditions", "http://www.mysplus.com/Legal/PrivacyPolicy", "http://www.mysplus.com/mobile/faq", "http://www.mysplus.com", "https://refreshbulkdataapi-uat.sleeprefresh.net/api/rawdata/" }, { "https://refreshuserdataapi-stage.sleeprefresh.net/api/", "http://www.mysplus.com/Legal/TermsAndConditions", "http://www.mysplus.com/Legal/PrivacyPolicy", "http://www.mysplus.com/mobile/faq", "http://www.mysplus.com", "https://refreshbulkdataapi-staging.sleeprefresh.net/api/rawdata/" }, arrayOfString2 };
      REFRESH_URL = ENVIRONMENTAL_URL[5];
      if (!TEST_SHORT_RECORD) {
        break label449;
      }
      i = 1;
      label426:
      MIN_SECS_TO_SAVE_RECORD = i;
      if (!TEST_SHORT_RECORD) {
        break label456;
      }
    }
    label449:
    label456:
    for (int i = j;; i = 9600)
    {
      MIN_SAMPLES_TO_SAVE_RECORD = i;
      return;
      i = 1;
      break;
      i = 600;
      break label426;
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
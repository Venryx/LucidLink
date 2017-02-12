package com.resmed.refresh.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import java.util.Date;

public class RST_UserProfileDao
  extends AbstractDao<RST_UserProfile, Long>
{
  public static final String TABLENAME = "RST__USER_PROFILE";
  
  public RST_UserProfileDao(DaoConfig paramDaoConfig)
  {
    super(paramDaoConfig);
  }
  
  public RST_UserProfileDao(DaoConfig paramDaoConfig, DaoSession paramDaoSession)
  {
    super(paramDaoConfig, paramDaoSession);
  }
  
  public static void createTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (String str = "IF NOT EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE " + str + "'RST__USER_PROFILE' (" + "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + "'BED_TIME' INTEGER NOT NULL ," + "'CONSUME_ALCOHOL' INTEGER NOT NULL ," + "'DATE_OF_BIRTH' INTEGER NOT NULL ," + "'GENDER' INTEGER NOT NULL ," + "'EXERCISE_BEFORE_BED' INTEGER NOT NULL ," + "'EXERCISE_INTENSITY' INTEGER NOT NULL ," + "'HAS_SLEEP_PARTNER' INTEGER NOT NULL ," + "'HAS_PET' INTEGER NOT NULL ," + "'HAS_PET_IN_BED' INTEGER NOT NULL ," + "'HEIGHT' REAL NOT NULL ," + "'HIGH_BLOOD_PRESSURE' INTEGER NOT NULL ," + "'ILLNESS_FOR_SLEEP' INTEGER NOT NULL ," + "'MEDICATION' INTEGER NOT NULL ," + "'MEDICATION_FREQUENCY' INTEGER NOT NULL ," + "'NAPS' INTEGER NOT NULL ," + "'NECK_SIZE' INTEGER NOT NULL ," + "'NUM_CAFFEINE_DRINKS' INTEGER NOT NULL ," + "'NUM_CIGARETTES' INTEGER NOT NULL ," + "'NUM_DRINKS' INTEGER NOT NULL ," + "'NUM_EXERCISES' INTEGER NOT NULL ," + "'SLEEP_MOVER' INTEGER NOT NULL ," + "'SLEEP_PROBLEMS' INTEGER NOT NULL ," + "'SMOKER' INTEGER NOT NULL ," + "'SNORER' INTEGER NOT NULL ," + "'STOPS_BREATHING' INTEGER NOT NULL ," + "'STRESSED_FREQUENCY' INTEGER NOT NULL ," + "'TIRED_DAYTIME' INTEGER NOT NULL ," + "'WEIGHT' REAL NOT NULL ," + "'HEIGHT_UNIT' INTEGER NOT NULL, " + "'WEIGHT_UNIT' INTEGER NOT NULL," + "'TEMPERATURE_UNIT' INTEGER NOT NULL," + "'LOCALE' TEXT NOT NULL," + "'COUNTRY_CODE' TEXT NOT NULL," + "'PRESCRIPTIONPILLSTYPE' INTEGER NOT NULL," + "'PRESCRIPTIONPILLSCONCERN' INTEGER NOT NULL," + "'OTCMEDICATIONTYPE' INTEGER NOT NULL," + "'OTCMEDICATIONFREQUENCY' INTEGER NOT NULL," + "'OTCMEDICATIONCONCERN' INTEGER NOT NULL," + "'HERBALREMEDIESTYPE' INTEGER NOT NULL," + "'HERBALREMEDIESFREQUENCY' INTEGER NOT NULL," + "'CONTROLBEDROOMTEMPERATURE' INTEGER NOT NULL," + "'ISHUMIDIFIERUSED' INTEGER NOT NULL," + "'ISAIRPURIFIERUSED' INTEGER NOT NULL," + "'NOISELEVELINBEDROOM' INTEGER NOT NULL," + "'HOWOFTENELECTRONICSUSED' INTEGER NOT NULL," + "'GETWOKENUPINNIGHTBYANY' INTEGER NOT NULL," + "'ISSOUNDUSEDINSLEEP' INTEGER NOT NULL," + "'TYPEOFSOUNDUSEDINSLEEP' INTEGER NOT NULL," + "'LEVELOFLIGHTINBEDROOM' INTEGER NOT NULL," + "'SOURCEOFLIGHTINBEDROOM' INTEGER NOT NULL," + "'REDUCEAMOUNTOFLIGHTINBEDROOM' INTEGER NOT NULL," + "'SPECIALLIGHTUSEDINSLEEP' INTEGER NOT NULL," + "'MATTRESSTYPE' INTEGER NOT NULL," + "'MATTRESSSIZE' INTEGER NOT NULL," + "'MATTRESSBRAND' INTEGER NOT NULL," + "'MATTRESSAGE' INTEGER NOT NULL," + "'ISMATTRESSPADTOPPERONBED' INTEGER NOT NULL," + "'PILLOWSTYPE' INTEGER NOT NULL," + "'PILLOWSAGE' INTEGER NOT NULL," + "'SLEEPINGPOSITION' INTEGER NOT NULL," + "'WAKEUPDIFFICULTIES' INTEGER NOT NULL," + "'MORNINGALERTNESS' INTEGER NOT NULL," + "'TYPEOFPERSON' INTEGER NOT NULL," + "'TRAVELIMPACT' INTEGER NOT NULL," + "'CHILDRENCOUNT' INTEGER NOT NULL," + "'NAPTIME' INTEGER NOT NULL," + "'SLEEPDISORDER' INTEGER NOT NULL," + "'SLEEPDISORDERMACHINE' INTEGER NOT NULL," + "'MACHINEMANUFACTURER' INTEGER NOT NULL," + "'AFFECTSYOURSLEEP' INTEGER NOT NULL," + "'ISSTAYINGAWAKEDURINGDAY' INTEGER NOT NULL," + "'CONCERNSONSLEEPPROBLEM' INTEGER NOT NULL," + "'EXTENTINTERFERESLEEPPROBLEM' INTEGER NOT NULL," + "'SLEEPINGPOSITIONSNORE' INTEGER NOT NULL," + "'ISDRYMOUTH' INTEGER NOT NULL," + "'ISNASALCONGESTION' INTEGER NOT NULL," + "'ISBREATHINGOBSTRUCTION' INTEGER NOT NULL," + "'ISCRAWLINGSENSATION' INTEGER NOT NULL," + "'ISMUSCLETENSION' INTEGER NOT NULL," + "'ISLEGSSTILLATNIGHT' INTEGER NOT NULL," + "'ISSUFFERFROMALLERGIES' INTEGER NOT NULL," + "'ISSLEEPWITHPARTNER' INTEGER NOT NULL," + "'PETSLEEPWITHYOUONBED' INTEGER NOT NULL," + "'SNOREFREQUENCY' INTEGER NOT NULL," + "'USINGSLEEPAIDS' INTEGER NOT NULL," + "'PRESCRIPTIONPILLSFREQUENCY' INTEGER NOT NULL," + "'OTHERMACHINEMANUFACTURER' TEXT NOT NULL," + "'OTHERPRESCRIPTIONPILLSTYPE' TEXT NOT NULL," + "'OTHERHERBALREMEDIESTYPE' TEXT NOT NULL," + "'OTHERMATTRESSTYPE' TEXT NOT NULL," + "'OTHERMATTRESSBRAND' TEXT NOT NULL," + "'OTHERPILLOWSTYPE' TEXT NOT NULL," + "'OTHERCONTROLBEDROOMTEMPERATURE' TEXT NOT NULL," + "'OTHERTYPEOFSOUNDUSEDINSLEEP' TEXT NOT NULL," + "'OTHERSOURCEOFLIGHTINBEDROOM' TEXT NOT NULL," + "'OTHERREDUCEAMOUNTOFLIGHTINBEDROOM' TEXT NOT NULL," + "'NEWGENDER' TEXT NOT NULL" + ");");
      return;
    }
  }
  
  public static void dropTable(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder("DROP TABLE ");
    if (paramBoolean) {}
    for (String str = "IF EXISTS ";; str = "")
    {
      paramSQLiteDatabase.execSQL(str + "'RST__USER_PROFILE'");
      return;
    }
  }
  
  protected void bindValues(SQLiteStatement paramSQLiteStatement, RST_UserProfile paramRST_UserProfile)
  {
    long l2 = 1L;
    paramSQLiteStatement.clearBindings();
    Long localLong = paramRST_UserProfile.getId();
    if (localLong != null) {
      paramSQLiteStatement.bindLong(1, localLong.longValue());
    }
    paramSQLiteStatement.bindLong(2, paramRST_UserProfile.getBedTime());
    paramSQLiteStatement.bindLong(3, paramRST_UserProfile.getConsumeAlcohol());
    paramSQLiteStatement.bindLong(4, paramRST_UserProfile.getDateOfBirth().getTime());
    paramSQLiteStatement.bindLong(5, paramRST_UserProfile.getGender());
    paramSQLiteStatement.bindLong(6, paramRST_UserProfile.getExerciseBeforeBed());
    paramSQLiteStatement.bindLong(7, paramRST_UserProfile.getExerciseIntensity());
    paramSQLiteStatement.bindLong(8, paramRST_UserProfile.getHasSleepPartner());
    long l1;
    if (paramRST_UserProfile.getHasPet())
    {
      l1 = 1L;
      paramSQLiteStatement.bindLong(9, l1);
      if (!paramRST_UserProfile.getHasPetInBed()) {
        break label1193;
      }
      l1 = 1L;
      label128:
      paramSQLiteStatement.bindLong(10, l1);
      paramSQLiteStatement.bindDouble(11, paramRST_UserProfile.getHeight());
      paramSQLiteStatement.bindLong(12, paramRST_UserProfile.getHighBloodPressure());
      paramSQLiteStatement.bindLong(13, paramRST_UserProfile.getIllnessForSleep());
      paramSQLiteStatement.bindLong(14, paramRST_UserProfile.getMedication());
      paramSQLiteStatement.bindLong(15, paramRST_UserProfile.getMedicationFrequency());
      paramSQLiteStatement.bindLong(16, paramRST_UserProfile.getNaps());
      paramSQLiteStatement.bindLong(17, paramRST_UserProfile.getNeckSize());
      paramSQLiteStatement.bindLong(18, paramRST_UserProfile.getNumCaffeineDrinks());
      paramSQLiteStatement.bindLong(19, paramRST_UserProfile.getNumCigarettes());
      paramSQLiteStatement.bindLong(20, paramRST_UserProfile.getNumDrinks());
      paramSQLiteStatement.bindLong(21, paramRST_UserProfile.getNumExercises());
      paramSQLiteStatement.bindLong(22, paramRST_UserProfile.getSleepMover());
      paramSQLiteStatement.bindLong(23, paramRST_UserProfile.getSleepProblems());
      paramSQLiteStatement.bindLong(24, paramRST_UserProfile.getSmoker());
      paramSQLiteStatement.bindLong(25, paramRST_UserProfile.getSnorer());
      paramSQLiteStatement.bindLong(26, paramRST_UserProfile.getStopsBreathing());
      paramSQLiteStatement.bindLong(27, paramRST_UserProfile.getStressedFrequency());
      if (!paramRST_UserProfile.getTiredDaytime()) {
        break label1198;
      }
      l1 = l2;
      label332:
      paramSQLiteStatement.bindLong(28, l1);
      paramSQLiteStatement.bindDouble(29, paramRST_UserProfile.getWeight());
      paramSQLiteStatement.bindLong(30, paramRST_UserProfile.getHeightUnit());
      paramSQLiteStatement.bindLong(31, paramRST_UserProfile.getWeightUnit());
      paramSQLiteStatement.bindLong(32, paramRST_UserProfile.getTemperatureUnit());
      if (paramRST_UserProfile.getLocale() == null) {
        break label1203;
      }
      paramSQLiteStatement.bindString(33, paramRST_UserProfile.getLocale());
      label400:
      if (paramRST_UserProfile.getCountryCode() == null) {
        break label1214;
      }
      paramSQLiteStatement.bindString(34, paramRST_UserProfile.getCountryCode());
      label417:
      paramSQLiteStatement.bindLong(35, paramRST_UserProfile.getPrescriptionPillsType());
      paramSQLiteStatement.bindLong(36, paramRST_UserProfile.getPrescriptionPillsConcern());
      paramSQLiteStatement.bindLong(37, paramRST_UserProfile.getOTCMedicationType());
      paramSQLiteStatement.bindLong(38, paramRST_UserProfile.getOTCMedicationFrequency());
      paramSQLiteStatement.bindLong(39, paramRST_UserProfile.getOTCMedicationConcern());
      paramSQLiteStatement.bindLong(40, paramRST_UserProfile.getHerbalRemediesType());
      paramSQLiteStatement.bindLong(41, paramRST_UserProfile.getHerbalRemediesFrequency());
      paramSQLiteStatement.bindLong(42, paramRST_UserProfile.getControlBedroomTemperature());
      paramSQLiteStatement.bindLong(43, paramRST_UserProfile.isHumidifierUsed());
      paramSQLiteStatement.bindLong(44, paramRST_UserProfile.isAirPurifierUsed());
      paramSQLiteStatement.bindLong(45, paramRST_UserProfile.getNoiseLevelInBedroom());
      paramSQLiteStatement.bindLong(46, paramRST_UserProfile.getHowOftenElectronicsUsed());
      paramSQLiteStatement.bindLong(47, paramRST_UserProfile.getGetWokenUpInNightByAny());
      paramSQLiteStatement.bindLong(48, paramRST_UserProfile.isSoundUsedInSleep());
      paramSQLiteStatement.bindLong(49, paramRST_UserProfile.getTypeOfSoundUsedInSleep());
      paramSQLiteStatement.bindLong(50, paramRST_UserProfile.getLevelOfLightInBedroom());
      paramSQLiteStatement.bindLong(51, paramRST_UserProfile.getSourceOfLightInBedroom());
      paramSQLiteStatement.bindLong(52, paramRST_UserProfile.getReduceAmountOfLightInBedroom());
      paramSQLiteStatement.bindLong(53, paramRST_UserProfile.getSpecialLightUsedInSleep());
      paramSQLiteStatement.bindLong(54, paramRST_UserProfile.getMattressType());
      paramSQLiteStatement.bindLong(55, paramRST_UserProfile.getMattressSize());
      paramSQLiteStatement.bindLong(56, paramRST_UserProfile.getMattressBrand());
      paramSQLiteStatement.bindLong(57, paramRST_UserProfile.getMattressAge());
      paramSQLiteStatement.bindLong(58, paramRST_UserProfile.isMattressPadTopperOnBed());
      paramSQLiteStatement.bindLong(59, paramRST_UserProfile.getPillowsType());
      paramSQLiteStatement.bindLong(60, paramRST_UserProfile.getPillowsAge());
      paramSQLiteStatement.bindLong(61, paramRST_UserProfile.getSleepingPosition());
      paramSQLiteStatement.bindLong(62, paramRST_UserProfile.getWakeUpDifficulties());
      paramSQLiteStatement.bindLong(63, paramRST_UserProfile.getMorningAlertness());
      paramSQLiteStatement.bindLong(64, paramRST_UserProfile.getTypeofPerson());
      paramSQLiteStatement.bindLong(65, paramRST_UserProfile.getTravelImpact());
      paramSQLiteStatement.bindLong(66, paramRST_UserProfile.getChildrenCount());
      paramSQLiteStatement.bindLong(67, paramRST_UserProfile.getNapTime());
      paramSQLiteStatement.bindLong(68, paramRST_UserProfile.getSleepDisorder());
      paramSQLiteStatement.bindLong(69, paramRST_UserProfile.getSleepDisorderMachine());
      paramSQLiteStatement.bindLong(70, paramRST_UserProfile.getMachineManufacturer());
      paramSQLiteStatement.bindLong(71, paramRST_UserProfile.getAffectsYourSleep());
      paramSQLiteStatement.bindLong(72, paramRST_UserProfile.isStayingAwakeDuringDay());
      paramSQLiteStatement.bindLong(73, paramRST_UserProfile.getConcernsOnSleepProblem());
      paramSQLiteStatement.bindLong(74, paramRST_UserProfile.getExtentInterfereSleepProblem());
      paramSQLiteStatement.bindLong(75, paramRST_UserProfile.getSleepingPositionSnore());
      paramSQLiteStatement.bindLong(76, paramRST_UserProfile.isDryMouth());
      paramSQLiteStatement.bindLong(77, paramRST_UserProfile.isNasalCongestion());
      paramSQLiteStatement.bindLong(78, paramRST_UserProfile.isBreathingObstruction());
      paramSQLiteStatement.bindLong(79, paramRST_UserProfile.isCrawlingSensation());
      paramSQLiteStatement.bindLong(80, paramRST_UserProfile.isMuscleTension());
      paramSQLiteStatement.bindLong(81, paramRST_UserProfile.isLegsStillAtNight());
      paramSQLiteStatement.bindLong(82, paramRST_UserProfile.isSufferFromAllergies());
      paramSQLiteStatement.bindLong(83, paramRST_UserProfile.isSleepWithPartner());
      paramSQLiteStatement.bindLong(84, paramRST_UserProfile.getPetSleepWithYouOnBed());
      paramSQLiteStatement.bindLong(85, paramRST_UserProfile.getSnoreFrequency());
      paramSQLiteStatement.bindLong(86, paramRST_UserProfile.getUsingSleepAids());
      paramSQLiteStatement.bindLong(87, paramRST_UserProfile.getPrescriptionPillsFrequency());
      if (paramRST_UserProfile.getOtherMachineManufacturer() == null) {
        break label1225;
      }
      paramSQLiteStatement.bindString(88, paramRST_UserProfile.getOtherMachineManufacturer());
      label1017:
      if (paramRST_UserProfile.getOtherPrescriptionPillsType() == null) {
        break label1236;
      }
      paramSQLiteStatement.bindString(89, paramRST_UserProfile.getOtherPrescriptionPillsType());
      label1034:
      if (paramRST_UserProfile.getOtherHerbalRemediesType() == null) {
        break label1247;
      }
      paramSQLiteStatement.bindString(90, paramRST_UserProfile.getOtherHerbalRemediesType());
      label1051:
      if (paramRST_UserProfile.getOtherMattressType() == null) {
        break label1258;
      }
      paramSQLiteStatement.bindString(91, paramRST_UserProfile.getOtherMattressType());
      label1068:
      if (paramRST_UserProfile.getOtherMattressBrand() == null) {
        break label1269;
      }
      paramSQLiteStatement.bindString(92, paramRST_UserProfile.getOtherMattressBrand());
      label1085:
      if (paramRST_UserProfile.getOtherPillowsType() == null) {
        break label1280;
      }
      paramSQLiteStatement.bindString(93, paramRST_UserProfile.getOtherPillowsType());
      label1102:
      if (paramRST_UserProfile.getOtherControlBedroomTemperature() == null) {
        break label1291;
      }
      paramSQLiteStatement.bindString(94, paramRST_UserProfile.getOtherControlBedroomTemperature());
      label1119:
      if (paramRST_UserProfile.getOtherTypeOfSoundUsedInSleep() == null) {
        break label1302;
      }
      paramSQLiteStatement.bindString(95, paramRST_UserProfile.getOtherTypeOfSoundUsedInSleep());
      label1136:
      if (paramRST_UserProfile.getOtherSourceOfLightInBedroom() == null) {
        break label1313;
      }
      paramSQLiteStatement.bindString(96, paramRST_UserProfile.getOtherSourceOfLightInBedroom());
      label1153:
      if (paramRST_UserProfile.getOtherReduceAmountOfLightInBedroom() == null) {
        break label1324;
      }
      paramSQLiteStatement.bindString(97, paramRST_UserProfile.getOtherReduceAmountOfLightInBedroom());
      label1170:
      if (paramRST_UserProfile.getNewGender() == null) {
        break label1335;
      }
      paramSQLiteStatement.bindString(98, paramRST_UserProfile.getNewGender());
    }
    for (;;)
    {
      return;
      l1 = 0L;
      break;
      label1193:
      l1 = 0L;
      break label128;
      label1198:
      l1 = 0L;
      break label332;
      label1203:
      paramSQLiteStatement.bindString(33, "");
      break label400;
      label1214:
      paramSQLiteStatement.bindString(34, "");
      break label417;
      label1225:
      paramSQLiteStatement.bindString(88, "");
      break label1017;
      label1236:
      paramSQLiteStatement.bindString(89, "");
      break label1034;
      label1247:
      paramSQLiteStatement.bindString(90, "");
      break label1051;
      label1258:
      paramSQLiteStatement.bindString(91, "");
      break label1068;
      label1269:
      paramSQLiteStatement.bindString(92, "");
      break label1085;
      label1280:
      paramSQLiteStatement.bindString(93, "");
      break label1102;
      label1291:
      paramSQLiteStatement.bindString(94, "");
      break label1119;
      label1302:
      paramSQLiteStatement.bindString(95, "");
      break label1136;
      label1313:
      paramSQLiteStatement.bindString(96, "");
      break label1153;
      label1324:
      paramSQLiteStatement.bindString(97, "");
      break label1170;
      label1335:
      paramSQLiteStatement.bindString(98, "");
    }
  }
  
  public Long getKey(RST_UserProfile paramRST_UserProfile)
  {
    if (paramRST_UserProfile != null) {}
    for (paramRST_UserProfile = paramRST_UserProfile.getId();; paramRST_UserProfile = null) {
      return paramRST_UserProfile;
    }
  }
  
  protected boolean isEntityUpdateable()
  {
    return true;
  }
  
  public RST_UserProfile readEntity(Cursor paramCursor, int paramInt)
  {
    Long localLong;
    int i6;
    int i;
    Date localDate;
    int i2;
    int i18;
    int i14;
    int i10;
    boolean bool1;
    label117:
    boolean bool2;
    label133:
    float f2;
    int i3;
    int n;
    int i16;
    int i12;
    int i4;
    int i13;
    int i7;
    int i17;
    int i1;
    int j;
    int k;
    int i20;
    int i19;
    int i15;
    int i5;
    int i8;
    boolean bool3;
    label353:
    float f1;
    int i9;
    int i11;
    int m;
    String str1;
    if (paramCursor.isNull(paramInt + 0))
    {
      localLong = null;
      i6 = paramCursor.getInt(paramInt + 1);
      i = paramCursor.getInt(paramInt + 2);
      localDate = new Date(paramCursor.getLong(paramInt + 3));
      i2 = paramCursor.getInt(paramInt + 4);
      i18 = paramCursor.getInt(paramInt + 5);
      i14 = paramCursor.getInt(paramInt + 6);
      i10 = paramCursor.getInt(paramInt + 7);
      if (paramCursor.getShort(paramInt + 8) == 0) {
        break label1166;
      }
      bool1 = true;
      if (paramCursor.getShort(paramInt + 9) == 0) {
        break label1172;
      }
      bool2 = true;
      f2 = paramCursor.getFloat(paramInt + 10);
      i3 = paramCursor.getInt(paramInt + 11);
      n = paramCursor.getInt(paramInt + 12);
      i16 = paramCursor.getInt(paramInt + 13);
      i12 = paramCursor.getInt(paramInt + 14);
      i4 = paramCursor.getInt(paramInt + 15);
      i13 = paramCursor.getInt(paramInt + 16);
      i7 = paramCursor.getInt(paramInt + 17);
      i17 = paramCursor.getInt(paramInt + 18);
      i1 = paramCursor.getInt(paramInt + 19);
      j = paramCursor.getInt(paramInt + 20);
      k = paramCursor.getInt(paramInt + 21);
      i20 = paramCursor.getInt(paramInt + 22);
      i19 = paramCursor.getInt(paramInt + 23);
      i15 = paramCursor.getInt(paramInt + 24);
      i5 = paramCursor.getInt(paramInt + 25);
      i8 = paramCursor.getInt(paramInt + 26);
      if (paramCursor.getShort(paramInt + 27) == 0) {
        break label1178;
      }
      bool3 = true;
      f1 = paramCursor.getFloat(paramInt + 28);
      i9 = paramCursor.getInt(paramInt + 29);
      i11 = paramCursor.getInt(paramInt + 30);
      m = paramCursor.getInt(paramInt + 31);
      if (!paramCursor.isNull(paramInt + 32)) {
        break label1184;
      }
      str1 = "";
      label417:
      if (!paramCursor.isNull(paramInt + 33)) {
        break label1199;
      }
    }
    label1166:
    label1172:
    label1178:
    label1184:
    label1199:
    for (String str2 = "";; str2 = paramCursor.getString(paramInt + 33))
    {
      return new RST_UserProfile(localLong, i6, i, localDate, i2, i18, i14, i10, bool1, bool2, f2, i3, n, i16, i12, i4, i13, i7, i17, i1, j, k, i20, i19, i15, i5, i8, bool3, f1, i9, i11, m, str1, str2, paramCursor.getInt(paramInt + 34), paramCursor.getInt(paramInt + 35), paramCursor.getInt(paramInt + 36), paramCursor.getInt(paramInt + 37), paramCursor.getInt(paramInt + 38), paramCursor.getInt(paramInt + 39), paramCursor.getInt(paramInt + 40), paramCursor.getInt(paramInt + 41), paramCursor.getInt(paramInt + 42), paramCursor.getInt(paramInt + 43), paramCursor.getInt(paramInt + 44), paramCursor.getInt(paramInt + 45), paramCursor.getInt(paramInt + 46), paramCursor.getInt(paramInt + 47), paramCursor.getInt(paramInt + 48), paramCursor.getInt(paramInt + 49), paramCursor.getInt(paramInt + 50), paramCursor.getInt(paramInt + 51), paramCursor.getInt(paramInt + 52), paramCursor.getInt(paramInt + 53), paramCursor.getInt(paramInt + 54), paramCursor.getInt(paramInt + 55), paramCursor.getInt(paramInt + 56), paramCursor.getInt(paramInt + 57), paramCursor.getInt(paramInt + 58), paramCursor.getInt(paramInt + 59), paramCursor.getInt(paramInt + 60), paramCursor.getInt(paramInt + 61), paramCursor.getInt(paramInt + 62), paramCursor.getInt(paramInt + 63), paramCursor.getInt(paramInt + 64), paramCursor.getInt(paramInt + 65), paramCursor.getInt(paramInt + 66), paramCursor.getInt(paramInt + 67), paramCursor.getInt(paramInt + 68), paramCursor.getInt(paramInt + 69), paramCursor.getInt(paramInt + 70), paramCursor.getInt(paramInt + 71), paramCursor.getInt(paramInt + 72), paramCursor.getInt(paramInt + 73), paramCursor.getInt(paramInt + 74), paramCursor.getInt(paramInt + 75), paramCursor.getInt(paramInt + 76), paramCursor.getInt(paramInt + 77), paramCursor.getInt(paramInt + 78), paramCursor.getInt(paramInt + 79), paramCursor.getInt(paramInt + 80), paramCursor.getInt(paramInt + 81), paramCursor.getInt(paramInt + 82), paramCursor.getInt(paramInt + 83), paramCursor.getInt(paramInt + 84), paramCursor.getInt(paramInt + 85), paramCursor.getInt(paramInt + 86), paramCursor.getString(paramInt + 87), paramCursor.getString(paramInt + 88), paramCursor.getString(paramInt + 89), paramCursor.getString(paramInt + 90), paramCursor.getString(paramInt + 91), paramCursor.getString(paramInt + 92), paramCursor.getString(paramInt + 93), paramCursor.getString(paramInt + 94), paramCursor.getString(paramInt + 95), paramCursor.getString(paramInt + 96), paramCursor.getString(paramInt + 97));
      localLong = Long.valueOf(paramCursor.getLong(paramInt + 0));
      break;
      bool1 = false;
      break label117;
      bool2 = false;
      break label133;
      bool3 = false;
      break label353;
      str1 = paramCursor.getString(paramInt + 32);
      break label417;
    }
  }
  
  public void readEntity(Cursor paramCursor, RST_UserProfile paramRST_UserProfile, int paramInt)
  {
    boolean bool2 = true;
    boolean bool1;
    if (paramCursor.isNull(paramInt + 0))
    {
      localObject = null;
      paramRST_UserProfile.setId((Long)localObject);
      paramRST_UserProfile.setBedTime(paramCursor.getInt(paramInt + 1));
      paramRST_UserProfile.setConsumeAlcohol(paramCursor.getInt(paramInt + 2));
      paramRST_UserProfile.setDateOfBirth(new Date(paramCursor.getLong(paramInt + 3)));
      paramRST_UserProfile.setGender(paramCursor.getInt(paramInt + 4));
      paramRST_UserProfile.setExerciseBeforeBed(paramCursor.getInt(paramInt + 5));
      paramRST_UserProfile.setExerciseIntensity(paramCursor.getInt(paramInt + 6));
      paramRST_UserProfile.setHasSleepPartner(paramCursor.getInt(paramInt + 7));
      if (paramCursor.getShort(paramInt + 8) == 0) {
        break label1445;
      }
      bool1 = true;
      label140:
      paramRST_UserProfile.setHasPet(bool1);
      if (paramCursor.getShort(paramInt + 9) == 0) {
        break label1451;
      }
      bool1 = true;
      label162:
      paramRST_UserProfile.setHasPetInBed(bool1);
      paramRST_UserProfile.setHeight(paramCursor.getFloat(paramInt + 10));
      paramRST_UserProfile.setHighBloodPressure(paramCursor.getInt(paramInt + 11));
      paramRST_UserProfile.setIllnessForSleep(paramCursor.getInt(paramInt + 12));
      paramRST_UserProfile.setMedication(paramCursor.getInt(paramInt + 13));
      paramRST_UserProfile.setMedicationFrequency(paramCursor.getInt(paramInt + 14));
      paramRST_UserProfile.setNaps(paramCursor.getInt(paramInt + 15));
      paramRST_UserProfile.setNeckSize(paramCursor.getInt(paramInt + 16));
      paramRST_UserProfile.setNumCaffeineDrinks(paramCursor.getInt(paramInt + 17));
      paramRST_UserProfile.setNumCigarettes(paramCursor.getInt(paramInt + 18));
      paramRST_UserProfile.setNumDrinks(paramCursor.getInt(paramInt + 19));
      paramRST_UserProfile.setNumExercises(paramCursor.getInt(paramInt + 20));
      paramRST_UserProfile.setSleepMover(paramCursor.getInt(paramInt + 21));
      paramRST_UserProfile.setSleepProblems(paramCursor.getInt(paramInt + 22));
      paramRST_UserProfile.setSmoker(paramCursor.getInt(paramInt + 23));
      paramRST_UserProfile.setSnorer(paramCursor.getInt(paramInt + 24));
      paramRST_UserProfile.setStopsBreathing(paramCursor.getInt(paramInt + 25));
      paramRST_UserProfile.setStressedFrequency(paramCursor.getInt(paramInt + 26));
      if (paramCursor.getShort(paramInt + 27) == 0) {
        break label1457;
      }
      bool1 = bool2;
      label423:
      paramRST_UserProfile.setTiredDaytime(bool1);
      paramRST_UserProfile.setWeight(paramCursor.getFloat(paramInt + 28));
      paramRST_UserProfile.setHeightUnit(paramCursor.getInt(paramInt + 29));
      paramRST_UserProfile.setWeightUnit(paramCursor.getInt(paramInt + 30));
      paramRST_UserProfile.setTemperatureUnit(paramCursor.getInt(paramInt + 31));
      if (!paramCursor.isNull(paramInt + 32)) {
        break label1463;
      }
      localObject = "";
      label502:
      paramRST_UserProfile.setLocale((String)localObject);
      if (!paramCursor.isNull(paramInt + 33)) {
        break label1478;
      }
    }
    label1445:
    label1451:
    label1457:
    label1463:
    label1478:
    for (Object localObject = "";; localObject = paramCursor.getString(paramInt + 33))
    {
      paramRST_UserProfile.setCountryCode((String)localObject);
      paramRST_UserProfile.setPrescriptionPillsType(paramCursor.getInt(paramInt + 34));
      paramRST_UserProfile.setPrescriptionPillsConcern(paramCursor.getInt(paramInt + 35));
      paramRST_UserProfile.setOTCMedicationType(paramCursor.getInt(paramInt + 36));
      paramRST_UserProfile.setOTCMedicationFrequency(paramCursor.getInt(paramInt + 37));
      paramRST_UserProfile.setOTCMedicationConcern(paramCursor.getInt(paramInt + 38));
      paramRST_UserProfile.setHerbalRemediesType(paramCursor.getInt(paramInt + 39));
      paramRST_UserProfile.setHerbalRemediesFrequency(paramCursor.getInt(paramInt + 40));
      paramRST_UserProfile.setControlBedroomTemperature(paramCursor.getInt(paramInt + 41));
      paramRST_UserProfile.setHumidifierUsed(paramCursor.getInt(paramInt + 42));
      paramRST_UserProfile.setAirPurifierUsed(paramCursor.getInt(paramInt + 43));
      paramRST_UserProfile.setNoiseLevelInBedroom(paramCursor.getInt(paramInt + 44));
      paramRST_UserProfile.setHowOftenElectronicsUsed(paramCursor.getInt(paramInt + 45));
      paramRST_UserProfile.setGetWokenUpInNightByAny(paramCursor.getInt(paramInt + 46));
      paramRST_UserProfile.setSoundUsedInSleep(paramCursor.getInt(paramInt + 47));
      paramRST_UserProfile.setTypeOfSoundUsedInSleep(paramCursor.getInt(paramInt + 48));
      paramRST_UserProfile.setLevelOfLightInBedroom(paramCursor.getInt(paramInt + 49));
      paramRST_UserProfile.setSourceOfLightInBedroom(paramCursor.getInt(paramInt + 50));
      paramRST_UserProfile.setReduceAmountOfLightInBedroom(paramCursor.getInt(paramInt + 51));
      paramRST_UserProfile.setSpecialLightUsedInSleep(paramCursor.getInt(paramInt + 52));
      paramRST_UserProfile.setMattressType(paramCursor.getInt(paramInt + 53));
      paramRST_UserProfile.setMattressSize(paramCursor.getInt(paramInt + 54));
      paramRST_UserProfile.setMattressBrand(paramCursor.getInt(paramInt + 55));
      paramRST_UserProfile.setMattressAge(paramCursor.getInt(paramInt + 56));
      paramRST_UserProfile.setMattressPadTopperOnBed(paramCursor.getInt(paramInt + 57));
      paramRST_UserProfile.setPillowsType(paramCursor.getInt(paramInt + 58));
      paramRST_UserProfile.setPillowsAge(paramCursor.getInt(paramInt + 59));
      paramRST_UserProfile.setSleepingPosition(paramCursor.getInt(paramInt + 60));
      paramRST_UserProfile.setWakeUpDifficulties(paramCursor.getInt(paramInt + 61));
      paramRST_UserProfile.setMorningAlertness(paramCursor.getInt(paramInt + 62));
      paramRST_UserProfile.setTypeofPerson(paramCursor.getInt(paramInt + 63));
      paramRST_UserProfile.setTravelImpact(paramCursor.getInt(paramInt + 64));
      paramRST_UserProfile.setChildrenCount(paramCursor.getInt(paramInt + 65));
      paramRST_UserProfile.setNapTime(paramCursor.getInt(paramInt + 66));
      paramRST_UserProfile.setSleepDisorder(paramCursor.getInt(paramInt + 67));
      paramRST_UserProfile.setSleepDisorderMachine(paramCursor.getInt(paramInt + 68));
      paramRST_UserProfile.setMachineManufacturer(paramCursor.getInt(paramInt + 69));
      paramRST_UserProfile.setAffectsYourSleep(paramCursor.getInt(paramInt + 70));
      paramRST_UserProfile.setStayingAwakeDuringDay(paramCursor.getInt(paramInt + 71));
      paramRST_UserProfile.setConcernsOnSleepProblem(paramCursor.getInt(paramInt + 72));
      paramRST_UserProfile.setExtentInterfereSleepProblem(paramCursor.getInt(paramInt + 73));
      paramRST_UserProfile.setSleepingPositionSnore(paramCursor.getInt(paramInt + 74));
      paramRST_UserProfile.setDryMouth(paramCursor.getInt(paramInt + 75));
      paramRST_UserProfile.setNasalCongestion(paramCursor.getInt(paramInt + 76));
      paramRST_UserProfile.setBreathingObstruction(paramCursor.getInt(paramInt + 77));
      paramRST_UserProfile.setCrawlingSensation(paramCursor.getInt(paramInt + 78));
      paramRST_UserProfile.setMuscleTension(paramCursor.getInt(paramInt + 79));
      paramRST_UserProfile.setLegsStillAtNight(paramCursor.getInt(paramInt + 80));
      paramRST_UserProfile.setSufferFromAllergies(paramCursor.getInt(paramInt + 81));
      paramRST_UserProfile.setSleepWithPartner(paramCursor.getInt(paramInt + 82));
      paramRST_UserProfile.setPetSleepWithYouOnBed(paramCursor.getInt(paramInt + 83));
      paramRST_UserProfile.setSnoreFrequency(paramCursor.getInt(paramInt + 84));
      paramRST_UserProfile.setUsingSleepAids(paramCursor.getInt(paramInt + 85));
      paramRST_UserProfile.setPrescriptionPillsFrequency(paramCursor.getInt(paramInt + 86));
      paramRST_UserProfile.setOtherMachineManufacturer(paramCursor.getString(paramInt + 87));
      paramRST_UserProfile.setOtherPrescriptionPillsType(paramCursor.getString(paramInt + 88));
      paramRST_UserProfile.setOtherHerbalRemediesType(paramCursor.getString(paramInt + 89));
      paramRST_UserProfile.setOtherMattressType(paramCursor.getString(paramInt + 90));
      paramRST_UserProfile.setOtherMattressBrand(paramCursor.getString(paramInt + 91));
      paramRST_UserProfile.setOtherPillowsType(paramCursor.getString(paramInt + 92));
      paramRST_UserProfile.setOtherControlBedroomTemperature(paramCursor.getString(paramInt + 93));
      paramRST_UserProfile.setOtherTypeOfSoundUsedInSleep(paramCursor.getString(paramInt + 94));
      paramRST_UserProfile.setOtherSourceOfLightInBedroom(paramCursor.getString(paramInt + 95));
      paramRST_UserProfile.setOtherReduceAmountOfLightInBedroom(paramCursor.getString(paramInt + 96));
      paramRST_UserProfile.setNewGender(paramCursor.getString(paramInt + 97));
      return;
      localObject = Long.valueOf(paramCursor.getLong(paramInt + 0));
      break;
      bool1 = false;
      break label140;
      bool1 = false;
      break label162;
      bool1 = false;
      break label423;
      localObject = paramCursor.getString(paramInt + 32);
      break label502;
    }
  }
  
  public Long readKey(Cursor paramCursor, int paramInt)
  {
    if (paramCursor.isNull(paramInt + 0)) {}
    for (paramCursor = null;; paramCursor = Long.valueOf(paramCursor.getLong(paramInt + 0))) {
      return paramCursor;
    }
  }
  
  protected Long updateKeyAfterInsert(RST_UserProfile paramRST_UserProfile, long paramLong)
  {
    paramRST_UserProfile.setId(Long.valueOf(paramLong));
    return Long.valueOf(paramLong);
  }
  
  public static class Properties
  {
    public static final Property BedTime;
    public static final Property ConsumeAlcohol;
    public static final Property DateOfBirth;
    public static final Property ExerciseBeforeBed;
    public static final Property ExerciseIntensity;
    public static final Property Gender;
    public static final Property HasPet;
    public static final Property HasPetInBed;
    public static final Property HasSleepPartner;
    public static final Property Height;
    public static final Property HeightUnit;
    public static final Property HighBloodPressure;
    public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    public static final Property IllnessForSleep;
    public static final Property Medication;
    public static final Property MedicationFrequency;
    public static final Property Naps;
    public static final Property NeckSize;
    public static final Property NumCaffeineDrinks;
    public static final Property NumCigarettes;
    public static final Property NumDrinks;
    public static final Property NumExercises;
    public static final Property OTCMedicationConcern;
    public static final Property OTCMedicationFrequency;
    public static final Property OTCMedicationType;
    public static final Property SleepMover;
    public static final Property SleepProblems;
    public static final Property Smoker;
    public static final Property Snorer;
    public static final Property StopsBreathing;
    public static final Property StressedFrequency;
    public static final Property TemperatureUnit;
    public static final Property TiredDaytime;
    public static final Property Weight;
    public static final Property WeightUnit;
    public static final Property affectsYourSleep;
    public static final Property childrenCount;
    public static final Property concernsOnSleepProblem;
    public static final Property controlBedroomTemperature;
    public static final Property countryCode;
    public static final Property extentInterfereSleepProblem;
    public static final Property getWokenUpInNightByAny;
    public static final Property herbalRemediesFrequency;
    public static final Property herbalRemediesType;
    public static final Property howOftenElectronicsUsed;
    public static final Property isAirPurifierUsed;
    public static final Property isBreathingObstruction;
    public static final Property isCrawlingSensation;
    public static final Property isDryMouth;
    public static final Property isHumidifierUsed;
    public static final Property isLegsStillAtNight;
    public static final Property isMattressPadTopperOnBed;
    public static final Property isMuscleTension;
    public static final Property isNasalCongestion;
    public static final Property isSleepWithPartner;
    public static final Property isSoundUsedInSleep;
    public static final Property isStayingAwakeDuringDay;
    public static final Property isSufferFromAllergies;
    public static final Property levelOfLightInBedroom;
    public static final Property locale;
    public static final Property machineManufacturer;
    public static final Property mattressAge;
    public static final Property mattressBrand;
    public static final Property mattressSize;
    public static final Property mattressType;
    public static final Property morningAlertness;
    public static final Property napTime;
    public static final Property newGender = new Property(97, String.class, "text", false, "NEWGENDER");
    public static final Property noiseLevelInBedroom;
    public static final Property otherControlBedroomTemperature;
    public static final Property otherHerbalRemediesType;
    public static final Property otherMachineManufacturer;
    public static final Property otherMattressBrand;
    public static final Property otherMattressType;
    public static final Property otherPillowsType;
    public static final Property otherPrescriptionPillsType;
    public static final Property otherReduceAmountOfLightInBedroom;
    public static final Property otherSourceOfLightInBedroom;
    public static final Property otherTypeOfSoundUsedInSleep;
    public static final Property petSleepWithYouOnBed;
    public static final Property pillowsAge;
    public static final Property pillowsType;
    public static final Property prescriptionPillsConcern;
    public static final Property prescriptionPillsFrequency;
    public static final Property prescriptionPillsType;
    public static final Property reduceAmountOfLightInBedroom;
    public static final Property sleepDisorder;
    public static final Property sleepDisorderMachine;
    public static final Property sleepingPosition;
    public static final Property sleepingPositionSnore;
    public static final Property snoreFrequency;
    public static final Property sourceOfLightInBedroom;
    public static final Property specialLightUsedInSleep;
    public static final Property travelImpact;
    public static final Property typeOfSoundUsedInSleep;
    public static final Property typeofPerson;
    public static final Property usingSleepAids;
    public static final Property wakeUpDifficulties;
    
    static
    {
      BedTime = new Property(1, Integer.TYPE, "bedTime", false, "BED_TIME");
      ConsumeAlcohol = new Property(2, Integer.TYPE, "consumeAlcohol", false, "CONSUME_ALCOHOL");
      DateOfBirth = new Property(3, Date.class, "dateOfBirth", false, "DATE_OF_BIRTH");
      Gender = new Property(4, Integer.TYPE, "gender", false, "GENDER");
      ExerciseBeforeBed = new Property(5, Integer.TYPE, "exerciseBeforeBed", false, "EXERCISE_BEFORE_BED");
      ExerciseIntensity = new Property(6, Integer.TYPE, "exerciseIntensity", false, "EXERCISE_INTENSITY");
      HasSleepPartner = new Property(7, Integer.TYPE, "hasSleepPartner", false, "HAS_SLEEP_PARTNER");
      HasPet = new Property(8, Boolean.TYPE, "hasPet", false, "HAS_PET");
      HasPetInBed = new Property(9, Boolean.TYPE, "hasPetInBed", false, "HAS_PET_IN_BED");
      Height = new Property(10, Float.TYPE, "height", false, "HEIGHT");
      HighBloodPressure = new Property(11, Integer.TYPE, "highBloodPressure", false, "HIGH_BLOOD_PRESSURE");
      IllnessForSleep = new Property(12, Integer.TYPE, "illnessForSleep", false, "ILLNESS_FOR_SLEEP");
      Medication = new Property(13, Integer.TYPE, "medication", false, "MEDICATION");
      MedicationFrequency = new Property(14, Integer.TYPE, "medicationFrequency", false, "MEDICATION_FREQUENCY");
      Naps = new Property(15, Integer.TYPE, "naps", false, "NAPS");
      NeckSize = new Property(16, Integer.TYPE, "neckSize", false, "NECK_SIZE");
      NumCaffeineDrinks = new Property(17, Integer.TYPE, "numCaffeineDrinks", false, "NUM_CAFFEINE_DRINKS");
      NumCigarettes = new Property(18, Integer.TYPE, "numCigarettes", false, "NUM_CIGARETTES");
      NumDrinks = new Property(19, Integer.TYPE, "numDrinks", false, "NUM_DRINKS");
      NumExercises = new Property(20, Integer.TYPE, "numExercises", false, "NUM_EXERCISES");
      SleepMover = new Property(21, Integer.TYPE, "sleepMover", false, "SLEEP_MOVER");
      SleepProblems = new Property(22, Integer.TYPE, "sleepProblems", false, "SLEEP_PROBLEMS");
      Smoker = new Property(23, Integer.TYPE, "smoker", false, "SMOKER");
      Snorer = new Property(24, Integer.TYPE, "snorer", false, "SNORER");
      StopsBreathing = new Property(25, Integer.TYPE, "stopsBreathing", false, "STOPS_BREATHING");
      StressedFrequency = new Property(26, Integer.TYPE, "stressedFrequency", false, "STRESSED_FREQUENCY");
      TiredDaytime = new Property(27, Boolean.TYPE, "tiredDaytime", false, "TIRED_DAYTIME");
      Weight = new Property(28, Float.TYPE, "weight", false, "WEIGHT");
      HeightUnit = new Property(29, Integer.TYPE, "heightUnit", false, "HEIGHT_UNIT");
      WeightUnit = new Property(30, Integer.TYPE, "weightUnit", false, "WEIGHT_UNIT");
      TemperatureUnit = new Property(31, Integer.TYPE, "temperatureUnit", false, "TEMPERATURE_UNIT");
      locale = new Property(32, String.class, "locale", false, "LOCALE");
      countryCode = new Property(33, String.class, "countryCode", false, "COUNTRY_CODE");
      prescriptionPillsType = new Property(34, Integer.TYPE, "prescriptionPillsType", false, "PRESCRIPTIONPILLSTYPE");
      prescriptionPillsConcern = new Property(35, Integer.TYPE, "prescriptionPillsConcern", false, "PRESCRIPTIONPILLSCONCERN");
      OTCMedicationType = new Property(36, Integer.TYPE, "OTCMedicationType", false, "OTCMEDICATIONTYPE");
      OTCMedicationFrequency = new Property(37, Integer.TYPE, "OTCMedicationFrequency", false, "OTCMEDICATIONFREQUENCY");
      OTCMedicationConcern = new Property(38, Integer.TYPE, "OTCMedicationConcern", false, "OTCMEDICATIONCONCERN");
      herbalRemediesType = new Property(39, Integer.TYPE, "herbalRemediesType", false, "HERBALREMEDIESTYPE");
      herbalRemediesFrequency = new Property(40, Integer.TYPE, "herbalRemediesFrequency", false, "HERBALREMEDIESFREQUENCY");
      controlBedroomTemperature = new Property(41, Integer.TYPE, "controlBedroomTemperature", false, "CONTROLBEDROOMTEMPERATURE");
      isHumidifierUsed = new Property(42, Integer.TYPE, "isHumidifierUsed", false, "ISHUMIDIFIERUSED");
      isAirPurifierUsed = new Property(43, Integer.TYPE, "isAirPurifierUsed", false, "ISAIRPURIFIERUSED");
      noiseLevelInBedroom = new Property(44, Integer.TYPE, "noiseLevelInBedroom", false, "NOISELEVELINBEDROOM");
      howOftenElectronicsUsed = new Property(45, Integer.TYPE, "howOftenElectronicsUsed", false, "HOWOFTENELECTRONICSUSED");
      getWokenUpInNightByAny = new Property(46, Integer.TYPE, "getWokenUpInNightByAny", false, "GETWOKENUPINNIGHTBYANY");
      isSoundUsedInSleep = new Property(47, Integer.TYPE, "isSoundUsedInSleep", false, "ISSOUNDUSEDINSLEEP");
      typeOfSoundUsedInSleep = new Property(48, Integer.TYPE, "typeOfSoundUsedInSleep", false, "TYPEOFSOUNDUSEDINSLEEP");
      levelOfLightInBedroom = new Property(49, Integer.TYPE, "levelOfLightInBedroom", false, "LEVELOFLIGHTINBEDROOM");
      sourceOfLightInBedroom = new Property(50, Integer.TYPE, "sourceOfLightInBedroom", false, "SOURCEOFLIGHTINBEDROOM");
      reduceAmountOfLightInBedroom = new Property(51, Integer.TYPE, "reduceAmountOfLightInBedroom", false, "REDUCEAMOUNTOFLIGHTINBEDROOM");
      specialLightUsedInSleep = new Property(52, Integer.TYPE, "specialLightUsedInSleep", false, "SPECIALLIGHTUSEDINSLEEP");
      mattressType = new Property(53, Integer.TYPE, "mattressType", false, "MATTRESSTYPE");
      mattressSize = new Property(54, Integer.TYPE, "mattressSize", false, "MATTRESSSIZE");
      mattressBrand = new Property(55, Integer.TYPE, "mattressBrand", false, "MATTRESSBRAND");
      mattressAge = new Property(56, Integer.TYPE, "mattressAge", false, "MATTRESSAGE");
      isMattressPadTopperOnBed = new Property(57, Integer.TYPE, "isMattressPadTopperOnBed", false, "ISMATTRESSPADTOPPERONBED");
      pillowsType = new Property(58, Integer.TYPE, "pillowsType", false, "PILLOWSTYPE");
      pillowsAge = new Property(59, Integer.TYPE, "pillowsAge", false, "PILLOWSAGE");
      sleepingPosition = new Property(60, Integer.TYPE, "sleepingPosition", false, "SLEEPINGPOSITION");
      wakeUpDifficulties = new Property(61, Integer.TYPE, "wakeUpDifficulties", false, "WAKEUPDIFFICULTIES");
      morningAlertness = new Property(62, Integer.TYPE, "morningAlertness", false, "MORNINGALERTNESS");
      typeofPerson = new Property(63, Integer.TYPE, "typeofPerson", false, "TYPEOFPERSON");
      travelImpact = new Property(64, Integer.TYPE, "travelImpact", false, "TRAVELIMPACT");
      childrenCount = new Property(65, Integer.TYPE, "childrenCount", false, "CHILDRENCOUNT");
      napTime = new Property(66, Integer.TYPE, "napTime", false, "NAPTIME");
      sleepDisorder = new Property(67, Integer.TYPE, "sleepDisorder", false, "SLEEPDISORDER");
      sleepDisorderMachine = new Property(68, Integer.TYPE, "sleepDisorderMachine", false, "SLEEPDISORDERMACHINE");
      machineManufacturer = new Property(69, Integer.TYPE, "machineManufacturer", false, "MACHINEMANUFACTURER");
      affectsYourSleep = new Property(70, Integer.TYPE, "affectsYourSleep", false, "AFFECTSYOURSLEEP");
      isStayingAwakeDuringDay = new Property(71, Integer.TYPE, "isStayingAwakeDuringDay", false, "ISSTAYINGAWAKEDURINGDAY");
      concernsOnSleepProblem = new Property(72, Integer.TYPE, "concernsOnSleepProblem", false, "CONCERNSONSLEEPPROBLEM");
      extentInterfereSleepProblem = new Property(73, Integer.TYPE, "extentInterfereSleepProblem", false, "EXTENTINTERFERESLEEPPROBLEM");
      sleepingPositionSnore = new Property(74, Integer.TYPE, "sleepingPositionSnore", false, "SLEEPINGPOSITIONSNORE");
      isDryMouth = new Property(75, Integer.TYPE, "isDryMouth", false, "ISDRYMOUTH");
      isNasalCongestion = new Property(76, Integer.TYPE, "isNasalCongestion", false, "ISNASALCONGESTION");
      isBreathingObstruction = new Property(77, Integer.TYPE, "isBreathingObstruction", false, "ISBREATHINGOBSTRUCTION");
      isCrawlingSensation = new Property(78, Integer.TYPE, "isCrawlingSensation", false, "ISCRAWLINGSENSATION");
      isMuscleTension = new Property(79, Integer.TYPE, "isMuscleTension", false, "ISMUSCLETENSION");
      isLegsStillAtNight = new Property(80, Integer.TYPE, "isLegsStillAtNight", false, "ISLEGSSTILLATNIGHT");
      isSufferFromAllergies = new Property(81, Integer.TYPE, "isSufferFromAllergies", false, "ISSUFFERFROMALLERGIES");
      isSleepWithPartner = new Property(82, Integer.TYPE, "isSleepWithPartner", false, "ISSLEEPWITHPARTNER");
      petSleepWithYouOnBed = new Property(83, Integer.TYPE, "petSleepWithYouOnBed", false, "PETSLEEPWITHYOUONBED");
      snoreFrequency = new Property(84, Integer.TYPE, "snoreFrequency", false, "SNOREFREQUENCY");
      usingSleepAids = new Property(85, Integer.TYPE, "usingSleepAids", false, "USINGSLEEPAIDS");
      prescriptionPillsFrequency = new Property(86, Integer.TYPE, "prescriptionPillsFrequency", false, "PRESCRIPTIONPILLSFREQUENCY");
      otherMachineManufacturer = new Property(87, String.class, "text", false, "OTHERMACHINEMANUFACTURER");
      otherPrescriptionPillsType = new Property(88, String.class, "text", false, "OTHERPRESCRIPTIONPILLSTYPE");
      otherHerbalRemediesType = new Property(89, String.class, "text", false, "OTHERHERBALREMEDIESTYPE");
      otherMattressType = new Property(90, String.class, "text", false, "OTHERMATTRESSTYPE");
      otherMattressBrand = new Property(91, String.class, "text", false, "OTHERMATTRESSBRAND");
      otherPillowsType = new Property(92, String.class, "text", false, "OTHERPILLOWSTYPE");
      otherControlBedroomTemperature = new Property(93, String.class, "text", false, "OTHERCONTROLBEDROOMTEMPERATURE");
      otherTypeOfSoundUsedInSleep = new Property(94, String.class, "text", false, "OTHERTYPEOFSOUNDUSEDINSLEEP");
      otherSourceOfLightInBedroom = new Property(95, String.class, "text", false, "OTHERSOURCEOFLIGHTINBEDROOM");
      otherReduceAmountOfLightInBedroom = new Property(96, String.class, "text", false, "OTHERREDUCEAMOUNTOFLIGHTINBEDROOM");
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
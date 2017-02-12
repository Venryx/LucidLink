package com.resmed.refresh.ui.utils;

import android.content.res.Resources;

import com.resmed.refresh.model.RST_User;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.MeasureManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UserProfileDataManager
{
  public static final Integer CENTIMETERS;
  public static String COLLAR_SIZE_BIG;
  public static String COLLAR_SIZE_BIG_IN_BUTTON = RefreshApplication.getInstance().getApplicationContext().getResources().getString(2131165916);
  public static String COLLAR_SIZE_SMALL;
  public static String COLLAR_SIZE_SMALL_IN_BUTTON = RefreshApplication.getInstance().getApplicationContext().getResources().getString(2131165917);
  public static String GENDER_FEMALE;
  private static String GENDER_FEMALE_IN_BUTTON;
  public static final int GENDER_FEMALE_VALUE = 1;
  public static String GENDER_MALE;
  private static String GENDER_MALE_IN_BUTTON;
  public static final int GENDER_MALE_VALUE = 0;
  public static final int HEIGHT_UNIT_CM = 1;
  public static final int HEIGHT_UNIT_IN = 0;
  public static final Integer INCH;
  public static final Integer KILOGRAM;
  public static final Integer PICKER_BIRTH = Integer.valueOf(0);
  public static final Integer PICKER_COLLAR_SIZE;
  public static final Integer PICKER_GENDER = Integer.valueOf(1);
  public static final Integer PICKER_HEIGHT = Integer.valueOf(2);
  public static final Integer PICKER_HEIGHT_UNIT;
  public static final Integer PICKER_TEMP_UNIT;
  public static final Integer PICKER_WEIGHT = Integer.valueOf(3);
  public static final Integer PICKER_WEIGHT_UNIT;
  public static final Integer POUND;
  public static final int TEMPERATURE_UNIT_C = 1;
  public static final int TEMPERATURE_UNIT_F = 0;
  public static final int WEIGHT_UNIT_KG = 1;
  public static final int WEIGHT_UNIT_LB = 0;
  public static final int WEIGHT_UNIT_ST = 2;
  public static String[] heightUnitArray;
  public static String[] temperatureUnitArray;
  private static UserProfileDataManager userProfileDataManager;
  public static String[] weightUnitArray;
  private final Integer HEIGHT_DEFAULT_VALUE = Integer.valueOf(170);
  private final Integer WEIGHT_DEFAULT_VALUE = Integer.valueOf(60);
  private Date birth;
  private Boolean collarSize;
  private Integer currentPicker;
  private Boolean genderValue;
  private float heightInch;
  private float heightMeters;
  private int heightUnit;
  private Resources resources = RefreshApplication.getInstance().getApplicationContext().getResources();
  private Date tempBirth;
  private Boolean tempCollarSize;
  private Boolean tempGenderValue;
  private float tempHeightInch;
  private float tempHeightMeters;
  private int tempHeightUnit;
  private int tempTemperatureUnit;
  private float tempWeightKilogram;
  private float tempWeightPound;
  private int tempWeightUnit;
  private int temperatureUnit;
  private RST_User user;
  private float weightKilogram;
  private float weightPound;
  private int weightUnit;
  
  static
  {
    PICKER_COLLAR_SIZE = Integer.valueOf(4);
    PICKER_WEIGHT_UNIT = Integer.valueOf(5);
    PICKER_HEIGHT_UNIT = Integer.valueOf(6);
    PICKER_TEMP_UNIT = Integer.valueOf(7);
    CENTIMETERS = Integer.valueOf(0);
    INCH = Integer.valueOf(1);
    KILOGRAM = Integer.valueOf(2);
    POUND = Integer.valueOf(3);
    weightUnitArray = new String[] { "lb", "kg", "st" };
    heightUnitArray = new String[] { "in", "cm" };
    temperatureUnitArray = new String[] { RefreshApplication.getInstance().getApplicationContext().getResources().getString(2131165308), RefreshApplication.getInstance().getApplicationContext().getResources().getString(2131165307) };
    GENDER_MALE = RefreshApplication.getInstance().getApplicationContext().getResources().getString(2131165904);
    GENDER_FEMALE = RefreshApplication.getInstance().getApplicationContext().getResources().getString(2131165905);
    GENDER_MALE_IN_BUTTON = RefreshApplication.getInstance().getApplicationContext().getResources().getString(2131165906);
    GENDER_FEMALE_IN_BUTTON = RefreshApplication.getInstance().getApplicationContext().getResources().getString(2131165907);
    COLLAR_SIZE_BIG = RefreshApplication.getInstance().getApplicationContext().getResources().getString(2131165914);
    COLLAR_SIZE_SMALL = RefreshApplication.getInstance().getApplicationContext().getResources().getString(2131165915);
  }
  
  public static boolean getBooleanValueForGenderProgress(int paramInt)
  {
    if (paramInt == 0) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  private Integer getDiffYears(Date paramDate)
  {
    Object localObject = Calendar.getInstance();
    int k = ((Calendar)localObject).get(1);
    Integer localInteger1 = Integer.valueOf(((Calendar)localObject).get(2));
    int i = ((Calendar)localObject).get(5);
    localObject = GregorianCalendar.getInstance();
    ((Calendar)localObject).setTime(paramDate);
    int m = ((Calendar)localObject).get(1);
    Integer localInteger2 = Integer.valueOf(((Calendar)localObject).get(2));
    int j = ((Calendar)localObject).get(5);
    localObject = Integer.valueOf(Integer.valueOf(k).intValue() - Integer.valueOf(m).intValue());
    if (localInteger1.intValue() < localInteger2.intValue()) {
      paramDate = Integer.valueOf(((Integer)localObject).intValue() - 1);
    }
    for (;;)
    {
      return paramDate;
      paramDate = (Date)localObject;
      if (localInteger1 == localInteger2)
      {
        paramDate = (Date)localObject;
        if (Integer.valueOf(i).intValue() < Integer.valueOf(j).intValue()) {
          paramDate = Integer.valueOf(((Integer)localObject).intValue() - 1);
        }
      }
    }
  }
  
  public static UserProfileDataManager getInstance()
  {
    try
    {
      if (userProfileDataManager == null)
      {
        localUserProfileDataManager = new com/resmed/refresh/ui/utils/UserProfileDataManager;
        localUserProfileDataManager.<init>();
        userProfileDataManager = localUserProfileDataManager;
        userProfileDataManager.init();
      }
      UserProfileDataManager localUserProfileDataManager = userProfileDataManager;
      return localUserProfileDataManager;
    }
    finally {}
  }
  
  public static int getProgressValueForGender(int paramInt)
  {
    int i = 1;
    if (paramInt == 0) {
      paramInt = i;
    }
    for (;;)
    {
      return paramInt;
      if (paramInt == 1) {
        paramInt = 0;
      } else {
        paramInt = -1;
      }
    }
  }
  
  public static String getStringValueForGender(int paramInt)
  {
    String str;
    if (paramInt == 1) {
      str = GENDER_MALE;
    }
    for (;;)
    {
      return str;
      if (paramInt == 0) {
        str = GENDER_FEMALE;
      } else {
        str = "";
      }
    }
  }
  
  public static int getValueForGenderString(String paramString)
  {
    int i = 0;
    if (paramString == null) {}
    for (;;)
    {
      return i;
      if (paramString.equalsIgnoreCase(GENDER_MALE)) {
        i = 1;
      } else if (!paramString.equalsIgnoreCase(GENDER_FEMALE)) {
        i = -1;
      }
    }
  }
  
  public static int getValueForGenderToSetInDB(int paramInt)
  {
    int i = 1;
    if (paramInt == 0) {
      paramInt = i;
    }
    for (;;)
    {
      return paramInt;
      if (paramInt == 1) {
        paramInt = 0;
      } else {
        paramInt = -1;
      }
    }
  }
  
  private void init()
  {
    this.user = RefreshModelController.getInstance().getUser();
    float f2 = this.user.getProfile().getHeight();
    float f1 = this.user.getProfile().getWeight();
    int j = this.user.getProfile().getWeightUnit();
    int k = this.user.getProfile().getHeightUnit();
    int i = this.user.getProfile().getTemperatureUnit();
    System.out.println("Values in seq W H T " + j + k + i);
    boolean bool;
    Date localDate;
    if ((this.user.getProfile().getNeckSize() == -1) || (this.user.getProfile().getNeckSize() == 1))
    {
      bool = false;
      String str = this.user.getProfile().getNewGender();
      localDate = this.user.getProfile().getDateOfBirth();
      if (f2 != 0.0F) {
        break label358;
      }
      this.tempHeightMeters = initHeightDefaultData().intValue();
      this.tempHeightInch = MeasureManager.getInchFromMeters(initHeightDefaultData().intValue());
      label188:
      if (f1 != 0.0F) {
        break label374;
      }
      this.tempWeightKilogram = initWeightDefaultData().intValue();
      this.tempWeightPound = MeasureManager.getPoundFromKilogram(initWeightDefaultData().intValue());
      label221:
      this.tempCollarSize = Boolean.valueOf(bool);
      if ((!str.equals("")) && (!str.equals(GENDER_MALE))) {
        break label390;
      }
    }
    label358:
    label374:
    label390:
    for (this.tempGenderValue = Boolean.valueOf(false);; this.tempGenderValue = Boolean.valueOf(true))
    {
      this.heightMeters = f2;
      this.heightInch = MeasureManager.getInchFromMeters(f2);
      this.weightKilogram = f1;
      this.weightPound = MeasureManager.getPoundFromKilogram(f1);
      this.collarSize = Boolean.valueOf(bool);
      this.genderValue = this.tempGenderValue;
      this.birth = null;
      this.tempBirth = localDate;
      this.weightUnit = j;
      this.tempWeightUnit = j;
      this.heightUnit = k;
      this.tempHeightUnit = k;
      this.temperatureUnit = i;
      this.tempTemperatureUnit = i;
      initBirthDayDefaultData();
      return;
      bool = true;
      break;
      this.tempHeightMeters = f2;
      this.tempHeightInch = MeasureManager.getInchFromMeters(f2);
      break label188;
      this.tempWeightKilogram = f1;
      this.tempWeightPound = MeasureManager.getPoundFromKilogram(f1);
      break label221;
    }
  }
  
  private void initBirthDayDefaultData()
  {
    if ((RefreshModelController.getInstance().getUser() != null) && (RefreshModelController.getInstance().getUser().getProfile().getDateOfBirth() != null)) {
      this.birth = RefreshModelController.getInstance().getUser().getProfile().getDateOfBirth();
    }
  }
  
  private Integer initHeightDefaultData()
  {
    return this.HEIGHT_DEFAULT_VALUE;
  }
  
  private Integer initWeightDefaultData()
  {
    return this.WEIGHT_DEFAULT_VALUE;
  }
  
  public static void logout()
  {
    userProfileDataManager = null;
  }
  
  private void save()
  {
    RefreshModelController.getInstance().save();
  }
  
  private void setTempBirth(Date paramDate)
  {
    this.tempBirth = paramDate;
  }
  
  private void setTempCollarSize(Integer paramInteger)
  {
    if (paramInteger.intValue() == 0) {}
    for (boolean bool = true;; bool = false)
    {
      this.tempCollarSize = Boolean.valueOf(bool);
      return;
    }
  }
  
  private void setTempGender(Integer paramInteger)
  {
    boolean bool = true;
    if (paramInteger.intValue() == 1) {}
    for (;;)
    {
      this.tempGenderValue = Boolean.valueOf(bool);
      return;
      bool = false;
    }
  }
  
  private void setTempHeight(Integer paramInteger)
  {
    if (this.user.getSettings().getHeightUnit() == 0) {
      this.tempHeightInch = paramInteger.intValue();
    }
    for (this.tempHeightMeters = MeasureManager.getMetersFromInch(paramInteger.intValue());; this.tempHeightMeters = paramInteger.intValue())
    {
      return;
      this.tempHeightInch = MeasureManager.getInchFromMeters(paramInteger.intValue());
    }
  }
  
  private void setTempHeightUnit(int paramInt)
  {
    this.tempHeightUnit = paramInt;
  }
  
  private void setTempTemperatureUnit(int paramInt)
  {
    this.tempTemperatureUnit = paramInt;
  }
  
  private void setTempWeight(Integer paramInteger)
  {
    if (this.user.getSettings().getWeightUnit() == 1)
    {
      this.tempWeightKilogram = paramInteger.intValue();
      this.tempWeightPound = MeasureManager.getPoundFromKilogram(paramInteger.intValue());
    }
    for (;;)
    {
      return;
      if ((this.user.getSettings().getWeightUnit() == 0) || (this.user.getSettings().getWeightUnit() == 2))
      {
        this.tempWeightPound = paramInteger.intValue();
        this.tempWeightKilogram = MeasureManager.getKilogramFromPound(paramInteger.intValue());
      }
    }
  }
  
  private void setTempWeightUnit(int paramInt)
  {
    this.tempWeightUnit = paramInt;
  }
  
  public Integer getCurrentPicker()
  {
    return this.currentPicker;
  }
  
  public Object getCurrentValue()
  {
    Object localObject;
    if (this.currentPicker == PICKER_BIRTH) {
      localObject = this.tempBirth;
    }
    for (;;)
    {
      return localObject;
      if (this.currentPicker == PICKER_GENDER)
      {
        if (GENDER_FEMALE_IN_BUTTON.equals(this.tempGenderValue)) {
          localObject = Integer.valueOf(1);
        } else {
          localObject = Integer.valueOf(0);
        }
      }
      else
      {
        float f;
        if (this.currentPicker == PICKER_HEIGHT)
        {
          f = 0.0F;
          if (this.user.getSettings().getHeightUnit() == 1) {
            f = this.tempHeightMeters;
          }
          for (;;)
          {
            localObject = new Integer(Math.round(f));
            break;
            if (this.user.getSettings().getHeightUnit() == 0) {
              f = this.tempHeightInch;
            }
          }
        }
        if (this.currentPicker == PICKER_WEIGHT)
        {
          f = 0.0F;
          if ((this.user.getSettings().getWeightUnit() == 0) || (this.user.getSettings().getWeightUnit() == 2)) {
            f = this.tempWeightPound;
          }
          for (;;)
          {
            localObject = new Integer(Math.round(f));
            break;
            if (this.user.getSettings().getWeightUnit() == 1) {
              f = this.tempWeightKilogram;
            }
          }
        }
        if (this.currentPicker == PICKER_COLLAR_SIZE) {
          localObject = this.tempCollarSize;
        } else if (this.currentPicker == PICKER_WEIGHT_UNIT) {
          localObject = Integer.valueOf(this.tempWeightUnit);
        } else if (this.currentPicker == PICKER_HEIGHT_UNIT) {
          localObject = Integer.valueOf(this.tempHeightUnit);
        } else if (this.currentPicker == PICKER_TEMP_UNIT) {
          localObject = Integer.valueOf(this.tempTemperatureUnit);
        } else {
          localObject = null;
        }
      }
    }
  }
  
  public boolean getGenderValue()
  {
    return this.genderValue.booleanValue();
  }
  
  public String getLabelForAge()
  {
    if (this.birth == null) {}
    for (String str = "Age";; str = getDiffYears(this.birth).toString()) {
      return str;
    }
  }
  
  public String getLabelForBithDate()
  {
    new SimpleDateFormat("dd/MM/yyyy");
    if (this.birth == null) {
      if ((RefreshModelController.getInstance().getUser() != null) && (RefreshModelController.getInstance().getUser().getProfile().getDateOfBirth() != null)) {
        this.birth = RefreshModelController.getInstance().getUser().getProfile().getDateOfBirth();
      }
    }
    for (String str = this.resources.getString(2131165911);; str = DateFormat.getDateInstance().format(this.birth)) {
      return str;
    }
  }
  
  public String getLabelForCollarSize()
  {
    if (this.collarSize.booleanValue()) {}
    for (String str = COLLAR_SIZE_BIG_IN_BUTTON;; str = COLLAR_SIZE_SMALL_IN_BUTTON) {
      return str;
    }
  }
  
  public String getLabelForGender()
  {
    if (this.genderValue.booleanValue()) {}
    for (String str = GENDER_MALE_IN_BUTTON;; str = GENDER_FEMALE_IN_BUTTON) {
      return str;
    }
  }
  
  public String getLabelForHeight()
  {
    if (this.heightMeters == 0.0F) {}
    for (String str1 = this.resources.getString(2131165909);; str1 = Math.round(this.heightMeters) + " cm")
    {
      return str1;
      if (this.user.getSettings().getHeightUnit() != 1) {
        break;
      }
    }
    int i = (int)Math.floor(this.heightInch / 12.0F);
    int j = Math.round(this.heightInch % 12.0F);
    if (i > 0)
    {
      str1 = i + "' ";
      label117:
      if (j <= 0) {
        break label172;
      }
    }
    label172:
    for (String str2 = j + "\" ";; str2 = "")
    {
      str1 = str1 + str2;
      break;
      str1 = "";
      break label117;
    }
  }
  
  public String getLabelForHeightUnit()
  {
    return heightUnitArray[this.heightUnit];
  }
  
  public String getLabelForPickerFragment()
  {
    String str;
    if (this.currentPicker == PICKER_BIRTH) {
      str = getLabelForAge();
    }
    for (;;)
    {
      return str;
      if (this.currentPicker == PICKER_GENDER) {
        str = getLabelForGender();
      } else if (this.currentPicker == PICKER_HEIGHT) {
        str = getLabelForHeight();
      } else if (this.currentPicker == PICKER_WEIGHT) {
        str = getLabelForWeight();
      } else if (this.currentPicker == PICKER_COLLAR_SIZE) {
        str = getLabelForCollarSize();
      } else if (this.currentPicker == PICKER_WEIGHT_UNIT) {
        str = getLabelForWeightUnit();
      } else if (this.currentPicker == PICKER_HEIGHT_UNIT) {
        str = getLabelForHeightUnit();
      } else if (this.currentPicker == PICKER_TEMP_UNIT) {
        str = getLabelForTemperatureUnit();
      } else {
        str = "";
      }
    }
  }
  
  public String getLabelForTemperatureUnit()
  {
    return temperatureUnitArray[this.temperatureUnit];
  }
  
  public String getLabelForWeight()
  {
    String str1;
    if (this.weightKilogram == 0.0F) {
      str1 = this.resources.getString(2131165910);
    }
    for (;;)
    {
      return str1;
      System.out.println("######  weight unit2 :" + this.user.getSettings().getWeightUnit());
      System.out.println("######  weight unit3 :" + this.user.getProfile().getWeightUnit());
      if (this.user.getSettings().getWeightUnit() == 2)
      {
        float f = MeasureManager.getPoundFromKilogram(this.weightKilogram);
        int j = Math.round(f) / 14;
        int i = Math.round(f) % 14;
        if (j > 0)
        {
          str1 = j + " st ";
          label152:
          if (i <= 0) {
            break label210;
          }
        }
        label210:
        for (String str2 = i + " lb";; str2 = "")
        {
          str1 = str1 + str2;
          break;
          str1 = "";
          break label152;
        }
      }
      if (this.user.getSettings().getWeightUnit() == 1) {
        str1 = Math.round(this.weightKilogram) + " kg";
      } else {
        str1 = Math.round(this.weightPound) + " lb";
      }
    }
  }
  
  public String getLabelForWeightUnit()
  {
    return weightUnitArray[this.weightUnit];
  }
  
  public String getNameValue()
  {
    if ((this.user.getFirstName().length() == 0) || (this.user.getFamilyName().length() == 0) || (this.user.getFirstName().equals("none")) || (this.user.getFamilyName().equals("none"))) {}
    for (String str = this.user.getEmail();; str = this.user.getFirstName() + " " + this.user.getFamilyName()) {
      return str;
    }
  }
  
  public String getStringValueForGender()
  {
    if (this.genderValue.booleanValue()) {}
    for (String str = GENDER_MALE;; str = GENDER_FEMALE) {
      return str;
    }
  }
  
  public boolean isNeckBig()
  {
    return this.collarSize.booleanValue();
  }
  
  public boolean isValidBirth()
  {
    if (this.birth != null) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public boolean isValidHeigth()
  {
    if (this.heightMeters != 0.0F) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public boolean isValidWeight()
  {
    if (this.weightKilogram != 0.0F) {}
    for (boolean bool = true;; bool = false) {
      return bool;
    }
  }
  
  public void performDoneButton()
  {
    if (this.currentPicker == PICKER_BIRTH) {
      setBirthValue();
    }
    if (this.currentPicker == PICKER_GENDER) {
      setGenderValue();
    }
    if (this.currentPicker == PICKER_HEIGHT) {
      setHeightValue();
    }
    if (this.currentPicker == PICKER_WEIGHT) {
      setWeightValue();
    }
    if (this.currentPicker == PICKER_COLLAR_SIZE) {
      setCollarSizeValue();
    }
    if (this.currentPicker == PICKER_WEIGHT_UNIT) {
      setWeightUnitValue();
    }
    if (this.currentPicker == PICKER_HEIGHT_UNIT) {
      setHeightUnitValue();
    }
    if (this.currentPicker == PICKER_TEMP_UNIT) {
      setTemperatureUnitValue();
    }
  }
  
  public void setBirthValue()
  {
    this.birth = this.tempBirth;
    this.user.getProfile().setDateOfBirth(this.birth);
    save();
  }
  
  public void setCollarSizeValue()
  {
    this.collarSize = this.tempCollarSize;
    if (this.collarSize.booleanValue()) {
      this.user.getProfile().setNeckSize(0);
    }
    for (;;)
    {
      save();
      return;
      this.user.getProfile().setNeckSize(1);
    }
  }
  
  public void setCurrentPicker(Integer paramInteger)
  {
    this.currentPicker = paramInteger;
  }
  
  public void setGenderValue()
  {
    this.genderValue = this.tempGenderValue;
    if (this.genderValue.booleanValue()) {
      this.user.getProfile().setNewGender("F");
    }
    for (;;)
    {
      save();
      return;
      this.user.getProfile().setNewGender("M");
    }
  }
  
  public void setHeightUnitValue()
  {
    this.heightUnit = this.tempHeightUnit;
    this.user.getProfile().setHeightUnit(this.heightUnit);
    this.user.getSettings().setHeightUnit(this.heightUnit);
    save();
  }
  
  public void setHeightValue()
  {
    this.heightMeters = this.tempHeightMeters;
    this.heightInch = this.tempHeightInch;
    this.user.getProfile().setHeight(this.heightMeters);
    save();
  }
  
  public void setTempValue(Object paramObject)
  {
    if (this.currentPicker == PICKER_BIRTH) {
      setTempBirth((Date)paramObject);
    }
    for (;;)
    {
      return;
      if (this.currentPicker == PICKER_GENDER) {
        setTempGender((Integer)paramObject);
      } else if (this.currentPicker == PICKER_HEIGHT) {
        setTempHeight((Integer)paramObject);
      } else if (this.currentPicker == PICKER_WEIGHT) {
        setTempWeight((Integer)paramObject);
      } else if (this.currentPicker == PICKER_COLLAR_SIZE) {
        setTempCollarSize((Integer)paramObject);
      } else if (this.currentPicker == PICKER_WEIGHT_UNIT) {
        setTempWeightUnit(((Integer)paramObject).intValue());
      } else if (this.currentPicker == PICKER_HEIGHT_UNIT) {
        setTempHeightUnit(((Integer)paramObject).intValue());
      } else if (this.currentPicker == PICKER_TEMP_UNIT) {
        setTempTemperatureUnit(((Integer)paramObject).intValue());
      }
    }
  }
  
  public void setTemperatureUnitValue()
  {
    boolean bool = true;
    this.temperatureUnit = this.tempTemperatureUnit;
    this.user.getProfile().setTemperatureUnit(this.temperatureUnit);
    this.user.getSettings().setTemperatureUnit(this.temperatureUnit);
    save();
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    if (this.temperatureUnit == 1) {}
    for (;;)
    {
      localRefreshModelController.setUseMetricUnits(bool);
      return;
      bool = false;
    }
  }
  
  public void setWeightUnitValue()
  {
    this.weightUnit = this.tempWeightUnit;
    this.user.getProfile().setWeightUnit(this.weightUnit);
    this.user.getSettings().setWeightUnit(this.weightUnit);
    save();
    System.out.println("######  weight unit :" + this.weightUnit);
    System.out.println("######  weight unit1 :" + this.user.getSettings().getWeightUnit());
  }
  
  public void setWeightValue()
  {
    this.weightKilogram = this.tempWeightKilogram;
    this.weightPound = this.tempWeightPound;
    this.user.getProfile().setWeight(this.weightKilogram);
    save();
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
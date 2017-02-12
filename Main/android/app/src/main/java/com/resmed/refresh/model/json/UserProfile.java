package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

public class UserProfile
{
  @SerializedName("OTC_Concern")
  private int OTCMedicationConcern;
  @SerializedName("OTC_Frequency")
  private int OTCMedicationFrequency;
  @SerializedName("OTC_Medication")
  private int OTCMedicationType;
  @SerializedName("MajorSleepProblem")
  private int affectsYourSleep;
  @SerializedName("BedTime")
  private int bedTime;
  @SerializedName("Children")
  private int childrenCount;
  @SerializedName("ConcernsOnSleepPblm")
  private int concernsOnSleepProblem;
  @SerializedName("ConsumeAlcohol")
  private int consumeAlcohol;
  @SerializedName("TemperatureControlled")
  private int controlBedroomTemperature;
  @SerializedName("CountryCode")
  private String countryCode;
  @SerializedName("DateOfBirth")
  private String dateOfBirth = "1970-01-01'T'00:00:00'Z";
  @SerializedName("ExerciseBeforeBed")
  private int exerciseBeforeBed;
  @SerializedName("ExerciseIntensity")
  private int exerciseIntensity;
  @SerializedName("InterferenceOnLife")
  private int extentInterfereSleepProblem;
  @SerializedName("Gender")
  private String gender = "M";
  @SerializedName("WakeUpOnDisturbance")
  private int getWokenUpInNightByAny;
  @SerializedName("HasPet")
  private boolean hasPet;
  @SerializedName("HasPetInBed")
  private boolean hasPetInBed;
  @SerializedName("HasSleepPartner")
  private int hasSleepPartner;
  @SerializedName("Height")
  private float height;
  @SerializedName("HeightUnit")
  private int heightUnit;
  @SerializedName("Herbal_Frequency")
  private int herbalRemediesFrequency;
  @SerializedName("Herbal_Medication")
  private int herbalRemediesType;
  @SerializedName("HighBP")
  private int highBloodPressure;
  @SerializedName("ElectronicsUse")
  private int howOftenElectronicsUsed;
  @SerializedName("SleepIllness")
  private int illnessForSleep;
  @SerializedName("UseAirPurifier")
  private int isAirPurifierUsed;
  @SerializedName("Obstruction")
  private int isBreathingObstruction;
  @SerializedName("Crawling")
  private int isCrawlingSensation;
  @SerializedName("DryMouth")
  private int isDryMouth;
  @SerializedName("UseHumidifer")
  private int isHumidifierUsed;
  @SerializedName("StillLegs")
  private int isLegsStillAtNight;
  @SerializedName("UseToppers")
  private int isMattressPadTopperOnBed;
  @SerializedName("MuscleTension")
  private int isMuscleTension;
  @SerializedName("NasalCongestion")
  private int isNasalCongestion;
  @SerializedName("SleepPartnerExists")
  private int isSleepWithPartner;
  @SerializedName("SoundSoothingUsed")
  private int isSoundUsedInSleep;
  @SerializedName("SleepyDayTime")
  private int isStayingAwakeDuringDay;
  @SerializedName("Allergies")
  private int isSufferFromAllergies;
  @SerializedName("LightLevel")
  private int levelOfLightInBedroom;
  @SerializedName("Locale")
  private String locale;
  @SerializedName("Location")
  private Location location;
  @SerializedName("LocationPermission")
  private boolean locationPermission;
  @SerializedName("MachineManufacturer")
  private int machineManufacturer;
  @SerializedName("MattressAge")
  private int mattressAge;
  @SerializedName("Brand")
  private int mattressBrand;
  @SerializedName("Size")
  private int mattressSize;
  @SerializedName("MattressType")
  private int mattressType;
  @SerializedName("Medication")
  private int medication;
  @SerializedName("MedicationFreq")
  private int medicationFrequency;
  @SerializedName("MorningAlertness")
  private int morningAlertness;
  @SerializedName("NapMins")
  private int napTime;
  @SerializedName("NapTime")
  private int naps;
  @SerializedName("LargeNeck")
  private int neckSize;
  @SerializedName("NoiseLevel")
  private int noiseLevelInBedroom;
  @SerializedName("NumCaffeineDrinks")
  private int numCaffeineDrinks;
  @SerializedName("NumCigarettes")
  private int numCigarettes;
  @SerializedName("NumDrinks")
  private int numDrinks;
  @SerializedName("NumExercises")
  private int numExercises;
  @SerializedName("Other_TemperatureControlled")
  private String otherControlBedroomTemperature;
  @SerializedName("Other_Herbal")
  private String otherHerbalRemediesType;
  @SerializedName("Other_MachineManufacturer")
  private String otherMachineManufacturer;
  @SerializedName("Other_Brand")
  private String otherMattressBrand;
  @SerializedName("Other_MattressType")
  private String otherMattressType;
  @SerializedName("Other_PillowType")
  private String otherPillowsType;
  @SerializedName("Other_PP_Type")
  private String otherPrescriptionPillsType;
  @SerializedName("Other_LightReducer")
  private String otherReduceAmountOfLightInBedroom;
  @SerializedName("Other_LightSource")
  private String otherSourceOfLightInBedroom;
  @SerializedName("Other_SoundSource")
  private String otherTypeOfSoundUsedInSleep;
  @SerializedName("SleepsWithPet")
  private int petSleepWithYouOnBed;
  @SerializedName("PillowAge")
  private int pillowsAge;
  @SerializedName("PillowType")
  private int pillowsType;
  @SerializedName("PP_Concern")
  private int prescriptionPillsConcern;
  @SerializedName("PP_Frequency")
  private int prescriptionPillsFrequency;
  @SerializedName("PrescriptionPills")
  private int prescriptionPillsType;
  @SerializedName("PushNotifications")
  private boolean pushNotifications;
  @SerializedName("LightReducer")
  private int reduceAmountOfLightInBedroom;
  @SerializedName("SleepDisorder")
  private int sleepDisorder;
  @SerializedName("SleepDisorderMachine")
  private int sleepDisorderMachine;
  @SerializedName("SleepMover")
  private int sleepMover;
  @SerializedName("SleepProblems")
  private int sleepProblems;
  @SerializedName("SleepPosition")
  private int sleepingPosition;
  @SerializedName("PositionalSnore")
  private int sleepingPositionSnore;
  @SerializedName("IsSmoker")
  private int smoker;
  @SerializedName("SnoringFreq")
  private int snoreFrequency;
  @SerializedName("IsSnorer")
  private int snorer;
  @SerializedName("LightSource")
  private int sourceOfLightInBedroom;
  @SerializedName("SpecialLights")
  private int specialLightUsedInSleep;
  @SerializedName("StopsBreathing")
  private int stopsBreathing;
  @SerializedName("StressFrequency")
  private int stressedFrequency;
  @SerializedName("Tac1")
  private boolean tac1;
  @SerializedName("Tac2")
  private boolean tac2;
  @SerializedName("Tac3")
  private boolean tac3;
  @SerializedName("TemperatureUnit")
  private int temperatureUnit;
  @SerializedName("TimeZone")
  private int timeZone;
  @SerializedName("IsTired")
  private boolean tiredDaytime;
  @SerializedName("TravelImpact")
  private int travelImpact;
  @SerializedName("SoundSource")
  private int typeOfSoundUsedInSleep;
  @SerializedName("TypeofPerson")
  int typeofPerson;
  @SerializedName("UseMetricUnits")
  private boolean useMetricUnits;
  @SerializedName("UsingSleepAids")
  private int usingSleepAids;
  @SerializedName("WakeUpDifficulties")
  private int wakeUpDifficulties;
  @SerializedName("Weight")
  private float weight;
  @SerializedName("WeightUnit")
  private int weightUnit;
  
  public int getAffectsYourSleep()
  {
    return this.affectsYourSleep;
  }
  
  public int getBedTime()
  {
    return this.bedTime;
  }
  
  public int getChildrenCount()
  {
    return this.childrenCount;
  }
  
  public int getConcernsOnSleepProblem()
  {
    return this.concernsOnSleepProblem;
  }
  
  public int getConsumeAlcohol()
  {
    return this.consumeAlcohol;
  }
  
  public int getControlBedroomTemperature()
  {
    return this.controlBedroomTemperature;
  }
  
  public String getCountryCode()
  {
    return this.countryCode;
  }
  
  public String getDateOfBirth()
  {
    return this.dateOfBirth;
  }
  
  public int getExerciseBeforeBed()
  {
    return this.exerciseBeforeBed;
  }
  
  public int getExerciseIntensity()
  {
    return this.exerciseIntensity;
  }
  
  public int getExtentInterfereSleepProblem()
  {
    return this.extentInterfereSleepProblem;
  }
  
  public String getGender()
  {
    return this.gender;
  }
  
  public int getGetWokenUpInNightByAny()
  {
    return this.getWokenUpInNightByAny;
  }
  
  public boolean getHasPet()
  {
    return this.hasPet;
  }
  
  public boolean getHasPetInBed()
  {
    return this.hasPetInBed;
  }
  
  public int getHasSleepPartner()
  {
    return this.hasSleepPartner;
  }
  
  public float getHeight()
  {
    return this.height;
  }
  
  public int getHeightUnit()
  {
    return this.heightUnit;
  }
  
  public int getHerbalRemediesFrequency()
  {
    return this.herbalRemediesFrequency;
  }
  
  public int getHerbalRemediesType()
  {
    return this.herbalRemediesType;
  }
  
  public int getHighBloodPressure()
  {
    return this.highBloodPressure;
  }
  
  public int getHowOftenElectronicsUsed()
  {
    return this.howOftenElectronicsUsed;
  }
  
  public int getIllnessForSleep()
  {
    return this.illnessForSleep;
  }
  
  public int getLevelOfLightInBedroom()
  {
    return this.levelOfLightInBedroom;
  }
  
  public String getLocale()
  {
    return this.locale;
  }
  
  public Location getLocation()
  {
    return this.location;
  }
  
  public boolean getLocationPermission()
  {
    return this.locationPermission;
  }
  
  public int getMachineManufacturer()
  {
    return this.machineManufacturer;
  }
  
  public int getMattressAge()
  {
    return this.mattressAge;
  }
  
  public int getMattressBrand()
  {
    return this.mattressBrand;
  }
  
  public int getMattressSize()
  {
    return this.mattressSize;
  }
  
  public int getMattressType()
  {
    return this.mattressType;
  }
  
  public int getMedication()
  {
    return this.medication;
  }
  
  public int getMedicationFrequency()
  {
    return this.medicationFrequency;
  }
  
  public int getMorningAlertness()
  {
    return this.morningAlertness;
  }
  
  public int getNapTime()
  {
    return this.napTime;
  }
  
  public int getNaps()
  {
    return this.naps;
  }
  
  public int getNeckSize()
  {
    return this.neckSize;
  }
  
  public int getNoiseLevelInBedroom()
  {
    return this.noiseLevelInBedroom;
  }
  
  public int getNumCaffeineDrinks()
  {
    return this.numCaffeineDrinks;
  }
  
  public int getNumCigarettes()
  {
    return this.numCigarettes;
  }
  
  public int getNumDrinks()
  {
    return this.numDrinks;
  }
  
  public int getNumExercises()
  {
    return this.numExercises;
  }
  
  public int getOTCMedicationConcern()
  {
    return this.OTCMedicationConcern;
  }
  
  public int getOTCMedicationFrequency()
  {
    return this.OTCMedicationFrequency;
  }
  
  public int getOTCMedicationType()
  {
    return this.OTCMedicationType;
  }
  
  public String getOtherControlBedroomTemperature()
  {
    return this.otherControlBedroomTemperature;
  }
  
  public String getOtherHerbalRemediesType()
  {
    return this.otherHerbalRemediesType;
  }
  
  public String getOtherMachineManufacturer()
  {
    return this.otherMachineManufacturer;
  }
  
  public String getOtherMattressBrand()
  {
    return this.otherMattressBrand;
  }
  
  public String getOtherMattressType()
  {
    return this.otherMattressType;
  }
  
  public String getOtherPillowsType()
  {
    return this.otherPillowsType;
  }
  
  public String getOtherPrescriptionPillsType()
  {
    return this.otherPrescriptionPillsType;
  }
  
  public String getOtherReduceAmountOfLightInBedroom()
  {
    return this.otherReduceAmountOfLightInBedroom;
  }
  
  public String getOtherSourceOfLightInBedroom()
  {
    return this.otherSourceOfLightInBedroom;
  }
  
  public String getOtherTypeOfSoundUsedInSleep()
  {
    return this.otherTypeOfSoundUsedInSleep;
  }
  
  public int getPetSleepWithYouOnBed()
  {
    return this.petSleepWithYouOnBed;
  }
  
  public int getPillowsAge()
  {
    return this.pillowsAge;
  }
  
  public int getPillowsType()
  {
    return this.pillowsType;
  }
  
  public int getPrescriptionPillsConcern()
  {
    return this.prescriptionPillsConcern;
  }
  
  public int getPrescriptionPillsFrequency()
  {
    return this.prescriptionPillsFrequency;
  }
  
  public int getPrescriptionPillsType()
  {
    return this.prescriptionPillsType;
  }
  
  public boolean getPushNotifications()
  {
    return this.pushNotifications;
  }
  
  public int getReduceAmountOfLightInBedroom()
  {
    return this.reduceAmountOfLightInBedroom;
  }
  
  public int getSleepDisorder()
  {
    return this.sleepDisorder;
  }
  
  public int getSleepDisorderMachine()
  {
    return this.sleepDisorderMachine;
  }
  
  public int getSleepMover()
  {
    return this.sleepMover;
  }
  
  public int getSleepProblems()
  {
    return this.sleepProblems;
  }
  
  public int getSleepingPosition()
  {
    return this.sleepingPosition;
  }
  
  public int getSleepingPositionSnore()
  {
    return this.sleepingPositionSnore;
  }
  
  public int getSmoker()
  {
    return this.smoker;
  }
  
  public int getSnoreFrequency()
  {
    return this.snoreFrequency;
  }
  
  public int getSnorer()
  {
    return this.snorer;
  }
  
  public int getSourceOfLightInBedroom()
  {
    return this.sourceOfLightInBedroom;
  }
  
  public int getSpecialLightUsedInSleep()
  {
    return this.specialLightUsedInSleep;
  }
  
  public int getStopsBreathing()
  {
    return this.stopsBreathing;
  }
  
  public int getStressedFrequency()
  {
    return this.stressedFrequency;
  }
  
  public boolean getTac1()
  {
    return this.tac1;
  }
  
  public boolean getTac2()
  {
    return this.tac2;
  }
  
  public boolean getTac3()
  {
    return this.tac3;
  }
  
  public int getTemperatureUnit()
  {
    return this.temperatureUnit;
  }
  
  public int getTimeZone()
  {
    return this.timeZone;
  }
  
  public boolean getTiredDaytime()
  {
    return this.tiredDaytime;
  }
  
  public int getTravelImpact()
  {
    return this.travelImpact;
  }
  
  public int getTypeOfSoundUsedInSleep()
  {
    return this.typeOfSoundUsedInSleep;
  }
  
  public int getTypeofPerson()
  {
    return this.typeofPerson;
  }
  
  public boolean getUseMetricUnits()
  {
    return this.useMetricUnits;
  }
  
  public int getUsingSleepAids()
  {
    return this.usingSleepAids;
  }
  
  public int getWakeUpDifficulties()
  {
    return this.wakeUpDifficulties;
  }
  
  public float getWeight()
  {
    return this.weight;
  }
  
  public int getWeightUnit()
  {
    return this.weightUnit;
  }
  
  public int isAirPurifierUsed()
  {
    return this.isAirPurifierUsed;
  }
  
  public int isBreathingObstruction()
  {
    return this.isBreathingObstruction;
  }
  
  public int isCrawlingSensation()
  {
    return this.isCrawlingSensation;
  }
  
  public int isDryMouth()
  {
    return this.isDryMouth;
  }
  
  public int isHumidifierUsed()
  {
    return this.isHumidifierUsed;
  }
  
  public int isLegsStillAtNight()
  {
    return this.isLegsStillAtNight;
  }
  
  public int isMattressPadTopperOnBed()
  {
    return this.isMattressPadTopperOnBed;
  }
  
  public int isMuscleTension()
  {
    return this.isMuscleTension;
  }
  
  public int isNasalCongestion()
  {
    return this.isNasalCongestion;
  }
  
  public int isSleepWithPartner()
  {
    return this.isSleepWithPartner;
  }
  
  public int isSoundUsedInSleep()
  {
    return this.isSoundUsedInSleep;
  }
  
  public int isStayingAwakeDuringDay()
  {
    return this.isStayingAwakeDuringDay;
  }
  
  public int isSufferFromAllergies()
  {
    return this.isSufferFromAllergies;
  }
  
  public void setAffectsYourSleep(int paramInt)
  {
    this.affectsYourSleep = paramInt;
  }
  
  public void setAirPurifierUsed(int paramInt)
  {
    this.isAirPurifierUsed = paramInt;
  }
  
  public void setBedTime(int paramInt)
  {
    this.bedTime = paramInt;
  }
  
  public void setBreathingObstruction(int paramInt)
  {
    this.isBreathingObstruction = paramInt;
  }
  
  public void setChildrenCount(int paramInt)
  {
    this.childrenCount = paramInt;
  }
  
  public void setConcernsOnSleepProblem(int paramInt)
  {
    this.concernsOnSleepProblem = paramInt;
  }
  
  public void setConsumeAlcohol(int paramInt)
  {
    this.consumeAlcohol = paramInt;
  }
  
  public void setControlBedroomTemperature(int paramInt)
  {
    this.controlBedroomTemperature = paramInt;
  }
  
  public void setCountryCode(String paramString)
  {
    this.countryCode = paramString;
  }
  
  public void setCrawlingSensation(int paramInt)
  {
    this.isCrawlingSensation = paramInt;
  }
  
  public void setDateOfBirth(String paramString)
  {
    this.dateOfBirth = paramString;
  }
  
  public void setDryMouth(int paramInt)
  {
    this.isDryMouth = paramInt;
  }
  
  public void setExerciseBeforeBed(int paramInt)
  {
    this.exerciseBeforeBed = paramInt;
  }
  
  public void setExerciseIntensity(int paramInt)
  {
    this.exerciseIntensity = paramInt;
  }
  
  public void setExtentInterfereSleepProblem(int paramInt)
  {
    this.extentInterfereSleepProblem = paramInt;
  }
  
  public void setGender(String paramString)
  {
    this.gender = paramString;
  }
  
  public void setGetWokenUpInNightByAny(int paramInt)
  {
    this.getWokenUpInNightByAny = paramInt;
  }
  
  public void setHasPet(boolean paramBoolean)
  {
    this.hasPet = paramBoolean;
  }
  
  public void setHasPetInBed(boolean paramBoolean)
  {
    this.hasPetInBed = paramBoolean;
  }
  
  public void setHasSleepPartner(int paramInt)
  {
    this.hasSleepPartner = paramInt;
  }
  
  public void setHeight(float paramFloat)
  {
    this.height = paramFloat;
  }
  
  public void setHeightUnit(int paramInt)
  {
    this.heightUnit = paramInt;
  }
  
  public void setHerbalRemediesFrequency(int paramInt)
  {
    this.herbalRemediesFrequency = paramInt;
  }
  
  public void setHerbalRemediesType(int paramInt)
  {
    this.herbalRemediesType = paramInt;
  }
  
  public void setHighBloodPressure(int paramInt)
  {
    this.highBloodPressure = paramInt;
  }
  
  public void setHowOftenElectronicsUsed(int paramInt)
  {
    this.howOftenElectronicsUsed = paramInt;
  }
  
  public void setHumidifierUsed(int paramInt)
  {
    this.isHumidifierUsed = paramInt;
  }
  
  public void setIllnessForSleep(int paramInt)
  {
    this.illnessForSleep = paramInt;
  }
  
  public void setLegsStillAtNight(int paramInt)
  {
    this.isLegsStillAtNight = paramInt;
  }
  
  public void setLevelOfLightInBedroom(int paramInt)
  {
    this.levelOfLightInBedroom = paramInt;
  }
  
  public void setLocale(String paramString)
  {
    this.locale = paramString;
  }
  
  public void setLocation(Location paramLocation)
  {
    this.location = paramLocation;
  }
  
  public void setLocationPermission(boolean paramBoolean)
  {
    this.locationPermission = paramBoolean;
  }
  
  public void setMachineManufacturer(int paramInt)
  {
    this.machineManufacturer = paramInt;
  }
  
  public void setMattressAge(int paramInt)
  {
    this.mattressAge = paramInt;
  }
  
  public void setMattressBrand(int paramInt)
  {
    this.mattressBrand = paramInt;
  }
  
  public void setMattressPadTopperOnBed(int paramInt)
  {
    this.isMattressPadTopperOnBed = paramInt;
  }
  
  public void setMattressSize(int paramInt)
  {
    this.mattressSize = paramInt;
  }
  
  public void setMattressType(int paramInt)
  {
    this.mattressType = paramInt;
  }
  
  public void setMedication(int paramInt)
  {
    this.medication = paramInt;
  }
  
  public void setMedicationFrequency(int paramInt)
  {
    this.medicationFrequency = paramInt;
  }
  
  public void setMorningAlertness(int paramInt)
  {
    this.morningAlertness = paramInt;
  }
  
  public void setMuscleTension(int paramInt)
  {
    this.isMuscleTension = paramInt;
  }
  
  public void setNapTime(int paramInt)
  {
    this.napTime = paramInt;
  }
  
  public void setNaps(int paramInt)
  {
    this.naps = paramInt;
  }
  
  public void setNasalCongestion(int paramInt)
  {
    this.isNasalCongestion = paramInt;
  }
  
  public void setNeckSize(int paramInt)
  {
    this.neckSize = paramInt;
  }
  
  public void setNoiseLevelInBedroom(int paramInt)
  {
    this.noiseLevelInBedroom = paramInt;
  }
  
  public void setNumCaffeineDrinks(int paramInt)
  {
    this.numCaffeineDrinks = paramInt;
  }
  
  public void setNumCigarettes(int paramInt)
  {
    this.numCigarettes = paramInt;
  }
  
  public void setNumDrinks(int paramInt)
  {
    this.numDrinks = paramInt;
  }
  
  public void setNumExercises(int paramInt)
  {
    this.numExercises = paramInt;
  }
  
  public void setOTCMedicationConcern(int paramInt)
  {
    this.OTCMedicationConcern = paramInt;
  }
  
  public void setOTCMedicationFrequency(int paramInt)
  {
    this.OTCMedicationFrequency = paramInt;
  }
  
  public void setOTCMedicationType(int paramInt)
  {
    this.OTCMedicationType = paramInt;
  }
  
  public void setOtherControlBedroomTemperature(String paramString)
  {
    this.otherControlBedroomTemperature = paramString;
  }
  
  public void setOtherHerbalRemediesType(String paramString)
  {
    this.otherHerbalRemediesType = paramString;
  }
  
  public void setOtherMachineManufacturer(String paramString)
  {
    this.otherMachineManufacturer = paramString;
  }
  
  public void setOtherMattressBrand(String paramString)
  {
    this.otherMattressBrand = paramString;
  }
  
  public void setOtherMattressType(String paramString)
  {
    this.otherMattressType = paramString;
  }
  
  public void setOtherPillowsType(String paramString)
  {
    this.otherPillowsType = paramString;
  }
  
  public void setOtherPrescriptionPillsType(String paramString)
  {
    this.otherPrescriptionPillsType = paramString;
  }
  
  public void setOtherReduceAmountOfLightInBedroom(String paramString)
  {
    this.otherReduceAmountOfLightInBedroom = paramString;
  }
  
  public void setOtherSourceOfLightInBedroom(String paramString)
  {
    this.otherSourceOfLightInBedroom = paramString;
  }
  
  public void setOtherTypeOfSoundUsedInSleep(String paramString)
  {
    this.otherTypeOfSoundUsedInSleep = paramString;
  }
  
  public void setPetSleepWithYouOnBed(int paramInt)
  {
    this.petSleepWithYouOnBed = paramInt;
  }
  
  public void setPillowsAge(int paramInt)
  {
    this.pillowsAge = paramInt;
  }
  
  public void setPillowsType(int paramInt)
  {
    this.pillowsType = paramInt;
  }
  
  public void setPrescriptionPillsConcern(int paramInt)
  {
    this.prescriptionPillsConcern = paramInt;
  }
  
  public void setPrescriptionPillsFrequency(int paramInt)
  {
    this.prescriptionPillsFrequency = paramInt;
  }
  
  public void setPrescriptionPillsType(int paramInt)
  {
    this.prescriptionPillsType = paramInt;
  }
  
  public void setPushNotifications(boolean paramBoolean)
  {
    this.pushNotifications = paramBoolean;
  }
  
  public void setReduceAmountOfLightInBedroom(int paramInt)
  {
    this.reduceAmountOfLightInBedroom = paramInt;
  }
  
  public void setSleepDisorder(int paramInt)
  {
    this.sleepDisorder = paramInt;
  }
  
  public void setSleepDisorderMachine(int paramInt)
  {
    this.sleepDisorderMachine = paramInt;
  }
  
  public void setSleepMover(int paramInt)
  {
    this.sleepMover = paramInt;
  }
  
  public void setSleepProblems(int paramInt)
  {
    this.sleepProblems = paramInt;
  }
  
  public void setSleepWithPartner(int paramInt)
  {
    this.isSleepWithPartner = paramInt;
  }
  
  public void setSleepingPosition(int paramInt)
  {
    this.sleepingPosition = paramInt;
  }
  
  public void setSleepingPositionSnore(int paramInt)
  {
    this.sleepingPositionSnore = paramInt;
  }
  
  public void setSmoker(int paramInt)
  {
    this.smoker = paramInt;
  }
  
  public void setSnoreFrequency(int paramInt)
  {
    this.snoreFrequency = paramInt;
  }
  
  public void setSnorer(int paramInt)
  {
    this.snorer = paramInt;
  }
  
  public void setSoundUsedInSleep(int paramInt)
  {
    this.isSoundUsedInSleep = paramInt;
  }
  
  public void setSourceOfLightInBedroom(int paramInt)
  {
    this.sourceOfLightInBedroom = paramInt;
  }
  
  public void setSpecialLightUsedInSleep(int paramInt)
  {
    this.specialLightUsedInSleep = paramInt;
  }
  
  public void setStayingAwakeDuringDay(int paramInt)
  {
    this.isStayingAwakeDuringDay = paramInt;
  }
  
  public void setStopsBreathing(int paramInt)
  {
    this.stopsBreathing = paramInt;
  }
  
  public void setStressedFrequency(int paramInt)
  {
    this.stressedFrequency = paramInt;
  }
  
  public void setSufferFromAllergies(int paramInt)
  {
    this.isSufferFromAllergies = paramInt;
  }
  
  public void setTac1(boolean paramBoolean)
  {
    this.tac1 = paramBoolean;
  }
  
  public void setTac2(boolean paramBoolean)
  {
    this.tac2 = paramBoolean;
  }
  
  public void setTac3(boolean paramBoolean)
  {
    this.tac3 = paramBoolean;
  }
  
  public void setTemperatureUnit(int paramInt)
  {
    this.temperatureUnit = paramInt;
  }
  
  public void setTimeZone(int paramInt)
  {
    this.timeZone = paramInt;
  }
  
  public void setTiredDaytime(boolean paramBoolean)
  {
    this.tiredDaytime = paramBoolean;
  }
  
  public void setTravelImpact(int paramInt)
  {
    this.travelImpact = paramInt;
  }
  
  public void setTypeOfSoundUsedInSleep(int paramInt)
  {
    this.typeOfSoundUsedInSleep = paramInt;
  }
  
  public void setTypeofPerson(int paramInt)
  {
    this.typeofPerson = paramInt;
  }
  
  public void setUseMetricUnits(boolean paramBoolean)
  {
    this.useMetricUnits = paramBoolean;
  }
  
  public void setUsingSleepAids(int paramInt)
  {
    this.usingSleepAids = paramInt;
  }
  
  public void setWakeUpDifficulties(int paramInt)
  {
    this.wakeUpDifficulties = paramInt;
  }
  
  public void setWeight(float paramFloat)
  {
    this.weight = paramFloat;
  }
  
  public void setWeightUnit(int paramInt)
  {
    this.weightUnit = paramInt;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\json\UserProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
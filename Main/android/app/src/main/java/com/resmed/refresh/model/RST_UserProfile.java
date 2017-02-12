package com.resmed.refresh.model;

import java.util.Date;

public class RST_UserProfile
{
  private int OTCMedicationConcern;
  private int OTCMedicationFrequency = -1;
  private int OTCMedicationType;
  private int TemperatureUnit;
  private int WeightUnit;
  private int affectsYourSleep = -1;
  private int bedTime = -1;
  private int childrenCount = -1;
  private int concernsOnSleepProblem;
  private int consumeAlcohol;
  private int controlBedroomTemperature;
  private String countryCode;
  private Date dateOfBirth;
  private int exerciseBeforeBed = -1;
  private int exerciseIntensity;
  private int extentInterfereSleepProblem = -1;
  private int gender = -1;
  private int getWokenUpInNightByAny;
  private boolean hasPet;
  private boolean hasPetInBed;
  private int hasSleepPartner;
  private float height;
  private int heightUnit;
  private int herbalRemediesFrequency = -1;
  private int herbalRemediesType;
  private int highBloodPressure = -1;
  private int howOftenElectronicsUsed;
  private Long id;
  private int illnessForSleep = -1;
  private int isAirPurifierUsed = -1;
  private int isBreathingObstruction = -1;
  private int isCrawlingSensation = -1;
  private int isDryMouth = -1;
  private int isHumidifierUsed = -1;
  private int isLegsStillAtNight = -1;
  private int isMattressPadTopperOnBed = -1;
  private int isMuscleTension = -1;
  private int isNasalCongestion = -1;
  private int isSleepWithPartner = -1;
  private int isSoundUsedInSleep = -1;
  private int isStayingAwakeDuringDay = -1;
  private int isSufferFromAllergies = -1;
  private int levelOfLightInBedroom;
  private String locale;
  private int machineManufacturer = -1;
  private int mattressAge = -1;
  private int mattressBrand = -1;
  private int mattressSize = -1;
  private int mattressType = -1;
  private int medication;
  private int medicationFrequency;
  private int morningAlertness;
  private int napTime = -1;
  private int naps;
  private int neckSize = -1;
  private String newGender = "";
  private int noiseLevelInBedroom;
  private int numCaffeineDrinks;
  private int numCigarettes;
  private int numDrinks;
  private int numExercises;
  private String otherControlBedroomTemperature = "";
  private String otherHerbalRemediesType = "";
  private String otherMachineManufacturer = "";
  private String otherMattressBrand = "";
  private String otherMattressType = "";
  private String otherPillowsType = "";
  private String otherPrescriptionPillsType = "";
  private String otherReduceAmountOfLightInBedroom = "";
  private String otherSourceOfLightInBedroom = "";
  private String otherTypeOfSoundUsedInSleep = "";
  private int petSleepWithYouOnBed;
  private int pillowsAge = -1;
  private int pillowsType;
  private int prescriptionPillsConcern;
  private int prescriptionPillsFrequency = -1;
  private int prescriptionPillsType;
  private int reduceAmountOfLightInBedroom;
  private int sleepDisorder;
  private int sleepDisorderMachine = -1;
  private int sleepMover = -1;
  private int sleepProblems;
  private int sleepingPosition;
  private int sleepingPositionSnore;
  private int smoker = -1;
  private int snoreFrequency = -1;
  private int snorer = -1;
  private int sourceOfLightInBedroom;
  private int specialLightUsedInSleep;
  private int stopsBreathing = -1;
  private int stressedFrequency;
  private boolean tiredDaytime;
  private int travelImpact;
  private int typeOfSoundUsedInSleep;
  private int typeofPerson = -1;
  private int usingSleepAids;
  private int wakeUpDifficulties;
  private float weight;
  
  public RST_UserProfile() {}
  
  public RST_UserProfile(Long paramLong)
  {
    this.id = paramLong;
  }
  
  public RST_UserProfile(Long paramLong, int paramInt1, int paramInt2, Date paramDate, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean1, boolean paramBoolean2, float paramFloat1, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15, int paramInt16, int paramInt17, int paramInt18, int paramInt19, int paramInt20, int paramInt21, int paramInt22, boolean paramBoolean3, float paramFloat2, int paramInt23, int paramInt24, int paramInt25, String paramString1, String paramString2, int paramInt26, int paramInt27, int paramInt28, int paramInt29, int paramInt30, int paramInt31, int paramInt32, int paramInt33, int paramInt34, int paramInt35, int paramInt36, int paramInt37, int paramInt38, int paramInt39, int paramInt40, int paramInt41, int paramInt42, int paramInt43, int paramInt44, int paramInt45, int paramInt46, int paramInt47, int paramInt48, int paramInt49, int paramInt50, int paramInt51, int paramInt52, int paramInt53, int paramInt54, int paramInt55, int paramInt56, int paramInt57, int paramInt58, int paramInt59, int paramInt60, int paramInt61, int paramInt62, int paramInt63, int paramInt64, int paramInt65, int paramInt66, int paramInt67, int paramInt68, int paramInt69, int paramInt70, int paramInt71, int paramInt72, int paramInt73, int paramInt74, int paramInt75, int paramInt76, int paramInt77, int paramInt78, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11, String paramString12, String paramString13)
  {
    this.id = paramLong;
    this.bedTime = paramInt1;
    this.consumeAlcohol = paramInt2;
    this.dateOfBirth = paramDate;
    this.gender = paramInt3;
    this.exerciseBeforeBed = paramInt4;
    this.exerciseIntensity = paramInt5;
    this.hasSleepPartner = paramInt6;
    this.hasPet = paramBoolean1;
    this.hasPetInBed = paramBoolean2;
    this.height = paramFloat1;
    this.highBloodPressure = paramInt7;
    this.illnessForSleep = paramInt8;
    this.medication = paramInt9;
    this.medicationFrequency = paramInt10;
    this.naps = paramInt11;
    this.neckSize = paramInt12;
    this.numCaffeineDrinks = paramInt13;
    this.numCigarettes = paramInt14;
    this.numDrinks = paramInt15;
    this.numExercises = paramInt16;
    this.sleepMover = paramInt17;
    this.sleepProblems = paramInt18;
    this.smoker = paramInt19;
    this.snorer = paramInt20;
    this.stopsBreathing = paramInt21;
    this.stressedFrequency = paramInt22;
    this.tiredDaytime = paramBoolean3;
    this.weight = paramFloat2;
    this.heightUnit = paramInt23;
    this.WeightUnit = paramInt24;
    this.TemperatureUnit = paramInt25;
    this.locale = paramString1;
    this.countryCode = paramString2;
    this.prescriptionPillsType = paramInt26;
    this.prescriptionPillsConcern = paramInt27;
    this.OTCMedicationType = paramInt28;
    this.OTCMedicationFrequency = paramInt29;
    this.OTCMedicationConcern = paramInt30;
    this.herbalRemediesType = paramInt31;
    this.herbalRemediesFrequency = paramInt32;
    this.controlBedroomTemperature = paramInt33;
    this.isHumidifierUsed = paramInt34;
    this.isAirPurifierUsed = paramInt35;
    this.noiseLevelInBedroom = paramInt36;
    this.howOftenElectronicsUsed = paramInt37;
    this.getWokenUpInNightByAny = paramInt38;
    this.isSoundUsedInSleep = paramInt39;
    this.typeOfSoundUsedInSleep = paramInt40;
    this.levelOfLightInBedroom = paramInt41;
    this.sourceOfLightInBedroom = paramInt42;
    this.reduceAmountOfLightInBedroom = paramInt43;
    this.specialLightUsedInSleep = paramInt44;
    this.mattressType = paramInt45;
    this.mattressSize = paramInt46;
    this.mattressBrand = paramInt47;
    this.mattressAge = paramInt48;
    this.isMattressPadTopperOnBed = paramInt49;
    this.pillowsType = paramInt50;
    this.pillowsAge = paramInt51;
    this.sleepingPosition = paramInt52;
    this.wakeUpDifficulties = paramInt53;
    this.morningAlertness = paramInt54;
    this.typeofPerson = paramInt55;
    this.travelImpact = paramInt56;
    this.childrenCount = paramInt57;
    this.napTime = paramInt58;
    this.sleepDisorder = paramInt59;
    this.sleepDisorderMachine = paramInt60;
    this.machineManufacturer = paramInt61;
    this.affectsYourSleep = paramInt62;
    this.isStayingAwakeDuringDay = paramInt63;
    this.concernsOnSleepProblem = paramInt64;
    this.extentInterfereSleepProblem = paramInt65;
    this.sleepingPositionSnore = paramInt66;
    this.isDryMouth = paramInt67;
    this.isNasalCongestion = paramInt68;
    this.isBreathingObstruction = paramInt69;
    this.isCrawlingSensation = paramInt70;
    this.isMuscleTension = paramInt71;
    this.isLegsStillAtNight = paramInt72;
    this.isSufferFromAllergies = paramInt73;
    this.isSleepWithPartner = paramInt74;
    this.petSleepWithYouOnBed = paramInt75;
    this.snoreFrequency = paramInt76;
    this.usingSleepAids = paramInt77;
    this.prescriptionPillsFrequency = paramInt78;
    this.otherMachineManufacturer = paramString3;
    this.otherPrescriptionPillsType = paramString4;
    this.otherHerbalRemediesType = paramString5;
    this.otherMattressType = paramString6;
    this.otherMattressBrand = paramString7;
    this.otherPillowsType = paramString8;
    this.otherControlBedroomTemperature = paramString9;
    this.otherTypeOfSoundUsedInSleep = paramString10;
    this.otherSourceOfLightInBedroom = paramString11;
    this.otherReduceAmountOfLightInBedroom = paramString12;
    this.newGender = paramString13;
  }
  
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
  
  public Date getDateOfBirth()
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
  
  public int getGender()
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
  
  public Long getId()
  {
    return this.id;
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
  
  public String getNewGender()
  {
    return this.newGender;
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
  
  public int getTemperatureUnit()
  {
    return this.TemperatureUnit;
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
    return this.WeightUnit;
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
  
  public void setDateOfBirth(Date paramDate)
  {
    this.dateOfBirth = paramDate;
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
  
  public void setGender(int paramInt)
  {
    this.gender = paramInt;
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
  
  public void setId(Long paramLong)
  {
    this.id = paramLong;
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
  
  public void setNewGender(String paramString)
  {
    this.newGender = paramString;
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
  
  public void setTemperatureUnit(int paramInt)
  {
    this.TemperatureUnit = paramInt;
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
    this.WeightUnit = paramInt;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RST_UserProfile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.model;

import de.greenrobot.dao.DaoException;
import java.util.Iterator;
import java.util.List;

public class RST_User
{
  private List<RST_AdviceItem> advices;
  private transient DaoSession daoSession;
  private String email;
  private String familyName;
  private String firstName;
  private String id;
  private Long idNightQuestion;
  private RST_LocationItem location;
  private Long locationId;
  private Long location__resolvedKey;
  private transient RST_UserDao myDao;
  private RST_NightQuestions nightQuestions;
  private Long nightQuestions__resolvedKey;
  private String password;
  private RST_UserProfile profile;
  private Long profileId;
  private Long profile__resolvedKey;
  private RST_Settings settings;
  private Long settingsId;
  private Long settings__resolvedKey;
  private List<RST_SleepSessionInfo> sleepSessions;
  
  public RST_User() {}
  
  public RST_User(String paramString)
  {
    this.id = paramString;
  }
  
  public RST_User(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, Long paramLong1, Long paramLong2, Long paramLong3, Long paramLong4)
  {
    this.id = paramString1;
    this.email = paramString2;
    this.familyName = paramString3;
    this.firstName = paramString4;
    this.password = paramString5;
    this.profileId = paramLong1;
    this.settingsId = paramLong2;
    this.locationId = paramLong3;
    this.idNightQuestion = paramLong4;
  }
  
  public void __setDaoSession(DaoSession paramDaoSession)
  {
    this.daoSession = paramDaoSession;
    if (paramDaoSession != null) {}
    for (paramDaoSession = paramDaoSession.getRST_UserDao();; paramDaoSession = null)
    {
      this.myDao = paramDaoSession;
      return;
    }
  }
  
  public void addAdviceValue(RST_AdviceItem paramRST_AdviceItem)
  {
    if (this.daoSession == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    if (this.advices == null) {
      getAdvices();
    }
    RST_AdviceItemDao localRST_AdviceItemDao = this.daoSession.getRST_AdviceItemDao();
    paramRST_AdviceItem.setRST_User(this);
    localRST_AdviceItemDao.insertOrReplace(paramRST_AdviceItem);
    this.advices.add(paramRST_AdviceItem);
  }
  
  public void addAdvicesArray(List<RST_AdviceItem> paramList)
  {
    if (this.daoSession == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    if (this.advices == null) {
      getAdvices();
    }
    RST_AdviceItemDao localRST_AdviceItemDao = this.daoSession.getRST_AdviceItemDao();
    for (int i = 0;; i++)
    {
      if (i >= paramList.size())
      {
        localRST_AdviceItemDao.insertOrReplaceInTx(paramList);
        this.advices.addAll(paramList);
        return;
      }
      ((RST_AdviceItem)paramList.get(i)).setRST_User(this);
    }
  }
  
  public void addSleepSessionInfo(RST_SleepSessionInfo paramRST_SleepSessionInfo)
  {
    if (this.daoSession == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    if (this.sleepSessions == null) {
      getSleepSessions();
    }
    RST_SleepSessionInfoDao localRST_SleepSessionInfoDao = this.daoSession.getRST_SleepSessionInfoDao();
    paramRST_SleepSessionInfo.setRST_User(this);
    localRST_SleepSessionInfoDao.insertOrReplace(paramRST_SleepSessionInfo);
    this.sleepSessions.add(paramRST_SleepSessionInfo);
  }
  
  public void addSleepSessionInfoArray(List<RST_SleepSessionInfo> paramList)
  {
    if (this.daoSession == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    if (this.sleepSessions == null) {
      getSleepSessions();
    }
    RST_SleepSessionInfoDao localRST_SleepSessionInfoDao = this.daoSession.getRST_SleepSessionInfoDao();
    for (int i = 0;; i++)
    {
      if (i >= paramList.size())
      {
        localRST_SleepSessionInfoDao.insertOrReplaceInTx(paramList);
        this.sleepSessions.addAll(paramList);
        return;
      }
      ((RST_SleepSessionInfo)paramList.get(i)).setRST_User(this);
    }
  }
  
  public void delete()
  {
    if ((this.myDao == null) || (this.daoSession == null)) {
      throw new DaoException("Entity is detached from DAO context");
    }
    if (this.profile == null) {
      getProfile();
    }
    Object localObject = this.daoSession.getRST_UserProfileDao();
    if (this.profile != null) {
      ((RST_UserProfileDao)localObject).delete(this.profile);
    }
    if (this.location == null) {
      getLocation();
    }
    localObject = this.daoSession.getRST_LocationItemDao();
    if (this.location != null) {
      ((RST_LocationItemDao)localObject).delete(this.location);
    }
    if (this.nightQuestions == null) {
      getNightQuestions();
    }
    localObject = this.daoSession.getRST_NightQuestionsDao();
    if (this.nightQuestions != null) {
      ((RST_NightQuestionsDao)localObject).delete(this.nightQuestions);
    }
    if (this.sleepSessions == null) {
      getSleepSessions();
    }
    localObject = this.sleepSessions.iterator();
    if (!((Iterator)localObject).hasNext())
    {
      if (this.advices == null) {
        getAdvices();
      }
      localObject = this.advices.iterator();
    }
    for (;;)
    {
      if (!((Iterator)localObject).hasNext())
      {
        this.myDao.delete(this);
        return;
        ((RST_SleepSessionInfo)((Iterator)localObject).next()).delete();
        break;
      }
      ((RST_AdviceItem)((Iterator)localObject).next()).delete();
    }
  }
  
  public List<RST_AdviceItem> getAdvices()
  {
    List localList;
    if (this.advices == null)
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localList = this.daoSession.getRST_AdviceItemDao()._queryRST_User_Advices(this.id);
    }
    try
    {
      if (this.advices == null) {
        this.advices = localList;
      }
      return this.advices;
    }
    finally {}
  }
  
  public String getEmail()
  {
    return this.email;
  }
  
  public String getFamilyName()
  {
    return this.familyName;
  }
  
  public String getFirstName()
  {
    return this.firstName;
  }
  
  public String getId()
  {
    return this.id;
  }
  
  public Long getIdNightQuestion()
  {
    return this.idNightQuestion;
  }
  
  public RST_LocationItem getLocation()
  {
    Long localLong = this.locationId;
    RST_LocationItem localRST_LocationItem;
    if ((this.location__resolvedKey == null) || (!this.location__resolvedKey.equals(localLong)))
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localRST_LocationItem = (RST_LocationItem)this.daoSession.getRST_LocationItemDao().load(localLong);
    }
    try
    {
      this.location = localRST_LocationItem;
      this.location__resolvedKey = localLong;
      return this.location;
    }
    finally {}
  }
  
  public Long getLocationId()
  {
    return this.locationId;
  }
  
  public RST_NightQuestions getNightQuestions()
  {
    Long localLong = this.idNightQuestion;
    RST_NightQuestions localRST_NightQuestions;
    if ((this.nightQuestions__resolvedKey == null) || (!this.nightQuestions__resolvedKey.equals(localLong)))
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localRST_NightQuestions = (RST_NightQuestions)this.daoSession.getRST_NightQuestionsDao().load(localLong);
    }
    try
    {
      this.nightQuestions = localRST_NightQuestions;
      this.nightQuestions__resolvedKey = localLong;
      return this.nightQuestions;
    }
    finally {}
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public RST_UserProfile getProfile()
  {
    Long localLong = this.profileId;
    RST_UserProfile localRST_UserProfile;
    if ((this.profile__resolvedKey == null) || (!this.profile__resolvedKey.equals(localLong)))
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localRST_UserProfile = (RST_UserProfile)this.daoSession.getRST_UserProfileDao().load(localLong);
    }
    try
    {
      this.profile = localRST_UserProfile;
      this.profile__resolvedKey = localLong;
      return this.profile;
    }
    finally {}
  }
  
  public Long getProfileId()
  {
    return this.profileId;
  }
  
  public RST_Settings getSettings()
  {
    Long localLong = this.settingsId;
    RST_Settings localRST_Settings;
    if ((this.settings__resolvedKey == null) || (!this.settings__resolvedKey.equals(localLong)))
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localRST_Settings = (RST_Settings)this.daoSession.getRST_SettingsDao().load(localLong);
    }
    try
    {
      this.settings = localRST_Settings;
      this.settings__resolvedKey = localLong;
      return this.settings;
    }
    finally {}
  }
  
  public Long getSettingsId()
  {
    return this.settingsId;
  }
  
  public List<RST_SleepSessionInfo> getSleepSessions()
  {
    List localList;
    if (this.sleepSessions == null)
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localList = this.daoSession.getRST_SleepSessionInfoDao()._queryRST_User_SleepSessions(this.id);
    }
    try
    {
      if (this.sleepSessions == null) {
        this.sleepSessions = localList;
      }
      return this.sleepSessions;
    }
    finally {}
  }
  
  public void refresh()
  {
    if (this.myDao == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    this.myDao.refresh(this);
  }
  
  public void resetAdvices()
  {
    try
    {
      this.advices = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public void resetSleepSessions()
  {
    try
    {
      this.sleepSessions = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public void setAndSaveLocation(RST_LocationItem paramRST_LocationItem)
  {
    if (this.daoSession == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    this.daoSession.getRST_LocationItemDao().insertOrReplace(paramRST_LocationItem);
    setLocation(paramRST_LocationItem);
    update();
  }
  
  public void setAndSaveProfile(RST_UserProfile paramRST_UserProfile)
  {
    if (this.daoSession == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    this.daoSession.getRST_UserProfileDao().insertOrReplace(paramRST_UserProfile);
    setProfile(paramRST_UserProfile);
    update();
  }
  
  public void setAndSaveSettings(RST_Settings paramRST_Settings)
  {
    if (this.daoSession == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    this.daoSession.getRST_SettingsDao().insertOrReplace(paramRST_Settings);
    setSettings(paramRST_Settings);
    update();
  }
  
  public void setEmail(String paramString)
  {
    this.email = paramString;
  }
  
  public void setFamilyName(String paramString)
  {
    this.familyName = paramString;
  }
  
  public void setFirstName(String paramString)
  {
    this.firstName = paramString;
  }
  
  public void setId(String paramString)
  {
    this.id = paramString;
  }
  
  public void setIdNightQuestion(Long paramLong)
  {
    this.idNightQuestion = paramLong;
  }
  
  /* Error */
  public void setLocation(RST_LocationItem paramRST_LocationItem)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield 167	com/resmed/refresh/model/RST_User:location	Lcom/resmed/refresh/model/RST_LocationItem;
    //   7: aload_1
    //   8: ifnonnull +21 -> 29
    //   11: aconst_null
    //   12: astore_1
    //   13: aload_0
    //   14: aload_1
    //   15: putfield 59	com/resmed/refresh/model/RST_User:locationId	Ljava/lang/Long;
    //   18: aload_0
    //   19: aload_0
    //   20: getfield 59	com/resmed/refresh/model/RST_User:locationId	Ljava/lang/Long;
    //   23: putfield 225	com/resmed/refresh/model/RST_User:location__resolvedKey	Ljava/lang/Long;
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: aload_1
    //   30: invokevirtual 303	com/resmed/refresh/model/RST_LocationItem:getId	()Ljava/lang/Long;
    //   33: astore_1
    //   34: goto -21 -> 13
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	42	0	this	RST_User
    //   0	42	1	paramRST_LocationItem	RST_LocationItem
    // Exception table:
    //   from	to	target	type
    //   2	7	37	finally
    //   13	28	37	finally
    //   29	34	37	finally
    //   38	40	37	finally
  }
  
  public void setLocationId(Long paramLong)
  {
    this.locationId = paramLong;
  }
  
  /* Error */
  public void setNightQuestions(RST_NightQuestions paramRST_NightQuestions)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield 180	com/resmed/refresh/model/RST_User:nightQuestions	Lcom/resmed/refresh/model/RST_NightQuestions;
    //   7: aload_1
    //   8: ifnonnull +21 -> 29
    //   11: aconst_null
    //   12: astore_1
    //   13: aload_0
    //   14: aload_1
    //   15: putfield 61	com/resmed/refresh/model/RST_User:idNightQuestion	Ljava/lang/Long;
    //   18: aload_0
    //   19: aload_0
    //   20: getfield 61	com/resmed/refresh/model/RST_User:idNightQuestion	Ljava/lang/Long;
    //   23: putfield 239	com/resmed/refresh/model/RST_User:nightQuestions__resolvedKey	Ljava/lang/Long;
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: aload_1
    //   30: invokevirtual 307	com/resmed/refresh/model/RST_NightQuestions:getId	()Ljava/lang/Long;
    //   33: astore_1
    //   34: goto -21 -> 13
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	42	0	this	RST_User
    //   0	42	1	paramRST_NightQuestions	RST_NightQuestions
    // Exception table:
    //   from	to	target	type
    //   2	7	37	finally
    //   13	28	37	finally
    //   29	34	37	finally
    //   38	40	37	finally
  }
  
  public void setPassword(String paramString)
  {
    this.password = paramString;
  }
  
  /* Error */
  public void setProfile(RST_UserProfile paramRST_UserProfile)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield 152	com/resmed/refresh/model/RST_User:profile	Lcom/resmed/refresh/model/RST_UserProfile;
    //   7: aload_1
    //   8: ifnonnull +21 -> 29
    //   11: aconst_null
    //   12: astore_1
    //   13: aload_0
    //   14: aload_1
    //   15: putfield 55	com/resmed/refresh/model/RST_User:profileId	Ljava/lang/Long;
    //   18: aload_0
    //   19: aload_0
    //   20: getfield 55	com/resmed/refresh/model/RST_User:profileId	Ljava/lang/Long;
    //   23: putfield 245	com/resmed/refresh/model/RST_User:profile__resolvedKey	Ljava/lang/Long;
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: aload_1
    //   30: invokevirtual 309	com/resmed/refresh/model/RST_UserProfile:getId	()Ljava/lang/Long;
    //   33: astore_1
    //   34: goto -21 -> 13
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	42	0	this	RST_User
    //   0	42	1	paramRST_UserProfile	RST_UserProfile
    // Exception table:
    //   from	to	target	type
    //   2	7	37	finally
    //   13	28	37	finally
    //   29	34	37	finally
    //   38	40	37	finally
  }
  
  public void setProfileId(Long paramLong)
  {
    this.profileId = paramLong;
  }
  
  /* Error */
  public void setSettings(RST_Settings paramRST_Settings)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield 264	com/resmed/refresh/model/RST_User:settings	Lcom/resmed/refresh/model/RST_Settings;
    //   7: aload_1
    //   8: ifnonnull +21 -> 29
    //   11: aconst_null
    //   12: astore_1
    //   13: aload_0
    //   14: aload_1
    //   15: putfield 57	com/resmed/refresh/model/RST_User:settingsId	Ljava/lang/Long;
    //   18: aload_0
    //   19: aload_0
    //   20: getfield 57	com/resmed/refresh/model/RST_User:settingsId	Ljava/lang/Long;
    //   23: putfield 253	com/resmed/refresh/model/RST_User:settings__resolvedKey	Ljava/lang/Long;
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: aload_1
    //   30: invokevirtual 311	com/resmed/refresh/model/RST_Settings:getId	()Ljava/lang/Long;
    //   33: astore_1
    //   34: goto -21 -> 13
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	42	0	this	RST_User
    //   0	42	1	paramRST_Settings	RST_Settings
    // Exception table:
    //   from	to	target	type
    //   2	7	37	finally
    //   13	28	37	finally
    //   29	34	37	finally
    //   38	40	37	finally
  }
  
  public void setSettingsId(Long paramLong)
  {
    this.settingsId = paramLong;
  }
  
  public void update()
  {
    if (this.myDao == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    this.myDao.update(this);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RST_User.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
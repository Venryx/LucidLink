package com.resmed.refresh.model;

import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScope;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;
import java.util.Map;

public class DaoSession
  extends AbstractDaoSession
{
  private final RST_AdviceItemDao rST_AdviceItemDao;
  private final DaoConfig rST_AdviceItemDaoConfig;
  private final RST_ButtonDao rST_ButtonDao;
  private final DaoConfig rST_ButtonDaoConfig;
  private final RST_DisplayItemDao rST_DisplayItemDao;
  private final DaoConfig rST_DisplayItemDaoConfig;
  private final RST_EnvironmentalInfoDao rST_EnvironmentalInfoDao;
  private final DaoConfig rST_EnvironmentalInfoDaoConfig;
  private final RST_LocationItemDao rST_LocationItemDao;
  private final DaoConfig rST_LocationItemDaoConfig;
  private final RST_NightQuestionsDao rST_NightQuestionsDao;
  private final DaoConfig rST_NightQuestionsDaoConfig;
  private final RST_QuestionItemDao rST_QuestionItemDao;
  private final DaoConfig rST_QuestionItemDaoConfig;
  private final RST_SettingsDao rST_SettingsDao;
  private final DaoConfig rST_SettingsDaoConfig;
  private final RST_SleepEventDao rST_SleepEventDao;
  private final DaoConfig rST_SleepEventDaoConfig;
  private final RST_SleepSessionInfoDao rST_SleepSessionInfoDao;
  private final DaoConfig rST_SleepSessionInfoDaoConfig;
  private final RST_UserDao rST_UserDao;
  private final DaoConfig rST_UserDaoConfig;
  private final RST_UserProfileDao rST_UserProfileDao;
  private final DaoConfig rST_UserProfileDaoConfig;
  private final RST_ValueItemDao rST_ValueItemDao;
  private final DaoConfig rST_ValueItemDaoConfig;
  
  public DaoSession(SQLiteDatabase paramSQLiteDatabase, IdentityScopeType paramIdentityScopeType, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> paramMap)
  {
    super(paramSQLiteDatabase);
    this.rST_UserDaoConfig = ((DaoConfig)paramMap.get(RST_UserDao.class)).clone();
    this.rST_UserDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.rST_UserProfileDaoConfig = ((DaoConfig)paramMap.get(RST_UserProfileDao.class)).clone();
    this.rST_UserProfileDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.rST_SettingsDaoConfig = ((DaoConfig)paramMap.get(RST_SettingsDao.class)).clone();
    this.rST_SettingsDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.rST_SleepSessionInfoDaoConfig = ((DaoConfig)paramMap.get(RST_SleepSessionInfoDao.class)).clone();
    this.rST_SleepSessionInfoDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.rST_EnvironmentalInfoDaoConfig = ((DaoConfig)paramMap.get(RST_EnvironmentalInfoDao.class)).clone();
    this.rST_EnvironmentalInfoDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.rST_LocationItemDaoConfig = ((DaoConfig)paramMap.get(RST_LocationItemDao.class)).clone();
    this.rST_LocationItemDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.rST_AdviceItemDaoConfig = ((DaoConfig)paramMap.get(RST_AdviceItemDao.class)).clone();
    this.rST_AdviceItemDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.rST_ButtonDaoConfig = ((DaoConfig)paramMap.get(RST_ButtonDao.class)).clone();
    this.rST_ButtonDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.rST_SleepEventDaoConfig = ((DaoConfig)paramMap.get(RST_SleepEventDao.class)).clone();
    this.rST_SleepEventDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.rST_NightQuestionsDaoConfig = ((DaoConfig)paramMap.get(RST_NightQuestionsDao.class)).clone();
    this.rST_NightQuestionsDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.rST_QuestionItemDaoConfig = ((DaoConfig)paramMap.get(RST_QuestionItemDao.class)).clone();
    this.rST_QuestionItemDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.rST_DisplayItemDaoConfig = ((DaoConfig)paramMap.get(RST_DisplayItemDao.class)).clone();
    this.rST_DisplayItemDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.rST_ValueItemDaoConfig = ((DaoConfig)paramMap.get(RST_ValueItemDao.class)).clone();
    this.rST_ValueItemDaoConfig.initIdentityScope(paramIdentityScopeType);
    this.rST_UserDao = new RST_UserDao(this.rST_UserDaoConfig, this);
    this.rST_UserProfileDao = new RST_UserProfileDao(this.rST_UserProfileDaoConfig, this);
    this.rST_SettingsDao = new RST_SettingsDao(this.rST_SettingsDaoConfig, this);
    this.rST_SleepSessionInfoDao = new RST_SleepSessionInfoDao(this.rST_SleepSessionInfoDaoConfig, this);
    this.rST_EnvironmentalInfoDao = new RST_EnvironmentalInfoDao(this.rST_EnvironmentalInfoDaoConfig, this);
    this.rST_LocationItemDao = new RST_LocationItemDao(this.rST_LocationItemDaoConfig, this);
    this.rST_AdviceItemDao = new RST_AdviceItemDao(this.rST_AdviceItemDaoConfig, this);
    this.rST_ButtonDao = new RST_ButtonDao(this.rST_ButtonDaoConfig, this);
    this.rST_SleepEventDao = new RST_SleepEventDao(this.rST_SleepEventDaoConfig, this);
    this.rST_NightQuestionsDao = new RST_NightQuestionsDao(this.rST_NightQuestionsDaoConfig, this);
    this.rST_QuestionItemDao = new RST_QuestionItemDao(this.rST_QuestionItemDaoConfig, this);
    this.rST_DisplayItemDao = new RST_DisplayItemDao(this.rST_DisplayItemDaoConfig, this);
    this.rST_ValueItemDao = new RST_ValueItemDao(this.rST_ValueItemDaoConfig, this);
    registerDao(RST_User.class, this.rST_UserDao);
    registerDao(RST_UserProfile.class, this.rST_UserProfileDao);
    registerDao(RST_Settings.class, this.rST_SettingsDao);
    registerDao(RST_SleepSessionInfo.class, this.rST_SleepSessionInfoDao);
    registerDao(RST_EnvironmentalInfo.class, this.rST_EnvironmentalInfoDao);
    registerDao(RST_LocationItem.class, this.rST_LocationItemDao);
    registerDao(RST_AdviceItem.class, this.rST_AdviceItemDao);
    registerDao(RST_Button.class, this.rST_ButtonDao);
    registerDao(RST_SleepEvent.class, this.rST_SleepEventDao);
    registerDao(RST_NightQuestions.class, this.rST_NightQuestionsDao);
    registerDao(RST_QuestionItem.class, this.rST_QuestionItemDao);
    registerDao(RST_DisplayItem.class, this.rST_DisplayItemDao);
    registerDao(RST_ValueItem.class, this.rST_ValueItemDao);
  }
  
  public void clear()
  {
    this.rST_UserDaoConfig.getIdentityScope().clear();
    this.rST_UserProfileDaoConfig.getIdentityScope().clear();
    this.rST_SettingsDaoConfig.getIdentityScope().clear();
    this.rST_SleepSessionInfoDaoConfig.getIdentityScope().clear();
    this.rST_EnvironmentalInfoDaoConfig.getIdentityScope().clear();
    this.rST_LocationItemDaoConfig.getIdentityScope().clear();
    this.rST_AdviceItemDaoConfig.getIdentityScope().clear();
    this.rST_ButtonDaoConfig.getIdentityScope().clear();
    this.rST_SleepEventDaoConfig.getIdentityScope().clear();
    this.rST_NightQuestionsDaoConfig.getIdentityScope().clear();
    this.rST_QuestionItemDaoConfig.getIdentityScope().clear();
    this.rST_DisplayItemDaoConfig.getIdentityScope().clear();
    this.rST_ValueItemDaoConfig.getIdentityScope().clear();
  }
  
  public RST_AdviceItemDao getRST_AdviceItemDao()
  {
    return this.rST_AdviceItemDao;
  }
  
  public RST_ButtonDao getRST_ButtonDao()
  {
    return this.rST_ButtonDao;
  }
  
  public RST_DisplayItemDao getRST_DisplayItemDao()
  {
    return this.rST_DisplayItemDao;
  }
  
  public RST_EnvironmentalInfoDao getRST_EnvironmentalInfoDao()
  {
    return this.rST_EnvironmentalInfoDao;
  }
  
  public RST_LocationItemDao getRST_LocationItemDao()
  {
    return this.rST_LocationItemDao;
  }
  
  public RST_NightQuestionsDao getRST_NightQuestionsDao()
  {
    return this.rST_NightQuestionsDao;
  }
  
  public RST_QuestionItemDao getRST_QuestionItemDao()
  {
    return this.rST_QuestionItemDao;
  }
  
  public RST_SettingsDao getRST_SettingsDao()
  {
    return this.rST_SettingsDao;
  }
  
  public RST_SleepEventDao getRST_SleepEventDao()
  {
    return this.rST_SleepEventDao;
  }
  
  public RST_SleepSessionInfoDao getRST_SleepSessionInfoDao()
  {
    return this.rST_SleepSessionInfoDao;
  }
  
  public RST_UserDao getRST_UserDao()
  {
    return this.rST_UserDao;
  }
  
  public RST_UserProfileDao getRST_UserProfileDao()
  {
    return this.rST_UserProfileDao;
  }
  
  public RST_ValueItemDao getRST_ValueItemDao()
  {
    return this.rST_ValueItemDao;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
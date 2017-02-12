package com.resmed.refresh.model;

import de.greenrobot.dao.DaoException;
import java.util.Iterator;
import java.util.List;

public class RST_NightQuestions
{
  private transient DaoSession daoSession;
  private Long id;
  private transient RST_NightQuestionsDao myDao;
  private List<RST_QuestionItem> questions;
  private int version;
  
  public RST_NightQuestions() {}
  
  public RST_NightQuestions(Long paramLong)
  {
    this.id = paramLong;
  }
  
  public RST_NightQuestions(Long paramLong, int paramInt)
  {
    this.id = paramLong;
    this.version = paramInt;
  }
  
  public void __setDaoSession(DaoSession paramDaoSession)
  {
    this.daoSession = paramDaoSession;
    if (paramDaoSession != null) {}
    for (paramDaoSession = paramDaoSession.getRST_NightQuestionsDao();; paramDaoSession = null)
    {
      this.myDao = paramDaoSession;
      return;
    }
  }
  
  public void addArrayOfQuestionItem(List<RST_QuestionItem> paramList)
  {
    if (this.daoSession == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    if (this.questions == null) {
      getQuestions();
    }
    RST_QuestionItemDao localRST_QuestionItemDao = this.daoSession.getRST_QuestionItemDao();
    for (int i = 0;; i++)
    {
      if (i >= paramList.size())
      {
        localRST_QuestionItemDao.insertOrReplaceInTx(paramList);
        this.questions.addAll(paramList);
        return;
      }
      ((RST_QuestionItem)paramList.get(i)).setRST_NightQuestions(this);
    }
  }
  
  public void addQuestionItem(RST_QuestionItem paramRST_QuestionItem)
  {
    if (this.daoSession == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    if (this.questions == null) {
      getQuestions();
    }
    RST_QuestionItemDao localRST_QuestionItemDao = this.daoSession.getRST_QuestionItemDao();
    paramRST_QuestionItem.setRST_NightQuestions(this);
    localRST_QuestionItemDao.insertOrReplace(paramRST_QuestionItem);
    this.questions.add(paramRST_QuestionItem);
  }
  
  public void delete()
  {
    if ((this.myDao == null) || (this.daoSession == null)) {
      throw new DaoException("Entity is detached from DAO context");
    }
    if (this.questions == null) {
      getQuestions();
    }
    Iterator localIterator = this.questions.iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        this.myDao.delete(this);
        return;
      }
      ((RST_QuestionItem)localIterator.next()).delete();
    }
  }
  
  public Long getId()
  {
    return this.id;
  }
  
  public List<RST_QuestionItem> getQuestions()
  {
    List localList;
    if (this.questions == null)
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localList = this.daoSession.getRST_QuestionItemDao()._queryRST_NightQuestions_Questions(this.id);
    }
    try
    {
      if (this.questions == null) {
        this.questions = localList;
      }
      return this.questions;
    }
    finally {}
  }
  
  public int getVersion()
  {
    return this.version;
  }
  
  public void refresh()
  {
    if (this.myDao == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    this.myDao.refresh(this);
  }
  
  public void resetQuestions()
  {
    try
    {
      this.questions = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public void setId(Long paramLong)
  {
    this.id = paramLong;
  }
  
  public void setVersion(int paramInt)
  {
    this.version = paramInt;
  }
  
  public void update()
  {
    if (this.myDao == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    this.myDao.update(this);
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
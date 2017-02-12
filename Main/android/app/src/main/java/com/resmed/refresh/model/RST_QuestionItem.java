package com.resmed.refresh.model;

import com.resmed.refresh.utils.preSleepLog;
import de.greenrobot.dao.DaoException;
import java.util.Iterator;
import java.util.List;

public class RST_QuestionItem
{
  private int answer;
  private transient DaoSession daoSession;
  private List<RST_DisplayItem> display;
  private Long id;
  private Long idNightQuestion;
  private transient RST_QuestionItemDao myDao;
  private int ordr;
  private int questionId;
  private RST_NightQuestions rST_NightQuestions;
  private Long rST_NightQuestions__resolvedKey;
  private String text;
  
  public RST_QuestionItem()
  {
    preSleepLog.addTrace("Inside Default constructor:RST_QuestionItem");
  }
  
  public RST_QuestionItem(Long paramLong)
  {
    preSleepLog.addTrace("Inside constructor:RST_QuestionItem=" + paramLong);
    this.id = paramLong;
  }
  
  public RST_QuestionItem(Long paramLong1, int paramInt1, String paramString, int paramInt2, int paramInt3, Long paramLong2)
  {
    this.id = paramLong1;
    this.questionId = paramInt1;
    this.text = paramString;
    this.ordr = paramInt2;
    this.answer = paramInt3;
    this.idNightQuestion = paramLong2;
    preSleepLog.addTrace("Inside Constructor:QuestionItem Question= " + this.text + "QuestionItem Question index= " + this.questionId + " answer=" + this.answer);
  }
  
  public void __setDaoSession(DaoSession paramDaoSession)
  {
    this.daoSession = paramDaoSession;
    if (paramDaoSession != null) {}
    for (paramDaoSession = paramDaoSession.getRST_QuestionItemDao();; paramDaoSession = null)
    {
      this.myDao = paramDaoSession;
      return;
    }
  }
  
  public void addArrayOfDisplayItem(List<RST_DisplayItem> paramList)
  {
    if (this.daoSession == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    if (this.display == null) {
      getDisplay();
    }
    RST_DisplayItemDao localRST_DisplayItemDao = this.daoSession.getRST_DisplayItemDao();
    for (int i = 0;; i++)
    {
      if (i >= paramList.size())
      {
        localRST_DisplayItemDao.insertOrReplaceInTx(paramList);
        this.display.addAll(paramList);
        return;
      }
      ((RST_DisplayItem)paramList.get(i)).setQuestion(this);
    }
  }
  
  public void addDisplayItem(RST_DisplayItem paramRST_DisplayItem)
  {
    if (this.daoSession == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    if (this.display == null) {
      getDisplay();
    }
    RST_DisplayItemDao localRST_DisplayItemDao = this.daoSession.getRST_DisplayItemDao();
    paramRST_DisplayItem.setQuestion(this);
    localRST_DisplayItemDao.insertOrReplace(paramRST_DisplayItem);
    this.display.add(paramRST_DisplayItem);
  }
  
  public void delete()
  {
    if ((this.myDao == null) || (this.daoSession == null)) {
      throw new DaoException("Entity is detached from DAO context");
    }
    if (this.display == null) {
      getDisplay();
    }
    Iterator localIterator = this.display.iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        this.myDao.delete(this);
        return;
      }
      ((RST_DisplayItem)localIterator.next()).delete();
    }
  }
  
  public int getAnswer()
  {
    return this.answer;
  }
  
  public List<RST_DisplayItem> getDisplay()
  {
    List localList;
    if (this.display == null)
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localList = this.daoSession.getRST_DisplayItemDao()._queryRST_QuestionItem_Display(this.id);
    }
    try
    {
      if (this.display == null) {
        this.display = localList;
      }
      return this.display;
    }
    finally {}
  }
  
  public Long getId()
  {
    return this.id;
  }
  
  public Long getIdNightQuestion()
  {
    return this.idNightQuestion;
  }
  
  public int getOrdr()
  {
    return this.ordr;
  }
  
  public int getQuestionId()
  {
    return this.questionId;
  }
  
  public RST_NightQuestions getRST_NightQuestions()
  {
    Long localLong = this.idNightQuestion;
    RST_NightQuestions localRST_NightQuestions;
    if ((this.rST_NightQuestions__resolvedKey == null) || (!this.rST_NightQuestions__resolvedKey.equals(localLong)))
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localRST_NightQuestions = (RST_NightQuestions)this.daoSession.getRST_NightQuestionsDao().load(localLong);
    }
    try
    {
      this.rST_NightQuestions = localRST_NightQuestions;
      this.rST_NightQuestions__resolvedKey = localLong;
      return this.rST_NightQuestions;
    }
    finally {}
  }
  
  public String getText()
  {
    return this.text;
  }
  
  public void refresh()
  {
    if (this.myDao == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    this.myDao.refresh(this);
  }
  
  public void resetDisplay()
  {
    try
    {
      this.display = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public void setAnswer(int paramInt)
  {
    this.answer = paramInt;
  }
  
  public void setId(Long paramLong)
  {
    this.id = paramLong;
  }
  
  public void setIdNightQuestion(Long paramLong)
  {
    this.idNightQuestion = paramLong;
  }
  
  public void setOrdr(int paramInt)
  {
    this.ordr = paramInt;
  }
  
  public void setQuestionId(int paramInt)
  {
    this.questionId = paramInt;
  }
  
  /* Error */
  public void setRST_NightQuestions(RST_NightQuestions paramRST_NightQuestions)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield 199	com/resmed/refresh/model/RST_QuestionItem:rST_NightQuestions	Lcom/resmed/refresh/model/RST_NightQuestions;
    //   7: aload_1
    //   8: ifnonnull +21 -> 29
    //   11: aconst_null
    //   12: astore_1
    //   13: aload_0
    //   14: aload_1
    //   15: putfield 64	com/resmed/refresh/model/RST_QuestionItem:idNightQuestion	Ljava/lang/Long;
    //   18: aload_0
    //   19: aload_0
    //   20: getfield 64	com/resmed/refresh/model/RST_QuestionItem:idNightQuestion	Ljava/lang/Long;
    //   23: putfield 180	com/resmed/refresh/model/RST_QuestionItem:rST_NightQuestions__resolvedKey	Ljava/lang/Long;
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: aload_1
    //   30: invokevirtual 214	com/resmed/refresh/model/RST_NightQuestions:getId	()Ljava/lang/Long;
    //   33: astore_1
    //   34: goto -21 -> 13
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	42	0	this	RST_QuestionItem
    //   0	42	1	paramRST_NightQuestions	RST_NightQuestions
    // Exception table:
    //   from	to	target	type
    //   2	7	37	finally
    //   13	28	37	finally
    //   29	34	37	finally
    //   38	40	37	finally
  }
  
  public void setText(String paramString)
  {
    this.text = paramString;
  }
  
  public void update()
  {
    if (this.myDao == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    this.myDao.update(this);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RST_QuestionItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
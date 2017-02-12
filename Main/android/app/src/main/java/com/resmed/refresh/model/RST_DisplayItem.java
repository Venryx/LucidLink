package com.resmed.refresh.model;

import de.greenrobot.dao.DaoException;

public class RST_DisplayItem
{
  private transient DaoSession daoSession;
  private int displayId;
  private Long id;
  private Long idDisplay;
  private transient RST_DisplayItemDao myDao;
  private int ordr;
  private RST_QuestionItem question;
  private Long question__resolvedKey;
  private String value;
  
  public RST_DisplayItem() {}
  
  public RST_DisplayItem(Long paramLong)
  {
    this.id = paramLong;
  }
  
  public RST_DisplayItem(Long paramLong1, int paramInt1, String paramString, int paramInt2, Long paramLong2)
  {
    this.id = paramLong1;
    this.displayId = paramInt1;
    this.value = paramString;
    this.ordr = paramInt2;
    this.idDisplay = paramLong2;
  }
  
  public void __setDaoSession(DaoSession paramDaoSession)
  {
    this.daoSession = paramDaoSession;
    if (paramDaoSession != null) {}
    for (paramDaoSession = paramDaoSession.getRST_DisplayItemDao();; paramDaoSession = null)
    {
      this.myDao = paramDaoSession;
      return;
    }
  }
  
  public void delete()
  {
    if (this.myDao == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    this.myDao.delete(this);
  }
  
  public int getDisplayId()
  {
    return this.displayId;
  }
  
  public Long getId()
  {
    return this.id;
  }
  
  public Long getIdDisplay()
  {
    return this.idDisplay;
  }
  
  public int getOrdr()
  {
    return this.ordr;
  }
  
  public RST_QuestionItem getQuestion()
  {
    Long localLong = this.idDisplay;
    RST_QuestionItem localRST_QuestionItem;
    if ((this.question__resolvedKey == null) || (!this.question__resolvedKey.equals(localLong)))
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localRST_QuestionItem = (RST_QuestionItem)this.daoSession.getRST_QuestionItemDao().load(localLong);
    }
    try
    {
      this.question = localRST_QuestionItem;
      this.question__resolvedKey = localLong;
      return this.question;
    }
    finally {}
  }
  
  public String getValue()
  {
    return this.value;
  }
  
  public void refresh()
  {
    if (this.myDao == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    this.myDao.refresh(this);
  }
  
  public void setDisplayId(int paramInt)
  {
    this.displayId = paramInt;
  }
  
  public void setId(Long paramLong)
  {
    this.id = paramLong;
  }
  
  public void setIdDisplay(Long paramLong)
  {
    this.idDisplay = paramLong;
  }
  
  public void setOrdr(int paramInt)
  {
    this.ordr = paramInt;
  }
  
  /* Error */
  public void setQuestion(RST_QuestionItem paramRST_QuestionItem)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield 91	com/resmed/refresh/model/RST_DisplayItem:question	Lcom/resmed/refresh/model/RST_QuestionItem;
    //   7: aload_1
    //   8: ifnonnull +21 -> 29
    //   11: aconst_null
    //   12: astore_1
    //   13: aload_0
    //   14: aload_1
    //   15: putfield 36	com/resmed/refresh/model/RST_DisplayItem:idDisplay	Ljava/lang/Long;
    //   18: aload_0
    //   19: aload_0
    //   20: getfield 36	com/resmed/refresh/model/RST_DisplayItem:idDisplay	Ljava/lang/Long;
    //   23: putfield 71	com/resmed/refresh/model/RST_DisplayItem:question__resolvedKey	Ljava/lang/Long;
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: aload_1
    //   30: invokevirtual 105	com/resmed/refresh/model/RST_QuestionItem:getId	()Ljava/lang/Long;
    //   33: astore_1
    //   34: goto -21 -> 13
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	42	0	this	RST_DisplayItem
    //   0	42	1	paramRST_QuestionItem	RST_QuestionItem
    // Exception table:
    //   from	to	target	type
    //   2	7	37	finally
    //   13	28	37	finally
    //   29	34	37	finally
    //   38	40	37	finally
  }
  
  public void setValue(String paramString)
  {
    this.value = paramString;
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
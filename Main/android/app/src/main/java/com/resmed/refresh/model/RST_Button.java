package com.resmed.refresh.model;

import de.greenrobot.dao.DaoException;

public class RST_Button
{
  private long buttonId;
  private String colour;
  private transient DaoSession daoSession;
  private String icon;
  private Long id;
  private Long idAdviceButton;
  private transient RST_ButtonDao myDao;
  private int ordr;
  private RST_AdviceItem rST_AdviceItem;
  private Long rST_AdviceItem__resolvedKey;
  private String title;
  
  public RST_Button() {}
  
  public RST_Button(Long paramLong)
  {
    this.id = paramLong;
  }
  
  public RST_Button(Long paramLong1, long paramLong, int paramInt, String paramString1, String paramString2, String paramString3, Long paramLong2)
  {
    this.id = paramLong1;
    this.buttonId = paramLong;
    this.ordr = paramInt;
    this.title = paramString1;
    this.icon = paramString2;
    this.colour = paramString3;
    this.idAdviceButton = paramLong2;
  }
  
  public void __setDaoSession(DaoSession paramDaoSession)
  {
    this.daoSession = paramDaoSession;
    if (paramDaoSession != null) {}
    for (paramDaoSession = paramDaoSession.getRST_ButtonDao();; paramDaoSession = null)
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
  
  public long getButtonId()
  {
    return this.buttonId;
  }
  
  public String getColour()
  {
    return this.colour;
  }
  
  public String getIcon()
  {
    return this.icon;
  }
  
  public Long getId()
  {
    return this.id;
  }
  
  public Long getIdAdviceButton()
  {
    return this.idAdviceButton;
  }
  
  public int getOrdr()
  {
    return this.ordr;
  }
  
  public RST_AdviceItem getRST_AdviceItem()
  {
    Long localLong = this.idAdviceButton;
    RST_AdviceItem localRST_AdviceItem;
    if ((this.rST_AdviceItem__resolvedKey == null) || (!this.rST_AdviceItem__resolvedKey.equals(localLong)))
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localRST_AdviceItem = (RST_AdviceItem)this.daoSession.getRST_AdviceItemDao().load(localLong);
    }
    try
    {
      this.rST_AdviceItem = localRST_AdviceItem;
      this.rST_AdviceItem__resolvedKey = localLong;
      return this.rST_AdviceItem;
    }
    finally {}
  }
  
  public String getTitle()
  {
    return this.title;
  }
  
  public void refresh()
  {
    if (this.myDao == null) {
      throw new DaoException("Entity is detached from DAO context");
    }
    this.myDao.refresh(this);
  }
  
  public void setButtonId(long paramLong)
  {
    this.buttonId = paramLong;
  }
  
  public void setColour(String paramString)
  {
    this.colour = paramString;
  }
  
  public void setIcon(String paramString)
  {
    this.icon = paramString;
  }
  
  public void setId(Long paramLong)
  {
    this.id = paramLong;
  }
  
  public void setIdAdviceButton(Long paramLong)
  {
    this.idAdviceButton = paramLong;
  }
  
  public void setOrdr(int paramInt)
  {
    this.ordr = paramInt;
  }
  
  /* Error */
  public void setRST_AdviceItem(RST_AdviceItem paramRST_AdviceItem)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield 102	com/resmed/refresh/model/RST_Button:rST_AdviceItem	Lcom/resmed/refresh/model/RST_AdviceItem;
    //   7: aload_1
    //   8: ifnonnull +21 -> 29
    //   11: aconst_null
    //   12: astore_1
    //   13: aload_0
    //   14: aload_1
    //   15: putfield 43	com/resmed/refresh/model/RST_Button:idAdviceButton	Ljava/lang/Long;
    //   18: aload_0
    //   19: aload_0
    //   20: getfield 43	com/resmed/refresh/model/RST_Button:idAdviceButton	Ljava/lang/Long;
    //   23: putfield 82	com/resmed/refresh/model/RST_Button:rST_AdviceItem__resolvedKey	Ljava/lang/Long;
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: aload_1
    //   30: invokevirtual 118	com/resmed/refresh/model/RST_AdviceItem:getId	()J
    //   33: invokestatic 122	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   36: astore_1
    //   37: goto -24 -> 13
    //   40: astore_1
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_1
    //   44: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	45	0	this	RST_Button
    //   0	45	1	paramRST_AdviceItem	RST_AdviceItem
    // Exception table:
    //   from	to	target	type
    //   2	7	40	finally
    //   13	28	40	finally
    //   29	37	40	finally
    //   41	43	40	finally
  }
  
  public void setTitle(String paramString)
  {
    this.title = paramString;
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
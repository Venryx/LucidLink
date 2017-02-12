package com.resmed.refresh.model;

import de.greenrobot.dao.DaoException;

public class RST_ValueItem
{
  private transient DaoSession daoSession;
  private Long id;
  private Long idEnvironmentalInfoL;
  private Long idEnvironmentalInfoS;
  private Long idEnvironmentalInfoT;
  private Long idSleepSignalRatings;
  private Long idSleepStates;
  private RST_EnvironmentalInfo light;
  private Long light__resolvedKey;
  private transient RST_ValueItemDao myDao;
  private int ordr;
  private RST_SleepSessionInfo sleepSignalRatings;
  private Long sleepSignalRatings__resolvedKey;
  private RST_SleepSessionInfo sleepStates;
  private Long sleepStates__resolvedKey;
  private RST_EnvironmentalInfo sound;
  private Long sound__resolvedKey;
  private RST_EnvironmentalInfo temperature;
  private Long temperature__resolvedKey;
  private float value;
  
  public RST_ValueItem() {}
  
  public RST_ValueItem(Long paramLong)
  {
    this.id = paramLong;
  }
  
  public RST_ValueItem(Long paramLong1, float paramFloat, int paramInt, Long paramLong2, Long paramLong3, Long paramLong4, Long paramLong5, Long paramLong6)
  {
    this.id = paramLong1;
    this.value = paramFloat;
    this.ordr = paramInt;
    this.idEnvironmentalInfoL = paramLong2;
    this.idEnvironmentalInfoT = paramLong3;
    this.idEnvironmentalInfoS = paramLong4;
    this.idSleepStates = paramLong5;
    this.idSleepSignalRatings = paramLong6;
  }
  
  public void __setDaoSession(DaoSession paramDaoSession)
  {
    this.daoSession = paramDaoSession;
    if (paramDaoSession != null) {}
    for (paramDaoSession = paramDaoSession.getRST_ValueItemDao();; paramDaoSession = null)
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
  
  public Long getId()
  {
    return this.id;
  }
  
  public Long getIdEnvironmentalInfoL()
  {
    return this.idEnvironmentalInfoL;
  }
  
  public Long getIdEnvironmentalInfoS()
  {
    return this.idEnvironmentalInfoS;
  }
  
  public Long getIdEnvironmentalInfoT()
  {
    return this.idEnvironmentalInfoT;
  }
  
  public Long getIdSleepSignalRatings()
  {
    return this.idSleepSignalRatings;
  }
  
  public Long getIdSleepStates()
  {
    return this.idSleepStates;
  }
  
  public RST_EnvironmentalInfo getLight()
  {
    Long localLong = this.idEnvironmentalInfoL;
    RST_EnvironmentalInfo localRST_EnvironmentalInfo;
    if ((this.light__resolvedKey == null) || (!this.light__resolvedKey.equals(localLong)))
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localRST_EnvironmentalInfo = (RST_EnvironmentalInfo)this.daoSession.getRST_EnvironmentalInfoDao().load(localLong);
    }
    try
    {
      this.light = localRST_EnvironmentalInfo;
      this.light__resolvedKey = localLong;
      return this.light;
    }
    finally {}
  }
  
  public int getOrdr()
  {
    return this.ordr;
  }
  
  public RST_SleepSessionInfo getSleepSignalRatings()
  {
    Long localLong = this.idSleepSignalRatings;
    RST_SleepSessionInfo localRST_SleepSessionInfo;
    if ((this.sleepSignalRatings__resolvedKey == null) || (!this.sleepSignalRatings__resolvedKey.equals(localLong)))
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localRST_SleepSessionInfo = (RST_SleepSessionInfo)this.daoSession.getRST_SleepSessionInfoDao().load(localLong);
    }
    try
    {
      this.sleepSignalRatings = localRST_SleepSessionInfo;
      this.sleepSignalRatings__resolvedKey = localLong;
      return this.sleepSignalRatings;
    }
    finally {}
  }
  
  public RST_SleepSessionInfo getSleepStates()
  {
    Long localLong = this.idSleepStates;
    RST_SleepSessionInfo localRST_SleepSessionInfo;
    if ((this.sleepStates__resolvedKey == null) || (!this.sleepStates__resolvedKey.equals(localLong)))
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localRST_SleepSessionInfo = (RST_SleepSessionInfo)this.daoSession.getRST_SleepSessionInfoDao().load(localLong);
    }
    try
    {
      this.sleepStates = localRST_SleepSessionInfo;
      this.sleepStates__resolvedKey = localLong;
      return this.sleepStates;
    }
    finally {}
  }
  
  public RST_EnvironmentalInfo getSound()
  {
    Long localLong = this.idEnvironmentalInfoS;
    RST_EnvironmentalInfo localRST_EnvironmentalInfo;
    if ((this.sound__resolvedKey == null) || (!this.sound__resolvedKey.equals(localLong)))
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localRST_EnvironmentalInfo = (RST_EnvironmentalInfo)this.daoSession.getRST_EnvironmentalInfoDao().load(localLong);
    }
    try
    {
      this.sound = localRST_EnvironmentalInfo;
      this.sound__resolvedKey = localLong;
      return this.sound;
    }
    finally {}
  }
  
  public RST_EnvironmentalInfo getTemperature()
  {
    Long localLong = this.idEnvironmentalInfoT;
    RST_EnvironmentalInfo localRST_EnvironmentalInfo;
    if ((this.temperature__resolvedKey == null) || (!this.temperature__resolvedKey.equals(localLong)))
    {
      if (this.daoSession == null) {
        throw new DaoException("Entity is detached from DAO context");
      }
      localRST_EnvironmentalInfo = (RST_EnvironmentalInfo)this.daoSession.getRST_EnvironmentalInfoDao().load(localLong);
    }
    try
    {
      this.temperature = localRST_EnvironmentalInfo;
      this.temperature__resolvedKey = localLong;
      return this.temperature;
    }
    finally {}
  }
  
  public float getValue()
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
  
  public void setId(Long paramLong)
  {
    this.id = paramLong;
  }
  
  public void setIdEnvironmentalInfoL(Long paramLong)
  {
    this.idEnvironmentalInfoL = paramLong;
  }
  
  public void setIdEnvironmentalInfoS(Long paramLong)
  {
    this.idEnvironmentalInfoS = paramLong;
  }
  
  public void setIdEnvironmentalInfoT(Long paramLong)
  {
    this.idEnvironmentalInfoT = paramLong;
  }
  
  public void setIdSleepSignalRatings(Long paramLong)
  {
    this.idSleepSignalRatings = paramLong;
  }
  
  public void setIdSleepStates(Long paramLong)
  {
    this.idSleepStates = paramLong;
  }
  
  /* Error */
  public void setLight(RST_EnvironmentalInfo paramRST_EnvironmentalInfo)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield 110	com/resmed/refresh/model/RST_ValueItem:light	Lcom/resmed/refresh/model/RST_EnvironmentalInfo;
    //   7: aload_1
    //   8: ifnonnull +21 -> 29
    //   11: aconst_null
    //   12: astore_1
    //   13: aload_0
    //   14: aload_1
    //   15: putfield 46	com/resmed/refresh/model/RST_ValueItem:idEnvironmentalInfoL	Ljava/lang/Long;
    //   18: aload_0
    //   19: aload_0
    //   20: getfield 46	com/resmed/refresh/model/RST_ValueItem:idEnvironmentalInfoL	Ljava/lang/Long;
    //   23: putfield 90	com/resmed/refresh/model/RST_ValueItem:light__resolvedKey	Ljava/lang/Long;
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: aload_1
    //   30: invokevirtual 157	com/resmed/refresh/model/RST_EnvironmentalInfo:getId	()Ljava/lang/Long;
    //   33: astore_1
    //   34: goto -21 -> 13
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	42	0	this	RST_ValueItem
    //   0	42	1	paramRST_EnvironmentalInfo	RST_EnvironmentalInfo
    // Exception table:
    //   from	to	target	type
    //   2	7	37	finally
    //   13	28	37	finally
    //   29	34	37	finally
    //   38	40	37	finally
  }
  
  public void setOrdr(int paramInt)
  {
    this.ordr = paramInt;
  }
  
  /* Error */
  public void setSleepSignalRatings(RST_SleepSessionInfo paramRST_SleepSessionInfo)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield 127	com/resmed/refresh/model/RST_ValueItem:sleepSignalRatings	Lcom/resmed/refresh/model/RST_SleepSessionInfo;
    //   7: aload_1
    //   8: ifnonnull +21 -> 29
    //   11: aconst_null
    //   12: astore_1
    //   13: aload_0
    //   14: aload_1
    //   15: putfield 54	com/resmed/refresh/model/RST_ValueItem:idSleepSignalRatings	Ljava/lang/Long;
    //   18: aload_0
    //   19: aload_0
    //   20: getfield 54	com/resmed/refresh/model/RST_ValueItem:idSleepSignalRatings	Ljava/lang/Long;
    //   23: putfield 116	com/resmed/refresh/model/RST_ValueItem:sleepSignalRatings__resolvedKey	Ljava/lang/Long;
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: aload_1
    //   30: invokevirtual 164	com/resmed/refresh/model/RST_SleepSessionInfo:getId	()J
    //   33: invokestatic 168	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   36: astore_1
    //   37: goto -24 -> 13
    //   40: astore_1
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_1
    //   44: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	45	0	this	RST_ValueItem
    //   0	45	1	paramRST_SleepSessionInfo	RST_SleepSessionInfo
    // Exception table:
    //   from	to	target	type
    //   2	7	40	finally
    //   13	28	40	finally
    //   29	37	40	finally
    //   41	43	40	finally
  }
  
  /* Error */
  public void setSleepStates(RST_SleepSessionInfo paramRST_SleepSessionInfo)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield 132	com/resmed/refresh/model/RST_ValueItem:sleepStates	Lcom/resmed/refresh/model/RST_SleepSessionInfo;
    //   7: aload_1
    //   8: ifnonnull +21 -> 29
    //   11: aconst_null
    //   12: astore_1
    //   13: aload_0
    //   14: aload_1
    //   15: putfield 52	com/resmed/refresh/model/RST_ValueItem:idSleepStates	Ljava/lang/Long;
    //   18: aload_0
    //   19: aload_0
    //   20: getfield 52	com/resmed/refresh/model/RST_ValueItem:idSleepStates	Ljava/lang/Long;
    //   23: putfield 130	com/resmed/refresh/model/RST_ValueItem:sleepStates__resolvedKey	Ljava/lang/Long;
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: aload_1
    //   30: invokevirtual 164	com/resmed/refresh/model/RST_SleepSessionInfo:getId	()J
    //   33: invokestatic 168	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   36: astore_1
    //   37: goto -24 -> 13
    //   40: astore_1
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_1
    //   44: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	45	0	this	RST_ValueItem
    //   0	45	1	paramRST_SleepSessionInfo	RST_SleepSessionInfo
    // Exception table:
    //   from	to	target	type
    //   2	7	40	finally
    //   13	28	40	finally
    //   29	37	40	finally
    //   41	43	40	finally
  }
  
  /* Error */
  public void setSound(RST_EnvironmentalInfo paramRST_EnvironmentalInfo)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield 137	com/resmed/refresh/model/RST_ValueItem:sound	Lcom/resmed/refresh/model/RST_EnvironmentalInfo;
    //   7: aload_1
    //   8: ifnonnull +21 -> 29
    //   11: aconst_null
    //   12: astore_1
    //   13: aload_0
    //   14: aload_1
    //   15: putfield 50	com/resmed/refresh/model/RST_ValueItem:idEnvironmentalInfoS	Ljava/lang/Long;
    //   18: aload_0
    //   19: aload_0
    //   20: getfield 50	com/resmed/refresh/model/RST_ValueItem:idEnvironmentalInfoS	Ljava/lang/Long;
    //   23: putfield 135	com/resmed/refresh/model/RST_ValueItem:sound__resolvedKey	Ljava/lang/Long;
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: aload_1
    //   30: invokevirtual 157	com/resmed/refresh/model/RST_EnvironmentalInfo:getId	()Ljava/lang/Long;
    //   33: astore_1
    //   34: goto -21 -> 13
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	42	0	this	RST_ValueItem
    //   0	42	1	paramRST_EnvironmentalInfo	RST_EnvironmentalInfo
    // Exception table:
    //   from	to	target	type
    //   2	7	37	finally
    //   13	28	37	finally
    //   29	34	37	finally
    //   38	40	37	finally
  }
  
  /* Error */
  public void setTemperature(RST_EnvironmentalInfo paramRST_EnvironmentalInfo)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield 142	com/resmed/refresh/model/RST_ValueItem:temperature	Lcom/resmed/refresh/model/RST_EnvironmentalInfo;
    //   7: aload_1
    //   8: ifnonnull +21 -> 29
    //   11: aconst_null
    //   12: astore_1
    //   13: aload_0
    //   14: aload_1
    //   15: putfield 48	com/resmed/refresh/model/RST_ValueItem:idEnvironmentalInfoT	Ljava/lang/Long;
    //   18: aload_0
    //   19: aload_0
    //   20: getfield 48	com/resmed/refresh/model/RST_ValueItem:idEnvironmentalInfoT	Ljava/lang/Long;
    //   23: putfield 140	com/resmed/refresh/model/RST_ValueItem:temperature__resolvedKey	Ljava/lang/Long;
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: aload_1
    //   30: invokevirtual 157	com/resmed/refresh/model/RST_EnvironmentalInfo:getId	()Ljava/lang/Long;
    //   33: astore_1
    //   34: goto -21 -> 13
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	42	0	this	RST_ValueItem
    //   0	42	1	paramRST_EnvironmentalInfo	RST_EnvironmentalInfo
    // Exception table:
    //   from	to	target	type
    //   2	7	37	finally
    //   13	28	37	finally
    //   29	34	37	finally
    //   38	40	37	finally
  }
  
  public void setValue(float paramFloat)
  {
    this.value = paramFloat;
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
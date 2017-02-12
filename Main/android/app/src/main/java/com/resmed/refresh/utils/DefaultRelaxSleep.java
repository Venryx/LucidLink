package com.resmed.refresh.utils;

import android.os.CountDownTimer;
import com.resmed.refresh.ui.activity.SleepTimeActivity;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.utils.audio.SoundResources.RelaxSound;
import java.util.ArrayList;

public class DefaultRelaxSleep
  extends RelaxSoundManager
{
  private SleepTimeActivity activity;
  private RelaxSoundManager.UserStatus status;
  private int userCurrentVolume = -1;
  
  public DefaultRelaxSleep(BaseBluetoothActivity paramBaseBluetoothActivity, RelaxPlayerCallback paramRelaxPlayerCallback)
  {
    this.activity = ((SleepTimeActivity)paramBaseBluetoothActivity);
    this.relaxCallback = paramRelaxPlayerCallback;
    this.waitingForBreathingRate = true;
  }
  
  private void updateState()
  {
    for (;;)
    {
      try
      {
        if (this.workFlowStack != null)
        {
          boolean bool = this.workFlowStack.isEmpty();
          if (!bool) {}
        }
        else
        {
          return;
        }
        RelaxState localRelaxState = (RelaxState)this.workFlowStack.get(this.workFlowStack.size() - 1);
        switch (localRelaxState.getStage())
        {
        default: 
          break;
        case EMPTY_STACK: 
          this.waitingForBreathingRate = false;
          this.workFlowStack.remove(this.workFlowStack.size() - 1);
          break;
        case INTERVAL_STAGE_BREATH_RATE: 
          f = ((RelaxState)localObject).getBPM() - RelaxState.RATE_DROP_LEVEL;
        }
      }
      finally {}
      float f;
      if (((RelaxState)localObject).getBPM() == 6.0F)
      {
        this.workFlowStack.remove(this.workFlowStack.size() - 1);
        Log.i("com.resmed.refresh.relax", "workflowThread - next step of the workflow => Steady");
      }
      else if (f < 6.0F)
      {
        ((RelaxState)this.workFlowStack.get(this.workFlowStack.size() - 1)).setBPM(6.0F);
        Log.i("com.resmed.refresh.relax", "workflowThread - next step MIN_RATE");
      }
      else if (f > 12.0F)
      {
        ((RelaxState)this.workFlowStack.get(this.workFlowStack.size() - 1)).setBPM(12.0F);
        Log.i("com.resmed.refresh.relax", "workflowThread - next step MAX_RATE");
      }
      else
      {
        ((RelaxState)this.workFlowStack.get(this.workFlowStack.size() - 1)).setBPM(f);
        StringBuilder localStringBuilder = new java.lang.StringBuilder;
        localStringBuilder.<init>("workflowThread - next step ");
        Log.i("com.resmed.refresh.relax", f + " bpms");
        continue;
        ((RelaxState)this.workFlowStack.get(this.workFlowStack.size() - 1)).setStage(RelaxState.IntervalStage.INTERVAL_STAGE_CHECK_AWAKE);
        localStringBuilder = new java.lang.StringBuilder;
        localStringBuilder.<init>("workflowThread - UserStatus ");
        Log.i("com.resmed.refresh.relax", this.status);
        localStringBuilder = new java.lang.StringBuilder;
        localStringBuilder.<init>("Relax - INTERVAL_STAGE_CHECK_AWAKE=");
        AppFileLog.addTrace(this.status);
        if (this.status == RelaxSoundManager.UserStatus.USER_ASLEEP)
        {
          this.workFlowStack.remove(this.workFlowStack.size() - 1);
          Log.i("com.resmed.refresh.relax", "workflowThread - next step volume");
        }
        else
        {
          localStringBuilder = new java.lang.StringBuilder;
          localStringBuilder.<init>("workflowThread - ");
          Log.i("com.resmed.refresh.relax", getTimePlayed() + " + " + TOTAL_VOLUME_DROPOFF + " >= " + MAX_WORKFLOW);
          if (getTimePlayed() + TOTAL_VOLUME_DROPOFF >= MAX_WORKFLOW)
          {
            this.workFlowStack.remove(this.workFlowStack.size() - 1);
            Log.i("com.resmed.refresh.relax", "workflowThread - next step drop volume");
            continue;
            int i = localStringBuilder.getVolume() - RelaxState.VOLUME_DROP_LEVEL;
            if (i <= 0)
            {
              this.workFlowStack.remove(this.workFlowStack.size() - 1);
              Log.i("com.resmed.refresh.relax", "workflowThread - next step end");
            }
            else
            {
              ((RelaxState)this.workFlowStack.get(this.workFlowStack.size() - 1)).setVolume(i);
              localStringBuilder = new java.lang.StringBuilder;
              localStringBuilder.<init>("workflowThread - next step ");
              Log.i("com.resmed.refresh.relax", i + " volume");
            }
          }
        }
      }
    }
  }
  
  protected CountDownTimer createCountDownTimer(int paramInt)
  {
    Log.e("com.resmed.refresh.relax", "workflowThread - createCountDownTimer next workflow in " + paramInt);
    new CountDownTimer(paramInt, 1000L)
    {
      public void onFinish()
      {
        DefaultRelaxSleep.this.updateState();
        DefaultRelaxSleep.this.executeWorkflow();
      }
      
      public void onTick(long paramAnonymousLong)
      {
        if (paramAnonymousLong < 2000L) {
          DefaultRelaxSleep.this.activity.requestUserStatus();
        }
      }
    }.start();
  }
  
  protected void endWorkflow()
  {
    this.relaxCallback.onRelaxComplete();
    resetState();
    AppFileLog.addTrace("Relax - endWorkflow");
  }
  
  /* Error */
  protected void executeWorkflow()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 79	com/resmed/refresh/utils/DefaultRelaxSleep:workFlowStack	Ljava.util.List;
    //   6: invokeinterface 85 1 0
    //   11: ifeq +23 -> 34
    //   14: ldc 113
    //   16: ldc -17
    //   18: invokestatic 121	com/resmed/refresh/utils/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   21: pop
    //   22: aload_0
    //   23: iconst_0
    //   24: putfield 242	com/resmed/refresh/utils/DefaultRelaxSleep:isPlaying	Z
    //   27: aload_0
    //   28: invokevirtual 244	com/resmed/refresh/utils/DefaultRelaxSleep:endWorkflow	()V
    //   31: aload_0
    //   32: monitorexit
    //   33: return
    //   34: aload_0
    //   35: getfield 79	com/resmed/refresh/utils/DefaultRelaxSleep:workFlowStack	Ljava.util.List;
    //   38: aload_0
    //   39: getfield 79	com/resmed/refresh/utils/DefaultRelaxSleep:workFlowStack	Ljava.util.List;
    //   42: invokeinterface 88 1 0
    //   47: iconst_1
    //   48: isub
    //   49: invokeinterface 92 2 0
    //   54: checkcast 94	com/resmed/refresh/utils/RelaxState
    //   57: astore_2
    //   58: aload_0
    //   59: getfield 248	com/resmed/refresh/utils/DefaultRelaxSleep:diracDefaultManager	Lcom/resmed/diracndk/DiracDefaultManager;
    //   62: aload_2
    //   63: invokevirtual 251	com/resmed/refresh/utils/RelaxState:getDuration	()F
    //   66: invokevirtual 256	com/resmed/diracndk/DiracDefaultManager:changeDuration	(F)V
    //   69: aload_0
    //   70: aload_2
    //   71: invokevirtual 198	com/resmed/refresh/utils/RelaxState:getVolume	()I
    //   74: invokevirtual 259	com/resmed/refresh/utils/DefaultRelaxSleep:setCurrentVolume	(I)V
    //   77: aload_0
    //   78: aload_0
    //   79: aload_0
    //   80: aload_2
    //   81: invokevirtual 100	com/resmed/refresh/utils/RelaxState:getStage	()Lcom/resmed/refresh/utils/RelaxState$IntervalStage;
    //   84: invokevirtual 263	com/resmed/refresh/utils/DefaultRelaxSleep:getTimeForStage	(Lcom/resmed/refresh/utils/RelaxState$IntervalStage;)I
    //   87: invokevirtual 265	com/resmed/refresh/utils/DefaultRelaxSleep:createCountDownTimer	(I)Landroid/os/CountDownTimer;
    //   90: putfield 269	com/resmed/refresh/utils/DefaultRelaxSleep:countDownTimer	Landroid/os/CountDownTimer;
    //   93: aload_0
    //   94: invokevirtual 180	com/resmed/refresh/utils/DefaultRelaxSleep:getTimePlayed	()I
    //   97: getstatic 274	com/resmed/refresh/ui/utils/Consts:RELAX_TIME_DIVIDER	I
    //   100: imul
    //   101: istore_1
    //   102: new 132	java/lang/StringBuilder
    //   105: astore_3
    //   106: aload_3
    //   107: ldc_w 276
    //   110: invokespecial 137	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   113: ldc 113
    //   115: aload_3
    //   116: aload_0
    //   117: invokevirtual 180	com/resmed/refresh/utils/DefaultRelaxSleep:getTimePlayed	()I
    //   120: invokevirtual 183	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   123: invokevirtual 150	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   126: invokestatic 121	com/resmed/refresh/utils/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   129: pop
    //   130: ldc_w 278
    //   133: iconst_2
    //   134: anewarray 280	java/lang/Object
    //   137: dup
    //   138: iconst_0
    //   139: getstatic 286	java.util.concurrent/TimeUnit:MILLISECONDS	Ljava.util.concurrent/TimeUnit;
    //   142: iload_1
    //   143: i2l
    //   144: invokevirtual 290	java.util.concurrent/TimeUnit:toMinutes	(J)J
    //   147: invokestatic 296	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   150: aastore
    //   151: dup
    //   152: iconst_1
    //   153: getstatic 286	java.util.concurrent/TimeUnit:MILLISECONDS	Ljava.util.concurrent/TimeUnit;
    //   156: iload_1
    //   157: i2l
    //   158: invokevirtual 299	java.util.concurrent/TimeUnit:toSeconds	(J)J
    //   161: getstatic 302	java.util.concurrent/TimeUnit:MINUTES	Ljava.util.concurrent/TimeUnit;
    //   164: getstatic 286	java.util.concurrent/TimeUnit:MILLISECONDS	Ljava.util.concurrent/TimeUnit;
    //   167: iload_1
    //   168: i2l
    //   169: invokevirtual 290	java.util.concurrent/TimeUnit:toMinutes	(J)J
    //   172: invokevirtual 299	java.util.concurrent/TimeUnit:toSeconds	(J)J
    //   175: lsub
    //   176: invokestatic 296	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   179: aastore
    //   180: invokestatic 308	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   183: astore_3
    //   184: new 132	java/lang/StringBuilder
    //   187: astore 4
    //   189: aload 4
    //   191: ldc_w 310
    //   194: invokespecial 137	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   197: ldc 113
    //   199: aload 4
    //   201: aload_3
    //   202: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   205: ldc_w 312
    //   208: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   211: aload_2
    //   212: invokevirtual 251	com/resmed/refresh/utils/RelaxState:getDuration	()F
    //   215: invokevirtual 141	java/lang/StringBuilder:append	(F)Ljava/lang/StringBuilder;
    //   218: ldc_w 314
    //   221: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   224: aload_2
    //   225: invokevirtual 107	com/resmed/refresh/utils/RelaxState:getBPM	()F
    //   228: invokevirtual 141	java/lang/StringBuilder:append	(F)Ljava/lang/StringBuilder;
    //   231: ldc_w 316
    //   234: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   237: aload_2
    //   238: invokevirtual 198	com/resmed/refresh/utils/RelaxState:getVolume	()I
    //   241: invokevirtual 183	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   244: invokevirtual 150	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   247: invokestatic 121	com/resmed/refresh/utils/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   250: pop
    //   251: new 132	java/lang/StringBuilder
    //   254: astore 4
    //   256: aload 4
    //   258: ldc_w 318
    //   261: invokespecial 137	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   264: aload 4
    //   266: aload_3
    //   267: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   270: ldc_w 312
    //   273: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   276: aload_2
    //   277: invokevirtual 251	com/resmed/refresh/utils/RelaxState:getDuration	()F
    //   280: invokevirtual 141	java/lang/StringBuilder:append	(F)Ljava/lang/StringBuilder;
    //   283: ldc_w 314
    //   286: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   289: aload_2
    //   290: invokevirtual 107	com/resmed/refresh/utils/RelaxState:getBPM	()F
    //   293: invokevirtual 141	java/lang/StringBuilder:append	(F)Ljava/lang/StringBuilder;
    //   296: ldc_w 316
    //   299: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   302: aload_2
    //   303: invokevirtual 198	com/resmed/refresh/utils/RelaxState:getVolume	()I
    //   306: invokevirtual 183	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   309: invokevirtual 150	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   312: invokestatic 168	com/resmed/refresh/utils/AppFileLog:addTrace	(Ljava/lang/String;)V
    //   315: goto -284 -> 31
    //   318: astore_2
    //   319: aload_0
    //   320: monitorexit
    //   321: aload_2
    //   322: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	323	0	this	DefaultRelaxSleep
    //   101	67	1	i	int
    //   57	246	2	localRelaxState	RelaxState
    //   318	4	2	localObject1	Object
    //   105	162	3	localObject2	Object
    //   187	78	4	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   2	31	318	finally
    //   34	315	318	finally
  }
  
  protected float getDefaultTime()
  {
    return 0.5833333F;
  }
  
  public void notifyUserStatus(RelaxSoundManager.UserStatus paramUserStatus)
  {
    this.status = paramUserStatus;
    Log.d("com.resmed.refresh.relax", "notifyUserStatus status " + paramUserStatus);
  }
  
  public void setBreathingRate(double paramDouble)
  {
    Log.i("com.resmed.refresh.relax", "workflowThread - setBreathingRate, received rate: " + paramDouble);
    if ((-1.0D == paramDouble) || (this.workFlowStack == null) || (this.workFlowStack.isEmpty())) {
      return;
    }
    if (this.waitingForBreathingRate)
    {
      this.waitingForBreathingRate = false;
      if (this.countDownTimer != null) {
        this.countDownTimer.cancel();
      }
      AppFileLog.addTrace("Relax - setBreathingRate:" + paramDouble);
      updateState();
      if (paramDouble <= 12.0D) {
        break label144;
      }
      ((RelaxState)this.workFlowStack.get(this.workFlowStack.size() - 1)).setBPM(12.0F);
    }
    for (;;)
    {
      executeWorkflow();
      break;
      break;
      label144:
      if (paramDouble < 6.0D) {
        ((RelaxState)this.workFlowStack.get(this.workFlowStack.size() - 1)).setBPM(6.0F);
      } else {
        ((RelaxState)this.workFlowStack.get(this.workFlowStack.size() - 1)).setBPM((float)paramDouble);
      }
    }
  }
  
  protected void setupStack()
  {
    if (this.workFlowStack != null) {
      this.workFlowStack.clear();
    }
    this.status = RelaxSoundManager.UserStatus.UNKNOWN;
    this.workFlowStack = new ArrayList();
    this.workFlowStack.add(new RelaxState(RelaxState.IntervalStage.INTERVAL_STAGE_VOLUME_DROP_OFF, 6.0F, 90));
    this.workFlowStack.add(new RelaxState(RelaxState.IntervalStage.INTERVAL_STEADY, 6.0F, 100));
    this.workFlowStack.add(new RelaxState(RelaxState.IntervalStage.INTERVAL_STAGE_TRANSITION, 12 - RelaxState.RATE_DROP_LEVEL, 100));
    this.workFlowStack.add(new RelaxState(RelaxState.IntervalStage.INTERVAL_STAGE_BREATH_RATE, 12.0F, 100));
  }
  
  protected void setupVolume()
  {
    setupInitialVolume();
    int j = this.maxVolume * 70 / 100;
    Log.e("com.resmed.refresh.relax", "workflowThread - setupInitialVolume() - audio level: " + j + " audio max: " + this.maxVolume + " userCurrentVolume = " + this.userCurrentVolume);
    int i;
    if (this.userCurrentVolume <= 0)
    {
      this.userCurrentVolume = j;
      i = j;
    }
    for (;;)
    {
      this.audioManager.setStreamVolume(3, i, 0);
      return;
      i = j;
      if (this.userCurrentVolume < j) {
        i = this.userCurrentVolume;
      }
    }
  }
  
  public boolean startPlayer(SoundResources.RelaxSound paramRelaxSound)
  {
    boolean bool2 = true;
    Log.e("com.resmed.refresh.relax", "workflowThread - startPlayer() - isPlaying = " + this.isPlaying);
    boolean bool1;
    if (paramRelaxSound.getAssetFileDescriptor() == null) {
      bool1 = false;
    }
    for (;;)
    {
      return bool1;
      if (paramRelaxSound == this.currentFilePlayed)
      {
        bool1 = bool2;
        if (this.isPlaying) {}
      }
      else
      {
        init();
        Log.e("com.resmed.refresh.relax", "workflowThread - startPlayer() - create new player (start new thread)");
        this.isPlaying = true;
        playMedia(paramRelaxSound.getAssetFileDescriptor());
        setupVolume();
        this.currentFilePlayed = paramRelaxSound;
        this.startTime = System.currentTimeMillis();
        this.waitingForBreathingRate = true;
        executeWorkflow();
        bool1 = bool2;
      }
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
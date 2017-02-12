package com.resmed.refresh.utils;

import android.app.Activity;
import android.os.CountDownTimer;

import com.resmed.refresh.utils.audio.SoundResources.RelaxSound;
import java.util.ArrayList;

public class DefaultRelaxMeditation
  extends RelaxSoundManager
{
  public DefaultRelaxMeditation(Activity paramActivity, RelaxPlayerCallback paramRelaxPlayerCallback)
  {
    this.relaxCallback = paramRelaxPlayerCallback;
  }
  
  protected CountDownTimer createCountDownTimer(int paramInt)
  {
    Log.e("com.resmed.refresh.relax", "workflowThread - createCountDownTimer next workflow in " + paramInt);
    new CountDownTimer(paramInt, 1000L)
    {
      public void onFinish()
      {
        DefaultRelaxMeditation.this.executeWorkflow();
      }
      
      public void onTick(long paramAnonymousLong) {}
    }.start();
  }
  
  protected void endWorkflow()
  {
    Log.i("com.resmed.refresh.relax", "end workflow");
    this.relaxCallback.onRelaxComplete();
    resetState();
  }
  
  protected void executeWorkflow()
  {
    for (;;)
    {
      try
      {
        if (this.workFlowStack.isEmpty())
        {
          Log.i("com.resmed.refresh.relax", "workflowThread - end workflow");
          this.isPlaying = false;
          endWorkflow();
          return;
        }
        RelaxState localRelaxState = (RelaxState)this.workFlowStack.get(this.workFlowStack.size() - 1);
        this.diracDefaultManager.changeDuration(localRelaxState.getDuration());
        setCurrentVolume(localRelaxState.getVolume());
        this.countDownTimer = createCountDownTimer(getTimeForStage(localRelaxState.getStage()));
        StringBuilder localStringBuilder2 = new java.lang.StringBuilder;
        localStringBuilder2.<init>("workflowThread - updateState Dirac to ");
        Log.i("com.resmed.refresh.relax", localRelaxState.getDuration() + " or " + localRelaxState.getBPM() + " beats per minute.");
        switch (localRelaxState.getStage())
        {
        case INTERVAL_STAGE_TRANSITION: 
        default: 
          break;
        case EMPTY_STACK: 
          this.workFlowStack.remove(this.workFlowStack.size() - 1);
          break;
        case INTERVAL_STAGE_BREATH_RATE: 
          this.waitingForBreathingRate = false;
        }
      }
      finally {}
      float f = ((RelaxState)localObject).getBPM() - RelaxState.RATE_DROP_LEVEL;
      if (f < 6.0F)
      {
        this.workFlowStack.remove(this.workFlowStack.size() - 1);
        Log.i("com.resmed.refresh.relax", "workflowThread - next step of the workflow => Steady");
      }
      else if (f > 12.0F)
      {
        ((RelaxState)this.workFlowStack.get(this.workFlowStack.size() - 1)).setBPM(12.0F);
        Log.i("com.resmed.refresh.relax", "workflowThread - next step MAX_RATE");
      }
      else
      {
        ((RelaxState)this.workFlowStack.get(this.workFlowStack.size() - 1)).setBPM(f);
        StringBuilder localStringBuilder1 = new java.lang.StringBuilder;
        localStringBuilder1.<init>("workflowThread - next step ");
        Log.i("com.resmed.refresh.relax", f + " bpms");
        continue;
        this.workFlowStack.remove(this.workFlowStack.size() - 1);
        continue;
        int i = localStringBuilder1.getVolume() - RelaxState.VOLUME_DROP_LEVEL;
        if (i <= 0)
        {
          this.workFlowStack.remove(this.workFlowStack.size() - 1);
          Log.i("com.resmed.refresh.relax", "workflowThread - next step end");
        }
        else
        {
          ((RelaxState)this.workFlowStack.get(this.workFlowStack.size() - 1)).setVolume(i);
          localStringBuilder1 = new java.lang.StringBuilder;
          localStringBuilder1.<init>("workflowThread - next step ");
          Log.i("com.resmed.refresh.relax", i + " volume");
        }
      }
    }
  }
  
  protected float getDefaultTime()
  {
    return 0.5833333F;
  }
  
  protected void setupStack()
  {
    if (this.workFlowStack != null) {
      this.workFlowStack.clear();
    }
    this.workFlowStack = new ArrayList();
    this.workFlowStack.add(new RelaxState(RelaxState.IntervalStage.INTERVAL_STAGE_VOLUME_DROP_OFF, 6.0F, 90));
    this.workFlowStack.add(new RelaxState(RelaxState.IntervalStage.INTERVAL_STEADY, 6.0F, 100));
    this.workFlowStack.add(new RelaxState(RelaxState.IntervalStage.INTERVAL_STAGE_TRANSITION, 12.0F, 100));
  }
  
  public boolean startPlayer(SoundResources.RelaxSound paramRelaxSound)
  {
    boolean bool = true;
    if (paramRelaxSound.getAssetFileDescriptor() == null) {
      bool = false;
    }
    for (;;)
    {
      return bool;
      if (this.isPlaying)
      {
        Log.i("com.resmed.refresh.relax", "reset old player");
        resetState();
        startPlayer(paramRelaxSound);
      }
      else
      {
        init();
        Log.i("com.resmed.refresh.relax", "create new player (start new thread)");
        this.isPlaying = true;
        playMedia(paramRelaxSound.getAssetFileDescriptor());
        setupInitialVolume();
        this.currentFilePlayed = paramRelaxSound;
        this.startTime = System.currentTimeMillis();
        executeWorkflow();
      }
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.resmed.refresh.model.mindclear.MindClearManager;
import com.resmed.refresh.ui.activity.MindClearActivity;
import com.resmed.refresh.ui.font.GroteskBold;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.ActiveFragmentInterface;
import com.resmed.refresh.utils.FileManager;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import com.resmed.refresh.utils.audio.PCMDefaultRecorder;

import java.io.File;

public class MindClearVoiceFragment
  extends BaseFragment
  implements PCMDefaultRecorder.AudioSampleReceiver
{
  private String TAG = "MC_voice_fragment";
  private final int TIME_INTERVAL = 350;
  private PCMDefaultRecorder audioRecorder;
  private TextView controller;
  private File currentFile;
  private TextView discard;
  private File fileToDelete;
  private View.OnClickListener listener;
  private ActiveFragmentInterface mcActiveFragmentInterface;
  private MindClearVoiceBtn mindClearVoiceBtn;
  private String pathFile;
  private boolean recording;
  private final long recordingTime = 59000L;
  private TextView save;
  private long startTime = 0L;
  private long tick = 1000L;
  private long timeSpent = 0L;
  private CountDownTimer timer;
  private GroteskBold timerText;
  private RelativeLayout wrapperAnimation;
  
  private void createAndLaunchTimer()
  {
    Log.i(this.TAG, "tick: " + this.tick);
    this.timer = new CountDownTimer(this.tick, this.tick)
    {
      public void onFinish()
      {
        MindClearVoiceFragment.this.tickCallback();
      }
      
      public void onTick(long paramAnonymousLong) {}
    }.start();
  }
  
  private void deleteAllFiles()
  {
    FileManager.deleteFile(this.fileToDelete);
    FileManager.deleteFile(this.currentFile);
  }
  
  private void deletePCMFile()
  {
    FileManager.deleteFile(this.fileToDelete);
  }
  
  private String getTimerText()
  {
    if (this.timeSpent > 59000L) {}
    for (String str = "01:00";; str = RefreshTools.getMinsSecsStringForTime((int)this.timeSpent)) {
      return str;
    }
  }
  
  private void mapGUI(View paramView)
  {
    this.controller = ((TextView)paramView.findViewById(2131099945));
    this.discard = ((TextView)paramView.findViewById(2131099943));
    this.save = ((TextView)paramView.findViewById(2131099944));
    this.wrapperAnimation = ((RelativeLayout)paramView.findViewById(2131099940));
    this.timerText = ((GroteskBold)paramView.findViewById(2131099941));
  }
  
  private void pauseRecording()
  {
    if (this.audioRecorder != null) {
      this.audioRecorder.pause();
    }
  }
  
  private void playRecording()
  {
    if (this.audioRecorder != null) {
      this.audioRecorder.play();
    }
  }
  
  private void setupAudioRecorder()
  {
    String str = MindClearManager.getInstance().getFileName();
    this.audioRecorder = PCMDefaultRecorder.getInstance();
    this.audioRecorder.setupRecorder(str, MindClearManager.getInstance().getTempFileName(), 350, this);
    this.fileToDelete = new File(this.audioRecorder.getTempFilePath());
    this.currentFile = new File(this.audioRecorder.getFilePath());
  }
  
  private void setupListener()
  {
    try
    {
      View.OnClickListener local1 = new com/resmed/refresh/ui/fragments/MindClearVoiceFragment$1;
      local1.<init>(this);
      this.listener = local1;
      return;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  private void startAnimation(final Double paramDouble)
  {
    new Thread()
    {
      public void run()
      {
        try
        {
          FragmentActivity localFragmentActivity = MindClearVoiceFragment.this.getActivity();
          Runnable local1 = new com/resmed/refresh/ui/fragments/MindClearVoiceFragment$2$1;
          local1.<init>(this, paramDouble);
          localFragmentActivity.runOnUiThread(local1);
          return;
        }
        catch (Exception localException)
        {
          for (;;) {}
        }
      }
    }.start();
  }
  
  private void startRecording()
  {
    try
    {
      if (this.audioRecorder != null) {
        this.audioRecorder.startRecording();
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  private void stopRecording()
  {
    if (this.audioRecorder != null)
    {
      this.pathFile = this.audioRecorder.getFilePath();
      this.audioRecorder.stop(true);
      System.gc();
    }
  }
  
  private void tickCallback()
  {
    this.timeSpent += this.tick;
    this.timerText.setText(getTimerText());
    Log.i(this.TAG, "tickCallback() timeSpent: " + this.timeSpent + " recordingTime: " + 59000L);
    if (this.timeSpent > 59000L) {
      timeExpired();
    }
    for (;;)
    {
      return;
      this.tick = 1000L;
      createAndLaunchTimer();
    }
  }
  
  private void timeExpired()
  {
    this.controller.setVisibility(8);
    stopRecording();
  }
  
  private void timerPauseRecording()
  {
    if (this.timer != null)
    {
      this.timer.cancel();
      this.timer = null;
    }
    long l = (System.nanoTime() - this.startTime) / 1000000L % 1000L;
    Log.i(this.TAG, "timerPauseRecording() " + l + " timeSpent: " + this.timeSpent);
    this.timeSpent += l;
    Log.i(this.TAG, "timerPauseRecording() " + this.timeSpent);
  }
  
  private void timerStartRecording()
  {
    this.startTime = System.nanoTime();
    this.tick = 1000L;
    if (this.timeSpent != 0L) {
      this.tick = (1000L - this.timeSpent % 1000L);
    }
    createAndLaunchTimer();
    Log.i(this.TAG, "timerStartRecording() " + this.startTime + " tick: " + this.tick);
  }
  
  public void discardRecording(boolean paramBoolean)
  {
    stopRecording();
    deleteAllFiles();
    if (!paramBoolean) {
      this.mindClearVoiceBtn.setVoiceButton();
    }
  }
  
  public void handleAudioAmplitude(Double paramDouble)
  {
    Log.i("com.resmed.refresh.recorder", "AMPLITUDE: " + paramDouble);
    startAnimation(paramDouble);
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.mindClearVoiceBtn = ((MindClearVoiceBtn)paramActivity);
      this.mcActiveFragmentInterface = ((ActiveFragmentInterface)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new ClassCastException(paramActivity.toString() + " ...you must implement MindClearVoiceBtn from your Activity ;-) !");
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903134, paramViewGroup, false);
    this.recording = true;
    this.timeSpent = 0L;
    this.startTime = 0L;
    this.tick = 1000L;
    if (this.timer != null) {
      this.timer.cancel();
    }
    this.timer = null;
    mapGUI(paramLayoutInflater);
    setupListener();
    this.discard.setOnClickListener(this.listener);
    this.save.setOnClickListener(this.listener);
    this.controller.setOnClickListener(this.listener);
    setupAudioRecorder();
    startRecording();
    timerStartRecording();
    return paramLayoutInflater;
  }
  
  public void onPause()
  {
    super.onPause();
    if (this.recording)
    {
      pauseRecording();
      this.recording = false;
      this.controller.setText(2131165362);
      timerPauseRecording();
    }
  }
  
  public void onResume()
  {
    super.onResume();
    ActiveFragmentInterface localActiveFragmentInterface = this.mcActiveFragmentInterface;
    ((MindClearActivity)getActivity()).getClass();
    localActiveFragmentInterface.notifyCurrentFragment(3);
  }
  
  public void onStop()
  {
    super.onStop();
    System.gc();
  }
  
  public static abstract interface MindClearVoiceBtn
  {
    public abstract void setVoiceButton();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\MindClearVoiceFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
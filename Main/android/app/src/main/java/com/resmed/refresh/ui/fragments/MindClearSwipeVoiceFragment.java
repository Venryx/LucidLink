package com.resmed.refresh.ui.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MindClearSwipeVoiceFragment
  extends BaseFragment
{
  private TextView duration;
  private String filePath;
  private boolean isInit = true;
  private boolean isPlaying;
  private MediaPlayer mediaPlayer;
  private TextView progress;
  private MindClearPagerFragment ref;
  Runnable run = new Runnable()
  {
    public void run()
    {
      if (MindClearSwipeVoiceFragment.this.threadIsRunning) {
        MindClearSwipeVoiceFragment.this.seekUpdation();
      }
    }
  };
  private SeekBar seekBar;
  private Handler seekHandler = new Handler();
  private boolean threadIsRunning = false;
  private Integer timeSpent = Integer.valueOf(0);
  private ImageView voiceController;
  
  public MindClearSwipeVoiceFragment(MindClearPagerFragment paramMindClearPagerFragment, String paramString)
  {
    this.filePath = paramString;
    this.ref = paramMindClearPagerFragment;
  }
  
  private void mapGUI(View paramView)
  {
    this.voiceController = ((ImageView)paramView.findViewById(2131100684));
    this.seekBar = ((SeekBar)paramView.findViewById(2131100683));
    this.progress = ((TextView)paramView.findViewById(2131100681));
    this.duration = ((TextView)paramView.findViewById(2131100682));
  }
  
  private void setTimeLabel(int paramInt)
  {
    this.timeSpent = Integer.valueOf(paramInt / 1000);
    Object localObject2 = TimeZone.getTimeZone("UTC");
    Object localObject1 = new SimpleDateFormat("mm:ss");
    ((SimpleDateFormat)localObject1).setTimeZone((TimeZone)localObject2);
    localObject2 = ((SimpleDateFormat)localObject1).format(new Date(this.timeSpent.intValue() * 1000L));
    localObject1 = ((SimpleDateFormat)localObject1).format(new Date(this.mediaPlayer.getDuration() - this.timeSpent.intValue() * 1000L));
    this.progress.setText((CharSequence)localObject2);
    this.duration.setText("-" + (String)localObject1);
  }
  
  private void setupListener()
  {
    View.OnClickListener local3 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        switch (paramAnonymousView.getId())
        {
        }
        for (;;)
        {
          return;
          if (MindClearSwipeVoiceFragment.this.isPlaying)
          {
            MindClearSwipeVoiceFragment.this.threadIsRunning = false;
            MindClearSwipeVoiceFragment.this.mediaPlayer.pause();
            MindClearSwipeVoiceFragment.this.voiceController.setImageResource(2130837832);
            MindClearSwipeVoiceFragment.this.isPlaying = false;
          }
          else
          {
            if (!MindClearSwipeVoiceFragment.this.threadIsRunning)
            {
              MindClearSwipeVoiceFragment.this.threadIsRunning = true;
              MindClearSwipeVoiceFragment.this.seekUpdation();
              MindClearSwipeVoiceFragment.this.duration.setText(MindClearSwipeVoiceFragment.this.duration.getText().toString().replace("-", ""));
            }
            MindClearSwipeVoiceFragment.this.mediaPlayer.start();
            MindClearSwipeVoiceFragment.this.voiceController.setImageResource(2130837829);
            MindClearSwipeVoiceFragment.this.isPlaying = true;
          }
        }
      }
    };
    this.voiceController.setOnClickListener(local3);
    this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
    {
      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        Log.i("SEEKBAR TRACKING", String.valueOf(paramAnonymousSeekBar.getProgress()));
        if (paramAnonymousBoolean)
        {
          MindClearSwipeVoiceFragment.this.mediaPlayer.seekTo(paramAnonymousInt);
          if (paramAnonymousInt / 1000 != MindClearSwipeVoiceFragment.this.timeSpent.intValue()) {
            MindClearSwipeVoiceFragment.this.setTimeLabel(paramAnonymousInt);
          }
        }
      }
      
      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {}
      
      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
        Log.i("SEEKBAR TRACKING", String.valueOf(paramAnonymousSeekBar.getProgress()));
      }
    });
  }
  
  private void setupMediaPlayer()
  {
    this.mediaPlayer = new MediaPlayer();
    this.mediaPlayer.setAudioStreamType(3);
    try
    {
      File localFile = new java/io/File;
      localFile.<init>(this.filePath);
      System.out.println(localFile.exists());
      FileInputStream localFileInputStream = new java/io/FileInputStream;
      localFileInputStream.<init>(localFile);
      this.mediaPlayer.setDataSource(localFileInputStream.getFD());
      localFileInputStream.close();
      this.mediaPlayer.prepare();
      this.mediaPlayer.setVolume(1.0F, 1.0F);
      this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
      {
        public void onCompletion(MediaPlayer paramAnonymousMediaPlayer)
        {
          MindClearSwipeVoiceFragment.this.resetMediaPlayer();
        }
      });
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      for (;;)
      {
        localIllegalArgumentException.printStackTrace();
      }
    }
    catch (SecurityException localSecurityException)
    {
      for (;;)
      {
        localSecurityException.printStackTrace();
      }
    }
    catch (IllegalStateException localIllegalStateException)
    {
      for (;;)
      {
        localIllegalStateException.printStackTrace();
      }
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        localIOException.printStackTrace();
      }
    }
  }
  
  private void textViewSetup()
  {
    this.duration.setText(RefreshTools.getMinsSecsStringForTime(this.mediaPlayer.getDuration()));
    this.progress.setText("00:00");
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903203, paramViewGroup, false);
    setupMediaPlayer();
    this.isPlaying = false;
    mapGUI(paramLayoutInflater);
    this.seekBar.setMax(this.mediaPlayer.getDuration());
    textViewSetup();
    setupListener();
    return paramLayoutInflater;
  }
  
  public void onResume()
  {
    super.onResume();
    if (!this.isInit) {
      if (this.ref != null) {
        this.ref.getAdapter().resetAllAudio();
      }
    }
    for (;;)
    {
      return;
      this.isInit = false;
    }
  }
  
  public void onStop()
  {
    super.onStop();
    this.threadIsRunning = false;
    this.mediaPlayer.pause();
    this.voiceController.setImageResource(2130837832);
    this.isPlaying = false;
  }
  
  public void resetMediaPlayer()
  {
    this.isPlaying = false;
    this.voiceController.setImageResource(2130837832);
    this.timeSpent = Integer.valueOf(0);
    textViewSetup();
    this.mediaPlayer.reset();
    setupMediaPlayer();
    this.seekBar.setProgress(0);
    this.threadIsRunning = false;
  }
  
  public void seekUpdation()
  {
    Log.d("com.resmed.refresh.player", Integer.valueOf(this.mediaPlayer.getCurrentPosition()).toString());
    Integer localInteger = Integer.valueOf(this.mediaPlayer.getCurrentPosition());
    this.seekBar.setProgress(localInteger.intValue());
    Log.i("com.resmed.refresh.player", "seek bar set progress -> position: " + localInteger + "\t Time Spent seek Updation: " + this.timeSpent);
    this.seekHandler.postDelayed(this.run, 100L);
    if (localInteger.intValue() / 1000 > this.timeSpent.intValue()) {
      setTimeLabel(localInteger.intValue());
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\MindClearSwipeVoiceFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.diracndk;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

public class IndexActivity
  extends Activity
{
  public static String TAG = "*dirac";
  Button buttonForFileName = null;
  private DiracDefaultManager diracDefaultManager;
  Button lastButtonPressed = null;
  double mPitchFactor = 1.0D;
  int mProcessingType = 1;
  double mTimeFactor = 1.0D;
  boolean mUseVarispeed = false;
  String pathFile;
  CheckBox playDirac = null;
  CheckBox playRaw = null;
  SeekBar seekPitch = null;
  SeekBar seekTime = null;
  CheckBox useBuffering = null;
  ToggleButton useVarispeed = null;
  
  private void enableCheckBoxes(boolean paramBoolean)
  {
    CheckBox localCheckBox2 = (CheckBox)findViewById(R.id.playRaw);
    CheckBox localCheckBox1 = (CheckBox)findViewById(R.id.playDirac);
    localCheckBox2.setEnabled(paramBoolean);
    localCheckBox1.setEnabled(paramBoolean);
  }
  
  private void handleButtonClick(View paramView, int paramInt)
  {
    String str = ((Button)paramView).getText().toString();
    if (str.startsWith("Stop"))
    {
      this.diracDefaultManager.stopAudioThreads();
      ((Button)paramView).setText(str.replace("Stop", "Play"));
      enableCheckBoxes(true);
    }
    for (;;)
    {
      return;
      resetButton((Button)paramView);
      ((Button)paramView).setText(str.replace("Play", "Stop"));
      paramView = getApplicationContext().getResources().openRawResourceFd(paramInt);
      enableCheckBoxes(false);
      playMedia(paramView);
    }
  }
  
  private void playMedia(AssetFileDescriptor paramAssetFileDescriptor)
  {
    FileDescriptor localFileDescriptor;
    int j;
    int i;
    if (paramAssetFileDescriptor != null)
    {
      localFileDescriptor = paramAssetFileDescriptor.getFileDescriptor();
      j = (int)paramAssetFileDescriptor.getStartOffset();
      i = (int)paramAssetFileDescriptor.getLength();
      if (this.diracDefaultManager != null) {
        this.diracDefaultManager.stopAudioThreads();
      }
      this.diracDefaultManager = null;
      this.diracDefaultManager = new DiracDefaultManager();
      this.diracDefaultManager.setProcessingType(this.mProcessingType);
    }
    try
    {
      this.diracDefaultManager.setupDirac(localFileDescriptor, j, i);
      return;
    }
    catch (IllegalArgumentException paramAssetFileDescriptor)
    {
      for (;;)
      {
        paramAssetFileDescriptor.printStackTrace();
      }
    }
  }
  
  private void resetButton(Button paramButton)
  {
    if ((this.lastButtonPressed != null) && (this.lastButtonPressed.getText().toString().startsWith("Stop")))
    {
      if (!this.lastButtonPressed.getText().toString().startsWith("Stop playing")) {
        break label62;
      }
      this.lastButtonPressed.setText("Play a File");
    }
    for (;;)
    {
      this.lastButtonPressed = paramButton;
      return;
      label62:
      this.lastButtonPressed.setText(this.lastButtonPressed.getText().toString().replace("Stop", "Play"));
    }
  }
  
  private void setProcessingType(int paramInt)
  {
    CheckBox localCheckBox1 = (CheckBox)findViewById(R.id.playRaw);
    CheckBox localCheckBox2 = (CheckBox)findViewById(R.id.playDirac);
    this.mProcessingType = paramInt;
    switch (this.mProcessingType)
    {
    }
    for (;;)
    {
      return;
      localCheckBox1.setChecked(true);
      localCheckBox2.setEnabled(false);
      continue;
      localCheckBox1.setChecked(false);
      localCheckBox2.setChecked(true);
      localCheckBox2.setEnabled(true);
      continue;
      localCheckBox1.setChecked(false);
      localCheckBox2.setEnabled(true);
    }
  }
  
  private void setUpUI()
  {
    ((Button)findViewById(R.id.loadFromFile)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (((String)((Button)paramAnonymousView).getText().toString().subSequence(0, 4)).startsWith("Stop"))
        {
          IndexActivity.this.diracDefaultManager.stopAudioThreads();
          ((Button)paramAnonymousView).setText("Play a file");
          IndexActivity.this.enableCheckBoxes(true);
        }
        for (;;)
        {
          return;
          IndexActivity.this.resetButton((Button)paramAnonymousView);
          IndexActivity.this.buttonForFileName = ((Button)paramAnonymousView);
          paramAnonymousView = new Intent("android.intent.action.GET_CONTENT");
          paramAnonymousView.setType("audio/vnd.wave");
          paramAnonymousView = Intent.createChooser(paramAnonymousView, "Select soundfile");
          IndexActivity.this.startActivityForResult(paramAnonymousView, 1);
          IndexActivity.this.enableCheckBoxes(false);
        }
      }
    });
    this.seekTime = ((SeekBar)findViewById(R.id.seekTime));
    this.seekTime.setMax(100);
    double d = (this.mTimeFactor - 0.5D) / 1.5D;
    this.seekTime.setProgress((int)Math.floor(100.0D * d));
    Object localObject = new SeekBar.OnSeekBarChangeListener()
    {
      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        IndexActivity.this.mTimeFactor = (paramAnonymousInt / 100.0D * 1.5D + 0.5D);
        if (IndexActivity.this.mUseVarispeed)
        {
          float f = (float)(1.0D / IndexActivity.this.mTimeFactor);
          IndexActivity.this.seekPitch.setProgress((int)((f - 0.5D) / 1.5D * 100.0D));
          IndexActivity.this.diracDefaultManager.changePitch((float)IndexActivity.this.mPitchFactor);
        }
        IndexActivity.this.diracDefaultManager.changeDuration((float)IndexActivity.this.mTimeFactor);
      }
      
      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {}
      
      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {}
    };
    this.seekTime.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener)localObject);
    this.seekPitch = ((SeekBar)findViewById(R.id.seekPitch));
    this.seekPitch.setMax(100);
    d = (this.mPitchFactor - 0.5D) / 1.5D;
    this.seekPitch.setProgress((int)Math.floor(100.0D * d));
    localObject = new SeekBar.OnSeekBarChangeListener()
    {
      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        IndexActivity.this.mPitchFactor = (paramAnonymousInt / 100.0D * 1.5D + 0.5D);
        IndexActivity.this.diracDefaultManager.changePitch((float)IndexActivity.this.mPitchFactor);
      }
      
      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {}
      
      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {}
    };
    this.seekPitch.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener)localObject);
    this.useBuffering = ((CheckBox)findViewById(R.id.useBuffering));
    this.playRaw = ((CheckBox)findViewById(R.id.playRaw));
    this.playDirac = ((CheckBox)findViewById(R.id.playDirac));
    setProcessingType(this.mProcessingType);
    localObject = new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean) {}
    };
    this.useBuffering.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener)localObject);
    localObject = new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean)
        {
          IndexActivity.this.playDirac.setEnabled(false);
          IndexActivity.this.diracDefaultManager.setProcessingType(0);
          IndexActivity.this.setProcessingType(0);
        }
        for (;;)
        {
          return;
          IndexActivity.this.playDirac.setEnabled(true);
          if (IndexActivity.this.playDirac.isChecked())
          {
            IndexActivity.this.setProcessingType(1);
            IndexActivity.this.diracDefaultManager.setProcessingType(1);
          }
          else
          {
            IndexActivity.this.setProcessingType(2);
            IndexActivity.this.diracDefaultManager.setProcessingType(2);
          }
        }
      }
    };
    this.playRaw.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener)localObject);
    localObject = new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean)
        {
          IndexActivity.this.setProcessingType(1);
          IndexActivity.this.diracDefaultManager.setProcessingType(1);
        }
        for (;;)
        {
          return;
          IndexActivity.this.setProcessingType(2);
          IndexActivity.this.diracDefaultManager.setProcessingType(2);
        }
      }
    };
    this.playDirac.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener)localObject);
    this.useVarispeed = ((ToggleButton)findViewById(R.id.useVarispeed));
    localObject = new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        IndexActivity.this.mUseVarispeed = paramAnonymousBoolean;
        paramAnonymousCompoundButton = IndexActivity.this.seekPitch;
        if (IndexActivity.this.mUseVarispeed) {}
        for (paramAnonymousBoolean = false;; paramAnonymousBoolean = true)
        {
          paramAnonymousCompoundButton.setEnabled(paramAnonymousBoolean);
          if (IndexActivity.this.mUseVarispeed)
          {
            float f = (float)(1.0D / IndexActivity.this.mTimeFactor);
            IndexActivity.this.seekPitch.setProgress((int)((f - 0.5D) / 1.5D * 100.0D));
            IndexActivity.this.diracDefaultManager.changePitch((float)IndexActivity.this.mPitchFactor);
          }
          return;
        }
      }
    };
    this.useVarispeed.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener)localObject);
    this.useVarispeed.setChecked(this.mUseVarispeed);
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    Object localObject;
    if (paramInt1 == 1)
    {
      localObject = paramIntent.getData();
      paramIntent = ((Uri)localObject).toString();
      paramIntent = paramIntent.substring(paramIntent.lastIndexOf('/') + 1, paramIntent.length());
      Log.i(TAG, "startPlaying from url " + ((Uri)localObject).toString());
      this.buttonForFileName.setText("Stop playing " + paramIntent);
      paramIntent = null;
    }
    try
    {
      localObject = getContentResolver().openAssetFileDescriptor((Uri)localObject, "r");
      paramIntent = (Intent)localObject;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      for (;;)
      {
        localFileNotFoundException.printStackTrace();
      }
    }
    enableCheckBoxes(false);
    if (paramIntent != null) {
      playMedia(paramIntent);
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.main);
    setUpUI();
    this.diracDefaultManager = new DiracDefaultManager();
  }
  
  protected void onResume()
  {
    super.onResume();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\diracndk\IndexActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
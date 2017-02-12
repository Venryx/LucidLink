package com.resmed.refresh.ui.activity;

import android.os.Bundle;

import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.utils.Log;

public class RelaxMeditationActivity
  extends BaseActivity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903079);
    getWindow().addFlags(128);
    setTitle(2131165937);
  }
  
  public void onDestroy()
  {
    Log.e("com.resmed.refresh.relax", "RelaxMeditationActivity onDestroy");
    super.onDestroy();
  }
  
  public void onPause()
  {
    Log.e("com.resmed.refresh.relax", "RelaxMeditationActivity onPause");
    super.onPause();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\RelaxMeditationActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
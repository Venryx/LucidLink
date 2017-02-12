package com.resmed.refresh.ui.activity;

import android.os.Bundle;
import com.resmed.refresh.ui.uibase.base.BaseActivity;

public class SleepHistoryCircleHelpActivity
  extends BaseActivity
{
  public void onBackPressed()
  {
    finish();
    overridePendingTransition(2130968582, 2130968583);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903066);
    setIsModalActivity(true);
    setTitle(2131165996);
    showRightButton(2130837770);
  }
  
  protected void rightBtnPressed()
  {
    finish();
    overridePendingTransition(2130968582, 2130968583);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\SleepHistoryCircleHelpActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
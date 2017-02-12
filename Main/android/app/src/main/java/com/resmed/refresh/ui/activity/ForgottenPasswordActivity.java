package com.resmed.refresh.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.resmed.refresh.ui.fragments.ForgottenPasswordFragment;
import com.resmed.refresh.ui.uibase.base.BaseActivity;

public class ForgottenPasswordActivity
  extends BaseActivity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903071);
    paramBundle = new ForgottenPasswordFragment();
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    localFragmentTransaction.add(2131099792, paramBundle);
    localFragmentTransaction.commit();
    setTypeRefreshBar(BaseActivity.TypeBar.NO_BED);
    setTitle(2131165431);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\ForgottenPasswordActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
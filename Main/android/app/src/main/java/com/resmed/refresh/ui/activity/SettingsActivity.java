package com.resmed.refresh.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_UserProfile;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.fragments.SettingPickerFragment;
import com.resmed.refresh.ui.fragments.SettingsFragment;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.utils.UserProfileDataManager;

public class SettingsActivity
  extends BaseBluetoothActivity
  implements SettingPickerFragment.UserSettingsPickerButtons, SettingsFragment.UserSettingsButtons, RST_CallbackItem<RST_Response<RST_UserProfile>>
{
  private SettingPickerFragment settingPickerFragment;
  private SettingsFragment settingsFragment;
  private UserProfileDataManager userProfileDataManager;
  
  public void closePicker()
  {
    getSupportFragmentManager().popBackStack();
    Integer localInteger = this.userProfileDataManager.getCurrentPicker();
    if (localInteger == UserProfileDataManager.PICKER_WEIGHT_UNIT) {
      this.settingsFragment.setWeightUnitUI();
    }
    if (localInteger == UserProfileDataManager.PICKER_HEIGHT_UNIT) {
      this.settingsFragment.setHeightUnitUI();
    }
    if (localInteger == UserProfileDataManager.PICKER_TEMP_UNIT) {
      this.settingsFragment.setTempratureUnitUI();
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903088);
    setTypeRefreshBar(BaseActivity.TypeBar.NO_BED);
    this.userProfileDataManager = UserProfileDataManager.getInstance();
    this.settingPickerFragment = new SettingPickerFragment();
    this.settingsFragment = new SettingsFragment();
    paramBundle = getSupportFragmentManager().beginTransaction();
    paramBundle.add(2131099806, this.settingsFragment);
    paramBundle.commitAllowingStateLoss();
    setTitle(2131166018);
  }
  
  public void onResult(RST_Response<RST_UserProfile> paramRST_Response)
  {
    boolean bool = false;
    try
    {
      if (!isAppValidated(paramRST_Response.getErrorCode())) {}
      for (;;)
      {
        return;
        if ((paramRST_Response == null) || (!paramRST_Response.isStatus())) {
          break;
        }
        i = 1;
        paramRST_Response = RefreshModelController.getInstance();
        if (i == 0) {
          break label56;
        }
        paramRST_Response.setProfileUpdatePending(bool);
      }
    }
    catch (Exception paramRST_Response)
    {
      for (;;)
      {
        paramRST_Response.printStackTrace();
        continue;
        int i = 0;
        continue;
        label56:
        bool = true;
      }
    }
  }
  
  protected void onStop()
  {
    RefreshModelController.getInstance().save();
    RefreshModelController.getInstance().updateUserProfile(this);
    super.onStop();
  }
  
  public void openPicker()
  {
    if (this.settingPickerFragment.isVisible()) {
      this.settingPickerFragment.refreshPicker();
    }
    for (;;)
    {
      return;
      FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
      localFragmentTransaction.setCustomAnimations(2130968597, 2130968588);
      localFragmentTransaction.add(2131099807, this.settingPickerFragment);
      localFragmentTransaction.addToBackStack(null);
      localFragmentTransaction.commit();
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\SettingsActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
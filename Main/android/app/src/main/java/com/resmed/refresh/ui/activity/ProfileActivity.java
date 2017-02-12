package com.resmed.refresh.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_UserProfile;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.fragments.ProfileFragment;
import com.resmed.refresh.ui.fragments.ProfilePickerFragment;
import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.ui.utils.UserProfileDataManager;

public class ProfileActivity
  extends BaseActivity
  implements ProfileFragment.UserProfileButtons, ProfilePickerFragment.UserProfilePickerButtons, RST_CallbackItem<RST_Response<RST_UserProfile>>
{
  private ProfileFragment profileFragment;
  private ProfilePickerFragment profilePickerFragment;
  private UserProfileDataManager userProfileDataManager;
  
  public void closePicker()
  {
    getSupportFragmentManager().popBackStack();
    Integer localInteger = this.userProfileDataManager.getCurrentPicker();
    if (localInteger == UserProfileDataManager.PICKER_BIRTH) {
      this.profileFragment.setAgeUI();
    }
    if (localInteger == UserProfileDataManager.PICKER_GENDER) {
      this.profileFragment.setGenderUI();
    }
    if (localInteger == UserProfileDataManager.PICKER_HEIGHT) {
      this.profileFragment.setHeightUI();
    }
    if (localInteger == UserProfileDataManager.PICKER_WEIGHT) {
      this.profileFragment.setWeightUI();
    }
    if (localInteger == UserProfileDataManager.PICKER_COLLAR_SIZE) {
      this.profileFragment.setCollarSizeUI();
    }
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903083);
    setIsModalActivity(true);
    this.userProfileDataManager = UserProfileDataManager.getInstance();
    this.profilePickerFragment = new ProfilePickerFragment();
    this.profileFragment = new ProfileFragment();
    paramBundle = getSupportFragmentManager().beginTransaction();
    paramBundle.add(2131099768, this.profileFragment);
    paramBundle.commitAllowingStateLoss();
    setTitle(2131165901);
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
    if (this.profilePickerFragment.isVisible()) {
      this.profilePickerFragment.refreshPicker();
    }
    for (;;)
    {
      return;
      FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
      localFragmentTransaction.setCustomAnimations(2130968597, 2130968588);
      localFragmentTransaction.add(2131099802, this.profilePickerFragment);
      localFragmentTransaction.addToBackStack(null);
      localFragmentTransaction.commit();
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\ProfileActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
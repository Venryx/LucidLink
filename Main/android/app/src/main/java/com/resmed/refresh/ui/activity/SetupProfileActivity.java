package com.resmed.refresh.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_User;
import com.resmed.refresh.model.RST_UserProfile;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.fragments.AboutMeFragment;
import com.resmed.refresh.ui.fragments.MyLifeStyleFragment;
import com.resmed.refresh.ui.fragments.MySleepFragment;
import com.resmed.refresh.ui.fragments.ProfilePickerFragment;
import com.resmed.refresh.ui.fragments.SignupFragment;
import com.resmed.refresh.ui.fragments.SleepAidsQuestionaireFragment;
import com.resmed.refresh.ui.fragments.SleepEnvironmentFragment;
import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.CustomDialogBuilder;
import com.resmed.refresh.ui.utils.UserProfileDataManager;
import com.resmed.refresh.utils.RefreshTools;
import java.util.BitSet;

public class SetupProfileActivity
  extends BaseActivity
  implements ProfileFragment.UserProfileButtons, ProfilePickerFragment.UserProfilePickerButtons, View.OnClickListener, RST_CallbackItem<RST_Response<RST_UserProfile>>, SleepAidsQuestionaireFragment.ISleepAidsUpdateProgress, AboutMeFragment.IAboutUpdateProgress, MyLifeStyleFragment.IMyLifeStyleUpdateProgress, MySleepFragment.IMySleepUpdateProgress, SleepEnvironmentFragment.ISleepEnvironmentUpdateProgress
{
  private final int BTN_ENABLE = 0;
  private final int FULL_FILL = 3;
  private final int HALF_FILL = 2;
  private final int NO_RESULT = -1;
  private final int ZERO_FILL = 1;
  boolean aboutMeCompleteFlag;
  private AboutMeFragment aboutmeFragment;
  private boolean cameFromSettings = false;
  private ImageView img_aboutme;
  private ImageView img_aboutme_spick;
  private ImageView img_mySleep;
  private ImageView img_mySleep_spick;
  private ImageView img_mylifestyle;
  private ImageView img_mylifestyle_spick;
  private ImageView img_sleepaids;
  private ImageView img_sleepaids_spick;
  private ImageView img_sleepenvironment;
  private ImageView img_sleepenvironment_spick;
  private int myLifeStyleBtnFlag;
  private BaseFragment myLifeStyleFragment;
  private int mySleepBtnFlag;
  private BaseFragment mySleepfragment;
  private ProfilePickerFragment profilePickerFragment;
  private int sleepAidsBtnFlag;
  private BaseFragment sleepAidsQuestionaireFragment;
  private int sleepEnvironmentBtnFlag;
  private BaseFragment sleepEnvironmentFragment;
  private TextView txtview_aboutme;
  private TextView txtview_mySleep;
  private TextView txtview_mylifestyle;
  private TextView txtview_sleepaids;
  private TextView txtview_sleepenvironment;
  private UserProfileDataManager userProfileDataManager;
  
  private int calculateAboutMeValue()
  {
    int i = 0;
    RST_User localRST_User = RefreshModelController.getInstance().getUser();
    if (localRST_User.getProfile().getNewGender().trim().length() != 0) {
      i = 0 + 'ú';
    }
    int j = i;
    if (this.userProfileDataManager.isValidWeight()) {
      j = i + 250;
    }
    i = j;
    if (this.userProfileDataManager.isValidHeigth()) {
      i = j + 250;
    }
    j = i;
    if (this.userProfileDataManager.isValidBirth()) {
      j = i + 250;
    }
    i = j;
    if (localRST_User.getProfile().getNeckSize() != -1) {
      i = j + 250;
    }
    j = i;
    if (localRST_User.getProfile().getHighBloodPressure() != -1) {
      j = i + 250;
    }
    return j;
  }
  
  private int calculateMyLifeStyleValue()
  {
    RST_User localRST_User = RefreshModelController.getInstance().getUser();
    int i = 100 + 25;
    int j = i;
    if (localRST_User.getProfile().getMattressType() != -1) {
      j = i + 300;
    }
    i = j;
    if (localRST_User.getProfile().getMattressSize() != -1) {
      i = j + 300;
    }
    j = i;
    if (localRST_User.getProfile().getMattressBrand() != -1) {
      j = i + 300;
    }
    int k = j;
    if (localRST_User.getProfile().getMattressAge() != -1) {
      k = j + 25;
    }
    i = k;
    if (localRST_User.getProfile().getPillowsType() > 0) {
      i = k + 450;
    }
    j = i;
    if (localRST_User.getProfile().getPillowsAge() != -1) {
      j = i + 400;
    }
    i = j;
    if (localRST_User.getProfile().getSmoker() != -1) {
      i = j + 300;
    }
    j = i;
    if (localRST_User.getProfile().isMattressPadTopperOnBed() != -1) {
      j = i + 300;
    }
    if (j == 0) {
      this.myLifeStyleBtnFlag = 1;
    }
    for (;;)
    {
      return j;
      if (j < 2500) {
        this.myLifeStyleBtnFlag = 2;
      } else if (j >= 2500) {
        this.myLifeStyleBtnFlag = 3;
      }
    }
  }
  
  private int calculateMySleepValue()
  {
    RST_User localRST_User = RefreshModelController.getInstance().getUser();
    int j = 50 + 25;
    int i = j;
    if (localRST_User.getProfile().getBedTime() != -1) {
      i = j + 350;
    }
    j = i;
    if (localRST_User.getProfile().getSleepProblems() > 0) {
      j = i + 675;
    }
    i = j;
    if (localRST_User.getProfile().getSleepDisorder() > 0) {
      i = j + 350;
    }
    j = i;
    if (localRST_User.getProfile().getChildrenCount() != -1) {
      j = i + 350;
    }
    i = j;
    if (localRST_User.getProfile().isSleepWithPartner() != -1) {
      i = j + 350;
    }
    j = i;
    if (localRST_User.getProfile().isSufferFromAllergies() != -1) {
      j = i + 350;
    }
    if (j == 0) {
      this.mySleepBtnFlag = 1;
    }
    for (;;)
    {
      return j;
      if (j < 2500) {
        this.mySleepBtnFlag = 2;
      } else if (j >= 2500) {
        this.mySleepBtnFlag = 3;
      }
    }
  }
  
  private int calculateSleepAidsValue()
  {
    int j = 0;
    RST_User localRST_User = RefreshModelController.getInstance().getUser();
    int i = j;
    BitSet localBitSet;
    if (localRST_User.getProfile().getUsingSleepAids() > 0)
    {
      localBitSet = RefreshTools.bitmaskFromInt(localRST_User.getProfile().getUsingSleepAids());
      if (localBitSet.get(3)) {
        i = 0 + 'Ϩ';
      }
    }
    else
    {
      if (i != 0) {
        break label547;
      }
      this.sleepAidsBtnFlag = 1;
    }
    for (;;)
    {
      return i;
      if ((localBitSet.get(0)) && (localBitSet.get(1)) && (localBitSet.get(2)))
      {
        if ((localRST_User.getProfile().getPrescriptionPillsType() > 0) && (localRST_User.getProfile().getPrescriptionPillsFrequency() != -1) && (localRST_User.getProfile().getOTCMedicationType() > 0) && (localRST_User.getProfile().getOTCMedicationFrequency() != -1) && (localRST_User.getProfile().getHerbalRemediesType() > 0) && (localRST_User.getProfile().getHerbalRemediesFrequency() != -1))
        {
          i = 0 + 'Ϩ';
          break;
        }
        i = 0 + 'Ǵ';
        break;
      }
      if ((localBitSet.get(0)) && (localBitSet.get(1)))
      {
        if ((localRST_User.getProfile().getPrescriptionPillsType() > 0) && (localRST_User.getProfile().getPrescriptionPillsFrequency() != -1) && (localRST_User.getProfile().getOTCMedicationType() > 0) && (localRST_User.getProfile().getOTCMedicationFrequency() != -1))
        {
          i = 0 + 'Ϩ';
          break;
        }
        i = 0 + 'Ǵ';
        break;
      }
      if ((localBitSet.get(0)) && (localBitSet.get(2)))
      {
        if ((localRST_User.getProfile().getPrescriptionPillsType() > 0) && (localRST_User.getProfile().getPrescriptionPillsFrequency() != -1) && (localRST_User.getProfile().getHerbalRemediesType() > 0) && (localRST_User.getProfile().getHerbalRemediesFrequency() != -1))
        {
          i = 0 + 'Ϩ';
          break;
        }
        i = 0 + 'Ǵ';
        break;
      }
      if ((localBitSet.get(1)) && (localBitSet.get(2)))
      {
        if ((localRST_User.getProfile().getOTCMedicationType() > 0) && (localRST_User.getProfile().getOTCMedicationFrequency() != -1) && (localRST_User.getProfile().getHerbalRemediesType() > 0) && (localRST_User.getProfile().getHerbalRemediesFrequency() != -1))
        {
          i = 0 + 'Ϩ';
          break;
        }
        i = 0 + 'Ǵ';
        break;
      }
      if (localBitSet.get(0))
      {
        if ((localRST_User.getProfile().getPrescriptionPillsType() > 0) && (localRST_User.getProfile().getPrescriptionPillsFrequency() != -1))
        {
          i = 0 + 'Ϩ';
          break;
        }
        i = 0 + 'Ǵ';
        break;
      }
      if (localBitSet.get(1))
      {
        if ((localRST_User.getProfile().getOTCMedicationType() > 0) && (localRST_User.getProfile().getOTCMedicationFrequency() != -1))
        {
          i = 0 + 'Ϩ';
          break;
        }
        i = 0 + 'Ǵ';
        break;
      }
      i = j;
      if (!localBitSet.get(2)) {
        break;
      }
      if ((localRST_User.getProfile().getHerbalRemediesType() > 0) && (localRST_User.getProfile().getHerbalRemediesFrequency() != -1))
      {
        i = 0 + 'Ϩ';
        break;
      }
      i = 0 + 'Ǵ';
      break;
      label547:
      if (i < 1000) {
        this.sleepAidsBtnFlag = 2;
      } else if (i >= 1000) {
        this.sleepAidsBtnFlag = 3;
      }
    }
  }
  
  private int calculateSleepEnvironmentValue()
  {
    RST_User localRST_User = RefreshModelController.getInstance().getUser();
    int j = 50 + 25;
    int i = j;
    if (localRST_User.getProfile().getControlBedroomTemperature() > 0) {
      i = j + 400;
    }
    int k = i;
    if (localRST_User.getProfile().getGetWokenUpInNightByAny() > 0) {
      k = i + 425;
    }
    j = k;
    if (localRST_User.getProfile().getReduceAmountOfLightInBedroom() > 0) {
      j = k + 200;
    }
    i = j;
    if (localRST_User.getProfile().getSpecialLightUsedInSleep() > 0) {
      i = j + 200;
    }
    j = i;
    if (localRST_User.getProfile().isHumidifierUsed() != -1) {
      j = i + 400;
    }
    i = j;
    if (localRST_User.getProfile().isAirPurifierUsed() != -1) {
      i = j + 400;
    }
    j = i;
    if (localRST_User.getProfile().isSoundUsedInSleep() != -1) {
      j = i + 400;
    }
    if (j == 0) {
      this.sleepEnvironmentBtnFlag = 1;
    }
    for (;;)
    {
      return j;
      if (j < 2500) {
        this.sleepEnvironmentBtnFlag = 2;
      } else if (j >= 2500) {
        this.sleepEnvironmentBtnFlag = 3;
      }
    }
  }
  
  private void initUI()
  {
    this.img_aboutme = ((ImageView)findViewById(2131099810));
    this.img_mySleep = ((ImageView)findViewById(2131099814));
    this.img_sleepaids = ((ImageView)findViewById(2131099818));
    this.img_mylifestyle = ((ImageView)findViewById(2131099822));
    this.img_sleepenvironment = ((ImageView)findViewById(2131099826));
    this.img_aboutme.setOnClickListener(this);
    this.img_mySleep.setOnClickListener(this);
    this.img_sleepaids.setOnClickListener(this);
    this.img_mylifestyle.setOnClickListener(this);
    this.img_sleepenvironment.setOnClickListener(this);
    this.img_aboutme_spick = ((ImageView)findViewById(2131099812));
    this.img_mySleep_spick = ((ImageView)findViewById(2131099816));
    this.img_sleepaids_spick = ((ImageView)findViewById(2131099820));
    this.img_mylifestyle_spick = ((ImageView)findViewById(2131099824));
    this.img_sleepenvironment_spick = ((ImageView)findViewById(2131099828));
    this.txtview_aboutme = ((TextView)findViewById(2131099811));
    this.txtview_mySleep = ((TextView)findViewById(2131099815));
    this.txtview_sleepaids = ((TextView)findViewById(2131099819));
    this.txtview_mylifestyle = ((TextView)findViewById(2131099823));
    this.txtview_sleepenvironment = ((TextView)findViewById(2131099827));
  }
  
  private void makeSpikeInvisible()
  {
    this.img_aboutme_spick.setVisibility(4);
    this.img_mySleep_spick.setVisibility(4);
    this.img_sleepaids_spick.setVisibility(4);
    this.img_mylifestyle_spick.setVisibility(4);
    this.img_sleepenvironment_spick.setVisibility(4);
  }
  
  private void overAllPercentageCalculation()
  {
    int i = 0;
    int i3 = 0;
    int j = 0;
    int n = 0;
    int k = 0;
    int i1 = 0;
    int i2 = 0;
    int m;
    if (!this.aboutMeCompleteFlag) {
      m = calculateAboutMeValue();
    }
    for (;;)
    {
      setProgressBarpercentage((m + i3 + n + i1 + i2) / 100);
      return;
      int i4 = calculateAboutMeValue();
      if (this.mySleepBtnFlag != -1) {
        i = calculateMySleepValue();
      }
      if (this.sleepAidsBtnFlag != -1) {
        j = calculateSleepAidsValue();
      }
      if (this.myLifeStyleBtnFlag != -1) {
        k = calculateMyLifeStyleValue();
      }
      i3 = i;
      i1 = k;
      n = j;
      m = i4;
      if (this.sleepEnvironmentBtnFlag != -1)
      {
        i2 = calculateSleepEnvironmentValue();
        i3 = i;
        i1 = k;
        n = j;
        m = i4;
      }
    }
  }
  
  private void saveData()
  {
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    localRefreshModelController.save();
    localRefreshModelController.updateUserProfile(this);
  }
  
  private void setButtonVisibility()
  {
    if (!this.aboutMeCompleteFlag)
    {
      this.img_aboutme.setBackgroundResource(2130837593);
      this.img_mySleep.setBackgroundResource(2130837815);
      this.img_sleepaids.setBackgroundResource(2130837919);
      this.img_mylifestyle.setBackgroundResource(2130837810);
      this.img_sleepenvironment.setBackgroundResource(2130837820);
    }
    for (;;)
    {
      return;
      this.img_aboutme.setBackgroundResource(2130837594);
      if (this.mySleepBtnFlag == 1)
      {
        this.img_mySleep.setBackgroundResource(2130837819);
        label86:
        if (this.sleepAidsBtnFlag != 1) {
          break label198;
        }
        this.img_sleepaids.setBackgroundResource(2130837924);
        label104:
        if (this.myLifeStyleBtnFlag != 1) {
          break label253;
        }
        this.img_mylifestyle.setBackgroundResource(2130837814);
      }
      for (;;)
      {
        if (this.sleepEnvironmentBtnFlag != 1) {
          break label308;
        }
        this.img_sleepenvironment.setBackgroundResource(2130837824);
        break;
        if (this.mySleepBtnFlag == 2)
        {
          this.img_mySleep.setBackgroundResource(2130837817);
          break label86;
        }
        if (this.mySleepBtnFlag == 3)
        {
          this.img_mySleep.setBackgroundResource(2130837818);
          break label86;
        }
        this.img_mySleep.setBackgroundResource(2130837816);
        break label86;
        label198:
        if (this.sleepAidsBtnFlag == 2)
        {
          this.img_sleepaids.setBackgroundResource(2130837921);
          break label104;
        }
        if (this.sleepAidsBtnFlag == 3)
        {
          this.img_sleepaids.setBackgroundResource(2130837922);
          break label104;
        }
        this.img_sleepaids.setBackgroundResource(2130837920);
        break label104;
        label253:
        if (this.myLifeStyleBtnFlag == 2) {
          this.img_mylifestyle.setBackgroundResource(2130837812);
        } else if (this.myLifeStyleBtnFlag == 3) {
          this.img_mylifestyle.setBackgroundResource(2130837813);
        } else {
          this.img_mylifestyle.setBackgroundResource(2130837811);
        }
      }
      label308:
      if (this.sleepEnvironmentBtnFlag == 2) {
        this.img_sleepenvironment.setBackgroundResource(2130837822);
      } else if (this.sleepEnvironmentBtnFlag == 3) {
        this.img_sleepenvironment.setBackgroundResource(2130837823);
      } else {
        this.img_sleepenvironment.setBackgroundResource(2130837821);
      }
    }
  }
  
  private void setTextSelectionStyle(int paramInt)
  {
    switch (paramInt)
    {
    }
    for (;;)
    {
      return;
      this.txtview_aboutme.setTypeface(Typeface.DEFAULT_BOLD);
      this.txtview_aboutme.setTextSize(0, getResources().getDimension(2131034209));
      this.txtview_mySleep.setTypeface(Typeface.DEFAULT);
      this.txtview_mySleep.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_sleepaids.setTypeface(Typeface.DEFAULT);
      this.txtview_sleepaids.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_mylifestyle.setTypeface(Typeface.DEFAULT);
      this.txtview_mylifestyle.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_sleepenvironment.setTypeface(Typeface.DEFAULT);
      this.txtview_sleepenvironment.setTextSize(0, getResources().getDimension(2131034208));
      continue;
      this.txtview_aboutme.setTypeface(Typeface.DEFAULT);
      this.txtview_aboutme.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_mySleep.setTypeface(Typeface.DEFAULT_BOLD);
      this.txtview_mySleep.setTextSize(0, getResources().getDimension(2131034209));
      this.txtview_sleepaids.setTypeface(Typeface.DEFAULT);
      this.txtview_sleepaids.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_mylifestyle.setTypeface(Typeface.DEFAULT);
      this.txtview_mylifestyle.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_sleepenvironment.setTypeface(Typeface.DEFAULT);
      this.txtview_sleepenvironment.setTextSize(0, getResources().getDimension(2131034208));
      continue;
      this.txtview_aboutme.setTypeface(Typeface.DEFAULT);
      this.txtview_aboutme.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_mySleep.setTypeface(Typeface.DEFAULT);
      this.txtview_mySleep.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_sleepaids.setTypeface(Typeface.DEFAULT_BOLD);
      this.txtview_sleepaids.setTextSize(0, getResources().getDimension(2131034209));
      this.txtview_mylifestyle.setTypeface(Typeface.DEFAULT);
      this.txtview_mylifestyle.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_sleepenvironment.setTypeface(Typeface.DEFAULT);
      this.txtview_sleepenvironment.setTextSize(0, getResources().getDimension(2131034208));
      continue;
      this.txtview_aboutme.setTypeface(Typeface.DEFAULT);
      this.txtview_aboutme.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_mySleep.setTypeface(Typeface.DEFAULT);
      this.txtview_mySleep.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_sleepaids.setTypeface(Typeface.DEFAULT);
      this.txtview_sleepaids.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_mylifestyle.setTypeface(Typeface.DEFAULT_BOLD);
      this.txtview_mylifestyle.setTextSize(0, getResources().getDimension(2131034209));
      this.txtview_sleepenvironment.setTypeface(Typeface.DEFAULT);
      this.txtview_sleepenvironment.setTextSize(0, getResources().getDimension(2131034208));
      continue;
      this.txtview_aboutme.setTypeface(Typeface.DEFAULT);
      this.txtview_aboutme.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_mySleep.setTypeface(Typeface.DEFAULT);
      this.txtview_mySleep.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_sleepaids.setTypeface(Typeface.DEFAULT);
      this.txtview_sleepaids.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_mylifestyle.setTypeface(Typeface.DEFAULT);
      this.txtview_mylifestyle.setTextSize(0, getResources().getDimension(2131034208));
      this.txtview_sleepenvironment.setTypeface(Typeface.DEFAULT_BOLD);
      this.txtview_sleepenvironment.setTextSize(0, getResources().getDimension(2131034209));
    }
  }
  
  private void showAboutMeIncompleteDialog()
  {
    showDialog(new CustomDialogBuilder(this).title(2131165470).description(2131165471).setPositiveButton(2131165472, new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView) {}
    }));
  }
  
  public void closePicker()
  {
    getSupportFragmentManager().popBackStack();
    Integer localInteger = this.userProfileDataManager.getCurrentPicker();
    if (localInteger == UserProfileDataManager.PICKER_BIRTH) {
      this.aboutmeFragment.setAgeUI();
    }
    if (localInteger == UserProfileDataManager.PICKER_HEIGHT) {
      this.aboutmeFragment.setHeightUI();
    }
    if (localInteger == UserProfileDataManager.PICKER_WEIGHT) {
      this.aboutmeFragment.setWeightUI();
    }
  }
  
  public void hideKeyboard()
  {
    try
    {
      InputMethodManager localInputMethodManager = (InputMethodManager)getSystemService("input_method");
      if (getCurrentFocus().getWindowToken() != null) {
        localInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 2);
      }
      return;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        Log.e("KeyBoardUtil", localException.toString(), localException);
      }
    }
  }
  
  public void onBackPressed()
  {
    if (this.cameFromSettings) {
      super.onBackPressed();
    }
    for (;;)
    {
      return;
      saveData();
      startActivity(new Intent(this, GuideQuestionActivty.class));
    }
  }
  
  public void onClick(View paramView)
  {
    screenTransition(paramView.getId(), 2);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903089);
    this.profilePickerFragment = new ProfilePickerFragment();
    this.userProfileDataManager = UserProfileDataManager.getInstance();
    initUI();
    this.aboutmeFragment = new AboutMeFragment();
    this.mySleepfragment = new MySleepFragment();
    this.sleepAidsQuestionaireFragment = new SleepAidsQuestionaireFragment();
    this.myLifeStyleFragment = new MyLifeStyleFragment();
    this.sleepEnvironmentFragment = new SleepEnvironmentFragment();
    this.aboutmeFragment.setAboutUpdateProgressRef(this);
    ((MySleepFragment)this.mySleepfragment).setMySleepUpdateProgressRef(this);
    ((SleepAidsQuestionaireFragment)this.sleepAidsQuestionaireFragment).setSleepAidsUpdateProgressRef(this);
    ((MyLifeStyleFragment)this.myLifeStyleFragment).setMyLifeStyleUpdateProgressRef(this);
    ((SleepEnvironmentFragment)this.sleepEnvironmentFragment).setSleepEnvironmentUpdateProgressRef(this);
    if (SignupFragment.isStartedAppFromSignUp)
    {
      this.mySleepBtnFlag = -1;
      this.sleepAidsBtnFlag = -1;
      this.myLifeStyleBtnFlag = -1;
      this.sleepEnvironmentBtnFlag = -1;
      SignupFragment.isStartedAppFromSignUp = false;
    }
    for (;;)
    {
      FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
      paramBundle = getSupportFragmentManager().findFragmentByTag("aboutmeFragment");
      if (paramBundle != null) {
        localFragmentTransaction.remove(paramBundle);
      }
      localFragmentTransaction.add(2131099829, this.aboutmeFragment, "aboutmeFragment");
      localFragmentTransaction.commit();
      this.cameFromSettings = getIntent().getBooleanExtra("com.resmed.refresh.ui.uibase.app.came_from_settings", false);
      setIsRemoveProfileFragment(true);
      setTypeRefreshBar(BaseActivity.TypeBar.PROFILE_QUESTIONAIRE);
      setTitle(2131165469);
      return;
      this.mySleepBtnFlag = 0;
      this.sleepAidsBtnFlag = 0;
      this.myLifeStyleBtnFlag = 0;
      this.sleepEnvironmentBtnFlag = 0;
    }
  }
  
  public void onResult(RST_Response<RST_UserProfile> paramRST_Response)
  {
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    if (paramRST_Response.isStatus()) {}
    for (boolean bool = false;; bool = true)
    {
      localRefreshModelController.setProfileUpdatePending(bool);
      return;
    }
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
      localFragmentTransaction.add(2131099830, this.profilePickerFragment);
      localFragmentTransaction.addToBackStack(null);
      localFragmentTransaction.commit();
    }
  }
  
  public void screenTransition(int paramInt1, int paramInt2)
  {
    hideKeyboard();
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    switch (paramInt1)
    {
    }
    for (;;)
    {
      return;
      setTextSelectionStyle(paramInt1);
      if (paramInt2 == 1) {
        localFragmentTransaction.setCustomAnimations(2130968591, 2130968596, 2130968595, 2130968592);
      }
      for (;;)
      {
        RefreshModelController.getInstance().updateFlurryEvents("Menu_Profile_AboutMe");
        localFragmentTransaction.replace(2131099829, this.aboutmeFragment);
        localFragmentTransaction.addToBackStack(null);
        localFragmentTransaction.commit();
        break;
        localFragmentTransaction.setCustomAnimations(2130968595, 2130968592, 2130968591, 2130968596);
      }
      if (this.aboutMeCompleteFlag)
      {
        this.mySleepBtnFlag = 0;
        setTextSelectionStyle(paramInt1);
        if (paramInt2 == 1) {
          localFragmentTransaction.setCustomAnimations(2130968591, 2130968596, 2130968595, 2130968592);
        }
        for (;;)
        {
          RefreshModelController.getInstance().updateFlurryEvents("Menu_Profile_MySleep");
          localFragmentTransaction.replace(2131099829, this.mySleepfragment);
          localFragmentTransaction.addToBackStack(null);
          localFragmentTransaction.commit();
          break;
          localFragmentTransaction.setCustomAnimations(2130968595, 2130968592, 2130968591, 2130968596);
        }
      }
      showAboutMeIncompleteDialog();
      continue;
      if (this.aboutMeCompleteFlag)
      {
        this.sleepAidsBtnFlag = 0;
        setTextSelectionStyle(paramInt1);
        if (paramInt2 == 1) {
          localFragmentTransaction.setCustomAnimations(2130968591, 2130968596, 2130968595, 2130968592);
        }
        for (;;)
        {
          RefreshModelController.getInstance().updateFlurryEvents("Menu_Profile_SleepAids");
          localFragmentTransaction.replace(2131099829, this.sleepAidsQuestionaireFragment);
          localFragmentTransaction.addToBackStack(null);
          localFragmentTransaction.commit();
          break;
          localFragmentTransaction.setCustomAnimations(2130968595, 2130968592, 2130968591, 2130968596);
        }
      }
      showAboutMeIncompleteDialog();
      continue;
      if (this.aboutMeCompleteFlag)
      {
        this.myLifeStyleBtnFlag = 0;
        setTextSelectionStyle(paramInt1);
        if (paramInt2 == 1) {
          localFragmentTransaction.setCustomAnimations(2130968591, 2130968596, 2130968595, 2130968592);
        }
        for (;;)
        {
          RefreshModelController.getInstance().updateFlurryEvents("Menu_Profile_MyLifestyle");
          localFragmentTransaction.replace(2131099829, this.myLifeStyleFragment);
          localFragmentTransaction.addToBackStack(null);
          localFragmentTransaction.commit();
          break;
          localFragmentTransaction.setCustomAnimations(2130968595, 2130968592, 2130968591, 2130968596);
        }
      }
      showAboutMeIncompleteDialog();
      continue;
      if (this.aboutMeCompleteFlag)
      {
        this.sleepEnvironmentBtnFlag = 0;
        setTextSelectionStyle(paramInt1);
        if (paramInt2 == 1) {
          localFragmentTransaction.setCustomAnimations(2130968591, 2130968596, 2130968595, 2130968592);
        }
        for (;;)
        {
          RefreshModelController.getInstance().updateFlurryEvents("Menu_Profile_MySleepEnv");
          localFragmentTransaction.replace(2131099829, this.sleepEnvironmentFragment);
          localFragmentTransaction.addToBackStack(null);
          localFragmentTransaction.commit();
          break;
          localFragmentTransaction.setCustomAnimations(2130968595, 2130968592, 2130968591, 2130968596);
        }
      }
      showAboutMeIncompleteDialog();
    }
  }
  
  public void screenTransitionFromAboutFrag(int paramInt1, int paramInt2)
  {
    screenTransition(paramInt1, paramInt2);
  }
  
  public void screenTransitionFromMyLifeStyleFrag(int paramInt1, int paramInt2)
  {
    screenTransition(paramInt1, paramInt2);
  }
  
  public void screenTransitionFromMySleepFrag(int paramInt1, int paramInt2)
  {
    screenTransition(paramInt1, paramInt2);
  }
  
  public void screenTransitionFromSleepAidsFrag(int paramInt1, int paramInt2)
  {
    screenTransition(paramInt1, paramInt2);
  }
  
  public void screenTransitionFromSleepEnvFrag(int paramInt1, int paramInt2)
  {
    if (2131099822 == paramInt1) {
      screenTransition(paramInt1, paramInt2);
    }
    for (;;)
    {
      return;
      onBackPressed();
    }
  }
  
  public void updateAboutProgress()
  {
    RST_UserProfile localRST_UserProfile = RefreshModelController.getInstance().getUser().getProfile();
    UserProfileDataManager localUserProfileDataManager = UserProfileDataManager.getInstance();
    if ((localUserProfileDataManager.isValidWeight()) && (localUserProfileDataManager.isValidHeigth()) && (localUserProfileDataManager.isValidBirth()) && (localRST_UserProfile.getNewGender().trim().length() != 0) && (localRST_UserProfile.getNeckSize() != -1) && (localRST_UserProfile.getHighBloodPressure() != -1)) {
      this.aboutMeCompleteFlag = true;
    }
    makeSpikeInvisible();
    this.img_aboutme_spick.setVisibility(0);
    overAllPercentageCalculation();
    setButtonVisibility();
  }
  
  public void updateMyLifeStyleProgress()
  {
    calculateMyLifeStyleValue();
    makeSpikeInvisible();
    this.img_mylifestyle_spick.setVisibility(0);
    overAllPercentageCalculation();
    setButtonVisibility();
  }
  
  public void updateMySleepProgress()
  {
    calculateMySleepValue();
    makeSpikeInvisible();
    this.img_mySleep_spick.setVisibility(0);
    overAllPercentageCalculation();
    setButtonVisibility();
  }
  
  public void updateSleepAidsProgress()
  {
    calculateSleepAidsValue();
    makeSpikeInvisible();
    this.img_sleepaids_spick.setVisibility(0);
    overAllPercentageCalculation();
    setButtonVisibility();
  }
  
  public void updateSleepEnvironmentProgress()
  {
    calculateSleepEnvironmentValue();
    makeSpikeInvisible();
    this.img_sleepenvironment_spick.setVisibility(0);
    overAllPercentageCalculation();
    setButtonVisibility();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\activity\SetupProfileActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
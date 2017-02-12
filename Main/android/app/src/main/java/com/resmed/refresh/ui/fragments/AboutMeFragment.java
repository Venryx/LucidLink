package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_User;
import com.resmed.refresh.model.RST_UserProfile;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.customview.CustomSemiSliderSeekBar;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.CustomDialogBuilder;
import com.resmed.refresh.ui.utils.UserProfileDataManager;
import com.resmed.refresh.utils.RefreshTools;
import java.util.ArrayList;
import java.util.List;

public class AboutMeFragment
  extends BaseFragment
  implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener, RST_CallbackItem<RST_Response<RST_UserProfile>>
{
  private CustomSemiSliderSeekBar csssbSetupAboutBlood;
  private CustomSemiSliderSeekBar csssbSetupAboutGender;
  private CustomSemiSliderSeekBar csssbSetupAboutNeck;
  private LinearLayout llMyInfo;
  private TextView llMyInfoMail;
  private TextView llMyInfoName;
  private LinearLayout llSetupAboutNext;
  private ProfileFragment.UserProfileButtons profileButtonsListener;
  private RelativeLayout rlSetupAboutBirth;
  private RelativeLayout rlSetupAboutHeight;
  private RelativeLayout rlSetupAboutWeight;
  private TextView tvSetupAboutBirthValue;
  private TextView tvSetupAboutHeightValue;
  private TextView tvSetupAboutNeck;
  private TextView tvSetupAboutWeightValue;
  public IAboutUpdateProgress updateAboutProg;
  private UserProfileDataManager userProfileDataManager;
  
  private void checkData()
  {
    RST_UserProfile localRST_UserProfile = RefreshModelController.getInstance().getUser().getProfile();
    if ((this.userProfileDataManager.isValidWeight()) && (this.userProfileDataManager.isValidHeigth()) && (this.userProfileDataManager.isValidBirth()) && (localRST_UserProfile.getNewGender().trim().length() != 0) && (localRST_UserProfile.getNeckSize() != -1) && (localRST_UserProfile.getHighBloodPressure() != -1))
    {
      saveProfile();
      this.updateAboutProg.screenTransitionFromAboutFrag(2131099814, 2);
    }
    for (;;)
    {
      return;
      getBaseActivity().showDialog(new CustomDialogBuilder(getBaseActivity()).title(2131165470).description(2131165471).setPositiveButton(2131165472, new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView) {}
      }));
    }
  }
  
  private void initData()
  {
    this.userProfileDataManager = UserProfileDataManager.getInstance();
    setAgeUI();
    setHeightUI();
    setWeightUI();
    RST_User localRST_User = RefreshModelController.getInstance().getUser();
    if (localRST_User != null)
    {
      this.llMyInfoName.setText(localRST_User.getFirstName() + " " + localRST_User.getFamilyName());
      this.llMyInfoMail.setText(localRST_User.getEmail());
    }
    Object localObject = new ArrayList();
    ((List)localObject).add(getString(2131165906));
    ((List)localObject).add(getString(2131165907));
    localObject = localRST_User.getProfile().getNewGender();
    int j = 0;
    int i = j;
    if (localObject != null)
    {
      if (!((String)localObject).equalsIgnoreCase("M")) {
        break label189;
      }
      i = 0;
    }
    for (;;)
    {
      setCustomSeekBarSemiSliderAdapterForGender(i, this.csssbSetupAboutGender);
      setNeckTextForGender(i);
      setCustomSeekBarSemiSliderAdapter(localRST_User.getProfile().getNeckSize(), this.csssbSetupAboutNeck);
      setCustomSeekBarSemiSliderAdapter(localRST_User.getProfile().getHighBloodPressure(), this.csssbSetupAboutBlood);
      return;
      label189:
      if (((String)localObject).equalsIgnoreCase("F"))
      {
        i = 1;
      }
      else
      {
        i = j;
        if (((String)localObject).trim().length() == 0) {
          i = -1;
        }
      }
    }
  }
  
  private void mapGUI(View paramView)
  {
    this.llMyInfo = ((LinearLayout)paramView.findViewById(2131099893));
    this.llMyInfoName = ((TextView)paramView.findViewById(2131099894));
    this.llMyInfoMail = ((TextView)paramView.findViewById(2131099895));
    this.tvSetupAboutNeck = ((TextView)paramView.findViewById(2131099904));
    this.rlSetupAboutWeight = ((RelativeLayout)paramView.findViewById(2131099897));
    this.tvSetupAboutWeightValue = ((TextView)paramView.findViewById(2131099898));
    this.rlSetupAboutHeight = ((RelativeLayout)paramView.findViewById(2131099900));
    this.tvSetupAboutHeightValue = ((TextView)paramView.findViewById(2131099901));
    this.rlSetupAboutBirth = ((RelativeLayout)paramView.findViewById(2131099902));
    this.tvSetupAboutBirthValue = ((TextView)paramView.findViewById(2131099903));
    this.llSetupAboutNext = ((LinearLayout)paramView.findViewById(2131099907));
    this.csssbSetupAboutBlood = ((CustomSemiSliderSeekBar)paramView.findViewById(2131099906));
    this.csssbSetupAboutBlood.setProgressLineColor(-7829368);
    this.csssbSetupAboutBlood.setBackgroundLineColor(-7829368);
    this.csssbSetupAboutGender = ((CustomSemiSliderSeekBar)paramView.findViewById(2131099896));
    this.csssbSetupAboutGender.setProgressLineColor(-7829368);
    this.csssbSetupAboutGender.setBackgroundLineColor(-7829368);
    this.csssbSetupAboutNeck = ((CustomSemiSliderSeekBar)paramView.findViewById(2131099905));
    this.csssbSetupAboutNeck.setProgressLineColor(-7829368);
    this.csssbSetupAboutNeck.setBackgroundLineColor(-7829368);
    if (getActivity().getIntent().getBooleanExtra("com.resmed.refresh.ui.uibase.app.came_from_home", false)) {
      this.llMyInfo.setVisibility(0);
    }
  }
  
  private void saveData()
  {
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    localRefreshModelController.save();
    localRefreshModelController.updateUserProfile(this);
  }
  
  private void saveProfile()
  {
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    RST_User localRST_User = localRefreshModelController.getUser();
    if (this.csssbSetupAboutGender.getPositionSelected() == 0) {
      localRST_User.getProfile().setNewGender("M");
    }
    for (;;)
    {
      localRefreshModelController.save();
      return;
      if (this.csssbSetupAboutGender.getPositionSelected() == 1) {
        localRST_User.getProfile().setNewGender("F");
      } else {
        localRST_User.getProfile().setNewGender("");
      }
    }
  }
  
  private void setCustomSeekBarSemiSliderAdapter(int paramInt, View paramView)
  {
    ArrayList localArrayList = new ArrayList();
    if (paramInt == -1)
    {
      localArrayList.add(getResources().getString(2131165299));
      localArrayList.add("");
      localArrayList.add(getResources().getString(2131165298));
      paramInt = 1;
      switch (paramView.getId())
      {
      }
    }
    for (;;)
    {
      return;
      localArrayList.add(getResources().getString(2131165299));
      localArrayList.add(getResources().getString(2131165298));
      break;
      this.csssbSetupAboutNeck.setAdapter(localArrayList);
      this.csssbSetupAboutNeck.setSelection(paramInt);
      continue;
      this.csssbSetupAboutBlood.setAdapter(localArrayList);
      this.csssbSetupAboutBlood.setSelection(paramInt);
    }
  }
  
  private void setCustomSeekBarSemiSliderAdapterForGender(int paramInt, View paramView)
  {
    ArrayList localArrayList = new ArrayList();
    if (paramInt == -1)
    {
      localArrayList.add(getResources().getString(2131165906));
      localArrayList.add("");
      localArrayList.add(getResources().getString(2131165907));
      paramInt = 1;
      switch (paramView.getId())
      {
      }
    }
    for (;;)
    {
      return;
      localArrayList.add(getResources().getString(2131165906));
      localArrayList.add(getResources().getString(2131165907));
      break;
      this.csssbSetupAboutGender.setAdapter(localArrayList);
      this.csssbSetupAboutGender.setSelection(paramInt);
    }
  }
  
  private void setNeckTextForGender(int paramInt)
  {
    if (paramInt == 0) {
      this.tvSetupAboutNeck.setText(getString(2131165478) + getString(2131165479));
    }
    for (;;)
    {
      return;
      if (paramInt == 1) {
        this.tvSetupAboutNeck.setText(getString(2131165478) + getString(2131165480));
      }
    }
  }
  
  private void setupListeners()
  {
    this.rlSetupAboutWeight.setOnClickListener(this);
    this.rlSetupAboutHeight.setOnClickListener(this);
    this.rlSetupAboutBirth.setOnClickListener(this);
    this.llSetupAboutNext.setOnClickListener(this);
    this.csssbSetupAboutGender.setOnSeekBarChangeListener(this);
    this.csssbSetupAboutNeck.setOnSeekBarChangeListener(this);
    this.csssbSetupAboutBlood.setOnSeekBarChangeListener(this);
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.profileButtonsListener = ((ProfileFragment.UserProfileButtons)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new ClassCastException(paramActivity.toString() + " ...you must implement ProfileButtons from your Activity ;-) !");
    }
  }
  
  public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
  {
    RefreshModelController.getInstance().getUser();
    paramCompoundButton.getId();
    RefreshModelController.getInstance().save();
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    }
    for (;;)
    {
      RefreshTools.hideKeyBoard(getActivity());
      paramView.requestFocus();
      return;
      this.userProfileDataManager.setCurrentPicker(UserProfileDataManager.PICKER_WEIGHT);
      this.profileButtonsListener.openPicker();
      continue;
      this.userProfileDataManager.setCurrentPicker(UserProfileDataManager.PICKER_HEIGHT);
      this.profileButtonsListener.openPicker();
      continue;
      this.userProfileDataManager.setCurrentPicker(UserProfileDataManager.PICKER_BIRTH);
      this.profileButtonsListener.openPicker();
      continue;
      checkData();
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903127, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    initData();
    this.updateAboutProg.updateAboutProgress();
    return paramLayoutInflater;
  }
  
  public void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean)
  {
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    RST_User localRST_User = localRefreshModelController.getUser();
    switch (paramSeekBar.getId())
    {
    }
    for (;;)
    {
      localRefreshModelController.save();
      this.updateAboutProg.updateAboutProgress();
      return;
      int i;
      if (localRST_User.getProfile().getHighBloodPressure() == -1)
      {
        if ((paramInt == 0) || (paramInt == 2))
        {
          i = paramInt;
          if (paramInt == 2) {
            i = 1;
          }
          setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupAboutBlood);
          localRST_User.getProfile().setHighBloodPressure(i);
        }
      }
      else
      {
        localRST_User.getProfile().setHighBloodPressure(paramInt);
        continue;
        if (localRST_User.getProfile().getNewGender().trim().length() == 0)
        {
          if ((paramInt == 0) || (paramInt == 2))
          {
            i = paramInt;
            if (paramInt == 2) {
              i = 1;
            }
            setCustomSeekBarSemiSliderAdapterForGender(i, this.csssbSetupAboutGender);
            if (i == 1) {
              localRST_User.getProfile().setNewGender("F");
            }
            for (;;)
            {
              setNeckTextForGender(i);
              break;
              localRST_User.getProfile().setNewGender("M");
            }
          }
        }
        else
        {
          if (paramInt == 1) {
            localRST_User.getProfile().setNewGender("F");
          }
          for (;;)
          {
            setNeckTextForGender(paramInt);
            break;
            localRST_User.getProfile().setNewGender("M");
          }
          if (localRST_User.getProfile().getNeckSize() == -1)
          {
            if ((paramInt == 0) || (paramInt == 2))
            {
              i = paramInt;
              if (paramInt == 2) {
                i = 1;
              }
              setCustomSeekBarSemiSliderAdapter(i, this.csssbSetupAboutNeck);
              localRST_User.getProfile().setNeckSize(i);
            }
          }
          else {
            localRST_User.getProfile().setNeckSize(paramInt);
          }
        }
      }
    }
  }
  
  public void onResult(RST_Response<RST_UserProfile> paramRST_Response)
  {
    if ((getBaseActivity() == null) || (getBaseActivity().isFinishing()) || (!getBaseActivity().isAppValidated(paramRST_Response.getErrorCode()))) {
      return;
    }
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    if (paramRST_Response.isStatus()) {}
    for (boolean bool = false;; bool = true)
    {
      localRefreshModelController.setProfileUpdatePending(bool);
      break;
    }
  }
  
  public void onStartTrackingTouch(SeekBar paramSeekBar) {}
  
  public void onStop()
  {
    super.onStop();
  }
  
  public void onStopTrackingTouch(SeekBar paramSeekBar) {}
  
  public void setAboutUpdateProgressRef(IAboutUpdateProgress paramIAboutUpdateProgress)
  {
    this.updateAboutProg = paramIAboutUpdateProgress;
  }
  
  public void setAgeUI()
  {
    this.tvSetupAboutBirthValue.setText(this.userProfileDataManager.getLabelForBithDate());
    this.updateAboutProg.updateAboutProgress();
  }
  
  public void setHeightUI()
  {
    this.tvSetupAboutHeightValue.setText(this.userProfileDataManager.getLabelForHeight());
    this.updateAboutProg.updateAboutProgress();
  }
  
  public void setWeightUI()
  {
    this.tvSetupAboutWeightValue.setText(this.userProfileDataManager.getLabelForWeight());
    this.updateAboutProg.updateAboutProgress();
  }
  
  public static abstract interface IAboutUpdateProgress
  {
    public abstract void screenTransitionFromAboutFrag(int paramInt1, int paramInt2);
    
    public abstract void updateAboutProgress();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\AboutMeFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.UserProfileDataManager;
import com.resmed.refresh.utils.RefreshTools;

public class ProfileFragment
  extends BaseFragment
{
  private RelativeLayout ageButton;
  private TextView ageProfileEditText;
  private RelativeLayout collarSizeButton;
  private TextView collarSizeProfileEditText;
  private EditText emailProfileTextView;
  private RelativeLayout genderButton;
  private TextView genderProfileEditText;
  private RelativeLayout heightButton;
  private TextView heightProfileEditText;
  private UserProfileButtons profileButtonsListener;
  private UserProfileDataManager userProfileDataManager;
  private RelativeLayout weightButton;
  private TextView weightProfileEditText;
  
  private void mapGUI(View paramView)
  {
    this.ageProfileEditText = ((TextView)paramView.findViewById(2131100316));
    this.heightProfileEditText = ((TextView)paramView.findViewById(2131100318));
    this.weightProfileEditText = ((TextView)paramView.findViewById(2131100321));
    this.collarSizeProfileEditText = ((TextView)paramView.findViewById(2131100323));
    this.genderProfileEditText = ((TextView)paramView.findViewById(2131100314));
    this.emailProfileTextView = ((EditText)paramView.findViewById(2131100311));
    this.ageButton = ((RelativeLayout)paramView.findViewById(2131100315));
    this.genderButton = ((RelativeLayout)paramView.findViewById(2131100313));
    this.heightButton = ((RelativeLayout)paramView.findViewById(2131100317));
    this.weightButton = ((RelativeLayout)paramView.findViewById(2131100320));
    this.collarSizeButton = ((RelativeLayout)paramView.findViewById(2131100322));
    this.emailProfileTextView.setCursorVisible(false);
  }
  
  private void setupListeners()
  {
    View.OnClickListener local1 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        switch (paramAnonymousView.getId())
        {
        }
        for (;;)
        {
          RefreshTools.hideKeyBoard(ProfileFragment.this.getActivity());
          paramAnonymousView.requestFocus();
          return;
          ProfileFragment.this.userProfileDataManager.setCurrentPicker(UserProfileDataManager.PICKER_BIRTH);
          ProfileFragment.this.profileButtonsListener.openPicker();
          continue;
          ProfileFragment.this.userProfileDataManager.setCurrentPicker(UserProfileDataManager.PICKER_GENDER);
          ProfileFragment.this.profileButtonsListener.openPicker();
          continue;
          ProfileFragment.this.userProfileDataManager.setCurrentPicker(UserProfileDataManager.PICKER_HEIGHT);
          ProfileFragment.this.profileButtonsListener.openPicker();
          continue;
          ProfileFragment.this.userProfileDataManager.setCurrentPicker(UserProfileDataManager.PICKER_WEIGHT);
          ProfileFragment.this.profileButtonsListener.openPicker();
          continue;
          ProfileFragment.this.userProfileDataManager.setCurrentPicker(UserProfileDataManager.PICKER_COLLAR_SIZE);
          ProfileFragment.this.profileButtonsListener.openPicker();
        }
      }
    };
    this.ageButton.setOnClickListener(local1);
    this.genderButton.setOnClickListener(local1);
    this.heightButton.setOnClickListener(local1);
    this.weightButton.setOnClickListener(local1);
    this.collarSizeButton.setOnClickListener(local1);
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.profileButtonsListener = ((UserProfileButtons)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new ClassCastException(paramActivity.toString() + " ...you must implement ProfileButtons from your Activity ;-) !");
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903153, paramViewGroup, false);
    this.userProfileDataManager = UserProfileDataManager.getInstance();
    mapGUI(paramLayoutInflater);
    this.emailProfileTextView.setText(this.userProfileDataManager.getNameValue());
    setupListeners();
    return paramLayoutInflater;
  }
  
  public void onResume()
  {
    super.onResume();
    setHeightUI();
    setWeightUI();
    setAgeUI();
    setCollarSizeUI();
    setGenderUI();
  }
  
  public void setAgeUI()
  {
    this.ageProfileEditText.setText(this.userProfileDataManager.getLabelForAge());
  }
  
  public void setCollarSizeUI()
  {
    this.collarSizeProfileEditText.setText(this.userProfileDataManager.getLabelForCollarSize());
  }
  
  public void setGenderUI()
  {
    this.genderProfileEditText.setText(this.userProfileDataManager.getLabelForGender());
  }
  
  public void setHeightUI()
  {
    this.heightProfileEditText.setText(this.userProfileDataManager.getLabelForHeight());
  }
  
  public void setWeightUI()
  {
    this.weightProfileEditText.setText(this.userProfileDataManager.getLabelForWeight());
  }
  
  public static abstract interface UserProfileButtons
  {
    public abstract void openPicker();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\ProfileFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
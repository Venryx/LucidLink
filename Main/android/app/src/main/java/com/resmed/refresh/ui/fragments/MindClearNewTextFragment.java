package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.resmed.refresh.model.mindclear.MindClearManager;
import com.resmed.refresh.ui.activity.MindClearActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.ActiveFragmentInterface;

public class MindClearNewTextFragment
  extends BaseFragment
{
  private EditText editText;
  private ActiveFragmentInterface mcActiveFragmentInterface;
  
  private void mapGUI(View paramView)
  {
    this.editText = ((EditText)paramView.findViewById(2131099933));
  }
  
  private void setupListeners()
  {
    this.editText.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramAnonymousEditable)
      {
        MindClearManager.getInstance().setTempText(MindClearNewTextFragment.this.editText.getText().toString());
      }
      
      public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
      
      public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
    });
  }
  
  public void hideKeyboard()
  {
    ((InputMethodManager)getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.editText.getWindowToken(), 0);
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.mcActiveFragmentInterface = ((ActiveFragmentInterface)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new ClassCastException(paramActivity.toString() + " ...you must implement SmartAlarmBtn from your Activity ;-) !");
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903132, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    return paramLayoutInflater;
  }
  
  public void onResume()
  {
    super.onResume();
    if (MindClearActivity.isBackPressed) {
      this.editText.setText("");
    }
    ActiveFragmentInterface localActiveFragmentInterface = this.mcActiveFragmentInterface;
    ((MindClearActivity)getActivity()).getClass();
    localActiveFragmentInterface.notifyCurrentFragment(2);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\MindClearNewTextFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
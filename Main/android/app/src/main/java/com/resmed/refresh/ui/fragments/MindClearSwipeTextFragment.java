package com.resmed.refresh.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.resmed.refresh.model.mindclear.MindClearManager;
import com.resmed.refresh.model.mindclear.MindClearText;
import com.resmed.refresh.ui.uibase.base.BaseFragment;

public class MindClearSwipeTextFragment
  extends BaseFragment
{
  private EditText editText;
  private int index;
  private boolean isInit = true;
  private MindClearPagerFragment ref;
  private String text;
  
  public MindClearSwipeTextFragment(MindClearPagerFragment paramMindClearPagerFragment, String paramString, int paramInt)
  {
    this.text = paramString;
    this.ref = paramMindClearPagerFragment;
    this.index = paramInt;
  }
  
  private void mapGUI(View paramView)
  {
    this.editText = ((EditText)paramView.findViewById(2131100679));
  }
  
  private void setupListener()
  {
    this.editText.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramAnonymousEditable)
      {
        ((MindClearText)MindClearManager.getInstance().getMindClearNotes().get(MindClearSwipeTextFragment.this.index)).setText(MindClearSwipeTextFragment.this.editText.getText().toString());
      }
      
      public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
      
      public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
    });
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903202, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    this.editText.setText(this.text);
    setupListener();
    return paramLayoutInflater;
  }
  
  public void onResume()
  {
    super.onResume();
    if (!this.isInit) {
      if (this.ref != null) {
        this.ref.getAdapter().resetAllAudio();
      }
    }
    for (;;)
    {
      return;
      this.isInit = false;
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\MindClearSwipeTextFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
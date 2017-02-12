package com.resmed.refresh.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.utils.BluetoothDataSerializeUtil;

public class ReconnectionProblemFragment
  extends BaseFragment
  implements View.OnClickListener
{
  private TextView tvRecProbOk;
  
  private void mapGUI(View paramView)
  {
    this.tvRecProbOk = ((TextView)paramView.findViewById(2131100331));
    this.tvRecProbOk.setOnClickListener(this);
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    }
    for (;;)
    {
      return;
      getBaseActivity().onBackPressed();
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903155, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    BluetoothDataSerializeUtil.clearJSONFile(getActivity());
    return paramLayoutInflater;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\ReconnectionProblemFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
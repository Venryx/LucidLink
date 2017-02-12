package com.resmed.refresh.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.resmed.refresh.ui.activity.GuideActivty;
import com.resmed.refresh.ui.activity.HomeActivity;
import com.resmed.refresh.ui.activity.SensorSetupActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;

public class GuideQuestionFragment
  extends BaseFragment
  implements View.OnClickListener
{
  private TextView tvGuideQuestionLater;
  private TextView tvGuideQuestionOk;
  
  private void mapGUI(View paramView)
  {
    this.tvGuideQuestionOk = ((TextView)paramView.findViewById(2131099998));
    this.tvGuideQuestionLater = ((TextView)paramView.findViewById(2131099999));
  }
  
  private void setupListeners()
  {
    this.tvGuideQuestionOk.setOnClickListener(this);
    this.tvGuideQuestionLater.setOnClickListener(this);
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    }
    for (;;)
    {
      return;
      startActivity(new Intent(getActivity(), GuideActivty.class));
      continue;
      paramView = new Intent(getActivity(), HomeActivity.class);
      paramView.setFlags(268533760);
      Intent localIntent = new Intent(getActivity(), SensorSetupActivity.class);
      localIntent.putExtra("com.resmed.refresh.ui.uibase.app.activity_modal", true);
      localIntent.putExtra("com.resmed.refresh.ui.uibase.app.came_from_home", true);
      startActivity(paramView);
      startActivity(localIntent);
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903143, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    return paramLayoutInflater;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\GuideQuestionFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
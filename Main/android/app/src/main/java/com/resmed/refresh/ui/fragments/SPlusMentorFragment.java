package com.resmed.refresh.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.resmed.refresh.model.RST_AdviceItem;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import java.util.List;

public class SPlusMentorFragment
  extends BaseFragment
  implements PullToRefreshBase.OnRefreshListener<ScrollView>, RST_CallbackItem<RST_Response<List<RST_AdviceItem>>>
{
  private View fragmentSMenorAdvices;
  private PullToRefreshScrollView pltSmentorNoAdvices;
  private View viewLoadingSPlusMentor;
  
  private void initDataView()
  {
    this.viewLoadingSPlusMentor.setVisibility(0);
    this.pltSmentorNoAdvices.setVisibility(4);
    this.fragmentSMenorAdvices.setVisibility(4);
  }
  
  private void mapGUI(View paramView)
  {
    this.viewLoadingSPlusMentor = paramView.findViewById(2131100560);
    this.pltSmentorNoAdvices = ((PullToRefreshScrollView)paramView.findViewById(2131100562));
    this.fragmentSMenorAdvices = paramView.findViewById(2131100563);
  }
  
  private void setupListeners()
  {
    this.pltSmentorNoAdvices.setOnRefreshListener(this);
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903178, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    initDataView();
    RefreshModelController.getInstance().latestAdviceList(this, false);
    return paramLayoutInflater;
  }
  
  public void onRefresh(PullToRefreshBase<ScrollView> paramPullToRefreshBase)
  {
    RefreshModelController.getInstance().latestAdviceList(this, true);
  }
  
  public void onResult(RST_Response<List<RST_AdviceItem>> paramRST_Response)
  {
    if ((getBaseActivity() == null) || (getBaseActivity().isFinishing()) || (!getBaseActivity().isAppValidated(paramRST_Response.getErrorCode()))) {}
    for (;;)
    {
      return;
      if (this.pltSmentorNoAdvices.isRefreshing()) {
        this.pltSmentorNoAdvices.onRefreshComplete();
      }
      this.viewLoadingSPlusMentor.setVisibility(4);
      if (paramRST_Response.isStatus())
      {
        if ((paramRST_Response.getResponse() != null) && (((List)paramRST_Response.getResponse()).size() > 0))
        {
          this.pltSmentorNoAdvices.setVisibility(4);
          this.fragmentSMenorAdvices.setVisibility(0);
          getFragmentManager().beginTransaction().add(2131100563, new SPlusMentorPagerFragment()).commit();
        }
        else
        {
          this.pltSmentorNoAdvices.setVisibility(0);
          this.fragmentSMenorAdvices.setVisibility(4);
        }
      }
      else
      {
        getBaseActivity().showErrorDialog(paramRST_Response);
        this.pltSmentorNoAdvices.setVisibility(0);
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SPlusMentorFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
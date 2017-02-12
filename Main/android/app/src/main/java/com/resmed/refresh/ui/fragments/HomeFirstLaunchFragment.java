package com.resmed.refresh.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.ui.activity.GuideActivty;
import com.resmed.refresh.ui.activity.SleepAidsActivity;
import com.resmed.refresh.ui.activity.SleepTimeActivity;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;
import java.util.List;

public class HomeFirstLaunchFragment
  extends BaseFragment
  implements BluetoothDataListener, View.OnClickListener, PullToRefreshBase.OnRefreshListener<ScrollView>
{
  private Button bHomeFirstSleep;
  private RST_CallbackItem<RST_Response<Object>> callbackLoading = new RST_CallbackItem()
  {
    public void onResult(RST_Response<Object> paramAnonymousRST_Response)
    {
      if ((HomeFirstLaunchFragment.this.getBaseActivity() == null) || (HomeFirstLaunchFragment.this.getBaseActivity().isFinishing()) || (!HomeFirstLaunchFragment.this.getBaseActivity().isAppValidated(paramAnonymousRST_Response.getErrorCode()))) {}
      for (;;)
      {
        return;
        if (HomeFirstLaunchFragment.this.ptrHome.isRefreshing()) {
          HomeFirstLaunchFragment.this.ptrHome.onRefreshComplete();
        }
        if (!paramAnonymousRST_Response.isStatus()) {
          HomeFirstLaunchFragment.this.getBaseActivity().showErrorDialog(paramAnonymousRST_Response);
        } else {
          HomeFirstLaunchFragment.this.onDataReceived();
        }
      }
    }
  };
  private RST_CallbackItem<RST_Response<List<RST_SleepSessionInfo>>> callbackRecords = new RST_CallbackItem()
  {
    public void onResult(RST_Response<List<RST_SleepSessionInfo>> paramAnonymousRST_Response)
    {
      if ((HomeFirstLaunchFragment.this.getBaseActivity() == null) || (HomeFirstLaunchFragment.this.getBaseActivity().isFinishing()) || (!HomeFirstLaunchFragment.this.getBaseActivity().isAppValidated(paramAnonymousRST_Response.getErrorCode()))) {}
      for (;;)
      {
        return;
        if (HomeFirstLaunchFragment.this.ptrHome.isRefreshing()) {
          HomeFirstLaunchFragment.this.ptrHome.onRefreshComplete();
        }
        if (!paramAnonymousRST_Response.isStatus()) {
          HomeFirstLaunchFragment.this.getBaseActivity().showErrorDialog(paramAnonymousRST_Response);
        } else {
          HomeFirstLaunchFragment.this.onDataReceived();
        }
      }
    }
  };
  private LinearLayout llHomeTakeTour;
  private PullToRefreshScrollView ptrHome;
  private RelativeLayout rlHomeFirstSleepAids;
  
  private void initDataView()
  {
    BaseActivity localBaseActivity = getBaseActivity();
    localBaseActivity.setTitle(2131165945);
    if ((localBaseActivity instanceof BaseBluetoothActivity)) {
      handleConnectionStatus(RefreshApplication.getInstance().getCurrentConnectionState());
    }
  }
  
  private void mapGUI(View paramView)
  {
    this.ptrHome = ((PullToRefreshScrollView)paramView.findViewById(2131100000));
    this.rlHomeFirstSleepAids = ((RelativeLayout)paramView.findViewById(2131100023));
    this.bHomeFirstSleep = ((Button)paramView.findViewById(2131100026));
    this.llHomeTakeTour = ((LinearLayout)paramView.findViewById(2131100022));
    this.bHomeFirstSleep.setTypeface(this.akzidenzLight);
  }
  
  private void onDataReceived()
  {
    if ((getActivity() != null) && (!getActivity().isFinishing()) && (RefreshModelController.getInstance().localSleepSessionsAll().size() > 0))
    {
      Object localObject = getFragmentManager();
      if (localObject != null)
      {
        localObject = ((FragmentManager)localObject).beginTransaction();
        ((FragmentTransaction)localObject).replace(2131099795, new HomeFragment());
        ((FragmentTransaction)localObject).commitAllowingStateLoss();
      }
    }
  }
  
  private void setupListeners()
  {
    this.ptrHome.setOnRefreshListener(this);
    this.rlHomeFirstSleepAids.setOnClickListener(this);
    this.bHomeFirstSleep.setOnClickListener(this);
    this.llHomeTakeTour.setOnClickListener(this);
  }
  
  private void updateSleepButtonBackground()
  {
    try
    {
      this.bHomeFirstSleep.setBackgroundResource(RefreshApplication.getInstance().getConnectionStatus().getBackgroundButtonConnected());
      return;
    }
    finally
    {
      localObject = finally;
      throw ((Throwable)localObject);
    }
  }
  
  public void handleBreathingRate(Bundle paramBundle) {}
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    updateSleepButtonBackground();
  }
  
  public void handleEnvSample(Bundle paramBundle) {}
  
  public void handleReceivedRpc(JsonRPC paramJsonRPC) {}
  
  public void handleSessionRecovered(Bundle paramBundle) {}
  
  public void handleSleepSessionStopped(Bundle paramBundle) {}
  
  public void handleStreamPacket(Bundle paramBundle) {}
  
  public void handleUserSleepState(Bundle paramBundle) {}
  
  public void onClick(View paramView)
  {
    Object localObject2 = null;
    Object localObject1 = localObject2;
    switch (paramView.getId())
    {
    default: 
      localObject1 = localObject2;
    }
    for (;;)
    {
      if (localObject1 != null) {
        startActivity((Intent)localObject1);
      }
      return;
      localObject1 = new Intent(getActivity(), SleepAidsActivity.class);
      continue;
      paramView = getBaseActivity().getPendingManageDataIntent();
      localObject1 = paramView;
      if (paramView == null)
      {
        localObject1 = new Intent(getActivity(), SleepTimeActivity.class);
        continue;
        localObject1 = new Intent(getActivity(), GuideActivty.class);
        ((Intent)localObject1).putExtra("com.resmed.refresh.ui.uibase.app.came_from_landing", true);
      }
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903145, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    initDataView();
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        HomeFirstLaunchFragment.this.ptrHome.setRefreshing(true);
        RefreshModelController.getInstance().synchroniseLatestAll(HomeFirstLaunchFragment.this.callbackLoading, true);
      }
    }, 500L);
    return paramLayoutInflater;
  }
  
  public void onRefresh(PullToRefreshBase<ScrollView> paramPullToRefreshBase)
  {
    RefreshModelController.getInstance().synchroniseLatestAll(this.callbackLoading, true);
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\HomeFirstLaunchFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
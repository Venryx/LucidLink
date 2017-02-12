package com.resmed.refresh.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.RST_AdviceItem;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.ui.activity.MindClearActivity;
import com.resmed.refresh.ui.activity.SPlusMentorActivity;
import com.resmed.refresh.ui.activity.SleepHistoryDayActivity;
import com.resmed.refresh.ui.activity.SleepHistoryMonthActivity;
import com.resmed.refresh.ui.activity.SleepTimeActivity;
import com.resmed.refresh.ui.customview.BadgyButton;
import com.resmed.refresh.ui.customview.CustomBattery;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseActivity;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.uibase.base.BluetoothDataListener;
import com.resmed.refresh.utils.Log;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

public class HomeFragment
  extends BaseFragment
  implements BluetoothDataListener, View.OnClickListener, PullToRefreshBase.OnRefreshListener<ScrollView>, RST_CallbackItem<RST_Response<Object>>
{
  private static final int ANIMATION_TIME = 2000;
  private Button bHomeClearMind;
  private Button bHomeSMentor;
  private Button bHomeSleep;
  private Button bHomeSleepHistory;
  private BadgyButton badgyHomeClearMind;
  private BadgyButton badgyHomeSleepHistory;
  private BadgyButton badgyHomeSmentor;
  private CustomBattery batteryBody;
  private CustomBattery batteryMind;
  private long idSleepSession;
  private boolean isRefreshing = false;
  private ImageView ivHomeShare;
  private LinearLayout llHomeContent;
  private LinearLayout llHomeMotivationalPart;
  private LinearLayout llHomeSleepInfo;
  private PullToRefreshScrollView ptrHome;
  private TextView tvHomeBodyValue;
  private TextView tvHomeLastDay;
  private TextView tvHomeMindValue;
  private TextView tvHomeMotivational;
  private TextView tvHomeSleepScore;
  
  private void bindViewData(final RST_SleepSessionInfo paramRST_SleepSessionInfo, List<RST_AdviceItem> paramList)
  {
    if (paramRST_SleepSessionInfo != null)
    {
      Log.d("com.resmed.refresh.ids", "HomeFragment initDataView id = " + paramRST_SleepSessionInfo.getId() + " " + paramRST_SleepSessionInfo.getStartTime() + " " + paramRST_SleepSessionInfo.getStopTime());
      if (this.idSleepSession != paramRST_SleepSessionInfo.getId())
      {
        final View localView = getView();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
        {
          public void run()
          {
            HomeFragment.this.idSleepSession = paramRST_SleepSessionInfo.getId();
            localView.startAnimation(new HomeFragment.ScoreAnimation(HomeFragment.this, paramRST_SleepSessionInfo.getSleepScore(), paramRST_SleepSessionInfo.getBodyScore(), paramRST_SleepSessionInfo.getMindScore()));
            HomeFragment.this.batteryMind.setPercentage(paramRST_SleepSessionInfo.getMindScore(), true);
            HomeFragment.this.batteryBody.setPercentage(paramRST_SleepSessionInfo.getBodyScore(), true);
            SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("dd MMM");
            HomeFragment.this.tvHomeLastDay.setText(localSimpleDateFormat.format(paramRST_SleepSessionInfo.getStartTime()));
          }
        }, 500L);
      }
      if (paramList.size() != 0) {
        break label116;
      }
      this.llHomeMotivationalPart.setVisibility(8);
    }
    for (;;)
    {
      return;
      label116:
      this.llHomeMotivationalPart.setVisibility(0);
    }
  }
  
  private void debugPrintRecords()
  {
    Object localObject = RefreshModelController.getInstance().localSleepSessionsAll();
    Log.d("Records", " ");
    Log.d("Records", "Start ");
    Iterator localIterator = ((List)localObject).iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        Log.d("Records", "End ");
        Log.d("Records", " ");
        return;
      }
      localObject = (RST_SleepSessionInfo)localIterator.next();
      Log.d("Records", "ID: " + ((RST_SleepSessionInfo)localObject).getId() + "\tScore: " + ((RST_SleepSessionInfo)localObject).getSleepScore() + "\t" + ((RST_SleepSessionInfo)localObject).getStartTime() + "\t" + ((RST_SleepSessionInfo)localObject).getStopTime());
    }
  }
  
  private void initDataView()
  {
    BaseActivity localBaseActivity = getBaseActivity();
    if ((localBaseActivity == null) || (localBaseActivity.isFinishing())) {
      return;
    }
    localBaseActivity.setTitle(2131165945);
    if (RefreshModelController.getInstance().localLatestSleepSession() == null) {
      this.ptrHome.setRefreshing(true);
    }
    for (;;)
    {
      this.badgyHomeSleepHistory.setBadgeCount(Integer.valueOf(0));
      if (!(localBaseActivity instanceof BaseBluetoothActivity)) {
        break;
      }
      handleConnectionStatus(RefreshApplication.getInstance().getCurrentConnectionState());
      break;
      this.llHomeContent.setVisibility(0);
    }
  }
  
  private void mapGUI(View paramView)
  {
    this.ptrHome = ((PullToRefreshScrollView)paramView.findViewById(2131100000));
    this.llHomeContent = ((LinearLayout)paramView.findViewById(2131100001));
    this.bHomeSMentor = ((Button)paramView.findViewById(2131100016));
    this.bHomeSleepHistory = ((Button)paramView.findViewById(2131100014));
    this.bHomeClearMind = ((Button)paramView.findViewById(2131100018));
    this.bHomeSleep = ((Button)paramView.findViewById(2131100020));
    this.tvHomeSleepScore = ((TextView)paramView.findViewById(2131100004));
    this.tvHomeMindValue = ((TextView)paramView.findViewById(2131100005));
    this.tvHomeBodyValue = ((TextView)paramView.findViewById(2131100007));
    this.tvHomeLastDay = ((TextView)paramView.findViewById(2131100002));
    this.tvHomeMotivational = ((TextView)paramView.findViewById(2131100011));
    this.llHomeMotivationalPart = ((LinearLayout)paramView.findViewById(2131100010));
    this.llHomeSleepInfo = ((LinearLayout)paramView.findViewById(2131100003));
    this.badgyHomeSmentor = ((BadgyButton)paramView.findViewById(2131100017));
    this.badgyHomeSleepHistory = ((BadgyButton)paramView.findViewById(2131100015));
    this.badgyHomeClearMind = ((BadgyButton)paramView.findViewById(2131100019));
    this.ivHomeShare = ((ImageView)paramView.findViewById(2131100012));
    this.batteryMind = ((CustomBattery)paramView.findViewById(2131100006));
    this.batteryBody = ((CustomBattery)paramView.findViewById(2131100008));
    this.bHomeSMentor.setTypeface(this.akzidenzLight);
    this.bHomeSleepHistory.setTypeface(this.akzidenzLight);
    this.bHomeClearMind.setTypeface(this.akzidenzLight);
    this.bHomeSleep.setTypeface(this.akzidenzLight);
    this.bHomeSleep.setTypeface(this.akzidenzLight);
    this.badgyHomeSmentor.setTypeface(this.akzidenzLight);
    this.badgyHomeSleepHistory.setTypeface(this.akzidenzLight);
    this.badgyHomeClearMind.setTypeface(this.akzidenzLight);
    this.badgyHomeSmentor.setBadgeCount(Integer.valueOf(0));
    this.badgyHomeClearMind.setBadgeCount(Integer.valueOf(0));
  }
  
  private void setupListeners()
  {
    this.ptrHome.setScrollingWhileRefreshingEnabled(false);
    this.ptrHome.setOnRefreshListener(this);
    this.bHomeSMentor.setOnClickListener(this);
    this.bHomeSleepHistory.setOnClickListener(this);
    this.bHomeClearMind.setOnClickListener(this);
    this.bHomeSleep.setOnClickListener(this);
    this.ivHomeShare.setOnClickListener(this);
    this.llHomeSleepInfo.setOnClickListener(this);
  }
  
  private void updateSleepButtonBackground(CONNECTION_STATE paramCONNECTION_STATE)
  {
    if (paramCONNECTION_STATE == null) {}
    for (;;)
    {
      return;
      try
      {
        this.bHomeSleep.setBackgroundResource(RefreshApplication.getInstance().getConnectionStatus().getBackgroundButtonConnected());
      }
      finally {}
    }
  }
  
  public void handleBreathingRate(Bundle paramBundle) {}
  
  public void handleConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE)
  {
    Log.d("com.resmed.refresh.pair", "HomeFragment handleConnectionStatus CONNECTION_STATE : " + CONNECTION_STATE.toString(paramCONNECTION_STATE));
    if (paramCONNECTION_STATE == null) {}
    for (;;)
    {
      return;
      updateSleepButtonBackground(paramCONNECTION_STATE);
    }
  }
  
  public void handleEnvSample(Bundle paramBundle) {}
  
  public void handleReceivedRpc(JsonRPC paramJsonRPC) {}
  
  public void handleSessionRecovered(Bundle paramBundle) {}
  
  public void handleSleepSessionStopped(Bundle paramBundle) {}
  
  public void handleStreamPacket(Bundle paramBundle) {}
  
  public void handleUserSleepState(Bundle paramBundle) {}
  
  public void onClick(View paramView)
  {
    Object localObject = null;
    switch (paramView.getId())
    {
    default: 
      paramView = (View)localObject;
    }
    for (;;)
    {
      if (paramView != null) {
        startActivity(paramView);
      }
      return;
      RefreshModelController.getInstance().updateFlurryEvents("Home_Splusmentor");
      paramView = new Intent(getActivity(), SPlusMentorActivity.class);
      continue;
      RefreshModelController.getInstance().updateFlurryEvents("Home_Sleephistory");
      paramView = new Intent(getActivity(), SleepHistoryMonthActivity.class);
      continue;
      RefreshModelController.getInstance().updateFlurryEvents("Home_mindClear");
      paramView = new Intent(getActivity(), MindClearActivity.class);
      paramView.putExtra("IsFromHome", true);
      continue;
      RefreshModelController.getInstance().updateFlurryEvents("Home_sleep");
      boolean bool = RefreshApplication.getInstance().getConnectionStatus().isSocketConnected();
      paramView = getBaseActivity().getPendingManageDataIntent();
      if ((paramView == null) || (!bool))
      {
        paramView = new Intent(getActivity(), SleepTimeActivity.class);
        continue;
        Log.d("com.resmed.refresh.ids", "HomeFragment onClick id = " + this.idSleepSession);
        paramView = (View)localObject;
        if (this.idSleepSession != -1L)
        {
          paramView = new Intent(getActivity(), SleepHistoryDayActivity.class);
          paramView.putExtra("com.resmed.refresh.ui.uibase.app.sleep_history_id", this.idSleepSession);
          continue;
          paramView = new Intent();
          paramView.setAction("android.intent.action.SEND");
          paramView.putExtra("android.intent.extra.TEXT", this.tvHomeMotivational.getText().toString());
          paramView.setType("text/plain");
          paramView.setFlags(268435456);
        }
      }
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903144, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    if (RefreshModelController.getInstance().localSleepSessionsAll().size() == 0) {
      this.llHomeContent.setVisibility(4);
    }
    this.idSleepSession = -1L;
    BaseBluetoothActivity.IN_SLEEP_SESSION = false;
    RefreshModelController.getInstance().clearDB();
    new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
    {
      public void run()
      {
        HomeFragment.this.initDataView();
      }
    }, 750L);
    return paramLayoutInflater;
  }
  
  public void onDestroy()
  {
    super.onDestroy();
  }
  
  public void onRefresh(PullToRefreshBase<ScrollView> paramPullToRefreshBase)
  {
    RefreshModelController.getInstance().synchroniseLatestAll(this, true);
    if (getBaseActivity() != null) {
      getBaseActivity().hideLeftButton();
    }
  }
  
  public void onResult(RST_Response<Object> paramRST_Response)
  {
    if ((getBaseActivity() == null) || (getBaseActivity().isFinishing()) || (!getBaseActivity().isAppValidated(paramRST_Response.getErrorCode()))) {}
    for (;;)
    {
      return;
      getBaseActivity().showLeftButton();
      if (this.ptrHome.isRefreshing()) {
        this.ptrHome.onRefreshComplete();
      }
      this.llHomeContent.setVisibility(0);
      if (!paramRST_Response.isStatus()) {
        getBaseActivity().showErrorDialog(paramRST_Response);
      } else {
        bindViewData(RefreshModelController.getInstance().localLatestSleepSession(), RefreshModelController.getInstance().localTasks());
      }
    }
  }
  
  public void onResume()
  {
    super.onResume();
    bindViewData(RefreshModelController.getInstance().localLatestSleepSession(), RefreshModelController.getInstance().localTasks());
  }
  
  private class ScoreAnimation
    extends Animation
  {
    private int mBodyValue;
    private int mMindValue;
    private int mScoreValue;
    
    public ScoreAnimation(int paramInt1, int paramInt2, int paramInt3)
    {
      this.mScoreValue = paramInt1;
      this.mBodyValue = paramInt2;
      this.mMindValue = paramInt3;
      if ((this.mBodyValue == 100) || (this.mMindValue == 100))
      {
        HomeFragment.this.tvHomeBodyValue.setTextSize(0, HomeFragment.this.getResources().getDimension(2131034202));
        HomeFragment.this.tvHomeMindValue.setTextSize(0, HomeFragment.this.getResources().getDimension(2131034202));
      }
      setInterpolator(new LinearInterpolator());
      HomeFragment.this.tvHomeSleepScore.setText("0");
      HomeFragment.this.tvHomeBodyValue.setText("0%");
      HomeFragment.this.tvHomeMindValue.setText("0%");
      setDuration(2000L);
    }
    
    protected void applyTransformation(float paramFloat, Transformation paramTransformation)
    {
      super.applyTransformation(paramFloat, paramTransformation);
      float f = paramFloat;
      if (paramFloat >= 1.0F) {
        f = 1.0F;
      }
      HomeFragment.this.tvHomeSleepScore.setText(String.valueOf(Math.round(this.mScoreValue * f)));
      HomeFragment.this.tvHomeBodyValue.setText(Math.round(this.mBodyValue * f) + "%");
      HomeFragment.this.tvHomeMindValue.setText(Math.round(this.mMindValue * f) + "%");
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\HomeFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
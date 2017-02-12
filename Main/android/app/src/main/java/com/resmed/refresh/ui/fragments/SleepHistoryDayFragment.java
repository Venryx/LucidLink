package com.resmed.refresh.ui.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import com.resmed.refresh.model.RST_EnvironmentalInfo;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.adapters.SleepHistoryDayPageAdapter;
import com.resmed.refresh.ui.adapters.SleepHistoryItem;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.utils.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class SleepHistoryDayFragment
  extends BaseFragment
  implements View.OnClickListener, CalendarView.OnDateChangeListener, ViewPager.OnPageChangeListener, View.OnTouchListener
{
  public static final String LOG_TAG = "SleepHistoryFragment";
  private boolean animate;
  private Button bDatePickerCancel;
  private Button bDatePickerDone;
  private int currentPosition;
  private CalendarView cvDatePickerCalendar;
  private List<SleepHistoryItem> items;
  private ImageView ivBackgroundPicker;
  private ImageView ivInfinitePagerLeft;
  private ImageView ivInfinitePagerRigth;
  private int lastPosition;
  private float lastX;
  private int pageSelectedFromPicker;
  private SleepHistoryDayPageAdapter pagerAdapter;
  private View rlDatePicker;
  private float screenWidth;
  private TextView tvDatePickerDate;
  private TextView tvDatePickerDay;
  private TextView tvInfinitePagerValue;
  private TextView tvInfinitePagerValueNext;
  private ViewPager viewPager;
  
  private int findPositionForId(long paramLong)
  {
    int k = 0;
    int i = this.items.size() - 1;
    int j = (0 + i) / 2;
    if (k > i) {
      label28:
      return i;
    }
    if (((SleepHistoryItem)this.items.get(j)).getSessionInfo().getId() < paramLong) {
      k = j + 1;
    }
    for (;;)
    {
      j = (k + i) / 2;
      break;
      i = j;
      if (((SleepHistoryItem)this.items.get(j)).getSessionInfo().getId() == paramLong) {
        break label28;
      }
      i = j - 1;
    }
  }
  
  private void initDataView()
  {
    this.pageSelectedFromPicker = -1;
    this.animate = true;
    Object localObject = RefreshModelController.getInstance();
    List localList = ((RefreshModelController)localObject).localSleepSessionsAll();
    this.items = SleepHistoryItem.getList(localList);
    long l = getBaseActivity().getIntent().getLongExtra("com.resmed.refresh.ui.uibase.app.sleep_history_id", -1L);
    if (localList.size() == 0) {
      getActivity().onBackPressed();
    }
    for (;;)
    {
      return;
      RST_SleepSessionInfo localRST_SleepSessionInfo = ((RefreshModelController)localObject).localSleepSessionForId(l);
      localObject = localRST_SleepSessionInfo;
      if (localRST_SleepSessionInfo == null) {
        localObject = (RST_SleepSessionInfo)localList.get(localList.size() - 1);
      }
      Log.d("com.resmed.refresh.env", "day  " + ((RST_SleepSessionInfo)localObject).getEnvironmentalInfo().getSessionLight().size() + " light values and " + ((RST_SleepSessionInfo)localObject).getEnvironmentalInfo().getSessionTemperature().size() + " temp values");
      setupAdapter((RST_SleepSessionInfo)localObject, ((RST_SleepSessionInfo)localObject).getStartTime());
      this.screenWidth = getResources().getDisplayMetrics().widthPixels;
    }
  }
  
  private void mapGUI(View paramView)
  {
    this.viewPager = ((ViewPager)paramView.findViewById(2131100382));
    this.tvInfinitePagerValue = ((TextView)paramView.findViewById(2131100601));
    this.tvInfinitePagerValueNext = ((TextView)paramView.findViewById(2131100602));
    this.ivInfinitePagerLeft = ((ImageView)paramView.findViewById(2131100599));
    this.ivInfinitePagerRigth = ((ImageView)paramView.findViewById(2131100600));
    this.bDatePickerCancel = ((Button)paramView.findViewById(2131100588));
    this.bDatePickerDone = ((Button)paramView.findViewById(2131100589));
    this.rlDatePicker = paramView.findViewById(2131100584);
    this.cvDatePickerCalendar = ((CalendarView)paramView.findViewById(2131100590));
    this.tvDatePickerDay = ((TextView)paramView.findViewById(2131100586));
    this.tvDatePickerDate = ((TextView)paramView.findViewById(2131100587));
    this.ivBackgroundPicker = ((ImageView)paramView.findViewById(2131100585));
  }
  
  private void setupAdapter(RST_SleepSessionInfo paramRST_SleepSessionInfo, Date paramDate)
  {
    Log.d("", "loadData new adapter");
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    int i = 0;
    if (paramRST_SleepSessionInfo != null)
    {
      Log.d("com.resmed.refresh.ids", "SleepDayAdapter setupAdapter id = " + paramRST_SleepSessionInfo.getId() + " " + paramRST_SleepSessionInfo.getStartTime() + " " + paramRST_SleepSessionInfo.getStopTime());
      i = findPositionForId(paramRST_SleepSessionInfo.getId());
      this.pagerAdapter = new SleepHistoryDayPageAdapter(getActivity(), this.viewPager, this.items, this.animate);
    }
    for (;;)
    {
      this.viewPager.setAdapter(this.pagerAdapter);
      this.viewPager.setCurrentItem(i, false);
      paramRST_SleepSessionInfo = getStringRepresentation(this.pagerAdapter.getIndicator(i));
      this.tvInfinitePagerValue.setText(paramRST_SleepSessionInfo);
      this.tvInfinitePagerValueNext.setText(paramRST_SleepSessionInfo);
      paramRST_SleepSessionInfo = new GregorianCalendar();
      paramRST_SleepSessionInfo.setTime(this.pagerAdapter.getIndicator(i));
      updateDate(paramRST_SleepSessionInfo.get(1), paramRST_SleepSessionInfo.get(2), paramRST_SleepSessionInfo.get(5));
      return;
      Log.d("com.resmed.refresh.ids", "SleepDayAdapter setupAdapter for time = " + paramDate);
      ArrayList localArrayList = new ArrayList();
      RST_SleepSessionInfo localRST_SleepSessionInfo = localRefreshModelController.localPreviousSleepSession(paramDate);
      if (localRST_SleepSessionInfo != null)
      {
        i = 1;
        localArrayList.add(new SleepHistoryItem(localRST_SleepSessionInfo.getStartTime().getTime(), localRST_SleepSessionInfo));
        Log.d("com.resmed.refresh.ids", "SleepDayAdapter setupAdapter 3 days adapter");
        Log.d("com.resmed.refresh.ids", "SleepDayAdapter setupAdapter position = 0 id = " + localRST_SleepSessionInfo.getId() + " " + localRST_SleepSessionInfo.getStartTime() + " " + localRST_SleepSessionInfo.getStopTime());
      }
      localArrayList.add(new SleepHistoryItem(paramDate.getTime(), paramRST_SleepSessionInfo));
      Log.d("com.resmed.refresh.ids", "SleepDayAdapter setupAdapter position = 1 session null");
      paramRST_SleepSessionInfo = localRefreshModelController.localNextSleepSession(new Date(paramDate.getTime() - 1000L));
      if (paramRST_SleepSessionInfo != null)
      {
        localArrayList.add(new SleepHistoryItem(paramRST_SleepSessionInfo.getStartTime().getTime(), paramRST_SleepSessionInfo));
        Log.d("com.resmed.refresh.ids", "SleepDayAdapter setupAdapter position = 2 id = " + paramRST_SleepSessionInfo.getId() + " " + paramRST_SleepSessionInfo.getStartTime() + " " + paramRST_SleepSessionInfo.getStopTime());
      }
      this.pageSelectedFromPicker = i;
      this.pagerAdapter = new SleepHistoryDayPageAdapter(getActivity(), this.viewPager, localArrayList, this.animate);
      this.animate = false;
    }
  }
  
  private void setupListeners()
  {
    this.tvInfinitePagerValue.setOnClickListener(this);
    this.ivInfinitePagerLeft.setOnClickListener(this);
    this.ivInfinitePagerRigth.setOnClickListener(this);
    this.bDatePickerCancel.setOnClickListener(this);
    this.bDatePickerDone.setOnClickListener(this);
    this.cvDatePickerCalendar.setOnDateChangeListener(this);
    this.viewPager.setOnPageChangeListener(this);
    this.viewPager.setOnTouchListener(this);
  }
  
  private void updateDate(int paramInt1, int paramInt2, int paramInt3)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(paramInt1, paramInt2, paramInt3);
    String str2 = localGregorianCalendar.getDisplayName(2, 2, Locale.getDefault());
    String str1 = localGregorianCalendar.getDisplayName(7, 2, Locale.getDefault());
    this.tvDatePickerDay.setText(str1);
    this.tvDatePickerDate.setText(String.valueOf(paramInt3) + " " + str2 + " " + String.valueOf(paramInt1));
    this.cvDatePickerCalendar.setDate(localGregorianCalendar.getTimeInMillis());
  }
  
  public int getCurrentScore()
  {
    return ((SleepHistoryItem)this.items.get(this.currentPosition)).getSessionInfo().getSleepScore();
  }
  
  public Object getCurrentViewTag()
  {
    return ((SleepHistoryItem)this.items.get(this.currentPosition)).getSessionInfo();
  }
  
  public String getStringRepresentation(long paramLong)
  {
    return new SimpleDateFormat("dd MMMM yyyy").format(Long.valueOf(paramLong));
  }
  
  public String getStringRepresentation(Date paramDate)
  {
    return getStringRepresentation(paramDate.getTime());
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    }
    for (;;)
    {
      return;
      if (this.viewPager.getCurrentItem() > 0)
      {
        this.viewPager.setCurrentItem(this.viewPager.getCurrentItem() - 1, true);
        continue;
        if (this.pagerAdapter.getCount() > this.viewPager.getCurrentItem() + 1)
        {
          this.viewPager.setCurrentItem(this.viewPager.getCurrentItem() + 1, true);
          continue;
          getBaseActivity().setRollingBackground(this.ivBackgroundPicker);
          this.rlDatePicker.setClickable(true);
          this.rlDatePicker.setVisibility(0);
          this.rlDatePicker.setAnimation(AnimationUtils.loadAnimation(getActivity(), 2130968597));
          continue;
          this.rlDatePicker.setClickable(false);
          paramView = new Date(this.cvDatePickerCalendar.getDate());
          setupAdapter(RefreshModelController.getInstance().localSleepSessionInDay(paramView), paramView);
          this.rlDatePicker.setVisibility(8);
          this.rlDatePicker.setAnimation(AnimationUtils.loadAnimation(getActivity(), 2130968588));
          continue;
          this.rlDatePicker.setClickable(false);
          this.rlDatePicker.setVisibility(8);
          this.rlDatePicker.setAnimation(AnimationUtils.loadAnimation(getActivity(), 2130968588));
        }
      }
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903166, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    initDataView();
    return paramLayoutInflater;
  }
  
  public void onPageScrollStateChanged(int paramInt)
  {
    if (paramInt == 0)
    {
      this.lastPosition = 0;
      this.tvInfinitePagerValue.setAlpha(1.0F);
      this.tvInfinitePagerValueNext.setAlpha(0.0F);
      if ((this.pageSelectedFromPicker != -1) && (this.pageSelectedFromPicker != this.currentPosition))
      {
        this.pageSelectedFromPicker = -1;
        Date localDate = this.pagerAdapter.getIndicator(this.currentPosition);
        RST_SleepSessionInfo localRST_SleepSessionInfo2 = RefreshModelController.getInstance().localSleepSessionForId(localDate.getTime() / 1000L);
        RST_SleepSessionInfo localRST_SleepSessionInfo1 = localRST_SleepSessionInfo2;
        if (localRST_SleepSessionInfo2 == null) {
          localRST_SleepSessionInfo1 = RefreshModelController.getInstance().localSleepSessionInDay(localDate);
        }
        setupAdapter(localRST_SleepSessionInfo1, localDate);
      }
    }
    for (;;)
    {
      return;
      if (paramInt == 2)
      {
        this.tvInfinitePagerValueNext.setText(getStringRepresentation(this.pagerAdapter.getIndicator(this.lastPosition)));
        this.tvInfinitePagerValue.setAlpha(1.0F);
        this.tvInfinitePagerValueNext.setAlpha(0.0F);
      }
    }
  }
  
  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
    this.lastPosition = paramInt1;
  }
  
  public void onPageSelected(int paramInt)
  {
    this.currentPosition = paramInt;
    Log.d("SleepHistoryFragment", "onPageSelected  = " + paramInt);
    this.tvInfinitePagerValue.setText(getStringRepresentation(this.pagerAdapter.getIndicator(paramInt)));
    this.tvInfinitePagerValueNext.setText(getStringRepresentation(this.pagerAdapter.getIndicator(paramInt)));
  }
  
  public void onSelectedDayChange(CalendarView paramCalendarView, int paramInt1, int paramInt2, int paramInt3)
  {
    updateDate(paramInt1, paramInt2, paramInt3);
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    if ((this.viewPager == null) || (this.viewPager.getAdapter().getCount() == 0)) {}
    for (;;)
    {
      return false;
      switch (paramMotionEvent.getAction())
      {
      default: 
        break;
      case 0: 
        this.lastX = paramMotionEvent.getX();
        break;
      case 2: 
        if (this.lastX == 0.0F) {
          this.lastX = paramMotionEvent.getX();
        }
        this.tvInfinitePagerValue.getText().toString();
        if (paramMotionEvent.getX() > this.lastX) {}
        for (paramView = getStringRepresentation(this.pagerAdapter.getPreviousIndicator());; paramView = getStringRepresentation(this.pagerAdapter.getNextIndicator()))
        {
          float f2 = 1.0F - Math.abs(2.0F * (paramMotionEvent.getX() - this.lastX) / this.screenWidth);
          float f1 = f2;
          if (f2 < 0.0F) {
            f1 = 0.0F;
          }
          this.tvInfinitePagerValueNext.setText(paramView);
          this.tvInfinitePagerValue.setAlpha(f1);
          this.tvInfinitePagerValueNext.setAlpha(1.0F - f1);
          break;
        }
      case 1: 
      case 3: 
        this.lastX = 0.0F;
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SleepHistoryDayFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
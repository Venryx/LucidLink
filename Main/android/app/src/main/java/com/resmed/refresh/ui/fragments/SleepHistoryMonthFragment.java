package com.resmed.refresh.ui.fragments;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import com.resmed.refresh.model.RST_SleepSessionInfo;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.adapters.SleepHistoryMonthPageAdapter;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.InfiniteViewPager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SleepHistoryMonthFragment
  extends BaseFragment
  implements View.OnClickListener, CalendarView.OnDateChangeListener, InfiniteViewPager.OnInfinitePageChangeListener
{
  public static final String LOG_TAG = "SleepHistoryFragment";
  private Button bDatePickerCancel;
  private Button bDatePickerDone;
  private boolean changeState = false;
  private CalendarView cvDatePickerCalendar;
  private int directionOld;
  private ImageView ivBackgroundPicker;
  private ImageView ivInfinitePagerLeft;
  private ImageView ivInfinitePagerRigth;
  private SleepHistoryMonthPageAdapter pagerAdapter;
  private View rlDatePicker;
  private int state;
  private TextView tvDatePickerDate;
  private TextView tvDatePickerDay;
  private TextView tvInfinitePagerValue;
  private TextView tvInfinitePagerValueNext;
  private InfiniteViewPager viewPager;
  
  private PagerAdapter buildAdapter(long paramLong)
  {
    this.pagerAdapter = new SleepHistoryMonthPageAdapter(getBaseActivity(), Long.valueOf(paramLong));
    return this.pagerAdapter;
  }
  
  private void initDataView()
  {
    long l = new Date().getTime();
    if (RefreshModelController.getInstance().localLatestSleepSession() != null) {
      l = RefreshModelController.getInstance().localLatestSleepSession().getStartTime().getTime();
    }
    this.viewPager.setAdapter(buildAdapter(l));
    Object localObject = this.pagerAdapter.getStringRepresentation((Long)this.pagerAdapter.getCurrentIndicator());
    this.tvInfinitePagerValue.setText((CharSequence)localObject);
    this.tvInfinitePagerValueNext.setText((CharSequence)localObject);
    localObject = GregorianCalendar.getInstance();
    updateDate(((Calendar)localObject).get(1), ((Calendar)localObject).get(2), ((Calendar)localObject).get(5));
  }
  
  private void mapGUI(View paramView)
  {
    this.viewPager = ((InfiniteViewPager)paramView.findViewById(2131100383));
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
  
  private void setupListeners()
  {
    this.tvInfinitePagerValue.setOnClickListener(this);
    this.ivInfinitePagerLeft.setOnClickListener(this);
    this.ivInfinitePagerRigth.setOnClickListener(this);
    this.bDatePickerCancel.setOnClickListener(this);
    this.bDatePickerDone.setOnClickListener(this);
    this.cvDatePickerCalendar.setOnDateChangeListener(this);
    this.viewPager.setOnInfinitePageChangeListener(this);
  }
  
  private void updateDate(int paramInt1, int paramInt2, int paramInt3)
  {
    Object localObject = new GregorianCalendar(paramInt1, paramInt2, paramInt3);
    String str = ((GregorianCalendar)localObject).getDisplayName(2, 2, Locale.getDefault());
    localObject = ((GregorianCalendar)localObject).getDisplayName(7, 2, Locale.getDefault());
    this.tvDatePickerDay.setText((CharSequence)localObject);
    this.tvDatePickerDate.setText(String.valueOf(paramInt3) + " " + str + " " + String.valueOf(paramInt1));
  }
  
  public String getStringRepresentation(long paramLong)
  {
    return new SimpleDateFormat("MMMM").format(Long.valueOf(paramLong));
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
      this.tvInfinitePagerValueNext.setText(this.pagerAdapter.getStringRepresentation(this.pagerAdapter.getPreviousIndicator()));
      this.viewPager.showPrev();
      continue;
      this.tvInfinitePagerValueNext.setText(this.pagerAdapter.getStringRepresentation(this.pagerAdapter.getNextIndicator()));
      this.viewPager.showNext();
      continue;
      getBaseActivity().setRollingBackground(this.ivBackgroundPicker);
      this.rlDatePicker.setClickable(true);
      this.rlDatePicker.setVisibility(0);
      this.rlDatePicker.setAnimation(AnimationUtils.loadAnimation(getActivity(), 2130968597));
      continue;
      this.rlDatePicker.setClickable(false);
      long l = this.cvDatePickerCalendar.getDate();
      paramView = RefreshModelController.getInstance().localSleepSessionInDay(new Date(l));
      if (paramView != null) {
        l = paramView.getStartTime().getTime();
      }
      this.viewPager.setAdapter(buildAdapter(l));
      this.tvInfinitePagerValue.setText(this.pagerAdapter.getStringRepresentation(Long.valueOf(l)));
      this.rlDatePicker.setVisibility(8);
      this.rlDatePicker.setAnimation(AnimationUtils.loadAnimation(getActivity(), 2130968588));
      continue;
      this.rlDatePicker.setClickable(false);
      this.cvDatePickerCalendar.setDate(((Long)this.pagerAdapter.getCurrentIndicator()).longValue());
      this.rlDatePicker.setVisibility(8);
      this.rlDatePicker.setAnimation(AnimationUtils.loadAnimation(getActivity(), 2130968588));
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903167, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    initDataView();
    return paramLayoutInflater;
  }
  
  public void onPageDisplayed(Object paramObject)
  {
    this.state = 0;
    this.tvInfinitePagerValue.setText(this.pagerAdapter.getStringRepresentation((Long)paramObject));
    this.tvInfinitePagerValueNext.setText(this.tvInfinitePagerValue.getText());
    Log.d("com.resmed.refresh.ui", "onPageDisplayed(" + new Date(((Long)paramObject).longValue()) + ") setting date to:" + this.tvInfinitePagerValue.getText());
  }
  
  public void onPageScrollStateChanged(int paramInt)
  {
    if ((this.state == 2) && (paramInt == 1)) {
      this.changeState = true;
    }
    if (paramInt != 1) {
      this.directionOld = 0;
    }
    this.state = paramInt;
  }
  
  public void onPageScrolled(Object paramObject, float paramFloat, int paramInt1, int paramInt2)
  {
    if ((this.directionOld != paramInt2) && (this.state == 1))
    {
      this.directionOld = paramInt2;
      if (!this.changeState) {
        break label93;
      }
      this.tvInfinitePagerValueNext.setText(this.pagerAdapter.getStringRepresentation((Long)this.pagerAdapter.getCurrentIndicator()));
    }
    for (;;)
    {
      this.changeState = false;
      float f = paramFloat;
      if (paramInt2 < 0) {
        f = 1.0F - paramFloat;
      }
      this.tvInfinitePagerValue.setAlpha(1.0F - f);
      this.tvInfinitePagerValueNext.setAlpha(f);
      return;
      label93:
      if (this.directionOld > 0) {
        this.tvInfinitePagerValueNext.setText(this.pagerAdapter.getStringRepresentation(this.pagerAdapter.getNextIndicator()));
      } else {
        this.tvInfinitePagerValueNext.setText(this.pagerAdapter.getStringRepresentation(this.pagerAdapter.getPreviousIndicator()));
      }
    }
  }
  
  public void onPageSelected(Object paramObject)
  {
    if (this.state != 0) {
      this.tvInfinitePagerValue.setText(this.tvInfinitePagerValueNext.getText());
    }
  }
  
  public void onSelectedDayChange(CalendarView paramCalendarView, int paramInt1, int paramInt2, int paramInt3)
  {
    updateDate(paramInt1, paramInt2, paramInt3);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SleepHistoryMonthFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
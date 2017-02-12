package com.resmed.refresh.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import com.resmed.refresh.ui.adapters.SPlusMentorPageAdapter;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.InfiniteViewPager;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SPlusMentorPagerFragment
  extends BaseFragment
  implements View.OnClickListener, InfiniteViewPager.OnInfinitePageChangeListener, CalendarView.OnDateChangeListener
{
  private Button bDatePickerCancel;
  private Button bDatePickerDone;
  private boolean changeState = false;
  private CalendarView cvDatePickerCalendar;
  private int directionOld;
  private ImageView ivBackgroundPicker;
  private ImageView ivInfinitePagerLeft;
  private ImageView ivInfinitePagerRigth;
  private SPlusMentorPageAdapter pagerAdapter;
  private View rlDatePicker;
  private int state;
  private TextView tvDatePickerDate;
  private TextView tvDatePickerDay;
  private TextView tvInfinitePagerValue;
  private TextView tvInfinitePagerValueNext;
  private InfiniteViewPager viewPagerSplusMentor;
  
  private void initDataView()
  {
    long l = getActivity().getIntent().getLongExtra("com.resmed.refresh.ui.uibase.app.splus_menotr_day", 0L);
    Calendar localCalendar = GregorianCalendar.getInstance();
    if (l > 0L) {
      localCalendar.setTimeInMillis(l);
    }
    localCalendar.set(11, 0);
    localCalendar.set(12, 0);
    localCalendar.set(13, 0);
    localCalendar.set(14, 0);
    this.pagerAdapter = new SPlusMentorPageAdapter(getActivity(), Long.valueOf(localCalendar.getTimeInMillis()));
    this.viewPagerSplusMentor.setAdapter(this.pagerAdapter);
    this.tvInfinitePagerValue.setText(this.pagerAdapter.getStringRepresentation((Long)this.pagerAdapter.getCurrentIndicator()));
    this.tvInfinitePagerValueNext.setText(this.pagerAdapter.getStringRepresentation((Long)this.pagerAdapter.getCurrentIndicator()));
    updateDate(localCalendar.get(1), localCalendar.get(2), localCalendar.get(5));
    this.tvInfinitePagerValue.setTypeface(this.akzidenzLight);
    this.tvDatePickerDay.setTypeface(this.akzidenzLight);
    this.tvDatePickerDate.setTypeface(this.akzidenzLight);
    this.bDatePickerCancel.setTypeface(this.akzidenzLight);
    this.bDatePickerDone.setTypeface(this.akzidenzLight);
  }
  
  private void mapGUI(View paramView)
  {
    this.viewPagerSplusMentor = ((InfiniteViewPager)paramView.findViewById(2131100564));
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
    this.viewPagerSplusMentor.setOnInfinitePageChangeListener(this);
    this.cvDatePickerCalendar.setOnDateChangeListener(this);
  }
  
  private void updateDate(int paramInt1, int paramInt2, int paramInt3)
  {
    Object localObject = new GregorianCalendar(paramInt1, paramInt2, paramInt3);
    String str = ((GregorianCalendar)localObject).getDisplayName(2, 2, Locale.getDefault());
    localObject = ((GregorianCalendar)localObject).getDisplayName(7, 2, Locale.getDefault());
    this.tvDatePickerDay.setText((CharSequence)localObject);
    this.tvDatePickerDate.setText(String.valueOf(paramInt3) + " " + str + " " + String.valueOf(paramInt1));
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
      this.viewPagerSplusMentor.showPrev();
      continue;
      this.tvInfinitePagerValueNext.setText(this.pagerAdapter.getStringRepresentation(this.pagerAdapter.getNextIndicator()));
      this.viewPagerSplusMentor.showNext();
      continue;
      getBaseActivity().setRollingBackground(this.ivBackgroundPicker);
      this.rlDatePicker.setClickable(true);
      this.rlDatePicker.setVisibility(0);
      this.rlDatePicker.setAnimation(AnimationUtils.loadAnimation(getActivity(), 2130968597));
      continue;
      this.rlDatePicker.setClickable(false);
      long l = this.cvDatePickerCalendar.getDate();
      this.viewPagerSplusMentor.setCurrentIndicator(Long.valueOf(l));
      this.tvInfinitePagerValue.setText(this.pagerAdapter.getStringRepresentation((Long)this.pagerAdapter.getCurrentIndicator()));
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
    paramLayoutInflater = paramLayoutInflater.inflate(2130903179, paramViewGroup, false);
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
        break label184;
      }
      this.tvInfinitePagerValueNext.setText(this.pagerAdapter.getStringRepresentation((Long)this.pagerAdapter.getCurrentIndicator()));
    }
    for (;;)
    {
      this.changeState = false;
      Log.d("com.resmed.refresh.ui", "onPageScrolled tvInfinitePagerValueNext = " + this.tvInfinitePagerValueNext.getText());
      Log.d("com.resmed.refresh.ui", "onPageScrolled directionOld=" + this.directionOld + " direction=" + paramInt2 + " state=" + this.state + " positionOffset=" + paramFloat);
      float f = paramFloat;
      if (paramInt2 < 0) {
        f = 1.0F - paramFloat;
      }
      this.tvInfinitePagerValue.setAlpha(1.0F - f);
      this.tvInfinitePagerValueNext.setAlpha(f);
      return;
      label184:
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
    Log.d("com.resmed.refresh.ui", "onPageSelected(" + new Date(((Long)paramObject).longValue()) + ") setting date to:" + this.tvInfinitePagerValue.getText());
  }
  
  public void onSelectedDayChange(CalendarView paramCalendarView, int paramInt1, int paramInt2, int paramInt3)
  {
    updateDate(paramInt1, paramInt2, paramInt3);
  }
  
  /* Error */
  public void setAdvicesRead(Date paramDate)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: invokestatic 370	com/resmed/refresh/model/RefreshModelController:getInstance	()Lcom/resmed/refresh/model/RefreshModelController;
    //   5: astore_3
    //   6: aload_3
    //   7: aload_1
    //   8: invokevirtual 374	com/resmed/refresh/model/RefreshModelController:localAdvicesForDate	(Ljava/util/Date;)Ljava/util/List;
    //   11: astore_1
    //   12: aload_1
    //   13: ifnull +15 -> 28
    //   16: iconst_0
    //   17: istore_2
    //   18: iload_2
    //   19: aload_1
    //   20: invokeinterface 379 1 0
    //   25: if_icmplt +10 -> 35
    //   28: aload_3
    //   29: invokevirtual 382	com/resmed/refresh/model/RefreshModelController:save	()V
    //   32: aload_0
    //   33: monitorexit
    //   34: return
    //   35: aload_1
    //   36: iload_2
    //   37: invokeinterface 385 2 0
    //   42: checkcast 387	com/resmed/refresh/model/RST_AdviceItem
    //   45: iconst_1
    //   46: invokevirtual 390	com/resmed/refresh/model/RST_AdviceItem:setRead	(Z)V
    //   49: aload_1
    //   50: iload_2
    //   51: invokeinterface 385 2 0
    //   56: checkcast 387	com/resmed/refresh/model/RST_AdviceItem
    //   59: invokevirtual 393	com/resmed/refresh/model/RST_AdviceItem:update	()V
    //   62: iinc 2 1
    //   65: goto -47 -> 18
    //   68: astore_1
    //   69: aload_0
    //   70: monitorexit
    //   71: aload_1
    //   72: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	73	0	this	SPlusMentorPagerFragment
    //   0	73	1	paramDate	Date
    //   17	46	2	i	int
    //   5	24	3	localRefreshModelController	RefreshModelController
    // Exception table:
    //   from	to	target	type
    //   2	12	68	finally
    //   18	28	68	finally
    //   28	32	68	finally
    //   35	62	68	finally
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SPlusMentorPagerFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
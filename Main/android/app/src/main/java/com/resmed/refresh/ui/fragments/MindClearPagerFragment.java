package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.resmed.refresh.model.mindclear.MindClearBase;
import com.resmed.refresh.model.mindclear.MindClearManager;
import com.resmed.refresh.model.mindclear.MindClearText;
import com.resmed.refresh.model.mindclear.MindClearVoice;
import com.resmed.refresh.ui.activity.MindClearActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.ActiveFragmentInterface;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class MindClearPagerFragment
  extends BaseFragment
  implements ViewPager.OnPageChangeListener, View.OnTouchListener
{
  private TextView date;
  private TextView dateNext;
  private FragmentManager fragmentManager;
  private int lastPosition;
  private float lastX;
  private ImageView left;
  private ViewPager mPager;
  private SampleAdapter mPagerAdapter;
  private ActiveFragmentInterface mcActiveFragmentInterface;
  private ImageView right;
  private float screenWidth;
  
  private PagerAdapter buildAdapter()
  {
    this.fragmentManager = getChildFragmentManager();
    List localList = MindClearManager.getInstance().getMindClearNotes();
    this.mPagerAdapter = new SampleAdapter(getActivity(), localList, this.fragmentManager);
    return this.mPagerAdapter;
  }
  
  private void mapGUI(View paramView)
  {
    this.mPager = ((ViewPager)paramView.findViewById(2131099939));
    this.left = ((ImageView)paramView.findViewById(2131099935));
    this.right = ((ImageView)paramView.findViewById(2131099936));
    this.date = ((TextView)paramView.findViewById(2131099937));
    this.dateNext = ((TextView)paramView.findViewById(2131099938));
    this.screenWidth = getResources().getDisplayMetrics().widthPixels;
  }
  
  private void setDateText()
  {
    if ((MindClearManager.getInstance().getMindClearNotes().get(MindClearManager.getInstance().getCurrentPosition()) instanceof MindClearText)) {
      this.date.setText(((MindClearText)MindClearManager.getInstance().getMindClearNotes().get(MindClearManager.getInstance().getCurrentPosition())).getDateTime());
    }
    if ((MindClearManager.getInstance().getMindClearNotes().get(MindClearManager.getInstance().getCurrentPosition()) instanceof MindClearVoice)) {
      this.date.setText(((MindClearVoice)MindClearManager.getInstance().getMindClearNotes().get(MindClearManager.getInstance().getCurrentPosition())).getDateTime());
    }
  }
  
  private void setupListener()
  {
    this.mPager.setOnPageChangeListener(this);
    this.mPager.setOnTouchListener(this);
    this.left.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (MindClearManager.getInstance().getCurrentPosition() > 0) {
          MindClearPagerFragment.this.mPager.setCurrentItem(MindClearManager.getInstance().getCurrentPosition() - 1);
        }
      }
    });
    this.right.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (MindClearManager.getInstance().getCurrentPosition() < MindClearManager.getInstance().getMindClearNotes().size() - 1) {
          MindClearPagerFragment.this.mPager.setCurrentItem(MindClearManager.getInstance().getCurrentPosition() + 1);
        }
      }
    });
  }
  
  public SampleAdapter getAdapter()
  {
    return this.mPagerAdapter;
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
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.lastPosition = MindClearManager.getInstance().getCurrentPosition();
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903133, paramViewGroup, false);
    ((MindClearBase)MindClearManager.getInstance().getMindClearNotes().get(MindClearManager.getInstance().getCurrentPosition())).setRead();
    MindClearManager.getInstance().storeDataIntoFilePreference();
    mapGUI(paramLayoutInflater);
    this.mPager.setAdapter(buildAdapter());
    return paramLayoutInflater;
  }
  
  public void onDetach()
  {
    super.onDetach();
    try
    {
      Field localField = Fragment.class.getDeclaredField("mChildFragmentManager");
      localField.setAccessible(true);
      localField.set(this, null);
      return;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      throw new RuntimeException(localNoSuchFieldException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      throw new RuntimeException(localIllegalAccessException);
    }
  }
  
  public void onPageScrollStateChanged(int paramInt)
  {
    if (paramInt == 0)
    {
      this.lastPosition = MindClearManager.getInstance().getCurrentPosition();
      this.date.setAlpha(1.0F);
      this.dateNext.setAlpha(0.0F);
    }
    for (;;)
    {
      return;
      if (paramInt == 2)
      {
        this.dateNext.setText(this.mPagerAdapter.getNote(this.lastPosition).getDateTime());
        this.date.setAlpha(1.0F);
        this.dateNext.setAlpha(0.0F);
      }
    }
  }
  
  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
    this.lastPosition = paramInt1;
  }
  
  public void onPageSelected(int paramInt)
  {
    MindClearManager.getInstance().setCurrentPosition(paramInt);
    this.date.setText(((MindClearBase)MindClearManager.getInstance().getMindClearNotes().get(MindClearManager.getInstance().getCurrentPosition())).getDateTime());
    this.dateNext.setText(((MindClearBase)MindClearManager.getInstance().getMindClearNotes().get(MindClearManager.getInstance().getCurrentPosition())).getDateTime());
    ((MindClearBase)MindClearManager.getInstance().getMindClearNotes().get(paramInt)).setRead();
    MindClearManager.getInstance().storeDataIntoFilePreference();
  }
  
  public void onResume()
  {
    super.onResume();
    this.mPager.setCurrentItem(this.lastPosition);
    setDateText();
    setupListener();
    ActiveFragmentInterface localActiveFragmentInterface = this.mcActiveFragmentInterface;
    ((MindClearActivity)getActivity()).getClass();
    localActiveFragmentInterface.notifyCurrentFragment(1);
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    if ((this.mPager == null) || (this.mPager.getAdapter().getCount() == 0)) {}
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
        this.date.getText().toString();
        if (paramMotionEvent.getX() > this.lastX) {}
        for (paramView = this.mPagerAdapter.getPrevTime();; paramView = this.mPagerAdapter.getNextTime())
        {
          float f2 = 1.0F - Math.abs(2.0F * (paramMotionEvent.getX() - this.lastX) / this.screenWidth);
          float f1 = f2;
          if (f2 < 0.0F) {
            f1 = 0.0F;
          }
          this.dateNext.setText(paramView);
          this.date.setAlpha(f1);
          this.dateNext.setAlpha(1.0F - f1);
          break;
        }
      case 1: 
      case 3: 
        this.lastX = 0.0F;
      }
    }
  }
  
  public void refreshAdapter()
  {
    this.mPager.getAdapter().notifyDataSetChanged();
  }
  
  public class SampleAdapter
    extends FragmentPagerAdapter
  {
    Context ctxt = null;
    private List<MindClearBase> list;
    private Vector<MindClearSwipeVoiceFragment> voiceFragmentList;
    
    public SampleAdapter(List<MindClearBase> paramList, FragmentManager paramFragmentManager)
    {
      super();
      this.ctxt = paramList;
      this.voiceFragmentList = new Vector();
      this.list = paramFragmentManager;
    }
    
    public int getCount()
    {
      return this.list.size();
    }
    
    public Fragment getItem(int paramInt)
    {
      Object localObject;
      if ((MindClearManager.getInstance().getMindClearNotes().get(paramInt) instanceof MindClearVoice))
      {
        localObject = new MindClearSwipeVoiceFragment(MindClearPagerFragment.this, ((MindClearVoice)this.list.get(paramInt)).getFileName());
        this.voiceFragmentList.add(localObject);
      }
      for (;;)
      {
        return (Fragment)localObject;
        localObject = new MindClearSwipeTextFragment(MindClearPagerFragment.this, ((MindClearBase)this.list.get(paramInt)).getDescription(), paramInt);
      }
    }
    
    public String getNextTime()
    {
      if (MindClearManager.getInstance().getMindClearNotes().size() > MindClearManager.getInstance().getCurrentPosition() + 1) {}
      for (String str = ((MindClearBase)MindClearManager.getInstance().getMindClearNotes().get(MindClearManager.getInstance().getCurrentPosition() + 1)).getDateTime();; str = ((MindClearBase)MindClearManager.getInstance().getMindClearNotes().get(MindClearManager.getInstance().getCurrentPosition())).getDateTime()) {
        return str;
      }
    }
    
    public MindClearBase getNote(int paramInt)
    {
      return (MindClearBase)this.list.get(paramInt);
    }
    
    public String getPrevTime()
    {
      if (MindClearManager.getInstance().getCurrentPosition() > 0) {}
      for (String str = ((MindClearBase)MindClearManager.getInstance().getMindClearNotes().get(MindClearManager.getInstance().getCurrentPosition() - 1)).getDateTime();; str = ((MindClearBase)MindClearManager.getInstance().getMindClearNotes().get(MindClearManager.getInstance().getCurrentPosition())).getDateTime()) {
        return str;
      }
    }
    
    public void resetAllAudio()
    {
      Iterator localIterator = this.voiceFragmentList.iterator();
      for (;;)
      {
        if (!localIterator.hasNext()) {
          return;
        }
        MindClearSwipeVoiceFragment localMindClearSwipeVoiceFragment = (MindClearSwipeVoiceFragment)localIterator.next();
        if (localMindClearSwipeVoiceFragment != null) {
          localMindClearSwipeVoiceFragment.resetMediaPlayer();
        }
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\MindClearPagerFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
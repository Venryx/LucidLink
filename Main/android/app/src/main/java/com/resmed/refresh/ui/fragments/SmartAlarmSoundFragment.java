package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.resmed.refresh.ui.activity.SmartAlarmActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.ActiveFragmentInterface;
import com.resmed.refresh.ui.utils.SmartAlarmDataManager;
import com.resmed.refresh.utils.audio.PlaySoundManager;
import com.resmed.refresh.utils.audio.SoundResources.SmartAlarmSound;
import java.util.ArrayList;
import java.util.List;

public class SmartAlarmSoundFragment
  extends BaseFragment
{
  private SmartAlarmAdapter adapter;
  private boolean isPlaying;
  private ListView lvSmartAlarmSelectSound;
  private ActiveFragmentInterface mcActiveFragmentInterface;
  private int selectedIndex;
  private SmartAlarmDataManager smartAlarmDataManager;
  private PlaySoundManager soundManager;
  
  private void selectSound(int paramInt)
  {
    Log.e("sound", "selectSound = " + paramInt);
    if ((paramInt == this.selectedIndex) && (this.isPlaying)) {}
    for (boolean bool = false;; bool = true)
    {
      this.isPlaying = bool;
      this.selectedIndex = paramInt;
      SoundResources.SmartAlarmSound localSmartAlarmSound = SoundResources.SmartAlarmSound.getSmartAlarmForPosition(paramInt);
      this.smartAlarmDataManager.setSoundValue(localSmartAlarmSound.getId());
      this.adapter.updateList(paramInt);
      this.soundManager.stopAudio();
      if (this.isPlaying) {
        this.soundManager.playAudio(localSmartAlarmSound.getAssetFileDescriptor());
      }
      return;
    }
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
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903176, paramViewGroup, false);
    this.smartAlarmDataManager = SmartAlarmDataManager.getInstance();
    this.lvSmartAlarmSelectSound = ((ListView)paramLayoutInflater.findViewById(2131100555));
    paramViewGroup = new ArrayList();
    for (int i = 0;; i++)
    {
      if (i >= SoundResources.SmartAlarmSound.values().length)
      {
        this.adapter = new SmartAlarmAdapter(paramViewGroup);
        this.lvSmartAlarmSelectSound.setAdapter(this.adapter);
        this.soundManager = PlaySoundManager.getInstance();
        this.lvSmartAlarmSelectSound.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
          public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
          {
            SmartAlarmSoundFragment.this.selectSound(paramAnonymousInt);
          }
        });
        return paramLayoutInflater;
      }
      paramViewGroup.add(Boolean.valueOf(false));
    }
  }
  
  public void onPause()
  {
    super.onPause();
    if (this.soundManager != null) {
      this.soundManager.stopAudio();
    }
    this.isPlaying = false;
  }
  
  public void onResume()
  {
    super.onResume();
    this.smartAlarmDataManager = SmartAlarmDataManager.getInstance();
    int j = this.smartAlarmDataManager.getSoundResource().getId();
    int i = j;
    if (j == -1) {
      i = 0;
    }
    this.selectedIndex = 0;
    for (j = 0;; j++)
    {
      if (j >= SoundResources.SmartAlarmSound.values().length) {}
      for (;;)
      {
        this.adapter.updateList(this.selectedIndex);
        ActiveFragmentInterface localActiveFragmentInterface = this.mcActiveFragmentInterface;
        ((SmartAlarmActivity)getActivity()).getClass();
        localActiveFragmentInterface.notifyCurrentFragment(2);
        return;
        if (i != SoundResources.SmartAlarmSound.getSmartAlarmForPosition(j).getId()) {
          break;
        }
        this.selectedIndex = j;
      }
    }
  }
  
  private class SmartAlarmAdapter
    extends BaseAdapter
  {
    List<Boolean> list;
    
    public SmartAlarmAdapter()
    {
      List localList;
      this.list = localList;
    }
    
    public int getCount()
    {
      return this.list.size();
    }
    
    public Object getItem(int paramInt)
    {
      return this.list.get(paramInt);
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      paramView = paramViewGroup;
      if (paramViewGroup == null)
      {
        paramView = SmartAlarmSoundFragment.this.getActivity().getLayoutInflater().inflate(2130903191, null);
        paramViewGroup = new SmartAlarmSoundFragment.ViewHolder();
        paramViewGroup.radio_group_icon = ((ImageView)paramView.findViewById(2131099883));
        paramViewGroup.radio_group_label = ((TextView)paramView.findViewById(2131099884));
        paramViewGroup.radio_group_button = ((ImageView)paramView.findViewById(2131099885));
        paramView.setTag(paramViewGroup);
      }
      Log.e("sound", "getView(" + paramInt + ") , selectedIndex = " + SmartAlarmSoundFragment.this.selectedIndex + "  isPlaying = " + SmartAlarmSoundFragment.this.isPlaying);
      paramViewGroup = (SmartAlarmSoundFragment.ViewHolder)paramView.getTag();
      paramViewGroup.radio_group_label.setText(SoundResources.SmartAlarmSound.getSmartAlarmForPosition(paramInt).getName());
      if (((Boolean)this.list.get(paramInt)).booleanValue())
      {
        paramViewGroup.radio_group_button.setImageResource(2130837867);
        if (SmartAlarmSoundFragment.this.isPlaying) {
          paramViewGroup.radio_group_icon.setImageResource(2130837886);
        }
      }
      for (;;)
      {
        paramView.invalidate();
        return paramView;
        paramViewGroup.radio_group_icon.setImageResource(2130837883);
        continue;
        paramViewGroup.radio_group_button.setImageResource(2130837863);
        paramViewGroup.radio_group_icon.setImageResource(2130837883);
      }
    }
    
    public void updateList(int paramInt)
    {
      int i = 0;
      if (i >= this.list.size())
      {
        notifyDataSetChanged();
        return;
      }
      List localList = this.list;
      if (paramInt == i) {}
      for (boolean bool = true;; bool = false)
      {
        localList.set(i, Boolean.valueOf(bool));
        i++;
        break;
      }
    }
  }
  
  static class ViewHolder
  {
    public ImageView radio_group_button;
    public ImageView radio_group_icon;
    public TextView radio_group_label;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SmartAlarmSoundFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
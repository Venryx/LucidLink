package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.resmed.refresh.model.mindclear.MindClearBase;
import com.resmed.refresh.model.mindclear.MindClearManager;
import com.resmed.refresh.model.mindclear.MindClearText;
import com.resmed.refresh.model.mindclear.MindClearVoice;
import com.resmed.refresh.ui.activity.MindClearActivity;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.ui.utils.ActiveFragmentInterface;
import com.resmed.refresh.utils.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MindClearFragment
  extends BaseFragment
  implements View.OnClickListener
{
  private ClearMindArrayAdapter clearMindAdapter;
  private AdapterView.OnItemClickListener displayItemListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
    {
      MindClearManager.getInstance().setCurrentPosition(paramAnonymousInt);
      MindClearFragment.this.mindClearBtn.performListClick();
    }
  };
  private AdapterView.OnItemClickListener editClickistener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
    {
      MindClearFragment.this.clearMindAdapter.selectItem(paramAnonymousInt);
    }
  };
  private AdapterView.OnItemLongClickListener editLongClickistener = new AdapterView.OnItemLongClickListener()
  {
    public boolean onItemLongClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
    {
      MindClearFragment.this.clearMindAdapter.selectItem(paramAnonymousInt);
      return true;
    }
  };
  private ListView listView;
  private ActiveFragmentInterface mcActiveFragmentInterface;
  private MindClearBtn mindClearBtn;
  private Button recordVoice;
  private Button typeText;
  
  private void cancelEdit()
  {
    this.clearMindAdapter.resetSelectedItems();
    this.clearMindAdapter.notifyDataSetChanged();
  }
  
  private void mapGUI(View paramView)
  {
    this.listView = ((ListView)paramView.findViewById(2131099928));
    this.recordVoice = ((Button)paramView.findViewById(2131099930));
    this.typeText = ((Button)paramView.findViewById(2131099931));
  }
  
  private void setupListener()
  {
    this.listView.setOnItemClickListener(this.displayItemListener);
    this.listView.setOnItemLongClickListener(this.editLongClickistener);
    this.recordVoice.setOnClickListener(this);
    this.typeText.setOnClickListener(this);
  }
  
  public void deleteItems()
  {
    List localList1 = this.clearMindAdapter.getList();
    List localList2 = this.clearMindAdapter.getSelectedItems();
    Collections.sort(localList2);
    int i = localList2.size() - 1;
    for (;;)
    {
      if (i < 0)
      {
        this.clearMindAdapter.resetSelectedItems();
        this.clearMindAdapter.updateList(MindClearManager.getInstance().getMindClearNotes());
        this.listView.setOnItemClickListener(this.displayItemListener);
        return;
      }
      int j = ((Integer)localList2.get(i)).intValue();
      try
      {
        if ((localList1.get(j) instanceof MindClearVoice)) {
          ((MindClearVoice)localList1.get(j)).deleteFileAudio();
        }
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>("position to delete -> ");
        Log.i("com.resmed.refresh.mindClear", j);
        MindClearManager.getInstance().deleteElement(j);
        i--;
      }
      catch (Exception localException)
      {
        for (;;)
        {
          localException.printStackTrace();
        }
      }
    }
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.mindClearBtn = ((MindClearBtn)paramActivity);
      this.mcActiveFragmentInterface = ((ActiveFragmentInterface)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new ClassCastException(paramActivity.toString() + " ...you must implement SmartAlarmBtn from your Activity ;-) !");
    }
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    }
    for (;;)
    {
      return;
      cancelEdit();
      this.mindClearBtn.goToMindClearVoiceFragment();
      continue;
      cancelEdit();
      this.mindClearBtn.goToMindClearTypeFragment();
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramViewGroup = paramLayoutInflater.inflate(2130903131, paramViewGroup, false);
    mapGUI(paramViewGroup);
    paramLayoutInflater = paramLayoutInflater.inflate(2130903125, null, false);
    this.listView.addFooterView(paramLayoutInflater, null, false);
    this.clearMindAdapter = new ClearMindArrayAdapter(getActivity(), 2130903104, MindClearManager.getInstance().getMindClearNotes());
    this.listView.setAdapter(this.clearMindAdapter);
    setupListener();
    getBaseActivity().hideRightButton();
    return paramViewGroup;
  }
  
  public void onResume()
  {
    super.onResume();
    this.clearMindAdapter.updateList(MindClearManager.getInstance().getMindClearNotes());
    this.clearMindAdapter.notifyDataSetChanged();
    ActiveFragmentInterface localActiveFragmentInterface = this.mcActiveFragmentInterface;
    ((MindClearActivity)getActivity()).getClass();
    localActiveFragmentInterface.notifyCurrentFragment(0);
  }
  
  public void refreshList()
  {
    this.clearMindAdapter.notifyDataSetChanged();
  }
  
  private class ClearMindArrayAdapter
    extends ArrayAdapter<MindClearBase>
  {
    private List<Integer> checkedItems;
    private List<MindClearBase> clearMindList;
    private final Context context;
    
    public ClearMindArrayAdapter(int paramInt, List<MindClearBase> paramList)
    {
      super(paramList, localList);
      this.context = paramInt;
      this.clearMindList = localList;
      this.checkedItems = new ArrayList();
    }
    
    public int getCount()
    {
      return this.clearMindList.size();
    }
    
    public List<MindClearBase> getList()
    {
      return this.clearMindList;
    }
    
    public List<Integer> getSelectedItems()
    {
      return this.checkedItems;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      int i = getCount();
      Log.i("com.resmed.refresh.mindClear", "viewPosition = " + paramInt + "    posMindClearManager = " + (i - paramInt - 1));
      paramViewGroup = paramView;
      if (paramView == null)
      {
        paramViewGroup = ((LayoutInflater)this.context.getSystemService("layout_inflater")).inflate(2130903104, null);
        paramView = new ViewHolder(null);
        paramView.timeText = ((TextView)paramViewGroup.findViewById(2131099855));
        paramView.previewText = ((TextView)paramViewGroup.findViewById(2131099856));
        paramView.type = ((ImageView)paramViewGroup.findViewById(2131099853));
        paramView.rlMindClearList = paramViewGroup.findViewById(2131099852);
        paramViewGroup.setTag(paramView);
      }
      ViewHolder localViewHolder = (ViewHolder)paramViewGroup.getTag();
      if (this.checkedItems.contains(new Integer(paramInt))) {
        localViewHolder.rlMindClearList.setBackgroundColor(2131296310);
      }
      for (;;)
      {
        Log.i("com.resmed.refresh.mindClear", "checkedItems.size=" + this.checkedItems.size());
        paramView = null;
        if ((this.clearMindList.get(paramInt) instanceof MindClearText))
        {
          paramView = ((MindClearText)this.clearMindList.get(paramInt)).getDateTime();
          localViewHolder.type.setImageResource(2130837653);
        }
        if ((this.clearMindList.get(paramInt) instanceof MindClearVoice))
        {
          paramView = ((MindClearVoice)this.clearMindList.get(paramInt)).getDateTime();
          localViewHolder.type.setImageResource(2130837650);
        }
        localViewHolder.timeText.setText(paramView);
        paramView = ((MindClearBase)this.clearMindList.get(paramInt)).getDescription();
        localViewHolder.previewText.setText(paramView);
        return paramViewGroup;
        localViewHolder.rlMindClearList.setBackgroundColor(17170445);
      }
    }
    
    public boolean isEmpty()
    {
      return false;
    }
    
    public void resetSelectedItems()
    {
      this.checkedItems = new ArrayList();
    }
    
    public void selectItem(int paramInt)
    {
      Log.i("com.resmed.refresh.mindClear", "selectItem = " + paramInt);
      if (this.checkedItems.contains(new Integer(paramInt)))
      {
        this.checkedItems.remove(new Integer(paramInt));
        if (!this.checkedItems.isEmpty()) {
          break label125;
        }
        MindClearFragment.this.getBaseActivity().hideRightButton();
        MindClearFragment.this.listView.setOnItemClickListener(MindClearFragment.this.displayItemListener);
      }
      for (;;)
      {
        notifyDataSetChanged();
        return;
        this.checkedItems.add(new Integer(paramInt));
        break;
        label125:
        MindClearFragment.this.getBaseActivity().showRightButton(2130837773);
        MindClearFragment.this.listView.setOnItemClickListener(MindClearFragment.this.editClickistener);
      }
    }
    
    public void updateList(List<MindClearBase> paramList)
    {
      this.clearMindList = paramList;
      notifyDataSetChanged();
    }
    
    private class ViewHolder
    {
      public TextView previewText;
      public View rlMindClearList;
      public TextView timeText;
      public ImageView type;
      
      private ViewHolder() {}
    }
  }
  
  public static abstract interface MindClearBtn
  {
    public abstract void goToMindClearTypeFragment();
    
    public abstract void goToMindClearVoiceFragment();
    
    public abstract void performListClick();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\MindClearFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
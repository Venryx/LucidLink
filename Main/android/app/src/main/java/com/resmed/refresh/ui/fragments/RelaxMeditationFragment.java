package com.resmed.refresh.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.resmed.refresh.ui.adapters.MeditationAdapter;
import com.resmed.refresh.ui.customview.HorizontalListView;
import com.resmed.refresh.ui.uibase.base.BaseFragment;
import com.resmed.refresh.utils.DefaultRelaxMeditation;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RelaxPlayerCallback;
import com.resmed.refresh.utils.audio.SoundResources.RelaxSound;

public class RelaxMeditationFragment
  extends BaseFragment
  implements HorizontalListView.MeditationClickCallback, RelaxPlayerCallback
{
  private MeditationAdapter adapter;
  private boolean isPlaying;
  private HorizontalListView listView;
  private View.OnClickListener play = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      RelaxMeditationFragment.this.startPlayer();
    }
  };
  DefaultRelaxMeditation relaxMeditation;
  private int selectedIndex;
  private TextView startMeditation;
  private View.OnClickListener stop = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      RelaxMeditationFragment.this.stopPlayer();
    }
  };
  
  private void startPlayer()
  {
    Object localObject = SoundResources.RelaxSound.getRelaxForPosition(this.selectedIndex);
    try
    {
      this.relaxMeditation.startPlayer((SoundResources.RelaxSound)localObject);
      this.startMeditation.setText(getActivity().getResources().getString(2131165428));
      this.isPlaying = true;
      this.startMeditation.setOnClickListener(null);
      localObject = new android/os/Handler;
      ((Handler)localObject).<init>();
      Runnable local5 = new com/resmed/refresh/ui/fragments/RelaxMeditationFragment$5;
      local5.<init>(this);
      ((Handler)localObject).post(local5);
      return;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        stopPlayer();
      }
    }
  }
  
  private void stopPlayer()
  {
    this.relaxMeditation.stopPlayer();
    this.startMeditation.setText(getActivity().getResources().getString(2131165427));
    this.isPlaying = false;
    this.startMeditation.setOnClickListener(null);
    new Handler().post(new Runnable()
    {
      public void run()
      {
        if (RelaxMeditationFragment.this.startMeditation != null) {
          RelaxMeditationFragment.this.startMeditation.setOnClickListener(RelaxMeditationFragment.this.play);
        }
      }
    });
  }
  
  public void meditationClick(int paramInt)
  {
    if (paramInt != this.selectedIndex)
    {
      stopPlayer();
      this.startMeditation.setOnClickListener(this.play);
    }
    this.selectedIndex = paramInt;
    this.adapter.selectItem(paramInt);
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903157, paramViewGroup, false);
    this.startMeditation = ((TextView)paramLayoutInflater.findViewById(2131100341));
    this.listView = ((HorizontalListView)paramLayoutInflater.findViewById(2131100340));
    this.selectedIndex = 0;
    this.relaxMeditation = new DefaultRelaxMeditation(getActivity(), this);
    this.listView.setCallback(this);
    this.adapter = new MeditationAdapter(this.selectedIndex);
    this.listView.setAdapter(this.adapter);
    new Handler().post(new Runnable()
    {
      public void run()
      {
        if (RelaxMeditationFragment.this.startMeditation != null) {
          RelaxMeditationFragment.this.startMeditation.setOnClickListener(RelaxMeditationFragment.this.play);
        }
      }
    });
    return paramLayoutInflater;
  }
  
  public void onDestroy()
  {
    super.onDestroy();
    Log.e("com.resmed.refresh.relax", "onDestroy");
    if (this.relaxMeditation != null) {
      stopPlayer();
    }
    this.isPlaying = false;
  }
  
  public void onPause()
  {
    super.onPause();
  }
  
  public void onRelaxComplete()
  {
    this.startMeditation.setText(getActivity().getResources().getString(2131165427));
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\RelaxMeditationFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
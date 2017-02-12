package com.resmed.refresh.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import com.resmed.refresh.ui.activity.GuideActivty;
import com.resmed.refresh.ui.activity.LoginActivty;
import com.resmed.refresh.ui.activity.SignupActivty;
import com.resmed.refresh.ui.uibase.base.BaseFragment;

public class LandingFragment
  extends BaseFragment
  implements View.OnClickListener
{
  private static final int ANIMATION_TIME = 500;
  private Button bLandingLogin;
  private Button bLandingSignup;
  private Animation.AnimationListener listener = new Animation.AnimationListener()
  {
    public void onAnimationEnd(Animation paramAnonymousAnimation)
    {
      if ((LandingFragment.this.getActivity() != null) && (LandingFragment.this.getActivity().isFinishing())) {
        LandingFragment.this.setItemsVisibility(4);
      }
    }
    
    public void onAnimationRepeat(Animation paramAnonymousAnimation) {}
    
    public void onAnimationStart(Animation paramAnonymousAnimation) {}
  };
  private LinearLayout llLandingGuide;
  private LinearLayout llLandingTitle;
  
  private void mapGUI(View paramView)
  {
    this.llLandingTitle = ((LinearLayout)paramView.findViewById(2131100027));
    this.bLandingSignup = ((Button)paramView.findViewById(2131100028));
    this.bLandingLogin = ((Button)paramView.findViewById(2131100029));
    this.llLandingGuide = ((LinearLayout)paramView.findViewById(2131100030));
    setItemsVisibility(4);
    this.bLandingSignup.setTypeface(this.akzidenzLight);
    this.bLandingLogin.setTypeface(this.akzidenzLight);
  }
  
  private void setItemsVisibility(int paramInt)
  {
    this.llLandingTitle.setVisibility(paramInt);
    this.bLandingSignup.setVisibility(paramInt);
    this.bLandingLogin.setVisibility(paramInt);
    this.llLandingGuide.setVisibility(paramInt);
  }
  
  private void setupListeners()
  {
    this.bLandingSignup.setOnClickListener(this);
    this.bLandingLogin.setOnClickListener(this);
    this.llLandingGuide.setOnClickListener(this);
  }
  
  private void startAnimation()
  {
    Animation localAnimation = AnimationUtils.loadAnimation(getActivity(), 2130968582);
    localAnimation.setDuration(500L);
    this.llLandingTitle.startAnimation(localAnimation);
    localAnimation = AnimationUtils.loadAnimation(getActivity(), 2130968582);
    localAnimation.setDuration(500L);
    localAnimation.setStartOffset(300L);
    this.bLandingSignup.startAnimation(localAnimation);
    localAnimation = AnimationUtils.loadAnimation(getActivity(), 2130968582);
    localAnimation.setDuration(500L);
    localAnimation.setStartOffset(600L);
    this.bLandingLogin.startAnimation(localAnimation);
    localAnimation = AnimationUtils.loadAnimation(getActivity(), 2130968582);
    localAnimation.setAnimationListener(this.listener);
    localAnimation.setDuration(500L);
    localAnimation.setStartOffset(900L);
    this.llLandingGuide.startAnimation(localAnimation);
    setItemsVisibility(0);
  }
  
  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    }
    for (;;)
    {
      return;
      startActivity(new Intent(getActivity(), SignupActivty.class));
      continue;
      startActivity(new Intent(getActivity(), LoginActivty.class));
      continue;
      paramView = new Intent(getActivity(), GuideActivty.class);
      paramView.putExtra("com.resmed.refresh.ui.uibase.app.came_from_landing", true);
      startActivity(paramView);
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    paramLayoutInflater = paramLayoutInflater.inflate(2130903146, paramViewGroup, false);
    mapGUI(paramLayoutInflater);
    setupListeners();
    setItemsVisibility(4);
    super.onResume();
    new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
    {
      public void run()
      {
        LandingFragment.this.startAnimation();
      }
    }, 200L);
    return paramLayoutInflater;
  }
  
  public void onStop()
  {
    super.onStop();
    this.llLandingTitle.clearAnimation();
    this.bLandingSignup.clearAnimation();
    this.bLandingLogin.clearAnimation();
    this.llLandingGuide.clearAnimation();
    setItemsVisibility(0);
  }
}
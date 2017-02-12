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
import android.widget.ImageView;
import android.widget.TextView;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.activity.HomeActivity;
import com.resmed.refresh.ui.customview.SparkleLogoView;
import com.resmed.refresh.ui.uibase.base.BaseFragment;

public class SplashFragment
  extends BaseFragment
{
  private static final int ANIMATION_FADE_IN = 500;
  private static final int DELAY_START = 400;
  private ImageView ivSplashResMed;
  private SparkleLogoView sparkleLogoView;
  private TextView tvSplashHeader;
  
  public View onCreateView(final LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130903180, paramViewGroup, false);
    localView.findViewById(2131099770).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SplashFragment.this.startApplication();
      }
    });
    paramLayoutInflater = localView.findViewById(2131100566);
    float f = getResources().getDimension(2131034256);
    paramLayoutInflater.setX(f);
    paramLayoutInflater.setY(f / 3.0F);
    this.ivSplashResMed = ((ImageView)localView.findViewById(2131100567));
    this.tvSplashHeader = ((TextView)localView.findViewById(2131100568));
    this.sparkleLogoView = ((SparkleLogoView)localView.findViewById(2131100565));
    paramBundle = (TextView)localView.findViewById(2131100569);
    this.sparkleLogoView.setOnAnimationEndsListener(new SparkleLogoView.OnAnimationEndsListener()
    {
      public void onAnimationEnds()
      {
        if (SplashFragment.this.getActivity() != null)
        {
          Animation localAnimation = AnimationUtils.loadAnimation(SplashFragment.this.getActivity(), 2130968582);
          localAnimation.setDuration(500L);
          localAnimation.setAnimationListener(new Animation.AnimationListener()
          {
            public void onAnimationEnd(Animation paramAnonymous2Animation)
            {
              new Handler().postDelayed(new Runnable()
              {
                public void run()
                {
                  if (SplashFragment.this.getActivity() != null) {
                    SplashFragment.this.startApplication();
                  }
                }
              }, 400L);
            }
            
            public void onAnimationRepeat(Animation paramAnonymous2Animation) {}
            
            public void onAnimationStart(Animation paramAnonymous2Animation) {}
          });
          paramLayoutInflater.startAnimation(localAnimation);
          paramLayoutInflater.setVisibility(0);
        }
      }
    });
    paramLayoutInflater = new Intent("ACTION_RESMED_CONNECTION_STATUS");
    getActivity().removeStickyBroadcast(paramLayoutInflater);
    paramLayoutInflater = "";
    try
    {
      paramViewGroup = getActivity().getPackageName();
      paramViewGroup = getActivity().getPackageManager().getPackageInfo(paramViewGroup, 0).versionName;
      paramLayoutInflater = paramViewGroup;
    }
    catch (Exception paramViewGroup)
    {
      for (;;)
      {
        paramViewGroup.printStackTrace();
      }
    }
    paramBundle.setText(paramLayoutInflater);
    new Handler().post(new Runnable()
    {
      public void run()
      {
        RefreshModelController.getInstance();
      }
    });
    return localView;
  }
  
  public void onResume()
  {
    super.onResume();
    new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
    {
      public void run()
      {
        SplashFragment.this.tvSplashHeader.setVisibility(0);
        SplashFragment.this.ivSplashResMed.setVisibility(0);
        SplashFragment.this.sparkleLogoView.startAnimation();
      }
    }, 150L);
  }
  
  public void startApplication()
  {
    Intent localIntent = new Intent(getActivity(), HomeActivity.class);
    localIntent.setFlags(268468224);
    getActivity().startActivity(localIntent);
    getActivity().finish();
    getActivity().overridePendingTransition(2130968582, 2130968583);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\SplashFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
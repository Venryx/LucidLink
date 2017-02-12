package com.resmed.refresh.ui.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.resmed.refresh.utils.Log;

public class RefreshDialogFragment
  extends DialogFragment
{
  private View currentView;
  private boolean fullScreen = false;
  private View.OnTouchListener noCancelableListener = new View.OnTouchListener()
  {
    public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
    {
      return true;
    }
  };
  
  public static RefreshDialogFragment newInstance()
  {
    RefreshDialogFragment localRefreshDialogFragment = new RefreshDialogFragment();
    Log.d("com.resmed.refresh.ui", "newInstance");
    Bundle localBundle = new Bundle();
    localBundle.putBoolean("fullScreen", false);
    localRefreshDialogFragment.setArguments(localBundle);
    return localRefreshDialogFragment;
  }
  
  public static RefreshDialogFragment newInstance(boolean paramBoolean)
  {
    RefreshDialogFragment localRefreshDialogFragment = new RefreshDialogFragment();
    Log.d("com.resmed.refresh.ui", "newInstance");
    Bundle localBundle = new Bundle();
    localBundle.putBoolean("fullScreen", paramBoolean);
    localRefreshDialogFragment.setArguments(localBundle);
    return localRefreshDialogFragment;
  }
  
  public void onCancel(DialogInterface paramDialogInterface)
  {
    super.onCancel(paramDialogInterface);
    Log.d("com.resmed.refresh.dialog", "RefreshDialogFragment onCancel");
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.fullScreen = getArguments().getBoolean("fullScreen");
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    Bundle localBundle = null;
    if (this.currentView != null) {}
    try
    {
      paramBundle = this.currentView.getParent();
      if (paramBundle != null) {
        ((ViewGroup)paramBundle).removeAllViews();
      }
    }
    catch (Exception paramBundle)
    {
      for (;;)
      {
        paramBundle.printStackTrace();
        continue;
        paramBundle = new Dialog(getActivity(), 16973840);
      }
    }
    if (this.fullScreen)
    {
      paramBundle = new Dialog(getActivity(), 16973841);
      paramBundle.addContentView(this.currentView, new ViewGroup.LayoutParams(-1, -1));
      localBundle = paramBundle;
      if (!isCancelable())
      {
        this.currentView.setOnTouchListener(this.noCancelableListener);
        this.currentView.findViewById(2131099909).setOnTouchListener(this.noCancelableListener);
        localBundle = paramBundle;
      }
      return localBundle;
    }
  }
  
  public void onDestroyView()
  {
    super.onDestroyView();
    Log.d("com.resmed.refresh.dialog", "RefreshDialogFragment onCancel");
  }
  
  public void onDismiss(DialogInterface paramDialogInterface)
  {
    super.onDismiss(paramDialogInterface);
    Log.d("com.resmed.refresh.dialog", "RefreshDialogFragment onDismiss");
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    Log.d("com.resmed.refresh.dialog", "RefreshDialogFragment onSaveInstanceState");
  }
  
  public void onStart()
  {
    super.onStart();
    Log.d("com.resmed.refresh.dialog", "RefreshDialogFragment onStart");
  }
  
  public void onStop()
  {
    super.onStop();
    Log.d("com.resmed.refresh.dialog", "RefreshDialogFragment onStop");
  }
  
  public void setView(View paramView)
  {
    this.currentView = paramView;
  }
  
  public void updateProgress(int paramInt)
  {
    if (this.currentView != null)
    {
      ProgressBar localProgressBar = (ProgressBar)this.currentView.findViewById(2131099954);
      if (localProgressBar != null)
      {
        localProgressBar.setProgress(paramInt);
        if ((paramInt >= 100) && (isResumed()))
        {
          Log.d("com.resmed.refresh.dialog", "RefreshDialogFragment updateProgress dismissAllowingStateLoss");
          dismiss();
        }
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\RefreshDialogFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
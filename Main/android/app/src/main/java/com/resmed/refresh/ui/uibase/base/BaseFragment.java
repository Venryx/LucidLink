package com.resmed.refresh.ui.uibase.base;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class BaseFragment
  extends Fragment
{
  protected Typeface akzidenzBold;
  protected Typeface akzidenzLight;
  
  public BaseActivity getBaseActivity()
  {
    return (BaseActivity)getActivity();
  }
  
  public void handleBackPressed()
  {
    if (getBaseActivity() != null) {
      getBaseActivity().hideRightButton();
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.akzidenzBold = Typeface.createFromAsset(getActivity().getAssets(), "AkzidenzGroteskBE-Bold.otf");
    this.akzidenzLight = Typeface.createFromAsset(getActivity().getAssets(), "AkzidenzGroteskBE-Light.otf");
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
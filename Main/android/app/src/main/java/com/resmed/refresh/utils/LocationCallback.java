package com.resmed.refresh.utils;

import android.location.Location;

public abstract interface LocationCallback
{
  public abstract void allProviderAreDisabled();
  
  public abstract void onLocationUpdateReceived(Location paramLocation);
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\utils\LocationCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
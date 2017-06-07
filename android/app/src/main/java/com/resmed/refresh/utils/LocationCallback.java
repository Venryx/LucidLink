package com.resmed.refresh.utils;

import android.location.Location;

public abstract interface LocationCallback
{
  public abstract void allProviderAreDisabled();
  
  public abstract void onLocationUpdateReceived(Location paramLocation);
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
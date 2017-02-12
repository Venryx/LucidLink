package com.resmed.refresh.model;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.gms.maps.model.LatLng;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.AppFileLog;
import com.resmed.refresh.utils.LocationCallback;
import com.resmed.refresh.utils.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RST_LocationManager
{
  private static final long RST_MIN_DISTANCE_UPDATES = 1L;
  private static final long RST_MIN_TIME_UPDATES = 3600000L;
  private static final int TIME_NEW = 300000;
  private static List<LocationCallback> locationCallbackList;
  private String bestProvider;
  private Context context = RefreshApplication.getInstance();
  private Location currentLocation;
  private RST_LocationListener locationListener = new RST_LocationListener(null);
  private LocationManager locationManager = (LocationManager)this.context.getSystemService("location");
  
  public RST_LocationManager()
  {
    locationCallbackList = new ArrayList();
    Criteria localCriteria = new Criteria();
    this.bestProvider = this.locationManager.getBestProvider(localCriteria, false);
    if (this.bestProvider == null) {
      if (!this.locationManager.isProviderEnabled("gps")) {
        break label157;
      }
    }
    label157:
    for (this.bestProvider = "gps";; this.bestProvider = "network")
    {
      this.currentLocation = getBestLocation();
      if (this.currentLocation != null) {
        AppFileLog.addTrace("Location - LocationManager currentLocation(" + this.currentLocation.getLatitude() + "," + this.currentLocation.getLongitude() + ")");
      }
      return;
    }
  }
  
  private Location getBestLocation()
  {
    Object localObject = getLocationByProvider("gps");
    Location localLocation = getLocationByProvider("network");
    if (localObject == null)
    {
      Log.d("com.resmed.refresh.location", "No GPS Location available.");
      localObject = localLocation;
    }
    for (;;)
    {
      return (Location)localObject;
      if (localLocation == null)
      {
        Log.d("com.resmed.refresh.location", "No Network Location available");
      }
      else
      {
        long l = System.currentTimeMillis() - 86400000L;
        int i;
        if (((Location)localObject).getTime() < l)
        {
          i = 1;
          label72:
          if (localLocation.getTime() >= l) {
            break label104;
          }
        }
        label104:
        for (int j = 1;; j = 0)
        {
          if (i != 0) {
            break label109;
          }
          Log.d("com.resmed.refresh.location", "Returning current GPS Location");
          break;
          i = 0;
          break label72;
        }
        label109:
        if (j == 0)
        {
          Log.d("com.resmed.refresh.location", "GPS is old, Network is current, returning network");
          localObject = localLocation;
        }
        else if (((Location)localObject).getTime() > localLocation.getTime())
        {
          Log.d("com.resmed.refresh.location", "Both are old, returning gps(newer)");
        }
        else
        {
          Log.d("com.resmed.refresh.location", "Both are old, returning network(newer)");
          localObject = localLocation;
        }
      }
    }
  }
  
  private Location getLocationByProvider(String paramString)
  {
    Object localObject3 = null;
    if (!this.locationManager.isProviderEnabled(paramString)) {
      paramString = null;
    }
    for (;;)
    {
      return paramString;
      LocationManager localLocationManager = (LocationManager)RefreshApplication.getInstance().getApplicationContext().getSystemService("location");
      Object localObject1 = localObject3;
      try
      {
        if (localLocationManager.isProviderEnabled(paramString)) {
          localObject1 = localLocationManager.getLastKnownLocation(paramString);
        }
        paramString = (String)localObject1;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        for (;;)
        {
          Log.d("com.resmed.refresh.location", "Cannot acces Provider " + paramString);
          Object localObject2 = localObject3;
        }
      }
    }
  }
  
  private boolean isPresent(LocationCallback paramLocationCallback)
  {
    boolean bool = false;
    Iterator localIterator = locationCallbackList.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return bool;
      }
      if (paramLocationCallback == (LocationCallback)localIterator.next()) {
        bool = true;
      }
    }
  }
  
  private void startLocationUpdates()
  {
    Log.d("com.resmed.refresh.location", "startLocationUpdates");
    if (this.currentLocation == null)
    {
      Location localLocation = this.locationManager.getLastKnownLocation(this.bestProvider);
      if (localLocation != null) {
        updateLocation(localLocation);
      }
    }
    try
    {
      this.locationManager.requestLocationUpdates(this.bestProvider, 3600000L, 1.0F, this.locationListener);
      AppFileLog.addTrace("Location - LocationManager locationManager.requestLocationUpdates");
      return;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        localException.printStackTrace();
      }
    }
  }
  
  private void stopLocationUpdates()
  {
    Log.d("com.resmed.refresh.location", "stop location updates");
    if (this.locationManager != null) {
      this.locationManager.removeUpdates(this.locationListener);
    }
  }
  
  private void updateLocation(Location paramLocation)
  {
    if (paramLocation == null) {}
    for (;;)
    {
      return;
      this.currentLocation = paramLocation;
      RefreshModelController.getInstance().updateLocationUser(this.currentLocation);
      AppFileLog.addTrace("Location - LocationManager location(" + paramLocation.getLatitude() + "," + paramLocation.getLongitude() + ")");
      Log.d("com.resmed.refresh.location", "updateLocation Lat:" + paramLocation.getLatitude() + "  Long:" + paramLocation.getLongitude());
      Iterator localIterator = locationCallbackList.iterator();
      while (localIterator.hasNext())
      {
        LocationCallback localLocationCallback = (LocationCallback)localIterator.next();
        if (localLocationCallback != null) {
          localLocationCallback.onLocationUpdateReceived(paramLocation);
        }
      }
    }
  }
  
  public LatLng getLastLocation()
  {
    Object localObject = getBestLocation();
    if (localObject != null) {
      updateLocation((Location)localObject);
    }
    if (this.currentLocation == null) {}
    for (localObject = new LatLng(-1.0D, -1.0D);; localObject = new LatLng(this.currentLocation.getLatitude(), this.currentLocation.getLongitude())) {
      return (LatLng)localObject;
    }
  }
  
  public Location registerCallback(LocationCallback paramLocationCallback)
  {
    if (!isPresent(paramLocationCallback)) {
      locationCallbackList.add(paramLocationCallback);
    }
    return this.currentLocation;
  }
  
  public void setLocationUpdatesListener(boolean paramBoolean)
  {
    if (paramBoolean) {
      startLocationUpdates();
    }
    for (;;)
    {
      return;
      stopLocationUpdates();
    }
  }
  
  public void unregisterCallback(LocationCallback paramLocationCallback)
  {
    locationCallbackList.remove(paramLocationCallback);
  }
  
  public void updateLocation()
  {
    startLocationUpdates();
  }
  
  private class RST_LocationListener
    implements LocationListener
  {
    private RST_LocationListener() {}
    
    private boolean isSameProvider(String paramString1, String paramString2)
    {
      boolean bool;
      if (paramString1 == null) {
        if (paramString2 == null) {
          bool = true;
        }
      }
      for (;;)
      {
        return bool;
        bool = false;
        continue;
        bool = paramString1.equals(paramString2);
      }
    }
    
    protected boolean isBetterLocation(Location paramLocation1, Location paramLocation2)
    {
      boolean bool;
      if (paramLocation2 == null) {
        bool = true;
      }
      for (;;)
      {
        return bool;
        if (paramLocation1 == null)
        {
          bool = false;
        }
        else
        {
          long l = paramLocation1.getTime() - paramLocation2.getTime();
          int j;
          label43:
          int k;
          if (l > 300000L)
          {
            j = 1;
            if (l >= -300000L) {
              break label81;
            }
            k = 1;
            label55:
            if (l <= 0L) {
              break label87;
            }
          }
          label81:
          label87:
          for (int i = 1;; i = 0)
          {
            if (j == 0) {
              break label92;
            }
            bool = true;
            break;
            j = 0;
            break label43;
            k = 0;
            break label55;
          }
          label92:
          if (k != 0)
          {
            bool = false;
          }
          else
          {
            int m = (int)(paramLocation1.getAccuracy() - paramLocation2.getAccuracy());
            if (m > 0)
            {
              j = 1;
              label123:
              if (m >= 0) {
                break label173;
              }
              k = 1;
              label131:
              if (m <= 200) {
                break label179;
              }
            }
            label173:
            label179:
            for (m = 1;; m = 0)
            {
              bool = isSameProvider(paramLocation1.getProvider(), paramLocation2.getProvider());
              if (k == 0) {
                break label185;
              }
              bool = true;
              break;
              j = 0;
              break label123;
              k = 0;
              break label131;
            }
            label185:
            if ((i != 0) && (j == 0)) {
              bool = true;
            } else if ((i != 0) && (m == 0) && (bool)) {
              bool = true;
            } else {
              bool = false;
            }
          }
        }
      }
    }
    
    public void onLocationChanged(Location paramLocation)
    {
      AppFileLog.addTrace("Location - LocationListener");
      if (paramLocation != null) {
        AppFileLog.addTrace("Location - LocationListener receiver location to (" + paramLocation.getLatitude() + "," + paramLocation.getLongitude() + ")");
      }
      if (isBetterLocation(paramLocation, RST_LocationManager.this.currentLocation)) {
        RST_LocationManager.this.updateLocation(paramLocation);
      }
    }
    
    public void onProviderDisabled(String paramString)
    {
      if (paramString == RST_LocationManager.this.bestProvider)
      {
        paramString = new Criteria();
        RST_LocationManager.this.bestProvider = RST_LocationManager.this.locationManager.getBestProvider(paramString, false);
        RST_LocationManager.this.locationManager.requestLocationUpdates(RST_LocationManager.this.bestProvider, 3600000L, 1.0F, this);
      }
    }
    
    public void onProviderEnabled(String paramString)
    {
      Location localLocation = RST_LocationManager.this.locationManager.getLastKnownLocation(RST_LocationManager.this.bestProvider);
      if (isBetterLocation(localLocation, RST_LocationManager.this.currentLocation))
      {
        RST_LocationManager.this.bestProvider = paramString;
        RST_LocationManager.this.updateLocation(localLocation);
        RST_LocationManager.this.locationManager.requestLocationUpdates(RST_LocationManager.this.bestProvider, 3600000L, 1.0F, this);
      }
    }
    
    public void onStatusChanged(String paramString, int paramInt, Bundle paramBundle) {}
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
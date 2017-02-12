package com.resmed.refresh.model;

import android.content.*;
import com.resmed.refresh.ui.uibase.app.*;
import com.resmed.refresh.utils.*;
import java.util.*;
import android.location.*;
import com.google.android.gms.maps.model.*;

public class RST_LocationManager
{
	private static final long RST_MIN_DISTANCE_UPDATES = 1L;
	private static final long RST_MIN_TIME_UPDATES = 3600000L;
	private static final int TIME_NEW = 300000;
	private static List<LocationCallback> locationCallbackList;
	private String bestProvider;
	private Context context;
	private Location currentLocation;
	private RST_LocationManager.RST_LocationListener locationListener;
	private LocationManager locationManager;

	public RST_LocationManager() {
		this.context = (Context)RefreshApplication.getInstance();
		this.locationManager = (LocationManager)this.context.getSystemService("location");
		this.locationListener = new RST_LocationManager.RST_LocationListener(this, (RST_LocationManager.RST_LocationListener)null);
		RST_LocationManager.locationCallbackList = new ArrayList<LocationCallback>();
		this.bestProvider = this.locationManager.getBestProvider(new Criteria(), false);
		if (this.bestProvider == null) {
			if (this.locationManager.isProviderEnabled("gps")) {
				this.bestProvider = "gps";
			}
			else {
				this.bestProvider = "network";
			}
		}
		this.currentLocation = this.getBestLocation();
		if (this.currentLocation != null) {
			AppFileLog.addTrace("Location - LocationManager currentLocation(" + this.currentLocation.getLatitude() + "," + this.currentLocation.getLongitude() + ")");
		}
	}

	private Location getBestLocation() {
		final Location locationByProvider = this.getLocationByProvider("gps");
		final Location locationByProvider2 = this.getLocationByProvider("network");
		if (locationByProvider == null) {
			Log.d("com.resmed.refresh.location", "No GPS Location available.");
			return locationByProvider2;
		}
		if (locationByProvider2 == null) {
			Log.d("com.resmed.refresh.location", "No Network Location available");
			return locationByProvider;
		}
		final long n = System.currentTimeMillis() - 86400000L;
		int n2;
		if (locationByProvider.getTime() < n) {
			n2 = 1;
		}
		else {
			n2 = 0;
		}
		final boolean b = locationByProvider2.getTime() < n;
		if (n2 == 0) {
			Log.d("com.resmed.refresh.location", "Returning current GPS Location");
			return locationByProvider;
		}
		if (!b) {
			Log.d("com.resmed.refresh.location", "GPS is old, Network is current, returning network");
			return locationByProvider2;
		}
		if (locationByProvider.getTime() > locationByProvider2.getTime()) {
			Log.d("com.resmed.refresh.location", "Both are old, returning gps(newer)");
			return locationByProvider;
		}
		Log.d("com.resmed.refresh.location", "Both are old, returning network(newer)");
		return locationByProvider2;
	}

	private Location getLocationByProvider(final String s) {
		if (!this.locationManager.isProviderEnabled(s)) {
			return null;
		}
		final LocationManager locationManager = (LocationManager)RefreshApplication.getInstance().getApplicationContext().getSystemService("location");
		try {
			final boolean providerEnabled = locationManager.isProviderEnabled(s);
			Location lastKnownLocation = null;
			if (providerEnabled) {
				lastKnownLocation = locationManager.getLastKnownLocation(s);
			}
			return lastKnownLocation;
		}
		catch (IllegalArgumentException ex) {
			Log.d("com.resmed.refresh.location", "Cannot acces Provider " + s);
			return null;
		}
	}

	private boolean isPresent(final LocationCallback locationCallback) {
		boolean b = false;
		final Iterator<LocationCallback> iterator = RST_LocationManager.locationCallbackList.iterator();
		while (iterator.hasNext()) {
			if (locationCallback == iterator.next()) {
				b = true;
			}
		}
		return b;
	}

	private void startLocationUpdates() {
		Log.d("com.resmed.refresh.location", "startLocationUpdates");
		if (this.currentLocation == null) {
			final Location lastKnownLocation = this.locationManager.getLastKnownLocation(this.bestProvider);
			if (lastKnownLocation != null) {
				this.updateLocation(lastKnownLocation);
			}
		}
		try {
			this.locationManager.requestLocationUpdates(this.bestProvider, 3600000L, 1.0f, (LocationListener)this.locationListener);
			AppFileLog.addTrace("Location - LocationManager locationManager.requestLocationUpdates");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void stopLocationUpdates() {
		Log.d("com.resmed.refresh.location", "stop location updates");
		if (this.locationManager != null) {
			this.locationManager.removeUpdates((LocationListener)this.locationListener);
		}
	}

	private void updateLocation(final Location currentLocation) {
		if (currentLocation != null) {
			this.currentLocation = currentLocation;
			RefreshModelController.getInstance().updateLocationUser(this.currentLocation);
			AppFileLog.addTrace("Location - LocationManager location(" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() + ")");
			Log.d("com.resmed.refresh.location", "updateLocation Lat:" + currentLocation.getLatitude() + "  Long:" + currentLocation.getLongitude());
			for (final LocationCallback locationCallback : RST_LocationManager.locationCallbackList) {
				if (locationCallback != null) {
					locationCallback.onLocationUpdateReceived(currentLocation);
				}
			}
		}
	}

	public LatLng getLastLocation() {
		final Location bestLocation = this.getBestLocation();
		if (bestLocation != null) {
			this.updateLocation(bestLocation);
		}
		if (this.currentLocation == null) {
			return new LatLng(-1.0, -1.0);
		}
		return new LatLng(this.currentLocation.getLatitude(), this.currentLocation.getLongitude());
	}

	public Location registerCallback(final LocationCallback locationCallback) {
		if (!this.isPresent(locationCallback)) {
			RST_LocationManager.locationCallbackList.add(locationCallback);
		}
		return this.currentLocation;
	}

	public void setLocationUpdatesListener(final boolean b) {
		if (b) {
			this.startLocationUpdates();
			return;
		}
		this.stopLocationUpdates();
	}

	public void unregisterCallback(final LocationCallback locationCallback) {
		RST_LocationManager.locationCallbackList.remove(locationCallback);
	}

	public void updateLocation() {
		this.startLocationUpdates();
	}
}

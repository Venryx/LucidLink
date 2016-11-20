package com.lucidlink;

import android.app.Application;
import android.util.Log;

import com.facebook.react.ReactApplication;
import com.github.yamill.orientation.OrientationPackage;
import com.rnfs.RNFSPackage;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;

import android.content.Intent; // <--- import
import android.content.res.Configuration; // <--- import

import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {
	private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
		@Override
		protected boolean getUseDeveloperSupport() {
		  return BuildConfig.DEBUG;
		}

		@Override
		protected List<ReactPackage> getPackages() {
			return Arrays.asList(
				new MainReactPackage(),
            	new OrientationPackage(),
				new Main_ReactPackage(),
				new RNFSPackage()
			);
		}
	};

	@Override
	public ReactNativeHost getReactNativeHost() {
	  return mReactNativeHost;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Intent intent = new Intent("onConfigurationChanged");
		intent.putExtra("newConfig", newConfig);
		this.sendBroadcast(intent);
	}
}
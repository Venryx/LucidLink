package com.lucidlink;

import android.app.Application;
import android.util.Log;

import com.facebook.react.ReactApplication;
import fr.bamlab.reactnativenumberpickerdialog.RNNumberPickerDialogPackage;
import com.rnfs.RNFSPackage;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.filepicker.FilePickerPackage;
import com.github.yamill.orientation.OrientationPackage;
import com.zmxv.RNSound.RNSoundPackage;
import com.aakashns.reactnativedialogs.ReactNativeDialogsPackage;

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
           		new RNNumberPickerDialogPackage(),
				new Main_ReactPackage(),
				new RNFSPackage(),
				new FilePickerPackage(),
				new OrientationPackage(),
				new RNSoundPackage(),
				new ReactNativeDialogsPackage()
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
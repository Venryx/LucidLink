package v.lucidlink;

import android.app.Application;
import com.facebook.react.ReactApplication;
import com.corbt.keepawake.KCKeepAwakePackage;
import com.zmxv.RNSound.RNSoundPackage;

import SPlus.SPlusPackage;
import fr.bamlab.reactnativenumberpickerdialog.RNNumberPickerDialogPackage;
import com.github.xinthink.rnmk.ReactMaterialKitPackage;
import v.LibMuse.LibMusePackage;

import com.rnfs.RNFSPackage;
import com.filepicker.FilePickerPackage;
import com.aakashns.reactnativedialogs.ReactNativeDialogsPackage;
import com.ocetnik.timer.BackgroundTimerPackage;
import com.lugg.ReactSnackbar.ReactSnackbarPackage;
import com.mihir.react.tts.RCTTextToSpeechModule;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import android.content.Intent;
import android.content.res.Configuration;
import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {
	/*public static List<ReactPackage> packages;
	public static <T> T GetPackageOfType(Class<T> class1) {
		return (T)Stream.of(packages).filter(a->a.getClass() == class1).toArray()[0];
	}*/

	private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
		@Override
		protected String getJSMainModuleName() {
			return "Build/Source/index.android";
		}

		@Override
		public boolean getUseDeveloperSupport() {
		  return BuildConfig.DEBUG;
		}

		@Override
		protected List<ReactPackage> getPackages() {
			//packages = Arrays.asList(
			return Arrays.asList(
				new MainReactPackage(),
            	new ReactMaterialKitPackage(),
           		new RNNumberPickerDialogPackage(),
				new LucidLinkPackage(),
				new RNFSPackage(),
				new FilePickerPackage(),
				//new OrientationPackage(),
				new RNSoundPackage(),
				new ReactNativeDialogsPackage(),
				new LibMusePackage(),
				new SPlusPackage(),
				new RCTTextToSpeechModule(),
				new ReactSnackbarPackage(),
				new BackgroundTimerPackage(),
				new KCKeepAwakePackage()
			);
			//return packages;
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
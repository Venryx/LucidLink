package v.lucidlink;

import android.app.Application;
import android.util.Log;

import com.annimon.stream.Stream;
import com.facebook.react.ReactApplication;
import com.github.xinthink.rnmk.ReactMaterialKitPackage;
import fr.bamlab.reactnativenumberpickerdialog.RNNumberPickerDialogPackage;

import v.lucidlink.BuildConfig;
import com.mihir.react.tts.RCTTextToSpeechModule;
import com.rnfs.RNFSPackage;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.filepicker.FilePickerPackage;
import com.github.yamill.orientation.OrientationPackage;
import v.LibMuse.LibMuse;
import com.zmxv.RNSound.RNSoundPackage;
import com.aakashns.reactnativedialogs.ReactNativeDialogsPackage;
import com.lugg.ReactSnackbar.ReactSnackbarPackage;

import android.content.Intent; // <--- import
import android.content.res.Configuration; // <--- import

import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {
	/*public static List<ReactPackage> packages;
	public static <T> T GetPackageOfType(Class<T> class1) {
		return (T)Stream.of(packages).filter(a->a.getClass() == class1).toArray()[0];
	}*/

	private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
		@Override
		protected boolean getUseDeveloperSupport() {
		  return BuildConfig.DEBUG;
		}

		@Override
		protected List<ReactPackage> getPackages() {
			//packages = Arrays.asList(
			return Arrays.asList(
				new MainReactPackage(),
            	new ReactMaterialKitPackage(),
           		new RNNumberPickerDialogPackage(),
				new Main_ReactPackage(),
				new RNFSPackage(),
				new FilePickerPackage(),
				new OrientationPackage(),
				new RNSoundPackage(),
				new ReactNativeDialogsPackage(),
				new LibMuse(),
				new RCTTextToSpeechModule(),
				new ReactSnackbarPackage()
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
package com.resmed.refresh.ui.uibase.app;

import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;
import com.flurry.android.FlurryAgent;
import com.resmed.refresh.bluetooth.BeDConnectionStatus;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.json.LocaleMUnitResponse;
import com.resmed.refresh.ui.utils.Consts;
import com.resmed.refresh.utils.Log;
import java.util.Locale;
import org.acra.ACRA;
import org.acra.ACRAConfiguration;

//@ReportsCrashes(formKey = "", mailTo = "RstAndroidTest@ResMed.com", mode = ReportingInteractionMode.TOAST, resToastText = 2131165352)
//public class RefreshApplication extends Application {
public class RefreshApplication extends Application {
	private static int activitiesInForeground = 0;
	public static RefreshApplication instance;
	public static boolean is64bitdevice = false;
	private static boolean isPlayingAlarm = false;
	public static String userCountry = "";
	public static LocaleMUnitResponse userMeasurementUnitMappingObj;
	protected BeDConnectionStatus connectionStatus;

	static {
		userMeasurementUnitMappingObj = Consts.DEFAULT_USER_MEASURMENT_MAPPING;
	}

	public static RefreshApplication getInstance() {
		synchronized(RefreshApplication.class){
			return instance;
		}
	}

	public void decreaseActivitiesInForeground() {
		activitiesInForeground += -1;
	}

	public int getAppVersion() {
		try {
			int var2 = this.getApplicationContext().getPackageManager().getPackageInfo(this.getApplicationContext().getPackageName(), 0).versionCode;
			return var2;
		} catch (NameNotFoundException var3) {
			var3.printStackTrace();
			return 0;
		}
	}

	public BeDConnectionStatus getConnectionStatus() {
		return this.connectionStatus;
	}

	public CONNECTION_STATE getCurrentConnectionState() {
		return this.connectionStatus.getState();
	}

	public void increaseActivitiesInForeground() {
		++activitiesInForeground;
	}

	public boolean isAppInForeground() {
		Log.d("com.resmed.refresh.sync", "activitiesInForeground=" + activitiesInForeground);
		return activitiesInForeground > 0;
	}

	public boolean isPlayingAlarm() {
		return isPlayingAlarm;
	}

	public void onCreate() {
		if(Consts.USE_ACRA_REPORTS) {
			ACRAConfiguration var2 = ACRA.getNewDefaultConfig(this);
			var2.setResToastText(2131165352);
			ACRA.setConfig(var2);
			ACRA.init(this);
		}

		super.onCreate();
		if(Consts.USE_FLURRY_REPORTS) {
			FlurryAgent.setLogEnabled(true);
			FlurryAgent.init(this, Consts.FLURRY_API_KEY);
		}

		Locale var1 = this.getResources().getConfiguration().locale;
		userCountry = var1.getCountry();
		System.out.println("************Current Locale0 ::" + var1.toString());
		System.out.println("************Current Locale1 ::" + var1.getCountry());
		System.out.println("************Current Locale2 ::" + var1.getDisplayLanguage());
		System.out.println("************Current Locale3 ::" + var1.getDisplayName());
		System.out.println("************Current Locale4 ::" + var1.getLanguage());
		if(System.getProperty("os.arch").equalsIgnoreCase("aarch64")) {
			is64bitdevice = true;
		}

		instance = this;
		this.connectionStatus = BeDConnectionStatus.getInstance();
	}

	public void setCurrentConnectionState(CONNECTION_STATE var1) {
		this.connectionStatus.updateState(var1);
	}
}
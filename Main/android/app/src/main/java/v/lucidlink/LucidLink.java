package v.lucidlink;

import com.facebook.react.bridge.ReactApplicationContext;
import com.google.firebase.analytics.FirebaseAnalytics;

// If:
// * It's managed by the LL tree, or...
// * You can't easily make a singleton in the class itself (and it doesn't benefit from being in LLS)
// Then, put it here.
public class LucidLink {
	//public Context baseContext;
	public MainApplication appContext;
	public static ReactApplicationContext reactContext;
	public LucidLinkModule mainModule;
	public FirebaseAnalytics analytics;
}
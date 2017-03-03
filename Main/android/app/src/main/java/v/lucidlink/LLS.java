package v.lucidlink;

import com.facebook.react.bridge.ReactApplicationContext;
import com.google.firebase.analytics.FirebaseAnalytics;

// If:
// * You can't easily make a singleton in the class itself, and it DOES benefit from being in LLS (ie, should be ready even when LL is not)
// Then, put it here.
public class LLS {
	public static LucidLink LL;
	// others
	//public static ReactApplicationContext reactContext;
}
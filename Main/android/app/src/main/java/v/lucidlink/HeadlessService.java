package v.lucidlink;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;
import com.facebook.soloader.SoLoader;

import vpackages.V;

/*public class HeadlessService extends HeadlessJsTaskService {
	@Override @Nullable protected HeadlessJsTaskConfig getTaskConfig(Intent intent) {
		Bundle extras = intent.getExtras();
		V.LogJava("Getting HeadlessService task-config.");
		if (extras != null)
			return new HeadlessJsTaskConfig("PreAppClose2", Arguments.fromBundle(extras), 5000);
		//return null;
		//return new HeadlessJsTaskConfig("PreAppClose2", Arguments.createMap(), 5000);
		return new HeadlessJsTaskConfig("PreAppClose2", null, 5000);
	}
}*/
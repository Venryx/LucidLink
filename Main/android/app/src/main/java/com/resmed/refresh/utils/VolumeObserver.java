package com.resmed.refresh.utils;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;

public class VolumeObserver
  extends ContentObserver
{
  public static int initVolume;
  private VolumeChangeCallback callback;
  private Context context;
  
  public VolumeObserver(VolumeChangeCallback paramVolumeChangeCallback, Context paramContext)
  {
    super(new Handler(RefreshApplication.getInstance().getMainLooper()));
    this.context = paramContext;
    this.callback = paramVolumeChangeCallback;
  }
  
  public boolean deliverSelfNotifications()
  {
    return super.deliverSelfNotifications();
  }
  
  public void onChange(boolean paramBoolean)
  {
    super.onChange(paramBoolean);
    int i = ((AudioManager)this.context.getSystemService("audio")).getStreamVolume(3);
    Log.v("com.resmed.refresh.smartAlarm", "Volume change: " + i);
    if (this.callback != null) {
      this.callback.onVolumeChange(i);
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
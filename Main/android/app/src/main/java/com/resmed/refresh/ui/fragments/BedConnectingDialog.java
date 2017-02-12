package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import com.resmed.refresh.ui.uibase.base.BaseActivity;

public class BedConnectingDialog
  extends DialogFragment
{
  public static String TAG = "BeDPickerDialog";
  private static final int TIMEOUT_TO_CONNECT = 15000;
  private BaseActivity baseActivity;
  private OnTimeoutConnectingDialog beDHandler;
  private ProgressBar progressBar;
  private UpdateProgress updateProgress;
  
  public static BedConnectingDialog newInstance()
  {
    return new BedConnectingDialog();
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.baseActivity = ((BaseActivity)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
      throw new ClassCastException(paramActivity.toString() + " ...BeDConnecting dialog should belong to a BaseActivity!");
    }
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    paramBundle = new Dialog(getActivity(), 16973840);
    View localView = getActivity().getLayoutInflater().inflate(2130903135, null);
    this.progressBar = ((ProgressBar)localView.findViewById(2131099950));
    this.progressBar.setProgress(0);
    if (this.updateProgress != null)
    {
      this.updateProgress.interrupt();
      this.updateProgress = null;
    }
    this.updateProgress = new UpdateProgress(null);
    this.updateProgress.start();
    paramBundle.setContentView(localView);
    paramBundle.setCancelable(false);
    return paramBundle;
  }
  
  public void onDismiss(DialogInterface paramDialogInterface)
  {
    super.onDismiss(paramDialogInterface);
    if (this.updateProgress != null) {
      this.updateProgress.finishUpdate();
    }
  }
  
  public void setOnTimeoutConnectingDialog(OnTimeoutConnectingDialog paramOnTimeoutConnectingDialog)
  {
    this.beDHandler = paramOnTimeoutConnectingDialog;
  }
  
  public static abstract interface OnTimeoutConnectingDialog
  {
    public abstract void onTimeoutConnectingDialog();
  }
  
  private class UpdateProgress
    extends Thread
  {
    private boolean isAlive = true;
    private int progress;
    
    private UpdateProgress() {}
    
    public void finishUpdate()
    {
      this.isAlive = false;
    }
    
    public void run()
    {
      super.run();
      for (;;)
      {
        if (!this.isAlive)
        {
          if ((BedConnectingDialog.this.beDHandler != null) && (this.progress > 100)) {
            BedConnectingDialog.this.beDHandler.onTimeoutConnectingDialog();
          }
          if ((BedConnectingDialog.this == null) || (BedConnectingDialog.this.isRemoving()) || (BedConnectingDialog.this.baseActivity == null) || (!BedConnectingDialog.this.baseActivity.isActivityReadyToCommit())) {}
        }
        try
        {
          BedConnectingDialog.this.dismiss();
          return;
        }
        catch (NullPointerException localNullPointerException)
        {
          try
          {
            Thread.sleep(150L);
            BedConnectingDialog.this.progressBar.setProgress(this.progress);
            this.progress += 1;
            if (this.progress > 100) {
              this.isAlive = false;
            }
            if ((BedConnectingDialog.this.getActivity() != null) && (!BedConnectingDialog.this.getActivity().isFinishing())) {
              continue;
            }
            this.isAlive = false;
            continue;
            localNullPointerException = localNullPointerException;
            localNullPointerException.printStackTrace();
          }
          catch (Exception localException)
          {
            for (;;) {}
          }
        }
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\BedConnectingDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.ui.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class QueryFirmwareUpgradeDialog
  extends DialogFragment
{
  public static String TAG = "UpgradeFirmwareDialog";
  private QueryFirmwareUpgradeManager upgradeFirmwareDialogManager;
  
  public QueryFirmwareUpgradeDialog(QueryFirmwareUpgradeManager paramQueryFirmwareUpgradeManager)
  {
    this.upgradeFirmwareDialogManager = paramQueryFirmwareUpgradeManager;
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    paramBundle = new AlertDialog.Builder(getActivity());
    paramBundle.setMessage("Upgrade firmware?").setPositiveButton(2131165296, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        paramAnonymousDialogInterface.dismiss();
        QueryFirmwareUpgradeDialog.this.upgradeFirmwareDialogManager.handleOk();
      }
    }).setNegativeButton(2131165297, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        paramAnonymousDialogInterface.dismiss();
        QueryFirmwareUpgradeDialog.this.upgradeFirmwareDialogManager.handleCancel();
      }
    });
    return paramBundle.create();
  }
  
  public static abstract interface QueryFirmwareUpgradeManager
  {
    public abstract void handleCancel();
    
    public abstract void handleOk();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\QueryFirmwareUpgradeDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
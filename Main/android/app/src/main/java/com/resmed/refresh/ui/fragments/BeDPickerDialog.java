package com.resmed.refresh.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.resmed.refresh.bluetooth.BeDConnectionStatus;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.utils.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeDPickerDialog
  extends DialogFragment
{
  private static String S_PLUS_DEVICE_PREFIX = "S+ by ResMed";
  public static String TAG = "BeDPickerDialog";
  private BaseBluetoothActivity bAct;
  private OnPickedDevice beDHandler;
  private BroadcastReceiver deviceFoundReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = (BluetoothDevice)paramAnonymousIntent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
      Log.d("com.resmed.refresh", " BLUETOOTH DEVICE FOUND! Name : " + paramAnonymousContext.getName() + " Address : " + paramAnonymousContext.getAddress());
      if (paramAnonymousContext != null) {
        BeDPickerDialog.this.addDevice(paramAnonymousContext);
      }
    }
  };
  private List<BluetoothDevice> deviceList;
  private View headerSpinner;
  private SensorListAdapter listAdapter;
  private ListView listView;
  
  public static BeDPickerDialog newInstance()
  {
    BeDPickerDialog localBeDPickerDialog = new BeDPickerDialog();
    Log.d("com.resmed.refresh.pair", "newInstance");
    return localBeDPickerDialog;
  }
  
  protected void addDevice(BluetoothDevice paramBluetoothDevice)
  {
    if (paramBluetoothDevice != null) {}
    try
    {
      if (!this.deviceList.contains(paramBluetoothDevice))
      {
        Object localObject = paramBluetoothDevice.getName();
        if ((localObject != null) && (((String)localObject).startsWith(S_PLUS_DEVICE_PREFIX)))
        {
          this.deviceList.add(paramBluetoothDevice);
          localObject = this.deviceList;
          paramBluetoothDevice = new com/resmed/refresh/ui/fragments/BeDPickerDialog$3;
          paramBluetoothDevice.<init>(this);
          Collections.sort((List)localObject, paramBluetoothDevice);
          this.listAdapter.notifyDataSetChanged();
        }
      }
      return;
    }
    finally {}
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    this.bAct = ((BaseBluetoothActivity)paramActivity);
    if (this.bAct == null) {}
    for (;;)
    {
      return;
      paramActivity = new Message();
      paramActivity.what = 10;
      this.bAct.sendMsgBluetoothService(paramActivity);
    }
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    Log.d("com.resmed.refresh.pair", "onCreateDialog");
    paramBundle = new Dialog(getActivity(), 16973840);
    View localView = getActivity().getLayoutInflater().inflate(2130903123, null);
    paramBundle.addContentView(localView, new ViewGroup.LayoutParams(-1, -1));
    this.deviceList = new ArrayList();
    this.listView = ((ListView)localView.findViewById(2131099891));
    this.listAdapter = new SensorListAdapter(this.deviceList);
    this.listView.setAdapter(this.listAdapter);
    this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        paramAnonymousAdapterView = (BluetoothDevice)BeDPickerDialog.this.deviceList.get(paramAnonymousInt);
        if (BeDPickerDialog.this.beDHandler != null) {
          BeDPickerDialog.this.beDHandler.onPickedDevice(paramAnonymousAdapterView);
        }
      }
    });
    this.headerSpinner = localView.findViewById(2131099890);
    getActivity().registerReceiver(this.deviceFoundReceiver, new IntentFilter("android.bluetooth.device.action.FOUND"));
    return paramBundle;
  }
  
  public void onDismiss(DialogInterface paramDialogInterface)
  {
    super.onDismiss(paramDialogInterface);
    Log.d("com.resmed.refresh.pair", "onDismiss");
    if (this.bAct == null) {}
    for (;;)
    {
      return;
      paramDialogInterface = new Message();
      paramDialogInterface.what = 22;
      this.bAct.sendMsgBluetoothService(paramDialogInterface);
    }
  }
  
  public void onResume()
  {
    super.onResume();
    Log.e("com.resmed.refresh.pair", "BeDPickerDialog onResume isConnected?:" + BeDConnectionStatus.getInstance().isSocketConnected());
    if (BeDConnectionStatus.getInstance().isSocketConnected()) {
      dismiss();
    }
  }
  
  public void onStart()
  {
    super.onStart();
    Log.e("com.resmed.refresh.pair", "BeDPickerDialog onStart isConnected?:" + BeDConnectionStatus.getInstance().isSocketConnected());
    if (BeDConnectionStatus.getInstance().isSocketConnected()) {
      dismiss();
    }
  }
  
  public void onViewStateRestored(Bundle paramBundle)
  {
    super.onViewStateRestored(paramBundle);
    Log.e("com.resmed.refresh.pair", "BeDPickerDialog onViewStateRestored isConnected?:" + BeDConnectionStatus.getInstance().isSocketConnected());
    if (BeDConnectionStatus.getInstance().isSocketConnected()) {
      dismiss();
    }
  }
  
  public void setOnPickedDevice(OnPickedDevice paramOnPickedDevice)
  {
    this.beDHandler = paramOnPickedDevice;
  }
  
  public static abstract interface OnPickedDevice
  {
    public abstract void onPickedDevice(BluetoothDevice paramBluetoothDevice);
  }
  
  private class SensorListAdapter
    extends BaseAdapter
    implements ListAdapter
  {
    private List<BluetoothDevice> devices;
    
    public SensorListAdapter()
    {
      List localList;
      this.devices = localList;
    }
    
    public int getCount()
    {
      return this.devices.size();
    }
    
    public BluetoothDevice getItem(int paramInt)
    {
      return (BluetoothDevice)this.devices.get(paramInt);
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      if (paramView == null) {
        paramViewGroup = LayoutInflater.from(RefreshApplication.getInstance()).inflate(2130903200, null);
      }
      paramView = (TextView)paramViewGroup.findViewById(2131099796);
      paramView.setTextColor(-1);
      paramView.setText(getItem(paramInt).getName());
      return paramViewGroup;
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\BeDPickerDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
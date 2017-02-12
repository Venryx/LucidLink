package com.resmed.refresh;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.utils.Log;
import java.util.ArrayList;
import java.util.List;

public class TestBluetoothDevicesActivity
  extends BaseBluetoothActivity
{
  private BroadcastReceiver deviceFoundReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if ("android.bluetooth.device.action.FOUND".equals(paramAnonymousIntent.getAction()))
      {
        paramAnonymousContext = (BluetoothDevice)paramAnonymousIntent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
        Log.d("com.resmed.refresh", " BLUETOOTH DEVICE FOUND! Name : " + paramAnonymousContext.getName() + " Address : " + paramAnonymousContext.getAddress());
        if (!TestBluetoothDevicesActivity.this.deviceList.contains(paramAnonymousContext))
        {
          TestBluetoothDevicesActivity.this.deviceList.add(paramAnonymousContext);
          TestBluetoothDevicesActivity.this.listAdapter.notifyDataSetChanged();
        }
      }
    }
  };
  private List<BluetoothDevice> deviceList;
  private BaseAdapter listAdapter;
  private ListView listView;
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    setContentView(2130903075);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903075);
    this.deviceList = new ArrayList();
    this.listView = ((ListView)findViewById(2131099797));
    this.listAdapter = new BluetoothDevicesAdapter(this.deviceList);
    this.listView.setAdapter(this.listAdapter);
    this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        paramAnonymousAdapterView = (BluetoothDevice)TestBluetoothDevicesActivity.this.deviceList.get(paramAnonymousInt);
        Log.d("com.resmed.refresh", " SETTING bluetooth PREFIX & MAC ADDRESS : " + paramAnonymousAdapterView.getName() + " addr : " + paramAnonymousAdapterView.getAddress());
        paramAnonymousView = new Intent(TestBluetoothDevicesActivity.this.getApplicationContext(), TestBluetoothRPCActivity.class);
        paramAnonymousView.putExtra(TestBluetoothDevicesActivity.this.getString(2131165943), paramAnonymousAdapterView.getName());
        paramAnonymousView.putExtra(TestBluetoothDevicesActivity.this.getString(2131165944), paramAnonymousAdapterView.getAddress());
        TestBluetoothDevicesActivity.this.startActivity(paramAnonymousView);
      }
    });
    paramBundle = new IntentFilter("android.bluetooth.device.action.FOUND");
    registerReceiver(this.deviceFoundReceiver, paramBundle);
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    try
    {
      unregisterReceiver(this.deviceFoundReceiver);
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      for (;;) {}
    }
  }
  
  protected void onResume()
  {
    super.onResume();
    BluetoothAdapter localBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    localBluetoothAdapter.cancelDiscovery();
    localBluetoothAdapter.startDiscovery();
  }
  
  public class BluetoothDevicesAdapter
    extends BaseAdapter
    implements ListAdapter
  {
    private List<BluetoothDevice> deviceNames;
    
    public BluetoothDevicesAdapter()
    {
      List localList;
      this.deviceNames = localList;
    }
    
    public int getCount()
    {
      return this.deviceNames.size();
    }
    
    public BluetoothDevice getItem(int paramInt)
    {
      return (BluetoothDevice)this.deviceNames.get(paramInt);
    }
    
    public long getItemId(int paramInt)
    {
      return 0L;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      if (paramView == null) {
        paramViewGroup = ((LayoutInflater)TestBluetoothDevicesActivity.this.getSystemService("layout_inflater")).inflate(2130903200, null);
      }
      ((TextView)paramViewGroup.findViewById(2131099796)).setText(getItem(paramInt).getName() + " [" + getItem(paramInt).getAddress() + " ]");
      return paramViewGroup;
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.sleepsession.SleepSessionManager;
import java.nio.ByteBuffer;
import java.util.Set;

public abstract interface RefreshBluetoothManager
{
  public abstract void bluetoothOff();
  
  public abstract boolean cancelDiscovery();
  
  public abstract void cancelReconnection();
  
  public abstract void connectDevice(BluetoothDevice paramBluetoothDevice);
  
  public abstract void disable();
  
  public abstract void discoverResMedDevices(boolean paramBoolean);
  
  public abstract void enable();
  
  public abstract BluetoothAdapter getBluetoothAdapter();
  
  public abstract CONNECTION_STATE getConnectionStatus();
  
  public abstract void handleNewPacket(ByteBuffer paramByteBuffer);
  
  public abstract boolean isBluetoothEnabled();
  
  public abstract boolean isDevicePaired(BluetoothDevice paramBluetoothDevice);
  
  public abstract void manageConnection(BluetoothSocket paramBluetoothSocket);
  
  public abstract void pairDevice(BluetoothDevice paramBluetoothDevice);
  
  public abstract Set<BluetoothDevice> queryPairedDevices();
  
  public abstract void setConnectionStatusAndNotify(CONNECTION_STATE paramCONNECTION_STATE, boolean paramBoolean);
  
  public abstract void testStreamData(SleepSessionManager paramSleepSessionManager);
  
  public abstract void unpairAll(String paramString);
  
  public abstract boolean unpairDevice(BluetoothDevice paramBluetoothDevice);
  
  public abstract void writeData(byte[] paramArrayOfByte);
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
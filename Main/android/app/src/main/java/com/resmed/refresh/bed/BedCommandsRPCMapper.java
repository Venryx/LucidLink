package com.resmed.refresh.bed;

import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;

public abstract interface BedCommandsRPCMapper
{
  public abstract JsonRPC clearBuffers();
  
  public abstract JsonRPC closeSession();
  
  public abstract JsonRPC fillBuffer(int paramInt);
  
  public abstract JsonRPC getBioSensorSerialNumber();
  
  public abstract JsonRPC getOperationalStatus();
  
  public abstract int getRPCid();
  
  public abstract JsonRPC getSampleNumber(boolean paramBoolean);
  
  public abstract JsonRPC getSerialNumber();
  
  public abstract JsonRPC leds(LedsState paramLedsState);
  
  public abstract JsonRPC openSession(String paramString);
  
  public abstract JsonRPC putSerialNumber(String paramString);
  
  public abstract JsonRPC reset();
  
  public abstract JsonRPC resetEngineeringMode();
  
  public abstract JsonRPC setBioSensorSerialNumber(String paramString);
  
  public abstract void setContextBroadcaster(BaseBluetoothActivity paramBaseBluetoothActivity);
  
  public abstract void setRPCid(int paramInt);
  
  public abstract JsonRPC startNightTracking();
  
  public abstract JsonRPC startRealTimeStream();
  
  public abstract JsonRPC stopNightTimeTracking();
  
  public abstract JsonRPC stopRealTimeStream();
  
  public abstract JsonRPC transmitPacket(int paramInt, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract JsonRPC upgradeFirmware(byte[] paramArrayOfByte, int paramInt, boolean paramBoolean);
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
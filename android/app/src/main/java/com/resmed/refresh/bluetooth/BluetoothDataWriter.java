package com.resmed.refresh.bluetooth;

import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.packets.VLPacketType;

public abstract interface BluetoothDataWriter
{
  public abstract boolean sendBytesToBeD(byte[] paramArrayOfByte, VLPacketType paramVLPacketType);
  
  public abstract void sendRpcToBed(JsonRPC paramJsonRPC);
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
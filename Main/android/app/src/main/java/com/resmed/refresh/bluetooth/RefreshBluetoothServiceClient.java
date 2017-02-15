package com.resmed.refresh.bluetooth;

import android.content.Context;
import android.os.Message;

import com.resmed.refresh.packets.VLP;

public abstract interface RefreshBluetoothServiceClient
{
  public static final String BLUETOOTH_SERVICE_INTENT_RESTART = "BLUETOOTH_SERVICE_INTENT_RESTART";
  
  public abstract Context getContext();
  
  public abstract void handlePacket(VLP.VLPacket paramVLPacket);
  
  public abstract void sendConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE);
  
  public abstract void sendMessageToClient(Message paramMessage);
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
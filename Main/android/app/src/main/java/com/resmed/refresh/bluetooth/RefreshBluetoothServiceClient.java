package com.resmed.refresh.bluetooth;

import android.content.Context;
import android.os.Message;

import com.resmed.refresh.packets.VLP;

public abstract interface RefreshBluetoothServiceClient
{
  public static final String BLUETOOTH_SERVICE_INTENT_RESTART = "BLUETOOTH_SERVICE_INTENT_RESTART";
  public static final int MSG_BLUETOOTH_BULK_TRANSFER_START = 24;
  public static final int MSG_BLUETOOTH_BULK_TRANSFER_STOP = 25;
  public static final int MSG_BLUETOOTH_RADIO_RESET = 29;
  public static final int MSG_BeD_CONNECTION_STATUS = 6;
  public static final int MSG_BeD_CONNECT_TO_DEVICE = 11;
  public static final int MSG_BeD_DISCONNECT = 7;
  public static final int MSG_BeD_STREAM_PACKET = 5;
  public static final int MSG_BeD_STREAM_PACKET_PARTIAL = 19;
  public static final int MSG_BeD_STREAM_PACKET_PARTIAL_END = 20;
  public static final int MSG_BeD_UNPAIR = 8;
  public static final int MSG_BeD_UNPAIR_ALL = 23;
  public static final int MSG_CANCEL_DISCOVERY = 22;
  public static final int MSG_DISCOVER_PAIR_CONNECT_RESMED = 1;
  public static final int MSG_DISCOVER_RESMED_DEVICES = 10;
  public static final int MSG_REGISTER_CLIENT = 4;
  public static final int MSG_REQUEST_BeD_AVAILABLE_DATA = 27;
  public static final int MSG_RESET_BeD_AVAILABLE_DATA = 28;
  public static final int MSG_SEND_BYTES = 2;
  public static final int MSG_SLEEP_BREATHING_RATE = 18;
  public static final int MSG_SLEEP_ENV_SAMPLE = 15;
  public static final int MSG_SLEEP_SESSION_RECOVER = 21;
  public static final int MSG_SLEEP_SESSION_START = 12;
  public static final int MSG_SLEEP_SESSION_STOP = 14;
  public static final int MSG_SLEEP_SET_SMART_ALARM = 16;
  public static final int MSG_SLEEP_USER_SLEEP_STATE = 17;
  public static final int MSG_SMARTALARM_UPDATE = 26;
  public static final int MSG_START_DISCOVERY = 3;
  public static final int MSG_UNREGISTER_CLIENT = 9;
  
  public abstract Context getContext();
  
  public abstract void handlePacket(VLP.VLPacket paramVLPacket);
  
  public abstract void sendConnectionStatus(CONNECTION_STATE paramCONNECTION_STATE);
  
  public abstract void sendMessageToClient(Message paramMessage);
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
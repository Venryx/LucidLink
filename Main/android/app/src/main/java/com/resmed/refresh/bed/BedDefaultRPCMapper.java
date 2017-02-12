package com.resmed.refresh.bed;

import android.content.Intent;
import com.resmed.refresh.bluetooth.CONNECTION_STATE;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;
import com.resmed.refresh.utils.Log;
import java.util.LinkedHashMap;
import java.util.Map;

public class BedDefaultRPCMapper
  implements BedCommandsRPCMapper
{
  private static BedDefaultRPCMapper Instance = null;
  private BaseBluetoothActivity btContext;
  private String lastGuid;
  private int rpcId = 115;
  
  private void broadcastState(CONNECTION_STATE paramCONNECTION_STATE)
  {
    Intent localIntent = new Intent("ACTION_RESMED_CONNECTION_STATUS");
    localIntent.putExtra("EXTRA_RESMED_CONNECTION_STATE", paramCONNECTION_STATE);
    Log.d("com.resmed.refresh.pair", "broadcastState broadcastState : " + CONNECTION_STATE.toString(paramCONNECTION_STATE));
    this.btContext.sendStickyOrderedBroadcast(localIntent, null, null, -1, null, null);
  }
  
  private int calculateChecksum(byte[] paramArrayOfByte)
  {
    int j = 0;
    int k = paramArrayOfByte.length;
    for (int i = 0;; i++)
    {
      if (i >= k) {
        return (j ^ 0xFFFFFFFF) + 1;
      }
      j += (paramArrayOfByte[i] & 0xFF);
    }
  }
  
  public static BedDefaultRPCMapper getInstance()
  {
    if (Instance == null) {
      Instance = new BedDefaultRPCMapper();
    }
    return Instance;
  }
  
  public JsonRPC clearBuffers()
  {
    JsonRPC localJsonRPC = new JsonRPC("clearBuffers", null, Integer.valueOf(this.rpcId));
    if (this.btContext != null) {
      localJsonRPC.setRPCallback(new JsonRPC.RPCallback()
      {
        public void execute()
        {
          BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.SESSION_OPENED);
        }
        
        public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {}
        
        public void preExecute() {}
      });
    }
    return localJsonRPC;
  }
  
  public JsonRPC closeSession()
  {
    JsonRPC localJsonRPC = new JsonRPC("closeSession", null, Integer.valueOf(this.rpcId));
    if (this.btContext != null) {
      localJsonRPC.setRPCallback(new JsonRPC.RPCallback()
      {
        public void execute()
        {
          BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.SOCKET_CONNECTED);
        }
        
        public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {}
        
        public void preExecute() {}
      });
    }
    return localJsonRPC;
  }
  
  public JsonRPC fillBuffer(int paramInt)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    localLinkedHashMap.put("timeInSecs", Integer.valueOf(paramInt));
    return new JsonRPC("fillBuffer", localLinkedHashMap, Integer.valueOf(this.rpcId));
  }
  
  public JsonRPC getBioSensorSerialNumber()
  {
    return new JsonRPC("getSerialNumberSensor", null, Integer.valueOf(this.rpcId));
  }
  
  public JsonRPC getOperationalStatus()
  {
    return new JsonRPC("getOperationalStatus", null, Integer.valueOf(this.rpcId));
  }
  
  public int getRPCid()
  {
    return this.rpcId;
  }
  
  public JsonRPC getSampleNumber(boolean paramBoolean)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    String str = "BUF_ENV";
    if (!paramBoolean) {
      str = "BUFF_BIO";
    }
    localLinkedHashMap.put("buffID", str);
    return new JsonRPC("getSampleNumUnsent", localLinkedHashMap, Integer.valueOf(this.rpcId));
  }
  
  public JsonRPC getSerialNumber()
  {
    return new JsonRPC("getSerialNumberBeD", null, Integer.valueOf(this.rpcId));
  }
  
  public JsonRPC leds(LedsState paramLedsState)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    localLinkedHashMap.put("ledName", "LED_STATUS");
    localLinkedHashMap.put("state", paramLedsState.toString());
    return new JsonRPC("leds", localLinkedHashMap, Integer.valueOf(this.rpcId));
  }
  
  public JsonRPC openSession(String paramString)
  {
    this.lastGuid = paramString;
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    localLinkedHashMap.put("guid", paramString);
    paramString = new JsonRPC("requestSession", localLinkedHashMap, Integer.valueOf(this.rpcId));
    paramString.setRPCallback(new JsonRPC.RPCallback()
    {
      public void execute()
      {
        BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.SESSION_OPENED);
      }
      
      public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc)
      {
        if (-19 == paramAnonymousErrorRpc.getCode().intValue()) {
          BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.SESSION_OPENED);
        }
      }
      
      public void preExecute()
      {
        Log.d("com.resmed.refresh.pair", "**************** Sending openning when state is " + RefreshApplication.getInstance().getCurrentConnectionState().toString());
        BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.SESSION_OPENING);
      }
    });
    return paramString;
  }
  
  public JsonRPC putSerialNumber(String paramString)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    localLinkedHashMap.put("serialID", paramString);
    return new JsonRPC("putSerialNumber", localLinkedHashMap, Integer.valueOf(this.rpcId));
  }
  
  public JsonRPC reset()
  {
    return new JsonRPC("reset", null, Integer.valueOf(this.rpcId));
  }
  
  public JsonRPC resetEngineeringMode()
  {
    return new JsonRPC("resetEngineering", null, Integer.valueOf(this.rpcId));
  }
  
  public JsonRPC setBioSensorSerialNumber(String paramString)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    localLinkedHashMap.put("serialSensorID", paramString);
    return new JsonRPC("setSerialNumberSensor", localLinkedHashMap, Integer.valueOf(this.rpcId));
  }
  
  public void setContextBroadcaster(BaseBluetoothActivity paramBaseBluetoothActivity)
  {
    this.btContext = paramBaseBluetoothActivity;
  }
  
  public void setRPCid(int paramInt)
  {
    this.rpcId = paramInt;
  }
  
  public JsonRPC startNightTracking()
  {
    final Object localObject = new LinkedHashMap();
    ((Map)localObject).put("channels", "ALL");
    ((Map)localObject).put("src", "REAL");
    ((Map)localObject).put("nTicks", Integer.valueOf(3456000));
    ((Map)localObject).put("bandWidth", Integer.valueOf(8));
    localObject = new JsonRPC("startSample", (Map)localObject, Integer.valueOf(this.rpcId));
    if (this.btContext != null) {
      ((JsonRPC)localObject).setRPCallback(new JsonRPC.RPCallback()
      {
        public void execute()
        {
          BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.NIGHT_TRACK_ON);
        }
        
        public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc)
        {
          if (paramAnonymousErrorRpc == null) {}
          for (;;)
          {
            return;
            if (-18 == paramAnonymousErrorRpc.getCode().intValue())
            {
              paramAnonymousErrorRpc = BedDefaultRPCMapper.this.openSession(BedDefaultRPCMapper.this.lastGuid);
              paramAnonymousErrorRpc.setRPCallback(new JsonRPC.RPCallback()
              {
                public void execute()
                {
                  JsonRPC localJsonRPC = new JsonRPC(this.val$jsonRPC.getMethod(), this.val$jsonRPC.getParams(), Integer.valueOf(BedDefaultRPCMapper.this.getRPCid()));
                  BedDefaultRPCMapper.this.btContext.sendRpcToBed(localJsonRPC);
                }
                
                public void onError(JsonRPC.ErrorRpc paramAnonymous2ErrorRpc) {}
                
                public void preExecute() {}
              });
              BedDefaultRPCMapper.this.btContext.sendRpcToBed(paramAnonymousErrorRpc);
            }
            else if (-13 == paramAnonymousErrorRpc.getCode().intValue())
            {
              BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.NIGHT_TRACK_ON);
            }
          }
        }
        
        public void preExecute() {}
      });
    }
    return (JsonRPC)localObject;
  }
  
  public JsonRPC startRealTimeStream()
  {
    final Object localObject = new LinkedHashMap();
    ((Map)localObject).put("src", "REAL");
    ((Map)localObject).put("nTicks", Integer.valueOf(100000));
    localObject = new JsonRPC("startStream", (Map)localObject, Integer.valueOf(this.rpcId));
    if (this.btContext != null) {
      ((JsonRPC)localObject).setRPCallback(new JsonRPC.RPCallback()
      {
        public void execute()
        {
          BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.REAL_STREAM_ON);
        }
        
        public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc)
        {
          if (paramAnonymousErrorRpc == null) {}
          for (;;)
          {
            return;
            if (-18 == paramAnonymousErrorRpc.getCode().intValue())
            {
              paramAnonymousErrorRpc = BedDefaultRPCMapper.this.openSession(BedDefaultRPCMapper.this.lastGuid);
              paramAnonymousErrorRpc.setRPCallback(new JsonRPC.RPCallback()
              {
                public void execute()
                {
                  JsonRPC localJsonRPC = new JsonRPC(this.val$jsonRPC.getMethod(), this.val$jsonRPC.getParams(), Integer.valueOf(BedDefaultRPCMapper.this.getRPCid()));
                  BedDefaultRPCMapper.this.btContext.sendRpcToBed(localJsonRPC);
                }
                
                public void onError(JsonRPC.ErrorRpc paramAnonymous2ErrorRpc) {}
                
                public void preExecute() {}
              });
              BedDefaultRPCMapper.this.btContext.sendRpcToBed(paramAnonymousErrorRpc);
            }
          }
        }
        
        public void preExecute() {}
      });
    }
    return (JsonRPC)localObject;
  }
  
  public JsonRPC stopNightTimeTracking()
  {
    JsonRPC localJsonRPC = new JsonRPC("stopSample", null, Integer.valueOf(this.rpcId));
    if (this.btContext != null) {
      localJsonRPC.setRPCallback(new JsonRPC.RPCallback()
      {
        public void execute()
        {
          BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.SESSION_OPENED);
        }
        
        public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {}
        
        public void preExecute() {}
      });
    }
    return localJsonRPC;
  }
  
  public JsonRPC stopRealTimeStream()
  {
    JsonRPC localJsonRPC = new JsonRPC("stopStream", null, Integer.valueOf(this.rpcId));
    if (this.btContext != null) {
      localJsonRPC.setRPCallback(new JsonRPC.RPCallback()
      {
        public void execute()
        {
          BedDefaultRPCMapper.this.broadcastState(CONNECTION_STATE.SESSION_OPENED);
        }
        
        public void onError(JsonRPC.ErrorRpc paramAnonymousErrorRpc) {}
        
        public void preExecute() {}
      });
    }
    return localJsonRPC;
  }
  
  public JsonRPC transmitPacket(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    String str = "BUF_ENV";
    if (!paramBoolean1) {
      str = "BUF_BIO";
    }
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    localLinkedHashMap.put("nSamples", Integer.valueOf(paramInt));
    localLinkedHashMap.put("buffID", str);
    str = "transmitPacket";
    if (paramBoolean2) {
      str = "transmitPacketOldest";
    }
    return new JsonRPC(str, localLinkedHashMap, Integer.valueOf(this.rpcId));
  }
  
  public JsonRPC upgradeFirmware(byte[] paramArrayOfByte, int paramInt, boolean paramBoolean)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    if (paramInt == 0) {
      localLinkedHashMap.put("src", "image1");
    }
    for (;;)
    {
      localLinkedHashMap.put("opt", "upgrade");
      paramInt = -1;
      if (!paramBoolean) {
        paramInt = calculateChecksum(paramArrayOfByte);
      }
      localLinkedHashMap.put("checksum", Integer.valueOf(paramInt));
      return new JsonRPC("putApplication", localLinkedHashMap, Integer.valueOf(this.rpcId));
      if (1 == paramInt) {
        localLinkedHashMap.put("src", "image2");
      }
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\bed\BedDefaultRPCMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
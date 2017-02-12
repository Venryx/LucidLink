package com.resmed.refresh.model.json;

import java.util.Map;

public class JsonRPC
{
  private RPCallback callback;
  private ErrorRpc error;
  private int id;
  private String jsonrpc = "2.0";
  private String method;
  private Map<String, Object> params;
  private ResultRPC result;
  
  public JsonRPC(String paramString, Map<String, Object> paramMap, Integer paramInteger)
  {
    this.method = paramString;
    this.params = paramMap;
    this.id = paramInteger.intValue();
  }
  
  public ErrorRpc getError()
  {
    return this.error;
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public String getJsonrpc()
  {
    return this.jsonrpc;
  }
  
  public String getMethod()
  {
    return this.method;
  }
  
  public Map<String, Object> getParams()
  {
    return this.params;
  }
  
  public RPCallback getRPCallback()
  {
    return this.callback;
  }
  
  public ResultRPC getResult()
  {
    return this.result;
  }
  
  public void setId(Integer paramInteger)
  {
    this.id = paramInteger.intValue();
  }
  
  public void setMethod(String paramString)
  {
    this.method = paramString;
  }
  
  public void setRPCallback(RPCallback paramRPCallback)
  {
    this.callback = paramRPCallback;
  }
  
  public class ErrorRpc
  {
    public static final int RPC_ERROR_ACCESS_DENIED = -17;
    public static final int RPC_ERROR_ALREADY_IDLE = -21;
    public static final int RPC_ERROR_ALREADY_QUIESCENT = -14;
    public static final int RPC_ERROR_ALREADY_RECORDING = -13;
    public static final int RPC_ERROR_ALREADY_STREAMING = -20;
    public static final int RPC_ERROR_APP_UPGRADE = -15;
    public static final int RPC_ERROR_CALL_NOT_MATCHED = -7;
    public static final int RPC_ERROR_EVENT_QUEUE = -2;
    public static final int RPC_ERROR_INVALID_PARAMS = -16;
    public static final int RPC_ERROR_LED_SET = -4;
    public static final int RPC_ERROR_MEM_ALLOC = -1;
    public static final int RPC_ERROR_NOT_ENOUGH_DATA = -12;
    public static final int RPC_ERROR_NOT_IN_ENGG_MODE = -6;
    public static final int RPC_ERROR_NO_LATEST_DATA = -11;
    public static final int RPC_ERROR_OUT_OF_SESSION = -18;
    public static final int RPC_ERROR_PAYLOAD_SEND = -10;
    public static final int RPC_ERROR_READ_QUEUE = -8;
    public static final int RPC_ERROR_RESET = -5;
    public static final int RPC_ERROR_SESSION_IN_PROGRESS = -19;
    public static final int RPC_ERROR_SET_BED_SERIAL_NO = -3;
    public static final int RPC_ERROR_VLE_CHK_SUM = -9;
    private Integer code;
    private String message;
    
    public ErrorRpc() {}
    
    public Integer getCode()
    {
      return this.code;
    }
    
    public String getMessage()
    {
      return this.message;
    }
  }
  
  public static abstract interface RPCallback
  {
    public abstract void execute();
    
    public abstract void onError(JsonRPC.ErrorRpc paramErrorRpc);
    
    public abstract void preExecute();
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\json\JsonRPC.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
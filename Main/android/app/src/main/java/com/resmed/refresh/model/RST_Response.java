package com.resmed.refresh.model;

public class RST_Response<T>
{
  private int errorCode;
  private String errorMessage;
  private T response;
  private boolean status;
  
  public int getErrorCode()
  {
    return this.errorCode;
  }
  
  public String getErrorMessage()
  {
    return this.errorMessage;
  }
  
  public T getResponse()
  {
    return (T)this.response;
  }
  
  public boolean getStatus()
  {
    return this.status;
  }
  
  public boolean isStatus()
  {
    return this.status;
  }
  
  public void setErrorCode(int paramInt)
  {
    this.errorCode = paramInt;
  }
  
  public void setErrorMessage(String paramString)
  {
    this.errorMessage = paramString;
  }
  
  public void setResponse(T paramT)
  {
    this.response = paramT;
  }
  
  public void setStatus(boolean paramBoolean)
  {
    this.status = paramBoolean;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\RST_Response.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
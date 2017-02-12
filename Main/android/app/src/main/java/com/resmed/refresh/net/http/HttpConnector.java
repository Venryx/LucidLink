package com.resmed.refresh.net.http;

import java.io.File;
import org.apache.http.message.BasicNameValuePair;

public abstract interface HttpConnector
{
  public abstract void delete(String paramString, HttpCallback paramHttpCallback, BasicNameValuePair... paramVarArgs);
  
  public abstract void get(String paramString, HttpCallback paramHttpCallback, BasicNameValuePair... paramVarArgs);
  
  public abstract void post(String paramString1, HttpCallback paramHttpCallback, String paramString2, String... paramVarArgs);
  
  public abstract void put(String paramString, HttpCallback paramHttpCallback, File paramFile);
  
  public abstract void put(String paramString1, HttpCallback paramHttpCallback, String paramString2);
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\net\http\HttpConnector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.net.http;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Base64;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.Log;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

public class HttpDefaultConnector
  implements HttpConnector
{
  private static final int TIMEOUT_CONNECTION = 30000;
  
  private DefaultHttpClient getDefaultHttpClient()
  {
    BasicHttpParams localBasicHttpParams = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(localBasicHttpParams, 30000);
    return new DefaultHttpClient(localBasicHttpParams);
  }
  
  private void setResMedHeaders(HttpRequest paramHttpRequest, String[] paramArrayOfString)
  {
    i = 0;
    Object localObject2 = RefreshApplication.getInstance().getApplicationContext();
    localObject1 = "0x0";
    try
    {
      localObject2 = ((Context)localObject2).getPackageManager().getPackageInfo(((Context)localObject2).getPackageName(), 0).versionName;
      localObject1 = localObject2;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      for (;;)
      {
        int j;
        int k;
        localNameNotFoundException.printStackTrace();
        continue;
        localObject1 = paramArrayOfString[i];
        if (localObject1 != null)
        {
          localObject1 = Base64.encodeToString(((String)localObject1).getBytes(), 4).trim();
          paramHttpRequest.setHeader("Key" + j, (String)localObject1);
        }
        j++;
        i++;
      }
    }
    paramHttpRequest.setHeader(new BasicHeader("Content-Type", "application/json"));
    paramHttpRequest.setHeader("ResMedDeviceId", RefreshModelController.getInstance().getDeviceId());
    paramHttpRequest.setHeader("ResMedAppVersion ", (String)localObject1);
    paramHttpRequest.setHeader("ResMedOsVersion", Build.VERSION.RELEASE);
    paramHttpRequest.setHeader("ResMedDeviceModel", Build.MODEL);
    paramHttpRequest.setHeader("ResMedClientOs", "android");
    if (paramArrayOfString != null)
    {
      j = 1;
      k = paramArrayOfString.length;
      if (i < k) {}
    }
    else
    {
      paramArrayOfString = RefreshModelController.getInstance().getAuthentication();
      if ((paramArrayOfString != null) && (paramArrayOfString.length() > 0)) {
        paramHttpRequest.setHeader("Authorization", paramArrayOfString);
      }
      return;
    }
  }
  
  public void delete(String paramString, HttpCallback paramHttpCallback, BasicNameValuePair... paramVarArgs) {}
  
  public void get(String paramString, HttpCallback paramHttpCallback, BasicNameValuePair... paramVarArgs)
  {
    HttpGet localHttpGet = new HttpGet();
    List localList = Arrays.asList(paramVarArgs);
    paramVarArgs = paramString;
    if (!paramString.endsWith("?"))
    {
      paramVarArgs = paramString;
      if (localList.size() > 0) {
        paramVarArgs = paramString + "?";
      }
    }
    paramString = URLEncodedUtils.format(localList, "utf-8");
    paramVarArgs = paramVarArgs + paramString;
    Log.d("com.resmed.refresh.net", " requesting : " + paramVarArgs);
    try
    {
      paramString = new java/net/URI;
      paramString.<init>(paramVarArgs);
      localHttpGet.setURI(paramString);
      setResMedHeaders(localHttpGet, null);
      new HttpRequestThread(getDefaultHttpClient(), localHttpGet, paramHttpCallback).start();
      return;
    }
    catch (URISyntaxException paramString)
    {
      for (;;)
      {
        paramString.printStackTrace();
      }
    }
  }
  
  public void post(String paramString1, HttpCallback paramHttpCallback, String paramString2, String... paramVarArgs)
  {
    paramString1 = new HttpPost(paramString1);
    if (paramString2 != null) {}
    try
    {
      StringEntity localStringEntity = new org/apache/http/entity/StringEntity;
      localStringEntity.<init>(paramString2, "UTF-8");
      StringBuilder localStringBuilder = new java.lang.StringBuilder;
      localStringBuilder.<init>(" http post body :");
      Log.d("com.resmed.refresh.net", paramString2);
      paramString1.setEntity(localStringEntity);
      if (paramVarArgs != null) {
        Log.d("com.resmed.refresh.net", "HttpDefaultConnnector::post() extraHeaders : " + paramVarArgs.length);
      }
      setResMedHeaders(paramString1, paramVarArgs);
      new HttpRequestThread(getDefaultHttpClient(), paramString1, paramHttpCallback).start();
      return;
    }
    catch (UnsupportedEncodingException paramString2)
    {
      for (;;)
      {
        paramString2.printStackTrace();
      }
    }
  }
  
  public void put(String paramString, HttpCallback paramHttpCallback, File paramFile)
  {
    paramString = new HttpPut(paramString);
    paramFile = new FileEntity(paramFile, "application/octet-stream");
    Log.d("com.resmed.refresh.net", " http put file :" + paramFile);
    paramString.setEntity(paramFile);
    setResMedHeaders(paramString, null);
    paramString.setHeader("Content-Type", "application/octet-stream");
    new HttpRequestThread(getDefaultHttpClient(), paramString, paramHttpCallback).start();
  }
  
  public void put(String paramString1, HttpCallback paramHttpCallback, String paramString2)
  {
    paramString1 = new HttpPut(paramString1);
    try
    {
      StringEntity localStringEntity = new org/apache/http/entity/StringEntity;
      localStringEntity.<init>(paramString2, "UTF-8");
      StringBuilder localStringBuilder = new java.lang.StringBuilder;
      localStringBuilder.<init>(" http put body :");
      Log.d("com.resmed.refresh.net", paramString2);
      paramString1.setEntity(localStringEntity);
      setResMedHeaders(paramString1, null);
      new HttpRequestThread(getDefaultHttpClient(), paramString1, paramHttpCallback).start();
      return;
    }
    catch (UnsupportedEncodingException paramString2)
    {
      for (;;)
      {
        paramString2.printStackTrace();
      }
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
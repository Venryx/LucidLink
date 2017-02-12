package com.resmed.refresh.net.http;

import com.resmed.refresh.utils.Log;
import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

public class HttpRequestThread
  extends Thread
{
  private HttpClient client;
  private HttpCallback httpCallback;
  private HttpUriRequest httpRequest;
  
  public HttpRequestThread(HttpClient paramHttpClient, HttpUriRequest paramHttpUriRequest, HttpCallback paramHttpCallback)
  {
    this.client = paramHttpClient;
    this.httpRequest = paramHttpUriRequest;
    this.httpCallback = paramHttpCallback;
  }
  
  public void run()
  {
    super.run();
    Log.d("com.resmed.refresh.net", "http doInBackground the following request : " + this.httpRequest + " line : " + this.httpRequest.getRequestLine());
    Object localObject2 = this.httpRequest.getAllHeaders();
    int j = localObject2.length;
    for (int i = 0;; i++)
    {
      if (i >= j) {
        localObject1 = null;
      }
      try
      {
        localObject2 = this.client.execute(this.httpRequest);
        localObject1 = localObject2;
      }
      catch (ClientProtocolException localClientProtocolException)
      {
        for (;;)
        {
          localClientProtocolException.printStackTrace();
        }
      }
      catch (IOException localIOException)
      {
        for (;;)
        {
          localIOException.printStackTrace();
        }
      }
      this.httpCallback.handleResult((HttpResponse)localObject1);
      return;
      Object localObject1 = localObject2[i];
      Log.d("com.resmed.refresh.net", " header : " + ((Header)localObject1).getName() + " value : " + ((Header)localObject1).getValue());
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
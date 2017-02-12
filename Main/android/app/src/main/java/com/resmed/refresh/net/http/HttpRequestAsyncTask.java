package com.resmed.refresh.net.http;

import android.os.AsyncTask;
import com.resmed.refresh.utils.Log;
import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

public class HttpRequestAsyncTask
  extends AsyncTask<Void, Void, HttpResponse>
{
  private HttpClient client;
  private HttpCallback httpCallback;
  private HttpUriRequest httpRequest;
  
  public HttpRequestAsyncTask(HttpClient paramHttpClient, HttpUriRequest paramHttpUriRequest, HttpCallback paramHttpCallback)
  {
    this.client = paramHttpClient;
    this.httpRequest = paramHttpUriRequest;
    this.httpCallback = paramHttpCallback;
  }
  
  protected HttpResponse doInBackground(Void... paramVarArgs)
  {
    Log.d("com.resmed.refresh.net", "http doInBackground the following request : " + this.httpRequest + " line : " + this.httpRequest.getRequestLine());
    Object localObject = this.httpRequest.getAllHeaders();
    int j = localObject.length;
    for (int i = 0;; i++)
    {
      if (i >= j) {
        paramVarArgs = null;
      }
      try
      {
        localObject = this.client.execute(this.httpRequest);
        paramVarArgs = (Void[])localObject;
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
      this.httpCallback.handleResult(paramVarArgs);
      return paramVarArgs;
      paramVarArgs = localObject[i];
      Log.d("com.resmed.refresh.net", " header : " + paramVarArgs.getName() + "value : " + paramVarArgs.getValue());
    }
  }
  
  protected void onPostExecute(HttpResponse paramHttpResponse)
  {
    super.onPostExecute(paramHttpResponse);
    Log.d("com.resmed.refresh.net", "http onPostExecute ");
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
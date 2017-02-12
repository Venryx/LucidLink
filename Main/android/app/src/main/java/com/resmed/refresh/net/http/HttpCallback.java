package com.resmed.refresh.net.http;

import org.apache.http.HttpResponse;

public abstract interface HttpCallback
{
  public abstract ErrorCallback getError();
  
  public abstract Object getResult();
  
  public abstract void handleResult(HttpResponse paramHttpResponse);
  
  public abstract void onError(HttpResponse paramHttpResponse);
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.resmed.refresh.net.callbacks;

import android.content.Context;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.net.http.ErrorCallback;
import com.resmed.refresh.net.http.HttpCallback;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

public class HttpStatusCallback
  implements HttpCallback
{
  private RST_Response<Object> result;
  
  /* Error */
  private static String getStringFromInputStream(java.io.InputStream paramInputStream)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 4
    //   3: aconst_null
    //   4: astore_3
    //   5: new 23	java/lang/StringBuilder
    //   8: dup
    //   9: invokespecial 24	java/lang/StringBuilder:<init>	()V
    //   12: astore 5
    //   14: aload 4
    //   16: astore_1
    //   17: new 26	java/io/BufferedReader
    //   20: astore_2
    //   21: aload 4
    //   23: astore_1
    //   24: new 28	java/io/InputStreamReader
    //   27: astore 6
    //   29: aload 4
    //   31: astore_1
    //   32: aload 6
    //   34: aload_0
    //   35: invokespecial 31	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   38: aload 4
    //   40: astore_1
    //   41: aload_2
    //   42: aload 6
    //   44: invokespecial 34	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   47: aload_2
    //   48: invokevirtual 38	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   51: astore_0
    //   52: aload_0
    //   53: ifnonnull +17 -> 70
    //   56: aload_2
    //   57: ifnull +79 -> 136
    //   60: aload_2
    //   61: invokevirtual 41	java/io/BufferedReader:close	()V
    //   64: aload 5
    //   66: invokevirtual 44	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   69: areturn
    //   70: aload 5
    //   72: aload_0
    //   73: invokevirtual 48	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   76: pop
    //   77: goto -30 -> 47
    //   80: astore_0
    //   81: aload_2
    //   82: astore_1
    //   83: aload_0
    //   84: astore_2
    //   85: aload_1
    //   86: astore_0
    //   87: aload_0
    //   88: astore_1
    //   89: aload_2
    //   90: invokevirtual 51	java/io/IOException:printStackTrace	()V
    //   93: aload_0
    //   94: ifnull -30 -> 64
    //   97: aload_0
    //   98: invokevirtual 41	java/io/BufferedReader:close	()V
    //   101: goto -37 -> 64
    //   104: astore_0
    //   105: aload_0
    //   106: invokevirtual 51	java/io/IOException:printStackTrace	()V
    //   109: goto -45 -> 64
    //   112: astore_0
    //   113: aload_1
    //   114: ifnull +7 -> 121
    //   117: aload_1
    //   118: invokevirtual 41	java/io/BufferedReader:close	()V
    //   121: aload_0
    //   122: athrow
    //   123: astore_1
    //   124: aload_1
    //   125: invokevirtual 51	java/io/IOException:printStackTrace	()V
    //   128: goto -7 -> 121
    //   131: astore_0
    //   132: aload_0
    //   133: invokevirtual 51	java/io/IOException:printStackTrace	()V
    //   136: goto -72 -> 64
    //   139: astore_0
    //   140: aload_2
    //   141: astore_1
    //   142: goto -29 -> 113
    //   145: astore_2
    //   146: aload_3
    //   147: astore_0
    //   148: goto -61 -> 87
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	151	0	paramInputStream	java.io.InputStream
    //   16	102	1	localObject1	Object
    //   123	2	1	localIOException1	java.io.IOException
    //   141	1	1	localObject2	Object
    //   20	121	2	localObject3	Object
    //   145	1	2	localIOException2	java.io.IOException
    //   4	143	3	localObject4	Object
    //   1	38	4	localObject5	Object
    //   12	59	5	localStringBuilder	StringBuilder
    //   27	16	6	localInputStreamReader	java.io.InputStreamReader
    // Exception table:
    //   from	to	target	type
    //   47	52	80	java/io/IOException
    //   70	77	80	java/io/IOException
    //   97	101	104	java/io/IOException
    //   17	21	112	finally
    //   24	29	112	finally
    //   32	38	112	finally
    //   41	47	112	finally
    //   89	93	112	finally
    //   117	121	123	java/io/IOException
    //   60	64	131	java/io/IOException
    //   47	52	139	finally
    //   70	77	139	finally
    //   17	21	145	java/io/IOException
    //   24	29	145	java/io/IOException
    //   32	38	145	java/io/IOException
    //   41	47	145	java/io/IOException
  }
  
  public ErrorCallback getError()
  {
    return new ErrorResponse(this.result.getErrorCode(), this.result.getErrorMessage());
  }
  
  public RST_Response<Object> getResult()
  {
    return this.result;
  }
  
  public void handleResult(HttpResponse paramHttpResponse)
  {
    this.result = new RST_Response();
    if ((paramHttpResponse == null) || (200 != paramHttpResponse.getStatusLine().getStatusCode())) {
      onError(paramHttpResponse);
    }
    for (;;)
    {
      return;
      Log.d("com.resmed.refresh.net", " http result code : " + paramHttpResponse.getStatusLine().getStatusCode());
      if (200 == paramHttpResponse.getStatusLine().getStatusCode())
      {
        this.result.setStatus(true);
      }
      else
      {
        this.result.setStatus(false);
        this.result.setErrorCode(paramHttpResponse.getStatusLine().getStatusCode());
        this.result.setErrorMessage(paramHttpResponse.getStatusLine().getReasonPhrase());
      }
    }
  }
  
  public void onError(HttpResponse paramHttpResponse)
  {
    Log.d("com.resmed.refresh.net", " http onError()");
    this.result.setStatus(false);
    Context localContext = RefreshApplication.getInstance().getApplicationContext();
    if (paramHttpResponse == null) {}
    try
    {
      this.result.setErrorCode(500);
      this.result.setResponse(localContext.getString(2131165352));
      for (;;)
      {
        return;
        String str = getStringFromInputStream(paramHttpResponse.getEntity().getContent());
        StringBuilder localStringBuilder = new java.lang.StringBuilder;
        localStringBuilder.<init>(" http error CALLBACK : ");
        Log.d("com.resmed.refresh.net", str);
        this.result.setErrorCode(paramHttpResponse.getStatusLine().getStatusCode());
        this.result.setResponse(str);
      }
    }
    catch (Exception paramHttpResponse)
    {
      for (;;)
      {
        this.result.setErrorCode(500);
        this.result.setResponse(localContext.getString(2131165352));
        paramHttpResponse.printStackTrace();
      }
    }
  }
  
  public class ErrorResponse
    implements ErrorCallback
  {
    private int code;
    private String msg;
    
    public ErrorResponse(int paramInt, String paramString)
    {
      this.msg = paramString;
      this.code = paramInt;
    }
    
    public int getErrorCode()
    {
      return this.code;
    }
    
    public String getErrorMessage()
    {
      return this.msg;
    }
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\net\callbacks\HttpStatusCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
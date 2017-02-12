package com.resmed.refresh.net.http;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.resmed.refresh.utils.Log;
import java.lang.reflect.Type;

public class HttpAPIResultDeserializer<T>
  implements JsonDeserializer<T>
{
  public T deserialize(JsonElement paramJsonElement, Type paramType, JsonDeserializationContext paramJsonDeserializationContext)
    throws JsonParseException
  {
    paramJsonDeserializationContext = paramJsonElement.getAsJsonObject().get("Result");
    Log.d("com.resmed.refresh.net", " deserialize : " + paramJsonDeserializationContext.toString());
    Gson localGson = new Gson();
    if (paramJsonDeserializationContext.isJsonArray()) {}
    for (paramJsonElement = localGson.fromJson(paramJsonElement, paramType);; paramJsonElement = localGson.fromJson(paramJsonDeserializationContext, paramType)) {
      return paramJsonElement;
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
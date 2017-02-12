package com.resmed.refresh.net.http;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.resmed.refresh.utils.Log;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpAPIResultListDeserializer<T1, T2>
  implements JsonDeserializer<T1>
{
  Type classType;
  
  public HttpAPIResultListDeserializer(Type paramType)
  {
    this.classType = paramType;
  }
  
  public T1 deserialize(JsonElement paramJsonElement, Type paramType, JsonDeserializationContext paramJsonDeserializationContext)
    throws JsonParseException
  {
    paramJsonElement = paramJsonElement.getAsJsonObject().get("Result");
    Log.d("com.resmed.refresh.net", " deserialize : " + paramJsonElement.toString());
    paramJsonDeserializationContext = new Gson();
    if (paramJsonElement.isJsonArray())
    {
      paramType = (List)paramJsonDeserializationContext.fromJson(paramJsonElement, new TypeToken() {}.getType());
      paramJsonElement = new ArrayList(paramType.size());
      paramType = paramType.iterator();
      if (paramType.hasNext()) {}
    }
    for (;;)
    {
      return paramJsonElement;
      paramJsonElement.add(paramJsonDeserializationContext.fromJson(paramJsonDeserializationContext.toJson((Map)paramType.next()), this.classType));
      break;
      paramJsonElement = paramJsonDeserializationContext.fromJson(paramJsonElement, paramType);
    }
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
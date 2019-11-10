package com.naxanria.mining_resource.util.json;

import com.google.gson.JsonObject;

/*
  @author: Naxanria
*/
public interface IJsonSerializer<T>
{
  JsonObject serialize(T t);
  T deserialize(JsonObject json);
}

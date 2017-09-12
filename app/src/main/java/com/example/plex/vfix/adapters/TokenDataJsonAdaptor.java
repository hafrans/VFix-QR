package com.example.plex.vfix.adapters;

import com.example.plex.vfix.beans.TokenDataBean;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Plex on 2017/9/10.
 */

public class TokenDataJsonAdaptor implements JsonDeserializer {
    @Override
    public TokenDataBean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        TokenDataBean bean = new TokenDataBean();
        try {
            if(json == null || "".equals(json.getAsString())){
                bean.setToken("");
                //   return bean;
            }else{
                JsonObject obj = json.getAsJsonObject();
                bean.setToken(obj.get("token").getAsString());
            }
        }catch (Exception e){
            JsonObject obj = json.getAsJsonObject();
            bean.setToken(obj.get("token").getAsString());
        }

        return bean;
    }
}

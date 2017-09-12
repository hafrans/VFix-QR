package com.example.plex.vfix.beans;

import com.example.plex.vfix.adapters.TokenDataJsonAdaptor;
import com.google.gson.annotations.JsonAdapter;

/**
 * Created by Plex on 2017/9/10.
 */

@JsonAdapter(TokenDataJsonAdaptor.class)
public class TokenDataBean {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

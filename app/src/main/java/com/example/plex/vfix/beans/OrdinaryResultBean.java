package com.example.plex.vfix.beans;

import com.google.gson.annotations.JsonAdapter;

/**
 * Created by Plex on 2017/9/10.
 */

public class OrdinaryResultBean {

    private int code;
    private String msg;
    private TokenDataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public TokenDataBean getData() {
        return data;
    }

    public void setData(TokenDataBean data) {
        this.data = data;
    }

    public int getCode() {

        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

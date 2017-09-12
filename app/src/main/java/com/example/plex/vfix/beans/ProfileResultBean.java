package com.example.plex.vfix.beans;

/**
 * Created by Plex on 2017/9/11.
 */

public class ProfileResultBean {

    private int code;
    private String msg;
    private ProfileBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ProfileBean getData() {
        return data;
    }

    public void setData(ProfileBean data) {
        this.data = data;
    }
}

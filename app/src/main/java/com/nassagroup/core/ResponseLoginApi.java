package com.nassagroup.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hollyn.derisse on 29/05/2018.
 */


public class ResponseLoginApi {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("Login")
    @Expose
    private boolean login;
    @SerializedName("Info")
    @Expose
    private String info;
    @SerializedName("Comments")
    @Expose
    private String comments;
    @SerializedName("success")
    @Expose
    private boolean success;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}

package com.nassagroup.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginInassapp {
    @SerializedName("error")
    @Expose
    public boolean error;
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("error_api")
    @Expose
    public boolean errorApi;

}
package com.nassagroup.core;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Signout {

    @SerializedName("error")
    @Expose
    public boolean error;
    @SerializedName("message")
    @Expose
    public String message;

}
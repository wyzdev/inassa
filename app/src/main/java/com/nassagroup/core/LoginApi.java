package com.nassagroup.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginApi {

    @SerializedName("Response")
    @Expose
    private List<ResponseLoginApi> response = null;

    public List<ResponseLoginApi> getResponse() {
        return response;
    }

    public void setResponse(List<ResponseLoginApi> response) {
        this.response = response;
    }

}

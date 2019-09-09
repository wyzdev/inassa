package com.nassagroup.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Noel Emmanuel Roodly on 8/31/2019.
 */
public class ApiResponse {
    @SerializedName("error") @Expose private boolean error;
    @SerializedName("message") @Expose private String message;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.nassagroup.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Noel Emmanuel Roodly on 8/31/2019.
 */
public class Request {
    @SerializedName("Login") @Expose private KeyRequestUser keyRequestUser;
    public KeyRequestUser getKeyRequestUser() {
        return keyRequestUser;
    }
    public void setKeyRequestUser(KeyRequestUser keyRequestUser) {
        this.keyRequestUser = keyRequestUser;
    }
}

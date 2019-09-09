package com.nassagroup.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

/**
 * Created by Noel Emmanuel Roodly on 8/31/2019.
 */


public class SearchRequest {
    @SerializedName("first_name") @Expose private String first_name;
    @SerializedName("last_name") @Expose private String last_name;
    @SerializedName("dob") @Expose private String dob;
    @SerializedName("user_id") @Expose private Integer user_id;
    @SerializedName("username") @Expose private String username;
    @SerializedName("resquestkey") @Expose private KeyRequestUser keyRequestUser;


    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public KeyRequestUser getKeyRequestUser() {
        return keyRequestUser;
    }

    public void setKeyRequestUser(KeyRequestUser keyRequestUser) {
        this.keyRequestUser = keyRequestUser;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

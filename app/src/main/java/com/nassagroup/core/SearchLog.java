package com.nassagroup.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Noel Emmanuel Roodly on 8/31/2019.
 */
@Entity
public class SearchLog {
    @Id private long id;
    @SerializedName("first_name") @Expose private String user_firstname; //user firstname
    @SerializedName("last_name") @Expose private String user_lastname; // user last name
    @SerializedName("role") @Expose private String role; // user role
    @SerializedName("institution") @Expose private String institution; // user institution
    @SerializedName("firstname") @Expose private String client_firstname; // client firstname
    @SerializedName("lastname") @Expose private String client_lastname; // client client_lastname
    @SerializedName("dob") @Expose private String dob; // client dob
    private boolean is_sync;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getUser_firstname() {
        return user_firstname;
    }

    public void setUser_firstname(String user_firstname) {
        this.user_firstname = user_firstname;
    }

    public String getUser_lastname() {
        return user_lastname;
    }

    public void setUser_lastname(String user_lastname) {
        this.user_lastname = user_lastname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getClient_firstname() {
        return client_firstname;
    }

    public void setClient_firstname(String client_firstname) {
        this.client_firstname = client_firstname;
    }

    public String getClient_lastname() {
        return client_lastname;
    }

    public void setClient_lastname(String client_lastname) {
        this.client_lastname = client_lastname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public boolean isIs_sync() {
        return is_sync;
    }

    public void setIs_sync(boolean is_sync) {
        this.is_sync = is_sync;
    }
}


package com.nassagroup.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    public long id;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("institution")
    @Expose
    public String institution;
    @SerializedName("role")
    @Expose
    public String role;
    @SerializedName("status")
    @Expose
    public boolean status;
    @SerializedName("first_login")
    @Expose
    public boolean firstLogin;
    @SerializedName("created")
    @Expose
    public String created;
    @SerializedName("modified")
    @Expose
    public String modified;

}

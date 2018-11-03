package com.nassagroup.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendResearch {

    @SerializedName("error")
    @Expose
    private boolean error;


    @SerializedName("mail")
    @Expose
    private boolean mail;

    @SerializedName("clients")
    @Expose
    private Client client;

    @SerializedName("user")
    @Expose
    private User user;


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isMail() {
        return mail;
    }

    public void setMail(boolean mail) {
        this.mail = mail;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

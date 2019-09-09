package com.nassagroup.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchClient {

        @SerializedName("succes")
        @Expose
        private boolean succes;
        @SerializedName("clients")
        @Expose
        private List<Client> clients = null;
        @SerializedName("errorMessage")
        @Expose
        private String errorMessage;

        public boolean isSucces() {
            return succes;
        }

        public void setSucces(boolean succes) {
            this.succes = succes;
        }

        public List<Client> getClients() {
            return clients;
        }

        public void setClients(List<Client> clients) {
            this.clients = clients;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

    }
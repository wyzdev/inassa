package com.nassagroup.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.json.JSONArray;

/**
 * Created by Noel Emmanuel Roodly on 8/31/2019.
 */
public class ResponseKey {
    @SerializedName("Response") @Expose private List<KeyRequestUser> responses;

    public List<KeyRequestUser> getResponses() {
        return responses;
    }

    public void setResponses(List<KeyRequestUser> response) {
        this.responses = response;
    }
}

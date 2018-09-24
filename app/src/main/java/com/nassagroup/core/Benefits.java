package com.nassagroup.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Benefits {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("error_message")
    @Expose
    private String error_message;
    @SerializedName("risk_extensions")
    @Expose
    private List<Object> riskExtensions = null;
    @SerializedName("policy_extensions")
    @Expose
    private List<PolicyExtension> policyExtensions = null;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Object> getRiskExtensions() {
        return riskExtensions;
    }

    public void setRiskExtensions(List<Object> riskExtensions) {
        this.riskExtensions = riskExtensions;
    }

    public List<PolicyExtension> getPolicyExtensions() {
        return policyExtensions;
    }

    public void setPolicyExtensions(List<PolicyExtension> policyExtensions) {
        this.policyExtensions = policyExtensions;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}

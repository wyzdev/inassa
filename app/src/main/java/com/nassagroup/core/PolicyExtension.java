
package com.nassagroup.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PolicyExtension {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("extension")
    @Expose
    private String extension;
    @SerializedName("limit")
    @Expose
    private long limit;
    @SerializedName("name_for_display")
    @Expose
    private String nameForDisplay;
    @SerializedName("type_for_display")
    @Expose
    private String typeForDisplay;
    @SerializedName("description")
    @Expose
    private String description;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public String getNameForDisplay() {
        return nameForDisplay;
    }

    public void setNameForDisplay(String nameForDisplay) {
        this.nameForDisplay = nameForDisplay;
    }

    public String getTypeForDisplay() {
        return typeForDisplay;
    }

    public void setTypeForDisplay(String typeForDisplay) {
        this.typeForDisplay = typeForDisplay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
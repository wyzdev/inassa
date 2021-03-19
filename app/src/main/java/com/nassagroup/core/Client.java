package com.nassagroup.core;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Client {

    @SerializedName("employee_id")
    @Expose
    private long employeeId;
    @SerializedName("global_name_number")
    @Expose
    private long globalNameNumber;
    @SerializedName("first_name")
    @Expose
    private String firstname;
    @SerializedName("last_name")
    @Expose
    private String lastname;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("primary_name")
    @Expose
    private String primaryName;
    @SerializedName("primary_employee_id")
    @Expose
    private long primaryEmployeeId;
    @SerializedName("policy_number")
    @Expose
    private long policyNumber;
    @SerializedName("legacy_policy_number")
    @Expose
    private String legacyPolicyNumber;
    @SerializedName("has_hero")
    @Expose
    private boolean hasHero;
    @SerializedName("hero_tag")
    @Expose
    private String heroTag;

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getGlobalNameNumber() {
        return globalNameNumber;
    }

    public void setGlobalNameNumber(long globalNameNumber) {
        this.globalNameNumber = globalNameNumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public long getPrimaryEmployeeId() {
        return primaryEmployeeId;
    }

    public void setPrimaryEmployeeId(long primaryEmployeeId) {
        this.primaryEmployeeId = primaryEmployeeId;
    }

    public long getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(long policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getLegacyPolicyNumber() {
        return legacyPolicyNumber;
    }

    public void setLegacyPolicyNumber(String legacyPolicyNumber) {
        this.legacyPolicyNumber = legacyPolicyNumber;
    }

    public boolean isHasHero() {
        return hasHero;
    }

    public void setHasHero(boolean hasHero) {
        this.hasHero = hasHero;
    }

    public String getHeroTag() {
        return heroTag;
    }

    public void setHeroTag(String heroTag) {
        this.heroTag = heroTag;
    }

}
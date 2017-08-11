package com.inassa.inassa.tools;

/**
 * Created by hollyn.derisse on 10/08/2017.
 */

public class Client {

    String firstname;
    String lastname;
    String dob;
    String address;
    String policy_number;
    String company;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    boolean status;

    public Client(String string) {
    }

    public Client() {

    }
/*
* new Client(jsonObject.getString(FIRSTNAME)
* , jsonObject.getString(LASTNAME)
 * jsonObject.getString(DOB),
 *
                    jsonObject.getString(COMPANY),
                     jsonObject.getString(POLICY_NUMBER),
                      jsonObject.getString(GLOBAL_NAME_NUMBER),
                    jsonObject.getString(ADDRESS)));
                    */
    public Client(String firstname, String lastname, String dob, String company, String policy_number, String global_name_number,
                  String address) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.company = company;
        this.policy_number = policy_number;
        this.global_name_number = global_name_number;
        this.address = address;
    }

    public String getGlobal_name_number() {
        return global_name_number;
    }

    public void setGlobal_name_number(String global_name_number) {
        this.global_name_number = global_name_number;
    }

    String global_name_number;

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

    public String getPolicy_number() {
        return policy_number;
    }

    public void setPolicy_number(String policy_number) {
        this.policy_number = policy_number;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}

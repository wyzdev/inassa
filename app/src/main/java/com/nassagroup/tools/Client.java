package com.nassagroup.tools;

/**
 * Created by hollyn.derisse on 10/08/2017.
 */

public class Client {

    private String firstname;
    private String lastname;
    private String dob;
    private String address;
    private String policy_number;
    private String company;
    private int employee_id;
    private boolean is_dependant;
    private String primary_name;
    private int primary_employee_id;
    private String hero_tag;
    private String legacy_policy_number;

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
    public Client(String firstname, String lastname, String dob, String company,
                  String policy_number, String global_name_number,
                  String address, boolean status,
                  int employee_id,
                  boolean is_dependant,
                  String primary_name,
                  int primary_employee_id, String hero_tag, String legacy_policy_number) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.company = company;
        this.policy_number = policy_number;
        this.global_name_number = global_name_number;
        this.address = address;
        this.status = status;
        this.employee_id = employee_id;
        this.is_dependant = is_dependant;
        this.primary_employee_id = primary_employee_id;
        this.primary_name = primary_name;
        this.hero_tag = hero_tag;
        this.legacy_policy_number = legacy_policy_number;
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

    public boolean isDependant() {
        return is_dependant;
    }

    public void setDependant(boolean is_dependant) {
        this.is_dependant = is_dependant;
    }

    public String getPrimaryName() {
        return primary_name;
    }

    public void setPrimaryName(String primary_name) {
        this.primary_name = primary_name;
    }

    public int getEmployeeId() {
        return employee_id;
    }

    public void setEmployeeId(int employee_id) {
        this.employee_id = employee_id;
    }

    public int getPrimaryEmployeeId() {
        return primary_employee_id;
    }

    public void setPrimaryEmployeeId(int primary_employee_id) {
        this.primary_employee_id = primary_employee_id;
    }

    public String getHero_tag() {
        return hero_tag;
    }

    public void setHero_tag(String hero_tag) {
        this.hero_tag = hero_tag;
    }

    public String getLegacy_policy_number() {
        return legacy_policy_number;
    }

    public void setLegacy_policy_number(String legacy_policy_number) {
        this.legacy_policy_number = legacy_policy_number;
    }
}


package com.nassagroup.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Log {

    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("doctor_name")
    @Expose
    public String doctorName;
    @SerializedName("status")
    @Expose
    public boolean status;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("dob")
    @Expose
    public String dob;
    @SerializedName("institution")
    @Expose
    public String institution;
    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("employee_id")
    @Expose
    public String employeeId;
    @SerializedName("is_dependant")
    @Expose
    public boolean isDependant;
    @SerializedName("hero")
    @Expose
    public String hero;
    @SerializedName("primary_name")
    @Expose
    public String primaryName;
    @SerializedName("user_id")
    @Expose
    public long userId;
    @SerializedName("id")
    @Expose
    public int id;

}
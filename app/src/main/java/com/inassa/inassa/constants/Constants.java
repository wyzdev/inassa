package com.inassa.inassa.constants;

/**
 * Created by root on 3/15/17.
 */

public class Constants {
    public static final String SERVER = "http://192.168.0.124:8765/";
    //public static final String SERVER = "http://192.168.15.77:8765/";

    public static final String ADD_ADDRESS = SERVER + "logs/add.json";

    public static final String KEY_GLOBAL_NUMBER = "global_number";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_DATE = "date";
    public static final String KEY_STATUS = "status";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_POSTAL_ADDRESS = "postal_address";

    public static double LATITUDE = 18.537682;
    public static double LONGITUDE = -72.322805;

    public static void setLatitude(double latitude){
        LATITUDE = latitude;
    }
    public static void setLongitude(double longitude){
        LONGITUDE = longitude;
    }

}

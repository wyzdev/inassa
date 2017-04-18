package com.inassa.inassa.tools;

/**
 * Created by root on 3/15/17.
 */

public class Constants {
//    public static final String SERVER = "http://192.168.15.241:8765/";
    public static final String SERVER = "http://192.168.0.138:8765/";

    public static final String SERVER_INASSA = "http://200.113.219.221:8180/RequestQuote/RequestLogin";
    public static final String API_INASSA_AUTH ="RequestQuote/RequestLogin";

    public static final String LOGIN_ADDRESS = SERVER + "users/requestUser.json";
    public static final String ADD_ADDRESS = SERVER + "logs/add.json";
    public static final String FORGOT_PASSWORD_ADDRESS = SERVER + "users/forgotPassword.json";

    public static final String KEY_GLOBAL_NUMBER = "global_number";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_DATE = "date";
    public static final String KEY_STATUS = "status";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_POSTAL_ADDRESS = "postal_address";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    public static final String API_INASSA_AUTH_USERNAME = "jotest@test.com";
    public static final String API_INASSA_AUTH_PASSWORD = "P@$$w0rd";

    public static double LATITUDE = -1;
    public static double LONGITUDE = -1;

    public static void setLatitude(double latitude){
        LATITUDE = latitude;
    }
    public static void setLongitude(double longitude){
        LONGITUDE = longitude;
    }

}

package com.inassa.inassa.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * Created by root on 4/18/17.
 */

public class UserInfo {
/*"user": {
    "id": 1,
    "first_name": "Hollyn",
    "last_name": "DERISSE",
    "username": "hollyn_derisse",
    "institution": "INASSA",
    "email": "hollynderisse93@gmail.com",
    "role": "admin",
    "status": true,
    "first_login": false,
    "created": "2017-03-20T00:00:00+00:00",
    "modified": "2017-04-18T16:26:31+00:00"
  }*/
    private final String PREFERENCE_NAME = "session";
    private final String KEY_ID = "id";
    private final String KEY_LOGIN = "login";
    private final String KEY_FIRSTNAME = "first_name";
    private final String KEY_LASTNAME = "last_name";
    private final String KEY_USERNAME = "username";
    private final String KEY_INSTITUTION = "institution";
    private final String KEY_EMAIL = "email";
    private final String KEY_ROLE = "role";
    private final String KEY_STATUS = "status";
    private final String KEY_FIRST_LOGIN = "first_login";
    private final String KEY_CURRENT_DATE = "current_date";


    private Context context;

    public UserInfo(Context context) {
        this.context = context;
    }

    public void setUserInfo(String value) {

        JSONObject user = null;
        String firstname = "", lastname = "", username = "", email = "", institution = "", role = "";
        boolean status = false, first_login = false;
        int id = 0, current_date = 0;


        try {
            user = new JSONObject(value);
            id = user.getInt(KEY_ID);
            firstname = user.getString(KEY_FIRSTNAME);
            lastname = user.getString(KEY_LASTNAME);
            username = user.getString(KEY_USERNAME);
            email = user.getString(KEY_EMAIL);
            institution = user.getString(KEY_INSTITUTION);
            role = user.getString(KEY_ROLE);
            status = user.getBoolean(KEY_STATUS);
            first_login = user.getBoolean(KEY_FIRST_LOGIN);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_ID, id);
        editor.putString(KEY_FIRSTNAME, firstname);
        editor.putString(KEY_LASTNAME, lastname);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_INSTITUTION, institution);
        editor.putString(KEY_ROLE, role);
        editor.putBoolean(KEY_STATUS, status);
        editor.putBoolean(KEY_FIRST_LOGIN, first_login);
        editor.apply();

    }


    public void setCurrentDate(int current_date) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();

        editor.putInt(KEY_CURRENT_DATE, current_date);
        editor.apply();
    }

    public int getCurrentDate() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_CURRENT_DATE, 0);
    }

    public void setLoggedin(boolean state) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();

        editor.putBoolean(KEY_LOGIN, state);
        editor.apply();
    }

    public boolean getLoggedIn() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_LOGIN, false);
    }

    public int getUserId() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_ID, 0);
    }

    public String getUserFirstname() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_FIRSTNAME, "");
    }

    public String getUserLastname() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LASTNAME, "");
    }

    public String getUserUsername() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USERNAME, "");
    }

    public String getUserEmail() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_EMAIL, "");
    }

    public String getUserInstitution() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_INSTITUTION, "");
    }

    public String getUserRole() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_INSTITUTION, "");
    }

    public boolean getUserStatus() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_STATUS, false);
    }


    public boolean getUserFirstLogin() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_FIRST_LOGIN, false);
    }

    public void clear() {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
    }

    public void setUserId(int id) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_ID, id);
        editor.apply();
    }

    public void setUserFirstname(String firstname) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_FIRSTNAME, firstname);
        editor.apply();
    }

    public void setUserLastname(String lastname) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_LASTNAME, lastname);
        editor.apply();
    }

    public void setUserUsername(String username) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public void setUserEmail(String email) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public void setUserInstitution(String institution) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_INSTITUTION, institution);
        editor.apply();
    }

    public void setUserRole(String role) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    public void setUserStatus(boolean status) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_STATUS, status);
        editor.apply();
    }

    public void setUserFirstLogin(boolean first_login) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_FIRST_LOGIN, first_login);
        editor.apply();
    }


}

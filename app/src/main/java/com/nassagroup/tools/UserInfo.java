package com.nassagroup.tools;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that manages the user's information in the application
 */

public class UserInfo {
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

    /**
     * Method that sets the user's information
     * @param value
     */
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


    /**
     * Method that sets the current date
     * @param current_date
     */
    public void setCurrentDate(int current_date) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();

        editor.putInt(KEY_CURRENT_DATE, current_date);
        editor.apply();
    }

    /**
     * Method that gets the current date
     * @return String
     */
    public int getCurrentDate() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_CURRENT_DATE, 0);
    }

    /**
     * Method that sets the state of the login
     * @param state
     */
    public void setLoggedin(boolean state) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();

        editor.putBoolean(KEY_LOGIN, state);
        editor.apply();
    }

    /**
     * Method that gets the the state of the login
     * @return boolean
     */
    public boolean getLoggedIn() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_LOGIN, false);
    }

    /**
     * Method that gets the user's ID
     * @return int
     */
    public int getUserId() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_ID, 0);
    }

    /**
     * Method that gets the user's firstname
     * @return String
     */
    public String getUserFirstname() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_FIRSTNAME, "");
    }

    /**
     * Method that gets the user's lastname
     * @return String
     */
    public String getUserLastname() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LASTNAME, "");
    }

    /**
     * Method that gets the user's username
     * @return String
     */
    public String getUserUsername() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USERNAME, "");
    }

    /**
     * Method that gets the user's e-mail
     * @return String
     */
    public String getUserEmail() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_EMAIL, "");
    }

    /**
     * Method that gets the user's institution
     * @return String
     */
    public String getUserInstitution() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_INSTITUTION, "");
    }

    /**
     * Method that gets the user's role
     * @return String
     */
    public String getUserRole() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_INSTITUTION, "");
    }

    /**
     * Method that gets the user's status
     * @return boolean
     */
    public boolean getUserStatus() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_STATUS, false);
    }

    /**
     * Method that gets if it's the user's first login
     * @return boolean
     */
    public boolean getUserFirstLogin() {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_FIRST_LOGIN, false);
    }

    /**
     * Method that clears the shared preferences
     */
    public void clear() {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
    }

    /**
     * Method that sets the user's ID
     * @param id
     */
    public void setUserId(int id) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_ID, id);
        editor.apply();
    }

    /**
     * Method that sets the user's firstname
     * @param firstname
     */
    public void setUserFirstname(String firstname) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_FIRSTNAME, firstname);
        editor.apply();
    }

    /**
     * Method that sets the user's lastname
     * @param lastname
     */
    public void setUserLastname(String lastname) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_LASTNAME, lastname);
        editor.apply();
    }

    /**
     * Method that sets the user's username
     * @param username
     */
    public void setUserUsername(String username) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    /**
     * Method that sets the user's e-mail
     * @param email
     */
    public void setUserEmail(String email) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    /**
     * Method that sets the user's institution
     * @param institution
     */
    public void setUserInstitution(String institution) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_INSTITUTION, institution);
        editor.apply();
    }

    /**
     * Method that sets the user's role
     * @param role
     */
    public void setUserRole(String role) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    /**
     * Method that sets the user's status
     * @param status
     */
    public void setUserStatus(boolean status) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_STATUS, status);
        editor.apply();
    }

    /**
     * Method that sets if it's the user's first login
     * @param first_login
     */
    public void setUserFirstLogin(boolean first_login) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_FIRST_LOGIN, first_login);
        editor.apply();
    }


}

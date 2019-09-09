package com.nassagroup.activities;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nassagroup.APIInterfaces.RetrofitInterfaces;
import com.nassagroup.App;
import com.nassagroup.R;
import com.nassagroup.RetrofitClientInstance;
import com.nassagroup.core.CheckLogin;
import com.nassagroup.core.LoginInassapp;
import com.nassagroup.tools.Constants;
import com.nassagroup.tools.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A screen that allows the authentication via username/password
 */
public class LoginActivity extends AppCompatActivity {

    private static final int MY_SOCKET_TIMEOUT_MS = 30000;

    // References du UI.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private TextView forgot_password;
    UserInfo userInfo;
    ProgressDialog progressDialog;


    /**
     * Method tha renders the login form
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Connexion ...");
        progressDialog.setMessage("Attendez s'il vous plait");
        progressDialog.setCancelable(false);

        userInfo = new UserInfo(this);

        // Mis en place du formulaire
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.login_username);


        forgot_password = (TextView) findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();

            }
        });
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        Button signInButton = (Button) findViewById(R.id.login_sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    /**
     * Method that checks the user's inputs are correct
     */
    public void attemptLogin() {
        // Reinitialiser les Errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Enregistrer les valeurs saisis dans des String
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Verifie que le mot de passe est valide, si l'utilisateur en a saisi un
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Verifie que l'utilisateur a saisi un nom d'utilisateur.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_email));
            focusView = mUsernameView;
            cancel = true;
        }

        if (mUsernameView.getText().toString().equals("inassa") && mPasswordView.getText().toString().equals("settings")) {
            Toast.makeText(this, "Paramètres", Toast.LENGTH_SHORT).show();
            openApp("com.android.settings");
        } else if (mUsernameView.getText().toString().equals("inassa") && mPasswordView.getText().toString().equals("dialer")) {
            Toast.makeText(this, "Dialer", Toast.LENGTH_SHORT).show();
            openApp("com.fineos.calculator");
        } else if (mUsernameView.getText().toString().equals(Constants.USERNAME_TO_QUIT_APP) && mPasswordView.getText().toString().equals(Constants.PASSWORD_TO_QUIT_APP)) {
            Toast.makeText(this, "Apps", Toast.LENGTH_SHORT).show();
            // lance les apps
            startActivity(new Intent(this, AppsListActivity.class));
            // Effacer les identifiants dans les editText
            mUsernameView.setText("");
            mPasswordView.setText("");
        } else {

            if (cancel) {
                focusView.requestFocus();
            } else {
                loginUser(mUsernameView.getText().toString().trim(), mPasswordView.getText().toString()
                        .trim());

            }
        }
    }

    public void openApp(String packageName) {
        PackageManager manager = getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(packageName);
        startActivity(i);
    }

    /**
     * Method that allows the user to authenticate
     *
     * @param username
     * @param password
     */
    private void loginUser(final String username, final String password) {
        progressDialog.show();
        proceedLoginInassapp(username, password);
    }

    public void proceedLoginInassapp(final String username, final String password) {
        RetrofitInterfaces retrofitInterfaces = RetrofitClientInstance.getClientForInassapp().create(RetrofitInterfaces.class);
        Call<LoginInassapp> call = retrofitInterfaces.loginInassap(username, password, Constants.TOKEN);
        call.enqueue(new Callback<LoginInassapp>() {
            @Override
            public void onResponse(Call<LoginInassapp> call, retrofit2.Response<LoginInassapp> response) {
                progressDialog.dismiss();
                LoginInassapp loginInassapp = response.body();
                String json;
                Gson gson = new Gson();
                json = gson.toJson(loginInassapp.user);
                if (loginInassapp.error) {
                    if (loginInassapp.errorApi) {
                        dialogError();
                        return;
                    }
                    Toast.makeText(LoginActivity.this, "Nom d'utilisateur ou mot de passe incorrect.", Toast.LENGTH_SHORT).show();
                } else {
                    userInfo.setUserInfo(json);
//                    checkCanLogin();
                    progressDialog.dismiss();
                    DateFormat dateFormat = new SimpleDateFormat("dd", Locale.FRANCE);
                    userInfo.setLoggedin(true);
                    App.getInstance().requestKeyInBackground();
                    userInfo.setCurrentDate(Integer.parseInt(dateFormat.format(new Date())));
                    startActivity(new Intent(LoginActivity.this, SearchClientActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginInassapp> call, Throwable t) {
                progressDialog.dismiss();
                dialogError();
//                Toast.makeText(LoginActivity.this,
//                        "S'il vous plait, verifier votre connection internet." + call.toString(), Toast.LENGTH_LONG).show();
            }

        });
    }

    /**
     * Method that checks the user's username is valid
     *
     * @param username
     * @return boolean
     */
    private boolean isUsernameValid(String username) {
//        return username.contains("_");
        return true;
    }

    /**
     * Method that checks if the user's password is valid
     *
     * @param password
     * @return boolean
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Method that redirects the user to SEARCHCLIENT view if the user is already logged in.
     */
    @Override
    protected void onResume() {
        mUsernameView.clearFocus();
        if (userInfo.getLoggedIn()) {
            startActivity(new Intent(this, SearchClientActivity.class));
            finish();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
//        finish();
//        startActivity(new Intent(LoginActivity.this, LoginActivity.class));
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    private final List mBlockedKeys = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_VOLUME_UP));

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mBlockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public void onBackPressed() {

    }


    public void checkCanLogin() {
        progressDialog.setMessage("Patientez s'il vous plait ...");
        progressDialog.show();
        RetrofitInterfaces service = RetrofitClientInstance.getClientForInassapp().create(RetrofitInterfaces.class);
        Call<CheckLogin> call = service.canLogin(userInfo.getUserId());
        call.enqueue(new Callback<CheckLogin>() {
            @Override
            public void onResponse(Call<CheckLogin> call, retrofit2.Response<CheckLogin> response) {
                CheckLogin checkLogin = response.body();
                if (checkLogin != null) {
                    progressDialog.dismiss();
                    if (checkLogin.can) {
                        DateFormat dateFormat = new SimpleDateFormat("dd", Locale.FRANCE);
                        Date date = new Date();
                        userInfo.setLoggedin(true);
                        App.getInstance().requestKeyInBackground();
                        userInfo.setCurrentDate(Integer.parseInt(dateFormat.format(date)));
                        startActivity(new Intent(LoginActivity.this, SearchClientActivity.class));
                        finish();
                    } else {
                        dialogError();
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckLogin> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Une problème est survenu", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void dialogError() {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Erreur")
                .setMessage(this.getResources().getString(R.string.server_problem))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
//                        logout();
                        userInfo.setLoggedin(false);
                        userInfo.clear();
                        mUsernameView.setText("");
                        mPasswordView.setText("");

                    }
                })
                .show();
    }


    public void logout() {
        userInfo.setLoggedin(false);
        userInfo.clear();
        startActivity(new Intent(LoginActivity.this, LoginActivity.class));
        finish();
    }

}


package com.inassa.inassa.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inassa.inassa.R;
import com.inassa.inassa.tools.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    EditText editText_username, editText_email;
    Button button_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText_email = (EditText) findViewById(R.id.forgot_password_email);
        editText_username = (EditText) findViewById(R.id.forgot_password_username);
        
        button_send = (Button) findViewById(R.id.forgot_password_send_button);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptChangeemail();
            }
        });
    }
    
    public void attemptChangeemail(){

        // Reset errors.
        editText_username.setError(null);
        editText_email.setError(null);

        // Store values at the time of the login attempt.
        String username = editText_username.getText().toString();
        String email = editText_email.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email, if the user entered one.
        if(TextUtils.isEmpty(email)) {
            editText_email.setError(getString(R.string.error_field_required));
            focusView = editText_email;
            cancel = true;
        }else if (!TextUtils.isEmpty(email) && !isEmailValid(email)) {
            editText_email.setError(getString(R.string.error_invalid_email));
            focusView = editText_email;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            editText_username.setError(getString(R.string.error_field_required));
            focusView = editText_username;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);

            resetUser(username.trim(), email.trim());


        }
    }

    private void resetUser(final String username, final String email) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Reinitialisation du mot de passe ...");
        progressDialog.setMessage("Attendez s'il vous plait");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FORGOT_PASSWORD_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response_login", response);


                        progressDialog.dismiss();
//                        try {
//                            JSONObject jso  = new JSONObject(response);
//                            jso = new JSONObject(String.valueOf(jso.getJSONObject("mail")));

                            //if (!jso.getBoolean("error") || response.contains("false")){
                            if (response.contains("false")){

                                ///////////////////////////////////////////////////////////////////////////////////////
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgotPasswordActivity.this);

// set title
//                                alertDialogBuilder.setTitle(getString(R.string.password_reset_title));

// set dialog message
                                alertDialogBuilder
                                        .setMessage(getString(R.string.password_reset_message) + email)
                                        .setCancelable(false)
                                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                // if this button is clicked, close
                                                // current activity
                                                //MainActivity.this.finish();
                                                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                                                finish();
                                            }
                                        });
//                                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog,int id) {
//                                                // if this button is clicked, just close
//                                                // the dialog box and do nothing
//                                                dialog.cancel();
//                                            }
//                                        });

// create alert dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();

// show it
                                alertDialog.show();
                                //////////////////////////////////////////////////////////////////////////////////////

                            }
                            else{
                                Toast.makeText(ForgotPasswordActivity.this, getString(R.string.error_auth), Toast.LENGTH_SHORT).show();
                            }

//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(ForgotPasswordActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
                        Toast.makeText(ForgotPasswordActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_USERNAME, username);
                params.put(Constants.KEY_EMAIL, email);
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        finish();
    }
}

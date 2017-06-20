package com.inassa.inassa.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A class that allows the user to reset its password
 * and to send it to him via e-mail
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    // References du UI
    private static final int MY_SOCKET_TIMEOUT_MS = 30000;
    EditText editText_email;
    Button button_send;

    /**
     *  Method that sets up the form
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText_email = (EditText) findViewById(R.id.forgot_password_email);

        button_send = (Button) findViewById(R.id.forgot_password_send_button);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptChangeemail();
            }
        });
    }

    /**
     * Method that checks if the user's input
     * are correct.
     */
    public void attemptChangeemail() {

        // Reinitialiser les Errors.
        editText_email.setError(null);

        // Enregistre les valeurs saisis par l'utilisateur dans des String
        String email = editText_email.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // verifie si l'email saisis est valide
        if (TextUtils.isEmpty(email)) {
            editText_email.setError(getString(R.string.error_field_required));
            focusView = editText_email;
            cancel = true;
        } else if (!TextUtils.isEmpty(email) && !isEmailValid(email)) {
            editText_email.setError(getString(R.string.error_invalid_email));
            focusView = editText_email;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            resetUser(email.trim());


        }
    }

    /**
     * Method that allows to reset the user's password.
     * @param email
     */
    private void resetUser(final String email) {
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
                        if (response.contains("false")) {
                            // show dialog message
                            showDialogMessage(email).show();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, getString(R.string.error_auth), Toast.LENGTH_SHORT).show();
                        }
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
                params.put(Constants.KEY_EMAIL, email);
                params.put(Constants.KEY_TOKEN, Constants.TOKEN);
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

    /**
     * Method that checks if the e-mail input is correct.
     * @param email
     * @return boolean
     */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Method that allows the user to go back to the login form
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * Method that creates a message dialog to confirm that the e-mail that contents the new password
     * is sent
     * @param email
     * @return AlertDialog
     */
    private AlertDialog showDialogMessage(String email) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgotPasswordActivity.this);

        // preparer le message du dialog
        alertDialogBuilder
                .setMessage(getString(R.string.password_reset_message) +' ' + email)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Si on clique sur ce boutton, ferme
                        // l'activite actuel
                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                        finish();
                    }
                });

        // cree un alert dialog
        return alertDialogBuilder.create();
    }

    public void go_to_login(View view){
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        finish();
    }
}

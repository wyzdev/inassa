package com.inassa.inassa.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inassa.inassa.R;
import com.inassa.inassa.tools.Constants;
import com.inassa.inassa.tools.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Un ecran qui offre la fonction de changer son mot de passe
 */
public class ChangePasswordActivity extends AppCompatActivity {

    // UI references.
    EditText editText_email, editText_password1, editText_password2;
    UserInfo userInfo;

    /**
     * Methode ou se fera le set up du formulaire
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Mis en place du formulaire de changement de mot de passe.
        userInfo = new UserInfo(this);
        editText_email = (EditText) findViewById(R.id.change_password_email);
        editText_password1 = (EditText) findViewById(R.id.change_password_password1);
        editText_password2 = (EditText) findViewById(R.id.change_password_password2);

        editText_password2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attempChangePassword();
                    return true;
                }
                return false;
            }
        });

        Button mChangePasswordButton = (Button) findViewById(R.id.change_password_button);
        mChangePasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attempChangePassword();
            }
        });
    }

    /**
     * AttempChangePassword permet de changer le mot de passe de l'utilisateur.
     * Si les champs restent vide, la chaine de caractere pour le mot de passe est trop courte, etc
     *  un message d'erreur s'affichera.
     */
    public void attempChangePassword() {

        // Reinitialiser les Errors.
        editText_email.setError(null);
        editText_password1.setError(null);
        editText_password2.setError(null);

        // Enregistre les les valeurs des saisis dans un String.
        String email = editText_email.getText().toString();
        String password1 = editText_password1.getText().toString();
        String password2 = editText_password2.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Verifie que le mot de passe est valide. Si l'utilisateur en a saisi.
        if (!TextUtils.isEmpty(password1) && !isPasswordValid(password1)) {
            editText_password1.setError(getString(R.string.error_invalid_password));
            focusView = editText_password1;
            cancel = true;
        }

        // Verifie que le mot de passe est valide. Si l'utilisateur en a saisi.
        if (!TextUtils.isEmpty(password2) && !isPasswordValid(password2)) {
            editText_password2.setError(getString(R.string.error_invalid_password));
            focusView = editText_password2;
            cancel = true;
        }

        // Verifie que l'adresse e-mail est valide. Si l'utilisateur en a saisi.
        if (TextUtils.isEmpty(email)) {
            editText_email.setError(getString(R.string.error_field_required));
            focusView = editText_email;
            cancel = true;
        } else if (!isEmailValid(email)) {
            editText_email.setError(getString(R.string.error_invalid_email));
            focusView = editText_email;
            cancel = true;
        }

        if (!password1.equals(password2)){
            editText_password2.setError(getString(R.string.error_password_not_match));
            focusView = editText_password1;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }

        changePasswordUser(userInfo.getUserUsername(), email, password1);
    }

    /**
     * Une methode qui permet de verifier si un e-mail est correct.
     *
     * @param email
     * @return boolean
     */
    public boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    /**
     * Une methode qui permet de verifier si un mot de passe est fort ou pase.
     * Si le mot de passe n'est pas fort, un message d'erreur s'affichera.
     *
     * @param password
     * @return boolean
     */
    public boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Une methode qui permet de changer le mot de passe de l'utilisateur dans la base de donnees.
     * @param username
     * @param email
     * @param password
     */
    public void changePasswordUser(final String username, final String email, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Changement de mot de passe en cours  ...");
        progressDialog.setMessage("Patientez s'il vous plait");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CHANGE_PASSWORD_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response_login", response);


                        progressDialog.dismiss();
                        try {
                            JSONObject jso  = new JSONObject(response);

                            if (!jso.getBoolean("error")){
                                userInfo.setLoggedin(true);
                                userInfo.setUserInfo(String.valueOf(jso.getJSONObject("user")));
                                startActivity(new Intent(ChangePasswordActivity.this, SearchClientActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(ChangePasswordActivity.this, getString(R.string.error_auth), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(ChangePasswordActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_USERNAME, username);
                params.put(Constants.KEY_EMAIL, email);
                params.put(Constants.KEY_PASSWORD, password);
                params.put(Constants.KEY_TOKEN, Constants.TOKEN);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}


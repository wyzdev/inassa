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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Un ecran de login qui offre une authentification via nom d'utilisateur/mot de passe.
 */
public class LoginActivity extends AppCompatActivity {

    // References du UI.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private TextView forgot_password;
    UserInfo userInfo;


    /**
     * La methode qui cree le rendu de la vue du formulaire de Login
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

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

        Button signInButton = (Button) findViewById(R.id.login_sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    /**
     * Une methode qui permet de faire la verification des saisis de l'utilisateur
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
        if(TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
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

        if (cancel) {
            focusView.requestFocus();
        } else {
            loginUser( mUsernameView.getText().toString().trim(), mPasswordView.getText().toString().trim());

        }
    }


    /**
     * La methode qui permet de faire le l'authentification si l'utilisateur existe.
     * @param username
     * @param password
     */
    private void loginUser(final String username, final String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loging in ...");
        progressDialog.setMessage("Please wait");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOGIN_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response_login", response);


                        progressDialog.dismiss();
                        try {
                            JSONObject jso  = new JSONObject(response);

                            if (!jso.getBoolean("error")){
                                DateFormat dateFormat = new SimpleDateFormat("dd");
                                Date date = new Date();

                                userInfo.setLoggedin(true);
                                userInfo.setUserInfo(String.valueOf(jso.getJSONObject("user")));
                                userInfo.setCurrentDate(Integer.parseInt(dateFormat.format(date)));
                                startActivity(new Intent(LoginActivity.this, SearchClientActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(LoginActivity.this, getString(R.string.error_auth), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LoginActivity.this, "S'il vous plait, verifier votre connection internet.", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_USERNAME, username);
                params.put(Constants.KEY_PASSWORD, password);
                params.put(Constants.KEY_TOKEN, Constants.TOKEN);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    /**
     * Methode qui permet de verifier que le nom d'utilisateur saisi est valide
     * @param username
     * @return boolean
     */
    private boolean isUsernameValid(String username) {
//        return username.contains("_");
        return true;
    }

    /**
     * Methode qui permet de verifier que le mot de passe saisi est valide.
     * @param password
     * @return boolean
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Une methode qui permet de renvoyer l'utilisateur a la vue SearchClient s'il
     * s'etait deja authentifier ave succes.
     */
    @Override
    protected void onResume() {
        mUsernameView.clearFocus();
        if (userInfo.getLoggedIn()){
            startActivity(new Intent(this, SearchClientActivity.class));
            finish();
        }
        super.onResume();
    }
}


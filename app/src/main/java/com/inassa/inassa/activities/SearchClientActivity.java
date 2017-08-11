package com.inassa.inassa.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.inassa.inassa.R;
import com.inassa.inassa.tools.Constants;
import com.inassa.inassa.tools.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A screen that allows the user to search a client
 */
public class SearchClientActivity extends AppCompatActivity {

    private static final int MY_SOCKET_TIMEOUT_MS = 30000;
    UserInfo userInfo;
    int current_date;

    private Calendar calendar;
    private int year, month, day;
    private EditText editText_birthdate, editText_firstname, editText_lastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Rechercher un client");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);

        userInfo = new UserInfo(this);

        editText_birthdate = (EditText) findViewById(R.id.search_client_edittext_birthdate_client);
        editText_birthdate.setEnabled(false);

        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        current_date = Integer.parseInt(dateFormat.format(date));

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        editText_firstname = (EditText) findViewById(R.id.search_firstname);
        editText_lastname = (EditText) findViewById(R.id.search_lastname);
        Button button_search = (Button) findViewById(R.id.search_button);

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText_firstname.getText().toString().isEmpty() && !editText_lastname.getText().toString().isEmpty() && !editText_birthdate.getText().toString().isEmpty())
                    authAPI(editText_firstname.getText().toString().toUpperCase(), editText_lastname.getText().toString().toUpperCase(), editText_birthdate.getText().toString());
            }
        });
    }

    /**
     * Method that creates an option menu
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    /**
     * Method that allows the user to choose an item in the option menu
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                userInfo.setLoggedin(false);
                userInfo.clear();
                startActivity(new Intent(SearchClientActivity.this, LoginActivity.class));
                finish();
                return true;
            case R.id.change_password:
                startActivity(new Intent(SearchClientActivity.this, ChangePasswordActivity.class));
                return true;
            case R.id.user_guide:
                startActivity(new Intent(SearchClientActivity.this, UserGuide.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Method that authenticates the application to the API of INASSA
     * @param prenom
     * @param nom
     * @param dob
     */
    public void authAPI(final String prenom, final String nom, final String dob){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Authentification ...");
        progressDialog.setMessage("Patientez s'il vous plait");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SERVER_INASSA + Constants.API_INASSA_AUTH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response_login", response);
                        progressDialog.dismiss();
                        //Toast.makeText(SearchClientActivity.this, "Susccess", Toast.LENGTH_SHORT).show();

                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject obj = new JSONObject(array.get(0).toString());
                            array = new JSONArray(obj.getJSONArray("Response").toString());
                            obj = new JSONObject(array.get(0).toString());
                            String key = obj.getString("key");

                            getInfoClient(key, prenom, nom, dob);
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
                        Log.i("error_response", error.toString());
                        Toast.makeText(SearchClientActivity.this, "ERroR " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                String str = "{\"Login\":{\"username\":\"" + Constants.API_INASSA_AUTH_USERNAME + "\",\"password\":\"" + Constants.API_INASSA_AUTH_PASSWORD + "\"}}";
                return str.getBytes();
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
     * Method that retrieves the client's information
     * @param key
     * @param prenom
     * @param nom
     * @param dob
     */
    public void getInfoClient(final String key, final String prenom, final String nom, final String dob){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Recherche en cours ...");
        progressDialog.setMessage("Patientez s'il vous plait");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SERVER_INASSA + Constants.INASSA_API_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response_login", response);
                        Log.i("birthday", dob);
                        progressDialog.dismiss();

                        saveInLogs(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        error.printStackTrace();
                        Log.i("error_response", error.toString());
                        Toast.makeText(SearchClientActivity.this, "ERroR", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                String str = "{\n" +
                        "\"resquestkey\":{\n" +
                        "\"key\":\""+ key + "\"},\n" +
                        " \"first_name\":\""+ prenom +"\",\n" +
                        " \"last_name\":\""+ nom +"\",\n" +
                        " \"dob\":\""+ dob +"\"\n" +
                        "}";

                return str.getBytes();
                //return super.getBody();
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
     * Method that saves the client's information retrieved and doctor's full name
     * and institution in online database.
     * @param info_client
     */
    private void saveInLogs(final String info_client) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Enregistrement ...");
        progressDialog.setMessage("Patientez s'il vous plait");
        progressDialog.show();

        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date date = new Date();

        JSONObject obj;
        String firstname = "";
        String lastname = "";
        boolean status = false;
        String dob = "";
        DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
        try {
            obj = new JSONObject(info_client);
            if (obj.getBoolean("success") && obj.getJSONArray("clients").length() > 0){

                obj = (JSONObject) obj.getJSONArray("clients").get(0);
                firstname = editText_firstname.getText().toString();
                lastname = editText_lastname.getText().toString();
                status = obj.getBoolean("status");
                Date birthdate = originalFormat.parse(obj.getString("dob"));
                dob = targetFormat.format(birthdate);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////

        final String finalFirstname = firstname;
        final String finalLastname = lastname;
        final boolean finalStatus = status;
        final String finalDob = dob;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("response_login", response);


                        progressDialog.dismiss();
                        try {
                            JSONObject jso  = new JSONObject(response);

                            if (!jso.getBoolean("error")){
                                Log.i("info_2", info_client);
                                Intent intent = new Intent(SearchClientActivity.this, InfoClientActivity.class);
                                intent.putExtra("info_client", info_client);
                                startActivity(intent);
                                editText_birthdate.setText("");
                                editText_firstname.setText("");
                                editText_lastname.setText("");

                                editText_lastname.requestFocus();
                            }
                            else{
                                Toast.makeText(SearchClientActivity.this, "Client inconnu.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SearchClientActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_FIRSTNAME, finalFirstname.toUpperCase());
                params.put(Constants.KEY_LASTNAME, finalLastname.toUpperCase());
                params.put(Constants.KEY_DOCTOR, userInfo.getUserFirstname() + " " + userInfo.getUserLastname());
                params.put(Constants.KEY_STATUS, String.valueOf((finalStatus) ? 1 : 0));
                params.put(Constants.KEY_DATE, dateFormat.format(date));
                params.put(Constants.KEY_DOB, finalDob);
                params.put(Constants.KEY_INSTITUTION, userInfo.getUserInstitution());
                params.put(Constants.KEY_TOKEN, Constants.TOKEN);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Method that logs the user out when the day is changed
     */
    @Override
    protected void onResume() {

        if (userInfo.getUserFirstLogin()){
            startActivity(new Intent(SearchClientActivity.this, ChangePasswordActivity.class));
            finish();
        }

        if (userInfo.getCurrentDate() != current_date){
            userInfo.clear();
            userInfo.setLoggedin(false);
            startActivity(new Intent(SearchClientActivity.this, LoginActivity.class));
            finish();
        }
        super.onResume();
    }


    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        String mm = (month < 10) ? "0" + String.valueOf(month) : String.valueOf(month);
        String dd = (day < 10) ? "0" + String.valueOf(day) : String.valueOf(day);
        String yyyy = String.valueOf(year);
        String date = mm + "/" + dd + "/" + yyyy;

        editText_birthdate.setText(date);
    }
}

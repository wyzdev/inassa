package com.inassa.inassa.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.inassa.inassa.R;
import com.inassa.inassa.tools.Constants;
import com.inassa.inassa.tools.UserInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchClientActivity extends AppCompatActivity {

    UserInfo userInfo;
    int current_date;

    private DatePicker datePicker;
    private Calendar calendar;
    private EditText dateView;
    private int year, month, day;
    private LinearLayout linearLayout_datepicker;
    private EditText editText_birthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.inassa_white);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);

        userInfo = new UserInfo(this);

        editText_birthdate = (EditText) findViewById(R.id.search_client_edittext_birthdate_client);
        editText_birthdate.setEnabled(false);

        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        current_date = Integer.parseInt(dateFormat.format(date));


        dateView = (EditText) findViewById(R.id.search_client_edittext_birthdate_client);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                userInfo.setLoggedin(false);
                userInfo.clear();
                startActivity(new Intent(SearchClientActivity.this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void loginAPI(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOGIN_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("success_response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        Log.i("error_response", error.toString());

                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String str = "{\"Login\":{\"username\":\""+Constants.API_INASSA_AUTH_USERNAME+"\",\"password\":\""+Constants.API_INASSA_AUTH_PASSWORD+"\"}}";
                return str.getBytes();
            }

            public String getBodyContentType()
            {
                return "application/json; charset=utf-8";
            }

         /*   @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.KEY_USERNAME, username);
                return params;
            }*/

        };

       /* RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);*/

    }

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
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
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
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
}

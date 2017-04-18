package com.inassa.inassa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.inassa.inassa.R;
import com.inassa.inassa.tools.Constants;
import com.inassa.inassa.tools.UserInfo;

import java.util.zip.Inflater;

public class SearchClientActivity extends AppCompatActivity {

    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.inassa_white);
        setSupportActionBar(toolbar);

        userInfo = new UserInfo(this);


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
}

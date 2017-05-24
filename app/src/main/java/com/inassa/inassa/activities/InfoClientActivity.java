package com.inassa.inassa.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.inassa.inassa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class InfoClientActivity extends AppCompatActivity {

    String info_client;
    JSONObject obj;
    TextView textView_client_firstname, textView_client_lastname, textView_client_global_name_number, textView_client_status;

    /*{
         "success": true,
         "clients": [
           {
             "global_name_number": 116246,
             "first_name": "S�bastien",
             "last_name": "M�rov�-Pierre",
             "dob": "09/18/1988",
             "address": "10, Rue Ciraud\nBourdon\nPort-au-Prince\nHaiti",
             "policy_number": 2901,
             "company": "Dupuy & M�rov�-Pierre",
             "status": true
           }
         ]
       }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setLogo(R.drawable.inassa_white);
        toolbar.setTitle("Information du client");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        info_client = getIntent().getStringExtra("info_client");

        textView_client_firstname = (TextView) findViewById(R.id.info_client_firstname);
        textView_client_lastname = (TextView) findViewById(R.id.info_client_lastname);
        textView_client_global_name_number = (TextView) findViewById(R.id.info_client_global_name_number);
        textView_client_status = (TextView) findViewById(R.id.info_client_status);

        try {
            obj = new JSONObject(info_client);
            if (obj.getBoolean("success") && obj.getJSONArray("clients").length() > 0){

                obj = (JSONObject) obj.getJSONArray("clients").get(0);
                textView_client_firstname.setText(obj.getString("first_name"));
                textView_client_lastname.setText(obj.getString("last_name"));
                textView_client_global_name_number.setText(obj.getString("global_name_number"));
                if (obj.getBoolean("status")){
                    textView_client_status.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_good));
                    textView_client_status.setTextColor(getResources().getColor(R.color.green_active));
                    textView_client_status.setText(getResources().getString(R.string.active));
                }
                else{
                    textView_client_status.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_bad));
                    textView_client_status.setTextColor(getResources().getColor(R.color.red_inactive));
                    textView_client_status.setText(getResources().getString(R.string.inactive));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}

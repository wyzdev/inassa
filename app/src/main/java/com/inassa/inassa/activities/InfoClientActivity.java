package com.inassa.inassa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inassa.inassa.R;
import com.inassa.inassa.adapers.RecyclerViewAdapter;
import com.inassa.inassa.tools.Client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A screen that displays the client's information
 */
public class InfoClientActivity extends AppCompatActivity {

    String info_client;
    JSONObject obj;
    TextView textView_client_firstname, textView_client_lastname, textView_client_global_name_number, textView_client_status;

    private final String FIRSTNAME = "first_name";
    private final String LASTNAME = "last_name";
    private final String GLOBAL_NAME_NUMBER = "global_name_number";
    private final String CLIENTS = "clients";
    RelativeLayout viewClient, viewRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
            if (obj.getBoolean("success") && obj.getJSONArray(CLIENTS).length() > 0){

                if (obj.getJSONArray(CLIENTS).length() == 1){
                    printInfoClient(obj);
                }else if (obj.getJSONArray(CLIENTS).length() > 1){
                    finish();
                    Intent intent = new Intent(this, ListClient.class);
                    intent.putExtra("jsonObject", obj.toString());
                    startActivity(intent);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void printInfoClient(JSONObject obj) throws JSONException {

        obj = (JSONObject) obj.getJSONArray(CLIENTS).get(0);
        textView_client_firstname.setText(obj.getString(FIRSTNAME));
        textView_client_lastname.setText(obj.getString(LASTNAME));
        textView_client_global_name_number.setText(obj.getString(GLOBAL_NAME_NUMBER));
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

    /**
     * Method that allows the user to go to the parent activity
     * @param item
     * @return MenuItem
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
